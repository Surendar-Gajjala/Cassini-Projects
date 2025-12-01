define(
    [
        'app/desktop/modules/mes/mes.module',
        'app/assets/bower_components/cassini-platform/app/desktop/modules/common/comments/commentsDirective',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/commonService',
        'app/desktop/modules/mes/material/details/tabs/basic/materialBasicInfoController',
        'app/desktop/modules/mes/material/details/tabs/attributes/materialAttributesController',
        'app/desktop/modules/mes/material/details/tabs/timeline/materialTimelineController',
        'app/desktop/modules/mes/material/details/tabs/files/materialFilesController',
        'app/assets/bower_components/cassini-platform/app/desktop/directives/freetext/freeTextDirective',
        'app/shared/services/core/mesObjectTypeService',
        'app/shared/services/core/materialService',
        'app/desktop/directives/plugin-directive/pluginTabsDirective'

    ],
    function (module) {
        module.controller('MaterialDetailsController', MaterialDetailsController);

        function MaterialDetailsController($scope, $rootScope, $timeout, $state, $stateParams, $cookies, $window, $application,
                                           MaterialService, $translate, MESObjectTypeService, CommentsService) {
            $rootScope.viewInfo.title = "";
            $rootScope.viewInfo.showDetails = false;
            var vm = this;

            $rootScope.viewInfo.icon = "fa flaticon-contract11";
            $rootScope.viewInfo.title = "Material Details";

            vm.tabActivated = tabActivated;
            vm.refreshDetails = refreshDetails;
            var lastSelectedTab = null;

            vm.materialId = $stateParams.materialId;
            var parsed = angular.element("<div></div>");
            var basic = parsed.html($translate.instant("DETAILS_TAB_BASIC")).html();
            var attributesTitle = parsed.html($translate.instant("DETAILS_TAB_ATTRIBUTES")).html();
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
                    template: 'app/desktop/modules/mes/material/details/tabs/basic/materialBasicInfoView.jsp',
                    index: 0,
                    active: true,
                    activated: true
                }/*,
                 attributes: {
                 id: 'details.attributes',
                 heading: attributesTitle,
                 template: 'app/desktop/modules/mes/material/details/tabs/attributes/materialAttributesView.jsp',
                 index: 1,
                 active: true,
                 activated: true
                 }*/,
                files: {
                    id: 'details.files',
                    heading: filesTabHeading,
                    template: 'app/desktop/modules/mes/material/details/tabs/files/materialFilesView.jsp',
                    index: 1,
                    active: false,
                    activated: false
                },
                timelineHistory: {
                    id: 'details.timelineHistory',
                    heading: timelineHeading,
                    template: 'app/desktop/modules/mes/material/details/tabs/timeline/materialTimelineView.jsp',
                    index: 2,
                    active: false,
                    activated: false
                }

            };


            var tabId = $stateParams.tab;
            if (tabId == null || tabId === undefined || tabId.trim() === "") {
                tabId = 'details.basic';
                $state.transitionTo('app.mes.masterData.material.details', {
                    materialId: vm.materialId,
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
                $scope.$broadcast('app.material.tabActivated', {tabId: $rootScope.selectedTab.id});
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
                $state.transitionTo('app.mes.masterData.material.details', {
                    materialId: vm.materialId, tab: tabId
                }, {notify: false});
                var tab = getTabById(tabId);
                vm.active = tab.index;
                if (tab != null) {
                    $rootScope.selectedTab = tab;
                    tab.activated = true;
                    tab.active = true;
                    activateTab(tab);
                    $scope.$broadcast('app.material.tabActivated', {tabId: tabId});

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
                    $scope.$broadcast('app.material.tabActivated', {tabId: 'details.files'});
                }
            }

            vm.onClear = onClear;
            function onClear() {
                $rootScope.freeTextQuerys = null;
                $scope.$broadcast('app.material.tabActivated', {tabId: 'details.files'});
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
                    JSON.parse($window.localStorage.getItem("lastSelectedMaterialTab"));
                } catch (e) {
                    return false;
                }
                return true;
            }


            function loadMaterial() {
                MaterialService.getMaterial(vm.materialId).then(
                    function (data) {
                        vm.material = data;
                        $rootScope.material = data;
                        $rootScope.viewInfo.title = "Material Details";
                        $rootScope.viewInfo.description = "Number: {0}, Status: {1}".
                            format(vm.material.number, vm.material.name);
                        $scope.$evalAsync();
                        $rootScope.$broadcast("loadMesObjectFilesCount", {
                            objectId: vm.materialId,
                            objectType: "MESOBJECT", heading: vm.tabs.files.heading
                        });
                        loadCommentsCount();
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                        $rootScope.hideBusyIndicator();
                    }
                )
            }

            function loadCommentsCount() {
                CommentsService.getAllCommentsCount('MATERIAL', vm.materialId).then(
                    function (data) {
                        $rootScope.showComments('MATERIAL', vm.materialId, data);
                        $rootScope.showTags('MATERIAL', vm.materialId, vm.material.tags.length);
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                        $rootScope.hideBusyIndicator();
                    }
                )
            }

            (function () {

                if (validateJSON()) {
                    lastSelectedTab = JSON.parse($window.localStorage.getItem("lastSelectedMaterialTab"));
                }

                $window.localStorage.setItem("lastSelectedMaterialTab", "");

                if (lastSelectedTab != null && lastSelectedTab != undefined) {
                    tabActivated(lastSelectedTab);

                    $timeout(function () {
                        $scope.$broadcast('app.material.tabActivated', {tabId: lastSelectedTab});
                    }, 1000)
                } else if (tabId != null && tabId != undefined) {
                    tabActivated(tabId);

                    $timeout(function () {
                        $scope.$broadcast('app.material.tabActivated', {tabId: tabId});
                    }, 1000)
                }

                loadMaterial();
            })();

        }
    }
);
