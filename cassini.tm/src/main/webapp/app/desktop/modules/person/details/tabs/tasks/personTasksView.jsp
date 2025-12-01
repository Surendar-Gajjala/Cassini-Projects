<div class="view-container">
    <div class="view-content">
        <div class="view-content no-padding" style="overflow-y: auto;padding: 10px;">
            <div style="padding: 10px;">
                <div class="pull-right text-center">
                    <span ng-if="personTasksVm.loading == false">Total :{{personTasksVm.personTasks.length}} Person Tasks
                   </span>
                </div>
            </div>
        </div>

        <table class="table table-striped">
            <thead>

            <tr>
                <th>Project</th>
                <th>Name</th>
                <th>Assigned Date</th>
                <th>Shift</th>
                <th>Work Location</th>
                <th>Status</th>
                <th>Assigned To</th>
                <th>Verified By</th>
                <th>Approved By</th>
                <th style="width: 100px;">Actions</th>
            </tr>
            </thead>

            <tbody>
            <tr ng-if="personTasksVm.loading == true">
                <td colspan="12">
                           <span style="font-size: 15px;">
                               <img src="app/assets/bower_components/cassini-platform/images/loaders/loader19.gif"
                                    class="mr5">Loading DepartmentTasks...
                           </span>
                </td>
            </tr>

            <tr ng-if="personTasksVm.loading == false && personTasksVm.personTasks.content.length == 0">
                <td colspan="12">No DepartmentTasks</td>

            </tr>
            <tr ng-repeat="task in personTasksVm.personTasks">
                <td>{{task.project.name}}</td>
                <td style="width: 150px;"><a href="" <%--ui-sref="app.task.details({taskId: task.id, projectId: task.project.id})"--%>>{{task.name}}</a></td>
                <td>{{task.assignedDate}}</td>
                <td>{{task.shift.name}}</td>
                <td>{{task.location}}</td>
                <td>
                    <div class="task-status"
                         ng-class="{'completed': (task.status == 'FINISHED' || task.status == 'VERIFIED' || task.status == 'APPROVED'),
                                    'pending': task.status == 'ASSIGNED'}"></div>
                    <div class="task-status"
                         ng-class="{'completed': (task.status == 'VERIFIED' || task.status == 'APPROVED'),
                                    'pending': task.status == 'ASSIGNED' || task.status == 'FINISHED'}"></div>
                    <div class="task-status"
                         ng-class="{'completed': task.status == 'APPROVED',
                                    'pending': task.status != 'APPROVED'}"></div>
                </td>
                <td>{{task.assignedToObject.firstName}}</td>
                <td>{{task.verifiedByObject.firstName}}</td>
                <td>{{task.approvedByObject.firstName}}</td>
                <td style="width:100px;">
                    <button title="Delete Task" class="btn btn-xs btn-danger" ng-click="personTasksVm.deletePersonTask(task)">
                        <i class="fa fa-trash"></i>
                    </button>
                </td>
            </tr>
            </tbody>
        </table>
    </div>
</div>
