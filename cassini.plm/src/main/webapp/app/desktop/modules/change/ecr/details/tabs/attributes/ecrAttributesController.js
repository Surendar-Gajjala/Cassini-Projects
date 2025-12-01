define(
    [
        'app/desktop/modules/change/change.module',
        'app/desktop/modules/directives/objectAttributeDetailsDirectiveController'
    ],
    function (module) {
        module.controller('ECRAttributesController', ECRAttributesController);

        function ECRAttributesController($scope, $rootScope, $sce, $timeout, $state, $stateParams) {
            var vm = this;

            vm.ecrId = $stateParams.ecrId;

            (function () {
                $scope.$on('app.ecr.tabActivated', function (event, data) {
                    if (data.tabId == 'details.attribute') {
                        $scope.$broadcast('app.attributes.tabActivated', {});
                    }
                })
            })();
        }
    }
);
