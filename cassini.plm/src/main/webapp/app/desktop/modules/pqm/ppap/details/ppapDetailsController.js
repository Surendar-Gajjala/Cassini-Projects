define(
    [
        'app/desktop/modules/pqm/pqm.module',
        // 'app/assets/bower_components/cassini-platform/app/desktop/modules/common/comments/commentsDirective',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/commonService',
        'app/desktop/modules/pqm/ppap/details/tabs/basic/ppapBasicInfoController',
        'app/desktop/modules/pqm/ppap/details/tabs/checklist/ppapChecklistController',
        'app/desktop/modules/pqm/ppap/details/tabs/timeline/ppapTimelineController',
        // 'app/assets/bower_components/cassini-platform/app/desktop/directives/freetext/freeTextDirective',
        'app/shared/services/core/ppapService'
    ],
    function (module) {
        module.controller('PPAPDetailsController', PPAPDetailsController);

        function PPAPDetailsController($scope, $rootScope, $timeout, $state, $stateParams, $cookies, $window, $application,
                                       $translate, PpapService, CommonService, DialogService, CommentsService) {

            $rootScope.viewInfo.icon = "fa fa-cubes";
            $rootScope.viewInfo.title = "PPAP Details";
            $rootScope.viewInfo.showDetails = true;
            var vm = this;
            vm.tabActivated = tabActivated;
            vm.refreshDetails = refreshDetails;
            var lastSelectedTab = null;


            vm.ppapId = $stateParams.ppapId;
            var parsed = angular.element("<div></div>");
            var basic = parsed.html($translate.instant("DETAILS_TAB_BASIC")).html();
            var checklistHeading = parsed.html($translate.instant("CHECKLIST")).html();
            var timelineHeading = parsed.html($translate.instant("TIMELINE")).html();
            $rootScope.clipBoardObjectFiles = [];
            $rootScope.clipBoardObjectFiles = $application.clipboard.files;
            vm.tabId = $stateParams.tab;
            vm.customTabs = [];

            vm.tabs = {
                basic: {
                    id: 'details.basic',
                    heading: basic,
                    template: 'app/desktop/modules/pqm/ppap/details/tabs/basic/ppapBasicInfoView.jsp',
                    index: 0,
                    active: true,
                    activated: true
                },
                checklist: {
                    id: 'details.checklist',
                    heading: checklistHeading,
                    template: 'app/desktop/modules/pqm/ppap/details/tabs/checklist/ppapChecklistView.jsp',
                    index: 1,
                    active: false,
                    activated: false
                },
                timelineHistory: {
                    id: 'details.timelineHistory',
                    heading: timelineHeading,
                    template: 'app/desktop/modules/pqm/ppap/details/tabs/timeline/ppapTimelineView.jsp',
                    index: 2,
                    active: false,
                    activated: false
                }

            };

            $rootScope.sharedPermission = null;

            var tabId = $stateParams.tab;
            if (tabId == null || tabId === undefined || tabId.trim() === "") {
                tabId = 'details.basic';
                $state.transitionTo('app.pqm.ppap.details', {
                    ppapId: vm.ppapId,
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
                $scope.$broadcast('app.ppap.tabActivated', {tabId: $rootScope.selectedTab.id});
            }

            vm.showExternalUserSuppliers = showExternalUserSuppliers;
            function showExternalUserSuppliers() {
                $state.go('app.home');
                $rootScope.sharedSupplier();

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
                $state.transitionTo('app.pqm.ppap.details', {
                    ppapId: vm.ppapId, tab: tabId
                }, {notify: false});
                var tab = getTabById(tabId);
                vm.active = tab.index;
                if (tab != null) {
                    $rootScope.selectedTab = tab;
                    tab.activated = true;
                    tab.active = true;
                    activateTab(tab);
                    $scope.$broadcast('app.ppap.tabActivated', {tabId: tabId});

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
                    JSON.parse($window.localStorage.getItem("lastSelectedPPAPTab"));
                } catch (e) {
                    return false;
                }
                return true;
            }

            var number = $translate.instant("NUMBER");

            $rootScope.loadPpap = loadPpap;
            function loadPpap() {
                $rootScope.showBusyIndicator();
                vm.loading = true;
                PpapService.getPpap(vm.ppapId).then(
                    function (data) {
                        loadPPAPCounts();
                        vm.ppap = data;
                        vm.loading = false;
                        $rootScope.ppap = vm.ppap;
                        $rootScope.viewInfo.description = number + " : {0}, Status: {1}".
                                format(data.number, data.status.phase);
                        setLifecycles();
                        loadCommentsCount();
                        vm.lastLifecyclePhase = vm.ppap.type.lifecycle.phases[vm.ppap.type.lifecycle.phases.length - 1];
                        vm.firstLifecyclePhase = vm.ppap.type.lifecycle.phases[0];
                        $rootScope.hideBusyIndicator();
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                        $rootScope.hideBusyIndicator();
                    }
                )
            }

            function loadCommentsCount() {
                CommentsService.getAllCommentsCount('PPAP', vm.ppapId).then(
                    function (data) {
                        $rootScope.showComments('PPAP', vm.ppapId, data);
                        $rootScope.showTags('PPAP', vm.ppapId, vm.ppap.tags.length);
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                    }
                )
            }

            function setLifecycles() {
                var phases = [];
                var currentPhase = vm.ppap.status.phase;
                var currentStatusPhase = vm.ppap.status;
                $rootScope.lifeCycleStatus = vm.ppap.status.phase;
                var defs = vm.ppap.type.lifecycle.phases;
                defs.sort(function (a, b) {
                    return a.id - b.id;
                });
                var phaseMap = new Hashtable();
                var lastPhase = defs[defs.length - 1].phase;
                angular.forEach(defs, function (def) {
                    if (def.phase == currentPhase && lastPhase == def.phase) {
                        if (phaseMap.get(def.phase) == null) {
                            phases.push({
                                name: def.phase,
                                finished: true,
                                rejected: (def.phase == currentPhase && vm.ppap.rejected),
                                current: (def.phase == currentPhase)
                            })
                            phaseMap.put(def.phase, def);
                        }
                    } else {
                        if (phaseMap.get(def.phase) == null) {
                            phases.push({
                                name: def.phase,
                                finished: false,
                                rejected: (def.phase == currentPhase && vm.ppap.rejected),
                                current: (def.phase == currentPhase)
                            })
                            phaseMap.put(def.phase, def);
                        }
                    }
                });

                var index = -1;
                for (var i = 0; i < phases.length; i++) {
                    if (phases[i].current == true) {
                        index = i;
                    }
                }

                if (index > 0) {
                    for (i = 0; i < index; i++) {
                        phases[i].finished = true;
                    }
                }

                $rootScope.setLifecyclePhases(phases);
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
                    $scope.$broadcast('app.ppap.tabActivated', {tabId: 'details.files'});
                }
            }

            vm.onClear = onClear;
            function onClear() {
                $rootScope.freeTextQuerys = null;
                $scope.$broadcast('app.ppap.tabActivated', {tabId: 'details.files'});
            }


            $rootScope.loadPPAPCounts = loadPPAPCounts;
            function loadPPAPCounts() {
                PpapService.getPPAPTabCounts(vm.ppapId).then(
                    function (data) {
                        vm.ppapCounts = data;
                        $rootScope.ppapCountNumber=data.checklistCount;
                        var checklistTab = document.getElementById("checklistTab");

                        checklistTab.lastElementChild.innerHTML = vm.tabs.checklist.heading +
                            "<span class='label label-default' style='margin-top:20px;background-color: #e4dddd;height: 20px;margin-left: 5px;'>{0}</span>".format(vm.ppapCounts.checklistCount);

                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                        $rootScope.hideBusyIndicator();
                    }
                )
            }

            (function () {

                if (validateJSON()) {
                    lastSelectedTab = JSON.parse($window.localStorage.getItem("lastSelectedPPAPTab"));
                }
                if ($window.localStorage.getItem("shared-permission") != undefined && $window.localStorage.getItem("shared-permission") != null) {
                    var sharedPermission = $window.localStorage.getItem("shared-permission");
                    if (sharedPermission != null && sharedPermission != "") {
                        $rootScope.sharedPermission = sharedPermission;
                    }
                }
                $window.localStorage.setItem("lastSelectedPPAPTab", "");

                if (lastSelectedTab != null && lastSelectedTab != undefined) {
                    tabActivated(lastSelectedTab);

                    $timeout(function () {
                        $scope.$broadcast('app.ppap.tabActivated', {tabId: lastSelectedTab});
                    }, 1000)
                } else if (tabId != null && tabId != undefined) {
                    tabActivated(tabId);

                    $timeout(function () {
                        $scope.$broadcast('app.ppap.tabActivated', {tabId: tabId});
                    }, 1000)
                }

                loadPpap();
            })();

        }
    }
);
