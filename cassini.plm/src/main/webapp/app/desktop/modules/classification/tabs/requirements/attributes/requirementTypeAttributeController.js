define(
    [
        'app/desktop/modules/settings/settings.module',
        'app/desktop/modules/directives/classificationAttributesDirective'
    ],
    function (module) {
        module.controller('RequirementTypeAttributeController', RequirementTypeAttributeController);

        function RequirementTypeAttributeController($scope) {


            (function () {
                $scope.$on('app.rmType.tabactivated', function (event, data) {
                    if (data.tabId == 'details.attributes') {
                        $scope.$broadcast('app.classification.attribute', {});
                    }
                })
            })();

        }
    });