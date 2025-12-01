<div ng-if="problemBasicVm.loading == true" style="padding: 30px;">
    <br/>
                <span style="font-size: 15px;">
                    <img src="app/assets/images/loaders/loader6.gif" class="mr5">Loading problem details...
                </span>
    <br/>
</div>
<div class="row row-eq-height" style="margin: 0;" ng-if="problemBasicVm.loading == false">
    <div class="item-details col-sm-12" style="padding: 30px;">
        <div class="row">
            <div class="label col-xs-4 col-sm-3 text-right">
                <span>Type: </span>
            </div>
            <div class="value col-xs-8 col-sm-9">
                <span>{{problemBasicVm.issue.typeName}}</span>
            </div>
        </div>
        <div class="row">
            <div class="label col-xs-4 col-sm-3 text-right">
                <span>Title: </span>
            </div>
            <div class="value col-xs-8 col-sm-9">
                <a style="color:#428bca;"
                   ng-if="selectedProject.locked == false && (hasPermission('permission.issues.edit') || login.person.isProjectOwner || problemBasicVm.isAssignedPerson)"
                   href="#" editable-text="problemBasicVm.issue.title"
                   onaftersave="problemBasicVm.updateIssue()">{{problemBasicVm.issue.title}}
                </a>

                <p ng-if="selectedProject.locked == false && !(hasPermission('permission.issues.edit') || login.person.isProjectOwner || problemBasicVm.isAssignedPerson)">
                    {{problemBasicVm.issue.title}}</p>

                <p ng-if="selectedProject.locked == true">{{problemBasicVm.issue.title}}</p>

            </div>
        </div>
        <div class="row">
            <div class="label col-xs-4 col-sm-3 text-right">
                <span>Status: </span>
            </div>
            <div class="value col-xs-8 col-sm-9" style="line-height: 20px">

                <a style="color:#428bca;"
                   ng-if="selectedProject.locked == false && (hasPermission('permission.issues.edit')  || login.person.isProjectOwner || problemBasicVm.isAssignedPerson)"
                   href="#" title="Edit Status"
                   style="text-decoration: none !important;"
                   editable-select="problemBasicVm.issue.status" onaftersave="problemBasicVm.updateIssue()"
                   e-ng-options="issue for issue in problemBasicVm.status">

                            <span class="label" style="color: white;" ng-class="{
                                    'label-success': problemBasicVm.issue.status == 'NEW',
                                    'label-info': problemBasicVm.issue.status == 'ASSIGNED',
                                    'label-warning': problemBasicVm.issue.status == 'INPROGRESS',
                                    'label-danger': problemBasicVm.issue.status == 'CLOSED'}">
                            {{problemBasicVm.issue.status}} &nbsp &nbsp<span style="color:white;"><i
                                    class="fa fa-pencil-square-o" aria-hidden="true"></i>
                            </span>

                            </span>
                </a>

                <p ng-if="selectedProject.locked == false && !(hasPermission('permission.issues.edit')  || login.person.isProjectOwner || problemBasicVm.isAssignedPerson)">

                            <span class="label" style="color: white;" ng-class="{
                                    'label-success': problemBasicVm.issue.status == 'NEW',
                                    'label-info': problemBasicVm.issue.status == 'ASSIGNED',
                                    'label-warning': problemBasicVm.issue.status == 'INPROGRESS',
                                    'label-danger': problemBasicVm.issue.status == 'CLOSED'}">
                            {{problemBasicVm.issue.status}} &nbsp &nbsp<span style="color:white;">
                            </span>

                            </span>
                </p>

                <p ng-if="selectedProject.locked == true">

                            <span class="label" style="color: white;" ng-class="{
                                    'label-success': problemBasicVm.issue.status == 'NEW',
                                    'label-info': problemBasicVm.issue.status == 'ASSIGNED',
                                    'label-warning': problemBasicVm.issue.status == 'INPROGRESS',
                                    'label-danger': problemBasicVm.issue.status == 'CLOSED'}">
                            {{problemBasicVm.issue.status}} &nbsp &nbsp<span style="color:white;">
                            </span>

                            </span>
                </p>
            </div>
        </div>
        <div class="row">
            <div class="label col-xs-4 col-sm-3 text-right">
                <span>Reported By: </span>
            </div>
            <div class="value col-xs-8 col-sm-9">{{problemBasicVm.issue.createdByPerson.fullName}}</div>
        </div>
        <div class="row">
            <div class="label col-xs-4 col-sm-3 text-right">
                <span>Priority: </span>
            </div>
            <div class="value col-xs-8 col-sm-9" style="line-height: 20px">
                        <span class="label" style="color: white;" ng-class="{
                                    'label-info': problemBasicVm.issue.priority == 'LOW',
                                    'label-warning': problemBasicVm.issue.priority == 'MEDIUM',
                                    'label-danger': problemBasicVm.issue.priority == 'HIGH'}">
                            {{problemBasicVm.issue.priority}}
                        </span>
            </div>
        </div>
        <div class="row">
            <div class="label col-xs-4 col-sm-3 text-right">
                <span>Description:</span>
            </div>
            <div class="value col-xs-8 col-sm-9">
                <a style="color:#428bca;" href="#" editable-text="problemBasicVm.issue.description"
                   ng-if="selectedProject.locked == false && (hasPermission('permission.issues.edit') || login.person.isProjectOwner || problemBasicVm.isAssignedPerson)"
                   onaftersave="problemBasicVm.updateIssue()">{{problemBasicVm.issue.description || 'Click to enter
                    description'}}</a>

                <p ng-if="selectedProject.locked == false && !(hasPermission('permission.issues.edit') || login.person.isProjectOwner)">
                    {{problemBasicVm.issue.description}}</p>

                <p ng-if="selectedProject.locked">{{problemBasicVm.issue.description}}</p>
            </div>
        </div>
        <div class="row">
            <div class="label col-xs-4 col-sm-3 text-right">
                <span>Task:</span>
            </div>
            <div class="value col-xs-8 col-sm-9">
                <a ui-sref="app.pm.project.taskdetails({taskId: problemBasicVm.issue.task})">{{problemBasicVm.issue.taskName}}</a>
            </div>
        </div>
        <attributes-details-view attribute-id="problemId" attribute-type="PROBLEM"
                                 has-permission="(hasPermission('permission.issues.edit') || login.person.isProjectOwner || problemBasicVm.isAssignedPerson)"></attributes-details-view>
    </div>
</div>