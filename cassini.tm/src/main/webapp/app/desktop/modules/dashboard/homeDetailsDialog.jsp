<div class="view-container">
    <style scoped>
        .user-container {
            border: 1px solid #ddd;
            margin-bottom: 10px;
            background-color: #fff;
        }
        .user-container .user-name {
            padding: 10px;
            font-size: 20px;
            cursor: pointer;
        }
        .user-container .user-tasks {
            border-top: 1px solid #ddd;
        }

        .user-container .user-tasks .user-task {
            border-bottom: 1px solid #ddd;
            padding: 10px;
            background-color: #F7F7F7;
        }
        .user-container .user-tasks .user-task:last-child {
            border: 0;
        }
        .user-container .user-tasks .user-task .task-label {
            color: grey;
            font-size: 12px;
        }
        .user-container .user-tasks .user-task .task-value {
            font-size: 16px;
        }
    </style>

    <div class="view-toolbar">
        <h3 style="margin-top: 5px;">{{homeDetailsVm.statusLabel}} Tasks</h3>
    </div>

    <div class="modal-body" style="padding: 20px; min-height: 250px; max-height: 500px; background-color: #eef0f3;overflow-y: auto">
        <div ng-if="homeDetailsVm.users.length == 0" style="font-size: 20px; line-height: 210px;vertical-align: middle; text-align:center; color:#bbbbbb;">
            No Tasks
        </div>
        <div class="user-container" ng-repeat="user in homeDetailsVm.users">
            <div class="user-name" ng-click="user.showTasks = !user.showTasks">
                <div class="row">
                    <div class="col-sm-10">
                        {{user.assignedToObject.firstName}}, {{user.assignedToObject.lastName}} ({{user.tasks.length}})
                    </div>
                    <div class="col-sm-2 text-right">
                        <i class="fa" ng-class="{'fa-caret-down':user.showTasks == false, 'fa-caret-up':user.showTasks == true}"></i>
                    </div>
                </div>
            </div>
            <div class="user-tasks" ng-if="user.showTasks == true">
                <div class="user-task" ng-repeat="task in user.tasks">
                    <div class="row">
                        <div class="col-sm-8" style="margin-bottom: 5px;">
                            <span class="task-label">Name</span><br>
                            <span class="task-value">{{task.name}}</span>
                        </div>

                        <div class="col-sm-4 text-right" style="margin-bottom: 5px;">
                            <span class="task-label">Assigned Date</span><br>
                            <span class="task-value">{{task.assignedDate}}</span>
                        </div>
                    </div>
                    <div class="row" style="margin-bottom: 5px;">
                        <div class="col-sm-12">
                            <span class="task-label">Description</span><br>
                            <span class="task-value">{{task.description}}</span>
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-sm-4">
                            <span class="task-label">Assigned To</span><br>
                            <span class="task-value">{{task.assignedToObject.firstName}}</span>
                        </div>
                        <div class="col-sm-4 text-center">
                            <span class="task-label">Verified By</span><br>
                            <span class="task-value">{{task.verifiedByObject.firstName}}</span>
                        </div>
                        <div class="col-sm-4 text-right">
                            <span class="task-label">Approved By</span><br>
                            <span class="task-value">{{task.approvedByObject.firstName}}</span>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>

<div class="modal-footer">
    <div class="row">
        <div class="col-md-6">
        </div>
        <div class="modal-buttons" class="col-md-6">
            <button type="button" class="btn btn-sm btn-primary" ng-click="homeDetailsVm.cancel()">Close</button>
        </div>
    </div>
</div>
