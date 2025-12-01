define(
    [
        'app/desktop/modules/settings/settings.module',
        'app/desktop/modules/directives/timelineDirective'
    ],
    function (module) {
        module.controller('WorkflowTypeHistoryController', WorkflowTypeHistoryController);

        function WorkflowTypeHistoryController($q, $scope, $rootScope, $timeout, $state, DialogService, $stateParams, $cookies) {

            (function () {
                $scope.$on('app.workflowType.tabactivated', function (event, data) {
                    if (data.tabId == 'details.history') {
                        $scope.$broadcast('app.object.timeline', {});

                    }
                })
            })();

        }
    });