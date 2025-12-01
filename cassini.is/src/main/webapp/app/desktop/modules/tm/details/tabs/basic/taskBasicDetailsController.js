define(['app/desktop/modules/tm/tm.module',
        'dropzone',
        'app/shared/services/tm/taskService',
        'app/shared/services/pm/project/projectSiteService',
        'app/shared/services/pm/project/projectService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/commonService',
        'app/shared/services/core/itemService',
        'app/shared/services/pm/project/wbsService',
        'app/assets/bower_components/cassini-platform/app/desktop/directives/attributes/attributesDetailsDirectiveController',
        'app/shared/services/pm/project/projectService'
    ],
    function (module) {
        module.controller('TaskBasicDetailsController', TaskBasicDetailsController);

        function TaskBasicDetailsController($scope, $rootScope, $timeout, $state, $sce, $stateParams,
                                            TaskService, CommonService, WbsService, ProjectSiteService, IssueService, ProjectService) {
            var vm = this;

            $rootScope.viewInfo.icon = "fa flaticon-deadlines";

            vm.loading = true;

            vm.taskId = $stateParams.taskId;
            vm.task = null;
            vm.selectedPerson = null;
            vm.persons = [];
            vm.projectPersons = [];
            vm.taskProperties = [];
            vm.flags = [{
                name: "True",
                value: true
            },
                {
                    name: "False",
                    value: false
                }
            ];

            vm.updateTask = updateTask;
            vm.startTask = startTask;
            vm.finishTask = finishTask;
            vm.assignToTask = assignToTask;
            vm.addUnitsOfWorkCompleted = addUnitsOfWorkCompleted;
            vm.updateTaskCompletion = updateTaskCompletion;
            vm.hasUpdate = false;
            vm.newUnitOfWorkCompleted = {
                task: $stateParams.taskId,
                timeStamp: new Date(),
                completedBy: null,
                unitsCompleted: 0,
                notes: null,
            };
            vm.cancelNewRow = cancelNewRow;
            vm.totalUnitsCompleted = 0;

            vm.clearBrowse = true;
            vm.emptyFilters = {
                name: null,
                description: null,
                person: null,
                wbsItem: null,
                status: null,
                percentComplete: null,
                personObject: null,
                wbsItemObject: null,
                percentCompleteObject: null,
                plannedStartDate: null,
                plannedFinishDate: null,
                actualStartDate: null,
                actualFinishDate: null,
                searchQuery: null
            };

            vm.filters = angular.copy(vm.emptyFilters);

            var pageable = {
                page: $rootScope.taskPage,
                size: 20,
                sort: {
                    field: "modifiedDate"

                }
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

            var siteId = null;
            var tasks = [];
            var taskMap = new Hashtable();

            function loadTask() {
                vm.taskHistory = [];
                vm.totalUnitsCompleted = 0;
                TaskService.getProjectTask($stateParams.projectId, vm.taskId).then(
                    function (data) {
                        vm.task = data;
                        $rootScope.task = data;
                        $rootScope.task.hasProblems = false;
                        $rootScope.login.person.isTaskOwner = false;
                        if ($rootScope.login.person.id == vm.task.createdByPerson) {
                            $rootScope.login.person.isTaskOwner = true;
                        }
                        loadTaskProblems($rootScope.task);
                        $rootScope.viewInfo.title = vm.task.name;
                        tasks = data.projectTasks;
                        angular.forEach(tasks, function (task) {
                            if (task.id != $stateParams.taskId) {
                                taskMap.put(task.name, task);
                            }
                        });
                        vm.taskHistory = data.taskCompletionHistories;
                        CommonService.getPersonReferences(vm.taskHistory, 'completedBy');
                        angular.forEach(vm.taskHistory, function (history) {
                            vm.totalUnitsCompleted += history.unitsCompleted;
                        });
                        if (vm.totalUnitsCompleted <= 0 && vm.task.totalUnits <= 0) {
                            vm.task.percentComplete = 0;
                        }
                        $scope.$parent.$parent.$parent.task = data;
                        return CommonService.getPerson(data.createdBy);
                    }
                ).then(
                    function (data) {
                        vm.task.createdByPerson = data;
                        if (vm.task.person != null) {
                            return CommonService.getPerson(vm.task.person);
                        }
                    }
                ).then(
                    function (data) {
                        vm.task.assignedToPerson = data;
                        siteId = vm.task.site;
                        return ProjectSiteService.getSite(vm.task.site);
                    }
                ).then(
                    function (data) {
                        vm.loading = false;
                        vm.task.siteName = data.name;
                        if (vm.task.inspectedBy != null) {
                            return CommonService.getPerson(vm.task.inspectedBy);
                        }
                    }
                ).then(
                    function (data) {
                        vm.task.inspectedByPerson = data;
                    }
                );
            }

            function loadTaskProblems(task) {
                IssueService.getIssues("TASK", task.id).then(
                    function (data) {
                        angular.forEach(data, function (problem) {
                            if (problem.status != 'CLOSED') {
                                task.hasProblems = true;
                                return;
                            }
                        });
                    });
            }

            function assignToTask() {
                var person = vm.selectedPerson;
                TaskService.assignTaskTo($stateParams.projectId, vm.task.id, person.id).then(
                    function (data) {
                        vm.task.assignedPersons = data;
                        loadTask();
                    }
                )
            }

            function updateTask() {
                vm.valid = true;
                vm.task.site = siteId;
                /* if (taskMap.get(vm.task.name) != null || taskMap.get(vm.task.name) == vm.task.name) {
                 vm.valid = false;
                 $rootScope.showErrorMessage("{0} Name already exists".format(vm.task.name));
                 loadTask();
                 } else*/
                if (vm.task.name == "" || vm.task.name == undefined || vm.task.name == null) {
                    vm.valid = false;
                    $rootScope.showErrorMessage("Task Name cannot be empty");
                    loadTask();
                } else if (vm.task.totalUnits != null && vm.task.totalUnits == undefined) {
                    vm.valid = false;
                    $rootScope.showErrorMessage("Please enter +ve number for Total Units of Work");
                    loadTask();
                } /*else if (!Number.isInteger(vm.task.totalUnits) && vm.task.totalUnits != null) {
                 vm.valid = false;
                 $rootScope.showErrorMessage("Please enter numeric value for the field Total Units of Work");
                 loadTask();
                 }*/ else if (vm.task.totalUnits <= 0 && vm.task.totalUnits != null) {
                    vm.valid = false;
                    $rootScope.showErrorMessage("Total Units of Work should be +ve number");
                    loadTask();
                }
                else {
                    if (vm.task.assignedToPerson != null) {
                        vm.task.person = vm.task.assignedToPerson.id;
                    }
                    if (vm.task.projectSite != null) {
                        vm.task.site = vm.task.projectSite.id;
                    }
                    if (vm.totalUnitsCompleted == vm.task.totalUnits) {
                        vm.task.status = 'FINISHED';
                        if (vm.task.person == $application.login.person.id) {
                            $rootScope.$broadcast("mytasks.decrement.finished");
                        }
                        vm.task.actualFinishDate = moment(new Date()).format("DD/MM/YYYY");
                    }
                    if (vm.task.inspectedByPerson != null) {
                        vm.task.inspectedBy = vm.task.inspectedByPerson.id;
                    }
                    TaskService.updateProjectTask($stateParams.projectId, vm.task).then(
                        function (data) {
                            $rootScope.task = data;
                            loadTask();
                            loadAssignedTasks();
                            loadInProgressTasks();
                            $rootScope.showSuccessMessage("Task details updated successfully");
                        });
                }
            }

            function startTask() {
                if (vm.task.person == $application.login.person.id) {
                    $rootScope.$broadcast("mytasks.increment.inprogress");
                }
                if (vm.task.status == 'ASSIGNED' && vm.taskHistory.length == 0) {
                    vm.task.status = 'INPROGRESS';
                    vm.task.actualStartDate = moment(new Date()).format("DD/MM/YYYY");
                }
                vm.task.site = siteId;
                TaskService.updateProjectTask($stateParams.projectId, vm.task).then(
                    function (data) {
                        $rootScope.task = data;
                        loadTask();
                        loadAssignedTasks();
                        loadInProgressTasks();
                        $rootScope.showSuccessMessage("Task started successfully");
                    }
                )
            }

            function finishTask() {
                if (vm.totalUnitsCompleted == vm.task.totalUnits) {
                    vm.task.status = 'FINISHED';
                    if (vm.task.person == $application.login.person.id) {
                        $rootScope.$broadcast("mytasks.decrement.finished");
                    }
                    vm.task.actualFinishDate = moment(new Date()).format("DD/MM/YYYY");
                    vm.task.site = siteId;
                    TaskService.updateProjectTask($stateParams.projectId, vm.task).then(
                        function (data) {
                            $rootScope.task = data;
                            loadTask();
                            $rootScope.showSuccessMessage("Task finished successfully");
                        }
                    )
                }
                else {
                    $rootScope.showErrorMessage("Total Units of Work must be completed to finish this Task");
                }
            }

            function loadPersonsById(person) {
                vm.loading = false;
                CommonService.getPerson(person).then(
                    function (data) {
                        vm.persons.push(data);
                    }
                )
            }

            function addUnitsOfWorkCompleted() {
                var today = moment(new Date());
                var todayStr = today.format('DD/MM/YYYY');
                vm.newUnitOfWorkCompleted = {
                    task: $stateParams.taskId,
                    timeStamp: new Date(),
                    completedBy: $rootScope.login.person,
                    unitsCompleted: null
                };
                vm.newUnitOfWorkCompleted.timeStamp = todayStr;
                $rootScope.newUnitOfWorkCompleted.timeStamp = new Date();
                $rootScope.newUnitOfWorkCompleted.completedBy = $rootScope.login.person;
                vm.hasUpdate = true;
            }

            function updateTaskCompletion() {
                var today = new Date();
                vm.newUnitOfWorkCompleted.timeStamp = moment(today);
                vm.newUnitOfWorkCompleted.timeStamp = vm.newUnitOfWorkCompleted.timeStamp.format('DD/MM/YYYY, HH:mm:ss');

                if (vm.newUnitOfWorkCompleted.unitsCompleted <= vm.task.totalUnits) {
                    if ((vm.totalUnitsCompleted + vm.newUnitOfWorkCompleted.unitsCompleted) <= vm.task.totalUnits) {
                        if (vm.newUnitOfWorkCompleted.unitsCompleted == "" || vm.newUnitOfWorkCompleted.unitsCompleted == null) {
                            vm.newUnitOfWorkCompleted.unitsCompleted = 0;
                        }
                        vm.newUnitOfWorkCompleted.completedBy = $rootScope.login.person.id;
                        TaskService.updateTaskCompletion($stateParams.projectId, $stateParams.taskId, vm.newUnitOfWorkCompleted).then(
                            function (data) {
                                vm.hasUpdate = false;
                                loadTask();
                                loadAssignedTasks();
                                loadInProgressTasks();
                                $rootScope.loadProject();
                            }
                        );
                        $rootScope.showSuccessMessage("Task details updated successfully");

                    }
                    else {
                        $rootScope.showErrorMessage("Total Units cannot be greater than Total Units completed");
                    }
                }

                else {
                    $rootScope.showErrorMessage("Unit of Work cannot be greater than Total Units");

                }
            }

            function cancelNewRow() {
                vm.hasUpdate = false;
            }

            function loadAssignedTasks() {
                vm.filters.status = "ASSIGNED";
                vm.filters.person = window.$application.login.person.id;
                TaskService.getListTasks($stateParams.projectId, vm.filters).then(
                    function (data) {
                        $rootScope.assignedTasks = data;
                        setValues(tasks);
                    });
            }

            function loadInProgressTasks() {
                vm.filters.status = "INPROGRESS";
                vm.filters.person = window.$application.login.person.id;
                TaskService.getListTasks($stateParams.projectId, vm.filters).then(
                    function (data) {
                        $rootScope.inprogressTasks = data;
                        setValues(tasks);
                    });
            }

            function setValues(tasks) {
                CommonService.getPersonReferences(tasks.content, 'person');
                WbsService.getMultipleWbsWithTasks($stateParams.projectId, tasks.content, 'wbsItem');
            };

            function inspectTask() {
                var options = {
                    title: 'Task Inspection Remarks',
                    showMask: true,
                    template: 'app/desktop/modules/tm/details/tabs/inspect/taskInspectionView.jsp',
                    controller: 'TaskInspectionController as inspectVm',
                    resolve: 'app/desktop/modules/tm/details/tabs/inspect/taskInspectionController',
                    width: 600,
                    data: {
                        task: vm.task
                    },
                    buttons: [
                        {text: 'Reject', btnClass: 'btn-danger', broadcast: 'app.task.inspect.reject'},
                        {text: 'Accept', broadcast: 'app.task.inspect.accept'}
                    ],
                    callback: function (task) {
                        $scope.task = task;
                        //vm.task.inspectedByPerson = window.$application.login.person;
                        vm.task.inspectedOn = task.inspectedOn;
                        vm.task.inspectionResult = task.inspectionResult;
                        vm.task.inspectionRemarks = task.inspectionRemarks;
                    }
                };
                $rootScope.showSidePanel(options);
            }

            function loadProjectPersons() {
                vm.persons = [];
                vm.loading = false;
                ProjectService.getProjectPersons($stateParams.projectId).then(
                    function (data) {
                        angular.forEach(data, function (obj) {
                            loadPersonsById(obj.person);
                        });
                    }
                )
            }

            function loadProjectSites() {
                ProjectSiteService.getPagedSitesByProject($stateParams.projectId, pageable).then(
                    function (data) {
                        vm.sites = data.content;
                    });
            }

            vm.showResources = showResources;
            function showResources() {
                var options = {
                    title: 'Task Resources',
                    showMask: true,
                    template: 'app/desktop/modules/tm/details/tabs/basic/taskResourcesView.jsp',
                    controller: 'TaskResourcesController as taskResourcesVm',
                    resolve: 'app/desktop/modules/tm/details/tabs/basic/taskResourcesController',
                    width: 700,
                    data: {
                        task: vm.task,
                        totalUnitsCompleted: vm.totalUnitsCompleted
                    },
                    buttons: [
                        {text: 'Add', broadcast: 'app.task.resources'}
                    ],
                    callback: function (data) {
                        $rootScope.hideSidePanel();
                        loadTask();
                        loadAssignedTasks();
                        loadInProgressTasks();
                        $rootScope.loadProject();
                    }
                };
                $rootScope.showSidePanel(options);
            }

            vm.showTaskResources = showTaskResources;
            function showTaskResources(taskCompletion) {
                var options = {
                    title: 'Task Resources',
                    showMask: true,
                    template: 'app/desktop/modules/tm/details/tabs/basic/taskResourcesCompletedView.jsp',
                    controller: 'TaskResourcesCompletedController as taskResourceCompleteVm',
                    resolve: 'app/desktop/modules/tm/details/tabs/basic/taskResourcesCompletedController',
                    width: 700,
                    data: {
                        mode: 'EDIT',
                        taskHistory: taskCompletion
                    },
                    buttons: [],
                    callback: function () {
                        $rootScope.hideSidePanel();
                    }
                };
                $rootScope.showSidePanel(options);
            }

            (function () {
                if ($application.homeLoaded == true) {
                    loadTask();
                    loadProjectSites();
                    loadProjectPersons();
                    $scope.$on('app.task.start', startTask);
                    $scope.$on('app.task.finish', finishTask);
                    $scope.$on('app.team.assignedTo', assignToTask);
                    $scope.$on('app.task.inspect', inspectTask)
                }
            })();
        }
    }
)
;