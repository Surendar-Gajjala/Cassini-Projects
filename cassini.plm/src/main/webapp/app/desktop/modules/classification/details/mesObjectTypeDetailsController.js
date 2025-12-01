define(
    ['app/desktop/modules/classification/classification.module',
        'app/shared/services/core/classificationService',
        'app/assets/bower_components/cassini-platform/app/desktop/directives/commonDirectives',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/autonumberService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/commonService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/lovService',
        'app/desktop/modules/classification/tabs/mesObjectTypes/basic/mesObjectTypeBasicController',
        'app/desktop/modules/classification/tabs/mesObjectTypes/attributes/mesObjectTypeAttributesController',
        'app/desktop/modules/classification/tabs/mesObjectTypes/history/mesObjectTypeHistoryController',
        'app/shared/services/core/mesObjectTypeService'
    ],
    function (module) {
        module.controller('MESObjectTypeDetailsController', MESObjectTypeDetailsController);

        function MESObjectTypeDetailsController($scope, $rootScope, $timeout, $window, $state, $stateParams, $cookies, $translate, ItemTypeService, AutonumberService,
                                                CommonService, DialogService, LovService, MESObjectTypeService) {
            var vm = this;


            vm.tabs = {
                basic: {
                    id: 'details.basic',
                    heading: "Basic",
                    template: 'app/desktop/modules/classification/tabs/mesObjectTypes/basic/mesObjectTypeBasicView.jsp',
                    index: 0,
                    active: true,
                    activated: true
                },
                attributes: {
                    id: 'details.attributes',
                    heading: 'Attributes',
                    template: 'app/desktop/modules/classification/tabs/mesObjectTypes/attributes/mesObjectTypeAttributesView.jsp',
                    index: 1,
                    active: false,
                    activated: false
                },
                history: {
                    id: 'details.history',
                    heading: 'History',
                    template: 'app/desktop/modules/classification/tabs/mesObjectTypes/history/mesObjectTypeHistoryView.jsp',
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

            vm.mesObjectDetailsTabActivated = mesObjectDetailsTabActivated;
            function mesObjectDetailsTabActivated(tabId) {

                var tab = getTabById(tabId);
                vm.active = tab.index;
                if (tab != null) {
                    $rootScope.selectedTab = tab;
                    tab.activated = true;
                    $scope.$broadcast('app.objectType.tabActivated', {tabId: tabId});
                }
                if (tab != null) {
                    activateTab(tab);
                }
            }

            function resizeAttributesView() {
                /*   var height = $('.itemtype1-view').outerHeight();
                 $('.tab-content').height(height - 50);
                 $('.tab-pane').height(height - 50);
                 var tabPane = $('.tab-pane').outerHeight();
                 var attributeButtons = $('#action-buttons').outerHeight();
                 $("#attributes-table").height(tabPane - (attributeButtons));*/

            }

            function mesObjectTypeDetailsSelected(event, args) {
                mesObjectDetailsTabActivated(vm.tabs.basic.id);
                $rootScope.selectedProductionResType = args.typeObject;
                $timeout(function () {
                    $rootScope.loadBasicInfo();
                }, 1000);
            }

            (function () {
                $scope.$on('app.mesObjectType.selected', mesObjectTypeDetailsSelected);

            })();
        }
    }
);
