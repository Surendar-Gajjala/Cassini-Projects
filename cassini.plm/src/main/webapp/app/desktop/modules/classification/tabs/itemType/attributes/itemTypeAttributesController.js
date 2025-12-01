define(
    [
        'app/desktop/modules/settings/settings.module',
        'app/desktop/modules/directives/classificationAttributesDirective'
    ],
    function (module) {
        module.controller('ItemTypeAttributesController', ItemTypeAttributesController);

        function ItemTypeAttributesController($scope) {

            (function () {
                $scope.$on('app.itemType.tabactivated', function (event, data) {
                    if (data.tabId == 'details.attributes') {
                        $scope.$broadcast('app.classification.attribute', {});
                    }
                })
            })();
        }
    }
)
;