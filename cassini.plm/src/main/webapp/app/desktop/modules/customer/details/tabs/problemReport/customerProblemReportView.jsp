<div>
    <div class='responsive-table'>
        <table class='table table-striped highlight-row'>
            <thead>
            <tr>
                <th style="width: 100px;" translate>PR_NUMBER</th>
                <th style="width: 150px" translate>TYPE</th>
                <th style="width: 150px" translate>PRODUCT</th>
                <th style="width: 150px;text-align: center" translate>REVISION</th>
                <th style="width: 150px" translate>PROBLEM</th>
                <th translate>DESCRIPTION</th>
                <th style="width: 150px" translate>DEFECT_TYPE</th>
                <th style="width: 150px" translate>SEVERITY</th>
                <th style="width: 150px" translate>STATUS</th>
                <th style="width: 150px" translate>DISPOSITION</th>
            </tr>
            </thead>
            <tbody>
            <tr ng-if="customerPrVm.loading == true">
                <td colspan="17"><img src="app/assets/bower_components/cassini-platform/images/loaders/loader19.gif"
                                      class="mr5"><span translate>LOADING_PRS</span>
                </td>
            </tr>
            <tr ng-if="customerPrVm.loading == false && customerPrVm.problemReports.length == 0">
                <td colspan="17" style="background-color: #f9fbfe  !important;color: unset !important;">
                    <div class="no-data">
                        <img src="app/assets/no_data_images/problemReport.png" alt="" class="image">

                        <div class="message">{{ 'NO_PRS' | translate}}</div>
                        <div class="message ng-scope" style="font-size: 14px;color: #8f979d;" translate>NO_PERMISSION_MESSAGE</div>
                    </div>
            </tr>
            <tr ng-if="customerPrVm.problemReports.length > 0"
                ng-repeat="problemReport in customerPrVm.problemReports">
                <td style="width: 100px;">
                    <a href="" ng-click="customerPrVm.showProblemReport(problemReport)"
                       title="{{'CLICK_TO_SHOW_DETAILS' | translate}}">
                        <span ng-bind-html="problemReport.prNumber | highlightText: freeTextQuery"></span>
                    </a>
                </td>
                <td title="{{problemReport.prType}}">
                    <span ng-bind-html="problemReport.prType  | highlightText: freeTextQuery"></span>
                    {{problemReport.prType.length > 18 ? '...' : ''}}
                </td>
                <td title="{{problemReport.product}}">
                    <span ng-bind-html="problemReport.product  | highlightText: freeTextQuery"></span>
                    {{problemReport.product.length > 18 ? '...' : ''}}
                </td>
                <td style="text-align: center;">
                    {{problemReport.revision}}
                </td>
                <td title="{{problemReport.problem}}" class="col-width-250">
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
                    <workflow-status workflow="problemReport"></workflow-status>
                    <span class="label label-warning" ng-if="problemReport.onHold">HOLD</span>
                </td>
                <td>
                    <span ng-bind-html="problemReport.disposition  | highlightText: freeTextQuery"></span>
                </td>
            </tr>
            </tbody>
        </table>
    </div>
</div>