define(
    [
        'app/desktop/modules/change/change.module',
        'app/shared/services/core/ecoService',
        'app/shared/services/core/dcrService',
        'app/shared/services/core/ecrService',
        'app/shared/services/core/itemService',
        'app/shared/services/core/workflowDefinitionService',
        'app/shared/services/core/mfrService',
        'app/shared/services/core/mfrPartsService',
        'app/shared/services/core/specificationsService',
        'app/assets/bower_components/cassini-platform/app/desktop/modules/common/comments/commentsDirective',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/commonService',
        'app/desktop/modules/change/ecr/details/tabs/basic/ecrBasicInfoController',
        'app/desktop/modules/change/ecr/details/tabs/attributes/ecrAttributesController',
        'app/desktop/modules/change/ecr/details/tabs/problemReports/ecrProblemReportsController',
        'app/desktop/modules/change/ecr/details/tabs/workflow/ecrWorkflowController',
        'app/desktop/modules/change/ecr/details/tabs/affectedItem/ecrAffectedItemsController',
        'app/desktop/modules/change/ecr/details/tabs/relatedItem/ecrRelatedItemsController',
        'app/desktop/modules/change/ecr/details/tabs/files/ecrFilesController',
        'app/desktop/modules/change/ecr/details/tabs/timeLine/ecrTimeLineController',
        'app/assets/bower_components/cassini-platform/app/desktop/directives/freetext/freeTextDirective',
        'app/desktop/directives/plugin-directive/pluginTabsDirective'

    ],
    function (module) {
        module.controller('ECRDetailsController', ECRDetailsController);

        function ECRDetailsController($scope, $rootScope, $timeout, $state, $stateParams, $cookies, $window, $application,
                                      ECOService, $translate, CommentsService, ECRService) {
            var vm = this;

            $rootScope.viewInfo.icon = "fa fa-exchange";
            $rootScope.viewInfo.title = "";
            $rootScope.viewInfo.showDetails = true;

            vm.ecrId = $stateParams.ecrId;
            vm.back = back;

            var lastSelectedTab = null;
            vm.active = 0;

            var parsed = angular.element("<div></div>");
            vm.detailsShareTitle = parsed.html($translate.instant("DETAILS_SHARE_TITLE")).html();
            vm.refreshTitle = parsed.html($translate.instant("CLICK_TO_REFRESH")).html();
            var AffectedItems = parsed.html($translate.instant("ECO_AFFECTED_ITEMS")).html();
            var problemReportsTitle = parsed.html($translate.instant("PROBLEMREPORTS")).html();
            var RelatedItems = parsed.html($translate.instant("RELATED_ITEMS")).html();
            var filesTabHeading = parsed.html($translate.instant("DETAILS_TAB_FILES")).html();
            vm.addItems = parsed.html($translate.instant("ADD_ITEM")).html();
            vm.changeWorkflowTitle = parsed.html($translate.instant("CHANGE_WORKFLOW")).html();
            var files = parsed.html($translate.instant("FILES")).html();
            var worlflow = parsed.html($translate.instant("WORKFLOW")).html();
            var attributes = parsed.html($translate.instant("DETAILS_TAB_ATTRIBUTES")).html();
            var basic = parsed.html($translate.instant("DETAILS_TAB_BASIC")).html();
            var timeline = parsed.html($translate.instant("TIMELINE")).html();
            $rootScope.clipBoardObjectFiles = [];
            $rootScope.clipBoardObjectFiles = $application.clipboard.files;
            vm.tabId = $stateParams.tab;
            vm.customTabs = [];

            vm.tabs = {
                basic: {
                    id: 'details.basic',
                    index: 0,
                    heading: basic,
                    template: 'app/desktop/modules/change/ecr/details/tabs/basic/ecrBasicInfoView.jsp',
                    active: true,
                    activated: true
                }/*,
                 attributes: {
                 id: 'details.attribute',
                 index: 1,
                 heading: attributes,
                 template: 'app/desktop/modules/change/ecr/details/tabs/attributes/ecrAttributesView.jsp',
                 active: false,
                 activated: false
                 }*/,
                problemReports: {
                    id: 'details.problemReports',
                    index: 1,
                    heading: problemReportsTitle,
                    template: 'app/desktop/modules/change/ecr/details/tabs/problemReports/ecrProblemReportsView.jsp',
                    active: false,
                    activated: false
                },
                affectedItems: {
                    id: 'details.affectedItems',
                    index: 2,
                    heading: AffectedItems,
                    template: 'app/desktop/modules/change/ecr/details/tabs/affectedItem/ecrAffectedItemsView.jsp',
                    active: false,
                    activated: false
                },
                relatedItems: {
                    id: 'details.relatedItems',
                    index: 3,
                    heading: RelatedItems,
                    template: 'app/desktop/modules/change/ecr/details/tabs/relatedItem/ecrRelatedItemsView.jsp',
                    active: false,
                    activated: false
                },
                workflow: {
                    id: 'details.workflow',
                    index: 4,
                    heading: worlflow,
                    template: 'app/desktop/modules/change/ecr/details/tabs/workflow/ecrWorkflowView.jsp',
                    active: false,
                    activated: false
                },
                files: {
                    id: 'details.files',
                    index: 5,
                    heading: files,
                    template: 'app/desktop/modules/change/ecr/details/tabs/files/ecrFilesView.jsp',
                    active: false,
                    activated: false
                },
                timeLine: {
                    id: 'details.timeLine',
                    index: 6,
                    heading: timeline,
                    template: 'app/desktop/modules/change/ecr/details/tabs/timeLine/ecrTimeLineView.jsp',
                    active: false,
                    activated: false
                }
            };

            var tabId = $stateParams.tab;
            if (tabId == null || tabId === undefined || tabId.trim() === "") {
                tabId = 'details.basic';
                $state.transitionTo('app.changes.ecr.details', {
                    ecoId: vm.ecrId,
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

            vm.tabActivated = tabActivated;
            vm.refreshDetails = refreshDetails;

            function refreshDetails() {
                $scope.$broadcast('app.ecr.tabActivated', {tabId: $rootScope.selectedTab.id});
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
                    JSON.parse($window.localStorage.getItem("lastSelectedEcrTab"));
                } catch (e) {
                    return false;
                }
                return true;
            }

            function tabActivated(tabId) {
                $state.transitionTo('app.changes.ecr.details', {
                    ecrId: vm.ecrId, tab: tabId
                }, {notify: false});
                var tab = getTabById(tabId);
                vm.active = tab.index;
                if (tab != null) {
                    $rootScope.selectedTab = tab;
                    tab.activated = true;
                    tab.active = true;
                    activateTab(tab);
                    $scope.$broadcast('app.ecr.tabActivated', {tabId: tabId});

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

            function back() {
                window.history.back();
            }

            vm.changeWorkflow = changeWorkflow;
            function changeWorkflow() {
                $scope.$broadcast('app.change.workflow');
            }

            $rootScope.loadECRCounts = loadECRCounts;
            $rootScope.loadEcrDetails = loadEcrDetails;
            function loadEcrDetails() {
                ECRService.getECR(vm.ecrId).then(
                    function (data) {
                        vm.ecr = data;
                        $rootScope.ecr = data;
                        //  $rootScope.viewInfo.title = "ECR Details";
                        //$rootScope.viewInfo.description = "Number: {0}, Status: {1}".
                        //  format(vm.ecr.crNumber, vm.ecr.status);
                        $rootScope.viewInfo.title = "<div class='item-number'>" +
                            "{0}</div>".
                                format(vm.ecr.crNumber);
                        $rootScope.viewInfo.description = vm.ecr.title;
                        loadECRCounts();
                        loadCommentsCount();
                        $timeout(function () {
                            $scope.$broadcast('app.object.plugins.tabActivated', {});
                        }, 500);
                        $scope.$evalAsync();
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                    }
                )
            }

            function loadECRCounts() {
                ECRService.getEcrDetailsCount(vm.ecrId).then(
                    function (data) {
                        $rootScope.ecrDetailsCount = data;
                        $rootScope.ecrAffectedItems = $rootScope.ecrDetailsCount.affectedItems;
                        var problemReportsTab = document.getElementById("problemReports");
                        var affectedItems = document.getElementById("affectedItems");
                        var relatedItems = document.getElementById("relatedItems");
                        var files = document.getElementById("files");

                        if (affectedItems != null) {
                            affectedItems.lastElementChild.innerHTML = vm.tabs.affectedItems.heading +
                                "<span class='label label-default' style='margin-top:20px;background-color: #e4dddd;height: 20px;margin-left: 5px;'>{0}</span>".format($rootScope.ecrDetailsCount.affectedItems);
                        }

                        if (problemReportsTab != null) {
                            problemReportsTab.lastElementChild.innerHTML = vm.tabs.problemReports.heading +
                                "<span class='label label-default' style='margin-top:20px;background-color: #e4dddd;height: 20px;margin-left: 5px;'>{0}</span>".format($rootScope.ecrDetailsCount.problemReports);
                        }

                        if (relatedItems != null) {
                            relatedItems.lastElementChild.innerHTML = vm.tabs.relatedItems.heading +
                                "<span class='label label-default' style='margin-top:20px;background-color: #e4dddd;height: 20px;margin-left: 5px;'>{0}</span>".format($rootScope.ecrDetailsCount.relatedItems);
                        }
                        if (files != null) {
                            files.lastElementChild.innerHTML = vm.tabs.files.heading +
                                "<span class='label label-default' style='margin-top:20px;background-color: #e4dddd;height: 20px;margin-left: 5px;'>{0}</span>".format($rootScope.ecrDetailsCount.itemFiles);
                        }
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                    }
                )
            }

            vm.onClear = onClear;
            function onClear() {
                $rootScope.freeTextQuerys = null;
                $scope.$broadcast('app.ecr.tabActivated', {tabId: 'details.files'});
            }

            vm.freeTextSearch = freeTextSearch;
            $rootScope.freeTextQuerys = null;
            function freeTextSearch(freeText) {
                $rootScope.freeTextQuerys = freeText;
                if (freeText != null && freeText != undefined && freeText.trim() != "") {
                    $scope.$broadcast('app.changeFile.files.search', {name: freeText});
                }
                else {
                    $rootScope.freeTextQuerys = null;
                    $scope.$broadcast('app.ecr.tabActivated', {tabId: 'details.files'});
                }
            }


            function loadCommentsCount() {
                CommentsService.getAllCommentsCount('CHANGE', vm.ecrId).then(
                    function (data) {
                        $rootScope.showComments('CHANGE', vm.ecrId, data);
                        $rootScope.showTags('CHANGE', vm.ecrId, vm.ecr.tags.length);
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                    }
                )
            }

            (function () {
                //if ($application.homeLoaded == true) {
                if (validateJSON()) {
                    lastSelectedTab = JSON.parse($window.localStorage.getItem("lastSelectedEcrTab"));
                }

                $window.localStorage.setItem("lastSelectedEcrTab", "");

                if (lastSelectedTab != null && lastSelectedTab != undefined) {
                    tabActivated(lastSelectedTab);

                    $timeout(function () {
                        $scope.$broadcast('app.ecr.tabActivated', {tabId: lastSelectedTab});
                    }, 1000)
                } else if (tabId != null && tabId != undefined) {
                    tabActivated(tabId);

                    $timeout(function () {
                        $scope.$broadcast('app.ecr.tabActivated', {tabId: tabId});
                    }, 1000)
                }
                loadEcrDetails();
                //}
            })();
        }
    }
);