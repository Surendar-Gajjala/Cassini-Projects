define(
    [
        'app/desktop/modules/mes/mes.module',
        'app/desktop/modules/directives/basicAttributeDetailsDirectiveController',
        'app/desktop/directives/mes-mfr-data/mesMfrDataDirectiveController',
        'app/desktop/modules/directives/objectAttributeDetailsDirectiveController',
        'app/shared/services/core/assetService'
    ],
    function (module) {
        module.controller('JigsAndFixturesBasicInfoController', JigsAndFixturesBasicInfoController);

        function JigsAndFixturesBasicInfoController($scope, $rootScope, $sce, $timeout, $state, $stateParams, $cookies,
                                                    CommonService, $translate, JigsFixtureService, DialogService, AssetService) {

            var vm = this;
            vm.loading = true;
            vm.jigsFixId = $stateParams.jigsFixId;

            var parsed = angular.element("<div></div>");
            var jigUpdateMsg = parsed.html($translate.instant("JIGS_UPDATE_MSG")).html();
            var fixtureUpdateMsg = parsed.html($translate.instant("JIGS_UPDATE_MSG")).html();
            var nameValidation = parsed.html($translate.instant("NAME_VALIDATION")).html();


            vm.updateJigsFixture = updateJigsFixture;

            function loadJigAndFixture() {
                JigsFixtureService.getJigsFix(vm.jigsFixId).then(
                    function (data) {
                        vm.jigsFixture = data;
                        $rootScope.jigsFixture = data;
                        if (vm.jigsFixture.manufacturerData == null || vm.jigsFixture.manufacturerData == "") {
                            vm.jigsFixture.manufacturerData = {
                                object: vm.jigsFixture.id,
                                mfrName: null,
                                mfrDescription: null,
                                mfrModelNumber: null,
                                mfrPartNumber: null,
                                mfrSerialNumber: null,
                                mfrDate: null
                            }
                        }
                        if (vm.jigsFixture.image != null) {
                            vm.jigsFixture.imagePath = "api/mes/jigsfixs/" + vm.jigsFixId + "/image/download?" + new Date().getTime();
                        }
                        $scope.name = vm.jigsFixture.name;
                        vm.loading = false;
                        if (vm.jigsFixture.jigType == 'JIG')$rootScope.viewInfo.title = $translate.instant("JIG_DETAILS");
                        if (vm.jigsFixture.jigType == 'FIXTURE')$rootScope.viewInfo.title = $translate.instant("FIXTURE_DETAILS");
                        $rootScope.viewInfo.description = vm.jigsFixture.number + " , " + vm.jigsFixture.name;
                        CommonService.getPersonReferences([vm.jigsFixture], 'createdBy');
                        CommonService.getPersonReferences([vm.jigsFixture], 'modifiedBy');
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

            function updateJigsFixture() {
                if (validate()) {
                    JigsFixtureService.updateJigsFix(vm.jigsFixture).then(
                        function (data) {
                            vm.jigsFixture = data;
                            loadJigAndFixture();
                            vm.loading = false;
                            vm.editStatus = false;
                            vm.editMaintenance = false;
                            if (vm.jigsFixture.jigType == 'JIG') {
                                $rootScope.showSuccessMessage(jigUpdateMsg);
                            } else {
                                $rootScope.showSuccessMessage(fixtureUpdateMsg);
                            }
                            CommonService.getPersonReferences([vm.jigsFixture], 'createdBy');
                            CommonService.getPersonReferences([vm.jigsFixture], 'modifiedBy');
                        }, function (error) {
                            vm.jigsFixture.name = $scope.name;
                            $rootScope.showErrorMessage(error.message);
                            $rootScope.hideBusyIndicator();
                        }
                    )
                }
            }


            function validate() {
                var valid = true;
                if (vm.jigsFixture.name == null || vm.jigsFixture.name == "" || vm.jigsFixture.name == undefined) {
                    valid = false;
                    vm.jigsFixture.name = $scope.name;
                    $rootScope.showWarningMessage(nameValidation);
                }

                return valid;
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
                vm.oldStatus = vm.jigsFixture.active;
            }

            vm.cancelStatus = cancelStatus;
            function cancelStatus() {
                vm.editStatus = false;
                vm.jigsFixture.active = vm.oldStatus;
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
                    vm.oldMaintenane = vm.jigsFixture.requiresMaintenance;
                }
            }

            vm.cancelMaintenance = cancelMaintenance;
            function cancelMaintenance() {
                vm.editMaintenance = false;
                vm.jigsFixture.requiresMaintenance = vm.oldMaintenane;
            }

            vm.image = null;
            $scope.saveImage = saveImage;
            function saveImage(image) {
                if (image == null || image == "") {
                    $rootScope.showWarningMessage(SelectThumbnail);

                } else {
                    vm.image = image;
                    if (validateImage()) {
                        JigsFixtureService.uploadImage(vm.jigsFixId, image).then(
                            function (data) {
                                $rootScope.showSuccessMessage("Image uploaded successfully");
                                loadJigAndFixture();
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
                    if (vm.jigsFixture.image == null || vm.jigsFixture.image == "") {
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
                        vm.jigsFixture.image = null;
                        JigsFixtureService.updateJigsFix(vm.jigsFixture).then(
                            function (data) {
                                $rootScope.showSuccessMessage("Image removed successfully");
                                loadJigAndFixture();
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
                var modal = document.getElementById('item-thumbnail-basic' + vm.jigsFixId);
                modal.style.display = "block";

                var span = document.getElementById("thumbnail-close-basic" + vm.jigsFixId);
                $("#thumbnail-image-basic" + vm.jigsFixId).width($('#thumbnail-view-basic' + vm.jigsFixId).outerWidth());
                $("#thumbnail-image-basic" + vm.jigsFixId).height($('#thumbnail-view-basic' + vm.jigsFixId).outerHeight());
                $(".split-pane-divider").css('z-index', 0);
                span.onclick = function () {
                    modal.style.display = "none";
                }
                $scope.$evalAsync();
            }


            function loadJigAndFixtrueAssets() {
                AssetService.getAssetsByResource(vm.jigsFixId).then(
                    function (data) {
                        vm.assets = data;
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                    }
                )
            }

            (function () {
                $scope.$on('app.jigsAndFixtures.tabActivated', function (event, data) {
                    if (data.tabId == 'details.basic') {
                        loadJigAndFixture();
                        loadJigAndFixtrueAssets();
                    }
                });

            })();

        }
    }
);