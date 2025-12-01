define(['app/desktop/modules/report/report.module',
        'app/shared/services/app/application'
    ],
    function(module) {
        module.controller('ReportMainController', ReportMainController);

        function ReportMainController($scope, $rootScope, $timeout, $state, $stateParams, $cookies,$application) {


            var vm = this;
            $rootScope.viewInfo.title = "Reports";


            (function() {
                if($application.homeLoaded == false) {

                }
            })();
        }
    }
);
