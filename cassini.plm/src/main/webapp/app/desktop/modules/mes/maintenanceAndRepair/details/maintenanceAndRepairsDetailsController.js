define(
    [
        'app/desktop/modules/change/change.module',
        'app/assets/bower_components/cassini-platform/app/desktop/modules/common/comments/commentsDirective',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/commonService',
        'app/desktop/modules/mes/maintenanceAndRepair/details/tabs/basic/maintenanceAndRepairsBasicInfoController',
        'app/desktop/modules/mes/maintenanceAndRepair/details/tabs/attributes/maintenanceAndRepairsAttributesController',
        'app/desktop/modules/mes/maintenanceAndRepair/details/tabs/timeline/maintenanceAndRepairsTimelineController',
        'app/desktop/modules/mes/maintenanceAndRepair/details/tabs/files/maintenanceAndRepairsFilesController',
        'app/assets/bower_components/cassini-platform/app/desktop/directives/freetext/freeTextDirective',
        'app/shared/services/core/mesObjectTypeService',
        'app/desktop/directives/plugin-directive/pluginTabsDirective'

    ],
    function (module) {
        module.controller('MaintenanceAndRepairDetailsController', MaintenanceAndRepairDetailsController);

        function MaintenanceAndRepairDetailsController($scope, $rootScope, $timeout, $state, $stateParams, $cookies, $window, $application,
                                                       $translate, MESObjectTypeService) {
            $rootScope.viewInfo.title = "";
            $rootScope.viewInfo.showDetails = true;
            var vm = this;


            vm.maintenanceAndRepairId = $stateParams.maintenanceAndRepairId;

            vm.tabActivated = tabActivated;
            vm.refreshDetails = refreshDetails;
            var lastSelectedTab = null;


            var parsed = angular.element("<div></div>");
            var basic = parsed.html($translate.instant("DETAILS_TAB_BASIC")).html();
            var attributesTitle = parsed.html($translate.instant("DETAILS_TAB_ATTRIBUTES")).html();
            var filesTabHeading = parsed.html($translate.instant("DETAILS_TAB_FILES")).html();
            var timelineHeading = parsed.html($translate.instant("TIMELINE")).html();
            vm.tabId = $stateParams.tab;
            vm.customTabs = [];


            vm.tabs = {
                basic: {
                    id: 'details.basic',
                    heading: basic,
                    template: 'app/desktop/modules/mes/maintenanceAndRepair/details/tabs/basic/maintenanceAndRepairsBasicInfoView.jsp',
                    index: 0,
                    active: true,
                    activated: true
                },
                attributes: {
                    id: 'details.attributes',
                    heading: attributesTitle,
                    template: 'app/desktop/modules/mes/maintenanceAndRepair/details/tabs/attributes/maintenanceAndRepairsAttributesView.jsp',
                    index: 1,
                    active: true,
                    activated: true
                },
                files: {
                    id: 'details.files',
                    heading: filesTabHeading,
                    template: 'app/desktop/modules/mes/maintenanceAndRepair/details/tabs/files/maintenanceAndRepairsFilesView.jsp',
                    index: 2,
                    active: false,
                    activated: false
                },
                timelineHistory: {
                    id: 'details.timelineHistory',
                    heading: timelineHeading,
                    template: 'app/desktop/modules/mes/maintenanceAndRepair/details/tabs/timeline/maintenanceAndRepairsTimelineView.jsp',
                    index: 3,
                    active: false,
                    activated: false
                }

            };


            var tabId = $stateParams.tab;
            if (tabId == null || tabId === undefined || tabId.trim() === "") {
                tabId = 'details.basic';
                $state.transitionTo('app.mes.masterData.maintenanceAndRepair.details', {
                    maintenanceAndRepairId: vm.maintenanceAndRepairId,
                    tab: 'details.basic'
                }, {notify: false});
                vm.active = 0;
            }
            else {
                var tab = getTabById(tabId);
                if (tab != null) {
                    //vm.active = tab.index;
                }
            }

            function refreshDetails() {
                $scope.$broadcast('app.maintenanceAndRepair.tabActivated', {tabId: $rootScope.selectedTab.id});
            }


            function activateTab(tab) {
                for (var t in vm.tabs) {
                    vm.tabs[t].active = (vm.tabs[t].id == tab.id);
                }
                for (var t in vm.customTabs) {
                    vm.customTabs[t].active = (vm.customTabs[t].id == tab.id);
                }

            }

            function tabActivated(tabId) {
                $state.transitionTo('app.mes.masterData.maintenanceAndRepair.details', {
                    maintenanceAndRepairId: vm.maintenanceAndRepairId, tab: tabId
                }, {notify: false});
                var tab = getTabById(tabId);
                vm.active = tab.index;
                if (tab != null) {
                    $rootScope.selectedTab = tab;
                    tab.activated = true;
                    tab.active = true;
                    activateTab(tab);
                    $scope.$broadcast('app.maintenanceAndRepair.tabActivated', {tabId: tabId});

                }
            }


            function getTabById(tabId) {
                var tab = null;
                for (var t in vm.tabs) {
                    if (vm.tabs.hasOwnProperty(t) && vm.tabs[t].id == tabId) {
                        tab = vm.tabs[t];
                    }
                }
                if (tab == null) {
                    angular.forEach(vm.customTabs, function (customTab) {
                        if (customTab.id === tabId) {
                            tab = customTab;
                        }
                    });
                }
                return tab;
            }

            vm.back = back;
            function back() {
                window.history.back();
            }


            function validateJSON() {
                try {
                    JSON.parse($window.localStorage.getItem("lastSelectedMaintenanceAndRepairTab"));
                } catch (e) {
                    return false;
                }
                return true;
            }


            function loadMaintenanceAndRepairs() {
                $rootScope.$broadcast("loadMroObjectFilesCount", {
                    objectId: vm.maintenanceAndRepairId,
                    objectType: "MROOBJECT",
                    heading: vm.tabs.files.heading
                });
            }


            (function () {

                if (validateJSON()) {
                    lastSelectedTab = JSON.parse($window.localStorage.getItem("lastSelectedMaintenanceAndRepairTab"));
                }

                $window.localStorage.setItem("lastSelectedMaintenanceAndRepairTab", "");

                if (lastSelectedTab != null && lastSelectedTab != undefined) {
                    tabActivated(lastSelectedTab);

                    $timeout(function () {
                        $scope.$broadcast('app.maintenanceAndRepair.tabActivated', {tabId: lastSelectedTab});
                    }, 1000)
                } else if (tabId != null && tabId != undefined) {
                    tabActivated(tabId);

                    $timeout(function () {
                        $scope.$broadcast('app.maintenanceAndRepair.tabActivated', {tabId: tabId});
                    }, 1000)
                }

                loadMaintenanceAndRepairs();
            })();

        }
    }
);
