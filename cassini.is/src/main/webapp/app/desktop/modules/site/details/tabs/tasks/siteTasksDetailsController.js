define(['app/desktop/modules/site/sites.module',
        'app/shared/services/tm/taskService',
        'app/shared/services/pm/project/wbsService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/commonService'

    ],
    function (module) {
        module.controller('SiteTasksDetailsController', SiteTasksDetailsController);

        function SiteTasksDetailsController($scope, $rootScope, CommonService, TaskService, $timeout, WbsService, $state, $stateParams) {

            var vm = this;

            vm.loading = true;
            vm.site = null;
            vm.siteId = $stateParams.siteId;
            vm.project = $stateParams.projectId;
            vm.taskList = [];

            function loadTasks() {
                TaskService.getTasksBySite(vm.project, vm.siteId).then(
                    function (data) {
                        vm.taskList = data;
                        CommonService.getPersonReferences(vm.taskList, 'person');
                        /*WbsService.getMultipleWbsWithTasks($stateParams.projectId, vm.taskList, 'wbsItem');*/

                    }
                )
            }

            (function () {
                if ($application.homeLoaded == true) {
                    loadTasks();
                }
            })();
        }
    }
);