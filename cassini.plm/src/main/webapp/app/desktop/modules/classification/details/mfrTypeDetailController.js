define(
    ['app/desktop/modules/classification/classification.module',
        'app/shared/services/core/requirementsTypeService',
        'app/shared/services/core/classificationService',
        'app/shared/services/core/workflowService',
        'app/assets/bower_components/cassini-platform/app/desktop/directives/commonDirectives',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/autonumberService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/commonService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/lovService',
        'app/desktop/modules/classification/tabs/manufactur/basic/mfrTypeBasicController',
        'app/desktop/modules/classification/tabs/manufactur/attributes/mfrTypeAttributeController',
        'app/desktop/modules/classification/tabs/manufactur/history/mfrTypeHistoryController'
    ],
    function (module) {
        module.controller('MfrTypeDetailController', MfrTypeDetailController);

        function MfrTypeDetailController($scope, $rootScope, $timeout, $window, $state, $stateParams, $cookies, $translate,
                                         ClassificationService, ItemTypeService, AutonumberService,
                                         CommonService, DialogService, LovService, RequirementsTypeService) {
            var vm = this;


            vm.tabs = {
                basic: {
                    id: 'details.basic',
                    heading: "Basic",
                    template: 'app/desktop/modules/classification/tabs/manufactur/basic/mfrTypeBasicView.jsp',
                    index: 0,
                    active: true,
                    activated: true
                },
                attributes: {
                    id: 'details.attributes',
                    heading: 'Attributes',
                    template: 'app/desktop/modules/classification/tabs/manufactur/attributes/mfrTypeAttributeView.jsp',
                    index: 1,
                    active: false,
                    activated: false
                },
                history: {
                    id: 'details.history',
                    heading: 'Timeline',
                    template: 'app/desktop/modules/classification/tabs/manufactur/history/mfrTypeHistoryView.jsp',
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

            vm.mfrDetailsTabActivated = mfrDetailsTabActivated;
            function mfrDetailsTabActivated(tabId) {

                var tab = getTabById(tabId);
                vm.active = tab.index;
                if (tab != null) {
                    $rootScope.selectedTab = tab;
                    tab.activated = true;
                    $scope.$broadcast('app.mfrType.tabactivated', {tabId: tabId});
                }
                if (tab != null) {
                    activateTab(tab);
                }
            }

            function mfrTypeDetailsSelected(event, args) {
                $rootScope.selectedmfrType = args.typeObject;
                mfrDetailsTabActivated(vm.tabs.basic.id);
                $timeout(function () {
                    $rootScope.mfrTypeSelected();
                }, 1000)
            }


            (function () {
                $scope.$on('app.mfrType.selected', mfrTypeDetailsSelected);
            })();
        }
    }
);
