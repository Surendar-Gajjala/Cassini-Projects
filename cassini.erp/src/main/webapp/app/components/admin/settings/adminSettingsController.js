define(['app/app.modules'],
    function($app) {
        $app.controller('AdminSettingsController',
            [
                '$scope', '$rootScope', '$timeout', '$interval', '$state', '$cookies',

                function($scope, $rootScope, $timeout, $interval, $state, $cookies) {

                    $rootScope.iconClass = "fa fa-wrench";
                    $rootScope.viewTitle = "Admin Settings";
                }
            ]
        );
    }
);