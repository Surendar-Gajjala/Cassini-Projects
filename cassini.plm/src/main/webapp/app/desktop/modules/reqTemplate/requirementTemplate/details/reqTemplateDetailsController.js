define(
    [
        'app/desktop/modules/req/reqdocs/requirement/requirement.module',
        'app/assets/bower_components/cassini-platform/app/desktop/modules/common/comments/commentsDirective',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/commonService',
        'app/desktop/modules/reqTemplate/requirementTemplate/details/tabs/basic/reqTemplateBasicInfoController',
        'app/desktop/modules/reqTemplate/requirementTemplate/details/tabs/reviewers/reqTemplateReviewersController',
        'app/assets/bower_components/cassini-platform/app/desktop/directives/freetext/freeTextDirective',
        'app/shared/services/core/reqDocumentService',
        'app/shared/services/core/reqDocTemplateService',
        'app/shared/services/core/requirementService',
        'app/desktop/directives/plugin-directive/pluginTabsDirective'

    ],
    function (module) {
        module.controller('ReqTemplateDetailsController', ReqTemplateDetailsController);

        function ReqTemplateDetailsController($scope, $rootScope, $timeout, $state, $stateParams, $cookies, $window, $application,
                                              $translate, ReqDocumentService, RequirementService, ReqDocTemplateService, CommentsService) {
            var vm = this;
            $rootScope.viewInfo.showDetails = true;
            vm.reqId = $stateParams.requirementId;
            var parsed = angular.element("<div></div>");
            var basic = parsed.html($translate.instant("DETAILS_TAB_BASIC")).html();
            var reviewersAndApprovers = parsed.html($translate.instant("REVIEWERS_AND_APPROVERS")).html();
            var lastSelectedTab = null;
            vm.active = 0;
            $scope.reqTemplate = null;
            vm.tabId = $stateParams.tab;
            vm.customTabs = [];

            vm.tabs = {
                basic: {
                    id: 'details.basic',
                    heading: basic,
                    template: 'app/desktop/modules/reqTemplate/requirementTemplate/details/tabs/basic/reqTemplateBasicInfoView.jsp',
                    index: 0,
                    active: true,
                    activated: true
                },
                reviewers: {
                    id: 'details.reviewers',
                    heading: reviewersAndApprovers,
                    template: 'app/desktop/modules/reqTemplate/requirementTemplate/details/tabs/reviewers/reqTemplateReviewersView.jsp',
                    index: 1,
                    active: false,
                    activated: false
                }

            };
            var tabId = $stateParams.tab;
            if (tabId == null || tabId === undefined || tabId.trim() === "") {
                tabId = 'details.basic';
                $state.transitionTo('app.req.template.details', {
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
                $state.transitionTo('app.req.template.details', {
                    requirementId: vm.reqId, tab: tabId
                }, {notify: false});
                var tab = getTabById(tabId);
                vm.active = tab.index;
                if (tab != null) {
                    $rootScope.selectedTab = tab;
                    tab.activated = true;
                    tab.active = true;
                    activateTab(tab);
                    $scope.$broadcast('app.req.template.tabActivated', {tabId: tabId});

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
                    $scope.$broadcast('app.req.template.tabActivated', {tabId: 'details.files'});
                }
            }

            vm.onClear = onClear;
            function onClear() {
                $rootScope.freeTextQuerys = null;
                $scope.$broadcast('app.req.template.tabActivated', {tabId: 'details.files'});
            }

            var reviewersTitle = parsed.html($translate.instant("REQ_DOC_REVIEWERS")).html();
            $rootScope.showReqTemplateReviewers = showReqTemplateReviewers;
            function showReqTemplateReviewers() {
                var options = {
                    title: 'Req Template Reviewers',
                    template: 'app/desktop/modules/req/reqdocs/details/tabs/requirements/reviewersView.jsp',
                    controller: 'ReviewersController as reviewersVm',
                    resolve: 'app/desktop/modules/req/reqdocs/details/tabs/requirements/reviewersController',
                    width: 400,
                    showMask: true,
                    data: {
                        requirement: vm.reqTemplate,
                        type: 'REQUIREMENTTEMPLATE'
                    }
                };

                $rootScope.showSidePanel(options);
            }

            function loadRequirement() {
                vm.loading = true;
                ReqDocTemplateService.getRequirementTemplate(vm.reqId).then(
                    function (data) {
                        $scope.reqTemplate = data;
                        vm.reqTemplate = data;
                        $rootScope.reqTemplate = data;
                        $rootScope.viewInfo.title = "<div class='item-number'>" +
                            "{0}</div> <span class='item-rev'>Priority: {1}</span> <span class='item-rev'>Status: {2}</span>".
                                format(vm.reqTemplate.name, vm.reqTemplate.priority,
                                vm.reqTemplate.lifeCyclePhase.phase);
                        //setLifecycles();
                        loadRequirementTemplateTabCounts();
                        loadCommentsCount();
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                        $rootScope.hideBusyIndicator();
                    }
                );

            }

            function setLifecycles() {
                var phases = [];
                var currentPhase = vm.reqTemplate.lifeCyclePhase.phase;
                var currentLifeCyclePhase = vm.reqTemplate.lifeCyclePhase;
                $rootScope.lifeCycleStatus = vm.reqTemplate.lifeCyclePhase.phase;
                var defs = vm.reqTemplate.type.lifecycle.phases;
                defs.sort(function (a, b) {
                    return a.id - b.id;
                });
                var lastPhase = defs[defs.length - 1].phase;
                angular.forEach(defs, function (def) {
                    if (def.phase == currentPhase && lastPhase == def.phase) {
                        phases.push({
                            name: def.phase,
                            finished: true,
                            rejected: (def.phase == currentPhase && vm.reqTemplate.rejected),
                            current: (def.phase == currentPhase)
                        })
                    } else {
                        phases.push({
                            name: def.phase,
                            finished: false,
                            rejected: (def.phase == currentPhase && vm.reqTemplate.rejected),
                            current: (def.phase == currentPhase)
                        })
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
                $state.go('app.req.document.template.details', {
                    reqDocId: vm.reqTemplate.documentTemplate.id,
                    tab: 'details.requirements'
                });
            }

            function loadCommentsCount() {
                CommentsService.getAllCommentsCount('REQUIREMENTTEMPLATE', vm.reqId).then(
                    function (data) {
                        $rootScope.showComments('REQUIREMENTTEMPLATE', vm.reqId, data);
                        $rootScope.showTags('REQUIREMENTTEMPLATE', vm.reqId, vm.reqTemplate.tags.length);
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                    }
                )
            }

            $rootScope.loadRequirementTemplateTabCounts = loadRequirementTemplateTabCounts;
            function loadRequirementTemplateTabCounts() {
                ReqDocTemplateService.getRequirementTemplateTabCount(vm.reqId).then(
                    function (data) {
                        vm.tabCounts = data;
                        var reviewersTab = document.getElementById("reviewer");
                        var tmplStr = "<span class='label label-default' style='margin-top:20px;background-color: #e4dddd;height: 20px;margin-left: 5px;'>{0}</span>";
                        if (reviewersTab != null) {
                            reviewersTab.lastElementChild.innerHTML = vm.tabs.reviewers.heading +
                                tmplStr.format(vm.tabCounts.reviewer);
                        }
                    }
                )
            }

            (function () {
                if (validateJSON()) {
                    lastSelectedTab = JSON.parse($window.localStorage.getItem("lastSelectedSubstanceTab"));
                }

                $window.localStorage.setItem("lastSelectedSubstanceTab", "");

                if (lastSelectedTab != null && lastSelectedTab != undefined) {
                    tabActivated(lastSelectedTab);

                    $timeout(function () {
                        $scope.$broadcast('app.req.template.tabActivated', {tabId: lastSelectedTab});
                    }, 1000)
                } else if (tabId != null && tabId != undefined) {
                    tabActivated(tabId);

                    $timeout(function () {
                        $scope.$broadcast('app.req.template.tabActivated', {tabId: tabId});
                    }, 1000)
                }
                loadRequirement();
            })();

        }
    }
);
