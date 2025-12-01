define(
    [
        'app/desktop/desktop.app',
        'dropzone'
    ],

    function (module) {
        module.directive('dropzone', function () {
            return {
                templateUrl: 'app/desktop/directives/dropzone/dropzoneTemplate.jsp',
                restrict: 'E',
                scope: {
                    url: '&',
                    onBack: '&',
                    onStart: '&',
                    onFinish: '&',
                    dropzoneSetter: '&'
                },

                link: function (scope, element, attrs) {
                    function initDropzone() {
                        $("#filesDropzoneContainer").on('dragover', handleDragEnter);
                        $("#filesDropzoneContainer").on('dragleave', handleDragLeave);
                        $("#filesDropzoneContainer").on('drop', handleDragLeave);

                        var previewNode = $("#template");
                        var previewTemplate = previewNode.parent().html();
                        previewNode.remove();

                        var myDropzone = new Dropzone(document.querySelector("#dropzoneForm"), { // Make the whole body a dropzone
                            url: scope.url,
                            thumbnailWidth: 80,
                            thumbnailHeight: 80,
                            parallelUploads: 20,
                            maxFilesize: 1000,
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
                            document.querySelector("#filesDropZone").style.display = "none";

                        });

                        myDropzone.on("removedfile", function (file) {
                            if (myDropzone.files.length === 0) {
                                document.querySelector("#filesDropZone").style.display = "table";
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
                            scope.onFinish();
                        });

                        // Setup the buttons for all transfers
                        // The "add files" button doesn't need to be setup because the config
                        // `clickable` has already been specified.
                        document.querySelector("#actions .start").onclick = function () {
                            if (scope.onStart() == true) {
                                myDropzone.enqueueFiles(myDropzone.getFilesWithStatus(Dropzone.ADDED));
                            }
                        };
                        document.querySelector("#actions .cancel").onclick = function () {
                            myDropzone.removeAllFiles(true);
                            document.querySelector("#filesDropZone").style.display = "table";
                        };

                        scope.dropzoneSetter({dropzone: myDropzone});
                    }

                    function handleDragEnter(e) {
                        this.classList.add('drag-over');
                    }

                    function handleDragLeave(e) {
                        this.classList.remove('drag-over');
                    }

                    (function () {
                        initDropzone();
                    })();
                }
            }
        });
    }
);