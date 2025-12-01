/**
 * Created by Nageshreddy on 07-08-2018.
 */
define(['app/app.modules',
        'app/components/prod/supplier/suppliersFactory',
        'app/components/prod/material/materialFactory'
    ],
    function ($app) {
        $app.controller('SelectSupplierController',
            [
                '$scope', '$rootScope', '$timeout', '$interval', '$state', '$cookies',
                '$modalInstance', 'suppliersFactory', 'materialFactory', 'materialId',
                function ($scope, $rootScope, $timeout, $interval, $state, $cookies,
                          $modalInstance, suppliersFactory, materialFactory, materialId) {

                    $scope.loading = true;
                    $scope.suppliers = null;
                    $scope.materialSuppliers = null;
                    $scope.selectedSupp = [];
                    $scope.loadSuppliers = loadSuppliers;
                    $scope.materialId = materialId;

                    $scope.selectSupplier = function (supp) {
                        if (supp.checked == false) {
                            var index = $scope.selectedSupp.indexOf(supp);
                            if (index != -1) {
                                $scope.selectedSupp.splice(index, 1);
                            }
                        }
                        else {
                            $scope.selectedSupp.push(supp);
                        }
                    };

                    $scope.selectAll = function () {
                        $scope.selectedSupp = [];
                        angular.forEach($scope.suppliers, function (supplier) {
                            supplier.checked = true;
                            $scope.selectedSupp.push(supplier);
                        });
                    };

                    $scope.pageable = {
                        page: 1,
                        size: 20,
                        sort: {
                            label: "name",
                            field: "name",
                            order: "asc"
                        }
                    };

                    function loadSuppliers() {
                        suppliersFactory.getSuppliers($scope.pageable).then(
                            function (data) {
                                $scope.suppliers = data;
                                angular.forEach($scope.suppliers.content, function (sup) {
                                    sup.checked = false;
                                });
                                $scope.loading = false;
                                loadMaterialSuppliers();
                            }
                        )
                    }

                    function loadMaterialSuppliers() {
                        materialFactory.getMaterialSuppliersByMaterial($scope.materialId).then(
                            function (data) {
                                $scope.materialSuppliers = data;
                                angular.forEach($scope.materialSuppliers, function (ms) {
                                    angular.forEach($scope.suppliers.content, function (sup) {
                                        if (ms.supplierId == sup.id) {
                                            sup.cost = ms.cost;
                                            sup.checked = true;
                                            $scope.selectedSupp.push(sup);
                                        }
                                    });
                                });
                                $scope.loading = false;
                            }
                        )
                    }

                    $scope.ok = function () {
                        /*var valid = true;
                        angular.forEach($scope.selectedSupp, function (supplier) {
                            if (supplier.cost == null || supplier.cost == undefined || supplier.cost == "") {
                                valid = false;
                            }
                        });
                        if (valid) {
                            $modalInstance.close($scope.selectedSupp);
                        } else {
                            $rootScope.showSuccessMessage("Please enter cost to selected suppliers");
                        }*/
                        $modalInstance.close($scope.selectedSupp);
                    };

                    $scope.cancel = function () {
                        $modalInstance.dismiss('cancel');
                    };

                    (function () {
                        loadSuppliers();
                    })();

                }
            ]
        );
    }
);
