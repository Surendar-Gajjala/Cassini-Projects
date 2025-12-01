define(
    ['app/desktop/modules/classification/classification.module',
        'app/shared/services/core/requirementsTypeService',
        'app/shared/services/core/classificationService',
        'app/shared/services/core/workflowService',
        'app/assets/bower_components/cassini-platform/app/desktop/directives/commonDirectives',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/autonumberService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/commonService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/lovService',
        'app/desktop/modules/classification/tabs/workflow/basic/workflowTypeBasicController',
        'app/desktop/modules/classification/tabs/workflow/attributes/workflowTypeAttributeController',
        'app/desktop/modules/classification/tabs/workflow/history/workflowTypeHistoryController'
    ],
    function (module) {
        module.controller('WorkflowTypeDetailController', WorkflowTypeDetailController);

        function WorkflowTypeDetailController($scope, $rootScope, $timeout, $window, $state, $stateParams, $cookies, $translate,
                                              ClassificationService, ItemTypeService, AutonumberService,
                                              CommonService, DialogService, LovService, RequirementsTypeService) {
            var vm = this;


            vm.tabs = {
                basic: {
                    id: 'details.basic',
                    heading: "Basic",
                    template: 'app/desktop/modules/classification/tabs/workflow/basic/workflowTypeBasicView.jsp',
                    index: 0,
                    active: true,
                    activated: true
                },
                attributes: {
                    id: 'details.attributes',
                    heading: 'Attributes',
                    template: 'app/desktop/modules/classification/tabs/workflow/attributes/workflowTypeAttributeView.jsp',
                    index: 1,
                    active: false,
                    activated: false
                },
                history: {
                    id: 'details.history',
                    heading: 'Timeline',
                    template: 'app/desktop/modules/classification/tabs/workflow/history/workflowTypeHistoryView.jsp',
                    index: 1,
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

            vm.workflowDetailsTabActivated = workflowDetailsTabActivated;
            function workflowDetailsTabActivated(tabId) {

                var tab = getTabById(tabId);
                vm.active = tab.index;
                if (tab != null) {
                    $rootScope.selectedTab = tab;
                    tab.activated = true;
                    $scope.$broadcast('app.workflowType.tabactivated', {tabId: tabId});
                }
                if (tab != null) {
                    activateTab(tab);
                }
            }


            function workflowTypeDetailsSelected(event, args) {
                $rootScope.selectedWfType = args.typeObject;
                workflowDetailsTabActivated(vm.tabs.basic.id);
                $timeout(function () {
                    $rootScope.workflowTypeSelected();
                }, 1000);
            }

            (function () {
                $scope.$on('app.workflowType.selected', workflowTypeDetailsSelected);
            })();
        }
    }
);
