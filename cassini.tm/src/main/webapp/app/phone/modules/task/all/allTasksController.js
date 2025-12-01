define(
    [
        'app/phone/modules/task/task.module',
        'app/shared/services/taskService',
        'app/shared/services/projectService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/commonService'
    ],
    function(module) {
        module.controller('AllTasksController', AllTasksController);

        function AllTasksController($scope, $rootScope, $timeout, $state, $stateParams, $cookies, $application,
                                    TaskService, ProjectService, CommonService) {

            if($application.homeLoaded == false) {
                return;
            }

            $rootScope.viewName = "Tasks";
            var vm = this;


            vm.loading = true;
            vm.tasks = [];

            vm.loadTasks = loadTasks;
            vm.showTaskDetails = showTaskDetails;

            var pageable = {
                page: 0,
                size: 10000,
                sort: {
                    field: "modifiedDate"
                }
            };

            vm.filters = {
                project: null,
                name: null,
                description: null,
                status: null,
                assignedTo: null,
                projectObject: null,
                assignedToObject: null,
                verifiedByObject: null,
                approvedByObject: null,
                shiftObject: null,
                assignedDate: null,
                verifiedBy: null,
                approvedBy: null,
                searchQuery: null,
                workLocation: null,
                shift: null
            };


            function loadTasks() {
                vm.loading = true;
                vm.tasks = [];

                TaskService.getAllTasks(pageable, vm.filters).then(
                    function (data) {
                        vm.tasks = data.content;
                        CommonService.getPersonReferences(vm.tasks, 'approvedBy');
                        CommonService.getPersonReferences(vm.tasks, 'verifiedBy');
                        CommonService.getPersonReferences(vm.tasks, 'assignedTo');

                        angular.forEach(vm.tasks.content, function (task) {
                            ProjectService.getProjectById(task.project).then(function (data) {
                                task.project = data;
                            })
                        });

                        vm.loading = false;
                    }
                );
            }

            function showTaskDetails(task) {
                $state.go('app.task.details', {taskId: task.id, projectId: task.project});
            }


            (function() {
                loadTasks();
            })();
        }
    }
);