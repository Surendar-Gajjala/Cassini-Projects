<div class="view-container">
    <div class="view-content">
        <div class="view-content no-padding" style="overflow-y: auto;padding: 10px;">
            <div style="padding: 10px;">
                <div class="pull-right text-center">

                <%--<span ng-if="departmentTasksVm.loading == false"><small>Page {{departmentTasksVm.departmentTasks.number+1}} of
                    {{departmentTasksVm.departmentTasks.totalPages}}
                </small></span>
                    <br>

                    <div class="btn-group" style="margin-bottom: 0">
                        <button class="btn btn-xs btn-default"
                                ng-click="departmentTasksVm.previousPage()"
                                ng-disabled="departmentTasksVm.departmentTasks.first">
                            <i class="fa fa-chevron-left"></i>
                        </button>
                        <button class="btn btn-xs btn-default"
                                ng-click="departmentTasksVm.nextPage()"
                                ng-disabled="departmentTasksVm.departmentTasks.last">
                            <i class="fa fa-chevron-right"></i>
                        </button>
                    </div>
                    <br>--%>
                    <span ng-if="departmentTasksVm.loading == false">Total :{{departmentTasksVm.departmentTasks.length}} Department Tasks
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
            <tr ng-if="departmentTasksVm.loading == true">
                <td colspan="12">
                           <span style="font-size: 15px;">
                               <img src="app/assets/bower_components/cassini-platform/images/loaders/loader19.gif"
                                    class="mr5">Loading DepartmentTasks...
                           </span>
                </td>
            </tr>

            <tr ng-if="departmentTasksVm.loading == false && departmentTasksVm.departmentTasks.content.length == 0">
                <td colspan="12">No DepartmentTasks</td>

            </tr>
            <tr ng-repeat="task in departmentTasksVm.departmentTasks">
                <td>{{task.project.name}}</td>
                <td style="width: 150px;"><%--<a href="" ui-sref="app.task.details({taskId: task.id, projectId: task.project.id})">--%>{{task.name}}<%--</a>--%></td>
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
                    <button title="delete Task" class="btn btn-xs btn-danger" ng-click="departmentTasksVm.deleteDepartmentTask(task)">
                        <i class="fa fa-trash"></i>
                    </button>
                </td>
            </tr>
            </tbody>
        </table>
    </div>
</div>
