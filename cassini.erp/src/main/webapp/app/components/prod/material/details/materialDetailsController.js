define(['app/app.modules',
        'app/shared/services/cachingService',
        'app/components/prod/material/materialFactory',
        'app/components/prod/supplier/suppliersFactory',
        'app/components/prod/material/dialog/selectSupplierController'
    ],
    function ($app) {
        $app.controller('MaterialDetailsController',
            [
                '$scope', '$rootScope', '$timeout', '$interval', '$state', '$stateParams', '$cookies', '$modal',
                'cachingService', 'suppliersFactory', 'materialFactory',
                function ($scope, $rootScope, $timeout, $interval, $state, $stateParams, $cookies, $modal,
                          cachingService, suppliersFactory, materialFactory) {

                    $rootScope.save = function () {
                        materialFactory.updateMaterial($scope.material).then(
                            function (data) {
                                $state.go('app.prod.materials');
                            }
                        )
                    };

                    $rootScope.cancel = function () {
                        $state.go('app.prod.materials');
                    };
                    $scope.material = {
                        sku: null,
                        type: null,
                        suppliers: [],
                        materialSuppliers: [],
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
                    $rootScope.viewTitle = "Material Details";
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


                    $scope.$on('$viewContentLoaded', function () {
                        $rootScope.setToolbarTemplate('material-view-tb');
                    });

                    function initTree() {
                        $scope.tree = $('#materialCategoriesTree').tree({
                            data: [$scope.categoriesTreeRoot],

                            onSelect: function (node) {
                                window.$("body").trigger("click");
                                $scope.material.category = node.attributes.category;
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

                    function loadMaterialTypes() {
                        materialFactory.getMaterialTypes().then(
                            function (data) {
                                $scope.materialTypes = data;
                            }
                        )
                    }

                    function loadMaterial(materialId) {
                        materialFactory.getMaterial(materialId).then(
                            function (data) {
                                $scope.material = data;

                            }
                        )
                    }

                    $scope.checkSKU = function () {
                        materialFactory.getMaterialsBySku($scope.material.sku).then(
                            function (data) {
                                if ($scope.material.sku <= 0) {
                                    $rootScope.showErrorMessage("Enter positive number for SKU");
                                    $scope.itemExists = true;
                                }
                                else if (data != null && data != "") {
                                    $rootScope.showErrorMessage("already SKU is exists");
                                    $scope.itemExists = true;
                                }
                            }
                        )
                    };

                    $scope.deleteMaterialSupplier = function (ms) {
                        materialFactory.deleteMaterialSupplier(ms).then(
                            function (data) {
                                var index = $scope.material.materialSuppliers.indexOf(ms);
                                $scope.material.materialSuppliers.splice(index, 1);
                                $rootScope.showSuccessMessage("Material supplier deleted successfully");
                            }
                        )
                    };

                    $scope.addSupplier = function () {
                        var materialSuppliers = [];
                        var modalInstance = $modal.open({
                            animation: true,
                            templateUrl: 'app/components/prod/material/dialog/selectSupplierDialog.jsp',
                            controller: 'SelectSupplierController',
                            size: 'md',
                            resolve: {
                                materialId: function () {
                                    return $stateParams.materialId;
                                },
                                "dialogTitle": "Select suppliers for materials"
                            }
                        });

                        modalInstance.result.then(
                            function (selectedSuppliers) {
                                angular.forEach(selectedSuppliers, function (supplier) {
                                    var ms = {
                                        supplierId: supplier.id,
                                        cost: supplier.cost,
                                        materialId: $stateParams.materialId
                                    };
                                    materialSuppliers.push(ms);
                                });
                                materialFactory.createMaterialSuppliers(materialSuppliers).then(
                                    function (data) {
                                        loadMaterial($stateParams.materialId);
                                    }
                                )
                            }
                        );
                    };


                    (function () {
                        loadCategoryTypes();
                        loadMaterialTypes();

                        if ($stateParams.materialId != null && $stateParams.materialId != undefined && $stateParams.materialId > 0) {
                            loadMaterial($stateParams.materialId);
                        }
                    })();
                }
            ]
        );
    }
);
