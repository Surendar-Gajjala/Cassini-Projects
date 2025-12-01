define(
    [
        'app/desktop/modules/pqm/pqm.module',
        'dropzone',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/commonService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/attachmentService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/mediaService'
    ],
    function (module) {
        module.controller('ChecklistAttachmentsController', ChecklistAttachmentsController);

        function ChecklistAttachmentsController($scope, $rootScope, $timeout, $state, $stateParams, $cookies, $translate,
                                                CommonService, MediaService, AttachmentService) {
            var vm = this;
            var parsed = angular.element("<div></div>");

            vm.loading = true;
            vm.files = [];
            vm.showDropzone = false;
            vm.showFileDropzone = false;
            vm.checklist = $scope.data.checklistDetails;
            vm.hasPermission = true;
            vm.hasPermission = $scope.data.checklistPermission;
            vm.checklist.attachmentCount = 0;
            var objectId = $scope.data.checklistParentId;
            $scope.checklistMode = $scope.data.checklistMode;
            vm.planChecklistId = null;
            vm.checklistId = null;
            var attachmentsUploadedAndExistMsg = parsed.html($translate.instant("CHECKLIST_ATTACHMENTS_UPLOAD")).html();
            var attachmentsUploaded = parsed.html($translate.instant("ATTACHMENTS_UPLOADED")).html();
            var attachmentsExists = parsed.html($translate.instant("ATTACHMENTS_EXISTS")).html();
            var attachmentsDeletedMsg = parsed.html($translate.instant("ATTACHMENT_DELETE_MESSAGE")).html();

            function handleDragEnter(e) {
                $("#itemFilesTableContainer")[0].classList.add('drag-over');
                vm.showFileDropzone = true;

            }

            function handleDragLeave(e) {
                $("#itemFilesTableContainer")[0].classList.remove('drag-over');
                vm.showFileDropzone = false;
            }

            $scope.registerCallBack = function (callback) {
                $scope.conCallBack = callback;
            };

            $scope.showAutoDeskFile = function (file) {
                $scope.conCallBack(file);
            };

            function loadFileConfig() {
                vm.fileConfig = {};
                vm.fileConfig.fileTypeError = false;
                vm.fileConfig.fileSizeError = false;
                vm.fileConfig.message = "";
                var context = 'APPLICATION';
                CommonService.getPreferenceByContext(context).then(
                    function (data) {
                        vm.configs = data;
                        angular.forEach(vm.configs, function (config) {
                            if (config.preferenceKey == "APPLICATION.FILESIZE") {
                                vm.fileConfig.fileSize = config.integerValue;
                            }
                            if (config.preferenceKey == "APPLICATION.FILETYPE") {
                                vm.fileConfig.fileType = config.stringValue.split("\n");
                            }
                        });

                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                        $rootScope.hideBusyIndicator();
                    });
            };

            var filelimit = parsed.html($translate.instant("FILE_LIMIT")).html();
            var fileType = $translate.instant("FILE_TYPE");
            var fileReplaceMsg = parsed.html($translate.instant("FILE_REPLACE_MSG")).html();
            var eFiles = "";

            vm.initFilesTableDropzone = initFilesTableDropzone;
            function initFilesTableDropzone() {
                vm.fileConfig.error = false;
                vm.fileConfig.selectedFiles = 0;
                vm.fileConfig.uploadedFiles = 0;
                var dropZone = new Dropzone(document.querySelector('#checklistAttachments'), { // Make the whole body a dropzone
                    url: "api/pqm/inspectionplans/" + objectId + "/checklists/" + $scope.data.uploadChecklistId + "/attachments", // Set the url
                    thumbnailWidth: 80,
                    thumbnailHeight: 80,
                    timeout: 500000,
                    //previewTemplate: previewTemplate,
                    //autoQueue: true,// Make sure the files aren't queued until manually added
                    parallelUploads: 10,//Files are sent to the server one by one
                    previewsContainer: "#previews",
                    maxFilesize: vm.fileConfig.fileSize,
                    uploadMultiple: true
                });

                dropZone.on("success", function (file, response) {
                    $rootScope.showBusyIndicator($('#rightSidePanel'));
                    if (response.checklistImages.length > 0 || response.checklistAttachments.length > 0) {
                        eFiles = file.name;
                        vm.fileConfig.uploadedFiles = response.checklistImages.length + response.checklistAttachments.length;
                        //vm.fileConfig.fileTypeError = true;
                        //vm.fileConfig.error = true;
                    }
                });

                dropZone.on("error", function (file, response) {
                    if (response.code != undefined && response.code == "GENERAL") {
                        vm.fileConfig.message = response.message;
                        vm.fileConfig.error = true;
                    } else {
                        vm.fileConfig.fileSizeError = true;
                        vm.fileConfig.error = true;
                        vm.fileConfig.message = response;
                    }
                });

                dropZone.on("queuecomplete", function (progress) {
                    $("#total-progress").hide();
                    $("#itemFilesTableContainer").removeClass('drag-over');
                    vm.showFileDropzone = false;
                    var filesCount = dropZone.files.length;
                    dropZone.removeAllFiles(true);
                    $scope.$apply();
                    if (vm.fileConfig.error) {
                        if (vm.fileConfig.fileTypeError == true && vm.fileConfig.fileSizeError == true) {
                            $rootScope.showErrorMessage(fileType + eFiles + " and " + filelimit + vm.fileConfig.fileSize + " MB");
                        } else if (vm.fileConfig.fileTypeError == true && vm.fileConfig.fileSizeError == false) {
                            $rootScope.showErrorMessage(fileType + eFiles);
                        } else if (vm.fileConfig.fileTypeError == false && vm.fileConfig.fileSizeError == true) {
                            $rootScope.showErrorMessage(vm.fileConfig.message);
                        } else {
                            $rootScope.showErrorMessage(vm.fileConfig.message);
                        }
                    } else {
                        if (filesCount == vm.fileConfig.uploadedFiles) {
                            $rootScope.showSuccessMessage(attachmentsUploaded);
                        } else {
                            if (vm.fileConfig.uploadedFiles > 0) {
                                $rootScope.showSuccessMessage(attachmentsUploadedAndExistMsg.format(filesCount - vm.fileConfig.uploadedFiles, vm.fileConfig.uploadedFiles));
                            } else {
                                $rootScope.showWarningMessage(filesCount + " " + attachmentsExists);
                            }
                        }
                    }
                    vm.fileConfig.uploadedFiles = 0;
                    vm.fileConfig.error = false;
                    eFiles = "";
                    vm.fileConfig.fileTypeError = false;
                    vm.fileConfig.fileSizeError = false;
                    vm.checklist.attachmentCount = 0;
                    loadPlanChecklistAttachments();
                });

                $("#itemFilesTableContainer").on('dragover', handleDragEnter);
                $("#itemFilesTableContainer").on('dragleave', handleDragLeave);
                $("#itemFilesTableContainer").on('drop', handleDragLeave);
            }

            vm.selectFiles = selectFiles;
            function selectFiles() {
                $('#checklistAttachments')[0].click();
            }

            function loadChecklistAttachments() {
                MediaService.getMediaByObjectId(vm.checklistId).then(
                    function (data) {
                        vm.checklistImages = data;
                        vm.checklist.attachmentCount = vm.checklist.attachmentCount + vm.checklistImages.length;
                        vm.imageFiles = [[]];
                        var rows = Math.ceil(data.length / 5);
                        var count = -1;
                        for (var i = 0; i < rows; i++) {
                            vm.imageFiles[i] = [];

                            for (var j = 0; j < 5; j++) {
                                count++;
                                if (data[count] !== null && data[count] !== undefined) {
                                    vm.imageFiles[i][j] = data[count];
                                }
                            }
                        }
                        AttachmentService.getAttachments("ATTACHMENT", vm.checklistId).then(
                            function (data) {
                                vm.checklistAttachments = data;
                                vm.checklist.attachmentCount = vm.checklist.attachmentCount + vm.checklistAttachments.length;
                                $rootScope.hideBusyIndicator();
                            }, function (error) {
                                $rootScope.showErrorMessage(error.message);
                                $rootScope.hideBusyIndicator();
                            }
                        )
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                        $rootScope.hideBusyIndicator();
                    }
                )
            }

            function loadPlanChecklistAttachments() {
                MediaService.getMediaByObjectId(vm.planChecklistId).then(
                    function (data) {
                        vm.planChecklistImages = data;
                        vm.checklist.attachmentCount = vm.checklist.attachmentCount + vm.planChecklistImages.length;
                        vm.planImageFiles = [[]];
                        var rows = Math.ceil(vm.planChecklistImages.length / 5);
                        var count = -1;
                        for (var i = 0; i < rows; i++) {
                            vm.planImageFiles[i] = [];

                            for (var j = 0; j < 5; j++) {
                                count++;
                                if (vm.planChecklistImages[count] !== null && vm.planChecklistImages[count] !== undefined) {
                                    vm.planImageFiles[i][j] = vm.planChecklistImages[count];
                                }
                            }
                        }
                        AttachmentService.getAttachments("ATTACHMENT", vm.planChecklistId).then(
                            function (data) {
                                vm.planChecklistAttachments = data;
                                vm.checklist.attachmentCount = vm.checklist.attachmentCount + vm.planChecklistAttachments.length;

                                if ($scope.checklistMode == "ITEMINSPECTION") {
                                    loadChecklistAttachments();
                                } else {
                                    $rootScope.hideBusyIndicator();
                                }
                            }, function (error) {
                                $rootScope.showErrorMessage(error.message);
                                $rootScope.hideBusyIndicator();
                            }
                        )
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                        $rootScope.hideBusyIndicator();
                    }
                )
            }

            vm.checklistImages = [];
            vm.selectedImages = [];
            vm.selectedImage = null;
            vm.showImages = showImages;
            function showImages(image, type) {
                if (type == "PLAN") {
                    vm.selectedImages = vm.planChecklistImages;
                } else {
                    vm.selectedImages = vm.checklistImages;
                }
                vm.selectedImage = image;
                angular.forEach(vm.selectedImages, function (commentImage) {
                    if (image.id == commentImage.id) {
                        commentImage.showImage = true;
                    } else {
                        commentImage.showImage = false;
                    }
                })
                var modal = document.getElementById("attachment-image");
                modal.style.display = "block";

                $timeout(function () {
                    $("#" + vm.selectedImage.id).width($('.image-view').outerWidth());
                    $("#" + vm.selectedImage.id).height($('.image-view').outerHeight());
                }, 100)
            }

            $scope.showPreviousImage = showPreviousImage;
            function showPreviousImage(image) {
                var index = vm.selectedImages.indexOf(image);
                vm.selectedImage = null;
                if (index == 0) {
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
                }, 50)
            }

            $scope.showNextImage = showNextImage;
            function showNextImage(image) {
                var index = vm.selectedImages.indexOf(image);
                vm.selectedImage = null;
                if (index == 0 || index < (vm.selectedImages.length - 1)) {
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
                }, 50)
            }

            $scope.hideImagesView = hideImagesView;
            function hideImagesView() {
                var modal = document.getElementById("attachment-image");
                modal.style.display = "none";
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

            vm.deleteImage = deleteImage;
            function deleteImage(image) {
                $rootScope.showBusyIndicator($('#rightSidePanel'));
                MediaService.deleteMediaById(image.id).then(
                    function (data) {
                        vm.checklist.attachmentCount = 0;
                        loadPlanChecklistAttachments();
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
                        vm.checklist.attachmentCount = 0;
                        loadPlanChecklistAttachments();
                        $rootScope.showSuccessMessage(attachmentsDeletedMsg);
                        $rootScope.hideBusyIndicator();
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                        $rootScope.hideBusyIndicator();
                    }
                )
            }

            function close() {
                $scope.callback();
                $rootScope.hideSidePanel();
            }

            (function () {
                loadFileConfig();
                if ($scope.data.checklistMode == "PLAN") {
                    vm.planChecklistId = $scope.data.checklistDetails.id;
                    loadPlanChecklistAttachments();
                } else {
                    vm.checklistId = $scope.data.checklistDetails.id;
                    vm.planChecklistId = $scope.data.checklistDetails.planChecklist.id;
                    loadPlanChecklistAttachments();
                }
                $timeout(function () {
                    initFilesTableDropzone();
                    Dropzone.options.url = null;
                }, 1000);
                $rootScope.$on('app.checklists.attachments', close);
            })();

        }
    }
);