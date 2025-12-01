define(['app/desktop/modules/site/sites.module',
        'app/shared/services/pm/project/bomService',
        'app/shared/services/tm/taskService',
        'app/shared/services/pm/project/projectService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/commonService'

    ],
    function (module) {
        module.controller('SiteResourcesController', SiteResourcesController);

        function SiteResourcesController($scope, $rootScope, $timeout, $state, $stateParams, ProjectService, BomService, CommonService, TaskService) {
            var vm = this;

            vm.resources = [];
            vm.taskList = [];

            function loadTasks() {
                TaskService.getTasksBySite($stateParams.projectId, $stateParams.siteId).then(
                    function (data) {
                        vm.taskList = data;
                        loadResources();
                    }
                )
            }

            function loadResources() {
                var taskIds = [];
                angular.forEach(vm.taskList, function (task) {
                    taskIds.push(task.id);
                });
                TaskService.getProjectResourcesByTasks($stateParams.projectId, taskIds).then(
                    function (data) {
                        vm.resources = data;
                        TaskService.getTaskReferencesByResources($stateParams.projectId, vm.resources, 'task');
                        var items = [];
                        var people = [];
                        var roles = [];
                        angular.forEach(vm.resources, function (resource) {
                            if (resource.resourceType == "MATERIALTYPE" || resource.resourceType == "MACHINETYPE") {
                                items.push(resource);
                            } else if (resource.resourceType == "MANPOWERTYPE") {
                                people.push(resource);
                            } else if (resource.resourceType == "ROLE") {
                                roles.push(resource);
                            }

                        });

                        BomService.getBoqItemReferences($stateParams.projectId, items, 'referenceId');
                        ProjectService.getRoleReferences($stateParams.projectId, roles, 'referenceId');
                        CommonService.getPersonReferences(people, 'referenceId');
                    }
                )
            }

            (function () {
                if ($application.homeLoaded == true) {
                    loadTasks();
                }
            })();
        }
    });
