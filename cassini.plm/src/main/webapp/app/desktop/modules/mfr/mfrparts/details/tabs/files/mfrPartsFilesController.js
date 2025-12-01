define([
        'app/desktop/modules/mfr/mfrparts/mfrparts.module',
        'app/desktop/modules/directives/files/objectFilesDirectiveController'

    ],
    function (module) {
        module.controller('MfrPartsFilesController', MfrPartsFilesController);

        function MfrPartsFilesController($scope, $rootScope, $timeout, $state, $translate, $stateParams, $cookies) {

            var vm = this;
            vm.mfrPartId = $stateParams.manufacturePartId;

            (function () {
                $scope.$on('app.mfrPart.tabactivated', function (event, data) {
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