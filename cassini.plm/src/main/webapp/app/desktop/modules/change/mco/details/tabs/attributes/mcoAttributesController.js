define(
    [
        'app/desktop/modules/change/change.module',
        'app/desktop/modules/directives/objectAttributeDetailsDirectiveController'
    ],
    function (module) {
        module.controller('MCOAttributesController', MCOAttributesController);

        function MCOAttributesController($scope, $rootScope, $sce, $timeout, $state, $stateParams) {
            var vm = this;

            vm.mcoId = $stateParams.mcoId;

            (function () {
                $scope.$on('app.mco.tabActivated', function (event, data) {
                    if (data.tabId == 'details.attribute') {
                        $scope.$broadcast('app.attributes.tabActivated', {});
                    }
                })
            })();
        }
    }
);
