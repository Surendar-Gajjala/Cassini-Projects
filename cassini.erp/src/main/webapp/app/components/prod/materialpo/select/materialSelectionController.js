define(['app/app.modules',
        'app/components/prod/material/materialFactory',
        'app/components/prod/supplier/suppliersFactory',
    ],
    function ($app) {
        $app.controller('MaterialSelectionController',
            [
                '$scope', '$localStorage', '$modalInstance', 'selectType', 'poMaterials', 'materialFactory', 'suppliersFactory',

                function ($scope, $localStorage, $modalInstance, selectType, poMaterials, materialFactory, suppliersFactory) {
                    $scope.$storage = $localStorage;

                    $scope.selectedType = selectType;
                    $scope.poMaterials = poMaterials;
                    $scope.loading = true;
                    $scope.emptyResults = {
                        content: [],
                        last: false,
                        totalPages: 0,
                        totalElements: 0,
                        size: 10,
                        number: 0,
                        sort: null,
                        first: false,
                        numberOfElements: 0
                    };
                    $scope.selectedProducts = [];

                    $scope.materials = $scope.emptyResults;

                    $scope.pageable = {
                        page: 1,
                        size: 20,
                        sort: {
                            label: "name",
                            field: "name",
                            order: "asc"
                        }
                    };

                    $scope.filters = {
                        name: ""
                    };

                    $scope.applyCriteria = function () {
                        $scope.pageable.page = 1;
                        $scope.loadMaterials();
                    };

                    $scope.selectProduct = function (product) {

                    };

                    $scope.resetCriteria = function () {
                        $scope.filters = {
                            name: ""
                        };
                        $scope.pageable.page = 1;
                        $scope.loadMaterials();
                    };

                    $scope.editCost = editCost;
                    $scope.edit = false;
                    function editCost() {
                        $scope.edit = true;
                    }

                    $scope.saveCost = saveCost;

                    function saveCost() {
                        $scope.edit = false;
                    }

                    $scope.ok = function () {
                        var valid = true;
                        var selectedProducts = $scope.getSelectedProducts();
                        angular.forEach(selectedProducts, function (material) {
                            if (material.unitPrice == 0) {
                                valid = false;
                                $scope.errorMessage = "Please select supplier for all selected materials";
                            }
                        });
                        if (valid) {
                            $scope.errorMessage = null;
                            $modalInstance.close(selectedProducts);
                        }
                    };

                    $scope.cancel = function () {
                        $modalInstance.dismiss('cancel');
                    };

                    $scope.toggleSelection = function (prod) {
                        if (prod.selected) {
                            $scope.selectedProducts.push(prod);
                        } else {
                            var index = $scope.selectedProducts.indexOf(prod);
                            $scope.selectedProducts.splice(index, 1);
                            prod.unitPrice = 0;
                        }
                        /*if (prod.selected == true && $scope.isProductSelected(prod) == false) {
                         $scope.selectedProducts.push(prod)
                         }
                         else if ($scope.isProductSelected(prod) == true) {
                         $scope.selectedProducts.splice(prod, 1);
                         prod.unitPrice = 0;
                         angular.forEach(prod.materialSuppliers, function (supplier) {
                         supplier.selected = false;
                         });
                         }*/
                    };

                    $scope.isProductSelected = function (prod) {
                        var selected = false;
                        angular.forEach($scope.selectedProducts, function (product) {
                            if (prod.id == product.id && product.selected == true) {
                                selected = true;
                            }
                        });

                        return selected;
                    };

                    $scope.errorMessage = null;
                    $scope.getSelectedProducts = function () {
                        var selected = [];
                        angular.forEach($scope.materials.content, function (material) {
                            if (material.selected == true) {
                                selected.push(material);
                            }
                        });

                        return selected;
                    };

                    $scope.loadMaterials = function () {
                        materialFactory.getMaterials($scope.pageable).then(
                            function (data) {
                                $scope.loading = false;
                                $scope.materials = data;
                                angular.forEach($scope.materials.content, function (material) {
                                    material.unitPrice = 0;
                                    material.selected = false;
                                    material.supplier = null;
                                    material.quantity = 0;
                                    material.itemTotal = 0.0;

                                    materialFactory.getMaterialSuppliersByMaterial(material.id).then(
                                        function (data) {
                                            if ($scope.poMaterials.length == 0) {
                                                material.materialSuppliers = data;
                                            } else {
                                                material.materialSuppliers = [];
                                                angular.forEach(data, function (matsup) {
                                                    if (matsup.supplierId == $scope.poMaterials[0].supplier.id) {
                                                        material.materialSuppliers.push(matsup);
                                                    }
                                                })
                                            }
                                            suppliersFactory.getSupplierNameReferences(material.materialSuppliers, 'supplierId');
                                        }
                                    )
                                    angular.forEach($scope.poMaterials, function (pMaterial) {
                                        if (pMaterial.material.id == material.id) {
                                            material.unitPrice = pMaterial.unitPrice;
                                            material.supplier = pMaterial.supplier;
                                            material.selected = true;
                                            material.quantity = pMaterial.quantity;
                                            material.itemTotal = pMaterial.itemTotal;
                                        }
                                    })
                                })
                            }
                        );
                    };

                    (function () {
                        $scope.loadMaterials();
                    })();

                }
            ]
        )
        ;
    }
)
;