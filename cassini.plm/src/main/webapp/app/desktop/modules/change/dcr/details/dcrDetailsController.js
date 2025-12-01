define(
    [
        'app/desktop/modules/change/change.module',
        'app/assets/bower_components/cassini-platform/app/desktop/modules/common/comments/commentsDirective',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/commonService',
        'app/desktop/modules/change/dcr/details/tabs/basic/dcrBasicInfoController',
        'app/desktop/modules/change/dcr/details/tabs/attributes/dcrAttributesController',
        'app/desktop/modules/change/dcr/details/tabs/workflow/dcrWorkflowController',
        'app/desktop/modules/change/dcr/details/tabs/affectedItem/dcrAffectedItemsController',
        'app/desktop/modules/change/dcr/details/tabs/relatedItem/dcrRelatedItemsController',
        'app/desktop/modules/change/dcr/details/tabs/files/dcrFilesController',
        'app/desktop/modules/change/dcr/details/tabs/timeLine/dcrTimeLineController',
        'app/assets/bower_components/cassini-platform/app/desktop/directives/freetext/freeTextDirective',
        'app/shared/services/core/dcrService',
        'app/desktop/directives/plugin-directive/pluginTabsDirective'

    ],
    function (module) {
        module.controller('DCRDetailsController', DCRDetailsController);

        function DCRDetailsController($scope, $rootScope, $timeout, $state, $stateParams, $cookies, $window, $application,
                                      ECOService, DCRService, $translate, CommentsService) {
            var vm = this;

            $rootScope.viewInfo.icon = "fa fa-exchange";
            $rootScope.viewInfo.showDetails = true;
            $rootScope.viewInfo.title = "";

            vm.dcrId = $stateParams.dcrId;
            vm.back = back;

            var lastSelectedTab = null;
            vm.active = 0;

            var parsed = angular.element("<div></div>");
            vm.detailsShareTitle = parsed.html($translate.instant("DETAILS_SHARE_TITLE")).html();
            vm.refreshTitle = parsed.html($translate.instant("CLICK_TO_REFRESH")).html();
            var AffectedItems = parsed.html($translate.instant("ECO_AFFECTED_ITEMS")).html();
            var RelatedItems = parsed.html($translate.instant("RELATED_ITEMS")).html();
            var filesTabHeading = parsed.html($translate.instant("DETAILS_TAB_FILES")).html();
            var files = parsed.html($translate.instant("FILES")).html();
            var worlflow = parsed.html($translate.instant("WORKFLOW")).html();
            var attributes = parsed.html($translate.instant("DETAILS_TAB_ATTRIBUTES")).html();
            var basic = parsed.html($translate.instant("DETAILS_TAB_BASIC")).html();
            var timeline = parsed.html($translate.instant("TIMELINE")).html();
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
                    heading: basic,
                    template: 'app/desktop/modules/change/dcr/details/tabs/basic/dcrBasicInfoView.jsp',
                    active: true,
                    activated: true
                }/*,
                 attributes: {
                 id: 'details.attribute',
                 index: 1,
                 heading: attributes,
                 template: 'app/desktop/modules/change/dcr/details/tabs/attributes/dcrAttributesView.jsp',
                 active: false,
                 activated: false
                 }*/,
                affectedItems: {
                    id: 'details.affectedItems',
                    index: 1,
                    heading: AffectedItems,
                    template: 'app/desktop/modules/change/dcr/details/tabs/affectedItem/dcrAffectedItemsView.jsp',
                    active: false,
                    activated: false
                },
                relatedItems: {
                    id: 'details.relatedItems',
                    index: 2,
                    heading: RelatedItems,
                    template: 'app/desktop/modules/change/dcr/details/tabs/relatedItem/dcrRelatedItemsView.jsp',
                    active: false,
                    activated: false
                },
                workflow: {
                    id: 'details.workflow',
                    index: 3,
                    heading: worlflow,
                    template: 'app/desktop/modules/change/dcr/details/tabs/workflow/dcrWorkflowView.jsp',
                    active: false,
                    activated: false
                },
                files: {
                    id: 'details.files',
                    index: 4,
                    heading: files,
                    template: 'app/desktop/modules/change/dcr/details/tabs/files/dcrFilesView.jsp',
                    active: false,
                    activated: false
                },
                timeLine: {
                    id: 'details.timeLine',
                    index: 5,
                    heading: timeline,
                    template: 'app/desktop/modules/change/dcr/details/tabs/timeLine/dcrTimeLineView.jsp',
                    active: false,
                    activated: false
                }
            };

            vm.changeWorkflow = changeWorkflow;
            function changeWorkflow() {
                $scope.$broadcast('app.change.workflow');
            }

            var tabId = $stateParams.tab;
            if (tabId == null || tabId === undefined || tabId.trim() === "") {
                tabId = 'details.basic';
                $state.transitionTo('app.changes.dcr.details', {
                    dcrId: vm.dcrId,
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
                $scope.$broadcast('app.dcr.tabActivated', {tabId: $rootScope.selectedTab.id});
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
                    JSON.parse($window.localStorage.getItem("lastSelectedDcrTab"));
                } catch (e) {
                    return false;
                }
                return true;
            }

            function tabActivated(tabId) {
                $state.transitionTo('app.changes.dcr.details', {
                    dcrId: vm.dcrId, tab: tabId
                }, {notify: false});
                var tab = getTabById(tabId);
                vm.active = tab.index;
                if (tab != null) {
                    $rootScope.selectedTab = tab;
                    tab.activated = true;
                    tab.active = true;
                    activateTab(tab);
                    $scope.$broadcast('app.dcr.tabActivated', {tabId: tabId});

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

            $rootScope.loadDcr = loadDcr;
            function loadDcr() {
                DCRService.getDCR(vm.dcrId).then(
                    function (data) {
                        vm.dcr = data;
                        $rootScope.dcr = data;
                        $timeout(function () {
                            $scope.$broadcast('app.object.plugins.tabActivated', {});
                        }, 500);
                        vm.dcrStatus = vm.dcr.isApproved;
                        if (vm.dcr.statusType == 'REJECTED') {
                            vm.dcrStatus = true;
                        }
                        $rootScope.startDcrWorkflow = vm.dcr.startWorkflow;
                        // $rootScope.viewInfo.title = "DCR Details";
                        // $rootScope.viewInfo.description = "Number: {0}, Status: {1}".
                        //     format(vm.dcr.crNumber, vm.dcr.status);
                        $rootScope.viewInfo.title = "<div class='item-number'>" +
                        "{0}</div>".
                            format(vm.dcr.crNumber);
                       $rootScope.viewInfo.description = vm.dcr.title;
                        loadDCRCounts();
                        loadCommentsCount();
                        $scope.$evalAsync();
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                    }
                )
            }

            $rootScope.loadDCRCounts = loadDCRCounts;

            function loadDCRCounts() {
                DCRService.getDCRCounts(vm.dcrId).then(
                    function (data) {
                        vm.dcrCounts = data;
                        $rootScope.affectedItems = vm.dcrCounts.affectedItems;
                        var filesTab = document.getElementById("files");
                        var affectedItems = document.getElementById("affectedItems");
                        var relatedItems = document.getElementById("relatedItems");
                        filesTab.lastElementChild.innerHTML = vm.tabs.files.heading +
                            "<span class='label label-default' style='margin-top:20px;background-color: #e4dddd;height: 20px;margin-left: 5px;'>{0}</span>".format(vm.dcrCounts.files);
                        affectedItems.lastElementChild.innerHTML = vm.tabs.affectedItems.heading +
                            "<span class='label label-default' style='margin-top:20px;background-color: #e4dddd;height: 20px;margin-left: 5px;'>{0}</span>".format(vm.dcrCounts.affectedItems);
                        relatedItems.lastElementChild.innerHTML = vm.tabs.relatedItems.heading +
                            "<span class='label label-default' style='margin-top:20px;background-color: #e4dddd;height: 20px;margin-left: 5px;'>{0}</span>".format(vm.dcrCounts.relatedItems);

                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                    });
            }

            vm.onClear = onClear;
            function onClear() {
                $rootScope.freeTextQuerys = null;
                $scope.$broadcast('app.dcr.tabActivated', {tabId: 'details.files'});
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
                    $scope.$broadcast('app.dcr.tabActivated', {tabId: 'details.files'});
                }
            }


            function loadCommentsCount() {
                CommentsService.getAllCommentsCount('CHANGE', vm.dcrId).then(
                    function (data) {
                        $rootScope.showComments('CHANGE', vm.dcrId, data);
                        $rootScope.showTags('CHANGE', vm.dcrId, vm.dcr.tags.length);
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                    }
                )
            }

            (function () {
                //if ($application.homeLoaded == true) {
                if (validateJSON()) {
                    lastSelectedTab = JSON.parse($window.localStorage.getItem("lastSelectedDcrTab"));
                }

                $window.localStorage.setItem("lastSelectedDcrTab", "");

                if (lastSelectedTab != null && lastSelectedTab != undefined) {
                    tabActivated(lastSelectedTab);

                    $timeout(function () {
                        $scope.$broadcast('app.dcr.tabActivated', {tabId: lastSelectedTab});
                    }, 1000)
                } else if (tabId != null && tabId != undefined) {
                    tabActivated(tabId);

                    $timeout(function () {
                        $scope.$broadcast('app.dcr.tabActivated', {tabId: tabId});
                    }, 1000)
                }
                loadDcr();
                //}
            })();
        }
    }
);