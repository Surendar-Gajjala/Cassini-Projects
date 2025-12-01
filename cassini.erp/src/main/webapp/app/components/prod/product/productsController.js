define(['app/app.modules',
        'app/shared/services/cachingService',
        'app/components/prod/product/productFactory',
        'app/components/prod/product/category/newProductCategoryController'],
    function($app) {
        $app.controller('ProductsController',
            [
                '$scope', '$rootScope', '$timeout', '$interval', '$state', '$stateParams',
                '$cookies', '$modal','cachingService', 'productFactory',

                function($scope, $rootScope, $timeout, $interval, $state, $stateParams,
                         $cookies, $modal, cachingService, productFactory) {

                    $scope.$storage = cachingService;

                    $rootScope.iconClass = "fa flaticon-debit-card";
                    $rootScope.viewTitle = "Products";

                    $scope.loading = true;
                    $scope.hasProducts = false;

                    $scope.numCols = 4;
                    $scope.searchText = "";
                    $scope.selectedCategory = null;
                    $scope.showNoResults = false;
                    $scope.mode = "view";

                    if($stateParams.mode != undefined && $stateParams.mode == 'buy') {
                        $scope.mode = 'buy';
                    }


                    $scope.page = {
                        page: 1,
                        size: 24,
                        sort: {
                            label: "name",
                            field: "name",
                            order: "asc"
                        }
                    };

                    $scope.results = {
                        rows:[]
                    };

                    $scope.emptyPagedResults = {
                        content: [],
                        last: false,
                        totalPages: 0,
                        totalElements: 0,
                        size: 24,
                        number: 0,
                        sort: null,
                        first: false,
                        numberOfElements: 0
                    };

                    $scope.pagedResults = $scope.emptyPagedResults;

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

                    $rootScope.viewTypes = [
                        { icon:"fa fa-th-list", tooltip:"List view", name:"list", active:true},
                        { icon:"fa fa-th", tooltip:"Grid view", name:"grid", active:false }
                    ];

                    $scope.viewStorage = {
                        lastViewType: $scope.viewTypes[0]
                    };

                    $scope.$on('$viewContentLoaded', function(){
                        $rootScope.setToolbarTemplate('products-view-tb');
                    });

                    $rootScope.setView = function(view) {
                        if(view.active != true) {
                            angular.forEach($scope.viewTypes, function (v) {
                                v.active = false;
                            });
                            view.active = true;
                            $scope.viewStorage.lastViewType = view;
                        }
                    };
                    
                    
                    
                    $rootScope.addProductCategory = function() {
                        var modalInstance = $modal.open({
                            animation: true,
                            templateUrl: 'app/components/prod/product/category/newProductCategoryView.jsp',
                            controller: 'NewProductCategoryController',
                            size: 'lg'

                        });

                        modalInstance.result.then(
                            function (result) {
                             //   $scope.materials.content.unshift(result);
                                //   $state.go("app.prod.materials");

                            }
                        );

                    };

                    $rootScope.newProduct = function() {
                        $state.go('app.prod.newproduct');
                    };

                    //Load view storage
                    if($scope.$storage.productsViewStorage != null &&
                        $scope.$storage.productsViewStorage != undefined) {
                        $scope.viewStorage = $scope.$storage.productsViewStorage;

                        if($scope.viewStorage.lastViewType != null) {
                            angular.forEach($scope.viewTypes, function (view) {
                                if(view.name == $scope.viewStorage.lastViewType.name) {
                                    view.active = true;
                                }
                                else {
                                    view.active = false;
                                }
                            });
                        }
                    }
                    else {
                        $scope.$storage.productsViewStorage = $scope.viewStorage;
                    }


                    $scope.tree = $('#productCategoriesTree').tree({
                        data: [$scope.categoriesTreeRoot],

                        onSelect: function (node) {
                            window.$("body").trigger("click");
                            $scope.page.page = 1;
                            $scope.pagedResults = $scope.emptyPagedResults;
                            $scope.searchText = "";
                            $scope.selectedCategoryNode = node;

                            $scope.selectedCategory = node.attributes.category;

                            if ($scope.selectedCategory != null) {
                                $scope.categorySearch();
                            }

                        }
                    });

                    $scope.pageChanged = function() {
                        if ($scope.selectedCategory != null && $scope.selectedCategory != 0) {
                            $scope.categorySearch();
                        }
                        else {
                            $scope.freeTextSearch();
                        }
                    };

                    $scope.saveProductChanges = function(product) {
                        productFactory.updateProduct(product).then(
                            function(data) {
                                $rootScope.showSuccessMessage("Changes saved successfully")
                            },
                            function(error) {
                                $rootScope.showErrorMessage(error.message);
                            }
                        )
                    };

                    var formatResults = function() {
                        angular.forEach($scope.pagedResults.content, function(prod) {
                            var cartItem = $scope.getCartItem(prod);
                            if(cartItem != null) {
                                prod.quantity = cartItem.quantity;
                            }
                            prod.newPrice = prod.unitPrice;
                        });

                        $scope.showNoResults = ($scope.pagedResults.numberOfElements == 0);

                        var rows = 0;
                        if($scope.pagedResults.totalElements % $scope.numCols == 0) {
                            rows = $scope.pagedResults.numberOfElements / $scope.numCols;
                        }
                        else {
                            rows = (Math.floor($scope.pagedResults.numberOfElements / $scope.numCols)) + 1;
                        }

                        $scope.results.rows = [rows];

                        var counter = 0;
                        for(var i=0; i<rows; i++) {
                            $scope.results.rows[i] = [$scope.numCols];
                            for(var j=0; j<$scope.numCols; j++) {
                                $scope.results.rows[i][j] = $scope.pagedResults.content[counter];
                                counter = counter + 1;

                                if(counter == $scope.pagedResults.numberOfElements) {
                                    break;
                                }
                            }

                            if(counter == $scope.pagedResults.numberOfElements) {
                                break;
                            }
                        }

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

                    $scope.toggleRow = function(prod) {
                        prod.expanded = !prod.expanded;

                        if(prod.expanded == true) {
                            var pageable = {
                                page: 1,
                                size: 1000,
                                sort: {
                                    label: "name",
                                    field: "name",
                                    order: "asc"
                                }
                            };
                            productFactory.searchProducts(prod.id, pageable).then(
                                function (data) {
                                    angular.forEach(data.content, function (item) {
                                        item.newPrice = item.unitPrice;
                                    });

                                    var items = [];
                                    angular.forEach($scope.pagedResults.content, function (item) {
                                        items.push(item);
                                        if (item.id == prod.id) {
                                            angular.forEach(data.content, function (subitem) {
                                                subitem.child = true;
                                                items.push(subitem);
                                            });
                                        }
                                    });

                                    $scope.pagedResults.content = items;
                                    prod.children = data.content;
                                },
                                function (error) {
                                    console.error(error);
                                }
                            );
                        }
                        else {
                            if(prod.children != null && prod.children != undefined) {
                                var map = new Hashtable();
                                angular.forEach(prod.children, function(child) {
                                    map.put(child.id, child);
                                });

                                var items = [];
                                angular.forEach($scope.pagedResults.content, function (item) {
                                    if(map.get(item.id) == null) {
                                        items.push(item);
                                    }
                                });

                                $scope.pagedResults.content = items;
                                prod.children = null;
                            }
                        }
                    };

                    $scope.buyProduct = function(product) {
                        var proceed = true;

                        if(product.quantity == undefined ||
                            isNaN(product.quantity) ||
                                product.quantity < 0) {
                            $rootScope.showErrorMessage("Quantity has to be a positive whole number");
                            proceed = false;
                        }
                        else if(!isNaN(product.quantity)) {
                            var s = "" + product.quantity;
                            if(s.indexOf('.') != -1) {
                                $rootScope.showErrorMessage("Quantity has to be a positive whole number");
                                proceed = false;
                            }
                        }

                        if(proceed) {
                            $rootScope.closeNotification();

                            if(product.type == 'category') {
                                var pageable = {
                                    page: 1,
                                    size: 1000,
                                    sort: {
                                        label: "name",
                                        field: "name",
                                        order: "asc"
                                    }
                                };
                                productFactory.searchProducts(product.id, pageable).then (
                                    function(data) {
                                        angular.forEach(data.content, function(item) {
                                            item.newPrice = item.unitPrice;
                                            item.quantity = product.quantity;
                                            addToCart(item);
                                        });

                                        $rootScope.showSuccessMessage("{0} items added to the orders".format(data.content.length));
                                    },
                                    function(error) {
                                        console.error(error);
                                    }
                                )
                            }
                            else {
                                addToCart(product);
                            }
                        }

                        var qtyIndex = $scope.pagedResults.content.indexOf(product);
                        if(qtyIndex != -1) {
                            $timeout(function() {
                                window.$('#qty'+qtyIndex).focus();
                            }, 100);
                        }

                    };

                    function addToCart(product) {
                        if($app.shoppingCart == null) {
                            $app.shoppingCart = {
                                items: []
                            }
                        }

                        if($scope.checkCartIfExists(product) == false) {
                            var q = product.quantity;
                            if(q != null && q != undefined && q != "") {
                                var item = {
                                    product: {
                                        id: product.id,
                                        sku: product.sku,
                                        name: product.name,
                                        unitPrice: product.unitPrice
                                    },
                                    quantity: q,
                                    unitPrice: product.newPrice,
                                    serialNumber: $app.shoppingCart.items.length+1
                                };

                                $app.shoppingCart.items.push(item);
                            }
                        }
                        else {
                            q = product.quantity;
                            var cartItem = $scope.getCartItem(product);
                            if(q > 0) {
                                cartItem.quantity = product.quantity;
                            }
                            else {
                                $app.shoppingCart.items.splice($app.shoppingCart.items.indexOf(cartItem), 1);
                                product.quantity = 0;
                            }
                            cartItem.unitPrice = product.newPrice;
                        }
                    }

                    $scope.removeItem = function(prod) {
                        var cartItem = $scope.getCartItem(prod);
                        if(cartItem != null) {
                            var index = $app.shoppingCart.items.indexOf(cartItem);
                            $app.shoppingCart.items.splice(index, 1);

                            index = $scope.pagedResults.content.indexOf(prod);
                            $scope.pagedResults.content[index].quantity = 0;
                        }
                    };


                    $scope.getCartItem = function(product) {
                        var cartItem = null;
                        if($app.shoppingCart != null) {
                            angular.forEach($app.shoppingCart.items, function(item) {
                                if(item.product.id == product.id) {
                                    cartItem = item;
                                }
                            })
                        }

                        return cartItem;
                    };


                    $scope.checkCartIfExists = function(product) {
                        var check = false;
                        if($app.shoppingCart != null) {
                            angular.forEach($app.shoppingCart.items, function(item) {
                                if(item.product.id == product.id) {
                                    check = true;
                                    product.quantity = item.quantity;
                                    product.newPrice = item.unitPrice;
                                }
                            })
                        }

                        return check;
                    };

                    $scope.performTextSearch = function() {
                        $scope.selectedCategory = null;
                        $scope.page.page = 1;

                        $scope.freeTextSearch();
                    };

                    $scope.categorySearch = function() {
                        $scope.viewStorage.lastSearchType = "category";
                        $scope.viewStorage.lastCategory = $scope.selectedCategory;

                        var category = $scope.selectedCategory;
                        if(category != null &&
                            category.children != undefined &&
                            category.children != null &&
                            category.children.length > 0) {

                            var items = [];
                            angular.forEach(category.children, function(child) {
                                var item = {
                                    id: child.id,
                                    unitPrice: 0,
                                    name: child.name,
                                    quantity: null
                                };

                                item.type = "category";
                                item.expanded = false;
                                items.push(item);
                            });

                            $scope.pagedResults = {
                                content: items,
                                last: true,
                                totalPages: 0,
                                totalElements: items.length,
                                size: items.length,
                                number: items.length,
                                sort: null,
                                first: true,
                                numberOfElements: items.length
                            };

                            $scope.$apply();
                        }
                        else {
                            productFactory.searchProducts($scope.selectedCategory.id, $scope.page).then (
                                function(data) {
                                    $scope.pagedResults = data;
                                    formatResults();
                                },
                                function(error) {
                                    console.error(error);
                                }
                            )
                        }
                    };

                    $scope.freeTextSearch = function() {
                        $scope.viewStorage.lastSearchType = "text";
                        $scope.viewStorage.lastSearchText = $scope.searchText;

                        var selected = $('#productCategoriesTree').tree('getSelected');
                        if(selected != null) {
                            $(selected.target).removeClass('tree-node-selected')
                        }

                        productFactory.freeTextSearch($scope.searchText, $scope.page).then (
                            function(data) {
                                $scope.pagedResults = data;
                                formatResults();
                            },
                            function(error) {
                                $rootScope.showErrorMessage("error.message");
                            }
                        );
                    };

                    $scope.getPictureUrl = function(product) {
                        return "/api/production/products/{0}/picture?apiKey={1}".
                                    format(product.id, $app.session.apiKey);
                    };

                    productFactory.getProductsClassification().then (
                        function(data) {
                            if(data == "") {
                                $scope.hasProducts = false;
                                $scope.loading = false;
                            }
                            else {
                                $scope.hasProducts = true;
                                $scope.loading = false;
                                createTreeNodes(data);
                            }
                        },
                        function(error) {
                            console.error(error);
                        }
                    );

                    loadLastSearch = function() {
                        var searchType = $scope.viewStorage.lastSearchType;
                        if(searchType != null) {
                            if(searchType == 'category') {
                                var cat = $scope.viewStorage.lastCategory;
                                if(cat != null) {
                                    $scope.selectedCategory = cat;
                                    $scope.categorySearch();
                                }
                            }
                            else if(searchType == 'text') {
                                var text = $scope.viewStorage.lastSearchText;
                                if(text != null) {
                                    $scope.searchText = text;
                                    $scope.freeTextSearch();
                                }
                            }
                        }
                    };

                    (function() {
                        loadLastSearch();
                    })();

                }
            ]
        );
    }
);



