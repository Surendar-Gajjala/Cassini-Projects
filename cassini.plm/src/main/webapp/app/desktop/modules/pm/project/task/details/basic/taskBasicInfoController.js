define(
    [
        'app/desktop/modules/pm/project/task/task.module',
        'app/shared/services/core/activityService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/commonService',
        'app/shared/services/core/projectService',
        'app/desktop/modules/directives/basicAttributeDetailsDirectiveController'
    ],
    function (module) {
        module.controller('TaskBasicInfoController', TaskBasicInfoController);

        function TaskBasicInfoController($scope, $stateParams, $rootScope, $timeout, $sce, $window, $state, $translate, ActivityService, CommonService, ProjectService) {

            var vm = this;
            var taskId = $stateParams.taskId;
            var activityId = $stateParams.activityId;
            var projectManagerId = $stateParams.projectManagerId;
            var project = $rootScope.projectId;
            vm.persons = [];
            $rootScope.taskPercentage = 0;
            var initPercentage = null;

            var parsed = angular.element("<div></div>");
            var nameValidation = parsed.html($translate.instant("NAME_VALIDATION")).html();
            var successMsg = $translate.instant("PROJECT_ATTRIBUTE_UPDATE_MESSAGE");
            var timeMessage = $translate.instant("MFR_TIME_ATTRIBUTE_MESSAGE");
            var timestampMessage = $translate.instant("MFR_ATTRIBUTE_TIMESTAMP_MESSAGE");
            var taskUpdateMsg = $translate.instant("TASK_UPDATE_MSG");
            var assignedToMsg = parsed.html($translate.instant("ASSIGNED_TO_VALIDATION")).html();
            var percentValidation = parsed.html($translate.instant("PERCENT_VALIDATION")).html();
            var assignedOrPmMsg = parsed.html($translate.instant("ASSIGNED_OR_PM_VALIDATION")).html();
            var taskWorkflowComplete = parsed.html($translate.instant("TASK_WORKFLOW_COMPLETE")).html();

            vm.updateTask = updateTask;

            function updateTask() {
                if (validate()) {
                    var taskUpdate = true;
                    vm.task.percentComplete = parseInt(vm.task.percentComplete, 10);
                    if (vm.task.assignedToObject != undefined) vm.task.assignedTo = vm.task.assignedToObject.id;
                    if (vm.task.percentComplete != $rootScope.taskPercentage) {
                        if (vm.task.assignedTo == null) {
                            $rootScope.showWarningMessage(assignedToMsg);
                            taskUpdate = false;
                            vm.task.percentComplete = $rootScope.taskPercentage;
                        } else if (vm.task.assignedTo != $rootScope.loginPersonDetails.person.id && $rootScope.loginPersonDetails.person.id != vm.project.projectManager) {
                            $rootScope.showWarningMessage(assignedOrPmMsg);
                            taskUpdate = false;
                            vm.task.percentComplete = $rootScope.taskPercentage;
                        }
                    }
                    if (vm.task.percentComplete < 0) {
                        taskUpdate = false;
                        vm.task.percentComplete = 0;
                        $rootScope.showWarningMessage(percentValidation);
                    } else if (vm.task.percentComplete > 0 && taskUpdate == true && vm.task.percentComplete != 100) {
                        vm.task.status = "INPROGRESS";
                    } else if (vm.task.percentComplete == 100 && taskUpdate == true) {
                        if (vm.task.workflow != null && !vm.task.finishedWorkflow) {
                            taskUpdate = false;
                            loadTask();
                            $rootScope.showWarningMessage(taskWorkflowComplete);
                        } else vm.task.status = "FINISHED";
                    }
                    if (taskUpdate) {
                        ActivityService.updateActivityTask(vm.task.activity, vm.task).then(
                            function (data) {
                                vm.task.name = data.name;
                                vm.task.description = data.description;
                                vm.task.assignedTo = data.assignedTo;
                                $rootScope.taskPercentage = parseInt(vm.task.percentComplete, 10);
                                $rootScope.viewInfo.title = vm.task.name;
                                $rootScope.breadCrumb.task = data;
                                $rootScope.task = data;
                                $rootScope.showSuccessMessage(taskUpdateMsg);
                                CommonService.getPersonReferences([vm.task], 'assignedTo');
                                $rootScope.loadTask();
                            }, function (error) {
                                loadTask();
                                $rootScope.showErrorMessage(error.message);
                                $rootScope.hideBusyIndicator();
                            }
                        )
                    }
                }
            }

            function validate() {
                var valid = true;
                if (vm.task.name == null || vm.task.name == ""
                    || vm.task.name == undefined) {
                    valid = false;
                    vm.task.name = $rootScope.task.name;
                    $rootScope.showWarningMessage("Task name cannot be empty");
                } else if (vm.project != null && vm.project.program != null) {
                    $window.localStorage.setItem("project_open_from", vm.project.program);
                    if (vm.project.plannedStartDate == null || vm.project.plannedStartDate == "" || vm.project.plannedFinishDate == null || vm.project.plannedFinishDate == "") {
                        $rootScope.showWarningMessage("Please add project planned start and finish dates before edit task details");
                        vm.task.percentComplete = 0;
                        valid = false;
                        $timeout(function () {
                            $state.go('app.pm.project.details', {
                                projectId: vm.project.id,
                                tab: 'details.basic'
                            });
                        }, 1500)
                    }
                } else if (vm.task.percentComplete == null || vm.task.percentComplete == ""
                    || vm.task.percentComplete == undefined) {
                    valid = true;
                    vm.task.percentComplete = 0;
                } else if (vm.task.percentComplete > 100) {
                    valid = false;
                    vm.task.percentComplete = $rootScope.task.percentComplete;
                    $rootScope.showWarningMessage("Percentage of complete should not exceed 100");
                } else if (vm.task.percentComplete < 0) {
                    valid = false;
                    vm.task.percentComplete = $rootScope.task.percentComplete;
                    $rootScope.showWarningMessage(percentValidation);
                }
                return valid;
            }

            function loadPersons() {
                vm.persons = [];
                ProjectService.getAllProjectMembers(project).then(
                    function (data) {
                        vm.persons = data;
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                        $rootScope.hideBusyIndicator();
                    }
                )
            }

            function loadTask() {
                ActivityService.getActivityTask(activityId, taskId).then(
                    function (data) {
                        vm.task = data;
                        $rootScope.taskPercentage = vm.task.percentComplete;
                        $rootScope.viewInfo.title = vm.task.name;
                        CommonService.getMultiplePersonReferences([vm.task], ['assignedTo', 'createdBy', 'modifiedBy']);
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                        $rootScope.hideBusyIndicator();
                    }
                )
            }

            function loadProject() {
                ProjectService.getProject(project).then(
                    function (data) {
                        vm.project = data;
                        loadPersons();
                    }, function (error) {
                        //$rootScope.showErrorMessage(error.message);
                        $rootScope.hideBusyIndicator();
                    }
                )
            }

            (function () {
                $scope.$on('app.activity.tasks.tabActivated', function (event, data) {
                    if (data.tabId == 'details.basic') {
                        loadTask();
                        loadProject();
                    }
                });
                loadTask();
                loadProject();
            })();
        }
    }
);