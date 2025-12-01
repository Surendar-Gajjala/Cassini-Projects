define(['app/desktop/modules/col/col.module',
        'bootstrap-timepicker',
        'bootstrap3-wysihtml5',
        'dropzone',
        'app/shared/services/common/meetingService',
        'app/assets/bower_components/cassini-platform/app/shared/services/security/loginService'
    ],
    function (module) {
        module.controller('MeetingEditController', MeetingEditController);

        function MeetingEditController($scope, $rootScope, $timeout, $state, $stateParams, $cookies,
                                       MeetingService, AttachmentService, CommonService, LoginService) {

            var vm = this;
            vm.meeting = null;
            var projectId = $stateParams.projectId;
            vm.meetingId = $stateParams.meetingId;
            vm.removePerson = removePerson;
            vm.selectPerson = selectPersons;
            var fileDropZone = null;

            vm.pageable = {
                page: 1,
                size: 10,
                sort: {
                    label: "loginTime",
                    field: "loginTime",
                    order: "desc"
                }
            };

            vm.persons = [];

            function loadMeetingDetails() {
                vm.loading = true;
                MeetingService.getMeeting(projectId, $stateParams.meetingId).then(
                    function (data) {
                        vm.meeting = data;
                        vm.meeting.startTime = moment(vm.meeting.startTime, "hh:mm A").toDate();
                        vm.meeting.endTime = moment(vm.meeting.endTime, "hh:mm A").toDate();
                        return CommonService.getPerson(data.createdBy);
                    }
                ).then(
                    function (data) {
                        vm.meeting.createdByPerson = data;
                        return AttachmentService.getAttachments('MEETING', vm.meetingId);
                    }
                ).then(
                    function (data) {
                        vm.meeting.attachments = data;
                        vm.loading = false;

                    }
                );
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

            function validate() {
                vm.valid = true;
                if (vm.meeting.title == null || vm.meeting.title == "") {
                    vm.valid = false;
                    $rootScope.showErrorMessage("Meeting Title cannot be empty");
                }
                else if (vm.meeting.description == null || vm.meeting.description == "") {
                    vm.valid = false;
                    $rootScope.showErrorMessage("Meeting Description cannot be empty");
                }
                else if (vm.meeting.startDate == null || vm.meeting.startDate == "" ||
                    vm.meeting.startTime == null || vm.meeting.startTime == "") {
                    vm.valid = false;
                    $rootScope.showErrorMessage("Meeting Start Date or Time cannot be empty");
                }
                else if (vm.meeting.endDate == null || vm.meeting.endDate == "" ||
                    vm.meeting.endTime == null || vm.meeting.endTime == "") {
                    vm.valid = false;
                    $rootScope.showErrorMessage("Meeting End Date or Time cannot be empty");
                }
                else if (moment(vm.meeting.startDate, "DD/MM/YYYY").isAfter(moment(vm.meeting.endDate, "DD/MM/YYYY"))) {
                    vm.valid = false;
                    $rootScope.showErrorMessage("Meeting Start Date cannot be after the End Date");
                }
                else {
                    var m = moment(vm.meeting.startDate, "DD/MM/YYYY");
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
                    else {
                        /*var sm = moment(vm.meeting.startTime, "HH:mm:ss");
                         var em = moment(vm.meeting.endTime, "HH:mm:ss");*/
                        var sm = moment.utc(vm.meeting.startTime);
                        var em = moment.utc(vm.meeting.endTime);

                        if ((moment(vm.meeting.startDate, "DD/MM/YYYY").isSame(moment(vm.meeting.endDate, "DD/MM/YYYY")))) {
                            if (sm.isAfter(em)) {
                                vm.valid = false;
                                $rootScope.showErrorMessage("Meeting Start Time cannot be after the End Time");
                            }
                        }
                    }
                }
                return vm.valid;
            }

            function save() {
                if (validate() == true) {
                    vm.meeting.startTime = moment(vm.meeting.startTime).format('hh:mm A');
                    vm.meeting.endTime = moment(vm.meeting.endTime).format('hh:mm A');
                    $rootScope.showBusyIndicator();
                    MeetingService.updateMeeting(projectId, vm.meeting).then(
                        function (data) {
                            if (fileDropZone.files.length > 0) {
                                var url = "api/col/attachments?objectType={0}&objectId={1}".
                                    format('MEETING', data.id);
                                fileDropZone.options.url = url;
                                fileDropZone.enqueueFiles(fileDropZone.getFilesWithStatus(Dropzone.ADDED));
                            }
                            $rootScope.hideBusyIndicator();
                            $scope.callback(data);
                            $rootScope.hideSidePanel('right');
                            $rootScope.showSuccessMessage("Meeting updated successfully");
                        },
                        function (error) {
                            $rootScope.hideBusyIndicator();
                            $rootScope.showErrorMessage(error.message);
                        }
                    )
                }
            }

            function selectPersons(person) {
                vm.meeting.attendees = person;
            }

            function removePerson(personId) {
                MeetingService.deleteAttendee(projectId, $stateParams.meetingId, personId).then(
                    function (data) {
                        $rootScope.showSuccessMessage("Attendee deleted successfully");
                        $rootScope.$broadcast('app.meeting.details');
                    }
                )
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
                    $state.go('app.pm.project.meetings');
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
                loadPersons();
                loadMeetingDetails();
                $timeout(function () {
                    initDropzone();
                }, 2000);
                $scope.$on('edit.save', save);
            })();

        }
    }
);

