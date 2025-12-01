define(['app/app.modules'
    ],
    function($app) {
        $app.controller('ReturnsController',
            [
                '$scope', '$rootScope', '$timeout', '$interval', '$state', '$cookies',

                function($scope, $rootScope, $timeout, $interval, $state, $cookies) {
                    $rootScope.iconClass = "fa flaticon-refresh2";
                    $rootScope.viewTitle = "Returns";
                }
            ]
        );
    }
);