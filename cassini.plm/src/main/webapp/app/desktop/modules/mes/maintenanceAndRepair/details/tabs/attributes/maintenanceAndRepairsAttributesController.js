define(
    [
        'app/desktop/modules/change/change.module',
    ],
    function (module) {
        module.controller('MaintenanceAndRepairAttributesController', MaintenanceAndRepairAttributesController);

        function MaintenanceAndRepairAttributesController($scope, $rootScope, $sce, $timeout, $state, $stateParams) {
            var vm = this;
            vm.maintenanceAndRepairId = $stateParams.maintenanceAndRepairId;

            (function () {
                $scope.$on('app.maintenanceAndRepair.tabActivated', function (event, data) {
                    if (data.tabId == 'details.attributes') {

                    }
                })
            })();
        }
    }
);
