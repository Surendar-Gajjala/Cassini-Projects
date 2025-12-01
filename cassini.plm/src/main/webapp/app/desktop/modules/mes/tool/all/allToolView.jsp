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
        <%--<span style="font-weight: 600;font-size: 20px;padding-right: 10px;" translate>TOOLS_TITLS</span>--%>

        <button class="btn btn-sm new-button" ng-click="allToolVm.newTool()"
                title="{{'NEW_TOOL' | translate}}" ng-if="hasPermission('tool','create')">
            <i class="las la-plus" aria-hidden="true"></i>
            <span>{{'NEW' | translate}} {{'TOOL' | translate }}</span>
        </button>

        <div class="btn-group">
            <button class="btn btn-sm btn-success" ng-click="allToolVm.showTypeAttributes()"
                    title="{{'ALL_VIEW_ADD_ATTRIBUTES' | translate}}">
                <i class="fa fa-newspaper-o" style=""></i>
            </button>
        </div>
        <free-text-search on-clear="allToolVm.resetPage" search-term="allToolVm.searchText"
                          on-search="allToolVm.freeTextSearch"
                          filter-search="allToolVm.filterSearch"></free-text-search>
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
                    <th class="description-column" translate>DESCRIPTION</th>
                    <th style="width: 100px" translate>STATUS</th>
                    <th style="width: 150px" translate>REQUIRESMAINTENANCE</th>
                    <th style="width: 100px;text-align: center;" translate>IMAGE_TITLE</th>
                    <th style="width: 150px" translate>MODIFIED_BY</th>
                    <th style="width: 150px" translate>MODIFIED_DATE</th>
                    <th class='added-column'
                        style="width: 150px;z-index: auto !important;"
                        ng-repeat="selectedAttribute in allToolVm.selectedAttributes">
                        {{selectedAttribute.name}}
                        <i class="fa fa-times-circle"
                           ng-click="allToolVm.removeAttribute(selectedAttribute)"
                           title={{allToolVm.RemoveColumnTitle}}></i>
                    </th>
                    <th class="actions-col sticky-col sticky-actions-col" style="text-align:center; width: 80px;"
                        translate>ACTIONS
                    </th>
                </tr>
                </thead>
                <tbody>
                <tr ng-if="allToolVm.loading == true">
                    <td colspan="25">
                        <span style="font-size: 15px;">
                            <img src="app/assets/bower_components/cassini-platform/images/loaders/loader19.gif"
                                 class="mr5">
                            <span translate>LOADING_TOOLS</span>
                        </span>
                    </td>
                </tr>
                <tr ng-if="allToolVm.loading == false && allToolVm.tools.content.length == 0">
                    <td colspan="25" style="background-color: #f9fbfe  !important;color: unset !important;">
                        <div class="no-data">
                            <img src="app/assets/no_data_images/Tools.png" alt="" class="image">

                            <div class="message" translate>NO_TOOLS</div>
                            <div class="message ng-scope" style="font-size: 14px;color: #8f979d;" translate>NO_PERMISSION_MESSAGE</div>
                        </div>
                    </td>
                </tr>

                <tr ng-repeat="tool in allToolVm.tools.content">
                    <td style="width:1px !important;white-space: nowrap;text-align: left;">
                        <all-view-icons object="tool"></all-view-icons>
                    </td>
                    <td class="col-width-100">
                        <a href="" ng-click="allToolVm.showTool(tool)"
                           title="{{'CLICK_TO_SHOW_DETAILS' | translate}}">
                            <span ng-bind-html="tool.number | highlightText: freeTextQuery"></span>
                        </a>
                    </td>
                    <td class="col-width-150">{{tool.type.name}}</td>
                    <td class="col-width-200" title="{{tool.name}}">
                        <span ng-bind-html="tool.name  | highlightText: freeTextQuery"></span>
                        {{tool.name.length > 24 ? '...' : ''}}
                    </td>
                    <td class="description-column" title="{{tool.description}}">
                        <span ng-bind-html="tool.description  | highlightText: freeTextQuery"></span>
                    </td>
                    <td class="col-width-100">
                        <span ng-if="tool.active == true" class="label label-outline bg-light-success" translate>C_ACTIVE</span>
                        <span ng-if="tool.active == false" class="label label-outline bg-light-danger" translate>C_INACTIVE</span>
                    </td>
                    <td class="col-width-150">
                        <span ng-if="tool.requiresMaintenance == true" class="label label-outline bg-light-success"
                              translate>YES</span>
                        <span ng-if="tool.requiresMaintenance == false" class="label label-outline bg-light-danger"
                              translate>NO</span>
                    </td>
                    <td style="width: 100px;text-align: center">
                        <div>
                            <a ng-if="tool.image != null && tool.image != ''"
                               href="" ng-click="allToolVm.showImage(tool)"
                               title="{{clickToShowLargeImage}}">
                                <img ng-src="{{tool.imagePath}}"
                                     style="height: 20px;width: 30px;">
                            </a>
                            <a ng-if="tool.image == null && tool.image != ''" href="">
                                <img src="app/assets/images/cassini-logo-greyscale.png" title="No image" alt=""
                                     class="no-image-preview" style="height: 20px;width: 30px;">
                            </a>

                            <div id="item-thumbnail{{tool.id}}" class="item-thumbnail modal">
                                <div class="item-thumbnail-content">
                                    <div class="thumbnail-content" style="display: flex;width: 100%;">
                                        <div class="thumbnail-view" id="thumbnail-view{{tool.id}}">
                                            <div id="thumbnail-image{{tool.id}}"
                                                 style="display: table-cell;vertical-align: middle;text-align: center;">
                                                <img ng-src="{{tool.imagePath}}"
                                                     style="height: auto;width: auto;max-width: 100%;max-height: 90%"/>
                                                    <span class="thumbnail-close"
                                                          id="thumbnail-close{{tool.id}}"></span>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </td>
                    <td>{{tool.modifiedByObject.fullName}}</td>
                    <td>{{tool.modifiedDate}}</td>
                    <td class="added-column"
                        ng-repeat="objectAttribute in allToolVm.selectedAttributes">
                        <all-view-attributes object="tool"
                                             object-attribute="objectAttribute"></all-view-attributes>
                    </td>
                    <td class="text-center actions-col sticky-col sticky-actions-col">
                        <span class="row-menu" uib-dropdown dropdown-append-to-body style="min-width: 50px">
                            <i class="fa fa-ellipsis-h dropdown-toggle" uib-dropdown-toggle></i>
                                <ul uib-dropdown-menu class="dropdown-menu dropdown-menu-right" role="menu"
                                    style="z-index: 9999 !important;">
                                    <li>
                                        <a href=""
                                           ng-click="showPrintOptions(tool.id,'TOOL')" translate>PREVIEW_AND_PRINT</a>
                                    </li>
                                    <li title="{{hasPermission('tool','delete') ? '' : noPermission}}"
                                        ng-class="{'cursor-override': !hasPermission('tool','delete')}">
                                        <a href=""
                                           ng-class="{'permission-text-disabled': !hasPermission('tool','delete')}"
                                           ng-click="allToolVm.deleteTool(tool)"
                                           translate>
                                            DELETE
                                        </a>
                                    </li>
                                    <plugin-table-actions context="tool.all" object-value="tool"></plugin-table-actions>
                                </ul>
                        </span>
                    </td>
                </tr>
                </tbody>
            </table>
        </div>
        <div class="table-footer">
            <table-footer objects="allToolVm.tools" pageable="allToolVm.pageable"
                          page-size="allToolVm.pageSize"
                          previous-page="allToolVm.previousPage" next-page="allToolVm.nextPage"></table-footer>
        </div>
    </div>
</div>
