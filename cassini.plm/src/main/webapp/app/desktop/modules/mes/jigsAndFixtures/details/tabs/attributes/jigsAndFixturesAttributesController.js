define(
    [
        'app/desktop/modules/mes/mes.module',
        'app/desktop/modules/directives/objectAttributeDetailsDirectiveController'
    ],
    function (module) {
        module.controller('JigsAndFixturesAttributesController', JigsAndFixturesAttributesController);

        function JigsAndFixturesAttributesController($scope, $rootScope, $sce, $timeout, $state, $stateParams) {

            var vm = this;
            vm.jigsFixId = $stateParams.jigsFixId;

            (function () {
                $scope.$on('app.jigsAndFixtures.tabActivated', function (event, data) {
                    if (data.tabId == 'details.attributes') {
                        $scope.$broadcast('app.attributes.tabActivated', {});
                    }
                })
            })();
        }
    }
);