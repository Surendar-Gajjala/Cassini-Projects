define(['app/desktop/modules/pm/pm.module'
    ],
    function (module) {
        module.controller('PmMainController', PmMainController);

        function PmMainController($scope, $rootScope, $timeout, $state, $stateParams, $cookies) {
            $rootScope.setViewType('PROJECT');

            (function () {
                if ($application.homeLoaded == true) {

                }
            })();
        }
    }
);