define(['app/app.modules'],
    function($app) {
        $app.controller('AdminController',
            [
                '$scope', '$rootScope', '$timeout', '$interval', '$state', '$cookies',

                function($scope, $rootScope, $timeout, $interval, $state, $cookies) {

                    $rootScope.iconClass = "fa fa-database";
                    $rootScope.viewTitle = "Admin";
                }
            ]
        );
    }
);