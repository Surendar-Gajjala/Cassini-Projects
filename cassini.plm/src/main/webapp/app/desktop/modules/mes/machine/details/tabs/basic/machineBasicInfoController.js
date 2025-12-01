define(
    [
        'app/desktop/modules/mes/mes.module',
        'app/shared/services/core/machineService',
        'app/desktop/modules/directives/basicAttributeDetailsDirectiveController',
        'app/desktop/directives/mes-mfr-data/mesMfrDataDirectiveController',
        'app/desktop/modules/directives/objectAttributeDetailsDirectiveController',
        'app/shared/services/core/assetService'
    ],
    function (module) {
        module.controller('MachineBasicInfoController', MachineBasicInfoController);

        function MachineBasicInfoController($scope, $rootScope, $sce, $timeout, $state, $stateParams, $cookies, ECOService, CommonService,
                                            MachineService, $translate, QualityTypeService, DialogService, LoginService, AssetService) {
            var vm = this;
            vm.loading = true;
            vm.machineId = $stateParams.machineId;
            vm.machine = null;
            $scope.name = null;
            vm.persons = [];
            var parsed = angular.element("<div></div>");
            vm.updateMachine = updateMachine;

            function loadMachineBasicDetails() {
                vm.loading = true;
                if (vm.machineId != null && vm.machineId != undefined) {
                    MachineService.getMachine(vm.machineId).then(
                        function (data) {
                            vm.machine = data;
                            if (vm.machine.manufacturerData == null || vm.machine.manufacturerData == "") {
                                vm.machine.manufacturerData = {
                                    object: vm.machine.id,
                                    mfrName: null,
                                    mfrDescription: null,
                                    mfrModelNumber: null,
                                    mfrPartNumber: null,
                                    mfrSerialNumber: null,
                                    mfrDate: null
                                }
                            }
                            $rootScope.machine = vm.machine;
                            $scope.name = vm.machine.name;
                            CommonService.getPersonReferences([vm.machine], 'modifiedBy');
                            CommonService.getPersonReferences([vm.machine], 'createdBy');
                            if (vm.machine.createdDate) {
                                if ($rootScope.currentLang == 'de') {
                                    vm.machine.createDateDe = moment(vm.machine.createdDate, "DD/MM/YYYY, HH:mm:ss").format("DD.MM.YYYY, HH:mm:ss");

                                } else {
                                    vm.machine.createDateDe = vm.machine.createdDate;
                                }

                            }
                            if (vm.machine.modifiedDate) {
                                if ($rootScope.currentLang == 'de') {
                                    vm.machine.modifiedDateDe = moment(vm.machine.modifiedDate, "DD/MM/YYYY, HH:mm:ss").format("DD.MM.YYYY, HH:mm:ss");

                                } else {
                                    vm.machine.modifiedDateDe = vm.machine.modifiedDate;
                                }
                            }
                            if (vm.machine.description != null && vm.machine.description != undefined) {
                                vm.machine.descriptionHtml = $sce.trustAsHtml(vm.machine.description.replace(/(?:\ r\n|\r|\n)/g, '<br>'));
                            }
                            if (vm.machine.image != null) {
                                vm.machine.imagePath = "api/mes/machines/" + vm.machineId + "/image/download?" + new Date().getTime();
                            }
                            vm.loading = false;
                            $rootScope.viewInfo.title = $translate.instant("MACHINE_DETAILS");
                            $rootScope.viewInfo.description = vm.machine.number + " , " + vm.machine.name;
                            $timeout(function () {
                                $scope.$broadcast('app.attributes.tabActivated', {});
                            }, 1000);
                            $scope.$evalAsync();
                        }, function (error) {
                            $scope.name = vm.machine.name;
                            $rootScope.showErrorMessage(error.message);
                            $rootScope.hideBusyIndicator();
                        }
                    )
                }
            }

            vm.image = null;
            $scope.saveImage = saveImage;
            function saveImage(image) {
                if (image == null || image == "") {
                    $rootScope.showWarningMessage(SelectThumbnail);

                } else {
                    vm.image = image;
                    if (validateImage()) {
                        MachineService.uploadImage(vm.machineId, image).then(
                            function (data) {
                                $rootScope.showSuccessMessage("Image uploaded successfully");
                                loadMachineBasicDetails();
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
                    if (vm.machine.image == null || vm.machine.image == "") {
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
                        vm.machine.image = null;
                        MachineService.updateMachine(vm.machine).then(
                            function (data) {
                                $rootScope.showSuccessMessage("Image removed successfully");
                                loadMachineBasicDetails();
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
                var modal = document.getElementById('item-thumbnail-basic' + vm.machineId);
                modal.style.display = "block";

                var span = document.getElementById("thumbnail-close-basic" + vm.machineId);
                $("#thumbnail-image-basic" + vm.machineId).width($('#thumbnail-view-basic' + vm.machineId).outerWidth());
                $("#thumbnail-image-basic" + vm.machineId).height($('#thumbnail-view-basic' + vm.machineId).outerHeight());
                $(".split-pane-divider").css('z-index', 0);
                span.onclick = function () {
                    modal.style.display = "none";
                }
                $scope.$evalAsync();
            }

            var nameValidation = parsed.html($translate.instant("NAME_CANNOT_BE_EMPTY")).html();
            var machineManagerValidation = parsed.html($translate.instant("MACHINE_MANAGER_VALIDATION")).html();
            var enterValidEmail = parsed.html($translate.instant("ENTER_VALID_EMAIL")).html();
            var machineUpdatedSuccess = parsed.html($translate.instant("MACHINE_UPDATED")).html();


            vm.showWorkCenter = showWorkCenter;
            function showWorkCenter(machine) {
                $state.go('app.mes.masterData.workcenter.details', {
                    workcenterId: machine.workCenterId,
                    tab: 'details.basic'
                });
            }

            function validateMachine() {
                var valid = true;
                if (vm.machine.name == null || vm.machine.name == ""
                    || vm.machine.name == undefined) {
                    valid = false;
                    vm.machine.name = $scope.name;
                    $rootScope.showWarningMessage(nameValidation);

                }


                return valid;
            }

            function updateMachine() {
                if (validateMachine()) {
                    $rootScope.showBusyIndicator($('.view-container'));
                    MachineService.updateMachine(vm.machine).then(
                        function (data) {
                            loadMachineBasicDetails();
                            vm.editStatus = false;
                            vm.editMaintenance = false;
                            $rootScope.viewInfo.description = vm.machine.name;
                            $rootScope.showSuccessMessage(machineUpdatedSuccess);
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
                vm.oldStatus = vm.machine.active;
            }

            vm.cancelStatus = cancelStatus;
            function cancelStatus() {
                vm.editStatus = false;
                vm.machine.active = vm.oldStatus;
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
                    vm.oldMaintenane = vm.machine.requiresMaintenance;
                }
            }

            vm.cancelMaintenance = cancelMaintenance;
            function cancelMaintenance() {
                vm.editMaintenance = false;
                vm.machine.requiresMaintenance = vm.oldMaintenane;
            }

            function loadMachineAssets() {
                AssetService.getAssetsByResource(vm.machineId).then(
                    function (data) {
                        vm.assets = data;
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                    }
                )
            }

            (function () {
                $scope.$on('app.machine.tabActivated', function (event, data) {
                    if (data.tabId == 'details.basic') {
                        loadPersons();
                        loadMachineBasicDetails();
                        loadMachineAssets();
                    }
                });

            })();

        }
    }
);