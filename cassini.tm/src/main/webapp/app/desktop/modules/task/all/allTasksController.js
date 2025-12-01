define(['app/desktop/modules/task/task.module',
        'app/desktop/modules/task/new/newTaskDialogueController',
        'app/desktop/modules/task/print/printDialogueController',
        'app/shared/services/taskService',
        'app/shared/services/projectService',
        'app/shared/services/shiftService',
        'app/shared/services/personService',
        'app/assets/bower_components/cassini-platform/app/desktop/directives/freetext/freeTextDirective',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/commonService'

    ],
    function (module) {
        module.controller('AllTasksController', AllTasksController);

        function AllTasksController($scope, $rootScope, $timeout, $state, $stateParams, $cookies, $uibModal, TaskService, ProjectService, ShiftService,
                                    PersonService, CommonService, DialogService) {

            var vm = this;
            var shift = null;
            vm.clear = false;

            $rootScope.viewInfo.title = "Tasks";

            var pageable = {
                page: 0,
                size: 20,
                sort: {
                    field: "modifiedDate",
                }
            };

            vm.emptyFilters = {
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

            vm.filters = angular.copy(vm.emptyFilters);
            vm.sortValues = sortValues;

            if ($stateParams.mode != undefined && $stateParams.mode != "") {
                vm.filters.status = $stateParams.mode;
            }

            var pagedResults = {
                content: [],
                last: true,
                totalPages: 0,
                totalElements: 0,
                size: pageable.size,
                number: 0,
                sort: null,
                first: true,
                numberOfElements: 0
            };

            vm.newTask = newTask;
            vm.print = print;
            vm.nextPage = nextPage;
            vm.previousPage = previousPage;
            vm.project = null;
            vm.tasks = [];
            vm.projects = [];
            vm.deleteTask = deleteTask;
            vm.freeTextSearch = freeTextSearch;
            vm.loadProjects = loadProjects;
            vm.loading = true;
            vm.loadProjectTasks = loadProjectTasks;
            vm.resetFilters = resetFilters;
            vm.applyFilters = applyFilters;
            vm.clearFilter = clearFilter;
            vm.shifts = null;
            vm.locations = null;
            vm.listStatus = [
                'ASSIGNED',
                'PENDING',
                'FINISHED',
                'VERIFIED',
                'APPROVED',
                'REJECTED'
            ];
            vm.staffs = [];
            vm.supervisors = [];
            vm.officers = [];

            vm.tasks = angular.copy(pagedResults);

            $scope.assignedDate = null;

            function loadProjectTasks(project) {

                if (project == undefined) {
                    vm.filters.project = null;
                    vm.project = null;
                    loadTasks();
                }
                else {
                    vm.filters.project = project.id;
                    vm.project = project;
                    loadTasks();
                }
            }

            function clearFilter() {
                vm.filters.searchQuery = null;
                loadTasks();
                vm.clear = false;
            }

            function applyFilters() {
                pageable.page = 0;
                if (vm.filters.assignedToObject != null) {
                    vm.filters.assignedTo = vm.filters.assignedToObject.id;
                } else {
                    vm.filters.assignedTo = null;
                }
                if (vm.filters.verifiedByObject != null) {
                    vm.filters.verifiedBy = vm.filters.verifiedByObject.id;
                } else {
                    vm.filters.verifiedBy = null;
                }
                if (vm.filters.approvedByObject != null) {
                    vm.filters.approvedBy = vm.filters.approvedByObject.id;
                } else {
                    vm.filters.aprovedBy = null;
                }
                if (vm.filters.projectObject != null) {
                    vm.filters.project = vm.filters.projectObject.id;
                } else {
                    vm.filters.project = null;
                }
                if (vm.filters.shiftObject != null) {
                    vm.filters.shift = vm.filters.shiftObject.shiftId;
                } else {
                    vm.filters.shift = null;
                }
                if (vm.filters.assignedDate == "" || vm.filters.assignedDate == undefined) {
                    vm.filters.assignedDate = null;
                }
                loadTasks();
            }

            function resetFilters() {
                vm.filters = angular.copy(vm.emptyFilters);
                pageable.page = 0;
                loadTasks();
            };

            function newTask() {
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

                });
                modalInstance.result.then(
                    function (result) {
                        result.shift = result.shift.shiftId;
                        TaskService.createProjectTask(result.project, result).then(
                            function (data) {
                                $rootScope.showSuccessMessage("Task Created Successfully ");
                                loadTasks();
                            }
                        );
                    }
                )
            }


            function print() {
                var modalInstance = $uibModal.open({
                    animation: true,
                    templateUrl: 'app/desktop/modules/task/print/printDialogueView.jsp',
                    controller: 'PrintDialogueController as printDialogueVm',
                    size: 'md',

                });
                modalInstance.result.then(
                    function (result) {

                    }
                )
            }

            function loadProjects() {
                ProjectService.getProjects().then(
                    function (data) {
                        vm.projects = data;
                    });
            }

            function nextPage() {
                if (vm.tasks.last != true) {
                    pageable.page++;
                    loadTasks();
                }
            }

            function resetPage() {
                pageable.page = 0;
            }

            function freeTextSearch(freeText) {

                if (freeText != null && freeText != undefined && freeText.trim() != "") {
                    vm.filters.searchQuery = freeText;
                    TaskService.getAllTasks(pageable, vm.filters).then(
                        function (data) {
                            vm.tasks = data;
                            setValues();
                            vm.clear = true;
                        }
                    )
                    vm.clear = true;
                } else {
                    vm.filters.searchQuery = freeText;
                    resetPage();
                    loadTasks();
                }
            }

            function previousPage() {
                if (vm.tasks.first != true) {
                    pageable.page--;
                    loadTasks();
                }
            }

            function loadTasks() {

                var login = $application.login.person;

                if ($rootScope.hasRole('Staff')) {
                    vm.filters.assignedTo = login.id;
                }
                if ($rootScope.hasRole('Supervisor')) {
                    vm.filters.verifiedBy = login.id;
                }
                if ($rootScope.hasRole('Officer')) {
                    vm.filters.approvedBy = login.id;
                }

                TaskService.getAllTasks(pageable, vm.filters).then(
                    function (data) {
                        vm.tasks = data;
                        setValues();
                    });
                vm.loading = false;
            }

            $scope.$watch('assignedDate', function (date) {
                vm.filters.assignedDate = $scope.assignedDate;
                vm.applyFilters();
            });

            function setValues() {
                CommonService.getPersonReferences(vm.tasks.content, 'approvedBy');
                CommonService.getPersonReferences(vm.tasks.content, 'verifiedBy');
                CommonService.getPersonReferences(vm.tasks.content, 'assignedTo');
                ProjectService.getProjectReferences(vm.tasks.content, 'project');
                ShiftService.getShiftReferences(vm.tasks.content, 'shift');

            }

            function deleteTask(task) {
                var options = {
                    title: 'Delete Task',
                    message: 'Are you sure you want to delete this task?',
                    okButtonClass: 'btn-danger'
                };
                DialogService.confirm(options, function (yes) {
                    if (yes == true) {
                        TaskService.deleteProjectTask(task.project, task.id).then(
                            function (data) {
                                var index = vm.tasks.content.indexOf(task);
                                vm.tasks.content.splice(index, 1);
                                $rootScope.showErrorMessage(" Deleted Successfully!");
                                loadTasks();
                            }
                        )
                    }
                });
            }

            function getShifts() {
                ShiftService.getAllShifts().then(
                    function (data) {
                        vm.shifts = data;
                    });
            }

            function loadLocations() {
                TaskService.getLocations().then(
                    function (data) {
                        vm.locations = data;
                    });
            }

            function getassignedpersonsByRole() {
                PersonService.getPersonsByRole("Staff").then(
                    function (data) {
                        angular.forEach(data, function (obj) {
                            loadStaffPersons(obj);
                        });
                    });
                getAdminstratorByRole();
            }

            function loadStaffPersons(obj) {
                CommonService.getPerson(obj.person).then(
                    function (data) {
                        var staff = data;
                        vm.staffs.push(staff);
                    });
            }


            function getAdminstratorByRole() {
                PersonService.getPersonsByRole("Administrator").then(
                    function (data) {
                        angular.forEach(data, function (obj) {
                            loadAdministrator(obj);
                        });
                    });
            }

            function loadAdministrator(obj) {
                CommonService.getPerson(obj.person).then(
                    function (data) {
                        var admin = data;
                        vm.staffs.push(admin);
                        vm.supervisors.push(admin);
                        vm.officers.push(admin);
                    });
            }

            function getSuperVisors() {
                PersonService.getPersonsByRole("Supervisor").then(
                    function (data) {
                        angular.forEach(data, function (obj) {
                            loadSupervisors(obj);
                        });
                    });
            }

            function loadSupervisors(obj) {
                CommonService.getPerson(obj.person).then(
                    function (data) {
                        var supervisor = data;
                        vm.supervisors.push(supervisor);
                    });
            }


            function getOfficers() {
                PersonService.getPersonsByRole("Officer").then(
                    function (data) {
                        angular.forEach(data, function (obj) {
                            loadOfficers(obj);
                        });
                    });
            }

            function loadOfficers(obj) {
                CommonService.getPerson(obj.person).then(
                    function (data) {
                        var officer = data;
                        vm.officers.push(officer);
                    });
            }

            function compare(a, b) {
                if (a.firstName < b.firstName)
                    return -1;
                if (a.firstName > b.firstName)
                    return 1;
                return 0;
            }

            function sortValues(values) {
                values.sort(compare);
            }

            (function () {
                loadTasks();
                loadLocations();
                loadProjects();
                getShifts();
                getassignedpersonsByRole();
                getSuperVisors();
                getOfficers();
            })();
        }
    }
);