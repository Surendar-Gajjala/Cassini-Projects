define(
    [
        'app/app.modules',
        'app/shared/directives/commonDirectives',
        'app/components/crm/customer/customerFactory'
    ],
    function (module) {
        module.controller('CustomerController', CustomerController);

        function CustomerController($scope, $rootScope, $timeout, $state, $stateParams, $cookies,
                        customerFactory) {
            $rootScope.viewName = "Customer Details";
            $rootScope.backgroundColor = "#ff1744";

            $scope.customerId = $stateParams.customerId;
            $scope.customer = null;
            $scope.orders = [];
            $scope.sampleOrdersAmount = 0;
            $scope.productOrdersAmount = 0;
            $scope.loading = true;


            function loadCustomer() {
                customerFactory.getCustomer($scope.customerId).then (
                    function(data) {
                        $scope.customer = data;
                        loadCustomerOrders();
                    }
                )
            }

            function loadCustomerOrders() {
                customerFactory.getCustomerOrders($scope.customer.id).then(
                    function (data) {
                        $scope.orders = data;
                        $scope.sampleOrdersAmount = 0;
                        $scope.productOrdersAmount = 0;
                        angular.forEach($scope.orders, function (order) {
                            order.showDetails = false;
                            order.orderHistory = [];
                            angular.forEach(order.shipments, function (shipment) {
                                if (order.orderType == 'SAMPLE') {
                                    $scope.sampleOrdersAmount += shipment.invoiceAmount;
                                }
                                else {
                                    $scope.productOrdersAmount += shipment.invoiceAmount;
                                }
                            });
                        });

                        $scope.loading = false;
                    }
                );
            }


            (function () {
                loadCustomer();
            })();
        }
    }
);