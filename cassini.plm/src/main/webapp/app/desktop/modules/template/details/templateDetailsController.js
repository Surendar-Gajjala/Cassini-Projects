define(
    [
        'app/desktop/modules/template/template.module',
        'app/shared/services/core/projectTemplateService',
        'app/desktop/modules/template/details/basic/templateBasicInfoController',
        'app/desktop/modules/template/details/team/all/templateTeamController',
        'app/desktop/modules/template/details/plan/templatePlanController',
        'app/desktop/modules/template/details/files/projectTemplateFilesController',
        'app/desktop/modules/template/details/workflow/projectTemplateWorkflowController',
        'app/desktop/modules/template/details/plan/templateGanttEditor',
        'app/assets/bower_components/cassini-platform/app/desktop/directives/freetext/freeTextDirective',
        'app/desktop/directives/plugin-directive/pluginTabsDirective'

    ],
    function (module) {
        module.controller('TemplateDetailsController', TemplateDetailsController);

        function TemplateDetailsController($scope, $state, $stateParams, $rootScope, $translate, TemplateGanttEditor, DialogService, $window, $timeout, ProjectTemplateService) {

            var vm = this;
            $rootScope.viewInfo.showDetails = true;
            vm.templateDetailsTabActivated = templateDetailsTabActivated;

            var parsed = angular.element("<div></div>");

            var basic = parsed.html($translate.instant("DETAILS_TAB_BASIC")).html();
            var saveTemplate = parsed.html($translate.instant("SAVE_TEMPLATE")).html();
            var templateModified = parsed.html($translate.instant("TEMPLATE_MODIFIED")).html();
            var yes = parsed.html($translate.instant("YES")).html();
            var no = parsed.html($translate.instant("NO")).html();
            vm.addWorkflowTitle = parsed.html($translate.instant("ADD_WORKFLOW")).html();
            vm.changeWorkflowTitle = parsed.html($translate.instant("CHANGE_WORKFLOW")).html();
            vm.back = back;
            vm.expand = false;
            vm.tabId = $stateParams.tab;
            vm.customTabs = [];
            vm.templateOpenFrom = null;

            vm.expandAll = expandAll;
            vm.collapseAll = collapseAll;
            vm.saveGantt = saveGantt;

            function expandAll() {
                TemplateGanttEditor.expandAll();
                vm.expand = false;
            }

            function collapseAll() {
                TemplateGanttEditor.collapseAll();
                vm.expand = true;
            }

            function saveGantt() {
                $scope.$broadcast('app.template.createSaveGantt');
            }

            function back() {
                if (vm.templateOpenFrom != null && vm.templateOpenFrom != "null") {
                    $state.go('app.pm.programtemplate.details', {
                        templateId: vm.templateOpenFrom,
                        tab: 'details.project'
                    });
                } else {
                    $state.go('app.templates.all');
                }
            }

            vm.tabs = {
                basic: {
                    id: 'details.basic',
                    heading: basic,
                    index: 0,
                    template: 'app/desktop/modules/template/details/basic/templateBasicInfoView.jsp',
                    active: false,
                    activated: true
                },
                team: {
                    id: 'details.team',
                    heading: 'Team',
                    index: 1,
                    template: 'app/desktop/modules/template/details/team/all/templateTeamView.jsp',
                    active: true,
                    activated: false
                }, plan: {
                    id: 'details.plan',
                    heading: 'Plan',
                    index: 2,
                    template: 'app/desktop/modules/template/details/plan/templatePlanView.jsp',
                    active: true,
                    activated: false
                }, files: {
                    id: 'details.files',
                    heading: 'Files',
                    index: 3,
                    template: 'app/desktop/modules/template/details/files/projectTemplateFilesView.jsp',
                    active: true,
                    activated: false
                },
                workflow: {
                    id: 'details.workflow',
                    heading: "Workflow",
                    index: 4,
                    template: 'app/desktop/modules/template/details/workflow/projectTemplateWorkflowView.jsp',
                    active: true,
                    activated: false
                 }
            };

            function activateTab(tab) {
                for (var t in vm.tabs) {
                    vm.tabs[t].active = (vm.tabs[t].id == tab.id);
                }
                for (var t in vm.customTabs) {
                    vm.customTabs[t].active = (vm.customTabs[t].id == tab.id);
                }
            }

            vm.active = 0;
            var tabId = $stateParams.tab;

            function templateDetailsTabActivated(tabId) {
                if ($rootScope.templateGanttUpdated) {
                    var options = {
                        title: saveTemplate,
                        message: templateModified,
                        okButtonText: yes,
                        cancelButtonText: no
                    };
                    DialogService.confirm(options, function (yes) {
                        if (yes) {
                            $rootScope.saveTemplateGantt();
                            $rootScope.templateGanttUpdated = false;
                        }
                        else {
                            $rootScope.templateGanttUpdated = false;
                            tabActive(tabId);
                        }
                    });
                } else {
                    tabActive(tabId);
                }
            }

            function tabActive(tabId) {
                $state.transitionTo('app.templates.details', {
                    templateId: $stateParams.templateId,
                    tab: tabId
                }, {notify: false});
                var tab = getTabById(tabId);
                vm.active = tab.index;
                if (tab != null) {
                    tab.activated = true;
                    $scope.$broadcast('app.template.tabActivated', {tabId: tabId});

                }
                if (tab != null) {
                    activateTab(tab);
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


            function validateJSON() {
                try {
                    JSON.parse($window.localStorage.getItem("lastSelectedTemplateTab"));
                } catch (e) {
                    return false;
                }
                return true;
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
                    $scope.$broadcast('app.template.tabActivated', {tabId: 'details.files'});
                    $rootScope.searchModeType = false;
                }
            }

            vm.onClear = onClear;
            function onClear() {
                $scope.$broadcast('app.template.tabActivated', {tabId: 'details.files'});           
            }

            $rootScope.loadProjectTemplateCounts = loadProjectTemplateCounts;
            function loadProjectTemplateCounts() {
                ProjectTemplateService.getProjectTemplateDetails($stateParams.templateId).then(
                    function (data) {
                        vm.tabCounts = data;
                        var teamTab = document.getElementById("teamId");
                        var tmplStr = "<span class='label label-default' style='margin-top:20px;background-color: #e4dddd;height: 20px;margin-left: 5px;'>{0}</span>";
                        if (teamTab != null) {
                            teamTab.lastElementChild.innerHTML = vm.tabs.team.heading +
                                tmplStr.format(vm.tabCounts.team);
                        }
                    }
                )
            }

            $rootScope.loadTemplate = loadTemplate;
            function loadTemplate() {
                ProjectTemplateService.getProjectTemplate($stateParams.templateId).then(
                    function (data) {
                        vm.template = data;
                        $rootScope.projectTemplate = data;
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                        $rootScope.hideBusyIndicator();
                    }
                )
            }

            vm.changeWorkflow = changeWorkflow;
            function changeWorkflow() {
                $scope.$broadcast('app.change.workflow');
            }

            vm.addWorkflow = addWorkflow;
            function addWorkflow() {
                $scope.$broadcast('app.add.workflow');
            }

            var lastSelectedTab = null;
            (function () {
                //if ($application.homeLoaded == true) {
                $rootScope.templateGanttUpdated = false;
                loadProjectTemplateCounts();
                loadTemplate();
                if (validateJSON()) {
                    lastSelectedTab = JSON.parse($window.localStorage.getItem("lastSelectedTemplateTab"));
                }

                $window.localStorage.setItem("lastSelectedTemplateTab", "");

                if (lastSelectedTab != null && lastSelectedTab != undefined) {
                    templateDetailsTabActivated(lastSelectedTab);

                    $timeout(function () {
                        $scope.$broadcast('app.template.tabActivated', {tabId: lastSelectedTab});
                    }, 1000)
                } else if (tabId != null && tabId != undefined) {
                    templateDetailsTabActivated(tabId);

                    $timeout(function () {
                        $scope.$broadcast('app.template.tabActivated', {tabId: tabId});
                    }, 1000)
                }
                if ($window.localStorage.getItem("template_open_from") != undefined && $window.localStorage.getItem("template_open_from") != null) {
                    vm.templateOpenFrom = $window.localStorage.getItem("template_open_from");
                }
                //}
            })();
        }
    }
);