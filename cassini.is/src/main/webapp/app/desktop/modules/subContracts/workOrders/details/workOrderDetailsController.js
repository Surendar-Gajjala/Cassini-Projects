/**
 * Created by swapna on 23/01/19.
 */
define(['app/desktop/modules/subContracts/contracts.module',
        'app/desktop/modules/subContracts/workOrders/details/tabs/basic/workOrderBasicController',
        'app/desktop/modules/subContracts/workOrders/details/tabs/items/workOrderItemsController',
        'app/shared/services/core/subContractService'
    ],
    function (module) {
        module.controller('WorkOrderDetailsController', WorkOrderDetailsController);

        function WorkOrderDetailsController($scope, $rootScope, $timeout, $state, $cookies, $stateParams, $window,
                                            SubContractService) {

            var vm = this;

            $rootScope.viewInfo.title = "Work Order Details ";
            $rootScope.viewInfo.icon = "fa fa-hourglass";

            vm.activeTab = 0;
            vm.tabActive = 0;
            vm.back = back;
            vm.nextPage = nextPage;
            vm.previousPage = previousPage;
            vm.workOrderDetailsTabActivated = workOrderDetailsTabActivated;
            vm.newWOItem = newWOItem;
            vm.showAttributes = showAttributes;

            $rootScope.selectedWorkOrderDetailsTab = null;

            vm.tabs = {
                basic: {
                    id: 'details.basic',
                    heading: 'Basic',
                    template: 'app/desktop/modules/subContracts/workOrders/details/tabs/basic/workOrderBasicView.jsp',
                    index: 0,
                    active: true,
                    activated: true
                },
                items: {
                    id: 'details.items',
                    heading: 'Items',
                    template: 'app/desktop/modules/subContracts/workOrders/details/tabs/items/workOrderItemsView.jsp',
                    index: 1,
                    active: false,
                    activated: false
                }
            };

            function activateTab(tab) {
                for (var t in vm.tabs) {
                    if (vm.tabs.hasOwnProperty(t)) {
                        vm.tabs[t].active = (t != undefined && t == tab);
                    }
                }
            }

            function workOrderDetailsTabActivated(tabId) {
                $scope.$broadcast('app.workOrder.tabactivated', {tabId: tabId});
                $rootScope.selectedWorkOrderDetailsTab = tabId;
                var tab = getTabById(tabId);
                activateTab(tab);
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

            function showAttributes() {
                $scope.$broadcast('app.workOrder.items.attributes');
            }

            function newWOItem() {
                $rootScope.$broadcast('app.workOrder.createWOItem');
            }

            /*
             function back() {
             $state.go('app.contracts.workOrders');
             }*/

            function back() {
                window.history.back();
            }

            function nextPage() {
                $scope.$broadcast('app.workOrder.items.nextPageDetails');
            }

            function previousPage() {
                $scope.$broadcast('app.workOrder.items.previousPageDetails');
            }

            (function () {
            })();
        }
    }
);