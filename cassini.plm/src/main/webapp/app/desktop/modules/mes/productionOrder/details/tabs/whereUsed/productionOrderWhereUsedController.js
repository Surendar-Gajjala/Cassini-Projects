define(
    [
        'app/desktop/modules/mes/mes.module',
        'app/shared/services/core/productionOrderService'

    ],
    function (module) {
        module.controller('ProductionOrderWhereUsedController', ProductionOrderWhereUsedController);

        function ProductionOrderWhereUsedController($scope, $rootScope, $timeout, $state, $stateParams, $translate, $cookies, $window) {
            var vm = this;

            vm.loading = true;



            (function () {
                $scope.$on('app.productionOrder.tabActivated', function (event, data) {
                    if (data.tabId == 'details.whereUsed') {

                    }
                });
            })();
        }
    }
);