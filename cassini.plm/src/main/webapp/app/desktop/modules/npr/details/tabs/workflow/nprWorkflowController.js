define(
    [
        'app/desktop/modules/npr/npr.module',
        'app/desktop/directives/workflow/workflowDirective'
    ],
    function (module) {
        module.controller('NprWorkflowController', NprWorkflowController);

        function NprWorkflowController($scope, $rootScope, $timeout, $state, $translate, $stateParams, $cookies, $uibModal, httpFactory) {

            var vm = this;

            (function () {
                $scope.$on('app.npr.tabActivated', function (event, args) {
                    if (args.tabId == 'details.workflow') {
                        $scope.$broadcast('app.object.workflow', {});
                    }
                });
            })();
        }
    }
)
;


