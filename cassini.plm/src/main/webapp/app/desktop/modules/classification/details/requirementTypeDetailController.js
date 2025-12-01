define(
    ['app/desktop/modules/classification/classification.module',
        'app/shared/services/core/requirementsTypeService',
        'app/shared/services/core/classificationService',
        'app/shared/services/core/workflowService',
        'app/assets/bower_components/cassini-platform/app/desktop/directives/commonDirectives',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/autonumberService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/commonService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/lovService',
        'app/desktop/modules/classification/tabs/requirements/basic/requirementTypeBasicController',
        'app/desktop/modules/classification/tabs/requirements/attributes/requirementTypeAttributeController',
        'app/desktop/modules/classification/tabs/requirements/history/requirementTypeHistoryController'
    ],
    function (module) {
        module.controller('requirementTypeDetailController', requirementTypeDetailController);

        function requirementTypeDetailController($scope, $rootScope, $timeout, $window, $state, $stateParams, $cookies, $translate,
                                                  ClassificationService, ItemTypeService, AutonumberService,
                                                  CommonService, DialogService, LovService, RequirementsTypeService) {
            var vm = this;


            vm.tabs = {
                basic: {
                    id: 'details.basic',
                    heading: "Basic",
                    template: 'app/desktop/modules/classification/tabs/requirements/basic/requirementTypeBasicView.jsp',
                    index: 0,
                    active: true,
                    activated: true
                },
                attributes: {
                    id: 'details.attributes',
                    heading: 'Attributes',
                    template: 'app/desktop/modules/classification/tabs/requirements/attributes/requirementTypeAttributeView.jsp',
                    index: 1,
                    active: false,
                    activated: false
                },
                history: {
                    id: 'details.history',
                    heading: 'Timeline',
                    template: 'app/desktop/modules/classification/tabs/requirements/history/requirementTypeHistoryView.jsp',
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

            vm.requirementDetailsTabActivated = requirementDetailsTabActivated;
            function requirementDetailsTabActivated(tabId) {

                var tab = getTabById(tabId);
                vm.active = tab.index;
                if (tab != null) {
                    $rootScope.selectedTab = tab;
                    tab.activated = true;
                    $scope.$broadcast('app.rmType.tabactivated', {tabId: tabId});
                }
                if (tab != null) {
                    activateTab(tab);
                }
            }

            function rmTypeDetailsSelected(event, args) {
                $rootScope.selectedQualityType = args.typeObject;
                requirementDetailsTabActivated(vm.tabs.basic.id);
            }

            (function () {
                 $scope.$on('app.rmType.selected', rmTypeDetailsSelected);
            })();
        }
    }
);
