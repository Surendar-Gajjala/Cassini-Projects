define(
    [
        'app/desktop/modules/mes/mes.module',
        'app/desktop/modules/directives/objectAttributeDetailsDirectiveController'
    ],
    function (module) {
        module.controller('EquipmentAttributesController', EquipmentAttributesController);

        function EquipmentAttributesController($scope, $rootScope, $sce, $timeout, $state, $stateParams) {
            var vm = this;
            vm.equipmentId = $stateParams.equipmentId;

            (function () {
                $scope.$on('app.equipment.tabActivated', function (event, data) {
                    if (data.tabId == 'details.attributes') {
                        $scope.$broadcast('app.attributes.tabActivated', {});

                    }
                })
            })();
        }
    }
);
