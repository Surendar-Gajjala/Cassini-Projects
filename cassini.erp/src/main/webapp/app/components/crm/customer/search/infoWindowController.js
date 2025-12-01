define(['app/app.modules',
        'app/components/crm/customer/customerFactory',
        'app/shared/directives/commonDirectives'
    ],
    function($app) {
        $app.controller('CustomerInfoWindowController',
            [
                '$scope', '$rootScope', '$timeout', '$interval', '$state', '$stateParams', '$cookies',
                'customerFactory', 'commonFactory',

                function($scope, $rootScope, $timeout, $interval, $state, $stateParams, $cookies,
                         customerFactory, commonFactory) {
                    $scope.customer = null;

                    $scope.loadCustomerInfo = function() {
                        customerFactory.getCustomer($scope.selectedMarker.key).then(
                            function(data) {
                                $scope.customer = data;
                                $timeout(function() {
                                    $scope.showInfoWindow();
                                }, 200);
                            }
                        )
                    };

                    (function() {
                        $scope.loadCustomerInfo();
                    })();
                }
            ]
        );
    }
);