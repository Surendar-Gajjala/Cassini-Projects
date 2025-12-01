define(
    [
        'app/desktop/modules/tm/tm.module',
        'moment',
        'moment-timezone-with-data',
        'app/shared/services/workflow/workflowDefinitionService',
        'app/shared/services/tm/taskService',
        'app/assets/bower_components/cassini-platform/app/desktop/directives/commonDirectives',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/commonService'
    ],
    function (module) {
        module.controller('WorkflowHistoryController', WorkflowHistoryController);

        function WorkflowHistoryController($scope, $rootScope, $sce, $timeout, $state, $stateParams, $cookies,
                                           WorkflowDefinitionService, TaskService, DialogService, CommonService) {
            var vm = this;

            vm.taskId = $stateParams.taskId;
            vm.projectId = $stateParams.projectId;
            $rootScope.loadWorkflowHistory = loadWorkflowHistory;

            function loadWorkflow() {
                if (vm.task.workflow != null) {
                    WorkflowDefinitionService.getWorkflow(vm.task.workflow).then(
                        function (data) {
                            vm.workflow = data;
                            loadWorkflowHistory();
                        }
                    )
                }
            }

            function loadWorkflowHistory() {
                TaskService.getWorkflowHistory(vm.taskId).then(
                    function (data) {
                        vm.workflowHistory = data;
                        if (vm.workflow.started) {
                            vm.workflowHistory.push({
                                statusObject: {
                                    name: "Workflow Started",
                                    createdBy: vm.workflow.start.createdBy
                                },
                                timestamp: vm.workflow.startedOn
                            })
                        }

                        if (vm.workflow.finished) {
                            vm.workflowHistory.unshift({
                                statusObject: {name: "Workflow Finished", createdBy: vm.workflow.finish.createdBy},
                                timestamp: vm.workflow.finishedOn
                            })
                        }
                        angular.forEach(vm.workflowHistory, function (history) {
                            if (history.statusObject.createdBy != undefined) {
                                CommonService.getPerson(history.statusObject.createdBy).then(
                                    function (data) {
                                        history.statusObject.createdByObject = data;
                                    }
                                )
                            }
                        })

                        $rootScope.hideBusyIndicator();
                    }
                );
            }

            function loadProjectTask() {
                TaskService.getProjectTask(vm.projectId, vm.taskId).then(
                    function (data) {
                        vm.task = data;
                        loadWorkflow();
                    }
                )
            }

            (function () {
                /* $scope.$on('app.eco.workflow.tabActivated', function (event, data) {
                 if (data.tabId == 'details.workflow.history') {*/
                loadProjectTask();
                /*}
                 });*/
            })();
        }
    }
);