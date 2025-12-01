define(['app/app.modules'
    ],
    function ($app) {
        $app.controller('HelpController',
            [
                '$scope', '$rootScope', '$timeout', '$interval', '$state', '$cookies',

                function ($scope, $rootScope, $timeout, $interval, $state, $cookies) {

                    $rootScope.iconClass = "fa flaticon-searching1";
                    $rootScope.viewTitle = "Help";

                }
            ]
        );
    }
);