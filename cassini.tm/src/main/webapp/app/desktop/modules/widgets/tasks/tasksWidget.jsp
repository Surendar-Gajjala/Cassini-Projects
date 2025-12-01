<div class="panel panel-default panel-alt widget-messaging">
    <div class="panel-heading" style="background-color: #E4E7EA; padding:0; height: 56px; border: 1px solid #dddddd;">
        <div class="row">
            <div class="panel-title col-xs-12 col-sm-12 col-md-8 col-lg-8">
                <div class="col-md-3"
                     style="font-size:15px; padding: 11px 0 0 20px">
                    <button class="btn btn-sm btn-default" style="width:100px"
                            ng-click="TasksWidgetVm.previousDayTasks()">
                        <i class="fa fa-arrow-left mr10"></i>PREVIOUS
                    </button>
                </div>

                <div class="panel-title col-xs-12 col-sm-12 col-md-6 col-lg-6"
                     style="font-size:15px; text-align:center; padding: 20px 0 0 20px">
                    {{TasksWidgetVm.taskHeading}}
                </div>
                <div class="col-md-3"
                     style="font-size:15px; padding: 11px 0 0 20px">
                    <button class="btn btn-sm btn-default" style="width:100px"
                            ng-click="TasksWidgetVm.nextDayTasks()">Next
                        <i class="fa fa-arrow-right mr10"></i>
                    </button>

                </div>
            </div>
            <div class="col-xs-12 col-sm-12 col-md-4 col-lg-4 pull-right" style="padding-top: 12px;">
                <div class="btn-group" style="margin-top: 3px;">
                    <button class="btn btn-xs btn-info" ng-click="TasksWidgetVm.loadTasks('Today')">
                        <span class="pull-right badge"
                              style="margin: 3px 0 0 10px; background-color:#D43F3A"></span>
                        <span>Today</span>
                    </button>
                    <button class="btn btn-xs btn-warning" ng-click="TasksWidgetVm.loadTasks('Pending')">
                        <span class="pull-right badge"
                              style="margin: 3px 0 0 10px; background-color:#d42d26"></span>
                        <span>Pending</span>
                    </button>
                    <button class="btn btn-xs btn-success" ng-click="TasksWidgetVm.loadTasks('Completed')">
                        <span class="pull-right badge"
                              style="margin: 3px 0 0 10px; background-color:#D43F3A"></span>
                        <span>Completed</span>
                    </button>
                </div>
            </div>
        </div>
    </div>
    <div class="panel-body">
        <div class="widget-panel">
            <div class="pull-right text-center">

                <div class="btn-group" style="margin-top: 3px;">
                    <button class="btn btn-xs btn-default"
                            ng-click="TasksWidgetVm.previousPage()"
                            ng-disabled="TasksWidgetVm.tasks.first">
                        Previous
                    </button>
                    <button class="btn btn-xs btn-default"
                            ng-click="TasksWidgetVm.nextPage()"
                            ng-disabled="TasksWidgetVm.tasks.last">
                        Next
                    </button>
                </div>
                <br>
                    <span ng-if="TasksWidgetVm.loading == false"><small>{{TasksWidgetVm.tasks.totalElements}} Tasks
                    </small></span>
            </div>
            <table class="table table-striped">
                <thead>
                <tr>
                    <th>Project</th>
                    <th>Name</th>
                    <th>Description</th>
                    <th>Status</th>
                    <th>Assigned Date</th>
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

                <tr ng-if="TasksWidgetVm.loading == true && TasksWidgetVm.tasks.content.length == 0">
                    <td colspan="6">No Tasks</td>

                </tr>
                <tr ng-repeat="task in TasksWidgetVm.tasks.content">
                    <td style="width: 150px;">
                        <a href="" ui-sref="app.project.details({projectId: task.project.id})">{{task.project.name}}</a>
                    </td>
                    <td style="width: 150px;">
                        <a href=""
                           ui-sref="app.task.details({taskId: task.id, projectId: task.project.id})">
                            {{task.name}}</a></td>
                    <td>{{task.description}}</td>
                    <td>{{task.status}}</td>
                    <td>{{task.assignedDate}}</td>
                    <td>{{task.assignedToObject.firstName}}</td>
                    <td>{{task.verifiedByObject.firstName}}</td>
                    <td>{{task.approvedByObject.firstName}}</td>
                </tr>
                </tbody>
            </table>


        </div>
    </div>
</div>