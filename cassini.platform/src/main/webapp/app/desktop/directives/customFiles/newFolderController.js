define(
    [
        'app/desktop/modules/item/item.module',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/commonService'
    ],
    function (module) {
        module.controller('NewFolderController', NewFolderController);

        function NewFolderController($scope, $rootScope, $timeout, $state, $stateParams, $cookies, $translate, CommonService, CustomObjectFileService) {
            var vm = this;

            var parsed = angular.element("<div></div>");
            var fileDescriptionMsg = parsed.html($translate.instant("FILE_DESCRIPTION_ADDED_MSG")).html();
            var folderCreatedSuccessMsg = parsed.html($translate.instant("FOLDER_CREATED_SUCCESS_MSG")).html();
            var folderUpdatedSuccessMsg = parsed.html($translate.instant("FOLDER_UPDATED_SUCCESS_MSG")).html();
            var itemNameValidation = parsed.html($translate.instant("ITEM_NAME_VALIDATION")).html();
            vm.selectedFolderType = $scope.data.folderCreateType;

            vm.newFolder = {
                id: null,
                name: null,
                description: null,
                version: 1,
                latest: true,
                locked: false,
                fileNo: null,
                parent: $scope.data.parentFolder,
                parentFile: $scope.data.parentFolder,
                type: "FOLDER",
                fileType: "FOLDER",
                size: 0
            };

            function createFolder() {
                if (validate()) {
                    $rootScope.showBusyIndicator();
                    if ($scope.data.folderCreateType == "Add") {
                        vm.newFolder.object = $scope.data.fileObjectId;
                        CustomObjectFileService.createCustomObjectFileFolder(vm.newFolder.object, vm.newFolder).then(
                            function (data) {
                                $scope.callback(data);
                                $rootScope.showSuccessMessage(folderCreatedSuccessMsg);
                            }, function (error) {
                                $rootScope.showErrorMessage(error.message);
                                $rootScope.hideBusyIndicator();
                            }
                        )
                    } else {
                        vm.newFolder.object = $scope.data.fileObjectId;
                        CustomObjectFileService.updateCustomObjectFileFolder(vm.newFolder.object, vm.newFolder).then(
                            function (data) {
                                $scope.callback(data);
                                $rootScope.showSuccessMessage(folderUpdatedSuccessMsg);
                            }, function (error) {
                                $rootScope.showErrorMessage(error.message);
                                $rootScope.hideBusyIndicator();
                            }
                        )
                    }
                }
            }

            function validate() {
                var valid = true;

                if (vm.newFolder.name == null || vm.newFolder.name == "" || vm.newFolder.name == undefined) {
                    valid = false;
                    $rootScope.showWarningMessage(itemNameValidation);
                }

                return valid;
            }

            (function () {
                if ($scope.data.folderCreateType == "update") {
                    vm.newFolder = $scope.data.folderData;
                }
                $rootScope.$on('app.custom.folder', createFolder);
            })();
        }
    }
);
