define(
    [
        'app/desktop/modules/change/change.module',
        'app/desktop/directives/workflow/workflowDirective'
    ],
    function (module) {
        module.controller('DCOWorkflowController', DCOWorkflowController);

        function DCOWorkflowController($scope, $rootScope, $timeout, $state, $translate, $stateParams, $cookies, $uibModal, httpFactory) {

            var vm = this;

            (function () {
                $scope.$on('app.dco.tabActivated', function (event, args) {
                    if (args.tabId == 'details.workflow') {
                        $scope.$broadcast('app.object.workflow', {});
                    }
                });
            })();
        }
    }
)
;