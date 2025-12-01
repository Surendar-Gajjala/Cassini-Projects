define(['app/app.modules'
    ],
    function ($app) {
        $app.controller('WarehouseManagerHomeController',
            [
                '$scope', '$rootScope', '$timeout', '$interval', '$state', '$cookies',

                function ($scope, $rootScope, $timeout, $interval, $state, $cookies) {

                    if($rootScope.hasRole('Warehouse Manager')) {
                        $state.go('app.prod.inventory.products');
                    }

                }
            ]
        );
    }
);