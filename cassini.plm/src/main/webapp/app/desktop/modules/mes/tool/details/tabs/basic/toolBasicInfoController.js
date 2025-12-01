define(
    [
        'app/desktop/modules/mes/mes.module',
        'app/shared/services/core/toolService',
        'app/desktop/modules/directives/basicAttributeDetailsDirectiveController',
        'app/desktop/directives/mes-mfr-data/mesMfrDataDirectiveController',
        'app/desktop/modules/directives/objectAttributeDetailsDirectiveController',
        'app/shared/services/core/assetService'
    ],
    function (module) {
        module.controller('ToolBasicInfoController', ToolBasicInfoController);

        function ToolBasicInfoController($scope, $rootScope, $sce, $timeout, $state, $stateParams, $cookies, ECOService, CommonService,
                                         ToolService, $translate, QualityTypeService, DialogService, AssetService) {
            var vm = this;
            vm.loading = true;
            vm.toolId = $stateParams.toolId;

            var parsed = angular.element("<div></div>");
            var nameValidation = parsed.html($translate.instant("NAME_VALIDATION")).html();
            var toolUpdated = parsed.html($translate.instant("TOOL_UPDATED")).html();

            function loadTool() {
                ToolService.getTool(vm.toolId).then(
                    function (data) {
                        vm.tool = data;
                        if (vm.tool.manufacturerData == null || vm.tool.manufacturerData == "") {
                            vm.tool.manufacturerData = {
                                object: vm.tool.id,
                                mfrName: null,
                                mfrDescription: null,
                                mfrModelNumber: null,
                                mfrPartNumber: null,
                                mfrSerialNumber: null,
                                mfrDate: null
                            }
                        }

                        if (vm.tool.image != null) {
                            vm.tool.imagePath = "api/mes/tools/" + vm.toolId + "/image/download?" + new Date().getTime();
                        }
                        $scope.name = vm.tool.name;
                        $rootScope.tool = vm.tool;
                        loadPersons();
                        vm.loading = false;
                        vm.editStatus = false;
                        $rootScope.viewInfo.title = $translate.instant("TOOL_DETAILS");
                        $rootScope.viewInfo.description = vm.tool.number + " , " + vm.tool.name;
                        $timeout(function () {
                            $scope.$broadcast('app.attributes.tabActivated', {});
                        }, 1000);
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                        $rootScope.hideBusyIndicator();
                    }
                )
            }

            function loadPersons() {
                var personIds = [vm.tool.createdBy];

                if (vm.tool.createdBy != vm.tool.modifiedBy) {
                    personIds.push(vm.tool.modifiedBy);
                }

                CommonService.getPersons(personIds).then(
                    function (persons) {
                        var map = new Hashtable();
                        angular.forEach(persons, function (person) {
                            map.put(person.id, person);
                        });

                        if (vm.tool.createdBy != null) {
                            var person = map.get(vm.tool.createdBy);
                            if (person != null) {
                                vm.tool.createdByPerson = person;
                            }
                            else {
                                vm.tool.createdByPerson = {firstName: ""};
                            }
                        }

                        if (vm.tool.modifiedBy != null) {
                            person = map.get(vm.tool.modifiedBy);
                            if (person != null) {
                                vm.tool.modifiedByPerson = person;
                            }
                            else {
                                vm.tool.modifiedByPerson = {firstName: ""};
                            }
                        }
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                        $rootScope.hideBusyIndicator();
                    }
                );
            }

            vm.updateTool = updateTool;
            function updateTool() {
                if (validate()) {
                    $rootScope.showBusyIndicator($('.view-container'));
                    ToolService.updateTool(vm.tool).then(
                        function (data) {
                            loadTool();
                            vm.editMaintenance = false;
                            vm.editStatus = false;
                            $rootScope.showSuccessMessage(toolUpdated);
                            $rootScope.hideBusyIndicator();
                        }, function (error) {
                            vm.tool.name = $scope.name;
                            $rootScope.showErrorMessage(error.message);
                            $rootScope.hideBusyIndicator();
                        }
                    )
                }
            }

            function validate() {
                var valid = true;
                if (vm.tool.name == null || vm.tool.name == "" || vm.tool.name == undefined) {
                    valid = false;
                    vm.tool.name = $scope.name;
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
                vm.oldStatus = vm.tool.active;
            }

            vm.cancelStatus = cancelStatus;
            function cancelStatus() {
                vm.editStatus = false;
                vm.tool.active = vm.oldStatus;
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
                    vm.oldMaintenane = vm.tool.requiresMaintenance;
                }
            }

            vm.cancelMaintenance = cancelMaintenance;
            function cancelMaintenance() {
                vm.editMaintenance = false;
                vm.tool.requiresMaintenance = vm.oldMaintenane;
            }

            vm.image = null;
            $scope.saveImage = saveImage;
            function saveImage(image) {
                if (image == null || image == "") {
                    $rootScope.showWarningMessage(SelectThumbnail);

                } else {
                    vm.image = image;
                    if (validateImage()) {
                        ToolService.uploadImage(vm.toolId, image).then(
                            function (data) {
                                $rootScope.showSuccessMessage("Image uploaded successfully");
                                loadTool();
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
                    if (vm.tool.image == null || vm.tool.image == "") {
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
                        vm.tool.image = null;
                        ToolService.updateTool(vm.tool).then(
                            function (data) {
                                $rootScope.showSuccessMessage("Image removed successfully");
                                loadTool();
                            }, function (error) {
                                $rootScope.showErrorMessage(error.message);
                            }
                        )
                    }
                })
            }

            vm.showImage = showImage;
            function showImage(item) {
                var modal = document.getElementById('item-thumbnail-basic' + vm.toolId);
                modal.style.display = "block";

                var span = document.getElementById("thumbnail-close-basic" + vm.toolId);
                $("#thumbnail-image-basic" + vm.toolId).width($('#thumbnail-view-basic' + vm.toolId).outerWidth());
                $("#thumbnail-image-basic" + vm.toolId).height($('#thumbnail-view-basic' + vm.toolId).outerHeight());
                $(".split-pane-divider").css('z-index', 0);
                span.onclick = function () {
                    modal.style.display = "none";
                }
                $scope.$evalAsync();
            }

            function loadToolAssets() {
                AssetService.getAssetsByResource(vm.toolId).then(
                    function (data) {
                        vm.assets = data;
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                    }
                )
            }

            (function () {
                $scope.$on('app.tool.tabActivated', function (event, data) {
                    if (data.tabId == 'details.basic') {
                        loadTool();
                        loadToolAssets();
                    }
                });
                loadTool();
            })();

        }
    }
);