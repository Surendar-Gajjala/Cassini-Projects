<div>
    <style scoped>
        .myTask-progress.progress {
            background-color: #B0C7CF;
            height: 20px !important;
            margin: 0 !important;
        }

        .activity-node {
            background: transparent url("app/assets/images/activityIcon.png") no-repeat !important;
            height: 16px;
            padding-left: 20px;
        }

        .activity {
            font-weight: bold;
            font-size: 15px;
        }

        .name-column2 {
            word-wrap: break-word !important;
            min-width: 250px !important;
            width: 250px !important;
            white-space: normal !important;
            text-align: left !important;
        }

    </style>
<div class="panel-body">
    <div class="widget-panel" style="max-height: 400px;">
        <div class="responsive-table" style="padding: 5px;width:calc(100% - 1px);">
            <table class="table table-striped highlight-row">
                <thead>
                <tr style="font-size: 14px;">
                    <th class="name2-column" translate>NAME</th>
                    <th class="name2-column" translate>ACTIVITY</th>
                    <th class="name2-column" translate>PROJECT</th>
                    <th style="width: 150px" translate>PERCENT_COMPLETE</th>
                    <th style="width: 100px;" translate>STATUS</th>
                </tr>
                </thead>

                <tbody>
                <tr ng-if="myActivityTasksVm.loading == true">
                    <td colspan="7">
                        <span style="font-size: 15px;">
                            <img src="app/assets/bower_components/cassini-platform/images/loaders/loader19.gif"
                                 class="mr5"><span translate>LOADING_TASKS</span>
                        </span>
                    </td>
                </tr>

                <tr ng-if="myActivityTasksVm.loading == false && myActivityTasksVm.personTasks.content.length == 0">
                    <td colspan="7"><span translate>NO_TASKS</span></td>
                </tr>
                <tr ng-repeat="personTask in myActivityTasksVm.personTasks.content" style="font-size: 14px;">
                    <td class="name2-column">
                        <a href="" ng-click="myActivityTasksVm.showTaskDetails(personTask)">{{personTask.name}}</a>
                    </td>
                    <td class="name2-column">
                        <span>{{personTask.activityName}}</span>
                    </td>
                    <td class="name2-column">
                        <span>{{personTask.project}}</span>
                    </td>
                    <td style="width: 150px">
                        <div ng-if="personTask.percentComplete != 100"
                             class="myTask-progress progress text-center">
                            <div style="width:{{personTask.percentComplete}}%"
                                 class="progress-bar progress-bar-primary progress-bar-striped active"
                                 role="progressbar" aria-valuenow="40" aria-valuemin="0" aria-valuemax="100">
                                <span style="margin-left: 2px;">{{personTask.percentComplete}}%</span>
                            </div>
                        </div>
                        <div ng-if="personTask.percentComplete == 100"
                             class="myTask-progress progress text-center">
                            <div style="width:{{personTask.percentComplete}}%"
                                 class="progress-bar progress-bar-primary progress-bar-striped active"
                                 role="progressbar" aria-valuenow="40" aria-valuemin="0" aria-valuemax="100">
                                <span style="margin-left: 2px;">{{personTask.percentComplete}}%</span>
                            </div>
                        </div>
                    </td>
                    <td>
                        <task-status task="personTask"></task-status>
                    </td>
                </tr>
                </tbody>
            </table>
        </div>
    </div>
</div>
</div>