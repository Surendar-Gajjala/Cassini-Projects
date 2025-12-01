define(
    [
        'app/desktop/modules/mes/mes.module',
        'app/desktop/modules/directives/files/objectFilesDirectiveController'
    ],
    function (module) {
        module.controller('InstrumentFilesController', InstrumentFilesController);

        function InstrumentFilesController($scope, $rootScope, $timeout, $state, $stateParams, $q, $translate, $application) {
            var vm = this;
            vm.instrumentId = $stateParams.instrumentId;

            (function () {
                $scope.$on('app.instrument.tabActivated', function (event, data) {
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

