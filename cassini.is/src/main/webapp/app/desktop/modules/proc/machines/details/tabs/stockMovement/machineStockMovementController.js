define(['app/desktop/modules/proc/proc.module',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/commonService',
        'app/shared/services/core/itemService',
        'app/shared/services/core/storeService',
        'app/shared/services/core/inventoryService',
        'app/shared/services/store/topStockMovementService'
    ],
    function (module) {
        module.controller('MachineStockMovementController', MachineStockMovementController);

        function MachineStockMovementController($scope, $rootScope, $timeout, $state, $cookies, $stateParams, ItemService, CommonService, TopStockMovementService, StoreService) {
            var vm = this;
            vm.machineId = $stateParams.machineId;
            vm.machine = null;
            vm.itemStockMovement = [];

            function loadMachine() {
                vm.loading = true;
                $rootScope.showBusyIndicator($('.view-content'));
                ItemService.getMachineItem(vm.machineId).then(
                    function (data) {
                        vm.machine = data;
                        vm.loading = false;
                        CommonService.getPersonReferences([vm.machine], 'createdBy');
                        CommonService.getPersonReferences([vm.machine], 'modifiedBy');
                        loadStockMovement();
                    }
                )
            }

            function loadStockMovement() {
                TopStockMovementService.getStockMovementByItemNumber(vm.machine.itemNumber).then(
                    function (data) {
                        vm.itemStockMovement = data;
                        $rootScope.hideBusyIndicator();
                    }
                )
            }

            (function () {
                $scope.$on('app.machine.tabactivated', function (event, data) {
                    loadMachine();
                });
            })();
        }
    }
);