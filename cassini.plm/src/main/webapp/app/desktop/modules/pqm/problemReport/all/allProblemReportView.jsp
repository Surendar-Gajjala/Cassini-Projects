<style>
    .view-content {
        position: relative;
    }

    .view-content .responsive-table {
        padding: 10px;
        position: absolute;
        bottom: 40px;
        top: 0;
        overflow: auto;
    }

    .view-content .responsive-table table thead th {
        position: -webkit-sticky;
        position: sticky;
        top: -10px;
        z-index: 5;
    }

    .view-content .table-footer {
        padding: 0 10px 0 10px;
        position: absolute;
        bottom: 0px !important;
        height: 40px;
        width: 100%;
        border-top: 1px solid #D3D7DB;
        display: table;
    }

    .view-content .table-footer > div {
        display: table-row;
        line-height: 30px;
    }

    .view-content .table-footer > div h5 {
        margin: 0;
    }

    .view-content .table-footer > div > div {
        display: table-cell;
        vertical-align: middle;
    }

    .view-content .table-footer > div > div > i {
        font-size: 16px;
    }

    a.disabled {
        cursor: not-allowed;
        color: lightgrey;
    }

    .sticky-col {
        position: sticky !important;
        position: -webkit-sticky !important;
    }

    .sticky-actions-col {
        right: -10px !important;
    }

    .table-striped > tbody > tr:nth-child(2n) > td.actions-col {
        /*background-color: #fff;*/
    }

    .table-striped > tbody > tr:nth-child(2n):hover > td.sticky-col {
        background-color: #d6e1e0;
    }

    #freeTextSearchDirective {
        top: 7px !important;
    }

    .flaticon-prize3:before {
        font-size: 15px;
    }

    .attributeTooltip {
        position: relative;
        display: inline-block;
    }

    .attributeTooltip .attributeTooltiptext {
        visibility: hidden;
        width: 200px;
        background-color: #7BB7EB;
        color: #141f9f;
        text-align: left;
        border-radius: 6px;
        padding: 5px 0;
        position: absolute;
        z-index: 1;
        top: -5px;
        bottom: auto;
        right: 100%;
        opacity: 0;
        transition: opacity 1s;
    }

    .attributeTooltip .attributeTooltiptext::after {
        content: "";
        position: absolute;
        top: 25%;
        left: 102%;
        margin-left: -5px;
        border-width: 5px;
        border-style: solid;
        border-color: transparent transparent transparent #7BB7EB;
    }

    .attributeTooltip:hover .attributeTooltiptext {
        visibility: visible;
        opacity: 1;
    }

</style>
<div class="view-container" fitcontent>
    <div class="view-toolbar">
        <span style="font-weight: 600;font-size: 20px;padding-right: 10px;" translate>PROBLEMREPORTS</span>

        <button class="btn btn-sm new-button" ng-click="allProblemReportVm.newProblemReport()"
                id="newProblemReportButton"
                title="{{createProblemReportTitle}}" ng-if="hasPermission('problemreport','create')">
            <i class="las la-plus" aria-hidden="true"></i>
            <span>{{'NEW' | translate}} {{'PR' | translate }}</span>
        </button>

        <div class="btn-group">
            <button class="btn btn-sm btn-success" ng-click="allProblemReportVm.showTypeAttributes()"
                    id="attributesButton"
                    title="{{allProblemReportVm.showAttributes}}">
                <i class="fa fa-newspaper-o" style=""></i>
            </button>
            <button class="btn btn-sm btn-default" id="preferredPageButton"
                    style="" title="{{preferredPage}}"
                    ng-click="savePreferredPage()">
                <i class="fa fa fa-anchor" style=""></i>
            </button>
        </div>
        <free-text-search on-clear="allProblemReportVm.resetPage" search-term="allProblemReportVm.searchText"
                          on-search="allProblemReportVm.freeTextSearch"
                          filter-search="allProblemReportVm.filterSearch"></free-text-search>
    </div>
    <div class="view-content no-padding" style="overflow-y: auto;padding: 10px;">
        <div class="responsive-table" style="padding: 10px;">
            <table class="table table-striped highlight-row">
                <thead>
                <tr>
                    <th style="width:20px;"></th>
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
                    <th></th>
                    <th style="width: 150px" translate>STATUS</th>
                    <th style="width: 150px" translate>DISPOSITION</th>
                    <th style="width: 150px" translate>MODIFIED_BY</th>
                    <th style="width: 150px" translate>MODIFIED_DATE</th>
                    <th class='added-column'
                        style="width: 150px;z-index: auto !important;"
                        ng-repeat="selectedAttribute in allProblemReportVm.selectedAttributes">
                        {{selectedAttribute.name}}
                        <i class="fa fa-times-circle"
                           ng-click="allProblemReportVm.removeAttribute(selectedAttribute)"
                           title={{allProblemReportVm.RemoveColumnTitle}}></i>
                    </th>
                    <th class="actions-col sticky-col sticky-actions-col" style="text-align:center; width: 80px;"
                        translate>ACTIONS
                    </th>
                </tr>
                </thead>
                <tbody>
                <tr ng-if="allProblemReportVm.loading == true">
                    <td colspan="25">
                        <span style="font-size: 15px;">
                            <img src="app/assets/bower_components/cassini-platform/images/loaders/loader19.gif"
                                 class="mr5">
                            <span translate>LOADING_PRS</span>
                        </span>
                    </td>
                </tr>

                <tr ng-if="allProblemReportVm.loading == false && allProblemReportVm.problemReports.content.length == 0">

                    <td colspan="25" style="background-color: #f9fbfe  !important;color: unset !important;">
                        <div class="no-data">
                            <img src="app/assets/no_data_images/problemReport.png" alt="" class="image">

                            <div class="message">{{ 'NO_PRS' | translate}}</div>
                            <div class="message ng-scope" style="font-size: 14px;color: #8f979d;" translate>NO_PERMISSION_MESSAGE</div>
                        </div>
                    </td>
                </tr>

                <tr ng-repeat="problemReport in allProblemReportVm.problemReports.content">
                    <td style="width: 20px;">
                        <i class="fa flaticon-prize3" title="Implemented" ng-if="problemReport.isImplemented"></i>
                    </td>
                    <td style="width: 100px;">
                        <a href="" ng-click="allProblemReportVm.showProblemReport(problemReport)"
                           title="{{'CLICK_TO_SHOW_DETAILS' | translate}}">
                            <span ng-bind-html="problemReport.prNumber | highlightText: freeTextQuery"></span>
                        </a>
                    </td>
                    <td class="col-width-150" title="{{problemReport.prType}}">
                        <span ng-bind-html="problemReport.prType  | highlightText: freeTextQuery"></span>
                    </td>
                    <td class="col-width-200" title="{{problemReport.product}}">
                        <span ng-bind-html="problemReport.product  | highlightText: freeTextQuery"></span>
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
                        <reporter-type reporter-type="problemReport.reporterType"></reporter-type>
                    </td>
                    <td>{{problemReport.reportedBy || problemReport.otherReported}}</td>
                    <td style="width: 1% !important;white-space: nowrap">
                        <i ng-if='problemReport.startWorkflow && !problemReport.finishWorkflow && !problemReport.cancelWorkflow'
                           style='font-size: 18px;color: #ffa800' class='la la-clock-o' title="In Progress"></i>
                        <i ng-if='problemReport.finishWorkflow' style='font-size: 18px;color: #1CAF9A'
                           class='la la-check-circle-o' title="Finished"></i>
                        <i ng-if='problemReport.cancelWorkflow' style='font-size: 18px;color: #f64e60'
                           class='la la-times-circle-o' title="Rejected"></i>
                    </td>
                    <td>
                        <workflow-status-settings workflow="problemReport"></workflow-status-settings>
                        <span class="label label-warning" ng-if="problemReport.onHold">HOLD</span>
                    </td>
                    <td>
                        <span ng-bind-html="problemReport.disposition  | highlightText: freeTextQuery"></span>
                    </td>
                    <td>{{problemReport.modifiedBy}}</td>
                    <td>{{problemReport.modifiedDate}}</td>
                    <td class="added-column"
                        ng-repeat="objectAttribute in allProblemReportVm.selectedAttributes">
                        <all-view-attributes object="problemReport"
                                             object-attribute="objectAttribute"></all-view-attributes>
                    </td>
                    <td class="text-center actions-col sticky-col sticky-actions-col">
                        <span class="row-menu" uib-dropdown dropdown-append-to-body style=""
                              ng-if="hasPermission('problemreport','delete')">
                         <i class="fa fa-ellipsis-h dropdown-toggle" uib-dropdown-toggle></i>
                            <ul uib-dropdown-menu class="dropdown-menu dropdown-menu-right" role="menu"
                                style="z-index: 9999 !important;">
                                <li>
                                    <a href=""
                                       ng-click="showPrintOptions(problemReport.id, 'PR')"
                                       translate>PREVIEW_AND_PRINT</a>
                                </li>
                                <tags-button object-type="problemReport.objectType" object="problemReport.id"
                                             tags-count="problemReport.tagsCount"></tags-button>
                                <li title="{{problemReport.released || problemReport.statusType == 'REJECTED'? cannotDeleteApprovedProblemReport:''}}">
                                    <a href="" ng-click="allProblemReportVm.deleteProblemReport(problemReport)"
                                       ng-class="{'disabled': problemReport.released == true || problemReport.statusType == 'REJECTED'}"
                                       translate>DELETE</a>
                                </li>
                                <plugin-table-actions object-value="problemReport" context="problemReport.all"></plugin-table-actions>
                            </ul>
                        </span>
                    </td>
                </tr>
                </tbody>
            </table>
        </div>
        <div class="table-footer">
            <table-footer objects="allProblemReportVm.problemReports" pageable="allProblemReportVm.pageable"
                          previous-page="allProblemReportVm.previousPage"
                          next-page="allProblemReportVm.nextPage"
                          page-size="allProblemReportVm.pageSize"></table-footer>
        </div>
    </div>
</div>
