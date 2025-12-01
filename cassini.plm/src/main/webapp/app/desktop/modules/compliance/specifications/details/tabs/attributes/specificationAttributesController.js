define(
    [
        'app/desktop/modules/compliance/compliance.module',
        'app/desktop/modules/directives/objectAttributeDetailsDirectiveController'
    ],
    function (module) {
        module.controller('SpecificationAttributesController', SpecificationAttributesController);

        function SpecificationAttributesController($scope, $rootScope, $sce, $timeout, $state, $stateParams) {
            var vm = this;
            vm.specificationId = $stateParams.specificationId;

            (function () {
                $scope.$on('app.specification.tabActivated', function (event, data) {
                    if (data.tabId == 'details.attributes') {
                        $scope.$broadcast('app.attributes.tabActivated', {});

                    }
                })
            })();
        }
    }
);
