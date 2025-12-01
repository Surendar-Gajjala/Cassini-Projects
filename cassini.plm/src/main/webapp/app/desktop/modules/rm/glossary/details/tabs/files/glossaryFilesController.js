define(
    [
        'app/desktop/modules/rm/rm.module',
        'app/desktop/modules/directives/files/objectFilesDirectiveController'
    ],
    function (module) {
        module.controller('GlossaryFilesController', GlossaryFilesController);

        function GlossaryFilesController($scope, $rootScope, $timeout, $state, $stateParams, $cookies, $translate, $application) {
            var vm = this;

            vm.glossaryId = $stateParams.glossaryId;

            (function () {

                $scope.$on('app.glossary.tabactivated', function (event, data) {
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