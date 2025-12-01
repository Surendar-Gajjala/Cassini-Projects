define(
    [
        'app/desktop/modules/settings/settings.module',
        'split-pane',
        'jquery.easyui',
        'app/desktop/modules/classification/tabs/itemType/basic/itemTypeBasicController',
        'app/desktop/modules/classification/tabs/itemType/attributes/itemTypeAttributesController',
        'app/desktop/modules/classification/tabs/itemType/history/itemTypeHistoryController',
        'app/assets/bower_components/cassini-platform/app/desktop/directives/commonDirectives',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/commonService',
    ],
    function (module) {
        module.controller('ItemTypeDetailController', ItemTypeDetailController);

        function ItemTypeDetailController($q, $scope, $rootScope, $timeout, $state, DialogService, $stateParams, $cookies,
                                          CommonService, $translate) {

            var vm = this;


            vm.tabs = {
                basic: {
                    id: 'details.basic',
                    heading: "Basic",
                    template: 'app/desktop/modules/classification/tabs/itemType/basic/itemTypeBasicView.jsp',
                    index: 0,
                    active: true,
                    activated: true
                },
                attributes: {
                    id: 'details.attributes',
                    heading: 'Attributes',
                    template: 'app/desktop/modules/classification/tabs/itemType/attributes/itemTypeAttributesView.jsp',
                    index: 1,
                    active: false,
                    activated: false
                },
                history: {
                    id: 'details.history',
                    heading: 'Timeline',
                    template: 'app/desktop/modules/classification/tabs/itemType/history/itemTypeHistoryView.jsp',
                    index: 2,
                    active: false,
                    activated: false
                }
            }

            function getTabById(tabId) {
                var tab = null;
                for (var t in vm.tabs) {
                    if (vm.tabs.hasOwnProperty(t) && vm.tabs[t].id == tabId) {
                        tab = vm.tabs[t];
                    }
                }

                return tab;
            }

            function activateTab(tab) {
                for (var t in vm.tabs) {
                    vm.tabs[t].active = (vm.tabs[t].id == tab.id);
                }
            }

            vm.itemDetailsTabActivated = itemDetailsTabActivated;
            function itemDetailsTabActivated(tabId) {

                var tab = getTabById(tabId);
                vm.active = tab.index;
                if (tab != null) {
                    $rootScope.selectedTab = tab;
                    tab.activated = true;
                    $scope.$broadcast('app.itemType.tabactivated', {tabId: tabId});
                }
                if (tab != null) {
                    activateTab(tab);
                }
            }

            function itemTypeDetailsSelected(event, args) {
                $rootScope.selectedItemType = args.typeObject;
                itemDetailsTabActivated(vm.tabs.basic.id);
                $timeout(function () {
                    $rootScope.itemTypeSelected();
                }, 1000)
            }


            (function () {
                $scope.$on('app.itemType.selected', itemTypeDetailsSelected);
            })();
        }
    }
);