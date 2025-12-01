define(
    [
        'app/desktop/modules/pm/project/task/task.module',
        'app/desktop/modules/directives/files/objectFilesDirectiveController'
    ],
    function (module) {
        module.controller('TaskFilesController', TaskFilesController);

        function TaskFilesController($scope, $stateParams, $translate, $rootScope, $timeout, $state, $application) {

            var vm = this;
            vm.taskId = $stateParams.taskId;

            (function () {
                $scope.$on('app.activity.tasks.tabActivated', function (event, data) {
                    if (data.tabId == 'details.files') {
                        $timeout(function () {
                            $scope.$broadcast('app.objectFile.tabActivated', {load: true});
                        }, 500);
                    }
                })
            })();
        }
    }
);