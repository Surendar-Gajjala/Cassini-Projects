define(['app/app.modules',
        'app/shared/services/cachingService',
        'app/components/prod/product/productFactory',
        'app/components/common/businessUnitFactory'],
    function($app) {
        $app.controller('NewProductController',
            [
                '$scope', '$rootScope', '$timeout', '$interval', '$state', '$stateParams',
                '$cookies', 'cachingService', 'productFactory', 'businessUnitFactory',

                function($scope, $rootScope, $timeout, $interval, $state, $stateParams,
                         $cookies, cachingService, productFactory, businessUnitFactory) {
                    $rootScope.iconClass = "fa flaticon-debit-card";
                    $rootScope.viewTitle = "New Product";

                    $scope.itemExists = false;
                    $scope.newProduct = {
                        businessUnit: null,
                        sku: null,
                        type: 0,
                        typee: null,
                        category: {
                            id: null,
                            name: null
                        },
                        name: null,
                        description: null,
                        units: 'EA',
                        unitPrice: 0.0,
                        status: 'ACTIVE'
                    };

                    $scope.inventory = {
                        product: null,
                        inventory: 0,
                        threshold: 0
                    };

                    $scope.businessUnits = [];
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


                    $scope.$on('$viewContentLoaded', function(){
                        $rootScope.setToolbarTemplate('new-product-view-tb');
                    });

                    $scope.tree = $('#productCategoriesTree').tree({
                        data: [$scope.categoriesTreeRoot],

                        onSelect: function (node) {
                            window.$("body").trigger("click");
                            $scope.newProduct.category = node.attributes.category;
                            $scope.$apply();
                        }
                    });

                    createTreeNodes = function(categories) {
                        var roots = [categories.length];

                        for(var i=0; i<categories.length; i++) {
                            var category = categories[i];

                            roots[i] = buildNode(category);

                            if(category.children.length > 0) {
                                traverseChildren(category, roots[i]);
                            }
                        }

                        var rootNode = $('#productCategoriesTree').tree('find', 0);

                        if (rootNode != null && roots.length > 0) {
                            $('#productCategoriesTree').tree('append', {
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

                    productFactory.getProductsClassification().then (
                        function(data) {
                            createTreeNodes(data);
                        },
                        function(error) {
                            console.error(error);
                        }
                    );


                    $rootScope.createProduct = function() {
                        if(validate()) {
                            $scope.newProduct.type = $scope.newProduct.typee.id;
                            productFactory.createProduct($scope.newProduct).then(
                                function(data) {
                                    $scope.inventory.product = data;
                                    productFactory.createProductInventory($scope.inventory).then(
                                        function(data) {
                                            $rootScope.showSuccessMessage("Product '"  + $scope.newProduct.name + "' created successfully");
                                            $state.go('app.prod.products');
                                        }
                                    );
                                }
                            )
                        }
                    };

                    $rootScope.cancel = function() {
                        $scope.newProduct = null;
                        $state.go('app.prod.products');
                    }

                    function validate() {
                        var valid = true;
                        /*if($scope.newProduct.businessUnit == null) {
                            valid = false;
                            $rootScope.showErrorMessage("Business unit cannot be empty");
                        }
                        else*/ if($scope.newProduct.category == null) {
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
                            $rootScope.showErrorMessage("product cannot be empty");
                        }
                        else if($scope.newProduct.unitPrice == null && $scope.newProduct.unitPrice == "") {
                            valid = false;
                            $rootScope.showErrorMessage("unitPrice cannot be empty");
                        }
                       else if($scope.itemExists == true) {
                            valid = false;
                            $rootScope.showErrorMessage("SKU already exists");
                        }

                        return valid;
                    }

                    function loadBusinessUnits() {
                        businessUnitFactory.getBusinessUnits().then(
                            function(data) {
                                $scope.businessUnits = data;
                            }
                        )
                    }

                    function loadProductTypes() {
                        productFactory.getProductTypes().then(
                            function(data) {
                                $scope.productTypes = data;
                            }
                        )
                    }

                    $scope.checkSKU = function() {
                         productFactory.getProductBySku($scope.newProduct.sku).then(
                            function(data) {
                                if(data == "" && (data != null && data.name == undefined)){
                                    $scope.itemExists = false;
                                    $rootScope.closeNotification();
                                } else if(data != null  && data.name.length>1){
                                    $rootScope.showErrorMessage("SKU already exists");
                                    $scope.itemExists = true;
                                }
                            }
                        )
                    };


                    (function() {
                        loadBusinessUnits();
                        loadProductTypes();
                        //checkSKU();
                    })();

                }
            ]
        );
    }
);



