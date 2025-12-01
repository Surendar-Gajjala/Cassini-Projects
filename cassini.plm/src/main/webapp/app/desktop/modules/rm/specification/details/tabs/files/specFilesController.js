define(
    [
        'app/desktop/modules/rm/rm.module',
        'dropzone',
        'app/shared/services/core/specificationsService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/commonService'
    ],
    function (module) {
        module.controller('SpecificationFilesController', SpecificationFilesController);

        function SpecificationFilesController($scope, $rootScope, $timeout, $state, $stateParams, $cookies, $translate,
                                              CommonService, SpecificationsService, DialogService, $application) {
            var vm = this;
            var parsed = angular.element("<div></div>");

            vm.loading = true;
            vm.loading = true;
            var specId = $stateParams.specId;
            vm.specificationId = $stateParams.specId;
            vm.files = [];
            vm.showDropzone = false;

            vm.backToFiles = backToFiles;
            vm.fileSizeToString = fileSizeToString;
            vm.lockFile = lockFile;
            vm.downloadFile = downloadFile;
            vm.showFileHistory = showFileHistory;
            vm.deleteFile = deleteFile;
            vm.showFileEdit = showFileEdit;
            vm.showFileDownloadHistory = showFileDownloadHistory;

            var fileDropZone = null;
            vm.selectFile = selectFile;

            vm.addFilesTitle = parsed.html($translate.instant("DETAILS_ADD_FILES")).html();
            $scope.downloadFileTitle = parsed.html($translate.instant("DOWNLOAD_FILE")).html();
            $scope.showFileHistoryTitle = parsed.html($translate.instant("SHOW_FILE_HISTORY")).html();
            vm.startUploadTitle = parsed.html($translate.instant("START_UPLOAD")).html();
            vm.cancelUploadTitle = parsed.html($translate.instant("CANCEL_UPLOAD")).html();
            vm.cancelTitle = parsed.html($translate.instant("CANCEL")).html();
            vm.startTitle = parsed.html($translate.instant("START")).html();
            vm.dragDropMessage = parsed.html($translate.instant("DRAG_DROP_FILES")).html();
            vm.showFileEditTitle = parsed.html($translate.instant("SHOW_FILE_EDIT")).html();
            /* vm.showFileDownload = parsed.html($translate.instant("FILE_DOWNLOAD_HISTORY_TITLE")).html();*/
            var fileUpdate = parsed.html($translate.instant("FILE_UPDATE")).html();
            var update = parsed.html($translate.instant("UPDATE")).html();
            var fileSuccessMessage = parsed.html($translate.instant("FILE_SUCCESS_MESSAGE")).html();
            vm.previewFile = parsed.html($translate.instant("FILE_PREVIEW")).html();
            var folderDeleteMsg = parsed.html($translate.instant("FOLDER_DELETED_MESSAGE")).html();
            var fileCopiedToClipBoard = parsed.html($translate.instant("FILES_COPIED_TO_CLIPBOARD")).html();
            vm.ExpandCollapse = parsed.html($translate.instant("EXPAND_COLLAPSE")).html();
            var undoSuccessful = parsed.html($translate.instant("UNDO_SUCCESSFUL")).html();
            var fileIsLockedBy = parsed.html($translate.instant("FILE_IS_LOCKED_BY")).html();
            var youCannotCopyTheFile = parsed.html($translate.instant("YOU_CANNOT_COPY_THE_FILE")).html();
            var filesCopiedMessage = parsed.html($translate.instant("FILES_COPIED_MESSAGE")).html();
            var filesAlreadyExists = parsed.html($translate.instant("FILES_ALREADY_EXISTS")).html();
            var and = parsed.html($translate.instant("AND")).html();
            var fileRenameError = parsed.html($translate.instant("FILE_RENAME_ERROR")).html();

            function backToFiles() {
                vm.showDropzone = false;
            }

            vm.filesMap = new Hashtable();
            function loadFiles() {
                SpecificationsService.getRmObjectFiles(specId).then(
                    function (data) {
                        vm.files = data;
                        if (vm.files.length == 0) $rootScope.hasFiles = false;
                        else $rootScope.hasFiles = true;
                        angular.forEach(vm.files, function (obj) {
                            obj.level = 0;
                            obj.parentFolder = null;
                            obj.folderChildren = [];
                            if (obj.createdDate) {
                                obj.createdDatede = moment(obj.createdDate, "DD/MM/YYYY, HH:mm:ss").format("DD.MM.YYYY, HH:mm:ss");
                            }
                            if (obj.modifiedDate) {
                                obj.modifiedDatede = moment(obj.modifiedDate, "DD/MM/YYYY, HH:mm:ss").format("DD.MM.YYYY, HH:mm:ss");
                            }
                        });
                        vm.loading = false;
                        $rootScope.searchModeType = false;
                        $rootScope.loadSpecCounts();
                        CommonService.getPersonReferences(vm.files, 'createdBy');
                        CommonService.getPersonReferences(vm.files, 'modifiedBy');
                        CommonService.getPersonReferences(vm.files, 'lockedBy');

                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                        $rootScope.hideBusyIndicator();
                    }
                )
            }

            var fileHistoryTitle = parsed.html($translate.instant("FILE_HISTORY_TITLE")).html();
            var fileDownloadHistoryTitle = parsed.html($translate.instant("FILE_DOWNLOAD_HISTORY_TITLE")).html();

            function showFileHistory(file) {
                var options = {
                    title: fileHistoryTitle,
                    template: 'app/desktop/modules/rm/specification/details/tabs/files/specificationFileHistoryView.jsp',
                    controller: 'SpecificationFileHistoryController as fileHistoryVm',
                    resolve: 'app/desktop/modules/rm/specification/details/tabs/files/specificationFileHistoryController',
                    data: {
                        changeSpecId: specId,
                        specFile: file,
                        specFileMode: "SpecificationFileVersion"
                    },
                    callback: function (msg) {
                        console.log(msg);
                    }
                };

                $rootScope.showSidePanel(options);
            }

            function showFileDownloadHistory(file) {
                var options = {
                    title: fileDownloadHistoryTitle,
                    template: 'app/desktop/modules/rm/specification/details/tabs/files/specificationFileHistoryView.jsp',
                    controller: 'SpecificationFileHistoryController as fileHistoryVm',
                    resolve: 'app/desktop/modules/rm/specification/details/tabs/files/specificationFileHistoryController',
                    data: {
                        changeSpecId: specId,
                        specFile: file,
                        specFileMode: "FileDownloadHistory"
                    },
                    callback: function (msg) {
                        console.log(msg);
                    }
                };

                $rootScope.showSidePanel(options);
            }

            var fileUploadMessage = parsed.html($translate.instant("FILE_SUCCESS_MESSAGE")).html();

            function loadFileConfig() {
                vm.fileConfig = {};
                vm.fileConfig.fileTypeError = false;
                vm.fileConfig.fileSizeError = false;
                var context = 'APPLICATION';
                CommonService.getPreferenceByContext(context).then(
                    function (data) {
                        if (data.length != 0) {
                            vm.configs = data;
                            angular.forEach(vm.configs, function (config) {
                                if (config.preferenceKey == "APPLICATION.FILESIZE") {
                                    vm.fileConfig.fileSize = config.integerValue;
                                }
                                if (config.preferenceKey == "APPLICATION.FILETYPE") {
                                    vm.fileConfig.fileType = config.stringValue.split("\n");
                                }
                            });
                        } else {
                            vm.fileConfig.fileSize = 2000;
                        }

                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                        $rootScope.hideBusyIndicator();
                    });
            };

            var filelimit = parsed.html($translate.instant("FILE_LIMIT")).html();
            var fileType = $translate.instant("FILE_TYPE");
            var eFiles = "";

            function initFilesTableDropzone(previewTemplate) {
                vm.fileConfig.error = false;
                var myDropzone = new Dropzone(document.querySelector("#itemFiles"), { // Make the whole body a dropzone
                    url: "api/rm/specifications/" + specId + "/files", // Set the url
                    thumbnailHeight: 80,
                    thumbnailWidth: 80,
                    parallelUploads: 1,
                    timeout: 500000,
                    previewTemplate: previewTemplate,
                    autoQueue: true, // Make sure the files aren't queued until manually added
                    previewsContainer: "#previews",
                    uploadMultiple: true,
                    maxFilesize: vm.fileConfig.fileSize
                });

                // Update the total progress bar
                myDropzone.on("totaluploadprogress", function (progress) {
                    /*     document.querySelector("#total-progress .progress-bar").style.width = progress + "%";*/
                    var a = $("#total-progress .progress-bar");
                    a = progress + "%"
                });

                myDropzone.on("uploadprogress", function () {
                    $("#itemFilesTable").hide();
                })

                myDropzone.on("success", function (file, response) {
                    if (response.length == 0) {
                        eFiles += file.name + ", ";
                        vm.fileConfig.fileTypeError = true;
                        vm.fileConfig.error = true;
                    }
                });

                myDropzone.on("error", function (file, response) {
                    vm.fileConfig.fileSizeError = true;
                    vm.fileConfig.error = true;
                });

                myDropzone.on("queuecomplete", function (progress) {
                    $("#itemFilesTableContainer").removeClass('drag-over');
                    $("#total-progress").hide();
                    $("#itemFilesTable").show();
                    vm.showFileDropzone = false;
                    myDropzone.removeAllFiles(true);
                    $scope.$apply();
                    if (vm.fileConfig.error) {
                        if (vm.fileConfig.fileTypeError == true && vm.fileConfig.fileSizeError == true) {
                            $rootScope.showErrorMessage(fileType + eFiles.replace(/,\s*$/, "") + " and " + filelimit + vm.fileConfig.fileSize + " MB");
                        } else if (vm.fileConfig.fileTypeError == true && vm.fileConfig.fileSizeError == false) {
                            $rootScope.showErrorMessage(fileType + eFiles.replace(/,\s*$/, ""));
                        } else if (vm.fileConfig.fileTypeError == false && vm.fileConfig.fileSizeError == true) {
                            $rootScope.showErrorMessage(filelimit + vm.fileConfig.fileSize + " MB");
                        }
                    } else {
                        $rootScope.showSuccessMessage(fileSuccessMessage);
                    }
                    vm.fileConfig.error = false;
                    eFiles = "";
                    vm.fileConfig.fileTypeError = false;
                    vm.fileConfig.fileSizeError = false;
                    loadFiles();

                });

                $("#itemFilesTableContainer").on('dragover', handleDragEnter);
                $("#itemFilesTableContainer").on('dragleave', handleDragLeave);
                $("#itemFilesTableContainer").on('drop', handleDragLeave);

                fileDropZone = myDropzone;
            }

            function selectFile() {
                $('#itemFiles')[0].click();
            }

            var fileValidation = parsed.html($translate.instant("FILE_ALERT_MESSAGE")).html();
            var fileLockedValidation = parsed.html($translate.instant("FILE_LOCKED_MESSAGE")).html();
            var fileUpdatedMessage = $translate.instant("FILE_UPDATE_MESSAGE");

            function checkAndStartFileUpload() {
                var lockedFiles = [];
                for (var i = 0; i < fileDropZone.files.length; i++) {
                    if (isFileLocked(fileDropZone.files[i].name)) {
                        lockedFiles.push(fileDropZone.files[i].name);
                    }
                }

                if (lockedFiles.length == 0) {
                    $rootScope.showErrorMessage(fileValidation);
                    fileDropZone.enqueueFiles(fileDropZone.getFilesWithStatus(Dropzone.ADDED));
                }
                else {
                    var fileNames = "";

                    for (i = 0; i < lockedFiles.length; i++) {
                        fileNames += lockedFiles[i];

                        if (i != lockedFiles.length - 1) {
                            fileNames += ", ";
                        }
                    }

                    $rootScope.showWarningMessage(fileLockedValidation + " : " + fileNames);
                    loadFiles();
                }
            }

            function showFileEdit(file) {
                var options = {
                    title: fileUpdate,
                    template: 'app/desktop/modules/item/details/tabs/files/fileEditView.jsp',
                    controller: 'FileEditController as fileEditVm',
                    resolve: 'app/desktop/modules/item/details/tabs/files/fileEditController',
                    width: 700,
                    showMask: true,
                    data: {
                        editFile: file,
                        fileEditPermission: true
                    },
                    buttons: [
                        {text: update, broadcast: 'app.items.file.edit'}
                    ],
                    callback: function (result) {
                        file.description = result.description;
                    }
                };

                $rootScope.showSidePanel(options);

            }

            function isFileLocked(fileName) {
                var locked = false;

                angular.forEach(vm.files, function (file) {
                    if (file.name == fileName && file.locked == true) {
                        locked = true;
                    }
                });
                return locked;
            }

            function handleDragEnter(e) {
                $("#itemFilesTableContainer")[0].classList.add('drag-over');
                vm.showFileDropzone = true;

            }

            function handleDragLeave(e) {
                $("#itemFilesTableContainer")[0].classList.remove('drag-over');
                vm.showFileDropzone = false;
            }

            function fileSizeToString(bytes) {
                if (bytes == 0) {
                    return "0.00 B";
                }
                var e = Math.floor(Math.log(bytes) / Math.log(1024));
                return (bytes / Math.pow(1024, e)).toFixed(2) + ' ' + ' KMGTP'.charAt(e) + 'B';
            }

            function downloadFile(file) {
                var url = "{0}//{1}/api/rm/specifications/{2}/files/{3}/download".
                    format(window.location.protocol, window.location.host,
                    specId, file.id);
                //launchUrl(url);

                window.open(url);
                $timeout(function () {
                    window.close();
                }, 2000);
                SpecificationsService.updateFileDownloadHistory(specId, file.id).then(
                    function (data) {

                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                     }
                )
            }

            vm.filePreview = filePreview;
            function filePreview(file) {
                var fileId = file.id;
                var url = "{0}//{1}/api/rm/specifications/{2}/files/{3}/preview".
                    format(window.location.protocol, window.location.host, specId, fileId);
                var newWindow = window.open(url, "_blank");
                newWindow.addEventListener('load', function () {
                    newWindow.document.title = file.name;
                });
                $timeout(function () {
                    window.close();
                }, 2000);
            }

            function lockFile(file, lock) {
                file.locked = lock;
                if (lock) {
                    file.lockedBy = $application.login.person.id;
                    file.lockedByObject = $application.login.person;
                    file.lockedDate = moment(new Date()).format("DD/MM/YYYY, HH:mm:ss");
                }
                else {
                    file.lockedBy = null;
                    file.lockedByObject = null;
                    file.lockedDate = null;
                }
                SpecificationsService.updateSpecFile(specId, file).then(
                    function (data) {
                        file = data;
                        $rootScope.showSuccessMessage(fileUpdatedMessage);
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                        $rootScope.hideBusyIndicator();
                    }
                );
            }

            vm.renameFile = renameFile;
            function renameFile(fileRowObject) {
                vm.fileObj = fileRowObject;
                var fileId = vm.fileObj.id;
                var fileName = vm.fileObj.name;
                var period = fileName.lastIndexOf(".");
                var extension = fileName.substring(period + 1);
                swal({
                    title: $rootScope.reNameFile,
                    text: $rootScope.enterText,
                    type: "input",
                    showCancelButton: true,
                    closeOnConfirm: false,
                    cancelButtonText: $rootScope.cancelTitle,
                    inputPlaceholder: fileName
                }, function (inputValue) {
                    if (inputValue === false) return false;
                    if (inputValue === "") {
                        swal.showInputError($rootScope.reNameWarningMsg);
                        return false
                    }
                    vm.itemRename = false;
                    var newFileName = inputValue + "." + extension;
                    angular.forEach(vm.files, function (file) {
                        if (file.fileType == 'FILE' && file.parent == vm.fileObj.parent) {
                            if (file.name.toLowerCase() == newFileName.toLowerCase() && !vm.itemRename) {
                                swal.showInputError(fileRenameError);
                                vm.itemRename = true;
                            }
                        }
                    });
                    if (!vm.itemRename) {
                        SpecificationsService.updateSpecFileName(specId, fileId, newFileName).then(
                            function (data) {
                                vm.fileObj.name = newFileName;
                                swal($rootScope.done, $rootScope.reNameSuccessMsg, "success");
                                fileRowObject.id = data.id;
                                fileRowObject.name = data.name;
                                fileRowObject.version = data.version;
                                fileRowObject.modifiedDate = data.modifiedDate;
                            }, function (error) {
                                alert(JSON.stringify("Error" + error));
                            }
                        )
                    }
                });
            }

            function initFileDrop() {
                $("#itemFilesTable").on('dragover', function () {
                    console.log("Drag over");
                });
                $("#itemFilesTable").on('dragleave', function () {
                    console.log("Drag leave");
                });
                $("#itemFilesTable").on('drop', function () {
                    console.log("Drag drop");
                });

            }

            $scope.deleteFileTitle = parsed.html($translate.instant("FILE_DIALOG_TITLE")).html();
            $scope.deleteFileMessage = parsed.html($translate.instant("FILE_DIALOG_MESSAGE")).html();
            var fileDeletedMessage = parsed.html($translate.instant("SPEC_FILE_DELETED_MESSAGE")).html();
            var fileDelete = parsed.html($translate.instant("ITEMDELETE")).html();

            function deleteFile(file) {
                var options = {
                    title: $scope.deleteFileTitle,
                    message: $scope.deleteFileMessage + " [ " + file.name + " ] " + fileDelete + " ?",
                    okButtonClass: 'btn-danger'
                };
                DialogService.confirm(options, function (yes) {
                    if (yes == true) {
                        $rootScope.showBusyIndicator();
                        SpecificationsService.deleteSpecFile(specId, file.id).then(
                            function (data) {
                                var index = vm.files.indexOf(file);
                                vm.files.splice(index, 1);
                                if (file.parentFile != null) {
                                    file.parentFolder.count = file.parentFolder.count - 1;
                                    file.parentFolder.folderChildren.splice(file.parentFolder.folderChildren.indexOf(file), 1);
                                }
                                //loadFiles();
                                $rootScope.showSuccessMessage(fileDeletedMessage);
                                $rootScope.hideBusyIndicator();
                            }, function (error) {
                                $rootScope.showErrorMessage(error.message);
                                $rootScope.hideBusyIndicator();
                            }
                        )
                    }
                });
            }

            var fileReplaces = parsed.html($translate.instant("FILE_REPLACE")).html();
            vm.fileReplace = fileReplace;
            function fileReplace(file) {
                var options = {
                    title: fileReplaces,
                    template: 'app/desktop/modules/rm/specification/details/tabs/files/fileReplaceView.jsp',
                    controller: 'FileReplaceController as fileReplaceVm',
                    resolve: 'app/desktop/modules/rm/specification/details/tabs/files/fileReplaceController',
                    width: 500,
                    data: {
                        replaceFile: file,
                        files: vm.files
                    },
                    callback: function () {
                        SpecificationsService.getLatestUploadedObjectFile(specId, file.id).then(
                            function (data) {
                                file.id = data.id;
                                file.name = data.name;
                                file.version = data.version;
                                file.size = data.size;
                                file.modifiedDate = data.modifiedDate;
                                file.description = data.description;
                                $scope.$evalAsync();
                            }, function (error) {
                                $rootScope.showErrorMessage(error.message);
                                $rootScope.hideBusyIndicator();
                            }
                        )
                    }
                };

                $rootScope.showSidePanel(options);
            }

            var fileMovedToItem = parsed.html($translate.instant("FILE_MOVED_TO_SPEC")).html();
            var fileMovedTo = parsed.html($translate.instant("FILE_MOVED_TO")).html();
            var folderSuccess = parsed.html($translate.instant("FOLDER_SUCCESS")).html();

            vm.updateFile = updateFile;
            function updateFile(fileObject, folder, parent) {
                $rootScope.showBusyIndicator($('.view-container'));
                var parentFolder = fileObject.parentFolder;
                fileObject.parentFolder = null;
                SpecificationsService.moveSpecFileToFolder(specId, fileObject).then(
                    function (data) {
                        vm.files.splice(vm.files.indexOf(fileObject), 1);

                        if (folder != null) {
                            if (parent != null) {
                                parent.count = parent.count - 1;
                                parent.folderChildren.splice(parent.folderChildren.indexOf(fileObject), 1);
                            }

                            if (folder.expanded) {
                                var index = vm.files.indexOf(folder);
                                if (folder.folderChildren == undefined) {
                                    folder.folderChildren = [];
                                }

                                index = index + getIndexTopInsertNewChild(folder) + 1;
                                data.level = folder.level + 1;
                                folder.count = folder.count + 1;
                                folder.folderChildren.push(data);
                                data.parentFolder = folder;
                                vm.files.splice(index, 0, data);

                                CommonService.getPersonReferences(folder.folderChildren, 'createdBy');
                                CommonService.getPersonReferences(folder.folderChildren, 'modifiedBy');
                                CommonService.getPersonReferences(folder.folderChildren, 'lockedBy');
                                $scope.$evalAsync();
                            } else {
                                toggleNode(folder);
                            }
                            $rootScope.hideBusyIndicator();
                            $rootScope.showSuccessMessage(" [ " + data.name + " ] " + fileMovedTo + " [ " + folder.name + " ] " + folderSuccess);
                        } else {
                            if (parent != null) {
                                if (parentFolder != null) {
                                    parentFolder.count = parentFolder.count - 1;
                                    parentFolder.folderChildren.splice(parentFolder.folderChildren.indexOf(fileObject), 1);
                                }
                            }

                            CommonService.getPersonReferences(vm.files, 'createdBy');
                            CommonService.getPersonReferences(vm.files, 'modifiedBy');
                            data.level = 0;
                            vm.files.push(data);
                            $scope.$evalAsync();
                            $rootScope.hideBusyIndicator();
                            $rootScope.showSuccessMessage(" [ " + data.name + " ] " + fileMovedToItem);

                        }
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                        $rootScope.hideBusyIndicator();
                    }
                )
            }

            vm.toggleNode = toggleNode;
            function toggleNode(folder) {
                $rootScope.showBusyIndicator($('.view-content'));
                if (folder.expanded == null || folder.expanded == undefined) {
                    folder.expanded = false;
                }
                folder.expanded = !folder.expanded;
                var index = vm.files.indexOf(folder);
                if (folder.expanded == false) {
                    removeChildren(folder);
                }
                else {
                    SpecificationsService.getSpecFolderChildren(specId, folder.id).then(
                        function (data) {
                            folder.count = data.length;
                            angular.forEach(data, function (item) {
                                item.editMode = false;
                                item.expanded = false;
                                item.level = folder.level + 1;
                                item.parentFolder = folder;
                                item.folderChildren = [];
                                if (item.createdDate) {
                                    item.createdDatede = moment(item.createdDate, "DD/MM/YYYY, HH:mm:ss").format("DD.MM.YYYY, HH:mm:ss");
                                }
                                if (item.modifiedDate) {
                                    item.modifiedDatede = moment(item.modifiedDate, "DD/MM/YYYY, HH:mm:ss").format("DD.MM.YYYY, HH:mm:ss");
                                }
                                folder.folderChildren.push(item);
                            });

                            angular.forEach(folder.folderChildren, function (item) {
                                index = index + 1;
                                if (item.createdDate) {
                                    item.createdDatede = moment(item.createdDate, "DD/MM/YYYY, HH:mm:ss").format("DD.MM.YYYY, HH:mm:ss");
                                }
                                if (item.modifiedDate) {
                                    item.modifiedDatede = moment(item.modifiedDate, "DD/MM/YYYY, HH:mm:ss").format("DD.MM.YYYY, HH:mm:ss");
                                }
                                vm.files.splice(index, 0, item);
                            });
                            CommonService.getPersonReferences(folder.folderChildren, 'createdBy');
                            CommonService.getPersonReferences(folder.folderChildren, 'modifiedBy');
                            CommonService.getPersonReferences(folder.folderChildren, 'lockedBy');
                            $rootScope.hideBusyIndicator();

                        }, function (error) {
                            $rootScope.showErrorMessage(error.message);
                            $rootScope.hideBusyIndicator();
                        }
                    )
                }
            }

            function removeChildren(folder) {
                if (folder != null && folder.folderChildren != null && folder.folderChildren != undefined) {
                    angular.forEach(folder.folderChildren, function (item) {
                        removeChildren(item);
                    });

                    var index = vm.files.indexOf(folder);
                    vm.files.splice(index + 1, folder.folderChildren.length);
                    folder.folderChildren = [];
                    folder.expanded = false;

                }
                $rootScope.hideBusyIndicator();
            }

            function getIndexTopInsertNewChild(folder) {
                var index = 0;

                if (folder.folderChildren != undefined && folder.folderChildren != null) {
                    index = folder.folderChildren.length;
                    angular.forEach(folder.folderChildren, function (child) {
                        var childCount = getIndexTopInsertNewChild(child);
                        index = index + childCount;
                    })
                }

                return index;
            }

            var fileDialogMessage = parsed.html($translate.instant("FILE_DIALOG_MESSAGE")).html();
            var deleteFolderTitle = parsed.html($translate.instant("DELETE_FOLDER")).html();
            var folderTitle = parsed.html($translate.instant("FOLDER")).html();
            vm.deleteFolder = deleteFolder;
            function deleteFolder(folder) {
                var options = {
                    title: deleteFolderTitle,
                    message: fileDialogMessage + "[ " + folder.name + " ]" + folderTitle + "?",
                    okButtonClass: 'btn-danger'
                };
                DialogService.confirm(options, function (yes) {
                    if (yes == true) {
                        $rootScope.showBusyIndicator($('.view-content'));
                        SpecificationsService.deleteSpecFolder(specId, folder.id).then(
                            function (data) {
                                if (folder.folderChildren != undefined) {
                                    if (folder.expanded && folder.folderChildren.length > 0) {
                                        removeChildren(folder);
                                    }
                                }
                                var index = vm.files.indexOf(folder);
                                vm.files.splice(index, 1);

                                if (folder.parentFile != null) {
                                    folder.parentFolder.count = folder.parentFolder.count - 1;
                                    folder.parentFolder.folderChildren.splice(folder.parentFolder.folderChildren.indexOf(folder), 1);
                                }
                                //loadFiles();
                                $rootScope.loadSpecCounts();
                                $rootScope.showSuccessMessage(folderDeleteMsg);
                                $rootScope.hideBusyIndicator();
                            }, function (error) {
                                $rootScope.showErrorMessage(error.message);
                                $rootScope.hideBusyIndicator();
                            }
                        )
                    }
                })
            }

            vm.addFolder = addFolder;
            function addFolder(folder) {
                vm.parentFolderId = folder.id;
                addSpecFolder(folder);
            }

            var newFolder = parsed.html($translate.instant("NEW_FOLDER")).html();
            vm.title = parsed.html($translate.instant("ADD_FOLDER_OR_ADD_FILES")).html();
            var createButton = parsed.html($translate.instant("CREATE")).html();
            vm.parentFolderId = null;
            $rootScope.addSpecFolder = addSpecFolder;
            function addSpecFolder(folder) {
                var options = {
                    title: newFolder,
                    template: 'app/desktop/modules/item/details/tabs/files/newFolderView.jsp',
                    controller: 'NewFolderController as newFolderVm',
                    resolve: 'app/desktop/modules/item/details/tabs/files/newFolderController',
                    width: 500,
                    showMask: true,
                    data: {
                        parentFolder: vm.parentFolderId,
                        folderType: "SPEC",
                        fileObjectId: specId
                    },
                    buttons: [
                        {text: createButton, broadcast: 'app.items.folder'}
                    ],
                    callback: function (result) {
                        //loadFiles();
                        result.level = null;
                        result.folderChildren = [];
                        if (folder != null) {
                            if (folder.expanded) {
                                var index = vm.files.indexOf(folder);
                                if (folder.folderChildren == undefined) {
                                    folder.folderChildren = [];
                                }

                                var index = index + getIndexTopInsertNewChild(folder) + 1;
                                result.level = folder.level + 1;
                                result.folderChildren = [];
                                result.parentFolder = folder;
                                folder.count = folder.count + 1;
                                folder.folderChildren.push(result);
                                vm.files.splice(index, 0, result);

                                CommonService.getPersonReferences(folder.folderChildren, 'createdBy');
                                CommonService.getPersonReferences(folder.folderChildren, 'modifiedBy');
                                CommonService.getPersonReferences(folder.folderChildren, 'lockedBy');
                            } else {
                                toggleNode(folder);
                            }
                        } else {
                            vm.files.push(result);

                            CommonService.getPersonReferences(vm.files, 'createdBy');
                            CommonService.getPersonReferences(vm.files, 'modifiedBy');
                            CommonService.getPersonReferences(vm.files, 'lockedBy');
                        }

                        vm.parentFolderId = null;
                        $rootScope.hideSidePanel();
                        $rootScope.hideBusyIndicator();
                    }
                };

                $rootScope.showSidePanel(options);
            }

            vm.folderId = null;
            vm.loadFolderFile = loadFolderFile;
            function loadFolderFile(folder) {
                vm.folderId = folder.id;
                $timeout(function () {
                    loadFolderFileId(folder)
                }, 1000)
            }

            vm.validateFile = validateFile;
            function validateFile(files) {
                angular.forEach(files, function (file) {
                    var flag = true;
                    angular.forEach(vm.fileConfig.fileType, function (type) {
                        if (file.name.match(".*\\." + type + "(:|$).*") || file.name == type) {
                            eFiles += file.name;
                            vm.fileConfig.fileTypeError = true;
                            vm.fileConfig.error = true;
                            flag = false;
                        }
                    });
                    if (file.size > vm.fileConfig.fileSize * 1000000) {
                        if (!eFiles.includes(file.name)) {
                            vm.fileConfig.fileSizeError = true;
                            vm.fileConfig.error = true;
                            flag = false;
                        }
                    }
                    if (flag) {
                        vm.importFiles.push(file);
                    }
                });
                if (vm.fileConfig.error) {
                    if (vm.fileConfig.fileTypeError == true && vm.fileConfig.fileSizeError == true) {
                        $rootScope.showErrorMessage(fileType + eFiles + " and " + filelimit + vm.fileConfig.fileSize + " MB");
                    } else if (vm.fileConfig.fileTypeError == true && vm.fileConfig.fileSizeError == false) {
                        $rootScope.showErrorMessage(fileType + eFiles);
                    } else if (vm.fileConfig.fileTypeError == false && vm.fileConfig.fileSizeError == true) {
                        $rootScope.showErrorMessage(filelimit + vm.fileConfig.fileSize + " MB");
                    }
                } else {
                    $rootScope.showSuccessMessage(fileSuccessMessage);
                }
                vm.fileConfig.error = false;
                eFiles = "";
                vm.fileConfig.fileTypeError = false;
                vm.fileConfig.fileSizeError = false;
            }

            vm.loadFolderFileId = loadFolderFileId;
            function loadFolderFileId(folder) {
                document.getElementById(vm.folderId).onchange = function () {
                    var file = document.getElementById(vm.folderId);
                    vm.importFiles = [];
                    eFiles = "";
                    vm.validateFile(file.files);
                    $rootScope.showBusyIndicator($(".view-content"));
                    SpecificationsService.uploadSpecFolderFiles(specId, vm.folderId, vm.importFiles).then(
                        function (data) {

                            removeChildren(folder);
                            folder.expanded = false;
                            $timeout(function () {
                                toggleNode(folder);
                            }, 500);
                            /*if (folder.expanded) {
                             angular.forEach(data, function (file) {
                             var index = vm.files.indexOf(folder);
                             if (folder.folderChildren == undefined) {
                             folder.folderChildren = [];
                             }

                             var index = index + getIndexTopInsertNewChild(folder) + 1;
                             file.level = folder.level + 1;
                             folder.count = folder.count + 1;
                             folder.folderChildren.push(file);


                             CommonService.getPersonReferences(folder.folderChildren, 'createdBy');
                             CommonService.getPersonReferences(folder.folderChildren, 'modifiedBy');
                             CommonService.getPersonReferences(folder.folderChildren, 'lockedBy');
                             })
                             } else {
                             toggleNode(folder);
                             }*/
                            $rootScope.loadSpecCounts();
                            $rootScope.hideBusyIndicator();
                        }, function (error) {
                            $rootScope.showErrorMessage(error.message);
                            $rootScope.hideBusyIndicator();
                        }
                    )

                };
            }

            vm.selectedFiles = [];
            vm.selectClipboardFile = selectClipboardFile;
            function selectClipboardFile(file) {
                if (file.selected) {
                    if (file.locked && file.lockedByObject.id == $rootScope.loginPersonDetails.person.id) {
                        vm.selectedFiles.push(file);
                    } else if (!file.locked) {
                        vm.selectedFiles.push(file);
                    } else {
                        file.selected = false;
                        $rootScope.showWarningMessage(fileIsLockedBy + " [ " + file.lockedByObject.fullName + " ]. " + youCannotCopyTheFile)
                    }
                } else {
                    vm.selectedFiles.splice(vm.selectedFiles.indexOf(file), 1);
                }

                if (vm.selectedFiles.length > 0) {
                    $rootScope.showCopySpecFilesToClipBoard = true;
                } else {
                    $rootScope.showCopySpecFilesToClipBoard = false;
                }
            }

            $rootScope.copySpecFilesToClipBoard = copySpecFilesToClipBoard;
            function copySpecFilesToClipBoard() {
                angular.forEach(vm.selectedFiles, function (selectedItem) {
                    selectedItem.selected = false;
                    var file = angular.copy(selectedItem);
                    file.parentFolder = null;
                    $application.clipboard.files.push(file);
                });
                $rootScope.showCopySpecFilesToClipBoard = false;
                $rootScope.clipBoardSpecFiles = $application.clipboard.files;
                $rootScope.showSuccessMessage(fileCopiedToClipBoard);
                vm.selectedFiles = [];
                vm.flag = false;
            }

            $rootScope.clearAndCopySpecFilesToClipBoard = clearAndCopySpecFilesToClipBoard;
            function clearAndCopySpecFilesToClipBoard() {
                $application.clipboard.files = [];
                copySpecFilesToClipBoard();
            }

            vm.copiedData = [];
            vm.pasteFilesFromClipboard = pasteFilesFromClipboard;
            function pasteFilesFromClipboard(folder, type) {
                $rootScope.showBusyIndicator($('.view-content'));
                var fileId = 0;
                if (type == "FOLDER") {
                    fileId = folder.id;
                }
                SpecificationsService.pasteObjectFilesFromClipboard(specId, fileId, $application.clipboard.files).then(
                    function (data) {
                        vm.copiedData = data;
                        vm.selectedFolder = null;
                        if (type == "FOLDER") {
                            if (folder.expanded) {
                                removeChildren(folder);

                                $timeout(function () {
                                    toggleNode(folder);
                                }, 200)
                            } else {
                                toggleNode(folder);
                            }
                            vm.selectedFolder = folder;
                        } else {
                            angular.forEach(data, function (obj) {
                                obj.level = 0;
                                obj.expanded = false;
                                obj.folderChildren = [];
                                if (obj.createdDate) {
                                    obj.createdDatede = moment(obj.createdDate, "DD/MM/YYYY, HH:mm:ss").format("DD.MM.YYYY, HH:mm:ss");
                                }
                                if (obj.modifiedDate) {
                                    obj.modifiedDatede = moment(obj.modifiedDate, "DD/MM/YYYY, HH:mm:ss").format("DD.MM.YYYY, HH:mm:ss");
                                }
                                vm.files.push(obj);
                            });
                            vm.loading = false;
                            $rootScope.searchModeType = false;

                            CommonService.getPersonReferences(vm.files, 'createdBy');
                            CommonService.getPersonReferences(vm.files, 'modifiedBy');
                            CommonService.getPersonReferences(vm.files, 'lockedBy');
                        }
                        $rootScope.loadSpecCounts();
                        /*$rootScope.clipBoardSpecFiles = [];
                         $application.clipboard.files = [];*/
                        var clipboardFilesCount = $application.clipboard.files.length;
                        if ((clipboardFilesCount - vm.copiedData.length) > 0) {
                            if (vm.copiedData.length == 0) {
                                $rootScope.showWarningMessage(filesAlreadyExists);
                            } else {
                                $rootScope.showSuccessMessage("[ " + vm.copiedData.length + " ] " + filesCopiedMessage + " " + and + " [ " + (clipboardFilesCount - vm.copiedData.length) + " ] File(s) already exist", true, "SPECIFICATION");
                            }
                        } else {
                            $rootScope.showSuccessMessage("[ " + vm.copiedData.length + " ] " + filesCopiedMessage, true, "SPECIFICATION");
                        }
                        $rootScope.hideBusyIndicator();
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                        $rootScope.hideBusyIndicator();
                    }
                )
            }

            $rootScope.undoCopiedSpecFiles = undoCopiedSpecFiles;
            function undoCopiedSpecFiles() {
                if (vm.copiedData.length > 0) {
                    $rootScope.closeNotification();
                    $rootScope.showBusyIndicator($(".view-content"));
                    SpecificationsService.undoCopiedObjectFiles(specId, vm.copiedData).then(
                        function (data) {
                            if (vm.copiedData.length > 0) {
                                if (vm.copiedData[0].parentFile != null) {
                                    var folder = vm.selectedFolder;
                                    if (folder.expanded) {
                                        removeChildren(folder);

                                        $timeout(function () {
                                            toggleNode(folder);
                                        }, 200)
                                    } else {
                                        toggleNode(folder);
                                    }
                                } else {
                                    angular.forEach(vm.copiedData, function (file) {
                                        vm.files.splice(vm.files.indexOf(file), 1);
                                    })
                                }
                                $rootScope.loadSpecCounts();
                                $rootScope.showSuccessMessage(undoSuccessful);
                                $rootScope.hideBusyIndicator();
                            } else {
                                $rootScope.hideBusyIndicator();
                            }
                        }, function (error) {
                            $rootScope.showErrorMessage(error.message);
                            $rootScope.hideBusyIndicator();
                        }
                    )
                }
            }

            function loadSpecFilesByFileName(name) {
                SpecificationsService.getSpecFilesByName(specId, name).then(
                    function (data) {
                        vm.files = data;
                        vm.loading = false;
                        $rootScope.searchModeType = true;

                        CommonService.getPersonReferences(vm.files, 'createdBy');
                        CommonService.getPersonReferences(vm.files, 'modifiedBy');
                        CommonService.getPersonReferences(vm.files, 'lockedBy');
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                        $rootScope.hideBusyIndicator();
                    }
                )
            }

            (function () {
                $scope.$on('app.spec.tabactivated', function (event, data) {
                    if (data.tabId == 'details.files') {
                        loadFileConfig();
                        loadFiles();
                        var previewTemplate = $("#template").parent().html();
                        $("#template").remove();
                        $timeout(function () {
                            initFilesTableDropzone(previewTemplate);
                        }, 1000);

                    }
                    $scope.$on('app.spec.loadFiles', function (event, data) {
                        loadSpecFilesByFileName(data.name);
                    })

                });
            })();

        }
    }
);