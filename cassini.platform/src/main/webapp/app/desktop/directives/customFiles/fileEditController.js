define(
    [
        'app/desktop/modules/item/item.module',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/commonService'

    ],
    function (module) {
        module.controller('FileEditController', FileEditController);

        function FileEditController($scope, $rootScope, $timeout, $state, $stateParams, $cookies, $translate, CommonService, DialogService, CustomObjectFileService) {
            var vm = this;

            vm.loading = true;
            vm.files = [];
            vm.showDropzone = false;
            vm.showFileDropzone = false;
            vm.filesView = $scope.data.editFile;
            var currencyMap = new Hashtable();
            var parsed = angular.element("<div></div>");
            var fileDescriptionMsg = null;
            var mode = $scope.data.mode;
            vm.fileEditPermission = $scope.data.fileEditPermission;
            vm.files = {
                fileId: vm.filesView.id,
                name: vm.filesView.name,
                description: vm.filesView.description,
                locked: vm.filesView.locked,
                lockedBy: vm.filesView.lockedBy
            };
            var descriptionMsg = parsed.html($translate.instant("DESCRIPTION_VALIDATION")).html();

            function editFile() {
                if (vm.files.description == null || vm.files.description == "") {
                    //$rootScope.showWarningMessage(descriptionMsg);
                    $rootScope.hideSidePanel();
                } else {
                    CustomObjectFileService.updateCustomObjectFile(vm.filesView.object, vm.filesView.id, vm.files).then(
                        function (data) {
                            $scope.callback(data);
                            $rootScope.hideSidePanel();
                            if ($scope.data.editFile.description == null || $scope.data.editFile.description == "") {
                                fileDescriptionMsg = parsed.html($translate.instant("FILE_UPDATED_MSG")).html();
                                $rootScope.showSuccessMessage(fileDescriptionMsg);
                            } else {
                                fileDescriptionMsg = parsed.html($translate.instant("FILE_UPDATED_MSG")).html();
                                $rootScope.showSuccessMessage(fileDescriptionMsg);
                            }
                        }
                    )

                }
            }

            (function () {
                $rootScope.$on('app.custom.file.edit', editFile);
            })();
        }
    }
);
