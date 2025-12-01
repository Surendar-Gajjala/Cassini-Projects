define(
    [
        'app/desktop/modules/mro/mro.module',
        'app/desktop/modules/directives/objectAttributeDetailsDirectiveController'
    ],
    function (module) {
        module.controller('MeterAttributesController', MeterAttributesController);

        function MeterAttributesController($scope, $rootScope, $sce, $timeout, $state, $stateParams) {
            var vm = this;
            vm.meterId = $stateParams.meterId;

            (function () {
                $scope.$on('app.meter.tabActivated', function (event, data) {
                    if (data.tabId == 'details.attributes') {
                        $scope.$broadcast('app.attributes.tabActivated', {});

                    }
                })
            })();
        }
    }
);
