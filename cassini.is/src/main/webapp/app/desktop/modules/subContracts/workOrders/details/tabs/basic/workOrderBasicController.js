/**
 * Created by swapna on 22/01/19.
 */
define(['app/desktop/modules/subContracts/contracts.module',
        'app/shared/services/core/subContractService',
        'app/shared/services/pm/project/projectService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/commonService',
        'app/assets/bower_components/cassini-platform/app/desktop/directives/attributes/attributesDetailsDirectiveController'
    ],
    function (module) {
        module.controller('WorkOrderBasicController', WorkOrderBasicController);

        function WorkOrderBasicController($scope, $rootScope, $timeout, $state, $cookies, $stateParams,
                                          SubContractService, ProjectService, CommonService) {

            var vm = this;
            vm.workOrder = null;
            vm.loading = true;

            function loadWorkOrder() {
                vm.loading = true;
                SubContractService.getWorkOrder($stateParams.workOrderId).then(
                    function (data) {
                        $rootScope.workOrder = data;
                        vm.workOrder = $rootScope.workOrder;
                        $rootScope.viewInfo.description = data.number;
                        vm.loading = false;
                    }
                );
            }

            (function () {
                loadWorkOrder();
            })();
        }
    }
);