define(
    [
        'app/desktop/modules/pqm/pqm.module',
        'app/desktop/directives/workflow/workflowDirective'

    ],
    function (module) {
        module.controller('PlanWorkflowController', PlanWorkflowController);

        function PlanWorkflowController($scope, $rootScope, $timeout, $state, $translate, $stateParams, $cookies, $uibModal, httpFactory) {

            var vm = this;
            vm.planId = $stateParams.planId;

            (function () {
                $scope.$on('app.inspectionPlan.tabActivated', function (event, args) {
                    if (args.tabId == 'details.workflow') {
                        $scope.$broadcast('app.object.workflow', {});
                    }
                });
            })();
        }
    }
)
;


