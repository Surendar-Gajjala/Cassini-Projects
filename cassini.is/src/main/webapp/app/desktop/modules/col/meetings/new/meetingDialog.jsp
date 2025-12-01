<div style="position: relative;">
    <div style="overflow-y: auto; overflow-x: hidden; padding: 20px">
        <div class="row" style="margin: 0;">
            <div>

                <form class="form-horizontal">
                    <div class="form-group">
                        <label class="col-sm-3 control-label">Title<span class="asterisk">*</span>: </label>

                        <div class="col-sm-8">
                            <input type="text" class="form-control" name="title"
                                   ng-model="newMeetingVm.newMeeting.title">
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-3 control-label">Attendees<span class="asterisk">*</span>: </label>

                        <div class="col-sm-8">
                            <ui-select multiple ng-model="newMeetingVm.newMeeting.attendees" theme="bootstrap"
                                       style="width: 100%">
                                <ui-select-match placeholder="Select">
                                    {{$item.fullName}}
                                </ui-select-match>
                                <ui-select-choices
                                        repeat="person in newMeetingVm.persons | filter: $select.search |orderBy: 'fullName'">
                                    <div ng-bind="person.fullName"></div>
                                </ui-select-choices>
                            </ui-select>
                        </div>
                    </div>


                    <div class="form-group">
                        <label class="col-sm-3 control-label" style="margin-top: 40px">Start<span
                                class="asterisk">*</span> :</label>

                        <div class="col-sm-4" style="margin-top: 40px">
                            <div class="input-group">
                                <input type="text" class="form-control" date-picker
                                       ng-model="newMeetingVm.newMeeting.startDate">
                                <span class="input-group-addon"><i class="glyphicon glyphicon-calendar"></i></span>
                            </div>
                        </div>

                        <div class="col-sm-4">
                            <div uib-timepicker ng-model="newMeetingVm.newMeeting.startTime">

                            </div>
                        </div>
                    </div>

                    <div class="form-group">
                        <label class="col-sm-3 control-label" style="margin-top: 40px">End<span
                                class="asterisk">*</span> :</label>

                        <div class="col-sm-4" style="margin-top: 40px">
                            <div class="input-group">
                                <input type="text" class="form-control" date-picker
                                       ng-model="newMeetingVm.newMeeting.endDate">
                                <span class="input-group-addon"><i class="glyphicon glyphicon-calendar"></i></span>
                            </div>
                        </div>

                        <div class="col-sm-4">
                            <div uib-timepicker ng-model="newMeetingVm.newMeeting.endTime">
                            </div>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-3 control-label">Description<span class="asterisk">*</span>: </label>

                        <div class="col-sm-8">
                <textarea id="meetingDescription" name="description" rows="3" class="form-control" style="resize: none"
                          ng-model="newMeetingVm.newMeeting.description"></textarea>
                        </div>
                    </div>
                </form>
                <br>

                <div ng-show="newMeetingVm.showDropzone == true">
                    <%@include file="dropzoneComponent.jsp" %>
                </div>
                <br>
            </div>
        </div>
    </div>
</div>

