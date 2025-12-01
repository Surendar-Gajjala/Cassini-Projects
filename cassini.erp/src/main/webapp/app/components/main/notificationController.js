define(['app/app.modules',
        'app/components/crm/order/orderFactory',
        'app/components/crm/return/returnFactory',
        'app/components/login/loginFactory',
        'app/components/admin/security/session/sessionFactory'
    ],
    function ($app) {
        $app.controller('NotificationController',
            [
                '$scope', '$rootScope', '$state', '$interval',
                'orderFactory', 'returnFactory', 'loginFactory', 'sessionFactory',

                function ($scope, $rootScope, $state, $interval,
                          orderFactory, returnFactory, loginFactory, sessionFactory) {
                    $scope.ordersCheckPromise = null;
                    $scope.mode = "pending";
                    $scope.headerText = "PENDING RETURNS";

                    $scope.pageable = {
                        page: 1,
                        size: 10,
                        sort: {
                            label: "modifiedDate",
                            field: "modifiedDate",
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

                    $rootScope.ordersNotification = {
                        newOrders: angular.copy($scope.pagedResults),
                        approvedOrders: angular.copy($scope.pagedResults),
                        pendingOrders: angular.copy($scope.pagedResults),
                        partiallyShippedOrders: angular.copy($scope.pagedResults),
                        lateApprovedOrders: angular.copy($scope.pagedResults),
                        lateProcessedOrders: angular.copy($scope.pagedResults),
                        lateShippedOrders: angular.copy($scope.pagedResults)

                    };

                    $rootScope.returnsNotification = {
                        approvedReturns: angular.copy($scope.pagedResults),
                        pendingReturns: angular.copy($scope.pagedResults),
                    };


                    $scope.criteria = {
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
                        status: null,
                        invoiceNumber: null,
                        trackingNumber: null,
                        modifiedDate: null
                    };

                    $scope.initOrdersCheck = function () {
                        if ($rootScope.hasRole('Administrator')) {
                            $scope.ordersCheckPromise = $interval(function () {
                                $scope.checkForOrders();
                            }, 1 * 60 * 1000);
                        }
                    };

                    $rootScope.$on('app.logout', function (event, args) {
                        cancelOrderCheck();
                    });


                    $scope.setMode = function (mode) {
                        $scope.mode = mode;

                        if (mode == "pending") {
                            $scope.headerText = "PENDING RETURNS";
                        }
                        else if (mode == "approved") {
                            $scope.headerText = "APPROVED RETURNS";
                        }
                    };

                    function cancelOrderCheck() {
                        if ($scope.ordersCheckPromise != null) {
                            $interval.cancel($scope.ordersCheckPromise);
                        }
                    }

                    $rootScope.$on('cassini.session.expired', function (event, args) {
                        cancelOrderCheck();
                    });

                    $scope.checkForOrders = function () {
                        sessionFactory.isSesstionActive().then(
                            function (session) {
                                if (session.active == true) {
                                    $scope.criteria.status = "NEW";

                                    orderFactory.getOrders($scope.criteria, $scope.pageable).then(
                                        function (data) {
                                            $scope.ordersNotification.newOrders = data;
                                            $scope.criteria.status = "APPROVED";
                                            return orderFactory.getOrders($scope.criteria, $scope.pageable);
                                        }
                                    ).then(
                                        function (data) {
                                            $scope.ordersNotification.approvedOrders = data;
                                            $scope.criteria.status = "PROCESSED";
                                            return orderFactory.getOrders($scope.criteria, $scope.pageable);
                                        }
                                    ).then(
                                        function (data) {
                                            $scope.ordersNotification.pendingOrders = data;
                                            $scope.criteria.status = "PARTIALLYSHIPPED";
                                            return orderFactory.getOrders($scope.criteria, $scope.pageable);
                                        }
                                    ).then(
                                        function (data) {
                                            $scope.ordersNotification.partiallyShippedOrders = data;
                                            /*return orderFactory.getLateApproveOrders($scope.pageable);*/
                                        }
                                    );
                                    /*.then(
                                     function (data) {
                                     $scope.ordersNotification.lateApprovedOrders = data;
                                     return orderFactory.getLateProcessedOrders($scope.pageable);
                                     }
                                     ).then(
                                     function (data){
                                     $scope.ordersNotification.lateProcessingOrders = data;
                                     return orderFactory.lateShippedOrders($scope.pageable);
                                     }
                                     ).then(
                                     function(data){
                                     $scope.ordersNotification.lateShippedOrders = data;
                                     }
                                     );*/
                                }
                            }
                        );
                    };

                    $scope.checkForReturns = function () {
                        sessionFactory.isSesstionActive().then(
                            function (session) {
                                if (session.active == true) {
                                    $scope.criteria.status = "PENDING";
                                    returnFactory.getReturns($scope.criteria, $scope.pageable).then(
                                        function (data) {
                                            $scope.returnsNotification.pendingReturns = data;
                                            $scope.criteria.status = "APPROVED";
                                            return returnFactory.getReturns($scope.criteria, $scope.pageable);
                                        }
                                    ).then(
                                        function (data) {
                                            $scope.returnsNotification.approvedReturns = data;
                                            $scope.criteria.status = "REJECTED";
                                            return returnFactory.getReturns($scope.criteria, $scope.pageable);
                                        }
                                    ).then(
                                        function (data) {
                                            $scope.returnsNotification.rejectedReturns = data;
                                        }
                                    );
                                }
                            }
                        );
                    };

                    $rootScope.$on('app.loggedIn', function (data) {
                        $scope.checkForOrders();
                    });

                    $rootScope.$on('app.updateNotification', function (data) {
                        $scope.checkForOrders();
                        $scope.checkForReturns();
                    });

                    $scope.showMore = function (status) {
                        $state.go('app.crm.orders.all', {status: status});
                    };

                    $scope.showMoreReturns = function (status) {
                        $state.go('app.crm.returns.all', {status: status});
                    };

                    $scope.returnsApprove = function () {
                        returnFactory.approveAllNewReturns().then(
                            function (data) {
                                $rootScope.$broadcast('app.updateNotification');
                                if (data.length == 0) {
                                    $rootScope.showSuccessMessage("All PendingReturns are approved!");
                                }
                                else {
                                    $scope.returnsNotification = data;
                                }
                            }
                        )
                    };

                    $scope.approveReturn = function (Returns) {
                        returnFactory.approveReturn(Returns.id).then(
                            function (data) {
                                $rootScope.$broadcast('app.updateNotification');
                                $rootScope.showSuccessMessage("Return approved!")
                            }
                        )
                    };
                    $scope.openReturn = function (Returns) {
                        $state.go('app.crm.returns.details', {returnId: Returns.id})
                    };

                    $scope.cancelReturn = function (Returns) {
                        returnFactory.cancelReturn(Returns.id).then(
                            function (data) {
                                if (data == null) {
                                    $rootScope.$broadcast('app.updateNotification');
                                    $rootScope.showSuccessMessage("Return canceled!")
                                }
                            }
                        )
                    };

                    $rootScope.$broadcast('app.updateNotification');

                    (function () {
                        $scope.checkForOrders();
                        //$scope.initOrdersCheck();
                    })();
                }
            ]
        );
    }
);
