define(['app/desktop/modules/mfr/mfrparts/mfrparts.module',
        'app/shared/services/core/mfrPartsService',
        'app/shared/services/core/mfrService',
        'app/shared/services/core/itemService',
        'app/shared/services/core/workflowDefinitionService',
        'app/shared/services/core/mfrService',
        'app/shared/services/core/specificationsService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/objectAttributeService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/objectTypeAttributeService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/attachmentService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/attributeAttachmentService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/commonService',
        'app/desktop/modules/directives/basicAttributeDetailsDirectiveController',
        'app/desktop/modules/directives/objectAttributeDetailsDirectiveController'
    ],
    function (module) {
        module.controller('MfrPartsBasicController', MfrPartsBasicController);

        function MfrPartsBasicController($scope, $rootScope, $timeout, $state, $sce, $stateParams, $cookies, MfrPartsService, CommonService, DialogService, $translate) {

            $rootScope.viewInfo.icon = "fa fa-cubes";
            var vm = this;
            vm.loading = true;
            var mfId = $stateParams.manufacturePartId;
            vm.mfrPartId = $stateParams.manufacturePartId;
            vm.manufacturePart = null;
            $scope.partNumber = null;
            $scope.partName = null;
            vm.mfrPartProperties = [];
            vm.updateManufacturerPart = updateManufacturerPart;
            vm.status = ['ACTIVE', 'OBSOLETE'];

            var parsed = angular.element("<div></div>");

            $rootScope.loadManufacturerParts = loadManufacturerParts;
            function loadManufacturerParts() {
                MfrPartsService.getManufacturepart(mfId).then(
                    function (data) {
                        vm.manufacturePart = data;
                        $scope.partNumber = vm.manufacturePart.partNumber;
                        $scope.partName = vm.manufacturePart.partName;
                        vm.loading = false;
                        if (vm.manufacturePart.thumbnail != null) {
                            vm.manufacturePart.imagePath = "api/plm/mfr/parts/" + vm.manufacturePart.id + "/image/download?" + new Date().getTime();
                        }
                        CommonService.getMultiplePersonReferences([vm.manufacturePart], ['createdBy', 'modifiedBy']);
                        $timeout(function () {
                            $scope.$broadcast('app.attributes.tabActivated', {});
                        }, 1000);
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                        $rootScope.hideBusyIndicator();
                    }
                )
            }

            var mfrPartUpdateMsg = $translate.instant("MANUFACTURER_PART_UPDATE_MESSAGE");

            function updateManufacturerPart() {
                if (validate()) {
                    MfrPartsService.updateManufacturepart(mfId, vm.manufacturePart).then(
                        function (data) {
                            vm.manufacturePart = data;
                            $scope.partNumber = data.partNumber;
                            $scope.partName = data.partName;
                            vm.editSerialized = false;
                            CommonService.getMultiplePersonReferences([vm.manufacturePart], ['createdBy', 'modifiedBy']);
                            $rootScope.showSuccessMessage(mfrPartUpdateMsg);
                        }, function (error) {
                            $rootScope.showErrorMessage(error.message);
                            $rootScope.hideBusyIndicator();
                        }
                    )
                }
            }

            var numberValidation = parsed.html($translate.instant("MANUFACTURER_PART_NUMBER_VALIDATE")).html();
            var nameValidation = parsed.html($translate.instant("NAME_CANNOT_BE_EMPTY")).html();

            function validate() {
                var valid = true;
                if (vm.manufacturePart.partNumber == null || vm.manufacturePart.partNumber == ""
                    || vm.manufacturePart.partNumber == undefined) {
                    valid = false;
                    vm.manufacturePart.partNumber = $scope.partNumber;
                    $rootScope.showWarningMessage(numberValidation);

                }
                else if (vm.manufacturePart.partName == null || vm.manufacturePart.partName == ""
                    || vm.manufacturePart.partName == undefined) {
                    valid = false;
                    vm.manufacturePart.partName = $scope.partName;
                    $rootScope.showWarningMessage(nameValidation);

                }
                return valid;

            }

            vm.thumbnail = null;
            $scope.saveImage = saveImage;
            function saveImage(image) {
                if (image == null || image == "") {
                    $rootScope.showWarningMessage(SelectThumbnail);
                } else {
                    vm.thumbnail = image;
                    if (validateImage()) {
                        MfrPartsService.uploadMfrPartImage(mfId, image).then(
                            function (data) {
                                $rootScope.showSuccessMessage("Thumbnail uploaded successfully");
                                loadManufacturerParts();
                            }, function (error) {
                                $rootScope.showErrorMessage(error.message);
                                $rootScope.hideBusyIndicator();
                            }
                        );
                    }
                }
            }

            var imageValidation = parsed.html($translate.instant("IMAGE_VALIDATION")).html();
            var SelectThumbnail = parsed.html($translate.instant("SELECT_THUMBNAIL")).html();
            $scope.removeImageTitle = parsed.html($translate.instant("REMOVE_IMAGE")).html();
            $scope.updateImageTitle = parsed.html($translate.instant("UPDATE_IMAGE")).html();

            function validateImage() {
                var valid = true;
                if (vm.thumbnail != null) {
                    if (vm.manufacturePart.thumbnail == null || vm.manufacturePart.thumbnail == "") {
                        var fup = document.getElementById('imageFile');
                        var fileName = fup.value;
                        var ext = fileName.substring(fileName.lastIndexOf('.') + 1);
                        if (ext == "JPEG" || ext == "jpeg" || ext == "jpg" || ext == "JPG" || ext == "PNG" || ext == "png" || ext == "GIF" || ext == "gif") {
                            return true;
                        } else {
                            $rootScope.showWarningMessage(imageValidation);
                            fup.focus();
                            valid = false;
                        }
                    } else {
                        var fup = document.getElementById('imageUpdateFile');
                        var fileName = fup.value;
                        var ext = fileName.substring(fileName.lastIndexOf('.') + 1);
                        if (ext == "JPEG" || ext == "jpeg" || ext == "jpg" || ext == "JPG" || ext == "PNG" || ext == "png" || ext == "GIF" || ext == "gif") {
                            return true;
                        } else {
                            $rootScope.showWarningMessage(imageValidation);
                            fup.focus();
                            valid = false;
                        }
                    }
                }

                return valid;
            }

            vm.deleteImage = deleteImage;
            function deleteImage() {
                var options = {
                    title: "Confirmation",
                    message: "Are you sure you want to remove thumbnail?",
                    okButtonClass: 'btn-danger'
                };
                DialogService.confirm(options, function (yes) {
                    if (yes == true) {
                        vm.manufacturePart.thumbnail = null;
                        MfrPartsService.updateManufacturepart(mfId, vm.manufacturePart).then(
                            function (data) {
                                $rootScope.showSuccessMessage("Thumbnail removed successfully");
                                loadManufacturerParts();
                            }, function (error) {
                                $rootScope.showErrorMessage(error.message);
                                $rootScope.hideBusyIndicator();
                            }
                        )
                    }
                })
            }

            var yes = parsed.html($translate.instant("YES")).html();
            var no = parsed.html($translate.instant("NO")).html();

            vm.editSerialized = false;
            vm.serialized = [
                {label: yes, value: true},
                {label: no, value: false}
            ];

            vm.changeSerialized = changeSerialized;
            function changeSerialized() {
                    vm.editSerialized = true;
                    vm.oldSerialized = vm.manufacturePart.serialized;
            }

            vm.cancelSerialized = cancelSerialized;
            function cancelSerialized() {
                vm.editSerialized = false;
                vm.manufacturePart.serialized = vm.oldSerialized;
            }

            vm.showImage = showImage;
            function showImage(item) {
                var modal = document.getElementById('item-thumbnail-basic' + vm.mfrPartId);
                modal.style.display = "block";

                var span = document.getElementById("thumbnail-close-basic" + vm.mfrPartId);
                $("#thumbnail-image-basic" + vm.mfrPartId).width($('#thumbnail-view-basic' + vm.mfrPartId).outerWidth());
                $("#thumbnail-image-basic" + vm.mfrPartId).height($('#thumbnail-view-basic' + vm.mfrPartId).outerHeight());
                $(".split-pane-divider").css('z-index', 0);
                span.onclick = function () {
                    modal.style.display = "none";
                }
                $scope.$evalAsync();
            }

            (function () {
                loadManufacturerParts();
            })();
        }
    }
);