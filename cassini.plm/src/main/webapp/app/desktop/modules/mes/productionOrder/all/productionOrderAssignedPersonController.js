define(
    [
        'app/desktop/modules/mes/mes.module',
        'moment',
        'moment-timezone-with-data',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/autonumberService',
        'app/shared/services/core/productionOrderService',
        'app/shared/services/core/shiftService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/objectAttributeService'
    ],
    function (module) {

        module.controller('ProductionOrderAssignedPersonController', ProductionOrderAssignedPersonController);

        function ProductionOrderAssignedPersonController($scope, $q, $rootScope, $translate, $timeout, $state, $stateParams, $cookies, $sce,
                                                         AutonumberService, ProductionOrderService, ObjectTypeAttributeService, ShiftService) {

            var vm = this;

            var parsed = angular.element("<div></div>");
            vm.productionOrderId = $scope.data.productionOrder;

            function loadShiftPersons() {
                ShiftService.getPOShiftPersons().then(
                    function (data) {
                        vm.shifts = data;
                    }
                )
            }

            function create() {
                vm.productionOrder.shift = vm.shift.id;
                ProductionOrderService.updateProductionOrder(vm.productionOrder).then(
                    function (data) {
                        $scope.callback(vm.productionOrder);
                        $rootScope.hideSidePanel();
                        $rootScope.showSuccessMessage("ProductionOrder update successfully");
                    }
                )
            }

            function loadProductionOrder() {
                ProductionOrderService.getProductionOrder(vm.productionOrderId).then(
                    function (data) {
                        vm.productionOrder = data;
                    }
                )
            }

            (function () {
                loadShiftPersons();
                loadProductionOrder();
                $rootScope.$on('app.productionOrder.update', create);
            })();
        }
    }
);