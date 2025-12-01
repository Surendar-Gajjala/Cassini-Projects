define(
    [
        'app/desktop/modules/settings/settings.module',
        'app/desktop/modules/directives/classificationAttributesDirective'
    ],
    function (module) {
        module.controller('QualityTypeAttributesController', QualityTypeAttributesController);

        function QualityTypeAttributesController($scope) {

            (function () {
                $scope.$on('app.qualityType.tabactivated', function (event, data) {
                    if (data.tabId == 'details.attributes') {
                        $scope.$broadcast('app.classification.attribute', {});
                    }
                })
            })();

        }
    });