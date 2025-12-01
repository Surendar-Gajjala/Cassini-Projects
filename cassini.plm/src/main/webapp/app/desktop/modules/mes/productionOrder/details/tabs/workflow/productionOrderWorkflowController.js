define(
    [
        'app/desktop/modules/mes/mes.module',
        'app/desktop/directives/workflow/workflowDirective'
    ],
    function (module) {
        module.controller('ProductionOrderWorkflowController', ProductionOrderWorkflowController);

        function ProductionOrderWorkflowController($scope, $rootScope, $timeout, $state, $translate, $stateParams, $cookies, $uibModal, httpFactory) {

            var vm = this;

            (function () {
                $scope.$on('app.productionOrder.tabActivated', function (event, args) {
                    if (args.tabId == 'details.workflow') {
                        $scope.$broadcast('app.object.workflow', {});
                    }
                });
            })();
        }
    }
)
;



