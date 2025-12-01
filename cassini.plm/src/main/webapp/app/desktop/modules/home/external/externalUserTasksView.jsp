<div style="position: relative;">
    <style scoped>
        table td {
            padding-top: 0px !important;
            padding-bottom: 0px !important;
        }

        .ui-select-bootstrap > .ui-select-match > .btn {

        }

        .nowrap-column {
            white-space: nowrap !important;
            text-align: left !important;
        }

        .dot {
            height: 10px;
            width: 10px;
            border-radius: 50%;
            display: inline-block;
        }

        .dot.blue-dot {
            background-color: #123dff;
        }

        .dot.green-dot {
            background-color: #29b010;
        }

        .dot.red-dot {
            background-color: #c22f3c;
        }
    </style>
    <div style="overflow-y: auto; overflow-x: hidden; padding: 20px;">
        <div class="row" style="margin: 0;">
            <div class='responsive-table'
                 style="height: 100%;overflow:auto;width: 100%;position: relative;">
                <table class="table table-striped highlight-row">
                    <thead>
                    <tr style="font-size: 14px;">
                        <th class="name-column3" translate>Name</th>
                        <th style="width: 150px" translate>TYPE</th>
                    </tr>
                    </thead>

                    <tbody>
                    <tr ng-if="externalUserTaskVm.loading == true">
                        <td colspan="10">
                           <span style="font-size: 15px;">
                               <img src="app/assets/bower_components/cassini-platform/images/loaders/loader19.gif"
                                    class="mr5"><span translate>LOADING_MY_TASKS</span>
                           </span>
                        </td>
                    </tr>
                    <tr ng-if="externalUserTaskVm.loading == false && externalUserTaskVm.userTasks.length == 0">
                        <td colspan="10" translate>NO_TASKS</td>
                    </tr>
                    <tr ng-repeat="userTask in externalUserTaskVm.userTasks"
                        style="font-size: 14px;">
                        <td class="nowrap-column" style="vertical-align: middle;"
                            ng-switch on="userTask.taskType">
                            <span class="dot"
                                  title="Task is {{userTask.status.toLowerCase()}}"
                                  ng-class="{'blue-dot': userTask.status === 'PENDING',
                                    'green-dot': userTask.status === 'FINISHED',
                                    'red-dot': userTask.status === 'CANCELLED'}"></span>
                            <a href="" ng-click="externalUserTaskVm.showChangeWorkflow(userTask)" title="Click to show details"
                               ng-switch-when="PLMWORKFLOWSTATUS">{{userTask.name}}
                            </a>
                            <a href="" ng-click="externalUserTaskVm.showProjectActivity(userTask)" title="Click to show details"
                               ng-switch-when="PROJECTACTIVITY">{{userTask.name}}
                            </a>
                            <a href="" ng-click="externalUserTaskVm.showProjectTask(userTask)" title="Click to show details"
                               ng-switch-when="PROJECTTASK">{{userTask.name}}
                            </a>
                            <a href="" ng-click="externalUserTaskVm.showRequirementDetails(userTask)" title="Click to show details"
                               ng-switch-when="REQUIREMENT">{{userTask.name}}
                            </a>
                        </td>
                        <td style="vertical-align: middle;" ng-switch on="userTask.taskType">
                            <span ng-switch-when="PLMWORKFLOWSTATUS" class="label label-success">WORKFLOW TASK</span>
                            <span ng-switch-when="PROJECTACTIVITY" class="label label-primary">PROJECT ACTIVITY</span>
                            <span ng-switch-when="PROJECTTASK" class="label label-warning">PROJECT TASK</span>
                            <span ng-switch-when="REQUIREMENT" class="label label-info">REQUIREMENT</span>
                        </td>
                    </tr>
                    </tbody>
                </table>
            </div>
        </div>
    </div>
</div>
