define(['app/desktop/modules/report/report.module'
    ],
    function(module) {
        module.controller('ReportDetailsController', ReportDetailsController);

        function ReportDetailsController($scope, $rootScope, $timeout, $state, $stateParams, $cookies,$application) {
            var vm = this;



            (function() {
                if($application.homeLoaded == true) {

                }
            })();
        }
    }
);