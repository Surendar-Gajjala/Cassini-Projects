define(
    [
        'app/desktop/modules/item/item.module',
        'app/shared/services/core/varianceService',
        'app/assets/bower_components/cassini-platform/app/desktop/modules/dialogs/dialogService',
        'app/desktop/modules/change/variance/details/varianceDetailsController',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/commonService'
    ],
    function (module) {
        module.controller('ItemVarianceController', ItemVarianceController);

        function ItemVarianceController($scope, $rootScope, $timeout, $window, CommonService, VarianceService, $state, $stateParams, $cookies) {

            var vm = this;
            vm.loading = true;
            vm.itemId = $stateParams.itemId;

            function loadVariance() {
                $rootScope.showBusyIndicator();
                vm.loading = true;
                VarianceService.getVarianceByItem(vm.itemId).then(
                    function (data) {
                        vm.variances = data;
                        CommonService.getPersonReferences(vm.variances, 'originator');
                        CommonService.getPersonReferences(vm.variances, 'createdBy');
                        vm.loading = false;
                        $rootScope.hideBusyIndicator();
                    }, function (error) {
                        $rootScope.hideBusyIndicator();
                        $rootScope.showErrorMessage(error.message);
                    }
                )
            }

            vm.showVariance = showVariance;
            function showVariance(variance) {
                $window.localStorage.setItem("lastSelectedItemTab", JSON.stringify(vm.itemChangesTabId));
                $state.go('app.changes.variance.details', {varianceId: variance.id});
            }

            (function () {
                $scope.$on('app.item.tabactivated', function (event, data) {
                    if (data.tabId == 'details.variance') {
                        vm.itemChangesTabId = data.tabId;
                        if ($rootScope.selectedMasterItemId != null) {
                            vm.itemId = $rootScope.selectedMasterItemId;
                            loadVariance();
                        }
                        if ($rootScope.selectedMasterItemId == null) {
                            loadVariance();
                        }
                    }
                });
            })();
        }
    }
);