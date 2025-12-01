define(
    [
        'app/desktop/modules/classification/classification.module',
        'app/desktop/modules/directives/classificationAttributesDirective'
    ],
    function (module) {
        module.controller('WorkflowTypeAttributeController', WorkflowTypeAttributeController);

        function WorkflowTypeAttributeController($scope) {

            (function () {
                $scope.$on('app.workflowType.tabactivated', function (event, data) {
                    if (data.tabId == 'details.attributes') {
                        $scope.$broadcast('app.classification.attribute', {});
                    }
                })
            })();
        }
    }
)
;





