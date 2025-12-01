define(
    [
        'app/desktop/modules/item/item.module',
        'dropzone',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/commonService',
        'app/shared/services/core/itemTypeService',
        'app/shared/services/core/itemService',
        'app/shared/services/core/itemFileService'
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
            vm.showFileDropzone = false;

            vm.showFileHistory = showFileHistory;
            vm.deleteFile = deleteFile;
            vm.backToFiles = backToFiles;
            vm.fileSizeToString = fileSizeToString;
            vm.lockFile = lockFile;
            vm.downloadFile = downloadFile;
            vm.showFileDownloadHistory = showFileDownloadHistory;
            vm.selectFiles = selectFiles;

            var fileDropZone = null;

            function showFileHistory(file) {
                var options = {
                    title: "File Version History",
                    template: 'app/desktop/modules/item/details/tabs/files/fileHistoryView.jsp',
                    controller: 'FileHistoryController as fileHistoryVm',
                    resolve: 'app/desktop/modules/item/details/tabs/files/fileHistoryController',
                    data: {
                        itemId: vm.itemId,
                        itemFile: file,
                        itemFileMode: "FileVersion"
                    },
                    callback: function (msg) {
                        console.log(msg);
                    }
                };

                $rootScope.showSidePanel(options);
            }

            function showFileDownloadHistory(file) {
                var options = {
                    title: "File Download History",
                    template: 'app/desktop/modules/item/details/tabs/files/fileHistoryView.jsp',
                    controller: 'FileHistoryController as fileHistoryVm',
                    resolve: 'app/desktop/modules/item/details/tabs/files/fileHistoryController',
                    data: {
                        itemId: vm.itemId,
                        itemFile: file,
                        itemFileMode: "FileDownloadHistory"
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
                        $rootScope.searchModeType = false;

                        CommonService.getPersonReferences(vm.files, 'createdBy');
                        CommonService.getPersonReferences(vm.files, 'modifiedBy');
                        CommonService.getPersonReferences(vm.files, 'lockedBy');

                        $rootScope.viewInfo.title = $rootScope.selectedItemRevisionDetails.itemMaster.itemName
                            + " - " + $rootScope.selectedTab.heading;
                    }
                );

            }

            function loadFilesByFileName(name) {

                ItemFileService.getItemFilesByName(vm.itemId, name).then(
                    function (data) {
                        vm.files = data;
                        vm.loading = false;
                        $rootScope.searchModeType = true;
                        CommonService.getPersonReferences(vm.files, 'createdBy');
                        CommonService.getPersonReferences(vm.files, 'modifiedBy');
                        CommonService.getPersonReferences(vm.files, 'lockedBy');
                    }
                );


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
                        $rootScope.showWarningMessage($scope.fileLockedMessage + " : " + fileNames);
                        loadFiles();
                    }
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
                vm.showFileDropzone = true;

            }

            function handleDragLeave(e) {
                this.classList.remove('drag-over');
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
                var url = "{0}//{1}/api/drdo/items/{2}/files/{3}/preview".
                    format(window.location.protocol, window.location.host,
                    vm.itemId, file.id);
                //launchUrl(url);
                var newWindow = window.open(url, "_blank");
                newWindow.addEventListener('load', function () {
                    newWindow.document.title = file.name;
                });
                /*window.open(url);
                 $timeout(function () {
                 window.close();
                 }, 2000);*/
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
                        $rootScope.showSuccessMessage("File updated successfully");
                    }
                );
            }

            function deleteFile(file) {
                var options = {
                    title: "Delete File",
                    message: "Please confirm to delete this file.",
                    okButtonClass: 'btn-danger'
                };
                DialogService.confirm(options, function (yes) {
                    if (yes == true) {
                        ItemFileService.deleteItemFile(vm.itemId, file.id).then(
                            function (data) {
                                var index = vm.files.indexOf(file);
                                vm.files.splice(index, 1);
                                $rootScope.showSuccessMessage("File deleted successfully");
                            }
                        )
                    }
                });
            }


            function initFilesTableDropzone() {
                var previewNode = $("#template");
                var previewTemplate = previewNode.parent().html();
                previewNode.remove();


                var dropZone = new Dropzone(document.querySelector('#itemFiles'), { // Make the whole body a dropzone
                    url: "api/drdo/items/" + vm.itemId + "/files", // Set the url
                    thumbnailWidth: 80,
                    thumbnailHeight: 80,
                    parallelUploads: 20,
                    maxFilesize: 1000,
                    previewTemplate: previewTemplate,
                    autoQueue: true, // Make sure the files aren't queued until manually added
                    previewsContainer: "#previews"
                });

                dropZone.on("queuecomplete", function (progress) {
                    $("#itemFilesTableContainer").removeClass('drag-over');
                    vm.showFileDropzone = false;
                    dropZone.removeAllFiles(true);
                    $scope.$apply();
                    $rootScope.showSuccessMessage("Files saved successfully");
                    loadFiles();
                });

                $("#itemFilesTableContainer").on('dragover', handleDragEnter);
                $("#itemFilesTableContainer").on('dragleave', handleDragLeave);
                $("#itemFilesTableContainer").on('drop', handleDragLeave);
            }

            function selectFiles() {
                $('#itemFiles')[0].click();

            }


            (function () {

                $scope.$on('app.item.tabActivated', function (event, data) {
                    if (data.tabId == 'details.files') {
                        loadFiles();

                        $timeout(function () {
                            initFilesTableDropzone();
                        }, 1000);
                    }
                });
                $scope.$on('app.item.loadFiles', function (event, data) {
                    loadFilesByFileName(data.name);
                });
            })();
        }
    }
);