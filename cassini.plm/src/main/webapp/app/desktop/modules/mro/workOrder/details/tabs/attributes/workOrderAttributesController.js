define(
    [
        'app/desktop/modules/mro/mro.module',
        'app/desktop/modules/directives/objectAttributeDetailsDirectiveController'
    ],
    function (module) {
        module.controller('WorkOrderAttributesController', WorkOrderAttributesController);

        function WorkOrderAttributesController($scope, $rootScope, $sce, $timeout, $state, $stateParams) {
            var vm = this;
            vm.workOrderId = $stateParams.workOrderId;

            (function () {
                $scope.$on('app.workOrder.tabActivated', function (event, data) {
                    if (data.tabId == 'details.attributes') {
                        $scope.$broadcast('app.attributes.tabActivated', {});

                    }
                })
            })();
        }
    }
);
