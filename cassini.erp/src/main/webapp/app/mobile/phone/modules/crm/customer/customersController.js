define(['app/app.modules',
        'app/shared/directives/commonDirectives',
        'app/components/crm/customer/customerFactory'
    ],
    function ($app) {
        $app.controller('CustomersController',
            [
                '$scope', '$rootScope', '$timeout', '$interval', '$state', '$cookies', '$mdDialog',
                'customerFactory',

                function ($scope, $rootScope, $timeout, $interval, $state, $cookies, $mdDialog,
                          customerFactory) {
                    $rootScope.viewName = "Customers";
                    $rootScope.backgroundColor = "#ff1744";

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

                    $scope.emptyFilters = {
                        searchQuery: null,
                        name: null,
                        region: null,
                        state: null,
                        salesRep: null,
                        contactPerson: null,
                        contactPhone: null
                    };

                    $scope.pagedResults = angular.copy($scope.emptyPagedResults);
                    $scope.filters = angular.copy($scope.emptyFilters);

                    $scope.$watch('searchText', function() {
                        $scope.pagedResults = angular.copy($scope.emptyPagedResults);
                        $scope.filters = angular.copy($scope.emptyFilters);
                        $scope.pageable.page = 0;
                    });

                    $scope.search = function() {
                        var inputs = window.$('#search').find('input');
                        inputs.blur();

                        if($scope.pageable.page == 0) {
                            $scope.results = [];
                        }

                        $scope.noResults = false;

                        if($scope.pagedResults.last == false) {
                            $scope.pageable.page = $scope.pageable.page + 1;
                            if($scope.searchText != null && $scope.searchText != "") {
                                $scope.filters.searchQuery = $scope.searchText;

                                customerFactory.searchCustomers($scope.filters, $scope.pageable).then (
                                    function(data) {
                                        $scope.pagedResults = data;
                                        angular.forEach(data.content, function(item) {
                                            $scope.results.push(item);
                                        });

                                        if(data.totalElements == 0) {
                                            $scope.noResults = true;
                                        }
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