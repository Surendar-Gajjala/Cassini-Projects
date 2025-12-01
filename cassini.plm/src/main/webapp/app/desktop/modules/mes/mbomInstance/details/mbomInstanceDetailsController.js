define(
    [
        'app/desktop/modules/mes/mes.module',
        'app/assets/bower_components/cassini-platform/app/desktop/modules/common/comments/commentsDirective',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/commonService',
        'app/desktop/modules/mes/mbomInstance/details/tabs/basic/mbomInstanceBasicInfoController',
        'app/desktop/modules/mes/mbomInstance/details/tabs/bom/mbomInstanceItemsController',
        'app/desktop/modules/mes/mbomInstance/details/tabs/operations/mbomInstanceOperationsController',
        'app/desktop/modules/mes/mbomInstance/details/tabs/timeline/mbomInstanceTimelineController',
        'app/desktop/modules/mes/mbomInstance/details/tabs/files/mbomInstanceFilesController',
        'app/assets/bower_components/cassini-platform/app/desktop/directives/freetext/freeTextDirective',
        'app/shared/services/core/mbomInstanceService'

    ],
    function (module) {
        module.controller('MBOMInstanceDetailsController', MBOMInstanceDetailsController);

        function MBOMInstanceDetailsController($scope, $rootScope, $timeout, $state, $stateParams, $cookies, $window, $application, MESObjectTypeService,
                                               MBOMInstanceService, $translate, CommentsService) {
            var vm = this;

            $rootScope.viewInfo.title = "";
            $rootScope.viewInfo.icon = "fa fa-sitemap";
            $rootScope.viewInfo.showDetails = true;
            vm.mbomInstanceId = $stateParams.mbomInstanceId;
            var parsed = angular.element("<div></div>");
            var basic = parsed.html($translate.instant("DETAILS_TAB_BASIC")).html();
            var bomTitle = parsed.html($translate.instant("ITEM_DETAILS_TAB_BOM")).html();
            var filesTabHeading = parsed.html($translate.instant("DETAILS_TAB_FILES")).html();
            var timelineHeading = parsed.html($translate.instant("TIMELINE")).html();
            var lastSelectedTab = null;
            vm.active = 0;
            $rootScope.clipBoardObjectFiles = [];
            $rootScope.clipBoardObjectFiles = $application.clipboard.files;
            vm.tabId = $stateParams.tab;
            vm.customTabs = [];

            vm.tabs = {
                basic: {
                    id: 'details.basic',
                    heading: basic,
                    template: 'app/desktop/modules/mes/mbomInstance/details/tabs/basic/mbomInstanceBasicInfoView.jsp',
                    index: 0,
                    active: true,
                    activated: true
                },
                bom: {
                    id: 'details.bom',
                    heading: bomTitle,
                    template: 'app/desktop/modules/mes/mbomInstance/details/tabs/bom/mbomInstanceItemsView.jsp',
                    index: 1,
                    active: false,
                    activated: false
                },
                operations: {
                    id: 'details.operations',
                    heading: "Operations",
                    template: 'app/desktop/modules/mes/mbomInstance/details/tabs/operations/mbomInstanceOperationsView.jsp',
                    index: 2,
                    active: false,
                    activated: false
                },
                files: {
                    id: 'details.files',
                    heading: filesTabHeading,
                    template: 'app/desktop/modules/mes/mbomInstance/details/tabs/files/mbomInstanceFilesView.jsp',
                    index: 3,
                    active: false,
                    activated: false
                },
                timelineHistory: {
                    id: 'details.timelineHistory',
                    heading: timelineHeading,
                    template: 'app/desktop/modules/mes/mbomInstance/details/tabs/timeline/mbomInstanceTimelineView.jsp',
                    index: 4,
                    active: false,
                    activated: false
                }

            };
            var tabId = $stateParams.tab;
            if (tabId == null || tabId === undefined || tabId.trim() === "") {
                tabId = 'details.basic';
                $state.transitionTo('app.mes.mbomInstance.details', {
                    mbomInstanceId: vm.mbomInstanceId, tab: 'details.basic'
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
                $scope.$broadcast('app.mbomInstance.tabActivated', {tabId: $rootScope.selectedTab.id});
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
                    JSON.parse($window.localStorage.getItem("lastSelectedMBOMInstanceTab"));
                } catch (e) {
                    return false;
                }
                return true;
            }

            function tabActivated(tabId) {
                $state.transitionTo('app.mes.mbomInstance.details', {
                    mbomInstanceId: vm.mbomInstanceId, tab: tabId
                }, {notify: false});
                var tab = getTabById(tabId);
                vm.active = tab.index;
                if (tab != null) {
                    $rootScope.selectedTab = tab;
                    tab.activated = true;
                    tab.active = true;
                    activateTab(tab);
                    $scope.$broadcast('app.mbomInstance.tabActivated', {tabId: tabId});

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

            vm.mbomInstanceOpenFrom = null;
            vm.loadMBOMInstanceDetails = loadMBOMInstanceDetails;
            function loadMBOMInstanceDetails() {
                MBOMInstanceService.getMBOMInstance(vm.mbomInstanceId).then(
                    function (data) {
                        vm.mbomInstance = data;
                        $rootScope.mbomInstance = data;
                        $rootScope.viewInfo.title = $translate.instant("PRODUCTION_ORDER_ITEM_DETAILS");
                        $rootScope.viewInfo.description = vm.mbomInstance.number;
                        loadMbomInstanceTabCounts();
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
                    $scope.$broadcast('app.mbomInstance.tabActivated', {tabId: 'details.files'});
                }
            }

            vm.onClear = onClear;
            function onClear() {
                $rootScope.freeTextQuerys = null;
                $scope.$broadcast('app.mbomInstance.tabActivated', {tabId: 'details.files'});
            }

            $rootScope.loadMbomInstanceTabCounts = loadMbomInstanceTabCounts;
            function loadMbomInstanceTabCounts() {
                MBOMInstanceService.getMbomInstanceTabCounts(vm.mbomInstanceId).then(
                    function (data) {
                        $rootScope.mbomInstanceCounts = data;
                        var files = document.getElementById("files");
                        if (files != null) {
                            files.lastElementChild.innerHTML = vm.tabs.files.heading +
                                "<span class='label label-default' style='margin-top:20px;background-color: #e4dddd;height: 20px;margin-left: 5px;'>{0}</span>".format($rootScope.mbomInstanceCounts.itemFiles);
                        }
                    }
                )
            }

            function loadCommentsCount() {
                CommentsService.getAllCommentsCount('MBOMINSTANCE', vm.mbomInstanceId).then(
                    function (data) {
                        $rootScope.showComments('MBOMINSTANCE', vm.mbomInstanceId, data);
                        $rootScope.showTags('MBOMINSTANCE', vm.mbomInstanceId, vm.mbomInstance.tags.length);
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                    }
                )
            }

            vm.showProductionOrderDetails = showProductionOrderDetails;
            function showProductionOrderDetails() {
                if (vm.mbomInstanceOpenFrom != null && vm.mbomInstanceOpenFrom != "null") {
                    $state.go('app.mes.productionOrder.details', {
                        productionOrderId: vm.mbomInstanceOpenFrom,
                        tab: 'details.items'
                    });
                } else {
                    $window.history.back();
                }
            }

            (function () {
                if (validateJSON()) {
                    lastSelectedTab = JSON.parse($window.localStorage.getItem("lastSelectedMBOMInstanceTab"));
                }

                $window.localStorage.setItem("lastSelectedMBOMInstanceTab", "");

                if (lastSelectedTab != null && lastSelectedTab != undefined) {
                    tabActivated(lastSelectedTab);

                    $timeout(function () {
                        $scope.$broadcast('app.mbomInstance.tabActivated', {tabId: lastSelectedTab});
                    }, 1000)
                } else if (tabId != null && tabId != undefined) {
                    tabActivated(tabId);

                    $timeout(function () {
                        $scope.$broadcast('app.mbomInstance.tabActivated', {tabId: tabId});
                    }, 1000)
                }
                if ($window.localStorage.getItem("mbomInstance_open_from") != undefined && $window.localStorage.getItem("mbomInstance_open_from") != null) {
                    vm.mbomInstanceOpenFrom = $window.localStorage.getItem("mbomInstance_open_from");
                }
                loadMBOMInstanceDetails();

            })();


        }
    }
);
