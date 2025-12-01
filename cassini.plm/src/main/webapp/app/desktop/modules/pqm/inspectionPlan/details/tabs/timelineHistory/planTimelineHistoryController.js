define(
    [
        'app/desktop/modules/pqm/pqm.module',
        'app/desktop/modules/directives/timelineDirective'
    ],
    function (module) {
        module.controller('PlanTimelineHistoryController', PlanTimelineHistoryController);

        function PlanTimelineHistoryController($scope, $rootScope, $timeout, $application, $sce, $state, $stateParams) {
            var vm = this;

            vm.loading = true;
            vm.planId = $stateParams.planId;

            (function () {
                $scope.$on('app.inspectionPlan.tabActivated', function (event, data) {

                    if (data.tabId == 'details.timelineHistory') {
                        $scope.$broadcast('app.object.timeline', {});
                    }

                });
            })();
        }
    }
);