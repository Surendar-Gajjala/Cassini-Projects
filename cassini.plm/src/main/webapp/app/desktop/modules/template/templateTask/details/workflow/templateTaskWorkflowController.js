define(
    [
        'app/desktop/modules/template/templateActivity/templateActivity.module',
        'app/desktop/directives/workflow/workflowDirective'
    ],
    function (module) {
        module.controller('TemplateTaskWorkflowController', TemplateTaskWorkflowController);

        function TemplateTaskWorkflowController($scope, $rootScope, $timeout, $state, $translate, $stateParams, $cookies, $uibModal, httpFactory) {

            var vm = this;

            (function () {
                $scope.$on('app.template.task.tabActivated', function (event, args) {
                    if (args.tabId == 'details.workflow') {
                        $scope.$broadcast('app.object.workflow', {});
                    }
                });
            })();
        }
    }
)
;


