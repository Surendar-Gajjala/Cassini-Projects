define(
    [
        'app/desktop/modules/mro/mro.module',
        'dropzone',
        'app/shared/services/core/workRequestService',
        'app/shared/services/core/assetService',
        'app/desktop/modules/directives/basicAttributeDetailsDirectiveController',
        'app/desktop/modules/directives/objectAttributeDetailsDirectiveController',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/mediaService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/attachmentService'
    ],
    function (module) {
        module.controller('WorkRequestBasicInfoController', WorkRequestBasicInfoController);

        function WorkRequestBasicInfoController($scope, $rootScope, $sce, $timeout, $state, $stateParams, $cookies, ECOService, CommonService,
                                                WorkRequestService, $translate, LoginService, AssetService, MediaService, AttachmentService) {
            var vm = this;
            vm.loading = true;
            vm.workRequestId = $stateParams.workRequestId;
            vm.workRequest = null;
            $scope.name = null;
            vm.persons = [];
            var parsed = angular.element("<div></div>");
            vm.updateWorkRequest = updateWorkRequest;

            $rootScope.loadWorkRequestBasicDetails = loadWorkRequestBasicDetails;
            function loadWorkRequestBasicDetails() {
                vm.loading = true;
                if (vm.workRequestId != null && vm.workRequestId != undefined) {
                    WorkRequestService.getWorkRequest(vm.workRequestId).then(
                        function (data) {
                            vm.workRequest = data;
                            $rootScope.workRequest = vm.workRequest;
                            $scope.name = vm.workRequest.name;
                            vm.loading = false;
                            CommonService.getMultiplePersonReferences([vm.workRequest], ['createdBy', 'modifiedBy', 'requestor']);
                            if (vm.workRequest.createdDate) {
                                if ($rootScope.currentLang == 'de') {
                                    vm.workRequest.createDateDe = moment(vm.workRequest.createdDate, "DD/MM/YYYY, HH:mm:ss").format("DD.MM.YYYY, HH:mm:ss");

                                } else {
                                    vm.workRequest.createDateDe = vm.workRequest.createdDate;
                                }

                            }
                            if (vm.workRequest.modifiedDate) {
                                if ($rootScope.currentLang == 'de') {
                                    vm.workRequest.modifiedDateDe = moment(vm.workRequest.modifiedDate, "DD/MM/YYYY, HH:mm:ss").format("DD.MM.YYYY, HH:mm:ss");

                                } else {
                                    vm.workRequest.modifiedDateDe = vm.workRequest.modifiedDate;
                                }
                            }
                            if (vm.workRequest.description != null && vm.workRequest.description != undefined) {
                                vm.workRequest.descriptionHtml = $sce.trustAsHtml(vm.workRequest.description.replace(/(?:\ r\n|\r|\n)/g, '<br>'));
                            }
                            loadAsset();
                            $timeout(function () {
                                $scope.$broadcast('app.attributes.tabActivated', {});
                            }, 1000);
                            vm.editStatus = false;
                            $rootScope.viewInfo.title = $translate.instant("WORK_REQUEST_DETAILS");
                            $rootScope.viewInfo.description = vm.workRequest.number + " , " + vm.workRequest.name;
                            $scope.$evalAsync();
                        }, function (error) {
                            $rootScope.showErrorMessage(error.message);
                            $rootScope.hideBusyIndicator();
                        }
                    )
                }
            }

            function loadAsset() {
                AssetService.getAsset(vm.workRequest.asset).then(
                    function (data) {
                        vm.asset = data;
                        if (vm.asset.resourceObject.objectType == 'MACHINE' && vm.asset.resourceObject.image != null) {
                            vm.asset.imagePath = "api/mes/machines/" + vm.asset.resourceObject.id + "/image/download?" + new Date().getTime();
                        } else if (vm.asset.resourceObject.objectType == 'TOOL' && vm.asset.resourceObject.image != null) {
                            vm.asset.imagePath = "api/mes/tools/" + vm.asset.resourceObject.id + "/image/download?" + new Date().getTime();
                        } else if (vm.asset.resourceObject.objectType == 'EQUIPMENT' && vm.asset.resourceObject.image != null) {
                            vm.asset.imagePath = "api/mes/equipments/" + vm.asset.resourceObject.id + "/image/download?" + new Date().getTime();
                        } else if (vm.asset.resourceObject.objectType == 'INSTRUMENT' && vm.asset.resourceObject.image != null) {
                            vm.asset.imagePath = "api/mes/instruments/" + vm.asset.resourceObject.id + "/image/download?" + new Date().getTime();
                        } else if (vm.asset.resourceObject.objectType == 'JIGFIXTURE' && vm.asset.resourceObject.image != null) {
                            vm.asset.imagePath = "api/mes/jigsfixs/" + vm.asset.resourceObject.id + "/image/download?" + new Date().getTime();
                        } else if (vm.asset.resourceObject.objectType == 'MATERIAL' && vm.asset.resourceObject.image != null) {
                            vm.asset.imagePath = "api/mes/materials/" + vm.asset.resourceObject.id + "/image/download?" + new Date().getTime();
                        }
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                        $rootScope.hideBusyIndicator();
                    }
                )
            }

            vm.showImage = showImage;
            function showImage(asset) {
                var modal = document.getElementById('item-thumbnail-basic' + asset.id);
                modal.style.display = "block";

                var span = document.getElementById("thumbnail-close-basic" + asset.id);
                $("#thumbnail-image-basic" + asset.id).width($('#thumbnail-view-basic' + asset.id).outerWidth());
                $("#thumbnail-image-basic" + asset.id).height($('#thumbnail-view-basic' + asset.id).outerHeight());
                $(".split-pane-divider").css('z-index', 0);
                span.onclick = function () {
                    modal.style.display = "none";
                }
                $scope.$evalAsync();
            }

            var itemNameValidation = parsed.html($translate.instant("NAME_VALIDATION")).html();
            var workRequestUpdated = parsed.html($translate.instant("WORK_REQUEST_UPDATED")).html();


            function validateWorkRequest() {
                var valid = true;
                if (vm.workRequest.name == null || vm.workRequest.name == ""
                    || vm.workRequest.name == undefined) {
                    valid = false;
                    vm.workRequest.name = $scope.name;
                    $rootScope.showWarningMessage(itemNameValidation);

                }


                return valid;
            }

            function updateWorkRequest() {
                if (validateWorkRequest()) {
                    $rootScope.showBusyIndicator($('.view-container'));
                    vm.workRequest.requestor = vm.workRequest.requestorObject.id;
                    WorkRequestService.updateWorkRequest(vm.workRequest).then(
                        function (data) {
                            loadWorkRequestBasicDetails();
                            vm.editMaintenance = false;
                            vm.editStatus = false;
                            $rootScope.showSuccessMessage(workRequestUpdated);
                            $rootScope.hideBusyIndicator();
                        }, function (error) {
                            vm.workRequest.name = $scope.name;
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

            vm.downloadFile = downloadFile;
            function downloadFile(attachment) {
                var url = "{0}//{1}/api/col/attachments/{2}/download".
                    format(window.location.protocol, window.location.host,
                    attachment.id);
                window.open(url);
            }

            vm.showImages = showImages;
            function showImages(workRequest, image) {
                vm.selectedImages = [];
                vm.selectedImage = image;
                vm.selectedImages = workRequest.images;
                angular.forEach(vm.selectedImages, function (wrImage) {
                    if (image.id === wrImage.id) {
                        wrImage.showImage = true;
                    } else {
                        wrImage.showImage = false;
                    }
                });
                var modal = document.getElementById("attachment-image");
                modal.style.display = "block";

                $timeout(function () {
                    $("#" + vm.selectedImage.id).width($('.image-view').outerWidth());
                    $("#" + vm.selectedImage.id).height($('.image-view').outerHeight());
                });
            }

            $scope.showPreviousImage = showPreviousImage;
            function showPreviousImage(image) {
                var index = vm.selectedImages.indexOf(image);
                vm.selectedImage = null;
                if (index === 0) {
                    image.showImage = false;
                    vm.selectedImages[vm.selectedImages.length - 1].showImage = true;
                    vm.selectedImage = vm.selectedImages[vm.selectedImages.length - 1];
                } else {
                    image.showImage = false;
                    vm.selectedImages[index - 1].showImage = true;
                    vm.selectedImage = vm.selectedImages[index - 1];
                }
                $timeout(function () {
                    $("#" + vm.selectedImage.id).width($('.image-view').outerWidth());
                    $("#" + vm.selectedImage.id).height($('.image-view').outerHeight());
                });
            }

            $scope.showNextImage = showNextImage;
            function showNextImage(image) {
                var index = vm.selectedImages.indexOf(image);
                vm.selectedImage = null;
                if (index === 0 || index < (vm.selectedImages.length - 1)) {
                    image.showImage = false;
                    vm.selectedImages[index + 1].showImage = true;
                    vm.selectedImage = vm.selectedImages[index + 1];
                } else {
                    image.showImage = false;
                    vm.selectedImages[0].showImage = true;
                    vm.selectedImage = vm.selectedImages[0];
                }

                $timeout(function () {
                    $("#" + vm.selectedImage.id).width($('.image-view').outerWidth());
                    $("#" + vm.selectedImage.id).height($('.image-view').outerHeight());
                });
            }

            $scope.hideImagesView = hideImagesView;
            function hideImagesView() {
                var modal = document.getElementById("attachment-image");
                modal.style.display = "none";
            }

            vm.editAttachment = false;
            vm.editWrAttachments = editWrAttachments;
            function editWrAttachments() {
                vm.editAttachment = true;
                loadDropZoneFiles();
            }

            var dropZoneComponent = null;
            vm.showFilesDropZone = false;
            function initDropZone() {
                //Dropzone.options.url
                var previewNode = document.getElementById("fileUploadTemplate");
                previewNode.id = "";
                previewNode.style.display = "block";
                var previewTemplate = previewNode.parentNode.innerHTML;
                previewNode.parentNode.removeChild(previewNode);

                dropZoneComponent = new Dropzone(document.getElementById("workReqBasicFiles"), {
                    url: "/target", // Set the url
                    thumbnailWidth: 50,
                    thumbnailHeight: 50,
                    parallelUploads: 20,
                    autoProcessQueue: false, // Make sure the files aren't queued until manually processed
                    previewTemplate: previewTemplate,
                    previewsContainer: "#fileUploadPreviews",
                    success: function (file, response) {
                    },
                    error: function (file, response) {
                    }
                });

                dropZoneComponent.on("queuecomplete", function (progress) {
                });

                dropZoneComponent.on("addedfiles", function (files) {
                    $(".drop-files-label").hide();
                    vm.workRequestFiles = dropZoneComponent.files;
                    $scope.$evalAsync();
                });
            }

            vm.selectWrFiles = selectWrFiles;
            function selectWrFiles() {
                $('#workReqBasicFiles')[0].click();
            }

            vm.workRequestFiles = [];
            vm.loadDropZoneFiles = loadDropZoneFiles;
            function loadDropZoneFiles() {
                vm.showFilesDropZone = !vm.showFilesDropZone;
                if (vm.showFilesDropZone) {
                    $timeout(function () {
                        initDropZone();
                    }, 1000);
                }
                else {
                    if (dropZoneComponent != null) {
                        vm.workRequestFiles = [];
                        vm.showFilesDropZone = true;
                        dropZoneComponent.destroy();
                    }
                }
            }

            vm.saveAttachments = saveAttachments;
            function saveAttachments() {
                if (vm.workRequestFiles.length > 0) {
                    WorkRequestService.uploadWorkRequestFiles(vm.workRequestId, vm.workRequestFiles).then(
                        function (data) {
                            vm.editAttachment = false;
                            dropZoneComponent = null;
                            loadDropZoneFiles();
                            loadWorkRequestBasicDetails();
                        }, function (error) {
                            $rootScope.showErrorMessage(error.message);
                            $rootScope.hideBusyIndicator();
                        })
                }
            }

            vm.cancelAttachments = cancelAttachments;
            function cancelAttachments() {
                vm.editAttachment = false;
                loadDropZoneFiles();
            }

            vm.deleteImage = deleteImage;
            var attachmentsDeletedMsg = parsed.html($translate.instant("ATTACHMENT_DELETE_MESSAGE")).html();

            function deleteImage(image) {
                $rootScope.showBusyIndicator($('.view-container'));
                MediaService.deleteMediaById(image.id).then(
                    function (data) {
                        vm.workRequest.images.splice(vm.workRequest.images.indexOf(image), 1);
                        $rootScope.showSuccessMessage(attachmentsDeletedMsg);
                        $rootScope.hideBusyIndicator();
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                        $rootScope.hideBusyIndicator();
                    }
                )
            }

            vm.deleteAttachment = deleteAttachment;
            function deleteAttachment(attachment) {
                $rootScope.showBusyIndicator($('.view-container'));
                AttachmentService.deleteAttachment(attachment.id).then(
                    function (data) {
                        vm.workRequest.attachmentList.splice(vm.workRequest.attachmentList.indexOf(attachment), 1);
                        $rootScope.showSuccessMessage(attachmentsDeletedMsg);
                        $rootScope.hideBusyIndicator();
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                        $rootScope.hideBusyIndicator();
                    }
                )
            }

            vm.filePreview = filePreview;
            function filePreview(file) {
                var fileId = file.id;
                var url = "{0}//{1}/api/col/attachments/{2}/preview".
                    format(window.location.protocol, window.location.host, fileId);
                var newWindow = window.open(url, "_blank");
                newWindow.addEventListener('load', function () {
                    newWindow.document.title = file.name;
                });
                $timeout(function () {
                    window.close();
                }, 2000);
            }

            (function () {
                $scope.$on('app.workRequest.tabActivated', function (event, data) {
                    if (data.tabId == 'details.basic') {
                        loadPersons();
                        loadWorkRequestBasicDetails();
                    }
                });

            })();

        }
    }
);