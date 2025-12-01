define(
    [
        'app/desktop/modules/mfr/mfr.module',
        'app/desktop/modules/directives/files/objectFilesDirectiveController'

    ],
    function (module) {
        module.controller('MfrFilesController', MfrFilesController);

        function MfrFilesController($scope, $rootScope, $timeout, $state, $translate, $stateParams, $cookies) {

            var vm = this;

            vm.manufacturerId = $stateParams.manufacturerId;
            (function () {

                $scope.$on('app.mfr.tabactivated', function (event, data) {
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