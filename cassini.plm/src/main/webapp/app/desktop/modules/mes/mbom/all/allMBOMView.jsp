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
        <span style="font-weight: 600;font-size: 20px;padding-right: 10px;" translate>MBOM</span>

        <button class="btn btn-sm new-button" ng-click="allMBOMVm.newMBOM()"
                title="{{'NEW_MBOM' | translate}}" ng-if="hasPermission('mbom','create')">
            <i class="las la-plus" aria-hidden="true"></i>
            <span>{{'NEW_MBOM' | translate}}</span>
        </button>

        <div class="btn-group">
            <button class="btn btn-sm btn-success" ng-click="allMBOMVm.showTypeAttributes()"
                    title="{{'ALL_VIEW_ADD_ATTRIBUTES' | translate}}">
                <i class="fa fa-newspaper-o" style=""></i>
            </button>
            <button class="btn btn-sm btn-default"
                        style="" title="{{preferredPage}}"
                        ng-click="savePreferredPage()">
                    <i class="fa fa fa-anchor" style=""></i> 
            </button>
        </div>
        <free-text-search on-clear="allMBOMVm.resetPage" search-term="allMBOMVm.searchText"
                          on-search="allMBOMVm.freeTextSearch"
                          filter-search="allMBOMVm.filterSearch"></free-text-search>
    </div>
    <div class="view-content no-padding" style="overflow-y: auto;padding: 10px;">
        <div class="responsive-table" style="padding: 10px;">
            <table class="table table-striped highlight-row">
                <thead>
                <tr>
                    <th style="width:1px;white-space:nowrap;"></th>
                    <th style="width: 100px" translate>NUMBER</th>
                    <th style="width: 150px" translate>TYPE</th>
                    <th style="width: 150px" translate>NAME</th>
                    <th style="width: 200px" translate>DESCRIPTION</th>
                    <th style="width: 150px" translate>ITEM_NAME</th>
                    <th style="width: 100px;text-align: center;" translate>REVISION</th>
                    <th style="width: 150px" translate>LIFECYCLE</th>
                    <th style="width: 150px" translate>RELEASED_REJECTED_DATE</th>
                    <th style="width: 150px" translate>MODIFIED_BY</th>
                    <th style="width: 150px" translate>MODIFIED_DATE</th>
                    <th class='added-column'
                        style="width: 150px;z-index: auto !important;"
                        ng-repeat="selectedMbomAttribute in allMBOMVm.selectedMbomAttributes">
                        {{selectedMbomAttribute.name}}
                        <i class="fa fa-times-circle"
                           ng-click="allMBOMVm.removeAttribute(selectedMbomAttribute)"
                           title={{allMBOMVm.RemoveColumnTitle}}></i>
                    </th>
                    <th class="actions-col sticky-col sticky-actions-col" style="text-align:center; width: 80px;"
                        translate>ACTIONS
                    </th>
                </tr>
                </thead>
                <tbody>
                <tr ng-if="allMBOMVm.loading == true">
                    <td colspan="25">
                        <span style="font-size: 15px;">
                            <img src="app/assets/bower_components/cassini-platform/images/loaders/loader19.gif"
                                 class="mr5">
                            <span translate>LOADING_MBOM</span>
                        </span>
                    </td>
                </tr>
                <tr ng-if="allMBOMVm.loading == false && allMBOMVm.mboms.content.length == 0">
                    <td colspan="25" style="background-color: #f9fbfe  !important;color: unset !important;">
                        <div class="no-data">
                            <img src="app/assets/no_data_images/MBOM.png" alt="" class="image">

                            <div class="message" translate>NO_MBOM</div>
                            <div class="message ng-scope" style="font-size: 14px;color: #8f979d;" translate>
                                NO_PERMISSION_MESSAGE
                            </div>
                        </div>
                    </td>
                </tr>
                <tr ng-repeat="mbom in allMBOMVm.mboms.content">
                        <td  style="width:1px !important;white-space: nowrap;text-align: left;">
                            <all-view-icons object="mbom"></all-view-icons>
                        </td>
                    <td class="col-width-100">
                        <a href="" ng-click="allMBOMVm.showMBOM(mbom)" id="plantNumber"
                           title="{{'CLICK_TO_SHOW_DETAILS' | translate}}">
                            <span ng-bind-html="mbom.number | highlightText: freeTextQuery"></span>
                        </a>
                    </td>
                    <td class="col-width-150">
                        <span ng-bind-html="mbom.typeName | highlightText: freeTextQuery"></span>
                    </td>
                    <td class="col-width-200">
                        <span ng-bind-html="mbom.name | highlightText: freeTextQuery"></span>
                    </td>
                    <td class="col-width-250" title="{{mbom.description}}">
                        <span ng-bind-html="mbom.description  | highlightText: freeTextQuery"></span>
                    </td>
                    <td>
                        <a href="" ng-click="allMBOMVm.showItem(mbom)"
                        title="{{'CLICK_TO_SHOW_DETAILS' | translate}}">
                         <span>{{mbom.itemName}} - {{mbom.itemRevision}}</span>
                     </a>
                    </td>
                    <td class="col-width-100" style="text-align: center">
                        <a href="" ng-click="allMBOMVm.showMBOMRevisionHistory(mbom)"
                        title="{{'ITEM_DETAILS_REVISION_HISTORY' | translate}}">
                         {{mbom.revision}}
                     </a>
                    </td>
                    <td>
                        <item-status item="mbom"></item-status>
                    </td>
                    <td>{{mbom.releasedDate}}</td>
                    <td>{{mbom.modifiedByName}}</td>
                    <td>{{mbom.modifiedDate}}</td>
                    <td class="added-column"
                        ng-repeat="objectAttribute in allMBOMVm.selectedMbomAttributes">
                        <all-view-attributes object="mbom"
                                             object-attribute="objectAttribute"></all-view-attributes>
                    </td>
                    <td class="text-center actions-col sticky-col sticky-actions-col">
                        <span class="row-menu" uib-dropdown dropdown-append-to-body style="min-width: 50px">
                        <i class="fa fa-ellipsis-h dropdown-toggle" uib-dropdown-toggle></i>
                            <ul uib-dropdown-menu class="dropdown-menu dropdown-menu-right" role="menu"
                                style="z-index: 9999 !important;">
                                <li>
                                    <a href=""
                                       ng-click="showPrintOptions(mbom.id,'PLANT')" translate>PREVIEW_AND_PRINT</a>
                                </li>
                                <li title="{{mbom.lifeCyclePhase.phaseType == 'RELEASED'  ? cannotDeleteApprovedMbom:'' || mbom.rejected == true ? cannotDeleteRejectedMbom:''}}">
                                    <a href="" ng-click="allMBOMVm.deleteMBOM(mbom)"
                                    ng-if="hasPermission('mbom','delete')"
                                    ng-class="{'disabled': mbom.lifeCyclePhase.phaseType == 'RELEASED' || mbom.rejected == true}" translate>DELETE</a>
                                       
                                </li>
                                <plugin-table-actions context="mbom.all" object-value="mbom"></plugin-table-actions>
                            </ul>
                        </span>
                    </td>
                </tr>

                </tbody>
            </table>
        </div>
        <div class="table-footer">
            <table-footer objects="allMBOMVm.mboms" pageable="allMBOMVm.pageable"
                          previous-page="allMBOMVm.previousPage" next-page="allMBOMVm.nextPage"></table-footer>
        </div>
    </div>
</div>
