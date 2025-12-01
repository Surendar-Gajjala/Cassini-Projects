define(['app/desktop/modules/tm/tm.module',
        'app/shared/services/tm/taskService',
        'app/shared/services/core/itemService',
        'app/shared/services/pm/project/projectService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/commonService'
    ],
    function (module) {
        module.controller('TaskResourcesCompletedController', TaskResourcesCompletedController);

        function TaskResourcesCompletedController($scope, $rootScope, $timeout, $state, $stateParams, $cookies, $window, CommonService, ItemService, ProjectService, TaskService, DialogService) {
            var vm = this;

            vm.taskId = $stateParams.taskId;
            vm.projectId = $stateParams.projectId;
            vm.taskHistory = $scope.data.taskHistory;

            vm.taskCompletionResources = [];
            function loadTaskCompletionResources() {
                TaskService.getTaskCompletionResources(vm.taskId, vm.taskHistory).then(
                    function (data) {
                        if (data.length > 0) {
                            vm.taskCompletionResources = data;
                            CommonService.getPersonReferences(vm.taskCompletionResources, 'resourceId');
                            ItemService.getItemReferences(vm.taskCompletionResources, 'resourceId');
                            ItemService.getMachineItemReferences(vm.taskCompletionResources, 'resourceId');
                            ProjectService.getRoleReferences($rootScope.task.project, vm.taskCompletionResources, 'resourceId');
                        }

                    }
                )
            }

            (function () {
                if ($application.homeLoaded == true) {
                    loadTaskCompletionResources();
                }
            })();
        }
    }
)
;
