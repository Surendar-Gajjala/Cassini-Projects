<div style="max-height: 220px;">
    <table class="table table-striped" style="padding: 0px;max-height: 200px;overflow-y: auto;">
        <thead>
        <tr>
            <th style="">Name</th>
            <th style="width: 100px;">Status</th>
            <th style="width: 100px;" translate>PERCENT_COMPLETE</th>
        </tr>

        <tr ng-if="wbs.activityTasks.length == 0" style="border-top: 1px solid lightgrey;">
            <td colspan="10"
                style="width: 100px;background: #fcfffa !important;color: blue !important;text-align: left;"
                translate>NO_TASKS
            </td>
        </tr>
        </thead>
        <tbody>
        <tr ng-repeat="task in wbs.activityTasks">
            <td style="background: #fcfffa !important;text-align: left">
                {{task.name}}
            </td>
            <td style="width: 100px;background: #fcfffa !important;text-align: left">
                <task-status task="task"></task-status>
            </td>
            <td style="width: 100px;background: #fcfffa !important;text-align: left">
                <div ng-if="wbs.objectType == 'PROJECTACTIVITY' && task.percentComplete < 100"
                     class="projectPlan-progress progress text-center">
                    <div style="width:{{task.percentComplete}}%"
                         class="progress-bar progress-bar-primary progress-bar-striped active"
                         role="progressbar" aria-valuenow="40" aria-valuemin="0"
                         aria-valuemax="100">
                        <span style="margin-left: 2px;">{{task.percentComplete}}%</span>
                    </div>
                </div>
                <div ng-if="wbs.objectType == 'PROJECTACTIVITY' && task.percentComplete == 100"
                     class="projectPlan-progress progress text-center">
                    <div style="width:{{task.percentComplete}}%"
                         class="progress-bar progress-bar-success progress-bar-striped active"
                         role="progressbar" aria-valuenow="40" aria-valuemin="0"
                         aria-valuemax="100">
                        <span style="margin-left: 2px;">{{task.percentComplete}}%</span>
                    </div>
                </div>
            </td>
        </tr>
        </tbody>
    </table>
</div>


<%--
<div class="planTooltiptext"
     style="width: 300px !important;background-color: #9efcff;max-height: 200px !important;border: 1px solid grey;">
    <table>
        <thead>
        <tr style="border-bottom: 1px solid lightgrey;">
            <th style="width: 100px;background: #9efcff !important;">Name</th>
            <th style="width: 100px;background: #9efcff !important;">Status</th>
            <th style="width: 100px;background: #9efcff !important;">Percent</th>
        </tr>
        <tr ng-if="wbs.activityTasks.length == 0" style="border-top: 1px solid lightgrey;">
            <td style="width: 100px;background: #9efcff !important;color: blue !important;text-align: left;">
                No Tasks
            </td>
        </tr>
        </thead>
        <tbody ng-repeat="task in wbs.activityTasks">

        <tr>
            <td style="width: 100px;background: #9efcff !important;text-align: left">
                {{task.name}}
            </td>
            <td style="width: 100px;background: #9efcff !important;text-align: left">
                <task-status task="task"></task-status>
            </td>
            <td style="width: 100px;background: #9efcff !important;text-align: left">
                <div ng-if="wbs.objectType == 'PROJECTACTIVITY' && task.percentComplete < 100"
                     class="projectPlan-progress progress text-center">
                    <div style="width:{{task.percentComplete}}%"
                         class="progress-bar progress-bar-primary progress-bar-striped active"
                         role="progressbar" aria-valuenow="40" aria-valuemin="0"
                         aria-valuemax="100">
                        <span style="margin-left: 2px;">{{task.percentComplete}}%</span>
                    </div>
                </div>
                <div ng-if="wbs.objectType == 'PROJECTACTIVITY' && task.percentComplete == 100"
                     class="projectPlan-progress progress text-center">
                    <div style="width:{{task.percentComplete}}%"
                         class="progress-bar progress-bar-success progress-bar-striped active"
                         role="progressbar" aria-valuenow="40" aria-valuemin="0"
                         aria-valuemax="100">
                        <span style="margin-left: 2px;">{{task.percentComplete}}%</span>
                    </div>
                </div>
            </td>
        </tr>
        </tbody>
    </table>

    .planTooltip {
        position: absolute;
        display: inline-block;
    }

    .planTooltip .planTooltiptext {
        visibility: hidden;
        width: 120px;
        background-
        color: #fff;
        text-align: center;
        border-radius: 6px;
        padding: 5px;

        /* Position the tooltip */
        position: absolute;
        overflow: auto;
        z-index: 1;
        bottom: 15px;
        left: 0%;
    }

    .planTooltip:hover .planTooltiptext {
        visibility: visible;
    }
</div>--%>
