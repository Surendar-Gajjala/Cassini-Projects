define(
    [
        'app/desktop/modules/items/item.module',
        'app/shared/services/itemFileService'
    ],
    function (module) {
        module.controller('FileHistoryController', FileHistoryController);

        function FileHistoryController($scope, $rootScope, $timeout, $state, $stateParams, $cookies,
                                       ItemFileService) {
            var vm = this;

            vm.data = $scope.data;
            vm.callback = $scope.callback;
            vm.fileVersions = [];
            vm.downloadFile = downloadFile;
            vm.fileName = "";

            function loadFileVersions() {
                ItemFileService.getAllFileVersions(vm.data.itemId, vm.data.fileId).then(
                    function (data) {
                        vm.fileVersions = data;
                        vm.fileName = vm.fileVersions[0].name
                    }
                );
            }

            function downloadFile(file) {
                var url = "{0}//{1}/api/pdm/itemFiles/{2}/files/{3}/download".
                format(window.location.protocol, window.location.host,
                    vm.data.itemId, file.file);
                launchUrl(url);
            }

            (function () {
                loadFileVersions();
            })();
        }
    }
);