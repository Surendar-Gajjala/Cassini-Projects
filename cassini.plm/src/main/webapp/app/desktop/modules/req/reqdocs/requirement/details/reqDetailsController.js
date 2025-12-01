define(
    [
        'app/desktop/modules/req/reqdocs/requirement/requirement.module',
        'app/assets/bower_components/cassini-platform/app/desktop/modules/common/comments/commentsDirective',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/commonService',
        'app/desktop/modules/req/reqdocs/requirement/details/tabs/basic/reqBasicInfoController',
        'app/desktop/modules/req/reqdocs/requirement/details/tabs/reviewers/reqReviewersController',
        'app/desktop/modules/req/reqdocs/requirement/details/tabs/timeline/reqTimelineController',
        'app/desktop/modules/req/reqdocs/requirement/details/tabs/items/reqItemsController',
        'app/desktop/modules/req/reqdocs/requirement/details/tabs/files/reqFilesController',
        'app/desktop/modules/req/reqdocs/requirement/details/tabs/workflow/requirementWorkflowController',
        'app/assets/bower_components/cassini-platform/app/desktop/directives/freetext/freeTextDirective',
        'app/shared/services/core/reqDocumentService',
        'app/shared/services/core/requirementService',
        'app/desktop/directives/plugin-directive/pluginTabsDirective'

    ],
    function (module) {
        module.controller('ReqDetailsController', ReqDetailsController);

        function ReqDetailsController($scope, $rootScope, $timeout, $state, $stateParams, $cookies, $window, $application,
                                      $translate, ReqDocumentService, RequirementService, CommentsService) {
            var vm = this;
            $rootScope.viewInfo.showDetails = true;
            vm.reqId = $stateParams.requirementId;
            var parsed = angular.element("<div></div>");
            var basic = parsed.html($translate.instant("DETAILS_TAB_BASIC")).html();
            var reviewersAndApprovers = parsed.html($translate.instant("REVIEWERS_AND_APPROVERS")).html();
            var filesTabHeading = parsed.html($translate.instant("DETAILS_TAB_FILES")).html();
            var itemsTabHeading = parsed.html($translate.instant("ITEMS")).html();
            var timelineHeading = parsed.html($translate.instant("TIMELINE")).html();
            var notesErrorMsg = parsed.html($translate.instant("ENTER_NOTES_VALIDATION")).html();
            var workflowApprMsg = parsed.html($translate.instant("PLEASE_FINISH_WORKFLOW")).html();
            $scope.requirementApproverTitle = parsed.html($translate.instant("REQUIREMENT_APPROVER")).html();
            $scope.requirementRejectTitle = parsed.html($translate.instant("REQUIREMENT_REJECT")).html();
            $scope.requirementReviewTitle = parsed.html($translate.instant("REQUIREMENT_REVIEW_TITLE")).html();
            var requirementApproverMsg = parsed.html($translate.instant("REQUIREMENT_APPROVER_MSG")).html();
            var requirementRejectMsg = parsed.html($translate.instant("REQUIREMENT_REJECT_MSG")).html();
            var requirementReviewMsg = parsed.html($translate.instant("REQUIREMENT_REVIEW_MSG")).html();
            $scope.requirementReviseTitle = parsed.html($translate.instant("REQUIREMENT_REVISE_TITLE_MSG")).html();
            var requirementReviseMsg = parsed.html($translate.instant("REQUIREMENT_REVISE_SUCCESS_MSG")).html();
            var lastSelectedTab = null;
            vm.active = 0;
            $rootScope.clipBoardObjectFiles = [];
            $rootScope.clipBoardObjectFiles = $application.clipboard.files;
            $scope.reqVersion = null;
            vm.tabId = $stateParams.tab;
            vm.customTabs = [];

            vm.tabs = {
                basic: {
                    id: 'details.basic',
                    heading: basic,
                    template: 'app/desktop/modules/req/reqdocs/requirement/details/tabs/basic/reqBasicInfoView.jsp',
                    index: 0,
                    active: true,
                    activated: true
                },
                reviewers: {
                    id: 'details.reviewers',
                    heading: reviewersAndApprovers,
                    template: 'app/desktop/modules/req/reqdocs/requirement/details/tabs/reviewers/reqReviewersView.jsp',
                    index: 1,
                    active: false,
                    activated: false
                },
                items: {
                    id: 'details.items',
                    heading: itemsTabHeading,
                    template: 'app/desktop/modules/req/reqdocs/requirement/details/tabs/items/reqItemsView.jsp',
                    index: 2,
                    active: false,
                    activated: false
                },
                files: {
                    id: 'details.files',
                    heading: filesTabHeading,
                    template: 'app/desktop/modules/req/reqdocs/requirement/details/tabs/files/reqFilesView.jsp',
                    index: 3,
                    active: false,
                    activated: false
                },
                workflow: {
                    id: 'details.workflow',
                    heading: 'Workflow',
                    template: 'app/desktop/modules/req/reqdocs/requirement/details/tabs/workflow/requirementWorkflowView.jsp',
                    index: 4,
                    active: false,
                    activated: false
                },
                timelineHistory: {
                    id: 'details.timelineHistory',
                    heading: timelineHeading,
                    template: 'app/desktop/modules/req/reqdocs/requirement/details/tabs/timeline/reqTimelineView.jsp',
                    index: 5,
                    active: false,
                    activated: false
                }

            };
            var tabId = $stateParams.tab;
            if (tabId == null || tabId === undefined || tabId.trim() === "") {
                tabId = 'details.basic';
                $state.transitionTo('app.req.requirements.details', {
                    requirementId: vm.reqId,
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
                $scope.$broadcast('app.req.requirement.tabActivated', {tabId: $rootScope.selectedTab.id});
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
                    JSON.parse($window.localStorage.getItem("lastSelectedSubstanceTab"));
                } catch (e) {
                    return false;
                }
                return true;
            }

            function tabActivated(tabId) {
                $state.transitionTo('app.req.requirements.details', {
                    requirementId: vm.reqId, tab: tabId
                }, {notify: false});
                var tab = getTabById(tabId);
                vm.active = tab.index;
                if (tab != null) {
                    $rootScope.selectedTab = tab;
                    tab.activated = true;
                    tab.active = true;
                    activateTab(tab);
                    $scope.$broadcast('app.req.requirement.tabActivated', {tabId: tabId});

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

            vm.back = back;
            function back() {
                window.history.back();
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
                    $scope.$broadcast('app.req.requirement.tabActivated', {tabId: 'details.files'});
                }
            }

            vm.onClear = onClear;
            function onClear() {
                $rootScope.freeTextQuerys = null;
                $scope.$broadcast('app.req.requirement.tabActivated', {tabId: 'details.files'});
            }

            $rootScope.loadRequirement = loadRequirement;
            function loadRequirement() {
                vm.loading = true;
                RequirementService.getRequirementVersionObject(vm.reqId).then(
                    function (data) {
                        vm.reqChild = data;
                        vm.reqVersion = data.requirementVersion;
                        $rootScope.reqVersion = vm.reqVersion;
                        $rootScope.reqquirementChild = vm.reqChild;
                        $scope.reqVersion = vm.reqVersion;
                        $rootScope.viewInfo.title = "<div class='item-number'>" +
                            "{0}</div> <span class='item-rev'>Version: {1}</span> <span class='item-rev'>Status: {2}</span>".
                                format(vm.reqVersion.master.number, vm.reqVersion.version,
                                vm.reqVersion.lifeCyclePhase.phase);
                        $rootScope.viewInfo.description = vm.reqVersion.name;
                        setLifecycles();
                        loadCommentsCount();
                        vm.lastLifecyclePhase = vm.reqVersion.master.type.lifecycle.phases[vm.reqVersion.master.type.lifecycle.phases.length - 1];
                        vm.firstLifecyclePhase = vm.reqVersion.master.type.lifecycle.phases[0];
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                        $rootScope.hideBusyIndicator();
                    }
                );

            }

            function setLifecycles() {
                var phases = [];
                var currentPhase = vm.reqVersion.lifeCyclePhase.phase;
                var currentLifeCyclePhase = vm.reqVersion.lifeCyclePhase;
                $rootScope.lifeCycleStatus = vm.reqVersion.lifeCyclePhase.phase;
                var defs = vm.reqVersion.master.type.lifecycle.phases;
                defs.sort(function (a, b) {
                    return a.id - b.id;
                });
                var phaseMap = new Hashtable();
                var lastPhase = defs[defs.length - 1].phase;
                angular.forEach(defs, function (def) {
                    if (def.phase == currentPhase && lastPhase == def.phase) {
                        if (phaseMap.get(def.phase) == null) {
                            phases.push({
                                name: def.phase,
                                finished: true,
                                rejected: (def.phase == currentPhase && vm.reqVersion.rejected),
                                current: (def.phase == currentPhase)
                            })
                            phaseMap.put(def.phase, def);
                        }
                    } else {
                        if (phaseMap.get(def.phase) == null) {
                            phases.push({
                                name: def.phase,
                                finished: false,
                                rejected: (def.phase == currentPhase && vm.reqVersion.rejected),
                                current: (def.phase == currentPhase)
                            })
                            phaseMap.put(def.phase, def);
                        }
                    }
                });

                var index = -1;
                for (var i = 0; i < phases.length; i++) {
                    if (phases[i].current == true) {
                        index = i;
                    }
                }

                if (index > 0) {
                    for (i = 0; i < index; i++) {
                        phases[i].finished = true;
                    }
                }

                $rootScope.setLifecyclePhases(phases);
            }

            vm.showAllRequirements = showAllRequirements;
            function showAllRequirements() {
                $state.go('app.req.document.details', {
                    reqId: vm.reqChild.document,
                    tab: 'details.requirements'
                });
            }

            function loadCommentsCount() {
                CommentsService.getAllCommentsCount('REQUIREMENT', vm.reqId).then(
                    function (data) {
                        $rootScope.showComments('REQUIREMENT', vm.reqId, data);
                        $rootScope.showTags('REQUIREMENT', vm.reqVersion.master.id, vm.reqVersion.master.tags.length);
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                    }
                )
            }

            vm.approveReq = approveReq;
            function approveReq() {
               if (vm.reqVersion.reviewer.notes == null || vm.reqVersion.reviewer.notes == '') {
                    $scope.error = notesErrorMsg;
                } else {
                    vm.reqVersion.reviewer.reqDoc = vm.reqChild.document;
                    RequirementService.approveRequirement(vm.reqVersion.id, vm.reqVersion.reviewer).then(
                        function (data) {
                            hideReqDialog();
                            loadRequirement();
                            $rootScope.loadRequirementDetails();
                            $rootScope.loadReviwers();
                            vm.reqVersion.reviewer = data;
                            $rootScope.showSuccessMessage(requirementApproverMsg);
                        }, function (error) {
                            $rootScope.showErrorMessage(error.message);
                            $rootScope.hideBusyIndicator();
                        }
                    )
                }
            }

            vm.rejectReq = rejectReq;
            function rejectReq() {
                if (vm.reqVersion.reviewer.notes == null || vm.reqVersion.reviewer.notes == '') {
                    $scope.error = notesErrorMsg;
                } else {
                    vm.reqVersion.reviewer.reqDoc = vm.reqChild.document;
                    RequirementService.rejectRequirement(vm.reqVersion.id, vm.reqVersion.reviewer).then(
                        function (data) {
                            hideReqDialog();
                            loadRequirement();
                            $rootScope.loadRequirementDetails();
                            $rootScope.loadReviwers();
                            vm.reqVersion.reviewer = data;
                            $rootScope.showSuccessMessage(requirementRejectMsg);
                        }, function (error) {
                            $rootScope.showErrorMessage(error.message);
                            $rootScope.hideBusyIndicator();
                        }
                    )
                }
            }


            vm.showReqDialog = showReqDialog;
            function showReqDialog(type) {
                if (vm.reqChild.workflow != null && vm.reqChild.finishWorkflow == false){
                    $rootScope.showWarningMessage(workflowApprMsg);
                } 
                else {
                 vm.reqType = type;
                 var modal = document.getElementById("req-modal");
                 modal.style.display = "block";
                }
            }

            vm.hideReqDialog = hideReqDialog;
            function hideReqDialog() {
                var modal = document.getElementById("req-modal");
                modal.style.display = "none";
                vm.reqVersion.reviewer.notes = null;
                $scope.error = "";
            }

            $rootScope.loadRequirementTabCounts = loadRequirementTabCounts;
            function loadRequirementTabCounts() {
                RequirementService.getRequirementTabCount(vm.reqId).then(
                    function (data) {
                        vm.tabCounts = data;
                        var requirementsTab = document.getElementById("reviewers");
                        var reqItemsTab = document.getElementById("req-items");
                        var filesTab = document.getElementById("files");
                        var tmplStr = "<span class='label label-default' style='margin-top:20px;background-color: #e4dddd;height: 20px;margin-left: 5px;'>{0}</span>";
                        if (requirementsTab != null) {
                            requirementsTab.lastElementChild.innerHTML = vm.tabs.reviewers.heading +
                                tmplStr.format(vm.tabCounts.reviewer);
                        }
                        if (filesTab != null) {
                            filesTab.lastElementChild.innerHTML = vm.tabs.files.heading +
                                tmplStr.format(vm.tabCounts.itemFiles);
                        }
                        if (reqItemsTab != null) {
                            reqItemsTab.lastElementChild.innerHTML = vm.tabs.items.heading +
                                tmplStr.format(vm.tabCounts.items);
                        }
                    }
                )
            }

            vm.showExternalUserItems = showExternalUserItems;
            function showExternalUserItems() {
                $state.go('app.home');

                $rootScope.sharedItem();
            }

            vm.reviewReq = reviewReq;
            function reviewReq() {
                if (vm.reqVersion.reviewer.notes == null || vm.reqVersion.reviewer.notes == '') {
                    $scope.error = notesErrorMsg;
                } else {
                    vm.reqVersion.reviewer.reqDoc = vm.reqChild.document;
                    RequirementService.reviewRequirement(vm.reqVersion.id, vm.reqVersion.reviewer).then(
                        function (data) {
                            hideReqDialog();
                            loadRequirement();
                            $rootScope.loadRequirementDetails();
                            $rootScope.loadReviwers();
                            vm.reqVersion.reviewer = data;
                            $rootScope.showSuccessMessage(requirementReviewMsg);
                        }, function (error) {
                            $rootScope.showErrorMessage(error.message);
                            $rootScope.hideBusyIndicator();
                        }
                    )
                }
            }

            vm.reviseReqVersion = reviseReqVersion;
            function reviseReqVersion() {
                RequirementService.reviseRequirement(vm.reqChild, vm.reqVersion.id).then(
                    function (data) {
                        $rootScope.loadRequirementDetails();
                        loadRequirement();
                        $state.go('app.req.requirements.details', {requirementId: data.id});
                        hideReqVersionDialog();
                        $rootScope.showSuccessMessage(requirementReviseMsg);
                        vm.reqVersion = data.requirementVersion;
                        $rootScope.reqVersion = vm.reqVersion;
                        $scope.reqVersion = vm.reqVersion;
                    })
            }

            vm.showReqVersionHistory = showReqVersionHistory;
            var versionHistoryTitle = $translate.instant("VERSION_HISTORY");

            function showReqVersionHistory() {
                var options = {
                    title: $rootScope.reqVersion.master.number + " - " + versionHistoryTitle,
                    template: 'app/desktop/modules/item/details/tabs/basic/itemRevisionHistoryView.jsp',
                    controller: 'ItemRevisionHistoryController as revHistoryVm',
                    resolve: 'app/desktop/modules/item/details/tabs/basic/itemRevisionHistoryController',
                    data: {
                        itemId: $rootScope.reqVersion.master.id,
                        revisionHistoryType: "REQUIREMENTVERSION"
                    },
                    width: 700,
                    showMask: true
                };

                $rootScope.showSidePanel(options);
            }


            vm.showReqVersionDialog = showReqVersionDialog;
            function showReqVersionDialog() {
                var modal = document.getElementById("version-modal");
                modal.style.display = "block";
            }

            vm.hideReqVersionDialog = hideReqVersionDialog;
            function hideReqVersionDialog() {
                var modal = document.getElementById("version-modal");
                modal.style.display = "none";
                vm.reqVersion.notes = null;
                $scope.error = "";
            }


            vm.submitReqVersion = submitReqVersion;
            function submitReqVersion() {
                RequirementService.submitReqVersion(vm.reqVersion.id).then(
                    function (data) {
                        loadRequirement();
                        $rootScope.showSuccessMessage("Submit successfully");
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                        $rootScope.hideBusyIndicator();
                    }
                )
            }

            vm.hasDisplayTab = hasDisplayTab;
            function hasDisplayTab(tabName) {
                var valid = true;

                if (vm.reqVersion != null && vm.reqVersion != undefined && vm.reqVersion.master.type != null && vm.reqVersion.master.type.tabs != null) {
                    var index = vm.reqVersion.master.type.tabs.indexOf(tabName);
                    if (index == -1) {
                        valid = false;
                    }
                }

                return valid;
            }

            
            vm.changeWorkflow = changeWorkflow;
            function changeWorkflow() {
                $scope.$broadcast('app.change.workflow');
            }

            vm.addWorkflow = addWorkflow;
            function addWorkflow() {
                $scope.$broadcast('app.add.workflow');
            }

            (function () {
                if (validateJSON()) {
                    lastSelectedTab = JSON.parse($window.localStorage.getItem("lastSelectedSubstanceTab"));
                }

                $window.localStorage.setItem("lastSelectedSubstanceTab", "");

                if (lastSelectedTab != null && lastSelectedTab != undefined) {
                    tabActivated(lastSelectedTab);

                    $timeout(function () {
                        $scope.$broadcast('app.req.requirement.tabActivated', {tabId: lastSelectedTab});
                    }, 1000)
                } else if (tabId != null && tabId != undefined) {
                    tabActivated(tabId);

                    $timeout(function () {
                        $scope.$broadcast('app.req.requirement.tabActivated', {tabId: tabId});
                    }, 1000)
                }
                loadRequirement();
                loadRequirementTabCounts();
            })();

        }
    }
);
