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
        <span style="font-weight: 600;font-size: 20px;padding-right: 10px;" translate>INSPECTIONS</span>

        <button class="btn btn-sm new-button" ng-click="allInspectionsVm.newInspection()" id="newInspectionButton"
                title="{{createInspectionTitle}}" ng-if="hasPermission('inspection','create')">
            <i class="las la-plus" aria-hidden="true"></i>
            <span>{{'NEW' | translate}} {{'INSPECTION' | translate }}</span>
        </button>

        <div class="btn-group">
            <button class="btn btn-sm btn-success" ng-click="allInspectionsVm.showTypeAttributes()"
                    id="attributesButton"
                    title="{{allInspectionsVm.showAttributes}}">
                <i class="fa fa-newspaper-o" style=""></i>
            </button>
            <button class="btn btn-sm btn-default" id="preferredPageButton"
                    style="" title="{{preferredPage}}"
                    ng-click="savePreferredPage()">
                <i class="fa fa fa-anchor" style=""></i>
            </button>
        </div>
        <span class="form-check" style="padding:10px;border-right: none">
            <label class="form-check-label" style="margin-right: 5px">
                <input class="form-check-input" type="radio" name="inspectionTypes"
                       id="productType"
                       ng-click="allInspectionsVm.selectInspectionType('ITEMINSPECTION', $event)"
                       checked>
                <span style="padding: 2px;margin-left: 5px;" translate>ITEMS</span>
            </label>
            <label class="form-check-label" style="margin-right: 5px">
                <input class="form-check-input" type="radio" name="inspectionTypes"
                       id="materialType"
                       ng-click="allInspectionsVm.selectInspectionType('MATERIALINSPECTION', $event)">
                <span style="padding: 2px;margin-left: 5px;" translate>MATERIALS</span>
            </label>
        </span>
        <free-text-search on-clear="allInspectionsVm.resetPage" search-term="allInspectionsVm.searchText"
                          on-search="allInspectionsVm.freeTextSearch"
                          filter-search="allInspectionsVm.filterSearch"></free-text-search>
    </div>
    <div class="view-content no-padding" style="overflow-y: auto;padding: 10px;">
        <div class="responsive-table" style="padding: 10px;">
            <table class="table table-striped highlight-row">
                <thead>
                <tr>
                    <th style="width: 100px" translate>INSPECTION_NUMBER</th>
                    <th class="col-width-200" translate>INSPECTION_PLAN</th>
                    <th class="col-width-200" ng-if="allInspectionType == 'ITEMINSPECTION'" translate>PRODUCT</th>
                    <th class="col-width-200" ng-if="allInspectionType == 'MATERIALINSPECTION'" translate>MATERIAL</th>
                    <th style="width:100px;text-align: center" translate>REVISION</th>
                    <th class="col-width-150" translate>ASSIGNED_TO</th>
                    <th></th>
                    <th style="width: 100px;" translate>STATUS</th>
                    <th class="col-width-200" translate>DEVIATION_SUMMARY</th>
                    <th class="col-width-200" translate>NOTES</th>
                    <th style="width: 150px" translate>MODIFIED_BY</th>
                    <th style="width: 150px" translate>MODIFIED_DATE</th>
                    <th class='added-column'
                        style="width: 150px;z-index: auto !important;"
                        ng-repeat="selectedAttribute in allInspectionsVm.selectedAttributes">
                        {{selectedAttribute.name}}
                        <i class="fa fa-times-circle"
                           ng-click="allInspectionsVm.removeAttribute(selectedAttribute)"
                           title={{allInspectionsVm.RemoveColumnTitle}}></i>
                    </th>
                    <th class="actions-col sticky-col sticky-actions-col" style="text-align:center; width: 80px;"
                        translate>ACTIONS
                    </th>
                </tr>
                </thead>
                <tbody>
                <tr ng-if="allInspectionsVm.loading == true">
                    <td colspan="25">
                        <span style="font-size: 15px;">
                            <img src="app/assets/bower_components/cassini-platform/images/loaders/loader19.gif"
                                 class="mr5">
                            <span translate>LOADING_INSPECTIONS</span>
                        </span>
                    </td>
                </tr>

                <tr ng-if="allInspectionsVm.loading == false && allInspectionsVm.inspections.content.length == 0">
                    <td colspan="25" style="background-color: #f9fbfe  !important;color: unset !important;">
                        <div class="no-data">
                            <img src="app/assets/no_data_images/inspections.png" alt="" class="image">

                            <div class="message">{{ 'NO_INSPECTIONS' | translate}}</div>
                            <div class="message ng-scope" style="font-size: 14px;color: #8f979d;" translate>
                                NO_PERMISSION_MESSAGE
                            </div>
                        </div>
                    </td>

                </tr>

                <tr ng-repeat="inspection in allInspectionsVm.inspections.content">
                    <td>
                        <a href="" ng-click="allInspectionsVm.showInspection(inspection)"
                           title="{{'CLICK_TO_SHOW_DETAILS' | translate}}">
                            <span ng-bind-html="inspection.inspectionNumber | highlightText: freeTextQuery"></span>
                        </a>
                    </td>
                    <td class="col-width-200" title="{{inspection.inspectionPlan}}">
                        <span ng-bind-html="inspection.inspectionPlan | highlightText: freeTextQuery"></span>
                    </td>
                    <td class="col-width-200" ng-if="allInspectionType == 'ITEMINSPECTION'"
                        title="{{inspection.productName}}">
                        <span ng-bind-html="inspection.productName | highlightText: freeTextQuery"></span>
                    </td>
                    <td class="col-width-200" ng-if="allInspectionType == 'MATERIALINSPECTION'"
                        title="{{inspection.materialName}}">
                        <span ng-bind-html="inspection.materialName | highlightText: freeTextQuery"></span>
                    </td>
                    <td style="text-align: center">{{inspection.revision}}</td>
                    <td class="col-width-150">{{inspection.assignedTo}}</td>
                    <td style="width: 1% !important;white-space: nowrap">
                        <i ng-if='inspection.startWorkflow && !inspection.finishWorkflow && !inspection.cancelWorkflow'
                           style='font-size: 18px;color: #ffa800' class='la la-clock-o' title="In Progress"></i>
                        <i ng-if='inspection.finishWorkflow' style='font-size: 18px;color: #1CAF9A'
                           class='la la-check-circle-o' title="Finished"></i>
                        <i ng-if='inspection.cancelWorkflow' style='font-size: 18px;color: #f64e60'
                           class='la la-times-circle-o' title="Rejected"></i>
                    </td>
                    <td>
                        <workflow-status-settings workflow="inspection"></workflow-status-settings>
                        <span class="label label-warning" ng-if="inspection.onHold">HOLD</span>
                    </td>
                    <td class="col-width-200" title="{{inspection.deviationSummary}}">
                        <span ng-bind-html="inspection.deviationSummary  | highlightText: freeTextQuery"></span>
                    </td>
                    <td title="{{inspection.notes}}" class="col-width-200">
                        <span ng-bind-html="inspection.notes  | highlightText: freeTextQuery"></span>
                    </td>
                    <td>{{inspection.modifiedBy}}</td>
                    <td>{{inspection.modifiedDate}}</td>
                    <td class="added-column"
                        ng-repeat="objectAttribute in allInspectionsVm.selectedAttributes">
                        <all-view-attributes object="inspection"
                                             object-attribute="objectAttribute"></all-view-attributes>
                    </td>
                    <td class="text-center actions-col sticky-col sticky-actions-col">
                        <span class="row-menu" uib-dropdown dropdown-append-to-body style=""
                              ng-if="hasPermission('inspection','delete')">
                         <i class="fa fa-ellipsis-h dropdown-toggle" uib-dropdown-toggle></i>
                            <ul uib-dropdown-menu class="dropdown-menu dropdown-menu-right" role="menu"
                                style="z-index: 9999 !important;">
                                <li>
                                    <a href=""
                                       ng-click="showPrintOptions(inspection.id, allInspectionType)" translate>PREVIEW_AND_PRINT</a>
                                </li>
                                <tags-button object-type="inspection.objectType" object="inspection.id"
                                             tags-count="inspection.tagsCount"></tags-button>
                                <li title="{{inspection.released || inspection.statusType == 'REJECTED'? cannotDeleteApprovedInspection :''}}">
                                    <a href="" ng-click="allInspectionsVm.deleteInspection(inspection)"
                                       ng-class="{'disabled':inspection.released || inspection.statusType == 'REJECTED'}"
                                       translate>DELETE</a>
                                </li>
                                <plugin-table-actions context="inspection.all"
                                                      object-value="inspection"></plugin-table-actions>
                            </ul>
                        </span>
                    </td>
                </tr>
                </tbody>
            </table>
        </div>
        <div class="table-footer">
            <table-footer objects="allInspectionsVm.inspections" pageable="allInspectionsVm.pageable"
                          previous-page="allInspectionsVm.previousPage"
                          next-page="allInspectionsVm.nextPage" page-size="allInspectionsVm.pageSize"></table-footer>
        </div>
    </div>
</div>
