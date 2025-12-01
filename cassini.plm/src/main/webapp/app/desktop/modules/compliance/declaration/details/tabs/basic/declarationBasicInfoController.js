define(
    [
        'app/desktop/modules/compliance/compliance.module',
        'app/shared/services/core/declarationService',
        'app/desktop/modules/directives/basicAttributeDetailsDirectiveController',
        'app/desktop/directives/mes-mfr-data/mesMfrDataDirectiveController',
        'app/desktop/modules/directives/objectAttributeDetailsDirectiveController'
    ],
    function (module) {
        module.controller('DeclarationBasicInfoController', DeclarationBasicInfoController);

        function DeclarationBasicInfoController($scope, $rootScope, $sce, $timeout, $state, $stateParams, $cookies, ECOService, CommonService,
                                                DeclarationService, $translate, QualityTypeService, LoginService, DialogService) {
            var vm = this;
            vm.loading = true;
            vm.declarationId = $stateParams.declarationId;
            $scope.name = null;
            vm.persons = [];
            var parsed = angular.element("<div></div>");
            vm.updateDeclaration = updateDeclaration;
            $rootScope.loadDeclarationBasicDetails = loadDeclarationBasicDetails;

            function loadDeclarationBasicDetails() {
                vm.loading = true;
                if (vm.declarationId != null && vm.declarationId != undefined) {
                    DeclarationService.getDeclaration(vm.declarationId).then(
                        function (data) {
                            vm.declaration = data;
                            $rootScope.declaration = vm.declaration;
                            $rootScope.viewInfo.title = "<div class='item-number'>" +
                                "{0}</div>".format(vm.declaration.number);
                            $rootScope.viewInfo.description = vm.declaration.name;
                            $scope.name = vm.declaration.name;
                            CommonService.getPersonReferences([vm.declaration], 'modifiedBy');
                            CommonService.getPersonReferences([vm.declaration], 'createdBy');
                            if (vm.declaration.description != null && vm.declaration.description != undefined) {
                                vm.declaration.descriptionHtml = $sce.trustAsHtml(vm.declaration.description.replace(/(?:\ r\n|\r|\n)/g, '<br>'));
                            }
                            $timeout(function () {
                                $scope.$broadcast('app.attributes.tabActivated', {});
                            }, 1000);
                            vm.loading = false;
                            vm.editStatus = false;
                            $scope.$evalAsync();
                        },
                        function (error) {
                            $rootScope.showErrorMessage(error.message);
                        }
                    )
                }
            }


            var itemNameValidation = parsed.html($translate.instant("NAME_VALIDATION")).html();
            var declarationManagerValidation = parsed.html($translate.instant("DECLARATION_MANAGER_VALIDATION")).html();
            var enterValidEmail = parsed.html($translate.instant("ENTER_VALID_EMAIL")).html();
            var declarationUpdatedSuccess = parsed.html($translate.instant("DECLARATION_UPDATED_SUCCESS")).html();
            var confirmation = parsed.html($translate.instant("CONFIRMATION")).html();
            var imageRemoved = parsed.html($translate.instant("IMAGE_REMOVED")).html();
            var removeImageMsg = parsed.html($translate.instant("REMOVE_IMAGE_MSG")).html();
            var supplierNameCheck = parsed.html($translate.instant("SUPPLIER_NAME_CHECK")).html();


            function validateDeclaration() {
                var valid = true;
                if (vm.declaration.name == null || vm.declaration.name == ""
                    || vm.declaration.name == undefined) {
                    valid = false;
                    vm.declaration.name = $scope.name;
                    $rootScope.showWarningMessage(itemNameValidation);

                } else if (vm.declaration.supplier == null || vm.declaration.supplier == ""
                    || vm.declaration.supplier == undefined) {
                    valid = false;
                    vm.declaration.name = $scope.name;
                    $rootScope.showWarningMessage(supplierNameCheck);

                }


                return valid;
            }

            function updateDeclaration() {
                if (validateDeclaration()) {
                    $rootScope.showBusyIndicator($('.view-container'));
                    /*  vm.declaration.supplier = vm.workRequest.requestorObject.id;*/
                    DeclarationService.updateDeclaration(vm.declaration).then(
                        function (data) {
                            loadDeclarationBasicDetails();
                            vm.editMaintenance = false;
                            vm.editStatus = false;
                            $rootScope.showSuccessMessage(declarationUpdatedSuccess);
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
                vm.oldStatus = vm.declaration.active;
            }

            vm.cancelStatus = cancelStatus;
            function cancelStatus() {
                vm.editStatus = false;
                vm.declaration.active = vm.oldStatus;
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
                vm.editMaintenance = true;
                vm.oldMaintenane = vm.declaration.requiresMaintenance;
            }

            vm.cancelMaintenance = cancelMaintenance;
            function cancelMaintenance() {
                vm.editMaintenance = false;
                vm.declaration.requiresMaintenance = vm.oldMaintenane;
            }

            vm.image = null;
            $scope.saveImage = saveImage;
            function saveImage(image) {
                if (image == null || image == "") {
                    $rootScope.showWarningMessage(SelectThumbnail);

                } else {
                    vm.image = image;
                    if (validateImage()) {
                        DeclarationService.uploadImage(vm.declarationId, image).then(
                            function (data) {
                                $rootScope.showSuccessMessage("Image uploaded successfully");
                                loadDeclarationBasicDetails();
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
                    if (vm.declaration.image == null || vm.declaration.image == "") {
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
                    title: confirmation,
                    message: removeImageMsg,
                    okButtonClass: 'btn-danger'
                }
                DialogService.confirm(options, function (yes) {
                    if (yes == true) {
                        vm.declaration.image = null;
                        DeclarationService.updateDeclaration(vm.declaration).then(
                            function (data) {
                                $rootScope.showSuccessMessage(imageRemoved);
                                loadDeclarationBasicDetails();
                            }, function (error) {
                                $rootScope.showErrorMessage(error.message);
                            }
                        )
                    }
                })
            }

            vm.showImage = showImage;
            function showImage(item) {
                var modal = document.getElementById('item-thumbnail-basic' + vm.declarationId);
                modal.style.display = "block";

                var span = document.getElementById("thumbnail-close-basic" + vm.declarationId);
                $("#thumbnail-image-basic" + vm.declarationId).width($('#thumbnail-view-basic' + vm.declarationId).outerWidth());
                $("#thumbnail-image-basic" + vm.declarationId).height($('#thumbnail-view-basic' + vm.declarationId).outerHeight());
                $(".split-pane-divider").css('z-index', 0);
                span.onclick = function () {
                    modal.style.display = "none";
                }
                $scope.$evalAsync();
            }

            (function () {
                $scope.$on('app.declaration.tabActivated', function (event, data) {
                    if (data.tabId == 'details.basic') {
                        loadPersons();
                        loadDeclarationBasicDetails();
                    }
                });

            })();

        }
    }
);