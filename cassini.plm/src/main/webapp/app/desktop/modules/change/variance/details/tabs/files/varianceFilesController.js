define(
    [
        'app/desktop/modules/change/change.module',
        'app/desktop/modules/directives/files/objectFilesDirectiveController'
    ],
    function (module) {
        module.controller('VarianceFilesController', VarianceFilesController);

        function VarianceFilesController($scope, $rootScope, $timeout, $state, $stateParams, $cookies, $translate, $application) {
            var vm = this;

            vm.varianceId = $stateParams.varianceId;

            (function () {
                $scope.$on('app.variance.tabactivated', function (event, data) {
                    if (data.tabId == 'details.files') {
                        $timeout(function () {
                            $scope.$broadcast('app.objectFile.tabActivated', {load: true});
                        }, 500);
                    }
                });
            })();

        }
    }
);