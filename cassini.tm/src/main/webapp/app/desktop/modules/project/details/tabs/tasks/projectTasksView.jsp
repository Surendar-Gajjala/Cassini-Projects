<div class="view-container">
    <div class="view-content">
        <div class="view-content no-padding" style="overflow-y: auto;padding: 10px;">


            <div style="padding: 10px;">
                <div class="pull-right text-center">

                <span ng-if="projectTasksVm.loading == false"><small>Page {{projectTasksVm.tasks.number+1}} of
                    {{projectTasksVm.tasks.totalPages}}
                </small></span>
                    <br>

                    <div class="btn-group" style="margin-bottom: 0">
                        <button class="btn btn-xs btn-default"
                                ng-click="projectTasksVm.previousPage()"
                                ng-disabled="projectTasksVm.tasks.first">
                            <i class="fa fa-chevron-left"></i>
                        </button>
                        <button class="btn btn-xs btn-default"
                                ng-click="projectTasksVm.nextPage()"
                                ng-disabled="projectTasksVm.tasks.last">
                            <i class="fa fa-chevron-right"></i>
                        </button>
                    </div>
                    <br>
                    <span ng-if="projectTasksVm.loading == false"><small>{{projectTasksVm.tasks.totalElements}} tasks
                    </small></span>
                </div>
            </div>
        </div>

        <table class="table table-striped">
            <thead>

            <tr>
                <th>Project</th>
                <th>Name</th>
                <th>Description</th>
                <th>Status</th>
                <th>Assigned To</th>
                <th>Verified By</th>
                <th>Approved By</th>
                <th>Actions</th>
            </tr>
            </thead>

            <tbody>
            <tr ng-if="projectTasksVm.loading == true">
                <td colspan="12">
                           <span style="font-size: 15px;">
                               <img src="app/assets/bower_components/cassini-platform/images/loaders/loader19.gif"
                                    class="mr5">Loading Tasks...
                           </span>
                </td>
            </tr>

            <tr ng-if="projectTasksVm.loading == false && projectTasksVm.tasks.content.length == 0">
                <td colspan="12">No Tasks</td>

            </tr>
            <tr ng-repeat="task in projectTasksVm.tasks.content">
                <td style="width: 150px;">
                    {{task.project.name}}
                </td>
                <td style="width: 150px;">
                    <a href="" ui-sref="app.task.details({taskId: task.id, projectId: task.project.id})">
                        {{task.name}}</a></td>
                <td>{{task.description}}</td>
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
                <td>
                    <button title="delete task" class="btn btn-xs btn-danger"
                            ng-click="projectTasksVm.deleteTask(task)">
                        <i class="fa fa-trash"></i>
                    </button>
                </td>
            </tr>
            </tbody>
        </table>
    </div>
</div>
