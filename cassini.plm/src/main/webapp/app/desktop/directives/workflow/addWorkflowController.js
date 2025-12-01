define(
    [
        'app/desktop/modules/item/item.module',
        'app/shared/services/core/itemService',
        'app/shared/services/core/specificationsService',
        'app/shared/services/core/requirementsTypeService',
        'app/shared/services/core/projectService',
        'app/shared/services/core/activityService',
        'app/shared/services/core/mfrService',
        'app/shared/services/core/mfrPartsService',
        'app/shared/services/core/workOrderService',
        'app/shared/services/core/nprService',
        'app/shared/services/core/supplierAuditService',
        'app/shared/services/core/workflowService',
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
        module.controller('AddWorkflowController', AddWorkflowController);

        function AddWorkflowController($scope, $rootScope, $stateParams, $translate, $timeout, CommonService, ItemService, MfrService,
                                       MfrPartsService, SpecificationsService, RequirementsTypeService, ProjectService, ActivityService, ProgramService, BOPService,
                                       WorkOrderService, NprService, SupplierAuditService, WorkflowService, ReqDocumentService, RequirementService, ProjectTemplateService, TemplateActivityService,
                                       ProgramTemplateService) {
            var vm = this;

            vm.workflows = [];
            var object = $scope.data.selectedObject;
            var objectType = $scope.data.selectedObjectType;
            vm.type = $scope.data.objectType;

            var parsed = angular.element("<div></div>");
            vm.selectWorkflow = parsed.html($translate.instant("SELECT")).html();
            var workflowAttached = parsed.html($translate.instant("WORKFLOW_ATTACHED_MSG")).html();
            var workflowChangeNoMsg = parsed.html($translate.instant("WORKFLOW_CHANGE_NO_MSG")).html();

            vm.addWf = {
                workflowDefinition: null
            };

            function addWorkflow() {
                if (vm.addWf.workflowDefinition != null) {
                    if (vm.type == "ITEMREVISION") {
                        ItemService.attachWorkflow(object.id, vm.addWf.workflowDefinition.id).then(
                            function (data) {
                                $rootScope.hideSidePanel();
                                $rootScope.showSuccessMessage(workflowAttached);
                                $scope.callback(data);
                            }
                        )
                    }
                    else if (vm.type == "SPECIFICATION") {
                        SpecificationsService.attachWorkflow(object.id, vm.addWf.workflowDefinition.id).then(
                            function (data) {
                                $rootScope.hideSidePanel();
                                $rootScope.showSuccessMessage(workflowAttached);
                                $scope.callback(data);
                            }
                        )
                    }
                    else if (vm.type == "MANUFACTURER") {
                        MfrService.attachWorkflow(object.id, vm.addWf.workflowDefinition.id).then(
                            function (data) {
                                $rootScope.hideSidePanel();
                                $rootScope.showSuccessMessage(workflowAttached);
                                $scope.callback(data);
                            }
                        )
                    }
                    else if (vm.type == "MANUFACTURERPART") {
                        MfrPartsService.attachWorkflow(object.id, vm.addWf.workflowDefinition.id).then(
                            function (data) {
                                $rootScope.hideSidePanel();
                                $rootScope.showSuccessMessage(workflowAttached);
                                $scope.callback(data);
                            }
                        )
                    }

                    else if (vm.type == "SUPPLIERAUDIT") {
                        SupplierAuditService.attachWorkflow(object.id, vm.addWf.workflowDefinition.id).then(
                            function (data) {
                                $rootScope.hideSidePanel();
                                $rootScope.showSuccessMessage(workflowAttached);
                                $scope.callback(data);
                            }
                        )
                    }
                    else if (vm.type == "PROJECT") {
                        ProjectService.attachWorkflow(object.id, vm.addWf.workflowDefinition.id).then(
                            function (data) {
                                $rootScope.hideSidePanel();
                                $rootScope.showSuccessMessage(workflowAttached);
                                $scope.callback(data);
                            }
                        )
                    }
                    else if (vm.type == "PROJECTACTIVITY") {
                        ActivityService.attachWorkflow(object.id, vm.addWf.workflowDefinition.id).then(
                            function (data) {
                                $rootScope.hideSidePanel();
                                $rootScope.showSuccessMessage(workflowAttached);
                                $scope.callback(data);
                            }
                        )
                    }
                    else if (vm.type == "PROJECTTASK") {
                        ActivityService.attachTaskWorkflow(object.id, vm.addWf.workflowDefinition.id).then(
                            function (data) {
                                $rootScope.hideSidePanel();
                                $rootScope.showSuccessMessage(workflowAttached);
                                $scope.callback(data);
                            }
                        )
                    }
                    else if (vm.type == "PROGRAM") {
                        ProgramService.attachProgramWorkflow(object.id, vm.addWf.workflowDefinition.id).then(
                            function (data) {
                                $rootScope.hideSidePanel();
                                $rootScope.showSuccessMessage(workflowAttached);
                                $scope.callback(data);
                            }
                        )
                    }
                    else if (vm.type == "MROWORKORDER") {
                        WorkOrderService.attachWorkOrderWorkflow(object.id, vm.addWf.workflowDefinition.id).then(
                            function (data) {
                                $rootScope.hideSidePanel();
                                $rootScope.showSuccessMessage(workflowAttached);
                                $scope.callback(data);
                            }
                        )

                    } else if (vm.type == "NPR") {
                        NprService.attachNprWorkflow(object.id, vm.addWf.workflowDefinition.id).then(
                            function (data) {
                                $rootScope.hideSidePanel();
                                $rootScope.showSuccessMessage(workflowAttached);
                                $scope.callback(data);
                            }
                        )
                    } else if (vm.type == "CUSTOMOBJECT") {
                        WorkflowService.attachCustomObjectWorkflow(object.id, vm.addWf.workflowDefinition.id).then(
                            function (data) {
                                $rootScope.hideSidePanel();
                                $rootScope.showSuccessMessage(workflowAttached);
                                $scope.callback(data);
                            }
                        )
                    } else if (vm.type == "REQUIREMENTDOCUMENT") {
                        ReqDocumentService.attachReqDocWorkflow(object.id, vm.addWf.workflowDefinition.id).then(
                            function (data) {
                                $rootScope.hideSidePanel();
                                $rootScope.showSuccessMessage(workflowAttached);
                                $scope.callback(data);
                            }
                        )
                    } else if (vm.type == "REQUIREMENT") {
                        ReqDocumentService.attachReqWorkflow(object.id, vm.addWf.workflowDefinition.id).then(
                            function (data) {
                                $rootScope.hideSidePanel();
                                $rootScope.showSuccessMessage(workflowAttached);
                                $scope.callback(data);
                            }
                        )
                    } else if (vm.type == "BOPREVISION") {
                        BOPService.attachBOPWorkflow(object.id, vm.addWf.workflowDefinition.id).then(
                            function (data) {
                                $rootScope.hideSidePanel();
                                $rootScope.showSuccessMessage(workflowAttached);
                                $scope.callback(data);
                            }
                        )

                    } else if (vm.type == "PROJECTTEMPLATE") {
                        ProjectTemplateService.attachProjectTempWorkflow(object.id, vm.addWf.workflowDefinition.id).then(
                            function (data) {
                                $rootScope.hideSidePanel();
                                $rootScope.showSuccessMessage(workflowAttached);
                                $scope.callback(data);
                            }
                        )

                    } else if (vm.type == "PROGRAMTEMPLATE") {
                        ProgramTemplateService.attachProgramTempWorkflow(object.id, vm.addWf.workflowDefinition.id).then(
                            function (data) {
                                $rootScope.hideSidePanel();
                                $rootScope.showSuccessMessage(workflowAttached);
                                $scope.callback(data);
                            }
                        )

                    }
                    else if (vm.type == "TEMPLATETASK") {
                        TemplateActivityService.attachTaskTempWorkflow(object.id, vm.addWf.workflowDefinition.id).then(
                            function (data) {
                                $rootScope.hideSidePanel();
                                $rootScope.showSuccessMessage(workflowAttached);
                                $scope.callback(data);
                            }
                        )

                    }


                } else if (vm.addWf.workflowDefinition == null) {
                    $rootScope.showWarningMessage(workflowChangeNoMsg)
                }

            }

            function loadWorkflows() {
                if (vm.type == "ITEMREVISION") {
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

                else if (vm.type == "SUPPLIERAUDIT") {
                    SupplierAuditService.getWorkflows(objectType, 'QUALITY').then(
                        function (data) {
                            vm.wfs = data;
                        }
                    )
                }
                else if (vm.type == "PROJECT" || vm.type == "PROJECTACTIVITY" || vm.type == "PROJECTTASK" || vm.type == "PROGRAM"
                    || vm.type == "PROJECTTEMPLATE" || vm.type == "PROGRAMTEMPLATE" || vm.type == "TEMPLATETASK") {
                    var projectType = null;
                    if (vm.type == "PROJECT") {
                        projectType = "PROJECTS";
                    } else if (vm.type == "PROJECTACTIVITY") {
                        projectType = "PROJECT ACTIVITIES";
                    } else if (vm.type == "PROJECTTASK") {
                        projectType = "PROJECT TASKS";
                    } else if (vm.type == "PROGRAM") {
                        projectType = "PROGRAM";
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
                } else if (vm.type == "REQUIREMENTDOCUMENT") {
                    ReqDocumentService.getReqDocWorkflows(objectType, 'REQUIREMENT DOCUMENTS').then(
                        function (data) {
                            vm.wfs = data;
                        }
                    )
                } else if (vm.type == "REQUIREMENT") {
                    RequirementService.getReqWorkflows(objectType, 'REQUIREMENT').then(
                        function (data) {
                            vm.wfs = data;
                        }
                    )
                } else if (vm.type == "BOPREVISION") {
                    BOPService.getMESTypeWorkflows(objectType, 'MANUFACTURING').then(
                        function (data) {
                            vm.wfs = data;
                        }
                    )
                }
            }

            (function () {
                //if ($application.homeLoaded == true) {
                $rootScope.$on('app.workflow.add', addWorkflow);
                loadWorkflows();
                //}
            })();
        }
    }
);