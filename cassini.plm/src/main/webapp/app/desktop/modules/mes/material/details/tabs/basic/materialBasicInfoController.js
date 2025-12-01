define(
    [
        'app/desktop/modules/mes/mes.module',
        'app/shared/services/core/ecoService',
        'app/shared/services/core/mesObjectTypeService',
        'app/desktop/modules/directives/basicAttributeDetailsDirectiveController',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/measurementService',
        'app/desktop/modules/directives/objectAttributeDetailsDirectiveController'
    ],
    function (module) {
        module.controller('MaterialBasicInfoController', MaterialBasicInfoController);

        function MaterialBasicInfoController($scope, $rootScope, $sce, $timeout, $state, $stateParams, $cookies, ECOService, CommonService,
                                             MESObjectTypeService, MaterialService, MeasurementService, $translate, DialogService) {
            var vm = this;
            vm.loading = true;
            vm.materialId = $stateParams.materialId;

            var parsed = angular.element("<div></div>");
            var materialUpdateMsg = parsed.html($translate.instant("MATERIAL_UPDATE_MSG")).html();
            var nameValidation = parsed.html($translate.instant("NAME_VALIDATION")).html();

            vm.updateMaterial = updateMaterial;

            function updateMaterial() {
                if (validate()) {
                    vm.material.uom = vm.material.uomObject.id;
                    if (vm.material.qom != vm.material.qomObject.id) {
                        vm.material.uom = null;
                    }
                    vm.material.qom = vm.material.qomObject.id;
                    MaterialService.updateMaterial(vm.material).then(
                        function (data) {
                            vm.material = data;
                            loadMaterial();
                            vm.loading = false;
                            CommonService.getPersonReferences([vm.material], 'createdBy');
                            CommonService.getPersonReferences([vm.material], 'modifiedBy');
                            $rootScope.showSuccessMessage(materialUpdateMsg)
                        }, function (error) {
                            $rootScope.showErrorMessage(error.message);
                            $rootScope.hideBusyIndicator();
                        }
                    )
                }
            }

            function validate() {
                var valid = true;
                if (vm.material.name == null || vm.material.name == "" || vm.material.name == undefined) {
                    valid = false;
                    vm.material.name = $scope.name;
                    $rootScope.showWarningMessage(nameValidation);
                }

                return valid;
            }

            function loadMaterial() {
                MaterialService.getMaterial(vm.materialId).then(
                    function (data) {
                        vm.material = data;
                        $scope.name = vm.material.name;
                        vm.loading = false;
                        if (vm.material.image != null) {
                            vm.material.imagePath = "api/mes/materials/" + vm.materialId + "/image/download?" + new Date().getTime();
                        }
                        $rootScope.viewInfo.description = vm.material.number + " , " + vm.material.name;
                        $rootScope.viewInfo.title = $translate.instant("MATERIAL_DETAILS");
                        CommonService.getPersonReferences([vm.material], 'createdBy');
                        CommonService.getPersonReferences([vm.material], 'modifiedBy');
                        $timeout(function () {
                            $scope.$broadcast('app.attributes.tabActivated', {});
                        }, 1000);
                        $scope.$evalAsync();
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                        $rootScope.hideBusyIndicator();
                    }
                )
            }

            function loadMeasurements() {
                MeasurementService.getAllMeasurements().then(
                    function (data) {
                        vm.measurements = data;
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                    }
                )
            }

            vm.changeQomTo = changeQomTo;
            vm.cancelQomTo = cancelAssignedTo;

            function changeQomTo() {
                vm.editQomTo = true;
            }

            function cancelAssignedTo() {
                vm.editQomTo = false;
            }


            vm.image = null;
            $scope.saveImage = saveImage;
            function saveImage(image) {
                if (image == null || image == "") {
                    $rootScope.showWarningMessage(SelectThumbnail);

                } else {
                    vm.image = image;
                    if (validateImage()) {
                        MaterialService.uploadImage(vm.materialId, image).then(
                            function (data) {
                                $rootScope.showSuccessMessage(updateImageMsg);
                                loadMaterial();
                            }, function (error) {
                                $rootScope.showErrorMessage(error.message);
                            }
                        );
                    }
                }
            }

            var imageValidation = parsed.html($translate.instant("IMAGE_VALIDATION")).html();
            var SelectThumbnail = parsed.html($translate.instant("SELECT_THUMBNAIL")).html();
            $scope.removeImageTitle = parsed.html($translate.instant("REMOVE_IMAGE")).html();
            $scope.updateImageTitle = parsed.html($translate.instant("UPDATE_IMAGE")).html();
            var updateImageMsg = parsed.html($translate.instant("IMAGE_SUCCESS_MESSAGE")).html();
            var confirmationTitle = parsed.html($translate.instant("CONFIRMATION")).html();
            var removeImageTitle = parsed.html($translate.instant("REMOVE_IMAGE_MSG")).html();
            var removeImageMsg = parsed.html($translate.instant("IMAGE_REMOVED")).html();

            function validateImage() {
                var valid = true;
                if (vm.image != null) {
                    if (vm.material.image == null || vm.material.image == "") {
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
                    title: confirmationTitle,
                    message: removeImageTitle,
                    okButtonClass: 'btn-danger'
                };
                DialogService.confirm(options, function (yes) {
                    if (yes == true) {
                        vm.material.image = null;
                        MaterialService.updateMaterial(vm.material).then(
                            function (data) {
                                $rootScope.showSuccessMessage(removeImageMsg);
                                loadMaterial();
                            }, function (error) {
                                $rootScope.showErrorMessage(error.message);
                            }
                        )
                    }
                })
            }

            vm.showImage = showImage;
            function showImage(item) {
                var modal = document.getElementById('item-thumbnail-basic' + vm.materialId);
                modal.style.display = "block";

                var span = document.getElementById("thumbnail-close-basic" + vm.materialId);
                $("#thumbnail-image-basic" + vm.materialId).width($('#thumbnail-view-basic' + vm.materialId).outerWidth());
                $("#thumbnail-image-basic" + vm.materialId).height($('#thumbnail-view-basic' + vm.materialId).outerHeight());
                $(".split-pane-divider").css('z-index', 0);
                span.onclick = function () {
                    modal.style.display = "none";
                }
                $scope.$evalAsync();
            }

            (function () {
                $scope.$on('app.material.tabActivated', function (event, data) {
                    if (data.tabId == 'details.basic') {
                        loadMaterial();
                        loadMeasurements();
                    }
                });

            })();

        }
    }
);