/**
 * Created by Hello on 10/19/2020.
 */
define(
    [
        'app/desktop/modules/mes/mes.module',
        'app/assets/bower_components/cassini-platform/app/desktop/modules/common/comments/commentsDirective',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/commonService',
        'app/desktop/modules/mes/manpower/details/tabs/basic/manpowerBasicInfoController',
        'app/desktop/modules/mes/manpower/details/tabs/attributes/manpowerAttributesController',
        'app/desktop/modules/mes/manpower/details/tabs/timeline/manpowerTimelineController',
        'app/desktop/modules/mes/manpower/details/tabs/persons/manpowerPersonsController',
        // 'app/desktop/modules/mes/manpower/details/tabs/files/manpowerFilesController',
        // 'app/desktop/modules/mes/manpower/details/tabs/relatedItem/manpowerRelatedItemController',
        // 'app/desktop/modules/mes/manpower/details/tabs/maintenance/manpowerMaintenanceController',
        'app/assets/bower_components/cassini-platform/app/desktop/directives/freetext/freeTextDirective',
        'app/shared/services/core/manpowerService',
        'app/desktop/directives/plugin-directive/pluginTabsDirective'

    ],
    function (module) {
        module.controller('ManpowerDetailsController', ManpowerDetailsController);

        function ManpowerDetailsController($scope, $rootScope, $timeout, $state, $stateParams, $cookies, $window, $application,
                                           $translate, ManpowerService, CommentsService) {
            $rootScope.viewInfo.title = "";
            $rootScope.viewInfo.showDetails = false;
            var vm = this;
            vm.tabActivated = tabActivated;
            vm.refreshDetails = refreshDetails;
            var lastSelectedTab = null;


            vm.manpowerId = $stateParams.manpowerId;
            var parsed = angular.element("<div></div>");
            var basic = parsed.html($translate.instant("DETAILS_TAB_BASIC")).html();
            var attributesTitle = parsed.html($translate.instant("DETAILS_TAB_ATTRIBUTES")).html();
            var maintenance = parsed.html($translate.instant("MAINTENANCE")).html();
            var relatedItems = parsed.html($translate.instant("RELATED_ITEMS")).html();
            var filesTabHeading = parsed.html($translate.instant("DETAILS_TAB_FILES")).html();
            var timelineHeading = parsed.html($translate.instant("TIMELINE")).html();
            var personsTitle = parsed.html($translate.instant("PERSONS")).html();
            $rootScope.clipBoardObjectFiles = [];
            $rootScope.clipBoardObjectFiles = $application.clipboard.files;
            vm.tabId = $stateParams.tab;
            vm.customTabs = [];

            vm.tabs = {
                basic: {
                    id: 'details.basic',
                    heading: basic,
                    template: 'app/desktop/modules/mes/manpower/details/tabs/basic/manpowerBasicInfoView.jsp',
                    index: 0,
                    active: true,
                    activated: true
                },
                persons: {
                    id: 'details.persons',
                    heading: personsTitle,
                    template: 'app/desktop/modules/mes/manpower/details/tabs/persons/manpowerPersonsView.jsp',
                    index: 1,
                    active: false,
                    activated: false
                    },/*,
                 attributes: {
                 id: 'details.attributes',
                 heading: attributesTitle,
                 template: 'app/desktop/modules/mes/manpower/details/tabs/attributes/manpowerAttributesView.jsp',
                 index: 1,
                 active: false,
                 activated: false
                 }*/
                // maintenance: {
                //     id: 'details.maintenance',
                //     heading: maintenance,
                //     template: 'app/desktop/modules/mes/manpower/details/tabs/maintenance/manpowerMaintenanceView.jsp',
                //     index: 1,
                //     active: true,
                //     activated: true
                // },
                // relatedItem: {
                //     id: 'details.relatedItem',
                //     heading: relatedItems,
                //     template: 'app/desktop/modules/mes/manpower/details/tabs/relatedItem/manpowerRelatedItemView.jsp',
                //     index: 2,
                //     active: true,
                //     activated: true
                // },

                // files: {
                //     id: 'details.files',
                //     heading: filesTabHeading,
                //     template: 'app/desktop/modules/mes/manpower/details/tabs/files/manpowerFilesView.jsp',
                //     index: 3,
                //     active: false,
                //     activated: false
                // },
                timelineHistory: {
                    id: 'details.timelineHistory',
                    heading: timelineHeading,
                    template: 'app/desktop/modules/mes/manpower/details/tabs/timeline/manpowerTimelineView.jsp',
                    index: 2,
                    active: false,
                    activated: false
                }

            };


            var tabId = $stateParams.tab;
            if (tabId == null || tabId === undefined || tabId.trim() === "") {
                tabId = 'details.basic';
                $state.transitionTo('app.mes.masterData.manpower.details', {
                    manpowerId: vm.manpowerId,
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

            vm.tabActivated = tabActivated;
            vm.refreshDetails = refreshDetails;

            function refreshDetails() {
                $scope.$broadcast('app.manpower.tabActivated', {tabId: $rootScope.selectedTab.id});
            }

            function activateTab(tab) {
                for (var t in vm.tabs) {
                    vm.tabs[t].active = (vm.tabs[t].id == tab.id);
                }
                for (var t in vm.customTabs) {
                    vm.customTabs[t].active = (vm.customTabs[t].id == tab.id);
                }
            }

            function validateJSON() {
                try {
                    JSON.parse($window.localStorage.getItem("lastSelectedManpowerTab"));
                } catch (e) {
                    return false;
                }
                return true;
            }

            function tabActivated(tabId) {
                $state.transitionTo('app.mes.masterData.manpower.details', {
                    manpowerId: vm.manpowerId, tab: tabId
                }, {notify: false});
                var tab = getTabById(tabId);
                vm.active = tab.index;
                if (tab != null) {
                    $rootScope.selectedTab = tab;
                    tab.activated = true;
                    tab.active = true;
                    activateTab(tab);
                    $scope.$broadcast('app.manpower.tabActivated', {tabId: tabId});

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

            function loadManpower() {
                ManpowerService.getManpower(vm.manpowerId).then(
                    function (data) {
                        vm.manpower = data;
                    /*    $rootScope.$broadcast("loadMesObjectFilesCount", {
                            objectId: vm.manpowerId,
                            objectType: "MESOBJECT",
                            heading: vm.tabs.files.heading
                        });*/
                        loadCommentsCount();
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                        $rootScope.hideBusyIndicator();
                    }
                );
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
                    $scope.$broadcast('app.manpower.tabActivated', {tabId: 'details.files'});
                }
            }

            vm.onClear = onClear;
            function onClear() {
                $rootScope.freeTextQuerys = null;
                $scope.$broadcast('app.manpower.tabActivated', {tabId: 'details.files'});
            }

            function loadCommentsCount() {
                CommentsService.getAllCommentsCount('MANPOWER', vm.manpowerId).then(
                    function (data) {
                        $rootScope.showComments('MANPOWER', vm.manpowerId, data);
                        $rootScope.showTags('MANPOWER', vm.manpowerId, vm.manpower.tags.length);
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                    }
                )
            }


            (function () {
                if (validateJSON()) {
                    lastSelectedTab = JSON.parse($window.localStorage.getItem("lastSelectedManpowerTab"));
                }

                $window.localStorage.setItem("lastSelectedManpowerTab", "");

                if (lastSelectedTab != null && lastSelectedTab != undefined) {
                    tabActivated(lastSelectedTab);

                    $timeout(function () {
                        $scope.$broadcast('app.manpower.tabActivated', {tabId: lastSelectedTab});
                    }, 1000)
                } else if (tabId != null && tabId != undefined) {
                    tabActivated(tabId);

                    $timeout(function () {
                        $scope.$broadcast('app.manpower.tabActivated', {tabId: tabId});
                    }, 1000)
                }
                loadManpower();
            })();

        }
    }
);
