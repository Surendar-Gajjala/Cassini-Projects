define(
    [
        'app/desktop/modules/mfr/mfr.module',
        'app/desktop/modules/directives/objectAttributeDetailsDirectiveController'
    ],
    function (module) {
        module.controller('MfrAttributesController', MfrAttributesController);

        function MfrAttributesController($scope, $rootScope, $sce, $timeout, $state, $stateParams, $window, $translate, $cookies) {
            var vm = this;

            vm.mfrId = $stateParams.manufacturerId;

            (function () {
                $scope.$on('app.mfr.tabactivated', function (event, data) {
                    if (data.tabId == 'details.attributes') {
                        $scope.$broadcast('app.attributes.tabActivated', {load: true});
                    }
                });
            })();
        }
    }
);

