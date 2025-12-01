define(
    [
        'app/desktop/modules/pm/project/task/task.module',
        'app/desktop/modules/directives/deliverablesDirective'
    ],
    function (module) {
        module.controller('TaskDeliverableController', TaskDeliverableController);

        function TaskDeliverableController($scope, $stateParams) {

            var vm = this;
            vm.activityId = $stateParams.activityId;
            vm.taskId = $stateParams.taskId;


            (function () {
                $scope.$on('app.activity.tasks.tabActivated', function (event, data) {
                    if (data.tabId == 'details.deliverables') {
                        $scope.$broadcast('app.object.deliverables', {});
                    }
                })
            })();
        }
    }
);