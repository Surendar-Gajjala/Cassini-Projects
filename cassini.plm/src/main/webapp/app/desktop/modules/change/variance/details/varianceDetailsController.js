define(
    [
        'app/desktop/modules/change/change.module',
        'app/assets/bower_components/cassini-platform/app/desktop/modules/common/comments/commentsDirective',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/commonService',
        'app/desktop/modules/change/variance/details/tabs/basic/varianceBasicController',
        'app/desktop/modules/change/variance/details/tabs/attributes/varianceAttributesController',
        'app/desktop/modules/change/variance/details/tabs/files/varianceFilesController',
        'app/desktop/modules/change/variance/details/tabs/items/affected/varianceAffectedItemsController',
        'app/desktop/modules/change/variance/details/tabs/items/related/varianceRelatedItemsController',
        'app/desktop/modules/change/variance/details/tabs/workflow/varianceWorkflowController',
        'app/desktop/modules/change/variance/details/tabs/history/varianceTimelineController',
        'app/shared/services/core/varianceService',
        'app/desktop/directives/plugin-directive/pluginTabsDirective'
    ],
    function (module) {
        module.controller('VarianceDetailsController', VarianceDetailsController);

        function VarianceDetailsController($scope, $rootScope, $timeout, $sce, $state, $stateParams, VarianceService, $cookies, $window, $application, $translate, CommentsService) {
            var vm = this;

            $rootScope.viewInfo.icon = "fa flaticon-contract11";
            $rootScope.viewInfo.title = "Variance Details";
            $rootScope.viewInfo.showDetails = false;

            vm.varianceId = $stateParams.varianceId;
            vm.tab = $stateParams.tab;
            vm.back = back;
            vm.onAddFiles = onAddFiles;
            vm.showMultiple = showMultiple;
            vm.showSingle = showSingle;
            vm.showRelatedSingle = showRelatedSingle;
            vm.showRelatedMultiple = showRelatedMultiple;
            vm.freeTextSearch = freeTextSearch;
            vm.downloadFilesAsZip = downloadFilesAsZip;
            vm.onClear = onClear;
            vm.selectedTabIndex = (vm.tab != null && vm.tab != undefined && vm.tab != "") ? -1 : 0;
            var searchMode = null;
            var freeTextQuery = null;

            var lastSelectedTab = null;
            vm.active = 0;

            var parsed = angular.element("<div></div>");
            vm.detailsShareTitle = parsed.html($translate.instant("DETAILS_SHARE_TITLE")).html();
            vm.refreshTitle = parsed.html($translate.instant("CLICK_TO_REFRESH")).html();
            var AffectedItems = parsed.html($translate.instant("ECO_AFFECTED_ITEMS")).html();
            var RelatedItems = parsed.html($translate.instant("RELATED_ITEMS")).html();
            var filesTabHeading = parsed.html($translate.instant("DETAILS_TAB_FILES")).html();
            var basic = parsed.html($translate.instant("DETAILS_TAB_BASIC")).html();
            var files = parsed.html($translate.instant("FILES")).html();
            var workflow = parsed.html($translate.instant("WORKFLOW")).html();
            var timeline = parsed.html($translate.instant("TIMELINE")).html();
            var attributes = parsed.html($translate.instant("DETAILS_TAB_ATTRIBUTES")).html();
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
                    template: 'app/desktop/modules/change/variance/details/tabs/basic/varianceBasicInfoView.jsp',
                    active: true,
                    activated: true
                }/*,
                 attributes: {
                 id: 'details.attribute',
                 index: 1,
                 heading: attributes,
                 template: 'app/desktop/modules/change/variance/details/tabs/attributes/varianceAttributesView.jsp',
                 active: false,
                 activated: false
                 }*/,
                affecteditems: {
                    id: 'details.affecteditems',
                    index: 1,
                    heading: AffectedItems,
                    template: 'app/desktop/modules/change/variance/details/tabs/' +
                    'items/affected/varianceAffectedItemsView.jsp',
                    active: false,
                    activated: false
                },
                relateditems: {
                    id: 'details.relateditems',
                    index: 2,
                    heading: RelatedItems,
                    template: 'app/desktop/modules/change/variance/details/tabs/' +
                    'items/related/varianceRelatedItemsView.jsp',
                    active: false,
                    activated: false
                },
                workflow: {
                    id: 'details.workflow',
                    index: 3,
                    heading: workflow,
                    template: 'app/desktop/modules/change/variance/details/tabs/workflow/varianceWorkflowView.jsp',
                    active: false,
                    activated: false
                },
                files: {
                    id: 'details.files',
                    index: 4,
                    heading: files,
                    template: 'app/desktop/modules/change/variance/details/tabs/files/varianceFilesView.jsp',
                    active: false,
                    activated: false
                },

                history: {
                    id: 'details.timeLine',
                    index: 5,
                    heading: timeline,
                    template: 'app/desktop/modules/change/variance/details/tabs/history/varianceTimelineView.jsp',
                    active: false,
                    activated: false
                }
            };

            var tabId = $stateParams.tab;
            if (tabId == null || tabId === undefined || tabId.trim() === "") {
                tabId = 'details.basic';
                $state.transitionTo('app.changes.variance.details', {
                    varianceId: vm.varianceId,
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

            vm.broadcast = broadcast;
            vm.tabActivated = tabActivated;
            vm.refreshDetails = refreshDetails;

            $rootScope.loadVarianceCounts = loadVarianceCounts;
            function loadVarianceCounts() {
                VarianceService.getVarianceCounts(vm.varianceId).then(
                    function (data) {
                        vm.varianceCounts = data;
                        var filesTab = document.getElementById("files");
                        var affectedItems = document.getElementById("affectedItems");
                        var relatedItems = document.getElementById("relatedItems");
                        $rootScope.affectedItems = vm.varianceCounts.affectedItems;

                        filesTab.lastElementChild.innerHTML = vm.tabs.files.heading +
                            "<span class='label label-default' style='margin-top:20px;background-color: #e4dddd;height: 20px;margin-left: 5px;'>{0}</span>".format(vm.varianceCounts.files);
                        affectedItems.lastElementChild.innerHTML = vm.tabs.affecteditems.heading +
                            "<span class='label label-default' style='margin-top:20px;background-color: #e4dddd;height: 20px;margin-left: 5px;'>{0}</span>".format(vm.varianceCounts.affectedItems);
                        relatedItems.lastElementChild.innerHTML = vm.tabs.relateditems.heading +
                            "<span class='label label-default' style='margin-top:20px;background-color: #e4dddd;height: 20px;margin-left: 5px;'>{0}</span>".format(vm.varianceCounts.relatedItems);

                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                    }
                )
            }

            vm.changeWorkflow = changeWorkflow;
            function changeWorkflow() {
                $scope.$broadcast('app.change.workflow');
            }

            $rootScope.loadVariance = loadVariance;
            function loadVariance() {
                VarianceService.getVariance(vm.varianceId).then(
                    function (data) {
                        vm.variance = data;
                        $rootScope.variance = vm.variance;
                        if (vm.variance.varianceType == "DEVIATION") $rootScope.varianceType = "Deviation";
                        if (vm.variance.varianceType == "WAIVER") $rootScope.varianceType = "Waiver";
                        vm.loading = false;
                        $rootScope.viewInfo.title = "<div class='item-number'>" +
                            "{0}</div>".format(vm.variance.varianceNumber);


                        loadVarianceCounts();
                        loadCommentsCount();
                        $scope.$evalAsync();
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                    }
                )
            }

            function refreshDetails() {
                $scope.$broadcast('app.variance.tabactivated', {tabId: $rootScope.selectedTab.id});
            }

            function onClear() {
                $scope.$broadcast('app.variance.tabactivated', {tabId: 'details.files'});
            }

            function freeTextSearch(freeText) {
                searchMode = "freetext";
                freeTextQuery = freeText;
                if (freeText != null && freeText != undefined && freeText.trim() != "") {
                    $scope.$broadcast('app.changeFile.files.search', {name: freeText});
                }
                else {
                    $scope.$broadcast('app.variance.tabactivated', {tabId: 'details.files'});
                }
            }

            function broadcast(event) {
                $scope.$broadcast(event);
            }

            function onAddFiles() {
                $scope.$broadcast('app.variance.details.onaddfiles');
            }

            function showMultiple() {
                $scope.$broadcast('app.variance.details.multiple');
            }

            function showSingle() {
                $scope.$broadcast('app.variance.details.single');
            }

            function showRelatedMultiple() {
                $scope.$broadcast('app.variance.details.related.multiple');
            }

            function showRelatedSingle() {
                $scope.$broadcast('app.variance.details.related.single');
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
                    JSON.parse($window.localStorage.getItem("lastSelectedVarianceTab"));
                } catch (e) {
                    return false;
                }
                return true;
            }

            function tabActivated(tabId) {
                $state.transitionTo('app.changes.variance.details', {
                    varianceId: vm.varianceId, tab: tabId
                }, {notify: false});
                var tab = getTabById(tabId);
                vm.active = tab.index;
                if (tab != null) {
                    $rootScope.selectedTab = tab;
                    tab.activated = true;
                    tab.active = true;
                    activateTab(tab);
                    $scope.$broadcast('app.variance.tabactivated', {tabId: tabId});

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

            function loadCommentsCount() {
                CommentsService.getAllCommentsCount('CHANGE', vm.varianceId).then(
                    function (data) {
                        $rootScope.showComments('CHANGE', vm.varianceId, data);
                        $rootScope.showTags('CHANGE', vm.varianceId, vm.variance.tags.length);
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                    }
                )
            }

            function downloadFilesAsZip() {
                $rootScope.showBusyIndicator($('.view-container'));
                var url = "{0}//{1}/api/cm/variance/{2}/files/zip".
                    format(window.location.protocol, window.location.host, vm.ecoId);

                //launchUrl(url);
                window.open(url);
                $rootScope.hideBusyIndicator();
            }

            (function () {
                //if ($application.homeLoaded == true) {
                loadVariance();
                loadVarianceCounts();
                if (validateJSON()) {
                    lastSelectedTab = JSON.parse($window.localStorage.getItem("lastSelectedVarianceTab"));
                }
                $window.localStorage.setItem("lastSelectedVarianceTab", "");
                if (lastSelectedTab != null && lastSelectedTab != undefined) {
                    tabActivated(lastSelectedTab);
                    $timeout(function () {
                        $scope.$broadcast('app.variance.tabactivated', {tabId: lastSelectedTab});
                    }, 1000)
                } else if (tabId != null && tabId != undefined) {
                    tabActivated(tabId);
                    $timeout(function () {
                        $scope.$broadcast('app.variance.tabactivated', {tabId: tabId});
                    }, 1000)
                }
                //}
            })();
        }
    }
);