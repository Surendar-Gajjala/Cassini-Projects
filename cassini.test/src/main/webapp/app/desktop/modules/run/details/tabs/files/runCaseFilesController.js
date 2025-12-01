define([
        'app/desktop/modules/run/run.module',
        'dropzone',
        'app/shared/services/testCaseService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/commonService',
        'app/assets/bower_components/cassini-platform/app/desktop/directives/commonDirectives'

    ],
    function (module) {
        module.controller('RunCaseFilesController', RunCaseFilesController);

        function RunCaseFilesController($scope, $rootScope, $timeout, $state, $translate, $stateParams, $cookies, CommonService, TestCaseService, DialogService) {

            var vm = this;
            var parsed = angular.element("<div></div>");

            vm.loading = true;
            vm.mfrFiles = [];
            vm.showDropzone = false;
            vm.deleteFile = deleteFile;
            vm.backToFiles = backToFiles;
            vm.fileSizeToString = fileSizeToString;
            vm.selectFile = selectFile;
            vm.downloadFile = downloadFile;
            var myDropzone = null;
            var caseId = $rootScope.selectedRunCaseId;

            var fileDropZone = null;


            function backToFiles() {
                vm.showDropzone = false;
            }

            function deleteFile(file) {
                var options = {
                    title: 'Delete File',
                    message: 'Are you sure you want to delete this file?',
                    okButtonClass: 'btn-danger'
                };
                DialogService.confirm(options, function (yes) {
                    if (yes == true) {
                        TestCaseService.deleteRunCaseFile(caseId, file.id).then(
                            function (data) {
                                var index = $rootScope.runCaseFiles.indexOf(file);
                                $rootScope.runCaseFiles.splice(index, 1);
                                $rootScope.loadRunCaseFiles();
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

                myDropzone = new Dropzone(document.querySelector("#caseFiles"), { // Make the whole body a dropzone
                    url: "api/test/run/case/" + $rootScope.selectedRunCaseId + "/files", // Set the url
                    thumbnailHeight: 80,
                    parallelUploads: 20,
                    maxFilesize: 1000,
                    previewTemplate: previewTemplate,
                    autoQueue: true, // Make sure the files aren't queued until manually added
                    previewsContainer: "#previews"
                });

                myDropzone.on("queuecomplete", function (progress) {
                    $("#itemFilesTableContainer").removeClass('drag-over');
                    vm.showFileDropzone = false;
                    myDropzone.removeAllFiles(true);
                    $scope.$apply();
                    $rootScope.showSuccessMessage("File(s) added successfully");
                    $rootScope.loadRunCaseFiles();
                });

                $("#itemFilesTableContainer").on('dragover', handleDragEnter);
                $("#itemFilesTableContainer").on('dragleave', handleDragLeave);
                $("#itemFilesTableContainer").on('drop', handleDragLeave);

                fileDropZone = myDropzone;
            }

            function selectFile() {
                $('#caseFiles')[0].click();
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
                var url = "{0}//{1}/api/test/run/case/{2}/files/{3}/download".
                    format(window.location.protocol, window.location.host,
                    file.testRunCase, file.id);
                launchUrl(url);
            }


            (function () {
                if ($application.homeLoaded == true) {
                    $scope.$on('app.run.tabactivated', function (event, data) {
                        if (data.tabId == 'details.files') {
                            /* loadFiles();*/
                            if (myDropzone == null) {
                                initFilesTableDropzone();
                            } else {
                                myDropzone.options.url = "api/test/run/case/" + $rootScope.selectedRunCaseId + "/files";
                            }
                        }
                    });
                }
            })();

        }
    }
);
