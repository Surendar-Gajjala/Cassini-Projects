define(
    [
        'app/desktop/modules/mes/mes.module',
        'app/assets/bower_components/cassini-platform/app/desktop/modules/common/comments/commentsDirective',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/commonService',
        'app/desktop/modules/mes/tool/details/tabs/basic/toolBasicInfoController',
        'app/desktop/modules/mes/tool/details/tabs/attributes/toolAttributesController',
        'app/desktop/modules/mes/tool/details/tabs/timeline/toolTimelineController',
        'app/desktop/modules/mes/tool/details/tabs/files/toolFilesController',
        'app/assets/bower_components/cassini-platform/app/desktop/directives/freetext/freeTextDirective',
        'app/shared/services/core/toolService',
        'app/desktop/directives/plugin-directive/pluginTabsDirective'

    ],
    function (module) {
        module.controller('ToolDetailsController', ToolDetailsController);

        function ToolDetailsController($scope, $rootScope, $timeout, $state, $stateParams, $cookies, $window, $application,
                                       ECOService, $translate, ToolService, CommentsService) {
            $rootScope.viewInfo.title = "";
            $rootScope.viewInfo.showDetails = false;
            var vm = this;
            vm.tabActivated = tabActivated;
            vm.refreshDetails = refreshDetails;
            var lastSelectedTab = null;


            vm.toolId = $stateParams.toolId;
            var parsed = angular.element("<div></div>");
            var basic = parsed.html($translate.instant("DETAILS_TAB_BASIC")).html();
            var attributesTitle = parsed.html($translate.instant("DETAILS_TAB_ATTRIBUTES")).html();
            var whereUsed = parsed.html($translate.instant("DETAILS_TAB_WHERE_USED")).html();
            var filesTabHeading = parsed.html($translate.instant("DETAILS_TAB_FILES")).html();
            var timelineHeading = parsed.html($translate.instant("TIMELINE")).html();

            $rootScope.clipBoardObjectFiles = [];
            $rootScope.clipBoardObjectFiles = $application.clipboard.files;
            vm.tabId = $stateParams.tab;
            vm.customTabs = [];

            vm.tabs = {
                basic: {
                    id: 'details.basic',
                    heading: basic,
                    template: 'app/desktop/modules/mes/tool/details/tabs/basic/toolBasicInfoView.jsp',
                    index: 0,
                    active: true,
                    activated: true
                }/*,
                 attributes: {
                 id: 'details.attributes',
                 heading: attributesTitle,
                 template: 'app/desktop/modules/mes/tool/details/tabs/attributes/toolAttributesView.jsp',
                 index: 1,
                 active: true,
                 activated: true
                 }*/,
                files: {
                    id: 'details.files',
                    heading: filesTabHeading,
                    template: 'app/desktop/modules/mes/tool/details/tabs/files/toolFilesView.jsp',
                    index: 1,
                    active: false,
                    activated: false
                },
                timelineHistory: {
                    id: 'details.timelineHistory',
                    heading: timelineHeading,
                    template: 'app/desktop/modules/mes/tool/details/tabs/timeline/toolTimelineView.jsp',
                    index: 2,
                    active: false,
                    activated: false
                }

            };


            var tabId = $stateParams.tab;
            if (tabId == null || tabId === undefined || tabId.trim() === "") {
                tabId = 'details.basic';
                $state.transitionTo('app.mes.masterData.tool.details', {
                    toolId: vm.toolId,
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
                $scope.$broadcast('app.tool.tabActivated', {tabId: $rootScope.selectedTab.id});
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
                $state.transitionTo('app.mes.masterData.tool.details', {
                    toolId: vm.toolId, tab: tabId
                }, {notify: false});
                var tab = getTabById(tabId);
                vm.active = tab.index;
                if (tab != null) {
                    $rootScope.selectedTab = tab;
                    tab.activated = true;
                    tab.active = true;
                    activateTab(tab);
                    $scope.$broadcast('app.tool.tabActivated', {tabId: tabId});

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
                    JSON.parse($window.localStorage.getItem("lastSelectedToolTab"));
                } catch (e) {
                    return false;
                }
                return true;
            }


            function loadTool() {
                $rootScope.$broadcast("loadMesObjectFilesCount", {
                    objectId: vm.toolId,
                    objectType: "MESOBJECT", heading: vm.tabs.files.heading
                });
                ToolService.getTool(vm.toolId).then(
                    function (data) {
                        vm.tool = data;
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
                    $scope.$broadcast('app.tool.tabActivated', {tabId: 'details.files'});
                }
            }

            vm.onClear = onClear;
            function onClear() {
                $rootScope.freeTextQuerys = null;
                $scope.$broadcast('app.tool.tabActivated', {tabId: 'details.files'});
            }

            function loadCommentsCount() {
                CommentsService.getAllCommentsCount('TOOL', vm.toolId).then(
                    function (data) {
                        $rootScope.showComments('TOOL', vm.toolId, data);
                        $rootScope.showTags('TOOL', vm.toolId, vm.tool.tags.length);
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                    }
                )
            }

            (function () {

                if (validateJSON()) {
                    lastSelectedTab = JSON.parse($window.localStorage.getItem("lastSelectedToolTab"));
                }

                $window.localStorage.setItem("lastSelectedToolTab", "");

                if (lastSelectedTab != null && lastSelectedTab != undefined) {
                    tabActivated(lastSelectedTab);

                    $timeout(function () {
                        $scope.$broadcast('app.tool.tabActivated', {tabId: lastSelectedTab});
                    }, 1000)
                } else if (tabId != null && tabId != undefined) {
                    tabActivated(tabId);

                    $timeout(function () {
                        $scope.$broadcast('app.tool.tabActivated', {tabId: tabId});
                    }, 1000)
                }

                loadTool();
            })();

        }
    }
);
