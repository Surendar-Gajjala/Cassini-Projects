define(['app/app.modules',
        'app/shared/services/cachingService',
        'app/components/prod/materialpo/materialsPOFactory',
        'app/components/prod/supplier/suppliersFactory',
        'app/components/prod/materialpo/select/materialSelectionController'],
    function ($app) {
        $app.controller('NewMaterialPOController',
            [
                '$scope', '$rootScope', '$timeout', '$interval', '$state', '$stateParams',
                '$cookies', 'cachingService', 'materialsPOFactory', 'suppliersFactory', '$modal',

                function ($scope, $rootScope, $timeout, $interval, $state, $stateParams,
                          $cookies, cachingService, materialsPOFactory, suppliersFactory, $modal) {
                    $rootScope.iconClass = "fa flaticon-debit-card";
                    $rootScope.newPO = $stateParams.materialPoId == null ? true : false;
                    $rootScope.viewTitle = "New Material PO";
                    //$rootScope.buttonLabel = $rootScope.newPO ? "Create " : "Edit";

                    $scope.materialPO = {
                        supplier: null,
                        details: [],
                        notes: null,
                        orderTotal: 0.0
                    };

                    $scope.allSupplierrs = [];

                    $scope.poMaterials = [];
                    $scope.selectedSupplier = null;

                    $scope.nodeId = 0;

                    $scope.addToTotal = function (item) {
                        item.itemTotal = item.quantity * item.unitPrice;
                        $scope.materialPO.orderTotal = 0.0;
                        angular.forEach($scope.poMaterials, function (item) {
                            $scope.materialPO.orderTotal = angular.copy($scope.materialPO.orderTotal) + item.itemTotal;
                        });
                    };

                    $scope.$on('$viewContentLoaded', function () {
                        $rootScope.setToolbarTemplate('new-materialpo-view-tb');
                    });

                    $scope.clearPO = function () {
                        $scope.poMaterials = [];
                        $scope.materialPO.orderTotal = 0.0;
                    };

                    $scope.removeItem = function (item) {
                        var index = $scope.poMaterials.indexOf(item);
                        $scope.poMaterials.splice(index, 1);
                        $scope.materialPO.orderTotal -= item.itemTotal;
                    };

                    $scope.selectMaterials = function () {
                        var modalInstance = $modal.open({
                            animation: true,
                            templateUrl: 'app/components/prod/materialpo/select/materialSelectionDialog.jsp',
                            controller: 'MaterialSelectionController',
                            size: 'md',
                            resolve: {
                                selectType: function () {
                                    return "check";
                                },
                                poMaterials: function () {
                                    return $scope.poMaterials;
                                }
                            }
                        });

                        modalInstance.result.then(
                            function (selectedMaterials) {
                                $scope.poMaterials = [];
                                angular.forEach(selectedMaterials, function (mat) {
                                    var item = {
                                        material: mat,
                                        unitPrice: mat.unitPrice,
                                        itemTotal: mat.itemTotal,
                                        quantity: mat.quantity,
                                        supplier: mat.supplier,
                                        notes: mat.notes
                                    };
                                    $scope.poMaterials.push(item);
                                });
                            },
                            function () {

                            }
                        );
                    };

                    $rootScope.updatePO = function () {
                        UpdateInitMaterialPO();
                        materialsPOFactory.updateMaterialPO($scope.materialPO).then(
                            function (data) {
                                $scope.materialPO = data;
                                $rootScope.showSuccessMessage("material PO updated successfully");
                                $state.go('app.prod.materialspo');
                            },
                            function (error) {
                                $rootScope.showErrorMessage(error.message);
                            }
                        );
                    };

                    function UpdateInitMaterialPO() {
                        var items = [];
                        angular.forEach($scope.poMaterials, function (item) {
                            var row = {};
                            row.material = item.material;
                            row.quantity = item.quantity;
                            row.issQuantity = item.issQuantity;
                            row.unitPrice = item.unitPrice;
                            row.itemTotal = item.itemTotal;
                            row.notes = item.notes;
                            items.push(row);
                        });

                        $scope.materialPO.details = angular.copy(items);

                    }

                    $rootScope.createPO = function () {
                        initMaterialPO();
                        materialsPOFactory.createMaterialPOs($scope.materialPOs).then(
                            function (data) {
                                var materialPOs = data;
                                $rootScope.showSuccessMessage("Material Purchase order(s) created successfully");
                                $state.go('app.prod.materialspo');
                            },
                            function (error) {
                                $rootScope.showErrorMessage(error.message);
                            }
                        );
                    };

                    function initMaterialPO() {
                        $scope.materialPOs = [];

                        var itemsMap = new Map();
                        angular.forEach($scope.poMaterials, function (item) {
                            var items = itemsMap.get(item.supplier.id);
                            if (items == undefined) {
                                items = [];
                            }
                            var row = {};
                            row.material = item.material;
                            row.quantity = item.quantity;
                            row.unitPrice = item.unitPrice;
                            row.itemTotal = item.itemTotal;
                            row.supplier = item.supplier;
                            row.notes = item.notes;
                            items.push(row);
                            itemsMap.set(item.supplier.id, items);
                        });

                        itemsMap.forEach(function (value, key) {
                            var tot = 0;
                            angular.forEach(value, function (total) {
                                tot += total.itemTotal;
                            });
                            $scope.materialPO = {
                                supplier: value[0].supplier,
                                details: angular.copy(value),
                                orderTotal: tot,
                                notes: value[0].notes
                            };
                            $scope.materialPOs.push($scope.materialPO)

                        });

                    }

                    $rootScope.cancelPo = function () {
                        $state.go('app.prod.materialspo');
                    };

                    function loadSuppliers() {
                        suppliersFactory.getAllSuppliers().then(
                            function (data) {
                                $scope.allSupplierrs = data;
                            }
                        )
                    }

                    function loadMOP(materialPoId) {
                        materialsPOFactory.getMaterialPurchaseOrder(materialPoId).then(
                            function (data) {
                                $scope.materialPO = data;
                                angular.forEach(data.details, function (mat) {
                                    var item = {
                                        material: mat.material,
                                        itemTotal: mat.itemTotal,
                                        quantity: mat.quantity,
                                        issQuantity: mat.issQuantity,
                                        notes: mat.notes,
                                        supplier: $scope.materialPO.supplier,
                                        unitPrice: mat.unitPrice
                                    };
                                    $scope.poMaterials.push(item);
                                });
                                $rootScope.viewTitle = $scope.materialPO.orderNumber + " Material PO Details";
                            })
                    }

                    (function () {
                        loadSuppliers();
                        if (!$rootScope.newPO) {
                            loadMOP($stateParams.materialPoId);
                        }
                    })();
                }
            ]
        );
    }
);