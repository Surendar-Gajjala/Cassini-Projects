define(['app/desktop/modules/im/im.module',
        'app/shared/services/core/storeService',
        'app/shared/services/pm/project/bomService'
    ],
    function (module) {
        module.controller('StockMovementController', StockMovementController);

        function StockMovementController($scope, $rootScope, $timeout, $stateParams, $state, $cookies, StoreService, BomService) {
            var vm = this;

            vm.historyList = [];
            vm.loading = true;

            function loadHistory() {
                vm.loading = true;
                StoreService.getHistory($stateParams.projectId, $stateParams.storeId).then(
                    function (data) {
                        vm.loading = false;
                        vm.historyList = data;
                        BomService.getBoqItemReferences($stateParams.projectId, vm.historyList, 'boqItem');
                        StoreService.getStoreReferences($stateParams.projectId, vm.historyList, 'store');
                    }
                )
            }

            (function () {
                if ($application.homeLoaded == true) {
                    loadHistory();
                }
            })();
        }
    }
);