define(
    [
        'app/desktop/modules/mes/mes.module',
        'app/desktop/modules/directives/files/objectFilesDirectiveController'
    ],
    function (module) {
        module.controller('EquipmentFilesController', EquipmentFilesController);

        function EquipmentFilesController($scope, $rootScope, $timeout, $state, $stateParams, $q, $translate, $application) {
            var vm = this;
            vm.equipmentId = $stateParams.equipmentId;

            (function () {
                $scope.$on('app.equipment.tabActivated', function (event, data) {
                    if (data.tabId == 'details.files') {
                        $timeout(function () {
                            $scope.$broadcast('app.objectFile.tabActivated', {});
                        }, 500);
                    }
                });
            })();
        }
    }
);

