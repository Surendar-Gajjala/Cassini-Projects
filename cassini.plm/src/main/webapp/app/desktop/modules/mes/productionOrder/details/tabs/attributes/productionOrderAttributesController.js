define(
    [
        'app/desktop/modules/mes/mes.module'
    ],
    function (module) {
        module.controller('ProductionOrderAttributesController', ProductionOrderAttributesController);

        function ProductionOrderAttributesController($scope, $rootScope, $sce, $timeout, $state, $stateParams) {
            var vm = this;

            vm.productionorderId = $stateParams.productionorderId;

            (function () {
                $scope.$on('app.productionOrder.tabActivated', function (event, data) {
                    if (data.tabId == 'details.attribute') {
                        $scope.$broadcast('app.attributes.tabActivated', {});
                    }
                })
            })();
        }
    }
);