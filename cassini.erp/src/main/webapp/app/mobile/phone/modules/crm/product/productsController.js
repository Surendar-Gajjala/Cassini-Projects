define(['app/app.modules',
        'app/shared/directives/commonDirectives',
        'app/components/prod/product/productFactory'
    ],
    function ($app) {
        $app.controller('ProductsController',
            [
                '$scope', '$rootScope', '$timeout', '$interval', '$state', '$cookies',
                'productFactory',

                function ($scope, $rootScope, $timeout, $interval, $state, $cookies,
                          productFactory) {
                    $rootScope.viewName = "Products";
                    $rootScope.backgroundColor = "#f57c00";

                    $scope.results = [];
                    $scope.searchText = null;

                    $scope.noResults = false;

                    $scope.pageable = {
                        page: 0,
                        size: 20,
                        sort: {
                            label: "name",
                            field: "name",
                            order: "asc"
                        }
                    };

                    $scope.emptyPagedResults = {
                        content: [],
                        last: false,
                        totalPages: 0,
                        totalElements: 0,
                        size: 20,
                        number: 0,
                        sort: null,
                        first: true,
                        numberOfElements: 0
                    };

                    $scope.pagedResults = angular.copy($scope.emptyPagedResults);

                    $scope.getPictureUrl = function(product) {
                        return "/api/production/products/{0}/picture".
                            format(product.id);
                    };

                    $scope.$watch('searchText', function() {
                        $scope.pagedResults = angular.copy($scope.emptyPagedResults);
                        $scope.pageable.page = 0;
                    });

                    $scope.loadProductInventories = function(products) {
                        if(products.length > 0) {
                            var ids = [];
                            angular.forEach(products, function(product) {
                                ids.push(product.id);
                            });

                            productFactory.getProductsInventory(ids).then(
                                function(data) {
                                    var hashtable = new Hashtable();
                                    angular.forEach(data, function(item) {
                                        hashtable.put(item.product.id, item);
                                    });

                                    angular.forEach(products, function(product) {
                                        var i = hashtable.get(product.id);
                                        if(i != null) {
                                            product.inventory = i;
                                        }
                                    });
                                }
                            )
                        }
                    };

                    $scope.search = function() {
                        $scope.noResults = false;
                        var inputs = window.$('#search').find('input');
                        inputs.blur();

                        if($scope.pageable.page == 0) {
                            $scope.results = [];
                        }

                        if($scope.pagedResults.last == false) {
                            $scope.pageable.page = $scope.pageable.page + 1;
                            if($scope.searchText != null && $scope.searchText != "") {
                                productFactory.freeTextSearch($scope.searchText, $scope.pageable).then (
                                    function(data) {
                                        $scope.pagedResults = data;
                                        angular.forEach(data.content, function(item) {
                                            $scope.results.push(item);
                                        });

                                        if(data.totalElements == 0) {
                                            $scope.noResults = true;
                                        }

                                        $scope.loadProductInventories(data.content);
                                    },
                                    function(error) {
                                    }
                                );
                            }
                        }
                    };

                }
            ]
        );
    }
);