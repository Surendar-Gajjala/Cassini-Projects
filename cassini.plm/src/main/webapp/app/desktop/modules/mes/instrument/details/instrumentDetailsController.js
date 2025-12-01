define(
    [
        'app/desktop/modules/mes/mes.module',
        'app/assets/bower_components/cassini-platform/app/desktop/modules/common/comments/commentsDirective',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/commonService',
        'app/desktop/modules/mes/instrument/details/tabs/basic/instrumentBasicInfoController',
        'app/desktop/modules/mes/instrument/details/tabs/attributes/instrumentAttributesController',
        'app/desktop/modules/mes/instrument/details/tabs/timeline/instrumentTimelineController',
        'app/desktop/modules/mes/instrument/details/tabs/files/instrumentFilesController',
        'app/desktop/modules/mes/instrument/details/tabs/relatedItem/instrumentRelatedItemController',
        'app/desktop/modules/mes/instrument/details/tabs/maintenance/instrumentMaintenanceController',
        'app/assets/bower_components/cassini-platform/app/desktop/directives/freetext/freeTextDirective',
        'app/shared/services/core/mesObjectTypeService',
        'app/shared/services/core/instrumentService',
        'app/desktop/directives/plugin-directive/pluginTabsDirective'

    ],
    function (module) {
        module.controller('InstrumentDetailsController', InstrumentDetailsController);

        function InstrumentDetailsController($scope, $rootScope, $timeout, $state, $stateParams, $cookies, $window, $application,
                                             $translate, CommentsService, InstrumentService) {
            $rootScope.viewInfo.title = "";
            $rootScope.viewInfo.showDetails = false;
            var vm = this;
            vm.tabActivated = tabActivated;
            vm.refreshDetails = refreshDetails;
            var lastSelectedTab = null;


            vm.instrumentId = $stateParams.instrumentId;
            var parsed = angular.element("<div></div>");
            var basic = parsed.html($translate.instant("DETAILS_TAB_BASIC")).html();
            var attributesTitle = parsed.html($translate.instant("DETAILS_TAB_ATTRIBUTES")).html();
            var filesTabHeading = parsed.html($translate.instant("DETAILS_TAB_FILES")).html();
            var timelineHeading = parsed.html($translate.instant("TIMELINE")).html();
            var relatedItemTitle = parsed.html($translate.instant("RELATED_ITEMS")).html();
            var maintenance = parsed.html($translate.instant("MAINTENANCE")).html();

            $rootScope.clipBoardObjectFiles = [];
            $rootScope.clipBoardObjectFiles = $application.clipboard.files;
            vm.tabId = $stateParams.tab;
            vm.customTabs = [];

            vm.tabs = {
                basic: {
                    id: 'details.basic',
                    heading: basic,
                    template: 'app/desktop/modules/mes/instrument/details/tabs/basic/instrumentBasicInfoView.jsp',
                    index: 0,
                    active: true,
                    activated: true
                }/*,
                 attributes: {
                 id: 'details.attributes',
                 heading: attributesTitle,
                 template: 'app/desktop/modules/mes/instrument/details/tabs/attributes/instrumentAttributesView.jsp',
                 index: 1,
                 active: true,
                 activated: true
                 }*/,
                maintenance: {
                    id: 'details.maintenance',
                    heading: maintenance,
                    template: 'app/desktop/modules/mes/instrument/details/tabs/maintenance/instrumentMaintenanceView.jsp',
                    index: 1,
                    active: true,
                    activated: true
                },
                relatedItem: {
                    id: 'details.relatedItem',
                    heading: relatedItemTitle,
                    template: 'app/desktop/modules/mes/instrument/details/tabs/relatedItem/instrumentRelatedItemView.jsp',
                    index: 2,
                    active: true,
                    activated: true
                },
                files: {
                    id: 'details.files',
                    heading: filesTabHeading,
                    template: 'app/desktop/modules/mes/instrument/details/tabs/files/instrumentFilesView.jsp',
                    index: 3,
                    active: false,
                    activated: false
                },
                timelineHistory: {
                    id: 'details.timelineHistory',
                    heading: timelineHeading,
                    template: 'app/desktop/modules/mes/instrument/details/tabs/timeline/instrumentTimelineView.jsp',
                    index: 4,
                    active: false,
                    activated: false
                }

            };


            var tabId = $stateParams.tab;
            if (tabId == null || tabId === undefined || tabId.trim() === "") {
                tabId = 'details.basic';
                $state.transitionTo('app.mes.masterData.instrument.details', {
                    instrumentId: vm.instrumentId,
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
                $scope.$broadcast('app.instrument.tabActivated', {tabId: $rootScope.selectedTab.id});
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
                $state.transitionTo('app.mes.masterData.instrument.details', {
                    instrumentId: vm.instrumentId, tab: tabId
                }, {notify: false});
                var tab = getTabById(tabId);
                vm.active = tab.index;
                if (tab != null) {
                    $rootScope.selectedTab = tab;
                    tab.activated = true;
                    tab.active = true;
                    activateTab(tab);
                    $scope.$broadcast('app.instrument.tabActivated', {tabId: tabId});

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
                    JSON.parse($window.localStorage.getItem("lastSelectedInstrumentTab"));
                } catch (e) {
                    return false;
                }
                return true;
            }


            function loadInstrument() {
                $rootScope.$broadcast("loadMesObjectFilesCount", {
                    objectId: vm.instrumentId,
                    objectType: "MESOBJECT",
                    heading: vm.tabs.files.heading
                });
                InstrumentService.getInstrument(vm.instrumentId).then(
                    function (data) {
                        vm.instrument = data;
                        $rootScope.instrument = data;
                        $rootScope.viewInfo.title = $translate.instant("EQUIPMENT_DETAILS");
                        $rootScope.viewInfo.description = vm.instrument.number + " , " + vm.instrument.name;
                        loadCommentsCount();
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
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
                    $scope.$broadcast('app.instrument.tabActivated', {tabId: 'details.files'});
                }
            }

            vm.onClear = onClear;
            function onClear() {
                $rootScope.freeTextQuerys = null;
                $scope.$broadcast('app.instrument.tabActivated', {tabId: 'details.files'});
            }

            function loadCommentsCount() {
                CommentsService.getAllCommentsCount('INSTRUMENT', vm.instrumentId).then(
                    function (data) {
                        $rootScope.showComments('INSTRUMENT', vm.instrumentId, data);
                        $rootScope.showTags('INSTRUMENT', vm.instrumentId, vm.instrument.tags.length);
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                    }
                )
            }

            (function () {

                if (validateJSON()) {
                    lastSelectedTab = JSON.parse($window.localStorage.getItem("lastSelectedInstrumentTab"));
                }

                $window.localStorage.setItem("lastSelectedInstrumentTab", "");

                if (lastSelectedTab != null && lastSelectedTab != undefined) {
                    tabActivated(lastSelectedTab);

                    $timeout(function () {
                        $scope.$broadcast('app.instrument.tabActivated', {tabId: lastSelectedTab});
                    }, 1000)
                } else if (tabId != null && tabId != undefined) {
                    tabActivated(tabId);

                    $timeout(function () {
                        $scope.$broadcast('app.instrument.tabActivated', {tabId: tabId});
                    }, 1000)
                }

                loadInstrument();
            })();

        }
    }
);
