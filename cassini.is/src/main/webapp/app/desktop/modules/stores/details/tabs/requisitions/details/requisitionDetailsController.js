/**
 * Created by swapna on 19/09/18.
 */
define(
    [
        'app/desktop/modules/stores/store.module',
        'app/desktop/modules/stores/details/tabs/requisitions/details/tabs/basic/requisitionBasicController',
        'app/desktop/modules/stores/details/tabs/requisitions/details/tabs/items/requisitionItemsController',
        'app/shared/services/store/requisitionService'
    ],
    function (module) {
        module.controller('RequisitionDetailsController', RequisitionDetailsController);

        function RequisitionDetailsController($scope, $rootScope, $timeout, $http, $state, $stateParams, RequisitionService) {

            var vm = this;

            vm.back = back;
            vm.stockReceive = null;
            vm.loading = true;
            vm.addItem = false;
            vm.store = null;
            vm.activeTab = 0;
            $rootScope.selectedRequisitionDetailsTab = null;
            $rootScope.hasNewItems = false;
            vm.detailsTabActivated = detailsTabActivated;
            vm.approveRequisition = approveRequisition;
            vm.addItems = addItems;
            vm.updateItems = updateItems;
            vm.nextPage = nextPage;
            vm.previousPage = previousPage;
            vm.printRequisitionChallan = printRequisitionChallan;

            vm.tabs = {
                basic: {
                    id: 'details.basic',
                    heading: 'Basic',
                    template: 'app/desktop/modules/stores/details/tabs/requisitions/details/tabs/basic/requisitionBasicView.jsp',
                    active: true,
                    activated: true,
                    index: 0
                },
                items: {
                    id: 'details.items',
                    heading: 'Items',
                    template: 'app/desktop/modules/stores/details/tabs/requisitions/details/tabs/items/requisitionItemsView.jsp',
                    active: false,
                    activated: false,
                    index: 1
                }
            };

            function back() {
                $state.go('app.store.details', {storeId: $rootScope.storeId, mode: 'REQ'});
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
                        $scope.$broadcast('app.stock.requisitionItems', {tabId: tabId});
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

            function addItems() {
                $scope.$broadcast('app.request.addItems', {requisition: $rootScope.requisition});
            }

            function approveRequisition() {
                $scope.$broadcast('app.request.approve', {requisition: $rootScope.requisition});
            }

            function updateItems() {
                $scope.$broadcast('app.request.updateItems', {requisition: $rootScope.requisition});
            }

            function loadRequisition() {
                vm.loading = true;
                RequisitionService.getRequisition($rootScope.storeId, $stateParams.requisitionId).then(
                    function (data) {
                        $rootScope.requisition = data;
                        $rootScope.viewInfo.title = "Requisition : " + $rootScope.requisition.requisitionNumber;
                    }
                )
            }

            function nextPage() {
                $scope.$broadcast('app.request.items.nextPageDetails');
            }

            function previousPage() {
                $scope.$broadcast('app.request.items.previousPageDetails');
            }

            function printRequisitionChallan() {
                RequisitionService.printRequisitionChallan(vm.customer, $rootScope.storeId, $stateParams.requisitionId).then(
                    function (data) {
                        var url = "{0}//{1}/api/is/stores/".format(window.location.protocol, window.location.host);
                        url += $rootScope.storeId + "/requisitions/file/" + data + "/download";

                        window.open(url, '_self');
                    }
                )
            }

            (function () {
                loadRequisition();
            })();
        }
    }
);