define(
    [
        'app/desktop/modules/rm/rm.module',
        'app/desktop/modules/directives/objectAttributeDetailsDirectiveController'
    ],
    function (module) {
        module.controller('SpecAttributesController', SpecAttributesController);

        function SpecAttributesController($scope, $rootScope, $sce, $timeout, $state, $stateParams, $window, $translate, $cookies) {
            var vm = this;

            vm.specId = $stateParams.specId;

            (function () {
                $scope.$on('app.spec.tabactivated', function (event, data) {
                    if (data.tabId == 'details.attributes') {
                        $scope.$broadcast('app.attributes.tabActivated', {load: true});
                    }
                });
            })();
        }
    }
);
