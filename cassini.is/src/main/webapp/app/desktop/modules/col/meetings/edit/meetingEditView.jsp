<div style="position: relative;">
    <div style="overflow-y: auto; overflow-x: hidden; padding: 20px">
        <div class="row" style="margin: 0;">
            <div>
                <form class="form-horizontal">
                    <div class="form-group">
                        <label class="col-sm-3 control-label">Title: </label>

                        <div class="col-sm-8">
                            <input type="text" class="form-control" name="title"
                                   ng-model="editMeetingVm.meeting.title">
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-3 control-label">Attendees: </label>

                        <div class="col-sm-8">
                            <ui-select multiple ng-model="editMeetingVm.meeting.attendees" theme="bootstrap"
                                       style="width: 100%"
                                       on-select="editMeetingVm.selectPersons(person)"
                                       on-remove="editMeetingVm.removePerson($item.id)">
                                <ui-select-match placeholder="Select">
                                    {{$item.fullName}}
                                </ui-select-match>
                                <ui-select-choices
                                        repeat="person in editMeetingVm.persons | filter: $select.search |orderBy: 'fullName'">
                                    <div ng-bind="person.fullName"></div>
                                </ui-select-choices>
                            </ui-select>

                        </div>
                    </div>

                    <div class="form-group">
                        <label class="col-sm-3 control-label" style="margin-top: 40px">Start :</label>

                        <div class="col-sm-4" style="margin-top: 40px">
                            <div class="input-group">
                                <input type="text" class="form-control" date-picker
                                       ng-model="editMeetingVm.meeting.startDate">
                                <span class="input-group-addon"><i class="glyphicon glyphicon-calendar"></i></span>
                            </div>
                        </div>

                        <div class="col-sm-4">
                            <div uib-timepicker ng-model="editMeetingVm.meeting.startTime">

                            </div>
                        </div>
                    </div>

                    <div class="form-group">
                        <label class="col-sm-3 control-label" style="margin-top: 40px">End :</label>

                        <div class="col-sm-4" style="margin-top: 40px">
                            <div class="input-group">
                                <input type="text" class="form-control" date-picker
                                       ng-model="editMeetingVm.meeting.endDate">
                                <span class="input-group-addon"><i class="glyphicon glyphicon-calendar"></i></span>
                            </div>
                        </div>

                        <div class="col-sm-4">
                            <div uib-timepicker ng-model="editMeetingVm.meeting.endTime">
                            </div>
                        </div>
                    </div>

                    <div class="form-group">
                        <label class="col-sm-3 control-label">Description: </label>

                        <div class="col-sm-8">
                            <textarea id="meetingDescription" name="description" rows="3" class="form-control"
                                      style="resize: none"
                                      ng-model="editMeetingVm.meeting.description"></textarea>
                        </div>
                    </div>
                </form>
                <div class="row" style="margin-top: 10px;">
                    <label class="col-sm-3 text-right">Attachments<span class="asterisk">*</span> : </label>

                    <div class="col-sm-8">
                        <div style="margin-bottom: 5px;">
                            <span class="btn btn-xs btn-success fileinput-button">
                                <i class="glyphicon glyphicon-plus"></i>
                                <span>Add Files</span>
                            </span>
                        </div>
                        <form action="" id="attachmentsForm" method="post" enctype="multipart/form-data">
                            <div id="attachmentsContainer" class="attachments-dropzone-container">
                                <div id="attachmentsDropZone" style="display: table; width: 100%;">
                                    <div class="meeting-attachments-dropzone">
                                        <div style="width: 100%; min-height: 50px; padding-top: 15px; display: inline-block; text-align: center">
                                            <span><i>You can drag and drop files here</i></span>
                                        </div>
                                    </div>
                                </div>

                                <div id="previews">
                                    <div id="template">
                                        <div class="row"
                                             style="border-bottom: 1px dotted lightgrey; margin-bottom: 10px;padding-bottom: 10px;">
                                            <div class="col-sm-7">
                                                <p class="name" data-dz-name></p>
                                                <small class="error text-danger" data-dz-errormessage></small>
                                            </div>
                                            <div class="col-sm-3">
                                                <p class="size" data-dz-size></p>

                                                <div class="progress progress-striped active" role="progressbar"
                                                     aria-valuemin="0" aria-valuemax="100" aria-valuenow="0"
                                                     style="margin-bottom: 0">
                                                    <div class="progress-bar progress-bar-success" style="width:0%;"
                                                         data-dz-uploadprogress></div>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </form>
                    </div>
                </div>
            </div>


            <%--
        <div ng-show="newMeetingVm.showDropzone == true">
            <%@include file="dropzoneComponent.jsp" %>
        </div>
        <br>
    </div>
</div>--%>
        </div>
    </div>
</div>
</div>
