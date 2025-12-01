define(['app/desktop/modules/col/col.module',
        /* 'bootstrap3-wysihtml5',*/
        'app/shared/services/common/meetingService',
        'app/shared/services/common/attachmentService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/commonService',
        'app/desktop/modules/col/meetings/edit/meetingEditController'
    ],
    function (module) {
        module.controller('MeetingDetailsController', MeetingDetailsController);

        function MeetingDetailsController($scope, $rootScope, $timeout, $state, $stateParams, $cookies, $sce,
                                          MeetingService, DialogService, AttachmentService, CommonService) {
            $rootScope.viewInfo.icon = "fa fa-calendar";
            $rootScope.viewInfo.title = "Meeting Details";

            var vm = this;

            vm.loading = true;
            vm.meeting = null;
            vm.meetingId = $stateParams.meetingId;
            vm.projectId = $stateParams.projectId;

            vm.saveMeeting = saveMeeting;
            vm.downloadAttachment = downloadAttachment;
            vm.editMeetingDialog = editMeetingDialog;
            vm.back = back;
            vm.deleteMeeting = deleteMeeting;

            //var meetingMinutesEditor = null;

            function loadMeetingDetails() {
                vm.loading = true;
                MeetingService.getMeeting(vm.projectId, $stateParams.meetingId).then(
                    function (data) {
                        data.descriptionHtml = $sce.trustAsHtml(data.description);
                        data.minutesHtml = $sce.trustAsHtml(data.minutes);
                        vm.meeting = data;
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

            function saveMeeting() {
                MeetingService.updateMeeting(vm.projectId, vm.meeting).then(
                    function (data) {
                        vm.meeting = data;
                        $rootScope.showSuccessMessage("Meeting updated successfully");
                    }
                )
            }

            function downloadAttachment(attachment) {
                var url = "api/col/attachments/" + attachment.id + "/download";
                window.open(url, '_self');
            }

            function editMeetingDialog() {
                var options = {
                    title: 'Edit Meeting',
                    template: 'app/desktop/modules/col/meetings/edit/meetingEditView.jsp',
                    controller: 'MeetingEditController as editMeetingVm',
                    resolve: 'app/desktop/modules/col/meetings/edit/meetingEditController',
                    width: 600,
                    data: {},
                    buttons: [
                        {text: 'Save', broadcast: 'edit.save'}
                    ],
                    callback: function () {
                        loadMeetingDetails();
                    }
                };

                $rootScope.showSidePanel(options);

            }

            function back() {
                window.history.back();
            }

            function deleteMeeting() {
                var options = {
                    title: 'Delete Meeting',
                    message: 'Are you sure you want to delete this Meeting?',
                    okButtonClass: 'btn-danger'
                };
                DialogService.confirm(options, function (yes) {
                    if (yes == true) {
                        MeetingService.deleteMeeting(vm.projectId, vm.meeting.id).then(
                            function (data) {
                                $rootScope.showSuccessMessage("Meeting deleted successfully");
                                $state.go('app.pm.project.meetings');
                            }
                        )
                    }
                })

            }

            (function () {
                if ($application.homeLoaded == true) {
                    $rootScope.$on("app.meeting.details", loadMeetingDetails);
                    loadMeetingDetails();

                }
            })();
        }
    }
);