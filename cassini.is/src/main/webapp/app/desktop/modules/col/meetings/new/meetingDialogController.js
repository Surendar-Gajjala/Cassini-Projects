define(['app/desktop/modules/col/col.module',
        'bootstrap-timepicker',
        'bootstrap3-wysihtml5',
        'dropzone',
        'app/shared/services/common/meetingService',
        'app/assets/bower_components/cassini-platform/app/shared/services/security/loginService'
    ],
    function (module) {
        module.controller('MeetingDialogController', MeetingDialogController);

        function MeetingDialogController($scope, $rootScope, $timeout, $state, $stateParams, $cookies, MeetingService, LoginService) {

            $rootScope.viewInfo.icon = "fa fa-calendar";
            var vm = this;

            var projectId = $stateParams.projectId;
            vm.backToFiles = backToFiles;
            vm.showDropzone = false;
            var meetingId = null;
            var filesLength = 0;

            vm.valid = true;
            vm.newMeeting = {
                project: projectId,
                title: null,
                attendees: [],
                startDate: null,
                startTime: null,
                endDate: null,
                endTime: null,
                description: null
            };

            vm.persons = [];
            vm.pageable = {
                page: 1,
                size: 10,
                sort: {
                    label: "loginTime",
                    field: "loginTime",
                    order: "desc"
                }
            };

            var fileDropZone = null;

            function backToFiles() {
                vm.showDropzone = false;
            }

            function validate() {
                vm.valid = true;
                if (vm.newMeeting.title == null || vm.newMeeting.title == "") {
                    vm.valid = false;
                    $rootScope.showErrorMessage("Meeting Title cannot be empty");
                }
                else if (vm.newMeeting.description == null || vm.newMeeting.description == "") {
                    vm.valid = false;
                    $rootScope.showErrorMessage("Meeting Description cannot be empty");
                }
                else if (vm.newMeeting.startDate == null || vm.newMeeting.startDate == "" ||
                    vm.newMeeting.startTime == null || vm.newMeeting.startTime == "") {
                    vm.valid = false;
                    $rootScope.showErrorMessage("Meeting Start Date or Time cannot be empty");
                }
                else if (vm.newMeeting.endDate == null || vm.newMeeting.endDate == "" ||
                    vm.newMeeting.endTime == null || vm.newMeeting.endTime == "") {
                    vm.valid = false;
                    $rootScope.showErrorMessage("Meeting End Date or Time cannot be empty");
                }
                else if (moment(vm.newMeeting.startDate, "DD/MM/YYYY").isAfter(moment(vm.newMeeting.endDate, "DD/MM/YYYY"))) {
                    vm.valid = false;
                    $rootScope.showErrorMessage("Meeting Start Date cannot be after the End Date");
                }
                else if ((moment(vm.newMeeting.startDate, "DD/MM/YYYY").isSame(moment(vm.newMeeting.endDate, "DD/MM/YYYY")))) {
                    var sm = moment.utc(vm.newMeeting.startTime);
                    var em = moment.utc(vm.newMeeting.endTime);
                    if (sm.isAfter(em)) {
                        vm.valid = false;
                        $rootScope.showErrorMessage("Meeting Start Time cannot be after the End Time");
                    }
                }

                else {
                    var m = moment(vm.newMeeting.startDate, "DD/MM/YYYY");
                    var sel = moment({
                        year: m.year(),
                        month: m.month(),
                        day: m.date()
                    });
                    var today = moment();
                    var current = moment({
                        year: today.year(),
                        month: today.month(),
                        day: today.date()
                    });
                    if (sel.isBefore(current)) {
                        vm.valid = false;
                        $rootScope.showErrorMessage("Meeting Start Date cannot be in the past");
                    }
                }
                return vm.valid;
            }

            function initDropzone() {
                $("#filesDropzoneContainer").on('dragover', handleDragEnter);
                $("#filesDropzoneContainer").on('dragleave', handleDragLeave);
                $("#filesDropzoneContainer").on('drop', handleDragLeave);

                var previewNode = $("#template");
                var previewTemplate = previewNode.parent().html();
                previewNode.remove();

                var myDropzone = new Dropzone(document.querySelector("#dropzoneForm"), { // Make the whole body a dropzone
                    url: "api/col/attachments?objectType=MEETING&objectId=",
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
                    vm.showDropzone = false;
                });

                myDropzone.on("complete", function (progress) {
                    createMail();
                });
                filesLength = myDropzone.files.length;
                fileDropZone = myDropzone;

            }

            function createMail() {
                MeetingService.createMail(projectId, meetingId).then(
                    function (data) {

                    }
                )
            }

            function handleDragEnter(e) {
                this.classList.add('drag-over');
            }

            function handleDragLeave(e) {
                this.classList.remove('drag-over');
            }

            function create() {
                if (validate() == true) {
                    vm.newMeeting.startTime = moment(vm.newMeeting.startTime).format('hh:mm A');
                    vm.newMeeting.endTime = moment(vm.newMeeting.endTime).format('hh:mm A');
                    MeetingService.createMeeting(projectId, vm.newMeeting).then(
                        function (data) {
                            meetingId = data.id;
                            fileDropZone.options.url = "api/col/attachments?objectType=MEETING&objectId=" + data.id;
                            fileDropZone.enqueueFiles(fileDropZone.getFilesWithStatus(Dropzone.ADDED));
                            vm.newMeeting = {
                                project: projectId,
                                title: null,
                                attendees: [],
                                startDate: null,
                                startTime: null,
                                endDate: null,
                                endTime: null,
                                description: null
                            };
                            if (filesLength == 0) {
                                createMail();
                            }
                            $scope.callback(data);
                            $rootScope.hideSidePanel('right');
                            $rootScope.showSuccessMessage("Meeting created successfully");

                        }
                    )
                }
            }

            function loadPersons() {
                LoginService.getLogins(vm.pageable).then(
                    function (data) {
                        angular.forEach(data.content, function (login) {
                            vm.persons.push(login.person);
                        })
                    }
                )
            }

            (function () {
                if ($application.homeLoaded == true) {
                    loadPersons();
                    $timeout(function () {
                        vm.showDropzone = true;
                        initDropzone();
                    }, 2000);
                    $rootScope.$on('app.create', create);
                }
            })();
        }
    }
);