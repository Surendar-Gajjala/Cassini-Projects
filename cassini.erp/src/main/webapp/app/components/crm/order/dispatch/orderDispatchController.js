define(['app/app.modules',
        'app/shared/directives/authorizationDirective',
        'app/components/crm/order/orderFactory'
    ],
    function($app) {
        $app.controller('OrderDispatchController',
            [
                '$scope', '$rootScope', '$timeout', '$interval', '$state', '$stateParams', '$cookies', 'orderFactory',

                function($scope, $rootScope, $timeout, $interval, $state, $stateParams, $cookies,
                         orderFactory) {

                    $rootScope.iconClass = "fa flaticon-global42";
                    $rootScope.viewTitle = "Order Dispatch";

                    $scope.authorizationFactory = $app.authorizationFactory;

                    $scope.order = {};
                    $scope.orderHistory = [];
                    $scope.loading = true;
                    $scope.dispatched = false;

                    $scope.dispatchFull = function() {
                        orderFactory.dispatchOrder($scope.order.id).then (
                            function(data) {
                                $scope.order = data;
                                $rootScope.showSuccessMessage("This order is now complete!");
                                $scope.dispatched = true;
                                loadOrderHistory();
                            },
                            function(error) {
                                $rootScope.showErrorMessage(error.message);
                            }
                        );
                    };

                    $scope.dispatchPartial = function() {

                    };

                    function loadOrder() {
                        orderFactory.getOrder($stateParams.orderId).then (
                            function(data) {
                                $scope.order = data;
                                $rootScope.viewTitle = "Order Dispatch (" + data.orderNumber + ")";
                                $scope.loading = false;

                                if($scope.order.status != 'PROCESSED') {
                                    $rootScope.showErrorMessage("This order hasn't been processed. You cannot dispatch this order!");
                                    $scope.dispatched = false;
                                }
                                else if($scope.order.status == 'SHIPPED') {
                                    $rootScope.showWarningMessage("This order has been dispatched!");
                                    $scope.dispatched = true;
                                }
                            }
                        );
                    }

                    function loadOrderHistory() {
                        orderFactory.getOrderHistory($stateParams.orderId).then (
                            function(data) {
                                $scope.orderHistory = data;
                            }
                        )
                    }
                    (function() {
                        loadOrder();
                        loadOrderHistory();
                    })();
                }
            ]
        );
    }
);