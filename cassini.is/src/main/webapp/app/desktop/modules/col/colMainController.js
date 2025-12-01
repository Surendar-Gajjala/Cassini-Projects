define(['app/desktop/modules/col/col.module'
    ],
    function (module) {
        module.controller('ProcMainController', ProcMainController);

        function ProcMainController($scope, $rootScope, $timeout, $state, $cookies) {
            var vm = this;

            $rootScope.viewInfo.icon = "fa fa-comments";
            $rootScope.viewInfo.title = "Communication";

            (function () {
                if ($application.homeLoaded == true) {

                }
            })();
        }
    }
);