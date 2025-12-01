define(['app/desktop/modules/tm/tm.module',
        'dropzone',
        'app/shared/services/tm/taskService',
        'app/desktop/modules/shared/comments/commentsButtonDirective',
        'app/assets/bower_components/cassini-platform/app/desktop/modules/dialogs/dialogService'],

    function (module) {
        module.controller('TaskAttachmentsController', TaskAttachmentsController);

        function TaskAttachmentsController($scope, $rootScope, $timeout, $state, $stateParams, $cookies, TaskService, CommonService, DialogService) {

            var vm = this;

            vm.loading = true;
            var taskId = $stateParams.taskId;
            var projectId = $stateParams.projectId;
            vm.mfrFiles = [];
            vm.showDropzone = false;
            vm.deleteFile = deleteFile;
            vm.backToFiles = backToFiles;
            vm.fileSizeToString = fileSizeToString;
            vm.downloadFile = downloadFile;
            vm.showFileHistory = showFileHistory;
            vm.selectFiles = selectFiles;
            vm.lockFile = lockFile;
            vm.loginPerson = window.$application.login.person;
            vm.loginPerson.isAdmin = false;
            vm.hasError = false;
            vm.message = "";
            vm.closeNotification = closeNotification;
            vm.showComments = showComments;
            vm.showFiles = true;

            var fileDropZone = null;

            function backToFiles() {
                vm.showDropzone = false;
            }

            function closeNotification() {
                vm.hasError = false;
                vm.message = "";
            }

            $scope.showAutoDeskFile = function (file) {
                $scope.conCallBack(file);
            };

            $scope.registerCallBack = function (callback) {
                $scope.conCallBack = callback;
            };

            if (window.$application.login.loginGroup != null && window.$application.login.loginGroup.name == 'Administrator') {
                vm.loginPerson.isAdmin = true;
            }

            function loadFiles() {
                TaskService.getTaskLatestFiles(projectId, taskId).then(
                    function (data) {
                        vm.taskFiles = data;
                        vm.loading = false;
                        CommonService.getPersonReferences(vm.taskFiles, 'createdBy');
                        CommonService.getPersonReferences(vm.taskFiles, 'modifiedBy');
                        CommonService.getPersonReferences(vm.taskFiles, 'lockedBy');
                    }
                )
            }

            function deleteFile(file) {
                var options = {
                    title: 'Delete File',
                    message: 'Are you sure you want to delete this File?',
                    okButtonClass: 'btn-danger'
                };
                DialogService.confirm(options, function (yes) {
                    if (yes == true) {
                        TaskService.deleteTaskfile($stateParams.projectId, taskId, file).then(
                            function (data) {
                                var index = vm.taskFiles.indexOf(file);
                                vm.taskFiles.splice(index, 1);
                                $rootScope.showSuccessMessage("File deleted successfully");
                                loadFiles();
                                $rootScope.loadDetailsCount();
                            }
                        )
                    }
                });
            }

            function showFileHistory(fileId) {
                var options = {
                    title: 'File History',
                    template: 'app/desktop/modules/tm/details/tabs/attachments/fileHistoryView.jsp',
                    controller: 'FileHistoryController as fileHistoryVm',
                    resolve: 'app/desktop/modules/tm/details/tabs/attachments/fileHistoryController',
                    data: {
                        tId: taskId,
                        fileId: fileId
                    },
                    callback: function (msg) {
                        console.log(msg);
                    }
                };

                $rootScope.showSidePanel(options);
            }

            function initDropzone() {
                $("#documentsDropzoneContainer").on('dragover', handleDragEnter);
                $("#documentsDropzoneContainer").on('dragleave', handleDragLeave);
                $("#documentsDropzoneContainer").on('drop', handleDragLeave);

                var previewNode = $("#template");
                var previewTemplate = previewNode.parent().html();
                previewNode.remove();

                var dropZone = new Dropzone(document.querySelector("#taskDropzoneForm"), { // Make the whole body a dropzone
                    url: "api/projects/" + projectId + "/tasks/" + taskId + "/upload/files", // Set the url
                    thumbnailWidth: 80,
                    thumbnailHeight: 80,
                    maxFilesize: 2000,
                    timeout: 500000,
                    previewTemplate: previewTemplate,
                    autoQueue: true,// Make sure the files aren't queued until manually added
                    parallelUploads: 1,//Files are sent to the server one by one
                    taskPreviewsContainer: "#taskPreviews",
                    success: function (file, response) {
                        if (file != undefined) {
                            dropZone.on("queuecomplete", function (progress) {
                                $("#total-progress").hide();
                                $("#itemFilesTableContainer").removeClass('drag-over');
                                vm.showFileDropzone = false;
                                dropZone.removeAllFiles(true);
                                $scope.$apply();
                                loadFiles();
                                $rootScope.loadDetailsCount();
                            });
                        }
                    },
                    error: function (file, response) {
                        vm.hasError = true;
                        vm.message = vm.message + file.name + ", ";
                        //$rootScope.showErrorMessage(response.message);
                        dropZone.removeFile(file);
                        $scope.$apply();
                    }
                });
                dropZone.on("queuecomplete", function (progress) {
                    $timeout(function () {
                        vm.hasError = false;
                        vm.message = "";
                        vm.showFiles = true;
                        $rootScope.showSuccessMessage("File(s) uploaded successfully");
                    }, 1000);
                });

                $("#taskFilesDropzoneContainer").on('dragover', handleDragEnter);
                $("#taskFilesDropzoneContainer").on('dragleave', handleDragLeave);
                $("#taskFilesDropzoneContainer").on('drop', handleDragLeave);

                fileDropZone = dropZone;
            }

            function handleDragEnter(e) {
                $("#taskFilesDropzoneContainer")[0].classList.add('drag-over');
                vm.showFileDropzone = true;

            }

            function handleDragLeave(e) {
                $("#taskFilesDropzoneContainer")[0].classList.remove('drag-over');
                vm.showFileDropzone = false;
            }

            function selectFiles() {
                vm.showFiles = false;
                $('#taskDropzoneForm')[0].click();
            }

            function checkAndStartFileUpload() {
                var lockedFiles = [];
                for (var i = 0; i < fileDropZone.files.length; i++) {
                    if (isFileLocked(fileDropZone.files[i].name)) {
                        lockedFiles.push(fileDropZone.files[i].name);
                    }
                }

                if (lockedFiles.length == 0) {
                    fileDropZone.enqueueFiles(fileDropZone.getFilesWithStatus(Dropzone.ADDED));
                    $rootScope.showSuccessMessage("File(s) added successfully");
                }
                else {
                    var fileNames = "";

                    for (i = 0; i < lockedFiles.length; i++) {
                        fileNames += lockedFiles[i];

                        if (i != lockedFiles.length - 1) {
                            fileNames += ", ";
                        }
                    }

                    $rootScope.showErrorMessage("Following files are locked and cannot be updated: " + fileNames);
                }
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

            function fileSizeToString(bytes) {
                if (bytes == 0) {
                    return "0.00 B";
                }
                var e = Math.floor(Math.log(bytes) / Math.log(1024));
                return (bytes / Math.pow(1024, e)).toFixed(2) + ' ' + ' KMGTP'.charAt(e) + 'B';
            }

            function downloadFile(file) {
                var url = "api/projects/" + projectId + "/tasks/" + taskId + "/files/" + file.id + "/download";
                window.open(url, '_self');
            }

            function lockFile(document, lock) {
                document.locked = lock;
                document.modifiedByPerson = $application.login.person;
                if (lock) {
                    document.lockedBy = $application.login.person.id;
                    document.lockedByPerson = $application.login.person;
                    document.lockedDate = moment(new Date()).format("DD/MM/YYYY, HH:mm:ss");
                }
                else {
                    document.lockedBy = null;
                    document.lockedByPerson = null;
                    document.lockedDate = null;
                }
                TaskService.updateTaskfile($stateParams.projectId, $stateParams.taskId, document).then(
                    function (data) {
                        document = data;
                        if (document.locked == true) {
                            $rootScope.showSuccessMessage(document.name + " : File locked successfully");
                        } else if (document.locked == false) {
                            $rootScope.showSuccessMessage(document.name + " : File unlocked successfully");
                        }
                        loadFiles();
                    },
                    function (error) {
                        $rootScope.showErrorMessage(error.message);
                    }
                );
            }

            function showComments(document) {
                var options = {
                    title: 'Comments',
                    template: 'app/desktop/modules/shared/comments/commentsView.jsp',
                    controller: 'CommentsController as commentsVm',
                    resolve: 'app/desktop/modules/shared/comments/commentsController',
                    data: {
                        objectType: 'FILE',
                        objectId: document.id
                    }
                };

                $rootScope.showSidePanel(options);
            }

            vm.filePreview = filePreview;
            function filePreview(file) {
                var fileId = file.id;
                var url = "{0}//{1}/api/projects/{2}/tasks/{3}/files/{4}/preview".
                    format(window.location.protocol, window.location.host, $stateParams.projectId, $stateParams.taskId, fileId);
                var newWindow = window.open(url, "_blank");
                newWindow.addEventListener('load', function () {
                    newWindow.document.title = file.name;
                });
                $timeout(function () {
                    window.close();
                }, 2000);
            }

            (function () {
                if ($application.homeLoaded == true) {
                    $scope.$on('app.task.tabactivated', function (event, data) {
                        if (data.tabId == 'details.attachments') {
                            loadFiles();
                            initDropzone();
                        }
                    });
                }
            })();

        }
    }
);