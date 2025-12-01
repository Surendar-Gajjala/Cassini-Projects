define(
    [
        'app/desktop/modules/npr/npr.module',
        'app/assets/bower_components/cassini-platform/app/desktop/modules/common/comments/commentsDirective',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/commonService',
        'app/desktop/modules/npr/details/tabs/basic/nprBasicInfoController',
        'app/desktop/modules/npr/details/tabs/requestedItems/nprRequestedItemsController',
        'app/desktop/modules/npr/details/tabs/timeline/nprTimelineController',
        'app/desktop/modules/npr/details/tabs/files/nprFilesController',
        'app/desktop/modules/npr/details/tabs/workflow/nprWorkflowController',
        'app/assets/bower_components/cassini-platform/app/desktop/directives/freetext/freeTextDirective',
        'app/shared/services/core/nprService'

    ],
    function (module) {
        module.controller('NprDetailsController', NprDetailsController);

        function NprDetailsController($scope, $rootScope, $timeout, $state, $stateParams, $cookies, $window, $application,
                                      NprService, $translate, CommentsService) {
            var vm = this;
            $rootScope.viewInfo.title = "";
            $rootScope.viewInfo.showDetails = false;
            vm.nprId = $stateParams.nprId;
            var parsed = angular.element("<div></div>");
            var basic = parsed.html($translate.instant("DETAILS_TAB_BASIC")).html();
            var requestedItems = parsed.html($translate.instant("REQUESTED_ITEMS")).html();
            var filesTabHeading = parsed.html($translate.instant("DETAILS_TAB_FILES")).html();
            var timelineHeading = parsed.html($translate.instant("TIMELINE")).html();

            var nprSubmitMsg = parsed.html($translate.instant("NPR_SUBMIT_MSG")).html();
            var nprApprovedMsg = parsed.html($translate.instant("NPR_APPROVED_MSG")).html();
            var nprRejectedMsg = parsed.html($translate.instant("NPR_REJECTED_MSG")).html();
            var nprRejectedErrorMsg = parsed.html($translate.instant("NPR_REJECTED_ERROR_MSG")).html();

            var lastSelectedTab = null;
            vm.active = 0;
            $rootScope.clipBoardObjectFiles = [];
            $rootScope.clipBoardObjectFiles = $application.clipboard.files;

            vm.tabs = {
                basic: {
                    id: 'details.basic',
                    heading: basic,
                    template: 'app/desktop/modules/npr/details/tabs/basic/nprBasicInfoView.jsp',
                    index: 0,
                    active: true,
                    activated: true
                },
                requestedItems: {
                    id: 'details.requestedItems',
                    heading: requestedItems,
                    template: 'app/desktop/modules/npr/details/tabs/requestedItems/nprRequestedItemsView.jsp',
                    index: 1,
                    active: true,
                    activated: true
                },
                files: {
                    id: 'details.files',
                    heading: filesTabHeading,
                    template: 'app/desktop/modules/npr/details/tabs/files/nprFilesView.jsp',
                    index: 2,
                    active: false,
                    activated: false
                },
                workflow: {
                    id: 'details.workflow',
                    heading: 'Workflow',
                    template: 'app/desktop/modules/npr/details/tabs/workflow/nprWorkflowView.jsp',
                    index: 3,
                    active: false,
                    activated: false
                },
                timelineHistory: {
                    id: 'details.timelineHistory',
                    heading: timelineHeading,
                    template: 'app/desktop/modules/npr/details/tabs/timeline/nprTimelineView.jsp',
                    index: 4,
                    active: false,
                    activated: false
                }

            };
            var tabId = $stateParams.tab;
            if (tabId == null || tabId === undefined || tabId.trim() === "") {
                tabId = 'details.basic';
                $state.transitionTo('app.nprs.details', {
                    nprId: vm.nprId,
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
                $scope.$broadcast('app.npr.tabActivated', {tabId: $rootScope.selectedTab.id});
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
                    $scope.$broadcast('app.npr.tabActivated', {tabId: 'details.files'});
                }
            }

            vm.onClear = onClear;
            function onClear() {
                $rootScope.freeTextQuerys = null;
                $scope.$broadcast('app.npr.tabActivated', {tabId: 'details.files'});
            }

            function activateTab(tab) {
                for (var t in vm.tabs) {
                    vm.tabs[t].active = (vm.tabs[t].id == tab.id);
                }
            }

            function validateJSON() {
                try {
                    JSON.parse($window.localStorage.getItem("lastSelectedCustomerTab"));
                } catch (e) {
                    return false;
                }
                return true;
            }

            function tabActivated(tabId) {
                $state.transitionTo('app.nprs.details', {
                    nprId: vm.nprId, tab: tabId
                }, {notify: false});
                var tab = getTabById(tabId);
                vm.active = tab.index;
                if (tab != null) {
                    $rootScope.selectedTab = tab;
                    tab.activated = true;
                    tab.active = true;
                    activateTab(tab);
                    $scope.$broadcast('app.npr.tabActivated', {tabId: tabId});

                }
            }

            function getTabById(tabId) {
                var tab = null;
                for (var t in vm.tabs) {
                    if (vm.tabs.hasOwnProperty(t) && vm.tabs[t].id == tabId) {
                        tab = vm.tabs[t];
                    }
                }

                return tab;
            }

            vm.back = back;
            function back() {
                window.history.back();
            }

            $rootScope.loadNpr = loadNpr;
            function loadNpr() {
                NprService.getNpr(vm.nprId).then(
                    function (data) {
                        vm.npr = data;
                        $rootScope.npr = vm.npr;
                        $rootScope.viewInfo.title = parsed.html($translate.instant("NEW_PART_DETAILS")).html();
                        loadCommentsCount();
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                        $rootScope.hideBusyIndicator();
                    }
                )
            }

            function loadCommentsCount() {
                CommentsService.getAllCommentsCount('PLMNPR', vm.nprId).then(
                    function (data) {
                        $rootScope.showComments('PLMNPR', vm.nprId, data);
                        $rootScope.showTags('PLMNPR', vm.nprId, vm.npr.tags.length);
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                    }
                )
            }

            $rootScope.loadNprTabCounts = loadNprTabCounts;
            function loadNprTabCounts() {
                NprService.getNprTabCounts(vm.nprId).then(
                    function (data) {
                        vm.tabCounts = data;
                        $rootScope.reqItems = vm.tabCounts.requestedItems;
                        var requestedItemsTab = document.getElementById("requestedItems");
                        var filesTab = document.getElementById("files");
                        var tmplStr = "<span class='label label-default' style='margin-top:20px;background-color: #e4dddd;height: 20px;margin-left: 5px;'>{0}</span>";
                        if (requestedItemsTab != null) {
                            requestedItemsTab.lastElementChild.innerHTML = vm.tabs.requestedItems.heading +
                                tmplStr.format(vm.tabCounts.requestedItems);
                        }
                        if (filesTab != null) {
                            filesTab.lastElementChild.innerHTML = vm.tabs.files.heading +
                                tmplStr.format(vm.tabCounts.itemFiles);
                        }
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                        $rootScope.hideBusyIndicator();
                    }
                )
            }

            vm.submitNpr = submitNpr;
            function submitNpr() {
                $rootScope.showBusyIndicator($('.view-container'));
                NprService.submitNpr(vm.nprId).then(
                    function (data) {
                        vm.npr = data;
                        $rootScope.npr = data;
                        $rootScope.loadNprBasicDetails();
                        $rootScope.showSuccessMessage(nprSubmitMsg);
                        $rootScope.hideBusyIndicator();
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                        $rootScope.hideBusyIndicator();
                    }
                )
            }

            vm.approveNpr = approveNpr;
            function approveNpr() {
                $rootScope.showBusyIndicator($('.view-container'));
                NprService.approveNpr(vm.nprId).then(
                    function (data) {
                        vm.npr = data;
                        $rootScope.npr = data;
                        $rootScope.loadNprItems();
                        $rootScope.loadNprBasicDetails();
                        $rootScope.showSuccessMessage(nprApprovedMsg);
                        $rootScope.hideBusyIndicator();
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                        $rootScope.hideBusyIndicator();
                    }
                )
            }

            $scope.error = "";
            vm.rejectNpr = rejectNpr;
            function rejectNpr() {
                if ($rootScope.npr.rejectReason == null || $rootScope.npr.rejectReason == '') {
                    $scope.error = nprRejectedErrorMsg;
                } else {
                    $rootScope.showBusyIndicator($('.view-container'));
                    NprService.rejectNpr($rootScope.npr).then(
                        function (data) {
                            vm.npr = data;
                            $rootScope.npr = data;
                            $rootScope.loadNprBasicDetails();
                            $rootScope.showSuccessMessage(nprRejectedMsg);
                            $rootScope.hideBusyIndicator();
                            hideNprDialog();
                        }, function (error) {
                            $rootScope.showErrorMessage(error.message);
                            $rootScope.hideBusyIndicator();
                            hideNprDialog();
                        }
                    )
                }
            }

            vm.addWorkflowTitle = parsed.html($translate.instant("ADD_WORKFLOW")).html();
            vm.changeWorkflowTitle = parsed.html($translate.instant("CHANGE_WORKFLOW")).html();

            vm.changeNprWorkflow = changeNprWorkflow;
            function changeNprWorkflow() {
                $scope.$broadcast('app.change.workflow');
            }

            vm.addNprWorkflow = addNprWorkflow;
            function addNprWorkflow() {
                $scope.$broadcast('app.add.workflow');
            }

            vm.showNprDialog = showNprDialog;
            function showNprDialog() {
                var modal = document.getElementById("npr-modal");
                modal.style.display = "block";
            }

            $scope.hideNprDialog = hideNprDialog;
            function hideNprDialog() {
                var modal = document.getElementById("npr-modal");
                modal.style.display = "none";
                $rootScope.npr.rejectReason = null;
                $scope.error = "";
            }

            (function () {
                if (validateJSON()) {
                    lastSelectedTab = JSON.parse($window.localStorage.getItem("lastSelectedCustomerTab"));
                }

                $window.localStorage.setItem("lastSelectedCustomerTab", "");

                if (lastSelectedTab != null && lastSelectedTab != undefined) {
                    tabActivated(lastSelectedTab);

                    $timeout(function () {
                        $scope.$broadcast('app.npr.tabActivated', {tabId: lastSelectedTab});
                    }, 1000)
                } else if (tabId != null && tabId != undefined) {
                    tabActivated(tabId);

                    $timeout(function () {
                        $scope.$broadcast('app.npr.tabActivated', {tabId: tabId});
                    }, 1000)
                }
                loadNpr();
                loadNprTabCounts();
            })();

        }
    }
);
