define(
    [
        'app/desktop/modules/mro/mro.module',
        'app/assets/bower_components/cassini-platform/app/desktop/modules/common/comments/commentsDirective',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/commonService',
        'app/desktop/modules/mro/assets/details/tabs/basic/assetBasicInfoController',
        'app/desktop/modules/mro/assets/details/tabs/attributes/assetAttributesController',
        'app/desktop/modules/mro/assets/details/tabs/timeline/assetTimelineController',
        'app/desktop/modules/mro/assets/details/tabs/files/assetFilesController',
        'app/assets/bower_components/cassini-platform/app/desktop/directives/freetext/freeTextDirective',
        'app/shared/services/core/assetService',
        'app/desktop/modules/mro/assets/details/tabs/spareParts/assetSparePartsController',
        'app/desktop/modules/mro/assets/details/tabs/maintenance/assetMaintenancesController',
        'app/desktop/directives/plugin-directive/pluginTabsDirective'

    ],
    function (module) {
        module.controller('AssetDetailsController', AssetDetailsController);

        function AssetDetailsController($scope, $rootScope, $timeout, $state, $stateParams, $cookies, $window, $application,
                                        ECOService, $translate, AssetService, CommentsService, CommonService) {

            $rootScope.viewInfo.title = "";
            $rootScope.viewInfo.showDetails = false;
            var vm = this;
            vm.tabActivated = tabActivated;
            vm.refreshDetails = refreshDetails;
            var lastSelectedTab = null;


            vm.assetId = $stateParams.assetId;
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
                    template: 'app/desktop/modules/mro/assets/details/tabs/basic/assetBasicInfoView.jsp',
                    index: 0,
                    active: true,
                    activated: true
                }/*,
                 attributes: {
                 id: 'details.attributes',
                 heading: attributesTitle,
                 template: 'app/desktop/modules/mro/assets/details/tabs/attributes/assetAttributesView.jsp',
                 index: 1,
                 active: true,
                 activated: true
                 }*/,
                spareParts: {
                    id: 'details.spareParts',
                    heading: "Spare Parts",
                    template: 'app/desktop/modules/mro/assets/details/tabs/spareParts/assetSparePartsView.jsp',
                    index: 1,
                    active: false,
                    activated: false
                },
                maintenance: {
                    id: 'details.maintenance',
                    heading: "Maintenance",
                    template: 'app/desktop/modules/mro/assets/details/tabs/maintenance/assetMaintenancesView.jsp',
                    index: 2,
                    active: false,
                    activated: false
                },
                files: {
                    id: 'details.files',
                    heading: filesTabHeading,
                    template: 'app/desktop/modules/mro/assets/details/tabs/files/assetFilesView.jsp',
                    index: 3,
                    active: false,
                    activated: false
                },
                timelineHistory: {
                    id: 'details.timelineHistory',
                    heading: timelineHeading,
                    template: 'app/desktop/modules/mro/assets/details/tabs/timeline/assetTimelineView.jsp',
                    index: 4,
                    active: false,
                    activated: false
                }

            };


            var tabId = $stateParams.tab;
            if (tabId == null || tabId === undefined || tabId.trim() === "") {
                tabId = 'details.basic';
                $state.transitionTo('app.mro.asset.details', {
                    assetId: vm.assetId,
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
                $scope.$broadcast('app.asset.tabActivated', {tabId: $rootScope.selectedTab.id});
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
                $state.transitionTo('app.mro.asset.details', {
                    assetId: vm.assetId, tab: tabId
                }, {notify: false});
                var tab = getTabById(tabId);
                vm.active = tab.index;
                if (tab != null) {
                    $rootScope.selectedTab = tab;
                    tab.activated = true;
                    tab.active = true;
                    $scope.$broadcast('app.asset.tabActivated', {tabId: tabId});

                }
                if (tab != null) {
                    activateTab(tab);
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
                    JSON.parse($window.localStorage.getItem("lastSelectedAssetTab"));
                } catch (e) {
                    return false;
                }
                return true;
            }


            function loadAsset() {
                $rootScope.$broadcast("loadMroObjectFilesCount", {
                    objectId: vm.assetId,
                    objectType: "MROOBJECT",
                    heading: vm.tabs.files.heading
                });
            }

            function loadAssetBasicDetails() {
                vm.loading = true;
                if (vm.assetId != null && vm.assetId != undefined) {
                    AssetService.getAsset(vm.assetId).then(
                        function (data) {
                            vm.asset = data;
                            $rootScope.asset = vm.asset;
                            $scope.name = vm.asset.name;
                            CommonService.getPersonReferences([vm.asset], 'modifiedBy');
                            CommonService.getPersonReferences([vm.asset], 'createdBy');
                            vm.loading = false;
                            $rootScope.loadAssetTabCounts();
                            loadCommentsCount();
                            $scope.$evalAsync();
                        }, function (error) {
                            $rootScope.showErrorMessage(error.message);
                            $rootScope.hideBusyIndicator();
                        }
                    )
                }
            }

            $rootScope.loadAssetTabCounts = loadAssetTabCounts;
            function loadAssetTabCounts() {
                AssetService.getAssetTabCounts(vm.assetId).then(
                    function (data) {
                        vm.tabCounts = data;
                        var sparePartsTab = document.getElementById("SpareParts");
                        var maintenanceTab = document.getElementById("maintenance");
                        var tmplStr = "<span class='label label-default' style='margin-top:20px;background-color: #e4dddd;height: 20px;margin-left: 5px;'>{0}</span>";
                        if (sparePartsTab != null) {
                            sparePartsTab.lastElementChild.innerHTML = vm.tabs.spareParts.heading +
                                tmplStr.format(vm.tabCounts.spareParts);
                        }
                        if (maintenanceTab != null) {
                            maintenanceTab.lastElementChild.innerHTML = vm.tabs.maintenance.heading +
                                tmplStr.format(vm.tabCounts.workOrders);
                        }
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
                    $scope.$broadcast('app.asset.tabActivated', {tabId: 'details.files'});
                }
            }

            vm.onClear = onClear;
            function onClear() {
                $rootScope.freeTextQuerys = null;
                $scope.$broadcast('app.asset.tabActivated', {tabId: 'details.files'});
            }

            function loadCommentsCount() {
                CommentsService.getAllCommentsCount('MROASSET', vm.assetId).then(
                    function (data) {
                        $rootScope.showComments('MROASSET', vm.assetId, data);
                        $rootScope.showTags('MROASSET', vm.assetId, vm.asset.tags.length);
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                        $rootScope.hideBusyIndicator();
                    }
                )
            }

            (function () {

                if (validateJSON()) {
                    lastSelectedTab = JSON.parse($window.localStorage.getItem("lastSelectedAssetTab"));
                }

                $window.localStorage.setItem("lastSelectedAssetTab", "");

                if (lastSelectedTab != null && lastSelectedTab != undefined) {
                    tabActivated(lastSelectedTab);

                    $timeout(function () {
                        $scope.$broadcast('app.asset.tabActivated', {tabId: lastSelectedTab});
                    }, 1000)
                } else if (tabId != null && tabId != undefined) {
                    tabActivated(tabId);

                    $timeout(function () {
                        $scope.$broadcast('app.asset.tabActivated', {tabId: tabId});
                    }, 1000)
                }

                loadAsset();
                loadAssetBasicDetails();
            })();

        }
    }
);
