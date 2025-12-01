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
        'app/desktop/modules/classification/tabs/change/basic/changeTypeBasicController',
        'app/desktop/modules/classification/tabs/change/attributes/changeTypeAttributesController',
        'app/desktop/modules/classification/tabs/change/history/changeTypeHistoryController'
    ],
    function (module) {
        module.controller('ChangeTypeDetailController', ChangeTypeDetailController);

        function ChangeTypeDetailController($q, $scope, $rootScope, $timeout, $state, DialogService, $stateParams, $cookies,
                                            CommonService, $translate) {

            var vm = this;


            vm.tabs = {
                basic: {
                    id: 'details.basic',
                    heading: "Basic",
                    template: 'app/desktop/modules/classification/tabs/change/basic/changeTypeBasicView.jsp',
                    index: 0,
                    active: true,
                    activated: true
                },
                attributes: {
                    id: 'details.attributes',
                    heading: 'Attributes',
                    template: 'app/desktop/modules/classification/tabs/change/attributes/changeTypeAttributesView.jsp',
                    index: 1,
                    active: false,
                    activated: false
                },
                history: {
                    id: 'details.history',
                    heading: 'Timeline',
                    template: 'app/desktop/modules/classification/tabs/change/history/changeTypeHistoryView.jsp',
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

            vm.changeDetailsTabActivated = changeDetailsTabActivated;
            function changeDetailsTabActivated(tabId) {

                var tab = getTabById(tabId);
                vm.active = tab.index;
                if (tab != null) {
                    $rootScope.selectedTab = tab;
                    tab.activated = true;
                    $scope.$broadcast('app.changeType.tabactivated', {tabId: tabId});
                }
                if (tab != null) {
                    activateTab(tab);
                }
            }

            function changeTypeDetailsSelected(event, args) {
                $rootScope.selectedChangeType = args.typeObject;
                $timeout(function () {
                    $rootScope.changeTypeSelected();
                }, 1000)
                changeDetailsTabActivated(vm.tabs.basic.id);
            }


            (function () {
                $scope.$on('app.changeType.selected', changeTypeDetailsSelected);
            })();
        }
    }
);