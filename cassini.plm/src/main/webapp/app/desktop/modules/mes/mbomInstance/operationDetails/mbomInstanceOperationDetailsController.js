define(
    [
        'app/desktop/modules/mes/mes.module',
        'app/assets/bower_components/cassini-platform/app/desktop/modules/common/comments/commentsDirective',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/commonService',
        'app/desktop/modules/mes/mbomInstance/operationDetails/tabs/basic/mbomInstanceOperationBasicInfoController',
        'app/desktop/modules/mes/mbomInstance/operationDetails/tabs/items/mbomInstanceOperationItemsController',
        'app/desktop/modules/mes/mbomInstance/operationDetails/tabs/resources/mbomInstanceOperationResourcesController',
        'app/desktop/modules/mes/mbomInstance/operationDetails/tabs/timeline/mbomInstanceOperationTimelineController',
        'app/desktop/modules/mes/mbomInstance/operationDetails/tabs/files/mbomInstanceOperationFilesController',
        'app/desktop/modules/mes/mbomInstance/operationDetails/tabs/instructions/mbomInstanceOperationInstructionsController',
        'app/assets/bower_components/cassini-platform/app/desktop/directives/freetext/freeTextDirective',
        'app/shared/services/core/mbomInstanceService',
        'app/desktop/directives/plugin-directive/pluginTabsDirective'

    ],
    function (module) {
        module.controller('MBOMInstanceOperationDetailsController', MBOMInstanceOperationDetailsController);

        function MBOMInstanceOperationDetailsController($scope, $rootScope, $timeout, $state, $stateParams, $cookies, $window, $application,
                                          $translate, CommentsService, MBOMInstanceService) {

            $rootScope.viewInfo.title = "";
            $rootScope.viewInfo.showDetails = false;
            var vm = this;
            vm.tabActivated = tabActivated;
            vm.refreshDetails = refreshDetails;
            var lastSelectedTab = null;


            vm.mbomInstanceId = $stateParams.mbomInstanceId;
            vm.operationId = $stateParams.operationId;
            var parsed = angular.element("<div></div>");
            var basic = parsed.html($translate.instant("DETAILS_TAB_BASIC")).html();
            var filesTabHeading = parsed.html($translate.instant("DETAILS_TAB_FILES")).html();
            var timelineHeading = parsed.html($translate.instant("TIMELINE")).html();
            vm.changeWorkflowTitle = parsed.html($translate.instant("CHANGE_WORKFLOW")).html();
            $rootScope.clipBoardObjectFiles = [];
            $rootScope.clipBoardObjectFiles = $application.clipboard.files;
            vm.tabId = $stateParams.tab;
            vm.customTabs = [];

            vm.tabs = {
                basic: {
                    id: 'details.basic',
                    heading: basic,
                    template: 'app/desktop/modules/mes/mbomInstance/operationDetails/tabs/basic/mbomInstanceOperationBasicInfoView.jsp',
                    index: 0,
                    active: true,
                    activated: true
                },
                items: {
                    id: 'details.items',
                    heading: 'Parts',
                    template: 'app/desktop/modules/mes/mbomInstance/operationDetails/tabs/items/mbomInstanceOperationItemsView.jsp',
                    index: 1,
                    active: false,
                    activated: false
                },
                resources: {
                    id: 'details.resources',
                    heading: 'Resources',
                    template: 'app/desktop/modules/mes/mbomInstance/operationDetails/tabs/resources/mbomInstanceOperationResourcesView.jsp',
                    index: 2,
                    active: false,
                    activated: false
                },
                instructions: {
                    id: 'details.instructions',
                    heading: 'Digital Work Instructions',
                    template: 'app/desktop/modules/mes/mbomInstance/operationDetails/tabs/instructions/mbomInstanceOperationInstructionsView.jsp',
                    index: 3,
                    active: false,
                    activated: false
                },
                files: {
                    id: 'details.files',
                    heading: filesTabHeading,
                    template: 'app/desktop/modules/mes/mbomInstance/operationDetails/tabs/files/mbomInstanceOperationFilesView.jsp',
                    index: 4,
                    active: false,
                    activated: false
                },
                timeline: {
                    id: 'details.timeline',
                    heading: timelineHeading,
                    template: 'app/desktop/modules/mes/mbomInstance/operationDetails/tabs/timeline/mbomInstanceOperationTimelineView.jsp',
                    index: 5,
                    active: false,
                    activated: false
                }

            };


            var tabId = $stateParams.tab;
            if (tabId == null || tabId === undefined || tabId.trim() === "") {
                tabId = 'details.basic';
                $state.transitionTo('app.mes.mbomInstance.operationDetails', {
                    mbomInstanceId: vm.mbomInstanceId,
                    operationId: vm.operationId,
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
                $scope.$broadcast('app.mbomInstance.operation.tabActivated', {tabId: $rootScope.selectedTab.id});
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
                $state.transitionTo('app.mes.mbomInstance.operationDetails', {
                    mbomInstanceId: vm.mbomInstanceId, operationId: vm.operationId, tab: tabId
                }, {notify: false});
                var tab = getTabById(tabId);
                vm.active = tab.index;
                if (tab != null) {
                    $rootScope.selectedTab = tab;
                    tab.activated = true;
                    tab.active = true;
                    activateTab(tab);
                    $scope.$broadcast('app.mbomInstance.operation.tabActivated', {tabId: tabId});

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
                    JSON.parse($window.localStorage.getItem("lastSelectedmbomInstanceOperationTab"));
                } catch (e) {
                    return false;
                }
                return true;
            }


            function loadMBOMInstanceOperation() {
                
                MBOMInstanceService.getMBOMInstanceOperations(vm.mbomInstanceId, vm.operationId).then(
                    function (data) {
                        vm.mbomInstanceOperation = data[0];
                        $rootScope.mbomInstanceOperation = data;
                        $rootScope.viewInfo.title = $translate.instant("BOP_PLAN_DETAILS");
                        $rootScope.viewInfo.description = vm.mbomInstanceOperation.sequenceNumber + " , " + vm.mbomInstanceOperation.name;
                        vm.name = vm.mbomInstanceOperation.name
                        vm.sequenceNumber = vm.mbomInstanceOperation.sequenceNumber;
                        loadOperationTabCounts();
                        loadCommentsCount();
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                    }
                );
                
            }


            $rootScope.loadOperationTabCounts = loadOperationTabCounts;
            function loadOperationTabCounts() {
                MBOMInstanceService.getMbomInstanceOperationCounts(vm.operationId).then(
                    function (data) {
                        $rootScope.bopPlanCounts = data;
                        var files = document.getElementById("files");
                        var parts = document.getElementById("plan-parts");
                        var resources = document.getElementById("plan-resources");

                        if (files != null) {
                            files.lastElementChild.innerHTML = vm.tabs.files.heading +
                                "<span class='label label-default' style='margin-top:20px;background-color: #e4dddd;height: 20px;margin-left: 5px;'>{0}</span>"
                                    .format($rootScope.bopPlanCounts.itemFiles);
                        }
                        if (parts != null) {
                            parts.lastElementChild.innerHTML = vm.tabs.items.heading +
                                "<span class='label label-default' style='margin-top:20px;background-color: #e4dddd;height: 20px;margin-left: 5px;'>{0}</span>"
                                    .format($rootScope.bopPlanCounts.items);
                        }
                        if (resources != null) {
                            resources.lastElementChild.innerHTML = vm.tabs.resources.heading +
                                "<span class='label label-default' style='margin-top:20px;background-color: #e4dddd;height: 20px;margin-left: 5px;'>{0}</span>"
                                    .format($rootScope.bopPlanCounts.resourcesCount);
                        }
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
                    $scope.$broadcast('app.mbomInstance.operation.tabActivated', {tabId: 'details.files'});
                }
            }

            vm.onClear = onClear;
            function onClear() {
                $rootScope.freeTextQuerys = null;
                $scope.$broadcast('app.mbomInstance.operation.tabActivated', {tabId: 'details.files'});
            }

            function loadCommentsCount() {
                CommentsService.getAllCommentsCount('BOPROUTEOPERATION', vm.operationId).then(
                    function (data) {
                        $rootScope.showComments('BOPROUTEOPERATION', vm.operationId, data);
                        $rootScope.showTags('BOPROUTEOPERATION', vm.operationId, 0);
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                    }
                )
            }

            vm.showBopDetails = showBopDetails;
            function showBopDetails() {
                $state.go('app.mes.mbomInstance.details', {mbomInstanceId: vm.mbomInstanceId, tab: 'details.operations'})
            }

            (function () {

                if (validateJSON()) {
                    lastSelectedTab = JSON.parse($window.localStorage.getItem("lastSelectedmbomInstanceOperationTab"));
                }

                $window.localStorage.setItem("lastSelectedmbomInstanceOperationTab", "");

                if (lastSelectedTab != null && lastSelectedTab != undefined) {
                    tabActivated(lastSelectedTab);

                    $timeout(function () {
                        $scope.$broadcast('app.mbomInstance.operation.tabActivated', {tabId: lastSelectedTab});
                    }, 1000)
                } else if (tabId != null && tabId != undefined) {
                    tabActivated(tabId);

                    $timeout(function () {
                        $scope.$broadcast('app.mbomInstance.operation.tabActivated', {tabId: tabId});
                    }, 1000)
                }

                loadMBOMInstanceOperation();
            })();

        }
    }
);
