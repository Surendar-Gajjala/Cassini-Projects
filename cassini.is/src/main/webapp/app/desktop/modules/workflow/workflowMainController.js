define(
    [
        'app/desktop/modules/workflow/workflow.module'
    ],
    function (module) {
        module.controller('WorkflowMainController', WorkflowMainController);

        function WorkflowMainController($scope, $rootScope, $timeout, $interval, $state, $cookies) {
            if ($application.homeLoaded == false) {
                return;
            }

            $rootScope.viewInfo.icon = "fa flaticon-plan2";
            $rootScope.viewInfo.title = "Workflows";

            (function () {

            })();
        }
    }
);