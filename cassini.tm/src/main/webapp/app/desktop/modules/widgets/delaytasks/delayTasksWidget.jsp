<div class="panel panel-default panel-alt widget-messaging">
    <div class="panel-heading" style="background-color: #E4E7EA; padding:0; height: 56px; border: 1px solid #dddddd;">
        <div class="row">
            <div class="panel-title col-xs-12 col-sm-12 col-md-3 col-lg-3"
                 style="font-size:15px; padding: 20px 0 0 20px">
                Today Tasks
            </div>

        </div>
    </div>
    <div class="panel-body">
        <div class="widget-panel">
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
                </tr>
                </thead>
                <tbody>
                <tr ng-if="TasksWidgetVm.loading == true">
                    <td colspan="6">
                           <span style="font-size: 15px;">
                               <img src="app/assets/bower_components/cassini-platform/images/loaders/loader19.gif"
                                    class="mr5">Loading todayTasks...
                           </span>
                    </td>
                </tr>

                <tr ng-if="TasksWidgetVm.loading == false && TasksWidgetVm.todayTasks.content.length == 0">
                    <td colspan="6">No todayTasks</td>

                </tr>
                <tr ng-repeat="task in TasksWidgetVm.todayTasks.content">
                    <td style="width: 150px;">
                        <a href="" ui-sref="app.project.details({projectId: task.project.id})">{{task.project.name}}</a>
                    </td>
                    <td style="width: 150px;">
                        <a href=""
                           ui-sref="app.task.details({taskId: task.id, projectId: task.project.id})">
                            {{task.name}}</a></td>
                    <td>{{task.description}}</td>
                    <td>{{task.status}}</td>
                    <td>{{task.assignedToObject.firstName}}</td>
                    <td>{{task.verifiedByObject.firstName}}</td>
                    <td>{{task.approvedByObject.firstName}}</td>
                </tr>
                </tbody>
            </table>

            <div class="pull-right text-center">

                <%--<span ng-if="TasksWidgetVm.loading == false"><small>Page {{TasksWidgetVm.tasks.number+1}} of
                    {{TasksWidgetVm.tasks.totalPages}}
                </small></span>
                <br>--%>
                <div class="btn-group" style="margin-bottom: 0">
                    <button class="btn btn-xs btn-default"
                            ng-click="TasksWidgetVm.previousPage()"
                            ng-disabled="TasksWidgetVm.todayTasks.first">
                        Previous
                    </button>
                    <button class="btn btn-xs btn-default"
                            ng-click="TasksWidgetVm.nextPage()"
                            ng-disabled="TasksWidgetVm.todayTasks.last">
                        Next
                    </button>
                </div>
                <br>
                    <span ng-if="TasksWidgetVm.loading == false"><small>{{TasksWidgetVm.todayTasks.totalElements}} todayTasks
                    </small></span>
            </div>
        </div>
    </div>
    <!-- panel-body -->
</div>
<!-- panel -->
