<div class="view-container" fitcontent>
    <div class="view-toolbar">
        <button class="btn btn-sm btn-success" ng-click="meetingsVm.showNewMeetingDialog()"
                ng-disabled="hasPermission('permission.meetings.new') == false"
        <%--ng-disabled="selectedProject.locked == true || !(hasPermission('permission.meetings.new')  || login.person.isProjectOwner)"--%>>
            New Meeting
        </button>
        <button class="btn btn-sm btn-success" ng-click="meetingsVm.openAllMeetings()"
                ng-disabled="selectedProject.locked == true || !(hasPermission('permission.meetings.allMeetings')  || login.person.isProjectOwner)">
            All Meetings
        </button>
    </div>

    <div class="view-content no-padding" style="overflow-y: auto;">
        <div id="project-meetings" class="project-meetings"
             ui-calendar="meetingsVm.uiConfig.calendar"
             ng-model="meetingsVm.meetings" style="padding: 10px;"></div>
    </div>
</div>
