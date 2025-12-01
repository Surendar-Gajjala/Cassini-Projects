define(
    [
        'app/desktop/modules/mes/mes.module',
        'app/assets/bower_components/cassini-platform/app/desktop/modules/common/comments/commentsDirective',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/commonService',
        'app/desktop/modules/mes/mbom/details/tabs/basic/mbomBasicInfoController',
        'app/desktop/modules/mes/mbom/details/tabs/bom/mbomItemsController',
        'app/desktop/modules/mes/mbom/details/tabs/changes/mbomChangesController',
        'app/desktop/modules/mes/mbom/details/tabs/whereUsed/mbomWhereUsedController',
        // 'app/desktop/modules/mes/mbom/details/tabs/workflow/mbomWorkflowController',
        'app/desktop/modules/mes/mbom/details/tabs/timeline/mbomTimelineController',
        'app/desktop/modules/mes/mbom/details/tabs/files/mbomFilesController',
        'app/shared/services/core/mesObjectTypeService',
        'app/assets/bower_components/cassini-platform/app/desktop/directives/freetext/freeTextDirective',
        'app/shared/services/core/mbomService'

    ],
    function (module) {
        module.controller('MBOMDetailsController', MBOMDetailsController);

        function MBOMDetailsController($scope, $rootScope, $timeout, $state, $stateParams, $cookies, $window, $application, MESObjectTypeService,
                                       MBOMService, $translate, CommentsService) {
            var vm = this;

            $rootScope.viewInfo.title = "";
            $rootScope.viewInfo.icon = "fa fa-sitemap";
            $rootScope.viewInfo.showDetails = true;
            vm.mbomId = $stateParams.mbomId;
            var parsed = angular.element("<div></div>");
            var basic = parsed.html($translate.instant("DETAILS_TAB_BASIC")).html();
            var bomTitle = parsed.html($translate.instant("ITEM_DETAILS_TAB_BOM")).html();
            var filesTabHeading = parsed.html($translate.instant("DETAILS_TAB_FILES")).html();
            var changesTabHeading = parsed.html($translate.instant("CHANGES_TITLE")).html();
            var timelineHeading = parsed.html($translate.instant("TIMELINE")).html();
            // var workflowHeading = parsed.html($translate.instant("WORKFLOW")).html();
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
                    template: 'app/desktop/modules/mes/mbom/details/tabs/basic/mbomBasicInfoView.jsp',
                    index: 0,
                    active: true,
                    activated: true
                },
                bom: {
                    id: 'details.bom',
                    heading: bomTitle,
                    template: 'app/desktop/modules/mes/mbom/details/tabs/bom/mbomItemsView.jsp',
                    index: 1,
                    active: false,
                    activated: false
                },
                files: {
                    id: 'details.files',
                    heading: filesTabHeading,
                    template: 'app/desktop/modules/mes/mbom/details/tabs/files/mbomFilesView.jsp',
                    index: 2,
                    active: false,
                    activated: false
                },
                changes: {
                    id: 'details.changes',
                    heading: changesTabHeading,
                    template: 'app/desktop/modules/mes/mbom/details/tabs/changes/mbomChangesView.jsp',
                    index: 3,
                    active: false,
                    activated: false
                },
                whereUsed: {
                    id: 'details.whereUsed',
                    heading: "BOPs",
                    template: 'app/desktop/modules/mes/mbom/details/tabs/whereUsed/mbomWhereUsedView.jsp',
                    index: 4,
                    active: false,
                    activated: false
                },
                // workflow: {
                //     id: 'details.workflow',
                //     heading: workflowHeading,
                //     template: 'app/desktop/modules/mes/mbom/details/tabs/workflow/mbomWorkflowView.jsp',
                //     index: 3,
                //     active: false,
                //     activated: false
                // },
                timelineHistory: {
                    id: 'details.timelineHistory',
                    heading: timelineHeading,
                    template: 'app/desktop/modules/mes/mbom/details/tabs/timeline/mbomTimelineView.jsp',
                    index: 5,
                    active: false,
                    activated: false
                }

            };
            var tabId = $stateParams.tab;
            if (tabId == null || tabId === undefined || tabId.trim() === "") {
                tabId = 'details.basic';
                $state.transitionTo('app.mes.mbom.details', {
                    mbomId: vm.mbomId, tab: 'details.basic'
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
                $scope.$broadcast('app.mbom.tabActivated', {tabId: $rootScope.selectedTab.id});
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
                    JSON.parse($window.localStorage.getItem("lastSelectedMBOMTab"));
                } catch (e) {
                    return false;
                }
                return true;
            }

            function tabActivated(tabId) {
                $state.transitionTo('app.mes.mbom.details', {
                    mbomId: vm.mbomId, tab: tabId
                }, {notify: false});
                var tab = getTabById(tabId);
                vm.active = tab.index;
                if (tab != null) {
                    $rootScope.selectedTab = tab;
                    tab.activated = true;
                    tab.active = true;
                    activateTab(tab);
                    $scope.$broadcast('app.mbom.tabActivated', {tabId: tabId});

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


            vm.loadMBOMDetails = loadMBOMDetails;
            function loadMBOMDetails() {
                $rootScope.$broadcast("loadMesObjectFilesCount", {
                    objectId: vm.mbomId,
                    objectType: "MESOBJECT",
                    heading: vm.tabs.files.heading
                });
                MBOMService.getMBOMRevision(vm.mbomId).then(
                    function (data) {
                        vm.mbomRevision = data;
                        $rootScope.mbomRevision = data;
                        MBOMService.getMBOM(vm.mbomRevision.master).then(
                            function (data) {
                                vm.mbom = data;
                                $rootScope.mbom = data;
                                $rootScope.viewInfo.title = $translate.instant("MBOM_DETAILS");
                                $rootScope.viewInfo.description = vm.mbom.number + " ," + vm.mbomRevision.revision + ", Item : " + vm.mbom.itemName + " - " + vm.mbom.revision;
                                setLifecycles();
                                loadMbomTabCounts();
                                loadCommentsCount();
                            }, function (error) {
                                $rootScope.showErrorMessage(error.message);
                                $rootScope.hideBusyIndicator();
                            }
                        )
                    }
                )
            }

            function setLifecycles() {
                var phases = [];
                var currentPhase = vm.mbomRevision.lifeCyclePhase.phase;
                var currentLifeCyclePhase = vm.mbomRevision.lifeCyclePhase;
                $rootScope.lifeCycleStatus = vm.mbomRevision.lifeCyclePhase.phase;
                var defs = vm.mbom.type.lifecycle.phases;
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
                            rejected: (def.phase == currentPhase && vm.mbomRevision.rejected),
                            current: (def.phase == currentPhase)
                        })
                    } else {
                        phases.push({
                            name: def.phase,
                            finished: false,
                            rejected: (def.phase == currentPhase && vm.mbomRevision.rejected),
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
                    $scope.$broadcast('app.mbom.tabActivated', {tabId: 'details.files'});
                }
            }

            vm.onClear = onClear;
            function onClear() {
                $rootScope.freeTextQuerys = null;
                $scope.$broadcast('app.mbom.tabActivated', {tabId: 'details.files'});
            }

            $rootScope.loadMbomTabCounts = loadMbomTabCounts;
            function loadMbomTabCounts() {
                MBOMService.getMbomTabCounts(vm.mbomId).then(
                    function (data) {
                        $rootScope.mbomCounts = data;
                        var affectedItems = document.getElementById("mbom-changes");
                        var whereUsed = document.getElementById("mbom-whereUsed");
                        var files = document.getElementById("files");

                        if (affectedItems != null) {
                            affectedItems.lastElementChild.innerHTML = vm.tabs.changes.heading +
                                "<span class='label label-default' style='margin-top:20px;background-color: #e4dddd;height: 20px;margin-left: 5px;'>{0}</span>".format($rootScope.mbomCounts.affectedItems);
                        }
                        if (whereUsed != null) {
                            whereUsed.lastElementChild.innerHTML = vm.tabs.whereUsed.heading +
                                "<span class='label label-default' style='margin-top:20px;background-color: #e4dddd;height: 20px;margin-left: 5px;'>{0}</span>".format($rootScope.mbomCounts.whereUsedItems);
                        }

                        if (files != null) {
                            files.lastElementChild.innerHTML = vm.tabs.files.heading +
                                "<span class='label label-default' style='margin-top:20px;background-color: #e4dddd;height: 20px;margin-left: 5px;'>{0}</span>".format($rootScope.mbomCounts.itemFiles);
                        }
                    }
                )
            }

            function loadCommentsCount() {
                CommentsService.getAllCommentsCount('MBOM', vm.mbomId).then(
                    function (data) {
                        $rootScope.showComments('MBOM', vm.mbomId, data);
                        $rootScope.showTags('MBOM', vm.mbomId, vm.mbom.tags.length);
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                    }
                )
            }

            vm.changeWorkflow = changeWorkflow;
            function changeWorkflow() {
                $scope.$broadcast('app.change.workflow');
            }

            vm.showMBOMRevisionHistory = showMBOMRevisionHistory;
            var revisionHistoryTitle = $translate.instant("REVISION_HISTORY_TITLE");

            function showMBOMRevisionHistory() {
                var options = {
                    title: vm.mbom.number + " - " + revisionHistoryTitle,
                    template: 'app/desktop/modules/item/details/tabs/basic/itemRevisionHistoryView.jsp',
                    controller: 'ItemRevisionHistoryController as revHistoryVm',
                    resolve: 'app/desktop/modules/item/details/tabs/basic/itemRevisionHistoryController',
                    data: {
                        itemId: vm.mbomRevision.master,
                        revisionHistoryType: "MBOM"
                    },
                    width: 700,
                    showMask: true,
                };

                $rootScope.showSidePanel(options);
            }

            (function () {
                if (validateJSON()) {
                    lastSelectedTab = JSON.parse($window.localStorage.getItem("lastSelectedMBOMTab"));
                }

                $window.localStorage.setItem("lastSelectedMBOMTab", "");

                if (lastSelectedTab != null && lastSelectedTab != undefined) {
                    tabActivated(lastSelectedTab);

                    $timeout(function () {
                        $scope.$broadcast('app.mbom.tabActivated', {tabId: lastSelectedTab});
                    }, 1000)
                } else if (tabId != null && tabId != undefined) {
                    tabActivated(tabId);

                    $timeout(function () {
                        $scope.$broadcast('app.mbom.tabActivated', {tabId: tabId});
                    }, 1000)
                }
                loadMBOMDetails();

            })();


        }
    }
);
