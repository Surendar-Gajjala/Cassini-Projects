define(
    [
        'app/desktop/modules/pm/project/activity/activity.module',
        'app/desktop/modules/directives/deliverablesDirective'
    ],
    function (module) {
        module.controller('ActivityDeliverableController', ActivityDeliverableController);

        function ActivityDeliverableController($scope, $stateParams) {

            var vm = this;
            vm.activityId = $stateParams.activityId;

            (function () {
                $scope.$on('app.activity.tabActivated', function (event, data) {
                    if (data.tabId == 'details.deliverables') {
                        $scope.$broadcast('app.object.deliverables', {});
                    }
                })
            })();
        }
    }
);