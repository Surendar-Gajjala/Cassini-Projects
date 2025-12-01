define(['app/app.modules'],
    function($app) {
        $app.controller('HrmDashboardController',
            [
                '$scope', '$rootScope', '$timeout', '$interval', '$state', '$cookies',

                function($scope, $rootScope, $timeout, $interval, $state, $cookies) {

                    $rootScope.iconClass = "fa fa-tachometer";
                    $rootScope.viewTitle = "HRM Dashboard";
                }
            ]
        );
    }
);