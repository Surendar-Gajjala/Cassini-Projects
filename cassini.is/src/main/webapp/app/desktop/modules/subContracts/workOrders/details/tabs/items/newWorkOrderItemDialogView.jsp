<div style="padding: 0px 10px;">
    <div class="row">
        <div class="col-md-12" style="padding:0px; height: auto;overflow: auto;">
            <table class="table table-striped">
                <tbody>
                <thead>
                <th style="vertical-align: middle;text-align: center">
                    <div class="ckbox ckbox-default" style="display: inline-block;"
                         ng-if="newWOItemVm.tasks.length > 1">
                        <input id="item{{$index}}" name="itemSelected" ng-value="true" type="checkbox"
                               ng-model="newWOItemVm.selectedAll"
                               ng-click="newWOItemVm.checkAll()">
                        <label for="item{{$index}}" class="item-selection-checkbox"></label>
                    </div>
                </th>
                <th style="vertical-align: middle;">Name</th>
                <th style="vertical-align: middle;">Description</th>
                <th style="vertical-align: middle">Percent Complete</th>
                <th style="vertical-align: middle;">Planned <br> Start Date</th>
                <th style="vertical-align: middle;">Planned <br> Finish Date</th>
                <th style="vertical-align: middle;">Actual <br> Start Date</th>
                </thead>
                <tbody>
                <tr ng-if="newWOItemVm.tasks.length == 0 && !newWOItemVm.loading">
                    <td colspan="12" style="padding-left: 30px;">No Tasks</td>
                </tr>
                <tr ng-repeat="task in newWOItemVm.tasks" ng-if="task.status != 'FINISHED' ">
                    <th style="width: 80px; text-align: center">
                        <div class="ckbox ckbox-default" style="display: inline-block;">
                            <input id="item{{$index}}" name="itemSelected" ng-value="true" type="checkbox"
                                   ng-click="newWOItemVm.select(task)"
                                   ng-model="task.selected">
                            <label for="item{{$index}}" class="item-selection-checkbox"></label>
                        </div>
                    </th>
                    <td>{{task.name}}</td>
                    <td>
                        <span title="{{task.description}}">{{task.description | limitTo: 20}}{{task.description.length > 20 ? '...' : ''}}</span>
                    </td>
                    <td>
                        <div class="task-progress progress text-center">
                            <div style="width:{{task.percentComplete}}%"
                                 class="progress-bar progress-bar-primary progress-bar-striped active"
                                 role="progressbar" aria-valuenow="40" aria-valuemin="0" aria-valuemax="100">
                                <span style="margin-left: 2px;">{{task.percentComplete}}%</span>
                            </div>
                        </div>
                    </td>
                    <td>{{task.plannedStartDate}}</td>
                    <td>{{task.plannedFinishDate}}</td>
                    <td>{{task.actualStartDate}}</td>
                </tr>
                </tbody>
            </table>
        </div>
        <br>
        <br>
    </div>
</div>
