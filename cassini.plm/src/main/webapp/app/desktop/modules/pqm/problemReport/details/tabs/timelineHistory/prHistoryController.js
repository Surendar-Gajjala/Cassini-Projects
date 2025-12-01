define(
    [
        'app/desktop/modules/pqm/pqm.module',
        'app/desktop/modules/directives/timelineDirective'
    ],
    function (module) {
        module.controller('PrHistoryController', PrHistoryController);

        function PrHistoryController($scope, $rootScope, $timeout, $application, $sce, $state, $stateParams) {
            var vm = this;

            vm.loading = true;
            vm.problemReportId = $stateParams.problemReportId;

            (function () {
                $scope.$on('app.problemReport.tabActivated', function (event, data) {

                    if (data.tabId == 'details.timelineHistory') {
                        $scope.$broadcast('app.object.timeline', {});
                    }

                });
            })();
        }
    }
);