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
        module.controller('FileReplaceController', FileReplaceController);

        function FileReplaceController($scope, $rootScope, $timeout, $state, $stateParams, $cookies, $translate,
                                       CommonService, ItemTypeService, ItemService, ItemFileService, DialogService) {
            var vm = this;
            var parsed = angular.element("<div></div>");

            vm.loading = true;
            vm.files = [];
            vm.showDropzone = false;
            vm.showFileDropzone = false;

            vm.file = $scope.data.replaceFile;
            vm.files = $scope.data.files;
            vm.itemId = $stateParams.itemId;
            var fileRenameError = parsed.html($translate.instant("FILE_RENAME_ERROR")).html();

            function handleDragEnter(e) {
                $("#itemFilesTableContainer")[0].classList.add('drag-over');
                vm.showFileDropzone = true;

            }

            function handleDragLeave(e) {
                $("#itemFilesTableContainer")[0].classList.remove('drag-over');
                vm.showFileDropzone = false;
            }

            function initFileDrop() {
                $("#itemFilesTable").on('dragover', function () {
                    vm.showFileDropzone = true;
                    $scope.$apply();
                });
                $("#itemFilesTable").on('dragleave', function () {
                    vm.showFileDropzone = false;
                    $scope.$apply();
                });
                $("#itemFilesTable").on('drop', function () {

                });
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

                    },
                    function (error) {
                        console.log(error);
                    });
            };

            var filelimit = parsed.html($translate.instant("FILE_LIMIT")).html();
            var fileType = $translate.instant("FILE_TYPE");
            var fileReplaceMsg = parsed.html($translate.instant("FILE_REPLACE_MSG")).html();
            var eFiles = "";

            vm.initFilesTableDropzone = initFilesTableDropzone;
            function initFilesTableDropzone() {
                vm.fileConfig.error = false;
                var previewNode = $("#replaceTemplate");
                /*var previewTemplate = previewNode.parent().html();*/
                previewNode.remove();

                var dropZone = new Dropzone(document.querySelector('#itemReplaceFiles'), { // Make the whole body a dropzone
                    url: "api/plm/items/" + vm.itemId + "/files/replaceFile/" + vm.file.id, // Set the url
                    thumbnailWidth: 80,
                    thumbnailHeight: 80,
                    timeout: 500000,
                    /*previewTemplate: previewTemplate,*/
                    autoQueue: true,// Make sure the files aren't queued until manually added
                    parallelUploads: 1,//Files are sent to the server one by one
                    previewsContainer: "#replacePreviews",
                    maxFilesize: vm.fileConfig.fileSize,
                    maxFiles: 1,
                    autoDiscover: false,
                    autoProcessQueue: false
                });

                /*  // Update the total progress bar
                 dropZone.on("totaluploadprogress", function (progress) {
                 /!*     document.querySelector("#total-progress .progress-bar").style.width = progress + "%";*!/
                 var a = $("#total-progress .progress-bar");
                 a = progress + "%"
                 });*/

                dropZone.on("success", function (file, response) {
                    if (response.length == 0) {
                        eFiles = file.name;
                        vm.fileConfig.fileTypeError = true;
                        vm.fileConfig.error = true;
                    }
                });

                dropZone.on("addedfile", function(file) {
                    vm.itemRename = false;
                    vm.fileConfig.error = false;
                    var uploadedfile = file.name;
                    if(vm.file.name != uploadedfile){
                        angular.forEach(vm.files, function(file){
                            if(file.fileType == 'FILE' && file.parent == vm.file.parent) {
                                if(file.name.toLowerCase() == uploadedfile.toLowerCase() && !vm.itemRename) {
                                    vm.itemRename = true;
                                    vm.fileConfig.filenameError = true;
                                    vm.fileConfig.error = true;
                                }
                            }
                        });
                    }
                    if(vm.fileConfig.error == false) {
                        $timeout(function(){
                            dropZone.processQueue();
                        }, 1000);
                    } else {
                        vm.showFileDropzone = false;
                        dropZone.removeAllFiles(true);
                        $scope.$apply();
                        $rootScope.showErrorMessage(fileRenameError);
                        $rootScope.hideSidePanel();
                        $scope.callback();
                    }
                });

                dropZone.on("error", function (file, response) {
                    vm.fileConfig.fileSizeError = true;
                    vm.fileConfig.error = true;
                });

                dropZone.on("queuecomplete", function (progress) {
                    $("#total-progress").hide();
                    $("#itemFilesTableContainer").removeClass('drag-over');
                    vm.showFileDropzone = false;
                    dropZone.removeAllFiles(true);
                    $scope.$apply();
                    if (vm.fileConfig.error) {
                        if (vm.fileConfig.fileTypeError == true && vm.fileConfig.fileSizeError == true) {
                            $rootScope.showErrorMessage(fileType + eFiles + " and " + filelimit + vm.fileConfig.fileSize + " MB");
                        } else if (vm.fileConfig.fileTypeError == true && vm.fileConfig.fileSizeError == false) {
                            $rootScope.showErrorMessage(fileType + eFiles);
                        } else if (vm.fileConfig.fileTypeError == false && vm.fileConfig.fileSizeError == true) {
                            $rootScope.showErrorMessage(filelimit + vm.fileConfig.fileSize + " MB");
                        }
                    } else {
                        $rootScope.showSuccessMessage(fileReplaceMsg);
                    }
                    vm.fileConfig.error = false;
                    eFiles = "";
                    vm.fileConfig.fileTypeError = false;
                    vm.fileConfig.fileSizeError = false;
                    $rootScope.hideSidePanel();
                    $scope.callback();
                });

                /* dropZone.on("error", function (file, message) {
                 $rootScope.showWarningMessage("We uploaded file size is exceeded 1GB");
                 dropZone.removeFile(file);
                 });*/

                $("#itemFilesTableContainer").on('dragover', handleDragEnter);
                $("#itemFilesTableContainer").on('dragleave', handleDragLeave);
                $("#itemFilesTableContainer").on('drop', handleDragLeave);
            }

            vm.selectFiles = selectFiles;
            function selectFiles() {
                $('#itemReplaceFiles')[0].click();
            }

            (function () {
                loadFileConfig();
                $timeout(function () {
                    initFilesTableDropzone();
                    Dropzone.options.url = null;
                }, 1000);

            })();

        }
    }
);