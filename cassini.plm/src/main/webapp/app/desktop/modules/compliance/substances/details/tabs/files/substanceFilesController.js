define(
    [
        'app/desktop/modules/compliance/compliance.module',
        'app/desktop/modules/directives/files/objectFilesDirectiveController'
    ],
    function (module) {
        module.controller('SubstanceFilesController', SubstanceFilesController);

        function SubstanceFilesController($scope, $rootScope, $timeout, $state, $stateParams, $q, $translate, $application) {
            var vm = this;
            vm.substanceId = $stateParams.substanceId;

            (function () {
                $scope.$on('app.substance.tabActivated', function (event, data) {
                    if (data.tabId == 'details.files') {
                        $timeout(function () {                             $scope.$broadcast('app.objectFile.tabActivated', {});                         }, 500);
                    }
                });
            })();
        }
    }
);

