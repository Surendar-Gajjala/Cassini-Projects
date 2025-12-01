define(
    [
        'app/desktop/modules/vaults/vault.module',
        'app/shared/services/vaultsDetailsService'
    ],
    function (module) {
        module.controller('FileHistoryController', FileHistoryController);

        function FileHistoryController($scope, $rootScope, $timeout, $state, $stateParams, $cookies,
                                       VaultsDetailsService) {
            var vm = this;

            vm.data = $scope.data;
            vm.callback = $scope.callback;
            vm.fileVersions = [];
            vm.downloadFile = downloadFile;
            vm.fileName = "";

            function loadFileVersions() {
                VaultsDetailsService.getAllFileVersion(vm.data.folderId, vm.data.fileId).then(
                    function (data) {
                        vm.fileVersions = data;
                        vm.fileName = vm.fileVersions[0].name
                    }
                );
            }

            function downloadFile(file) {
                var url = "{0}//{1}/api/pdm/vaults/folders/{2}/files/{3}/download".
                format(window.location.protocol, window.location.host,
                    vm.data.folderId, file.id);
                launchUrl(url);
            }

            (function () {
                loadFileVersions();
            })();
        }
    }
);