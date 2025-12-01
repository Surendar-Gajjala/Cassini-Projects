<div class="responsive-table">
    <table class="table table-striped highlight-row">
        <thead>
        <tr>
            <th>Name</th>
            <th>Description</th>
            <th>Assigned To</th>
            <th>WBS Ref</th>
            <th>Status</th>
            <th>Percent Complete</th>
            <th>Planned Start Date</th>
            <th>Planned Finish Date</th>
            <th>Actual Start Date</th>
            <th>Actual Finish Date</th>
        </tr>
        </thead>
        <tbody>
        <tr ng-if="siteTasksVm.taskList.length == 0">
            <td colspan="11">No Tasks are available to view</td>
        </tr>
        <tr ng-repeat="task in siteTasksVm.taskList">
            <td><a ui-sref="app.pm.project.taskdetails({taskId: task.id})">{{task.name}}</a>
            </td>
            <td>{{task.description.length > 25 ? task.description.trunc(25, true) :
                task.description}}
            </td>
            <td>{{task.personObject.fullName}}</td>
            <td>{{task.wbsStructure}}</td>
            <%--<td>{{task.wbsItemObject.name}}</td>--%>
            <td><span class="label" style="color: white;" ng-class="{
                                    'label-success': task.status == 'NEW',
                                    'label-info': task.status == 'ASSIGNED',
                                    'label-warning': task.status == 'INPROGRESS',
                                    'label-danger': task.status == 'FINISHED'}">
                            {{task.status}}
                        </span></td>
            <td>
                <div class="task-progress progress text-center">
                    <div style="width:{{task.percentComplete}}%"
                         class="progress-bar progress-bar-primary"
                         role="progressbar" aria-valuenow="40" aria-valuemin="0"
                         aria-valuemax="100">
                        <span style="margin-left: 2px;">{{task.percentComplete}}%</span>
                    </div>
                </div>
            </td>
            <td>{{task.plannedStartDate}}</td>
            <td>{{task.plannedFinishDate}}</td>
            <td>{{task.actualStartDate}}</td>
            <td>{{task.actualFinishDate}}</td>
        </tr>
        </tbody>
    </table>
</div>