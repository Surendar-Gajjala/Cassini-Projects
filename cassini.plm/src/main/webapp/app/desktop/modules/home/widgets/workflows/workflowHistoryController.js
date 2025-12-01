define(
    [
        'app/desktop/modules/home/home.module',
        'moment',
        'moment-timezone-with-data',
        'app/shared/services/core/workflowService',
        'app/assets/bower_components/cassini-platform/app/desktop/directives/commonDirectives',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/commonService'
    ],
    function (module) {
        module.controller('WidgetWorkflowHistoryController', WidgetWorkflowHistoryController);

        function WidgetWorkflowHistoryController($scope, $rootScope, $sce, $timeout, $state, $stateParams, $cookies, DialogService, WorkflowService, CommonService) {
            var vm = this;

            vm.workflow = $scope.data.selectedWorkflow;
            vm.number = $scope.data.selectedObjectNumber;

            function loadWorkflowHistory() {
                WorkflowService.getWorkflowAssigments(vm.workflow.id).then(
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
                            if (json != null && json.trim() !== "") {
                                try {
                                    history.assignmentsList = JSON.parse(json);
                                } catch (e) {
                                    history.assignmentsList = [];
                                }
                            }
                            else {
                                history.assignmentsList = [];
                            }
                            if (history.statusObject.createdBy != undefined) {
                                CommonService.getPerson(history.statusObject.createdBy).then(
                                    function (data) {
                                        history.statusObject.createdByObject = data;
                                    }, function (error) {
                                        $rootScope.showErrorMessage(error.message);
                                     }
                                )
                            }
                            CommonService.getPersonReferences(history.statusApprovers, "person");
                            CommonService.getPersonReferences(history.statusAcknowledgers, "person");
                        })
                        loadPersonReferences();
                        $rootScope.hideBusyIndicator();
                    }, function (error) {
                        $rootScope.hideBusyIndicator();
                        $rootScope.showErrorMessage(error.message);
                    }
                );
            }

            function loadPersonReferences() {
                var assignments = [];
                angular.forEach(vm.workflowHistory, function (history) {
                    var list = history.assignmentsList;
                    angular.forEach(list, function (a) {
                        assignments.push(a);
                    });
                });

                CommonService.getPersonReferences(assignments, "person");
            }

            (function () {
                //if ($application.homeLoaded == true) {
                vm.workflow = $scope.data.selectedWorkflow;
                loadWorkflowHistory();
                //}
            })();
        }
    }
);