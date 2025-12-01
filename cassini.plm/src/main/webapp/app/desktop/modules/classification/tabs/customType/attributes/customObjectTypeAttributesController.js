define(
    [
        'app/desktop/modules/settings/settings.module',
        'app/desktop/modules/directives/classificationAttributesDirective'
    ],
    function (module) {
        module.controller('CustomObjectTypeAttributesController', CustomObjectTypeAttributesController);

        function CustomObjectTypeAttributesController($scope) {

            (function () {
                $scope.$on('app.customType.tabActivated', function (event, data) {
                    if (data.tabId == 'details.attributes') {
                        $scope.$broadcast('app.classification.attribute', {});
                    }
                })
            })();
        }
    }
)
;