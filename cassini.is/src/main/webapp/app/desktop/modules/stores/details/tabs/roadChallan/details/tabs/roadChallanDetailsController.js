/**
 * Created by swapna on 19/09/18.
 */
define(
    [
        'app/desktop/modules/stores/store.module',
        'app/desktop/modules/stores/details/tabs/roadChallan/details/tabs/basic/roadChallanBasicController',
        'app/desktop/modules/stores/details/tabs/roadChallan/details/tabs/items/roadChallanItemsController',
        'app/shared/services/store/roadChallanService',
        'app/shared/services/store/topStoreService'
    ],
    function (module) {
        module.controller('RoadChallanDetailsController', RoadChallanDetailsController);

        function RoadChallanDetailsController($scope, $rootScope, $http, $timeout, $state, $stateParams, TopStoreService) {

            var vm = this;

            vm.back = back;
            vm.detailsTabActivated = detailsTabActivated;
            vm.printRoadChallan = printRoadChallan;
            vm.stockReceive = null;
            vm.loading = true;
            vm.addItem = false;
            vm.store = null;
            vm.activeTab = 0;
            vm.nextPage = nextPage;
            vm.previousPage = previousPage;
            $rootScope.selectedRoadChallanDetailsTab = null;

            vm.tabs = {
                basic: {
                    id: 'details.basic',
                    heading: 'Basic',
                    template: 'app/desktop/modules/stores/details/tabs/roadChallan/details/tabs/basic/roadChallanBasicView.jsp',
                    active: true,
                    activated: true,
                    index: 0
                },
                items: {
                    id: 'details.items',
                    heading: 'Items',
                    template: 'app/desktop/modules/stores/details/tabs/roadChallan/details/tabs/items/roadChallanItemsView.jsp',
                    active: false,
                    activated: false,
                    index: 1
                }
            };

            function back() {
                $state.go('app.store.details', {storeId: $rootScope.storeId, mode: 'RC'});
            }

            function activateTab(tab) {
                for (var t in vm.tabs) {
                    vm.tabs[t].active = (vm.tabs[t].id == tab.id);
                }
            }

            function detailsTabActivated(tabId) {
                var tab = getTabById(tabId);
                if (tab != null && !tab.activated) {
                    $rootScope.selectedRoadChallanDetailsTab = tabId;
                    tab.activated = true;
                    $scope.$broadcast('app.stock.roadChallanItems', {tabId: tabId});

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

            vm.showAttributes = showAttributes;

            function showAttributes() {
                $scope.$broadcast('app.roadChallan.items.attributes');
            }

            function printRoadChallan() {
                TopStoreService.printRoadChallan($stateParams.roadchallanId).then(
                    function (data) {
                        var url = "{0}//{1}/api/is/stores/file/".format(window.location.protocol, window.location.host);
                        url += data + "/download";
                        window.open(url, '_self');
                    }
                )
            }

            function nextPage() {
                $scope.$broadcast('app.roadChallan.items.nextPageDetails');
            }

            function previousPage() {
                $scope.$broadcast('app.roadChallan.items.previousPageDetails');
            }

            (function () {
            })();
        }
    }
);