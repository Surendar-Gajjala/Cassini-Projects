define(
    [
        'app/desktop/modules/pdm/pdm.module',
        'app/desktop/modules/pdm/parts/details/tabs/basic/basicController',
        'app/desktop/modules/pdm/parts/details/tabs/visualization/visualizationController',
        'app/shared/services/core/pdmService',
        'app/shared/services/core/pdmVaultService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/commonService'
    ],
    function (module) {
        module.controller('TimelineController', TimelineController);

        function TimelineController($scope, $rootScope, $timeout, $state, $stateParams, $sce, $cookies, $window,
                                           CommonService, PDMService, PDMVaultService) {

            $rootScope.viewInfo.showDetails = false;

            var vm = this;

            vm.revisionedObject = { id: -1 };
            vm.fileVersion = null;
            vm.fileVersions = [];

            function loadFileVersion() {
                PDMService.getFileVersionForRevisionedObject(vm.revisionedObject.id).then(
                    function (data) {
                        vm.fileVersion = data;
                        loadFileVersions();
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                        $rootScope.hideBusyIndicator();
                    }
                );
            }

            function loadFileVersions() {
                PDMVaultService.getFileVersions(vm.fileVersion.file.id).then (
                    function(data) {
                        vm.fileVersions = data;
                        /*vm.fileVersions.sort(function(f1, f2) {
                            return f2.version - f1.version;
                         });*/
                        loadReferences();
                    },
                    function(error) {
                        $rootScope.showErrorMessage(error.message);
                    }
                );
            }

            function loadReferences() {
                CommonService.getPersonReferences(vm.fileVersions, "createdBy");
                PDMVaultService.getCommitReferences(vm.fileVersions, 'commit');
            }

            vm.getThumbnailUrl = getThumbnailUrl;
            function getThumbnailUrl(fileVersion) {
                return "api/pdm/vaults/0/folders/{0}/files/{1}/thumbnail".format(fileVersion.file.folder, fileVersion.id);
            }

            (function () {
                $scope.$on("app.part.selected", function(event, data) {
                    var selectedItem = data.selectedItem;

                    if (vm.revisionedObject !== selectedItem.id) {
                        vm.revisionedObject = selectedItem;
                        vm.fileVersions = [];
                        $timeout(function () {
                            loadFileVersion();
                        }, 1000);
                    }
                });
            })();
        }
    }
);