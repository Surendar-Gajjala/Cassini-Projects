define(['app/app.modules'
    ],
    function($app) {
        $app.controller('OrderController',
            [
                '$scope', '$rootScope', '$timeout', '$interval', '$state', '$cookies',

                function($scope, $rootScope, $timeout, $interval, $state, $cookies,
                         orderFactory, ERPLists) {

                    $rootScope.iconClass = "fa flaticon-chart44";
                    $rootScope.viewTitle = "Orders";


                }
            ]
        );
    }
);