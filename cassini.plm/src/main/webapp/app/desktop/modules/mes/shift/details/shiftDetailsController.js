define(
    [
        'app/desktop/modules/mes/mes.module',
        'app/assets/bower_components/cassini-platform/app/desktop/modules/common/comments/commentsDirective',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/commonService',
        'app/desktop/modules/mes/shift/details/tabs/basic/shiftBasicInfoController',
        'app/desktop/modules/mes/shift/details/tabs/attributes/shiftAttributesController',
        'app/desktop/modules/mes/shift/details/tabs/timeline/shiftTimelineController',
        'app/desktop/modules/mes/shift/details/tabs/files/shiftFilesController',
        'app/desktop/modules/mes/shift/details/tabs/persons/shiftPersonsController',
        'app/assets/bower_components/cassini-platform/app/desktop/directives/freetext/freeTextDirective',
        'app/shared/services/core/mesObjectTypeService',
        'app/shared/services/core/shiftService',
        'app/desktop/directives/plugin-directive/pluginTabsDirective'

    ],
    function (module) {
        module.controller('ShiftDetailsController', ShiftDetailsController);

        function ShiftDetailsController($scope, $rootScope, $timeout, $state, $stateParams, $cookies, $window, $application,
                                        ShiftService, $translate, MESObjectTypeService, CommentsService) {
            $rootScope.viewInfo.title = "";
            $rootScope.viewInfo.showDetails = false;

            var vm = this;
            vm.tabActivated = tabActivated;
            vm.refreshDetails = refreshDetails;
            var lastSelectedTab = null;

            vm.shiftId = $stateParams.shiftId;
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
                    template: 'app/desktop/modules/mes/shift/details/tabs/basic/shiftBasicInfoView.jsp',
                    index: 0,
                    active: true,
                    activated: true
                },
                persons: {
                    id: 'details.persons',
                    heading: 'Persons',
                    template: 'app/desktop/modules/mes/shift/details/tabs/persons/shiftPersonsView.jsp',
                    index: 1,
                    active: false,
                    activated: false
                },
                files: {
                    id: 'details.files',
                    heading: filesTabHeading,
                    template: 'app/desktop/modules/mes/shift/details/tabs/files/shiftFilesView.jsp',
                    index: 2,
                    active: false,
                    activated: false
                },
                timelineHistory: {
                    id: 'details.timelineHistory',
                    heading: timelineHeading,
                    template: 'app/desktop/modules/mes/shift/details/tabs/timeline/shiftTimelineView.jsp',
                    index: 3,
                    active: false,
                    activated: false
                }

            };


            var tabId = $stateParams.tab;
            if (tabId == null || tabId === undefined || tabId.trim() === "") {
                tabId = 'details.basic';
                $state.transitionTo('app.mes.masterData.shift.details', {
                    shiftId: vm.shiftId,
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
                $scope.$broadcast('app.shift.tabActivated', {tabId: $rootScope.selectedTab.id});
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
                $state.transitionTo('app.mes.masterData.shift.details', {
                    shiftId: vm.shiftId, tab: tabId
                }, {notify: false});
                var tab = getTabById(tabId);
                vm.active = tab.index;
                if (tab != null) {
                    $rootScope.selectedTab = tab;
                    tab.activated = true;
                    tab.active = true;
                    activateTab(tab);
                    $scope.$broadcast('app.shift.tabActivated', {tabId: tabId});

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
                    JSON.parse($window.localStorage.getItem("lastSelectedShiftTab"));
                } catch (e) {
                    return false;
                }
                return true;
            }


            function loadShift() {
                $rootScope.$broadcast("loadMesObjectFilesCount", {
                    objectId: vm.shiftId,
                    objectType: "MESOBJECT", heading: vm.tabs.files.heading
                });

                if (vm.shiftId != null && vm.shiftId != undefined) {
                    ShiftService.getShift(vm.shiftId).then(
                        function (data) {
                            vm.shift = data;
                            $rootScope.shift = vm.shift;
                            $rootScope.viewInfo.title = $translate.instant("SHIFT_DETAILS");
                            $rootScope.viewInfo.description = vm.shift.number + " , " + vm.shift.name;
                            loadCommentsCount();
                            loadShiftObjectCounts();
                        }, function (error) {
                            $rootScope.showErrorMessage(error.message);
                            $rootScope.hideBusyIndicator();
                        }
                    )
                }
            }

            $rootScope.loadShiftObjectCounts = loadShiftObjectCounts;
            function loadShiftObjectCounts(){
                ShiftService.getShiftObjectCounts(vm.shiftId).then(
                    function (data) {
                        vm.tabCounts = data;
                        var personsTab = document.getElementById("persons");
                        var tmplStr = "<span class='label label-default' style='margin-top:20px;background-color: #e4dddd;height: 20px;margin-left: 5px;'>{0}</span>";
                        if (personsTab != null) {
                            personsTab.lastElementChild.innerHTML = vm.tabs.persons.heading +
                                tmplStr.format(vm.tabCounts.persons);
                        }
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
                    $scope.$broadcast('app.shift.tabActivated', {tabId: 'details.files'});
                }
            }

            vm.onClear = onClear;
            function onClear() {
                $rootScope.freeTextQuerys = null;
                $scope.$broadcast('app.shift.tabActivated', {tabId: 'details.files'});
            }

            function loadCommentsCount() {
                CommentsService.getAllCommentsCount('SHIFT', vm.shiftId).then(
                    function (data) {
                        $rootScope.showComments('SHIFT', vm.shiftId, data);
                        $rootScope.showTags('SHIFT', vm.shiftId, vm.shift.tags.length);
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                    }
                )
            }

            (function () {
                if (validateJSON()) {
                    lastSelectedTab = JSON.parse($window.localStorage.getItem("lastSelectedShiftTab"));
                }

                $window.localStorage.setItem("lastSelectedShiftTab", "");

                if (lastSelectedTab != null && lastSelectedTab != undefined) {
                    tabActivated(lastSelectedTab);

                    $timeout(function () {
                        $scope.$broadcast('app.shift.tabActivated', {tabId: lastSelectedTab});
                    }, 1000)
                } else if (tabId != null && tabId != undefined) {
                    tabActivated(tabId);

                    $timeout(function () {
                        $scope.$broadcast('app.shift.tabActivated', {tabId: tabId});
                    }, 1000)
                }

                loadShift();
            })();

        }
    }
)
;
