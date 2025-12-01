define(['app/assets/bower_components/cassini-platform/app/desktop/modules/help/help.module',
        'dropzone',
        'app/assets/bower_components/cassini-platform/app/shared/services/security/loginService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/commonService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/feedbackService'],


    function (module) {


        module.controller('NewFeedbackController', NewFeedbackController);

        function NewFeedbackController($scope, $rootScope, $timeout, $translate, $state, $stateParams, $cookies, LoginService, CommonService, FeedbackService) {

            var parsed = angular.element("<div></div>");

            $rootScope.viewInfo.icon = "fa flaticon-deadlines";
            $rootScope.viewInfo.title = parsed.html($translate.instant("FEEDBACK")).html();


            var vm = this;

            vm.valid = true;
            vm.error = "";
            vm.customerProductMappingData = null;

            vm.createFeedback = createFeedback;
            vm.cancelFeedback = cancelFeedback;
            vm.selectFeedBackFiles = selectFeedBackFiles;
            vm.template = false;
            vm.newFeedback = {
                summary: null,
                description: null,
                notification_list: "",
                assigned_to_id: "",
                custom_fields: null
            };

            vm.feedback = null;

            var fileDropZone = null;

            var titleValidation = $translate.instant("TITLE_VALIDATION");

            function validate() {
                vm.valid = true;
                if (vm.newFeedback.summary == null) {
                    vm.valid = false;
                    $rootScope.showWarningMessage(titleValidation);
                }
                return vm.valid;
            }

            var feedBackSuccessMessage = parsed.html($translate.instant("FEEDBACK_SUCCESS_MESSAGE")).html();

            $scope.$on('app.help.createNewFeedback', function () {
                createFeedback();
            });

            function createFeedback() {
                validate();
                if (vm.valid) {
                    vm.processing = true;

                    var customFields = {
                        "Ticket Type": "External",
                        "Customer ID": vm.customerProductMappingData["customerId"],
                        "Customer Name": vm.customerProductMappingData["customerName"],
                        "Product ID": vm.customerProductMappingData["productId"],
                        "Product Name": vm.customerProductMappingData["productName"],
                        "Reported By Customer": $application.login.person.firstName
                    };
                    vm.newFeedback.custom_fields = customFields;
                    vm.newFeedback.assigned_to_id = vm.customerProductMappingData["assignedToId"];
                    FeedbackService.createFeedback(vm.newFeedback).then(
                        function (data) {
                            vm.feedback = data;
                            if (fileDropZone.files.length > 0) {
                                var url = "api/common/feedback/" + vm.feedback.id + "/attachment";
                                fileDropZone.options.url = url;
                                fileDropZone.enqueueFiles(fileDropZone.getFilesWithStatus(Dropzone.ADDED));
                            }
                            else {
                                vm.processing = false;
                                $timeout(function(){
                                    $rootScope.showSuccessMessage(feedBackSuccessMessage);
                                    $state.go('app.home');
                                }, 500);
                            }
                        }
                    )
                }
            }

            function cancelFeedback() {
                window.history.back();
            }


            function loadMappingsData() {
                FeedbackService.getFeedbackMappings().then(
                    function (data) {
                        var tenant = $application.session.tenantId;
                        vm.customerProductMappingData = data[tenant];
                    }
                );
            }

            function selectFeedBackFiles() {
                $("#attachmentsForm")[0].click();
            }

            function initDropzone(previewTemplate) {
                var containerId = "#attachmentsContainer";
                var formId = "#attachmentsForm";
                var dropZoneId = "#attachmentsDropZone";

                $(containerId).on('dragover', handleDragEnter);
                $(containerId).on('dragleave', handleDragLeave);
                $(containerId).on('drop', handleDragLeave);

                /* var previewNode = document.querySelector("#template");
                 console.log(previewNode);
                 previewNode.id = "";
                 var previewTemplate = previewNode.parentNode.innerHTML;
                 previewNode.parentNode.removeChild(previewNode);*/
                var totalFiles = 0,
                    completeFiles = 0;

                var myDropzone = new Dropzone(document.querySelector(formId), { // Make the whole body a dropzone
                    url: "/target", // Set the url
                    thumbnailWidth: 80,
                    thumbnailHeight: 80,
                    parallelUploads: 20,
                    previewTemplate: previewTemplate,
                    autoQueue: false, // Make sure the files aren't queued until manually added
                    previewsContainer: "#previews",
                    success: function (file, response) {
                        if (file != undefined) {
                            myDropzone.on("queuecomplete", function (progress) {
                                $("#total-progress").hide();
                                $("#attachmentsForm").removeClass('drag-over');
                                myDropzone.removeAllFiles(true);
                                $scope.$apply();
                                vm.processing = false;
                                $rootScope.showSuccessMessage(feedBackSuccessMessage);
                            });
                        }
                    },
                    error: function (file, response) {
                        $rootScope.showErrorMessage(response.message);
                        myDropzone.removeFile(file);
                        $scope.$apply();
                    }
                });

                myDropzone.on("addedfile", function (file) {
                    // Hookup the start button
                    totalFiles += 1;
                });

                myDropzone.on("removedfile", function (file) {
                    totalFiles -= 1;
                    if (myDropzone.files.length === 0) {
                        $(dropZoneId).show();
                    }
                });

                myDropzone.on("complete", function (file) {
                    completeFiles += 1;
                    if (completeFiles === totalFiles) {
                        $state.go('app.home');
                    }
                });

                myDropzone.on("uploadprogress", function (file, progress, bytesSent) {
                    if (file.previewElement) {
                        var progressElement = file.previewElement.querySelector("[data-dz-uploadprogress]");
                        progressElement.style.width = progress + "%";
                    }
                });

                myDropzone.on("sending", function (file) {

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
                //if ($application.homeLoaded == true) {
                    loadMappingsData();
                    $timeout(function () {
                        vm.template = true;
                    }, 1500);
                    $timeout(function () {
                        var previewTemplate = $("#template").parent().html();
                        $("#template").remove();
                        initDropzone(previewTemplate);
                    }, 1502);


                //}
            })();
        }
    }
);