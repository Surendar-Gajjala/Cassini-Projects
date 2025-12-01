define(
    [
        'app/desktop/modules/media/media.module',
        'dropzone',
        'app/shared/services/projectService',
        'app/desktop/modules/media/picturesController',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/attachmentService',
        'app/assets/bower_components/cassini-platform/app/desktop/directives/commonDirectives'

    ],
    function ($app) {
        $app.controller('MediaController', MediaController);


        function MediaController ($scope, $rootScope, $timeout, $interval, $state, $cookies, $uibModal,
                    ProjectService, AttachmentService) {
            $rootScope.viewInfo.icon = "fa fa-image";
            $rootScope.viewInfo.title = "Media";

            var vm = this;
            vm.mode = "photos";

            var fileDropZone = null;

            vm.setMode = setMode;
            vm.downloadFile = downloadFile;
            vm.showPicturesDialog = showPicturesDialog;


            var pictures = [];


            addImages('d-day', 25, '.jpg');
            addImages('miscellaneous', 10, '.jpeg');
            addImages('ni-distribution', 4, '.jpg');
            addImages('drm-conference', 56, '.jpg');
            addImages('staff-conference', 169, '.jpg');

            vm.chunks = pictures.chunk(5);

            function addImages(label, count, ext) {
                for(var i=0; i<count; i++) {
                    var index = i+1;
                    var img = "app/assets/images/bzarri/" + label + "/" + label + "-" + index + ext;
                    pictures.push(img);
                }
            }

            function setMode(mode) {
                vm.mode = mode;
            }

            function initDropzone() {
                var containerId = "#attachmentsContainer";
                var formId = "#attachmentsForm";
                var dropZoneId = "#attachmentsDropZone";

                $(containerId).on('dragover', handleDragEnter);
                $(containerId).on('dragleave', handleDragLeave);
                $(containerId).on('drop', handleDragLeave);

                var previewNode = document.querySelector("#template");
                previewNode.id = "";
                var previewTemplate = previewNode.parentNode.innerHTML;
                previewNode.parentNode.removeChild(previewNode);

                var myDropzone = new Dropzone(document.querySelector(formId), { // Make the whole body a dropzone
                    url: "/target", // Set the url
                    thumbnailWidth: 80,
                    thumbnailHeight: 80,
                    parallelUploads: 20,
                    previewTemplate: previewTemplate,
                    autoQueue: false, // Make sure the files aren't queued until manually added
                    previewsContainer: null, // Define the container to display the previews
                    clickable: ".fileinput-button" // Define the element that should be used as click trigger to select files.
                });

                myDropzone.on("addedfile", function (file) {
                    // Hookup the start button
                    $(dropZoneId).hide();
                });

                myDropzone.on("removedfile", function (file) {
                    if (myDropzone.files.length === 0) {
                        $(dropZoneId).show();
                    }
                });

                myDropzone.on("sending", function (file) {

                });

                // Hide the total progress bar when nothing's uploading anymore
                myDropzone.on("queuecomplete", function (progress) {
                    $state.go('app.pm.project.taskdetails', {taskId: vm.task.id});
                });


                fileDropZone = myDropzone;

                function handleDragEnter(e) {
                    this.classList.add('drag-over');
                }

                function handleDragLeave(e) {
                    this.classList.remove('drag-over');
                }
            }


            function downloadFile(path) {
                launchUrl(path);
            }

            function showPicturesDialog(picture) {
                var pictureIndex = pictures.indexOf(picture);
                var modalInstance = $uibModal.open({
                    animation: true,
                    templateUrl: 'app/desktop/modules/media/picturesDialog.jsp',
                    controller: 'PicturesController as picturesVm',
                    size: 'lg',
                    resolve: {
                        pictures: function() {
                            return pictures;
                        },
                        pictureIndex: function() {
                            return pictureIndex;
                        }
                    }
                });
                modalInstance.result.then(
                    function (result) {
                    }
                );
            }


            $scope.$on('$viewContentLoaded', function(){

            });
        }
    }
);