define(
    [
        'app/desktop/modules/pm/pm.module',
        'app/desktop/directives/workflow/workflowDirective'
    ],
    function (module) {
        module.controller('ProjectWorkflowController', ProjectWorkflowController);

        function ProjectWorkflowController($scope, $rootScope, $timeout, $state, $translate, $stateParams, $cookies, $uibModal, httpFactory) {

            var vm = this;

            (function () {
                $scope.$on('app.project.tabactivated', function (event, args) {
                    if (args.tabId == 'details.workflow') {
                        $scope.$broadcast('app.object.workflow', {});
                    }
                });
            })();
        }
    }
)
;


