define(
    [
        'app/desktop/modules/mes/mes.module',
        'app/assets/bower_components/cassini-platform/app/desktop/modules/common/comments/commentsDirective',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/commonService',
        'app/desktop/modules/mes/equipment/details/tabs/basic/equipmentBasicInfoController',
        'app/desktop/modules/mes/equipment/details/tabs/attributes/equipmentAttributesController',
        'app/desktop/modules/mes/equipment/details/tabs/timeline/equipmentTimelineController',
        'app/desktop/modules/mes/equipment/details/tabs/files/equipmentFilesController',
        'app/assets/bower_components/cassini-platform/app/desktop/directives/freetext/freeTextDirective',
        'app/shared/services/core/equipmentService',
        'app/desktop/directives/plugin-directive/pluginTabsDirective'

    ],
    function (module) {
        module.controller('EquipmentDetailsController', EquipmentDetailsController);

        function EquipmentDetailsController($scope, $rootScope, $timeout, $state, $stateParams, $cookies, $window, $application,
                                            ECOService, $translate, EquipmentService, CommentsService) {

            $rootScope.viewInfo.title = "";
            $rootScope.viewInfo.showDetails = false;
            var vm = this;
            vm.tabActivated = tabActivated;
            vm.refreshDetails = refreshDetails;
            var lastSelectedTab = null;


            vm.equipmentId = $stateParams.equipmentId;
            var parsed = angular.element("<div></div>");
            var basic = parsed.html($translate.instant("DETAILS_TAB_BASIC")).html();
            var attributesTitle = parsed.html($translate.instant("DETAILS_TAB_ATTRIBUTES")).html();
            var filesTabHeading = parsed.html($translate.instant("DETAILS_TAB_FILES")).html();
            var whereUsed = parsed.html($translate.instant("DETAILS_TAB_WHERE_USED")).html();
            var timelineHeading = parsed.html($translate.instant("TIMELINE")).html();
            $rootScope.clipBoardObjectFiles = [];
            $rootScope.clipBoardObjectFiles = $application.clipboard.files;
            vm.tabId = $stateParams.tab;
            vm.customTabs = [];

            vm.tabs = {
                basic: {
                    id: 'details.basic',
                    heading: basic,
                    template: 'app/desktop/modules/mes/equipment/details/tabs/basic/equipmentBasicInfoView.jsp',
                    index: 0,
                    active: true,
                    activated: true
                }/*,
                 attributes: {
                 id: 'details.attributes',
                 heading: attributesTitle,
                 template: 'app/desktop/modules/mes/equipment/details/tabs/attributes/equipmentAttributesView.jsp',
                 index: 1,
                 active: true,
                 activated: true
                 }*/,
                files: {
                    id: 'details.files',
                    heading: filesTabHeading,
                    template: 'app/desktop/modules/mes/equipment/details/tabs/files/equipmentFilesView.jsp',
                    index: 1,
                    active: false,
                    activated: false
                },
                timelineHistory: {
                    id: 'details.timelineHistory',
                    heading: timelineHeading,
                    template: 'app/desktop/modules/mes/equipment/details/tabs/timeline/equipmentTimelineView.jsp',
                    index: 2,
                    active: false,
                    activated: false
                }

            };


            var tabId = $stateParams.tab;
            if (tabId == null || tabId === undefined || tabId.trim() === "") {
                tabId = 'details.basic';
                $state.transitionTo('app.mes.masterData.equipment.details', {
                    equipmentId: vm.equipmentId,
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
                $scope.$broadcast('app.equipment.tabActivated', {tabId: $rootScope.selectedTab.id});
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
                $state.transitionTo('app.mes.masterData.equipment.details', {
                    equipmentId: vm.equipmentId, tab: tabId
                }, {notify: false});
                var tab = getTabById(tabId);
                vm.active = tab.index;
                if (tab != null) {
                    $rootScope.selectedTab = tab;
                    tab.activated = true;
                    tab.active = true;
                    activateTab(tab);
                    $scope.$broadcast('app.equipment.tabActivated', {tabId: tabId});

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
                    JSON.parse($window.localStorage.getItem("lastSelectedEquipmentTab"));
                } catch (e) {
                    return false;
                }
                return true;
            }


            function loadEquipment() {
                $rootScope.$broadcast("loadMesObjectFilesCount", {
                    objectId: vm.equipmentId,
                    objectType: "MESOBJECT",
                    heading: vm.tabs.files.heading
                });

                EquipmentService.getEquipment(vm.equipmentId).then(
                    function (data) {
                        vm.equipment = data;
                        $rootScope.equipment = data;
                        $rootScope.viewInfo.title = $translate.instant("EQUIPMENT_DETAILS");
                        $rootScope.viewInfo.description = vm.equipment.number + " , " + vm.equipment.name;
                        loadCommentsCount();
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                    }
                )
            }

            vm.freeTextSearch = freeTextSearch;
            $rootScope.freeTextQuerys = null;
            function freeTextSearch(freeText) {
                $rootScope.freeTextQuerys = freeText;
                if (freeText != null && freeText != undefined && freeText.trim() != "") {
                    $scope.$broadcast('app.details.files.search', {name: freeText});
                }
                else {
                    $rootScope.freeTextQuerys = null;
                    $scope.$broadcast('app.equipment.tabActivated', {tabId: 'details.files'});
                }
            }

            vm.onClear = onClear;
            function onClear() {
                $rootScope.freeTextQuerys = null;
                $scope.$broadcast('app.equipment.tabActivated', {tabId: 'details.files'});
            }

            function loadCommentsCount() {
                CommentsService.getAllCommentsCount('EQUIPMENT', vm.equipmentId).then(
                    function (data) {
                        $rootScope.showComments('EQUIPMENT', vm.equipmentId, data);
                        $rootScope.showTags('EQUIPMENT', vm.equipmentId, vm.equipment.tags.length);
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                    }
                )
            }

            (function () {

                if (validateJSON()) {
                    lastSelectedTab = JSON.parse($window.localStorage.getItem("lastSelectedEquipmentTab"));
                }

                $window.localStorage.setItem("lastSelectedEquipmentTab", "");

                if (lastSelectedTab != null && lastSelectedTab != undefined) {
                    tabActivated(lastSelectedTab);

                    $timeout(function () {
                        $scope.$broadcast('app.equipment.tabActivated', {tabId: lastSelectedTab});
                    }, 1000)
                } else if (tabId != null && tabId != undefined) {
                    tabActivated(tabId);

                    $timeout(function () {
                        $scope.$broadcast('app.equipment.tabActivated', {tabId: tabId});
                    }, 1000)
                }

                loadEquipment();
            })();

        }
    }
);
