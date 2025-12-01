define(['app/app.modules',
        'app/shared/directives/commonDirectives',
        'app/mobile/directives/mobileDirectives',
        'app/components/crm/order/orderFactory'
    ],
    function ($app) {
        $app.controller('HomeController',
            [
                '$scope', '$rootScope', '$timeout', '$interval', '$state', '$cookies', '$q',
                'orderFactory',

                function ($scope, $rootScope, $timeout, $interval, $state, $cookies, $q,
                          orderFactory) {
                    $rootScope.viewName = "Home";
                    $rootScope.backgroundColor = "#1565c0";

                    window.$("#appview").show();

                    window.moment = moment;

                    $scope.flipped = false;
                    $scope.inited = false;
                    $scope.orders = [];
                    $scope.loading = true;

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
                    $scope.pageable = {
                        page: 1,
                        size: 10,
                        sort: {
                            label: "modifiedDate",
                            field: "modifiedDate",
                            order: "desc"
                        }
                    };
                    $scope.criteria = {
                        status: null,
                        orderedDate: {
                            startDate: null,
                            endDate: null
                        },
                        deliveryDate: {
                            startDate: null,
                            endDate: null
                        }
                    };

                    $scope.orderCounts = {
                        newOrders: 0,
                        approvedOrders: 0,
                        processingOrders: 0,
                        pendingOrders: 0,
                        shippedOrders: 0,
                        canceledOrders: 0
                    };


                    $scope.flip = function() {
                        $scope.flipped = !$scope.flipped;
                    };

                    $scope.refreshOrders = function() {
                        $scope.loading = true;
                        orderFactory.getOrders($scope.criteria, $scope.pageable).then(
                            function(data) {
                                $scope.orders = data.content;
                                $scope.loading = false;
                            }
                        );

                    };

                    $scope.approveOrder = function(order) {
                        orderFactory.approveOrder(order.id).then(
                            function(data) {
                                var index = $scope.orders.indexOf(order);
                                if(index != -1) {
                                    $scope.orders.splice(index, 1);
                                }
                            }
                        );
                    };

                    $scope.nextPage = function() {
                        if($scope.pagedResults.last == false) {
                            $scope.pageable.page = $scope.pageable.page + 1;
                            orderFactory.getOrders($scope.criteria, $scope.pageable).then(
                                function(data) {
                                    $scope.pagedResults = data;
                                    angular.forEach(data.content, function(order) {
                                        $scope.orders.push(order);
                                    });
                                }
                            )
                        }
                    };

                    $scope.showOrders = function(status) {
                        $scope.inited = true;
                        $scope.loading = true;
                        $scope.orders = [];
                        $scope.pageable.page = 1;
                        $scope.pagedResults = angular.copy($scope.emptyPagedResults);
                        $scope.flip();

                        $scope.criteria.status = status;
                        orderFactory.getOrders($scope.criteria, $scope.pageable).then(
                            function(data) {
                                $scope.pagedResults = data;
                                $scope.orders = data.content;
                                $scope.loading = false;
                            }
                        )
                    };

                    function loadOrderCounts() {
                        var criteria = angular.copy($scope.criteria);
                        var pageable = angular.copy($scope.pageable);
                        pageable.page = 1;
                        pageable.size = 1;

                        criteria.status = "NEW";

                        orderFactory.getOrders(criteria, pageable).then(
                            function(data) {
                                $scope.orderCounts.newOrders = data.totalElements;
                                criteria.status = "APPROVED";
                                return orderFactory.getOrders(criteria, pageable);
                            }
                        ).then(
                            function(data) {
                                $scope.orderCounts.approvedOrders = data.totalElements;
                                criteria.status = "PROCESSED,PARTIALLYSHIPPED";
                                return orderFactory.getOrders(criteria, pageable);
                            }
                        ).then(
                            function(data) {
                                $scope.orderCounts.pendingOrders = data.totalElements;
                                criteria.status = "PROCESSING";
                                return orderFactory.getOrders(criteria, pageable);
                            }
                        ).then(
                            function(data) {
                                $scope.orderCounts.processingOrders = data.totalElements;
                                criteria.status = "SHIPPED";
                                return orderFactory.getOrders(criteria, pageable);
                            }
                        ).then(
                            function(data) {
                                $scope.orderCounts.shippedOrders = data.totalElements;
                                criteria.status = "CANCELLED";
                                return orderFactory.getOrders(criteria, pageable);
                            }
                        ).then(
                            function(data) {
                                $scope.orderCounts.canceledOrders = data.totalElements;
                            }
                        );
                    }

                    $scope.removeHold = function(order) {
                        order.onhold = false;
                        orderFactory.updateOrder(order).then (
                            function(data) {

                            }
                        );
                    };

                    (function() {
                        loadOrderCounts();
                    })();
                }
            ]
        );
    }
);