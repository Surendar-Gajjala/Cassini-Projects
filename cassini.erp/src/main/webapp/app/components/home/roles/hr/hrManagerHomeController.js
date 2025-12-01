define(['app/app.modules'
    ],
    function ($app) {
        $app.controller('HrManagerHomeController',
            [
                '$scope', '$rootScope', '$timeout', '$interval', '$state', '$cookies',

                function ($scope, $rootScope, $timeout, $interval, $state, $cookies) {
                    if($rootScope.hasRole('HR Manager')) {
                        $state.go('app.hrm.employees');
                    }

                }
            ]
        );
    }
);