define(['app/app.modules',
        'app/components/prod/materialpo/materialsPOFactory',
        'app/components/prod/material/materialFactory',
        'app/shared/directives/tableDirectives',
        'app/components/store/inventory/material/materialInventoryHistoryController'
    ],
    function ($app) {
        $app.controller('MaterialInventoryController',
            [
                '$scope', '$rootScope', '$timeout', '$interval', '$state', '$cookies',
                'materialFactory', 'materialsPOFactory',

                function ($scope, $rootScope, $timeout, $interval, $state, $cookies,
                          materialFactory, materialsPOFactory) {

                    $scope.setActiveFlag(0);
                    $scope.addInvMode = false;
                    $scope.issueInvMode = false;

                    $scope.selectedMaterial = null;

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

                    $scope.materials = $scope.emptyPagedResults;

                    $scope.emptyFilters = {
                        sku: null,
                        name: null,
                        category: null
                    };

                    $scope.filters = angular.copy($scope.emptyFilters);

                    $scope.showMaterialInventoryHistory = function (material) {
                        $scope.selectedMaterial = material;

                        var index = $scope.materials.content.indexOf($scope.historyRow);
                        if (index != -1) {
                            $scope.historyRow.showHistory = false;
                            $scope.materials.content.splice(index, 1);
                            $scope.$apply();
                        }

                        $scope.historyRow.showHistory = true;
                        index = $scope.materials.content.indexOf(material);
                        $scope.materials.content.splice(index + 1, 0, $scope.historyRow);
                    };

                    $scope.closeMaterialInventoryHistory = function () {
                        $scope.historyRow.showHistory = false;
                        $scope.selectedMaterial = null;
                        var index = $scope.materials.content.indexOf($scope.historyRow);
                        $scope.materials.content.splice(index, 1);
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
                        $scope.loadMaterials();
                    };

                    $scope.pageChanged = function () {
                        $scope.loading = true;
                        $scope.materials.content = [];
                        $scope.loadMaterials();
                    };

                    $scope.resetFilters = function () {
                        $scope.filters = angular.copy($scope.emptyFilters);
                        $scope.materials.content = [];
                        $scope.pageable.page = 1;
                        $scope.loadMaterials();
                        $scope.addInvMode = false;
                    };

                    $scope.applyFilters = function () {
                        $scope.materials.content = [];
                        $scope.pageable.page = 1;
                        $scope.loadMaterials();
                    };

                    $scope.acceptChanges = function (material) {
                        material.editMode = false;

                        $timeout(function () {
                            material.showValues = true;
                        }, 500);

                        $scope.saveInventory(material);
                    };

                    $scope.acceptAddInvChanges = function (material) {
                        if ($scope.saveAddInventory(material)) {
                            material.addMode = false;
                            $scope.addInvMode = false;

                            $timeout(function () {
                                material.showValues = true;
                            }, 500);
                        }
                    };

                    $scope.acceptIssueInvChanges = function (material) {
                        if ($scope.saveIssueInventory(material)) {
                            material.issueMode = false;
                            $scope.issueInvMode = false;

                            $timeout(function () {
                                material.showValues = true;
                            }, 500);
                        }
                    };


                    $scope.showEditMode = function (material) {
                        material.editMode = true;
                        material.showValues = false;
                    };

                    $scope.showAddMode = function (material) {
                        materialsPOFactory.getMaterialPurchaseOrderDetailsByIssued(material.id, false).then(
                            function (data) {
                                $scope.materialPOs = data;
                            }
                        );
                        material.editMode = false;
                        material.showValues = true;
                        material.addMode = true;
                        $scope.addInvMode = true;
                        material.inventory.newInventory = '';
                        material.materialPO = '';
                    };

                    $scope.showIssueMode = function (material) {
                        material.editMode = false;
                        material.showValues = true;
                        material.issueMode = true;
                        $scope.issueInvMode = true;
                        material.inventory.issueInv = '';
                    };

                    $scope.hideAddMode = function (material) {
                        material.editMode = false;
                        material.addMode = false;
                        $scope.addInvMode = false;
                        material.inventory.newInventory = '';
                        material.materialPO = '';
                        $timeout(function () {
                            material.showValues = true;
                        }, 500);
                    };

                    $scope.hideIssueMode = function (material) {
                        material.editMode = false;
                        material.issueMode = false;
                        $scope.issueInvMode = false;
                        material.inventory.issueInv = '';
                    };


                    $scope.hideEditMode = function (material) {
                        material.editMode = false;

                        $timeout(function () {
                            material.showValues = true;
                        }, 500);
                    };

                    $scope.saveInventory = function (material) {
                        materialFactory.saveMaterialInventory(material.inventory).then(
                            function (data) {
                                material.inventory = data;
                            }
                        );
                    };

                    $scope.getMaterialPODetails = function (material) {
                        materialsPOFactory.getMaterialPODetailsByMaterialAndMaterialPurchaseOrder(material.id, material.materialPO.id).then(
                            function (data) {
                                material.materialPODetails = data;
                            }
                        )
                    };

                    $scope.saveAddInventory = function (material) {
                        if (material.inventory.newInventory == undefined ||
                            isNaN(material.inventory.newInventory) ||
                            material.inventory.newInventory < 0) {
                            $rootScope.showErrorMessage("Quantity has to be positive whole number");

                            return false;
                        }
                        else {
                            materialFactory.addInvInventory(material).then(
                                function (data) {
                                    material.inventory.inventory = /*parseInt(material.inventory.inventory) + */parseInt(data.quantity);
                                    material.inventory.newInventory = '';
                                    material.materialPO = '';
                                    $rootScope.showSuccessMessage("Inventory updated successfully!");
                                }
                            );
                            return true;
                        }
                    };

                    $scope.saveIssueInventory = function (material) {
                        if (material.inventory.issueInv == undefined ||
                            isNaN(material.inventory.issueInv) ||
                            material.inventory.issueInv < 0) {
                            $rootScope.showErrorMessage("Quantity has to be positive whole number");

                            return false;
                        }
                        else {
                            materialFactory.issueInvInventory(material).then(
                                function (data) {
                                    material.inventory.inventory = parseInt(material.inventory.inventory) - parseInt(material.inventory.issueInv);
                                    material.inventory.issueInv = '';
                                    $rootScope.showSuccessMessage("Inventory issued successfully!");
                                }
                            );
                            return true;
                        }
                    };

                    $scope.validateQuantity = function (material) {
                        $scope.okButton = true;
                        if ((material.materialPODetails.quantity - material.materialPODetails.issQuantity) < material.inventory.newInventory) {
                            $rootScope.showErrorMessage("Enter quantity is more than the purchase order quantity");
                            $scope.okButton = false;
                        } else {
                            $rootScope.closeNotification();
                            $scope.okButton = true;
                        }
                    };

                    $scope.validateIssueQuantity = function (material) {
                        $scope.okButton = true;
                        if (material.inventory.inventory < material.inventory.issueInv) {
                            $rootScope.showErrorMessage("Enter quantity is more than the inventory quantity");
                            $scope.okButton = false;
                        } else {
                            $rootScope.closeNotification();
                            $scope.okButton = true;
                        }
                    };

                    $scope.loadMaterials = function () {
                        $scope.loading = true;

                        materialFactory.getMaterialss($scope.filters, $scope.pageable).then(
                            function (data) {
                                $scope.materials = data;
                                $scope.loading = false;

                                angular.forEach($scope.materials.content, function (item) {
                                    var p = angular.copy(item);
                                    item.inventory = {
                                        rowId: null,
                                        material: p,
                                        inventory: 0,
                                        threshold: 0,
                                        newInventory: 0
                                    };
                                    item.showHistory = false;
                                    item.editMode = false;
                                    item.addMode = false;
                                    item.issueMode = false;
                                    item.showValues = true;
                                    $scope.addInvMode = false;
                                    $scope.issueInvMode = false;
                                });

                                return materialFactory.getMaterialsInventory(getMaterialIds());
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
                            hashtable.put(item.material.id, item);
                        });

                        angular.forEach($scope.materials.content, function (material) {
                            var i = hashtable.get(material.id);
                            if (i != null) {
                                material.inventory = i;
                            }
                        });
                    }

                    function getMaterialIds() {
                        var s = "";
                        for (var i = 0; i < $scope.materials.content.length; i++) {
                            s += "" + $scope.materials.content[i].id;

                            if (i != $scope.materials.content.length - 1) {
                                s += ",";
                            }
                        }
                        return s;
                    }


                    (function () {
                        $scope.loadMaterials();
                    })();
                }
            ]
        );
    }
);