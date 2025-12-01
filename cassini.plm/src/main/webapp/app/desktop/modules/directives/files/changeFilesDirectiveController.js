define(
    [
        'app/desktop/modules/main/main.module',
        'dropzone',
        'app/shared/services/core/qualityWorkflowService',
        'app/shared/services/core/objectFileService',
        'app/shared/services/core/itemService',
        'app/shared/services/core/itemFileService',
        'app/shared/services/core/documentService',
        'app/assets/bower_components/cassini-platform/app/shared/services/security/forgeService',
        'app/assets/bower_components/cassini-platform/app/desktop/directives/autodeskForge/showCADFileDirective',
        'app/desktop/directives/plugin-directive/pluginTableActionDirective'
    ],

    function (module) {
        module.directive('changeFilesView', ChangeFilesController);

        function ChangeFilesController($rootScope, $injector, $sce, $timeout, $translate, $application,
                                       ObjectFileService, ItemService, ItemFileService, ForgeService, CommonService,
                                       DialogService, DocumentService) {
            return {
                templateUrl: 'app/desktop/modules/directives/files/changeFilesView.jsp',
                restrict: 'E',
                scope: {
                    'objectType': '@',
                    'objectId': '=',
                    'hasPermission': '=',
                    'parentId': '=',
                    'subType': '@'
                },
                link: function ($scope, $elem, attrs) {
                    var parsed = angular.element("<div></div>");
                    $scope.clipboardFiles = $application.clipboard.files;
                    $scope.loading = true;
                    $scope.files = [];
                    $scope.showDropZone = false;
                    $scope.showReportDropzone = false;
                    $scope.showFileHistory = showFileHistory;
                    $scope.deleteFile = deleteFile;
                    $scope.backToFiles = backToFiles;
                    $scope.fileSizeToString = fileSizeToString;
                    $scope.lockFile = lockFile;
                    $scope.downloadFile = downloadFile;
                    $scope.showFileDownloadHistory = showFileDownloadHistory;
                    $scope.selectFiles = selectFiles;
                    $scope.searchText = null;
                    $scope.lockedOwner = false;

                    $scope.dragAndDropFilesTitle = parsed.html($translate.instant("DRAG_DROP_FILE")).html();
                    $scope.dragAndDropFilesTitle = $rootScope.dragAndDropFilesTitle.replace("&amp;", "&");
                    $scope.clickToAddFilesTitle = parsed.html($translate.instant("CLICK_TO_ADD_FILES")).html();
                    $scope.addFolderOrFiles = parsed.html($translate.instant("ADD_FOLDER_OR_ADD_FILES")).html();
                    var fileSuccessMessage = $translate.instant("REPORT_SUCCESS_MESSAGE");
                    $scope.fileLockedMessage = parsed.html($translate.instant("REPORT_LOCKED_MESSAGE")).html();
                    $scope.showFileHistoryTitle = parsed.html($translate.instant("SHOW_REPORT_HISTORY")).html();
                    var fileUpdatedMessage = $translate.instant("REPORT_UPDATE_MESSAGE");
                    var noPermission = $translate.instant("NO_PERMISSION_FOR_ACTION");
                    $scope.deleteFileTitle = parsed.html($translate.instant("REPORT_DIALOG_TITLE")).html();
                    $scope.downloadFileTitle = parsed.html($translate.instant("CLICK_TO_DOWNLOAD_ATTACHMENT")).html();
                    $scope.showFileEditTitle = parsed.html($translate.instant("SHOW_FILE_EDIT")).html();
                    $scope.fileDownloadHistory = parsed.html($translate.instant("REPORT_DOWNLOAD_HISTORY_TITLE")).html();
                    $scope.deleteFileTitle = parsed.html($translate.instant("REPORT_DIALOG_TITLE")).html();
                    var deleteFolderTitle = parsed.html($translate.instant("DELETE_FOLDER")).html();
                    var fileDialogMessage = parsed.html($translate.instant("REPORT_DIALOG_MESSAGE")).html();
                    var folderDialogMessage = parsed.html($translate.instant("FOLDER_DIALOG_MESSAGE")).html();
                    var fileDeleteMessage = parsed.html($translate.instant("REPORT_DELETE_MESSAGE")).html();
                    $scope.downloadFileTitle = parsed.html($translate.instant("DOWNLOAD_REPORT")).html();
                    var fileDelete = parsed.html($translate.instant("ITEMDELETE")).html();
                    var folderDelete = parsed.html($translate.instant("FOLDER_DELETE")).html();
                    var createButton = parsed.html($translate.instant("CREATE")).html();
                    var newFolder = parsed.html($translate.instant("NEW_FOLDER")).html();
                    var updateFolderTitle = parsed.html($translate.instant("UPDATE_FOLDER")).html();
                    var updateFolder = parsed.html($translate.instant("UPDATE_FOLDER")).html();
                    var fileMovedToItem = parsed.html($translate.instant("REPORT_MOVED_TO_QUALITY_TYPE")).html();
                    var fileMovedTo = parsed.html($translate.instant("REPORT_MOVED_TO")).html();
                    var folderSuccess = parsed.html($translate.instant("FOLDER_SUCCESS")).html();
                    $scope.title = parsed.html($translate.instant("ADD_FOLDER_OR_ADD_REPORTS")).html();
                    $scope.previewFile = parsed.html($translate.instant("REPORT_PREVIEW")).html();
                    var folderDeleteMsg = parsed.html($translate.instant("FOLDER_DELETED_MESSAGE")).html();
                    var fileCopiedToClipBoard = parsed.html($translate.instant("REPORTS_COPIED_TO_CLIPBOARD")).html();
                    var undoSuccessful = parsed.html($translate.instant("UNDO_SUCCESSFUL")).html();
                    var imageAddedAsDefaultThumbnail = parsed.html($translate.instant("IMAGE_ADDED_AS_DEFAULT_THUMBNAIL")).html();
                    var fileIsLockedBy = parsed.html($translate.instant("REPORT_IS_LOCKED_BY")).html();
                    var youCannotCopyTheFile = parsed.html($translate.instant("YOU_CANNOT_COPY_THE_FILE")).html();
                    var filesCopiedMessage = parsed.html($translate.instant("REPORTS_COPIED_MESSAGE")).html();
                    var filesAlreadyExists = parsed.html($translate.instant("REPORTS_ALREADY_EXISTS")).html();
                    var and = parsed.html($translate.instant("AND")).html();
                    $scope.ExpandCollapse = parsed.html($translate.instant("EXPAND_COLLAPSE")).html();
                    $scope.addFilesTitle = parsed.html($translate.instant("ADD_REPORTS")).html();
                    var fileRenameError = parsed.html($translate.instant("REPORT_RENAME_ERROR")).html();
                    var fileLockedUnLockedMessage = null;

                    $scope.getFileIcon = getFileIcon;
                    function getFileIcon(fileName) {
                        var fileExtensionPattern = /\.([0-9a-z]+)(?=[?#])|(\.)(?:[\w]+)$/gmi;
                        if (fileName.includes(".")) {
                            var extension = fileName.toLowerCase().match(fileExtensionPattern)[0];
                            if ([".html", ".csv", ".ttf", ".exe", ".log", ".bat", ".css", ".jsp", ".js", ".sql", ".java", ".class", ".c", ".net", ".py", ".ipynb"].indexOf(extension) > -1) {
                                return "fa fa-file-code-o";
                            }
                            else if ([".png", ".jpeg", ".jpg", ".bmp", ".eps", ".gif", ".pict", ".esd", ".tif"].indexOf(extension) > -1) {
                                return "fa fa-file-photo-o";
                            }
                            else if ([".xlsx", ".xls"].indexOf(extension) > -1) {
                                return "fa fa-file-excel-o";
                            }
                            else if ([".mp4", ".avi", ".flv", ".mpg", ".mpeg", ".mov", ".wmv"].indexOf(extension) > -1) {
                                return "fa fa-file-movie-o";
                            }
                            else if ([".mp3", ".wav", ".mid", ".mkv"].indexOf(extension) > -1) {
                                return "fa fa-file-audio-o";
                            }
                            else if ([".docx", ".doc"].indexOf(extension) > -1) {
                                return "fa fa-file-word-o";
                            }
                            else if ([".pdf"].indexOf(extension) > -1) {
                                return "fa fa-file-pdf-o";
                            }
                            else if ([".ppt"].indexOf(extension) > -1) {
                                return "fa fa-file-powerpoint-o";
                            }
                            else if ([".txt", ".rtf", ".wps", ".wpd"].indexOf(extension) > -1) {
                                return "fa fa-file-text-o";
                            }
                            else if ([".zip", ".war", ".jar", ".arc", ".arj", ".gz", ".hqx", ".sit", ".tar", ".z"].indexOf(extension) > -1) {
                                return "fa fa-file-zip-o";
                            }
                            else {
                                return "fa fa-file-o";
                            }
                        }
                    }

                    $scope.getFileIconColor = getFileIconColor;
                    function getFileIconColor(fileName) {
                        var fileExtensionPattern = /\.([0-9a-z]+)(?=[?#])|(\.)(?:[\w]+)$/gmi;
                        if (fileName.includes(".")) {
                            var extension = fileName.toLowerCase().match(fileExtensionPattern)[0];
                            if ([".html", ".csv", ".ttf", ".exe", ".log", ".bat", ".css", ".jsp", ".js", ".sql", ".java", ".class", ".c", ".net", ".py", ".ipynb"].indexOf(extension) > -1) {
                                return "{'color': '#e699ff'}";
                            }
                            else if ([".png", ".jpeg", ".jpg", ".bmp", ".eps", ".gif", ".pict", ".esd", ".tif"].indexOf(extension) > -1) {
                                return "{'color': '#9999ff'}";
                            }
                            else if ([".xlsx", ".xls"].indexOf(extension) > -1) {
                                return "{'color': '#00b377'}";
                            }
                            else if ([".mp4", ".avi", ".flv", ".mpg", ".mpeg", ".mov", ".wmv"].indexOf(extension) > -1) {
                                return "{'color': '#ff6600'}";
                            }
                            else if ([".mp3", ".wav", ".mid", ".mkv"].indexOf(extension) > -1) {
                                return "{'color': '#ff6600'}";
                            }
                            else if ([".docx", ".doc"].indexOf(extension) > -1) {
                                return "{'color': '#4d79ff'}";
                            }
                            else if ([".pdf"].indexOf(extension) > -1) {
                                return "{'color': '#ff0000'}";
                            }
                            else if ([".ppt"].indexOf(extension) > -1) {
                                return "{'color': '#ff531a'}";
                            }
                            else if ([".txt", ".rtf", ".wps", ".wpd"].indexOf(extension) > -1) {
                                return "{'color': '#5b1f07'}";
                            }
                            else if ([".zip", ".war", ".jar", ".arc", ".arj", ".gz", ".hqx", ".sit", ".tar", ".z"].indexOf(extension) > -1) {
                                return "{'color': '#ffb84d'}";
                            }
                            else {
                                return "{'color': '#ff6600'}";
                            }
                        }
                    }

                    var fileDropZone = null;

                    function showFileHistory(file) {
                        var options = {
                            title: $rootScope.fileHistoryTitle,
                            template: 'app/desktop/modules/item/details/tabs/files/fileHistoryView.jsp',
                            controller: 'FileHistoryController as fileHistoryVm',
                            resolve: 'app/desktop/modules/item/details/tabs/files/fileHistoryController',
                            showMask: true,
                            data: {
                                itemId: $scope.objectId,
                                itemFile: file,
                                itemFileMode: "FileVersion",
                                selectedFileType: $scope.objectType
                            },
                            callback: function (msg) {
                                console.log(msg);
                            }
                        };

                        $rootScope.showSidePanel(options);
                    }

                    $scope.selectFiletoRename = selectFiletoRename;
                    function selectFiletoRename(fileRowObject) {
                        $scope.fileObj = fileRowObject;
                        var fileId = $scope.fileObj.id;
                        var fileName = $scope.fileObj.name;
                        var period = fileName.lastIndexOf(".");
                        var extension = fileName.substring(period + 1);
                        swal({
                            title: $rootScope.reNameReportTitle,
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
                            $scope.itemRename = false;
                            var newFileName = inputValue + "." + extension;
                            angular.forEach($scope.files, function (file) {
                                if (file.fileType == 'FILE' && file.parent == $scope.fileObj.parent) {
                                    if (file.name.toLowerCase() == newFileName.toLowerCase() && !$scope.itemRename) {
                                        swal.showInputError(fileRenameError);
                                        $scope.itemRename = true;
                                    }
                                }
                            });
                            if (!$scope.itemRename) {
                                $rootScope.showBusyIndicator();
                                ObjectFileService.updateFileName($scope.objectId, $scope.objectType, fileId, newFileName).then(
                                    function (data) {
                                        var renamedFile = data.objectFile;
                                        swal($rootScope.done, $rootScope.reNameSuccessMsg, "success");
                                        //loadFiles();
                                        fileRowObject.id = renamedFile.id;
                                        fileRowObject.name = renamedFile.name;
                                        fileRowObject.version = renamedFile.version;
                                        fileRowObject.revision = renamedFile.revision;
                                        fileRowObject.lifeCyclePhase = renamedFile.lifeCyclePhase;
                                        fileRowObject.reviewers = renamedFile.reviewers;
                                        fileRowObject.modifiedDate = renamedFile.modifiedDate;
                                        fileRowObject.approver = false;
                                        fileRowObject.reviewer = false;
                                        angular.forEach(fileRowObject.reviewers, function (reviewer) {
                                            if (reviewer.approver && $scope.personDetails.id == reviewer.reviewer && reviewer.status == 'NONE') {
                                                fileRowObject.approver = true;
                                            } else if (!reviewer.approver && $scope.personDetails.id == reviewer.reviewer && reviewer.status == 'NONE') {
                                                fileRowObject.reviewer = true;
                                            }
                                        });
                                        updateParent(renamedFile);
                                        $rootScope.hideBusyIndicator();
                                    }, function (error) {
                                        $rootScope.showErrorMessage(error.message);
                                        $rootScope.hideBusyIndicator();
                                    }
                                )
                            }
                        });
                    }

                    function updateParent(file) {
                        if (file.parentFile != null && file.parentFile != "" && file.parentFile != undefined) {
                            ObjectFileService.getLatestUploadedFile($scope.objectId, $scope.objectType, file.parentFile).then(
                                function (data) {
                                    var fileData = data.objectFile;
                                    $timeout(function () {
                                        angular.forEach($scope.files, function (f) {
                                            if (f.id == file.parentFile) {
                                                f.modifiedDate = fileData.modifiedDate;
                                                f.reviewers = fileData.reviewers;
                                                if ($scope.objectType == "DOCUMENT" || $scope.objectType == "MFRPARTINSPECTIONREPORT" || $scope.objectType == "PPAPCHECKLIST") {
                                                    angular.forEach(f.reviewers, function (reviewer) {
                                                        if (reviewer.approver && $scope.personDetails.id == reviewer.reviewer && reviewer.status == 'NONE') {
                                                            f.approver = true;
                                                        } else if (!reviewer.approver && $scope.personDetails.id == reviewer.reviewer && reviewer.status == 'NONE') {
                                                            f.reviewer = true;
                                                        }
                                                    })
                                                }
                                            }
                                        })
                                    }, 100);
                                    $scope.$evalAsync();
                                }, function (error) {
                                    $rootScope.showErrorMessage(error.message);
                                    $rootScope.hideBusyIndicator();
                                }
                            )
                        }
                    }


                    function showFileDownloadHistory(file) {
                        var options = {
                            title: $rootScope.fileDownloadHistoryTitle,
                            template: 'app/desktop/modules/item/details/tabs/files/fileHistoryView.jsp',
                            controller: 'FileHistoryController as fileHistoryVm',
                            resolve: 'app/desktop/modules/item/details/tabs/files/fileHistoryController',
                            showMask: true,
                            data: {
                                itemId: $scope.objectId,
                                itemFile: file,
                                itemFileMode: "FileDownloadHistory",
                                selectedFileType: $scope.objectType
                            },
                            callback: function (msg) {
                                console.log(msg);
                            }
                        };

                        $rootScope.showSidePanel(options);
                    }

                    $scope.showDocuments = showDocuments;
                    function showDocuments(folder) {
                        var options = {
                            title: "Add Documents",
                            template: 'app/desktop/modules/directives/files/documentsSelectionView.jsp',
                            controller: 'DocumentsSelectionController as documentsSelectionVm',
                            resolve: 'app/desktop/modules/directives/files/documentsSelectionController',
                            showMask: true,
                            width: 750,
                            data: {
                                objectId: $scope.objectId,
                                documentType: $scope.objectType,
                                selectedObjectFolder: folder
                            },
                            buttons: [
                                {text: "Add", broadcast: 'app.object.files.documents.add'}
                            ],
                            callback: function (result) {
                                var objectDocuments = [];
                                var emptyDocument = {
                                    id: null,
                                    document: null,
                                    object: null,
                                    folder: null,
                                    documentType: $scope.objectType
                                };
                                angular.forEach(result, function (document) {
                                    var objectDocument = angular.copy(emptyDocument);
                                    if (folder != null) {
                                        objectDocument.folder = folder.id;
                                    }
                                    objectDocument.document = document;
                                    objectDocument.object = $scope.objectId;
                                    objectDocuments.push(objectDocument);
                                    if (result.length == objectDocuments.length && objectDocuments.length > 0) {
                                        DocumentService.createMultipleObjectDocuments(objectDocuments).then(
                                            function (data) {
                                                if (folder != null) {
                                                    if (folder.expanded) {
                                                        removeChildren(folder);

                                                        $timeout(function () {
                                                            toggleNode(folder);
                                                        }, 200)
                                                    } else {
                                                        toggleNode(folder);
                                                    }
                                                } else {
                                                    angular.forEach(data, function (file) {
                                                        file.level = 0;
                                                        file.expanded = false;
                                                        file.folderChildren = [];
                                                        file.parentFolder = null;
                                                        $scope.files.push(file);
                                                    })
                                                }
                                                loadObjectDetailCounts();
                                                $rootScope.showSuccessMessage("Documents added successfully");
                                                $timeout(function () {
                                                    calculateColumnWidthForSticky();
                                                }, 200);
                                                $rootScope.hideBusyIndicator();
                                            }, function (error) {
                                                $rootScope.showErrorMessage(error.message);
                                                $rootScope.hideBusyIndicator();
                                            }
                                        )
                                    }
                                })
                            }
                        };

                        $rootScope.showSidePanel(options);
                    }

                    function backToFiles() {
                        $scope.showDropZone = false;
                    }

                    $scope.filesMap = new Hashtable();
                    function loadFiles() {
                        $rootScope.showBusyIndicator();
                        $scope.loading = true;
                        $scope.searchText = null;
                        ObjectFileService.getObjectFiles($scope.objectId, $scope.objectType, false).then(
                            function (data) {
                                if ($scope.objectType == "ITEM") {
                                    $scope.files = data.objectFiles;
                                    $scope.item = $rootScope.item;
                                    $scope.itemLocked = true;
                                    if ($scope.item.lockObject) {
                                        $scope.itemLocked = true;
                                        angular.forEach($rootScope.loginPersonDetails.groups, function (grp) {
                                            if ($scope.itemLocked) {
                                                if ($scope.item.lockedBy.defaultGroup == grp.groupId) {
                                                    $scope.itemLocked = false;
                                                    $scope.lockedOwner = true;
                                                }
                                            }
                                        });
                                    } else {
                                        $scope.lockedOwner = true;
                                    }
                                    loadParentObjectFiles();
                                } else {
                                    $scope.files = data.objectFiles;
                                }
                                if (Object.keys(data.dmPermissions).length != 0 && ($scope.objectType == "DOCUMENT" || $scope.objectType == "MFRPARTINSPECTIONREPORT" || $scope.objectType == "PPAPCHECKLIST")) {
                                    $scope.dmPermissions = data.dmPermissions;
                                    loadHasPermission();
                                }
                                if ($scope.files.length == 0) $rootScope.hasFiles = false;
                                else $rootScope.hasFiles = true;

                                angular.forEach($scope.files, function (obj) {
                                    obj.level = 0;
                                    obj.expanded = false;
                                    obj.folderChildren = [];
                                    obj.parentFolder = null;
                                    if ($scope.objectType == "DOCUMENT" || $scope.objectType == "MFRPARTINSPECTIONREPORT" || $scope.objectType == "PPAPCHECKLIST") {
                                        angular.forEach(obj.reviewers, function (reviewer) {
                                            if (reviewer.approver && $scope.personDetails.id == reviewer.reviewer && reviewer.status == 'NONE') {
                                                obj.approver = true;
                                            } else if (!reviewer.approver && $scope.personDetails.id == reviewer.reviewer && reviewer.status == 'NONE') {
                                                obj.reviewer = true;
                                            }
                                        })
                                    }
                                });
                                $scope.backupFiles = angular.copy($scope.files);
                                $scope.loading = false;
                                $rootScope.searchModeType = false;
                                $timeout(function () {
                                    calculateColumnWidthForSticky();
                                }, 200);
                                $rootScope.hideBusyIndicator();
                            }, function (error) {
                                $rootScope.hideBusyIndicator();
                                $rootScope.showErrorMessage(error.message);
                            }
                        );
                    }

                    function calculateColumnWidthForSticky() {
                        var selectInputWidth = 0;
                        if ($scope.files.length > 0) {
                            selectInputWidth = 50;
                        }
                        var fileNameWidth = $('.file-name').outerWidth();

                        $('.select-input').css("left", -4 + "px");
                        $('.select-input').width(selectInputWidth);

                        $('.file-name').css("left", ((selectInputWidth) - 4) + "px");
                        $('.file-name').width(selectInputWidth + fileNameWidth);
                    }

                    function loadParentObjectFiles() {
                        var promise = null;
                        if ($scope.parentId != null && $scope.parentId != undefined) {
                            promise = ObjectFileService.getObjectFiles($scope.parentId, $scope.objectType);
                        }

                        if (promise != null) {
                            promise.then(
                                function (data) {
                                    Array.prototype.push.apply($scope.files, data.objectFiles);
                                    angular.forEach($scope.files, function (obj) {
                                        obj.level = 0;
                                        obj.expanded = false;
                                        obj.folderChildren = [];
                                        obj.parentFolder = null;
                                        if (obj.createdDate) {
                                            obj.createdDatede = moment(obj.createdDate, "DD/MM/YYYY, HH:mm:ss").format("DD.MM.YYYY, HH:mm:ss");
                                        }
                                        if (obj.modifiedDate) {
                                            obj.modifiedDatede = moment(obj.modifiedDate, "DD/MM/YYYY, HH:mm:ss").format("DD.MM.YYYY, HH:mm:ss");
                                        }
                                    });
                                    $scope.loading = false;
                                    $rootScope.searchModeType = false;
                                }, function (error) {
                                    $rootScope.showErrorMessage(error.message);
                                    $rootScope.hideBusyIndicator();
                                }
                            )
                        }
                    }

                    $scope.itemMaster = {
                        thumbnail: null
                    };

                    function loadFilesByFileName(name) {
                        $scope.searchText = name;
                        $rootScope.showBusyIndicator($('.view-container'));
                        ObjectFileService.getObjectFiles($scope.objectId, $scope.objectType, true).then(
                            function (data) {
                                $scope.files = [];
                                var filesData = data.objectFiles;
                                angular.forEach(filesData, function (file) {
                                    file.searchExist = false;
                                    if (file.name.toLowerCase().includes(name.toLowerCase())) {
                                        file.searchExist = true;
                                    }
                                    if (file.description != null && file.description != "" && file.description.toLowerCase().includes(name.toLowerCase())) {
                                        file.searchExist = true;
                                    }
                                    visitChildren(file, name);
                                });

                                angular.forEach(filesData, function (obj) {
                                    if (obj.searchExist) {
                                        obj.level = 0;
                                        obj.expanded = false;
                                        obj.folderChildren = [];
                                        obj.parentFolder = null;
                                        if ($scope.objectType == "DOCUMENT" || $scope.objectType == "MFRPARTINSPECTIONREPORT" || $scope.objectType == "PPAPCHECKLIST") {
                                            angular.forEach(obj.reviewers, function (reviewer) {
                                                if (reviewer.approver && $scope.personDetails.id == reviewer.reviewer && reviewer.status == 'NONE') {
                                                    obj.approver = true;
                                                } else if (!reviewer.approver && $scope.personDetails.id == reviewer.reviewer && reviewer.status == 'NONE') {
                                                    obj.reviewer = true;
                                                }
                                            })
                                        }
                                        $scope.files.push(obj);
                                        var index = $scope.files.indexOf(obj);
                                        index = visitFileSearchChildren(obj, index);
                                    }
                                });
                                $scope.loading = false;
                                $rootScope.searchModeType = false;
                                $timeout(function () {
                                    calculateColumnWidthForSticky();
                                }, 200);
                                $rootScope.hideBusyIndicator();
                            }, function (error) {
                                $rootScope.hideBusyIndicator();
                                $rootScope.showErrorMessage(error.message);
                            }
                        );

                    }

                    function loadHasPermission() {
                        var type = 'file';
                        if ($scope.objectType == 'DOCUMENT') {
                            type = 'document';
                            if ($scope.dmPermissions != undefined) {
                                $scope.hasCreate = $rootScope.hasPermission(type, 'create') || $scope.dmPermissions.create;
                                $scope.hasEdit = $rootScope.hasPermission(type, 'edit') || $scope.dmPermissions.edit;
                                $scope.hasDelete = $rootScope.hasPermission(type, 'delete') || $scope.dmPermissions.delete;
                                $scope.hasRename = $rootScope.hasPermission(type, 'rename') || $scope.dmPermissions.rename;
                                $scope.hasDownload = $rootScope.hasPermission(type, 'download') || $scope.dmPermissions.download;
                                $scope.hasPreview = $rootScope.hasPermission(type, 'preview') || $scope.dmPermissions.preview;
                                $scope.hasReplace = $rootScope.hasPermission(type, 'replace') || $scope.dmPermissions.replace;
                                $scope.hasPromote = $rootScope.hasPermission(type, 'promote') || $scope.dmPermissions.promote;
                                $scope.hasDemote = $rootScope.hasPermission(type, 'demote') || $scope.dmPermissions.demote;
                                $scope.hasRevise = $rootScope.hasPermission(type, 'revise') || $scope.dmPermissions.revise;
                            } else {
                                $scope.hasCreate = true;
                                $scope.hasEdit = true;
                                $scope.hasDelete = true;
                                $scope.hasRename = true;
                                $scope.hasDownload = true;
                                $scope.hasPreview = true;
                                $scope.hasReplace = true;
                            }
                        } else if ($scope.objectType == 'MFRPARTINSPECTIONREPORT') {
                            type = "mfrpartinspectionreport";
                            if ($scope.dmPermissions != undefined) {
                                $scope.hasCreate = $rootScope.hasPermission(type, 'create') || $scope.dmPermissions.create;
                                $scope.hasEdit = $rootScope.hasPermission(type, 'edit') || $scope.dmPermissions.edit;
                                $scope.hasDelete = $rootScope.hasPermission(type, 'delete') || $scope.dmPermissions.delete;
                                $scope.hasRename = $rootScope.hasPermission(type, 'rename') || $scope.dmPermissions.rename;
                                $scope.hasDownload = $rootScope.hasPermission(type, 'download') || $scope.dmPermissions.download;
                                $scope.hasPreview = $rootScope.hasPermission(type, 'preview') || $scope.dmPermissions.preview;
                                $scope.hasReplace = $rootScope.hasPermission(type, 'replace') || $scope.dmPermissions.replace;
                                $scope.hasPromote = $rootScope.hasPermission(type, 'promote') || $scope.dmPermissions.promote;
                                $scope.hasDemote = $rootScope.hasPermission(type, 'demote') || $scope.dmPermissions.demote;
                                $scope.hasRevise = $rootScope.hasPermission(type, 'revise') || $scope.dmPermissions.revise;
                                $scope.hasSignOff = $rootScope.hasPermission(type, 'signoff') || $scope.dmPermissions.signoff;
                            } else {
                                $scope.hasCreate = true;
                                $scope.hasEdit = true;
                                $scope.hasDelete = true;
                                $scope.hasRename = true;
                                $scope.hasDownload = true;
                                $scope.hasPreview = true;
                                $scope.hasReplace = true;
                                $scope.hasSignOff = true;
                            }
                        } else if ($scope.objectType == 'PPAPCHECKLIST') {
                            type = "ppapchecklist";
                            if ($scope.dmPermissions != undefined) {
                                $scope.hasCreate = $rootScope.hasPermission(type, 'create') || $scope.dmPermissions.create;
                                $scope.hasEdit = $rootScope.hasPermission(type, 'edit') || $scope.dmPermissions.edit;
                                $scope.hasDelete = $rootScope.hasPermission(type, 'delete') || $scope.dmPermissions.delete;
                                $scope.hasRename = $rootScope.hasPermission(type, 'rename') || $scope.dmPermissions.rename;
                                $scope.hasDownload = $rootScope.hasPermission(type, 'download') || $scope.dmPermissions.download;
                                $scope.hasPreview = $rootScope.hasPermission(type, 'preview') || $scope.dmPermissions.preview;
                                $scope.hasReplace = $rootScope.hasPermission(type, 'replace') || $scope.dmPermissions.replace;
                                $scope.hasPromote = $rootScope.hasPermission(type, 'promote') || $scope.dmPermissions.promote;
                                $scope.hasDemote = $rootScope.hasPermission(type, 'demote') || $scope.dmPermissions.demote;
                                $scope.hasRevise = $rootScope.hasPermission(type, 'revise') || $scope.dmPermissions.revise;
                            } else {
                                $scope.hasCreate = true;
                                $scope.hasEdit = true;
                                $scope.hasDelete = true;
                                $scope.hasRename = true;
                                $scope.hasDownload = true;
                                $scope.hasPreview = true;
                                $scope.hasReplace = true;
                                $scope.hasPromote = true;
                            }
                        } else {
                            $scope.hasCreate = $rootScope.hasPermission(type, 'create');
                            $scope.hasEdit = $rootScope.hasPermission(type, 'edit');
                            $scope.hasDelete = $rootScope.hasPermission(type, 'delete');
                            $scope.hasRename = $rootScope.hasPermission(type, 'rename');
                            $scope.hasDownload = $rootScope.hasPermission(type, 'download');
                            $scope.hasPreview = $rootScope.hasPermission(type, 'preview');
                            $scope.hasReplace = $rootScope.hasPermission(type, 'replace');
                        }
                    }

                    var fileUpdate = parsed.html($translate.instant("FILE_UPDATE")).html();
                    var fileReplaces = parsed.html($translate.instant("FILE_REPLACE")).html();
                    var update = parsed.html($translate.instant("UPDATE")).html();
                    $scope.noPermission = parsed.html($translate.instant('NO_PERMISSION_PERFORM')).html();

                    $scope.performCustomTableAction = performCustomTableAction;
                    function performCustomTableAction(action) {
                        var service = $injector.get(action.service);
                        if (service != null && service !== undefined) {
                            var method = service[action.method];
                            if (method != null && method !== undefined && typeof method === "function") {
                                if ($rootScope.pluginTableObjectRevision != null && $rootScope.pluginTableObjectRevision != undefined) method($rootScope.pluginTableObject, $rootScope.pluginTableObjectRevision);
                                else method($rootScope.pluginTableObject);
                            }
                        }
                    }


                    $scope.clearCreatedBy = clearCreatedBy;
                    function clearCreatedBy() {
                        $rootScope.showBusyIndicator($('.view-container'));
                        $scope.selectedPerson = null;
                        $scope.files = angular.copy($scope.backupFiles);
                        $rootScope.hideBusyIndicator();
                    }

                    $scope.selectedPerson = null;
                    $scope.onSelectCreatedBy = onSelectCreatedBy;
                    function onSelectCreatedBy(person) {
                        if ($scope.selectedPerson == null || $scope.selectedPerson == "") {
                            $rootScope.showBusyIndicator($('.view-container'));
                            $scope.selectedPerson = person;
                            $scope.backupFiles = angular.copy($scope.files);
                            $scope.files = [];
                            angular.forEach($scope.backupFiles, function (file) {
                                if (file.createdBy == person.id && file.parentFile == null) {
                                    $scope.files.push(file);
                                }
                            })
                            $rootScope.hideBusyIndicator();
                        } else {
                            $rootScope.showBusyIndicator($('.view-container'));
                            $scope.selectedPerson = person;
                            ObjectFileService.getObjectFiles($scope.objectId, $scope.objectType).then(
                                function (data) {
                                    $scope.backupFiles = angular.copy(data.objectFiles);
                                    $scope.files = [];
                                    angular.forEach($scope.backupFiles, function (file) {
                                        if (file.createdBy == person.id && file.parentFile == null) {
                                            $scope.files.push(file);
                                        }
                                    });
                                    $rootScope.hideBusyIndicator();
                                }
                            )
                        }
                    }

                    $scope.createdByPersons = [];
                    function loadCreatedByPersons() {
                        ObjectFileService.getCreatedByPersons($scope.objectId, $scope.objectType).then(
                            function (data) {
                                $scope.createdByPersons = data;
                            }
                        )
                    }

                    $scope.showFileEdit = showFileEdit;
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
                                mode: $scope.objectType,
                                fileEditPermission: true
                            },
                            buttons: [
                                {text: update, broadcast: 'app.items.file.edit'}
                            ],
                            callback: function (result) {
                                file.description = result.objectFile.description;
                                file.modifiedDate = result.objectFile.modifiedDate;
                            }
                        };

                        $rootScope.showSidePanel(options);

                    }

                    $scope.fileReplace = fileReplace;
                    function fileReplace(file) {
                        var options = {
                            title: fileReplaces,
                            template: 'app/desktop/modules/directives/files/objectFileReplaceView.jsp',
                            controller: 'ObjectFileReplaceController as objectFileReplaceVm',
                            resolve: 'app/desktop/modules/directives/files/objectFileReplaceController',
                            width: 500,
                            showMask: true,
                            data: {
                                replaceFile: file,
                                qualityTypeId: $scope.objectId,
                                qualityTypeObjectType: $scope.objectType,
                                files: $scope.files
                            },
                            callback: function () {
                                ObjectFileService.getLatestUploadedFile($scope.objectId, $scope.objectType, file.id).then(
                                    function (data) {
                                        var fileData = data.objectFile;
                                        if (fileData != null && fileData != undefined && fileData != "") {
                                            file.id = fileData.id;
                                            file.name = fileData.name;
                                            file.version = fileData.version;
                                            file.revision = fileData.revision;
                                            file.lifeCyclePhase = fileData.lifeCyclePhase;
                                            file.reviewers = fileData.reviewers;
                                            file.size = fileData.size;
                                            file.modifiedDate = fileData.modifiedDate;
                                            file.description = fileData.description;
                                            file.approver = false;
                                            file.reviewer = false;
                                            angular.forEach(file.reviewers, function (reviewer) {
                                                if (reviewer.approver && $scope.personDetails.id == reviewer.reviewer && reviewer.status == 'NONE') {
                                                    file.approver = true;
                                                } else if (!reviewer.approver && $scope.personDetails.id == reviewer.reviewer && reviewer.status == 'NONE') {
                                                    file.reviewer = true;
                                                }
                                            });
                                            updateParent(file);
                                            $scope.$evalAsync();
                                        }
                                    }, function (error) {
                                        $rootScope.showErrorMessage(error.message);
                                        $rootScope.hideBusyIndicator();
                                    }
                                )
                            }
                        };

                        $rootScope.showSidePanel(options);
                    }

                    $scope.parentFolderId = null;
                    $scope.addItemFolder = addItemFolder;
                    function addItemFolder(folder, type) {
                        var parentFolder;
                        var folderChildren;
                        if (folder != null) {
                            parentFolder = folder.parentFolder;
                            folderChildren = folder.folderChildren;
                            folder.parentFolder = null;
                            folder.folderChildren = [];
                        }
                        var title = "";
                        buttonName = createButton;
                        if (type == "Add") {
                            title = newFolder;
                        } else {
                            title = updateFolderTitle;
                            buttonName = update;
                        }
                        var options = {
                            title: title,
                            template: 'app/desktop/modules/item/details/tabs/files/newFolderView.jsp',
                            controller: 'NewFolderController as newFolderVm',
                            resolve: 'app/desktop/modules/item/details/tabs/files/newFolderController',
                            width: 500,
                            showMask: true,
                            data: {
                                parentFolder: $scope.parentFolderId,
                                folderType: $scope.objectType,
                                fileObjectId: $scope.objectId,
                                folderCreateType: type,
                                folderData: folder
                            },
                            buttons: [
                                {text: buttonName, broadcast: 'app.items.folder'}
                            ],
                            callback: function (data) {
                                var result = data.objectFile;
                                if (type == "Add") {
                                    result.level = null;
                                    result.folderChildren = [];
                                    updateParent(result);
                                    if (folder != null) {
                                        if (folder.expanded) {
                                            var index = $scope.files.indexOf(folder);
                                            folder.parentFolder = parentFolder;
                                            folder.folderChildren = folderChildren;
                                            if (folder.folderChildren == undefined) {
                                                folder.folderChildren = [];
                                            }

                                            var index = index + getIndexTopInsertNewChild(folder) + 1;
                                            result.level = folder.level + 1;
                                            result.folderChildren = [];
                                            result.parentFolder = folder;
                                            folder.count = folder.count + 1;
                                            folder.folderChildren.push(result);
                                            $scope.files.splice(index, 0, result);
                                            $timeout(function () {
                                                calculateColumnWidthForSticky();
                                            }, 200);
                                        } else {
                                            toggleNode(folder);
                                        }
                                    } else {
                                        $scope.files.push(result);
                                        $timeout(function () {
                                            calculateColumnWidthForSticky();
                                        }, 200);
                                    }
                                } else {
                                    folder.parentFolder = parentFolder;
                                    folder.folderChildren = folderChildren;
                                    folder.name = result.name;
                                    folder.description = result.description;
                                    folder.modifiedDate = result.modifiedDate;
                                    folder.modifiedByName = result.modifiedByName;
                                }
                                $scope.parentFolderId = null;
                                $rootScope.hideSidePanel();
                                $rootScope.hideBusyIndicator();
                            }
                        };

                        $rootScope.showSidePanel(options);
                    }

                    $scope.addFolder = addFolder;
                    function addFolder(folder, type) {
                        $scope.parentFolderId = folder.id;
                        addItemFolder(folder, type);
                    }

                    function isFileLocked(fileName) {
                        var locked = false;

                        angular.forEach($scope.files, function (file) {
                            if (file.name == fileName && file.locked == true) {
                                locked = true;
                            }
                        });
                        return locked;
                    }

                    function handleDragEnter(e) {
                        $("#changeFilesTableContainer")[0].classList.add('drag-over');
                        //$scope.showReportDropzone = true;

                    }

                    function handleDragLeave(e) {
                        $("#changeFilesTableContainer")[0].classList.remove('drag-over');
                        //$scope.showReportDropzone = false;
                    }

                    function fileSizeToString(bytes) {
                        if (bytes == 0) {
                            return "0.00 B";
                        }
                        var e = Math.floor(Math.log(bytes) / Math.log(1024));
                        return (bytes / Math.pow(1024, e)).toFixed(2) + ' ' + ' KMGTP'.charAt(e) + 'B';
                    }

                    function downloadFile(file) {

                        ObjectFileService.updateObjectFileDownloadHistory($scope.objectId, $scope.objectType, file.id).then(
                            function (data) {
                                var url = "{0}//{1}/api/plm/objects/{2}/{3}/files/{4}/download".
                                    format(window.location.protocol, window.location.host,
                                    $scope.objectId, $scope.objectType, file.id);
                                $rootScope.downloadFileFromIframe(url);
                            }, function (error) {
                                $rootScope.showErrorMessage(error.message);
                                $rootScope.hideBusyIndicator();
                            }
                        )
                    }

                    $rootScope.downloadObjectFilesAsZip = downloadObjectFilesAsZip;
                    function downloadObjectFilesAsZip() {
                        $rootScope.showBusyIndicator($('.view-container'));
                        var url = "{0}//{1}/api/plm/objects/{2}/{3}/files/zip".
                            format(window.location.protocol, window.location.host, $scope.objectId, $scope.objectType);

                        //launchUrl(url);
                        window.open(url);
                        $rootScope.hideBusyIndicator();
                    }

                    function lockFile(file, lock) {
                        file.locked = lock;
                        var folder = file.parentFolder;
                        file.parentFolder = null;
                        if (lock) {
                            file.lockedBy = $application.login.person.id;
                            file.lockedByName = $application.login.person.fullName;
                            file.lockedDate = moment(new Date()).format("DD/MM/YYYY, HH:mm:ss");
                            fileLockedUnLockedMessage = parsed.html($translate.instant("FILE_LOCKED_DIRECTIVE_MSG")).html();
                        }
                        else {
                            file.lockedBy = null;
                            file.lockedByName = null;
                            file.lockedDate = null;
                            fileLockedUnLockedMessage = parsed.html($translate.instant("FILE_UNLOCKED_DIRECTIVE_MSG")).html();
                        }
                        var qualityFileDto = {};
                        qualityFileDto.objectFile = file;
                        ObjectFileService.updateObjectFile($scope.objectId, file.id, $scope.objectType, qualityFileDto).then(
                            function (data) {
                                data.parentFolder = folder;
                                file = data.objectFile;
                                $rootScope.showSuccessMessage(fileLockedUnLockedMessage);
                            }, function (error) {
                                if (file.locked) {
                                    file.locked = false;
                                    file.lockedByName = null;
                                } else {
                                    file.lockedBy = $application.login.person.id;
                                    file.locked = true;
                                    file.lockedByName = $application.login.person.fullName;
                                }
                                $rootScope.showErrorMessage(error.message);
                                $rootScope.hideBusyIndicator();
                            }
                        );
                    }

                    var removeFileTitle = parsed.html($translate.instant("FILE_REMOVE_DIALOG_TITLE")).html();
                    var fileRemoveDialogMessage = parsed.html($translate.instant("FILE_REMOVE_DIALOG_MESSAGE")).html();
                    var fileRemoveMessage = parsed.html($translate.instant("FILE_REMOVE_MESSAGE")).html();

                    function deleteFile(file) {
                        var title;
                        var displayMessage;
                        var successMessage;
                        if (file.objectType == 'OBJECTDOCUMENT') {
                            title = removeFileTitle;
                            displayMessage = fileRemoveDialogMessage.format(file.name);
                            successMessage = fileRemoveMessage;
                        } else {
                            title = $scope.deleteFileTitle;
                            displayMessage = fileDialogMessage.format(file.name);
                            successMessage = fileDeleteMessage;
                        }
                        var options = {
                            title: title,
                            message: displayMessage,
                            okButtonClass: 'btn-danger'
                        };

                        DialogService.confirm(options, function (yes) {
                            if (yes == true) {
                                if (file.objectType != "OBJECTDOCUMENT") {
                                    $rootScope.showBusyIndicator();
                                    ObjectFileService.deleteObjectFile($scope.objectId, $scope.objectType, file.id).then(
                                        function (data) {
                                            var index = $scope.files.indexOf(file);
                                            $scope.files.splice(index, 1);
                                            if (file.parentFile != null && $scope.objectType != 'DOCUMENT') {
                                                file.parentFolder.count = file.parentFolder.count - 1;
                                                file.parentFolder.folderChildren.splice(file.parentFolder.folderChildren.indexOf(file), 1);
                                            }
                                            $scope.clipboardFiles = $application.clipboard.files;
                                            angular.forEach($scope.clipboardFiles, function (clipboardFile) {
                                                if (clipboardFile.id == file.id) {
                                                    $scope.clipboardFiles.splice($scope.clipboardFiles.indexOf(clipboardFile), 1);
                                                }
                                            })
                                            $application.clipboard.files = $scope.clipboardFiles;
                                            updateParent(file);
                                            loadObjectDetailCounts();
                                            $rootScope.showSuccessMessage(successMessage);
                                            $rootScope.hideBusyIndicator();
                                        }, function (error) {
                                            $rootScope.showErrorMessage(error.message);
                                            $rootScope.hideBusyIndicator();
                                        }
                                    )
                                } else {
                                    $rootScope.showBusyIndicator($('.view-container'));
                                    DocumentService.deleteObjectDocument(file.id).then(
                                        function (data) {
                                            var index = $scope.files.indexOf(file);
                                            $scope.files.splice(index, 1);
                                            if (file.folder != null && $scope.objectType != 'DOCUMENT') {
                                                file.parentFolder.count = file.parentFolder.count - 1;
                                                file.parentFolder.folderChildren.splice(file.parentFolder.folderChildren.indexOf(file), 1);
                                            }
                                            $scope.clipboardFiles = $application.clipboard.files;
                                            angular.forEach($scope.clipboardFiles, function (clipboardFile) {
                                                if (clipboardFile.id == file.id) {
                                                    $scope.clipboardFiles.splice($scope.clipboardFiles.indexOf(clipboardFile), 1);
                                                }
                                            })
                                            $application.clipboard.files = $scope.clipboardFiles;
                                            updateParent(file);
                                            loadObjectDetailCounts();
                                            $rootScope.showSuccessMessage(successMessage);
                                            $rootScope.hideBusyIndicator();
                                        }, function (error) {
                                            $rootScope.showErrorMessage(error.message);
                                            $rootScope.hideBusyIndicator();
                                        }
                                    )
                                }

                            }
                        });
                    }

                    var filesTabHeading = parsed.html($translate.instant("DETAILS_TAB_FILES")).html();

                    function loadObjectDetailCounts() {
                        if ($scope.objectType == "INSPECTIONPLAN") {
                            $rootScope.loadPlanDetails();
                        } else if ($scope.objectType == "INSPECTION") {
                            $rootScope.loadInspectionDetails();
                        } else if ($scope.objectType == "PROBLEMREPORT") {
                            $rootScope.loadProblemReportDetails();
                        } else if ($scope.objectType == "NCR") {
                            $rootScope.loadNcrDetails();
                        } else if ($scope.objectType == "QCR") {
                            $rootScope.loadQcrDetails();
                        } else if ($scope.objectType == "ITEM") {
                            $rootScope.loadItemDetails();
                        } else if ($scope.objectType == "PROJECT") {
                            $rootScope.loadProjectCounts();
                        } else if ($scope.objectType == "MANUFACTURER") {
                            $rootScope.loadMfrCounts();
                        } else if ($scope.objectType == "MANUFACTURERPART") {
                            $rootScope.loadMfrPartCounts();
                        } else if ($scope.objectType == "PROJECTACTIVITY") {
                            $rootScope.loadActivityCount();
                        } else if ($scope.objectType == "PROJECTTASK") {
                            $rootScope.loadTaskCount();
                        } else if ($scope.objectType == "TERMINOLOGY") {
                            $rootScope.loadGlossaryDetails();
                        } else if ($scope.objectType == "MESOBJECT") {
                            $rootScope.$broadcast("loadMesObjectFilesCount", {
                                objectId: $scope.objectId,
                                objectType: $scope.objectType,
                                heading: filesTabHeading
                            });
                        } else if ($scope.objectType == "MROOBJECT") {
                            $rootScope.$broadcast("loadMroObjectFilesCount", {
                                objectId: $scope.objectId,
                                objectType: $scope.objectType,
                                heading: filesTabHeading
                            });
                        } else if ($scope.objectType == "PGCOBJECT") {
                            $rootScope.$broadcast("loadPgcObjectFilesCount", {
                                objectId: $scope.objectId,
                                objectType: $scope.objectType,
                                heading: filesTabHeading
                            });
                        } else if ($scope.objectType == "REQUIREMENTDOCUMENT") {
                            $rootScope.loadReqDocumentTabCounts();
                        } else if ($scope.objectType == "REQUIREMENT") {
                            $rootScope.loadRequirementTabCounts();
                        }
                        else if ($scope.objectType == "MFRSUPPLIER") {
                            $rootScope.loadSupplierFileCounts();
                        } else if ($scope.objectType == "CUSTOMER") {
                            $rootScope.loadCustomerTabCounts();
                        } else if ($scope.objectType == "PLMNPR") {
                            $rootScope.loadNprTabCounts();
                        } else if ($scope.objectType == "DOCUMENT") {
                            $rootScope.$broadcast("app.documents.folder.update", {})
                        } else if ($scope.objectType == "PPAPCHECKLIST") {
                            $rootScope.loadPPAPCounts();
                        } else if ($scope.objectType == "MFRPARTINSPECTIONREPORT") {
                            loadCreatedByPersons();
                            $rootScope.loadMfrPartCounts();
                        } else if ($scope.objectType == "CHANGE") {
                            if ($scope.subType == "ECO") {
                                $rootScope.loadECOCounts();
                            } else if ($scope.subType == "ECR") {
                                $rootScope.loadEcrDetails();
                            } else if ($scope.subType == "DCO") {
                                $rootScope.loadDCOCounts();
                            } else if ($scope.subType == "DCR") {
                                $rootScope.loadDCRCounts();
                            } else if ($scope.subType == "MCO") {
                                $rootScope.loadMcoDetails();
                            } else if ($scope.subType == "VARIANCE") {
                                $rootScope.loadVarianceCounts();
                            }
                        }
                    }

                    function loadFileConfig() {
                        loadHasPermission();
                        $scope.fileConfig = {};
                        $scope.fileConfig.fileTypeError = false;
                        $scope.fileConfig.fileSizeError = false;
                        $scope.fileConfig.uploadedFiles = [];
                        var context = 'APPLICATION';
                        CommonService.getPreferenceByContext(context).then(
                            function (data) {
                                if (data.length != 0) {
                                    $scope.configs = data;
                                    angular.forEach($scope.configs, function (config) {
                                        if (config.preferenceKey == "APPLICATION.FILESIZE") {
                                            $scope.fileConfig.fileSize = config.integerValue;
                                        }
                                        if (config.preferenceKey == "APPLICATION.FILETYPE") {
                                            $scope.fileConfig.fileType = config.stringValue.split("\n");
                                        }
                                    });
                                } else {
                                    $scope.fileConfig.fileSize = 2000;
                                }
                            }, function (error) {
                                $rootScope.showErrorMessage(error.message);
                                $rootScope.hideBusyIndicator();
                            });
                    }

                    var filelimit = parsed.html($translate.instant("FILE_LIMIT")).html();
                    var fileType = $translate.instant("FILE_TYPE");
                    var eFiles = "";

                    function initFilesTableDropzone(previewTemplate) {
                        $scope.fileConfig.error = false;
                        if (Dropzone.instances.length > 0) {
                            angular.forEach(Dropzone.instances, function (instance) {
                                instance.options.url = "api/plm/objects/" + $scope.objectId + "/" + $scope.objectType + "/files/" + 0;
                            })
                        }
                        var dropZone = new Dropzone(document.querySelector('#changeObjectFiles'), { // Make the whole body a dropzone
                            url: "api/plm/objects/" + $scope.objectId + "/" + $scope.objectType + "/files/" + 0, // Set the url
                            thumbnailWidth: 80,
                            thumbnailHeight: 80,
                            timeout: 500000,
                            addRemoveLinks: true,
                            previewTemplate: previewTemplate,
                            //autoQueue: true,// Make sure the files aren't queued until manually added
                            parallelUploads: 1000000,//Files are sent to the server one by one
                            uploadMultiple: true,
                            previewsContainer: "#report-previews",
                            maxFilesize: $scope.fileConfig.fileSize
                        });

                        dropZone.on("queuecomplete", function (progress) {
                            $("#total-progress").hide();
                            $("#changeFilesTableContainer").removeClass('drag-over');
                            $scope.showReportDropzone = false;
                            dropZone.removeAllFiles(true);
                            $("#changeFilesTable").show();
                            $scope.$apply();
                            if ($scope.fileConfig.error) {
                                if ($scope.fileConfig.fileCanceled == false && $scope.fileConfig.fileTypeError == true && $scope.fileConfig.fileSizeError == true) {
                                    $rootScope.showErrorMessage(fileType + eFiles.replace(/,\s*$/, "") + " and " + filelimit + $scope.fileConfig.fileSize + " MB");
                                } else if ($scope.fileConfig.fileCanceled == false && $scope.fileConfig.fileTypeError == true && $scope.fileConfig.fileSizeError == false) {
                                    $rootScope.showErrorMessage(fileType + eFiles.replace(/,\s*$/, ""));
                                } else if ($scope.fileConfig.fileCanceled == false && $scope.fileConfig.fileTypeError == false && $scope.fileConfig.fileSizeError == true) {
                                    $rootScope.showErrorMessage(filelimit + $scope.fileConfig.fileSize + " MB");
                                } else if ($scope.fileConfig.fileCanceled == true) {
                                    $rootScope.showErrorMessage("File canceled successfully");
                                } else {
                                    $rootScope.showErrorMessage($scope.errorMessage);
                                }
                            } else {
                                $rootScope.showSuccessMessage(fileSuccessMessage);
                            }
                            $scope.fileConfig.error = false;
                            eFiles = "";
                            $scope.fileConfig.fileTypeError = false;
                            $scope.fileConfig.fileSizeError = false;
                            $scope.fileConfig.noPermission = false;
                            $scope.fileConfig.fileCanceled = false;
                            loadObjectDetailCounts();
                            if ($scope.files.length == 0) {
                                loadFiles();
                            } else {
                                updateFiles();
                            }
                        });

                        dropZone.on("uploadprogress", function () {
                            $("#changeFilesTable").hide();
                        });

                        dropZone.on("canceled", function (file) {
                            console.log("remove triggerd");
                            $scope.fileConfig.error = true;
                            $scope.fileConfig.fileCanceled = true;
                        });

                        dropZone.on("success", function (file, response) {
                            if (response.objectFiles.length == 0) {
                                eFiles += file.name + ", ";
                                $scope.fileConfig.fileTypeError = true;
                                $scope.fileConfig.error = true;
                            } else {
                                $scope.fileConfig.uploadedFiles = response;
                            }
                        });

                        dropZone.on("error", function (file, response) {
                            if (response.message != undefined && response.message != null && response.message != "") {
                                $scope.errorMessage = response.message;
                            }
                            if (response.message == undefined) {
                                $scope.fileConfig.fileSizeError = true;
                                $scope.fileConfig.fileCanceled = false;
                            }
                            $scope.fileConfig.error = true;
                        });

                        $("#changeFilesTableContainer").on('dragover', handleDragEnter);
                        $("#changeFilesTableContainer").on('dragleave', handleDragLeave);
                        $("#changeFilesTableContainer").on('drop', handleDragLeave);

                    }

                    function updateFiles() {
                        var uploadedFiles = $scope.fileConfig.uploadedFiles.objectFiles;
                        $timeout(function () {
                            angular.forEach(uploadedFiles, function (uploadedFile) {
                                var fileExist = false;
                                angular.forEach($scope.files, function (file) {
                                    if (uploadedFile.fileNo == file.fileNo) {
                                        fileExist = true;
                                        file.id = uploadedFile.id;
                                        file.size = uploadedFile.size;
                                        file.version = uploadedFile.version;
                                        file.createdDate = uploadedFile.createdDate;
                                        file.modifiedDate = uploadedFile.modifiedDate;
                                        if ($scope.objectType == "DOCUMENT" || $scope.objectType == "MFRPARTINSPECTIONREPORT" || $scope.objectType == "PPAPCHECKLIST") {
                                            file.reviewers = uploadedFile.reviewers;
                                            file.lifeCyclePhase = uploadedFile.lifeCyclePhase;
                                            file.approver = false;
                                            file.reviewer = false;
                                            angular.forEach(file.reviewers, function (reviewer) {
                                                if (reviewer.approver && $scope.personDetails.id == reviewer.reviewer && reviewer.status == 'NONE') {
                                                    file.approver = true;
                                                } else if (!reviewer.approver && $scope.personDetails.id == reviewer.reviewer && reviewer.status == 'NONE') {
                                                    file.reviewer = true;
                                                }
                                            })
                                        }
                                    }
                                });
                                if (!fileExist) {
                                    $scope.files.push(uploadedFile);
                                }
                            });
                            $timeout(function () {
                                calculateColumnWidthForSticky();
                            }, 200);
                        }, 500)
                    }

                    function selectFiles() {
                        $('#changeObjectFiles')[0].click();
                    }

                    $scope.folderId = null;
                    $scope.loadFolderFile = loadFolderFile;
                    function loadFolderFile(folder) {
                        $scope.folderId = folder.id;
                        var dropdown = $('#folder-dropdown-' + folder.id);
                        if (dropdown != null) {
                            var top = $('#folder-dropdown-' + folder.id).position().top;
                            var tableBottom = $(window).height();
                            var height = $('#folder-dropdown-' + folder.id).outerHeight();
                            if (tableBottom < (top + height)) {
                                $('#folder-dropdown-' + folder.id).css("top", (top - height - 30) + "px");
                            }
                        }
                        $timeout(function () {
                            loadFolderFileId(folder)
                        }, 1000)
                    }

                    $scope.loadFileDropdown = loadFileDropdown;
                    function loadFileDropdown(file) {
                        var dropdown = $('#file-dropdown-' + file.id);
                        if (dropdown != null) {
                            var top = $('#file-dropdown-' + file.id).position().top;
                            var tableBottom = $(window).height();
                            var height = $('#file-dropdown-' + file.id).outerHeight();
                            if (tableBottom < (top + height)) {
                                $('#file-dropdown-' + file.id).css("top", (top - height - 30) + "px");
                            }
                        }
                    }

                    $scope.validateFile = validateFile;
                    function validateFile(files) {
                        angular.forEach(files, function (file) {
                            var flag = true;
                            angular.forEach($scope.fileConfig.fileType, function (type) {
                                if (file.name.match(".*\\." + type + "(:|$).*") || file.name == type) {
                                    eFiles += file.name;
                                    $scope.fileConfig.fileTypeError = true;
                                    $scope.fileConfig.error = true;
                                    flag = false;
                                }
                            });
                            if (file.size > $scope.fileConfig.fileSize * 1000000) {
                                if (!eFiles.includes(file.name)) {
                                    $scope.fileConfig.fileSizeError = true;
                                    $scope.fileConfig.error = true;
                                    flag = false;
                                }
                            }
                            if (flag) {
                                $scope.importFiles.push(file);
                            }
                        });
                        if ($scope.fileConfig.error) {
                            if ($scope.fileConfig.fileCanceled == false && $scope.fileConfig.fileTypeError == true && $scope.fileConfig.fileSizeError == true) {
                                $rootScope.showErrorMessage(fileType + eFiles + " and " + filelimit + $scope.fileConfig.fileSize + " MB");
                            } else if ($scope.fileConfig.fileCanceled == false && $scope.fileConfig.fileTypeError == true && $scope.fileConfig.fileSizeError == false) {
                                $rootScope.showErrorMessage(fileType + eFiles);
                            } else if ($scope.fileConfig.fileCanceled == false && $scope.fileConfig.fileTypeError == false && $scope.fileConfig.fileSizeError == true) {
                                $rootScope.showErrorMessage(filelimit + $scope.fileConfig.fileSize + " MB");
                            } else if ($scope.fileConfig.fileCanceled == true) {
                                $rootScope.showErrorMessage("File canceled successfully");
                            }
                        } else {
                            $rootScope.showSuccessMessage(fileSuccessMessage);
                        }
                        $scope.fileConfig.error = false;
                        eFiles = "";
                        $scope.fileConfig.fileTypeError = false;
                        $scope.fileConfig.fileSizeError = false;
                        $scope.fileConfig.noPermission = false;
                    }

                    $scope.loadFolderFileId = loadFolderFileId;
                    function loadFolderFileId(folder) {
                        document.getElementById($scope.folderId).onchange = function () {
                            var file = document.getElementById($scope.folderId);
                            $scope.importFiles = [];
                            eFiles = "";
                            $scope.validateFile(file.files);
                            $rootScope.showBusyIndicator($(".view-content"));
                            ObjectFileService.uploadObjectFiles($scope.objectId, $scope.objectType, $scope.folderId, $scope.importFiles).then(
                                function (data) {
                                    document.getElementById($scope.folderId).value = [];
                                    updateModifiedDate(folder);
                                    removeChildren(folder);
                                    folder.expanded = false;
                                    $timeout(function () {
                                        toggleNode(folder);
                                    }, 500);
                                    loadObjectDetailCounts();
                                }, function (error) {
                                    $rootScope.showErrorMessage(error.message);
                                }, function (error) {
                                    $rootScope.showErrorMessage(error.message);
                                    $rootScope.hideBusyIndicator();
                                }
                            )

                        };
                    }

                    function updateParentFileData(folder) {
                        ObjectFileService.getObjectFile($scope.objectId, $scope.objectType, folder.id).then(
                            function (data) {
                                folder.lifeCyclePhase = data.lifeCyclePhase;
                                folder.modifiedDate = data.modifiedDate;
                                folder.modifiedByName = data.modifiedByName;
                                if (folder.parentFile != null) {
                                    updateParentFileData(folder.parentFolder);
                                }
                            }
                        )
                    }

                    function updateModifiedDate(file) {
                        ObjectFileService.getLatestUploadedFile($scope.objectId, $scope.objectType, file.id).then(
                            function (data) {
                                var fileData = data.objectFile;
                                if (fileData != null && fileData != undefined && fileData != "") {
                                    file.modifiedDate = fileData.modifiedDate;
                                    file.reviewers = fileData.reviewers;
                                    angular.forEach(file.reviewers, function (reviewer) {
                                        if (reviewer.approver && $scope.personDetails.id == reviewer.reviewer && reviewer.status == 'NONE') {
                                            file.approver = true;
                                        } else if (!reviewer.approver && $scope.personDetails.id == reviewer.reviewer && reviewer.status == 'NONE') {
                                            file.reviewer = true;
                                        }
                                    })
                                    $scope.$evalAsync();
                                }
                            }, function (error) {
                                $rootScope.showErrorMessage(error.message);
                                $rootScope.hideBusyIndicator();
                            }
                        )
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

                    $scope.filePreview = filePreview;
                    function filePreview(file) {
                        var itemId = $scope.objectId;
                        var fileId = file.id;
                        var url = "{0}//{1}/api/plm/objects/{2}/{3}/files/{4}/preview".
                            format(window.location.protocol, window.location.host, itemId, $scope.objectType, fileId);
                        var newWindow = window.open(url, "_blank");
                        newWindow.addEventListener('load', function () {
                            newWindow.document.title = file.name;
                        });
                        $timeout(function () {
                            window.close();
                        }, 2000);
                    }

                    $scope.toggleNode = toggleNode;
                    function toggleNode(folder) {
                        $rootScope.showBusyIndicator($('.view-content'));
                        if (folder.expanded == null || folder.expanded == undefined) {
                            folder.expanded = false;
                        }
                        folder.expanded = !folder.expanded;
                        var index = $scope.files.indexOf(folder);
                        if (folder.expanded == false) {
                            removeChildren(folder);
                        }
                        else {
                            var objectId = $scope.objectId;
                            if ($scope.objectType == "ITEM" && $scope.parentId != null && $scope.parentId != undefined) {
                                objectId = $scope.parentId;
                            }
                            var hierachy = false;
                            if ($scope.searchText != null && $scope.searchText != "") {
                                hierachy = true;
                            }
                            ObjectFileService.getFolderChildren(objectId, $scope.objectType, folder.id, hierachy).then(
                                function (data) {
                                    if ($scope.searchText != null && $scope.searchText != "") {
                                        var filesData = data.objectFiles;
                                        angular.forEach(filesData, function (file) {
                                            file.searchExist = false;
                                            if (file.name.toLowerCase().includes($scope.searchText.toLowerCase())) {
                                                file.searchExist = true;
                                            }
                                            if (file.description != null && file.description != "" && file.description.toLowerCase().includes($scope.searchText.toLowerCase())) {
                                                file.searchExist = true;
                                            }
                                            visitChildren(file, $scope.searchText);
                                        });

                                        angular.forEach(filesData, function (item) {
                                            if (item.searchExist) {
                                                item.editMode = false;
                                                item.expanded = false;
                                                item.level = folder.level + 1;
                                                item.parentFolder = folder;
                                                item.folderChildren = [];
                                                if (item.fileType == "FILE") {
                                                    folder.childFileCount++;
                                                }
                                                folder.folderChildren.push(item);
                                                if ($scope.objectType == "DOCUMENT" || $scope.objectType == "MFRPARTINSPECTIONREPORT" || $scope.objectType == "PPAPCHECKLIST") {
                                                    angular.forEach(item.reviewers, function (reviewer) {
                                                        if (reviewer.approver && $scope.personDetails.id == reviewer.reviewer && reviewer.status == "NONE") {
                                                            item.approver = true;
                                                        } else if (!reviewer.approver && $scope.personDetails.id == reviewer.reviewer && reviewer.status == "NONE") {
                                                            item.reviewer = true;
                                                        }
                                                    })
                                                }

                                                $scope.files.splice(index + 1, 0, item);
                                                index = $scope.files.indexOf(item);
                                                index = visitFileSearchChildren(item, index);
                                            }
                                        });
                                        $scope.$evalAsync();
                                        $timeout(function () {
                                            calculateColumnWidthForSticky();
                                            $rootScope.hideBusyIndicator();
                                        }, 500);
                                    } else {
                                        var childrenData = data.objectFiles;
                                        folder.count = childrenData.length;
                                        folder.childFileCount = 0;

                                        angular.forEach(childrenData, function (item) {
                                            item.editMode = false;
                                            item.expanded = false;
                                            item.level = folder.level + 1;
                                            item.parentFolder = folder;
                                            item.folderChildren = [];
                                            if (item.fileType == "FILE") {
                                                folder.childFileCount++;
                                            }
                                            folder.folderChildren.push(item);
                                            if ($scope.objectType == "DOCUMENT" || $scope.objectType == "MFRPARTINSPECTIONREPORT" || $scope.objectType == "PPAPCHECKLIST") {
                                                angular.forEach(item.reviewers, function (reviewer) {
                                                    if (reviewer.approver && $scope.personDetails.id == reviewer.reviewer && reviewer.status == "NONE") {
                                                        item.approver = true;
                                                    } else if (!reviewer.approver && $scope.personDetails.id == reviewer.reviewer && reviewer.status == "NONE") {
                                                        item.reviewer = true;
                                                    }
                                                })
                                            }
                                        });

                                        angular.forEach(folder.folderChildren, function (item) {
                                            index = index + 1;
                                            if (folder.selected) {
                                                item.selected = true;
                                                selectedFilesMap.put(item.id, item);
                                                $scope.selectedFiles.push(item);
                                            }
                                            $scope.files.splice(index, 0, item);
                                        });
                                        $scope.$evalAsync();
                                        $timeout(function () {
                                            calculateColumnWidthForSticky();
                                            $rootScope.hideBusyIndicator();
                                        }, 500);
                                    }


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

                            var index = $scope.files.indexOf(folder);
                            $scope.files.splice(index + 1, folder.folderChildren.length);
                            folder.folderChildren = [];
                            folder.expanded = false;
                        }

                        $scope.$evalAsync();
                        $rootScope.hideBusyIndicator();
                    }

                    var folderTitle = parsed.html($translate.instant("FOLDER")).html();
                    $scope.deleteFolder = deleteFolder;
                    function deleteFolder(folder) {
                        var options = {
                            title: deleteFolderTitle,
                            message: folderDialogMessage.format(folder.name),
                            okButtonClass: 'btn-danger'
                        };
                        DialogService.confirm(options, function (yes) {
                            if (yes == true) {
                                $rootScope.showBusyIndicator($('.view-content'));
                                ObjectFileService.deleteObjectFolder($scope.objectId, $scope.objectType, folder.id).then(
                                    function (data) {
                                        updateParent(folder);
                                        if (folder.folderChildren != undefined) {
                                            if (folder.expanded && folder.folderChildren.length > 0) {
                                                removeChildren(folder);
                                            }
                                        }
                                        var index = $scope.files.indexOf(folder);
                                        $scope.files.splice(index, 1);


                                        if (folder.parentFile != null) {
                                            folder.parentFolder.count = folder.parentFolder.count - 1;
                                            folder.parentFolder.folderChildren.splice(folder.parentFolder.folderChildren.indexOf(folder), 1);
                                        }
                                        loadObjectDetailCounts();
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

                    $scope.updateFile = updateFile;
                    function updateFile(fileObject, folder, parent) {
                        var qualityFileDto = {};
                        if (fileObject != undefined) {
                            $rootScope.showBusyIndicator($('.view-container'));
                            var parentFolder = fileObject.parentFolder;
                            fileObject.parentFolder = null;
                        }
                        qualityFileDto.objectFile = fileObject;
                        ObjectFileService.moveObjectFileToFolder($scope.objectId, $scope.objectType, fileObject.id, qualityFileDto).then(
                            function (data) {
                                $scope.files.splice($scope.files.indexOf(fileObject), 1);
                                var fileData = data.objectFile;
                                if (folder != null) {
                                    if (parent != null) {
                                        parent.count = parent.count - 1;
                                        parent.folderChildren.splice(parent.folderChildren.indexOf(fileObject), 1);
                                    }
                                    if (folder.expanded) {
                                        var index = $scope.files.indexOf(folder);
                                        if (folder.folderChildren == undefined) {
                                            folder.folderChildren = [];
                                        }
                                        index = index + getIndexTopInsertNewChild(folder) + 1;
                                        fileData.level = folder.level + 1;
                                        folder.count = folder.count + 1;
                                        folder.folderChildren.push(fileData);
                                        fileData.parentFolder = folder;
                                        $scope.files.splice(index, 0, fileData);
                                        $timeout(function () {
                                            calculateColumnWidthForSticky();
                                        }, 200);
                                        $scope.$evalAsync();
                                    } else {
                                        toggleNode(folder);
                                    }
                                    $rootScope.hideBusyIndicator();
                                    $rootScope.showSuccessMessage(" [ " + fileData.name + " ] " + fileMovedTo + " [ " + folder.name + " ] " + folderSuccess);
                                } else {
                                    if (parent != null) {
                                        if (parentFolder != null) {
                                            parentFolder.count = parentFolder.count - 1;
                                            parentFolder.folderChildren.splice(parentFolder.folderChildren.indexOf(fileObject), 1);
                                        }
                                    }
                                    fileData.level = 0;
                                    $scope.files.push(fileData);
                                    $scope.$evalAsync();
                                    $rootScope.hideBusyIndicator();
                                    var qualityTypeName = "";
                                    if ($scope.objectType == "INSPECTIONPLAN") {
                                        qualityTypeName = "inspection plan";
                                    } else if ($scope.objectType == "INSPECTION") {
                                        qualityTypeName = "inspection"
                                    } else if ($scope.objectType == "PROBLEMREPORT") {
                                        qualityTypeName = "problem report"
                                    } else if ($scope.objectType == "NCR") {
                                        qualityTypeName = "NCR"
                                    } else if ($scope.objectType == "QCR") {
                                        qualityTypeName = "QCR"
                                    } else if ($scope.objectType == "ITEM") {
                                        qualityTypeName = "ITEM";
                                    } else if ($scope.objectType == "PROJECT") {
                                        qualityTypeName = "PROJECT";
                                    } else if ($scope.objectType == "MANUFACTURER") {
                                        qualityTypeName = "MANUFACTURER";
                                    } else if ($scope.objectType == "MANUFACTURERPART") {
                                        qualityTypeName = "MANUFACTURERPART";
                                    } else if ($scope.objectType == "PROJECTACTIVITY") {
                                        qualityTypeName = "PROJECTACTIVITY";
                                    } else if ($scope.objectType == "PROJECTTASK") {
                                        qualityTypeName = "PROJECTTASK";
                                    } else if ($scope.objectType == "TERMINOLOGY") {
                                        qualityTypeName = "TERMINOLOGY";
                                    } else if ($scope.objectType == "MESOBJECT") {
                                        qualityTypeName = "MESOBJECT";
                                    } else if ($scope.objectType == "MROOBJECT") {
                                        qualityTypeName = "MROOBJECT";
                                    } else if ($scope.objectType == "PGCOBJECT") {
                                        qualityTypeName = "PGCOBJECT";
                                    } else if ($scope.objectType == "REQUIREMENTDOCUMENT") {
                                        qualityTypeName = "REQUIREMENTDOCUMENT";
                                    } else if ($scope.objectType == "REQUIREMENT") {
                                        qualityTypeName = "REQUIREMENT";
                                    } else if ($scope.objectType == "MFRSUPPLIER") {
                                        qualityTypeName = "MFRSUPPLIER";
                                    } else if ($scope.objectType == "CUSTOMER") {
                                        qualityTypeName = "CUSTOMER";
                                    } else if ($scope.objectType == "PLMNPR") {
                                        qualityTypeName = "PLMNPR";
                                    } else if ($scope.objectType == "DOCUMENT") {
                                        qualityTypeName = "DOCUMENT";
                                    } else if ($scope.objectType == "MFRPARTINSPECTIONREPORT") {
                                        qualityTypeName = "MFRPARTINSPECTIONREPORT";
                                    } else if ($scope.objectType == "PPAPCHECKLIST") {
                                        qualityTypeName = "PPAPCHECKLIST";
                                    }
                                    $rootScope.showSuccessMessage(" [ " + fileData.name + " ] " + fileMovedToItem.format(qualityTypeName));
                                    $timeout(function () {
                                        calculateColumnWidthForSticky();
                                    }, 200);
                                }
                            }, function (error) {
                                $rootScope.showErrorMessage(error.message);
                                $rootScope.hideBusyIndicator();
                                $scope.$evalAsync();
                            }
                        )
                    }

                    $scope.selectedFiles = [];
                    $scope.selectFile = selectFile;
                    function selectFile(file) {
                        if (file.selected) {
                            if (file.locked && file.lockedBy == $scope.personDetails.id) {
                                $scope.selectedFiles.push(file);
                            } else if (!file.locked) {
                                $scope.selectedFiles.push(file);
                            } else {
                                file.selected = false;
                                $rootScope.showWarningMessage(fileIsLockedBy + " [ " + file.lockedByName + " ]." + youCannotCopyTheFile)
                            }
                        } else {
                            $scope.selectedFiles.splice($scope.selectedFiles.indexOf(file), 1);
                        }

                        if ($scope.selectedFiles.length > 0) {
                            $rootScope.showCopyObjectFilesToClipBoard = true;
                        } else {
                            $rootScope.showCopyObjectFilesToClipBoard = false;
                        }
                    }

                    $rootScope.copyReportFilesToClipBoard = copyReportFilesToClipBoard;
                    function copyReportFilesToClipBoard() {
                        var fileMap = new Hashtable();
                        angular.forEach($scope.clipboardFiles, function (file) {
                            fileMap.put(file.id, file);
                        });
                        angular.forEach($scope.selectedFiles, function (selectedItem) {
                            selectedItem.selected = false;
                            var file = angular.copy(selectedItem);
                            file.parentFolder = null;
                            if (fileMap.get(selectedItem.id) == null) {
                                $application.clipboard.files.push(file);
                            }
                        });
                        $rootScope.showCopyObjectFilesToClipBoard = false;
                        $scope.clipboardFiles = $application.clipboard.files;
                        $scope.clipBoardObjectFiles = $application.clipboard.files;
                        $rootScope.showSuccessMessage(fileCopiedToClipBoard);
                        $scope.selectedFiles = [];
                        $scope.flag = false;
                    }

                    $rootScope.clearAndCopyReportFilesToClipBoard = clearAndCopyReportFilesToClipBoard;
                    function clearAndCopyReportFilesToClipBoard() {
                        $application.clipboard.files = [];
                        $scope.clipboardFiles = [];
                        copyReportFilesToClipBoard();
                    }

                    $scope.copiedData = [];
                    $scope.pasteFilesFromClipboard = pasteFilesFromClipboard;
                    function pasteFilesFromClipboard(folder, type) {
                        $rootScope.notification.undoType = "OBJECT";
                        $rootScope.showBusyIndicator($('.view-content'));
                        var fileId = 0;
                        if (type == "FOLDER") {
                            fileId = folder.id;
                        }
                        ObjectFileService.pasteObjectFilesFromClipboard($scope.objectId, $scope.objectType, fileId, $application.clipboard.files).then(
                            function (data) {
                                $scope.copiedData = data.objectFiles;
                                $scope.selectedFolder = null;
                                if (type == "FOLDER") {
                                    if (folder.expanded) {
                                        removeChildren(folder);

                                        $timeout(function () {
                                            toggleNode(folder);
                                        }, 200)
                                    } else {
                                        toggleNode(folder);
                                    }
                                    $scope.selectedFolder = folder;
                                } else {
                                    angular.forEach($scope.copiedData, function (obj) {
                                        obj.level = 0;
                                        obj.expanded = false;
                                        obj.folderChildren = [];
                                        if (obj.createdDate) {
                                            obj.createdDatede = moment(obj.createdDate, "DD/MM/YYYY, HH:mm:ss").format("DD.MM.YYYY, HH:mm:ss");
                                        }
                                        if (obj.modifiedDate) {
                                            obj.modifiedDatede = moment(obj.modifiedDate, "DD/MM/YYYY, HH:mm:ss").format("DD.MM.YYYY, HH:mm:ss");
                                        }
                                        $scope.files.push(obj);
                                    });
                                    $scope.loading = false;
                                    $rootScope.searchModeType = false;
                                }
                                //$scope.clipBoardObjectFiles = [];
                                loadObjectDetailCounts();
                                var clipboardFilesCount = $application.clipboard.files.length;
                                if ((clipboardFilesCount - $scope.copiedData.length) > 0) {
                                    if ($scope.copiedData.length == 0) {
                                        $rootScope.showWarningMessage(filesAlreadyExists);
                                    } else {
                                        $rootScope.showSuccessMessage("[ " + $scope.copiedData.length + " ] " + filesCopiedMessage + " " + and + " [ " + (clipboardFilesCount - $scope.copiedData.length) + " ] file(s) already exist", true, "OBJECT");
                                    }
                                } else {
                                    $rootScope.showSuccessMessage("[ " + $scope.copiedData.length + " ] " + filesCopiedMessage, true, "OBJECT");
                                }
                                $rootScope.hideBusyIndicator();
                            }, function (error) {
                                $rootScope.showErrorMessage(error.message);
                                $rootScope.hideBusyIndicator();
                            }
                        )
                    }

                    $rootScope.undoCopiedObjectFiles = undoCopiedObjectFiles;
                    function undoCopiedObjectFiles() {
                        if ($scope.copiedData.length > 0) {
                            $rootScope.closeNotification();
                            $rootScope.showBusyIndicator($(".view-content"));
                            var qualityFileDto = {};
                            qualityFileDto.objectFiles = $scope.copiedData;
                            ObjectFileService.undoCopiedObjectFiles($scope.objectId, $scope.objectType, qualityFileDto).then(
                                function (data) {
                                    if ($scope.copiedData.length > 0) {
                                        if ($scope.copiedData[0].parentFile != null) {
                                            var folder = $scope.selectedFolder;
                                            if (folder.expanded) {
                                                removeChildren(folder);

                                                $timeout(function () {
                                                    toggleNode(folder);
                                                }, 200)
                                            } else {
                                                toggleNode(folder);
                                            }
                                        } else {
                                            angular.forEach($scope.copiedData, function (file) {
                                                $scope.files.splice($scope.files.indexOf(file), 1);
                                            })
                                        }

                                        $rootScope.showSuccessMessage(undoSuccessful);
                                        $rootScope.hideBusyIndicator();
                                    } else {
                                        $rootScope.hideBusyIndicator();
                                    }
                                    loadObjectDetailCounts();
                                }, function (error) {
                                    $rootScope.showErrorMessage(error.message);
                                    $rootScope.hideBusyIndicator();
                                }
                            )
                        }
                    }

                    function setFileAsItemImage(file) {
                        $rootScope.showBusyIndicator($('.view-content'));
                        ItemService.setFileAsItemImage($scope.objectId, file.id).then(
                            function (data) {
                                $rootScope.showSuccessMessage(imageAddedAsDefaultThumbnail);
                                $rootScope.hideBusyIndicator();
                            }, function (error) {
                                $rootScope.showErrorMessage(error.message);
                                $rootScope.hideBusyIndicator();
                            }
                        )
                    }

                    $scope.registerCallBack = function (callback) {
                        $scope.conCallBack = callback;
                    };

                    $scope.showAutoDeskFile = function (file) {
                        var fileCopy = angular.copy(file);
                        fileCopy.parentFolder = null;
                        if (file.urn == "" || file.urn == null || file.urn == undefined) {
                            $rootScope.showBusyIndicator($('.view-container'));
                            ItemFileService.convertForgeCADFile($scope.objectId, fileCopy).then(
                                function (data) {
                                    file.urn = data.urn;
                                    file.thumbnail = data.thumbnail;
                                    $rootScope.hideBusyIndicator();
                                    if (file.urn != null) {
                                        showConvertedForgeFile(file);
                                    }
                                }, function (error) {
                                    $rootScope.hideBusyIndicator();
                                }
                            )
                        } else {
                            showConvertedForgeFile(file);
                        }
                    };

                    $scope.checkFileExt = checkFileExt;

                    function checkFileExt(file) {
                        /*if ($rootScope.AppForgeEnable == undefined) {
                         $rootScope.AppForgeEnable = false;
                         }
                         return (/!*ForgeService.verifyIsCADFile(file) && *!/$rootScope.AppForgeEnable == "true" && file.fileType == 'FILE');*/

                        return ($rootScope.AppForgeEnable && file != undefined && ForgeService.verifyIsCADFile(file) && file.fileType == 'FILE');
                    }

                    function showConvertedForgeFile(file) {
                        var modal = document.getElementById('forgeModel');
                        modal.style.display = "block";
                        if ($application.forgeToken == null || $application.forgeToken == undefined || $application.forgeToken == "") {
                            ForgeService.getForgeAuthentication().then(
                                function (data) {
                                    $application.forgeToken = data;
                                    showCADFile(file, modal)
                                })
                        } else {
                            showCADFile(file, modal)
                        }

                    }

                    function showCADFile(file, modal) {
                        $scope.fileUrl = "app/assets/bower_components/cassini-platform/app/desktop/directives/autodeskForge/forgeView.html?url=" + $application.forgeToken + "&urn=" + file.urn;
                        var span = document.getElementsByClassName("closeImage1")[0];
                        span.onclick = function () {
                            modal.style.display = "none";
                        };
                        $('#forgeFrame').attr('src', $scope.fileUrl);
                    }

                    $scope.updateToLatestDocument = updateToLatestDocument;
                    function updateToLatestDocument(file) {
                        $rootScope.showBusyIndicator($('.view-container'));
                        DocumentService.updateObjectDocumentToLatest(file.id).then(
                            function (data) {
                                file.id = data.id;
                                file.name = data.name;
                                file.description = data.description;
                                file.version = data.version;
                                file.revision = data.revision;
                                file.lifeCyclePhase = data.lifeCyclePhase;
                                file.latest = data.latest;
                                file.modifiedByName = data.modifiedByName;
                                file.modifiedDate = data.modifiedDate;
                                $scope.$evalAsync();
                                $rootScope.showSuccessMessage("File updated to latest successfully");
                                $rootScope.hideBusyIndicator();
                            }, function (error) {
                                $rootScope.showErrorMessage(error.message);
                                $rootScope.hideBusyIndicator();
                            }
                        )
                    }

                    var confirmation = parsed.html($translate.instant("CONFIRMATION")).html();
                    var nextRevDialog = parsed.html($translate.instant("DO_YOU_WANT_TO_REVISE_ITEM")).html();
                    var nextRevMsg = parsed.html($translate.instant("DOCUMENT_REVISED_MSG")).html();
                    var promoteDialogMessage = parsed.html($translate.instant("DOC_PROMOTE_DIALOG_MSG")).html();
                    var promoteDialogMessageIR = parsed.html($translate.instant("DOC_PROMOTE_DIALOG_MSG_IR")).html();
                    var demoteDialogMessage = parsed.html($translate.instant("DOC_DEMOTE_DIALOG_MSG")).html();
                    var demoteDialogMessageIR = parsed.html($translate.instant("DOC_DEMOTE_DIALOG_MSG_IR")).html();
                    var docPromotedMsg = parsed.html($translate.instant("DOC_PROMOTED_MSG")).html();
                    var docPromotedMsgIR = parsed.html($translate.instant("DOC_PROMOTED_MSG_IR")).html();
                    var docDemotedMsg = parsed.html($translate.instant("DOC_DEMOTED_MSG")).html();
                    var docDemotedMsgIR = parsed.html($translate.instant("DOC_DEMOTED_MSG_IR")).html();
                    $scope.promoteDocument = promoteDocument;
                    function promoteDocument(file) {
                        if ($scope.objectType == "PPAPCHECKLIST") {
                            var options = {
                                title: confirmation,
                                message: promoteDialogMessage.format(file.name),
                                okButtonClass: 'btn-danger'
                            };
                        } else {
                            var options = {
                                title: confirmation,
                                message: promoteDialogMessageIR.format(file.name),
                                okButtonClass: 'btn-danger'
                            };
                        }
                        var parentFolder = file.parentFolder;
                        var folderChildren = file.folderChildren;
                        file.parentFolder = null;
                        file.folderChildren = [];
                        DialogService.confirm(options, function (yes) {
                            if (yes == true) {
                                $rootScope.showBusyIndicator($('.view-content'));
                                DocumentService.promoteDocument(file).then(
                                    function (data) {
                                        file.lifeCyclePhase = data.lifeCyclePhase;
                                        file.parentFolder = parentFolder;
                                        file.folderChildren = folderChildren;
                                        file.reviewers = data.reviewers;
                                        if ($scope.objectType == "PPAPCHECKLIST") {
                                            removeChildren(file);
                                            file.expanded = false;
                                            $timeout(function () {
                                                toggleNode(file);
                                            }, 800);
                                            $rootScope.loadPpap();
                                        }
                                        if ($scope.objectType == "DOCUMENT" || $scope.objectType == "MFRPARTINSPECTIONREPORT" || $scope.objectType == "PPAPCHECKLIST") {
                                            angular.forEach(file.reviewers, function (reviewer) {
                                                if (reviewer.approver && $scope.personDetails.id == reviewer.reviewer && reviewer.status == "NONE") {
                                                    file.approver = true;
                                                } else if (!reviewer.approver && $scope.personDetails.id == reviewer.reviewer && reviewer.status == "NONE") {
                                                    file.reviewer = true;
                                                }
                                            })
                                        }
                                        if ($scope.objectType == "PPAPCHECKLIST") {
                                            $rootScope.showSuccessMessage(docPromotedMsg);
                                        } else {
                                            $rootScope.showSuccessMessage(docPromotedMsgIR);
                                        }
                                        $rootScope.hideBusyIndicator();
                                    }, function (error) {
                                        file.parentFolder = parentFolder;
                                        file.folderChildren = folderChildren;
                                        $rootScope.showErrorMessage(error.message);
                                        $rootScope.hideBusyIndicator();
                                    }
                                )
                            }
                        })
                    }

                    $scope.demoteDocument = demoteDocument;
                    function demoteDocument(file) {
                        if ($scope.objectType == "PPAPCHECKLIST") {
                            var options = {
                                title: confirmation,
                                message: demoteDialogMessage.format(file.name),
                                okButtonClass: 'btn-danger'
                            };
                        } else {
                            var options = {
                                title: confirmation,
                                message: demoteDialogMessageIR.format(file.name),
                                okButtonClass: 'btn-danger'
                            };
                        }
                        var parentFolder = file.parentFolder;
                        var folderChildren = file.folderChildren;
                        file.parentFolder = null;
                        file.folderChildren = [];
                        DialogService.confirm(options, function (yes) {
                            if (yes == true) {
                                $rootScope.showBusyIndicator($('.view-content'));
                                DocumentService.demoteDocument(file).then(
                                    function (data) {
                                        file.lifeCyclePhase = data.lifeCyclePhase;
                                        file.parentFolder = parentFolder;
                                        file.folderChildren = folderChildren;
                                        file.reviewers = data.reviewers;
                                        if ($scope.objectType == "PPAPCHECKLIST") {
                                            removeChildren(file);
                                            file.expanded = false;
                                            $timeout(function () {
                                                toggleNode(file);
                                            }, 800);
                                            $rootScope.loadPpap();
                                        }
                                        if ($scope.objectType == "DOCUMENT" || $scope.objectType == "MFRPARTINSPECTIONREPORT" || $scope.objectType == "PPAPCHECKLIST") {
                                            angular.forEach(file.reviewers, function (reviewer) {
                                                if (reviewer.approver && $scope.personDetails.id == reviewer.reviewer && reviewer.status == "NONE") {
                                                    file.approver = true;
                                                } else if (!reviewer.approver && $scope.personDetails.id == reviewer.reviewer && reviewer.status == "NONE") {
                                                    file.reviewer = true;
                                                }
                                            })
                                        }
                                        if ($scope.objectType == "PPAPCHECKLIST") {
                                            $rootScope.showSuccessMessage(docDemotedMsg);
                                        } else {
                                            $rootScope.showSuccessMessage(docDemotedMsgIR);
                                        }
                                        $rootScope.hideBusyIndicator();
                                    }, function (error) {
                                        file.parentFolder = parentFolder;
                                        file.folderChildren = folderChildren;
                                        $rootScope.showErrorMessage(error.message);
                                        $rootScope.hideBusyIndicator();
                                    }
                                )
                            }
                        })
                    }


                    $scope.reviseDocument = reviseDocument;
                    function reviseDocument(file) {
                        var options = {
                            title: confirmation,
                            message: nextRevDialog,
                            okButtonClass: 'btn-danger'
                        };
                        DialogService.confirm(options, function (yes) {
                            if (yes == true) {
                                $rootScope.showBusyIndicator($('.view-content'));
                                var parentFolder = file.parentFolder;
                                var folderChildren = file.folderChildren;
                                file.parentFolder = null;
                                file.folderChildren = [];
                                DocumentService.reviseDocument(file).then(
                                    function (data) {
                                        file.id = data.id;
                                        file.revision = data.revision;
                                        file.version = data.version;
                                        file.modifiedDate = data.modifiedDate;
                                        file.modifiedByName = data.modifiedByName;
                                        file.createdDate = data.createdDate;
                                        file.createdByName = data.createdByName;
                                        file.createdBy = data.createdBy;
                                        file.lifeCyclePhase = data.lifeCyclePhase;
                                        file.reviewers = data.reviewers;
                                        file.parentFolder = parentFolder;
                                        file.folderChildren = folderChildren;
                                        file.approver = false;
                                        file.reviewer = false;
                                        angular.forEach(file.reviewers, function (reviewer) {
                                            if (reviewer.approver && $scope.personDetails.id == reviewer.reviewer && reviewer.status == 'NONE') {
                                                file.approver = true;
                                            } else if (!reviewer.approver && $scope.personDetails.id == reviewer.reviewer && reviewer.status == 'NONE') {
                                                file.reviewer = true;
                                            }
                                        });
                                        loadCreatedByPersons();
                                        $rootScope.showSuccessMessage(nextRevMsg);
                                        $rootScope.hideBusyIndicator();
                                    }, function (error) {
                                        $rootScope.showErrorMessage(error.message);
                                        $rootScope.hideBusyIndicator();
                                    }
                                )
                            }
                        })
                    }

                    $scope.showReviewers = showReviewers;
                    function showReviewers(file) {
                        var options = {
                            title: "Document Reviewers",
                            template: 'app/desktop/modules/directives/files/documentReviewersView.jsp',
                            controller: 'DocumentReviewersController as documentReviewersVm',
                            resolve: 'app/desktop/modules/directives/files/documentReviewersController',
                            width: 500,
                            showMask: true,
                            data: {
                                documentData: file
                            }
                        };

                        $rootScope.showSidePanel(options);
                    }


                    $scope.showDocumentSubmitDialog = showDocumentSubmitDialog;
                    function showDocumentSubmitDialog(type, file) {
                        $scope.documentApproveType = type;
                        $scope.selectedDocumentReviewer = null;
                        $scope.selectedDocumentFile = file;
                        angular.forEach(file.reviewers, function (reviewer) {
                            if ($scope.personDetails.id == reviewer.reviewer) {
                                $scope.selectedDocumentReviewer = reviewer;
                            }
                        });
                        $timeout(function () {
                            $('#inspectionreport-modal').css("display", "block");
                        }, 500);
                    }

                    $scope.hideDocumentSubmitDialog = hideDocumentSubmitDialog;
                    function hideDocumentSubmitDialog() {
                        $('#inspectionreport-modal').css("display", "none");
                        $scope.selectedDocumentReviewer.notes = null;
                        $scope.error = "";
                    }

                    var notesErrorMsg = parsed.html($translate.instant("ENTER_NOTES_VALIDATION")).html();
                    var enterApproveComment = parsed.html($translate.instant("PLEASE_ENTER_APPROVE_COMMENT")).html();
                    var enterRejectComment = parsed.html($translate.instant("PLEASE_ENTER_REJECT_COMMENT")).html();
                    var enterReviewComment = parsed.html($translate.instant("PLEASE_ENTER_REVIEW_COMMENT")).html();
                    var documentApprovedMsg = parsed.html($translate.instant("DOCUMENT_APPROVED_MSG")).html();
                    var documentRejectedMsg = parsed.html($translate.instant("DOCUMENT_REJECTED_MSG")).html();
                    var documentReviewedMsg = parsed.html($translate.instant("DOCUMENT_REVIEWED_MSG")).html();
                    var inspectionReportTitle = parsed.html($translate.instant("INSPECTION_REPORT")).html();
                    var checklistTitle = parsed.html($translate.instant("CHECKLIST")).html();
                    var documentTitle = parsed.html($translate.instant("DOCUMENT")).html();
                    $scope.submitDocumentReview = submitDocumentReview;
                    function submitDocumentReview() {
                        var successMsg;
                        var docApprovedMsg;
                        var docRejectedMsg;
                        var docReviewedMsg;
                        if ($scope.objectType == "DOCUMENT") {
                            docApprovedMsg = documentApprovedMsg.format(documentTitle);
                            docRejectedMsg = documentRejectedMsg.format(documentTitle);
                            docReviewedMsg = documentReviewedMsg.format(documentTitle);
                        } else if ($scope.objectType == "MFRPARTINSPECTIONREPORT") {
                            docApprovedMsg = documentApprovedMsg.format(inspectionReportTitle);
                            docRejectedMsg = documentRejectedMsg.format(inspectionReportTitle);
                            docReviewedMsg = documentReviewedMsg.format(inspectionReportTitle);
                        } else if ($scope.objectType == "PPAPCHECKLIST") {
                            docApprovedMsg = documentApprovedMsg.format(checklistTitle);
                            docRejectedMsg = documentRejectedMsg.format(checklistTitle);
                            docReviewedMsg = documentReviewedMsg.format(checklistTitle);
                        }
                        if ($scope.selectedDocumentReviewer.notes == null || $scope.selectedDocumentReviewer.notes == '') {
                            if ($scope.documentApproveType == "Approve") {
                                $scope.error = enterApproveComment;
                            } else if ($scope.documentApproveType == "Reject") {
                                $scope.error = enterRejectComment;
                            } else {
                                $scope.error = enterReviewComment;
                            }
                        } else {
                            if ($scope.documentApproveType == "Approve") {
                                successMsg = docApprovedMsg;
                                $scope.selectedDocumentReviewer.status = "APPROVED";
                            } else if ($scope.documentApproveType == "Reject") {
                                successMsg = docRejectedMsg;
                                $scope.selectedDocumentReviewer.status = "REJECTED";
                            } else {
                                successMsg = docReviewedMsg;
                                $scope.selectedDocumentReviewer.status = "REVIEWED";
                            }
                            DocumentService.submitDocumentReview($scope.selectedDocumentFile.id, $scope.selectedDocumentReviewer).then(
                                function (data) {
                                    hideDocumentSubmitDialog();
                                    $scope.selectedDocumentFile.approver = false;
                                    $scope.selectedDocumentFile.reviewer = false;
                                    angular.forEach($scope.selectedDocumentFile.reviewers, function (reviewer) {
                                        if (reviewer.approver && $rootScope.loginPersonDetails.person.id == reviewer.reviewer && reviewer.status == "NONE") {
                                            $scope.selectedDocumentFile.approver = true;
                                        } else if (!reviewer.approver && $rootScope.loginPersonDetails.person.id == reviewer.reviewer && reviewer.status == "NONE") {
                                            $scope.selectedDocumentFile.reviewer = true;
                                        }
                                    });
                                    $rootScope.showSuccessMessage(successMsg);
                                }, function (error) {
                                    $rootScope.showErrorMessage(error.message);
                                    $rootScope.hideBusyIndicator();
                                }
                            )
                        }
                    }


                    (function () {
                        $scope.$on('app.changeFile.tabActivated', function (event, data) {
                            $scope.personDetails = $application.login.person;
                            $scope.isExternal = $application.login.external;
                            $scope.externalPermission = $rootScope.sharedPermission;
                            $scope.files = [];
                            $rootScope.showCopyObjectFilesToClipBoard = false;
                            loadFileConfig();
                            loadCreatedByPersons();
                            loadFiles();
                            var previewTemplate = $("#report-template").parent().html();
                            $("#report-template").remove();
                            $timeout(function () {
                                initFilesTableDropzone(previewTemplate);
                                Dropzone.options.url = null;
                            }, 1000);
                        });
                        $scope.$on('app.details.files.search', function (event, data) {
                            loadFilesByFileName(data.name);
                        });
                    })();
                }
            }
        }
    }
);
