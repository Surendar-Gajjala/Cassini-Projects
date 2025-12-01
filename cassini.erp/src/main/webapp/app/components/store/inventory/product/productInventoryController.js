define(['app/app.modules',
        'app/components/prod/product/productFactory',
        'app/shared/directives/tableDirectives',
        'app/components/store/inventory/product/productInventoryHistoryController'
    ],
    function ($app) {
        $app.controller('ProductInventoryController',
            [
                '$scope', '$rootScope', '$timeout', '$interval', '$state', '$cookies',
                'productFactory',

                function ($scope, $rootScope, $timeout, $interval, $state, $cookies,
                          productFactory) {

                    //$scope.setActiveFlag(1);
                    $scope.addInvMode = false;

                    $scope.selectedProduct = null;

                    $scope.emptyPagedResults = {
                        content: [],
                        last: false,
                        totalPages: 0,
                        totalElements: 0,
                        size: 0,
                        number: 0,
                        sort: null,
                        first: false,
                        numberOfElements: 0
                    };
                    $scope.pageable = {
                        page: 1,
                        size: 10,
                        sort: {
                            label: "name",
                            field: "name",
                            order: "asc"
                        }
                    };

                    $scope.historyRow = {
                        showHistory: false
                    };

                    $scope.products = $scope.emptyPagedResults;

                    $scope.emptyFilters = {
                        sku: null,
                        name: null,
                        category: null
                    };

                    $scope.filters = angular.copy($scope.emptyFilters);

                    $scope.showProductInventoryHistory = function (product) {
                        $scope.selectedProduct = product;

                        var index = $scope.products.content.indexOf($scope.historyRow);
                        if (index != -1) {
                            $scope.historyRow.showHistory = false;
                            $scope.products.content.splice(index, 1);
                            $scope.$apply();
                        }

                        $scope.historyRow.showHistory = true;
                        index = $scope.products.content.indexOf(product);
                        $scope.products.content.splice(index + 1, 0, $scope.historyRow);
                    };


                    $scope.closeProductInventoryHistory = function () {
                        $scope.historyRow.showHistory = false;
                        $scope.selectedProduct = null;
                        var index = $scope.products.content.indexOf($scope.historyRow);
                        $scope.products.content.splice(index, 1);
                    };

                    $scope.sortColumn = function (col) {
                        if ($scope.pageable.sort.label == col) {
                            if ($scope.pageable.sort.order == 'asc') {
                                $scope.pageable.sort.order = 'desc';
                            }
                            else {
                                $scope.pageable.sort.order = 'asc';
                            }
                        }
                        else {
                            $scope.pageable.sort.label = col
                            $scope.pageable.sort.order = 'asc';
                        }

                        if (col == "name") {
                            $scope.pageable.sort.field = "name";
                        }
                        else if (col == "inventory") {
                            $scope.pageable.sort.field = "inventory";
                        }
                        else if (col == "threshold") {
                            $scope.pageable.sort.field = "threshold";
                        }

                        //$scope.pageable.page = 1;
                        $scope.loadProducts();
                    };

                    $scope.pageChanged = function () {
                        $scope.loading = true;
                        $scope.products.content = [];
                        $scope.loadProducts();
                    };

                    $scope.resetFilters = function () {
                        $scope.filters = angular.copy($scope.emptyFilters);
                        $scope.products.content = [];
                        $scope.pageable.page = 1;
                        $scope.loadProducts();
                        $scope.addInvMode = false;
                    };

                    $scope.applyFilters = function () {
                        $scope.products.content = [];
                        $scope.pageable.page = 1;
                        $scope.loadProducts();
                    };

                    $scope.acceptChanges = function (product) {
                        product.editMode = false;

                        $timeout(function () {
                            product.showValues = true;
                        }, 500);

                        $scope.saveInventory(product);
                    };

                    $scope.acceptAddInvChanges = function (product) {
                        if ($scope.saveAddInventory(product)) {
                            product.addMode = false;
                            $scope.addInvMode = false;

                            $timeout(function () {
                                product.showValues = true;
                            }, 500);
                        }
                    };

                    $scope.showIssueMode = function (product) {
                        product.editMode = false;
                        product.showValues = true;
                        product.issueMode = true;
                        $scope.issueInvMode = true;
                        product.inventory.issueInv = '';
                    };

                    $scope.hideIssueMode = function (product) {
                        product.editMode = false;
                        product.issueMode = false;
                        $scope.issueInvMode = false;
                        product.inventory.issueInv = '';
                    };

                    $scope.acceptIssueInvChanges = function (product) {
                        if ($scope.saveIssueInventory(product)) {
                            product.issueMode = false;
                            $scope.issueInvMode = false;

                            $timeout(function () {
                                product.showValues = true;
                            }, 500);
                        }
                    };

                    $scope.saveIssueInventory = function (product) {
                        if (product.inventory.issueInv == undefined ||
                            isNaN(product.inventory.issueInv) ||
                            product.inventory.issueInv < 0) {
                            $rootScope.showErrorMessage("Quantity has to be positive whole number");

                            return false;
                        }
                        else {
                            productFactory.issueInvInventory(product).then(
                                function (data) {
                                    product.inventory.inventory = parseInt(product.inventory.inventory) - parseInt(product.inventory.issueInv);
                                    product.inventory.issueInv = '';
                                    $rootScope.showSuccessMessage("Inventory issued successfully!");
                                }
                            );
                            return true;
                        }
                    };

                    $scope.validateIssueQuantity = function (product) {
                        $scope.okButton = true;
                        if (product.inventory.inventory < product.inventory.issueInv) {
                            $rootScope.showErrorMessage("Enter quantity is more than the inventory quantity");
                            $scope.okButton = false;
                        } else {
                            $rootScope.closeNotification();
                            $scope.okButton = true;
                        }
                    };

                    $scope.showEditMode = function (product) {
                        product.editMode = true;
                        product.showValues = false;
                    };

                    $scope.showAddMode = function (product) {
                        product.editMode = false;
                        product.showValues = true;
                        product.addMode = true;
                        $scope.addInvMode = true;
                        product.inventory.newInventory = '';
                    };
                    $scope.hideAddMode = function (product) {
                        product.editMode = false;
                        product.addMode = false;
                        $scope.addInvMode = false;
                        product.inventory.newInventory = '';
                        $timeout(function () {
                            product.showValues = true;
                        }, 500);
                    };


                    $scope.hideEditMode = function (product) {
                        product.editMode = false;

                        $timeout(function () {
                            product.showValues = true;
                        }, 500);
                    };

                    $scope.saveInventory = function (product) {
                        productFactory.saveProductInventory(product.inventory).then(
                            function (data) {
                                product.inventory = data;
                            }
                        );
                    };

                    $scope.saveRestockLevel = function (product) {
                        productFactory.updateRestockLevel(product.id, product.inventory).then(
                            function (data) {
                                $rootScope.showSuccessMessage("RestockLevel updated")
                            }
                        )
                    };

                    $scope.saveAddInventory = function (product) {
                        if (product.inventory.newInventory == undefined ||
                            isNaN(product.inventory.newInventory) ||
                            product.inventory.newInventory < 0) {
                            $rootScope.showErrorMessage("Quantity has to be a positive whole number");

                            return false;
                        }
                        else {
                            productFactory.addInvInventory(product).then(
                                function (data) {
                                    product.inventory.inventory = parseInt(product.inventory.inventory) + parseInt(data.quantity);
                                    product.inventory.newInventory = '';
                                    $rootScope.showSuccessMessage("Inventory updated successfully!");
                                }
                            );
                            return true;
                        }
                    };


                    $scope.loadProducts = function () {
                        $scope.loading = true;
                        productFactory.getProducts($scope.filters, $scope.pageable).then(
                            function (data) {
                                $scope.products = data;
                                $scope.loading = false;

                                angular.forEach($scope.products.content, function (item) {
                                    var p = angular.copy(item);
                                    item.inventory = {
                                        rowId: null,
                                        product: p,
                                        inventory: 0,
                                        threshold: 0,
                                        newInventory: 0
                                    };
                                    item.showHistory = false;
                                    item.editMode = false;
                                    item.addMode = false;
                                    item.showValues = true;
                                    $scope.addInvMode = false;
                                });

                                return productFactory.getProductsInventory(getProductIds());
                            }
                        ).then(
                            function (data) {
                                updateInventoryInfo(data);
                            }
                        );
                    };

                    function updateInventoryInfo(inventoryList) {
                        var hashtable = new Hashtable();
                        angular.forEach(inventoryList, function (item) {
                            hashtable.put(item.product.id, item);
                        });

                        angular.forEach($scope.products.content, function (product) {
                            var i = hashtable.get(product.id);
                            if (i != null) {
                                product.inventory = i;
                            }
                        });
                    }

                    function getProductIds() {
                        var s = "";
                        for (var i = 0; i < $scope.products.content.length; i++) {
                            s += "" + $scope.products.content[i].id;

                            if (i != $scope.products.content.length - 1) {
                                s += ",";
                            }
                        }
                        return s;
                    }

                    (function () {
                        $scope.loadProducts();
                    })();
                }
            ]
        );
    }
);
