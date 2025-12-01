define(['app/app.modules'
    ],
    function (app) {
        app.controller('SalesRepPerformanceController',
            [
                '$scope', '$rootScope', '$timeout', '$interval', '$state',

                function ($scope, $rootScope, $timeout, $interval, $state) {
                    $scope.setActiveFlag(2);
                }
            ]
        );
    }
);