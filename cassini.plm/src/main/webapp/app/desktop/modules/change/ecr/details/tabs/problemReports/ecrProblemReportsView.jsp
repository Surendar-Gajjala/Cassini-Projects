<div class='responsive-table'>
    <table class='table table-striped highlight-row'>
        <thead>
        <tr>
            <th style="width: 30px;" ng-if="hasPermission('change','ecr','edit') && !ecr.isApproved && ecr.statusType != 'REJECTED'">
                <i class="la la-plus" style="cursor: pointer"
                   title="{{addPrsTitle}}"
                   ng-click="ecrProblemReportsVm.addProblemReports()"></i>
            </th>
            <th style="width: 100px;" translate>PR_NUMBER</th>
            <th class="col-width-150" translate>TYPE</th>
            <th class="col-width-200" translate>PRODUCT</th>
            <th style="width: 100px;text-align: center" translate>REVISION</th>
            <th class="col-width-200" translate>PROBLEM</th>
            <th class="col-width-250" translate>DESCRIPTION</th>
            <th style="width: 150px" translate>DEFECT_TYPE</th>
            <th style="width: 150px" translate>SEVERITY</th>
            <th style="width: 150px" translate>REPORTER_TYPE</th>
            <th style="width: 150px" translate>REPORTED_BY</th>
            <th style="width: 150px" translate>STATUS</th>
            <th style="width: 150px" translate>DISPOSITION</th>
            <th class="actions-col sticky-col sticky-actions-col" style="text-align:center; width: 80px;" translate>
                ACTIONS
            </th>
        </tr>
        </thead>
        <tbody>
        <tr ng-if="ecrProblemReportsVm.loading == true">
            <td colspan="14"><img src="app/assets/bower_components/cassini-platform/images/loaders/loader19.gif"
                                  class="mr5"><span translate>LOADING_PRS</span>
            </td>
        </tr>
        <tr ng-if="ecrProblemReportsVm.loading == false && ecrProblemReportsVm.problemReports.length == 0">
            <td colspan="12" style="background-color: #f9fbfe  !important;color: unset !important;">
                <div class="no-data">
                    <img src="app/assets/no_data_images/changeRequest.png" alt="" class="image">
                    <div class="message">{{ 'NO_PROBLEM_REPORTS' | translate}}</div>
                    <div class="message ng-scope" style="font-size: 14px;color: #8f979d;" translate>NO_PERMISSION_MESSAGE</div>
                </div>
            </td>
        </tr>
        <tr ng-repeat="problemReport in ecrProblemReportsVm.problemReports">
            <td ng-if="hasPermission('change','ecr','edit') && !ecr.isApproved && ecr.statusType != 'REJECTED'"></td>
            <td style="width: 100px;">
                <a href="" ui-sref="app.pqm.pr.details({problemReportId:problemReport.id})"
                   title="{{'CLICK_TO_SHOW_DETAILS' | translate}}">
                    <span ng-bind-html="problemReport.prNumber | highlightText: freeTextQuery"></span>
                </a>
            </td>
            <td class="col-width-150" title="{{problemReport.prType}}">
                <span ng-bind-html="problemReport.prType  | highlightText: freeTextQuery"></span>
                {{problemReport.prType.length > 18 ? '...' : ''}}
            </td>
            <td class="col-width-200" title="{{problemReport.product}}">
                <span ng-bind-html="problemReport.product  | highlightText: freeTextQuery"></span>
            </td>
            <td style="text-align: center;">
                {{problemReport.revision}}
            </td>
            <td class="col-width-200" title="{{problemReport.problem}}" class="col-width-250">
                <span ng-bind-html="problemReport.problem  | highlightText: freeTextQuery"></span>
            </td>
            <td title="{{problemReport.description}}" class="col-width-250">
                <span ng-bind-html="problemReport.description  | highlightText: freeTextQuery"></span>
            </td>
            <td title="{{problemReport.failureType}}">
                <span ng-bind-html="problemReport.failureType  | highlightText: freeTextQuery"></span>
            </td>
            <td>
                <span ng-bind-html="problemReport.severity | highlightText: freeTextQuery"></span>
            </td>
            <td>
                <reporter-type reporter-type="problemReport.reporterType"></reporter-type>
            </td>
            <td>{{problemReport.reportedBy || problemReport.otherReported}}</td>
            <td>
                <workflow-status workflow="problemReport"></workflow-status>
            </td>
            <td>
                <span ng-bind-html="problemReport.disposition  | highlightText: freeTextQuery"></span>
            </td>
            <td class="text-center actions-col sticky-col sticky-actions-col">
                <span class="row-menu" uib-dropdown dropdown-append-to-body style="min-width: 50px"
                      ng-if="!ecr.startWorkflow">
                    <i class="fa fa-ellipsis-h dropdown-toggle" uib-dropdown-toggle></i>
                    <ul uib-dropdown-menu class="dropdown-menu dropdown-menu-right" role="menu"
                        style="z-index: 9999 !important;">
                        <li>
                            <a ng-click="ecrProblemReportsVm.deleteProblemReport(problemReport)" translate>REMOVE_PROBLEM_REPORT</a>
                        </li>
                        <plugin-table-actions context="ecr.pr" object-value="problemReport"></plugin-table-actions>
                    </ul>
                </span>
            </td>
        </tr>
        </tbody>
    </table>
</div>