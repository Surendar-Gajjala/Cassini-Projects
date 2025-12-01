define(
    [
        'app/desktop/modules/template/templateTask/templateTask.module',
        'app/desktop/modules/directives/files/objectFilesDirectiveController'
    ],
    function (module) {
        module.controller('TemplateTaskFilesController', TemplateTaskFilesController);

        function TemplateTaskFilesController($scope, $rootScope, $timeout, $state, $stateParams, $translate, $application) {
            var vm = this;
            vm.taskId = $stateParams.taskId;

            (function () {
                $scope.$on('app.template.task.tabActivated', function (event, data) {
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