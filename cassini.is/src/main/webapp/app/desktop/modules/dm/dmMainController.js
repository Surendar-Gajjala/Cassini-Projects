define(['app/desktop/modules/dm/dm.module'
    ],
    function (module) {
        module.controller('DmMainController', DmMainController);

        function DmMainController($scope, $rootScope, $timeout, $state, $cookies) {

            $rootScope.viewInfo.icon = "fa fa-files-o";
            $rootScope.viewInfo.title = "Documents";
            var vm = this;

            (function () {
                if ($application.homeLoaded == true) {

                }
            })();
        }
    }
);