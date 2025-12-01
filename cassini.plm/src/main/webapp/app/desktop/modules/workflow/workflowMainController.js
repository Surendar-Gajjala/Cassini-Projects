define(
    [
        'app/desktop/modules/workflow/workflow.module'
    ],
    function (module) {
        module.controller('WorkflowMainController', WorkflowMainController);

        function WorkflowMainController($scope, $rootScope, $timeout, $interval, $state, $cookies) {

            $rootScope.viewInfo.icon = "fa flaticon-plan2";
            $rootScope.viewInfo.title = "Workflows";

            (function () {

            })();
        }
    }
);