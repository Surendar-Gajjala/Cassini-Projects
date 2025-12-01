define(
    [
        'app/desktop/modules/pm/pm.module',
        'app/desktop/modules/directives/referenceItemsDirectiveController'
    ],

    function (module) {
        module.controller('TaskReferenceItemsController', TaskReferenceItemsController);

        function TaskReferenceItemsController($scope, $stateParams) {

            var vm = this;
            vm.activityId = $stateParams.activityId;
            vm.taskId = $stateParams.taskId;

            (function () {
                $scope.$on('app.activity.tasks.tabActivated', function (event, data) {
                    if (data.tabId == 'details.itemReferences') {
                        $scope.$broadcast('app.project.referenceItems', {});
                    }
                })
            })();
        }
    }
);