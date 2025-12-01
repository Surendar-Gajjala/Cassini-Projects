define(
    [
        'app/desktop/modules/mes/mes.module',
        'app/desktop/modules/directives/objectAttributeDetailsDirectiveController'
    ],
    function (module) {
        module.controller('MachineAttributesController', MachineAttributesController);

        function MachineAttributesController($scope, $rootScope, $sce, $timeout, $state, $stateParams) {
            var vm = this;
            vm.instrumentId = $stateParams.instrumentId;

            (function () {
                $scope.$on('app.machine.tabActivated', function (event, data) {
                    if (data.tabId == 'details.attributes') {
                        $scope.$broadcast('app.attributes.tabActivated', {});
                    }
                })
            })();
        }
    }
);
