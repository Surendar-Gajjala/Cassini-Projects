define(
    [
        'app/desktop/modules/mes/mes.module',
        'app/shared/services/core/instrumentService',
        'app/desktop/modules/directives/basicAttributeDetailsDirectiveController',
        'app/desktop/directives/mes-mfr-data/mesMfrDataDirectiveController',
        'app/desktop/modules/directives/objectAttributeDetailsDirectiveController',
        'app/shared/services/core/assetService'
    ],
    function (module) {
        module.controller('InstrumentBasicInfoController', InstrumentBasicInfoController);

        function InstrumentBasicInfoController($scope, $rootScope, $sce, $timeout, $state, $stateParams, $cookies, ECOService, CommonService,
                                               InstrumentService, $translate, QualityTypeService, LoginService, DialogService, AssetService) {
            var vm = this;
            vm.loading = true;
            vm.instrumentId = $stateParams.instrumentId;
            vm.instrument = null;
            $scope.name = null;
            vm.persons = [];
            var parsed = angular.element("<div></div>");
            vm.updateInstrument = updateInstrument;

            function loadInstrumentBasicDetails() {
                vm.loading = true;
                if (vm.instrumentId != null && vm.instrumentId != undefined) {
                    InstrumentService.getInstrument(vm.instrumentId).then(
                        function (data) {
                            vm.instrument = data;
                            if (vm.instrument.manufacturerData == null || vm.instrument.manufacturerData == "") {
                                vm.instrument.manufacturerData = {
                                    object: vm.instrument.id,
                                    mfrName: null,
                                    mfrDescription: null,
                                    mfrModelNumber: null,
                                    mfrPartNumber: null,
                                    mfrSerialNumber: null,
                                    mfrDate: null
                                }
                            }
                            $rootScope.instrument = vm.instrument;
                            $scope.name = vm.instrument.name;
                            CommonService.getPersonReferences([vm.instrument], 'modifiedBy');
                            CommonService.getPersonReferences([vm.instrument], 'createdBy');
                            if (vm.instrument.createdDate) {
                                if ($rootScope.currentLang == 'de') {
                                    vm.instrument.createDateDe = moment(vm.instrument.createdDate, "DD/MM/YYYY, HH:mm:ss").format("DD.MM.YYYY, HH:mm:ss");

                                } else {
                                    vm.instrument.createDateDe = vm.instrument.createdDate;
                                }

                            }
                            if (vm.instrument.modifiedDate) {
                                if ($rootScope.currentLang == 'de') {
                                    vm.instrument.modifiedDateDe = moment(vm.instrument.modifiedDate, "DD/MM/YYYY, HH:mm:ss").format("DD.MM.YYYY, HH:mm:ss");

                                } else {
                                    vm.instrument.modifiedDateDe = vm.instrument.modifiedDate;
                                }
                            }
                            if (vm.instrument.description != null && vm.instrument.description != undefined) {
                                vm.instrument.descriptionHtml = $sce.trustAsHtml(vm.instrument.description.replace(/(?:\ r\n|\r|\n)/g, '<br>'));
                            }
                            if (vm.instrument.image != null) {
                                vm.instrument.imagePath = "api/mes/instruments/" + vm.instrumentId + "/image/download?" + new Date().getTime();
                            }
                            vm.loading = false;
                            $rootScope.viewInfo.title = $translate.instant("INSTRUMENT_DETAILS");
                            $rootScope.viewInfo.description = vm.instrument.number + " , " + vm.instrument.name;
                            $timeout(function () {
                                $scope.$broadcast('app.attributes.tabActivated', {});
                            }, 1000);
                            $scope.$evalAsync();
                        }, function (error) {
                            $rootScope.showErrorMessage(error.message);
                        }
                    )
                }
            }


            var itemNameValidation = parsed.html($translate.instant("NAME_VALIDATION")).html();
            var instrumentManagerValidation = parsed.html($translate.instant("INSTRUMENT_MANAGER_VALIDATION")).html();
            var enterValidEmail = parsed.html($translate.instant("ENTER_VALID_EMAIL")).html();


            function validateInstrument() {
                var valid = true;
                if (vm.instrument.name == null || vm.instrument.name == ""
                    || vm.instrument.name == undefined) {
                    valid = false;
                    vm.instrument.name = $scope.name;
                    $rootScope.showWarningMessage(itemNameValidation);

                }


                return valid;
            }


            var instrumentUpdateMessage = parsed.html($translate.instant("INSTRUMENT_UPDATED")).html();

            function updateInstrument() {
                if (validateInstrument()) {
                    $rootScope.showBusyIndicator($('.view-container'));
                    InstrumentService.updateInstrument(vm.instrument).then(
                        function (data) {
                            loadInstrumentBasicDetails();
                            vm.editMaintenance = false;
                            vm.editStatus = false;
                            $rootScope.showSuccessMessage(instrumentUpdateMessage);
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
                vm.oldStatus = vm.instrument.active;
            }

            vm.cancelStatus = cancelStatus;
            function cancelStatus() {
                vm.editStatus = false;
                vm.instrument.active = vm.oldStatus;
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
                    vm.oldMaintenane = vm.instrument.requiresMaintenance;
                }
            }

            vm.cancelMaintenance = cancelMaintenance;
            function cancelMaintenance() {
                vm.editMaintenance = false;
                vm.instrument.requiresMaintenance = vm.oldMaintenane;
            }

            vm.image = null;
            $scope.saveImage = saveImage;
            function saveImage(image) {
                if (image == null || image == "") {
                    $rootScope.showWarningMessage(SelectThumbnail);

                } else {
                    vm.image = image;
                    if (validateImage()) {
                        InstrumentService.uploadImage(vm.instrumentId, image).then(
                            function (data) {
                                $rootScope.showSuccessMessage("Image uploaded successfully");
                                loadInstrumentBasicDetails();
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

            function validateImage() {
                var valid = true;
                if (vm.image != null) {
                    if (vm.instrument.image == null || vm.instrument.image == "") {
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
                        vm.instrument.image = null;
                        InstrumentService.updateInstrument(vm.instrument).then(
                            function (data) {
                                $rootScope.showSuccessMessage("Image removed successfully");
                                loadInstrumentBasicDetails();
                            }, function (error) {
                                $rootScope.showErrorMessage(error.message);
                            }
                        )
                    }
                })
            }

            vm.showImage = showImage;
            function showImage(item) {
                var modal = document.getElementById('item-thumbnail-basic' + vm.instrumentId);
                modal.style.display = "block";

                var span = document.getElementById("thumbnail-close-basic" + vm.instrumentId);
                $("#thumbnail-image-basic" + vm.instrumentId).width($('#thumbnail-view-basic' + vm.instrumentId).outerWidth());
                $("#thumbnail-image-basic" + vm.instrumentId).height($('#thumbnail-view-basic' + vm.instrumentId).outerHeight());
                $(".split-pane-divider").css('z-index', 0);
                span.onclick = function () {
                    modal.style.display = "none";
                }
                $scope.$evalAsync();
            }

            function loadInstrumentAssets() {
                AssetService.getAssetsByResource(vm.instrumentId).then(
                    function (data) {
                        vm.assets = data;
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                    }
                )
            }

            (function () {
                $scope.$on('app.instrument.tabActivated', function (event, data) {
                    if (data.tabId == 'details.basic') {
                        loadPersons();
                        loadInstrumentBasicDetails();
                        loadInstrumentAssets();
                    }
                });

            })();

        }
    }
);