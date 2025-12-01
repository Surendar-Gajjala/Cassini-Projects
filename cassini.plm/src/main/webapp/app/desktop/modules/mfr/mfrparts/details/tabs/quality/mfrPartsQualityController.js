define(
    [
        'app/desktop/modules/mfr/mfrparts/mfrparts.module',
        'app/shared/services/core/itemService',
        'app/shared/services/core/mfrPartsService',
        'app/shared/services/core/workflowService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/commonService'
    ],
    function (module) {
        module.controller('MfrPartQualityController', MfrPartQualityController);

        function MfrPartQualityController($scope, $translate, $rootScope,$window, $timeout, MfrPartsService, $state, WorkflowService, CommonService, ItemService, $stateParams, $cookies) {

            var vm = this;
            vm.loading = true;
            vm.manufacturePartId = $stateParams.manufacturePartId;
            vm.collapseNcrs = false;
            vm.collapseQcrs = false;
            var parsed = angular.element("<div></div>");

            vm.pr = parsed.html($translate.instant("NCR")).html();
            vm.qcr = parsed.html($translate.instant("QCR")).html();

            vm.expandAllNcrs = expandAllNcrs;
            function expandAllNcrs() {
                vm.collapseNcrs = true;
            }


            vm.collapseAllNcrs = collapseAllNcrs;
            function collapseAllNcrs() {
                vm.collapseNcrs = false;
            }

            vm.expandAllQcrs = expandAllQcrs;
            function expandAllQcrs() {
                vm.collapseQcrs = true;
            }


            vm.collapseAllQcrs = collapseAllQcrs;
            function collapseAllQcrs() {
                vm.collapseQcrs = false;
            }

            function loadNCRs(){
                vm.prQuality = [];
                MfrPartsService.getMfrPartNCRs(vm.manufacturePartId).then(
                    function(data){
                        vm.loading = false;
                        vm.ncrQuality = data;
                        console.log(vm.ncrQuality);
                        CommonService.getPersonReferences(vm.ncrQuality, 'createdBy');
                        WorkflowService.getWorkflowReferences(vm.ncrQuality, 'workflow');
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                        $rootScope.hideBusyIndicator();
                    }
                )
            }

            function loadQCRs(){
                vm.qcrQuality = [];
                MfrPartsService.getMfrPartQCRs(vm.manufacturePartId).then(
                    function(data){
                        vm.loading = false;
                        vm.qcrQuality = data;
                        console.log(vm.qcrQuality);
                        CommonService.getPersonReferences(vm.qcrQuality, 'createdBy');
                        WorkflowService.getWorkflowReferences(vm.qcrQuality, 'workflow');
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                        $rootScope.hideBusyIndicator();
                    }
                )
            }

            vm.showNcr = showNcr;
            function showNcr(quality) {
                $window.localStorage.setItem("lastSelectedItemTab", JSON.stringify(vm.itemChangesTabId));
                $state.go('app.pqm.ncr.details', {ncrId: quality.id});
            }

            vm.showQcr = showQcr;
            function showQcr(quality) {
                $window.localStorage.setItem("lastSelectedItemTab", JSON.stringify(vm.itemChangesTabId));
                $state.go('app.pqm.qcr.details', {qcrId: quality.id});
            }

            (function () {
                $scope.$on('app.mfrPart.tabactivated', function (event, data) {
                    if (data.tabId == 'details.quality') {
                        vm.itemChangesTabId = data.tabId;
                        loadNCRs();
                        loadQCRs();
                    }
                });
            })();
        }
    }
);