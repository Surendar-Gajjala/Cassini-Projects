define(
    [
        'app/desktop/modules/template/templateTask/templateTask.module',
        'app/desktop/modules/template/templateTask/details/basic/templateTaskBasicInfoController',
        'app/desktop/modules/template/templateTask/details/files/templateTaskFilesController',
        'app/desktop/modules/template/templateTask/details/workflow/templateTaskWorkflowController',
        'app/shared/services/core/templateActivityService'

    ],
    function (module) {
        module.controller('TemplateTaskDetailsController', TemplateTaskDetailsController);

        function TemplateTaskDetailsController($scope, $stateParams, $rootScope, $sce, $translate, $timeout, $state,
                                               $window, TemplateActivityService) {

            var vm = this;
            var taskId = $stateParams.taskId;
            vm.templateDetailsTabActivated = templateDetailsTabActivated;
            vm.back = back;

            function back() {
                window.history.back();
            }

            var parsed = angular.element("<div></div>");

            var basic = parsed.html($translate.instant("DETAILS_TAB_BASIC")).html();

            vm.tabs = {
                basic: {
                    id: 'details.basic',
                    heading: basic,
                    template: 'app/desktop/modules/template/templateTask/details/basic/templateTaskBasicInfoView.jsp',
                    active: true,
                    activated: true
                },
                files: {
                    id: 'details.files',
                    heading: "Files",
                    template: 'app/desktop/modules/template/templateTask/details/files/templateTaskFilesView.jsp',
                    active: false,
                    activated: false
                },
                workflow: {
                    id: 'details.workflow',
                    heading: "Workflow",
                    template: 'app/desktop/modules/template/templateTask/details/workflow/templateTaskWorkflowView.jsp',
                    active: false,
                    activated: false
                }
            };

            function activateTab(tab) {
                for (var t in vm.tabs) {
                    vm.tabs[t].active = (vm.tabs[t].id == tab.id);
                }
            }

            function templateDetailsTabActivated(tabId) {
                var tab = getTabById(tabId);
                if (tab != null && !tab.activated) {
                    tab.activated = true;
                    $scope.$broadcast('app.template.task.tabActivated', {tabId: tabId});

                }
                if (tab != null) {
                    activateTab(tab);
                }
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
                    $scope.$broadcast('app.template.task.tabActivated', {tabId: 'details.files'});
                    $rootScope.searchModeType = false;
                }
            }

            vm.onClear = onClear;
            function onClear() {
                $scope.$broadcast('app.template.task.tabActivated', {tabId: 'details.files'});           
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

            vm.addWorkflowTitle = parsed.html($translate.instant("ADD_WORKFLOW")).html();
            vm.changeWorkflowTitle = parsed.html($translate.instant("CHANGE_WORKFLOW")).html();
            vm.changeWorkflow = changeWorkflow;
            function changeWorkflow() {
                $scope.$broadcast('app.change.workflow');
            }

            vm.addWorkflow = addWorkflow;
            function addWorkflow() {
                $scope.$broadcast('app.add.workflow');
            }

            $rootScope.loadTemplateTask = loadTemplateTask;
            function loadTemplateTask() {
                TemplateActivityService.getTemplateTask(taskId).then(
                    function (data) {
                        vm.task = data;
                        $rootScope.task = data;
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                        $rootScope.hideBusyIndicator();
                    }
                )
            }

            (function () {
                loadTemplateTask();
            })();

        }
    }
)
;