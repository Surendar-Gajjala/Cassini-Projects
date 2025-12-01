define(
    [
        'app/desktop/modules/change/change.module',
        'app/desktop/directives/workflow/workflowDirective'
    ],
    function (module) {
        module.controller('ECOWorkflowController', ECOWorkflowController);

        function ECOWorkflowController($scope, $rootScope, $timeout, $state, $translate, $stateParams, $cookies, $uibModal, httpFactory) {

            var vm = this;

            (function () {
                $scope.$on('app.eco.tabactivated', function (event, args) {
                    if (args.tabId == 'details.workflow') {
                        $scope.$broadcast('app.object.workflow', {});
                    }
                });
            })();
        }
    }
);