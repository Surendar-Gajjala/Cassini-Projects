define(['app/app.modules',
        'app/components/crm/order/orderFactory',
        'app/shared/directives/tableDirectives'
    ],
    function ($app) {
        $app.controller('OrderProcessingHomeController',
            [
                '$scope', '$rootScope', '$timeout', '$interval', '$state', '$cookies',
                'orderFactory',

                function ($scope, $rootScope, $timeout, $interval, $state, $cookies,
                          orderFactory) {
                    $scope.dateRangeOptions = {
                        ranges: {
                            'Today': [moment(), moment()],
                            'Yesterday': [moment().subtract(1, 'days'), moment().subtract(1, 'days')],
                            'Last 7 Days': [moment().subtract(6, 'days'), moment()],
                            'Last 30 Days': [moment().subtract(29, 'days'), moment()],
                            'This Month': [moment().startOf('month'), moment().endOf('month')],
                            'Last Month': [moment().subtract(1, 'month').startOf('month'), moment().subtract(1, 'month').endOf('month')],
                            'Yea to Date': [
                                moment().startOf('year').startOf('month').startOf('hour').startOf('minute').startOf('second'),
                                moment()
                            ],
                            'Last Year': [
                                moment().subtract(1, 'year').startOf('year'),
                                moment().subtract(1, 'year').endOf('year')
                            ]
                        }
                    };
                    $scope.loading = true;
                    $scope.pageable = {
                        page: 1,
                        size: 15,
                        sort: {
                            label: "orderedDate",
                            field: "orderedDate",
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

                    $scope.orders = $scope.pagedResults;

                    $scope.emptyFilters = {
                        orderType: null,
                        orderNumber: null,
                        orderedDate: {
                            startDate: null,
                            endDate: null
                        },
                        deliveryDate: {
                            startDate: null,
                            endDate: null
                        },
                        customer: null,
                        region: null,
                        salesRep: null,
                        orderTotal: null,
                        status: "APPROVED,PROCESSING",
                        invoiceNumber: null,
                        trackingNumber: null
                    };
                    $scope.filters = angular.copy($scope.emptyFilters);

                    $scope.sortColumn =  function (label, field) {
                        if($scope.pageable.sort.label == label) {
                            if($scope.pageable.sort.order == 'asc') {
                                $scope.pageable.sort.order = 'desc';
                            }
                            else {
                                $scope.pageable.sort.order = 'asc';
                            }
                        }
                        else {
                            $scope.pageable.sort.label = label;
                            $scope.pageable.sort.order = 'asc';
                        }

                        $scope.pageable.sort.field = field;

                        $scope.pageable.page = 1;
                        loadOrders();
                    };

                    $scope.$watch('filters.orderedDate', function(date){
                        $scope.applyFilters();
                    });


                    $scope.pageChanged = function() {
                        $scope.loading = true;
                        $scope.orders.content = [];
                        loadOrders();
                    };

                    $scope.resetFilters = function() {
                        $scope.filters = angular.copy($scope.emptyFilters);
                        $scope.pageable.page = 1;
                        loadOrders();
                    };

                    $scope.applyFilters = function() {
                        $scope.pageable.page = 1;
                        loadOrders();
                    };

                    $scope.openOrderDetails = function(order) {
                        $state.go('app.crm.orders.details', { orderId: order.id });
                    };

                    function loadOrders() {
                        if($scope.filters.orderedDate != null &&
                            $scope.filters.orderedDate.startDate != null) {
                            var d = $scope.filters.orderedDate.startDate;
                            $scope.filters.orderedDate.startDate = moment(d).format('DD/MM/YYYY');
                        }
                        if($scope.filters.orderedDate != null &&
                            $scope.filters.orderedDate.endDate != null) {
                            var d = $scope.filters.orderedDate.endDate;
                            $scope.filters.orderedDate.endDate = moment(d).format('DD/MM/YYYY');
                        }
                        orderFactory.getOrders($scope.filters, $scope.pageable).then(
                            function(data) {
                                $scope.orders = data;
                                $scope.loading = false;
                            }
                        )
                    }

                    $scope.bookmarkOrder = function(order) {
                        order.starred = true;
                        var savedOrder = angular.copy(order);
                        delete savedOrder["shippingAddress"];
                        delete savedOrder["billingAddress"];

                        orderFactory.updateOrder(savedOrder).then (
                            function(data) {
                                $rootScope.showSuccessMessage("Order bookmarked!");
                            }
                        )
                    };

                    (function() {
                        loadOrders();
                    })();
                }
            ]
        );
    }
);