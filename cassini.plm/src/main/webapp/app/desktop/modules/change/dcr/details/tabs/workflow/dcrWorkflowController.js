define(
    [
        'app/desktop/modules/change/change.module',
        'app/desktop/directives/workflow/workflowDirective'
    ],
    function (module, Modeler) {
        module.controller('DCRWorkflowController', DCRWorkflowController);

        function DCRWorkflowController($scope, $rootScope, $timeout, $state, $translate, $stateParams, $cookies, $uibModal, httpFactory) {

            var vm = this;

            (function () {
                $scope.$on('app.dcr.tabActivated', function (event, args) {
                    if (args.tabId == 'details.workflow') {
                        $scope.$broadcast('app.object.workflow', {});
                    }
                });
            })();
        }
    }
)
;