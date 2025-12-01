<div class="panel panel-default panel-alt widget-messaging" style="height: 500px">
    <div class="panel-heading" style="background-color: #E4E7EA; padding:0; height: 56px">
        <div class="row" style="padding-right: 10px;">
            <div class="panel-title col-sm-4" style="font-size:15px; padding: 20px 0 0 20px">Unfinished Tasks</div>
        </div>
    </div>
    <div class="panel-body">
        <treasure-overlay-spinner active='spinner.active == true'>
            <div class='responsive-table' style="min-height: 440px; max-height: 440px; overflow: auto;">
                <table class='table table-striped highlight-row'>
                    <thead>
                    <tr>
                        <th style="text-align: center;cursor: pointer" ng-click="order('name')">
                            Name
                            <i ng-if="predicate == 'name'" class="fa"
                               ng-class="{'fa-sort-desc': reverse, 'fa-sort-asc': !reverse}"></i>
                        </th>
                        <th style="text-align: center;cursor: pointer" ng-click="order('product')">
                            Status
                            <i ng-if="predicate == 'status'" class="fa"
                               ng-class="{'fa-sort-desc': reverse, 'fa-sort-asc': !reverse}"></i>
                        </th>
                        <th style="text-align: center;cursor: pointer" ng-click="order('sample')">
                            PercentCompleted
                            <i ng-if="predicate == 'percent'" class="fa"
                               ng-class="{'fa-sort-desc': reverse, 'fa-sort-asc': !reverse}"></i>
                        </th>
                        <th style="text-align: center;cursor: pointer" ng-click="order('percent')">
                            PlannedStartDate
                            <i ng-if="predicate == 'plannedStartDate'" class="fa"
                               ng-class="{'fa-sort-desc': reverse, 'fa-sort-asc': !reverse}"></i>
                        </th>
                        <th style="text-align: center;cursor: pointer" ng-click="order('plannedFinishDate')">
                            PlannedFinishDate
                            <i ng-if="predicate == 'plannedFinishDate'" class="fa"
                               ng-class="{'fa-sort-desc': reverse, 'fa-sort-asc': !reverse}"></i>
                        </th>
                        <th style="text-align: center;cursor: pointer" ng-click="order('actualFinishDate')">
                            ActualFinishDate
                            <i ng-if="predicate == 'plannedFinishDate'" class="fa"
                               ng-class="{'fa-sort-desc': reverse, 'fa-sort-asc': !reverse}"></i>
                        </th>
                    </tr>
                    </thead>
                    <tbody>
                    <tr ng-repeat="row in reportData">
                        <td style="text-align: center">{{row.name}}</td>
                        <td style="text-align: center">
              <span class="label" ng-class="{'label-success': row.status == 'NEW','label-info': row.status == 'ASSIGNED',
                                              'label-warning': row.status == 'INPROGRESS', 'label-danger': row.status == 'FINISHED'}">{{row.status}}</span>
                        </td>
                        <td>
                            <div class="task-progress progress text-center">
                                <div style="width:{{row.percent}}%" class="progress-bar progress-bar-primary"
                                     role="progressbar" aria-valuenow="40" aria-valuemin="0" aria-valuemax="100">
                                    <span style="margin-left: 2px;">{{row.percent}}%</span>
                                </div>
                            </div>
                        </td>
                        <td style="text-align: center">{{row.start}}</td>
                        <td style="text-align: center">{{row.finish}}</td>
                        <td style="text-align: center">{{row.actualFinish}}</td>
                    </tr>
                    </tbody>
                </table>
            </div>
        </treasure-overlay-spinner>
    </div>
</div>