define(
    [
        'app/desktop/modules/change/change.module',
        'app/desktop/modules/directives/files/objectFilesDirectiveController'
    ],
    function (module) {
        module.controller('MCOFilesController', MCOFilesController);

        function MCOFilesController($scope, $rootScope, $timeout, $state, $stateParams, $q, $translate, $application) {
            var vm = this;
            vm.mcoId = $stateParams.mcoId;

            (function () {
                $scope.$on('app.mco.tabActivated', function (event, data) {

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
