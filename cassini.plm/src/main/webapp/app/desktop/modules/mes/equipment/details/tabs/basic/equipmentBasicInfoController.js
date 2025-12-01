define(
    [
        'app/desktop/modules/mes/mes.module',
        'app/shared/services/core/equipmentService',
        'app/desktop/modules/directives/basicAttributeDetailsDirectiveController',
        'app/desktop/directives/mes-mfr-data/mesMfrDataDirectiveController',
        'app/desktop/modules/directives/objectAttributeDetailsDirectiveController',
        'app/shared/services/core/assetService'
    ],
    function (module) {
        module.controller('EquipmentBasicInfoController', EquipmentBasicInfoController);

        function EquipmentBasicInfoController($scope, $rootScope, $sce, $timeout, $state, $stateParams, $cookies, ECOService, CommonService,
                                              EquipmentService, $translate, QualityTypeService, LoginService, DialogService, AssetService) {
            var vm = this;
            vm.loading = true;
            vm.equipmentId = $stateParams.equipmentId;
            vm.equipment = null;
            $scope.name = null;
            vm.persons = [];
            var parsed = angular.element("<div></div>");
            vm.updateEquipment = updateEquipment;

            function loadEquipmentBasicDetails() {
                vm.loading = true;
                if (vm.equipmentId != null && vm.equipmentId != undefined) {
                    EquipmentService.getEquipment(vm.equipmentId).then(
                        function (data) {
                            vm.equipment = data;
                            if (vm.equipment.manufacturerData == null || vm.equipment.manufacturerData == "") {
                                vm.equipment.manufacturerData = {
                                    object: vm.equipment.id,
                                    mfrName: null,
                                    mfrDescription: null,
                                    mfrModelNumber: null,
                                    mfrPartNumber: null,
                                    mfrSerialNumber: null,
                                    mfrDate: null
                                }
                            }
                            $rootScope.equipment = vm.equipment;
                            $scope.name = vm.equipment.name;
                            CommonService.getPersonReferences([vm.equipment], 'modifiedBy');
                            CommonService.getPersonReferences([vm.equipment], 'createdBy');
                            if (vm.equipment.createdDate) {
                                if ($rootScope.currentLang == 'de') {
                                    vm.equipment.createDateDe = moment(vm.equipment.createdDate, "DD/MM/YYYY, HH:mm:ss").format("DD.MM.YYYY, HH:mm:ss");

                                } else {
                                    vm.equipment.createDateDe = vm.equipment.createdDate;
                                }

                            }
                            if (vm.equipment.modifiedDate) {
                                if ($rootScope.currentLang == 'de') {
                                    vm.equipment.modifiedDateDe = moment(vm.equipment.modifiedDate, "DD/MM/YYYY, HH:mm:ss").format("DD.MM.YYYY, HH:mm:ss");

                                } else {
                                    vm.equipment.modifiedDateDe = vm.equipment.modifiedDate;
                                }
                            }
                            if (vm.equipment.description != null && vm.equipment.description != undefined) {
                                vm.equipment.descriptionHtml = $sce.trustAsHtml(vm.equipment.description.replace(/(?:\ r\n|\r|\n)/g, '<br>'));
                            }
                            if (vm.equipment.image != null) {
                                vm.equipment.imagePath = "api/mes/equipments/" + vm.equipmentId + "/image/download?" + new Date().getTime();
                            }
                            vm.loading = false;
                            vm.editStatus = false;
                            $rootScope.viewInfo.title = $translate.instant("EQUIPMENT_DETAILS");
                            $rootScope.viewInfo.description = vm.equipment.number + " , " + vm.equipment.name;
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
            }


            var itemNameValidation = parsed.html($translate.instant("NAME_VALIDATION")).html();
            var equipmentManagerValidation = parsed.html($translate.instant("EQUIPMENT_MANAGER_VALIDATION")).html();
            var enterValidEmail = parsed.html($translate.instant("ENTER_VALID_EMAIL")).html();
            var equipmentUpdated = parsed.html($translate.instant("EQUIPMENT_UPDATED")).html();


            function validateEquipment() {
                var valid = true;
                if (vm.equipment.name == null || vm.equipment.name == ""
                    || vm.equipment.name == undefined) {
                    valid = false;
                    vm.equipment.name = $scope.name;
                    $rootScope.showWarningMessage(itemNameValidation);

                }


                return valid;
            }

            function updateEquipment() {
                if (validateEquipment()) {
                    $rootScope.showBusyIndicator($('.view-container'));
                    EquipmentService.updateEquipment(vm.equipment).then(
                        function (data) {
                            loadEquipmentBasicDetails();
                            vm.editMaintenance = false;
                            vm.editStatus = false;
                            $rootScope.showSuccessMessage(equipmentUpdated);
                            $rootScope.hideBusyIndicator();
                        }, function (error) {
                            $rootScope.showErrorMessage(error.message);
                            $rootScope.hideBusyIndicator();
                        }
                    )
                }
            }

            function loadPersons() {
                vm.persons = [];
                LoginService.getAllLogins().then(
                    function (data) {
                        angular.forEach(data, function (login) {
                            if (login.isActive == true && login.external == false) {
                                vm.persons.push(login.person);
                            }
                        });
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                        $rootScope.hideBusyIndicator();
                    }
                )
            }

            var active = parsed.html($translate.instant("C_ACTIVE")).html();
            var inActive = parsed.html($translate.instant("C_INACTIVE")).html();
            vm.editStatus = false;
            vm.statuses = [
                {label: active, value: true},
                {label: inActive, value: false}
            ]

            vm.changeStatus = changeStatus;
            function changeStatus() {
                vm.editStatus = true;
                vm.oldStatus = vm.equipment.active;
            }

            vm.cancelStatus = cancelStatus;
            function cancelStatus() {
                vm.editStatus = false;
                vm.equipment.active = vm.oldStatus;
            }

            var yes = parsed.html($translate.instant("YES")).html();
            var no = parsed.html($translate.instant("NO")).html();

            vm.editMaintenance = false;
            vm.maintenances = [
                {label: yes, value: true},
                {label: no, value: false}
            ]

            vm.changeMaintenance = changeMaintenance;
            function changeMaintenance() {
                if (vm.assets.length == 0) {
                    vm.editMaintenance = true;
                    vm.oldMaintenane = vm.equipment.requiresMaintenance;
                }
            }

            vm.cancelMaintenance = cancelMaintenance;
            function cancelMaintenance() {
                vm.editMaintenance = false;
                vm.equipment.requiresMaintenance = vm.oldMaintenane;
            }

            vm.image = null;
            $scope.saveImage = saveImage;
            function saveImage(image) {
                if (image == null || image == "") {
                    $rootScope.showWarningMessage(SelectThumbnail);

                } else {
                    vm.image = image;
                    if (validateImage()) {
                        EquipmentService.uploadImage(vm.equipmentId, image).then(
                            function (data) {
                                $rootScope.showSuccessMessage("Image uploaded successfully");
                                loadEquipmentBasicDetails();
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
                if (vm.image != null) {
                    if (vm.equipment.image == null || vm.equipment.image == "") {
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
                    message: "Are you sure you want to remove image?",
                    okButtonClass: 'btn-danger'
                }
                DialogService.confirm(options, function (yes) {
                    if (yes == true) {
                        vm.equipment.image = null;
                        EquipmentService.updateEquipment(vm.equipment).then(
                            function (data) {
                                $rootScope.showSuccessMessage("Image removed successfully");
                                loadEquipmentBasicDetails();
                            }, function (error) {
                                $rootScope.showErrorMessage(error.message);
                                $rootScope.hideBusyIndicator();
                            }
                        )
                    }
                })
            }

            vm.showImage = showImage;
            function showImage(item) {
                var modal = document.getElementById('item-thumbnail-basic' + vm.equipmentId);
                modal.style.display = "block";

                var span = document.getElementById("thumbnail-close-basic" + vm.equipmentId);
                $("#thumbnail-image-basic" + vm.equipmentId).width($('#thumbnail-view-basic' + vm.equipmentId).outerWidth());
                $("#thumbnail-image-basic" + vm.equipmentId).height($('#thumbnail-view-basic' + vm.equipmentId).outerHeight());
                $(".split-pane-divider").css('z-index', 0);
                span.onclick = function () {
                    modal.style.display = "none";
                }
                $scope.$evalAsync();
            }

            function loadEquipmentAssets() {
                AssetService.getAssetsByResource(vm.equipmentId).then(
                    function (data) {
                        vm.assets = data;
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                    }
                )
            }

            (function () {
                $scope.$on('app.equipment.tabActivated', function (event, data) {
                    if (data.tabId == 'details.basic') {
                        loadPersons();
                        loadEquipmentBasicDetails();
                        loadEquipmentAssets();
                    }
                });

            })();

        }
    }
);