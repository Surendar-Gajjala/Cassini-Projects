define(
    [
        'app/desktop/modules/req/req.module',
        'app/desktop/directives/workflow/workflowDirective'
    ],
    function (module) {
        module.controller('ReqDocumentWorkflowController', ReqDocumentWorkflowController);

        function ReqDocumentWorkflowController($scope, $rootScope, $timeout, $state, $translate, $stateParams, $cookies, $uibModal, httpFactory) {

            var vm = this;

            (function () {
                $scope.$on('app.req.document.tabActivated', function (event, args) {
                    if (args.tabId == 'details.workflow') {
                        $scope.$broadcast('app.object.workflow', {});
                    }
                });
            })();
        }
    }
)
;


