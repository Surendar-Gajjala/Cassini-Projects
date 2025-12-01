define(
    [
        'app/desktop/modules/mro/mro.module',
        'app/assets/bower_components/cassini-platform/app/desktop/modules/common/comments/commentsDirective',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/commonService',
        'app/desktop/modules/mro/workRequest/details/tabs/basic/workRequestBasicInfoController',
        'app/desktop/modules/mro/workRequest/details/tabs/attributes/workRequestAttributesController',
        'app/desktop/modules/mro/workRequest/details/tabs/workflow/workRequestWorkflowController',
        'app/desktop/modules/mro/workRequest/details/tabs/timeline/workRequestTimelineController',
        'app/desktop/modules/mro/workRequest/details/tabs/files/workRequestFilesController',
        'app/assets/bower_components/cassini-platform/app/desktop/directives/freetext/freeTextDirective',
        'app/shared/services/core/workRequestService',
        'app/desktop/directives/plugin-directive/pluginTabsDirective'

    ],
    function (module) {
        module.controller('WorkRequestDetailsController', WorkRequestDetailsController);

        function WorkRequestDetailsController($scope, $rootScope, $timeout, $state, $stateParams, $cookies, $window, $application,
                                              ECOService, $translate, WorkRequestService, CommentsService, CommonService) {

            $rootScope.viewInfo.title = "";
            $rootScope.viewInfo.showDetails = false;
            var vm = this;
            vm.tabActivated = tabActivated;
            vm.refreshDetails = refreshDetails;
            var lastSelectedTab = null;


            vm.workRequestId = $stateParams.workRequestId;
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
                    template: 'app/desktop/modules/mro/workRequest/details/tabs/basic/workRequestBasicInfoView.jsp',
                    index: 0,
                    active: true,
                    activated: true
                }/*,
                 attributes: {
                 id: 'details.attributes',
                 heading: attributesTitle,
                 template: 'app/desktop/modules/mro/workRequest/details/tabs/attributes/workRequestAttributesView.jsp',
                 index: 1,
                 active: true,
                 activated: true
                 }*/,
                files: {
                    id: 'details.files',
                    heading: filesTabHeading,
                    template: 'app/desktop/modules/mro/workRequest/details/tabs/files/workRequestFilesView.jsp',
                    index: 1,
                    active: false,
                    activated: false
                },
                /*workflow: {
                 id: 'details.workflow',
                 heading: "Workflow",
                 index: 3,
                 template: 'app/desktop/modules/mro/workRequest/details/tabs/workflow/workRequestWorkflowView.jsp',
                 active: false,
                 activated: false
                 },*/
                timelineHistory: {
                    id: 'details.timelineHistory',
                    heading: timelineHeading,
                    template: 'app/desktop/modules/mro/workRequest/details/tabs/timeline/workRequestTimelineView.jsp',
                    index: 2,
                    active: false,
                    activated: false
                }

            };


            var tabId = $stateParams.tab;
            if (tabId == null || tabId === undefined || tabId.trim() === "") {
                tabId = 'details.basic';
                $state.transitionTo('app.mro.workRequest.details', {
                    workRequestId: vm.workRequestId,
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
                $scope.$broadcast('app.workRequest.tabActivated', {tabId: $rootScope.selectedTab.id});
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
                $state.transitionTo('app.mro.workRequest.details', {
                    workRequestId: vm.workRequestId, tab: tabId
                }, {notify: false});
                var tab = getTabById(tabId);
                vm.active = tab.index;
                if (tab != null) {
                    $rootScope.selectedTab = tab;
                    tab.activated = true;
                    tab.active = true;
                    activateTab(tab);
                    $scope.$broadcast('app.workRequest.tabActivated', {tabId: tabId});

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
                    JSON.parse($window.localStorage.getItem("lastSelectedSparPartTab"));
                } catch (e) {
                    return false;
                }
                return true;
            }


            function loadWorkRequest() {
                $rootScope.$broadcast("loadMroObjectFilesCount", {
                    objectId: vm.workRequestId,
                    objectType: "MROOBJECT",
                    heading: vm.tabs.files.heading
                });
            }

            function loadWorkRequestBasicDetails() {
                vm.loading = true;
                if (vm.workRequestId != null && vm.workRequestId != undefined) {
                    WorkRequestService.getWorkRequest(vm.workRequestId).then(
                        function (data) {
                            vm.workRequest = data;
                            $rootScope.workRequest = vm.workRequest;
                            $scope.name = vm.workRequest.name;
                            vm.loading = false;
                            loadCommentsCount();
                        }, function (error) {
                            $rootScope.showErrorMessage(error.message);
                            $rootScope.hideBusyIndicator();
                        }
                    )
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
                    $scope.$broadcast('app.workRequest.tabActivated', {tabId: 'details.files'});
                }
            }

            vm.onClear = onClear;
            function onClear() {
                $rootScope.freeTextQuerys = null;
                $scope.$broadcast('app.workRequest.tabActivated', {tabId: 'details.files'});
            }

            function loadCommentsCount() {
                CommentsService.getAllCommentsCount('MROWORKREQUEST', vm.workRequestId).then(
                    function (data) {
                        $rootScope.showComments('MROWORKREQUEST', vm.workRequestId, data);
                        $rootScope.showTags('MROWORKREQUEST', vm.workRequestId, vm.workRequest.tags.length);
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                        $rootScope.hideBusyIndicator();
                    }
                )
            }

            function loadWorkOrderByWorkRequest() {
                WorkRequestService.getWorkRequestWorkOrders(vm.workRequestId).then(
                    function (data) {
                        vm.workOrders = data;
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                        $rootScope.hideBusyIndicator();
                    }
                )
            }

            vm.newWorkOrder = newWorkOrder;
            var create = parsed.html($translate.instant("CREATE")).html();
            var newWorkOrderHeading = parsed.html($translate.instant("NEW_WORK_ORDER_TYPE")).html();

            vm.newWorkOrder = newWorkOrder;
            function newWorkOrder() {
                var options = {
                    title: newWorkOrderHeading,
                    template: 'app/desktop/modules/mro/workOrder/new/newWorkOrderView.jsp',
                    controller: 'NewWorkOrderController as newWorkOrderVm',
                    resolve: 'app/desktop/modules/mro/workOrder/new/newWorkOrderController',
                    width: 700,
                    showMask: true,
                    data: {
                        workOrderMode: "WORKREQUEST",
                        workRequestId: vm.workRequestId
                    },
                    buttons: [
                        {text: create, broadcast: 'app.workOrder.new'}
                    ],
                    callback: function (workOrder) {
                        $state.go('app.mro.workOrder.details', {workOrderId: workOrder.id, tab: 'details.basic'});
                    }
                };

                $rootScope.showSidePanel(options);
            }

            (function () {

                if (validateJSON()) {
                    lastSelectedTab = JSON.parse($window.localStorage.getItem("lastSelectedSparPartTab"));
                }

                $window.localStorage.setItem("lastSelectedSparPartTab", "");

                if (lastSelectedTab != null && lastSelectedTab != undefined) {
                    tabActivated(lastSelectedTab);

                    $timeout(function () {
                        $scope.$broadcast('app.workRequest.tabActivated', {tabId: lastSelectedTab});
                    }, 1000)
                } else if (tabId != null && tabId != undefined) {
                    tabActivated(tabId);

                    $timeout(function () {
                        $scope.$broadcast('app.workRequest.tabActivated', {tabId: tabId});
                    }, 1000)
                }

                loadWorkRequest();
                loadWorkRequestBasicDetails();
                loadWorkOrderByWorkRequest();
            })();

        }
    }
);
