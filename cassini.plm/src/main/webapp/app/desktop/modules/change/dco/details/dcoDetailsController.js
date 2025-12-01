define(
    [
        'app/desktop/modules/change/change.module',
        'app/assets/bower_components/cassini-platform/app/desktop/modules/common/comments/commentsDirective',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/commonService',
        'app/desktop/modules/change/dco/details/tabs/basic/dcoBasicInfoController',
        'app/desktop/modules/change/dco/details/tabs/attributes/dcoAttributesController',
        'app/desktop/modules/change/dco/details/tabs/workflow/dcoWorkflowController',
        'app/desktop/modules/change/dco/details/tabs/affectedItems/dcoAffectedItemsController',
        'app/desktop/modules/change/dco/details/tabs/changeRequests/dcoChangeRequestController',
        'app/desktop/modules/change/dco/details/tabs/relatedItems/dcoRelatedItemsController',
        'app/desktop/modules/change/dco/details/tabs/timeline/dcoTimelineController',
        'app/desktop/modules/change/dco/details/tabs/files/dcoFilesController',
        'app/assets/bower_components/cassini-platform/app/desktop/directives/freetext/freeTextDirective',
        'app/shared/services/core/dcoService',
        'app/desktop/directives/plugin-directive/pluginTabsDirective'
    ],
    function (module) {
        module.controller('DCODetailsController', DCODetailsController);

        function DCODetailsController($scope, $rootScope, $timeout, $state, $stateParams, $cookies, $window, $application,
                                      ECOService, $translate, DCOService, CommentsService) {
            var vm = this;

            $rootScope.viewInfo.icon = "fa fa-exchange";
            $rootScope.viewInfo.title = "";
            $rootScope.viewInfo.showDetails = true;

            vm.dcoId = $stateParams.dcoId;
            vm.back = back;

            var lastSelectedTab = null;
            vm.active = 0;
            $rootScope.showCopyEcoFilesToClipBoard = false;
            $rootScope.clipBoardEcoFiles = [];
            $rootScope.clipBoardEcoFiles = $application.clipboard.files;
            $rootScope.loadDco = loadDco;
            var parsed = angular.element("<div></div>");
            vm.detailsShareTitle = parsed.html($translate.instant("DETAILS_SHARE_TITLE")).html();
            vm.refreshTitle = parsed.html($translate.instant("CLICK_TO_REFRESH")).html();
            var AffectedItems = parsed.html($translate.instant("ECO_AFFECTED_ITEMS")).html();
            var RelatedItems = parsed.html($translate.instant("RELATED_ITEMS")).html();
            var ChangeRequests = parsed.html($translate.instant("CHANGE_REQUESTS")).html();
            var Attributes = parsed.html($translate.instant("ATTRIBUTES")).html();
            var Basic = parsed.html($translate.instant("DETAILS_TAB_BASIC")).html();
            var Files = parsed.html($translate.instant("FILES")).html();
            var Workflow = parsed.html($translate.instant("WORKFLOW")).html();
            var Timeline = parsed.html($translate.instant("TIMELINE")).html();
            var filesTabHeading = parsed.html($translate.instant("DETAILS_TAB_FILES")).html();
            vm.addItems = parsed.html($translate.instant("ADD_ITEM")).html();
            vm.changeWorkflowTitle = parsed.html($translate.instant("CHANGE_WORKFLOW")).html();
            $rootScope.clipBoardObjectFiles = [];
            $rootScope.clipBoardObjectFiles = $application.clipboard.files;
            vm.tabId = $stateParams.tab;
            vm.customTabs = [];


            vm.tabs = {
                basic: {
                    id: 'details.basic',
                    index: 0,
                    heading: Basic,
                    template: 'app/desktop/modules/change/dco/details/tabs/basic/dcoBasicInfoView.jsp',
                    active: true,
                    activated: true
                }/*,
                 attributes: {
                 id: 'details.attribute',
                 index: 1,
                 heading: Attributes,
                 template: 'app/desktop/modules/change/dco/details/tabs/attributes/dcoAttributesView.jsp',
                 active: false,
                 activated: false
                 }*/,
                changeRequest: {
                    id: 'details.changeRequest',
                    index: 1,
                    heading: ChangeRequests,
                    template: 'app/desktop/modules/change/dco/details/tabs/changeRequests/dcoChangeRequestView.jsp',
                    active: false,
                    activated: false
                },
                affectedItems: {
                    id: 'details.affectedItems',
                    index: 2,
                    heading: AffectedItems,
                    template: 'app/desktop/modules/change/dco/details/tabs/affectedItems/dcoAffectedItemsView.jsp',
                    active: false,
                    activated: false
                },
                relatedItems: {
                    id: 'details.relatedItems',
                    index: 3,
                    heading: RelatedItems,
                    template: 'app/desktop/modules/change/dco/details/tabs/relatedItems/dcoRelatedItemsView.jsp',
                    active: false,
                    activated: false
                },
                workflow: {
                    id: 'details.workflow',
                    index: 4,
                    heading: Workflow,
                    template: 'app/desktop/modules/change/dco/details/tabs/workflow/dcoWorkflowView.jsp',
                    active: false,
                    activated: false
                },
                files: {
                    id: 'details.files',
                    index: 5,
                    heading: Files,
                    template: 'app/desktop/modules/change/dco/details/tabs/files/dcoFilesView.jsp',
                    active: false,
                    activated: false
                },
                timeLine: {
                    id: 'details.timeLine',
                    index: 6,
                    heading: Timeline,
                    template: 'app/desktop/modules/change/dco/details/tabs/timeline/dcoTimelineView.jsp',
                    active: false,
                    activated: false
                }
            };

            var tabId = $stateParams.tab;
            if (tabId == null || tabId === undefined || tabId.trim() === "") {
                tabId = 'details.basic';
                $state.transitionTo('app.changes.dco.details', {
                    dcoId: vm.dcoId,
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
                $scope.$broadcast('app.dco.tabActivated', {tabId: $rootScope.selectedTab.id});
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
                    JSON.parse($window.localStorage.getItem("lastSelectedDcoTab"));
                } catch (e) {
                    return false;
                }
                return true;
            }

            function tabActivated(tabId) {
                $state.transitionTo('app.changes.dco.details', {
                    dcoId: vm.dcoId, tab: tabId
                }, {notify: false});
                var tab = getTabById(tabId);
                vm.active = tab.index;
                if (tab != null) {
                    $rootScope.selectedTab = tab;
                    tab.activated = true;
                    tab.active = true;
                    activateTab(tab);
                    $scope.$broadcast('app.dco.tabActivated', {tabId: tabId});

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

            function loadDco(flag) {
                DCOService.getDCO(vm.dcoId).then(
                    function (data) {
                        vm.dco = data;
                        $rootScope.dco = data;
                        if(flag){
                            $timeout(function () {
                                $scope.$broadcast('app.object.plugins.tabActivated', {});
                            }, 500);
                        }

                        $rootScope.startDcoWorkflow = vm.dco.startWorkflow;
                        // $rootScope.viewInfo.title = "DCO Details";
                        // $rootScope.viewInfo.description = "Number: {0}, Status: {1}".
                        //     format(vm.dco.dcoNumber, vm.dco.status);
                        $rootScope.viewInfo.title = "<div class='item-number'>" +
                        "{0}</div>".
                            format(vm.dco.dcoNumber);
                       $rootScope.viewInfo.description = vm.dco.title;
                        loadDCOCounts();
                        loadCommentsCount();
                        $scope.$evalAsync();
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                    }
                )
            }


            $rootScope.loadDCOCounts = loadDCOCounts;

            function loadDCOCounts() {
                DCOService.getDCOCounts(vm.dcoId).then(
                    function (data) {
                        vm.dcoCounts = data;
                        $rootScope.affectedItems = vm.dcoCounts.affectedItems;
                        var filesTab = document.getElementById("files");
                        var affectedItems = document.getElementById("affectedItems");
                        var changeRequests = document.getElementById("changeRequests");
                        var relatedItems = document.getElementById("relatedItems");
                        filesTab.lastElementChild.innerHTML = vm.tabs.files.heading +
                            "<span class='label label-default' style='margin-top:20px;background-color: #e4dddd;height: 20px;margin-left: 5px;'>{0}</span>".format(vm.dcoCounts.files);
                        affectedItems.lastElementChild.innerHTML = vm.tabs.affectedItems.heading +
                            "<span class='label label-default' style='margin-top:20px;background-color: #e4dddd;height: 20px;margin-left: 5px;'>{0}</span>".format(vm.dcoCounts.affectedItems);
                        changeRequests.lastElementChild.innerHTML = vm.tabs.changeRequest.heading +
                            "<span class='label label-default' style='margin-top:20px;background-color: #e4dddd;height: 20px;margin-left: 5px;'>{0}</span>".format(vm.dcoCounts.changeRequests);
                        relatedItems.lastElementChild.innerHTML = vm.tabs.relatedItems.heading +
                            "<span class='label label-default' style='margin-top:20px;background-color: #e4dddd;height: 20px;margin-left: 5px;'>{0}</span>".format(vm.dcoCounts.relatedItems);

                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                        $rootScope.hideBusyIndicator();
                    });
            }

            vm.onClear = onClear;
            function onClear() {
                $rootScope.freeTextQuerys = null;
                $scope.$broadcast('app.dco.tabActivated', {tabId: 'details.files'});
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
                    $scope.$broadcast('app.dco.tabActivated', {tabId: 'details.files'});
                }
            }

            function loadCommentsCount() {
                CommentsService.getAllCommentsCount('CHANGE', vm.dcoId).then(
                    function (data) {
                        $rootScope.showComments('CHANGE', vm.dcoId, data);
                        $rootScope.showTags('CHANGE', vm.dcoId, vm.dco.tags.length);
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                    }
                )
            }

            (function () {
                //if ($application.homeLoaded == true) {
                if (validateJSON()) {
                    lastSelectedTab = JSON.parse($window.localStorage.getItem("lastSelectedDcoTab"));
                }

                $window.localStorage.setItem("lastSelectedDcoTab", "");

                if (lastSelectedTab != null && lastSelectedTab != undefined) {
                    tabActivated(lastSelectedTab);

                    $timeout(function () {
                        $scope.$broadcast('app.dco.tabActivated', {tabId: lastSelectedTab});
                    }, 1000)
                } else if (tabId != null && tabId != undefined) {
                    tabActivated(tabId);

                    $timeout(function () {
                        $scope.$broadcast('app.dco.tabActivated', {tabId: tabId});
                    }, 1000)
                }
                loadDco(true);
                //}
            })();
        }
    }
);