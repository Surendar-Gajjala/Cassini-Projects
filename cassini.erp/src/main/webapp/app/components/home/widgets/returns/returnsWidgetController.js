define(['app/app.modules',
        'app/components/crm/return/returnFactory'
    ],
    function($app) {
        $app.controller('ReturnsWidgetController',
            [
                '$scope', '$rootScope', '$state', 'returnFactory',

                function($scope, $rootScope, $state, returnFactory) {
                    $scope.mode = "pending";
                    $scope.headerText = "PENDING RETURNS";
                    $scope.openReturn = function(Returns) {
                        $state.go('app.crm.returns.details', { orderId: Returns.id })
                    };

                    $scope.setMode = function (mode) {
                        $scope.mode = mode;

                        if (mode == "pending") {
                            $scope.headerText = "PENDING RETURNS";
                        }
                        else if (mode == "approved" ) {
                            $scope.headerText = "APPROVED ORDERS";
                        }
                    };

                    $scope.approveReturn = function(Returns) {
                        returnFactory.approveReturn(Returns.id).then(
                            function(data) {
                                $rootScope.$broadcast('app.updateNotification');
                                $rootScope.showSuccessMessage("Return approved!")
                            }
                        )
                    };

                    $scope.cancelReturn = function(Returns) {
                        returnFactory.cancelReturn(Returns.id).then(
                            function(data) {
                                $rootScope.$broadcast('app.updateNotification');
                                $rootScope.showSuccessMessage("Return canceled!")
                            }
                        )
                    };
                    $scope.approveAll = function() {
                        returnFactory.approveAllNewReturns().then(
                            function(data) {
                                $rootScope.$broadcast('app.updateNotification');
                                if(data.length == 0) {
                                    $rootScope.showSuccessMessage("All pending Returns are approved!")
                                }
                                else {
                                    $scope.returnsNotification = data;
                                    angular.forEach($scope.returnsNotification, function(Returns) {
                                        Returns.lowInventory = true;
                                    });
                                    $rootScope.showErrorMessage("Some returns were not approved");
                                }
                            }
                        )
                    };

                    $scope.showMore = function(status) {
                        $state.go('app.crm.returns.all', {status: status});
                    };

                    $rootScope.$broadcast('app.updateNotification');
                }
            ]
        );
    }
);