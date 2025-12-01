define(['app/app.modules'],
    function($app) {
        $app.controller('CRMSettingsController',
            [
                '$scope', '$rootScope', '$timeout', '$interval', '$state', '$cookies',

                function($scope, $rootScope, $timeout, $interval, $state, $cookies) {

                    $rootScope.iconClass = "fa fa-wrench";
                    $rootScope.viewTitle = "CRM Settings";
                }
            ]
        );
    }
);