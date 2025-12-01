define(['app/shared/services/services.module', 'app/shared/factories/httpFactory'],
    function (mdoule) {
        mdoule.factory('MeetingService', MeetingService);

        function MeetingService(httpFactory) {
            return {
                getMeetings: getMeetings,
                getMeeting: getMeeting,
                createMeeting: createMeeting,
                updateMeeting: updateMeeting,
                deleteMeeting: deleteMeeting,
                deleteAttendee: deleteAttendee,
                createMail: createMail
            };

            function getMeetings(projectId) {
                var url = "api/projects/" + projectId + "/meetings";
                return httpFactory.get(url);
            }

            function getMeeting(projectId, meetingId) {
                var url = "api/projects/" + projectId + "/meetings/" + meetingId;
                return httpFactory.get(url);
            }

            function createMeeting(projectId, meeting) {
                var url = "api/projects/" + projectId + "/meetings";
                return httpFactory.post(url, meeting);
            }

            function updateMeeting(projectId, meeting) {
                var url = "api/projects/" + projectId + "/meetings/" + meeting.id;
                return httpFactory.put(url, meeting);
            }

            function deleteMeeting(projectId, meetingId) {
                var url = "api/projects/" + projectId + "/meetings/" + meetingId;
                return httpFactory.delete(url);
            }

            function deleteAttendee(projectId, meetingId, personId) {
                var url = "api/projects/" + projectId + "/meetings/" +meetingId+ "/attendee/" +personId;
                return httpFactory.delete(url);
            }

            function createMail(projectId, meetingId) {
                var url = "api/projects/" + projectId + "/meetings/" + meetingId + "/mail";
                return httpFactory.get(url);
            }
        }
    }
);