<div style="height: 100%;">
    <style scoped>
        .nowrap-column {
            white-space: nowrap !important;
            text-align: left !important;
        }

        .col-width-500 {
            /* display: run-in;*/
            word-wrap: break-word;
            min-width: 500px;
            width: 500px !important;
            white-space: normal !important;
            text-align: left;
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

        .user-tasks-table > thead > tr > th,
        .user-tasks-table > tbody > tr > td {
            padding-left: 10px !important;
        }

        .responsive-table table thead th {
            position: -webkit-sticky;
            position: sticky;
            top: -10px;
            z-index: 5;
        }
    </style>

    <div class='responsive-table'
         style="height: 100%;overflow:auto;">
        <table class="table table-striped highlight-row user-tasks-table">
            <thead>
            <tr>
                <th class="name-column3" translate>Name</th>
                <th style="width: 200px;" translate>TYPE</th>
            </tr>
            </thead>

            <tbody>
            <tr ng-if="userTasksVm.loading == true">
                <td colspan="10">
                   <span style="font-size: 15px;">
                       <img src="app/assets/bower_components/cassini-platform/images/loaders/loader19.gif"
                            class="mr5"><span translate>LOADING_MY_TASKS</span>
                   </span>
                </td>
            </tr>
            <tr ng-if="userTasksVm.loading == false && userTasksVm.userTasks.length == 0">
                <td colspan="10" translate>NO_TASKS</td>
            </tr>
            <tr ng-repeat="userTask in userTasksVm.userTasks"
                style="">
                <td class="col-width-500" style="vertical-align: middle;" ng-switch on="userTask.taskType">
                        <span class="dot"
                              style="margin-right: 5px;"
                              title="Task is {{userTask.status.toLowerCase()}}"
                              ng-class="{'blue-dot': userTask.status === 'PENDING',
                                'green-dot': userTask.status === 'FINISHED',
                                'red-dot': userTask.status === 'CANCELLED'}"></span>
                    <a href="" ng-click="userTasksVm.showChangeWorkflow(userTask)" ng-switch-when="PLMWORKFLOWSTATUS">
                        {{userTask.name}}
                    </a>
                    <a href="" ng-click="userTasksVm.showProjectActivity(userTask)"
                       ng-switch-when="PROJECTACTIVITY">{{userTask.name}}</a>
                    <a href="" ng-click="userTasksVm.showProjectTask(userTask)" ng-switch-when="PROJECTTASK">{{userTask.name}}</a>
                    <a href="" ng-click="userTasksVm.showRequirementDetails(userTask)" ng-switch-when="REQUIREMENT">{{userTask.name}}</a>
                    <a href="" ng-click="userTasksVm.showDocuments(userTask)" ng-switch-when="DOCUMENT">{{userTask.name}}</a>
                    <a href="" ng-click="userTasksVm.showMfrPartFiles(userTask)"
                       ng-switch-when="MFRPARTINSPECTIONREPORT">{{userTask.name}}</a>
                    <a href="" ng-click="userTasksVm.showPPAPChecklist(userTask)" ng-switch-when="PPAPCHECKLIST">{{userTask.name}}</a>
                    <a href="" ng-click="userTasksVm.showSupplierAuditPlan(userTask)"
                       ng-switch-when="SUPPLIERAUDITPLAN">{{userTask.name}}</a>
                </td>
                <td style="vertical-align: middle;" ng-switch on="userTask.taskType">
                    <span ng-switch-when="PLMWORKFLOWSTATUS" class="label label-success">WORKFLOW TASK</span>
                    <span ng-switch-when="PROJECTACTIVITY" class="label label-primary">PROJECT ACTIVITY</span>
                    <span ng-switch-when="PROJECTTASK" class="label label-warning">PROJECT TASK</span>
                    <span ng-switch-when="REQUIREMENT" class="label label-info">REQUIREMENT</span>
                    <span ng-switch-when="DOCUMENT" class="label label-flat-olive">DOCUMENT</span>
                    <span ng-switch-when="MFRPARTINSPECTIONREPORT"
                          class="label label-flat-purple">INSPECTION REPORT</span>
                    <span ng-switch-when="PPAPCHECKLIST" class="label label-flat-brown">PPAP CHECKLIST</span>
                    <span ng-switch-when="SUPPLIERAUDITPLAN"
                          class="label label-flat-slategray">SUPPLIER AUDIT PLAN</span>
                </td>
            </tr>
            </tbody>
        </table>
    </div>
</div>