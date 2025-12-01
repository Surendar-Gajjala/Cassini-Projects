/**
 * Created by swapna on 06/11/18.
 */

define(['app/desktop/modules/tm/tm.module',
        'dropzone',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/mediaService',
        'app/assets/bower_components/cassini-platform/app/desktop/modules/dialogs/dialogService'
    ],
    function (module) {
        module.controller('TaskMediaController', TaskMediaController);

        function TaskMediaController($scope, $rootScope, $timeout, $state, $stateParams, $sce,
                                     DialogService, MediaService) {

            var vm = this;

            vm.media = [];
            vm.taskMedia = [[]];
            vm.task = $stateParams.taskId;
            vm.project = $stateParams.projectId;
            vm.files = [];
            var fileDropZone = null;
            var angle = 0;
            $scope.index = 0;
            vm.show = false;
            $scope.next = next;
            $scope.prev = prev;
            $scope.setTurtle = setTurtle;
            vm.selectFiles = selectFiles;
            $scope.print = print;
            $scope.rotateImage = rotateImage;
            $scope.deleteFile = deleteFile;

            var location = {
                latitude: "",
                longitude: "",
                uploadFrom: null
            };

            function setTurtle(file) {
                vm.selectedImage = file;
                $scope.index = vm.files.indexOf(file);
                var modal = document.getElementById('myModal2');
                var modalImg = document.getElementById('img03');

                modal.style.display = "block";
                modalImg.src = "/api/col/media/" + file.id + "/bytes?" + new Date().getTime();
                modalImg.style.height = "500px";
                modalImg.style.width = "510px";
                modalImg.style.transform = 'rotate(0deg)';

                var span = document.getElementsByClassName("closeImage")[0];

                span.onclick = function () {
                    modal.style.display = "none";
                }
            }

            function next() {
                $scope.index++;
                if ($scope.index == vm.files.length) {
                    $scope.index = 0;
                    setTurtle(vm.files[$scope.index]);
                }
                else {
                    setTurtle(vm.files[$scope.index]);
                }
            }

            function prev() {
                if ($scope.index == 0) {
                    $scope.index = vm.files.length - 1;
                    setTurtle(vm.files[$scope.index]);
                }
                else {
                    $scope.index--;
                    setTurtle(vm.files[$scope.index]);
                }
            }

            function uploadMedia() {
                document.getElementById("taskFile").onchange = function () {
                    var file = document.getElementById("taskFile");

                    if (vm.media = file.files) {
                        $rootScope.showBusyIndicator();
                        MediaService.uploadMedia($stateParams.taskId, vm.media).then(
                            function (data) {
                                document.getElementById("taskFile").value = "";
                                $rootScope.showSuccessMessage("File(s) uploaded successfully");
                                $rootScope.hideBusyIndicator();
                                getTaskMedia();

                            }, function (error) {
                                $rootScope.hideBusyIndicator();
                                $rootScope.showErrorMessage("Please add valid format Image/Video");
                                document.getElementById("taskFile").value = "";
                            }
                        )
                    }
                };

            }

            function getTaskMedia() {
                vm.loading = true;
                MediaService.getMediaByObjectId($stateParams.taskId).then(
                    function (data) {
                        var files = data;
                        vm.taskMedia = [[]];
                        var rows = Math.ceil(files.length / 4);
                        var count = -1;
                        for (var i = 0; i < rows; i++) {
                            vm.taskMedia[i] = [];

                            for (var j = 0; j < 4; j++) {
                                count++;
                                if (files[count] !== null && files[count] !== undefined) {
                                    if (files[count].mediaType == 'VIDEO') {
                                        var url = "/api/col/media/" + files[count].id + "/bytes";
                                        files[count].url = $sce.trustAsResourceUrl(url)
                                    }
                                    else {
                                        vm.files.push(files[count]);
                                    }
                                    vm.taskMedia[i][j] = files[count];
                                }
                            }
                        }
                        vm.loading = false;
                    }
                );
            }

            function getLocation() {

                if (!!navigator.geolocation) {
                    navigator.geolocation.getCurrentPosition(
                        function (position) {
                            location.latitude = position.coords.latitude;
                            location.longitude = position.coords.longitude;

                            var geocoder = new google.maps.Geocoder();
                            var latlng = new google.maps.LatLng(position.coords.latitude, position.coords.longitude);
                            var request = {
                                latLng: latlng
                            };
                            geocoder.geocode(request, function (data, status) {
                                if (status == google.maps.GeocoderStatus.OK) {
                                    if (data[0] != null) {
                                        location.uploadFrom = data[0].formatted_address;
                                    }
                                }
                            })
                        }, function (error) {

                        }
                    );
                }
            }

            function initDropzone() {

                var dropZone1 = new Dropzone(document.querySelector("#taskMediaDropzoneForm"), { // Make the whole body a dropzone
                    url: "api/col/media/object/" + $stateParams.taskId + "?latitude=" + location.latitude
                    + "&longitude=" + location.longitude + "&uploadFrom=" + location.uploadFrom, // Set the url
                    thumbnailWidth: 80,
                    thumbnailHeight: 80,
                    maxFilesize: 2000,
                    timeout: 500000,
                    autoQueue: true,// Make sure the files aren't queued until manually added
                    parallelUploads: 1,//Files are sent to the server one by one
                    acceptedFiles: ".JPEG, .jpeg, .JPG, .jpg, PNG, .png, .GIF, .gif, .mp4"
                });

                dropZone1.on("queuecomplete", function (progress) {
                    $("#total-progress").hide();
                    $("#taskMediaContainer").removeClass('drag-over');
                    vm.showFileDropzone = false;
                    dropZone1.removeAllFiles(true);
                    $scope.$apply();
                    $rootScope.showSuccessMessage("File(s) uploaded successfully");
                    getTaskMedia();
                    $rootScope.loadDetailsCount();
                });

                fileDropZone = dropZone1;
            }

            function selectFiles() {
                $('#taskMediaDropzoneForm')[0].click();
            }

            function print() {
                var prtContent = document.getElementById("taskImg");

                var printPreview = window.open("", "", "resizable=yes,scrollbars=yes,status=yes");

                printPreview.document.write(prtContent.innerHTML);
                printPreview.document.close();
                printPreview.focus();

                $timeout(function () {
                    printPreview.print();
                    printPreview.close();
                }, 500);

            }

            function rotateImage() {
                angle = angle + 90;
                var img = document.getElementById('img03');
                img.style.transform = 'rotate(' + angle + 'deg)';
                img.style.top = "50";
            }

            function deleteFile(file) {
                var options = {
                    title: 'Delete Media File',
                    message: 'Are you sure you want to delete this media file?',
                    okButtonClass: 'btn-danger'
                };
                DialogService.confirm(options, function (yes) {
                    if (yes == true) {
                        MediaService.deleteMediaById(file.id).then(
                            function (data) {
                                $rootScope.showSuccessMessage("File deleted successfully");
                                getTaskMedia();
                                $rootScope.loadDetailsCount();
                            }
                        )
                    }
                });
            }

            var isCtrl = false;
            $(document).keyup(function (e) {
                if (e.which == 17) isCtrl = false;
            }).keydown(function (e) {
                if (e.which == 17 || e.which == 224) isCtrl = true;
                if (e.which == 80 && isCtrl == true) {
                    print();
                }
            });

            (function () {
                if ($application.homeLoaded == true) {
                    $rootScope.$on('app.project.task.upload', uploadMedia);
                    $scope.$on('app.task.tabactivated', function (event, data) {
                        if (data.tabId == 'details.media') {
                            getLocation();
                            getTaskMedia();
                            $timeout(function () {
                                initDropzone();
                            }, 1000);
                        }
                    });
                }
            })();
        }
    }
);