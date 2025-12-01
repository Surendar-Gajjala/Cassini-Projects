<div class="view-container" fitcontent>
    <div class="view-toolbar">
        <button class="btn btn-sm btn-default min-width" ng-click="meetingVm.back()">Back</button>
        <button class="btn btn-sm btn-success min-width" ng-click="meetingVm.saveMeeting()"
                ng-disabled="hasPermission('permission.meetings.edit') == false && hasPermission('permission.meetings.delete') == false">
            Save
        </button>
        <button class="btn btn-sm btn-info min-width" ng-click="meetingVm.editMeetingDialog()"
                ng-disabled="hasPermission('permission.meetings.edit') == false">Edit
        </button>
        <button class="btn btn-sm btn-danger min-width" ng-click="meetingVm.deleteMeeting()"
                ng-disabled="hasPermission('permission.meetings.delete') == false">Delete
        </button>
    </div>

    <div class="view-content no-padding" style="overflow-y: auto;">
        <div ng-if="meetingVm.loading == true" style="padding: 30px;">
            <br/>
                <span style="font-size: 15px;">
                    <img src="app/assets/images/loaders/loader6.gif" class="mr5">Loading Meeting details..
                </span>
            <br/>
        </div>
        <div class="row row-eq-height" style="margin: 0;" ng-if="meetingVm.loading == false">
            <div class="item-details col-sm-12" style="padding: 30px;">
                <div class="row">
                    <div class="label col-sm-2 text-right">
                        <span>Title: </span>
                    </div>
                    <div class="value col-sm-10">
                        <span>{{meetingVm.meeting.title}}</span>
                    </div>
                </div>
                <div class="row">
                    <div class="label col-sm-2 text-right">
                        <span>Created By: </span>
                    </div>
                    <div class="value col-sm-10">{{meetingVm.meeting.createdByPerson.fullName}}</div>
                </div>
                <div class="row">
                    <div class="label col-sm-2 text-right">
                        <span>Attendees: </span>
                    </div>
                    <div class="value col-sm-10">
                        <ul style="margin-left: -25px">
                            <li ng-repeat="attendee in meetingVm.meeting.attendees">{{attendee.fullName}}</li>
                        </ul>
                    </div>
                </div>
                <div class="row">
                    <div class="label col-sm-2 text-right">
                        <span>Starts At:</span>
                    </div>
                    <div class="value col-sm-10">
                        <span>{{meetingVm.meeting.startDate}}, {{meetingVm.meeting.startTime}}</span>
                    </div>
                </div>
                <div class="row">
                    <div class="label col-sm-2 text-right">
                        <span>Ends At:</span>
                    </div>
                    <div class="value col-sm-10">
                        <span>{{meetingVm.meeting.endDate}}, {{meetingVm.meeting.endTime}}</span>
                    </div>
                </div>

                <div class="row">
                    <div class="label col-sm-2 text-right">
                        <span>Message:</span>
                    </div>
                    <div class="value col-sm-10">
                        <div ng-bind-html="meetingVm.meeting.descriptionHtml"></div>
                    </div>
                </div>

                <div class="row">
                    <div class="label col-sm-2 text-right">
                        <span>Attachments:</span>
                    </div>
                    <div class="value col-sm-10">
                        <span ng-repeat="attachment in meetingVm.meeting.attachments">
                            <span class="mr5"><i class="fa fa-paperclip"></i></span>
                            <span><a href="" ng-click="meetingVm.downloadAttachment(attachment)">{{attachment.name}}</a></span>
                            <br>
                        </span>
                    </div>
                </div>

                <div class="row">
                    <div class="label col-sm-2 text-right">
                        <span>Meeting Minutes:</span>
                    </div>
                    <div class="value col-sm-10">
                        <textarea ng-if="hasPermission('permission.meetings.edit') == true" name="minutes" id="minutes"
                                  rows="5" style="resize: none; width: 100%"
                                  ng-model="meetingVm.meeting.minutes"></textarea>

                        <p ng-if="hasPermission('permission.meetings.edit') == false">{{meetingVm.meeting.minutes}}</p>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
