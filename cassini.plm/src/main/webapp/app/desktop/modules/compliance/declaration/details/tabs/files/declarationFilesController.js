define(
    [
        'app/desktop/modules/compliance/compliance.module',
        'app/desktop/modules/directives/files/objectFilesDirectiveController'
    ],
    function (module) {
        module.controller('DeclarationFilesController', DeclarationFilesController);

        function DeclarationFilesController($scope, $rootScope, $timeout, $state, $stateParams, $q, $translate, $application) {
            var vm = this;
            vm.declarationId = $stateParams.declarationId;

            (function () {
                $scope.$on('app.declaration.tabActivated', function (event, data) {
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

