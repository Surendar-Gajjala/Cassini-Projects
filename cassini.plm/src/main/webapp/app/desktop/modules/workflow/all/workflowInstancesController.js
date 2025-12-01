define(
    [
        'app/desktop/modules/workflow/workflow.module',
        'app/shared/services/core/workflowService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/objectService'
    ],
    function (module) {
        module.controller('WorkflowInstanceController', WorkflowInstanceController);

        function WorkflowInstanceController($scope, $stateParams, $state, $window, $rootScope, $translate, ObjectService, WorkflowService) {

            var vm = this;
            vm.loading = true;
            vm.workflows = [];
            vm.workflow = $scope.data.workflow;
            vm.mode = $scope.data.mode;

            var parse = angular.element("<div></div>");
            $scope.wfHistory = parse.html($translate.instant('CLICK_TO_SHOW_WFH')).html();

            vm.gotoWorkflow = gotoWorkflow;
            function gotoWorkflow(workflow) {
                $state.go('app.workflow.editor', {mode: 'edit', workflow: workflow.workflowRevision});
            }

            function loadWorkflows() {
                WorkflowService.getWorkflowsByDefName(vm.workflow.id).then(
                    function (data) {
                        vm.workflows = data;
                        if (vm.workflows.length > 0) {
                            vm.selectedType = vm.workflows[0].objectType;
                        }
                        var objects = [];
                        for (var i = 0; i < vm.workflows.length; i++) {
                            objects.push(vm.workflows[i].workflow);
                        }
                        ObjectService.getObjectReferences(objects, "PLMWORKFLOWDEFINITION", "workflowRevision");
                        vm.loading = false;
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                        $rootScope.hideBusyIndicator();
                    }
                )
            }

            function loadMasterWorkflows() {
                WorkflowService.getMasterWorkflows(vm.workflow.id).then(
                    function (data) {
                        vm.workflows = data;
                        if (vm.workflows.length > 0) {
                            vm.selectedType = vm.workflows[0].objectType;
                        }
                        var objects = [];
                        for (var i = 0; i < vm.workflows.length; i++) {
                            objects.push(vm.workflows[i].workflow);
                        }
                        ObjectService.getObjectReferences(objects, "PLMWORKFLOWDEFINITION", "workflowRevision");
                        vm.loading = false;
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                        $rootScope.hideBusyIndicator();
                    }
                )
            }

            vm.showWorkflowHistory = showWorkflowHistory;

            function showWorkflowHistory(object) {
                var options = {
                    title: "Workflow History",
                    template: 'app/desktop/modules/home/widgets/workflows/workflowHistoryView.jsp',
                    controller: 'WidgetWorkflowHistoryController as workflowHistoryVm',
                    resolve: 'app/desktop/modules/home/widgets/workflows/workflowHistoryController',
                    width: 600,
                    side: "left",
                    data: {
                        selectedWorkflow: object.workflow,
                        selectedObjectNumber: object.number
                    },
                    callback: function (data) {

                    }
                };
                $rootScope.showSidePanel(options);
            }

            vm.showInstanceDetails = showInstanceDetails;
            function showInstanceDetails(workflow) {
                if (workflow.objectType == "ITEM") {
                    $state.go('app.items.details', {itemId: workflow.workflow.attachedTo, tab: 'details.workflow'});
                } else if (workflow.objectType == "ECO") {
                    $state.go('app.changes.eco.details', {
                        ecoId: workflow.workflow.attachedTo,
                        tab: 'details.workflow'
                    });
                } else if (workflow.objectType == "DCO") {
                    $state.go('app.changes.dco.details', {
                        dcoId: workflow.workflow.attachedTo,
                        tab: 'details.workflow'
                    });
                } else if (workflow.objectType == "DCR") {
                    $state.go('app.changes.dcr.details', {
                        dcrId: workflow.workflow.attachedTo,
                        tab: 'details.workflow'
                    });
                } else if (workflow.objectType == "ECR") {
                    $state.go('app.changes.ecr.details', {
                        ecrId: workflow.workflow.attachedTo,
                        tab: 'details.workflow'
                    });
                } else if (workflow.objectType == "MCO") {
                    $state.go('app.changes.mco.details', {
                        mcoId: workflow.workflow.attachedTo,
                        tab: 'details.workflow'
                    });
                } else if (workflow.objectType == "VARIANCES") {
                    $state.go('app.changes.variance.details', {
                        varianceId: workflow.workflow.attachedTo,
                        tab: 'details.workflow'
                    });
                } else if (workflow.objectType == "INSPECTIONPLANREVISION") {
                    $state.go('app.pqm.inspectionPlan.details', {
                        planId: workflow.workflow.attachedTo,
                        tab: 'details.workflow'
                    })
                } else if (workflow.objectType == "ITEMINSPECTION") {
                    $state.go('app.pqm.inspection.details', {
                        inspectionId: workflow.workflow.attachedTo,
                        tab: 'details.workflow'
                    })
                } else if (workflow.objectType == "PROBLEMREPORT") {
                    $state.go('app.pqm.pr.details', {
                        problemReportId: workflow.workflow.attachedTo,
                        tab: 'details.workflow'
                    })
                } else if (workflow.objectType == "NCR") {
                    $state.go('app.pqm.ncr.details', {ncrId: workflow.workflow.attachedTo, tab: 'details.workflow'})
                } else if (workflow.objectType == "QCR") {
                    $state.go('app.pqm.qcr.details', {qcrId: workflow.workflow.attachedTo, tab: 'details.workflow'})
                } else if (workflow.type == "PROJECT") {
                    $state.go('app.pm.project.details', {
                        projectId: workflow.workflow.attachedTo,
                        tab: 'details.workflow'
                    });
                } else if (workflow.objectType == "SPECIFICATION") {
                    $state.go('app.rm.specifications.details', {
                        specId: workflow.workflow.attachedTo,
                        tab: 'details.workflow'
                    });
                } else if (workflow.objectType == "REQUIREMENT") {
                    $state.go('app.rm.requirements.details', {
                        requirementId: workflow.workflow.attachedTo,
                        tab: 'details.workflow'
                    });
                } else if (workflow.objectType == "MANUFACTURERPARTS") {
                    $state.go('app.mfr.mfrparts.details', {
                        mfrId: workflow.id,
                        manufacturePartId: workflow.workflow.attachedTo, tab: 'details.workflow'
                    });
                } else if (workflow.objectType == "MANUFACTURERS") {
                    $state.go('app.mfr.details', {
                        manufacturerId: workflow.workflow.attachedTo,
                        tab: 'details.workflow'
                    });
                } else if (workflow.type == "PROJECTACTIVITY") {
                    $state.go('app.pm.project.activity.details', {
                        activityId: workflow.workflow.attachedTo,
                        tab: 'details.workflow'
                    });
                } else if (workflow.type == "PLMNPR") {
                    $state.go('app.nprs.details', {nprId: workflow.workflow.attachedTo, tab: 'details.workflow'});
                } else if (workflow.type == "WORKORDER") {
                    $state.go('app.mro.workOrder.details', {
                        workOrderId: workflow.workflow.attachedTo,
                        tab: 'details.workflow'
                    });
                } else if (workflow.type == "CUSTOMOBJECT") {
                    $state.go('app.customobjects.details', {
                        customId: workflow.workflow.attachedTo,
                        tab: 'details.workflow'
                    });
                } else if (workflow.type == "PROGRAMTEMPLATE") {
                    $state.go('app.pm.programtemplate.details', {
                        templateId: workflow.workflow.attachedTo,
                        tab: "details.workflow"
                    });
                } else if (workflow.type == "PROGRAM") {
                    $state.go('app.pm.program.details', {
                        programId: workflow.workflow.attachedTo,
                        tab: 'details.workflow'
                    });
                } else if (workflow.type == "TEMPLATE") {
                    $state.go('app.templates.details', {
                        templateId: workflow.workflow.attachedTo,
                        tab: 'details.workflow'
                    });
                } else if (workflow.type == "TEMPLATETASK") {
                    $state.go('app.templates.task.details', {
                        taskId: workflow.workflow.attachedTo,
                        tab: 'details.workflow'
                    });
                } else if (workflow.type == "TEMPLATEACTIVITY") {
                    $state.go('app.templates.activity.details', {
                        activityId: workflow.workflow.attachedTo,
                        tab: 'details.workflow'
                    });
                } else if (workflow.type == "PROJECTACTIVITY") {
                    $state.go('app.pm.project.activity.details', {
                        activityId: workflow.workflow.attachedTo,
                        tab: 'details.workflow'
                    });
                } else if (workflow.type == "PROJECTTASK") {
                    $state.go('app.pm.project.activity.task.details', {
                        activityId: workflow.task.activity,
                        taskId: workflow.workflow.attachedTo,
                        tab: 'details.workflow'
                    });
                }
            }

            (function () {
                var mode = $scope.data.mode;
                if (mode == "Master") {
                    loadMasterWorkflows();
                } else if (mode == "Revision") {
                    loadWorkflows();
                }
            })();
        }
    }
);