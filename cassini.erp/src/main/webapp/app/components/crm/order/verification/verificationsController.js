define([
        'app/app.modules',
        'app/components/crm/order/orderFactory',
        'app/shared/constants/listValues'
    ],
    function($app) {
        $app.controller('VerificationsController',
            [
                '$scope', '$rootScope', '$timeout', '$interval', '$state', '$cookies',
                    'orderFactory', 'ERPLists',

                function($scope, $rootScope, $timeout, $interval, $state, $cookies,
                         orderFactory, ERPLists) {

                    $rootScope.iconClass = "fa flaticon-chart44";
                    $rootScope.viewTitle = "Verifications";


                    $scope.orderStatusList = ERPLists.orderStatus;
                    $scope.loading = true;
                    $scope.pageable = {
                        page: 1,
                        size: 10,
                        sort: {
                            label: "assignedDate",
                            field: "assignedDate",
                            order: "desc"
                        }
                    };
                    $scope.pagedResults = {
                        content: [],
                        last: false,
                        totalPages: 0,
                        totalElements: 0,
                        size: $scope.pageable.size,
                        number: 0,
                        sort: null,
                        first: false,
                        numberOfElements: 0
                    };

                    $scope.verifications = $scope.pagedResults;

                    $scope.emptyFilters = {
                        orderNumber: null,
                        status: null,
                        poNumber: null,
                        customer: null,
                        assignedTo: null
                    };
                    $scope.filters = angular.copy($scope.emptyFilters);

                    function loadVerifications() {
                        orderFactory.getOrderVerifications($scope.filters, $scope.pageable).then(
                            function(data) {
                                $scope.verifications = data;
                                $scope.loading = false;
                            }
                        )
                    }

                    $scope.pageChanged = function() {
                        $scope.loading = true;
                        $scope.verifications.content = [];
                        loadVerifications();
                    };

                    $scope.resetFilters = function() {
                        $scope.loading = true;
                        $scope.verifications.content = [];
                        $scope.filters = angular.copy($scope.emptyFilters);
                        $scope.pageable.page = 1;
                        loadVerifications();
                    };

                    $scope.applyFilters = function() {
                        $scope.loading = true;
                        $scope.verifications.content = [];
                        $scope.pageable.page = 1;
                        loadVerifications();
                    };

                    (function() {
                        loadVerifications();
                    })();
                }
            ]
        );
    }
);