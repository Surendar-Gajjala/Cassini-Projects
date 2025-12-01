/**
 * Created by swapna on 03/10/18.
 */
define(['app/desktop/modules/reports/reports.module',
        'dropzone',
        'app/shared/services/pm/project/projectService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/mediaService',
        'app/assets/bower_components/cassini-platform/app/desktop/modules/dialogs/dialogService'
    ],
    function (module) {
        module.controller('ProjectMediaController', ProjectMediaController);

        function ProjectMediaController($scope, $rootScope, $timeout, $state, $stateParams, $sce, $window,
                                        MediaService, ProjectService, DialogService) {

            var vm = this;

            $rootScope.viewInfo.icon = "fa fa-play-circle";
            $rootScope.viewInfo.title = "Media";

            vm.media = [];
            vm.projectMedia = [[]];
            vm.project = $stateParams.projectId;
            vm.files = [];
            $scope.index = 0;
            var fileDropZone = null;
            vm.show = false;
            var angle = 0;
            $scope.next = next;
            $scope.prev = prev;
            $scope.setTurtle = setTurtle;
            $scope.print = print;
            vm.selectFiles = selectFiles;
            $scope.rotateImage = rotateImage;
            $scope.deleteFile = deleteFile;
            var pageable = {
                page: 0,
                size: 10
            };

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
                modalImg.style.width = "750px";
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

            function getProjectMedia() {
                vm.loading = true;
                ProjectService.getAllProjectMedia($stateParams.projectId, pageable).then(
                    function (data) {
                        var files = data;
                        vm.projectMedia = [[]];
                        var rows = Math.ceil(files.length / 4);
                        var count = -1;
                        for (var i = 0; i < rows; i++) {
                            vm.projectMedia[i] = [];

                            for (var j = 0; j < 4; j++) {
                                count++;
                                if (files[count] !== null && files[count] !== undefined) {
                                    var url = "/api/col/media/" + files[count].id + "/bytes";
                                    files[count].url = $sce.trustAsResourceUrl(url);
                                    vm.files.push(files[count]);
                                    vm.projectMedia[i][j] = files[count];
                                }
                            }
                        }
                        vm.loading = false;
                    }
                );
            }

            function initDropzone() {

                var dropZone = new Dropzone(document.querySelector("#dropzoneForm"), { // Make the whole body a dropzone
                    url: "api/col/media/object/" + $stateParams.projectId + "?latitude=" + location.latitude
                    + "&longitude=" + location.longitude + "&uploadFrom=" + location.uploadFrom, // Set the url
                    thumbnailWidth: 80,
                    thumbnailHeight: 80,
                    maxFilesize: 2000,
                    timeout: 500000,
                    autoQueue: true,// Make sure the files aren't queued until manually added
                    parallelUploads: 1,//Files are sent to the server one by one
                    acceptedFiles: ".JPEG, .jpeg, .JPG, .jpg, PNG, .png, .GIF, .gif, .mp4"
                });

                dropZone.on("queuecomplete", function (progress) {
                    $("#total-progress").hide();
                    $("#mediaContainer").removeClass('drag-over');
                    vm.showFileDropzone = false;
                    dropZone.removeAllFiles(true);
                    $scope.$apply();
                    $rootScope.showSuccessMessage("File(s) uploaded successfully");
                    getProjectMedia();
                });

                fileDropZone = dropZone;
            }

            function selectFiles() {
                $('#dropzoneForm')[0].click();
            }

            function print() {
                var prtContent = document.getElementById("printImg");

                var printPreview = window.open("", "", "resizable=yes,scrollbars=yes,status=yes");

                printPreview.document.write(prtContent.innerHTML);
                printPreview.document.close();
                printPreview.focus();

                $timeout(function () {
                    printPreview.print();
                    printPreview.close();
                }, 500);

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

            function rotateImage() {
                angle = angle + 90;
                var img = document.getElementById('img03');
                img.style.transform = 'rotate(' + angle + 'deg)';
                img.style.top = "50";
            }

            function deleteFile(file) {
                var options = {
                    title: 'Delete Media file',
                    message: 'Are you sure you want to delete this Media file?',
                    okButtonClass: 'btn-danger'
                };
                DialogService.confirm(options, function (yes) {
                    if (yes == true) {
                        MediaService.deleteMediaById(file.id).then(
                            function (data) {
                                $rootScope.showSuccessMessage("File deleted successfully");
                                getProjectMedia();
                            })
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
                    getLocation();
                    getProjectMedia();
                    $timeout(function () {
                        initDropzone();
                    }, 1000)
                }
            })();
        }
    }
);