define(['app/desktop/modules/proc/proc.module'
    ],
    function (module) {
        module.controller('ProcMainController', ProcMainController);

        function ProcMainController($scope, $rootScope, $timeout, $state, $cookies) {
            var vm = this;

            $rootScope.viewInfo.icon = "flaticon-agreement";
            $rootScope.viewInfo.title = "Procurement";

            (function () {
                if ($application.homeLoaded == true) {

                }
            })();
        }
    }
);