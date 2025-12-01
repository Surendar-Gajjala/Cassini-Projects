define(
    [
        'app/desktop/modules/mes/mes.module',
        'app/desktop/modules/directives/timelineDirective'

    ],
    function (module) {
        module.controller('MachineTimeLineController', MachineTimeLineController);

        function MachineTimeLineController($scope, $rootScope, $timeout, $state, $stateParams, $cookies, $window, $application,
                                           $translate) {
            var vm = this;
            vm.machineId = $stateParams.machineId;

            (function () {

                $scope.$on('app.machine.tabActivated', function (event, data) {
                    if (data.tabId == 'details.timelineHistory') {
                        $scope.$broadcast('app.object.timeline', {});
                    }
                });

            })();
        }
    }
);