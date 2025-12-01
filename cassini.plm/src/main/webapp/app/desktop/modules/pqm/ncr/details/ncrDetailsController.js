define(
    [
        'app/desktop/modules/pqm/pqm.module',
        'app/desktop/modules/pqm/ncr/details/tabs/basic/ncrBasicInfoController',
        'app/desktop/modules/pqm/ncr/details/tabs/attributes/ncrAttributesController',
        'app/desktop/modules/pqm/ncr/details/tabs/problemItems/ncrProblemItemsController',
        'app/desktop/modules/pqm/ncr/details/tabs/relatedItem/ncrRelatedItemsController',
        'app/desktop/modules/pqm/ncr/details/tabs/files/ncrFilesController',
        'app/desktop/modules/pqm/ncr/details/tabs/workflow/ncrWorkflowController',
        'app/desktop/modules/pqm/ncr/details/tabs/timelineHistory/ncrHistoryController',
        'app/shared/services/core/ncrService',
        'app/shared/services/core/objectFileService',
        'app/shared/services/core/qualityTypeService',
        'app/shared/services/core/itemTypeService',
        'app/shared/services/core/itemFileService',
        'app/shared/services/core/itemService',
        'app/shared/services/core/workflowService',
        'app/assets/bower_components/cassini-platform/app/desktop/directives/freetext/freeTextDirective',
        'app/desktop/directives/plugin-directive/pluginTabsDirective'

    ],
    function (module) {
        module.controller('NcrDetailsController', NcrDetailsController);

        function NcrDetailsController($scope, $rootScope, $timeout, $interval, $window, $state, $stateParams, $cookies, $sce,
                                      $translate, $application, $injector, NcrService, WorkflowService, CommentsService) {
                          
            $rootScope.viewInfo.icon = "fa fa-indent";
            $rootScope.viewInfo.title = "";
            $rootScope.viewInfo.showDetails = true;

            var vm = this;

            vm.back = back;
            vm.ncrDetailsTabActivated = ncrDetailsTabActivated;
            vm.ncrId = $stateParams.ncrId;
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
            vm.tabId = $stateParams.tab;
            vm.customTabs = [];

            vm.tabs = {
                basic: {
                    id: 'details.basic',
                    heading: basic,
                    template: 'app/desktop/modules/pqm/ncr/details/tabs/basic/ncrBasicInfoView.jsp',
                    index: 0,
                    active: true,
                    activated: true
                }/*,
                 attributes: {
                 id: 'details.attributes',
                 heading: attributesTitle,
                 template: 'app/desktop/modules/pqm/ncr/details/tabs/attributes/ncrAttributesView.jsp',
                 index: 1,
                 active: false,
                 activated: false
                 }*/,
                problemItem: {
                    id: 'details.problemItem',
                    heading: problemItemsTabHeading,
                    template: 'app/desktop/modules/pqm/ncr/details/tabs/problemItems/ncrProblemItemsView.jsp',
                    index: 1,
                    active: false,
                    activated: false
                },
                relatedItem: {
                    id: 'details.relatedItem',
                    heading: relatedItemHeading,
                    template: 'app/desktop/modules/pqm/ncr/details/tabs/relatedItem/ncrRelatedItemsView.jsp',
                    index: 2,
                    active: false,
                    activated: false
                },
                workflow: {
                    id: 'details.workflow',
                    heading: workflowHeading,
                    template: 'app/desktop/modules/pqm/ncr/details/tabs/workflow/ncrWorkflowView.jsp',
                    index: 3,
                    active: false,
                    activated: false
                },
                files: {
                    id: 'details.files',
                    heading: filesTabHeading,
                    template: 'app/desktop/modules/pqm/ncr/details/tabs/files/ncrFilesView.jsp',
                    index: 4,
                    active: false,
                    activated: false
                },
                timelineHistory: {
                    id: 'details.timelineHistory',
                    heading: timelineHeading,
                    template: 'app/desktop/modules/pqm/ncr/details/tabs/timelineHistory/ncrHistoryView.jsp',
                    index: 5,
                    active: false,
                    activated: false
                }

            };

            var tabId = $stateParams.tab;
            if (tabId == null || tabId === undefined || tabId.trim() === "") {
                tabId = 'details.basic';
                $state.transitionTo('app.pqm.ncr.details', {
                    ncrId: vm.ncrId,
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

            function ncrDetailsTabActivated(tabId) {
                $state.transitionTo('app.pqm.ncr.details', {
                    ncrId: vm.ncrId,
                    tab: tabId
                }, {notify: false});
                var tab = getTabById(tabId);
                vm.active = tab.index;
                if (tab != null) {
                    $rootScope.selectedTab = tab;
                    tab.activated = true;
                    tab.active = true;
                    activateTab(tab);
                    $scope.$broadcast('app.ncr.tabActivated', {tabId: tabId});
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

            $rootScope.loadNcr = loadNcr;
            function loadNcr() {
                NcrService.getNcr(vm.ncrId).then(
                    function (data) {
                        vm.ncr = data;
                        $rootScope.ncr = vm.ncr;
                        vm.loading = false;
                        $rootScope.viewInfo.title = "<div class='item-number'>" +
                            "{0}</div>".
                            format(vm.ncr.ncrNumber);
                            $rootScope.viewInfo.description = vm.ncr.title;
                        $scope.$evalAsync();
                        loadWorkflow();
                        loadCommentsCount();
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                        $rootScope.hideBusyIndicator();
                    }
                )
            }

            $rootScope.ncrWorkflowStarted = false;
            function loadWorkflow() {
                WorkflowService.getWorkflow($rootScope.ncr.workflow).then(
                    function (data) {
                        vm.workflow = data;
                        if (vm.workflow.started) {
                            $rootScope.ncrWorkflowStarted = true;
                        }
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                        $rootScope.hideBusyIndicator();
                    }
                )
            }

            $rootScope.loadNcrDetails = loadNcrDetails;
            function loadNcrDetails() {
                NcrService.getNcrDetailsCount(vm.ncrId).then(
                    function (data) {
                        $rootScope.ncrDetailsCount = data;
                        var affectedItems = document.getElementById("problemItems");
                        var relatedItems = document.getElementById("relatedItems");
                        var files = document.getElementById("files");

                        if (affectedItems != null) {
                            affectedItems.lastElementChild.innerHTML = vm.tabs.problemItem.heading +
                                "<span class='label label-default' style='margin-top:20px;background-color: #e4dddd;height: 20px;margin-left: 5px;'>{0}</span>".format($rootScope.ncrDetailsCount.problemItems);
                        }

                        if (relatedItems != null) {
                            relatedItems.lastElementChild.innerHTML = vm.tabs.relatedItem.heading +
                                "<span class='label label-default' style='margin-top:20px;background-color: #e4dddd;height: 20px;margin-left: 5px;'>{0}</span>".format($rootScope.ncrDetailsCount.relatedItems);
                        }
                        if (files != null) {
                            files.lastElementChild.innerHTML = vm.tabs.files.heading +
                                "<span class='label label-default' style='margin-top:20px;background-color: #e4dddd;height: 20px;margin-left: 5px;'>{0}</span>".format($rootScope.ncrDetailsCount.itemFiles);
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
                $scope.$broadcast('app.ncr.tabActivated', {tabId: 'details.files'});
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
                    $scope.$broadcast('app.ncr.tabActivated', {tabId: 'details.files'});
                }
            }

            var lastSelectedTab = null;

            function validateJSON() {
                try {
                    JSON.parse($window.localStorage.getItem("lastSelectedNcrTab"));
                } catch (e) {
                    return false;
                }
                return true;
            }

            function loadCommentsCount() {
                CommentsService.getAllCommentsCount('NCR', vm.ncrId).then(
                    function (data) {
                        $rootScope.showComments('NCR', vm.ncrId, data);
                        $rootScope.showTags('NCR', vm.ncrId, vm.ncr.tags.length);
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                    }
                )
            }

            (function () {
                //if ($application.homeLoaded == true) {
                if (validateJSON()) {
                    lastSelectedTab = JSON.parse($window.localStorage.getItem("lastSelectedNcrTab"));
                }

                $window.localStorage.setItem("lastSelectedNcrTab", "");

                if (lastSelectedTab != null && lastSelectedTab != undefined) {
                    ncrDetailsTabActivated(lastSelectedTab);

                    $timeout(function () {
                        $scope.$broadcast('app.ncr.tabActivated', {tabId: lastSelectedTab});
                    }, 1000)
                } else if (tabId != null && tabId != undefined) {
                    ncrDetailsTabActivated(tabId);

                    $timeout(function () {
                        $scope.$broadcast('app.ncr.tabActivated', {tabId: tabId});
                    }, 1000)
                }
                loadNcr();
                loadNcrDetails();
                //}
            })();
        }
    }
)
;