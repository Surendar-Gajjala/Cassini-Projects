define(['app/desktop/modules/tm/tm.module'
    ],
    function (module) {
        module.controller('TmMainController', TmMainController);

        function TmMainController($scope, $rootScope, $timeout, $state, $cookies) {
            var vm = this;

            $rootScope.viewInfo.icon = "fa flaticon-deadlines";
            $rootScope.viewInfo.title = "Tasks";

            (function () {
                if ($application.homeLoaded == true) {

                }
            })();
        }
    }
);