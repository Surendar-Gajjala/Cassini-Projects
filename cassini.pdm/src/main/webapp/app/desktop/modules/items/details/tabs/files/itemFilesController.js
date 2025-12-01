define(
    [
        'app/desktop/modules/items/item.module',
        'dropzone',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/commonService',
        'app/shared/services/itemTypeService',
        'app/shared/services/itemService',
        'app/shared/services/itemFileService'
    ],
    function (module) {
        module.controller('ItemFilesController', ItemFilesController);

        function ItemFilesController($scope, $rootScope, $timeout, $state, $stateParams, $cookies,
                                     CommonService, ItemTypeService, ItemService, ItemFileService, DialogService) {
            var vm = this;

            vm.loading = true;
            vm.itemId = $stateParams.itemId;
            vm.files = [];
            vm.showDropzone = false;
            vm.showFileHistory = showFileHistory;
            vm.deleteFile = deleteFile;
            vm.backToFiles = backToFiles;
            vm.fileSizeToString = fileSizeToString;
            vm.lockFile = lockFile;
            vm.downloadFile = downloadFile;

            var fileDropZone = null;

            function showFileHistory(fileId) {
                var options = {
                    title: 'File History',
                    template: 'app/desktop/modules/items/details/tabs/files/fileHistoryView.jsp',
                    controller: 'FileHistoryController as fileHistoryVm',
                    resolve: 'app/desktop/modules/items/details/tabs/files/fileHistoryController',
                    data: {
                        itemId: vm.itemId,
                        fileId: fileId
                    },
                    callback: function (msg) {
                        console.log(msg);
                    }
                };

                $rootScope.showSidePanel(options);
            }

            function backToFiles() {
                vm.showDropzone = false;
            }

            function loadFiles() {
                ItemFileService.getItemFiles(vm.itemId).then(
                    function (data) {
                        vm.files = data;
                        vm.loading = false;

                        CommonService.getPersonReferences(vm.files, 'createdBy');
                        CommonService.getPersonReferences(vm.files, 'modifiedBy');
                        CommonService.getPersonReferences(vm.files, 'lockedBy');
                    }
                );

            }


            function initDropzone() {
                $("#filesDropzoneContainer").on('dragover', handleDragEnter);
                $("#filesDropzoneContainer").on('dragleave', handleDragLeave);
                $("#filesDropzoneContainer").on('drop', handleDragLeave);

                var previewNode = $("#template");
                var previewTemplate = previewNode.parent().html();
                previewNode.remove();

                var myDropzone = new Dropzone(document.querySelector("#dropzoneForm"), { // Make the whole body a dropzone
                    url: "api/pdm/itemFiles/" + vm.itemId + "/files", // Set the url
                    thumbnailWidth: 80,
                    thumbnailHeight: 80,
                    parallelUploads: 20,
                    previewTemplate: previewTemplate,
                    autoQueue: false, // Make sure the files aren't queued until manually added
                    previewsContainer: "#previews", // Define the container to display the previews
                    clickable: ".fileinput-button" // Define the element that should be used as click trigger to select files.

                });

                myDropzone.on("addedfile", function (file) {
                    // Hookup the start button
                    file.previewElement.querySelector(".start").onclick = function () {
                        myDropzone.enqueueFile(file);

                    };

                    $scope.showDropArea = false;

                    document.querySelector("#filesDropZone").style.display = "none";
                    document.querySelector("#primaryBtn").style.display = "inline";
                    document.querySelector("#warningBtn").style.display = "inline";

                });

                myDropzone.on("removedfile", function (file) {
                    if (myDropzone.files.length === 0) {
                        document.querySelector("#filesDropZone").style.display = "table";
                        document.querySelector("#primaryBtn").style.display = "none";
                        document.querySelector("#warningBtn").style.display = "none";
                    }
                });

                // Update the total progress bar
                myDropzone.on("totaluploadprogress", function (progress) {
                    document.querySelector("#total-progress .progress-bar").style.width = progress + "%";
                });

                myDropzone.on("sending", function (file) {
                    // Show the total progress bar when upload starts
                    document.querySelector("#total-progress").style.opacity = "1";
                    // And disable the start button
                    file.previewElement.querySelector(".start").setAttribute("disabled", "disabled");
                });

                // Hide the total progress bar when nothing's uploading anymore
                myDropzone.on("queuecomplete", function (progress) {
                    document.querySelector("#total-progress").style.opacity = "0";
                    vm.showDropzone = false;
                    $scope.$apply();
                    $rootScope.showSuccessMessage("File(s) added successfully..");
                    loadFiles();
                });

                // Setup the buttons for all transfers
                // The "add files" button doesn't need to be setup because the config
                // `clickable` has already been specified.
                document.querySelector("#actions .start").onclick = function () {
                    checkAndStartFileUpload();
                };
                document.querySelector("#actions .cancel").onclick = function () {
                    myDropzone.removeAllFiles(true);

                    $scope.showDropArea = true;
                    document.querySelector("#filesDropZone").style.display = "table";
                    document.querySelector("#primaryBtn").style.display = "none";
                    document.querySelector("#warningBtn").style.display = "none";
                };

                fileDropZone = myDropzone;

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

            function handleDragEnter(e) {
                this.classList.add('drag-over');
            }

            function handleDragLeave(e) {
                this.classList.remove('drag-over');
            }

            function fileSizeToString(bytes) {
                if (bytes == 0) {
                    return "0.00 B";
                }
                var e = Math.floor(Math.log(bytes) / Math.log(1024));
                return (bytes / Math.pow(1024, e)).toFixed(2) + ' ' + ' KMGTP'.charAt(e) + 'B';
            }

            function downloadFile(file) {
                var url = "{0}//{1}/api/pdm/itemFiles/{2}/files/{3}/download".
                    format(window.location.protocol, window.location.host,
                    vm.itemId, file.id);
                launchUrl(url);
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
                ItemFileService.updateItemFile(vm.itemId, file).then(
                    function (data) {
                        file = data;
                        $rootScope.showSuccessMessage('File updated successfully.')
                    }
                );
            }

            function deleteFile(file) {
                var options = {
                    title: 'Delete File',
                    message: 'Are you sure you want to delete this file?',
                    okButtonClass: 'btn-danger'
                };
                DialogService.confirm(options, function (yes) {
                    if (yes == true) {
                        ItemFileService.deleteItemFile(vm.itemId, file.id).then(
                            function (data) {
                                var index = vm.files.indexOf(file);
                                vm.files.splice(index, 1);
                                $rootScope.showErrorMessage("Item file deleted successfully!");
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


            (function () {
                if ($application.homeLoaded == true) {
                    loadFiles();
                    initFileDrop();

                    $scope.$on('app.item.addfiles', function () {
                        vm.showDropzone = true;

                        if (fileDropZone == null) {
                            initDropzone();
                        }
                        else {
                            fileDropZone.removeAllFiles();
                        }
                    });
                }
            })();
        }
    }
);