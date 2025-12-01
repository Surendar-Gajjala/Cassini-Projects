define(
    [
        'app/desktop/modules/pqm/pqm.module',
        'app/desktop/modules/pqm/qcr/details/tabs/basic/qcrBasicInfoController',
        'app/desktop/modules/pqm/qcr/details/tabs/attributes/qcrAttributesController',
        'app/desktop/modules/pqm/qcr/details/tabs/problemItems/qcrProblemItemsController',
        'app/desktop/modules/pqm/qcr/details/tabs/problemSources/qcrProblemSourcesController',
        'app/desktop/modules/pqm/qcr/details/tabs/relatedItem/qcrRelatedItemsController',
        'app/desktop/modules/pqm/qcr/details/tabs/capa/qcrCaPaController',
        'app/desktop/modules/pqm/qcr/details/tabs/files/qcrFilesController',
        'app/desktop/modules/pqm/qcr/details/tabs/workflow/qcrWorkflowController',
        'app/desktop/modules/pqm/qcr/details/tabs/timelineHistory/qcrHistoryController',
        'app/shared/services/core/qcrService',
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
        module.controller('QcrDetailsController', QcrDetailsController);

        function QcrDetailsController($scope, $rootScope, $timeout, $interval, $window, $state, $stateParams, $cookies, $sce,
                                      $translate, $application, $injector, QcrService, WorkflowService, CommentsService) {

            $rootScope.viewInfo.icon = "fa fa-certificate";
            $rootScope.viewInfo.title = "";
            $rootScope.viewInfo.showDetails = true;

            var vm = this;

            vm.back = back;
            vm.qcrDetailsTabActivated = qcrDetailsTabActivated;
            vm.qcrId = $stateParams.qcrId;
            var parsed = angular.element("<div></div>");
            var basic = parsed.html($translate.instant("DETAILS_TAB_BASIC")).html();
            var attributesTitle = parsed.html($translate.instant("DETAILS_TAB_ATTRIBUTES")).html();
            var filesTabHeading = parsed.html($translate.instant("DETAILS_TAB_FILES")).html();
            var problemItemsTabHeading = parsed.html($translate.instant("PROBLEM_ITEMS")).html();
            var problemSourcesTabHeading = parsed.html($translate.instant("PROBLEM_SOURCES")).html();
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
                    template: 'app/desktop/modules/pqm/qcr/details/tabs/basic/qcrBasicInfoView.jsp',
                    index: 0,
                    active: true,
                    activated: true
                }/*,
                 attributes: {
                 id: 'details.attributes',
                 heading: attributesTitle,
                 template: 'app/desktop/modules/pqm/qcr/details/tabs/attributes/qcrAttributesView.jsp',
                 index: 1,
                 active: false,
                 activated: false
                 }*/,
                problemSource: {
                    id: 'details.problemSource',
                    heading: problemSourcesTabHeading,
                    template: 'app/desktop/modules/pqm/qcr/details/tabs/problemSources/qcrProblemSourcesView.jsp',
                    index: 1,
                    active: false,
                    activated: false
                },
                problemItem: {
                    id: 'details.problemItem',
                    heading: problemItemsTabHeading,
                    template: 'app/desktop/modules/pqm/qcr/details/tabs/problemItems/qcrProblemItemsView.jsp',
                    index: 2,
                    active: false,
                    activated: false
                },
                relatedItem: {
                    id: 'details.relatedItem',
                    heading: relatedItemHeading,
                    template: 'app/desktop/modules/pqm/qcr/details/tabs/relatedItem/qcrRelatedItemsView.jsp',
                    index: 3,
                    active: false,
                    activated: false
                },
                capa: {
                    id: 'details.capa',
                    heading: "CAPA",
                    template: 'app/desktop/modules/pqm/qcr/details/tabs/capa/qcrCaPaView.jsp',
                    index: 4,
                    active: false,
                    activated: false
                },
                workflow: {
                    id: 'details.workflow',
                    heading: workflowHeading,
                    template: 'app/desktop/modules/pqm/qcr/details/tabs/workflow/qcrWorkflowView.jsp',
                    index: 5,
                    active: false,
                    activated: false
                },
                files: {
                    id: 'details.files',
                    heading: filesTabHeading,
                    template: 'app/desktop/modules/pqm/qcr/details/tabs/files/qcrFilesView.jsp',
                    index: 6,
                    active: false,
                    activated: false
                },
                timelineHistory: {
                    id: 'details.timelineHistory',
                    heading: timelineHeading,
                    template: 'app/desktop/modules/pqm/qcr/details/tabs/timelineHistory/qcrHistoryView.jsp',
                    index: 7,
                    active: false,
                    activated: false
                }

            };

            var tabId = $stateParams.tab;
            if (tabId == null || tabId === undefined || tabId.trim() === "") {
                tabId = 'details.basic';
                $state.transitionTo('app.pqm.qcr.details', {
                    qcrId: vm.qcrId,
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

            function qcrDetailsTabActivated(tabId) {
                $state.transitionTo('app.pqm.qcr.details', {
                    qcrId: vm.qcrId,
                    tab: tabId
                }, {notify: false});
                var tab = getTabById(tabId);
                vm.active = tab.index;
                if (tab != null) {
                    $rootScope.selectedTab = tab;
                    tab.activated = true;
                    tab.active = true;
                    activateTab(tab);
                    $scope.$broadcast('app.qcr.tabActivated', {tabId: tabId});
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

            $rootScope.qcrReleased = false;
            $rootScope.loadQcr = loadQcr;
            function loadQcr() {
                QcrService.getQcr(vm.qcrId).then(
                    function (data) {
                        vm.qcr = data;
                        $rootScope.qcr = vm.qcr;
                        if (vm.qcr.released || vm.qcr.statusType == "REJECTED") {
                            $rootScope.qcrReleased = true;
                        }
                        vm.loading = false;
                        $rootScope.viewInfo.title = "<div class='item-number'>" +
                            "{0}</div>".format(vm.qcr.qcrNumber);
                            $rootScope.viewInfo.description = vm.qcr.title;
                        $scope.$evalAsync();
                        loadWorkflow();
                        loadCommentsCount();
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                        $rootScope.hideBusyIndicator();
                    }
                )
            }


            $rootScope.qcrWorkflowStarted = false;
            function loadWorkflow() {
                WorkflowService.getWorkflow($rootScope.qcr.workflow).then(
                    function (data) {
                        vm.workflow = data;
                        if (vm.workflow.started) {
                            $rootScope.qcrWorkflowStarted = true;
                        }
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                        $rootScope.hideBusyIndicator();
                    }
                )
            }

            $rootScope.loadQcrDetails = loadQcrDetails;
            function loadQcrDetails() {
                QcrService.getQcrDetailsCount(vm.qcrId).then(
                    function (data) {
                        $rootScope.qcrDetailCount = data;
                        var problemSources = document.getElementById("problemSources");
                        var affectedItems = document.getElementById("problemItems");
                        var relatedItems = document.getElementById("relatedItems");
                        var files = document.getElementById("files");
                        var capaTab = document.getElementById("capaTab");

                        if (problemSources != null) {
                            problemSources.lastElementChild.innerHTML = vm.tabs.problemSource.heading +
                                "<span class='label label-default' style='margin-top:20px;background-color: #e4dddd;height: 20px;margin-left: 5px;'>{0}</span>".format($rootScope.qcrDetailCount.problemSources);
                        }
                        if (affectedItems != null) {
                            affectedItems.lastElementChild.innerHTML = vm.tabs.problemItem.heading +
                                "<span class='label label-default' style='margin-top:20px;background-color: #e4dddd;height: 20px;margin-left: 5px;'>{0}</span>".format($rootScope.qcrDetailCount.problemItems);
                        }

                        if (relatedItems != null) {
                            relatedItems.lastElementChild.innerHTML = vm.tabs.relatedItem.heading +
                                "<span class='label label-default' style='margin-top:20px;background-color: #e4dddd;height: 20px;margin-left: 5px;'>{0}</span>".format($rootScope.qcrDetailCount.relatedItems);
                        }
                        if (files != null) {
                            files.lastElementChild.innerHTML = vm.tabs.files.heading +
                                "<span class='label label-default' style='margin-top:20px;background-color: #e4dddd;height: 20px;margin-left: 5px;'>{0}</span>".format($rootScope.qcrDetailCount.itemFiles);
                        }
                        if (capaTab != null) {
                            capaTab.lastElementChild.innerHTML = vm.tabs.capa.heading +
                                "<span class='label label-default' style='margin-top:20px;background-color: #e4dddd;height: 20px;margin-left: 5px;'>{0}</span>".format($rootScope.qcrDetailCount.capaCount);
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
                $scope.$broadcast('app.qcr.tabActivated', {tabId: 'details.files'});
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
                    $scope.$broadcast('app.qcr.tabActivated', {tabId: 'details.files'});
                }
            }

            var lastSelectedTab = null;

            function validateJSON() {
                try {
                    JSON.parse($window.localStorage.getItem("lastSelectedQcrTab"));
                } catch (e) {
                    return false;
                }
                return true;
            }


            function loadCommentsCount() {
                CommentsService.getAllCommentsCount('QCR', vm.qcrId).then(
                    function (data) {
                        $rootScope.showComments('QCR', vm.qcrId, data);
                        $rootScope.showTags('QCR', vm.qcrId, vm.qcr.tags.length);
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                    }
                )
            }

            (function () {
                //if ($application.homeLoaded == true) {
                if (validateJSON()) {
                    lastSelectedTab = JSON.parse($window.localStorage.getItem("lastSelectedQcrTab"));
                }

                $window.localStorage.setItem("lastSelectedQcrTab", "");

                if (lastSelectedTab != null && lastSelectedTab != undefined) {
                    qcrDetailsTabActivated(lastSelectedTab);

                    $timeout(function () {
                        $scope.$broadcast('app.qcr.tabActivated', {tabId: lastSelectedTab});
                    }, 1000)
                } else if (tabId != null && tabId != undefined) {
                    qcrDetailsTabActivated(tabId);

                    $timeout(function () {
                        $scope.$broadcast('app.qcr.tabActivated', {tabId: tabId});
                    }, 1000)
                }
                loadQcr();
                loadQcrDetails();
                //}
            })();
        }
    }
)
;