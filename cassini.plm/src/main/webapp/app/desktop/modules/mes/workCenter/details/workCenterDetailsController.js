define(
    [
        'app/desktop/modules/mes/mes.module',
        'app/assets/bower_components/cassini-platform/app/desktop/modules/common/comments/commentsDirective',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/commonService',
        'app/desktop/modules/mes/workCenter/details/tabs/basic/workCenterBasicInfoController',
        'app/desktop/modules/mes/workCenter/details/tabs/attributes/workCenterAttributesController',
        'app/desktop/modules/mes/workCenter/details/tabs/timeline/workCenterTimelineController',
        'app/desktop/modules/mes/workCenter/details/tabs/files/workCenterFilesController',
        'app/desktop/modules/mes/workCenter/details/tabs/resources/workCenterResourcesController',
        'app/desktop/modules/mes/workCenter/details/tabs/operations/workCenterOperationsController',
        'app/assets/bower_components/cassini-platform/app/desktop/directives/freetext/freeTextDirective',
        'app/shared/services/core/workCenterService',
        'app/desktop/directives/plugin-directive/pluginTabsDirective'

    ],
    function (module) {
        module.controller('WorkCenterDetailsController', WorkCenterDetailsController);

        function WorkCenterDetailsController($scope, $rootScope, $timeout, $state, $stateParams, $cookies, $window, $application,
                                             ECOService, $translate, WorkCenterService, CommentsService) {

            $rootScope.viewInfo.title = "";
            $rootScope.viewInfo.showDetails = false;
            var vm = this;
            vm.tabActivated = tabActivated;
            vm.refreshDetails = refreshDetails;
            var lastSelectedTab = null;

            vm.workCenterId = $stateParams.workcenterId;
            var parsed = angular.element("<div></div>");
            var basic = parsed.html($translate.instant("DETAILS_TAB_BASIC")).html();
            var attributesTitle = parsed.html($translate.instant("DETAILS_TAB_ATTRIBUTES")).html();
            var filesTabHeading = parsed.html($translate.instant("DETAILS_TAB_FILES")).html();
            var timelineHeading = parsed.html($translate.instant("TIMELINE")).html();
            var resources = parsed.html($translate.instant("RESOURCES")).html();
            var operations = parsed.html($translate.instant("OPERATIONS")).html();
            $rootScope.clipBoardObjectFiles = [];
            $rootScope.clipBoardObjectFiles = $application.clipboard.files;
            vm.tabId = $stateParams.tab;
            vm.customTabs = [];


            vm.tabs = {
                basic: {
                    id: 'details.basic',
                    heading: basic,
                    template: 'app/desktop/modules/mes/workCenter/details/tabs/basic/workCenterBasicInfoView.jsp',
                    index: 0,
                    active: true,
                    activated: true
                }/*,
                 attributes: {
                 id: 'details.attributes',
                 heading: attributesTitle,
                 template: 'app/desktop/modules/mes/workCenter/details/tabs/attributes/workCenterAttributesView.jsp',
                 index: 1,
                 active: true,
                 activated: true
                 }*/,
                resources: {
                    id: 'details.resources',
                    heading: resources,
                    template: 'app/desktop/modules/mes/workCenter/details/tabs/resources/workCenterResourcesView.jsp',
                    index: 1,
                    active: true,
                    activated: true
                },
                operations: {
                    id: 'details.operations',
                    heading: operations,
                    template: 'app/desktop/modules/mes/workCenter/details/tabs/operations/workCenterOperationsView.jsp',
                    index: 2,
                    active: true,
                    activated: true
                },
                files: {
                    id: 'details.files',
                    heading: filesTabHeading,
                    template: 'app/desktop/modules/mes/workCenter/details/tabs/files/workCenterFilesView.jsp',
                    index: 3,
                    active: false,
                    activated: false
                },
                timelineHistory: {
                    id: 'details.timelineHistory',
                    heading: timelineHeading,
                    template: 'app/desktop/modules/mes/workCenter/details/tabs/timeline/workCenterTimelineView.jsp',
                    index: 4,
                    active: false,
                    activated: false
                }

            };


            var tabId = $stateParams.tab;
            if (tabId == null || tabId === undefined || tabId.trim() === "") {
                tabId = 'details.basic';
                $state.transitionTo('app.mes.masterData.workcenter.details', {
                    workCenterId: vm.workCenterId,
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
                $scope.$broadcast('app.workcenter.tabActivated', {tabId: $rootScope.selectedTab.id});
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
                $state.transitionTo('app.mes.masterData.workcenter.details', {
                    workcenterId: vm.workCenterId, tab: tabId
                }, {notify: false});
                var tab = getTabById(tabId);
                vm.active = tab.index;
                if (tab != null) {
                    $rootScope.selectedTab = tab;
                    tab.activated = true;
                    tab.active = true;
                    activateTab(tab);
                    $scope.$broadcast('app.workcenter.tabActivated', {tabId: tabId});

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
                    JSON.parse($window.localStorage.getItem("lastSelectedWorkCenterTab"));
                } catch (e) {
                    return false;
                }
                return true;
            }


            function loadWorkCenter() {
                $rootScope.$broadcast("loadMesObjectFilesCount", {
                    objectId: vm.workCenterId,
                    objectType: "MESOBJECT",
                    heading: vm.tabs.files.heading
                });
                WorkCenterService.getWorkCenter(vm.workCenterId).then(
                    function (data) {
                        vm.workCenter = data;
                        loadCommentsCount();
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                        $rootScope.hideBusyIndicator();
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
                    $scope.$broadcast('app.workcenter.tabActivated', {tabId: 'details.files'});
                }
            }

            vm.onClear = onClear;
            function onClear() {
                $rootScope.freeTextQuerys = null;
                $scope.$broadcast('app.workcenter.tabActivated', {tabId: 'details.files'});
            }

            function loadCommentsCount() {
                CommentsService.getAllCommentsCount('WORKCENTER', vm.workCenterId).then(
                    function (data) {
                        $rootScope.showComments('WORKCENTER', vm.workCenterId, data);
                        $rootScope.showTags('WORKCENTER', vm.workCenterId, vm.workCenter.tags.length);
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                    }
                )
            }


            (function () {

                if (validateJSON()) {
                    lastSelectedTab = JSON.parse($window.localStorage.getItem("lastSelectedWorkCenterTab"));
                }

                $window.localStorage.setItem("lastSelectedWorkCenterTab", "");

                if (lastSelectedTab != null && lastSelectedTab != undefined) {
                    tabActivated(lastSelectedTab);

                    $timeout(function () {
                        $scope.$broadcast('app.workcenter.tabActivated', {tabId: lastSelectedTab});
                    }, 1000)
                } else if (tabId != null && tabId != undefined) {
                    tabActivated(tabId);

                    $timeout(function () {
                        $scope.$broadcast('app.workcenter.tabActivated', {tabId: tabId});
                    }, 1000)
                }

                loadWorkCenter();

            })();

        }
    }
);
