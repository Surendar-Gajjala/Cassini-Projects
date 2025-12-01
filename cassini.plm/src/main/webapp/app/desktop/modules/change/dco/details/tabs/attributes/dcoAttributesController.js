define(
    [
        'app/desktop/modules/change/change.module',
        'app/desktop/modules/directives/objectAttributeDetailsDirectiveController'
    ],
    function (module) {
        module.controller('DCOAttributesController', DCOAttributesController);

        function DCOAttributesController($scope, $rootScope, $sce, $timeout, $state, $stateParams) {
            var vm = this;

            vm.dcoId = $stateParams.dcoId;

            (function () {
                $scope.$on('app.dco.tabActivated', function (event, data) {
                    if (data.tabId == 'details.attribute') {
                        $scope.$broadcast('app.attributes.tabActivated', {});
                    }
                })
            })();
        }
    }
);
