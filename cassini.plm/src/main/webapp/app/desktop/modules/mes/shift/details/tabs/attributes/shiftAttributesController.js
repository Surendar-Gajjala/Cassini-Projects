define(
    [
        'app/desktop/modules/mes/mes.module',
    ],
    function (module) {
        module.controller('ShiftAttributesController', ShiftAttributesController);

        function ShiftAttributesController($scope, $rootScope, $sce, $timeout, $state, $stateParams) {
            var vm = this;
            vm.shiftId = $stateParams.shiftId;

            (function () {
                $scope.$on('app.shift.tabActivated', function (event, data) {
                    if (data.tabId == 'details.attributes') {

                    }
                })
            })();
        }
    }
);
