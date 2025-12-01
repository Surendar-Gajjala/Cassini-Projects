define(
    [
        'app/desktop/modules/mfr/mfrparts/mfrparts.module',
        'app/shared/services/core/mfrPartsService',
        'app/shared/services/core/workflowService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/commonService'
    ],
    function (module) {
        module.controller('MfrPartsChangesController', MfrPartsChangesController);

        function MfrPartsChangesController($scope, $translate, $rootScope, $timeout, $state, $stateParams, $cookies, $window,
                                           WorkflowService, MfrPartsService, CommonService) {
            var vm = this;

            vm.loading = true;
            vm.mfrPartId = $stateParams.manufacturePartId;
            vm.item = null;
            vm.collapseMcos = false;
            vm.collapseVariances = false;
            $rootScope.loadChanges = loadChanges;
            var parsed = angular.element("<div></div>");

            vm.mco = parsed.html($translate.instant("MCO")).html();
            vm.variance = parsed.html($translate.instant("VARIANCE")).html();

            vm.expandAllMcos = expandAllMcos;
            function expandAllMcos() {
                vm.collapseMcos = true;
            }


            vm.collapseAllMcos = collapseAllMcos;
            function collapseAllMcos() {
                vm.collapseMcos = false;
            }

            vm.expandAllVariances = expandAllVariances;
            function expandAllVariances() {
                vm.collapseVariances = true;
            }


            vm.collapseAllVariances = collapseAllVariances;
            function collapseAllVariances() {
                vm.collapseVariances = false;
            }

            function loadChanges() {
                vm.loading = true;
                vm.mfrPartChanges = [];
                MfrPartsService.getMfrPartChanges(vm.mfrPartId).then(
                    function (data) {
                        vm.mfrPartChanges = data;
                        vm.loading = false;
                        WorkflowService.getWorkflowReferences(vm.mfrPartChanges, 'workflow');
                        CommonService.getPersonReferences(vm.mfrPartChanges, 'changeAnalyst');
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                        $rootScope.hideBusyIndicator();
                    }
                )
            }

            function loadVariances() {
                vm.loading = true;
                vm.mfrPartChanges = [];
                MfrPartsService.getMfrPartVariances(vm.mfrPartId).then(
                    function (data) {
                        vm.variances = data;
                        vm.loading = false;
                        WorkflowService.getWorkflowReferences(vm.variances, 'workflow');
                        CommonService.getPersonReferences(vm.variances, 'originator');
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                        $rootScope.hideBusyIndicator();
                    }
                )
            }

            vm.showMco = showMco;
            function showMco(change) {
                $window.localStorage.setItem("lastSelectedItemTab", JSON.stringify(vm.itemChangesTabId));
                $state.go('app.changes.mco.details', {mcoId: change.id});
            }

            vm.showVariance = showVariance;
            function showVariance(change) {
                $window.localStorage.setItem("lastSelectedItemTab", JSON.stringify(vm.itemChangesTabId));
                $state.go('app.changes.variance.details', {varianceId: change.id});
            }

            (function () {
                $scope.$on('app.mfrPart.tabactivated', function (event, data) {
                    if (data.tabId == 'details.changes') {
                        vm.itemChangesTabId = data.tabId;
                        loadChanges();
                        loadVariances();
                    }
                });
            })();
        }
    }
);