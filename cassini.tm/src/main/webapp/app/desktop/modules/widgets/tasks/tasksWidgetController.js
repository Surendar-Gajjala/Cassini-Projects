define(['app/desktop/modules/widgets/tasks/taskWidget.module',
        'app/shared/services/taskService',
        'app/shared/services/projectService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/commonService'
    ],
    function (module) {
        module.controller('TasksWidgetController', TasksWidgetController);

        function TasksWidgetController($scope, $rootScope, $timeout, $state, $stateParams, $cookies, $uibModal, TaskService, ProjectService, CommonService) {

            var vm = this;

            vm.taskHeading = "Today-Tasks"
            var pageable = {
                page: 0,
                size: 20,
                sort: {
                    field: "modifiedDate",
                }
            };

            vm.filters = {
                project: null,
                name: null,
                description: null,
                status: null,
                assignedTo: null,
                verifiedBy: null,
                approvedBy: null,
                searchQuery: null,
                assignedDate: null
            };
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

            vm.nextPage = nextPage;
            vm.previousPage = previousPage;
            vm.previousDayTasks = previousDayTasks;
            vm.nextDayTasks = nextDayTasks;
            vm.loadTasks = loadTasks;
            vm.tasks = [];
            vm.loading = true;
            var moment = require('moment');

            vm.tasks = angular.copy(pagedResults);

            function nextPage() {
                if (vm.tasks.last != true) {
                    pageable.page++;
                    loadTasks();
                }
            }

            function previousPage() {
                if (vm.tasks.first != true) {
                    pageable.page--;
                    loadTasks();
                }
            }

            function getTasks() {
                vm.loading = false;
                TaskService.getTasksByAssignedDate(vm.filters, pageable).then(
                    function (data) {
                        vm.tasks = data;
                        CommonService.getPersonReferences(vm.tasks.content, 'approvedBy');
                        CommonService.getPersonReferences(vm.tasks.content, 'verifiedBy');
                        CommonService.getPersonReferences(vm.tasks.content, 'assignedTo');
                        angular.forEach(vm.tasks.content, function (task) {
                            ProjectService.getProjectById(task.project).then(function (data) {
                                task.project = data;
                            })
                        })
                    });
            }

            function previousDayTasks() {
                vm.today.setDate(vm.today.getDate() - 1);
                vm.taskHeading = moment(vm.today).format("DD/MM/YYYY");
                vm.filters.assignedDate = vm.taskHeading;
                if (vm.date == vm.taskHeading) {
                    if (vm.filters.status == "APPROVED") {
                        vm.taskHeading = "Today COMPLETED-Tasks"
                    } else {
                        vm.taskHeading = "Today " + vm.filters.status + "-Tasks";
                    }
                } else {
                    if (vm.filters.status == "APPROVED") {
                        vm.taskHeading = vm.taskHeading + " COMPLETED-Tasks";
                    } else {
                        vm.taskHeading = vm.taskHeading + " " + vm.filters.status + "-Tasks";
                    }
                }
                getTasks();
            }

            function nextDayTasks() {
                vm.today.setDate(vm.today.getDate() + 1);
                vm.taskHeading = moment(vm.today).format("DD/MM/YYYY");
                vm.filters.assignedDate = vm.taskHeading;
                if (vm.date == vm.taskHeading) {
                    if (vm.filters.status == "APPROVED") {
                        vm.taskHeading = "Today COMPLETED-Tasks"
                    } else {
                        vm.taskHeading = "Today " + vm.filters.status + "-Tasks";
                    }
                } else {
                    if (vm.filters.status == "APPROVED") {
                        vm.taskHeading = vm.taskHeading + " COMPLETED-Tasks";
                    } else {
                        vm.taskHeading = vm.taskHeading + " " + vm.filters.status + "-Tasks";
                    }
                }
                getTasks();
            }

            function loadTasks(state) {

                if (state == "Today") {
                    vm.today = new Date();
                    vm.date = moment(vm.today).format("DD/MM/YYYY");
                    vm.taskHeading = "Today ASSIGNED-Tasks";
                    vm.filters.status = "ASSIGNED";
                    vm.filters.assignedDate = vm.date;
                    getTasks();
                }
                if (state == "Completed") {
                    vm.today = new Date();
                    vm.date = moment(vm.today).format("DD/MM/YYYY");
                    vm.filters.status = "APPROVED"
                    vm.filters.assignedDate = vm.date;
                    vm.taskHeading = "Today COMPLETED-Tasks"
                    getTasks();
                }
                if (state == "Pending") {
                    vm.today = new Date();
                    vm.date = moment(vm.today).format("DD/MM/YYYY");
                    vm.filters.status = "PENDING"
                    vm.filters.assignedDate = vm.date;
                    vm.taskHeading = "Today PENDING-Tasks"
                    getTasks();
                }
            }

            function setValues(tasks) {
                CommonService.getPersonReferences(tasks.content, 'approvedBy');
                CommonService.getPersonReferences(tasks.content, 'verifiedBy');
                CommonService.getPersonReferences(tasks.content, 'assignedTo');
                angular.forEach(tasks.content, function (task) {
                    ProjectService.getProjectById(task.project).then(function (data) {
                        task.project = data;
                    })
                })
            }

            function loadTotalTasks() {
                vm.loading = false;
                vm.filters.status = null;
                TaskService.getAllTasks(pageable, vm.filters).then(
                    function (data) {
                        vm.totalTasks = data;
                        setValues(vm.totalTasks);
                    });
            }

            function loadPendingTasks() {
                vm.loading = false;
                vm.filters.status = "PENDING"
                TaskService.getAllTasks(pageable, vm.filters).then(
                    function (data) {
                        vm.pendingTasks = data;
                        setValues(vm.pendingTasks);
                    });
            }

            function loadCompletedTasks() {
                vm.loading = false;
                vm.filters.status = "APPROVED"
                TaskService.getAllTasks(pageable, vm.filters).then(
                    function (data) {
                        vm.completedTasks = data;
                        setValues(vm.completedTasks);
                    });
            }

            (function () {
                loadTotalTasks();
                loadCompletedTasks();
                loadPendingTasks();
                loadTasks("Today");
            })();
        }
    }
);