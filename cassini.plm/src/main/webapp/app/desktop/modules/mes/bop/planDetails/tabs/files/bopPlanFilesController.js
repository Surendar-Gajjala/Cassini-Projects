define(
    [
        'app/desktop/modules/mes/mes.module',
        'app/desktop/modules/directives/files/objectFilesDirectiveController'
    ],
    function (module) {
        module.controller('BOPPlanFilesController', BOPPlanFilesController);

        function BOPPlanFilesController($scope, $rootScope, $timeout, $state, $stateParams, $q, $translate, $application) {
            var vm = this;
            vm.bopPlanId = $stateParams.bopPlanId;

            (function () {
                $scope.$on('app.bop.plan.tabActivated', function (event, data) {
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

