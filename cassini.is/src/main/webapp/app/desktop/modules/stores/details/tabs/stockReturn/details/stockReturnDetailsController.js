/**
 * Created by swapna on 05/12/18.
 */
define(
    [
        'app/desktop/modules/stores/store.module',
        'app/desktop/modules/stores/details/tabs/stockReturn/details/tabs/items/stockReturnItemsController',
        'app/desktop/modules/stores/details/tabs/stockReturn/details/tabs/basic/stockReturnBasicController',
        'app/shared/services/store/stockReturnService'
    ],
    function (module) {
        module.controller('StockReturnDetailsController', StockReturnDetailsController);

        function StockReturnDetailsController($scope, $rootScope, $timeout, $http, $state, $stateParams, StockReturnService) {

            var vm = this;

            vm.back = back;
            vm.stockReceive = null;
            vm.loading = true;
            vm.addItem = false;
            vm.store = null;
            vm.activeTab = 0;
            vm.detailsTabActivated = detailsTabActivated;
            vm.nextPage = nextPage;
            vm.previousPage = previousPage;
            vm.printStockReturnChallan = printStockReturnChallan;
            vm.approveStockReturn = approveStockReturn;

            vm.tabs = {
                basic: {
                    id: 'details.basic',
                    heading: 'Basic',
                    template: 'app/desktop/modules/stores/details/tabs/stockReturn/details/tabs/basic/stockReturnBasicView.jsp',
                    active: true,
                    activated: true,
                    index: 0
                },
                items: {
                    id: 'details.items',
                    heading: 'Items',
                    template: 'app/desktop/modules/stores/details/tabs/stockReturn/details/tabs/items/stockReturnItemsView.jsp',
                    active: false,
                    activated: false,
                    index: 1
                }
            };

            function back() {
                $state.go('app.store.details', {storeId: $rootScope.storeId, mode: 'RET'});
            }

            function activateTab(tab) {
                for (var t in vm.tabs) {
                    vm.tabs[t].active = (vm.tabs[t].id == tab.id);
                }
            }

            function detailsTabActivated(tabId) {
                var tab = getTabById(tabId);
                $rootScope.selectedRequisitionDetailsTab = tabId;
                if (tab != null && !tab.activated) {
                    tab.activated = true;

                    if (tab == "items") {
                        vm.addItem = true;
                        $scope.$broadcast('app.stock.stockReturnItems', {tabId: tabId});
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

            function printStockReturnChallan() {
                StockReturnService.printStockReturnChallan($rootScope.storeId, $rootScope.stockReturnId).then(
                    function (data) {
                        var url = "{0}//{1}/api/is/stores/".format(window.location.protocol, window.location.host);
                        url += $rootScope.storeId + "/stockReturn/" + $rootScope.stockReturnId + "/file/" + data + "/download";

                        window.open(url, '_self');
                    }
                )
            }

            function approveStockReturn() {
                $scope.$broadcast('app.stockReturn.approve', {stockReturn: $rootScope.stockReturn});
            }

            function nextPage() {
                $scope.$broadcast('app.stockReturn.items.nextPageDetails');
            }

            function previousPage() {
                $scope.$broadcast('app.stockReturn.items.previousPageDetails');
            }

        }
    }
);