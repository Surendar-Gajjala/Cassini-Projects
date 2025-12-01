define(
    [
        'app/desktop/modules/mro/mro.module',
        'app/desktop/modules/directives/objectAttributeDetailsDirectiveController'
    ],
    function (module) {
        module.controller('SparePartAttributesController', SparePartAttributesController);

        function SparePartAttributesController($scope, $rootScope, $sce, $timeout, $state, $stateParams) {
            var vm = this;
            vm.sparePartId = $stateParams.sparePartId;

            (function () {
                $scope.$on('app.sparePart.tabActivated', function (event, data) {
                    if (data.tabId == 'details.attributes') {
                        $scope.$broadcast('app.attributes.tabActivated', {});

                    }
                })
            })();
        }
    }
);
