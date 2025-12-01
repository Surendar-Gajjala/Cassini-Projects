define(
    [
        'app/desktop/modules/pm/pm.module',
        'app/desktop/modules/directives/files/objectFilesDirectiveController'
    ],
    function (module) {
        module.controller('ProgramTemplateFilesController', ProgramTemplateFilesController);

        function ProgramTemplateFilesController($scope, $rootScope, $timeout, $state, $stateParams, $translate, $application) {
            var vm = this;
            vm.templateId = $stateParams.templateId;

            (function () {
                $scope.$on('app.programTemplate.tabActivated', function (event, data) {
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