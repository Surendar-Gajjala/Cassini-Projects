<div class="view-container" fitcontent>
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

    <div class="view-toolbar">
        <%--<span style="font-weight: 600;font-size: 20px;padding-right: 10px;" translate>WORKCENTERS</span>--%>

        <button class="btn btn-sm new-button" ng-click="allWorkCenterVm.newWorkCenter()"
                title="{{'NEW_WORKCENTER' | translate}}" ng-if="hasPermission('workcenter','create')">
            <i class="las la-plus" aria-hidden="true"></i>
            <span>{{'NEW_WORKCENTER' | translate}}</span>
        </button>

        <div class="btn-group">
            <button class="btn btn-sm btn-success" ng-click="allWorkCenterVm.showTypeAttributes()"
                    title="{{'ALL_VIEW_ADD_ATTRIBUTES' | translate}}">
                <i class="fa fa-newspaper-o" style=""></i>
            </button>
        </div>
        <free-text-search on-clear="allWorkCenterVm.resetPage" search-term="allWorkCenterVm.searchText"
                          on-search="allWorkCenterVm.freeTextSearch"
                          filter-search="allWorkCenterVm.filterSearch"></free-text-search>
    </div>
    <div class="view-content no-padding" style="overflow-y: auto;padding: 10px;">
        <div class="responsive-table" style="padding: 10px;">
            <table class="table table-striped highlight-row">
                <thead>
                <tr>
                    <th style="width:1px;white-space:nowrap;"></th>
                    <th style="width: 100px" translate>NUMBER</th>
                    <th class="col-width-150" translate>TYPE</th>
                    <th class="col-width-200" translate>NAME</th>
                    <th class="col-width-200" translate>PLANT</th>
                    <th class="col-width-200" translate>ASSEMBLY_LINE</th>
                    <th class="col-width-250" translate>DESCRIPTION</th>
                    <th class="col-width-200" translate>LOCATION</th>
                    <th style="width: 100px" translate>STATUS</th>
                    <th style="width: 150px;" translate>REQUIRESMAINTENANCE</th>
                    <th style="width: 150px" translate>MODIFIED_BY</th>
                    <th style="width: 150px" translate>MODIFIED_DATE</th>
                    <th class='added-column'
                        style="width: 150px;z-index: auto !important;"
                        ng-repeat="selectedAttribute in allWorkCenterVm.selectedAttributes">
                        {{selectedAttribute.name}}
                        <i class="fa fa-times-circle"
                           ng-click="allWorkCenterVm.removeAttribute(selectedAttribute)"
                           title={{allWorkCenterVm.RemoveColumnTitle}}></i>
                    </th>
                    <th class="actions-col sticky-col sticky-actions-col" style="text-align:center; width: 80px;"
                        translate>ACTIONS
                    </th>
                </tr>
                </thead>
                <tbody>
                <tr ng-if="allWorkCenterVm.loading == true">
                    <td colspan="25">
                        <span style="font-size: 15px;">
                            <img src="app/assets/bower_components/cassini-platform/images/loaders/loader19.gif"
                                 class="mr5">
                            <span translate>LOADING_WORKCENTERS</span>
                        </span>
                    </td>
                </tr>
                <tr ng-if="allWorkCenterVm.loading == false && allWorkCenterVm.workCenters.content.length == 0">
                    <td colspan="25" style="background-color: #f9fbfe  !important;color: unset !important;">
                        <div class="no-data">
                            <img src="app/assets/no_data_images/WorkCenter.png" alt="" class="image">

                            <div class="message">{{noWorkCenters}}</div>
                            <div class="message ng-scope" style="font-size: 14px;color: #8f979d;" translate>NO_PERMISSION_MESSAGE</div>
                        </div>
                    </td>
                </tr>
                <tr ng-repeat="workCenter in allWorkCenterVm.workCenters.content">
                    <td style="width:1px !important;white-space: nowrap;text-align: left;">
                        <all-view-icons object="workCenter"></all-view-icons>
                    </td>
                    <td class="col-width-100">
                        <a href="" ng-click="allWorkCenterVm.showWorkCenter(workCenter)"
                           title="{{'CLICK_TO_SHOW_DETAILS' | translate}}">
                            <span ng-bind-html="workCenter.number | highlightText: freeTextQuery"></span>
                        </a>
                    </td>
                    <td class="col-width-150">{{workCenter.type.name}}</td>
                    <td class="col-width-200" title="{{workCenter.name}}">
                        <span ng-bind-html="workCenter.name  | highlightText: freeTextQuery"></span>
                    </td>
                    <td class="col-width-200">{{workCenter.plantName}}</td>
                    <td class="col-width-200">{{workCenter.assemblyLineName}}</td>
                    <td class="col-width-250" title="{{workCenter.description}}">
                        <span ng-bind-html="workCenter.description  | highlightText: freeTextQuery"></span>
                    </td>
                    <td class="col-width-200" title="{{workCenter.location}}">
                        <span ng-bind-html="workCenter.location  | highlightText: freeTextQuery"></span>
                    </td>
                    <td class="col-width-100">
                        <span ng-if="workCenter.active == true" class="label label-outline bg-light-success" translate>C_ACTIVE</span>
                        <span ng-if="workCenter.active == false" class="label label-outline bg-light-danger" translate>C_INACTIVE</span>
                    </td>
                    <td class="col-width-150">
                        <span ng-if="workCenter.requiresMaintenance == true"
                              class="label label-outline bg-light-success"
                              translate>YES</span>
                        <span ng-if="workCenter.requiresMaintenance == false"
                              class="label label-outline bg-light-danger"
                              translate>NO</span>
                    </td>
                    <td>{{workCenter.modifiedByObject.fullName}}</td>
                    <td>{{workCenter.modifiedDate}}</td>
                    <td class="added-column"
                        ng-repeat="objectAttribute in allWorkCenterVm.selectedAttributes">
                        <all-view-attributes object="workCenter"
                                             object-attribute="objectAttribute"></all-view-attributes>
                    </td>
                    <td class="text-center actions-col sticky-col sticky-actions-col">
                        <span class="row-menu" uib-dropdown dropdown-append-to-body style="min-width: 50px">
                            <i class="fa fa-ellipsis-h dropdown-toggle" uib-dropdown-toggle></i>
                            <ul uib-dropdown-menu class="dropdown-menu dropdown-menu-right" role="menu"
                                style="z-index: 9999 !important;">
                                <li>
                                    <a href=""
                                       ng-click="showPrintOptions(workCenter.id,'WORKCENTER')" translate>PREVIEW_AND_PRINT</a>
                                </li>
                                <li title="{{hasPermission('workcenter','delete') ? '' : noPermission}}"
                                    ng-class="{'cursor-override': !hasPermission('workcenter','delete')}">
                                    <a href=""
                                       ng-class="{'permission-text-disabled': !hasPermission('workcenter','delete')}"
                                       ng-click="allWorkCenterVm.deleteWorkCenter(workCenter)" translate>DELETE</a>
                                </li>
                                <plugin-table-actions context="workCenters.all" object-value="workCenter"></plugin-table-actions>
                            </ul>
                        </span>
                    </td>
                </tr>
                </tbody>
            </table>
        </div>
        <div class="table-footer">
            <table-footer objects="allWorkCenterVm.workCenters" pageable="allWorkCenterVm.pageable"
                          previous-page="allWorkCenterVm.previousPage"
                          page-size="allWorkCenterVm.pageSize"
                          next-page="allWorkCenterVm.nextPage"></table-footer>
        </div>
    </div>
</div>
