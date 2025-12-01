define(['app/app.modules'
    ],
    function ($app) {
        $app.controller('ShippingManagerHomeController',
            [
                '$scope', '$rootScope', '$timeout', '$interval', '$state', '$cookies',

                function ($scope, $rootScope, $timeout, $interval, $state, $cookies) {

                    if($rootScope.hasRole('Shipping Manager')) {
                        $state.go('app.crm.shipping');
                    }
                }
            ]
        );
    }
);