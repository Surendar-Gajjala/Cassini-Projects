define(
    [
        'app/desktop/modules/mes/mes.module',
        'app/assets/bower_components/cassini-platform/app/desktop/modules/common/comments/commentsDirective',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/commonService',
        'app/desktop/modules/mes/bop/details/tabs/basic/bopBasicInfoController',
        'app/desktop/modules/mes/bop/details/tabs/plan/bopPlanController',
        'app/desktop/modules/mes/bop/details/tabs/timeline/bopTimelineController',
        'app/desktop/modules/mes/bop/details/tabs/files/bopFilesController',
        'app/desktop/modules/mes/bop/details/tabs/workflow/bopWorkflowController',
        'app/assets/bower_components/cassini-platform/app/desktop/directives/freetext/freeTextDirective',
        'app/shared/services/core/equipmentService',
        'app/shared/services/core/bopService',
        'app/desktop/directives/plugin-directive/pluginTabsDirective'

    ],
    function (module) {
        module.controller('BOPDetailsController', BOPDetailsController);

        function BOPDetailsController($scope, $rootScope, $timeout, $state, $stateParams, $cookies, $window, $application,
                                      ECOService, $translate, DialogService, CommentsService, BOPService) {

            $rootScope.viewInfo.title = "";
            $rootScope.viewInfo.showDetails = true;
            var vm = this;
            vm.tabActivated = tabActivated;
            vm.refreshDetails = refreshDetails;
            var lastSelectedTab = null;


            vm.bopId = $stateParams.bopId;
            var parsed = angular.element("<div></div>");
            var basic = parsed.html($translate.instant("DETAILS_TAB_BASIC")).html();
            var attributesTitle = parsed.html($translate.instant("DETAILS_TAB_ATTRIBUTES")).html();
            var filesTabHeading = parsed.html($translate.instant("DETAILS_TAB_FILES")).html();
            var whereUsed = parsed.html($translate.instant("DETAILS_TAB_WHERE_USED")).html();
            var timelineHeading = parsed.html($translate.instant("TIMELINE")).html();
            vm.reviseBopTitle = parsed.html($translate.instant("REVISE_BOP")).html();

            vm.addWorkflowTitle = parsed.html($translate.instant("ADD_WORKFLOW")).html();
            vm.changeWorkflowTitle = parsed.html($translate.instant("CHANGE_WORKFLOW")).html();
            $rootScope.clipBoardObjectFiles = [];
            $rootScope.clipBoardObjectFiles = $application.clipboard.files;
            vm.tabId = $stateParams.tab;
            vm.customTabs = [];

            vm.tabs = {
                basic: {
                    id: 'details.basic',
                    heading: basic,
                    template: 'app/desktop/modules/mes/bop/details/tabs/basic/bopBasicInfoView.jsp',
                    index: 0,
                    active: true,
                    activated: true
                },
                plan: {
                    id: 'details.plan',
                    heading: 'Route',
                    template: 'app/desktop/modules/mes/bop/details/tabs/plan/bopPlanView.jsp',
                    index: 1,
                    active: false,
                    activated: false
                },
                files: {
                    id: 'details.files',
                    heading: filesTabHeading,
                    template: 'app/desktop/modules/mes/bop/details/tabs/files/bopFilesView.jsp',
                    index: 2,
                    active: false,
                    activated: false
                },
                workflow: {
                    id: 'details.workflow',
                    heading: 'Workflow',
                    template: 'app/desktop/modules/mes/bop/details/tabs/workflow/bopWorkflowView.jsp',
                    index: 3,
                    active: false,
                    activated: false
                },
                timelineHistory: {
                    id: 'details.timelineHistory',
                    heading: timelineHeading,
                    template: 'app/desktop/modules/mes/bop/details/tabs/timeline/bopTimelineView.jsp',
                    index: 4,
                    active: false,
                    activated: false
                }

            };


            var tabId = $stateParams.tab;
            if (tabId == null || tabId === undefined || tabId.trim() === "") {
                tabId = 'details.basic';
                $state.transitionTo('app.mes.bop.details', {
                    bopId: vm.bopId,
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
                $scope.$broadcast('app.bop.tabActivated', {tabId: $rootScope.selectedTab.id});
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
                $state.transitionTo('app.mes.bop.details', {
                    bopId: vm.bopId, tab: tabId
                }, {notify: false});
                var tab = getTabById(tabId);
                vm.active = tab.index;
                if (tab != null) {
                    $rootScope.selectedTab = tab;
                    tab.activated = true;
                    tab.active = true;
                    activateTab(tab);
                    $scope.$broadcast('app.bop.tabActivated', {tabId: tabId});

                }
            }

            vm.changeWorkflow = changeWorkflow;
            function changeWorkflow() {
                $scope.$broadcast('app.change.workflow');
            }

            vm.addWorkflow = addWorkflow;
            function addWorkflow() {
                $scope.$broadcast('app.add.workflow');
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
                    JSON.parse($window.localStorage.getItem("lastSelectedBopTab"));
                } catch (e) {
                    return false;
                }
                return true;
            }

            $rootScope.loadBOP = loadBOP;
            function loadBOP() {
                BOPService.getBOPRevision(vm.bopId).then(
                    function (data) {
                        vm.bopRevision = data;
                        $rootScope.bopRevision = data;
                        BOPService.getBOP(vm.bopRevision.master).then(
                            function (data) {
                                vm.bop = data;
                                $rootScope.bop = data;
                                $rootScope.viewInfo.title = $translate.instant("BOP_DETAILS");
                                $rootScope.viewInfo.description = vm.bop.number + " , " + vm.bop.name + ", Rev :" + vm.bopRevision.revision;
                                setLifecycles();
                                loadBOPTabCounts();
                                loadCommentsCount();
                            }, function (error) {
                                $rootScope.showErrorMessage(error.message);
                            }
                        )
                    }
                )
            }

            $rootScope.loadBOPTabCounts = loadBOPTabCounts;
            function loadBOPTabCounts() {
                BOPService.getBOPCounts(vm.bopId).then(
                    function (data) {
                        $rootScope.bopCounts = data;
                        var files = document.getElementById("files");
                        var files = document.getElementById("files");

                        if (files != null) {
                            files.lastElementChild.innerHTML = vm.tabs.files.heading +
                                "<span class='label label-default' style='margin-top:20px;background-color: #e4dddd;height: 20px;margin-left: 5px;'>{0}</span>".format($rootScope.bopCounts.itemFiles);
                        }
                    }
                )
            }

            function setLifecycles() {
                var phases = [];
                var currentPhase = vm.bopRevision.lifeCyclePhase.phase;
                var currentLifeCyclePhase = vm.bopRevision.lifeCyclePhase;
                $rootScope.lifeCycleStatus = vm.bopRevision.lifeCyclePhase.phase;
                var defs = vm.bop.type.lifecycle.phases;
                defs.sort(function (a, b) {
                    return a.id - b.id;
                });
                var lastPhase = defs[defs.length - 1].phase;
                angular.forEach(defs, function (def) {
                    if (def.phaseType === 'OBSOLETE' && currentLifeCyclePhase.phaseType != 'OBSOLETE') return;
                    if (def.phase == currentPhase && lastPhase == def.phase) {
                        phases.push({
                            name: def.phase,
                            finished: true,
                            rejected: (def.phase == currentPhase && vm.bopRevision.rejected),
                            current: (def.phase == currentPhase)
                        })
                    } else {
                        phases.push({
                            name: def.phase,
                            finished: false,
                            rejected: (def.phase == currentPhase && vm.bopRevision.rejected),
                            current: (def.phase == currentPhase)
                        })
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
                    $scope.$broadcast('app.bop.tabActivated', {tabId: 'details.files'});
                }
            }

            vm.onClear = onClear;
            function onClear() {
                $rootScope.freeTextQuerys = null;
                $scope.$broadcast('app.bop.tabActivated', {tabId: 'details.files'});
            }

            function loadCommentsCount() {
                CommentsService.getAllCommentsCount('BOP', vm.bopId).then(
                    function (data) {
                        $rootScope.showComments('BOP', vm.bopId, data);
                        $rootScope.showTags('BOP', vm.bopId, vm.bop.tags.length);
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                    }
                )
            }

            vm.showBOPRevisionHistory = showMBOMRevisionHistory;
            var revisionHistoryTitle = $translate.instant("REVISION_HISTORY_TITLE");

            function showMBOMRevisionHistory() {
                var options = {
                    title: vm.bop.number + " - " + revisionHistoryTitle,
                    template: 'app/desktop/modules/item/details/tabs/basic/itemRevisionHistoryView.jsp',
                    controller: 'ItemRevisionHistoryController as revHistoryVm',
                    resolve: 'app/desktop/modules/item/details/tabs/basic/itemRevisionHistoryController',
                    data: {
                        itemId: vm.bopRevision.master,
                        revisionHistoryType: "BOP"
                    },
                    width: 700,
                    showMask: true
                };

                $rootScope.showSidePanel(options);
            }

            var confirmation = parsed.html($translate.instant("CONFIRMATION")).html();
            var doYouWantToRevise = parsed.html($translate.instant("DO_YOU_WANT_TO_REVISE_ITEM")).html();
            var yesTitle = parsed.html($translate.instant("YES")).html();
            var noTitle = parsed.html($translate.instant("NO")).html();
            vm.reviseBOP = reviseBOP;
            function reviseBOP() {
                var options = {
                    title: confirmation,
                    message: doYouWantToRevise,
                    okButtonClass: 'btn-danger',
                    okButtonText: yesTitle,
                    cancelButtonText: noTitle
                };

                DialogService.confirm(options, function (yes) {
                    if (yes == true) {
                        $rootScope.showBusyIndicator();
                        if (vm.bopId != null && vm.bopId != undefined && vm.bopId != "") {
                            BOPService.reviseBOP(vm.bopId, vm.bopRevision).then(
                                function (revisedItem) {
                                    $timeout(function () {
                                        $state.go('app.mes.bop.details', {
                                            bopId: revisedItem.id,
                                            tab: 'details.basic'
                                        });
                                        $rootScope.hideBusyIndicator();
                                    }, 500)
                                },
                                function (error) {
                                    $rootScope.showErrorMessage(error.message);
                                    $rootScope.hideBusyIndicator();
                                }
                            )
                        }
                    }
                })
            }

            (function () {

                if (validateJSON()) {
                    lastSelectedTab = JSON.parse($window.localStorage.getItem("lastSelectedBopTab"));
                }

                $window.localStorage.setItem("lastSelectedBopTab", "");

                if (lastSelectedTab != null && lastSelectedTab != undefined) {
                    tabActivated(lastSelectedTab);

                    $timeout(function () {
                        $scope.$broadcast('app.bop.tabActivated', {tabId: lastSelectedTab});
                    }, 1000)
                } else if (tabId != null && tabId != undefined) {
                    tabActivated(tabId);

                    $timeout(function () {
                        $scope.$broadcast('app.bop.tabActivated', {tabId: tabId});
                    }, 1000)
                }

                loadBOP();
            })();

        }
    }
);
