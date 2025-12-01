define(
    [
        'app/desktop/modules/pqm/pqm.module',
        'app/assets/bower_components/cassini-platform/app/desktop/modules/common/comments/commentsDirective',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/commonService',
        'app/desktop/modules/pqm/supplierAudit/details/tabs/basic/supplierAuditBasicInfoController',
        'app/desktop/modules/pqm/supplierAudit/details/tabs/plan/supplierAuditPlanController',
        'app/desktop/modules/pqm/supplierAudit/details/tabs/workflow/supplierAuditWorkflowController',
        'app/desktop/modules/pqm/supplierAudit/details/tabs/timeline/supplierAuditTimelineController',
        'app/desktop/modules/pqm/supplierAudit/details/tabs/files/supplierAuditFilesController',
        'app/assets/bower_components/cassini-platform/app/desktop/directives/freetext/freeTextDirective',
        'app/shared/services/core/workflowService',
        'app/shared/services/core/supplierAuditService'
    ],
    function (module) {
        module.controller('SupplierAuditDetailsController', SupplierAuditDetailsController);

        function SupplierAuditDetailsController($scope, $rootScope, $timeout, $state, $stateParams, $cookies, $window, $application,
                                                $translate, SupplierAuditService, CommonService, WorkflowService, CommentsService) {

            $rootScope.viewInfo.icon = "fa fa-list-alt";
            $rootScope.viewInfo.title = $translate.instant("SUPPLIER_AUDIT_DETAILS");
            $rootScope.viewInfo.showDetails = true;
            var vm = this;
            vm.tabActivated = tabActivated;
            vm.refreshDetails = refreshDetails;
            var lastSelectedTab = null;


            vm.supplierAuditId = $stateParams.supplierAuditId;
            var parsed = angular.element("<div></div>");
            var basic = parsed.html($translate.instant("DETAILS_TAB_BASIC")).html();
            var filesHeading = parsed.html($translate.instant("FILES")).html();
            var planHeading = parsed.html($translate.instant("PLAN")).html();
            var timelineHeading = parsed.html($translate.instant("TIMELINE")).html();
            var workflowHeading = parsed.html($translate.instant("WORKFLOW")).html();
            $rootScope.clipBoardObjectFiles = [];
            $rootScope.clipBoardObjectFiles = $application.clipboard.files;
            vm.tabId = $stateParams.tab;
            vm.customTabs = [];
            vm.active = 0;


            vm.tabs = {
                basic: {
                    id: 'details.basic',
                    heading: basic,
                    template: 'app/desktop/modules/pqm/supplierAudit/details/tabs/basic/supplierAuditBasicInfoView.jsp',
                    index: 0,
                    active: true,
                    activated: true
                },
                plan: {
                    id: 'details.plan',
                    heading: planHeading,
                    template: 'app/desktop/modules/pqm/supplierAudit/details/tabs/plan/supplierAuditPlanView.jsp',
                    index: 1,
                    active: false,
                    activated: false
                },
                files: {
                    id: 'details.files',
                    heading: filesHeading,
                    template: 'app/desktop/modules/pqm/supplierAudit/details/tabs/files/supplierAuditFilesView.jsp',
                    index: 2,
                    active: false,
                    activated: false
                },
                workflow: {
                    id: 'details.workflow',
                    heading: workflowHeading,
                    template: 'app/desktop/modules/pqm/supplierAudit/details/tabs/workflow/supplierAuditWorkflowView.jsp',
                    index: 3,
                    active: false,
                    activated: false
                },
                timeline: {
                    id: 'details.timeline',
                    heading: timelineHeading,
                    template: 'app/desktop/modules/pqm/supplierAudit/details/tabs/timeline/supplierAuditTimelineView.jsp',
                    index: 4,
                    active: false,
                    activated: false
                }

            };
            $rootScope.sharedPermission = null;
            vm.showExternalUserSuppliers = showExternalUserSuppliers;
            function showExternalUserSuppliers() {
                $state.go('app.home');
                $rootScope.sharedSupplier();

            }

            var tabId = $stateParams.tab;
            if (tabId == null || tabId === undefined || tabId.trim() === "") {
                tabId = 'details.basic';
                $state.transitionTo('app.pqm.supplierAudit.details', {
                    supplierAuditId: vm.supplierAuditId,
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

            function refreshDetails() {
                $scope.$broadcast('app.supplierAudit.tabActivated', {tabId: $rootScope.selectedTab.id});
            }


            function activateTab(tab) {
                for (var t in vm.tabs) {
                    vm.tabs[t].active = (vm.tabs[t].id == tab.id);
                }
                for (var t in vm.customTabs) {
                    vm.customTabs[t].active = (vm.customTabs[t].id == tab.id);
                }
            }

            function tabActivated(tabId) {
                $state.transitionTo('app.pqm.supplierAudit.details', {
                    supplierAuditId: vm.supplierAuditId, tab: tabId
                }, {notify: false});
                var tab = getTabById(tabId);
                vm.active = tab.index;
                if (tab != null) {
                    $rootScope.selectedTab = tab;
                    tab.activated = true;
                    tab.active = true;
                    activateTab(tab);
                    $scope.$broadcast('app.supplierAudit.tabActivated', {tabId: tabId});

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
                    $scope.$broadcast('app.supplierAudit.tabActivated', {tabId: 'details.files'});
                }
            }


            function validateJSON() {
                try {
                    JSON.parse($window.localStorage.getItem("lastSelectedSupplierAuditTab"));
                } catch (e) {
                    return false;
                }
                return true;
            }

            var parse = angular.element("<div></div>");
            vm.addWorkflowTitle = parse.html($translate.instant("ADD_WORKFLOW")).html();
            vm.changeWorkflowTitle = parse.html($translate.instant("CHANGE_WORKFLOW")).html();

            vm.changeWorkflow = changeWorkflow;
            function changeWorkflow() {
                $scope.$broadcast('app.change.workflow');
            }

            vm.addPartWorkflow = addPartWorkflow;
            function addPartWorkflow() {
                $scope.$broadcast('app.add.workflow');
            }

            $rootScope.supplierAuditReleased = false;
            $rootScope.loadSupplierAudit = loadSupplierAudit;
            function loadSupplierAudit() {
                SupplierAuditService.getSupplierAudit(vm.supplierAuditId).then(
                    function (data) {
                        vm.supplierAudit = data;
                        $rootScope.supplierAudit = vm.supplierAudit;
                        vm.loading = false;
                        $rootScope.viewInfo.description = "Name: " + data.name;
                        $scope.$evalAsync();
                        loadSupplierAuditCounts();
                        setLifecycles();
                        //loadWorkflow();
                        loadCommentsCount();
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                        $rootScope.hideBusyIndicator();
                    }
                )
            }


            function setLifecycles() {
                var phases = [];
                var currentPhase = vm.supplierAudit.status.phase;
                var currentStatusPhase = vm.supplierAudit.status;
                $rootScope.lifeCycleStatus = vm.supplierAudit.status.phase;
                var defs = vm.supplierAudit.type.lifecycle.phases;
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
                                rejected: (def.phase == currentPhase && vm.supplierAudit.rejected),
                                current: (def.phase == currentPhase)
                            })
                            phaseMap.put(def.phase, def);
                        }
                    } else {
                        if (phaseMap.get(def.phase) == null) {
                            phases.push({
                                name: def.phase,
                                finished: false,
                                rejected: (def.phase == currentPhase && vm.supplierAudit.rejected),
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


            function loadCommentsCount() {
                CommentsService.getAllCommentsCount('SUPPLIERAUDIT', vm.supplierAuditId).then(
                    function (data) {
                        $rootScope.showComments('SUPPLIERAUDIT', vm.supplierAuditId, data);
                        $rootScope.showTags('SUPPLIERAUDIT', vm.supplierAuditId, vm.supplierAudit.tags.length);
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                    }
                )
            }

            $rootScope.supplierAuditWorkflowStarted = false;
            function loadWorkflow() {
                WorkflowService.getWorkflow($rootScope.supplierAudit.workflow).then(
                    function (data) {
                        vm.workflow = data;
                        if (vm.workflow.started) {
                            $rootScope.supplierAuditWorkflowStarted = true;
                        }
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                        $rootScope.hideBusyIndicator();
                    }
                )
            }


            $rootScope.loadSupplierAuditCounts = loadSupplierAuditCounts;
            function loadSupplierAuditCounts() {
                SupplierAuditService.getSupplierAuditTabCounts(vm.supplierAuditId).then(
                    function (data) {
                        vm.supplierAuditCounts = data;
                        $rootScope.supplierAuditCounts = data;
                        var planTab = document.getElementById("plan-tab");
                        var filesTab = document.getElementById("files");
                        if (planTab != null) {
                            planTab.lastElementChild.innerHTML = vm.tabs.plan.heading +
                                "<span class='label label-default' style='margin-top:20px;background-color: #e4dddd;height: 20px;margin-left: 5px;'>{0}</span>".format(vm.supplierAuditCounts.planCount);
                        }
                        if (filesTab != null) {
                            filesTab.lastElementChild.innerHTML = vm.tabs.files.heading +
                                "<span class='label label-default' style='margin-top:20px;background-color: #e4dddd;height: 20px;margin-left: 5px;'>{0}</span>".format(vm.supplierAuditCounts.files);
                        }

                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                        $rootScope.hideBusyIndicator();
                    }
                )
            }


            (function () {

                if (validateJSON()) {
                    lastSelectedTab = JSON.parse($window.localStorage.getItem("lastSelectedSupplierAuditTab"));
                }
                if ($window.localStorage.getItem("shared-permission") != undefined && $window.localStorage.getItem("shared-permission") != null) {
                    var sharedPermission = $window.localStorage.getItem("shared-permission");
                    if (sharedPermission != null && sharedPermission != "") {
                        $rootScope.sharedPermission = sharedPermission;
                    }
                }
                $window.localStorage.setItem("lastSelectedSupplierAuditTab", "");

                if (lastSelectedTab != null && lastSelectedTab != undefined) {
                    tabActivated(lastSelectedTab);

                    $timeout(function () {
                        $scope.$broadcast('app.supplierAudit.tabActivated', {tabId: lastSelectedTab});
                    }, 1000)
                } else if (tabId != null && tabId != undefined) {
                    tabActivated(tabId);

                    $timeout(function () {
                        $scope.$broadcast('app.supplierAudit.tabActivated', {tabId: tabId});
                    }, 1000)
                }

                loadSupplierAudit();
            })();


        }
    }
);                              