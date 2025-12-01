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
        <span style="font-weight: 600;font-size: 20px;padding-right: 10px;" translate>PPAP</span>

        <button class="btn btn-sm new-button" ng-click="allPpapVm.newPPap()" id="newPpapButton"
                title="New PPAP" ng-if="hasPermission('ppap','create')">
            <i class="las la-plus" aria-hidden="true"></i>
            <span>{{'NEW' | translate}} {{'PPAP' | translate }}</span>
        </button>

        <div class="btn-group">
            <button class="btn btn-sm btn-success" ng-click="allPpapVm.showTypeAttributes()" id="attributesButton"
                    title="{{'ALL_VIEW_ADD_ATTRIBUTES' | translate}}">
                <i class="fa fa-newspaper-o"></i>
            </button>
            <button class="btn btn-sm btn-default" id="preferredPageButton"
                    title="{{preferredPage}}"
                    ng-click="savePreferredPage()">
                <i class="fa fa fa-anchor"></i>
            </button>
        </div>

        <free-text-search on-clear="allPpapVm.resetPage" search-term="allPpapVm.searchText"
                          on-search="allPpapVm.freeTextSearch"
                          filter-search="allPpapVm.filterSearch"></free-text-search>
    </div>
    <div class="view-content no-padding" style="overflow-y: auto;padding: 10px;">
        <div class="responsive-table" style="padding: 10px;">
            <table class="table table-striped highlight-row">
                <thead>
                <tr>
                    <th style="width: 100px" translate>PPAP_NUMBER</th>
                    <th class="col-width-200" translate>PPAP_NAME</th>
                    <th class="col-width-200" translate>PPAP_TYPE</th>
                    <th class="col-width-250" translate>DESCRIPTION</th>
                    <th style="width: 150px" translate>SUPPLIER</th>
                    <th style="width: 150px" translate>SUPPLIER_PART</th>
                    <th style="width: 150px" translate>STATUS</th>
                    <th style="width: 150px" translate>MODIFIED_BY</th>
                    <th style="width: 150px" translate>MODIFIED_DATE</th>
                    <th class='added-column'
                        style="width: 200px;z-index: auto !important;"
                        ng-repeat="selectedAttribute in allPpapVm.selectedAttributes">
                        {{selectedAttribute.name}}
                        <i class="fa fa-times-circle"
                           ng-click="allPpapVm.removeAttribute(selectedAttribute)"
                           title={{allPpapVm.RemoveColumnTitle}}></i>
                    </th>
                    <th class="actions-col sticky-col sticky-actions-col" style="text-align:center; width: 80px;"
                        translate>ACTIONS
                    </th>
                </tr>
                </thead>
                <tbody>
                <tr ng-if="allPpapVm.loading == true">
                    <td colspan="25">
                        <span style="font-size: 15px;">
                            <img src="app/assets/bower_components/cassini-platform/images/loaders/loader19.gif"
                                 class="mr5">
                            <span translate>LOADING_ALL_PPAPS</span>
                        </span>
                    </td>
                </tr>

                <tr ng-if="allPpapVm.loading == false && allPpapVm.ppaps.content.length == 0">
                    <td colspan="25" style="background-color: #f9fbfe  !important;color: unset !important;">
                        <div class="no-data">
                            <img src="app/assets/no_data_images/NCR.png" alt="" class="image">

                            <div class="message">No PPAPS</div>
                            <div class="message ng-scope" style="font-size: 14px;color: #8f979d;" translate>
                                NO_PERMISSION_MESSAGE
                            </div>
                        </div>
                    </td>
                </tr>

                <tr ng-repeat="ppap in allPpapVm.ppaps.content">
                    <td>
                        <a href="" ng-click="allPpapVm.showPpap(ppap)"
                           title="{{'CLICK_TO_SHOW_DETAILS' | translate}}">
                            <span ng-bind-html="ppap.number | highlightText: freeTextQuery"></span>
                        </a>
                    </td>
                    <td class="col-width-200" title="{{ppap.title}}">
                        <span ng-bind-html="ppap.name  | highlightText: freeTextQuery"></span>
                    </td>
                    <td class="col-width-200">
                        <span ng-bind-html="ppap.type.name | highlightText: freeTextQuery"></span>
                    </td>

                    <td title="{{ppap.description}}" class="col-width-250">
                        <span ng-bind-html="ppap.description  | highlightText: freeTextQuery"></span>
                    </td>
                    <td class="col-width-250">
                        <a href="" ng-click="allPpapVm.showSuppierDetails(ppap)"
                           title="{{'CLICK_TO_SHOW_DETAILS' | translate}}">
                            <span ng-bind-html="ppap.supplierName | highlightText: freeTextQuery"></span>
                        </a>
                    </td>
                    <td class="col-width-250">
                        <a href="" ng-click="allPpapVm.showMfrPartDetails(ppap.mfrPart)"
                           title="{{'CLICK_TO_SHOW_DETAILS' | translate}}">
                            <span ng-bind-html="ppap.mfrPart.partName | highlightText: freeTextQuery"></span>
                        </a>
                    </td>

                    <td>
                        <ppap-status object="ppap"></ppap-status>
                    </td>
                    <td>{{ppap.modifiedByObject.fullName}}</td>
                    <td>{{ppap.modifiedDate}}</td>
                    <td class="added-column"
                        ng-repeat="objectAttribute in allPpapVm.selectedAttributes">
                        <all-view-attributes object="ppap"
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
                                       ng-click="showPrintOptions(ppap.id, 'PPAP')" translate>PREVIEW_AND_PRINT</a>
                                </li>

                                <li title="{{ppap.status.phaseType == 'RELEASED'? cannotDeleteApprovedPpap:''}}">
                                    <a href="" ng-click="allPpapVm.deletePPAP(ppap)"
                                       ng-if="hasPermission('ppap','delete')"
                                       ng-class="{'disabled':ppap.status.phaseType == 'RELEASED'}" translate>DELETE</a>
                                </li>
                            </ul>
                        </span>
                    </td>
                </tr>
                </tbody>
            </table>
        </div>
        <div class="table-footer">
            <table-footer objects="allPpapVm.ppaps" pageable="allPpapVm.pageable"
                          previous-page="allPpapVm.previousPage"
                          next-page="allPpapVm.nextPage" page-size="allPpapVm.pageSize"></table-footer>
        </div>
    </div>
</div>
