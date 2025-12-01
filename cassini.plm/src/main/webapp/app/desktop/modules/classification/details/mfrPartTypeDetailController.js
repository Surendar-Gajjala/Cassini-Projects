define(
    [
        'app/desktop/modules/classification/classification.module',
        'app/shared/services/core/itemTypeService',
        'app/shared/services/core/classificationService',
        'app/assets/bower_components/cassini-platform/app/desktop/directives/commonDirectives',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/autonumberService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/commonService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/lovService',
        'app/shared/services/core/mfrPartsService',
        'app/desktop/modules/classification/tabs/mfrPart/basic/mfrPartTypeBasicController',
        'app/desktop/modules/classification/tabs/mfrPart/attributes/mfrPartTypeAttributeController',
        'app/desktop/modules/classification/tabs/mfrPart/history/mfrPartTypeHistoryController'
    ],
    function (module) {
        module.controller('MfrPartTypeDetailController', MfrPartTypeDetailController);

        function MfrPartTypeDetailController($scope, $rootScope, $timeout, $window, $state, $stateParams, $cookies, ItemTypeService,
                                             ClassificationService, AutonumberService, CommonService, DialogService, LovService, $translate, MfrPartsService) {
            var vm = this;


            vm.tabs = {
                basic: {
                    id: 'details.basic',
                    heading: "Basic",
                    template: 'app/desktop/modules/classification/tabs/mfrPart/basic/mfrPartTypeBasicView.jsp',
                    index: 0,
                    active: true,
                    activated: true
                },
                attributes: {
                    id: 'details.attributes',
                    heading: 'Attributes',
                    template: 'app/desktop/modules/classification/tabs/mfrPart/attributes/mfrPartTypeAttributeView.jsp',
                    index: 1,
                    active: false,
                    activated: false
                },
                history: {
                    id: 'details.history',
                    heading: 'Timeline',
                    template: 'app/desktop/modules/classification/tabs/mfrPart/history/mfrPartTypeHistoryView.jsp',
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

            vm.mfrPartDetailsTabActivated = mfrPartDetailsTabActivated;
            function mfrPartDetailsTabActivated(tabId) {

                var tab = getTabById(tabId);
                vm.active = tab.index;
                if (tab != null) {
                    $rootScope.selectedTab = tab;
                    tab.activated = true;
                    $scope.$broadcast('app.mfrPartType.tabactivated', {tabId: tabId});
                }
                if (tab != null) {
                    activateTab(tab);
                }
            }

            function mfrPartTypeDetailsSelected(event, args) {
                $rootScope.selectedmfrPartType = args.typeObject;
                mfrPartDetailsTabActivated(vm.tabs.basic.id);
                $timeout(function () {
                    $rootScope.mfrPartTypeSelected();
                }, 1500)
            }

            (function () {
                $scope.$on('app.mfrPartType.selected', mfrPartTypeDetailsSelected);
            })();
        }
    }
);
