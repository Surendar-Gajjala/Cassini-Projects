  define(['app/app.modules',
            'dropzone',            
            'app/components/help/feedback/feedbackFactory'],


      function($app){


          $app.controller('FeedbackController',
        		  [ '$scope', '$rootScope', '$timeout', '$state', '$stateParams', '$cookies','FeedbackService',

          function ($scope, $rootScope, $timeout, $state, $stateParams, $cookies,FeedbackService) {
            $rootScope.iconClass = "flaticon-deadlines";
            $rootScope.viewTitle = " Feedback";


            var vm = this;

            vm.valid = true;
            vm.error = "";

            vm.createFeedback = createFeedback;

              vm.newFeedback = {
                summary: null,
                description: null,
                  notification_list:"",
                  assigned_to_id:"aWkuQCMyOr5zqcdmr6CpXy",
                  custom_fields:{Ticket_Type:"External",Customer_ID:"DPPL",Customer_Name:"Dachepalli Book Publishers Pvt. Ltd",Product_ID:"Cassini.ERP",Product_Name:"Cassini.ERP v1.0",Reported_By_Customer:$app.login.person.firstName}
                 };

            vm.feedback = null;

            var fileDropZone = null;




            function validate() {
                vm.valid = true;

                if(vm.newFeedback.summary === null) {
                    vm.valid = false;
                    vm.error = "Title cannot be empty";
                }

                return vm.valid;
            }

            function createFeedback() {
                if(validate() == true) {
                    FeedbackService.createFeedback(vm.newFeedback).then(
                        function(data) {
                            vm.feedback = data;
                            $rootScope.showSuccessMessage("Feedback saved successfully");
                            if(fileDropZone.files.length > 0) {
                                fileDropZone.options.url = "api/common/feedback/"+vm.feedback.id+"/attachment";
                                fileDropZone.enqueueFiles(fileDropZone.getFilesWithStatus(Dropzone.ADDED));
                            }
                            window.history.back();
                        }
                    )
                }
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
                    $state.go('app.help');
                });


                fileDropZone = myDropzone;

                function handleDragEnter(e) {
                    this.classList.add('drag-over');
                }

                function handleDragLeave(e) {
                    this.classList.remove('drag-over');
                }
            }

            (function () {
               // if ($application.homeLoaded == true) {
                    initDropzone();
               // }
            })();
        }
        		  ]
);
  }
  );
          