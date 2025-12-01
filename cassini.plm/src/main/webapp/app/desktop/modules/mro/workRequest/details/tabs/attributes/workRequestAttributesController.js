define(
    [
        'app/desktop/modules/mro/mro.module',
        'app/desktop/modules/directives/objectAttributeDetailsDirectiveController'
    ],
    function (module) {
        module.controller('WorkRequestAttributesController', WorkRequestAttributesController);

        function WorkRequestAttributesController($scope, $rootScope, $sce, $timeout, $state, $stateParams) {
            var vm = this;
            vm.workRequestId = $stateParams.workRequestId;

            (function () {
                $scope.$on('app.workRequest.tabActivated', function (event, data) {
                    if (data.tabId == 'details.attributes') {
                        $scope.$broadcast('app.attributes.tabActivated', {});

                    }
                })
            })();
        }
    }
);
