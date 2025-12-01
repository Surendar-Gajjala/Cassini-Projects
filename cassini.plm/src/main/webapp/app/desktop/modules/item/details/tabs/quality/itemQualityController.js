define(
    [
        'app/desktop/modules/item/item.module',
        'app/shared/services/core/itemService',
        'app/shared/services/core/workflowService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/commonService'
    ],
    function (module) {
        module.controller('ItemQualityController', ItemQualityController);

        function ItemQualityController($scope, $translate, $rootScope, $window, $timeout, $state, WorkflowService, CommonService, ItemService, $stateParams, $cookies) {

            var vm = this;
            vm.loading = true;
            vm.itemId = $stateParams.itemId;
            vm.collapsePrs = true;
            vm.collapseProductPrs = true;
            vm.collapseQcrs = true;
            var parsed = angular.element("<div></div>");

            vm.pr = parsed.html($translate.instant("PR")).html();
            vm.qcr = parsed.html($translate.instant("QCR")).html();
            vm.problemReportTitle = parsed.html($translate.instant("PROBLEMREPORTS")).html();

            vm.expandAllPrs = expandAllPrs;
            function expandAllPrs() {
                vm.collapsePrs = true;
            }

            vm.expandAllProductPrs = expandAllProductPrs;
            function expandAllProductPrs() {
                vm.collapseProductPrs = !vm.collapseProductPrs;
            }

            vm.collapseAllPrs = collapseAllPrs;
            function collapseAllPrs() {
                vm.collapsePrs = false;
            }

            vm.expandAllQcrs = expandAllQcrs;
            function expandAllQcrs() {
                vm.collapseQcrs = true;
            }


            vm.collapseAllQcrs = collapseAllQcrs;
            function collapseAllQcrs() {
                vm.collapseQcrs = false;
            }

            function loadPRs() {
                $rootScope.showBusyIndicator();
                vm.loading = true;
                vm.prQuality = [];
                ItemService.getItemPRs(vm.itemId).then(
                    function (data) {
                        vm.prQuality = data;
                        CommonService.getPersonReferences(vm.prQuality, 'createdBy');
                        WorkflowService.getWorkflowReferences(vm.prQuality, 'workflow');
                        vm.loading = false;
                        $rootScope.hideBusyIndicator();
                    }, function (error) {
                        $rootScope.hideBusyIndicator();
                        $rootScope.showErrorMessage(error.message);
                    }
                )
            }

            vm.productPrs = [];
            function loadProductProblemReports() {
                $rootScope.showBusyIndicator();
                vm.loading = true;
                if ($rootScope.item.itemType.itemClass == "PRODUCT") {
                    ItemService.getProductItemPRs(vm.itemId).then(
                        function (data) {
                            vm.productPrs = data;
                            CommonService.getPersonReferences(vm.productPrs, 'createdBy');
                            WorkflowService.getWorkflowReferences(vm.productPrs, 'workflow');
                            vm.loading = false;
                            $rootScope.hideBusyIndicator();
                        }, function (error) {
                            $rootScope.hideBusyIndicator();
                            $rootScope.showErrorMessage(error.message);
                        }
                    )
                }
            }

            function loadQCRs() {
                $rootScope.showBusyIndicator();
                vm.loading = true;
                vm.qcrQuality = [];
                ItemService.getItemQCRs(vm.itemId).then(
                    function (data) {
                        vm.loading = false;
                        vm.qcrQuality = data;
                        CommonService.getPersonReferences(vm.qcrQuality, 'createdBy');
                        WorkflowService.getWorkflowReferences(vm.qcrQuality, 'workflow');
                        vm.loading = false;
                        $rootScope.hideBusyIndicator();
                    }, function (error) {
                        $rootScope.hideBusyIndicator();
                        $rootScope.showErrorMessage(error.message);
                    }
                )
            }

            vm.showPr = showPr;
            function showPr(quality) {
                $window.localStorage.setItem("lastSelectedItemTab", JSON.stringify(vm.itemChangesTabId));
                $state.go('app.pqm.pr.details', {problemReportId: quality.id});
            }

            vm.showQcr = showQcr;
            function showQcr(quality) {
                $window.localStorage.setItem("lastSelectedItemTab", JSON.stringify(vm.itemChangesTabId));
                $state.go('app.pqm.qcr.details', {qcrId: quality.id});
            }

            (function () {
                $scope.$on('app.item.tabactivated', function (event, data) {
                    if (data.tabId == 'details.quality') {
                        vm.itemChangesTabId = data.tabId;
                        if ($rootScope.selectedMasterItemId != null) {
                            vm.itemId = $rootScope.selectedMasterItemId;
                            loadPRs();
                            loadProductProblemReports();
                            loadQCRs();
                        }
                        if ($rootScope.selectedMasterItemId == null) {
                            loadPRs();
                            loadProductProblemReports();
                            loadQCRs();
                        }

                    }
                });
            })();
        }
    }
);