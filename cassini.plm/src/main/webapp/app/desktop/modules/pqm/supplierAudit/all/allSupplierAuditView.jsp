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
        <span style="font-weight: 600;font-size: 20px;padding-right: 10px;" translate>SUPPLIER_AUDIT</span>

        <button class="btn btn-sm new-button" ng-click="allSupplierAuditVm.newSupplierAudit()"
                id="newSupplierAudit"
                title="{{'NEW_SUPPLIER_AUDIT' | translate}}" ng-if="hasPermission('supplieraudit','create')">
            <i class="las la-plus" aria-hidden="true"></i>
            <span>{{'NEW_SUPPLIER_AUDIT' | translate }}</span>
        </button>

        <div class="btn-group">
            <button class="btn btn-sm btn-success" ng-click="allSupplierAuditVm.showTypeAttributes()" id="attributesButton"
                    title="{{'ALL_VIEW_ADD_ATTRIBUTES' | translate}}">
                <i class="fa fa-newspaper-o" style=""></i>
            </button>
            <button class="btn btn-sm btn-default" id="preferredPageButton"
                    style="" title="{{preferredPage}}"
                    ng-click="savePreferredPage()">
                <i class="fa fa fa-anchor" style=""></i>
            </button>
        </div>

        <free-text-search on-clear="allSupplierAuditVm.resetPage" search-term="allSupplierAuditVm.searchText"
                          on-search="allSupplierAuditVm.freeTextSearch"
                          filter-search="allSupplierAuditVm.filterSearch"></free-text-search>
    </div>
    <div class="view-content no-padding" style="overflow-y: auto;padding: 10px;">
        <div class="responsive-table" style="padding: 10px;">
            <table class="table table-striped highlight-row">
                <thead>
                <tr>
                    <th style="width: 100px" translate>NUMBER</th>
                    <th class="col-width-200" translate>TITLE</th>
                    <th class="col-width-150" translate>AUDIT_TYPE</th>
                    <th class="col-width-250" translate>DESCRIPTION</th>
                    <th style="width: 150px" translate>ASSIGNED_TO</th>
                    <th style="width: 100px" translate>STATUS</th>
                    <th style="width: 100px" translate>PLANNED_YEAR</th>
                    <th style="width: 150px" translate>CREATED_DATE</th>
                    <th style="width: 150px" translate>CREATED_BY</th>
                    <th style="width: 150px" translate>MODIFIED_DATE</th>
                    <th style="width: 150px" translate>MODIFIED_BY</th>
                    <th class='added-column'
                        style="width: 200px;z-index: auto !important;"
                        ng-repeat="selectedAttribute in allSupplierAuditVm.selectedAttributes">
                        {{selectedAttribute.name}}
                        <i class="fa fa-times-circle"
                           ng-click="allSupplierAuditVm.removeAttribute(selectedAttribute)"
                           title={{allSupplierAuditVm.RemoveColumnTitle}}></i>
                    </th>
                    <th class="actions-col sticky-col sticky-actions-col" style="text-align:center; width: 80px;"
                        translate>ACTIONS
                    </th>
                </tr>
                </thead>
                <tbody>
                <tr ng-if="allSupplierAuditVm.loading == true">
                    <td colspan="25">
                        <span style="font-size: 15px;">
                            <img src="app/assets/bower_components/cassini-platform/images/loaders/loader19.gif"
                                 class="mr5">
                            <span translate>LOADING_ALL_SUPPLIER_AUDITS</span>
                        </span>
                    </td>
                </tr>

                <tr ng-if="allSupplierAuditVm.loading == false && allSupplierAuditVm.supplierAudits.content.length == 0">
                    <td colspan="25" style="background-color: #f9fbfe  !important;color: unset !important;">
                        <div class="no-data">
                            <img src="app/assets/no_data_images/NCR.png" alt="" class="image">

                            <div class="message" translate>NO_SUPPLIER_AUDITS</div>
                            <div class="message ng-scope" style="font-size: 14px;color: #8f979d;" translate>
                                NO_PERMISSION_MESSAGE
                            </div>
                        </div>
                    </td>
                </tr>

                <tr ng-repeat="supplierAudit in allSupplierAuditVm.supplierAudits.content">
                    <td>
                        <a href="" ng-click="allSupplierAuditVm.showSupplierAudit(supplierAudit)"
                           title="{{'CLICK_TO_SHOW_DETAILS' | translate}}">
                            <span ng-bind-html="supplierAudit.number | highlightText: freeTextQuery"></span>
                        </a>
                    </td>
                    <td class="col-width-200" title="{{supplierAudit.title}}">
                        <span ng-bind-html="supplierAudit.name  | highlightText: freeTextQuery"></span>
                    </td>

                    <td title="{{supplierAudit.auditType}}" class="col-width-150">
                        <span ng-bind-html="supplierAudit.type.name  | highlightText: freeTextQuery"></span>
                    </td>
                    <td title="{{supplierAudit.description}}" class="col-width-250">
                        <span ng-bind-html="supplierAudit.description  | highlightText: freeTextQuery"></span>
                    </td>
                    <td title="{{supplierAudit.assignedTo}}" class="col-width-150">
                        <span ng-bind-html="supplierAudit.assignedToName  | highlightText: freeTextQuery"></span>
                    </td>
                    <td>
                        <ppap-status object="supplierAudit"></ppap-status>
                    </td>
                    <td>
                        <span ng-bind-html="supplierAudit.plannedYear  | highlightText: freeTextQuery"></span>
                    </td>
                    <td title="{{supplierAudit.createdDate}}" class="col-width-150">
                        <span ng-bind-html="supplierAudit.createdDate  | highlightText: freeTextQuery"></span>
                    </td>
                    <td title="{{supplierAudit.createdBy}}" class="col-width-150">
                        <span ng-bind-html="supplierAudit.createdByObject.fullName  | highlightText: freeTextQuery"></span>
                    </td>
                    <td title="{{supplierAudit.modifiedDate}}" class="col-width-150">
                        <span ng-bind-html="supplierAudit.modifiedDate  | highlightText: freeTextQuery"></span>
                    </td>
                    <td title="{{supplierAudit.modifiedBy}}" class="col-width-150">
                        <span ng-bind-html="supplierAudit.modifiedByObject.fullName  | highlightText: freeTextQuery"></span>
                    </td>
                    <td class="added-column"
                        ng-repeat="objectAttribute in allSupplierAuditVm.selectedAttributes">
                        <all-view-attributes object="supplierAudit"
                                             object-attribute="objectAttribute"></all-view-attributes>
                    </td>
                    <td class="text-center actions-col sticky-col sticky-actions-col"
                        style="text-align:center; width: 80px;">
                        <span class="row-menu" uib-dropdown dropdown-append-to-body style="">
                         <i class="fa fa-ellipsis-h dropdown-toggle" uib-dropdown-toggle></i>
                            <ul uib-dropdown-menu class="dropdown-menu dropdown-menu-right" role="menu"
                                style="z-index: 9999 !important;">
                                <li>
                                    <a href=""
                                       ng-click="showPrintOptions(supplierAudit.id, 'SUPPLIERAUDIT')" translate>PREVIEW_AND_PRINT</a>
                                </li>

                                <li title="{{ supplierAudit.status.phaseType == 'RELEASED'? cannotDeleteApprovedSupplierAudit:''}}">
                                    <a href="" ng-click="allSupplierAuditVm.deleteSupplierAudit(supplierAudit)"
                                       ng-if="hasPermission('supplieraudit','delete')"
                                       ng-class="{'disabled': supplierAudit.status.phaseType == 'RELEASED'}" translate>DELETE</a>
                                </li>
                            </ul>
                        </span>
                    </td>
                </tr>
                </tbody>
            </table>
        </div>
        <div class="table-footer">
            <table-footer objects="allSupplierAuditVm.supplierAudits" pageable="allSupplierAuditVm.pageable"
                          previous-page="allSupplierAuditVm.previousPage"
                          next-page="allSupplierAuditVm.nextPage"
                          page-size="allSupplierAuditVm.pageSize"></table-footer>
        </div>
    </div>
</div>
