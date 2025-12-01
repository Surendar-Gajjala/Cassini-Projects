<div class="view-container" fitcontent>
    <div class="view-toolbar">
        <button class="btn btn-sm btn-success" ng-click="allMeetingsVm.showNewMeetingDialog()"
                ng-disabled="hasPermission('permission.meetings.new') == false"
        <%-- ng-disabled="selectedProject.locked == true || !(hasPermission('permission.meetings.new')  || login.person.isProjectOwner)"--%>>
            New Meeting
        </button>
        <button class="btn btn-sm btn-success" ng-click="allMeetingsVm.showCalendarView()">Calendar View</button>
    </div>
    <div class="view-content no-padding" style="overflow-y: auto;padding: 10px;">
        <div class="responsive-table" style="padding: 10px;">
            <table class="table table-striped highlight-row">
                <thead>
                <tr>
                    <th>Title</th>
                    <th>Description</th>
                    <th>Date/Duration</th>
                    <th>Attendees</th>
                </tr>
                </thead>

                <tbody>
                <tr ng-if="allMeetingsVm.loading == true">
                    <td colspan="11">
                        <span style="font-size: 15px;">
                            <img src="app/assets/bower_components/cassini-platform/images/loaders/loader19.gif"
                                 class="mr5">Loading Meetings..
                        </span>
                    </td>
                </tr>

                <tr ng-if="allMeetingsVm.loading == false && allMeetingsVm.allMeetings.length == 0">
                    <td colspan="11">No Meetings</td>
                </tr>
                <tr ng-repeat="meeting in allMeetingsVm.allMeetings">
                    <td>
                        <a ng-click="allMeetingsVm.openMeetingDetails(meeting.original)" title="Click to show details">{{meeting.title}}</a>
                    </td>
                    <td>
                        <span title="{{meeting.original.description}}">{{meeting.original.description | limitTo: 8 }}{{meeting.original.description.length > 8 ? '...' : ''}}</span>
                    </td>
                    <td>
                        <span>{{meeting.meetingDateTime}}</span>
                    </td>
                    <td>
                        <span>{{meeting.attendees}}</span>
                    </td>
                </tr>
                </tbody>
            </table>
        </div>
    </div>
</div>

