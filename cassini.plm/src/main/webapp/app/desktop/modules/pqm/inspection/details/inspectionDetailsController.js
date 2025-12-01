define(
    [
        'app/desktop/modules/pqm/pqm.module',
        'app/desktop/modules/pqm/inspection/details/tabs/basic/inspectionBasicInfoController',
        'app/desktop/modules/pqm/inspection/details/tabs/checklist/inspectionChecklistController',
        'app/desktop/modules/pqm/inspection/details/tabs/files/inspectionFilesController',
        'app/desktop/modules/pqm/inspection/details/tabs/workflow/inspectionWorkflowController',
        'app/desktop/modules/pqm/inspection/details/tabs/timelineHistory/inspectionTimelineHistoryController',
        'app/desktop/modules/pqm/inspection/details/tabs/relatedItem/inspectionRelatedItemsController',
        'app/shared/services/core/inspectionPlanService',
        'app/shared/services/core/inspectionService',
        'app/shared/services/core/objectFileService',
        'app/shared/services/core/qualityTypeService',
        'app/shared/services/core/workflowService',
        'app/assets/bower_components/cassini-platform/app/desktop/directives/freetext/freeTextDirective',
        'app/desktop/directives/plugin-directive/pluginTabsDirective'

    ],
    function (module) {
        module.controller('InspectionDetailsController', InspectionDetailsController);

        function InspectionDetailsController($scope, $rootScope, $timeout, $interval, $window, $state, $stateParams, $cookies, $sce,
                                             $translate, $application, $injector, InspectionService, WorkflowService, CommentsService) {

            $rootScope.viewInfo.icon = "fa fa-list-ul";
            $rootScope.viewInfo.showDetails = true;

            var vm = this;

            vm.back = back;
            vm.inspectionDetailsTabActivated = inspectionDetailsTabActivated;
            vm.inspectionId = $stateParams.inspectionId;
            var parsed = angular.element("<div></div>");
            var basic = parsed.html($translate.instant("DETAILS_TAB_BASIC")).html();
            var attributesTitle = parsed.html($translate.instant("DETAILS_TAB_ATTRIBUTES")).html();
            var filesTabHeading = parsed.html($translate.instant("DETAILS_TAB_FILES")).html();
            var checklistTabHeading = parsed.html($translate.instant("CHECKLIST")).html();
            var workflowHeading = parsed.html($translate.instant("WORKFLOW")).html();
            var relatedItemHeading = parsed.html($translate.instant("RELATED_ITEMS")).html();
            var timelineHeading = parsed.html($translate.instant("TIMELINE")).html();
            vm.changeWorkflowTitle = parsed.html($translate.instant("CHANGE_WORKFLOW")).html();
            vm.tabId = $stateParams.tab;
            vm.customTabs = [];


            vm.tabs = {
                basic: {
                    id: 'details.basic',
                    heading: basic,
                    template: 'app/desktop/modules/pqm/inspection/details/tabs/basic/inspectionBasicInfoView.jsp',
                    index: 0,
                    active: true,
                    activated: true
                },
                checklist: {
                    id: 'details.checklist',
                    heading: checklistTabHeading,
                    template: 'app/desktop/modules/pqm/inspection/details/tabs/checklist/inspectionChecklistView.jsp',
                    index: 1,
                    active: false,
                    activated: false
                },
                relatedItem: {
                    id: 'details.relatedItem',
                    heading: relatedItemHeading,
                    template: 'app/desktop/modules/pqm/inspection/details/tabs/relatedItem/inspectionRelatedItemsView.jsp',
                    index: 2,
                    active: false,
                    activated: false
                },
                workflow: {
                    id: 'details.workflow',
                    heading: workflowHeading,
                    template: 'app/desktop/modules/pqm/inspection/details/tabs/workflow/inspectionWorkflowView.jsp',
                    index: 3,
                    active: false,
                    activated: false
                },
                files: {
                    id: 'details.files',
                    heading: filesTabHeading,
                    template: 'app/desktop/modules/pqm/inspection/details/tabs/files/inspectionFilesView.jsp',
                    index: 4,
                    active: false,
                    activated: false
                },
                timelineHistory: {
                    id: 'details.timelineHistory',
                    heading: timelineHeading,
                    template: 'app/desktop/modules/pqm/inspection/details/tabs/timelineHistory/inspectionTimelineHistoryView.jsp',
                    index: 5,
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

            function inspectionDetailsTabActivated(tabId) {
                $state.transitionTo('app.pqm.inspection.details', {
                    inspectionId: vm.inspectionId,
                    tab: tabId
                }, {notify: false});
                var tab = getTabById(tabId);
                vm.active = tab.index;
                if (tab != null) {
                    $rootScope.selectedTab = tab;
                    tab.activated = true;
                    tab.active = true;
                    $scope.$broadcast('app.inspection.tabActivated', {tabId: tabId});
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


            function loadInspection() {
                vm.loading = true;
                InspectionService.getInspection(vm.inspectionId).then(
                    function (data) {
                        vm.inspection = data;
                        loadCommentsCount();
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                        $rootScope.hideBusyIndicator();
                    }
                )
            }

            $rootScope.clipBoardObjectFiles = [];
            $rootScope.clipBoardObjectFiles = $application.clipboard.files;

            $rootScope.loadInspectionDetails = loadInspectionDetails;
            function loadInspectionDetails() {
                InspectionService.getDetailsCount(vm.inspectionId).then(
                    function (data) {
                        $rootScope.inspectionCounts = data;
                        var checklists = document.getElementById("checklist");
                        var files = document.getElementById("files");
                        var relatedItems = document.getElementById("relatedItems");

                        if (checklists != null) {
                            checklists.lastElementChild.innerHTML = vm.tabs.checklist.heading +
                                "<span class='label label-default' style='margin-top:20px;background-color: #e4dddd;height: 20px;margin-left: 5px;'>{0}</span>".format($rootScope.inspectionCounts.checklists);
                        }

                        if (files != null) {
                            files.lastElementChild.innerHTML = vm.tabs.files.heading +
                                "<span class='label label-default' style='margin-top:20px;background-color: #e4dddd;height: 20px;margin-left: 5px;'>{0}</span>".format($rootScope.inspectionCounts.itemFiles);
                        }

                        if (relatedItems != null) {
                            relatedItems.lastElementChild.innerHTML = vm.tabs.relatedItem.heading +
                                "<span class='label label-default' style='margin-top:20px;background-color: #e4dddd;height: 20px;margin-left: 5px;'>{0}</span>".format($rootScope.inspectionCounts.relatedItems);
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
                $scope.$broadcast('app.inspection.tabActivated', {tabId: 'details.files'});
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
                    $scope.$broadcast('app.inspection.tabActivated', {tabId: 'details.files'});
                }
            }

            var lastSelectedTab = null;

            function validateJSON() {
                try {
                    JSON.parse($window.localStorage.getItem("lastSelectedInspectionTab"));
                } catch (e) {
                    return false;
                }
                return true;
            }

            function loadCommentsCount() {
                CommentsService.getAllCommentsCount('ITEMINSPECTION', vm.inspectionId).then(
                    function (data) {
                        $rootScope.showComments('ITEMINSPECTION', vm.inspectionId, data);
                        $rootScope.showTags('ITEMINSPECTION', vm.inspectionId, vm.inspection.tags.length);
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                    }
                )
            }

            (function () {
                if (validateJSON()) {
                    lastSelectedTab = JSON.parse($window.localStorage.getItem("lastSelectedInspectionTab"));
                }

                $window.localStorage.setItem("lastSelectedInspectionTab", "");

                if (lastSelectedTab != null && lastSelectedTab != undefined) {
                    inspectionDetailsTabActivated(lastSelectedTab);

                    $timeout(function () {
                        $scope.$broadcast('app.inspection.tabActivated', {tabId: lastSelectedTab});
                    }, 1000)
                } else if (tabId != null && tabId != undefined) {
                    inspectionDetailsTabActivated(tabId);

                    $timeout(function () {
                        $scope.$broadcast('app.inspection.tabActivated', {tabId: tabId});
                    }, 1000)
                }
                loadInspection();
                loadInspectionDetails();
            })();
        }
    }
)
;