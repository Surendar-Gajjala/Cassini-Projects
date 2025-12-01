define(['app/desktop/modules/pm/pm.module'
    ],
    function (module) {
        module.controller('AdminMainController', AdminMainController);

        function AdminMainController($scope, $rootScope, $timeout, $state, $stateParams, $cookies) {

            (function () {
                if ($application.homeLoaded == true) {

                }
            })();
        }
    }
);