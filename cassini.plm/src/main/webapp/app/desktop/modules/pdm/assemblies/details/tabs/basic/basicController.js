define(
    [
        'app/desktop/modules/pdm/pdm.module',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/commonService',
        'app/shared/services/core/pdmService',
        'app/shared/services/core/pdmVaultService'
    ],
    function (module) {
        module.controller('AssemblyDetailsBasicController', AssemblyDetailsBasicController);

        function AssemblyDetailsBasicController($scope, $rootScope, $timeout, $state, $stateParams, $cookies, $application,
                                                CommonService, PDMService, PDMVaultService) {

            var vm = this;

            vm.selectedItem = null;
            vm.revisionedObject = null;
            vm.fileVersion = null;
            vm.whereUsed = [];
            vm.children = [];

            function loadFileVersion() {
                PDMService.getFileVersionForRevisionedObject(vm.revisionedObject.id).then (
                    function(data) {
                        vm.fileVersion = data;
                        $rootScope.$broadcast('bomitem.fileversion.loaded', { fileVersion: vm.fileVersion });
                    }, function (error) {
                          $rootScope.showErrorMessage(error.message);
                          $rootScope.hideBusyIndicator();
                     }
                );
            }

            function loadWhereUsed() {
                vm.whereUsed = [];
                PDMService.getWhereUsed(vm.revisionedObject.id).then (
                    function(data) {
                        vm.whereUsed = data;
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                        $rootScope.hideBusyIndicator();
                    }
                );
            }

            function loadChildren() {
                vm.children = [];

                if(vm.selectedItem['@type'] === 'AssemblyDTO' || vm.selectedItem.objectType === 'PDM_ASSEMBLY') {
                    PDMService.getAssemblyChildren(vm.revisionedObject.id).then(
                        function (data) {
                            vm.children = data;
                        }, function (error) {
                            $rootScope.showErrorMessage(error.message);
                            $rootScope.hideBusyIndicator();
                        }
                    );
                }
            }

            vm.getThumbnailUrl = getThumbnailUrl;
            function getThumbnailUrl(fileVersion) {
                return "api/pdm/vaults/0/folders/{0}/files/{1}/thumbnail".format(fileVersion.file.folder, fileVersion.id);
            }

            vm.fileSizeToString = fileSizeToString;
            function fileSizeToString(bytes) {
                if (bytes === 0) {
                    return "0.00 B";
                }
                var e = Math.floor(Math.log(bytes) / Math.log(1024));
                return (bytes / Math.pow(1024, e)).toFixed(2) + ' ' + ' KMGTP'.charAt(e) + 'B';
            }

            (function () {

                $scope.$on('app.assembly.bomitem.selected', function (event, data) {
                    vm.selectedItem = data.selectedItem;
                    if(vm.selectedItem['@type'] === 'AssemblyDTO') {
                        vm.revisionedObject = vm.selectedItem.assembly;
                    }
                    else if(vm.selectedItem.objectType === 'PDM_ASSEMBLY') {
                        vm.revisionedObject = vm.selectedItem;
                    }
                    else if(vm.selectedItem['@type'] === 'PartDTO') {
                        vm.revisionedObject = vm.selectedItem.part;
                    }

                    CommonService.getPersonReferences([vm.revisionedObject], 'createdBy');
                    CommonService.getPersonReferences([vm.revisionedObject], 'modifiedBy');

                    loadFileVersion();
                    loadChildren();
                    loadWhereUsed();
                });

            })();
        }
    }
);