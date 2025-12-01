define(['app/desktop/modules/report/report.module'
    ],
    function (module) {
        module.controller('AllReportsController', AllReportsController);

        function AllReportsController($scope, $rootScope, $timeout, $state, $stateParams, $cookies, $application, $uibModal, ShiftService) {

            var vm = this;
            $rootScope.viewInfo.icon = "fa flaticon-deadlines";
            $rootScope.viewInfo.title = "Reports";



            (function () {
                if ($application.homeLoaded == true) {
                }
            })();
        }
    }
);