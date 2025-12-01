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
        <%--<span style="font-weight: 600;font-size: 20px;padding-right: 10px;" translate>OPERATIONS</span>--%>

        <button class="btn btn-sm new-button" ng-click="allOperationVm.newOperation()"
                title="{{'NEW_OPERATION_TYPE' | translate}}" ng-if="hasPermission('operation','create')">
            <i class="las la-plus" aria-hidden="true"></i>
            <span>{{'NEW' | translate}} {{'OPERATION_TITLE' | translate }}</span>
        </button>

        <div class="btn-group">
            <button class="btn btn-sm btn-success" ng-click="allOperationVm.showTypeAttributes()"
                    title="{{'ALL_VIEW_ADD_ATTRIBUTES' | translate}}">
                <i class="fa fa-newspaper-o" style=""></i>
            </button>
        </div>
        <free-text-search on-clear="allOperationVm.resetPage" search-term="allOperationVm.searchText"
                          on-search="allOperationVm.freeTextSearch"
                          filter-search="allOperationVm.filterSearch"></free-text-search>
    </div>
    <div class="view-content no-padding" style="overflow-y: auto;padding: 10px;">
        <div class="responsive-table" style="padding: 10px;">
            <table class="table table-striped highlight-row">
                <thead>
                <tr>
                    <th style="width:1px;white-space:nowrap;"></th>
                    <th class="col-width-100" translate>NUMBER</th>
                    <th class="col-width-150" translate>TYPE</th>
                    <th class="col-width-150" translate>NAME</th>
                    <th class="description-column" translate>DESCRIPTION</th>
                    <th class="col-width-150" translate>MODIFIED_BY</th>
                    <th class="col-width-150" translate>MODIFIED_DATE</th>
                    <th class='added-column'
                        style="width: 150px;z-index: auto !important;"
                        ng-repeat="selectedAttribute in allOperationVm.selectedAttributes">
                        {{selectedAttribute.name}}
                        <i class="fa fa-times-circle"
                           ng-click="allOperationVm.removeAttribute(selectedAttribute)"
                           title={{allOperationVm.RemoveColumnTitle}}></i>
                    </th>
                    <th class="actions-col sticky-col sticky-actions-col" style="text-align:center; width: 80px;"
                        translate>ACTIONS
                    </th>
                </tr>
                </thead>
                <tbody>
                <tr ng-if="allOperationVm.loading == true">
                    <td colspan="25">
                        <span style="font-size: 15px;">
                            <img src="app/assets/bower_components/cassini-platform/images/loaders/loader19.gif"
                                 class="mr5">
                            <span translate>LOADING_OPERATIONS</span>
                        </span>
                    </td>
                </tr>

                <tr ng-if="allOperationVm.loading == false && allOperationVm.operations.content.length == 0">
                    <td colspan="25" style="background-color: #f9fbfe  !important;color: unset !important;">
                        <div class="no-data">
                            <img src="app/assets/no_data_images/Operations.png" alt="" class="image">

                            <div class="message ng-scope" translate>NO_OPERATIONS</div>
                            <div class="message ng-scope" style="font-size: 14px;color: #8f979d;" translate>NO_PERMISSION_MESSAGE</div>
                        </div>
                    </td>
                </tr>

                <tr ng-repeat="operation in allOperationVm.operations.content">
                    <td style="width:1px !important;white-space: nowrap;text-align: left;">
                        <all-view-icons object="operation"></all-view-icons>
                    </td>
                    <td class="col-width-100">
                        <a href="" ng-click="allOperationVm.showOperation(operation)"
                           title="{{'CLICK_TO_SHOW_DETAILS' | translate}}">
                            <span ng-bind-html="operation.number | highlightText: freeTextQuery"></span>
                        </a>
                    </td>

                    <td class="col-width-150">
                        <span ng-bind-html="operation.type.name | highlightText: freeTextQuery"></span>
                    </td>
                    <td class="col-width-150" title="{{operation.name}}">
                        <span ng-bind-html="operation.name | highlightText: freeTextQuery"></span>
                        {{operation.name.length > 25 ? '...' : ''}}
                    </td>
                    <td class="description-column" title="{{operation.description}}">
                        <span ng-bind-html="operation.description  | highlightText: freeTextQuery"></span>
                    </td>

                    <td class="col-width-150">{{operation.modifiedByObject.fullName}}</td>
                    <td class="col-width-150">{{operation.modifiedDate}}</td>
                    <td class="added-column"
                        ng-repeat="objectAttribute in allOperationVm.selectedAttributes">
                        <all-view-attributes object="operation"
                                             object-attribute="objectAttribute"></all-view-attributes>
                    </td>
                    <td class="text-center actions-col sticky-col sticky-actions-col">
                        <span class="row-menu" uib-dropdown dropdown-append-to-body style="min-width: 50px">
                            <i class="fa fa-ellipsis-h dropdown-toggle" uib-dropdown-toggle></i>
                            <ul uib-dropdown-menu class="dropdown-menu dropdown-menu-right" role="menu"
                                style="z-index: 9999 !important;">
                                <li>
                                    <a href=""
                                       ng-click="showPrintOptions(operation.id,'OPERATION')"
                                       translate>PREVIEW_AND_PRINT</a>
                                </li>
                                <li title="{{hasPermission('operation','delete') ? '' : noPermission}}"
                                    ng-class="{'cursor-override': !hasPermission('operation','delete')}">
                                    <a ng-click="allOperationVm.deleteOperation(operation)"
                                       ng-class="{'permission-text-disabled': !hasPermission('operation','delete')}"
                                       translate>
                                        DELETE
                                    </a>
                                </li>
                                <plugin-table-actions context="operation.all" object-value="operation"></plugin-table-actions>
                            </ul>
                        </span>
                    </td>
                </tr>
                </tbody>
            </table>
        </div>
        <div class="table-footer">
            <table-footer objects="allOperationVm.operations" pageable="allOperationVm.pageable"
                          previous-page="allOperationVm.previousPage"
                          next-page="allOperationVm.nextPage"></table-footer>
        </div>
    </div>
</div>
