define(
    [
        'app/desktop/modules/workflow/workflow.module',
        'moment',
        'moment-timezone-with-data',
        'app/shared/services/core/workflowService',
        'app/assets/bower_components/cassini-platform/app/desktop/directives/commonDirectives',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/commonService'
    ],
    function (module) {
        module.controller('InstancesWorkflowHistoryController', InstancesWorkflowHistoryController);

        function InstancesWorkflowHistoryController($scope, $rootScope, $sce, $timeout, $state, $stateParams, $cookies, ProjectService, DialogService, WorkflowService, CommonService) {
            var vm = this;

            vm.workflow = $scope.workflow;

            function loadWorkflowHistory() {
                WorkflowService.getWorkflowHistory(vm.workflow.id).then(
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
                            var json = history.assignments;
                            if(json != null && json.trim() !== "") {
                                try {
                                    history.assignmentsList = JSON.parse(json);
                                } catch (e) {
                                    history.assignmentsList = [];
                                }
                            }
                            else {
                                history.assignmentsList = [];
                            }

                            if (history.statusObject.createdBy !== undefined) {
                                CommonService.getPerson(history.statusObject.createdBy).then(
                                    function (data) {
                                        history.statusObject.createdByObject = data;
                                    }, function (error) {
                                        $rootScope.showErrorMessage(error.message);
                                        $rootScope.hideBusyIndicator();
                                    }
                                )
                            }
                        });

                        loadPersonReferences();

                        $rootScope.hideBusyIndicator();
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                        $rootScope.hideBusyIndicator();
                    }
                );
            }

            function loadPersonReferences() {
                var assignments = [];
                angular.forEach(vm.workflowHistory, function (history) {
                    var list = history.assignmentsList;
                    angular.forEach(list, function(a) {
                        assignments.push(a);
                    });
                });

                CommonService.getPersonReferences(assignments, "person");
            }

            (function () {
                $scope.$watch('workflow', function (newval, oldval) {
                    vm.workflow = newval;
                    loadWorkflowHistory();
                });
            })();
        }
    }
);