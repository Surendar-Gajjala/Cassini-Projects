define(
    [
        'app/desktop/modules/pm/pm.module',
        'app/shared/services/core/programTemplateService',
        'app/desktop/modules/pm/programTemplate/details/basic/programTemplateBasicInfoController',
        'app/desktop/modules/pm/programTemplate/details/resources/programTemplateResourcesController',
        'app/desktop/modules/pm/programTemplate/details/project/programTemplateProjectsController',
        'app/desktop/modules/pm/programTemplate/details/files/programTemplateFilesController',
        'app/desktop/modules/pm/programTemplate/details/workflow/programTemplateWorkflowController',
        'app/assets/bower_components/cassini-platform/app/desktop/directives/freetext/freeTextDirective',
        'app/desktop/directives/plugin-directive/pluginTabsDirective'

    ],
    function (module) {
        module.controller('ProgramTemplateDetailsController', ProgramTemplateDetailsController);

        function ProgramTemplateDetailsController($scope, $state, $stateParams, $rootScope, $translate, DialogService, $window, $timeout, ProgramTemplateService) {

            var vm = this;
            $rootScope.viewInfo.showDetails = true;
            vm.templateDetailsTabActivated = templateDetailsTabActivated;

            var parsed = angular.element("<div></div>");

            var basic = parsed.html($translate.instant("DETAILS_TAB_BASIC")).html();
            vm.addWorkflowTitle = parsed.html($translate.instant("ADD_WORKFLOW")).html();
            vm.changeWorkflowTitle = parsed.html($translate.instant("CHANGE_WORKFLOW")).html();
            vm.back = back;
            vm.expand = false;
            vm.tabId = $stateParams.tab;
            vm.customTabs = [];

            function back() {
                $state.go('app.pm.programtemplate.all');
            }

            vm.tabs = {
                basic: {
                    id: 'details.basic',
                    heading: basic,
                    index: 0,
                    template: 'app/desktop/modules/pm/programTemplate/details/basic/programTemplateBasicInfoView.jsp',
                    active: false,
                    activated: true
                },
                resources: {
                    id: 'details.resources',
                    heading: 'Resources',
                    index: 1,
                    template: 'app/desktop/modules/pm/programTemplate/details/resources/programTemplateResourcesView.jsp',
                    active: true,
                    activated: false
                },
                projects: {
                    id: 'details.project',
                    heading: 'Projects',
                    index: 2,
                    template: 'app/desktop/modules/pm/programTemplate/details/project/programTemplateProjectsView.jsp',
                    active: true,
                    activated: false
                },
                files: {
                    id: 'details.files',
                    heading: 'Files',
                    index: 3,
                    template: 'app/desktop/modules/pm/programTemplate/details/files/programTemplateFilesView.jsp',
                    active: true,
                    activated: false
                },
                workflow: {
                    id: 'details.workflow',
                    heading: "Workflow",
                    index: 4,
                    template: 'app/desktop/modules/pm/programTemplate/details/workflow/programTemplateWorkflowView.jsp',
                    active: false,
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
                tabActive(tabId);
            }

            function tabActive(tabId) {
                $state.transitionTo('app.pm.programtemplate.details', {
                    templateId: $stateParams.templateId,
                    tab: tabId
                }, {notify: false});
                var tab = getTabById(tabId);
                vm.active = tab.index;
                if (tab != null) {
                    tab.activated = true;
                    tab.active = true;
                    activateTab(tab);
                    $scope.$broadcast('app.programTemplate.tabActivated', {tabId: tabId});

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

            vm.changeWorkflow = changeWorkflow;
            function changeWorkflow() {
                $scope.$broadcast('app.change.workflow');
            }

            vm.addWorkflow = addWorkflow;
            function addWorkflow() {
                $scope.$broadcast('app.add.workflow');
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
                    $scope.$broadcast('app.programTemplate.tabActivated', {tabId: 'details.files'});
                    $rootScope.searchModeType = false;
                }
            }

            vm.onClear = onClear;
            function onClear() {
                $scope.$broadcast('app.programTemplate.tabActivated', {tabId: 'details.files'});
            }

            $rootScope.loadProgramTemplateCounts = loadProgramTemplateCounts;
            function loadProgramTemplateCounts() {
                ProgramTemplateService.getProgramTemplateDetails($stateParams.templateId).then(
                    function (data) {
                        vm.tabCounts = data;
                        var resourcesTab = document.getElementById("program-resources");
                        var projectTab = document.getElementById("program-projects");
                        var tmplStr = "<span class='label label-default' style='margin-top:20px;background-color: #e4dddd;height: 20px;margin-left: 5px;'>{0}</span>";
                        if (resourcesTab != null) {
                            resourcesTab.lastElementChild.innerHTML = vm.tabs.resources.heading +
                                tmplStr.format(vm.tabCounts.resourcesCount);
                        }
                        if (projectTab != null) {
                            projectTab.lastElementChild.innerHTML = vm.tabs.projects.heading +
                                tmplStr.format(vm.tabCounts.projectCount);
                        }
                    }
                )
            }

            $rootScope.loadProgramTemplate = loadProgramTemplate;
            function loadProgramTemplate() {
                ProgramTemplateService.getProgramTemplate($stateParams.templateId).then(
                    function (data) {
                        vm.template = data;
                        $rootScope.template = data;
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                        $rootScope.hideBusyIndicator();
                    }
                )
            }

            (function () {
                loadProgramTemplateCounts();
                loadProgramTemplate();
            })();
        }
    }
);