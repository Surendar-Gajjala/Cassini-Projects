define(['app/app.modules'
    ],
    function ($app) {
        $app.controller('SalesRepHomeController',
            [
                '$scope', '$rootScope', '$timeout', '$interval', '$state', '$cookies',

                function ($scope, $rootScope, $timeout, $interval, $state, $cookies) {
                    if($rootScope.hasRole('Sales Executive')) {
                        $state.go('app.crm.salesrep', {salesRepId: $app.login.person.id});
                    }

                }
            ]
        );
    }
);