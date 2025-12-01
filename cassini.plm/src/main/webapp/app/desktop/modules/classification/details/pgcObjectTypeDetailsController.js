define(
    ['app/desktop/modules/classification/classification.module',
        'app/shared/services/core/classificationService',
        'app/assets/bower_components/cassini-platform/app/desktop/directives/commonDirectives',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/autonumberService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/commonService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/lovService',
        'app/desktop/modules/classification/tabs/pgcObjectTypes/basic/pgcObjectTypeBasicController',
        'app/desktop/modules/classification/tabs/pgcObjectTypes/attributes/pgcObjectTypeAttributesController',
        'app/desktop/modules/classification/tabs/pgcObjectTypes/history/pgcObjectTypeHistoryController',
        'app/shared/services/core/mesObjectTypeService',
        'app/shared/services/core/pgcObjectTypeService'
    ],
    function (module) {
        module.controller('PGCObjectTypeDetailsController', PGCObjectTypeDetailsController);

        function PGCObjectTypeDetailsController($scope, $rootScope, $timeout, $window, $state, $stateParams, $cookies, $translate, ItemTypeService, AutonumberService,
                                                CommonService, DialogService, LovService, MESObjectTypeService, PGCObjectTypeService) {
            var vm = this;


            vm.tabs = {
                basic: {
                    id: 'details.basic',
                    heading: "Basic",
                    template: 'app/desktop/modules/classification/tabs/pgcObjectTypes/basic/pgcObjectTypeBasicView.jsp',
                    index: 0,
                    active: true,
                    activated: true
                },
                attributes: {
                    id: 'details.attributes',
                    heading: 'Attributes',
                    template: 'app/desktop/modules/classification/tabs/pgcObjectTypes/attributes/pgcObjectTypeAttributesView.jsp',
                    index: 1,
                    active: false,
                    activated: false
                },
                history: {
                    id: 'details.history',
                    heading: 'History',
                    template: 'app/desktop/modules/classification/tabs/pgcObjectTypes/history/pgcObjectTypeHistoryView.jsp',
                    index: 2,
                    active: false,
                    activated: false
                }
            };

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

            vm.pgcObjectDetailsTabActivated = pgcObjectDetailsTabActivated;
            function pgcObjectDetailsTabActivated(tabId) {

                var tab = getTabById(tabId);
                vm.active = tab.index;
                if (tab != null) {
                    $rootScope.selectedTab = tab;
                    tab.activated = true;
                    $scope.$broadcast('app.selectedPgcType.tabActivated', {tabId: tabId});
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

            function pgcObjectTypeDetailsSelected(event, args) {
                $rootScope.selectedPgcObjectType = args.typeObject;
                pgcObjectDetailsTabActivated(vm.tabs.basic.id);
                $timeout(function () {
                    $rootScope.loadPgcBasicInfo();
                }, 1000);
            }

            (function () {
                $scope.$on('app.pgcObjectType.selected', pgcObjectTypeDetailsSelected);
            })();
        }
    }
);
