define(
    [
        'app/desktop/modules/customObject/customObject.module',
        'app/desktop/directives/workflow/workflowDirective'
    ],
    function (module) {
        module.controller('CustomObjectWorkflowController', CustomObjectWorkflowController);

        function CustomObjectWorkflowController($scope, $rootScope, $timeout, $state, $translate, $stateParams, $cookies, $uibModal, httpFactory) {

            var vm = this;

            (function () {
                $scope.$on('app.customObj.tabActivated', function (event, args) {
                    if (args.tabId == 'details.workflow') {
                        $scope.$broadcast('app.object.workflow', {});
                    }
                });
            })();
        }
    }
)
;


