define(
    [
        'app/desktop/modules/mes/mes.module',
        'app/desktop/modules/directives/files/objectFilesDirectiveController'
    ],
    function (module) {
        module.controller('ProductionOrderFilesController', ProductionOrderFilesController);

        function ProductionOrderFilesController($scope, $rootScope, $timeout, $state, $stateParams, $q, $translate, $application) {
            var vm = this;
            vm.productionOrderId = $stateParams.productionOrderId;
            (function () {
                $scope.$on('app.productionOrder.tabActivated', function (event, data) {
                    if (data.tabId == 'details.files') {
                        $scope.$broadcast('app.objectFile.tabActivated', {});
                    }
                });
            })();
        }
    }
);
