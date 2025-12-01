define(
    [
        'app/desktop/modules/mfr/mfr.module',
        'app/desktop/directives/workflow/workflowDirective'
    ],
    function (module, Modeler) {
        module.controller('ManufacturerWorkflowController', ManufacturerWorkflowController);

        function ManufacturerWorkflowController($scope, $rootScope, $timeout, $state, $translate, $stateParams, $cookies, $uibModal) {

            var vm = this;

            (function () {
                $scope.$on('app.mfr.tabactivated', function (event, args) {
                    if (args.tabId == 'details.workflow') {
                        $scope.$broadcast('app.object.workflow', {});
                    }
                });
            })();
        }
    }
)
;


