define(
    [
        'app/desktop/modules/rm/rm.module',
        'app/desktop/directives/workflow/workflowDirective'
    ],
    function (module) {
        module.controller('SpecWorkflowController', SpecWorkflowController);

        function SpecWorkflowController($scope, $rootScope, $timeout, $state, $translate, $stateParams, $cookies, $uibModal, httpFactory) {

            var vm = this;

            (function () {
                $scope.$on('app.spec.tabactivated', function (event, args) {
                    if (args.tabId == 'details.workflow') {
                        $scope.$broadcast('app.object.workflow', {});
                    }
                });
            })();
        }
    }
)
;


