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
                <th style="width: 100px; text-align: center;" translate>PLANNED_START_DATE</th>
                <th style="width: 100px; text-align: center;" translate>PLANNED_FINISH_DATE</th>
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
            <tr ng-repeat="activity in externalUserVm.sharedProjectActivities.content">
                <td style="width: 150px;">
                    <a href="" ng-click="externalUserVm.openActivityDetails(activity)"
                       title="{{'CLICK_TO_SHOW_DETAILS' | translate}}">
                        <span ng-bind-html="activity.name  | highlightText: externalUserVm.sharedObjectFilter.searchQuery"></span>
                    </a>
                </td>
                <td class="description-column"><span
                        ng-bind-html="activity.description  | highlightText: externalUserVm.sharedObjectFilter.searchQuery"></span>
                </td>
                <td style="width: 100px;">{{activity.assignedToObject.fullName}}</td>
                <td style="width: 100px;">
                    <div ng-if="activity.percentComplete < 100"
                         class="projectPlan-progress progress text-center">
                        <div style="width:{{activity.percentComplete}}%"
                             class="progress-bar progress-bar-primary progress-bar-striped active"
                             role="progressbar" aria-valuenow="40" aria-valuemin="0" aria-valuemax="100">
                            <span style="margin-left: 2px;">{{activity.percentComplete}}%</span>
                        </div>
                    </div>
                    <div ng-if="activity.percentComplete == 100"
                         class="project-progress progress text-center">
                        <div style="width:{{activity.percentComplete}}%"
                             class="progress-bar progress-bar-success progress-bar-striped active"
                             role="progressbar" aria-valuenow="40" aria-valuemin="0" aria-valuemax="100">
                            <span style="margin-left: 10px;">{{activity.percentComplete}}%</span>
                        </div>
                    </div>
                </td>
                <td style="width: 100px;">{{activity.projectName}}</td>
                <td style="width: 100px;">{{activity.phaseName}}</td>
                <td style="width: 100px; text-align: center;">{{activity.plannedStartDate}}</td>
                <td style="width: 100px; text-align: center;">{{activity.plannedFinishDate}}</td>
                <td style="width: 100px; text-align: center;">{{activity.shareType}}</td>
                <td style="width: 100px; text-align: center;">
                    <div class="label"
                         ng-class="{'label-info': activity.permission === 'READ','label-success': activity.permission === 'WRITE'}">
                        {{activity.permission}}
                    </div>
                </td>
                <td style="width: 100px;">{{activity.sharedBy}}</td>
                <td style="width: 100px; text-align: center;">{{activity.sharedOn}}</td>
            </tr>
            </tbody>
        </table>
    </div>
    <div class="table-footer" style="left: 0px;bottom: 10px !important;">
        <table-footer objects="externalUserVm.sharedProjectActivities" pageable="externalUserVm.pageable"
                      previous-page="externalUserVm.previousPage"
                      next-page="externalUserVm.nextPage" page-size="externalUserVm.pageSize"></table-footer>
    </div>
</div>