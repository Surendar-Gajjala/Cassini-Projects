/**
 * Created by Rajabrahmachary on 25-09-2018.
 */

define(['app/desktop/modules/col/col.module',
        'app/desktop/modules/col/meetings/new/meetingDialogController',
        'app/shared/services/common/meetingService'
    ],
    function (module) {
        module.controller('AllMeetingsController', AllMeetingsController);

        function AllMeetingsController($scope, $rootScope, $timeout, $state, $stateParams, $cookies, MeetingService) {

            $rootScope.viewInfo.icon = "fa fa-calendar";
            $rootScope.viewInfo.title = "All Meetings";

            var vm = this
            vm.loading = true;
            vm.showNewMeetingDialog = showNewMeetingDialog;
            vm.showCalendarView = showCalendarView;
            vm.openMeetingDetails = openMeetingDetails;

            var eventDateFormat = "DD/MM/YYYY,hh:mm A";

            function openMeetingDetails(meeting) {
                $state.go('app.pm.project.meeting', {meetingId: meeting.id})
            }

            function showCalendarView() {
                $state.go("app.pm.project.meetings");
            }

            function showNewMeetingDialog() {
                var options = {
                    title: 'New Meeting',
                    template: 'app/desktop/modules/col/meetings/new/meetingDialog.jsp',
                    controller: 'MeetingDialogController as newMeetingVm',
                    resolve: 'app/desktop/modules/col/meetings/new/meetingDialogController',
                    width: 600,
                    data: {},
                    buttons: [
                        {text: 'Create', broadcast: 'app.create'}
                    ],
                    callback: function () {
                        loadAllMeetings();
                    }
                };

                $rootScope.showSidePanel(options);
            }

            function loadAllMeetings() {
                vm.allMeetings = [];
                MeetingService.getMeetings($stateParams.projectId).then(
                    function (data) {
                        angular.forEach(data.content, function (item) {
                            var meeting = {
                                title: item.title,
                                meetingDateTime: (item.startDate + "(" + item.startTime + "  --  " + item.endTime + ")"),
                                original: item,
                                attendees: item.attendees.map(function (elem) {
                                    return elem.fullName;
                                }).join(",")
                            };
                            vm.allMeetings.push(meeting);
                        });
                        vm.loading = false;
                    }
                )
            }

            (function () {
                if ($application.homeLoaded == true) {
                    loadAllMeetings();
                }
            })();
        }
    }
);