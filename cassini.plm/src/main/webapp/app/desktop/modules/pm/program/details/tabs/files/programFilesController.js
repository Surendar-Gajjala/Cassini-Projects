define(
    [
        'app/desktop/modules/pm/pm.module',
        'app/desktop/modules/directives/files/objectFilesDirectiveController'
    ],
    function (module) {
        module.controller('ProgramFilesController', ProgramFilesController);

        function ProgramFilesController($scope, $rootScope, $timeout, $state, $stateParams, $translate, $application) {
            var vm = this;
            vm.programId = $stateParams.programId;

            (function () {
                $scope.$on('app.program.tabactivated', function (event, data) {
                    if (data.tabId == 'details.files') {
                        $rootScope.freeTextQuerys = null;
                        $timeout(function () {
                            $scope.$broadcast('app.objectFile.tabActivated', {load: true});
                        }, 500);
                    }
                });
            })();
        }
    }
);