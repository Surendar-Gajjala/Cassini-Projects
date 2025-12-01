define(['app/app.modules',
        'app/components/crm/order/orderFactory',
        'app/components/prod/product/productFactory',
        'app/components/crm/order/shipment/shipmentController',
        'app/components/crm/order/shipment/shipmentFactory'
    ],
    function ($app) {
        $app.controller('OrderController',
            [
                '$scope', '$rootScope', '$timeout', '$interval', '$state', '$stateParams', '$cookies',
                'orderFactory', 'productFactory', 'shipmentFactory',

                function ($scope, $rootScope, $timeout, $interval, $state, $stateParams, $cookies,
                          orderFactory, productFactory, shipmentFactory) {

                    $rootScope.viewName = "Order Details";
                    $rootScope.backgroundColor = "#00bcd4";
                    $scope.order = null;
                    $scope.invoices = [];
                    $scope.loading = true;

                    $scope.loadOrder = function() {
                        orderFactory.getOrder($stateParams.orderId).then (
                            function(data) {
                                $scope.order = data;
                                $rootScope.viewName = "Order Details (" + data.orderNumber + ")";
                                return orderFactory.getOrderItems($stateParams.orderId);
                            }
                        ).then (
                            function(data) {
                                $scope.order.details = data;
                                $scope.loading = false;
                                return orderFactory.getOrderHistory($stateParams.orderId);
                            }
                        ).then (
                            function(data) {
                                $scope.order.history = data;

                                return shipmentFactory.getOrderShipments($stateParams.orderId);
                            }
                        ).then (
                            function(data) {
                                $scope.invoices = data;
                            }
                        );
                    };



                    $scope.removeHold = function() {
                        $scope.order.onhold = false;
                        orderFactory.updateOrder($scope.order).then (
                            function(data) {

                            }
                        );
                    };

                    $scope.search = function() {

                    };

                    (function() {
                        $scope.loadOrder();
                    })();

                }
            ]
        );
    }
);