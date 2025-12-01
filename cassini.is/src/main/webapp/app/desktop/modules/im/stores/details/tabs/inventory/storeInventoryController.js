define(['app/desktop/modules/im/im.module',
        'app/shared/services/core/storeService',
        'app/shared/services/pm/project/bomService'
    ],
    function (module) {
        module.controller('StoreInventoryController', StoreInventoryController);

        function StoreInventoryController($scope, $rootScope, $timeout, $stateParams, $state, $cookies, StoreService, BomService) {
            var vm = this;

            vm.loading = true;
            vm.inventoryList = [];

            function loadInventory() {
                vm.loading = true;
                StoreService.getInventory($stateParams.projectId, $stateParams.storeId).then(
                    function (data) {
                        vm.loading = false;
                        vm.inventoryList = data;
                        $timeout(function () {
                            BomService.getBoqItemReferences($stateParams.projectId, vm.inventoryList, 'boqItem');
                            StoreService.getStoreReferences($stateParams.projectId, vm.inventoryList, 'store');
                        });

                    }
                )
            }

            (function () {
                if ($application.homeLoaded == true) {
                    loadInventory();
                }
            })();
        }
    }
);