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
        <span style="font-weight: 600;font-size: 20px;padding-right: 10px;" translate>WORK_ORDERS</span>

        <button class="btn btn-sm new-button" ng-click="workOrdersVm.newWorkOrder()"
                ng-if="hasPermission('mroworkorder','create')"
                title="{{'NEW_WORK_ORDER_TYPE' | translate}}">
            <i class="las la-plus" aria-hidden="true"></i>
            <span>{{'NEW_WORK_ORDER_TYPE' | translate }}</span>
        </button>

        <div class="btn-group">
            <button class="btn btn-sm btn-success" ng-click="workOrdersVm.showTypeAttributes()"
                    ng-if="hasPermission('mroworkorder','create')"
                    title="{{'ALL_VIEW_ADD_ATTRIBUTES' | translate}}">
                <i class="fa fa-newspaper-o" style=""></i>
            </button>
            <button class="btn btn-sm btn-default"
                    style="" title="{{preferredPage}}"
                    ng-click="savePreferredPage()">
                <i class="fa fa fa-anchor" style=""></i>
            </button>
        </div>
        <free-text-search on-clear="workOrdersVm.resetPage" search-term="workOrdersVm.searchText"
                          on-search="workOrdersVm.freeTextSearch"
                          filter-search="workOrdersVm.filterSearch"></free-text-search>
    </div>
    <div class="view-content no-padding" style="overflow-y: auto;padding: 10px;">
        <div class="responsive-table" style="padding: 10px;">
            <table class="table table-striped highlight-row">
                <thead>
                <tr>
                    <th style="width: 100px" translate>NUMBER</th>
                    <th class="col-width-150" translate>TYPE</th>
                    <th class="col-width-200" translate>NAME</th>
                    <th class="col-width-250" translate>DESCRIPTION</th>
                    <th class="col-width-200" translate>ASSET</th>
                    <th style="width: 150px;" translate>REQUEST_PLAN</th>
                    <th style="width: 150px;" translate>ASSIGNED_TO</th>
                    <th style="width: 100px" translate>PRIORITY</th>
                    <th style="width: 100px" translate>STATUS</th>
                    <th class="col-width-200" translate>NOTES</th>
                    <th style="width: 150px" translate>MODIFIED_BY</th>
                    <th style="width: 150px" translate>MODIFIED_DATE</th>
                    <th class='added-column'
                        style="width: 150px;z-index: auto !important;"
                        ng-repeat="selectedAttribute in workOrdersVm.selectedAttributes">
                        {{selectedAttribute.name}}
                        <i class="fa fa-times-circle"
                           ng-click="workOrdersVm.removeAttribute(selectedAttribute)"
                           title={{workOrdersVm.RemoveColumnTitle}}></i>
                    </th>
                    <th class="actions-col sticky-col sticky-actions-col" style="text-align:center; width: 80px;"
                        translate>ACTIONS
                    </th>
                </tr>
                </thead>
                <tbody>
                <tr ng-if="workOrdersVm.loading == true">
                    <td colspan="25">
                        <span style="font-size: 15px;">
                            <img src="app/assets/bower_components/cassini-platform/images/loaders/loader19.gif"
                                 class="mr5">
                            <span translate>LOADING_WORK_ORDERS</span>
                        </span>
                    </td>
                </tr>

                <tr ng-if="workOrdersVm.loading == false && workOrdersVm.workOrders.content.length == 0">
                    <td colspan="25" style="background-color: #f9fbfe  !important;color: unset !important;">
                        <div class="no-data">
                            <img src="app/assets/no_data_images/WorkOrder.png" alt="" class="image">

                            <div class="message" translate>NO_WORK_ORDERS</div>
                            <div class="message ng-scope" style="font-size: 14px;color: #8f979d;" translate>NO_PERMISSION_MESSAGE</div>
                        </div>
                    </td>
                </tr>

                <tr ng-repeat="workOrder in workOrdersVm.workOrders.content">
                    <td style="width: 100px;">
                        <a href="" ng-click="workOrdersVm.showWorkOrder(workOrder)"
                           title="{{'CLICK_TO_SHOW_DETAILS' | translate}}">
                            <span ng-bind-html="workOrder.number | highlightText: freeTextQuery"></span>
                        </a>
                    </td>
                    <td style="width: 150px;">
                        <span ng-bind-html="workOrder.typeName | highlightText: freeTextQuery"></span>
                    </td>
                    <td class="col-width-200" title="{{workOrder.name}}">
                        <span ng-bind-html="workOrder.name | highlightText: freeTextQuery"></span>
                    </td>
                    <td class="col-width-250">
                        <span ng-bind-html="workOrder.description | highlightText: freeTextQuery"></span>
                    </td>
                    <td class="col-width-200">{{workOrder.assetName}}</td>
                    <td>
                        <a href="" title="{{'CLICK_TO_SHOW_DETAILS' | translate}}"
                           ng-click="workOrdersVm.showMaintenancePlan(workOrder)">{{workOrder.planNumber}}</a>
                        <a href="" title="{{'CLICK_TO_SHOW_DETAILS' | translate}}"
                           ng-click="workOrdersVm.showWorkRequest(workOrder)">{{workOrder.requestNumber}}</a>
                    </td>
                    <td>{{workOrder.assignedToName}}</td>
                    <td>
                        <priority object="workOrder"></priority>
                    </td>
                    <td>
                        <wo-status object="workOrder"></wo-status>
                    </td>
                    <td class="col-width-200">
                        <span ng-bind-html="workOrder.notes | highlightText: freeTextQuery"></span>
                    </td>
                    <td>{{workOrder.modifiedByName}}</td>
                    <td>{{workOrder.modifiedDate}}</td>
                    <td class="added-column"
                        ng-repeat="objectAttribute in workOrdersVm.selectedAttributes">
                        <all-view-attributes object="workOrder"
                                             object-attribute="objectAttribute"></all-view-attributes>
                    </td>
                    <td class="text-center actions-col sticky-col sticky-actions-col">
                        <span class="row-menu" uib-dropdown dropdown-append-to-body style="min-width: 50px">
                            <i class="fa fa-ellipsis-h dropdown-toggle" uib-dropdown-toggle></i>
                            <ul uib-dropdown-menu class="dropdown-menu dropdown-menu-right" role="menu"
                                style="z-index: 9999 !important;">
                                <li>
                                    <a href=""
                                       ng-click="showPrintOptions(workOrder.id,'MROWORKORDER')" translate>PREVIEW_AND_PRINT</a>
                                </li>
                                <li title="{{workOrder.status == 'FINISH' ? cannotDeleteFinishedWorkOrder :''}}"
                                    ng-class="{'cursor-override': !hasPermission('mroworkorder','edit')}">
                                    <a ng-click="workOrdersVm.deleteWorkOrder(workOrder)"
                                       ng-class="{'permission-text-disabled':workOrder.status == 'INPROGRESS' || workOrder.status == 'FINISH' || !hasPermission('mroworkorder','edit')}"
                                       translate>
                                        DELETE
                                    </a>
                                </li>
                                <plugin-table-actions context="workOrder.all" object-value="workOrder"></plugin-table-actions>
                            </ul>
                        </span>
                    </td>
                </tr>


                </tbody>
            </table>
        </div>
        <div class="table-footer">
            <table-footer objects="workOrdersVm.workOrders" pageable="workOrdersVm.pageable"
                          page-size="workOrdersVm.pageSize"
                          previous-page="workOrdersVm.previousPage"
                          next-page="workOrdersVm.nextPage"></table-footer>
        </div>
    </div>
</div>
