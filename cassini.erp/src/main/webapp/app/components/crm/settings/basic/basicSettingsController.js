define(['app/app.modules',
        'app/components/crm/settings/basic/customerTypesController',
        'app/components/crm/settings/basic/shippersController',
        'app/components/crm/settings/basic/vehiclesController',
        'app/components/crm/salesregion/salesRegionsController',
        'app/shared/directives/commonDirectives'
    ],
    function($app) {
        $app.controller('BasicSettingsController',
            [
                '$scope', '$rootScope', '$timeout', '$interval', '$state', '$cookies',

                function($scope, $rootScope, $timeout, $interval, $state, $cookies) {
                    $scope.templates = {
                        customerTypes: 'app/components/crm/settings/basic/customerTypesView.jsp',
                        shippers: 'app/components/crm/settings/basic/shippersView.jsp'
                    };
                }
            ]
        );
    }
);