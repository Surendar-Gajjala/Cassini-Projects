define(['app/desktop/modules/col/col.module',
        'app/desktop/modules/col/meetings/new/meetingDialogController',
        'app/shared/services/common/meetingService'
    ],
    function (module) {
        module.controller('MeetingsController', MeetingsController);

        function MeetingsController($scope, $rootScope, $timeout, $state, $stateParams, $cookies, MeetingService) {
            var vm = this;

            $rootScope.viewInfo.icon = "fa fa-calendar";
            $rootScope.viewInfo.title = "Meetings";

            vm.meetings = [];
            vm.uiConfig = {
                calendar: {
                    editable: true,
                    header: {
                        left: 'prev,next today',
                        center: 'title',
                        right: 'month,basicWeek,basicDay'
                    },
                    eventLimit: true,
                    dayClick: dayClickCallback,
                    eventRender: eventRenderCallback,
                    eventClick: eventClickCallback
                }
            };

            vm.openAllMeetings = openAllMeetings;
            vm.showNewMeetingDialog = showNewMeetingDialog;

            var slotDate;
            var eventDateFormat = "DD/MM/YYYY,hh:mm A";

            function openAllMeetings() {
                $state.go("app.pm.project.allMeetings");
            }

            function eventClickCallback(calEvent, jsEvent, view) {
                $state.go('app.pm.project.meeting', {meetingId: calEvent.original.id})
            }

            function dayClickCallback(date) {
                slotDate = date;
                $("#project-meetings").on("mousemove", forgetSlot);
            }

            function eventRenderCallback(event, element) {
                element.on("dblclick", function () {
                    dblClickFunction(event.start)
                });
            }

            function forgetSlot() {
                slotDate = null;
                $("#project-meetings").off("mousemove", forgetSlot);
            }

            function dblClickFunction(date) {
                showNewMeetingDialog(date);
            }

            /*function showNewMeetingDialog(date) {
             if(date == null || date == undefined) {
             date = new Date();
             }
             $application.selectedDate = moment(date).format("DD/MM/YYYY");
             $state.go('app.pm.project.newmeeting');
             }*/

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
                        loadMeetings();
                    }
                };

                $rootScope.showSidePanel(options);
            }

            function loadMeetings() {
                vm.meetings = [];
                MeetingService.getMeetings($stateParams.projectId).then(
                    function (data) {
                        var meetings = [];
                        angular.forEach(data.content, function (item) {
                            var meeting = {
                                title: item.title,
                                start: moment(item.startDate + "," + item.startTime, eventDateFormat),
                                end: moment(item.endDate + "," + item.endTime, eventDateFormat),
                                original: item
                            };

                            meetings.push(meeting);
                        });

                        vm.meetings.push(meetings);

                    }
                )
            }

            (function () {
                if ($application.homeLoaded == true) {
                    $("#project-meetings").dblclick(function () {
                        if (slotDate) {
                            dblClickFunction(slotDate); //do something with the date
                        }
                    });

                    loadMeetings();
                }
            })();
        }
    }
);