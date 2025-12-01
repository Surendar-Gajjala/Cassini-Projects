define(
    [
        'app/desktop/desktop.css'
    ],
    function (cssConfig) {
        return {
            routes: {
                'app.col': {
                    url: '/col',
                    abstract: true,
                    templateUrl: 'app/desktop/modules/proc/procMainView.jsp',
                    controller: 'ProcMainController as procMainVm',
                    resolve: ['app/desktop/modules/proc/procMainController'],
                    css: cssConfig.getViewCss('app')
                },
                'app.pm.project.communication': {
                    url: '/communication',
                    templateUrl: 'app/desktop/modules/col/communication/communicationMainView.jsp',
                    controller: 'CommunicationMainController as communicationVm',
                    resolve: ['app/desktop/modules/col/communication/communicationMainController',
                        'app/desktop/modules/col/communication/chatFilter'],
                    css: cssConfig.getViewCss('app')
                },
                'app.pm.project.communication.discussion': {
                    url: '/discussion',
                    templateUrl: 'app/desktop/modules/col/communication/discussion/discussionView.jsp',
                    controller: 'DiscussionController as discussionVm',
                    resolve: ['app/desktop/modules/col/communication/discussion/discussionController'],
                    css: cssConfig.getViewCss('app')
                },

                'app.pm.project.meetings': {
                    url: '/meetings',
                    templateUrl: 'app/desktop/modules/col/meetings/meetingsView.jsp',
                    controller: 'MeetingsController as meetingsVm',
                    resolve: ['app/desktop/modules/col/meetings/meetingsController'],
                    css: cssConfig.getViewCss('meeting')
                },

                'app.pm.project.allMeetings': {
                    url: '/allMeetings',
                    templateUrl: 'app/desktop/modules/col/meetings/all/allMeetingsView.jsp',
                    controller: 'AllMeetingsController as allMeetingsVm',
                    resolve: ['app/desktop/modules/col/meetings/all/allMeetingsController'],
                    css: cssConfig.getViewCss('meeting')
                },
                'app.pm.project.meeting': {
                    url: '/meetings/:meetingId',
                    templateUrl: 'app/desktop/modules/col/meetings/details/meetingDetailsView.jsp',
                    controller: 'MeetingDetailsController as meetingVm',
                    resolve: ['app/desktop/modules/col/meetings/details/meetingDetailsController'],
                    css: cssConfig.getViewCss('meeting')
                }
            }
        };
    }
);