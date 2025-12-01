/**
 * Created by annap on 22/07/2016.
 */
define(
    [
        'app/desktop/modules/project/project.module',
        'app/desktop/modules/task/new/newTaskDialogueController',
        'app/shared/services/projectService',
        'app/shared/services/taskService',
        'app/assets/bower_components/cassini-platform/app/desktop/directives/freetext/freeTextDirective',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/commonService'
    ],
    function (module) {
        module.controller('ProjectTasksController', ProjectTasksController);

        function ProjectTasksController($scope, $rootScope, $timeout, $state, $stateParams, $cookies, $uibModal, ProjectService,
                                        TaskService, CommonService, DialogService) {
            var vm = this;

            vm.projectId = $stateParams.projectId;
            vm.project = null;
            vm.attributes = [];
            vm.deleteTask = deleteTask;
            vm.previousPage = previousPage;
            vm.nextPage = nextPage;
            vm.tasks = [];
            vm.loading = true;

            var pageable = {
                page: 0,
                size: 20,
                sort: {
                    field: "modifiedDate",
                }
            };

            vm.filters = {
                project: vm.projectId,
                name: null,
                description: null,
                status: null,
                assignedTo: null,
                verifiedBy: null,
                approvedBy: null,
                searchQuery: null,
                location: null
            };



            function nextPage() {
                if (vm.tasks.last != true) {
                    pageable.page++;
                    loadProjectTasks();
                }
            }

            function resetPage() {
                pageable.page = 0;
            }

            function freeTextSearch(freeText) {
                if (freeText != null && freeText != undefined && freeText.trim() != "") {
                    vm.filters.searchQuery = freeText;
                    TaskService.freeTextSearch(vm.filters, pageable, freeText).then(
                        function (data) {
                            vm.tasks = data;
                            setValues();
                            vm.clear = true;
                        }
                    )
                } else {
                    resetPage();
                    loadProjectTasks();
                }
            }

            function previousPage() {
                if (vm.tasks.first != true) {
                    pageable.page--;
                    loadProjectTasks();
                }
            }

            function getProjectById() {
                    ProjectService.getProjectById(vm.projectId).then(
                        function(data){
                            vm.project = data;
                        })
                }

            function loadProjectTasks() {
                TaskService.getAllTasks(pageable, vm.filters).then(
                    function (data) {
                        vm.tasks = data;
                        setValues();
                    });
                vm.loading = false;
            }

            function setValues() {
                CommonService.getPersonReferences(vm.tasks.content, 'approvedBy');
                CommonService.getPersonReferences(vm.tasks.content, 'verifiedBy');
                CommonService.getPersonReferences(vm.tasks.content, 'assignedTo');
                angular.forEach(vm.tasks.content, function (task) {
                    ProjectService.getProjectById(task.project).then(function (data) {
                        task.project = data;
                    })
                })
            }

            function deleteTask(task) {
                var options = {
                    title: 'Delete Task',
                    message: 'Are you sure you want to delete this task?',
                    okButtonClass: 'btn-danger'
                };
                DialogService.confirm(options, function (yes) {
                    if (yes == true) {
                        TaskService.deleteProjectTask(task.project.id, task.id).then(
                            function (data) {
                                var index = vm.tasks.content.indexOf(task);
                                vm.tasks.content.splice(index, 1);
                                $rootScope.showErrorMessage("Deleted Successfully!");
                                loadProjectTasks();
                            }
                        )
                    }
                });
            }

            function addCallbacks() {
                $scope.$on('app.project.resettasks',  function() {
                    resetPage();
                });
                $scope.$on('app.project.freetextsearch',  function(event, data) {
                    freeTextSearch(data.searchString);
                });
            }

            (function () {
                if ($application.homeLoaded == true) {
                    getProjectById();
                    loadProjectTasks();
                    addCallbacks();
                    $rootScope.$on('app.task.addTask',  function() {
                        var modalInstance = $uibModal.open({
                            animation: true,
                            templateUrl: 'app/desktop/modules/task/new/newTaskDialogueView.jsp',
                            controller: 'NewTaskDialogueController as newTaskDialogueVm',
                            size: 'md',
                            resolve: {
                                project: function () {
                                    return vm.project;
                                }
                            }
                        }
                        );
                        modalInstance.result.then(
                            function (result) {
                                TaskService.createProjectTask(result.project, result).then(
                                    function (data) {
                                        $rootScope.showSuccessMessage("Task Created Successfully ");
                                        loadProjectTasks();
                                    }
                                );
                            }
                        )
                    })

                }
            })();
        }
    }
);