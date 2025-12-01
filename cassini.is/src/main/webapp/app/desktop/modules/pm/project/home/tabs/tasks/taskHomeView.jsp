<%--<div class="view-container" fitcontent>
    <div class="view-content no-padding" style="overflow-y: auto;">--%>
<style>
    .panel-body {
        height: 300px;
    }
</style>
<div class="panel panel-default">
    <div class="panel-heading" style="background-color: #f2f2f2;">
        <div class="panel-title text-center" style="font-size: 15px">Tasks Material Shortage</div>
    </div>
    <div class="panel-body">
        <table class="table" style="overflow: auto">
            <thead>
            <tr>
                <th>Task</th>
                <th>Item Number</th>
                <th>Resource Qty</th>
                <th>Inventory</th>
                <th>Shortage</th>
            </tr>
            </thead>
            <tbody>
            <tr ng-if="tasksVm.materialShortage.length == 0">
                <td colspan="25">No material shortage</td>
            </tr>
            <tr ng-repeat="resource in tasksVm.materialShortage">

                <td>{{resource.taskName}}</td>
                <td>{{resource.itemNumber}}</td>
                <td>{{resource.resourceQty}}</td>
                <td>{{resource.inventoryQty}}</td>
                <td>{{resource.shortage}}</td>
            </tr>
            </tbody>
        </table>
    </div>
</div>

<br>

<div class="row">
    <div class="col-md-6">
        <div class="panel panel-default">
            <div class="panel-heading" style="background-color: #f2f2f2;">
                <div class="panel-title text-center" style="font-size: 15px">Tasks Assigned To Me</div>
            </div>
            <div class="panel-body">
                <table class="table" style="overflow: auto">
                    <thead>
                    <tr>
                        <th>Name</th>
                        <th style="width: 200px;">Description</th>
                        <th>Actual Start Date</th>
                        <th>Actual Finish Date</th>
                    </tr>
                    </thead>
                    <tbody>
                    <tr ng-if="tasksVm.assignedToMe.length == 0">
                        <td colspan="25">No Tasks are available to view</td>
                    </tr>
                    <tr ng-repeat="task in tasksVm.assignedToMe">
                        <td>{{task.name}}</td>
                        <td title="{{task.description}}">{{task.description}}
                            {{task.description | limitTo: 15}}{{task.description.length > 15 ? '...' : ''}}
                        </td>
                        <td>{{task.actualStartDate}}</td>
                        <td>{{task.actualFinishDate}}</td>
                    </tr>
                    </tbody>
                </table>
            </div>
        </div>
    </div>
    <div class="col-md-6">
        <div class="panel panel-default">
            <div class="panel-heading" style="background-color: #f2f2f2;">
                <div class="panel-title text-center" style="font-size: 15px">Tasks Assigned By Me
                </div>
            </div>
            <div class="panel-body">
                <table class="table table-striped" style="overflow: auto">
                    <thead>
                    <tr>
                        <th>Name</th>
                        <th style="width: 200px;">Description</th>
                        <th>Assigned To</th>
                        <th>Actual Start Date</th>
                        <th>Actual Finish Date</th>
                    </tr>
                    </thead>
                    <tbody>
                    <tr ng-if="tasksVm.newTasks.length == 0">
                        <td colspan="25">No new tasks</td>
                    </tr>
                    <tr ng-repeat="task in tasksVm.newTasks">
                        <td>{{task.name}}</td>
                        <td title="{{task.description}}">{{task.description | limitTo: 15}}{{task.description.length >
                            15 ? '...' : ''}}
                        </td>
                        <td>{{task.personObject.fullName}}</td>
                        <td>{{task.actualStartDate}}</td>
                        <td>{{task.actualFinishDate}}</td>
                    </tr>
                    </tbody>
                </table>


            </div>
        </div>
    </div>
</div>

<div class="row">
    <div class="col-md-6">
        <div class="panel panel-default">
            <div class="panel-heading" style="background-color: #f2f2f2;">
                <div class="panel-title text-center" style="font-size: 15px">Overdue Tasks
                </div>
            </div>
            <div class="panel-body">
                <table class="table table-striped" style="max-height: 300px;min-height: 300px;overflow: auto">
                    <thead>
                    <tr>
                        <th>Name</th>
                        <th style="width: 200px;">Description</th>
                        <th>Site</th>
                        <th>Assigned To</th>
                        <th>Percentage Complete</th>
                    </tr>
                    </thead>
                    <tr ng-if="tasksVm.newTasks.length == 0">
                        <td colspan="25">No overdue tasks</td>
                    </tr>

                    <tbody ng-repeat="task in tasksVm.overDueTasks">
                    <tr>
                        <td>{{task.name}}</td>
                        <td title="{{task.description}}">{{task.description | limitTo: 15}}{{task.description.length >
                            15 ? '...' : ''}}
                        </td>
                        <td>{{task.siteObject.name}}</td>
                        <td>{{task.personObject.fullName}}</td>
                        <td>{{task.percentComplete}}</td>
                    </tr>
                    </tbody>
                </table>

            </div>
        </div>
    </div>
</div>
<%-- </div>
</div>--%>
