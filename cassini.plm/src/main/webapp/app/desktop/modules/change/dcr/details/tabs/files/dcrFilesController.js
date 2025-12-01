define(
    [
        'app/desktop/modules/change/change.module',
        'app/desktop/modules/directives/files/objectFilesDirectiveController'
    ],
    function (module) {
        module.controller('DCRFilesController', DCRFilesController);

        function DCRFilesController($scope, $rootScope, $timeout, $state, $stateParams, $q, $translate, $application) {
            var vm = this;
            vm.dcrId = $stateParams.dcrId;

            (function () {
                $scope.$on('app.dcr.tabActivated', function (event, data) {

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
