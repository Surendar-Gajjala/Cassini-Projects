define(['app/app.modules', 'app/components/prod/supplier/suppliersFactory',
        'app/components/prod/supplier/new/newSupplierController'],
    function($app) {
        $app.controller('SuppliersController',
            [
                '$scope', '$rootScope', '$timeout', '$interval', '$state', '$cookies','$modal', 'suppliersFactory',

                function($scope, $rootScope, $timeout, $interval, $state, $cookies,$modal, suppliersFactory) {

                    $rootScope.iconClass = "fa flaticon-plan2";
                    $rootScope.viewTitle = "All Suppliers";

                    $scope.emptyPagedResults = {
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
                    $scope.pageable = {
                        page: 1,
                        size: 5,
                        sort: {
                            label: "Name",
                            field: "name",
                            order: "asc"
                        }
                    };

                    $scope.suppliers = $scope.emptyPagedResults;

                    $scope.$on('$viewContentLoaded', function(){
                        $rootScope.setToolbarTemplate('suppliers-view-tb');
                    });

                    $scope.pageChanged = function() {
                        $scope.loading = true;
                        $scope.suppliers.content = [];
                        $scope.loadSuppliers();
                    };

                    $scope.loadSuppliers = function() {
                        suppliersFactory.getSuppliers($scope.pageable).then (
                            function(data) {
                                $scope.suppliers = data;
                            }
                        )
                    };


                    $rootScope.addMaterialSupplier = function() {
                     $state.go('app.prod.newsupplier', { supplierId: 0 });
                  //  $state.go('app.prod.newsupplier');

                    };

                    $scope.showSupplier = function(supplier) {
                        $state.go('app.prod.newsupplier', { supplierId: supplier.id });
                    };

                    (function() {
                        $scope.loadSuppliers();
                    })();
                }
            ]
        );
    }
);


