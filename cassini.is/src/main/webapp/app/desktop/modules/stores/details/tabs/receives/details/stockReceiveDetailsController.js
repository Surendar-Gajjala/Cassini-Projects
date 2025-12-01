define(
    [
        'app/desktop/modules/stores/store.module',
        'app/shared/services/store/topStockReceivedService',
        'app/desktop/modules/stores/details/tabs/receives/details/tabs/basic/stockReceiveBasicDetailsController',
        'app/desktop/modules/stores/details/tabs/receives/details/tabs/item/stockReceiveItemsController',
        'app/shared/services/store/topStoreService'
    ],
    function (module) {
        module.controller('StockReceiveDetailsController', StockReceiveDetailsController);

        function StockReceiveDetailsController($scope, $rootScope, $timeout, $state, $stateParams, ItemService, TopStockReceivedService, TopStoreService) {

            var vm = this;

            vm.back = back;
            vm.detailsTabActivated = detailsTabActivated;
            vm.showAttributes = showAttributes;
            vm.stockReceive = null;
            vm.loading = true;
            vm.addItem = false;
            vm.store = null;
            vm.activeTab = 0;
            vm.nextPage = nextPage;
            vm.previousPage = previousPage;
            vm.printReceiveChallan = printReceiveChallan;
            $rootScope.selectedStockReceiveDetailsTab = null;

            vm.tabs = {
                basic: {
                    id: 'details.basic',
                    heading: 'Basic',
                    template: 'app/desktop/modules/stores/details/tabs/receives/details/tabs/basic/stockReceiveBasicDetailsView.jsp',
                    active: true,
                    activated: true,
                    index: 0
                },
                items: {
                    id: 'details.items',
                    heading: 'Items',
                    template: 'app/desktop/modules/stores/details/tabs/receives/details/tabs/item/stockReceiveItemsView.jsp',
                    active: false,
                    activated: false,
                    index: 1
                }
            };

            function back() {
                $state.go('app.store.details', {storeId: $rootScope.storeId, mode: 'RECEIVE'});
            }

            function activateTab(tab) {
                for (var t in vm.tabs) {
                    vm.tabs[t].active = (vm.tabs[t].id == tab.id);
                }
            }

            function showAttributes() {
                $scope.$broadcast('app.receive.items.attributes');
            }

            function detailsTabActivated(tabId) {
                var tab = getTabById(tabId);
                $rootScope.selectedStockReceiveDetailsTab = tabId;
                if (tab != null && !tab.activated) {
                    tab.activated = true;
                    $rootScope.$broadcast('app.stock.receiveItems', {tabId: tabId});

                    if (tab == "items") {
                        vm.addItem = true;
                    }
                    else {
                        vm.addItem = false;
                    }
                }
                if (tab != null) {
                    activateTab(tab);
                }
            }

            function getTabById(tabId) {
                var tab = null;
                for (var t in vm.tabs) {
                    if (vm.tabs.hasOwnProperty(t) && vm.tabs[t].id == tabId) {
                        tab = t;
                    }
                }
                return tab;
            }

            function loadStore() {
                TopStoreService.getTopStore($rootScope.storeId).then(
                    function (data) {
                        vm.store = data;
                        $rootScope.viewInfo.description = "Store : " + vm.store.storeName;
                    }
                )
            }

            function printReceiveChallan() {
                TopStoreService.printReceiveChallan($stateParams.receiveId).then(
                    function (data) {
                        var url = "{0}//{1}/api/is/stores/file/".format(window.location.protocol, window.location.host);
                        url += data + "/download";

                        window.open(url, '_self');
                    }
                )
            }

            function nextPage() {
                $scope.$broadcast('app.stores.receiveItems.nextPageDetails');
            }

            function previousPage() {
                $scope.$broadcast('app.stores.receiveItems.previousPageDetails');
            }

            (function () {
                loadStore();
            })();
        }
    }
);