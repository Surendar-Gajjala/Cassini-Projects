define(
    [
        'app/desktop/modules/mes/mes.module',
    ],
    function (module) {
        module.controller('MBOMAttributesController', MBOMAttributesController);

        function MBOMAttributesController($scope, $rootScope, $sce, $timeout, $state, $stateParams) {
            var vm = this;

            vm.plantId = $stateParams.plantId;

            (function () {
                $scope.$on('app.mbom.tabActivated', function (event, data) {
                    if (data.tabId == 'details.attribute') {
                        $scope.$broadcast('app.attributes.tabActivated', {});
                    }
                })
            })();
        }
    }
);
