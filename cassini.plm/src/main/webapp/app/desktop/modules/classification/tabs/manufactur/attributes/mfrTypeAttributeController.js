define(
    [
        'app/desktop/modules/classification/classification.module',
        'app/desktop/modules/directives/classificationAttributesDirective'
    ],
    function (module) {
        module.controller('MfrTypeAttributeController', MfrTypeAttributeController);

        function MfrTypeAttributeController($scope) {

            (function () {
                $scope.$on('app.mfrType.tabactivated', function (event, data) {
                    $scope.$broadcast('app.classification.attribute', {});
                })
            })();
        }
    }
);