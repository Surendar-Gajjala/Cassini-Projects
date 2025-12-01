define(
    [
        'app/desktop/modules/pqm/pqm.module',
        'app/desktop/directives/workflow/workflowDirective'
    ],
    function (module) {
        module.controller('SupplierAuditWorkflowController', SupplierAuditWorkflowController);

        function SupplierAuditWorkflowController($scope, $rootScope, $timeout, $state, $translate, $stateParams, $cookies, $uibModal, httpFactory) {

            var vm = this;
            vm.supplierAuditId = $stateParams.supplierAuditId;


            (function () {
                $scope.$on('app.supplierAudit.tabActivated', function (event, args) {
                    if (args.tabId == 'details.workflow') {
                        $scope.$broadcast('app.object.workflow', {});
                    }
                });
            })();
        }
    }
)
;



