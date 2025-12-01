define(
    [
        'app/desktop/modules/mro/mro.module',
        'app/desktop/modules/directives/files/objectFilesDirectiveController'
    ],
    function (module) {
        module.controller('WorkOrderFilesController', WorkOrderFilesController);

        function WorkOrderFilesController($scope, $rootScope, $timeout, $state, $stateParams, $q, $translate, $application) {
            var vm = this;
            vm.workOrderId = $stateParams.workOrderId;

            (function () {
                $scope.$on('app.workOrder.tabActivated', function (event, data) {
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

