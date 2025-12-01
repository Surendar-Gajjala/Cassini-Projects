define(
    [
        'app/desktop/modules/compliance/compliance.module',
        'app/assets/bower_components/cassini-platform/app/desktop/modules/common/comments/commentsDirective',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/commonService',
        'app/desktop/modules/compliance/substances/details/tabs/basic/substanceBasicInfoController',
        'app/desktop/modules/compliance/substances/details/tabs/attributes/substanceAttributesController',
        'app/desktop/modules/compliance/substances/details/tabs/timeline/substanceTimelineController',
        'app/desktop/modules/compliance/substances/details/tabs/files/substanceFilesController',
        'app/assets/bower_components/cassini-platform/app/desktop/directives/freetext/freeTextDirective',
        'app/shared/services/core/substanceService',
        'app/desktop/directives/plugin-directive/pluginTabsDirective'

    ],
    function (module) {
        module.controller('SubstancesDetailsController', SubstancesDetailsController);

        function SubstancesDetailsController($scope, $rootScope, $timeout, $state, $stateParams, $cookies, $window, $application,
                                             SubstanceService, $translate, CommentsService) {
            var vm = this;
            $rootScope.viewInfo.icon = "fa fa-object-group";
            $rootScope.viewInfo.title = "";
            $rootScope.viewInfo.showDetails = true;

            vm.substanceId = $stateParams.substanceId;
            var parsed = angular.element("<div></div>");
            var basic = parsed.html($translate.instant("DETAILS_TAB_BASIC")).html();
            var attributesTitle = parsed.html($translate.instant("DETAILS_TAB_ATTRIBUTES")).html();
            var filesTabHeading = parsed.html($translate.instant("DETAILS_TAB_FILES")).html();
            var timelineHeading = parsed.html($translate.instant("TIMELINE")).html();
            var lastSelectedTab = null;
            vm.active = 0;
            vm.tabId = $stateParams.tab;
            vm.customTabs = [];
            vm.broadcast = broadcast;
            $rootScope.clipBoardObjectFiles = $application.clipboard.files;
            vm.tabs = {
                basic: {
                    id: 'details.basic',
                    heading: basic,
                    template: 'app/desktop/modules/compliance/substances/details/tabs/basic/substanceBasicInfoView.jsp',
                    index: 0,
                    active: true,
                    activated: true
                }/*,
                 attributes: {
                 id: 'details.attributes',
                 heading: attributesTitle,
                 template: 'app/desktop/modules/compliance/substances/details/tabs/attributes/substanceAttributesView.jsp',
                 index: 1,
                 active: true,
                 activated: true
                 }*/,
                files: {
                    id: 'details.files',
                    heading: filesTabHeading,
                    template: 'app/desktop/modules/compliance/substances/details/tabs/files/substanceFilesView.jsp',
                    index: 1,
                    active: false,
                    activated: false
                },
                timelineHistory: {
                    id: 'details.timelineHistory',
                    heading: timelineHeading,
                    template: 'app/desktop/modules/compliance/substances/details/tabs/timeline/substanceTimelineView.jsp',
                    index: 2,
                    active: false,
                    activated: false
                }

            };
            var tabId = $stateParams.tab;
            if (tabId == null || tabId === undefined || tabId.trim() === "") {
                tabId = 'details.basic';
                $state.transitionTo('app.compliance.substance.details', {
                    substanceId: vm.substanceId,
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

            vm.tabActivated = tabActivated;
            vm.refreshDetails = refreshDetails;

            function refreshDetails() {
                $scope.$broadcast('app.substance.tabActivated', {tabId: $rootScope.selectedTab.id});
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
                    JSON.parse($window.localStorage.getItem("lastSelectedSubstanceTab"));
                } catch (e) {
                    return false;
                }
                return true;
            }

            function tabActivated(tabId) {
                $state.transitionTo('app.compliance.substance.details', {
                    substanceId: vm.substanceId, tab: tabId
                }, {notify: false});
                var tab = getTabById(tabId);
                vm.active = tab.index;
                if (tab != null) {
                    $rootScope.selectedTab = tab;
                    tab.activated = true;
                    tab.active = true;
                    activateTab(tab);
                    $scope.$broadcast('app.substance.tabActivated', {tabId: tabId});

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
                    $scope.$broadcast('app.substance.tabActivated', {tabId: 'details.files'});
                }
            }

            vm.onClear = onClear;
            function onClear() {
                $rootScope.freeTextQuerys = null;
                $scope.$broadcast('app.substance.tabActivated', {tabId: 'details.files'});
            }

            function broadcast(event) {
                $scope.$broadcast(event);
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

            function loadSubstance() {
                $rootScope.$broadcast("loadPgcObjectFilesCount", {
                    objectId: vm.substanceId,
                    objectType: "PGCOBJECT",
                    heading: vm.tabs.files.heading
                });
                SubstanceService.getSubstance(vm.substanceId).then(
                    function (data) {
                        vm.substance = data;
                        $rootScope.substance = vm.substance;
                        //$rootScope.viewInfo.title = $translate.instant("SUBSTANCE_DETAILS");
                        // $rootScope.viewInfo.description = vm.substance.number + " , " + vm.substance.name;
                        $rootScope.viewInfo.title = "<div class='item-number'>" +
                            "{0}</div>".
                                format(vm.substance.number);
                        $rootScope.viewInfo.description = vm.substance.name;
                        loadCommentsCount();
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                    }
                )
            }

            function loadCommentsCount() {
                CommentsService.getAllCommentsCount('PGCSUBSTANCE', vm.substanceId).then(
                    function (data) {
                        $rootScope.showComments('PGCSUBSTANCE', vm.substanceId, data);
                        $rootScope.showTags('PGCSUBSTANCE', vm.substanceId, vm.substance.tags.length);
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                    }
                )
            }

            (function () {
                if (validateJSON()) {
                    lastSelectedTab = JSON.parse($window.localStorage.getItem("lastSelectedSubstanceTab"));
                }

                $window.localStorage.setItem("lastSelectedSubstanceTab", "");

                if (lastSelectedTab != null && lastSelectedTab != undefined) {
                    tabActivated(lastSelectedTab);

                    $timeout(function () {
                        $scope.$broadcast('app.substance.tabActivated', {tabId: lastSelectedTab});
                    }, 1000)
                } else if (tabId != null && tabId != undefined) {
                    tabActivated(tabId);

                    $timeout(function () {
                        $scope.$broadcast('app.substance.tabActivated', {tabId: tabId});
                    }, 1000)
                }
                loadSubstance();
            })();

        }
    }
);
