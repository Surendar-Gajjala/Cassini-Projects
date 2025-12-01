<div>
    <div class='responsive-table' style="overflow-y: auto;left: 0px;bottom: 50px;">
        <table class="table table-striped highlight-row">
            <thead>
            <tr>
                <th style="width: 150px;" translate="NAME"></th>
                <th class="description-column" translate="ITEM_ALL_DESCRIPTION"></th>
                <th translate>ASSIGNED_TO</th>
                <th style="width: 100px;" translate>PERCENT_COMPLETE</th>
                <th style="width: 100px;" translate>PROJECT</th>
                <th style="width: 100px;" translate>PHASE</th>
                <th style="width: 100px;" translate>ACTIVITY</th>
                <th style="width: 100px; text-align: center;" translate="ITEM_ALL_SHARED_TYPE"></th>
                <th style="width: 100px; text-align: center;;" translate="ITEM_ALL_PERMISSION"></th>
                <th style="width: 100px;" translate="SHARED_BY"></th>
                <th style="width: 100px; text-align: center;" translate="SHARED_ON"></th>
            </tr>
            </thead>
            <tbody>

            <tr ng-if="externalUserVm.loading == true">
                <td colspan="25">
            <span style="font-size: 15px;">
                <img src="app/assets/bower_components/cassini-platform/images/loaders/loader19.gif"
                     class="mr5">
                <span translate>LOADING_ITEMS</span>
            </span>
                </td>
            </tr>

            <tr ng-repeat="task in externalUserVm.sharedProjectTasks.content">
                <td style="width: 150px;">
                    <a ng-if="task.editMode == false || task.percentComplete > 0" href=""
                       ng-click="externalUserVm.showTaskDetails(task)"><span
                            ng-bind-html="task.name  | highlightText: externalUserVm.sharedObjectFilter.searchQuery"></span></a>
    <span ng-if="task.editMode == true && task.percentComplete == 0">
        <input ng-model="task.name"/>
    </span>
                </td>
                <td class="description-column"><span
                        ng-bind-html="task.description  | highlightText: externalUserVm.sharedObjectFilter.searchQuery"></span>
                </td>
                <td style="width: 100px;">{{task.assignedToObject.fullName}}</td>
                <td style="width: 100px;">
                    <div ng-if="task.percentComplete < 100 && task.editMode == false"
                         class="activityTask-progress progress text-center">
                        <div style="width:{{task.percentComplete}}%"
                             class="progress-bar progress-bar-primary progress-bar-striped active"
                             role="progressbar" aria-valuenow="40" aria-valuemin="0" aria-valuemax="100">
                            <span style="margin-left: 2px;">{{task.percentComplete}}%</span>
                        </div>
                    </div>
                    <div ng-if="task.percentComplete == 100 && task.editMode == false"
                         class="activityTask-progress progress text-center">
                        <div style="width:{{task.percentComplete}}%"
                             class="progress-bar progress-bar-success progress-bar-striped active"
                             role="progressbar" aria-valuenow="40" aria-valuemin="0" aria-valuemax="100">
                            <span style="margin-left: 2px;">{{task.percentComplete}}%</span>
                        </div>
                    </div>
    <span ng-if="task.editMode == true">
        <input type="number" class="form-control" style="width: 100px;" ng-model="task.percentComplete"/>
    </span>
                </td>
                <td style="width: 100px;">{{task.projectName}}</td>
                <td style="width: 100px;">{{task.phaseName}}</td>
                <td style="width: 100px;">{{task.activityName}}</td>
                <td style="width: 100px; text-align: center;">{{task.shareType}}</td>
                <td style="width: 100px; text-align: center;">
                    <div class="label"
                         ng-class="{'label-info': task.permission === 'READ','label-success': task.permission === 'WRITE'}">
                        {{task.permission}}
                    </div>
                </td>
                <td style="width: 100px;">{{task.sharedBy}}</td>
                <td style="width: 100px; text-align: center;">{{task.sharedOn}}</td>
            </tr>
            </tbody>
        </table>
    </div>
    <div class="table-footer" style="left: 0px;bottom: 10px !important;">
        <table-footer objects="externalUserVm.sharedProjectTasks" pageable="externalUserVm.pageable"
                      previous-page="externalUserVm.previousPage"
                      next-page="externalUserVm.nextPage" page-size="externalUserVm.pageSize"></table-footer>
    </div>
</div>