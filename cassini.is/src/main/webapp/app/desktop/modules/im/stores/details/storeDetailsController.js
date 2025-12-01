define(['app/desktop/modules/im/im.module',
        'app/desktop/modules/im/stores/details/tabs/basic/storeBasicController',
        'app/shared/services/core/storeService',
        'app/desktop/modules/im/stores/details/tabs/inventory/storeInventoryController',
        'app/desktop/modules/im/stores/details/tabs/stockMovement/stockMovementController'

    ],
    function (module) {
        module.controller('StoreDetailsController', StoreDetailsController);

        function StoreDetailsController($scope, $rootScope, $timeout, $state, $cookies, StoreService, $stateParams) {
            var vm = this;

            $rootScope.viewInfo.icon = "fa fa-shopping-cart";
            $rootScope.viewInfo.title = "Store Details";

            vm.activeTab = 0;
            vm.storeDetailsTabActivated = storeDetailsTabActivated;
            vm.updateStore = updateStore;
            vm.stockIssued = stockIssued;
            vm.stockReceived = stockReceived;
            vm.back = back;

            vm.tabs = {
                basic: {
                    id: 'details.basic',
                    heading: 'Basic',
                    template: 'app/desktop/modules/im/stores/details/tabs/basic/storeBasicView.jsp',
                    active: false
                },
                inventory: {
                    id: 'details.inventory',
                    heading: 'Inventory',
                    template: 'app/desktop/modules/im/stores/details/tabs/inventory/storeInventoryView.jsp',
                    active: true
                },
                stockMovement: {
                    id: 'details.stockMovement',
                    heading: 'StockMovement',
                    template: 'app/desktop/modules/im/stores/details/tabs/stockMovement/stockMovementView.jsp',
                    active: false
                }
            };

            function activateTab(tab) {
                for (var t in vm.tabs) {
                    if (vm.tabs.hasOwnProperty(t)) {
                        vm.tabs[t].active = (t != undefined && t == tab);
                    }
                }
            }

            function storeDetailsTabActivated(tabId) {
                $scope.$broadcast('app.store.tabactivated', {tabId: tabId})
                var tab = getTabById(tabId);
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

            function back() {
                window.history.back();
            }

            function updateStore() {
                $scope.$broadcast('app.Store.update');
            }

            function stockIssued() {
                $state.go('app.pm.project.stores.stock.issued', {
                    storeId: $stateParams.storeId
                });
            }

            function stockReceived() {
                $state.go('app.pm.project.stores.stock.received', {
                    storeId: $stateParams.storeId
                });
            }

            vm.storeId = $stateParams.storeId;

            function loadStore() {
                StoreService.getStore($stateParams.projectId, vm.storeId).then(
                    function (data) {
                        vm.store = data;
                        $rootScope.viewInfo.title = vm.store.name;
                    }
                )
            }

            (function () {
                if ($application.homeLoaded == true) {
                    $rootScope.showComments('STORE', $stateParams.storeId);
                    if ($stateParams.mode == 'ISSUED' || $stateParams.mode == 'RECEIVED') {
                        vm.activeTab = 2;
                    }
                }
            })();
        }
    }
);