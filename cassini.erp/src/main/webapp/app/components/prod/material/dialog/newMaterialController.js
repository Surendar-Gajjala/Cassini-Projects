define(['app/app.modules',
        'app/shared/services/cachingService',
        'app/components/prod/material/materialFactory',
        'app/components/prod/supplier/suppliersFactory'
    ],
    function ($app) {
        $app.controller('NewMaterialController',
            [
                '$scope', '$rootScope', '$timeout', '$interval', '$state', '$cookies',
                '$modalInstance', 'cachingService', 'suppliersFactory', 'materialFactory',
                function ($scope, $rootScope, $timeout, $interval, $state, $cookies,
                          $modalInstance, cachingService, suppliersFactory, materialFactory) {

                    $scope.loading = true;
                    $scope.itemExists = false;
                    $scope.errorMessage = "";

                    /*function validate() {
                     var valid = true;
                     if ($scope.itemExists == true) {
                     valid = false;
                     }

                     return valid;
                     }*/

                    $scope.createMaterial = function () {
                        if (validate()) {
                            materialFactory.createMaterial($scope.newMaterial).then(
                                function (data) {
                                    $modalInstance.close(data);
                                }
                            )
                        }
                    };

                    $scope.cancel = function () {
                        $modalInstance.dismiss('cancel');
                    };

                    $scope.newMaterial = {
                        sku: null,
                        type: null,
                        suppliers: [],
                        category: {
                            id: null,
                            name: null
                        },
                        name: null,
                        description: null,
                        units: 'EA',
                        unitPrice: 0.0
                    };

                    $scope.materialTypes = [];

                    $scope.allSupplierrs = [];

                    $scope.nodeId = 0;
                    $scope.categoriesTreeRoot = {
                        id: 0,
                        text: "<b>Categories</b>",
                        iconCls: "product-categories-root-node",
                        attributes: {
                            catId: 0
                        },
                        children: []
                    };


                    /*   $scope.$on('$viewContentLoaded', function(){
                     $rootScope.setToolbarTemplate('new-product-view-tb');
                     });
                     */
                    function initTree() {
                        $scope.tree = $('#materialCategoriesTree').tree({
                            data: [$scope.categoriesTreeRoot],

                            onSelect: function (node) {
                                window.$("body").trigger("click");
                                $scope.newMaterial.category = node.attributes.category;
                                $scope.$apply();
                            }
                        });
                    }

                    createTreeNodes = function (categories) {
                        var roots = [categories.length];

                        for (var i = 0; i < categories.length; i++) {
                            var category = categories[i];

                            roots[i] = buildNode(category);

                            if (category.children.length > 0) {
                                traverseChildren(category, roots[i]);
                            }
                        }

                        var rootNode = $('#materialCategoriesTree').tree('find', 0);

                        if (rootNode != null && roots.length > 0) {
                            $('#materialCategoriesTree').tree('append', {
                                parent: rootNode.target,
                                data: roots
                            });
                        }
                    };

                    buildNode = function (category) {
                        var state = "closed";
                        if (category.children.length == 0) {
                            state = "open";
                        }

                        state = "open";

                        return {
                            id: $scope.nodeId++,
                            text: category.name,
                            state: state,
                            iconCls: "product-categories-category-node",
                            attributes: {
                                catId: category.id,
                                category: category
                            }
                        };
                    };

                    traverseChildren = function (category, parentNode) {
                        var nodes = [category.children.length];

                        for (var i = 0; i < category.children.length; i++) {
                            var cat = category.children[i];
                            nodes[i] = buildNode(cat);

                            if (cat.children.length > 0) {
                                traverseChildren(cat, nodes[i]);
                            }
                        }


                        parentNode.children = nodes;
                    };

                    function loadCategoryTypes() {
                        materialFactory.getMaterialsClassification().then(
                            function (data) {
                                initTree();
                                createTreeNodes(data);
                            },
                            function (error) {
                                console.error(error);
                            }
                        );

                    }


                    function validate() {
                        var valid = true;
                        if ($scope.newMaterial.sku == null || $scope.newMaterial.sku == undefined || $scope.newMaterial.sku == "") {
                            valid = false;
                            $scope.errorMessage = "SKU cannot be empty";
                        }
                        else if ($scope.newMaterial.type == null || $scope.newMaterial.type == undefined || $scope.newMaterial.type == "") {
                            valid = false;
                            $scope.errorMessage = "Type cannot be empty";
                        }
                        else if ($scope.newMaterial.category.id == null || $scope.newMaterial.category.id == undefined || $scope.newMaterial.category.id == "") {
                            valid = false;
                            $scope.errorMessage = "Category cannot be empty";
                        }
                        else if ($scope.newMaterial.name == null || $scope.newMaterial.name == undefined || $scope.newMaterial.name == "") {
                            valid = false;
                            $scope.errorMessage = "Name cannot be empty";
                        }else{
                            $scope.errorMessage = "";
                        }

                        return valid;
                    }


                    /* function loadBusinessUnits() {
                     businessUnitFactory.getBusinessUnits().then(
                     function(data) {
                     $scope.businessUnits = data;
                     }
                     )
                     }
                     */
                    function loadMaterialTypes() {
                        materialFactory.getMaterialTypes().then(
                            function (data) {
                                $scope.materialTypes = data;
                            }
                        )
                    }

                    function loadSuppliers() {
                        suppliersFactory.getAllSuppliers().then(
                            function (data) {
                                $scope.allSupplierrs = data;
                            }
                        )
                    }

                    $scope.checkSKU = function () {
                        materialFactory.getMaterialsBySku($scope.newMaterial.sku).then(
                            function (data) {
                                if ($scope.newMaterial.sku <= 0) {
                                    $scope.errorMessage = "Enter positive number for SKU";
                                    $scope.itemExists = true;
                                }
                                else if (data != null && data != "") {
                                    $scope.errorMessage = "SKU Already is exists";
                                    $scope.itemExists = true;
                                } else {
                                    $scope.itemExists = false;
                                    $scope.errorMessage = "";
                                }
                            }
                        )
                    };

                    (function () {
                        loadSuppliers();
                        loadCategoryTypes();
                        loadMaterialTypes();
                    })();
                }
            ]
        );
    }
);