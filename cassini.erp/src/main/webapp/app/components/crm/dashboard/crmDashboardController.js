define(['app/app.modules'],
    function($app) {
        $app.controller('CrmDashboardController',
            [
                '$scope', '$rootScope', '$timeout', '$interval', '$state', '$cookies',

                function($scope, $rootScope, $timeout, $interval, $state, $cookies) {

                    $rootScope.iconClass = "fa fa-tachometer";
                    $rootScope.viewTitle = "CRM Dashboard";
                }
            ]
        );
    }
);