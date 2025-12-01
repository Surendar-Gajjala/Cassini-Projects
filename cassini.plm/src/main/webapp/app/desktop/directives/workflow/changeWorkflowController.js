define(
    [
        'app/desktop/modules/change/change.module',
        'app/shared/services/core/ecoService',
        'app/shared/services/core/varianceService',
        'app/shared/services/core/dcoService',
        'app/shared/services/core/dcrService',
        'app/shared/services/core/mcoService',
        'app/shared/services/core/ecrService',
        'app/shared/services/core/itemService',
        'app/shared/services/core/specificationsService',
        'app/shared/services/core/requirementsTypeService',
        'app/shared/services/core/projectService',
        'app/shared/services/core/activityService',
        'app/shared/services/core/mfrService',
        'app/shared/services/core/mfrPartsService',
        'app/shared/services/core/qualityTypeService',
        'app/shared/services/core/qualityWorkflowService',
        'app/shared/services/core/workflowDefinitionService',
        'app/shared/services/core/workOrderService',
        'app/shared/services/core/nprService',
        'app/shared/services/core/workflowService',
        'app/shared/services/core/supplierAuditService',
        'app/shared/services/core/mbomService',
        'app/shared/services/core/reqDocumentService',
        'app/shared/services/core/requirementService',
        'app/shared/services/core/programService',
        'app/shared/services/core/bopService',
        'app/shared/services/core/projectTemplateService',
        'app/shared/services/core/programTemplateService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/commonService',
        'app/assets/bower_components/cassini-platform/app/shared/services/security/loginService',
        'app/shared/services/core/templateActivityService'
    ],
    function (module) {
        module.controller('ChangeWorkflowController', ChangeWorkflowController);

        function ChangeWorkflowController($scope, $rootScope, $stateParams, $translate, $timeout, CommonService, ECRService, ItemService, SpecificationsService,
                                          RequirementsTypeService, MfrService, WorkOrderService, QualityWorkflowService, QualityTypeService,
                                          MfrPartsService, ProjectService, ActivityService, MCOService, WorkflowDefinitionService, ECOService, ProgramService, BOPService,
                                          VarianceService, DCOService, DCRService, NprService, WorkflowService, SupplierAuditService, ReqDocumentService, RequirementService,
                                          MBOMService, ProjectTemplateService, ProgramTemplateService, TemplateActivityService) {
            var vm = this;

            vm.workflows = [];
            var object = $scope.data.selectedObject;
            var objectType = $scope.data.selectedObjectType;
            vm.type = $scope.data.objectType;

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
                    if (vm.type == "ECO") {
                        if (validate()) {
                            ECOService.deleteWorkflow(object.id).then(
                                function () {
                                    ECOService.attachNewWorkflow(object.id, vm.changeWf.workflowDefinition.id, vm.changeWf.revisionCreationType, vm.changeWf.workflowStatus).then(
                                        function (data) {
                                            $rootScope.hideSidePanel();
                                            $rootScope.showSuccessMessage(workflowChangeMsg);
                                            $scope.callback(data);
                                        }
                                    )
                                }
                            )
                        }
                    }
                    else if (vm.type == "DCO") {
                        if (validate()) {
                            DCOService.deleteDcoWorkflow(object.id).then(
                                function () {
                                    DCOService.attachNewDcoWorkflow(object.id, vm.changeWf.workflowDefinition.id, vm.changeWf.revisionCreationType, vm.changeWf.workflowStatus).then(
                                        function (data) {
                                            $rootScope.hideSidePanel();
                                            $rootScope.showSuccessMessage(workflowChangeMsg);
                                            $scope.callback(data);
                                        }
                                    )
                                }
                            )
                        }
                    }
                    else if (vm.type == "MCO") {
                        MCOService.deleteMcoWorkflow(object.id).then(
                            function () {
                                MCOService.attachMcoWorkflow(object.id, vm.changeWf.workflowDefinition.id).then(
                                    function (data) {
                                        $rootScope.hideSidePanel();
                                        $rootScope.showSuccessMessage(workflowChangeMsg);
                                        $scope.callback(data);
                                    }
                                )
                            })
                    }
                    else if (vm.type == "DCR") {
                        DCRService.deleteDcrWorkflow(object.id).then(
                            function () {
                                DCRService.attachDcrWorkflow(object.id, vm.changeWf.workflowDefinition.id).then(
                                    function (data) {
                                        $rootScope.hideSidePanel();
                                        $rootScope.showSuccessMessage(workflowChangeMsg);
                                        $scope.callback(data);
                                    }
                                )
                            })
                    }
                    else if (vm.type == "ECR") {
                        ECRService.deleteEcrWorkflow(object.id).then(
                            function () {
                                ECRService.attachEcrWorkflow(object.id, vm.changeWf.workflowDefinition.id).then(
                                    function (data) {
                                        $rootScope.hideSidePanel();
                                        $rootScope.showSuccessMessage(workflowChangeMsg);
                                        $scope.callback(data);
                                    }
                                )
                            })
                    }
                    else if (vm.type == "VARIANCE") {
                        VarianceService.deleteVarianceWorkflow(object.id).then(
                            function () {
                                VarianceService.attachWorkflow(object.id, vm.changeWf.workflowDefinition.id).then(
                                    function (data) {
                                        $rootScope.hideSidePanel();
                                        $rootScope.showSuccessMessage(workflowChangeMsg);
                                        $scope.callback(data);
                                    }
                                )
                            })
                    }
                    else if (vm.type == "PROBLEMREPORT" || vm.type == "NCR" || vm.type == "QCR" || vm.type == "ITEMINSPECTION" || vm.type == "MATERIALINSPECTION" || vm.type == "INSPECTIONPLANREVISION") {
                        QualityWorkflowService.deleteObjectWorkflow(object.id).then(
                            function () {
                                QualityWorkflowService.attachObjectWorkflow(object.id, vm.changeWf.workflowDefinition.id, object.objectType).then(
                                    function (data) {
                                        $rootScope.hideSidePanel();
                                        $rootScope.showSuccessMessage(workflowChangeMsg);
                                        $scope.callback(data);
                                    }
                                )
                            })
                    }
                    else if (vm.type == "ITEMREVISION") {
                        ItemService.attachWorkflow(object.id, vm.changeWf.workflowDefinition.id).then(
                            function (data) {
                                $rootScope.hideSidePanel();
                                $rootScope.showSuccessMessage(workflowChangeMsg);
                                $scope.callback(data);
                            }
                        )

                    }
                    else if (vm.type == "SPECIFICATION") {
                        SpecificationsService.deleteWorkflow(object.id).then(
                            function () {
                                SpecificationsService.attachWorkflow(object.id, vm.changeWf.workflowDefinition.id).then(
                                    function (data) {
                                        $rootScope.hideSidePanel();
                                        $rootScope.showSuccessMessage(workflowChangeMsg);
                                        $scope.callback(data);
                                    }
                                )
                            })
                    }
                    else if (vm.type == "MANUFACTURER") {
                        MfrService.deleteWorkflow(object.id).then(
                            function () {
                                MfrService.attachWorkflow(object.id, vm.changeWf.workflowDefinition.id).then(
                                    function (data) {
                                        $rootScope.hideSidePanel();
                                        $rootScope.showSuccessMessage(workflowChangeMsg);
                                        $scope.callback(data);
                                    }
                                )
                            })
                    }
                    else if (vm.type == "MANUFACTURERPART") {
                        MfrPartsService.deleteWorkflow(object.id).then(
                            function () {
                                MfrPartsService.attachWorkflow(object.id, vm.changeWf.workflowDefinition.id).then(
                                    function (data) {
                                        $rootScope.hideSidePanel();
                                        $rootScope.showSuccessMessage(workflowChangeMsg);
                                        $scope.callback(data);
                                    }
                                )
                            })
                    }

                    else if (vm.type == "SUPPLIERAUDIT") {
                        SupplierAuditService.deleteWorkflow(object.id).then(
                            function () {
                                SupplierAuditService.attachWorkflow(object.id, vm.changeWf.workflowDefinition.id).then(
                                    function (data) {
                                        $rootScope.hideSidePanel();
                                        $rootScope.showSuccessMessage(workflowChangeMsg);
                                        $scope.callback(data);
                                    }
                                )
                            })
                    }
                    else if (vm.type == "PROJECT") {
                        ProjectService.deleteWorkflow(object.id).then(
                            function () {
                                ProjectService.attachWorkflow(object.id, vm.changeWf.workflowDefinition.id).then(
                                    function (data) {
                                        $rootScope.hideSidePanel();
                                        $rootScope.showSuccessMessage(workflowChangeMsg);
                                        $scope.callback(data);
                                    }
                                )
                            })
                    }
                    else if (vm.type == "PROGRAM") {
                        ProjectService.deleteWorkflow(object.id).then(
                            function () {
                                ProgramService.attachProgramWorkflow(object.id, vm.changeWf.workflowDefinition.id).then(
                                    function (data) {
                                        $rootScope.hideSidePanel();
                                        $rootScope.showSuccessMessage(workflowChangeMsg);
                                        $scope.callback(data);
                                    }
                                )
                            })
                    }
                    else if (vm.type == "PROJECTACTIVITY") {
                        /*ActivityService.deleteWorkflow(object.id).then(
                            function () {*/
                                ActivityService.attachWorkflow(object.id, vm.changeWf.workflowDefinition.id).then(
                                    function (data) {
                                        $rootScope.hideSidePanel();
                                        $rootScope.showSuccessMessage(workflowChangeMsg);
                                        $scope.callback(data);
                                    }
                                )
                            /*})*/
                    } else if (vm.type == "PROJECTTASK") {
                      /*  ActivityService.deleteTaskWorkflow(object.id).then(
                            function () {*/
                                ActivityService.attachTaskWorkflow(object.id, vm.changeWf.workflowDefinition.id).then(
                                    function (data) {
                                        $rootScope.hideSidePanel();
                                        $rootScope.showSuccessMessage(workflowChangeMsg);
                                        $scope.callback(data);
                                    }
                                )
                            /*})*/
                    }
                    else if (vm.type == "MROWORKORDER") {
                        WorkOrderService.attachWorkOrderWorkflow(object.id, vm.changeWf.workflowDefinition.id).then(
                            function (data) {
                                $rootScope.hideSidePanel();
                                $rootScope.showSuccessMessage(workflowChangeMsg);
                                $scope.callback(data);
                            }
                        )

                    } else if (vm.type == "NPR") {
                        NprService.attachNprWorkflow(object.id, vm.changeWf.workflowDefinition.id).then(
                            function (data) {
                                $rootScope.hideSidePanel();
                                $rootScope.showSuccessMessage(workflowChangeMsg);
                                $scope.callback(data);
                            }
                        )

                    } else if (vm.type == "CUSTOMOBJECT") {
                        WorkflowService.attachCustomObjectWorkflow(object.id, vm.changeWf.workflowDefinition.id).then(
                            function (data) {
                                $rootScope.hideSidePanel();
                                $rootScope.showSuccessMessage(workflowChangeMsg);
                                $scope.callback(data);
                            }
                        )

                    } else if (vm.type == "MBOMREVISION") {
                        MBOMService.attachMBOMWorkflow(object.id, vm.changeWf.workflowDefinition.id).then(
                            function (data) {
                                $rootScope.hideSidePanel();
                                $rootScope.showSuccessMessage(workflowChangeMsg);
                                $scope.callback(data);
                            }
                        )

                    }
                    else if (vm.type == "REQUIREMENTDOCUMENT") {
                        ReqDocumentService.deleteWorkflow(object.id).then(
                            function () {
                                ReqDocumentService.attachReqDocWorkflow(object.id, vm.changeWf.workflowDefinition.id).then(
                                    function (data) {
                                        $rootScope.hideSidePanel();
                                        $rootScope.showSuccessMessage(workflowChangeMsg);
                                        $scope.callback(data);
                                    }
                                )
                            })
                    }
                    else if (vm.type == "REQUIREMENT") {
                        ReqDocumentService.deleteWorkflow(object.id).then(
                            function () {
                                ReqDocumentService.attachReqWorkflow(object.id, vm.changeWf.workflowDefinition.id).then(
                                    function (data) {
                                        $rootScope.hideSidePanel();
                                        $rootScope.showSuccessMessage(workflowChangeMsg);
                                        $scope.callback(data);
                                    }
                                )
                            })
                    } else if (vm.type == "BOPREVISION") {
                        BOPService.attachBOPWorkflow(object.id, vm.changeWf.workflowDefinition.id).then(
                            function (data) {
                                $rootScope.hideSidePanel();
                                $rootScope.showSuccessMessage(workflowChangeMsg);
                                $scope.callback(data);
                            }
                        )

                    } else if (vm.type == "PROJECTTEMPLATE") {
                        ProjectTemplateService.attachProjectTempWorkflow(object.id, vm.changeWf.workflowDefinition.id).then(
                            function (data) {
                                $rootScope.hideSidePanel();
                                $rootScope.showSuccessMessage(workflowChangeMsg);
                                $scope.callback(data);
                            }
                        )

                    }

                    else if (vm.type == "TEMPLATETASK") {
                        TemplateActivityService.attachTaskTempWorkflow(object.id, vm.changeWf.workflowDefinition.id).then(
                            function (data) {
                                $rootScope.hideSidePanel();
                                $rootScope.showSuccessMessage(workflowChangeMsg);
                                $scope.callback(data);
                            }
                        )

                    }
                    else if (vm.type == "PROGRAMTEMPLATE") {
                        ProgramTemplateService.attachProgramTempWorkflow(object.id, vm.changeWf.workflowDefinition.id).then(
                            function (data) {
                                $rootScope.hideSidePanel();
                                $rootScope.showSuccessMessage(workflowChangeMsg);
                                $scope.callback(data);
                            }
                        )

                    }
                } else if (vm.changeWf.workflowDefinition == null) {
                    $rootScope.showWarningMessage(workflowChangeNoMsg);
                }
            }

            function loadWorkflows() {
                if (vm.type == "ECO" || vm.type == "DCO" || vm.type == "DCR" || vm.type == "ECR" || vm.type == "MCO" || vm.type == "VARIANCE") {
                    ECOService.getWorkflows(objectType, 'CHANGES').then(
                        function (data) {
                            vm.wfs = data;
                        }
                    )
                }
                else if (vm.type == "PROBLEMREPORT" || vm.type == "NCR" || vm.type == "QCR" || vm.type == "INSPECTIONPLANREVISION" || vm.type == "SUPPLIERAUDIT") {
                    QualityTypeService.getQualityTypeWorkflows(objectType, 'QUALITY').then(
                        function (data) {
                            vm.wfs = data;
                        }
                    )
                }
                else if (vm.type == "ITEMREVISION") {
                    ItemService.getWorkflows(objectType, 'ITEMS').then(
                        function (data) {
                            vm.wfs = data;
                        }
                    )
                }
                else if (vm.type == "SPECIFICATION") {
                    SpecificationsService.getWorkflows(objectType, 'REQUIREMENTS').then(
                        function (data) {
                            vm.wfs = data;
                        }
                    )
                }
                else if (vm.type == "MANUFACTURER") {
                    MfrService.getWorkflows(objectType, 'MANUFACTURERS').then(
                        function (data) {
                            vm.wfs = data;
                        }
                    )
                }
                else if (vm.type == "MANUFACTURERPART") {
                    MfrPartsService.getWorkflows(objectType, 'MANUFACTURER PARTS').then(
                        function (data) {
                            vm.wfs = data;
                        }
                    )
                }
                /*   else if (vm.type == "SUPPLIERAUDIT") {
                 SupplierAuditService.getWorkflows(objectType, 'SUPPLIERAUDIT').then(
                 function (data) {
                 vm.wfs = data;
                 }
                 )
                 }*/
                else if (vm.type == "PROJECT" || vm.type == "PROJECTACTIVITY" || vm.type == "PROJECTTASK" || vm.type == "PROGRAM"
                    || vm.type == "PROJECTTEMPLATE" || vm.type == "PROGRAMTEMPLATE" || vm.type == "TEMPLATETASK") {
                    var projectType = null;
                    if (vm.type == "PROJECT") {
                        projectType = "PROJECTS"
                    } else if (vm.type == "PROJECTACTIVITY") {
                        projectType = "PROJECT ACTIVITIES"
                    } else if (vm.type == "PROJECTTASK") {
                        projectType = "PROJECT TASKS"
                    } else if (vm.type == "PROGRAM") {
                        projectType = "PROGRAM"
                    } else if (vm.type == "PROJECTTEMPLATE") {
                        projectType = "PROJECT TEMPLATE";
                    } else if (vm.type == "PROGRAMTEMPLATE") {
                        projectType = "PROGRAM TEMPLATE";
                    }
                    else if (vm.type == "TEMPLATETASK") {
                        projectType = "TASK TEMPLATE";
                    }
                    ProjectService.getWorkflows(projectType).then(
                        function (data) {
                            vm.wfs = data;
                        }
                    )
                }
                else if (vm.type == "ITEMINSPECTION" || vm.type == "MATERIALINSPECTION") {
                    var inspectionType = null;
                    if (vm.type == "ITEMINSPECTION") {
                        inspectionType = "ITEM INSPECTIONS";
                    } else {
                        inspectionType = "MATERIAL INSPECTIONS";
                    }
                    ProjectService.getWorkflows(inspectionType).then(
                        function (data) {
                            vm.wfs = data;
                        }
                    )
                } else if (vm.type == "MROWORKORDER") {
                    WorkOrderService.getWorkOrderWorkflows(objectType, 'MAINTENANCE&REPAIR').then(
                        function (data) {
                            vm.wfs = data;
                        }
                    )
                } else if (vm.type == "NPR") {
                    ProjectService.getWorkflows('NPR').then(
                        function (data) {
                            vm.wfs = data;
                        }
                    )
                } else if (vm.type == "CUSTOMOBJECT") {
                    WorkflowService.getCustomObjectWorkflows(objectType, 'CUSTOM OBJECTS').then(
                        function (data) {
                            vm.wfs = data;
                        }
                    )
                } else if (vm.type == "MBOMREVISION") {
                    MBOMService.getMESTypeWorkflows(objectType, 'MANUFACTURING').then(
                        function (data) {
                            vm.wfs = data;
                        }
                    )
                }
                else if (vm.type == "REQUIREMENTDOCUMENT") {
                    ReqDocumentService.getReqDocWorkflows(objectType, 'REQUIREMENT DOCUMENTS').then(
                        function (data) {
                            vm.wfs = data;
                        }
                    )
                }
                else if (vm.type == "REQUIREMENT") {
                    RequirementService.getReqWorkflows(objectType, 'REQUIREMENT').then(
                        function (data) {
                            vm.wfs = data;
                        }
                    )
                }
                else if (vm.type == "BOPREVISION") {
                    BOPService.getMESTypeWorkflows(objectType, 'MANUFACTURING').then(
                        function (data) {
                            vm.wfs = data;
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
)
;