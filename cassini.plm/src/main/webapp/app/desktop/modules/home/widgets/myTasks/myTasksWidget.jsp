<div class="sidepanel-widget user-tasks-widget">
    <style scoped>
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

    <div class="widget-header">
        <h5 translate>MY_TASKS</h5>
    </div>
    <div class="widget-body">
        <div class='responsive-table'
             style="height: 100%;overflow:auto;width: 100%;position: relative;">
            <table class="table table-striped highlight-row">
                <thead>
                <tr style="font-size: 14px;">
                    <th class="name-column3" translate>Name</th>
                    <th style="width: 100px;" translate>TYPE</th>
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
                    style="font-size: 14px;">
                    <td class="nowrap-column" style="vertical-align: middle;" ng-switch
                        on="userTask.sourceObject.objectType">
                            <span class="dot"
                                  title="Task is {{userTask.status.toLowerCase()}}"
                                  ng-class="{'blue-dot': userTask.status === 'PENDING',
                                    'green-dot': userTask.status === 'FINISHED',
                                    'red-dot': userTask.status === 'CANCELLED'}"></span>
                        <a href="" ng-click="userTasksVm.showChangeWorkflow(userTask)"
                           ng-switch-when="PLMWORKFLOWSTATUS">
                                <span ng-if="userTask.contextObject.attachedToObject.objectType == 'CHANGE'">
                                    <span ng-if="userTask.contextObject.attachedToObject.changeType == 'ECO'">{{userTask.contextObject.attachedToObject.ecoNumber}}</span>
                                    <span ng-if="userTask.contextObject.attachedToObject.changeType == 'DCO'">{{userTask.contextObject.attachedToObject.dcoNumber}}</span>
                                    <span ng-if="userTask.contextObject.attachedToObject.changeType == 'DCR'">{{userTask.contextObject.attachedToObject.crNumber}}</span>
                                    <span ng-if="userTask.contextObject.attachedToObject.changeType == 'MCO'">{{userTask.contextObject.attachedToObject.mcoNumber}}</span>
                                    <span ng-if="userTask.contextObject.attachedToObject.changeType == 'ECR'">{{userTask.contextObject.attachedToObject.crNumber}}</span>
                                    <span ng-if="userTask.contextObject.attachedToObject.changeType == 'WAIVER' || userTask.contextObject.attachedToObject.changeType == 'DEVIATION'">
                                        {{userTask.contextObject.attachedToObject.varianceNumber}}</span>
                                </span>
                                <span ng-if="userTask.contextObject.attachedToObject.objectType == 'OEMPARTMCO' || userTask.contextObject.attachedToObject.objectType == 'ITEMMCO'">
                                    {{userTask.contextObject.attachedToObject.mcoNumber}}
                                </span>
                                <span ng-if="userTask.contextObject.attachedToObject.objectType == 'PROBLEMREPORT'">
                                    {{userTask.contextObject.attachedToObject.prNumber}}
                                </span>
                                <span ng-if="userTask.contextObject.attachedToObject.objectType == 'NCR'">
                                    {{userTask.contextObject.attachedToObject.ncrNumber}}
                                </span>
                                <span ng-if="userTask.contextObject.attachedToObject.objectType == 'QCR'">
                                    {{userTask.contextObject.attachedToObject.qcrNumber}}
                                </span>
                                <span ng-if="userTask.contextObject.attachedToObject.objectType == 'INSPECTIONPLANREVISION'">
                                    {{userTask.contextObject.attachedToObject.plan.number}} ({{userTask.contextObject.attachedToObject.revision}})
                                </span>
                                <span ng-if="userTask.contextObject.attachedToObject.objectType == 'ITEMINSPECTION' || userTask.contextObject.attachedToObject.objectType == 'MATERIALINSPECTION'">
                                    {{userTask.contextObject.attachedToObject.inspectionNumber}}
                                </span>
                                <span ng-if="userTask.contextObject.attachedToObject.objectType == 'ITEMREVISION'">
                                    {{userTask.contextObject.itemNumber}} ({{userTask.contextObject.attachedToObject.revision}})
                                </span>
                                <span ng-if="userTask.contextObject.attachedToObject.objectType == 'MANUFACTURER'">
                                    {{userTask.contextObject.attachedToObject.name}}
                                </span>
                                <span ng-if="userTask.contextObject.attachedToObject.objectType == 'MANUFACTURERPART'">
                                    {{userTask.contextObject.attachedToObject.partNumber}}
                                </span>
                                <span ng-if="userTask.contextObject.attachedToObject.objectType == 'PROJECT'">
                                    {{userTask.contextObject.attachedToObject.name}}
                                </span>
                                <span ng-if="userTask.contextObject.attachedToObject.objectType == 'PROJECTACTIVITY'">
                                    {{userTask.contextObject.attachedToObject.name}}
                                </span>
                                <span ng-if="userTask.contextObject.attachedToObject.objectType == 'REQUIREMENT' || userTask.contextObject.attachedToObject.objectType == 'SPECIFICATION'">
                                    {{userTask.contextObject.attachedToObject.objectNumber}}
                                </span>
                                <span ng-if="userTask.contextObject.attachedToObject.objectType == 'MROWORKORDER' || userTask.contextObject.attachedToObject.objectType == 'PLMNPR'">
                                    {{userTask.contextObject.attachedToObject.number}}
                                </span>
                            [{{userTask.sourceObject.name}}]</a>
                        <a href="" ng-click="userTasksVm.showProjectActivity(userTask)"
                           ng-switch-when="PROJECTACTIVITY">{{userTask.contextObject.name}}
                            [{{userTask.sourceObject.name}}]</a>
                        <a href="" ng-click="userTasksVm.showProjectTask(userTask)" ng-switch-when="PROJECTTASK">{{userTask.contextObject.name}}
                            [{{userTask.sourceObject.name}}]</a>
                    </td>
                    <td style="vertical-align: middle;" ng-switch on="userTask.sourceObject.objectType">
                        <span ng-switch-when="PLMWORKFLOWSTATUS" class="label label-success">WORKFLOW TASK</span>
                        <span ng-switch-when="PROJECTACTIVITY" class="label label-primary">PROJECT ACTIVITY</span>
                        <span ng-switch-when="PROJECTTASK" class="label label-warning">PROJECT TASK</span>
                    </td>
                </tr>
                </tbody>
            </table>
        </div>
    </div>
</div>