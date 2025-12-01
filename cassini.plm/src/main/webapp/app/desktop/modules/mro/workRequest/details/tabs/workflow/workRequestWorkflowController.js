define(
    [
        'app/desktop/modules/mro/mro.module',
        'app/desktop/directives/workflow/workflowDirective'
    ],
    function (module, Modeler) {
        module.controller('WorkRequestWorkflowController', WorkRequestWorkflowController);

        function WorkRequestWorkflowController($scope, $rootScope, $timeout, $state, $translate, $stateParams, $cookies, $uibModal, httpFactory) {

            var vm = this;

            (function () {
                $scope.$on('app.workRequest.tabActivated', function (event, args) {
                    if (args.tabId == 'details.workflow') {
                        $scope.$broadcast('app.object.workflow', {});
                    }
                });
            })();
        }
    }
)
;