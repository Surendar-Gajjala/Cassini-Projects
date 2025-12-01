define(['app/desktop/modules/col/col.module'
    ],
    function (module) {
        module.controller('ReportingMainController', ReportingMainController);

        function ReportingMainController($scope, $rootScope, $timeout, $state, $cookies) {
            var vm = this;

            $rootScope.viewInfo.icon = "fa flaticon-chart44";
            $rootScope.viewInfo.title = "Reporting";

            (function () {
                if ($application.homeLoaded == true) {

                }
            })();
        }
    }
);