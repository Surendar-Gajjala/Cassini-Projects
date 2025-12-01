define(
    [
        'app/desktop/modules/mfr/mfr.module',
        'app/desktop/modules/directives/objectAttributeDetailsDirectiveController'
    ],
    function (module) {
        module.controller('SupplierAttributesController', SupplierAttributesController);

        function SupplierAttributesController($scope, $rootScope, $sce, $timeout, $state, $stateParams) {
            var vm = this;

            vm.supplierId = $stateParams.supplierId;

            (function () {
                $scope.$on('app.supplier.tabActivated', function (event, data) {
                    if (data.tabId == 'details.attributes') {
                        $scope.$broadcast('app.attributes.tabActivated', {});
                    }
                })
            })();
        }
    }
);
