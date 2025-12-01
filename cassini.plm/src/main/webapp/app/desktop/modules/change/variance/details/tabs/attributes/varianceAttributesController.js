define(
    [
        'app/desktop/modules/change/change.module',
        'app/desktop/modules/directives/objectAttributeDetailsDirectiveController'
    ],
    function (module) {
        module.controller('VarianceAttributesController', VarianceAttributesController);

        function VarianceAttributesController($scope, $rootScope, $sce, $timeout, $state, $stateParams) {
            var vm = this;

            vm.varianceId = $stateParams.varianceId;

            (function () {
                $scope.$on('app.variance.tabactivated', function (event, data) {
                    if (data.tabId == 'details.attribute') {
                        $scope.$broadcast('app.attributes.tabActivated', {});
                    }
                })
            })();
        }
    }
);