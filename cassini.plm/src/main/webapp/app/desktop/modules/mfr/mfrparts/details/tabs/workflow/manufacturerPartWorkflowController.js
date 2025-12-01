define(
    [
        'app/desktop/modules/mfr/mfrparts/mfrparts.module',
        'app/desktop/directives/workflow/workflowDirective'

    ],
    function (module) {
        module.controller('ManufacturerPartWorkflowController', ManufacturerPartWorkflowController);

        function ManufacturerPartWorkflowController($scope, $rootScope, $timeout, $state, $translate, $stateParams, $cookies, $uibModal) {

            var vm = this;

            (function () {
                $scope.$on('app.mfrPart.tabactivated', function (event, args) {
                    if (args.tabId == 'details.workflow') {
                        $scope.$broadcast('app.object.workflow', {});
                    }
                });
            })();
        }
    }
)
;


