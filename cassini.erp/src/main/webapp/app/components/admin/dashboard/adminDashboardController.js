define(['app/app.modules'],
    function($app) {
        $app.controller('AdminDashboardController',
            [
                '$scope', '$rootScope', '$timeout', '$interval', '$state', '$cookies',

                function($scope, $rootScope, $timeout, $interval, $state, $cookies) {

                    $rootScope.iconClass = "fa fa-tachometer";
                    $rootScope.viewTitle = "Admin Dashboard";
                }
            ]
        );
    }
);