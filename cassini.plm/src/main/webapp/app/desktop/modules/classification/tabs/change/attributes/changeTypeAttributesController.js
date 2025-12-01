define(
    [
        'app/desktop/modules/settings/settings.module',
        'app/desktop/modules/directives/classificationAttributesDirective'
    ],
    function (module) {
        module.controller('ChangeTypeAttributesController', ChangeTypeAttributesController);

        function ChangeTypeAttributesController($scope) {


            (function () {
                $scope.$on('app.changeType.tabactivated', function (event, data) {
                    if (data.tabId == 'details.attributes') {
                        $scope.$broadcast('app.classification.attribute', {});
                    }
                })
            })();

        }
    });