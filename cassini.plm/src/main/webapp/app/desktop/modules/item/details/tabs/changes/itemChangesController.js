define(
    [
        'app/desktop/modules/item/item.module',
        'app/shared/services/core/itemService',
        'app/shared/services/core/ecoService',
        'app/shared/services/core/dcoService',
        'app/shared/services/core/workflowService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/commonService'
    ],
    function (module) {
        module.controller('ItemChangesController', ItemChangesController);

        function ItemChangesController($scope, $translate, $rootScope, $timeout, $state, $stateParams, $cookies, $window,
                                       ItemService, WorkflowService, ECOService, DCOService, CommonService) {
            var vm = this;

            vm.loading = true;
            vm.itemId = $stateParams.itemId;
            vm.item = null;
            vm.showEco = showEco;
            $rootScope.loadChanges = loadChanges;
            vm.collapseRequests = true;
            vm.collapseOrders = true;
            var parsed = angular.element("<div></div>");

            vm.changeOrders = parsed.html($translate.instant("CHANGE_ORDERS")).html();
            vm.changeRequests = parsed.html($translate.instant("CHANGE_REQUESTS")).html();
            function loadChanges(itemId) {
                $rootScope.showBusyIndicator();
                if (itemId != null) {
                    vm.itemId = itemId;
                    vm.loading = true;
                    vm.ecoChanges = [];
                    ItemService.getItemChanges(vm.itemId).then(
                        function (data) {
                            vm.ecoChanges = data;
                            CommonService.getPersonReferences(vm.ecoChanges, 'ecoOwner');
                            WorkflowService.getWorkflowReferences(vm.ecoChanges, 'workflow');
                            ECOService.getChangeTypeReferences(vm.ecoChanges, 'ecoType');
                            vm.loading = false;
                            $rootScope.hideBusyIndicator();
                        }, function (error) {
                            $rootScope.hideBusyIndicator();
                            $rootScope.showErrorMessage(error.message);
                        }
                    )
                } else {
                    vm.loading = true;
                    vm.ecoChanges = [];
                    ItemService.getItemChanges(vm.itemId).then(
                        function (data) {
                            vm.ecoChanges = data;
                            CommonService.getPersonReferences(vm.ecoChanges, 'ecoOwner');
                            WorkflowService.getWorkflowReferences(vm.ecoChanges, 'workflow');
                            ECOService.getChangeTypeReferences(vm.ecoChanges, 'ecoType');
                            vm.loading = false;
                            $rootScope.hideBusyIndicator();
                        }, function (error) {
                            $rootScope.hideBusyIndicator();
                            $rootScope.showErrorMessage(error.message);
                        }
                    )
                }
            }

            function loadECRs() {
                $rootScope.showBusyIndicator();
                vm.loading = true;
                vm.ecrChanges = [];
                ItemService.getItemECRs(vm.itemId).then(
                    function (data) {
                        vm.ecrChanges = data;
                        CommonService.getPersonReferences(vm.ecrChanges, 'originator');
                        WorkflowService.getWorkflowReferences(vm.ecrChanges, 'workflow');
                        ECOService.getChangeTypeReferences(vm.ecrChanges, 'crType');
                        vm.loading = false;
                        $rootScope.hideBusyIndicator();
                    }, function (error) {
                        $rootScope.hideBusyIndicator();
                        $rootScope.showErrorMessage(error.message);
                    }
                )
            }

            function loadDCOs() {
                $rootScope.showBusyIndicator();
                vm.loading = true;
                vm.dcoChanges = [];
                ItemService.getItemDCOs(vm.itemId).then(
                    function (data) {
                        vm.dcoChanges = data;
                        CommonService.getPersonReferences(vm.dcoChanges, 'dcoOwner');
                        WorkflowService.getWorkflowReferences(vm.dcoChanges, 'workflow');
                        DCOService.getChangeTypeReferences(vm.dcoChanges, 'dcoType');
                        vm.loading = false;
                        $rootScope.hideBusyIndicator();
                    }, function (error) {
                        $rootScope.hideBusyIndicator();
                        $rootScope.showErrorMessage(error.message);
                    }
                )
            }

            function loadDCRs() {
                $rootScope.showBusyIndicator();
                vm.dcrChanges = [];
                ItemService.getItemDCRs(vm.itemId).then(
                    function (data) {
                        vm.dcrChanges = data;
                        CommonService.getPersonReferences(vm.dcrChanges, 'originator');
                        WorkflowService.getWorkflowReferences(vm.dcrChanges, 'workflow');
                        DCOService.getChangeTypeReferences(vm.dcrChanges, 'dcoType');
                        vm.loading = false;
                        $rootScope.hideBusyIndicator();
                    }, function (error) {
                        $rootScope.hideBusyIndicator();
                        $rootScope.showErrorMessage(error.message);
                    }
                )
            }

            vm.expandAllChangeOrders = expandAllChangeOrders;
            function expandAllChangeOrders() {
                vm.collapseOrders = true;
            }


            vm.collapseAllChangeOrders = collapseAllChangeOrders;
            function collapseAllChangeOrders() {
                vm.collapseOrders = false;
            }

            vm.expandAllChangeRequests = expandAllChangeRequests;
            function expandAllChangeRequests() {
                vm.collapseRequests = true;
            }


            vm.collapseAllChangeRequests = collapseAllChangeRequests;
            function collapseAllChangeRequests() {
                vm.collapseRequests = false;
            }

            function showEco(change) {
                $window.localStorage.setItem("lastSelectedItemTab", JSON.stringify(vm.itemChangesTabId));
                $state.go('app.changes.eco.details', {ecoId: change.id});
            }

            vm.showDco = showDco;
            function showDco(change) {
                $window.localStorage.setItem("lastSelectedItemTab", JSON.stringify(vm.itemChangesTabId));
                $state.go('app.changes.dco.details', {dcoId: change.id});
            }

            vm.showEcr = showEcr;
            function showEcr(change) {
                $window.localStorage.setItem("lastSelectedItemTab", JSON.stringify(vm.itemChangesTabId));
                $state.go('app.changes.ecr.details', {ecrId: change.id});
            }

            vm.showDcr = showDcr;
            function showDcr(change) {
                $window.localStorage.setItem("lastSelectedItemTab", JSON.stringify(vm.itemChangesTabId));
                $state.go('app.changes.dcr.details', {dcrId: change.id});
            }

            (function () {
                $scope.$on('app.item.tabactivated', function (event, data) {
                    if (data.tabId == 'details.changes') {
                        vm.itemChangesTabId = data.tabId;
                        if ($rootScope.selectedMasterItemId != null) {
                            $stateParams.itemId = $rootScope.selectedMasterItemId;
                            vm.itemId = $stateParams.itemId;
                        }
                        if ($rootScope.itemType != 'DOCUMENT') {
                            loadChanges(vm.itemId);
                            loadECRs();
                        }
                        if ($rootScope.itemType == 'DOCUMENT') {
                            loadDCOs();
                            loadDCRs();
                        }

                    }
                });
            })();
        }
    }
);