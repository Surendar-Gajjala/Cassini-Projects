define(
    [
        'app/desktop/modules/mes/mes.module',
        'app/assets/bower_components/cassini-platform/app/desktop/modules/common/comments/commentsDirective',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/commonService',
        'app/desktop/modules/mes/jigsAndFixtures/details/tabs/basic/jigsAndFixturesBasicInfoController',
        'app/desktop/modules/mes/jigsAndFixtures/details/tabs/attributes/jigsAndFixturesAttributesController',
        'app/desktop/modules/mes/jigsAndFixtures/details/tabs/timeline/jigsAndFixturesTimelineController',
        'app/desktop/modules/mes/jigsAndFixtures/details/tabs/files/jigsAndFixturesFilesController',
        'app/assets/bower_components/cassini-platform/app/desktop/directives/freetext/freeTextDirective',
        'app/shared/services/core/mesObjectTypeService',
        'app/shared/services/core/jigsFixService',
        'app/desktop/directives/plugin-directive/pluginTabsDirective'

    ],
    function (module) {
        module.controller('JigsAndFixturesDetailsController', JigsAndFixturesDetailsController);

        function JigsAndFixturesDetailsController($scope, $rootScope, $timeout, $state, $stateParams, $cookies, $window, $application,
                                                  $translate, CommentsService, JigsFixtureService) {
            var vm = this;
            $rootScope.viewInfo.title = "";
            $rootScope.viewInfo.showDetails = false;
            vm.jigsFixId = $stateParams.jigsFixId;
            vm.tabActivated = tabActivated;
            vm.refreshDetails = refreshDetails;
            var lastSelectedTab = null;


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
                    template: 'app/desktop/modules/mes/jigsAndFixtures/details/tabs/basic/jigsAndFixturesBasicInfoView.jsp',
                    index: 0,
                    active: true,
                    activated: true
                }/*,
                 attributes: {
                 id: 'details.attributes',
                 heading: attributesTitle,
                 template: 'app/desktop/modules/mes/jigsAndFixtures/details/tabs/attributes/jigsAndFixturesAttributesView.jsp',
                 index: 1,
                 active: true,
                 activated: true
                 }*/,
                files: {
                    id: 'details.files',
                    heading: filesTabHeading,
                    template: 'app/desktop/modules/mes/jigsAndFixtures/details/tabs/files/jigsAndFixturesFilesView.jsp',
                    index: 1,
                    active: false,
                    activated: false
                },
                timelineHistory: {
                    id: 'details.timelineHistory',
                    heading: timelineHeading,
                    template: 'app/desktop/modules/mes/jigsAndFixtures/details/tabs/timeline/jigsAndFixturesTimelineView.jsp',
                    index: 2,
                    active: false,
                    activated: false
                }

            };


            var tabId = $stateParams.tab;
            if (tabId == null || tabId === undefined || tabId.trim() === "") {
                tabId = 'details.basic';
                $state.transitionTo('app.mes.masterData.jigsAndFixtures.details', {
                    jigsFixId: vm.jigsFixId,
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
                $scope.$broadcast('app.jigsAndFixtures.tabActivated', {tabId: $rootScope.selectedTab.id});
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
                $state.transitionTo('app.mes.masterData.jigsAndFixtures.details', {
                    jigsFixId: vm.jigsFixId, tab: tabId
                }, {notify: false});
                var tab = getTabById(tabId);
                vm.active = tab.index;
                if (tab != null) {
                    $rootScope.selectedTab = tab;
                    tab.activated = true;
                    tab.active = true;
                    activateTab(tab);
                    $scope.$broadcast('app.jigsAndFixtures.tabActivated', {tabId: tabId});

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
                    JSON.parse($window.localStorage.getItem("lastSelectedJigFixtureTab"));
                } catch (e) {
                    return false;
                }
                return true;
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
                    $scope.$broadcast('app.jigsAndFixtures.tabActivated', {tabId: 'details.files'});
                }
            }

            vm.onClear = onClear;
            function onClear() {
                $rootScope.freeTextQuerys = null;
                $scope.$broadcast('app.jigsAndFixtures.tabActivated', {tabId: 'details.files'});
            }

            function loadCommentsCount() {
                CommentsService.getAllCommentsCount('JIGFIXTURE', vm.jigsFixId).then(
                    function (data) {
                        $rootScope.showComments('JIGFIXTURE', vm.jigsFixId, data);
                        $rootScope.showTags('JIGFIXTURE', vm.jigsFixId, vm.jigsFixture.tags.length);
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                    }
                )
            }

            function loadJigAndFixture() {
                JigsFixtureService.getJigsFix(vm.jigsFixId).then(
                    function (data) {
                        vm.jigsFixture = data;
                        $rootScope.jigFixture = data;
                        $rootScope.viewInfo.title = $translate.instant("JIGS_FIXTURES_DETAILS");
                        $rootScope.viewInfo.description = vm.jigsFixture.number + " , " + vm.jigsFixture.name;
                        $scope.$evalAsync();
                        $rootScope.$broadcast("loadMesObjectFilesCount", {
                            objectId: vm.jigsFixId,
                            objectType: "MESOBJECT",
                            heading: vm.tabs.files.heading
                        });
                        loadCommentsCount();
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                        $rootScope.hideBusyIndicator();
                    }
                )
            }

            (function () {

                if (validateJSON()) {
                    lastSelectedTab = JSON.parse($window.localStorage.getItem("lastSelectedJigFixtureTab"));
                }

                $window.localStorage.setItem("lastSelectedJigFixtureTab", "");

                if (lastSelectedTab != null && lastSelectedTab != undefined) {
                    tabActivated(lastSelectedTab);

                    $timeout(function () {
                        $scope.$broadcast('app.jigsAndFixtures.tabActivated', {tabId: lastSelectedTab});
                    }, 1000)
                } else if (tabId != null && tabId != undefined) {
                    tabActivated(tabId);

                    $timeout(function () {
                        $scope.$broadcast('app.jigsAndFixtures.tabActivated', {tabId: tabId});
                    }, 1000)
                }

                loadJigAndFixture();
            })();

        }
    }
);
