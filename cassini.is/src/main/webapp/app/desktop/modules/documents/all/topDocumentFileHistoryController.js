/**
 * Created by swapna on 30/11/18.
 */
define(
    [
        'app/desktop/modules/tm/tm.module',
        'app/shared/services/store/topDocumentService'
    ],
    function (module) {
        module.controller('TopDocumentFileHistoryController', TopDocumentFileHistoryController);

        function TopDocumentFileHistoryController($scope, $rootScope, $timeout, $state, $stateParams, $cookies,
                                                  TopDocumentService) {
            var vm = this;

            vm.data = $scope.data;
            vm.callback = $scope.callback;
            vm.fileVersions = [];
            vm.downloadFile = downloadFile;
            vm.fileName = "";

            function loadFileVersions() {
                TopDocumentService.getAllFileVersions(vm.data.folderId, vm.data.documentId).then(
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
                window.open(url, '_self');
            }

            (function () {
                loadFileVersions();
            })();
        }
    }
);