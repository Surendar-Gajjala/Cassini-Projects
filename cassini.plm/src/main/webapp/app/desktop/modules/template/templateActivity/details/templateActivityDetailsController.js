define(
    [
        'app/desktop/modules/template/templateActivity/templateActivity.module',
        'app/desktop/modules/template/templateActivity/details/basic/templateActivityBasicInfoController',
        'app/desktop/modules/template/templateActivity/details/files/templateActivityFilesController',
        'app/desktop/modules/template/templateActivity/details/workflow/templateActivityWorkflowController',
        'app/shared/services/core/templateActivityService'

    ],
    function (module) {
        module.controller('TemplateActivityDetailsController', TemplateActivityDetailsController);

        function TemplateActivityDetailsController($scope, $stateParams, $rootScope, $sce, $translate, $timeout, $state,
                                                   $window, TemplateActivityService) {

            var vm = this;
            var activityId = $stateParams.activityId;
            vm.templateDetailsTabActivated = templateDetailsTabActivated;
            vm.back = back;

            function back() {
                window.history.back();
            }

            var parsed = angular.element("<div></div>");

            var basic = parsed.html($translate.instant("DETAILS_TAB_BASIC")).html();
            var task = parsed.html($translate.instant("TASKS")).html();
            vm.tabs = {
                basic: {
                    id: 'details.basic',
                    heading: basic,
                    template: 'app/desktop/modules/template/templateActivity/details/basic/templateActivityBasicInfoView.jsp',
                    active: true,
                    index: 0,
                    activated: true
                },
                files: {
                    id: 'details.files',
                    heading: "Files",
                    template: 'app/desktop/modules/template/templateActivity/details/files/templateActivityFilesView.jsp',
                    active: false,
                    index: 1,
                    activated: false
                },
                workflow: {
                    id: 'details.workflow',
                    heading: "Workflow",
                    template: 'app/desktop/modules/template/templateActivity/details/workflow/templateActivityWorkflowView.jsp',
                    active: false,
                    index: 2,
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
                if (tab != null) {
                    tab.activated = true;
                    $scope.$broadcast('app.template.activity.tabActivated', {tabId: tabId});

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

                return tab;
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
                    $scope.$broadcast('app.template.activity.tabActivated', {tabId: 'details.files'});
                    $rootScope.searchModeType = false;
                }
            }

            vm.onClear = onClear;
            function onClear() {
                $scope.$broadcast('app.template.activity.tabActivated', {tabId: 'details.files'});
            }

            $rootScope.loadTemplateActivity = loadTemplateActivity;
            function loadTemplateActivity() {
                TemplateActivityService.getTemplateActivity(activityId).then(
                    function (data) {
                        vm.activity = data;
                        $rootScope.activity = data;
                        vm.activityName = vm.activity.name;
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                        $rootScope.hideBusyIndicator();
                    }
                )
            }

            (function () {
                loadTemplateActivity();
            })();

        }
    }
)
;