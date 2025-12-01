define(
    [
        'app/desktop/modules/req/req.module',
        'app/assets/bower_components/cassini-platform/app/desktop/modules/common/comments/commentsDirective',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/commonService',
        'app/desktop/modules/req/reqdocs/details/tabs/basic/reqDocumentBasicInfoController',
        'app/desktop/modules/req/reqdocs/details/tabs/requirements/requirementsController',
        'app/desktop/modules/req/reqdocs/details/tabs/timeline/reqDocumentTimelineController',
        'app/desktop/modules/req/reqdocs/details/tabs/files/reqDocumentFilesController',
        'app/desktop/modules/req/reqdocs/details/tabs/workflow/reqDocumentWorkflowController',
        'app/desktop/modules/req/reqdocs/details/tabs/documentReviewers/reqDocumentReviewersController',
        'app/assets/bower_components/cassini-platform/app/desktop/directives/freetext/freeTextDirective',
        'app/shared/services/core/reqDocumentService',
        'app/desktop/directives/plugin-directive/pluginTabsDirective'

    ],
    function (module) {
        module.controller('ReqDocumentDetailsController', ReqDocumentDetailsController);

        function ReqDocumentDetailsController($scope, $rootScope, $timeout, $state, $stateParams, $cookies, $window, $application,
                                              $translate, ReqDocumentService, CommentsService, DialogService) {
            var vm = this;
            $rootScope.viewInfo.showDetails = true;
            vm.reqDocId = $stateParams.reqId;
            var parsed = angular.element("<div></div>");
            var basic = parsed.html($translate.instant("DETAILS_TAB_BASIC")).html();
            var requirements = parsed.html($translate.instant("REQUIREMENT_TAB")).html();
            var workflow = parsed.html($translate.instant("WORKFLOW")).html();
            var filesTabHeading = parsed.html($translate.instant("DETAILS_TAB_FILES")).html();
            var timelineHeading = parsed.html($translate.instant("TIMELINE")).html();
            var reviewersAndApprovers = parsed.html($translate.instant("REVIEWERS_AND_APPROVERS")).html();
            $scope.requirementDocApproverTitle = parsed.html($translate.instant("REQUIREMENT_DOC_APPROVER")).html();
            $scope.requirementDocRejectTitle = parsed.html($translate.instant("REQUIREMENT_DOC_REJECT")).html();
            var requirementDocApproverMsg = parsed.html($translate.instant("REQUIREMENT_DOC_APPROVER_MSG")).html();
            var requirementDocRejectMsg = parsed.html($translate.instant("REQUIREMENT_DOC_REJECT_MSG")).html();
            var requirementDocReviewMsg = parsed.html($translate.instant("REQUIREMENT_DOC_REVIEW_MSG")).html();
            var requirementApprovedSuccessMsg = parsed.html($translate.instant("REQUIREMENT_APPROVED_SUCCESS_MSG")).html();
            $scope.requirementDocReviewTitle = parsed.html($translate.instant("REQUIREMENT_DOC_REVIEW")).html();
            $rootScope.addReviwersAndApprovers = parsed.html($translate.instant("ADD_REVIWERS_APPROVERS")).html();
            $scope.requirementDocReviseTitle = parsed.html($translate.instant("REQUIREMENT_DOC_REVISE")).html();
            var requirementDocReviseSucess = parsed.html($translate.instant("REQUIREMENT_DOC_REVISE_SUCCESS_MSG")).html();
            var lastSelectedTab = null;
            vm.active = 0;
            $rootScope.clipBoardObjectFiles = [];
            $rootScope.clipBoardObjectFiles = $application.clipboard.files;
            $scope.reqDocumentRevision = null;
            vm.tabId = $stateParams.tab;
            vm.customTabs = [];


            vm.tabs = {
                basic: {
                    id: 'details.basic',
                    heading: basic,
                    template: 'app/desktop/modules/req/reqdocs/details/tabs/basic/reqDocumentBasicInfoView.jsp',
                    index: 0,
                    active: true,
                    activated: true
                },
                reviewers: {
                    id: 'details.reviewers',
                    heading: reviewersAndApprovers,
                    template: 'app/desktop/modules/req/reqdocs/details/tabs/documentReviewers/reqDocumentReviewersView.jsp',
                    index: 1,
                    active: false,
                    activated: false
                },
                requirements: {
                    id: 'details.requirements',
                    heading: requirements,
                    template: 'app/desktop/modules/req/reqdocs/details/tabs/requirements/requirementsView.jsp',
                    index: 2,
                    active: false,
                    activated: false
                },
                files: {
                    id: 'details.files',
                    heading: filesTabHeading,
                    template: 'app/desktop/modules/req/reqdocs/details/tabs/files/reqDocumentFilesView.jsp',
                    index: 3,
                    active: false,
                    activated: false
                },
                workflow: {
                    id: 'details.workflow',
                    heading: workflow,
                    template: 'app/desktop/modules/req/reqdocs/details/tabs/workflow/reqDocumentWorkflowView.jsp',
                    index: 4,
                    active: false,
                    activated: false
                },
                timelineHistory: {
                    id: 'details.timelineHistory',
                    heading: timelineHeading,
                    template: 'app/desktop/modules/req/reqdocs/details/tabs/timeline/reqDocumentTimelineView.jsp',
                    index: 5,
                    active: false,
                    activated: false
                }

            };
            var tabId = $stateParams.tab;
            if (tabId == null || tabId === undefined || tabId.trim() === "") {
                tabId = 'details.basic';
                $state.transitionTo('app.req.document.details', {
                    reqId: vm.reqDocId,
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
                $scope.$broadcast('app.req.document.tabActivated', {tabId: $rootScope.selectedTab.id});
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
                $state.transitionTo('app.req.document.details', {
                    reqId: vm.reqDocId, tab: tabId
                }, {notify: false});
                var tab = getTabById(tabId);
                vm.activeTab = tab;
                vm.active = tab.index;
                if (tab != null) {
                    $rootScope.selectedTab = tab;
                    tab.activated = true;
                    tab.active = true;
                    activateTab(tab);
                    $scope.$broadcast('app.req.document.tabActivated', {tabId: tabId});

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

            vm.hasDisplayTab = hasDisplayTab;
            function hasDisplayTab(tabName) {
                var valid = true;

                if (vm.reqDocumentRevision != null && vm.reqDocumentRevision != undefined && vm.reqDocumentRevision.master.type.tabs != null) {
                    var index = vm.reqDocumentRevision.master.type.tabs.indexOf(tabName);
                    if (index == -1) {
                        valid = false;
                    }
                }

                return valid;
            }

            vm.back = back;
            function back() {
                window.history.back();
            }

            vm.freeTextSearch = freeTextSearch;
            $rootScope.freeTextQuerys = null;
            function freeTextSearch(freeText) {
                $rootScope.freeTextQuerys = freeText;
                if (freeText != null && freeText !== undefined && freeText.trim() !== "") {
                    if (vm.activeTab.id === 'details.files') {
                        $scope.$broadcast('app.details.files.search', {name: freeText});
                    }
                    else if (vm.activeTab.id === 'details.requirements') {
                        $scope.$broadcast('app.details.requirements.search', {freeText: freeText});
                    }
                }
                else {
                    $rootScope.freeTextQuerys = null;
                    if (vm.activeTab.id !== 'details.requirements') {
                        $scope.$broadcast('app.req.document.tabActivated', {tabId: vm.activeTab.id});
                    }
                }
            }

            vm.onClear = onClear;
            function onClear() {
                $rootScope.freeTextQuerys = null;
                if (vm.activeTab.id === 'details.files') {
                    $scope.$broadcast('app.req.document.tabActivated', {tabId: 'details.files'});
                }
                else if (vm.activeTab.id === 'details.requirements') {
                    $scope.$broadcast('app.details.requirements.clearsearch');
                }
            }

            $rootScope.loadReqDocument = loadReqDocument;
            function loadReqDocument() {
                vm.loading = true;
                ReqDocumentService.getReqDocumentRevision(vm.reqDocId).then(
                    function (data) {
                        $scope.reqDocumentRevision = data;
                        vm.reqDocumentRevision = data;
                        $rootScope.reqDocumentRevision = data;
                        $rootScope.viewInfo.title = "<div class='item-number'>" +
                            "{0}</div> <span class='item-rev'>Rev {1}</span>".format(vm.reqDocumentRevision.master.number, vm.reqDocumentRevision.revision);
                        $rootScope.viewInfo.description = vm.reqDocumentRevision.master.name;
                        setLifecycles();
                        loadReqDocumentTabCounts();
                        loadCommentsCount();
                        vm.lastLifecyclePhase = vm.reqDocumentRevision.master.type.lifecycle.phases[vm.reqDocumentRevision.master.type.lifecycle.phases.length - 1];
                        vm.firstLifecyclePhase = vm.reqDocumentRevision.master.type.lifecycle.phases[0];
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                        $rootScope.hideBusyIndicator();
                    }
                );

            }

            function setLifecycles() {
                var phases = [];
                var currentPhase = vm.reqDocumentRevision.lifeCyclePhase.phase;
                var currentLifeCyclePhase = vm.reqDocumentRevision.lifeCyclePhase;
                $rootScope.lifeCycleStatus = vm.reqDocumentRevision.lifeCyclePhase.phase;
                var defs = vm.reqDocumentRevision.master.type.lifecycle.phases;
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
                                rejected: (def.phase == currentPhase && vm.reqDocumentRevision.rejected),
                                current: (def.phase == currentPhase)
                            })
                            phaseMap.put(def.phase, def);
                        }
                    } else {
                        if (phaseMap.get(def.phase) == null) {
                            phases.push({
                                name: def.phase,
                                finished: false,
                                rejected: (def.phase == currentPhase && vm.reqDocumentRevision.rejected),
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

            var requirementSubmitMessage = parsed.html($translate.instant("REQ_DOCUMENT_SUBMIT_MESSAGE")).html();
            var requirementReleaseMessage = parsed.html($translate.instant("REQ_DOCUMENT_RELEASE_MESSAGE")).html();
            var doYouWantToSubmit = parsed.html($translate.instant("REQ_DOCUMENT_SUBMIT_TITLE_MESSAGE")).html();
            var doYouWantToRelease = parsed.html($translate.instant("REQ_DOCUMENT_RELEASE_TITLE_MESSAGE")).html();
            var confirmation = parsed.html($translate.instant("CONFIRMATION")).html();
            var yesTitle = parsed.html($translate.instant("YES")).html();
            var noTitle = parsed.html($translate.instant("NO")).html();

            vm.submitReqDoc = submitReqDoc;
            function submitReqDoc() {
                var options = {
                    title: confirmation,
                    message: doYouWantToSubmit,
                    okButtonClass: 'btn-danger',
                    okButtonText: yesTitle,
                    cancelButtonText: noTitle
                };
                DialogService.confirm(options, function (yes) {
                    if (yes == true) {
                        $rootScope.showBusyIndicator($('.view-container'));
                        ReqDocumentService.submitReqDocument(vm.reqDocId).then(
                            function (data) {
                                $rootScope.loadReqDocumentDetails();
                                loadReqDocument();
                                $rootScope.initRequirementsTree();
                                $rootScope.hideBusyIndicator();
                                $rootScope.showSuccessMessage(requirementSubmitMessage);
                            }, function (error) {
                                $rootScope.showErrorMessage(error.message);
                                $rootScope.hideBusyIndicator();
                            }
                        )
                    }
                })
            }

            vm.releasedReqDoc = releasedReqDoc;
            function releasedReqDoc() {
                var options = {
                    title: confirmation,
                    message: doYouWantToRelease,
                    okButtonClass: 'btn-danger',
                    okButtonText: yesTitle,
                    cancelButtonText: noTitle
                };
                DialogService.confirm(options, function (yes) {
                    if (yes == true) {
                        $rootScope.showBusyIndicator($('.view-container'));
                        ReqDocumentService.releaseReqDocument(vm.reqDocId).then(
                            function (data) {
                                $rootScope.loadReqDocumentDetails();
                                loadReqDocument();
                                $rootScope.initRequirementsTree();
                                $rootScope.hideBusyIndicator();
                                $rootScope.showSuccessMessage(requirementReleaseMessage);
                            }, function (error) {
                                $rootScope.showErrorMessage(error.message);
                                $rootScope.hideBusyIndicator();
                            }
                        )
                    }
                })
            }

            $rootScope.loadReqDocumentTabCounts = loadReqDocumentTabCounts;
            function loadReqDocumentTabCounts() {
                ReqDocumentService.getReqDocumentTabCount(vm.reqDocId).then(
                    function (data) {
                        vm.tabCounts = data;
                        $scope.requirements = vm.tabCounts.requirements;
                        var reviewersTab = document.getElementById("reviewers");
                        var requirementsTab = document.getElementById("reqs");
                        var filesTab = document.getElementById("files");
                        var tmplStr = "<span class='label label-default' style='margin-top:20px;background-color: #e4dddd;height: 20px;margin-left: 5px;'>{0}</span>";
                        if (requirementsTab != null) {
                            requirementsTab.lastElementChild.innerHTML = vm.tabs.requirements.heading +
                                tmplStr.format(vm.tabCounts.requirements);
                        }
                        if (filesTab != null) {
                            filesTab.lastElementChild.innerHTML = vm.tabs.files.heading +
                                tmplStr.format(vm.tabCounts.itemFiles);
                        }
                        if (reviewersTab != null) {
                            reviewersTab.lastElementChild.innerHTML = vm.tabs.reviewers.heading +
                                tmplStr.format(vm.tabCounts.reviewer);
                        }
                    }
                )
            }

            function loadCommentsCount() {
                CommentsService.getAllCommentsCount('REQUIREMENTDOCUMENTREVISION', vm.reqDocId).then(
                    function (data) {
                        $rootScope.showComments('REQUIREMENTDOCUMENTREVISION', vm.reqDocId, data);
                        $rootScope.showTags('REQUIREMENTDOCUMENTREVISION', vm.reqDocumentRevision.master.id, vm.reqDocumentRevision.master.tags.length);
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                    }
                )
            }

            var reviewersTitle = parsed.html($translate.instant("REQ_DOC_REVIEWERS")).html();
            $rootScope.showReviewers = showReviewers;
            function showReviewers() {
                var options = {
                    title: reviewersTitle,
                    template: 'app/desktop/modules/req/reqdocs/details/tabs/requirements/reviewersView.jsp',
                    controller: 'ReviewersController as reviewersVm',
                    resolve: 'app/desktop/modules/req/reqdocs/details/tabs/requirements/reviewersController',
                    width: 400,
                    showMask: true,
                    data: {
                        requirement: vm.reqDocumentRevision,
                        type: 'REQUIREMENTDOCUMENT'
                    }
                };

                $rootScope.showSidePanel(options);
            }

            vm.approveReqDoc = approveReqDoc;
            function approveReqDoc() {
                if (vm.reqDocumentRevision.reviewer.notes == null || vm.reqDocumentRevision.reviewer.notes == '') {
                    $scope.error = notesErrorMsg;
                } else {
                    ReqDocumentService.approveRequirementDocument(vm.reqDocId, vm.reqDocumentRevision.reviewer).then(
                        function (data) {
                            hideReqDocDialog();
                            $rootScope.loadReqDocumentDetails();
                            $rootScope.loadDocumentReviewers();
                            loadReqDocument();
                            vm.reqDocumentRevision.reviewer = data;
                            $rootScope.showSuccessMessage(requirementDocApproverMsg);
                        }, function (error) {
                            $rootScope.showErrorMessage(error.message);
                            $rootScope.hideBusyIndicator();
                        }
                    )
                }
            }

            var notesErrorMsg = parsed.html($translate.instant("ENTER_NOTES_VALIDATION")).html();
            vm.rejectReqDoc = rejectReqDoc;
            function rejectReqDoc() {
                if (vm.reqDocumentRevision.reviewer.notes == null || vm.reqDocumentRevision.reviewer.notes == '') {
                    $scope.error = notesErrorMsg;
                } else {
                    ReqDocumentService.rejectRequirementDocument(vm.reqDocId, vm.reqDocumentRevision.reviewer).then(
                        function (data) {
                            hideReqDocDialog();
                            $rootScope.loadReqDocumentDetails();
                            $rootScope.loadDocumentReviewers();
                            loadReqDocument();
                            vm.reqDocumentRevision.reviewer = data;
                            $rootScope.showSuccessMessage(requirementDocRejectMsg);
                        }, function (error) {
                            $rootScope.showErrorMessage(error.message);
                            $rootScope.hideBusyIndicator();
                        }
                    )
                }
            }

            vm.reviewReqDoc = reviewReqDoc;
            function reviewReqDoc() {
                if (vm.reqDocumentRevision.reviewer.notes == null || vm.reqDocumentRevision.reviewer.notes == '') {
                    $scope.error = notesErrorMsg;
                } else {
                    ReqDocumentService.reviewRequirementDocument(vm.reqDocId, vm.reqDocumentRevision.reviewer).then(
                        function (data) {
                            hideReqDocDialog();
                            $rootScope.loadReqDocumentDetails();
                            $rootScope.loadDocumentReviewers();
                            loadReqDocument();
                            vm.reqDocumentRevision.reviewer = data;
                            $rootScope.showSuccessMessage(requirementDocReviewMsg);
                        }, function (error) {
                            $rootScope.showErrorMessage(error.message);
                            $rootScope.hideBusyIndicator();
                        }
                    )
                }
            }


            vm.showReqDocDialog = showReqDocDialog;
            function showReqDocDialog(type) {
                vm.reqDocType = type;
                var modal = document.getElementById("reqDoc-modal");
                modal.style.display = "block";
            }

            vm.hideReqDocDialog = hideReqDocDialog;
            function hideReqDocDialog() {
                var modal = document.getElementById("reqDoc-modal");
                modal.style.display = "none";
                vm.reqDocumentRevision.reviewer.notes = null;
                $scope.error = "";
            }

            vm.approvedAllRequirements = approvedAllRequirements;
            function approvedAllRequirements() {
                ReqDocumentService.approveAllRequirements(vm.reqDocId).then(
                    function (data) {
                        $rootScope.showSuccessMessage(requirementApprovedSuccessMsg);
                        loadReqDocument();
                        $rootScope.initRequirementsTree();
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                    }
                )
            }

            var newReqDocTemp = parsed.html($translate.instant("NEW_REQ_DOC_TEMPLATE")).html();
            var reqDocTemplateSavedMsg = parsed.html($translate.instant("REQ_DOC_TEMPLATE_SAVED_MSG")).html();
            vm.newReqDocTemplate = newReqDocTemplate;
            function newReqDocTemplate() {
                var options = {
                    title: newReqDocTemp,
                    template: 'app/desktop/modules/reqTemplate/new/newReqDocTemplateView.jsp',
                    controller: 'NewReqDocTemplateController as newReqDocTemplateVm',
                    resolve: 'app/desktop/modules/reqTemplate/new/newReqDocTemplateController',
                    width: 675,
                    showMask: true,
                    data: {
                        documentRevision: vm.reqDocumentRevision
                    },
                    buttons: [
                        {text: 'Save', broadcast: 'app.req.doc.template.save'}
                    ],
                    callback: function (result) {
                        $rootScope.showSuccessMessage(reqDocTemplateSavedMsg)
                    }
                };

                $rootScope.showSidePanel(options);
            }

            var revisionHistoryTitle = $translate.instant("REVISION_HISTORY_TITLE");
            vm.showDocumentRevisionHistory = showDocumentRevisionHistory;
            function showDocumentRevisionHistory() {
                var options = {
                    title: $rootScope.reqDocumentRevision.master.number + " - " + revisionHistoryTitle,
                    template: 'app/desktop/modules/item/details/tabs/basic/itemRevisionHistoryView.jsp',
                    controller: 'ItemRevisionHistoryController as revHistoryVm',
                    resolve: 'app/desktop/modules/item/details/tabs/basic/itemRevisionHistoryController',
                    data: {
                        itemId: $rootScope.reqDocumentRevision.master.id,
                        revisionHistoryType: "REQUIREMENTDOCUMENT"
                    },
                    width: 700,
                    showMask: true
                };

                $rootScope.showSidePanel(options);
            }

            vm.reviseReqDocument = reviseReqDocument;

            function reviseReqDocument() {
                if (vm.reqDocumentRevision.comment == null || vm.reqDocumentRevision.comment == '') {
                    $scope.error = notesErrorMsg;
                    vm.reqDocumentRevision.comment = null;
                } else {
                    ReqDocumentService.reviseRequirementDocument(vm.reqDocumentRevision, vm.reqDocumentRevision.master.id).then(
                        function (data) {
                            vm.reqDocumentRevision = data;
                            $rootScope.showSuccessMessage(requirementDocReviseSucess);
                            vm.hideReviseDialog();
                            loadReqDocument();
                            $state.go('app.req.document.details', {reqId: data.id});
                        }
                    )
                }
            }


            vm.showReviseDialog = showReviseDialog;
            function showReviseDialog() {
                var modal = document.getElementById("rev-modal");
                modal.style.display = "block";
            }

            vm.hideReviseDialog = hideReviseDialog;
            function hideReviseDialog() {
                var modal = document.getElementById("rev-modal");
                modal.style.display = "none";
                vm.reqDocumentRevision.comment = null;
                $scope.error = "";
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
                        $scope.$broadcast('app.req.document.tabActivated', {tabId: lastSelectedTab});
                    }, 1000)
                } else if (tabId != null && tabId != undefined) {
                    tabActivated(tabId);

                    $timeout(function () {
                        $scope.$broadcast('app.req.document.tabActivated', {tabId: tabId});
                    }, 1000)
                }
                loadReqDocument();
            })();

        }
    }
)
;
