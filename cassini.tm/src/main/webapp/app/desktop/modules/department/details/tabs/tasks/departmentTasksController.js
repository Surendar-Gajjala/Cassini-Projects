define(
    [
        'app/desktop/modules/department/department.module',
        'app/desktop/modules/task/new/newTaskDialogueController',
        'app/shared/services/projectService',
        'app/shared/services/taskService',
        'app/shared/services/shiftService',
        'app/shared/services/departmentService',
        'app/assets/bower_components/cassini-platform/app/desktop/directives/freetext/freeTextDirective',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/commonService'
    ],
    function (module) {
        module.controller('DepartmentTasksController', DepartmentTasksController);

        function DepartmentTasksController($scope, $rootScope, $timeout, $state, $stateParams, $cookies, $uibModal, ProjectService,
                                           TaskService, CommonService, DialogService, DepartmentService, ShiftService) {
            var vm = this;

            vm.deptId = $stateParams.departmentId;
            vm.project = null;
            vm.attributes = [];
            vm.previousPage = previousPage;
            vm.nextPage = nextPage;
            vm.tasks = [];
            vm.loading = true;
            vm.deleteDepartmentTask = deleteDepartmentTask;
            vm.departmentTasks = null;

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

            function getshiftById(task, shiftId) {
                ShiftService.getShiftById(shiftId).then(
                    function (data) {
                        task.shift = data;
                    });
            }


            function loadDepartmentTasks() {
                DepartmentService.getPersonsByDepartment(vm.deptId).then(
                    function (data) {
                        vm.persons = data;
                        vm.ids = [];
                        angular.forEach(vm.persons, function (person) {
                            vm.ids.push(person.person);
                        });
                        TaskService.getDepartmentTasksByPersonIds(vm.ids).then(function (data) {
                            vm.departmentTasks = data;
                            angular.forEach(vm.departmentTasks, function (task) {
                                getshiftById(task, task.shift);
                            });
                            setValues(vm.departmentTasks);
                        });
                    }
                )
                vm.loading = false;
            }

            function freeTextSearch(freeText) {
                if (freeText != null && freeText != undefined && freeText.trim() != "") {
                    vm.filters.searchQuery = freeText;
                    TaskService.freeTextSearch(vm.filters, pageable).then(
                        function (data) {
                            var filterTasks = data.content;
                            vm.departmentTasks = [];
                            angular.forEach(filterTasks, function (task) {
                                angular.forEach(vm.ids, function (id) {
                                    if (task.assignedTo == id) {
                                        vm.departmentTasks.push(task);
                                    }
                                })
                            })
                            angular.forEach(vm.departmentTasks, function (task) {
                                getshiftById(task, task.shift);
                            });
                            setValues(vm.departmentTasks);
                            vm.clear = true;
                        }
                    )
                } else {
                    resetPage();
                    loadDepartmentTasks();
                }
            }

            function nextPage() {
                if (vm.tasks.last != true) {
                    pageable.page++;
                }
            }

            function resetPage() {
                pageable.page = 0;
            }

            function previousPage() {
                if (vm.tasks.first != true) {
                    pageable.page--;

                }
            }

            function setValues(tasks) {
                CommonService.getPersonReferences(tasks, 'approvedBy');
                CommonService.getPersonReferences(tasks, 'verifiedBy');
                CommonService.getPersonReferences(tasks, 'assignedTo');
                angular.forEach(tasks, function (task) {
                    ProjectService.getProjectById(task.project).then(function (data) {
                        task.project = data;
                    })
                })
            }

            function deleteDepartmentTask(task) {
                var options = {
                    title: 'Delete DepartmentTask',
                    message: 'Are you sure you want to delete this DepartmentTask?',
                    okButtonClass: 'btn-danger'
                };
                DialogService.confirm(options, function (yes) {
                    if (yes == true) {
                        TaskService.deleteProjectTask(task.project.id, task.id).then(
                            function (data) {
                                var index = vm.departmentTasks.indexOf(task);
                                vm.departmentTasks.splice(index, 1);
                                $rootScope.showErrorMessage(" Deleted Successfully!");
                                loadDepartmentTasks();
                            }
                        )
                    }
                });
            }

            (function () {
                if ($application.homeLoaded == true) {
                    loadDepartmentTasks();
                    $scope.$on('app.department.tasks.freetextsearch', function (event, data) {
                        freeTextSearch(data);
                    })
                }
            })();
        }
    }
);