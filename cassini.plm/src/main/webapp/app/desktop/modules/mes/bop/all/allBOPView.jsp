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
        <span style="font-weight: 600;font-size: 20px;padding-right: 10px;" translate>BOP</span>

        <button class="btn btn-sm new-button" ng-click="allBOPVm.newBOP()"
                title="{{'NEW_BOP' | translate}}" ng-if="hasPermission('bop','create')">
            <i class="las la-plus" aria-hidden="true"></i>
            <span>{{'NEW_BOP' | translate}}</span>
        </button>

        <div class="btn-group">
            <button class="btn btn-sm btn-success" ng-click="allBOPVm.showTypeAttributes()"
                    title="{{'ALL_VIEW_ADD_ATTRIBUTES' | translate}}">
                <i class="fa fa-newspaper-o" style=""></i>
            </button>
            <button class="btn btn-sm btn-default"
                    style="" title="{{preferredPage}}"
                    ng-click="savePreferredPage()">
                <i class="fa fa fa-anchor" style=""></i>
            </button>
        </div>
        <free-text-search on-clear="allBOPVm.resetPage" search-term="allBOPVm.searchText"
                          on-search="allBOPVm.freeTextSearch"
                          filter-search="allBOPVm.filterSearch"></free-text-search>
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
                    <th style="width: 150px" translate>MBOM</th>
                    <th style="width: 100px;text-align: center;" translate>REVISION</th>
                    <th style="width: 100px;text-align: center;" translate>LIFECYCLE</th>
                    <th></th>
                    <th style="width: 100px;text-align: center;" translate>STATUS</th>
                    <th style="width: 100px;" translate>CREATED_BY</th>
                    <th style="width: 100px;" translate>CREATED_DATE</th>
                    <th style="width: 100px;" translate>MODIFIED_BY</th>
                    <th style="width: 100px;" translate>MODIFIED_DATE</th>
                    <th class='added-column'
                        style="width: 100px;z-index: auto !important;"
                        ng-repeat="selectedBopAttribute in allBOPVm.selectedBopAttributes">
                        {{selectedBopAttribute.name}}
                        <i class="fa fa-times-circle"
                           ng-click="allBOPVm.removeAttribute(selectedBopAttribute)"
                           title={{allBOPVm.RemoveColumnTitle}}></i>
                    </th>
                    <th class="actions-col sticky-col sticky-actions-col" style="text-align:center; width: 80px;"
                        translate>ACTIONS
                    </th>
                </tr>
                </thead>
                <tbody>
                <tr ng-if="allBOPVm.loading == true">
                    <td colspan="25">
                        <span style="font-size: 15px;">
                            <img src="app/assets/bower_components/cassini-platform/images/loaders/loader19.gif"
                                 class="mr5">
                            <span translate>LOADING_BOP</span>
                        </span>
                    </td>
                </tr>
                <tr ng-if="allBOPVm.loading == false && allBOPVm.bops.content.length == 0">
                    <td colspan="25" style="background-color: #f9fbfe  !important;color: unset !important;">
                        <div class="no-data">
                            <img src="app/assets/no_data_images/MBOM.png" alt="" class="image">

                            <div class="message" translate>NO_BOP</div>
                            <div class="message ng-scope" style="font-size: 14px;color: #8f979d;" translate>
                                NO_PERMISSION_MESSAGE
                            </div>
                        </div>
                    </td>
                </tr>
                <tr ng-repeat="bop in allBOPVm.bops.content">
                    <td style="width:1px !important;white-space: nowrap;text-align: left;">
                        <all-view-icons object="bop"></all-view-icons>
                    </td>
                    <td class="col-width-100">
                        <a href="" ng-click="allBOPVm.showBOP(bop)" id="plantNumber"
                           title="{{'CLICK_TO_SHOW_DETAILS' | translate}}">
                            <span ng-bind-html="bop.number | highlightText: freeTextQuery"></span>
                        </a>
                    </td>
                    <td class="col-width-150">
                        <span ng-bind-html="bop.typeName | highlightText: freeTextQuery"></span>
                    </td>
                    <td class="col-width-200">
                        <span ng-bind-html="bop.name | highlightText: freeTextQuery"></span>
                    </td>
                    <td class="col-width-250" title="{{bop.description}}">
                        <span ng-bind-html="bop.description  | highlightText: freeTextQuery"></span>
                    </td>
                    <td>
                        <a href="" ng-click="allBOPVm.showMBOM(bop)"
                           title="{{'CLICK_TO_SHOW_DETAILS' | translate}}">
                            {{bop.mbomName}} - {{bop.mbomRevision}}
                        </a>
                    </td>
                    <td class="col-width-100" style="text-align: center">
                        <a href="" ng-click="allBOPVm.showBOPRevisionHistory(bop)"
                           title="{{'ITEM_DETAILS_REVISION_HISTORY' | translate}}">{{bop.revision}}</a>
                    </td>
                    <td class="col-width-100" style="text-align: center">
                        <item-status item="bop"></item-status>
                    </td>
                    <td style="width: 1% !important;white-space: nowrap">
                        <i ng-if='bop.startWorkflow && !bop.finishWorkflow && !bop.cancelWorkflow'
                           style='font-size: 18px;color: #ffa800' class='la la-clock-o' title="In Progress"></i>
                        <i ng-if='bop.finishWorkflow' style='font-size: 18px;color: #1CAF9A'
                           class='la la-check-circle-o' title="Finished"></i>
                        <i ng-if='bop.cancelWorkflow' style='font-size: 18px;color: #f64e60'
                           class='la la-times-circle-o' title="Rejected"></i>
                    </td>
                    <td>
                        <workflow-status-settings workflow="bop"></workflow-status-settings>
                        <span class="label label-warning" ng-if="bop.onHold">HOLD</span>
                    </td>
                    <td>{{bop.createdByName}}</td>
                    <td>{{bop.createdDate}}</td>
                    <td>{{bop.modifiedByName}}</td>
                    <td>{{bop.modifiedDate}}</td>
                    <td class="added-column" ng-repeat="objectAttribute in allBOPVm.selectedBopAttributes">
                        <all-view-attributes object="bop"
                                             object-attribute="objectAttribute"></all-view-attributes>
                    </td>
                    <td class="text-center actions-col sticky-col sticky-actions-col">
                        <span class="row-menu" uib-dropdown dropdown-append-to-body style="min-width: 50px">
                        <i class="fa fa-ellipsis-h dropdown-toggle" uib-dropdown-toggle></i>
                            <ul uib-dropdown-menu class="dropdown-menu dropdown-menu-right" role="menu"
                                style="z-index: 9999 !important;">
                                <li>
                                    <a href=""
                                       ng-click="showPrintOptions(bop.latestRevision,'BOP')" translate>PREVIEW_AND_PRINT</a>
                                </li>
                                <li title="{{hasPermission('bop','delete') ? '' : noPermission}}"
                                    ng-class="{'cursor-override': !hasPermission('bop','delete')}">
                                    <a href="" ng-click="allBOPVm.deleteBOP(bop)"
                                       ng-if="hasPermission('bop','delete')"
                                       ng-class="{'permission-text-disabled': !hasPermission('bop','delete') || bop.released}"
                                       translate>DELETE</a>
                                </li>
                                <plugin-table-actions context="bop.all" object-value="bop"></plugin-table-actions>
                            </ul>
                        </span>
                    </td>
                </tr>

                </tbody>
            </table>
        </div>
        <div class="table-footer">
            <table-footer objects="allBOPVm.bops" pageable="allBOPVm.pageable"
                          previous-page="allBOPVm.previousPage" next-page="allBOPVm.nextPage"></table-footer>
        </div>
    </div>
</div>
