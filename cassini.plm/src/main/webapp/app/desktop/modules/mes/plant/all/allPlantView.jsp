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
        <%--<span style="font-weight: 600;font-size: 20px;padding-right: 10px;" translate>PLANTS</span>--%>

        <button class="btn btn-sm new-button" ng-click="allPlantVm.newPlant()" id="newPlant"
                title="{{'NEW_PLANT' | translate}}" ng-if="hasPermission('plant','create')">
            <i class="las la-plus" aria-hidden="true"></i>
            <span>{{'NEW' | translate}} {{'PLANT' | translate }}</span>
        </button>

        <div class="btn-group">
            <button class="btn btn-sm btn-success" ng-click="allPlantVm.showTypeAttributes()"
                    title="{{'ALL_VIEW_ADD_ATTRIBUTES' | translate}}">
                <i class="fa fa-newspaper-o" style=""></i>
            </button>
        </div>
        <free-text-search on-clear="allPlantVm.resetPage" search-term="allPlantVm.searchText"
                          on-search="allPlantVm.freeTextSearch"
                          filter-search="allPlantVm.filterSearch"></free-text-search>
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
                    <th class="col-width-250" translate>DESCRIPTION</th>
                    <th style="width: 150px" translate>PLANT_MANAGER</th>
                    <th class="col-width-150" translate>ADDRESS</th>
                    <th style="width: 150px" translate>CITY</th>
                    <th style="width: 150px" translate>COUNTRY</th>
                    <th style="width: 150px;" translate>REQUIRESMAINTENANCE</th>
                    <%--<th style="width: 150px" translate>PHONE_NUMBER</th>--%>
                    <%--<th style="width: 150px" translate>EMAIL</th>--%>
                    <th style="width: 150px" translate>MODIFIED_BY</th>
                    <th style="width: 150px" translate>MODIFIED_DATE</th>
                    <th class='added-column'
                        style="width: 200px;z-index: auto !important;"
                        ng-repeat="selectedAttribute in allPlantVm.selectedAttributes">
                        {{selectedAttribute.name}}
                        <i class="fa fa-times-circle"
                           ng-click="allPlantVm.removeAttribute(selectedAttribute)"
                           title={{allPlantVm.RemoveColumnTitle}}></i>
                    </th>
                    <th class="actions-col sticky-col sticky-actions-col" style="text-align:center; width: 80px;"
                        translate>ACTIONS
                    </th>
                </tr>
                </thead>
                <tbody>
                <tr ng-if="allPlantVm.loading == true">
                    <td colspan="25">
                        <span style="font-size: 15px;">
                            <img src="app/assets/bower_components/cassini-platform/images/loaders/loader19.gif"
                                 class="mr5">
                            <span translate>LOADING_PLANTS</span>
                        </span>
                    </td>
                </tr>

                <tr ng-if="allPlantVm.loading == false && allPlantVm.plants.content.length == 0">
                    <td colspan="25" style="background-color: #f9fbfe  !important;color: unset !important;">
                        <div class="no-data">
                            <img src="app/assets/no_data_images/Plant.png" alt="" class="image">

                            <div class="message" translate>NO_PLANTS</div>
                            <div class="message ng-scope" style="font-size: 14px;color: #8f979d;" translate>NO_PERMISSION_MESSAGE</div>
                        </div>
                    </td>
                </tr>
                <tr ng-repeat="plant in allPlantVm.plants.content">
                    <td style="width:1px !important;white-space: nowrap;text-align: left;">
                        <all-view-icons object="plant"></all-view-icons>
                    </td>
                    <td class="col-width-100">
                        <a href="" ng-click="allPlantVm.showPlant(plant)" id="plantNumber"
                           title="{{'CLICK_TO_SHOW_DETAILS' | translate}}">
                            <span ng-bind-html="plant.number | highlightText: freeTextQuery"></span>
                        </a>
                    </td>
                    <td class="col-width-150">
                        <span ng-bind-html="plant.type.name | highlightText: freeTextQuery"></span>
                    </td>
                    <td class="col-width-200">
                        <span ng-bind-html="plant.name | highlightText: freeTextQuery"></span>
                    </td>
                    <td class="col-width-250" title="{{plant.description}}">
                        <span ng-bind-html="plant.description  | highlightText: freeTextQuery"></span>
                    </td>
                    <td>{{plant.person}}</td>
                    <td class="col-width-150">
                        <span ng-bind-html="plant.address | highlightText: freeTextQuery"></span>
                    </td>
                    <td class="col-width-100">{{plant.city}}</td>
                    <td class="col-width-100">{{plant.country}}</td>
                    <td class="col-width-150">
                        <span ng-if="plant.requiresMaintenance == true" class="label label-outline bg-light-success"
                              translate>YES</span>
                        <span ng-if="plant.requiresMaintenance == false" class="label label-outline bg-light-danger"
                              translate>NO</span>
                    </td>
                    <%--<td>{{plant.phoneNumber}}</td>--%>
                    <%--<td style="width: 150px;">
                        <span ng-bind-html="plant.email | highlightText: freeTextQuery"></span>
                    </td>--%>
                    <td>{{plant.modifiedByObject.fullName}}</td>
                    <td>{{plant.modifiedDate}}</td>
                    <td class="added-column"
                        ng-repeat="objectAttribute in allPlantVm.selectedAttributes">
                        <all-view-attributes object="plant"
                                             object-attribute="objectAttribute"></all-view-attributes>
                    </td>
                    <td class="text-center actions-col sticky-col sticky-actions-col">
                        <span class="row-menu" uib-dropdown dropdown-append-to-body style="min-width: 50px">
                        <i class="fa fa-ellipsis-h dropdown-toggle" uib-dropdown-toggle></i>
                            <ul uib-dropdown-menu class="dropdown-menu dropdown-menu-right" role="menu"
                                style="z-index: 9999 !important;">
                                <li>
                                    <a href=""
                                       ng-click="showPrintOptions(plant.id,'PLANT')" translate>PREVIEW_AND_PRINT</a>
                                </li>
                                <li title="{{hasPermission('plant','delete') ? '' : noPermission}}"
                                    ng-class="{'cursor-override': !hasPermission('plant','delete')}">
                                    <a href="" ng-click="allPlantVm.deletePlant(plant)"
                                       ng-class="{'permission-text-disabled': !hasPermission('plant','delete')}"
                                       translate>DELETE</a>
                                </li>
                                <plugin-table-actions context="plant.all" object-value="plant"></plugin-table-actions>
                            </ul>
                        </span>
                    </td>
                </tr>
                </tbody>
            </table>
        </div>
        <div class=" table-footer">
            <table-footer objects="allPlantVm.plants" pageable="allPlantVm.pageable"
                          previous-page="allPlantVm.previousPage" page-size="allPlantVm.pageSize"
                          next-page="allPlantVm.nextPage"></table-footer>
        </div>
    </div>
</div>
