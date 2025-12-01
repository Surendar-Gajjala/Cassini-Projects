define(['app/app.modules',
        'app/components/prod/product/productFactory'
    ],
    function($app) {
        $app.controller('ProductSelectionController',
            [
                '$scope', '$localStorage', '$modalInstance', 'selectType','productFactory',

                function($scope, $localStorage, $modalInstance,selectType, productFactory) {
                    $scope.$storage = $localStorage;

                    $scope.loading = true;
                    $scope.selectedType=selectType;
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

                    $scope.products = $scope.emptyResults;

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

                    $scope.applyCriteria = function() {
                        $scope.pageable.page = 1;
                        $scope.loadProducts();
                    };

                    $scope.selectProduct = function(product) {

                    };

                    $scope.resetCriteria = function() {
                        $scope.filters = {
                            name: ""
                        };
                        $scope.pageable.page = 1;
                        $scope.loadProducts();
                    };

                    $scope.ok = function() {
                        var selectedProducts = $scope.getSelectedProducts();
                        $modalInstance.close(selectedProducts);
                    };

                    $scope.cancel = function() {
                        $modalInstance.dismiss('cancel');
                    };

                    $scope.toggleSelection = function(prod) {
                        if(prod.selected == true && $scope.isProductSelected(prod) == false) {
                            $scope.selectedProducts.push(prod)
                        }
                        else if ($scope.isProductSelected(prod) == true){
                            $scope.selectedProducts.splice(prod, 1);
                        }
                    };

                    $scope.isProductSelected = function(prod) {
                        var selected = false;
                        angular.forEach($scope.selectedProducts, function(product) {
                            if(prod.id == product.id && product.selected == true) {
                                selected = true;
                            }
                        });

                        return selected;
                    };

                    $scope.getSelectedProducts = function() {
                        var selected = [];
                        angular.forEach($scope.products.content, function(product) {
                            if(product.selected == true) {
                                selected.push(product);
                            }
                        });

                        return selected;
                    };

                    $scope.loadProducts = function () {
                        productFactory.getProducts($scope.filters, $scope.pageable).then (
                            function(data) {
                                $scope.loading = false;
                                $scope.products = data;
                            }
                        );
                    };

                    (function() {
                        $scope.loadProducts();
                    })();

                }
            ]
        );
    }
);