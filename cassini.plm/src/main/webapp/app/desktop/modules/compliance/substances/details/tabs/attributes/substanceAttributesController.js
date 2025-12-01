define(
    [
        'app/desktop/modules/compliance/compliance.module',
        'app/desktop/modules/directives/objectAttributeDetailsDirectiveController'
    ],
    function (module) {
        module.controller('SubstanceAttributesController', SubstanceAttributesController);

        function SubstanceAttributesController($scope, $rootScope, $sce, $timeout, $state, $stateParams) {
            var vm = this;
            vm.substanceId = $stateParams.substanceId;

            (function () {
                $scope.$on('app.substance.tabActivated', function (event, data) {
                    if (data.tabId == 'details.attributes') {
                        $scope.$broadcast('app.attributes.tabActivated', {});

                    }
                })
            })();
        }
    }
);
