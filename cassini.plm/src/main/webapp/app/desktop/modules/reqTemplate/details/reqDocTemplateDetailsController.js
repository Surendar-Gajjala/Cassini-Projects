define(
    [
        'app/desktop/modules/reqTemplate/reqDocTemplate.module',
        'app/assets/bower_components/cassini-platform/app/desktop/modules/common/comments/commentsDirective',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/commonService',
        'app/desktop/modules/reqTemplate/details/tabs/basic/basicInfoController',
        'app/desktop/modules/reqTemplate/details/tabs/requirements/requirementTemplatesController',
        'app/desktop/modules/reqTemplate/details/tabs/reviewers/reviewerController',
        'app/shared/services/core/reqDocTemplateService',
        'app/desktop/directives/plugin-directive/pluginTabsDirective'

    ],
    function (module) {
        module.controller('ReqDocTemplateDetailsController', ReqDocTemplateDetailsController);

        function ReqDocTemplateDetailsController($scope, $rootScope, $timeout, $state, $stateParams, $cookies, $window, $application,
                                                 $translate, CommentsService, ReqDocTemplateService) {
            var vm = this;
            $rootScope.viewInfo.showDetails = true;
            vm.reqDocId = $stateParams.reqDocId;
            var parsed = angular.element("<div></div>");
            var basic = parsed.html($translate.instant("DETAILS_TAB_BASIC")).html();
            var requirements = parsed.html($translate.instant("REQUIREMENT_TAB")).html();

            var lastSelectedTab = null;
            vm.active = 0;
            vm.tabId = $stateParams.tab;
            vm.customTabs = [];


            vm.tabs = {
                basic: {
                    id: 'details.basic',
                    heading: basic,
                    template: 'app/desktop/modules/reqTemplate/details/tabs/basic/basicInfoView.jsp',
                    index: 0,
                    active: true,
                    activated: true
                },
                reviewers: {
                    id: 'details.reviewers',
                    heading: 'Reviewers',
                    template: 'app/desktop/modules/reqTemplate/details/tabs/reviewers/reviewerView.jsp',
                    index: 1,
                    active: false,
                    activated: false
                },
                requirements: {
                    id: 'details.requirements',
                    heading: requirements,
                    template: 'app/desktop/modules/reqTemplate/details/tabs/requirements/requirementTemplatesView.jsp',
                    index: 2,
                    active: false,
                    activated: false
                }

            };
            var tabId = $stateParams.tab;
            if (tabId == null || tabId === undefined || tabId.trim() === "") {
                tabId = 'details.basic';
                $state.transitionTo('app.req.document.template.details', {
                    reqDocId: vm.reqDocId,
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
                $scope.$broadcast('app.req.documentTemplate.tabActivated', {tabId: $rootScope.selectedTab.id});
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
                $state.transitionTo('app.req.document.template.details', {
                    reqDocId: vm.reqDocId, tab: tabId
                }, {notify: false});
                var tab = getTabById(tabId);
                vm.activeTab = tab;
                vm.active = tab.index;
                if (tab != null) {
                    $rootScope.selectedTab = tab;
                    tab.activated = true;
                    tab.active = true;
                    activateTab(tab);
                    $scope.$broadcast('app.req.documentTemplate.tabActivated', {tabId: tabId});

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
                if (freeText != null && freeText !== undefined && freeText.trim() !== "") {
                    if (vm.activeTab.id === 'details.requirements') {
                        $scope.$broadcast('app.details.requirements.search', {freeText: freeText});
                    }
                }
                else {
                    $rootScope.freeTextQuerys = null;
                    if (vm.activeTab.id !== 'details.requirements') {
                        $scope.$broadcast('app.req.documentTemplate.tabActivated', {tabId: vm.activeTab.id});
                    }
                }
            }

            vm.onClear = onClear;
            function onClear() {
                $rootScope.freeTextQuerys = null;
                if (vm.activeTab.id === 'details.requirements') {
                    $scope.$broadcast('app.details.requirements.clearsearch');
                }
            }

            function loadReqDocument() {
                vm.loading = true;
                ReqDocTemplateService.getReqDocTemplate(vm.reqDocId).then(
                    function (data) {
                        $scope.reqDocTemplate = data;
                        vm.reqDocTemplate = data;
                        $rootScope.reqDocTemplate = data;
                        $rootScope.viewInfo.title = "<div class='item-number'>" +
                            "{0}</div> ".
                                format(vm.reqDocTemplate.name);
                        //setLifecycles();
                        loadReqDocTemplateTabCounts();
                        loadCommentsCount();
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                        $rootScope.hideBusyIndicator();
                    }
                );

            }

            function setLifecycles() {
                var phases = [];
                var currentPhase = vm.reqDocTemplate.lifeCyclePhase.phase;
                var currentLifeCyclePhase = vm.reqDocTemplate.lifeCyclePhase;
                $rootScope.lifeCycleStatus = vm.reqDocTemplate.lifeCyclePhase.phase;
                var defs = vm.reqDocTemplate.type.lifecycle.phases;
                defs.sort(function (a, b) {
                    return a.id - b.id;
                });
                var lastPhase = defs[defs.length - 1].phase;
                angular.forEach(defs, function (def) {
                    if (def.phase == currentPhase && lastPhase == def.phase) {
                        phases.push({
                            name: def.phase,
                            finished: true,
                            rejected: (def.phase == currentPhase && vm.reqDocTemplate.rejected),
                            current: (def.phase == currentPhase)
                        })
                    } else {
                        phases.push({
                            name: def.phase,
                            finished: false,
                            rejected: (def.phase == currentPhase && vm.reqDocTemplate.rejected),
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

            var reqDocTemplateReviewersTitle = parsed.html($translate.instant("REQ_DOC_TEMPLATE_REVIEWERS")).html();
            $rootScope.showReqDocTemplateReviewers = showReqDocTemplateReviewers;
            function showReqDocTemplateReviewers() {
                var options = {
                    title: reqDocTemplateReviewersTitle,
                    template: 'app/desktop/modules/req/reqdocs/details/tabs/requirements/reviewersView.jsp',
                    controller: 'ReviewersController as reviewersVm',
                    resolve: 'app/desktop/modules/req/reqdocs/details/tabs/requirements/reviewersController',
                    width: 600,
                    showMask: true,
                    data: {
                        requirement: vm.reqDocTemplate,
                        type: 'REQUIREMENTDOCUMENTTEMPLATE'
                    }
                };

                $rootScope.showSidePanel(options);
            }


            function loadCommentsCount() {
                CommentsService.getAllCommentsCount('REQUIREMENTDOCUMENTTEMPLATE', vm.reqDocId).then(
                    function (data) {
                        $rootScope.showComments('REQUIREMENTDOCUMENTTEMPLATE', vm.reqDocId, data);
                        $rootScope.showTags('REQUIREMENTDOCUMENTTEMPLATE', vm.reqDocId, vm.reqDocTemplate.tags.length);
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                    }
                )
            }

            $rootScope.loadReqDocTemplateTabCounts = loadReqDocTemplateTabCounts;
            function loadReqDocTemplateTabCounts() {
                ReqDocTemplateService.getReqDocumentTemplateTabCount(vm.reqDocId).then(
                    function (data) {
                        vm.tabCounts = data;
                        var reviewersTab = document.getElementById("reviewers");
                        var requirementsTab = document.getElementById("reqs");
                        var tmplStr = "<span class='label label-default' style='margin-top:20px;background-color: #e4dddd;height: 20px;margin-left: 5px;'>{0}</span>";
                        if (requirementsTab != null) {
                            requirementsTab.lastElementChild.innerHTML = vm.tabs.requirements.heading +
                                tmplStr.format(vm.tabCounts.requirements);
                        }
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
                        $scope.$broadcast('app.req.documentTemplate.tabActivated', {tabId: lastSelectedTab});
                    }, 1000)
                } else if (tabId != null && tabId != undefined) {
                    tabActivated(tabId);

                    $timeout(function () {
                        $scope.$broadcast('app.req.documentTemplate.tabActivated', {tabId: tabId});
                    }, 1000)
                }
                loadReqDocument();
            })();

        }
    }
)
;

