define(
    [
        'app/desktop/desktop.app',
        'dropzone',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/customObjectFilesService',
        'app/assets/bower_components/cassini-platform/app/desktop/directives/autodeskForge/showCADFileDirective',
        'app/desktop/directives/plugin-directive/pluginTableActionDirective'
    ],

    function (module) {
        module.directive('customFilesView', CustomObjectFilesController);

        function CustomObjectFilesController($rootScope, $injector, $sce, $timeout, $translate, $application,
                                             CommonService, CustomObjectFileService, DialogService) {
            return {
                templateUrl: 'app/assets/bower_components/cassini-platform/app/desktop/directives/customFiles/customFilesView.jsp',
                restrict: 'E',
                scope: {
                    'objectType': '@',
                    'objectId': '=',
                    'hasPermission': '='
                },
                link: function ($scope, $elem, attrs) {

                    var parsed = angular.element("<div></div>");
                    $scope.loading = true;
                    $scope.files = [];
                    $scope.showDropzone = false;
                    $scope.showFileDropzone = false;

                    $scope.showFileHistory = showFileHistory;
                    $scope.deleteFile = deleteFile;
                    $scope.backToFiles = backToFiles;
                    $scope.fileSizeToString = fileSizeToString;
                    $scope.lockFile = lockFile;
                    $scope.downloadFile = downloadFile;
                    $scope.showFileDownloadHistory = showFileDownloadHistory;
                    $scope.selectFiles = selectFiles;
                    $scope.clipBoardChangesFiles = $application.clipboard.files;
                    $scope.searchText = null;


                    $scope.dragAndDropFilesTitle = parsed.html($translate.instant("DRAG_DROP_FILE")).html();
                    $scope.dragAndDropFilesTitle = $rootScope.dragAndDropFilesTitle.replace("&amp;", "&");
                    $scope.clickToAddFilesTitle = parsed.html($translate.instant("CLICK_TO_ADD_FILES")).html();
                    var fileSuccessMessage = $translate.instant("FILE_SUCCESS_MESSAGE");
                    $scope.fileLockedMessage = parsed.html($translate.instant("FILE_LOCKED_MESSAGE")).html();
                    $scope.showFileHistoryTitle = parsed.html($translate.instant("SHOW_FILE_HISTORY")).html();
                    var fileUpdatedMessage = $translate.instant("FILE_UPDATE_MESSAGE");
                    $scope.deleteFileTitle = parsed.html($translate.instant("FILE_DIALOG_TITLE")).html();
                    $scope.downloadFileTitle = parsed.html($translate.instant("CLICK_TO_DOWNLOAD_ATTACHMENT")).html();
                    $scope.showFileEditTitle = parsed.html($translate.instant("SHOW_FILE_EDIT")).html();
                    $scope.fileDownloadHistory = parsed.html($translate.instant("FILE_DOWNLOAD_HISTORY_TITLE")).html();
                    $scope.deleteFileTitle = parsed.html($translate.instant("FILE_DIALOG_TITLE")).html();
                    var deleteFolderTitle = parsed.html($translate.instant("DELETE_FOLDER")).html();
                    var fileDialogMessage = parsed.html($translate.instant("FILE_DELETE_DIALOG_MESSAGE")).html();
                    var folderDialogMessage = parsed.html($translate.instant("FOLDER_DELETE_DIALOG_MESSAGE")).html();
                    $scope.downloadFileTitle = parsed.html($translate.instant("DOWNLOAD_FILE")).html();
                    var fileDelete = parsed.html($translate.instant("ITEMDELETE")).html();
                    var folderDelete = parsed.html($translate.instant("FOLDER_DELETE")).html();
                    var createButton = parsed.html($translate.instant("CREATE")).html();
                    var newFolder = parsed.html($translate.instant("NEW_FOLDER")).html();
                    var fileMovedToItem = parsed.html($translate.instant("FILE_MOVED_MSG")).html();
                    var fileMovedTo = parsed.html($translate.instant("FILE_MOVED_TO")).html();
                    var folderSuccess = parsed.html($translate.instant("FOLDER_SUCCESS")).html();
                    $scope.title = parsed.html($translate.instant("ADD_FOLDER_OR_ADD_FILES")).html();
                    $scope.addFolderAndClipboard = parsed.html($translate.instant("ADD_FOLDER_CLIPBOARD")).html();
                    $scope.previewFile = parsed.html($translate.instant("FILE_PREVIEW")).html();
                    var folderDeleteMsg = parsed.html($translate.instant("FOLDER_DELETED_MESSAGE")).html();
                    var fileCopiedToClipBoard = parsed.html($translate.instant("FILES_COPIED_TO_CLIPBOARD")).html();
                    var undoSuccessful = parsed.html($translate.instant("UNDO_SUCCESSFUL")).html();
                    var imageAddedAsDefaultThumbnail = parsed.html($translate.instant("IMAGE_ADDED_AS_DEFAULT_THUMBNAIL")).html();
                    var fileIsLockedBy = parsed.html($translate.instant("FILE_IS_LOCKED_BY")).html();
                    var youCannotCopyTheFile = parsed.html($translate.instant("YOU_CANNOT_COPY_THE_FILE")).html();
                    var filesCopiedMessage = parsed.html($translate.instant("FILES_COPIED_MESSAGE")).html();
                    var filesAlreadyExists = parsed.html($translate.instant("FILES_ALREADY_EXISTS")).html();
                    var and = parsed.html($translate.instant("AND")).html();
                    $scope.ExpandCollapse = parsed.html($translate.instant("EXPAND_COLLAPSE")).html();
                    $scope.addFilesTitle = parsed.html($translate.instant("ADD_FILES")).html();

                    $scope.getIcon = getIcon;
                    function getIcon(fileName) {
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

                    $scope.getIconColor = getIconColor;
                    function getIconColor(fileName) {
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
                            template: 'app/assets/bower_components/cassini-platform/app/desktop/directives/customFiles/customFileHistoryView.jsp',
                            controller: 'CustomFileHistoryController as fileHistoryVm',
                            resolve: 'app/assets/bower_components/cassini-platform/app/desktop/directives/customFiles/customFileHistoryController',
                            showMask: true,
                            data: {
                                objectId: $scope.objectId,
                                file: file,
                                fileMode: "FileVersion"
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
                                CustomObjectFileService.updateFileName($scope.objectId, fileId, newFileName).then(
                                    function (data) {
                                        var renamedFile = data;
                                        swal($rootScope.done, $rootScope.reNameSuccessMsg, "success");
                                        //loadFiles();
                                        fileRowObject.id = renamedFile.id;
                                        fileRowObject.name = renamedFile.name;
                                        fileRowObject.version = renamedFile.version;
                                        fileRowObject.modifiedDate = renamedFile.modifiedDate;
                                    }, function (error) {
                                        $rootScope.showErrorMessage(error);
                                    }
                                )
                            }
                        });
                    }

                    function loadHasPermission() {
                        $scope.hasCreate = $rootScope.hasPermission('file', 'create');
                        $scope.hasEdit = $rootScope.hasPermission('file', 'edit');
                        $scope.hasDelete = $rootScope.hasPermission('file', 'delete');
                        $scope.hasRename = $rootScope.hasPermission('file', 'rename');
                        $scope.hasDownload = $rootScope.hasPermission('file', 'download');
                        $scope.hasPreview = $rootScope.hasPermission('file', 'preview');
                        $scope.hasReplace = $rootScope.hasPermission('file', 'replace');
                    }

                    function showFileDownloadHistory(file) {
                        var options = {
                            title: $rootScope.fileDownloadHistoryTitle,
                            template: 'app/assets/bower_components/cassini-platform/app/desktop/directives/customFiles/customFileHistoryView.jsp',
                            controller: 'CustomFileHistoryController as fileHistoryVm',
                            resolve: 'app/assets/bower_components/cassini-platform/app/desktop/directives/customFiles/customFileHistoryController',
                            showMask: true,
                            data: {
                                objectId: $scope.objectId,
                                file: file,
                                fileMode: "FileDownloadHistory"
                            },
                            callback: function (msg) {
                                console.log(msg);
                            }
                        };

                        $rootScope.showSidePanel(options);
                    }

                    function backToFiles() {
                        $scope.showDropzone = false;
                    }

                    $scope.filesMap = new Hashtable();
                    function loadFiles() {
                        $scope.searchText = null;
                        CustomObjectFileService.getCustomObjectFiles($scope.objectId).then(
                            function (data) {
                                $scope.files = data;
                                if ($scope.files.length == 0) $rootScope.hasFiles = false;
                                else $rootScope.hasFiles = true;
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

                                CommonService.getPersonReferences($scope.files, 'createdBy');
                                CommonService.getPersonReferences($scope.files, 'modifiedBy');
                                CommonService.getPersonReferences($scope.files, 'lockedBy');
                                loadCustomObjectCounts();
                            }
                        );
                    }

                    function loadCustomObjectCounts() {
                        $scope.$emit('app.customObj.files.count');
                    }

                    $scope.itemMaster = {
                        thumbnail: null
                    };

                    function loadFilesByFileName(name) {
                        $scope.searchText = name;
                        $rootScope.showBusyIndicator($('.view-container'));
                        CustomObjectFileService.getCustomObjectFilesByName($scope.objectId, name).then(
                            function (data) {
                                $scope.files = data;
                                if ($scope.files.length == 0) $rootScope.hasFiles = false;
                                else $rootScope.hasFiles = true;
                                $scope.loading = false;
                                CommonService.getPersonReferences($scope.files, 'createdBy');
                                CommonService.getPersonReferences($scope.files, 'modifiedBy');
                                CommonService.getPersonReferences($scope.files, 'lockedBy');
                                $rootScope.hideBusyIndicator();
                            }
                        );

                    }

                    var fileUpdate = parsed.html($translate.instant("FILE_UPDATE")).html();
                    var fileReplaces = parsed.html($translate.instant("FILE_REPLACE")).html();
                    var update = parsed.html($translate.instant("UPDATE")).html();
                    $scope.noPermission = parsed.html($translate.instant('NO_PERMISSION_PERFORM')).html();

                    $scope.showFileEdit = showFileEdit;
                    function showFileEdit(file) {
                        var options = {
                            title: fileUpdate,
                            template: 'app/assets/bower_components/cassini-platform/app/desktop/directives/customFiles/fileEditView.jsp',
                            controller: 'FileEditController as fileEditVm',
                            resolve: 'app/assets/bower_components/cassini-platform/app/desktop/directives/customFiles/fileEditController',
                            width: 700,
                            showMask: true,
                            data: {
                                editFile: file,
                                fileEditPermission: true
                            },
                            buttons: [
                                {text: update, broadcast: 'app.custom.file.edit'}
                            ],
                            callback: function (result) {
                                file.description = result.description;
                            }
                        };

                        $rootScope.showSidePanel(options);

                    }

                    $scope.fileReplace = fileReplace;
                    function fileReplace(file) {
                        var options = {
                            title: fileReplaces,
                            template: 'app/assets/bower_components/cassini-platform/app/desktop/directives/customFiles/customFileReplaceView.jsp',
                            controller: 'CustomFileReplaceController as customFileReplaceVm',
                            resolve: 'app/assets/bower_components/cassini-platform/app/desktop/directives/customFiles/customFileReplaceController',
                            width: 500,
                            showMask: true,
                            data: {
                                replaceFile: file,
                                selectedCustomId: $scope.objectId,
                                files: $scope.files
                            },
                            callback: function () {
                                CustomObjectFileService.getLatestUploadedFile($scope.objectId, file.id).then(
                                    function (data) {
                                        file.id = data.id;
                                        file.name = data.name;
                                        file.version = data.version;
                                        file.size = data.size;
                                        file.modifiedDate = data.modifiedDate;
                                        file.description = data.description;
                                        $scope.$evalAsync();
                                    }
                                )
                            }
                        };

                        $rootScope.showSidePanel(options);
                    }

                    var updateFolderTitle = parsed.html($translate.instant("UPDATE_FOLDER")).html();
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
                        var buttonName = createButton;
                        if (type == "Add") {
                            title = newFolder;
                        } else {
                            title = updateFolderTitle;
                            buttonName = update;
                        }
                        var options = {
                            title: title,
                            template: 'app/assets/bower_components/cassini-platform/app/desktop/directives/customFiles/newFolderView.jsp',
                            controller: 'NewFolderController as newFolderVm',
                            resolve: 'app/assets/bower_components/cassini-platform/app/desktop/directives/customFiles/newFolderController',
                            width: 500,
                            showMask: true,
                            data: {
                                parentFolder: $scope.parentFolderId,
                                fileObjectId: $scope.objectId,
                                folderCreateType: type,
                                folderData: folder
                            },
                            buttons: [
                                {text: buttonName, broadcast: 'app.custom.folder'}
                            ],
                            callback: function (data) {
                                var result = data;
                                if (type == "Add") {
                                    result.level = 0;
                                    result.folderChildren = [];
                                    if (folder != null) {
                                        if (folder.expanded) {
                                            var index = $scope.files.indexOf(folder);
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

                                            CommonService.getPersonReferences(folder.folderChildren, 'createdBy');
                                            CommonService.getPersonReferences(folder.folderChildren, 'modifiedBy');
                                            CommonService.getPersonReferences(folder.folderChildren, 'lockedBy');

                                        } else {
                                            toggleNode(folder);
                                        }
                                    } else {
                                        $scope.files.push(result);

                                        CommonService.getPersonReferences($scope.files, 'createdBy');
                                        CommonService.getPersonReferences($scope.files, 'modifiedBy');
                                        CommonService.getPersonReferences($scope.files, 'lockedBy');
                                    }
                                } else {
                                    folder.parentFolder = parentFolder;
                                    folder.folderChildren = folderChildren;
                                    folder.name = result.name;
                                    folder.description = result.description;
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
                        $("#itemFilesTableContainer")[0].classList.add('drag-over');
                        $scope.showFileDropzone = true;

                    }

                    function handleDragLeave(e) {
                        $("#itemFilesTableContainer")[0].classList.remove('drag-over');
                        $scope.showFileDropzone = false;
                    }

                    function fileSizeToString(bytes) {
                        if (bytes == 0) {
                            return "0.00 B";
                        }
                        var e = Math.floor(Math.log(bytes) / Math.log(1024));
                        return (bytes / Math.pow(1024, e)).toFixed(2) + ' ' + ' KMGTP'.charAt(e) + 'B';
                    }

                    function downloadFile(file) {
                        var url = "{0}//{1}/api/customObjects/{2}/files/{3}/download".
                            format(window.location.protocol, window.location.host,
                            $scope.objectId, file.id);
                        //launchUrl(url);
                        $rootScope.downloadFileFromIframe(url);
                        //window.open(url);
                        //$timeout(function () {
                        //    window.close();
                        //}, 2000);
                        CustomObjectFileService.updateCustomObjectFileDownloadHistory($scope.objectId, file.id).then(
                            function (data) {

                            }
                        )
                    }

                    $rootScope.downloadCustomFilesFilesAsZip = downloadCustomFilesFilesAsZip;
                    function downloadCustomFilesFilesAsZip() {
                        $rootScope.showBusyIndicator($('.view-container'));
                        var url = "{0}//{1}/api/customObjects/{2}/files/zip".
                            format(window.location.protocol, window.location.host, $scope.objectId);

                        //launchUrl(url);
                        window.open(url);
                        $rootScope.hideBusyIndicator();
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
                        CustomObjectFileService.updateCustomObjectFile($scope.objectId, file.id, file).then(
                            function (data) {
                                file = data;
                                $rootScope.showSuccessMessage(fileUpdatedMessage);
                            }
                        );
                    }

                    function deleteFile(file) {
                        var options = {
                            title: $scope.deleteFileTitle,
                            message: fileDialogMessage + " [ " + file.name + " ] " + "?",
                            okButtonClass: 'btn-danger'
                        };
                        DialogService.confirm(options, function (yes) {
                            if (yes == true) {
                                $rootScope.showBusyIndicator();
                                CustomObjectFileService.deleteCustomObjectFile($scope.objectId, file.id).then(
                                    function (data) {
                                        var index = $scope.files.indexOf(file);
                                        $scope.files.splice(index, 1);
                                        $rootScope.showSuccessMessage(fileDeleteMessage);

                                        if (file.parentFile != null) {
                                            file.parentFolder.count = file.parentFolder.count - 1;
                                            file.parentFolder.folderChildren.splice(file.parentFolder.folderChildren.indexOf(file), 1);
                                        }
                                        loadCustomObjectCounts();
                                        $rootScope.hideBusyIndicator();
                                    }
                                )
                            }
                        });
                    }

                    $scope.registerCallBack = function (callback) {
                        $scope.conCallBack = callback;
                    };

                    $scope.showAutoDeskFile = function (file) {
                        $scope.conCallBack(file);
                    };

                    function loadFileConfig() {
                        loadHasPermission();
                        $scope.fileConfig = {};
                        $scope.fileConfig.fileTypeError = false;
                        $scope.fileConfig.fileSizeError = false;
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
                            },
                            function (error) {
                                console.log(error);
                            });
                    };

                    var filelimit = parsed.html($translate.instant("FILE_LIMIT")).html();
                    var fileType = $translate.instant("FILE_TYPE");
                    var eFiles = "";

                    function initFilesTableDropzone(previewTemplate) {
                        $scope.fileConfig.error = false;
                        var dropZone = new Dropzone(document.querySelector('#itemFiles'), { // Make the whole body a dropzone
                            url: "api/customObjects/" + $scope.objectId + "/files/" + 0, // Set the url
                            thumbnailWidth: 80,
                            thumbnailHeight: 80,
                            timeout: 500000,
                            previewTemplate: previewTemplate,
                            //autoQueue: true,// Make sure the files aren't queued until manually added
                            parallelUploads: 1000000,//Files are sent to the server one by one
                            uploadMultiple: true,
                            previewsContainer: "#previews",
                            maxFilesize: $scope.fileConfig.fileSize
                        });

                        dropZone.on("queuecomplete", function (progress) {
                            $("#total-progress").hide();
                            $("#itemFilesTableContainer").removeClass('drag-over');
                            $scope.showFileDropzone = false;
                            dropZone.removeAllFiles(true);
                            $("#itemFilesTable").show();
                            $scope.$apply();
                            if ($scope.fileConfig.error) {
                                if ($scope.fileConfig.fileTypeError == true && $scope.fileConfig.fileSizeError == true) {
                                    $rootScope.showErrorMessage(fileType + eFiles.replace(/,\s*$/, "") + " and " + filelimit + $scope.fileConfig.fileSize + " MB");
                                } else if ($scope.fileConfig.fileTypeError == true && $scope.fileConfig.fileSizeError == false) {
                                    $rootScope.showErrorMessage(fileType + eFiles.replace(/,\s*$/, ""));
                                } else if ($scope.fileConfig.fileTypeError == false && $scope.fileConfig.fileSizeError == true) {
                                    $rootScope.showErrorMessage(filelimit + $scope.fileConfig.fileSize + " MB");
                                } else {
                                    $rootScope.showErrorMessage($scope.fileConfig.message);
                                }
                            } else {
                                $rootScope.showSuccessMessage(fileSuccessMessage);
                            }
                            $scope.fileConfig.error = false;
                            eFiles = "";
                            $scope.fileConfig.fileTypeError = false;
                            $scope.fileConfig.fileSizeError = false;
                            loadCustomObjectCounts();
                            loadFiles();
                        });

                        dropZone.on("uploadprogress", function () {
                            $("#itemFilesTable").hide();
                        })

                        dropZone.on("success", function (file, response) {
                            if (response.length == 0) {
                                eFiles += file.name + ", ";
                                $scope.fileConfig.fileTypeError = true;
                                $scope.fileConfig.error = true;
                            }
                        });

                        dropZone.on("error", function (file, response) {
                            $scope.fileConfig.fileSizeError = true;
                            $scope.fileConfig.error = true;
                        });

                        $("#itemFilesTableContainer").on('dragover', handleDragEnter);
                        $("#itemFilesTableContainer").on('dragleave', handleDragLeave);
                        $("#itemFilesTableContainer").on('drop', handleDragLeave);

                    }

                    function selectFiles() {
                        $('#itemFiles')[0].click();
                    }

                    $scope.folderId = null;
                    $scope.loadFolderFile = loadFolderFile;
                    function loadFolderFile(folder) {
                        $scope.folderId = folder.id;
                        $timeout(function () {
                            loadFolderFileId(folder)
                        }, 1000)
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
                            if ($scope.fileConfig.fileTypeError == true && $scope.fileConfig.fileSizeError == true) {
                                $rootScope.showErrorMessage(fileType + eFiles + " and " + filelimit + $scope.fileConfig.fileSize + " MB");
                            } else if ($scope.fileConfig.fileTypeError == true && $scope.fileConfig.fileSizeError == false) {
                                $rootScope.showErrorMessage(fileType + eFiles);
                            } else if ($scope.fileConfig.fileTypeError == false && $scope.fileConfig.fileSizeError == true) {
                                $rootScope.showErrorMessage(filelimit + $scope.fileConfig.fileSize + " MB");
                            }
                        } else {
                            $rootScope.showSuccessMessage(fileSuccessMessage);
                        }
                        $scope.fileConfig.error = false;
                        eFiles = "";
                        $scope.fileConfig.fileTypeError = false;
                        $scope.fileConfig.fileSizeError = false;
                    }

                    $scope.loadFolderFileId = loadFolderFileId;
                    function loadFolderFileId(folder) {
                        document.getElementById($scope.folderId).onchange = function () {
                            var file = document.getElementById($scope.folderId);
                            $scope.importFiles = [];
                            eFiles = "";
                            $scope.validateFile(file.files);
                            $rootScope.showBusyIndicator($(".view-content"));
                            CustomObjectFileService.uploadCustomObjectFiles($scope.objectId, $scope.folderId, $scope.importFiles).then(
                                function (data) {

                                    removeChildren(folder);
                                    folder.expanded = false;
                                    $timeout(function () {
                                        toggleNode(folder);
                                    }, 500);
                                    loadCustomObjectCounts();
                                    $rootScope.hideBusyIndicator();
                                }, function (error) {
                                    $rootScope.hideBusyIndicator();
                                }
                            )

                        };
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
                        var url = "{0}//{1}/api/customObjects/{2}/files/{3}/preview".
                            format(window.location.protocol, window.location.host, itemId, fileId);
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
                            CustomObjectFileService.getFolderChildren($scope.objectId, folder.id).then(
                                function (data) {
                                    var childrenData = data;
                                    folder.count = childrenData.length;
                                    angular.forEach(childrenData, function (item) {
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
                                        $scope.files.splice(index, 0, item);
                                    });
                                    CommonService.getPersonReferences(folder.folderChildren, 'createdBy');
                                    CommonService.getPersonReferences(folder.folderChildren, 'modifiedBy');
                                    CommonService.getPersonReferences(folder.folderChildren, 'lockedBy');
                                    $rootScope.hideBusyIndicator();

                                    $scope.$evalAsync();

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
                            message: folderDialogMessage + " [ " + folder.name + " ] " + "?",
                            okButtonClass: 'btn-danger'
                        };
                        DialogService.confirm(options, function (yes) {
                            if (yes == true) {
                                $rootScope.showBusyIndicator($('.view-content'));
                                CustomObjectFileService.deleteCustomObjectFolder($scope.objectId, folder.id).then(
                                    function (data) {
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
                                        loadCustomObjectCounts();
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
                        if (fileObject != undefined) {
                            $rootScope.showBusyIndicator($('.view-container'));
                            var parentFolder = fileObject.parentFolder;
                            fileObject.parentFolder = null;
                        }
                        CustomObjectFileService.moveCustomObjectFileToFolder($scope.objectId, fileObject).then(
                            function (data) {
                                $scope.files.splice($scope.files.indexOf(fileObject), 1);
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
                                        data.level = folder.level + 1;
                                        folder.count = folder.count + 1;
                                        folder.folderChildren.push(data);
                                        data.parentFolder = folder;
                                        $scope.files.splice(index, 0, data);

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

                                    CommonService.getPersonReferences($scope.files, 'createdBy');
                                    CommonService.getPersonReferences($scope.files, 'modifiedBy');
                                    data.level = 0;
                                    $scope.files.push(data);
                                    $scope.$evalAsync();
                                    $rootScope.hideBusyIndicator();
                                    $rootScope.showSuccessMessage(" [ " + data.name + " ] " + fileMovedToItem);

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
                            if (file.locked && file.lockedByObject.id == $rootScope.loginPersonDetails.person.id) {
                                $scope.selectedFiles.push(file);
                            } else if (!file.locked) {
                                $scope.selectedFiles.push(file);
                            } else {
                                file.selected = false;
                                $rootScope.showWarningMessage(fileIsLockedBy + " [ " + file.lockedByObject.fullName + " ]." + youCannotCopyTheFile)
                            }
                        } else {
                            $scope.selectedFiles.splice($scope.selectedFiles.indexOf(file), 1);
                        }

                        if ($scope.selectedFiles.length > 0) {
                            $rootScope.showCopyCustomFilesToClipBoard = true;
                            if ($rootScope.selectedMasterItemId == null) {
                                $rootScope.arrangeFreeTextSearch();
                            }
                        } else {
                            $rootScope.showCopyCustomFilesToClipBoard = false;
                            if ($rootScope.selectedMasterItemId == null) {
                                $rootScope.arrangeFreeTextSearch();
                            }
                        }
                    }

                    $rootScope.copyCustomFilesToClipBoard = copyCustomFilesToClipBoard;
                    function copyCustomFilesToClipBoard() {
                        angular.forEach($scope.selectedFiles, function (selectedItem) {
                            selectedItem.selected = false;
                            var file = angular.copy(selectedItem);
                            file.parentFolder = null;
                            $application.clipboard.files.push(file);
                        });
                        $rootScope.showCopyCustomFilesToClipBoard = false;
                        $rootScope.clipBoardCustomFiles = $application.clipboard.files;
                        $rootScope.showSuccessMessage(fileCopiedToClipBoard);
                        $scope.selectedFiles = [];
                        $scope.flag = false;
                        if ($rootScope.selectedMasterItemId == null) {
                            $rootScope.arrangeFreeTextSearch();
                        }
                    }

                    $rootScope.clearAndCopyChangeFilesToClipBoard = clearAndCopyChangeFilesToClipBoard;
                    function clearAndCopyChangeFilesToClipBoard() {
                        $application.clipboard.files = [];
                        copyChangeFilesToClipBoard();
                    }

                    $scope.copiedData = [];
                    $scope.pasteFilesFromClipboard = pasteFilesFromClipboard;
                    function pasteFilesFromClipboard(folder, type) {
                        $rootScope.showBusyIndicator($('.view-content'));
                        $rootScope.notification.undoType = "CHANGE";
                        var fileId = 0;
                        if (type == "FOLDER") {
                            fileId = folder.id;
                        }
                        CustomObjectFileService.pasteCustomObjectFilesFromClipboard($scope.objectId, fileId, $application.clipboard.files).then(
                            function (data) {
                                $scope.copiedData = data;
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
                                        $scope.files.push(obj);
                                    });
                                    $scope.loading = false;
                                    $rootScope.searchModeType = false;

                                    CommonService.getPersonReferences($scope.files, 'createdBy');
                                    CommonService.getPersonReferences($scope.files, 'modifiedBy');
                                    CommonService.getPersonReferences($scope.files, 'lockedBy');
                                }
                                //$rootScope.clipBoardChangesFiles = [];
                                loadCustomObjectCounts();
                                var clipboardFilesCount = $application.clipboard.files.length;
                                if ((clipboardFilesCount - $scope.copiedData.length) > 0) {
                                    if ($scope.copiedData.length == 0) {
                                        $rootScope.showWarningMessage(filesAlreadyExists);
                                    } else {
                                        $rootScope.showSuccessMessage("[ " + $scope.copiedData.length + " ] " + filesCopiedMessage + " " + and + " [ " + (clipboardFilesCount - $scope.copiedData.length) + " ] file(s) already exist", true, "CHANGE");
                                    }
                                } else {
                                    $rootScope.showSuccessMessage("[ " + $scope.copiedData.length + " ] " + filesCopiedMessage, true, "CHANGE");
                                }
                                $rootScope.hideBusyIndicator();
                            }, function (error) {
                                $rootScope.showErrorMessage(error.message);
                                $rootScope.hideBusyIndicator();
                            }
                        )
                    }

                    $rootScope.undoCopiedItemFiles = undoCopiedItemFiles;
                    function undoCopiedItemFiles() {
                        if ($scope.copiedData.length > 0) {
                            $rootScope.closeNotification();
                            $rootScope.showBusyIndicator($(".view-content"));
                            CustomObjectFileService.undoCopiedCustomObjectFiles($scope.objectId, $scope.copiedData).then(
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
                                    loadCustomObjectCounts();
                                }
                            )
                        }
                    }

                    var alreadyLoaded = false;
                    (function () {
                        $scope.$on('app.customObj.files.tabActivated', function (event, data) {
                            //if(!alreadyLoaded) {
                            $scope.personDetails = $rootScope.loginPersonDetails.person;
                            loadFileConfig();
                            loadFiles();
                            var previewTemplate = $("#template").parent().html();
                            $("#template").remove();
                            $timeout(function () {
                                initFilesTableDropzone(previewTemplate);
                                alreadyLoaded = true;
                            }, 1000);
                            //}
                        });
                        $scope.$on('app.customObj.files.search', function (event, data) {
                            loadFilesByFileName(data.name);
                        });
                    })();
                }
            }
        }
    }
);
