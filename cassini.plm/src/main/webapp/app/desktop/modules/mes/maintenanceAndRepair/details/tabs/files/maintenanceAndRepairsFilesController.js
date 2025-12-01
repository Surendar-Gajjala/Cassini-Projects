define(
    [
        'app/desktop/modules/change/change.module',
    ],
    function (module) {
        module.controller('MaintenanceAndRepairFilesController', MaintenanceAndRepairFilesController);

        function MaintenanceAndRepairFilesController($scope, $rootScope, $timeout, $state, $stateParams, $q, $translate, $application) {
            var vm = this;

            (function () {
                $scope.$on('app.maintenanceAndRepair.tabActivated', function (event, data) {
                    if (data.tabId == 'details.files') {

                    }
                });
            })();
        }
    }
);

