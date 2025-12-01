define(
    [
        'app/desktop/modules/classification/classification.module',
        'app/desktop/modules/directives/classificationAttributesDirective'
    ],
    function (module) {
        module.controller('MfrPartTypeAttributeController', MfrPartTypeAttributeController);

        function MfrPartTypeAttributeController($scope) {


            (function () {
                $scope.$on('app.mfrPartType.tabactivated', function (event, data) {
                    if (data.tabId == 'details.attributes') {
                        $scope.$broadcast('app.classification.attribute', {});
                    }
                })
            })();
        }
    });