define(
    [
        'app/desktop/modules/mro/mro.module',
        'app/desktop/modules/directives/files/objectFilesDirectiveController'
    ],
    function (module) {
        module.controller('MaintenancePlanFilesController', MaintenancePlanFilesController);

        function MaintenancePlanFilesController($scope, $rootScope, $timeout, $state, $stateParams, $q, $translate, $application) {
            var vm = this;
            vm.maintenancePlanId = $stateParams.maintenancePlanId;

            (function () {
                $scope.$on('app.maintenancePlan.tabActivated', function (event, data) {
                    if (data.tabId == 'details.files') {
                        $timeout(function () {
                            $scope.$broadcast('app.objectFile.tabActivated', {});
                        }, 500);
                    }
                });
            })();
        }
    }
);

