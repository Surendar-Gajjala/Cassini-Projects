define(
    [
        'app/desktop/modules/mro/mro.module',
        'app/assets/bower_components/cassini-platform/app/desktop/modules/common/comments/commentsDirective',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/commonService',
        'app/desktop/modules/mro/maintenancePlan/details/tabs/basic/maintenancePlanBasicInfoController',
        'app/desktop/modules/mro/maintenancePlan/details/tabs/operations/maintenancePlanOperationsController',
        'app/desktop/modules/mro/maintenancePlan/details/tabs/timeline/maintenancePlanTimelineController',
        'app/desktop/modules/mro/maintenancePlan/details/tabs/files/maintenancePlanFilesController',
        'app/assets/bower_components/cassini-platform/app/desktop/directives/freetext/freeTextDirective',
        'app/desktop/modules/mro/maintenancePlan/details/tabs/workflow/maintenancePlanWorkflowController',
        'app/shared/services/core/maintenancePlanService',
        'app/desktop/directives/plugin-directive/pluginTabsDirective'

    ],
    function (module) {
        module.controller('MaintenanceDetailsController', MaintenanceDetailsController);

        function MaintenanceDetailsController($scope, $rootScope, $timeout, $state, $stateParams, $cookies, $window, $application,
                                              ECOService, $translate, MaintenancePlanService, CommentsService) {

            $rootScope.viewInfo.title = "";
            $rootScope.viewInfo.showDetails = false;
            var vm = this;
            vm.tabActivated = tabActivated;
            vm.refreshDetails = refreshDetails;
            var lastSelectedTab = null;


            vm.maintenancePlanId = $stateParams.maintenancePlanId;
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
                    template: 'app/desktop/modules/mro/maintenancePlan/details/tabs/basic/maintenancePlanBasicInfoView.jsp',
                    index: 0,
                    active: true,
                    activated: true
                },
                operations: {
                    id: 'details.operations',
                    heading: "Operations",
                    template: 'app/desktop/modules/mro/maintenancePlan/details/tabs/operations/maintenancePlanOperationsView.jsp',
                    index: 1,
                    active: false,
                    activated: false
                },
                files: {
                    id: 'details.files',
                    heading: filesTabHeading,
                    template: 'app/desktop/modules/mro/maintenancePlan/details/tabs/files/maintenancePlanFilesView.jsp',
                    index: 2,
                    active: false,
                    activated: false
                },
                /*workflow: {
                 id: 'details.workflow',
                 heading: "Workflow",
                 index: 3,
                 template: 'app/desktop/modules/mro/maintenancePlan/details/tabs/workflow/maintenancePlanWorkflowView.jsp',
                 active: false,
                 activated: false
                 },*/
                timelineHistory: {
                    id: 'details.timelineHistory',
                    heading: timelineHeading,
                    template: 'app/desktop/modules/mro/maintenancePlan/details/tabs/timeline/maintenancePlanTimelineView.jsp',
                    index: 3,
                    active: false,
                    activated: false
                }

            };


            var tabId = $stateParams.tab;
            if (tabId == null || tabId === undefined || tabId.trim() === "") {
                tabId = 'details.basic';
                $state.transitionTo('app.mro.maintenancePlan.details', {
                    maintenancePlanId: vm.maintenancePlanId,
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
                $scope.$broadcast('app.maintenancePlan.tabActivated', {tabId: $rootScope.selectedTab.id});
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
                $state.transitionTo('app.mro.maintenancePlan.details', {
                    maintenancePlanId: vm.maintenancePlanId, tab: tabId
                }, {notify: false});
                var tab = getTabById(tabId);
                vm.active = tab.index;
                if (tab != null) {
                    $rootScope.selectedTab = tab;
                    tab.activated = true;
                    tab.active = true;
                    activateTab(tab);
                    $scope.$broadcast('app.maintenancePlan.tabActivated', {tabId: tabId});

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
                    JSON.parse($window.localStorage.getItem("lastSelectedMaintenancePlanTab"));
                } catch (e) {
                    return false;
                }
                return true;
            }


            function loadMaintenancePlanBasicDetails() {
                vm.loading = true;
                if (vm.maintenancePlanId != null && vm.maintenancePlanId != undefined) {
                    MaintenancePlanService.getMaintenancePlan(vm.maintenancePlanId).then(
                        function (data) {
                            vm.maintenancePlan = data;
                            $rootScope.maintenancePlanInfo = vm.maintenancePlan;
                            $rootScope.maintenancePlan = vm.maintenancePlan;
                            vm.loading = false;
                            $rootScope.viewInfo.title = $translate.instant("WORK_ORDER_DETAILS");
                            $rootScope.viewInfo.description = vm.maintenancePlan.number + " , " + vm.maintenancePlan.name;
                            loadPlanTabCounts();
                            loadCommentsCount();
                        }
                    )
                }
            }

            function loadMaintenancePlan() {
                $rootScope.$broadcast("loadMroObjectFilesCount", {
                    objectId: vm.maintenancePlanId,
                    objectType: "MROOBJECT",
                    heading: vm.tabs.files.heading
                });
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
                    $scope.$broadcast('app.maintenancePlan.tabActivated', {tabId: 'details.files'});
                }
            }

            vm.onClear = onClear;
            function onClear() {
                $rootScope.freeTextQuerys = null;
                $scope.$broadcast('app.maintenancePlan.tabActivated', {tabId: 'details.files'});
            }

            function loadCommentsCount() {
                CommentsService.getAllCommentsCount('MROMAINTENANCEPLAN', vm.maintenancePlanId).then(
                    function (data) {
                        $rootScope.showComments('MROMAINTENANCEPLAN', vm.maintenancePlanId, data);
                        $rootScope.showTags('MROMAINTENANCEPLAN', vm.maintenancePlanId, vm.maintenancePlan.tags.length);
                    }
                )
            }

            $rootScope.loadPlanTabCounts = loadPlanTabCounts;
            function loadPlanTabCounts() {
                MaintenancePlanService.getPlanTabCounts(vm.maintenancePlanId).then(
                    function (data) {
                        vm.tabCounts = data;
                        var operationsTab = document.getElementById("operations");
                        var tmplStr = "<span class='label label-default' style='margin-top:20px;background-color: #e4dddd;height: 20px;margin-left: 5px;'>{0}</span>";
                        if (operationsTab != null) {
                            operationsTab.lastElementChild.innerHTML = vm.tabs.operations.heading +
                                tmplStr.format(vm.tabCounts.operations);
                        }
                    }
                )
            }

            (function () {

                if (validateJSON()) {
                    lastSelectedTab = JSON.parse($window.localStorage.getItem("lastSelectedMaintenancePlanTab"));
                }

                $window.localStorage.setItem("lastSelectedMaintenancePlanTab", "");

                if (lastSelectedTab != null && lastSelectedTab != undefined) {
                    tabActivated(lastSelectedTab);

                    $timeout(function () {
                        $scope.$broadcast('app.maintenancePlan.tabActivated', {tabId: lastSelectedTab});
                    }, 1000)
                } else if (tabId != null && tabId != undefined) {
                    tabActivated(tabId);

                    $timeout(function () {
                        $scope.$broadcast('app.maintenancePlan.tabActivated', {tabId: tabId});
                    }, 1000)
                }
                loadMaintenancePlanBasicDetails();
                loadMaintenancePlan();
            })();

        }
    }
);
