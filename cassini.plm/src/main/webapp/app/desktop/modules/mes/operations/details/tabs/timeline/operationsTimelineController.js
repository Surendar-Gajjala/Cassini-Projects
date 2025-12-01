define(
    [
        'app/desktop/modules/mes/mes.module',
        'app/desktop/modules/directives/timelineDirective'

    ],
    function (module) {
        module.controller('OperationTimeLineController', OperationTimeLineController);

        function OperationTimeLineController($scope, $rootScope, $timeout, $state, $stateParams, $cookies, $window, $application,
                                             $translate) {
            var vm = this;
            vm.operationId = $stateParams.operationId;
            (function () {

                $scope.$on('app.operation.tabActivated', function (event, args) {
                    if (args.tabId == 'details.timelineHistory') {
                        $scope.$broadcast('app.object.timeline', {});
                    }
                });

            })();
        }
    }
);