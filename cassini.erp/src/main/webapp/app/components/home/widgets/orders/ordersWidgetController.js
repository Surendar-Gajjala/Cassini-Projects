define(['app/app.modules',
        'app/components/crm/order/orderFactory'
    ],
    function ($app) {
        $app.controller('OrdersWidgetController',
            [
                '$scope', '$rootScope', '$state', 'orderFactory',

                function ($scope, $rootScope, $state, orderFactory) {
                    $scope.mode = "new";
                    $scope.headerText = "NEW ORDERS";
                    $rootScope.approvingOrder = false;

                    $scope.setMode = function (mode) {
                        $scope.mode = mode;

                        if (mode == "new") {
                            $scope.headerText = "NEW ORDERS";
                        }
                        else if (mode == "approved") {
                            $scope.headerText = "APPROVED ORDERS";
                        }
                        else if (mode == "pending") {
                            $scope.headerText = "PENDING ORDERS";
                        }
                    };

                    $scope.openOrder = function (order) {
                        $state.go('app.crm.orders.details', {orderId: order.id})
                    };


                    $scope.approveOrder = function (order) {
                        orderFactory.approveOrder(order.id).then(
                            function (data) {
                                $rootScope.$broadcast('app.updateNotification');
                                $rootScope.showSuccessMessage("Order approved!")
                                $rootScope.approvingOrder = true;
                            }
                        )
                    };

                    $scope.cancelOrder = function (order) {
                        orderFactory.cancelOrder(order.id).then(
                            function (data) {
                                $rootScope.$broadcast('app.updateNotification');
                                $rootScope.showSuccessMessage("Order canceled!")
                            }
                        )
                    };

                    $scope.approveAll = function () {
                        orderFactory.approveAllNewOrders().then(
                            function (data) {
                                $rootScope.$broadcast('app.updateNotification');
                                if (data.length == 0) {
                                    $rootScope.showSuccessMessage("All new orders are approved!")
                                }
                                else {
                                    $scope.ordersNotification = data;
                                    angular.forEach($scope.ordersNotification, function (order) {
                                        order.lowInventory = true;
                                    });
                                    $rootScope.showErrorMessage("Some orders were not approved due to low inventory");
                                }
                            }
                        )
                    };

                    $scope.showMore = function (status) {
                        $state.go('app.crm.orders.all', {status: status});
                    };

                    $rootScope.$broadcast('app.updateNotification');
                }
            ]
        );
    }
);