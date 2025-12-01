define(
    [
        'app/desktop/modules/mfr/mfrparts/mfrparts.module',
        'app/desktop/modules/directives/objectAttributeDetailsDirectiveController'
    ],
    function (module) {
        module.controller('MfrPartsAttributesController', MfrPartsAttributesController);

        function MfrPartsAttributesController($scope, $rootScope, $sce, $timeout, $state, $stateParams, $window, $translate, $cookies) {
            var vm = this;

            vm.mfId = $stateParams.manufacturePartId;

            (function () {
                $scope.$on('app.mfrPart.tabactivated', function (event, data) {
                    if (data.tabId == 'details.attributes') {
                        $scope.$broadcast('app.attributes.tabActivated', {load: true});
                    }
                });
            })();
        }
    }
);

