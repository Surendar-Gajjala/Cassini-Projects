define(
    [
        'app/desktop/modules/home/home.module'
    ],
    function (module) {
        module.controller('NewObjectController', NewObjectController);

        function NewObjectController($scope, $rootScope, $state, $timeout, $cookies, $translate) {
            var vm = this;
            var parsed = angular.element("<div></div>");
            var newItemTitle = $translate.instant("NEW_ITEM_TITLE");
            var newProjectTitle = $translate.instant("NEW_PROJECT");
            var ecoTitle = $translate.instant("ECO_ALL_NEW_ECO");
            var newManufacturerTitle = $translate.instant("NEW_MANUFACTURER_TITLE");
            var newManufacturerPartTitle = $translate.instant("NEW_MANUFACTURER_PART_TITLE");
            var newSpecTitle = $translate.instant("NEW_SPECIFICATION");
            var newEcrTitle = $translate.instant("NEW_ECR");
            var newDcrTitle = $translate.instant("NEW_DCR");
            var newDcoTitle = $translate.instant("NEW_DCO");
            var newMcoTitle = $translate.instant("NEW_MCO");
            var newProblemReportTitle = $translate.instant("NEW_PROBLEM_REPORT");
            var newNcrTitle = $translate.instant("NEW_NCR");
            var newQcrTitle = $translate.instant("NEW_QCR");
            var createText = $translate.instant("CREATE");
            var inspectionPlanTitle = parsed.html($translate.instant("NEW_INSPECTION_PLAN")).html();
            var productInspectionPlanTitle = parsed.html($translate.instant("NEW_PRODUCT_INSPECTION_PLAN")).html();
            var materialInspectionPlanTitle = parsed.html($translate.instant("NEW_MATERIAL_INSPECTION_PLAN")).html();
            var newInspectionTitle = parsed.html($translate.instant("NEW_INSPECTION")).html();
            var itemInspectionTitle = parsed.html($translate.instant("NEW_ITEM_INSPECTION")).html();
            var materialInspectionTitle = parsed.html($translate.instant("NEW_MATERIAL_INSPECTION")).html();
            var newCustomerTitle = $translate.instant("NEW_CUSTOMER");
            var newPpapTitle = parsed.html($translate.instant("NEW_PPAP")).html();
            var newSupplierAuditTitle = parsed.html($translate.instant("NEW_SUPPLIER_AUDIT")).html();

            vm.newItem = newItem;
            vm.newProject = newProject;
            vm.newECR = newECR;
            vm.newECO = newECO;
            vm.newDCR = newDCR;
            vm.newDCO = newDCO;
            vm.newMCO = newMCO;
            vm.newDeviation = newDeviation;
            vm.newWaiver = newWaiver;

            vm.newInspectionPlan = newInspectionPlan;
            vm.newInspection = newInspection;
            vm.newProblemReport = newProblemReport;
            vm.newNCR = newNCR;
            vm.newQCR = newQCR;

            vm.newManufacturer = newManufacturer;
            vm.newManufacturerPart = newManufacturerPart;
            vm.newSupplier = newSupplier;

            vm.newCustomer = newCustomer;

            vm.newWorkCenter = newWorkCenter;
            vm.newEquipment = newEquipment;
            vm.newInstrument = newInstrument;
            vm.newTool = newTool;

            vm.newSpecification = newSpecification;
            vm.newDeclaration = newDeclaration;

            vm.newAsset = newAsset;
            vm.newWorkOrder = newWorkOrder;
            vm.newWorkRequest = newWorkRequest;
            vm.newMaintenancePlan = newMaintenancePlan;
            vm.newReqDocument = newReqDocument;

            vm.newWorkflow = newWorkflow;
            $rootScope.searchType = null;


            function newItem() {
                var options = {
                    title: newItemTitle,
                    template: 'app/desktop/modules/item/new/newItemView.jsp',
                    controller: 'NewItemController as newItemVm',
                    resolve: 'app/desktop/modules/item/new/newItemController',
                    width: 600,
                    data: {
                        itemsMode: ''
                    },
                    showMask: true,
                    buttons: [
                        {text: createText, broadcast: 'app.items.new'}
                    ],
                    callback: function (itemRevision) {
                        $rootScope.hideSidePanel();
                        $timeout(function () {
                            $state.go('app.items.details', {itemId: itemRevision.id});
                        }, 1000);
                    }
                };

                $rootScope.showSidePanel(options);

            }

            function newProject() {
                var options = {
                    title: newProjectTitle,
                    showMask: true,
                    template: 'app/desktop/modules/pm/project/new/newProjectDialog.jsp',
                    controller: 'NewProjectController as newProjectVm',
                    resolve: 'app/desktop/modules/pm/project/new/newProjectDialogController',
                    width: 600,
                    data: {
                        projectCreationFrom: "",
                        selectedProgramId: null
                    },
                    buttons: [
                        {text: createText, broadcast: 'app.project.new'}
                    ],
                    callback: function (newProject) {
                        $rootScope.hideSidePanel();
                        $timeout(function () {
                            $state.go('app.pm.project.details', {projectId: newProject.id});
                        }, 1000);
                    }
                };

                $rootScope.showSidePanel(options);
            }

            function newECO() {
                var options = {
                    title: ecoTitle,
                    template: 'app/desktop/modules/change/eco/new/newEcoView.jsp',
                    controller: 'NewECOController as newEcoVm',
                    resolve: 'app/desktop/modules/change/eco/new/newEcoController',
                    width: 600,
                    showMask: true,
                    buttons: [
                        {text: createText, broadcast: 'app.changes.ecos.new'}
                    ],
                    callback: function (newEco) {
                        $rootScope.hideSidePanel();
                        $timeout(function () {
                            $state.go('app.changes.eco.details', {ecoId: newEco.id});
                        }, 1000);
                    }
                };

                $rootScope.showSidePanel(options);
            }

            function newManufacturer() {
                var options = {
                    title: newManufacturerTitle,
                    template: 'app/desktop/modules/mfr/new/newMfrView.jsp',
                    controller: 'NewMfrController as newMfrVm',
                    resolve: 'app/desktop/modules/mfr/new/newMfrController',
                    width: 600,
                    showMask: true,
                    buttons: [
                        {text: createText, broadcast: 'app.mfrs.new'}
                    ],
                    callback: function (newMfr) {
                        $rootScope.hideSidePanel();
                        $timeout(function () {
                            $state.go('app.mfr.details', {manufacturerId: newMfr.id});
                        }, 1000);
                    }
                };

                $rootScope.showSidePanel(options);
            }


            function newManufacturerPart() {
                var options = {
                    title: newManufacturerPartTitle,
                    template: 'app/desktop/modules/mfr/all/newManufacturerPartView.jsp',
                    controller: 'NewManufacturerPartController as newManufacturerPartVm',
                    resolve: 'app/desktop/modules/mfr/all/newManufacturerPartController',
                    width: 600,
                    showMask: true,
                    buttons: [
                        {text: createText, broadcast: 'app.mfr.mfrPart.new'}
                    ],
                    callback: function (newMfr) {
                        $rootScope.hideSidePanel();
                        $timeout(function () {
                            $state.go('app.mfr.mfrparts.details', {
                                mfrId: newMfr.manufacturer,
                                manufacturePartId: newMfr.id
                            });
                        }, 1000);
                    }
                };

                $rootScope.showSidePanel(options);
            }

            function newECR() {
                var options = {
                    title: newEcrTitle,
                    template: 'app/desktop/modules/change/ecr/new/newEcrView.jsp',
                    controller: 'NewEcrController as newEcrVm',
                    resolve: 'app/desktop/modules/change/ecr/new/newEcrController',
                    width: 600,
                    showMask: true,
                    buttons: [
                        {text: createText, broadcast: 'app.ecrs.new'}
                    ],
                    callback: function (ecr) {
                        $timeout(function () {
                            $state.go('app.changes.ecr.details', {ecrId: ecr.id, tab: 'details.basic'});
                        }, 500);
                    }
                };
                $rootScope.showSidePanel(options);
            }

            function newDCR() {
                var options = {
                    title: newDcrTitle,
                    template: 'app/desktop/modules/change/dcr/new/newDCRView.jsp',
                    controller: 'NewDCRController as newDcrVm',
                    resolve: 'app/desktop/modules/change/dcr/new/newDCRController',
                    width: 600,
                    showMask: true,
                    buttons: [
                        {text: createText, broadcast: 'app.dcr.new'}
                    ],
                    callback: function (dcr) {
                        $timeout(function () {
                            $state.go('app.changes.dcr.details', {dcrId: dcr.id, tab: 'details.basic'});
                        }, 500);
                    }
                };

                $rootScope.showSidePanel(options);
            }

            function newDCO() {
                var options = {
                    title: newDcoTitle,
                    template: 'app/desktop/modules/change/dco/new/newDCOView.jsp',
                    controller: 'NewDCOController as newDcoVm',
                    resolve: 'app/desktop/modules/change/dco/new/newDCOController',
                    width: 600,
                    showMask: true,
                    buttons: [
                        {text: createText, broadcast: 'app.dco.new'}
                    ],
                    callback: function (dco) {
                        $timeout(function () {
                            $state.go('app.changes.dco.details', {dcoId: dco.id, tab: 'details.basic'});
                        }, 500);
                    }
                };

                $rootScope.showSidePanel(options);
            }

            function newMCO(type) {
                $rootScope.allMCOType = type;
                var options = {
                    title: newMcoTitle,
                    template: 'app/desktop/modules/change/mco/new/newMcoView.jsp',
                    controller: 'NewMCOController as newMcoVm',
                    resolve: 'app/desktop/modules/change/mco/new/newMcoController',
                    width: 600,
                    data: {
                        mcoType: $rootScope.allMCOType
                    },
                    showMask: true,
                    buttons: [
                        {text: createText, broadcast: 'app.mcos.new'}
                    ],
                    callback: function (mco) {
                        $timeout(function () {
                            $state.go('app.changes.mco.details', {mcoId: mco.id, tab: 'details.basic'});
                        }, 500);
                    }
                };

                $rootScope.showSidePanel(options);
            }

            function newInspectionPlan(type) {
                var title = inspectionPlanTitle;
                var options = {
                    title: title,
                    template: 'app/desktop/modules/pqm/inspectionPlan/new/newInspectionPlanView.jsp',
                    controller: 'NewInspectionPlanController as newInspectionPlanVm',
                    resolve: 'app/desktop/modules/pqm/inspectionPlan/new/newInspectionPlanController',
                    width: 600,
                    showMask: true,
                    data: {
                        inspectionPlanType: "PRODUCTINSPECTIONPLAN",
                        actionType: "home"
                    },
                    buttons: [
                        {text: createText, broadcast: 'app.inspectionPlans.new'}
                    ],
                    callback: function (plan) {
                        $timeout(function () {
                            $state.go('app.pqm.inspectionPlan.details', {
                                planId: plan.latestRevision,
                                tab: 'details.basic'
                            });
                        }, 500);
                    }
                };

                $rootScope.showSidePanel(options);
            }

            function newInspection() {
                var options = {
                    title: newInspectionTitle,
                    template: 'app/desktop/modules/pqm/inspection/new/newInspectionView.jsp',
                    controller: 'NewInspectionController as newInspectionVm',
                    resolve: 'app/desktop/modules/pqm/inspection/new/newInspectionController',
                    width: 600,
                    showMask: true,
                    data: {
                        inspectionType: 'ITEMINSPECTION',
                        actionType: "home"
                    },
                    buttons: [
                        {text: createText, broadcast: 'app.inspections.new'}
                    ],
                    callback: function (inspection) {
                        $timeout(function () {
                            $state.go('app.pqm.inspection.details', {inspectionId: inspection.id, tab: 'details.basic'})
                        }, 500);
                    }
                };

                $rootScope.showSidePanel(options);
            }

            function newProblemReport() {
                var options = {
                    title: newProblemReportTitle,
                    template: 'app/desktop/modules/pqm/problemReport/new/newProblemReportView.jsp',
                    controller: 'NewProblemReportController as newProblemReportVm',
                    resolve: 'app/desktop/modules/pqm/problemReport/new/newProblemReportController',
                    width: 600,
                    showMask: true,
                    buttons: [
                        {text: createText, broadcast: 'app.problemReports.new'}
                    ],
                    callback: function (problemReport) {
                        $timeout(function () {
                            $state.go("app.pqm.pr.details", {problemReportId: problemReport.id, tab: 'details.basic'});
                        }, 500);
                    }
                };

                $rootScope.showSidePanel(options);
            }

            function newNCR() {
                var options = {
                    title: newNcrTitle,
                    template: 'app/desktop/modules/pqm/ncr/new/newNcrView.jsp',
                    controller: 'NewNcrController as newNcrVm',
                    resolve: 'app/desktop/modules/pqm/ncr/new/newNcrController',
                    width: 600,
                    showMask: true,
                    buttons: [
                        {text: createText, broadcast: 'app.ncrs.new'}
                    ],
                    callback: function (ncr) {
                        $timeout(function () {
                            $state.go("app.pqm.ncr.details", {ncrId: ncr.id, tab: 'details.basic'});
                        }, 500);
                    }
                };

                $rootScope.showSidePanel(options);
            }

            function newQCR() {
                var options = {
                    title: newQcrTitle,
                    template: 'app/desktop/modules/pqm/qcr/new/newQcrView.jsp',
                    controller: 'NewQcrController as newQcrVm',
                    resolve: 'app/desktop/modules/pqm/qcr/new/newQcrController',
                    width: 600,
                    showMask: true,
                    buttons: [
                        {text: createText, broadcast: 'app.qcrs.new'}
                    ],
                    callback: function (qcr) {
                        $timeout(function () {
                            $state.go("app.pqm.qcr.details", {qcrId: qcr.id, tab: 'details.basic'});
                        }, 500);
                    }
                };

                $rootScope.showSidePanel(options);
            }

            function newDeviation() {
                $rootScope.varianceType = "Deviation";
                newVariance();
            }

            function newWaiver() {
                $rootScope.varianceType = "Waiver";
                newVariance();
            }

            var varianceTitle = null;

            function newVariance() {
                if ($rootScope.varianceType == "Deviation") {
                    varianceTitle = $translate.instant("NEW_DEVIATION");
                } else if ($rootScope.varianceType == "Waiver") {
                    varianceTitle = $translate.instant("NEW_WAIVER");
                }
                var options = {
                    title: varianceTitle,
                    template: 'app/desktop/modules/change/variance/new/newVarianceView.jsp',
                    controller: 'NewVarianceController as newVarianceVm',
                    resolve: 'app/desktop/modules/change/variance/new/newVarianceController',
                    width: 600,
                    showMask: true,
                    buttons: [
                        {text: createText, broadcast: 'app.variance.new'}
                    ],
                    callback: function (variance) {
                        $timeout(function () {
                            $state.go('app.changes.variance.details', {varianceId: variance.id, tab: 'details.basic'});
                        }, 500);
                    }
                };

                $rootScope.showSidePanel(options);
            }

            function newCustomer() {
                var options = {
                    title: newCustomerTitle,
                    template: 'app/desktop/modules/customer/new/newCustomerView.jsp',
                    controller: 'NewCustomerController as newCustomerVm',
                    resolve: 'app/desktop/modules/customer/new/newCustomerController',
                    width: 600,
                    showMask: true,
                    data: {
                        mode: "NEW",
                        customerDetails: null
                    },
                    buttons: [
                        {text: createText, broadcast: 'app.customers.new'}
                    ],
                    callback: function () {

                    }
                };

                $rootScope.showSidePanel(options);
            }

            var newWorkCenterHeading = parsed.html($translate.instant("NEW_WORKCENTER")).html();

            function newWorkCenter() {
                var options = {
                    title: newWorkCenterHeading,
                    template: 'app/desktop/modules/mes/workCenter/new/newWorkCenterView.jsp',
                    controller: 'NewWorkCenterController as newWorkCenterVm',
                    resolve: 'app/desktop/modules/mes/workCenter/new/newWorkCenterController',
                    width: 600,
                    showMask: true,
                    buttons: [
                        {text: createText, broadcast: 'app.workCenter.new'}
                    ],
                    callback: function (workCenter) {

                    }
                };

                $rootScope.showSidePanel(options);
            }


            var newEquipmentHeading = parsed.html($translate.instant("NEW_EQUIPMENT_TYPE")).html();

            function newEquipment() {
                var options = {
                    title: newEquipmentHeading,
                    template: 'app/desktop/modules/mes/equipment/new/newEquipmentView.jsp',
                    controller: 'NewEquipmentController as newEquipmentVm',
                    resolve: 'app/desktop/modules/mes/equipment/new/newEquipmentController',
                    width: 600,
                    showMask: true,
                    buttons: [
                        {text: createText, broadcast: 'app.equipment.new'}
                    ],
                    callback: function (equipment) {

                    }
                };

                $rootScope.showSidePanel(options);
            }


            var newInstrumentHeading = parsed.html($translate.instant("NEW_INSTRUMENT_TYPE")).html();

            function newInstrument() {
                var options = {
                    title: newInstrumentHeading,
                    template: 'app/desktop/modules/mes/instrument/new/newInstrumentView.jsp',
                    controller: 'NewInstrumentController as newInstrumentVm',
                    resolve: 'app/desktop/modules/mes/instrument/new/newInstrumentController',
                    width: 600,
                    showMask: true,
                    buttons: [
                        {text: createText, broadcast: 'app.instrument.new'}
                    ],
                    callback: function (instrument) {
                        $state.go('app.mes.masterData.instrument.details', {
                            instrumentId: instrument.id,
                            tab: 'details.basic'
                        });
                    }
                };

                $rootScope.showSidePanel(options);
            }


            var newToolHeading = parsed.html($translate.instant("NEW_TOOL")).html();

            function newTool() {
                var options = {
                    title: newToolHeading,
                    template: 'app/desktop/modules/mes/tool/new/newToolView.jsp',
                    controller: 'NewToolController as newToolVm',
                    resolve: 'app/desktop/modules/mes/tool/new/newToolController',
                    width: 600,
                    showMask: true,
                    buttons: [
                        {text: createText, broadcast: 'app.tool.new'}
                    ],
                    callback: function (tool) {

                    }
                };

                $rootScope.showSidePanel(options);
            }

            var newDeclarationHeading = parsed.html($translate.instant("NEW_DECLARATION")).html();

            function newDeclaration() {
                var options = {
                    title: newDeclarationHeading,
                    template: 'app/desktop/modules/compliance/declaration/new/newDeclarationView.jsp',
                    controller: 'NewDeclarationController as newDeclarationVm',
                    resolve: 'app/desktop/modules/compliance/declaration/new/newDeclarationController',
                    width: 600,
                    showMask: true,
                    buttons: [
                        {text: createText, broadcast: 'app.declaration.new'}
                    ],
                    callback: function (declaration) {
                        $rootScope.hideSidePanel();
                        $timeout(function () {
                            $state.go('app.compliance.declaration.details', {
                                declarationId: declaration.id,
                                tab: 'details.basic'
                            });
                        }, 500);
                    }
                };

                $rootScope.showSidePanel(options);
            }

            var newSpecificationHeading = parsed.html($translate.instant("NEW_SPECIFICATION")).html();

            function newSpecification() {
                var options = {
                    title: newSpecificationHeading,
                    template: 'app/desktop/modules/compliance/specifications/new/newSpecificationView.jsp',
                    controller: 'NewSpecificationController as newSpecificationVm',
                    resolve: 'app/desktop/modules/compliance/specifications/new/newSpecificationController',
                    width: 600,
                    showMask: true,
                    buttons: [
                        {text: createText, broadcast: 'app.specification.new'}
                    ],
                    callback: function (specification) {
                        $rootScope.hideSidePanel();
                        $timeout(function () {
                            $state.go('app.compliance.specification.details', {
                                specificationId: specification.id,
                                tab: 'details.basic'
                            });
                        }, 500);
                    }
                };

                $rootScope.showSidePanel(options);
            }

            var newAssetHeading = parsed.html($translate.instant("NEW_ASSETS_TYPE")).html();

            function newAsset() {
                var options = {
                    title: newAssetHeading,
                    template: 'app/desktop/modules/mro/assets/new/newAssetView.jsp',
                    controller: 'NewAssetController as newAssetVm',
                    resolve: 'app/desktop/modules/mro/assets/new/newAssetController',
                    width: 600,
                    showMask: true,
                    buttons: [
                        {text: createText, broadcast: 'app.asset.new'}
                    ],
                    callback: function (asset) {
                        $rootScope.hideSidePanel();
                        $timeout(function () {
                            $state.go('app.mro.asset.details', {assetId: asset.id, tab: 'details.basic'});
                        }, 500);
                    }
                };

                $rootScope.showSidePanel(options);
            }

            var newWorkOrderHeading = parsed.html($translate.instant("NEW_WORK_ORDER_TYPE")).html();

            function newWorkOrder() {
                var options = {
                    title: newWorkOrderHeading,
                    template: 'app/desktop/modules/mro/workOrder/new/newWorkOrderView.jsp',
                    controller: 'NewWorkOrderController as newWorkOrderVm',
                    resolve: 'app/desktop/modules/mro/workOrder/new/newWorkOrderController',
                    width: 700,
                    showMask: true,
                    data: {
                        workOrderMode: "WORKORDER"
                    },
                    buttons: [
                        {text: createText, broadcast: 'app.workOrder.new'}
                    ],
                    callback: function (workOrder) {
                        $rootScope.hideSidePanel();
                        $timeout(function () {
                            $state.go('app.mro.workOrder.details', {workOrderId: workOrder.id, tab: 'details.basic'});
                        }, 500);
                    }
                };

                $rootScope.showSidePanel(options);
            }

            var newWorkRequestHeading = parsed.html($translate.instant("NEW_WORK_REQUEST_TYPE")).html();

            function newWorkRequest() {
                var options = {
                    title: newWorkRequestHeading,
                    template: 'app/desktop/modules/mro/workRequest/new/newWorkRequestView.jsp',
                    controller: 'NewWorkRequestController as newWorkRequestVm',
                    resolve: 'app/desktop/modules/mro/workRequest/new/newWorkRequestController',
                    width: 600,
                    showMask: true,
                    buttons: [
                        {text: createText, broadcast: 'app.workRequest.new'}
                    ],
                    callback: function (workRequest) {
                        $timeout(function () {
                            $state.go('app.mro.workRequest.details', {
                                workRequestId: workOrder.request,
                                tab: 'details.basic'
                            });
                        }, 500);
                    }
                };

                $rootScope.showSidePanel(options);
            }

            var newMaintenancePlanHeading = parsed.html($translate.instant("NEW_MAINTENANCE_PLAN")).html();

            function newMaintenancePlan() {
                var options = {
                    title: newMaintenancePlanHeading,
                    template: 'app/desktop/modules/mro/maintenancePlan/new/newMaintenancePlanView.jsp',
                    controller: 'NewMaintenancePlanController as newMaintenancePlanVm',
                    resolve: 'app/desktop/modules/mro/maintenancePlan/new/newMaintenancePlanController',
                    width: 600,
                    showMask: true,
                    buttons: [
                        {text: createText, broadcast: 'app.maintenancePlan.new'}
                    ],
                    callback: function (maintenancePlan) {
                        $timeout(function () {
                            $state.go('app.mro.maintenancePlan.details', {
                                maintenancePlanId: maintenancePlan.id,
                                tab: 'details.basic'
                            });
                        }, 500);
                    }
                };

                $rootScope.showSidePanel(options);
            }

            var newProductionOrderHeading = parsed.html($translate.instant("NEW_PRODUCTION_ORDER")).html();

            function newProductionOrder() {
                var options = {
                    title: newProductionOrderHeading,
                    template: 'app/desktop/modules/mes/productionOrder/new/newProductionOrderView.jsp',
                    controller: 'NewProductionOrderController as newProductionOrderVm',
                    resolve: 'app/desktop/modules/mes/productionOrder/new/newProductionOrderController',
                    width: 600,
                    showMask: true,
                    buttons: [
                        {text: 'create', broadcast: 'app.productionOrder.new'}
                    ],
                    callback: function (productionOrder) {
                        $timeout(function () {
                            $state.go('app.mes.productionOrder.details', {
                                productionOrderId: productionOrder.id,
                                tab: 'details.basic'
                            });
                        }, 500);
                    }
                };

                $rootScope.showSidePanel(options);
            }

            var newOperationsHeading = parsed.html($translate.instant("NEW_OPERATIONS_TYPE")).html();

            function newOperation() {
                var options = {
                    title: newOperationsHeading,
                    template: 'app/desktop/modules/mes/operations/new/newOperationsView.jsp',
                    controller: 'NewOperationsController as newOperationVm',
                    resolve: 'app/desktop/modules/mes/operations/new/newOperationsController',
                    width: 600,
                    showMask: true,
                    buttons: [
                        {text: createText, broadcast: 'app.operation.new'}
                    ],
                    callback: function (operation) {
                        $timeout(function () {
                            $state.go('app.mes.masterData.operation.details', {
                                operationId: operation.id,
                                tab: 'details.basic'
                            });
                        }, 500);
                    }
                };

                $rootScope.showSidePanel(options);
            }


            var newReqDocumentHeading = parsed.html($translate.instant("NEW_REQUIREMENT_DOC")).html();

            function newReqDocument() {
                var options = {
                    title: newReqDocumentHeading,
                    template: 'app/desktop/modules/req/reqdocs/new/newReqDocumentView.jsp',
                    controller: 'NewReqDocumentController as newReqDocumentVm',
                    resolve: 'app/desktop/modules/req/reqdocs/new/newReqDocumentController',
                    width: 600,
                    showMask: true,
                    buttons: [
                        {text: createText, broadcast: 'app.req.doc.new'}
                    ],
                    callback: function (req) {
                        $timeout(function () {
                            $state.go('app.req.document.details', {reqId: req.latestRevision, tab: 'details.basic'});
                        }, 500);
                    }
                };

                $rootScope.showSidePanel(options);
            }

            var newSupplierTitle = parsed.html($translate.instant("NEW_SUPPLIER")).html();

            function newSupplier() {
                var options = {
                    title: newSupplierTitle,
                    template: 'app/desktop/modules/mfr/supplier/new/newSupplierView.jsp',
                    controller: 'NewSupplierController as newSupplierVm',
                    resolve: 'app/desktop/modules/mfr/supplier/new/newSupplierController',
                    width: 600,
                    showMask: true,
                    buttons: [
                        {text: createText, broadcast: 'app.supplier.new'}
                    ],
                    callback: function (supplier) {
                        $timeout(function () {
                            $state.go('app.mfr.supplier.details', {supplierId: supplier.id, tab: 'details.basic'});
                        }, 500);
                    }
                };

                $rootScope.showSidePanel(options);
            }

            vm.newVault = newVault;
            function newVault() {
                var options = {
                    title: 'New Vault',
                    template: 'app/desktop/modules/pdm/vaults/new/newVaultView.jsp',
                    controller: 'NewVaultController as newVaultVm',
                    resolve: 'app/desktop/modules/pdm/vaults/new/newVaultController',
                    width: 500,
                    data: {},
                    showMask: true,
                    buttons: [
                        {text: "Create", broadcast: 'app.pdm.vault.new'}
                    ],
                    callback: function (newVault) {

                    }
                };

                $rootScope.showSidePanel(options);
            }

            var newPlantTitle = parsed.html($translate.instant("NEW_PLANT")).html();

            function newPlant() {
                var options = {
                    title: newPlantTitle,
                    template: 'app/desktop/modules/mes/plant/new/newPlantView.jsp',
                    controller: 'NewPlantController as newPlantVm',
                    resolve: 'app/desktop/modules/mes/plant/new/newPlantController',
                    width: 600,
                    showMask: true,
                    buttons: [
                        {text: createText, broadcast: 'app.plant.new'}
                    ],
                    callback: function (plant) {
                        $timeout(function () {
                            $state.go('app.mes.masterData.plant.details', {plantId: plant.id, tab: 'details.basic'});
                        }, 500);
                    }
                };

                $rootScope.showSidePanel(options);
            }

            $scope.newAssemblyLineTitle = parsed.html($translate.instant("NEW_ASSEMBLYLINE")).html();
            function newAssemblyLine() {
                var options = {
                    title: $scope.newAssemblyLineTitle,
                    template: 'app/desktop/modules/mes/assemblyLine/new/newAssemblyLineView.jsp',
                    controller: 'NewAssemblyLineController as newAssemblyLineVm',
                    resolve: 'app/desktop/modules/mes/assemblyLine/new/newAssemblyLineController',
                    width: 600,
                    showMask: true,
                    buttons: [
                        {text: createText, broadcast: 'app.assemblyLine.new'}
                    ],
                    callback: function (assemblyLine) {
                        $state.go('app.mes.masterData.assemblyline.details', {
                            assemblyLineId: assemblyLine.id,
                            tab: 'details.basic'
                        });
                    }
                };

                $rootScope.showSidePanel(options);
            }

            var newMachineHeading = parsed.html($translate.instant("NEW_MACHINE_TYPE")).html();

            function newMachine() {
                var options = {
                    title: newMachineHeading,
                    template: 'app/desktop/modules/mes/machine/new/newMachineView.jsp',
                    controller: 'NewMachineController as newMachineVm',
                    resolve: 'app/desktop/modules/mes/machine/new/newMachineController',
                    width: 600,
                    showMask: true,
                    buttons: [
                        {text: createText, broadcast: 'app.machine.new'}
                    ],
                    callback: function (machine) {
                        $state.go('app.mes.masterData.machine.details', {machineId: machine.id, tab: 'details.basic'});
                    }
                };

                $rootScope.showSidePanel(options);
            }

            $scope.newJig = parsed.html($translate.instant("NEW_JIG_TITLE_MSG")).html();
            $scope.newFixture = parsed.html($translate.instant("NEW_FIXTURE_TITLE_MSG")).html();
            var newJigHeading = parsed.html($translate.instant("NEW_JIG_TITLE_MSG")).html();
            var newFixtureHeading = parsed.html($translate.instant("NEW_FIXTURE_TITLE_MSG")).html();

            function newJigsAndFixtures() {
                var newJigTitle = null;
                $rootScope.jigsFixtureType = 'JIG';
                if ($rootScope.jigsFixtureType == 'JIG') {
                    newJigTitle = newJigHeading;
                } else if ($rootScope.jigsFixtureType == 'FIXTURE') {
                    newJigTitle = newFixtureHeading;
                }
                var options = {
                    title: newJigTitle,
                    template: 'app/desktop/modules/mes/jigsAndFixtures/new/newJigsAndFixturesView.jsp',
                    controller: 'NewJigsAndFixturesController as newJigsAndFixturesVm',
                    resolve: 'app/desktop/modules/mes/jigsAndFixtures/new/newJigsAndFixturesController',
                    width: 700,
                    showMask: true,
                    data: {
                        selectedObject: $rootScope.jigsFixtureType,
                        actionType: "home"
                    },
                    buttons: [
                        {text: createText, broadcast: 'app.jigsAndFixtures.new'}
                    ],
                    callback: function (jigsAndFixture) {
                        $state.go('app.mes.masterData.jigsAndFixtures.details', {
                            jigsFixId: jigsAndFixture.id,
                            tab: 'details.basic'
                        });
                    }
                };

                $rootScope.showSidePanel(options);
            }

            var newMaterialHeading = parsed.html($translate.instant("NEW_MATERIAL")).html();

            function newMaterial() {
                var options = {
                    title: newMaterialHeading,
                    template: 'app/desktop/modules/mes/material/new/newMaterialView.jsp',
                    controller: 'NewMaterialController as newMaterialVm',
                    resolve: 'app/desktop/modules/mes/material/new/newMaterialController',
                    width: 600,
                    showMask: true,
                    buttons: [
                        {text: createText, broadcast: 'app.material.new'}
                    ],
                    callback: function (material) {
                        $state.go('app.mes.masterData.material.details', {
                            materialId: material.id,
                            tab: 'details.basic'
                        });
                    }
                };

                $rootScope.showSidePanel(options);
            }

            var newManpowerHeading = parsed.html($translate.instant("NEW_MANPOWER_TYPE")).html();

            function newManpower() {
                var options = {
                    title: newManpowerHeading,
                    template: 'app/desktop/modules/mes/manpower/new/newManpowerView.jsp',
                    controller: 'NewManpowerController as newManpowerVm',
                    resolve: 'app/desktop/modules/mes/manpower/new/newManpowerController',
                    width: 600,
                    showMask: true,
                    buttons: [
                        {text: createText, broadcast: 'app.manpower.new'}
                    ],
                    callback: function (manpower) {
                        $timeout(function () {
                            loadManpowers();
                        }, 500);
                    }
                };

                $rootScope.showSidePanel(options);
            }

            var newShiftsHeading = parsed.html($translate.instant("NEW_SHIFT")).html();

            function newShift() {
                var options = {
                    title: newShiftsHeading,
                    template: 'app/desktop/modules/mes/shift/new/newShiftView.jsp',
                    controller: 'NewShiftController as newShiftVm',
                    resolve: 'app/desktop/modules/mes/shift/new/newShiftController',
                    width: 600,
                    showMask: true,
                    buttons: [
                        {text: createText, broadcast: 'app.shift.new'}
                    ],
                    callback: function (shift) {
                        $state.go('app.mes.masterData.shift.details', {shiftId: shift.id, tab: 'details.basic'});
                    }
                };

                $rootScope.showSidePanel(options);
            }

            var newMeterHeading = parsed.html($translate.instant("NEW_METER")).html();

            function newMeter() {
                var options = {
                    title: newMeterHeading,
                    template: 'app/desktop/modules/mro/meter/new/newMeterView.jsp',
                    controller: 'NewMeterController as newMeterVm',
                    resolve: 'app/desktop/modules/mro/meter/new/newMeterController',
                    width: 600,
                    showMask: true,
                    buttons: [
                        {text: createText, broadcast: 'app.meter.new'}
                    ],
                    callback: function (meter) {
                        $state.go('app.mro.meter.details', {meterId: meter.id, tab: 'details.basic'});
                    }
                };

                $rootScope.showSidePanel(options);
            }

            function newPPap() {
                var options = {
                    title: newPpapTitle,
                    template: 'app/desktop/modules/pqm/ppap/new/newPPAPView.jsp',
                    controller: 'NewPPAPController as newPpapVm',
                    resolve: 'app/desktop/modules/pqm/ppap/new/newPPAPController',
                    width: 600,
                    showMask: true,
                    buttons: [
                        {text: createText, broadcast: 'app.ppap.new'}
                    ],
                    callback: function (ppap) {
                        $timeout(function () {
                            $state.go('app.pqm.ppap.details', {
                                ppapId: ppap.id,
                                tab: 'details.basic'
                            });
                        }, 500);
                    }
                };

                $rootScope.showSidePanel(options);
            }

            function newSupplierAudit() {
                var options = {
                    title: newSupplierAuditTitle,
                    template: 'app/desktop/modules/pqm/supplierAudit/new/newSupplierAuditView.jsp',
                    controller: 'NewSupplierAuditController as newSupplierAuditVm',
                    resolve: 'app/desktop/modules/pqm/supplierAudit/new/newSupplierAuditController',
                    width: 600,
                    showMask: true,
                    buttons: [
                        {text: createText, broadcast: 'app.supplierAudit.new'}
                    ],
                    callback: function (supplierAudit) {
                        $timeout(function () {
                            $state.go('app.pqm.supplierAudit.details', {
                                supplierAuditId: supplierAudit.id,
                                tab: 'details.basic'
                            });
                        }, 500);
                    }
                };

                $rootScope.showSidePanel(options);
            }

            var newMBOMHeading = parsed.html($translate.instant("NEW_MBOM")).html();

            function newMBOM() {
                var options = {
                    title: newMBOMHeading,
                    template: 'app/desktop/modules/mes/mbom/new/newMBOMView.jsp',
                    controller: 'NewMBOMController as newMBOMVm',
                    resolve: 'app/desktop/modules/mes/mbom/new/newMBOMController',
                    width: 700,
                    showMask: true,
                    buttons: [
                        {text: createText, broadcast: 'app.mbom.new'}
                    ],
                    callback: function (mbom) {
                        $timeout(function () {
                            $state.go('app.mes.mbom.details', {mbomId: mbom.latestRevision, tab: 'details.basic'});
                        }, 500);
                    }
                };

                $rootScope.showSidePanel(options);
            }

            function newProgram() {
                var options = {
                    title: "New Program",
                    template: 'app/desktop/modules/pm/program/new/newProgramView.jsp',
                    controller: 'NewProgramController as newProgramVm',
                    resolve: 'app/desktop/modules/pm/program/new/newProgramController',
                    width: 550,
                    showMask: true,
                    buttons: [
                        {text: createText, broadcast: 'app.program.new'}
                    ],
                    callback: function (program) {
                        $timeout(function () {
                            $state.go('app.pm.program.details', {programId: program.id, tab: 'details.basic'});
                        }, 200);
                    }
                };

                $rootScope.showSidePanel(options);
            }

            var newSparePartHeading = parsed.html($translate.instant("NEW_SPARE_PART_TYPE")).html();

            function newSparePart() {
                var options = {
                    title: newSparePartHeading,
                    template: 'app/desktop/modules/mro/spareParts/new/newSparePartView.jsp',
                    controller: 'NewSparePartController as newSparePartVm',
                    resolve: 'app/desktop/modules/mro/spareParts/new/newSparePartController',
                    width: 600,
                    showMask: true,
                    buttons: [
                        {text: createText, broadcast: 'app.sparePart.new'}
                    ],
                    callback: function (sparePart) {
                        $state.go('app.mro.sparePart.details', {sparePartId: sparePart.id, tab: 'details.basic'});
                    }
                };

                $rootScope.showSidePanel(options);
            }

            var newSubstanceHeading = parsed.html($translate.instant("NEW_SUBSTANCE")).html();

            function newSubstance() {
                var options = {
                    title: newSubstanceHeading,
                    template: 'app/desktop/modules/compliance/substances/new/newSubstanceView.jsp',
                    controller: 'NewSubstanceController as newSubstanceVm',
                    resolve: 'app/desktop/modules/compliance/substances/new/newSubstanceController',
                    width: 600,
                    showMask: true,
                    buttons: [
                        {text: createText, broadcast: 'app.substance.new'}
                    ],
                    callback: function (substance) {
                        $state.go('app.compliance.substance.details', {
                            substanceId: substance.id,
                            tab: 'details.basic'
                        });
                    }
                };

                $rootScope.showSidePanel(options);
            }


            function newWorkflow() {
                $state.go('app.workflow.editor');
            }

            vm.gotoItems = gotoItems;
            function gotoItems() {
                $state.go('app.items.all', {itemMode: 'all'});
            }

            vm.gotoEcos = gotoEcos;
            function gotoEcos() {
                $state.go("app.changes.eco.all");
            }

            vm.gotoEcrs = gotoEcrs;
            function gotoEcrs() {
                $state.go("app.changes.ecr.all");
            }

            vm.gotoDcos = gotoDcos;
            function gotoDcos() {
                $state.go("app.changes.dco.all");
            }

            vm.gotoDcrs = gotoDcrs;
            function gotoDcrs() {
                $state.go("app.changes.dcr.all");
            }

            vm.gotoMcos = gotoMcos;
            function gotoMcos() {
                $state.go("app.changes.mco.all");
            }

            vm.gotoPrs = gotoPrs;
            function gotoPrs() {
                $state.go("app.pqm.pr.all");
            }

            vm.gotoInspectionPlans = gotoInspectionPlans;
            function gotoInspectionPlans() {
                $state.go("app.pqm.inspectionPlan.all");
            }

            vm.gotoInspections = gotoInspections;
            function gotoInspections() {
                $state.go("app.pqm.inspection.all");
            }

            vm.gotoQcrs = gotoQcrs;
            function gotoQcrs() {
                $state.go("app.pqm.qcr.all");
            }

            vm.gotoNcrs = gotoNcrs;
            function gotoNcrs() {
                $state.go("app.pqm.ncr.all");
            }

            vm.gotoSpecifications = gotoSpecifications;
            function gotoSpecifications() {
                $state.go("app.compliance.specification.all");
            }

            vm.gotoDeclarations = gotoDeclarations;
            function gotoDeclarations() {
                $state.go("app.compliance.declaration.all");
            }

            vm.gotoAssets = gotoAssets;
            function gotoAssets() {
                $state.go("app.mro.asset.all");
            }

            vm.gotoWorkRequests = gotoWorkRequests;
            function gotoWorkRequests() {
                $state.go("app.mro.workRequest.all");
            }

            vm.gotoMaintenancePlans = gotoMaintenancePlans;
            function gotoMaintenancePlans() {
                $state.go("app.mro.maintenancePlan.all");
            }

            vm.gotoWorkOrders = gotoWorkOrders;
            function gotoWorkOrders() {
                $state.go("app.mro.workOrder.all");
            }

            vm.gotoVaults = gotoVaults;
            function gotoVaults() {
                $state.go("app.pdm.vaults");
            }

            vm.gotoProductionOrders = gotoProductionOrders;
            function gotoProductionOrders() {
                $state.go("app.mes.productionOrder.all");
            }

            vm.gotoperations = gotoperations;
            function gotoperations() {
                $state.go("app.mes.masterData.operation.all");
            }

            vm.gotoProjects = gotoProjects;
            function gotoProjects() {
                $state.go("app.pm.project.all");
            }

            vm.gotoReqDocuments = gotoReqDocuments;
            function gotoReqDocuments() {
                $state.go("app.req.document.all");
            }

            vm.gotoMfrs = gotoMfrs;
            function gotoMfrs() {
                $state.go("app.mfr.all");
            }

            vm.gotoMfrParts = gotoMfrParts;
            function gotoMfrParts() {
                $state.go("app.mfr.mfrparts.all");
            }

            vm.gotoSuppliers = gotoSuppliers;
            function gotoSuppliers() {
                $state.go("app.mfr.supplier.all");
            }

            vm.gotoWorkflows = gotoWorkflows;
            function gotoWorkflows() {
                $state.go("app.workflow.all");
            }

            vm.navigateWidget = navigateWidget;
            function navigateWidget(widget) {
                if (widget.privilege == 'view') {
                    if (widget.subType == "ecr") {
                        gotoEcrs();
                    } else if (widget.subType == "dcr") {
                        gotoDcrs();
                    } else if (widget.subType == "eco") {
                        gotoEcos();
                    } else if (widget.subType == "dco") {
                        gotoDcos();
                    } else if (widget.subType == "mco") {
                        gotoMcos();
                    } else if (widget.subType == "item") {
                        gotoItems();
                    } else if (widget.subType == "manufacturerpart") {
                        gotoMfrParts();
                    } else if (widget.subType == "pgcspecification") {
                        gotoSpecifications();
                    } else if (widget.subType == "pgcdeclaration") {
                        gotoDeclarations();
                    } else if (widget.subType == "mroasset") {
                        gotoAssets();
                    } else if (widget.subType == "mroworkrequest") {
                        gotoWorkRequests();
                    } else if (widget.subType == "mroworkorder") {
                        gotoWorkOrders();
                    } else if (widget.subType == "operation") {
                        gotoperations();
                    } else if (widget.subType == "productionorder") {
                        gotoProductionOrders();
                    } else if (widget.subType == "mromaintenanceplan") {
                        gotoMaintenancePlans();
                    } else if (widget.subType == "problemreport") {
                        gotoPrs();
                    } else if (widget.subType == "qcr") {
                        gotoQcrs();
                    } else if (widget.subType == "project") {
                        gotoProjects();
                    } else if (widget.subType == "requirementdocument") {
                        gotoReqDocuments();
                    } else if (widget.subType == "inspectionplan") {
                        gotoInspectionPlans();
                    } else if (widget.subType == "inspection") {
                        gotoInspections();
                    } else if (widget.subType == "ncr") {
                        gotoNcrs();
                    } else if (widget.subType == "manufacturer") {
                        gotoMfrs();
                    } else if (widget.subType == "mfrsupplier") {
                        gotoSuppliers();
                    } else if (widget.subType == "plmworkflow") {
                        gotoWorkflows();
                    } else if (widget.subType == "pdmvault") {
                        gotoVaults();
                    } else if (widget.subType == "plant") {
                        $state.go("app.mes.masterData.plant.all")
                    } else if (widget.subType == "workcenter") {
                        $state.go("app.mes.masterData.workcenter.all")
                    } else if (widget.subType == "machine") {
                        $state.go("app.mes.masterData.machine.all")
                    } else if (widget.subType == "equipment") {
                        $state.go("app.mes.masterData.equipment.all")
                    } else if (widget.subType == "instrument") {
                        $state.go("app.mes.masterData.instrument.all")
                    } else if (widget.subType == "tool") {
                        $state.go("app.mes.masterData.tool.all")
                    } else if (widget.subType == "jigfixture") {
                        $state.go("app.mes.masterData.jigsAndFixtures.all")
                    } else if (widget.subType == "material") {
                        $state.go("app.mes.masterData.material.all")
                    } else if (widget.subType == "manpower") {
                        $state.go("app.mes.masterData.manpower.all")
                    } else if (widget.subType == "shift") {
                        $state.go("app.mes.masterData.shift.all")
                    } else if (widget.subType == "assemblyline") {
                        $state.go("app.mes.masterData.assemblyline.all")
                    } else if (widget.subType == "pgcsubstance") {
                        $state.go("app.compliance.substance.all")
                    } else if (widget.subType == "mrometer") {
                        $state.go("app.mro.meter.all")
                    } else if (widget.subType == "mrosparepart") {
                        $state.go("app.mro.sparePart.all")
                    } else if (widget.subType == "ppap") {
                        $state.go("app.pqm.ppap.all")
                    } else if (widget.subType == "supplieraudit") {
                        $state.go("app.pqm.supplierAudit.all")
                    } else if (widget.subType == "mbom") {
                        $state.go("app.mes.mbom.all")
                    } else if (widget.subType == "program") {
                        $state.go("app.pm.program.all")
                    }
                } else if (widget.privilege == "create") {
                    if (widget.subType == "ecr") {
                        newECR();
                    } else if (widget.subType == "dcr") {
                        newDCR();
                    } else if (widget.subType == "eco") {
                        newECO();
                    } else if (widget.subType == "dco") {
                        newDCO();
                    } else if (widget.subType == "mco") {
                        newMCO();
                    } else if (widget.subType == "item") {
                        newItem();
                    } else if (widget.subType == "manufacturerpart") {
                        newManufacturerPart();
                    } else if (widget.subType == "pgcspecification") {
                        newSpecification();
                    } else if (widget.subType == "pgcdeclaration") {
                        newDeclaration();
                    } else if (widget.subType == "mroasset") {
                        newAsset();
                    } else if (widget.subType == "mroworkrequest") {
                        newWorkRequest();
                    } else if (widget.subType == "mroworkorder") {
                        newWorkOrder();
                    } else if (widget.subType == "operation") {
                        newOperation();
                    } else if (widget.subType == "productionorder") {
                        newProductionOrder();
                    } else if (widget.subType == "mromaintenanceplan") {
                        newMaintenancePlan();
                    } else if (widget.subType == "problemreport") {
                        newProblemReport();
                    } else if (widget.subType == "qcr") {
                        newQCR();
                    } else if (widget.subType == "project") {
                        newProject();
                    } else if (widget.subType == "requirementdocument") {
                        newReqDocument();
                    } else if (widget.subType == "inspectionplan") {
                        newInspectionPlan();
                    } else if (widget.subType == "inspection") {
                        newInspection();
                    } else if (widget.subType == "ncr") {
                        newNCR();
                    } else if (widget.subType == "manufacturer") {
                        newManufacturer();
                    } else if (widget.subType == "mfrsupplier") {
                        newSupplier();
                    } else if (widget.subType == "plmworkflow") {
                        newWorkflow();
                    } else if (widget.subType == "pdmvault") {
                        newVault();
                    } else if (widget.subType == "plant") {
                        newPlant();
                    } else if (widget.subType == "workcenter") {
                        newWorkCenter();
                    } else if (widget.subType == "machine") {
                        newMachine();
                    } else if (widget.subType == "equipment") {
                        newEquipment();
                    } else if (widget.subType == "instrument") {
                        newInstrument();
                    } else if (widget.subType == "tool") {
                        newTool();
                    } else if (widget.subType == "jigfixture") {
                        newJigsAndFixtures();
                    } else if (widget.subType == "material") {
                        newMaterial();
                    } else if (widget.subType == "manpower") {
                        newManpower();
                    } else if (widget.subType == "shift") {
                        newShift();
                    } else if (widget.subType == "assemblyline") {
                        newAssemblyLine();
                    } else if (widget.subType == "pgcsubstance") {
                        newSubstance();
                    } else if (widget.subType == "mrometer") {
                        newMeter();
                    } else if (widget.subType == "mrosparepart") {
                        newSparePart();
                    } else if (widget.subType == "ppap") {
                        newPPap();
                    } else if (widget.subType == "supplieraudit") {
                        newSupplierAudit();
                    } else if (widget.subType == "mbom") {
                        newMBOM();
                    } else if (widget.subType == "program") {
                        newProgram();
                    }
                }
            }
        }
    }
);