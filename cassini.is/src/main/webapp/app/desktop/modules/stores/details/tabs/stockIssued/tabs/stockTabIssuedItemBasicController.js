define(['app/desktop/modules/stores/store.module',
        'app/shared/services/store/topStoreService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/commonService'
    ],
    function (module) {
        module.controller('StockTabReceivedItemBasicController', StockTabReceivedItemBasicController);

        function StockTabReceivedItemBasicController($scope, $rootScope, $timeout, $state, $cookies, $stateParams, TopStoreService, CommonService) {

            var vm = this;

            vm.loading = true;
            vm.back = back;
            vm.stockReceviedItem = $stateParams.receviedItemId;

            function back() {
                $window.back();
            }

            function loadStockReceviedItem() {
                vm.stockReceviedItem = $stateParams.receviedItemObj;

                TopStoreService.getTopStore(vm.storeId).then(
                    function (data) {
                        vm.stockReceviedItem = data;
                        $rootScope.viewInfo.title = "Stock Received Item Details : " + vm.stockReceviedItem.boqName;
                    }
                )
            }

            function updateReceivedItem() {
                TopStoreService.updateTopStore(vm.store).then(
                    function (data) {
                        loadStockReceviedItem();
                        $rootScope.showSuccessMessage("Stock Received item updated successfully");
                    }
                )
            }

            (function () {
                loadStockReceviedItem();
                $scope.$on('app.stock.ReceivedItem.update', function () {
                    updateReceivedItem();
                })
            })();
        }
    }
);