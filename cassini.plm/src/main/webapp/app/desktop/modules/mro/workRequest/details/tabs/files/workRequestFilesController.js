define(
    [
        'app/desktop/modules/mro/mro.module',
        'app/desktop/modules/directives/files/objectFilesDirectiveController'
    ],
    function (module) {
        module.controller('WorkRequestFilesController', WorkRequestFilesController);

        function WorkRequestFilesController($scope, $rootScope, $timeout, $state, $stateParams, $q, $translate, $application) {
            var vm = this;
            vm.workRequestId = $stateParams.workRequestId;

            (function () {
                $scope.$on('app.workRequest.tabActivated', function (event, data) {
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

