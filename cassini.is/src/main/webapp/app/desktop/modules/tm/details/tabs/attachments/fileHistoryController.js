define(
    [
        'app/desktop/modules/tm/tm.module',
        'app/shared/services/tm/taskService'
    ],
    function (module) {
        module.controller('FileHistoryController', FileHistoryController);

        function FileHistoryController($scope, $rootScope, $timeout, $state, $stateParams, $cookies,
                                       TaskService) {
            var vm = this;

            vm.data = $scope.data;
            var taskId = $stateParams.taskId;
            var projectId = $stateParams.projectId;
            vm.callback = $scope.callback;
            vm.fileVersions = [];
            vm.downloadFile = downloadFile;
            vm.fileName = "";

            function loadFileVersions() {
                TaskService.getAllFileVersions(projectId, taskId, vm.data.fileId).then(
                    function (data) {
                        vm.fileVersions = data;
                        vm.fileName = vm.fileVersions[0].name
                    }
                );
            }

            /* function downloadFile(file) {
             var url = "api/projects/" + projectId + "/tasks/" + taskId + "/files/" + file.id + "/download";
             launchUrl(url);
             }*/

            function downloadFile(file) {
                var url = "api/projects/" + projectId + "/tasks/" + taskId + "/files/" + file.id + "/download";
                window.open(url, '_self');
            }

            (function () {
                loadFileVersions();
            })();
        }
    }
);