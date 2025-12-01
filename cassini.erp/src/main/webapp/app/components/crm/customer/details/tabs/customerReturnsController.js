define(['app/app.modules', 'app/components/crm/customer/customerFactory',
        'app/components/crm/return/returnFactory'],
    function ($app) {
        $app.controller('CustomerReturnsController',
            [
                '$scope', '$rootScope', '$timeout', '$interval', '$state', '$stateParams', '$cookies',
                'customerFactory', 'returnFactory',

                function ($scope, $rootScope, $timeout, $interval, $state, $stateParams, $cookies,
                          customerFactory, returnFactory) {

                    $scope.$parent.returnsScope = $scope;
                    $scope.returns = [];
                    $scope.lastSelectedReturn = null;

                    $scope.toggleDetails = function (rtn) {
                        if ($scope.lastSelectedReturn != null && $scope.lastSelectedReturn != rtn) {
                            $scope.lastSelectedReturn.showDetails = false;
                        }

                        rtn.showDetails = !rtn.showDetails;
                        $scope.lastSelectedReturn = rtn;


                        if (rtn.showDetails == true) {
                            loadReturnDetails(rtn);
                        }
                    };
                    function loadReturnDetails(rtn) {
                        returnFactory.getReturnItems(rtn.id).then(
                            function (data) {
                                rtn.details = data;
                            }
                        )
                    }

                    $scope.getCustomerReturns = function () {
                        customerFactory.getCustomerReturns($scope.customer.id).then(
                            function (data) {
                                $scope.returns = data;
                                angular.forEach($scope.returns, function (rtn) {
                                    rtn.showDetails = false;
                                });
                            }
                        )
                    };

                    $scope.showReturn = function (returns) {
                        $state.go('app.crm.returns.details', {returnId: returns.id})
                    };
                    $scope.$parent.$on("customerLoaded", function () {
                        $scope.getCustomerReturns();
                    });

                    (function () {
                        $rootScope.$on('customerLoaded', function() {
                            $scope.getCustomerReturns();
                        });
                    })();
                }
            ]
        );
    }
);