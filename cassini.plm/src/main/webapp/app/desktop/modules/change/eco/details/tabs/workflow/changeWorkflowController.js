define(
    [
        'app/desktop/modules/change/change.module',
        'app/shared/services/core/ecoService',
        'app/shared/services/core/varianceService',
        'app/shared/services/core/dcoService',
        'app/shared/services/core/dcrService',
        'app/shared/services/core/mcoService',
        'app/shared/services/core/ecrService',
        'app/shared/services/core/workflowDefinitionService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/commonService',
        'app/assets/bower_components/cassini-platform/app/shared/services/security/loginService'
    ],
    function (module) {
        module.controller('ChangeWorkflowController', ChangeWorkflowController);

        function ChangeWorkflowController($scope, $rootScope, $stateParams, $translate, $timeout, CommonService, ECRService, MCOService, WorkflowDefinitionService, ECOService, VarianceService, DCOService, DCRService) {
            var vm = this;

            vm.workflows = [];
            var eco = $scope.data.selectedEco;
            vm.type = $scope.data.selectedType;

            var parsed = angular.element("<div></div>");
            vm.selectWorkflow = parsed.html($translate.instant("SELECT")).html();
            var workflowChangeMsg = parsed.html($translate.instant("WORKFLOW_CHANGE_MSG")).html();
            var workflowChangeNoMsg = parsed.html($translate.instant("WORKFLOW_CHANGE_NO_MSG")).html();
            var selectRevisionCreationType = $translate.instant("SELECT_REVISION_CREATION_TYPE");
            $scope.selectWorkflowActivityTitle = $translate.instant("S_WORKFLOW_ACTIVITY");
            var selectWorkflowActivity = $translate.instant("SELECT_WORKFLOW_ACTIVITY");

            vm.changeWf = {
                workflowDefinition: null,
                revisionCreationType: null,
                workflowStatus: ''
            };

            function validate() {
                var valid = true;
                if (vm.changeWf.revisionCreationType == null || vm.changeWf.revisionCreationType == undefined ||
                    vm.changeWf.revisionCreationType == "") {
                    $rootScope.showErrorMessage(selectRevisionCreationType);
                    valid = false;
                } else if (vm.changeWf.revisionCreationType == "ACTIVITY_COMPLETION" && (vm.changeWf.workflowStatus == null || vm.changeWf.workflowStatus == undefined ||
                    vm.changeWf.workflowStatus == "")) {
                    $rootScope.showErrorMessage(selectWorkflowActivity);
                    valid = false;
                }
                return valid;
            }

            function changeWorkflow() {
                if (vm.changeWf.workflowDefinition != null) {
                    if (vm.type == "ECOS") {
                        if (validate()) {
                            ECOService.deleteWorkflow(eco.id).then(
                                function () {
                                    ECOService.attachNewWorkflow(eco.id, vm.changeWf.workflowDefinition.id, vm.changeWf.revisionCreationType, vm.changeWf.workflowStatus).then(
                                        function (data) {
                                            $rootScope.hideSidePanel();
                                            $rootScope.showSuccessMessage(workflowChangeMsg);
                                            $scope.callback(data);
                                        }, function (error) {
                                            $rootScope.showErrorMessage(error.message);
                                            $rootScope.hideBusyIndicator();
                                        }
                                    )
                                }
                            )
                        }
                    }
                    if (vm.type == "DCOS") {
                        if (validate()) {
                            DCOService.deleteDcoWorkflow(eco.id).then(
                                function () {
                                    DCOService.attachNewDcoWorkflow(eco.id, vm.changeWf.workflowDefinition.id, vm.changeWf.revisionCreationType, vm.changeWf.workflowStatus).then(
                                        function (data) {
                                            $rootScope.hideSidePanel();
                                            $rootScope.showSuccessMessage(workflowChangeMsg);
                                            $scope.callback(data);
                                        }, function (error) {
                                            $rootScope.showErrorMessage(error.message);
                                            $rootScope.hideBusyIndicator();
                                        }
                                    )
                                }
                            )
                        }
                    }
                    if (vm.type == "MCOS") {
                        MCOService.deleteMcoWorkflow(eco.id).then(
                            function () {
                                MCOService.attachMcoWorkflow(eco.id, vm.changeWf.workflowDefinition.id).then(
                                    function (data) {
                                        $rootScope.hideSidePanel();
                                        $rootScope.showSuccessMessage(workflowChangeMsg);
                                        $scope.callback(data);
                                    }
                                )
                            }, function (error) {
                                $rootScope.showErrorMessage(error.message);
                                $rootScope.hideBusyIndicator();
                            })
                    }
                    if (vm.type == "DCRS") {
                        DCRService.deleteDcrWorkflow(eco.id).then(
                            function () {
                                DCRService.attachDcrWorkflow(eco.id, vm.changeWf.workflowDefinition.id).then(
                                    function (data) {
                                        $rootScope.hideSidePanel();
                                        $rootScope.showSuccessMessage(workflowChangeMsg);
                                        $scope.callback(data);
                                    }, function (error) {
                                        $rootScope.showErrorMessage(error.message);
                                        $rootScope.hideBusyIndicator();
                                    }
                                )
                            })
                    }
                    if (vm.type == "ECRS") {
                        ECRService.deleteEcrWorkflow(eco.id).then(
                            function () {
                                ECRService.attachEcrWorkflow(eco.id, vm.changeWf.workflowDefinition.id).then(
                                    function (data) {
                                        $rootScope.hideSidePanel();
                                        $rootScope.showSuccessMessage(workflowChangeMsg);
                                        $scope.callback(data);
                                    }, function (error) {
                                        $rootScope.showErrorMessage(error.message);
                                        $rootScope.hideBusyIndicator();
                                    }
                                )
                            })
                    }
                    if (vm.type == "VARIANCES") {
                        VarianceService.deleteVarianceWorkflow(eco.id).then(
                            function () {
                                VarianceService.attachWorkflow(eco.id, vm.changeWf.workflowDefinition.id).then(
                                    function (data) {
                                        $rootScope.hideSidePanel();
                                        $rootScope.showSuccessMessage(workflowChangeMsg);
                                        $scope.callback(data);
                                    }
                                )
                            }, function (error) {
                                $rootScope.showErrorMessage(error.message);
                                $rootScope.hideBusyIndicator();
                            })
                    }
                } else if (vm.changeWf.workflowDefinition == null) {
                    $rootScope.showWarningMessage(workflowChangeNoMsg);
                }
            }

            function loadWorkflows() {
                if (vm.type == "ECOS") {
                    ECOService.getWorkflows(eco.ecoType, 'CHANGES').then(
                        function (data) {
                            vm.wfs = data;
                        }, function (error) {
                            $rootScope.showErrorMessage(error.message);
                        }
                    )
                }
                if (vm.type == "DCOS") {
                    ECOService.getWorkflows(eco.dcoType, 'CHANGES').then(
                        function (data) {
                            vm.wfs = data;
                        }, function (error) {
                            $rootScope.showErrorMessage(error.message);
                        }
                    )
                }
                if (vm.type == "DCRS") {
                    ECOService.getWorkflows(eco.crType, 'CHANGES').then(
                        function (data) {
                            vm.wfs = data;
                        }, function (error) {
                            $rootScope.showErrorMessage(error.message);
                        }
                    )
                }
                if (vm.type == "MCOS") {
                    ECOService.getWorkflows(eco.mcoType.id, 'CHANGES').then(
                        function (data) {
                            vm.wfs = data;
                        }, function (error) {
                            $rootScope.showErrorMessage(error.message);
                        }
                    )
                }
                if (vm.type == "ECRS") {
                    ECOService.getWorkflows(eco.crType, 'CHANGES').then(
                        function (data) {
                            vm.wfs = data;
                        }, function (error) {
                            $rootScope.showErrorMessage(error.message);
                        }
                    )
                }
                if (vm.type == "VARIANCES") {
                    ECOService.getWorkflows(eco.changeClass.id, 'CHANGES').then(
                        function (data) {
                            vm.wfs = data;
                        }, function (error) {
                            $rootScope.showErrorMessage(error.message);
                        }
                    )
                }
            }

            vm.selectRevisionCreationRule = selectRevisionCreationRule;
            function selectRevisionCreationRule(value) {
                if (value == 'workflowStart') {
                    vm.changeWf.revisionCreationType = "WORKFLOW_START";
                    vm.changeWf.workflowStatus = '';
                }
                if (value == 'activityCompletion') {
                    vm.changeWf.workflowStatus = '';
                    vm.changeWf.revisionCreationType = "ACTIVITY_COMPLETION";
                    $rootScope.showBusyIndicator($('#rightSidePanel'));
                    getNormalWorkflowStatuses();
                }
                $scope.$evalAsync();

            }

            vm.onSelectWorkflow = onSelectWorkflow;
            function onSelectWorkflow() {
                getNormalWorkflowStatuses();
            }

            vm.workflowStatuses = [];
            function getNormalWorkflowStatuses() {
                $rootScope.showBusyIndicator($('#rightSidePanel'));
                WorkflowDefinitionService.getNormalWorkflowStatuses(vm.changeWf.workflowDefinition.id).then(
                    function (data) {
                        vm.workflowStatuses = data;
                        $rootScope.hideBusyIndicator();
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                        $rootScope.hideBusyIndicator();
                    }
                )
            }

            (function () {
                //if ($application.homeLoaded == true) {
                    $rootScope.$on('app.workflow.change', changeWorkflow);
                    loadWorkflows();
                //}
            })();
        }
    }
);