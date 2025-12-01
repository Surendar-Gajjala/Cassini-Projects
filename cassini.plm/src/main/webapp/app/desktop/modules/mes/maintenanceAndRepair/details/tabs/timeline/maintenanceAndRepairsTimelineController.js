define(
    [
        'app/desktop/modules/change/change.module',
        'app/desktop/modules/directives/timelineDirective'

    ],
    function (module) {
        module.controller('MaintenanceAndRepairTimeLineController', MaintenanceAndRepairTimeLineController);

        function MaintenanceAndRepairTimeLineController($scope, $rootScope, $timeout, $state, $stateParams, $cookies, $window, $application,
                                                        $translate) {
            var vm = this;
            vm.maintenanceAndRepairId = $stateParams.maintenanceAndRepairId;

            (function () {

                $scope.$on('app.maintenanceAndRepair.tabActivated', function (event, args) {
                    if (args.tabId == 'details.timelineHistory') {

                    }
                });

            })();
        }
    }
);