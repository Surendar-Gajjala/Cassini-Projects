define(
    [
        'app/desktop/modules/pqm/pqm.module',
        'app/desktop/modules/pqm/inspectionPlan/details/tabs/basic/planBasicInfoController',
        'app/desktop/modules/pqm/inspectionPlan/details/tabs/attributes/planAttributesController',
        'app/desktop/modules/pqm/inspectionPlan/details/tabs/checklist/planChecklistController',
        'app/desktop/modules/pqm/inspectionPlan/details/tabs/files/planFilesController',
        'app/desktop/modules/pqm/inspectionPlan/details/tabs/workflow/planWorkflowController',
        'app/desktop/modules/pqm/inspectionPlan/details/tabs/timelineHistory/planTimelineHistoryController',
        'app/shared/services/core/inspectionPlanService',
        'app/shared/services/core/objectFileService',
        'app/shared/services/core/qualityTypeService',
        'app/shared/services/core/workflowService',
        'app/assets/bower_components/cassini-platform/app/desktop/directives/freetext/freeTextDirective',
        'app/desktop/directives/plugin-directive/pluginTabsDirective'

    ],
    function (module) {
        module.controller('InspectionPlanDetailsController', InspectionPlanDetailsController);

        function InspectionPlanDetailsController($scope, $rootScope, $timeout, $interval, $window, $state, $stateParams, $cookies, $sce, DialogService,
                                                 $translate, $application, $injector, InspectionPlanService, WorkflowService, CommentsService) {

            $rootScope.viewInfo.icon = "fa fa-list-ul";
            $rootScope.viewInfo.showDetails = true;

            var vm = this;

            vm.back = back;
            vm.planDetailsTabActivated = planDetailsTabActivated;
            vm.planId = $stateParams.planId;
            var parsed = angular.element("<div></div>");
            var basic = parsed.html($translate.instant("DETAILS_TAB_BASIC")).html();
            var attributesTitle = parsed.html($translate.instant("DETAILS_TAB_ATTRIBUTES")).html();
            var filesTabHeading = parsed.html($translate.instant("DETAILS_TAB_FILES")).html();
            var checklistTabHeading = parsed.html($translate.instant("CHECKLIST")).html();
            var workflowHeading = parsed.html($translate.instant("WORKFLOW")).html();
            var timelineHeading = parsed.html($translate.instant("TIMELINE")).html();
            vm.newRevisionTitle = parsed.html($translate.instant("NEW_REVISION")).html();
            vm.changeWorkflowTitle = parsed.html($translate.instant("CHANGE_WORKFLOW")).html();
            $rootScope.clipBoardObjectFiles = [];
            $rootScope.clipBoardObjectFiles = $application.clipboard.files;
            vm.tabId = $stateParams.tab;
            vm.customTabs = [];

            vm.tabs = {
                basic: {
                    id: 'details.basic',
                    heading: basic,
                    template: 'app/desktop/modules/pqm/inspectionPlan/details/tabs/basic/planBasicInfoView.jsp',
                    index: 0,
                    active: true,
                    activated: true
                }/*,
                 attributes: {
                 id: 'details.attributes',
                 heading: attributesTitle,
                 template: 'app/desktop/modules/pqm/inspectionPlan/details/tabs/attributes/planAttributesView.jsp',
                 index: 1,
                 active: false,
                 activated: false
                 }*/,
                checklist: {
                    id: 'details.checklist',
                    heading: checklistTabHeading,
                    template: 'app/desktop/modules/pqm/inspectionPlan/details/tabs/checklist/planChecklistView.jsp',
                    index: 1,
                    active: false,
                    activated: false
                },
                workflow: {
                    id: 'details.workflow',
                    heading: workflowHeading,
                    template: 'app/desktop/modules/pqm/inspectionPlan/details/tabs/workflow/planWorkflowView.jsp',
                    index: 2,
                    active: false,
                    activated: false
                },
                files: {
                    id: 'details.files',
                    heading: filesTabHeading,
                    template: 'app/desktop/modules/pqm/inspectionPlan/details/tabs/files/planFilesView.jsp',
                    index: 3,
                    active: false,
                    activated: false
                },
                timelineHistory: {
                    id: 'details.timelineHistory',
                    heading: timelineHeading,
                    template: 'app/desktop/modules/pqm/inspectionPlan/details/tabs/timelineHistory/planTimelineHistoryView.jsp',
                    index: 4,
                    active: false,
                    activated: false
                }

            };

            vm.active = -1;

            var tabId = $stateParams.tab;
            if (tabId == null || tabId === undefined || tabId.trim() === "") {
                tabId = 'details.basic';
                $state.transitionTo('app.pqm.inspectionPlan.details', {
                    planId: vm.planId,
                    tab: 'details.basic'
                }, {notify: false});
                vm.active = 0;
            }
            else {
                var tab = getTabById(tabId);
                if (tab != null) {
                    //console.log(tab);
                    //vm.active = tab.index;
                }
            }

            function back() {
                window.history.back();
            }

            vm.changeWorkflow = changeWorkflow;
            function changeWorkflow() {
                $scope.$broadcast('app.change.workflow');
            }

            function activateTab(tab) {
                for (var t in vm.tabs) {
                    vm.tabs[t].active = (vm.tabs[t].id == tab.id);
                }
                for (var t in vm.customTabs) {
                    vm.customTabs[t].active = (vm.customTabs[t].id == tab.id);
                }
            }

            function planDetailsTabActivated(id) {
                $state.transitionTo('app.pqm.inspectionPlan.details', {planId: vm.planId, tab: id}, {notify: false});
                tabId = id;
                var tab = getTabById(tabId);
                vm.active = tab.index;
                if (tab != null) {
                    $rootScope.selectedTab = tab;
                    tab.activated = true;
                    tab.active = true;
                    activateTab(tab);
                    $scope.$broadcast('app.inspectionPlan.tabActivated', {tabId: tabId});
                }
                /*if (tab != null) {
                 activateTab(tab);
                 }*/
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

            function loadInspectionPlan() {
                vm.loading = true;
                InspectionPlanService.getInspectionPlanRevision(vm.planId).then(
                    function (data) {
                        vm.inspectionPlan = data;
                        loadCommentsCount();
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                        $rootScope.hideBusyIndicator();
                    }
                )
            }

            $rootScope.loadPlanDetails = loadPlanDetails;
            function loadPlanDetails() {
                InspectionPlanService.getPlanDetailsCount(vm.planId).then(
                    function (data) {
                        $rootScope.inspectionDetailsCount = data;
                        var checklists = document.getElementById("checklist");
                        var files = document.getElementById("files");

                        if (checklists != null) {
                            checklists.lastElementChild.innerHTML = vm.tabs.checklist.heading +
                                "<span class='label label-default' style='margin-top:20px;background-color: #e4dddd;height: 20px;margin-left: 5px;'>{0}</span>".format($rootScope.inspectionDetailsCount.checklists);
                        }

                        if (files != null) {
                            files.lastElementChild.innerHTML = vm.tabs.files.heading +
                                "<span class='label label-default' style='margin-top:20px;background-color: #e4dddd;height: 20px;margin-left: 5px;'>{0}</span>".format($rootScope.inspectionDetailsCount.itemFiles);
                        }
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                        $rootScope.hideBusyIndicator();
                    }
                )
            }

            vm.onClear = onClear;
            function onClear() {
                $rootScope.freeTextQuerys = null;
                $scope.$broadcast('app.inspectionPlan.tabActivated', {tabId: 'details.files'});
            }

            var confirmation = parsed.html($translate.instant("CONFIRMATION")).html();
            var doYouWantToRevise = parsed.html($translate.instant("DO_YOU_WANT_TO_REVISE_ITEM")).html();
            var yesTitle = parsed.html($translate.instant("YES")).html();
            var noTitle = parsed.html($translate.instant("NO")).html();
            vm.reviseInspectionPlan = reviseInspectionPlan;
            function reviseInspectionPlan() {
                var options = {
                    title: confirmation,
                    message: doYouWantToRevise,
                    okButtonClass: 'btn-danger',
                    okButtonText: yesTitle,
                    cancelButtonText: noTitle
                };

                DialogService.confirm(options, function (yes) {
                        if (yes == true) {
                            $rootScope.showBusyIndicator($('.view-container'));
                            InspectionPlanService.reviseInspectionPlan(vm.planId).then(
                                function (data) {
                                    $rootScope.showSuccessMessage("Plan revised successfully");
                                    $state.go('app.pqm.inspectionPlan.details', {planId: data.id});
                                    $rootScope.hideBusyIndicator();
                                }, function (error) {
                                    $rootScope.showErrorMessage(error.message);
                                    $rootScope.hideBusyIndicator();
                                }
                            )
                        }
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
                    $scope.$broadcast('app.inspectionPlan.tabActivated', {tabId: 'details.files'});
                }
            }

            var lastSelectedTab = null;

            function validateJSON() {
                try {
                    JSON.parse($window.localStorage.getItem("lastSelectedPlanTab"));
                } catch (e) {
                    return false;
                }
                return true;
            }

            function loadCommentsCount() {
                CommentsService.getAllCommentsCount('INSPECTIONPLANREVISION', vm.planId).then(
                    function (data) {
                        $rootScope.showComments('INSPECTIONPLANREVISION', vm.planId, data);
                        $rootScope.showTags(vm.inspectionPlan.plan.objectType, vm.inspectionPlan.plan.id, vm.inspectionPlan.plan.tags.length);
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                    }
                )
            }

            var revisionHistoryTitle = $translate.instant("REVISION_HISTORY_TITLE");
            vm.showPlanRevisionHistory = showPlanRevisionHistory;

            function showPlanRevisionHistory(item) {
                var options = {
                    title: $rootScope.inspectionPlan.number + " - " + revisionHistoryTitle,
                    template: 'app/desktop/modules/item/details/tabs/basic/itemRevisionHistoryView.jsp',
                    controller: 'ItemRevisionHistoryController as revHistoryVm',
                    resolve: 'app/desktop/modules/item/details/tabs/basic/itemRevisionHistoryController',
                    data: {
                        itemId: $rootScope.inspectionPlan.id,
                        revisionHistoryType: "INSPECTIONPLAN"
                    },
                    width: 700,
                    showMask: true,
                };

                $rootScope.showSidePanel(options);
            }


            (function () {
                //if ($application.homeLoaded == true) {
                if (validateJSON()) {
                    lastSelectedTab = JSON.parse($window.localStorage.getItem("lastSelectedPlanTab"));
                }

                $window.localStorage.setItem("lastSelectedPlanTab", "");

                if (lastSelectedTab != null && lastSelectedTab != undefined) {
                    planDetailsTabActivated(lastSelectedTab);

                    $timeout(function () {
                        $scope.$broadcast('app.inspectionPlan.tabActivated', {tabId: lastSelectedTab});
                    }, 1000)
                } else if (tabId != null && tabId != undefined) {
                    planDetailsTabActivated(tabId);

                    $timeout(function () {
                        $scope.$broadcast('app.inspectionPlan.tabActivated', {tabId: tabId});
                    }, 1000)
                }
                loadInspectionPlan();
                loadPlanDetails();
                //}
            })();
        }
    }
)
;