define(
    [
        'app/desktop/modules/mes/mes.module',
        'app/desktop/directives/workflow/workflowDirective'
    ],
    function (module) {
        module.controller('BOPWorkflowController', BOPWorkflowController);

        function BOPWorkflowController($scope, $rootScope, $timeout, $state, $translate, $stateParams, $cookies, $uibModal, httpFactory) {

            var vm = this;

            (function () {
                $scope.$on('app.bop.tabActivated', function (event, args) {
                    if (args.tabId == 'details.workflow') {
                        $scope.$broadcast('app.object.workflow', {});
                    }
                });
            })();
        }
    }
)
;



