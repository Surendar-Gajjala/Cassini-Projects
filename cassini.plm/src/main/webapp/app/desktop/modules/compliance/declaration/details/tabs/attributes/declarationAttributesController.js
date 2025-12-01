define(
    [
        'app/desktop/modules/compliance/compliance.module',
        'app/desktop/modules/directives/objectAttributeDetailsDirectiveController'
    ],
    function (module) {
        module.controller('DeclarationAttributesController', DeclarationAttributesController);

        function DeclarationAttributesController($scope, $rootScope, $sce, $timeout, $state, $stateParams) {
            var vm = this;
            vm.declarationId = $stateParams.declarationId;

            (function () {
                $scope.$on('app.declaration.tabActivated', function (event, data) {
                    if (data.tabId == 'details.attributes') {
                        $scope.$broadcast('app.attributes.tabActivated', {});

                    }
                })
            })();
        }
    }
);
