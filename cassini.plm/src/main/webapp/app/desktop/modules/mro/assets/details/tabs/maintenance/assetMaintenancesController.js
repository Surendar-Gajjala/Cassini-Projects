define(
    [
        'app/desktop/modules/mes/mes.module',
        'app/desktop/modules/directives/objectAttributeDetailsDirectiveController',
        'app/shared/services/core/assetService'
    ],
    function (module) {
        module.controller('AssetMaintenanceController', AssetMaintenanceController);

        function AssetMaintenanceController($scope, $rootScope, $sce, $timeout, $state, $stateParams, DialogService, AssetService) {
            var vm = this;
            vm.assetId = $stateParams.assetId;
            vm.loading = false;
            vm.assetWorkOrders = [];


            function loadAssetWorkOrders() {
                vm.assetWorkOrders = [];
                vm.loading = true;
                AssetService.getAssetWorkOrders(vm.assetId).then(
                    function (data) {
                        vm.assetWorkOrders = data;
                        $rootScope.hideBusyIndicator();
                        vm.loading = false;
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                        $rootScope.hideBusyIndicator();
                    }
                )
            }

            vm.showWorkOrder = showWorkOrder;

            function showWorkOrder(workOrder) {
                $state.go('app.mro.workOrder.details', {workOrderId: workOrder.id, tab: 'details.basic'});
            }

            vm.showMaintenancePlan = showMaintenancePlan;
            function showMaintenancePlan(workOrder) {
                $state.go('app.mro.maintenancePlan.details', {
                    maintenancePlanId: workOrder.plan,
                    tab: 'details.basic'
                });
            }

            vm.showWorkRequest = showWorkRequest;
            function showWorkRequest(workOrder) {
                $state.go('app.mro.workRequest.details', {workRequestId: workOrder.request, tab: 'details.basic'});
            }

            (function () {
                $scope.$on('app.asset.tabActivated', function (event, data) {
                    if (data.tabId == 'details.maintenance') {
                        loadAssetWorkOrders();
                    }
                })
            })();
        }
    }
);
