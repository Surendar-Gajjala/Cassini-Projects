/**
 * Created by Nageshreddy on 08-08-2018.
 */
define(['app/app.modules',
        'app/components/prod/material/materialFactory'
    ],
    function ($app) {
        $app.controller('SelectMaterialController',
            [
                '$scope', '$rootScope', '$timeout', '$interval', '$state', '$cookies',
                '$modalInstance', 'materialFactory', 'supplierId',
                function ($scope, $rootScope, $timeout, $interval, $state, $cookies,
                          $modalInstance, materialFactory, supplierId) {

                    $scope.loading = true;
                    $scope.materials = null;
                    $scope.materialMaterials = null;
                    $scope.selectedMaterials = [];
                    $scope.loadMaterials = loadMaterials;
                    $scope.supplierId = supplierId;

                    $scope.selectMaterial = function (mat) {
                        if (mat.checked == false) {
                            var index = $scope.selectedMaterials.indexOf(mat);
                            if (index != -1) {
                                $scope.selectedMaterials.splice(index, 1);
                            }
                        }
                        else {
                            $scope.selectedMaterials.push(mat);
                        }
                    };

                    $scope.selectAll = function () {
                        $scope.selectedMaterials = [];
                        angular.forEach($scope.materials, function (material) {
                            material.checked = true;
                            $scope.selectedMaterials.push(material);
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

                    function loadMaterials() {
                        materialFactory.getMaterials($scope.pageable).then(
                            function (data) {
                                $scope.materials = data;
                                angular.forEach($scope.materials.content, function (item) {
                                    item.checked = false;
                                });
                                $scope.loading = false;
                                loadMaterialMaterials();
                            }
                        )
                    };

                    function loadMaterialMaterials() {
                        materialFactory.getMaterialSuppliersBySupplier($scope.supplierId).then(
                            function (data) {
                                $scope.materialMaterials = data;
                                angular.forEach($scope.materialMaterials, function (ms) {
                                    angular.forEach($scope.materials.content, function (mat) {
                                        if (ms.materialId == mat.id) {
                                            mat.cost = ms.cost;
                                            mat.checked = true;
                                            $scope.selectedMaterials.push(mat);
                                        }
                                    });
                                });
                                $scope.loading = false;
                            }
                        )
                    }

                    $scope.ok = function () {
                        /*var valid = true;
                        angular.forEach($scope.selectedMaterials, function (material) {
                            if (material.cost == null || material.cost == undefined || material.cost == "") {
                                valid = false;
                            }
                        });
                        if (valid) {
                            $modalInstance.close($scope.selectedMaterials);
                        } else {
                            $rootScope.showSuccessMessage("Please enter cost to selected materials");
                        }*/

                        $modalInstance.close($scope.selectedMaterials);

                    };

                    $scope.cancel = function () {
                        $modalInstance.dismiss('cancel');
                    };


                    (function () {
                        loadMaterials();
                    })();

                }
            ]
        );
    }
);
