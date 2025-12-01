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
</style>
<div class="view-container" fitcontent>
    <div class="view-toolbar">
        <span style="font-weight: 600;font-size: 20px;padding-right: 10px;" translate>MBOM</span>

        <button class="btn btn-sm new-button" ng-click="allMBOMInstanceVm.newMBOM()"
                title="{{'NEW_MBOM' | translate}}" ng-if="hasPermission('mbomInstance','create')">
            <i class="las la-plus" aria-hidden="true"></i>
            <span>{{'NEW_MBOM' | translate}}</span>
        </button>

        <div class="btn-group">
            <button class="btn btn-sm btn-success" ng-click="allMBOMInstanceVm.showTypeAttributes()"
                    title="{{'ALL_VIEW_ADD_ATTRIBUTES' | translate}}">
                <i class="fa fa-newspaper-o" style=""></i>
            </button>
            <button class="btn btn-sm btn-default"
                    style="" title="{{preferredPage}}"
                    ng-click="savePreferredPage()">
                <i class="fa fa fa-anchor" style=""></i>
            </button>
        </div>
        <free-text-search on-clear="allMBOMInstanceVm.resetPage" search-term="allMBOMInstanceVm.searchText"
                          on-search="allMBOMInstanceVm.freeTextSearch"
                          filter-search="allMBOMInstanceVm.filterSearch"></free-text-search>
    </div>
    <div class="view-content no-padding" style="overflow-y: auto;padding: 10px;">
        <div class="responsive-table" style="padding: 10px;">
            <table class="table table-striped highlight-row">
                <thead>
                <tr>
                    <th style="width:1px;white-space:nowrap;"></th>
                    <th style="width: 100px" translate>NUMBER</th>
                    <th style="width: 150px" translate>NAME</th>
                    <th style="width: 200px" translate>DESCRIPTION</th>
                    <th style="width: 150px" translate>SERIAL_NUMBER</th>
                    <th style="width: 100px;text-align: center;" translate>BATCH_NUMBER</th>
                    <th style="width: 150px" translate>STATUS</th>
                    <th style="width: 150px" translate>RELEASED_REJECTED_DATE</th>
                    <th style="width: 150px" translate>MODIFIED_BY</th>
                    <th style="width: 150px" translate>MODIFIED_DATE</th>
                    <th class='added-column'
                        style="width: 150px;z-index: auto !important;"
                        ng-repeat="selectedMbomAttribute in allMBOMInstanceVm.selectedMbomAttributes">
                        {{selectedMbomAttribute.name}}
                        <i class="fa fa-times-circle"
                           ng-click="allMBOMInstanceVm.removeAttribute(selectedMbomAttribute)"
                           title={{allMBOMInstanceVm.RemoveColumnTitle}}></i>
                    </th>
                    <th class="actions-col sticky-col sticky-actions-col" style="text-align:center; width: 80px;"
                        translate>ACTIONS
                    </th>
                </tr>
                </thead>
                <tbody>
                <tr ng-if="allMBOMInstanceVm.loading == true">
                    <td colspan="25">
                        <span style="font-size: 15px;">
                            <img src="app/assets/bower_components/cassini-platform/images/loaders/loader19.gif"
                                 class="mr5">
                            <span translate>LOADING_MBOMINSTANCES</span>
                        </span>
                    </td>
                </tr>
                <tr ng-if="allMBOMInstanceVm.loading == false && allMBOMInstanceVm.mboms.content.length == 0">
                    <td colspan="25" style="background-color: #f9fbfe  !important;color: unset !important;">
                        <div class="no-data">
                            <img src="app/assets/no_data_images/MBOM.png" alt="" class="image">

                            <div class="message" translate>NO_MBOMINSTANCES</div>
                            <div class="message ng-scope" style="font-size: 14px;color: #8f979d;" translate>
                                NO_PERMISSION_MESSAGE
                            </div>
                        </div>
                    </td>
                </tr>
                <tr ng-repeat="mbomInstance in allMBOMInstanceVm.mbomInstances.content">
                    <td style="width:1px !important;white-space: nowrap;text-align: left;">
                        <all-view-icons object="mbomInstance"></all-view-icons>
                    </td>
                    <td class="col-width-100">
                        <a href="" ng-click="allMBOMInstanceVm.showMBOM(mbomInstance)" id="plantNumber"
                           title="{{'CLICK_TO_SHOW_DETAILS' | translate}}">
                            <span ng-bind-html="mbomInstance.number | highlightText: freeTextQuery"></span>
                        </a>
                    </td>
                    <td class="col-width-150">
                        <span ng-bind-html="mbomInstance.typeName | highlightText: freeTextQuery"></span>
                    </td>
                    <td class="col-width-200">
                        <span ng-bind-html="mbomInstance.name | highlightText: freeTextQuery"></span>
                    </td>
                    <td class="col-width-250" title="{{mbomInstance.description}}">
                        <span ng-bind-html="mbomInstance.description  | highlightText: freeTextQuery"></span>
                    </td>
                    <td>
                        <a href="" ng-click="allMBOMInstanceVm.showItem(mbomInstance)"
                           title="{{'CLICK_TO_SHOW_DETAILS' | translate}}">
                            <span>{{mbomInstance.itemName}} - {{mbomInstance.itemRevision}}</span>
                        </a>
                    </td>
                    <td class="col-width-100" style="text-align: center">
                        <a href="" ng-click="allMBOMInstanceVm.showMBOMRevisionHistory(mbomInstance)"
                           title="{{'ITEM_DETAILS_REVISION_HISTORY' | translate}}">
                            {{mbomInstance.revision}}
                        </a>
                    </td>
                    <td>
                        <item-status item="mbomInstance"></item-status>
                    </td>
                    <td>{{mbomInstance.releasedDate}}</td>
                    <td>{{mbomInstance.modifiedByName}}</td>
                    <td>{{mbomInstance.modifiedDate}}</td>
                    <td class="added-column"
                        ng-repeat="objectAttribute in allMBOMInstanceVm.selectedMbomAttributes">
                        <all-view-attributes object="mbomInstance"
                                             object-attribute="objectAttribute"></all-view-attributes>
                    </td>
                    <td class="text-center actions-col sticky-col sticky-actions-col">
                        <span class="row-menu" uib-dropdown dropdown-append-to-body style="min-width: 50px">
                        <i class="fa fa-ellipsis-h dropdown-toggle" uib-dropdown-toggle></i>
                            <ul uib-dropdown-menu class="dropdown-menu dropdown-menu-right" role="menu"
                                style="z-index: 9999 !important;">
                                <li>
                                    <a href=""
                                       ng-click="showPrintOptions(mbomInstance.id,'PLANT')"
                                       translate>PREVIEW_AND_PRINT</a>
                                </li>
                                <li title="{{mbomInstance.lifeCyclePhase.phaseType == 'RELEASED'  ? cannotDeleteApprovedMbom:'' || mbomInstance.rejected == true ? cannotDeleteRejectedMbom:''}}">
                                    <a href="" ng-click="allMBOMInstanceVm.deleteMBOM(mbomInstance)"
                                       ng-if="hasPermission('mbomInstance','delete')"
                                       ng-class="{'disabled': mbomInstance.lifeCyclePhase.phaseType == 'RELEASED' || mbomInstance.rejected == true}"
                                       translate>DELETE</a>

                                </li>
                                <plugin-table-actions context="mbomInstance.all"
                                                      object-value="mbomInstance"></plugin-table-actions>
                            </ul>
                        </span>
                    </td>
                </tr>

                </tbody>
            </table>
        </div>
        <div class="table-footer">
            <table-footer objects="allMBOMInstanceVm.mboms" pageable="allMBOMInstanceVm.pageable"
                          previous-page="allMBOMInstanceVm.previousPage"
                          next-page="allMBOMInstanceVm.nextPage"></table-footer>
        </div>
    </div>
</div>
