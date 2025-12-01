define(
    [
        'app/desktop/modules/classification/classification.module',
        'app/desktop/modules/directives/classificationAttributesDirective'
    ],
    function (module) {
        module.controller('PMObjectTypeAttributesController', PMObjectTypeAttributesController);

        function PMObjectTypeAttributesController($scope) {

            (function () {
                $scope.$on('app.pmType.tabActivated', function (event, data) {
                    if (data.tabId == 'details.attributes') {
                        $scope.$broadcast('app.classification.attribute', {});
                    }
                })
            })();

        }
    });