define(
    [
        'app/desktop/modules/mes/mes.module',
        'app/assets/bower_components/cassini-platform/app/desktop/modules/common/comments/commentsDirective',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/commonService',
        'app/desktop/modules/mes/assemblyLine/details/tabs/basic/assemblyLineBasicInfoController',
        'app/desktop/modules/mes/assemblyLine/details/tabs/timeline/assemblyLineTimelineController',
        'app/desktop/modules/mes/assemblyLine/details/tabs/files/assemblyLineFilesController',
        'app/desktop/modules/mes/assemblyLine/details/tabs/workCenters/assemblyLineWorkCentersController',
        'app/assets/bower_components/cassini-platform/app/desktop/directives/freetext/freeTextDirective',
        'app/shared/services/core/assemblyLineService',
        'app/desktop/directives/plugin-directive/pluginTabsDirective'

    ],
    function (module) {
        module.controller('AssemblyLineDetailsController', AssemblyLineDetailsController);

        function AssemblyLineDetailsController($scope, $rootScope, $timeout, $state, $stateParams, $cookies, $window, $application,
                                               AssemblyLineService, $translate, CommentsService) {
            $rootScope.viewInfo.title = "";
            $rootScope.viewInfo.showDetails = false;
            var vm = this;

            $rootScope.viewInfo.icon = "fa flaticon-contract11";
            $rootScope.viewInfo.title = "AssemblyLine Details";

            vm.tabActivated = tabActivated;
            vm.refreshDetails = refreshDetails;
            var lastSelectedTab = null;

            vm.assemblyLineId = $stateParams.assemblyLineId;
            var parsed = angular.element("<div></div>");
            var basic = parsed.html($translate.instant("DETAILS_TAB_BASIC")).html();
            var filesTabHeading = parsed.html($translate.instant("DETAILS_TAB_FILES")).html();
            var wcsTabHeading = parsed.html($translate.instant("WORK_CENTERS_TITLE")).html();
            var timelineHeading = parsed.html($translate.instant("TIMELINE")).html();
            $rootScope.clipBoardObjectFiles = [];
            $rootScope.clipBoardObjectFiles = $application.clipboard.files;
            vm.tabId = $stateParams.tab;
            vm.customTabs = [];


            vm.tabs = {
                basic: {
                    id: 'details.basic',
                    heading: basic,
                    template: 'app/desktop/modules/mes/assemblyLine/details/tabs/basic/assemblyLineBasicInfoView.jsp',
                    index: 0,
                    active: true,
                    activated: true
                },
                workCenters: {
                    id: 'details.workCenters',
                    heading: wcsTabHeading,
                    template: 'app/desktop/modules/mes/assemblyLine/details/tabs/workCenters/assemblyLineWorkCentersView.jsp',
                    index: 1,
                    active: false,
                    activated: false
                },
                files: {
                    id: 'details.files',
                    heading: filesTabHeading,
                    template: 'app/desktop/modules/mes/assemblyLine/details/tabs/files/assemblyLineFilesView.jsp',
                    index: 2,
                    active: false,
                    activated: false
                },
                timelineHistory: {
                    id: 'details.timelineHistory',
                    heading: timelineHeading,
                    template: 'app/desktop/modules/mes/assemblyLine/details/tabs/timeline/assemblyLineTimelineView.jsp',
                    index: 3,
                    active: false,
                    activated: false
                }

            };


            var tabId = $stateParams.tab;
            if (tabId == null || tabId === undefined || tabId.trim() === "") {
                tabId = 'details.basic';
                $state.transitionTo('app.mes.masterData.assemblyline.details', {
                    assemblyLineId: vm.assemblyLineId,
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
                $scope.$broadcast('app.assemblyLine.tabActivated', {tabId: $rootScope.selectedTab.id});
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
                $state.transitionTo('app.mes.masterData.assemblyline.details', {
                    assemblyLineId: vm.assemblyLineId, tab: tabId
                }, {notify: false});
                var tab = getTabById(tabId);
                vm.active = tab.index;
                if (tab != null) {
                    $rootScope.selectedTab = tab;
                    tab.activated = true;
                    tab.active = true;
                    activateTab(tab);
                    $scope.$broadcast('app.assemblyLine.tabActivated', {tabId: tabId});

                }
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
                    $scope.$broadcast('app.assemblyLine.tabActivated', {tabId: 'details.files'});
                }
            }

            vm.onClear = onClear;
            function onClear() {
                $rootScope.freeTextQuerys = null;
                $scope.$broadcast('app.assemblyLine.tabActivated', {tabId: 'details.files'});
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
                    JSON.parse($window.localStorage.getItem("lastSelectedAssemblyLineTab"));
                } catch (e) {
                    return false;
                }
                return true;
            }


            function loadAssemblyLine() {
                AssemblyLineService.getAssemblyLine(vm.assemblyLineId).then(
                    function (data) {
                        vm.assemblyLine = data;
                        $rootScope.assemblyLine = data;
                        $rootScope.viewInfo.title = "AssemblyLine Details";
                        $rootScope.viewInfo.description = "Number: {0}, Status: {1}".
                            format(vm.assemblyLine.number, vm.assemblyLine.name);
                        $scope.$evalAsync();
                        $rootScope.$broadcast("loadMesObjectFilesCount", {
                            objectId: vm.assemblyLineId,
                            objectType: "MESOBJECT", heading: vm.tabs.files.heading
                        });
                        loadCommentsCount();
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                        $rootScope.hideBusyIndicator();
                    }
                )
            }

            $rootScope.loadAssemblyLineTabCounts = loadAssemblyLineTabCounts;
            function loadAssemblyLineTabCounts() {
                AssemblyLineService.getAssemblyLineTabCount(vm.assemblyLineId).then(
                    function (data) {
                        vm.tabCounts = data;
                        var workCentersTab = document.getElementById("workCenters");
                        var tmplStr = "<span class='label label-default' style='margin-top:20px;background-color: #e4dddd;height: 20px;margin-left: 5px;'>{0}</span>";
                        if (workCentersTab != null) {
                            workCentersTab.lastElementChild.innerHTML = vm.tabs.workCenters.heading +
                                tmplStr.format(vm.tabCounts.workCenters);
                        }
                    }
                )
            }

            function loadCommentsCount() {
                CommentsService.getAllCommentsCount('ASSEMBLYLINE', vm.assemblyLineId).then(
                    function (data) {
                        $rootScope.showComments('ASSEMBLYLINE', vm.assemblyLineId, data);
                        $rootScope.showTags('ASSEMBLYLINE', vm.assemblyLineId, vm.assemblyLine.tags.length);
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                        $rootScope.hideBusyIndicator();
                    }
                )
            }

            (function () {

                if (validateJSON()) {
                    lastSelectedTab = JSON.parse($window.localStorage.getItem("lastSelectedAssemblyLineTab"));
                }

                $window.localStorage.setItem("lastSelectedAssemblyLineTab", "");

                if (lastSelectedTab != null && lastSelectedTab != undefined) {
                    tabActivated(lastSelectedTab);

                    $timeout(function () {
                        $scope.$broadcast('app.assemblyLine.tabActivated', {tabId: lastSelectedTab});
                    }, 1000)
                } else if (tabId != null && tabId != undefined) {
                    tabActivated(tabId);

                    $timeout(function () {
                        $scope.$broadcast('app.assemblyLine.tabActivated', {tabId: tabId});
                    }, 1000)
                }

                loadAssemblyLine();
                loadAssemblyLineTabCounts();
            })();

        }
    }
);
