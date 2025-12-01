define(
    [
        'app/desktop/modules/pqm/pqm.module',
        'app/desktop/modules/pqm/problemReport/details/tabs/basic/prBasicInfoController',
        'app/desktop/modules/pqm/problemReport/details/tabs/attributes/prAttributesController',
        'app/desktop/modules/pqm/problemReport/details/tabs/problemItems/prProblemItemsController',
        'app/desktop/modules/pqm/problemReport/details/tabs/relatedItem/prRelatedItemsController',
        'app/desktop/modules/pqm/problemReport/details/tabs/files/prFilesController',
        'app/desktop/modules/pqm/problemReport/details/tabs/workflow/prWorkflowController',
        'app/desktop/modules/pqm/problemReport/details/tabs/timelineHistory/prHistoryController',
        'app/shared/services/core/problemReportService',
        'app/shared/services/core/objectFileService',
        'app/shared/services/core/qualityTypeService',
        'app/shared/services/core/itemTypeService',
        'app/shared/services/core/itemFileService',
        'app/shared/services/core/itemService',
        'app/shared/services/core/workflowService',
        'app/assets/bower_components/cassini-platform/app/desktop/directives/freetext/freeTextDirective',
        'app/desktop/directives/dynamic-controller/dynamicCtrl',
        'app/desktop/modules/item/details/customExtensionController',
        'app/desktop/directives/plugin-directive/pluginTabsDirective'

    ],
    function (module) {
        module.controller('ProblemReportDetailsController', ProblemReportDetailsController);

        function ProblemReportDetailsController($scope, $rootScope, $timeout, $interval, $window, $state, $stateParams, $cookies, $sce,
                                                $translate, $application, $injector, ProblemReportService, WorkflowService, CommentsService) {

            $rootScope.viewInfo.icon = "fa fa-indent";
            $rootScope.viewInfo.title = "";
            $rootScope.viewInfo.showDetails = true;

            var vm = this;

            vm.back = back;
            vm.problemReportDetailsTabActivated = problemReportDetailsTabActivated;
            vm.problemReportId = $stateParams.problemReportId;
            vm.tabId = $stateParams.tab;
            var parsed = angular.element("<div></div>");
            var basic = parsed.html($translate.instant("DETAILS_TAB_BASIC")).html();
            var attributesTitle = parsed.html($translate.instant("DETAILS_TAB_ATTRIBUTES")).html();
            var filesTabHeading = parsed.html($translate.instant("DETAILS_TAB_FILES")).html();
            var problemItemsTabHeading = parsed.html($translate.instant("PROBLEM_ITEMS")).html();
            var workflowHeading = parsed.html($translate.instant("WORKFLOW")).html();
            var relatedItemHeading = parsed.html($translate.instant("RELATED_ITEMS")).html();
            var timelineHeading = parsed.html($translate.instant("TIMELINE")).html();
            vm.changeWorkflowTitle = parsed.html($translate.instant("CHANGE_WORKFLOW")).html();
            $rootScope.clipBoardObjectFiles = [];
            $rootScope.clipBoardObjectFiles = $application.clipboard.files;
            vm.customTabs = [];

            vm.tabs = {
                basic: {
                    id: 'details.basic',
                    heading: basic,
                    template: 'app/desktop/modules/pqm/problemReport/details/tabs/basic/prBasicInfoView.jsp',
                    index: 0,
                    active: true,
                    activated: true
                }/*,
                 attributes: {
                 id: 'details.attributes',
                 heading: attributesTitle,
                 template: 'app/desktop/modules/pqm/problemReport/details/tabs/attributes/prAttributesView.jsp',
                 index: 1,
                 active: false,
                 activated: false
                 }*/,
                problemItem: {
                    id: 'details.problemItem',
                    heading: problemItemsTabHeading,
                    template: 'app/desktop/modules/pqm/problemReport/details/tabs/problemItems/prProblemItemsView.jsp',
                    index: 1,
                    active: false,
                    activated: false
                },
                relatedItem: {
                    id: 'details.relatedItem',
                    heading: relatedItemHeading,
                    template: 'app/desktop/modules/pqm/problemReport/details/tabs/relatedItem/prRelatedItemsView.jsp',
                    index: 2,
                    active: false,
                    activated: false
                },
                workflow: {
                    id: 'details.workflow',
                    heading: workflowHeading,
                    template: 'app/desktop/modules/pqm/problemReport/details/tabs/workflow/prWorkflowView.jsp',
                    index: 3,
                    active: false,
                    activated: false
                },
                files: {
                    id: 'details.files',
                    heading: filesTabHeading,
                    template: 'app/desktop/modules/pqm/problemReport/details/tabs/files/prFilesView.jsp',
                    index: 4,
                    active: false,
                    activated: false
                },
                timelineHistory: {
                    id: 'details.timelineHistory',
                    heading: timelineHeading,
                    template: 'app/desktop/modules/pqm/problemReport/details/tabs/timelineHistory/prHistoryView.jsp',
                    index: 5,
                    active: false,
                    activated: false
                }

            };
            vm.active = -1;

            var tabId = $stateParams.tab;
            if (tabId == null || tabId === undefined || tabId.trim() === "") {
                tabId = 'details.basic';
                $state.transitionTo('app.pqm.pr.details', {
                    problemReportId: vm.problemReportId,
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

            function problemReportDetailsTabActivated(tabId) {
                $state.transitionTo('app.pqm.pr.details', {
                    problemReportId: vm.problemReportId,
                    tab: tabId
                }, {notify: false});
                var tab = getTabById(tabId);
                vm.active = tab.index;
                if (tab != null) {
                    $rootScope.selectedTab = tab;
                    tab.activated = true;
                    tab.active = true;
                    $scope.$broadcast('app.problemReport.tabActivated', {tabId: tabId});
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

            vm.performCustomAction = performCustomAction;
            function performCustomAction(action) {
                var service = $injector.get(action.service);
                if (service != null && service !== undefined) {
                    var method = service[action.method];
                    if (method != null && method !== undefined && typeof method === "function") {
                        method(vm.problemReport);
                    }
                }
            }

            $rootScope.loadProblemReport = loadProblemReport;
            function loadProblemReport() {
                ProblemReportService.getProblemReport(vm.problemReportId).then(
                    function (data) {
                        vm.problemReport = data;
                        $rootScope.problemReport = vm.problemReport;
                        vm.loading = false;
                        $rootScope.viewInfo.title = "<div class='item-number'>" +
                        "{0}</div>".
                            format(vm.problemReport.prNumber);
                            $rootScope.viewInfo.description = vm.problemReport.title;
                        $scope.$evalAsync();
                        loadWorkflow();
                        loadCommentsCount();
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                        $rootScope.hideBusyIndicator();
                    }
                )
            }

            $rootScope.problemReportDetailsCount = null;
            $rootScope.loadProblemReportDetails = loadProblemReportDetails;
            function loadProblemReportDetails() {
                ProblemReportService.getDetailsCount(vm.problemReportId).then(
                    function (data) {
                        $rootScope.problemReportDetailsCount = data;
                        var affectedItems = document.getElementById("problemItems");
                        var relatedItems = document.getElementById("relatedItems");
                        var files = document.getElementById("files");

                        if (affectedItems != null) {
                            affectedItems.lastElementChild.innerHTML = vm.tabs.problemItem.heading +
                                "<span class='label label-default' style='margin-top:20px;background-color: #e4dddd;height: 20px;margin-left: 5px;'>{0}</span>".format($rootScope.problemReportDetailsCount.problemItems);
                        }

                        if (relatedItems != null) {
                            relatedItems.lastElementChild.innerHTML = vm.tabs.relatedItem.heading +
                                "<span class='label label-default' style='margin-top:20px;background-color: #e4dddd;height: 20px;margin-left: 5px;'>{0}</span>".format($rootScope.problemReportDetailsCount.relatedItems);
                        }
                        if (files != null) {
                            files.lastElementChild.innerHTML = vm.tabs.files.heading +
                                "<span class='label label-default' style='margin-top:20px;background-color: #e4dddd;height: 20px;margin-left: 5px;'>{0}</span>".format($rootScope.problemReportDetailsCount.itemFiles);
                        }
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                        $rootScope.hideBusyIndicator();
                    }
                )
            }

            $rootScope.prWorkflowStarted = false;
            function loadWorkflow() {
                WorkflowService.getWorkflow($rootScope.problemReport.workflow).then(
                    function (data) {
                        vm.workflow = data;
                        if (vm.workflow.started) {
                            $rootScope.prWorkflowStarted = true;
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
                $scope.$broadcast('app.problemReport.tabActivated', {tabId: 'details.files'});
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
                    $scope.$broadcast('app.problemReport.tabActivated', {tabId: 'details.files'});
                }
            }

            var lastSelectedTab = null;

            function validateJSON() {
                try {
                    JSON.parse($window.localStorage.getItem("lastSelectedProblemReportTab"));
                } catch (e) {
                    return false;
                }
                return true;
            }

            function loadCommentsCount() {
                CommentsService.getAllCommentsCount('PROBLEMREPORT', vm.problemReportId).then(
                    function (data) {
                        $rootScope.showComments('PROBLEMREPORT', vm.problemReportId, data);
                        $rootScope.showTags('PROBLEMREPORT', vm.problemReportId, vm.problemReport.tags.length);
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                    }
                )
            }

            function getLastTabIndexOfStandardTabs() {
                var index = 0;
                for (var t in vm.tabs) {
                    if (vm.tabs.hasOwnProperty(t)) {
                        index = vm.tabs[t].index;
                    }
                }
                return index;
            }


            (function () {
                //if ($application.homeLoaded == true) {
                if (validateJSON()) {
                    lastSelectedTab = JSON.parse($window.localStorage.getItem("lastSelectedProblemReportTab"));
                }

                $window.localStorage.setItem("lastSelectedProblemReportTab", "");

                if (lastSelectedTab != null && lastSelectedTab != undefined) {
                    problemReportDetailsTabActivated(lastSelectedTab);

                    $timeout(function () {
                        $scope.$broadcast('app.problemReport.tabActivated', {tabId: lastSelectedTab});
                    }, 1000)
                } else if (tabId != null && tabId != undefined) {
                    problemReportDetailsTabActivated(tabId);

                    $timeout(function () {
                        $scope.$broadcast('app.problemReport.tabActivated', {tabId: tabId});
                    }, 1000)
                }
                loadProblemReport();
                loadProblemReportDetails();
                //}
            })();
        }
    }
)
;