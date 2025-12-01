define(['app/app.modules',
        'app/shared/services/cachingService',
        'app/components/prod/material/materialFactory'
    ],
    function ($app) {
        $app.controller('NewMaterialCategoryController',
            [
                '$scope', '$rootScope', '$timeout', '$interval', '$state', '$cookies',
                '$modalInstance', 'cachingService', 'materialFactory',
                function ($scope, $rootScope, $timeout, $interval, $state, $cookies,
                          $modalInstance, cachingService, materialFactory) {

                    $scope.loading = true;
                    $scope.errorMessage = "";

                    $scope.ok = function () {
                        if (validate()) {
                            materialFactory.createMaterialCategory($scope.newMaterialCat).then(
                                function (data) {
                                    $modalInstance.close(data);
                                }
                            )
                        }
                    };

                    $scope.cancel = function () {
                        $modalInstance.dismiss('cancel');
                    };
                    $scope.newMaterialCat = {
                        type: null,
                        category: {
                            id: null,
                            name: null
                        },
                        name: null,
                        description: null
                    };

                    $scope.materialTypes = [];

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
                                $scope.newMaterialCat.category = node.attributes.category;
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
                        if ($scope.newMaterialCat.name == null || $scope.newMaterialCat.name == "" || $scope.newMaterialCat.name == undefined) {
                            valid = false;
                            $scope.errorMessage = "Name cannot be empty";
                        }
                        else if ($scope.newMaterialCat.type == null || $scope.newMaterialCat.type == "" || $scope.newMaterialCat.type == undefined) {
                            valid = false;
                            $scope.errorMessage = "Type cannot be empty";
                        }
                        else {
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

                    (function () {
                        loadCategoryTypes();
                        loadMaterialTypes();
                        //   checkSKU();
                    })();

                }
            ]
        );
    }
);



