/**
 * Created by swapna on 19/09/18.
 */
define(
    [
        'app/desktop/modules/stores/store.module',
        'app/desktop/modules/stores/details/tabs/purchaseOrders/details/tabs/basic/purchaseOrderBasicDetailsController',
        'app/desktop/modules/stores/details/tabs/purchaseOrders/details/tabs/items/purchaseOrderRequisitionItemsController',
        'app/shared/services/store/customPurchaseOrderService',
        'app/shared/services/store/topStoreService'
    ],
    function (module) {
        module.controller('PurchaseOrderDetailsController', PurchaseOrderDetailsController);

        function PurchaseOrderDetailsController($scope, $rootScope, $timeout, $http, CustomPurchaseOrderService, $state, $stateParams, TopStoreService) {

            $rootScope.viewInfo.icon = "fa fa-sign-in";
            $rootScope.viewInfo.title = "Purchase Order Details";

            var vm = this;

            vm.addItems = addItems;
            vm.nextPage = nextPage;
            vm.previousPage = previousPage;
            vm.updatePurchaseOrder = updatePurchaseOrder;
            vm.approvePurchaseOrder = approvePurchaseOrder;
            vm.printPurchaseOrderChallan = printPurchaseOrderChallan;

            vm.back = back;
            vm.detailsTabActivated = detailsTabActivated;
            vm.loading = true;
            vm.activeTab = 0;

            vm.tabs = {
                basic: {
                    id: 'details.basic',
                    heading: 'Basic',
                    template: 'app/desktop/modules/stores/details/tabs/purchaseOrders/details/tabs/basic/purchaseOrderBasicDetailsView.jsp',
                    active: true,
                    activated: true,
                    index: 0
                },
                items: {
                    id: 'details.items',
                    heading: 'Items',
                    template: 'app/desktop/modules/stores/details/tabs/purchaseOrders/details/tabs/items/purchaseOrderRequisitionItemsView.jsp',
                    active: false,
                    activated: false,
                    index: 1
                }
            };

            function addItems() {
                $scope.$broadcast('app.po.addItems', {purchaseOrder: vm.purchaseOrder});
            }

            function approvePurchaseOrder() {
                $scope.$broadcast('app.po.approve', {purchaseOrder: vm.purchaseOrder});
            }

            function updatePurchaseOrder() {
                $scope.$broadcast('app.po.update', {purchaseOrder: vm.purchaseOrder});
            }

            function nextPage() {
                if (vm.tabActive == 2) {
                    $scope.$broadcast('app.po.items.nextPageDetails');
                } else if (vm.tabActive == 3) {
                    $scope.$broadcast('app.po.items.nextPageDetails');
                }
            }

            function previousPage() {
                if (vm.tabActive == 2) {
                    $scope.$broadcast('app.po.items.previousPageDetails');
                } else if (vm.tabActive == 3) {
                    $scope.$broadcast('app.po.items.previousPageDetails');
                }
            }

            function back() {
                $state.go('app.store.details', {storeId: $rootScope.storeId, mode: 'PO'});
            }

            function activateTab(tab) {
                for (var t in vm.tabs) {
                    vm.tabs[t].active = (vm.tabs[t].id == tab.id);
                }
            }

            function detailsTabActivated(tabId) {
                var tab = getTabById(tabId);
                if (tab != null && !tab.activated) {
                    tab.activated = true;

                    if (tab == "items") {
                        vm.addItem = true;
                        vm.showApproveButton = false;
                        $scope.$broadcast('app.po.itemsTabActivated', {tabId: tabId});
                    }
                    else {
                        vm.addItem = false;
                        vm.showApproveButton = true;
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

            function loadPurchaseOrder() {
                vm.loading = true;
                CustomPurchaseOrderService.getPurchaseOrder($stateParams.purchaseOrderId).then(
                    function (data) {
                        vm.purchaseOrder = data;
                        $rootScope.purchaseOrderId = vm.purchaseOrder.id;
                        $rootScope.viewInfo.title = "Purchase Order Details (" + vm.purchaseOrder.poNumber + ")";
                    }
                )
            }

            function printPurchaseOrderChallan() {
                TopStoreService.printPurchaseOrderChallan($stateParams.purchaseOrderId).then(
                    function (data) {
                        var url = "{0}//{1}/api/is/stores/file/".format(window.location.protocol, window.location.host);
                        url += data + "/download";
                        window.open(url, '_self');
                    }
                )
            }

            (function () {
                loadPurchaseOrder();
            })();
        }
    }
);

