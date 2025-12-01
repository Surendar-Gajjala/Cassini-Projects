<div>
    <div class='responsive-table' style="overflow-y: auto;left: 0px;top:45px;bottom: 50px;">
        <table class="table table-striped highlight-row">
            <thead>
            <tr>
                <th style="width: 60px;" translate="NAME"></th>
                <th class="description-column" translate="ITEM_ALL_DESCRIPTION"></th>
                <th style="width: 50px;" translate>PROJECT_MANAGER</th>
                <th style="width: 50px;" translate>PERCENT_COMPLETE</th>
                <th style="width: 50px; text-align: center;" translate>PLANNED_START_DATE</th>
                <th style="width: 50px; text-align: center;" translate>PLANNED_FINISH_DATE</th>
                <th style="width: 50px; text-align: center;" translate>ACTUAL_START_DATE</th>
                <th style="width: 50px; text-align: center;" translate>ACTUAL_FINISH_DATE</th>
                <th style="width: 50px; text-align: center;" translate="ITEM_ALL_SHARED_TYPE"></th>
                <th style="width: 50px; text-align: center;" translate="ITEM_ALL_PERMISSION"></th>
                <th style="width: 100px" translate="SHARED_TO"></th>
                <th style="width: 50px;" translate="SHARED_BY"></th>
                <th style="width: 50px; text-align: center;" translate="SHARED_ON"></th>
            </tr>
            </thead>
            <tbody>
            <tr ng-if="allSharesVm.loading == true">
                <td colspan="25">
            <span style="font-size: 15px;">
                <img src="app/assets/bower_components/cassini-platform/images/loaders/loader19.gif"
                     class="mr5">
                <span translate>LOADING_ITEMS</span>
            </span>
                </td>
            </tr>

            <tr ng-repeat="project in allSharesVm.sharedProjects.content">
                <td style="width: 100px;">
                    <a href="" ng-click="allSharesVm.showProject(project)"
                       title="{{'CLICK_TO_SHOW_DETAILS' | translate}}">
                        <span ng-bind-html="project.name  | highlightText: allSharesVm.sharedObjectFilter.searchQuery"></span>
                    </a>
                </td>
                <td class="description-column"><span
                        ng-bind-html="project.description  | highlightText: allSharesVm.sharedObjectFilter.searchQuery"></span>
                </td>
                <td style="width: 50px;">{{project.projectManagerObject.firstName}}</td>
                <td>
                    <div ng-if="project.percentComplete < 100"
                         class="project-progress progress text-center">
                        <div style="width:{{project.percentComplete}}%"
                             class="progress-bar progress-bar-primary progress-bar-striped active"
                             role="progressbar" aria-valuenow="40" aria-valuemin="0" aria-valuemax="100">
                            <span style="margin-left: 2px;">{{project.percentComplete}}%</span>
                        </div>
                    </div>
                    <div ng-if="project.percentComplete == 100"
                         class="project-progress progress text-center">
                        <div style="width:{{project.percentComplete}}%"
                             class="progress-bar progress-bar-success progress-bar-striped active"
                             role="progressbar" aria-valuenow="40" aria-valuemin="0" aria-valuemax="100">
                            <span style="margin-left: 10px;">{{project.percentComplete}}%</span>
                        </div>
                    </div>
                </td>
                <td style="width: 50px; text-align: center;">{{project.plannedStartDate}}</td>
                <td style="width: 50px; text-align: center;">{{project.plannedFinishDate}}</td>
                <td style="width: 50px; text-align: center;">{{project.actualStartDate}}</td>
                <td style="width: 50px; text-align: center;">{{project.actualFinishDate}}</td>
                <td style="width: 50px; text-align: center;">{{project.shareType}}</td>
                <td style="width: 50px; text-align: center;">
                    <div class="label"
                         ng-class="{'label-info': project.permission === 'READ','label-success': project.permission === 'WRITE'}">
                        {{project.permission}}
                    </div>
                </td>
                <td style="width: 100px">{{project.sharedTo}}</td>
                <td style="width: 50px;">{{project.sharedBy}}</td>
                <td style="width: 50px; text-align: center;">{{project.sharedOn}}</td>
            </tr>
            </tbody>
        </table>
    </div>
    <div class="table-footer" style="left: 0px;bottom: 10px;">
        <table-footer objects="allSharesVm.sharedProjects" pageable="allSharesVm.pageable"
                      previous-page="allSharesVm.previousPage"
                      next-page="allSharesVm.nextPage" page-size="allSharesVm.pageSize"></table-footer>
    </div>
</div>