define(
    [
        'app/desktop/modules/pqm/pqm.module',
        'app/desktop/modules/directives/files/objectFilesDirectiveController'
    ],
    function (module) {
        module.controller('NcrFilesController', NcrFilesController);

        function NcrFilesController($scope, $rootScope, $timeout, $state, $stateParams, $q, $translate, $application) {
            var vm = this;
            vm.ncrId = $stateParams.ncrId;

            (function () {
                $scope.$on('app.ncr.tabActivated', function (event, data) {

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