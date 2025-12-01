define(['app/app.modules',
        'app/shared/services/cachingService',
        'app/components/prod/product/productFactory',
        'app/components/common/businessUnitFactory'
    ],
    function ($app) {
        $app.controller('NewProductCategoryController',
            [
                '$scope', '$rootScope', '$timeout', '$interval', '$state', '$cookies',
                '$modalInstance','cachingService', 'productFactory', 'businessUnitFactory',
                function ($scope, $rootScope, $timeout, $interval, $state, $cookies,
                          $modalInstance, cachingService,productFactory,businessUnitFactory) {

                    $scope.loading = true;

                    $scope.ok = function() {
                    	$scope.newProductCat.type = $scope.newProductCat.type.id;
                    	productFactory.createProductCategory($scope.newProductCat).then(

                            function(data)  {
                                $modalInstance.close(data);
                            }
                        )

                    };

                    $scope.cancel = function() {
                        $modalInstance.dismiss('cancel');
                    };
                    $scope.newProductCat = {

                        type: null,
                        category: {
                            id: null,
                            name: null
                        },
                        name: null,
                        code:null,
                        description: null
                     };

                    $scope.productTypes = [];

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
                        $scope.tree = $('#productCategoryTree').tree({
                            data: [$scope.categoriesTreeRoot],

                            onSelect: function (node) {
                                window.$("body").trigger("click");
                                $scope.newProductCat.category = node.attributes.category;
                                $scope.$apply();
                            }
                        });
                    };

                    createTreeNodes = function(categories) {
                        var roots = [categories.length];

                        for(var i=0; i<categories.length; i++) {
                            var category = categories[i];

                            roots[i] = buildNode(category);

                            if(category.children.length > 0) {
                                traverseChildren(category, roots[i]);
                            }
                        }

                        var rootNode = $('#productCategoryTree').tree('find', 0);

                        if (rootNode != null && roots.length > 0) {
                            $('#productCategoryTree').tree('append', {
                                parent: rootNode.target,
                                data: roots
                            });
                        }
                    };

                    buildNode = function(category) {
                        var state = "closed";
                        if(category.children.length == 0) {
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

                    traverseChildren = function(category, parentNode) {
                        var nodes = [category.children.length];

                        for(var i=0; i<category.children.length; i++) {
                            var cat = category.children[i];
                            nodes[i] = buildNode(cat);

                            if(cat.children.length > 0) {
                                traverseChildren(cat, nodes[i]);
                            }
                        }


                        parentNode.children = nodes;
                    };

                    function loadCategoryTypes() {
                    	productFactory.getProductsClassification().then (
                            function(data) {
                                initTree();
                                createTreeNodes(data);
                            },
                            function(error) {
                                console.error(error);
                            }
                        );

                    }  

                    /*  function validate() {
                     var valid = true;
                     if($scope.newProduct.businessUnit == null) {
                     valid = false;
                     $rootScope.showErrorMessage("Business unit cannot be empty");
                     }
                     else if($scope.newProduct.category == null) {
                     valid = false;
                     $rootScope.showErrorMessage("Category cannot be empty");
                     }
                     else if($scope.newProduct.sku == null) {
                     valid = false;
                     $rootScope.showErrorMessage("SKU cannot be empty");
                     }
                     else if($scope.newProduct.type == null) {
                     valid = false;
                     $rootScope.showErrorMessage("Product type cannot be empty");
                     }
                     else if($scope.newProduct.name == null && $scope.newProduct.name == "") {
                     valid = false;
                     $rootScope.showErrorMessage("Business unit cannot be empty");
                     }
                     else if($scope.newProduct.unitPrice == null && $scope.newProduct.unitPrice == "") {
                     valid = false;
                     $rootScope.showErrorMessage("Business unit cannot be empty");
                     }
                     else if($scope.itemExists == false) {
                     valid = false;
                     $rootScope.showErrorMessage("SKU already exists");
                     }

                     return valid;
                     }
                     */
                     function loadBusinessUnits() {
	                     businessUnitFactory.getBusinessUnits().then(
		                     function(data) {
		                    	 $scope.businessUnits = data;
		                     }
	                     )
                     }
                     
                    function getProductTypes() {
                    	productFactory.getProductTypes().then(
                            function(data) {
                                $scope.productTypes = data;
                            }
                        )
                    }

                   (function() {
                	    loadBusinessUnits();
                        loadCategoryTypes();                   
                        getProductTypes();                       
                       
                    })();

                }
            ]
        );
    }
);



