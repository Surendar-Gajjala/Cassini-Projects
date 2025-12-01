define([
        'app/desktop/modules/report/report.module'
    ],
    function(module) {
        module.controller('NewReportController', NewReportController);

        function NewReportController($scope, $rootScope, $timeout, $state, $stateParams, $cookies,$application,CommonService, $uibModalInstance){
            var vm = this;

            (function() {
                if($application.homeLoaded == true) {


                }
            })();
        }
    }
);
