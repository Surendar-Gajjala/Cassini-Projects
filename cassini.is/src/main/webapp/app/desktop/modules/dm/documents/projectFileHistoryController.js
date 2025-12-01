/**
 * Created by swapna on 29/11/18.
 */
define(
    [
        'app/desktop/modules/tm/tm.module',
        'app/shared/services/pm/project/documentService'
    ],
    function (module) {
        module.controller('ProjectFileHistoryController', ProjectFileHistoryController);

        function ProjectFileHistoryController($scope, $rootScope, $timeout, $state, $stateParams, $cookies,
                                              DocumentService) {
            var vm = this;

            vm.data = $scope.data;
            var projectId = $stateParams.projectId;
            vm.callback = $scope.callback;
            vm.fileVersions = [];
            vm.downloadFile = downloadFile;
            vm.fileName = "";

            function loadFileVersions() {
                DocumentService.getAllFileVersions(projectId, vm.data.folderId, vm.data.documentId).then(
                    function (data) {
                        vm.fileVersions = data;
                        vm.fileName = vm.fileVersions[0].name
                    }
                );
            }

            function downloadFile(file) {
                var url = "{0}//{1}/api/projects/{2}/folders/{3}/documents/{4}/download".
                    format(window.location.protocol, window.location.host,
                    $stateParams.projectId, vm.data.folderId, vm.data.documentId);
                window.open(url, '_self');
            }

            (function () {
                loadFileVersions();
            })();
        }
    }
);