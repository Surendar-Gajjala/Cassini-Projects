define(
    [
        'app/desktop/modules/req/reqdocs/requirement/requirement.module',
        'app/desktop/directives/workflow/workflowDirective'
    ],
    function (module) {
        module.controller('RequirementWorkflowController', RequirementWorkflowController);

        function RequirementWorkflowController($scope, $rootScope, $timeout, $state, $translate, $stateParams, $cookies, $uibModal, httpFactory) {

            var vm = this;

            (function () {
                $scope.$on('app.req.requirement.tabActivated', function (event, args) {
                    if (args.tabId == 'details.workflow') {
                        $scope.$broadcast('app.object.workflow', {});
                    }
                });
            })();
        }
    }
)
;


