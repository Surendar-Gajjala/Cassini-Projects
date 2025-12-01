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

    .actions-col {
        width: 60px;
        text-align: center;
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

    .added-column {
        text-align: left;
        width: 150px;
    }

    .added-column i {
        display: none;
        cursor: pointer;
        margin-left: 5px;
    }

    .added-column:hover i {
        display: inline-block;
    }

    .popover-title {
        font-size: 14px;
        font-weight: 600;
        text-align: center;
        line-height: 25px;
    }

    .popover {
        max-width: 500px;
        width: 500px;
    }

    .popover-content {
        max-height: 220px;
        overflow-y: auto;
    }

    .popover table {
        width: 497px;
        max-width: 100% !important;
    }

    .popover.bottom > .arrow::after {
        border-bottom-color: #f7f7f7;
    }

</style>
<div class="view-container" fitcontent>
    <div class="view-toolbar">
        <span style="font-weight: 600;font-size: 20px;padding-right: 10px;"
              ng-if="!allCustomObjectsVm.customObjectType.name.endsWith('s')">{{allCustomObjectsVm.customObjectType.name}}s</span>
        <span style="font-weight: 600;font-size: 20px;padding-right: 10px;"
              ng-if="allCustomObjectsVm.customObjectType.name.endsWith('s')">{{allCustomObjectsVm.customObjectType.name}}'</span>

        <button class="btn btn-sm new-button" ng-click="allCustomObjectsVm.newCustomObject()"
                title="Create new {{allCustomObjectsVm.customObjectType.name}}">
            <i class="las la-plus" aria-hidden="true"></i>
            <span>New {{allCustomObjectsVm.customObjectType.name}}</span>
        </button>
        <button class="btn btn-sm btn-info" title="{{allCustomObjectsVm.searchItemType}}"
                ng-click="allCustomObjectsVm.itemsSearch()">
            <i class="fa fa-search" style=""></i>
        </button>
        <button class="btn btn-sm btn-success" ng-click="allCustomObjectsVm.showAttributes()" id="attributesButton"
                title="{{allCustomObjectsVm.showAttributeTitle}}">
            <i class="fa fa-newspaper-o" style=""></i>
        </button>

        <free-text-search on-clear="allCustomObjectsVm.resetPage" search-term="allCustomObjectsVm.searchText"
                          on-search="allCustomObjectsVm.freeTextSearch"
                          filter-search="allCustomObjectsVm.filterSearch"></free-text-search>
    </div>
    <div class="view-content no-padding" style="overflow-y: auto;padding: 10px;">
        <div class="responsive-table" style="padding: 10px;">
            <table class="table table-striped highlight-row">
                <thead>
                <tr>
                    <th style="width:1%;white-space:nowrap;"></th>
                    <th class="col-width-100" translate>NUMBER</th>
                    <th class="col-width-150" translate>NAME</th>
                    <th class="col-width-150" translate>TYPE</th>
                    <th class="col-width-250" translate>DESCRIPTION</th>
                    <th class="col-width-150"
                        ng-if="allCustomObjectsVm.customObjectType.name == 'Supplier Performance Rating' 
                               || allCustomObjectsVm.customObjectType.name == 'CPI Form' || allCustomObjectsVm.customObjectType.name == '4MChange-Supplier'"
                        translate>SUPPLIER
                    </th>
                    <th style="width: 155px;z-index: auto !important;">
                        <span
                                translate="CREATED_DATE"></span>
                    </th>

                    <th style="width: 155px;z-index: auto !important;">
                        <span
                                translate="CREATED_BY"></span>
                    </th>
                    <th style="width: 155px;z-index: auto !important;">
                        <span
                                translate="MODIFIED_DATE"></span>
                    </th>

                    <th style="width: 155px;z-index: auto !important;">
                        <span
                                translate="MODIFIED_BY"></span>
                    </th>


                    <th class='added-column'
                        style="width: 100px;z-index: auto !important;"
                        ng-repeat="selectedAttribute in allCustomObjectsVm.selectAttributes">
                        {{selectedAttribute.name}}
                        <i ng-if="selectedAttribute.showInTable == false || selectedAttribute.objectType == 'CUSTOMOBJECT'"
                           class="fa fa-times-circle"
                           ng-click="allCustomObjectsVm.removeAttribute(selectedAttribute)"
                           title={{allCustomObjectsVm.RemoveColumnTitle}}></i>
                    </th>
                    <th class="actions-col sticky-col sticky-actions-col"
                        style="text-align:center; width: 60px !important;"
                        translate>ACTIONS
                    </th>
                </tr>
                </thead>
                <tbody>
                <tr ng-if="allCustomObjectsVm.loading == true">
                    <td colspan="25">
                        <span style="font-size: 15px;">
                            <img src="app/assets/bower_components/cassini-platform/images/loaders/loader19.gif"
                                 class="mr5">
                            <span translate>LOADING_CUSTOM_OBJECTS</span>
                        </span>
                    </td>
                </tr>

                <tr ng-if="allCustomObjectsVm.loading == false && allCustomObjectsVm.customObjects.content.length == 0">
                    <td colspan="25">No records for {{allCustomObjectsVm.customObjectType.name}}</td>
                </tr>

                <tr ng-repeat="customObject in allCustomObjectsVm.customObjects.content">
                    <td style="width:1% !important;white-space: nowrap;text-align: left;">
                        <all-view-icons object="customObject"></all-view-icons>
                    </td>
                    <td style="width:1% !important;white-space: nowrap;text-align: left;">
                        <a href="" ng-click="allCustomObjectsVm.showCustomObject(customObject)"
                           title="{{'CLICK_TO_SHOW_DETAILS' | translate}}">
                            <span ng-bind-html="customObject.number | highlightText: freeTextQuery"></span>
                        </a>
                    </td>
                    <td class="col-width-250">
                        <span ng-bind-html="customObject.name | highlightText: freeTextQuery"></span>
                    </td>
                    <td class="col-width-150">
                        <span ng-bind-html="customObject.type.name | highlightText: freeTextQuery"></span></td>
                    <td class="col-width-250">
                        <span ng-bind-html="customObject.description | highlightText: freeTextQuery"></span></td>
                    <td ng-if="allCustomObjectsVm.customObjectType.name == 'Supplier Performance Rating'
                               || allCustomObjectsVm.customObjectType.name == 'CPI Form' || allCustomObjectsVm.customObjectType.name == '4MChange-Supplier'">
                        <span>{{customObject.supplierObject.name}}</span>
                    </td>
                    <td style="width: 155px">
                        {{customObject.createdDate}}
                    </td>
                    <td style="width: 155px">
                        {{customObject.createdByObject.firstName}}
                    </td>
                    <td style="width: 155px">
                        {{customObject.modifiedDate}}
                    </td>
                    <td style="width: 155px">
                        {{customObject.modifiedByObject.firstName}}
                    </td>


                    <td class="added-column" ng-repeat="objectAttribute in allCustomObjectsVm.selectAttributes">
                        <all-view-attributes object="customObject"
                                             object-attribute="objectAttribute"></all-view-attributes>
                    </td>
                    <td class="text-center actions-col sticky-col sticky-actions-col">
                        <span class="row-menu" uib-dropdown dropdown-append-to-body style="">
                         <i class="fa fa-ellipsis-h dropdown-toggle" uib-dropdown-toggle></i>
                            <ul uib-dropdown-menu class="dropdown-menu dropdown-menu-right" role="menu"
                                style="z-index: 9999 !important;">
                                <tags-button object-type="'CUSTOMOBJECT'" object="customObject.id"
                                             tags-count="customObject.tags.length"></tags-button>
                                <li>
                                    <a ng-click="allCustomObjectsVm.deleteCustomObject(customObject)" translate>
                                        DELETE
                                    </a>
                                </li>
                                <plugin-table-actions context="custom.all"
                                                      object-value="customObject"></plugin-table-actions>
                            </ul>
                        </span>
                    </td>
                </tr>
                </tbody>
            </table>
        </div>
        <div class="table-footer">
            <table-footer objects="allCustomObjectsVm.customObjects" pageable="allCustomObjectsVm.pageable"
                          previous-page="allCustomObjectsVm.previousPage"
                          next-page="allCustomObjectsVm.nextPage"
                          page-size="allCustomObjectsVm.pageSize"></table-footer>
        </div>
    </div>
</div>
