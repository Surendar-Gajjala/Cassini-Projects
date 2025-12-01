define(
    [
        'app/desktop/modules/item/item.module',
        'app/shared/services/core/itemService',
        'app/shared/services/core/inspectionPlanService',
        'app/shared/services/core/workflowDefinitionService',
        'app/shared/services/core/reqDocumentService',
        'app/shared/services/core/requirementService',
        'app/shared/services/core/mbomService',
        'app/shared/services/core/bopService'
    ],
    function (module) {
        module.controller('ItemRevisionHistoryController', ItemRevisionHistoryController);

        function ItemRevisionHistoryController($scope, $rootScope, $timeout, $state, $stateParams, $cookies, MBOMService, BOPService,
                                               ItemService, InspectionPlanService, WorkflowDefinitionService, ReqDocumentService, RequirementService) {
            var vm = this;

            vm.data = $scope.data;
            vm.itemRevisionHisotry = [];
            vm.showVersion = false;
            vm.revisionHistoryType = $scope.data.revisionHistoryType;

            vm.showItemRevision = showItemRevision;

            function loadItemRevisionHistory() {
                var promise = null;
                if (vm.revisionHistoryType == "ITEM") {
                    promise = ItemService.getItemRevisionHistory(vm.data.itemId);
                } else if (vm.revisionHistoryType == "INSPECTIONPLAN") {
                    promise = InspectionPlanService.getPlanRevisionHistory(vm.data.itemId);
                } else if (vm.revisionHistoryType == "WORKFLOW") {
                    promise = WorkflowDefinitionService.getWorkflowRevisionHistory(vm.data.itemId);
                } else if (vm.revisionHistoryType == "REQUIREMENTDOCUMENT") {
                    promise = ReqDocumentService.getReqDocumentRevisionHistory(vm.data.itemId);
                } else if (vm.revisionHistoryType == "REQUIREMENTVERSION") {
                    promise = RequirementService.getReqVersionHistory(vm.data.itemId);
                    vm.showVersion = true;
                } else if (vm.revisionHistoryType == "MBOM") {
                    promise = MBOMService.getMBOMRevisionHistory(vm.data.itemId);
                } else if (vm.revisionHistoryType == "BOP") {
                    promise = BOPService.getBOPRevisionHistory(vm.data.itemId);
                }
                if (promise != null) {
                    promise.then(
                        function (data) {
                            vm.itemRevisionHisotry = data;
                        }, function (error) {
                            $rootScope.showErrorMessage(error.message);
                        }
                    )
                }
            }

            function showItemRevision(itemRevision) {
                if (vm.revisionHistoryType == "ITEM") {
                    $rootScope.seletedItemId = itemRevision.itemMaster;
                    $state.go('app.items.details', {itemId: itemRevision.id});
                } else if (vm.revisionHistoryType == "INSPECTIONPLAN") {
                    $state.go('app.pqm.inspectionPlan.details', {planId: itemRevision.id});
                } else if (vm.revisionHistoryType == "WORKFLOW") {
                    $state.go('app.workflow.editor', {mode: 'edit', workflow: itemRevision.id});
                } else if (vm.revisionHistoryType == "REQUIREMENTDOCUMENT") {
                    $state.go('app.req.document.details', {reqId: itemRevision.id});
                } else if (vm.revisionHistoryType == "REQUIREMENTVERSION") {
                    $state.go('app.req.requirements.details', {requirementId: itemRevision.reqChild});
                } else if (vm.revisionHistoryType == "MBOM") {
                    $state.go('app.mes.mbom.details', {mbomId: itemRevision.id, tab: 'details.basic'});
                } else if (vm.revisionHistoryType == "BOP") {
                    $state.go('app.mes.bop.details', {bopId: itemRevision.id, tab: 'details.basic'});
                }
            }

            (function () {
                loadItemRevisionHistory();
            })();
        }
    }
);