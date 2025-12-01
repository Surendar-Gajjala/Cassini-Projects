/**
 * Created by swapna on 30/11/18.
 */
define(
    [
        'app/desktop/modules/home/home.module',
        'app/shared/services/documentService'
    ],
    function (module) {
        module.controller('FileHistoryController', FileHistoryController);

        function FileHistoryController($scope, $rootScope, $timeout, $state, $stateParams, $cookies,
                                              DocumentService) {
            var vm = this;

            vm.data = $scope.data;
            vm.callback = $scope.callback;
            vm.fileVersions = [];
            vm.downloadFile = downloadFile;
            vm.fileName = "";

            function loadFileVersions() {
                DocumentService.getAllFileVersions(vm.data.folderId, vm.data.documentId).then(
                    function (data) {
                        vm.fileVersions = data;
                        vm.fileName = vm.fileVersions[0].name
                    }
                );
            }

            function downloadFile(document) {
                var url = "{0}//{1}/api/is/folders/{2}/documents/{3}/download".
                    format(window.location.protocol, window.location.host,
                    document.folder, document.id);
                launchUrl(url);
            }


            (function () {
                loadFileVersions();
            })();
        }
    }
);