define(['app/desktop/modules/reports/reports.module'
    ],
    function (module) {
        module.controller('ReportMainController', ReportMainController);

        function ReportMainController($scope, $rootScope, $timeout, $state, $cookies) {

            var vm = this;

            $rootScope.viewInfo.icon = "fa fa-area-chart";
            $rootScope.viewInfo.title = "Reports";


            (function () {
                if ($application.homeLoaded == true) {

                }
            })();
        }
    }
);