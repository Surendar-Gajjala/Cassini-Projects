define(
    [
        'app/desktop/modules/pm/pm.module',
        'app/desktop/modules/directives/files/objectFilesDirectiveController'
    ],
    function (module) {
        module.controller('ProjectFilesController', ProjectFilesController);

        function ProjectFilesController($scope, $rootScope, $timeout, $state, $stateParams, $translate, $application) {
            var vm = this;
            vm.projectId = $stateParams.projectId;

            (function () {
                $scope.$on('app.project.tabactivated', function (event, data) {
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