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
        <%--<span style="font-weight: 600;font-size: 20px;padding-right: 10px;" translate>INSTRUMENTS</span>--%>

        <button class="btn btn-sm new-button" ng-click="allInstrumentVm.newInstrument()"
                title="{{newInstrument}}" ng-if="hasPermission('instrument','create')">
            <i class="las la-plus" aria-hidden="true"></i>
            <span>{{newInstrument}}</span>
        </button>

        <div class="btn-group">
            <button class="btn btn-sm btn-success" ng-click="allInstrumentVm.showTypeAttributes()"
                    title="{{'ALL_VIEW_ADD_ATTRIBUTES' | translate}}">
                <i class="fa fa-newspaper-o" style=""></i>
            </button>
        </div>
        <free-text-search on-clear="allInstrumentVm.resetPage" search-term="allInstrumentVm.searchText"
                          on-search="allInstrumentVm.freeTextSearch"
                          filter-search="allInstrumentVm.filterSearch"></free-text-search>
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
                    <th translate>DESCRIPTION</th>
                    <th style="width: 100px" translate>STATUS</th>
                    <th style="width: 150px" translate>REQUIRESMAINTENANCE</th>
                    <th style="width: 100px;text-align: center;" translate>IMAGE_TITLE</th>
                    <th style="width: 150px" translate>MODIFIED_BY</th>
                    <th style="width: 150px" translate>MODIFIED_DATE</th>
                    <th class='added-column'
                        style="width: 150px;z-index: auto !important;"
                        ng-repeat="selectedAttribute in allInstrumentVm.selectedAttributes">
                        {{selectedAttribute.name}}
                        <i class="fa fa-times-circle"
                           ng-click="allInstrumentVm.removeAttribute(selectedAttribute)"
                           title={{allInstrumentVm.RemoveColumnTitle}}></i>
                    </th>
                    <th class="actions-col sticky-col sticky-actions-col" style="text-align:center; width: 80px;"
                        translate>ACTIONS
                    </th>
                </tr>
                </thead>
                <tbody>
                <tr ng-if="allInstrumentVm.loading == true">
                    <td colspan="25">
                        <span style="font-size: 15px;">
                            <img src="app/assets/bower_components/cassini-platform/images/loaders/loader19.gif"
                                 class="mr5">
                            <span translate>LOADING_INSTRUMENTS</span>
                        </span>
                    </td>
                </tr>

                <tr ng-if="allInstrumentVm.loading == false && allInstrumentVm.instruments.content.length == 0">
                    <td colspan="25" style="background-color: #f9fbfe  !important;color: unset !important;">
                        <div class="no-data">
                            <img src="app/assets/no_data_images/Instrument.png" alt="" class="image">

                            <div class="message" translate>NO_INSTRUMENTS</div>
                            <div class="message ng-scope" style="font-size: 14px;color: #8f979d;" translate>NO_PERMISSION_MESSAGE</div>
                        </div>
                    </td>
                </tr>

                <tr ng-repeat="instrument in allInstrumentVm.instruments.content">
                    <td style="width:1px !important;white-space: nowrap;text-align: left;">
                        <all-view-icons object="instrument"></all-view-icons>
                    </td>
                    <td class="col-width-100">
                        <a href="" ng-click="allInstrumentVm.showInstrument(instrument)"
                           title="{{'CLICK_TO_SHOW_DETAILS' | translate}}">
                            <span ng-bind-html="instrument.number | highlightText: freeTextQuery"></span>
                        </a>
                    </td>

                    <td class="col-width-150">
                        <span ng-bind-html="instrument.type.name | highlightText: freeTextQuery"></span>
                    </td>
                    <td class="col-width-150">
                        <span ng-bind-html="instrument.name | highlightText: freeTextQuery"></span>
                    </td>
                    <td class="col-width-250" title="{{instrument.description}}">
                        <span ng-bind-html="instrument.description  | highlightText: freeTextQuery"></span>
                    </td>

                    <td class="col-width-100">
                        <span ng-if="instrument.active == true" class="label label-outline bg-light-success" translate>C_ACTIVE</span>
                        <span ng-if="instrument.active == false" class="label label-outline bg-light-danger" translate>C_INACTIVE</span>
                    </td>
                    <td class="col-width-150">
                        <span ng-if="instrument.requiresMaintenance == true"
                              class="label label-outline bg-light-success"
                              translate>YES</span>
                        <span ng-if="instrument.requiresMaintenance == false"
                              class="label label-outline bg-light-danger"
                              translate>NO</span>
                    </td>
                    <td style="width: 100px;text-align: center">
                        <div>
                            <a ng-if="instrument.image != null && instrument.image != ''"
                               href="" ng-click="allInstrumentVm.showImage(instrument)"
                               title="{{clickToShowLargeImage}}">
                                <img ng-src="{{instrument.imagePath}}"
                                     style="height: 20px;width: 30px;">
                            </a>
                            <a ng-if="instrument.image == null && instrument.image != ''" href="">
                                <img src="app/assets/images/cassini-logo-greyscale.png" title="No image" alt=""
                                     class="no-image-preview" style="height: 20px;width: 30px;">
                            </a>

                            <div id="item-thumbnail{{instrument.id}}" class="item-thumbnail modal">
                                <div class="item-thumbnail-content">
                                    <div class="thumbnail-content" style="display: flex;width: 100%;">
                                        <div class="thumbnail-view" id="thumbnail-view{{instrument.id}}">
                                            <div id="thumbnail-image{{instrument.id}}"
                                                 style="display: table-cell;vertical-align: middle;text-align: center;">
                                                <img ng-src="{{instrument.imagePath}}"
                                                     style="height: auto;width: auto;max-width: 100%;max-height: 90%"/>
                                                    <span class="thumbnail-close"
                                                          id="thumbnail-close{{instrument.id}}"></span>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </td>
                    <td>{{instrument.modifiedByObject.fullName}}</td>
                    <td>{{instrument.modifiedDate}}</td>
                    <td class="added-column"
                        ng-repeat="objectAttribute in allInstrumentVm.selectedAttributes">
                        <all-view-attributes object="instrument"
                                             object-attribute="objectAttribute"></all-view-attributes>
                    </td>
                    <td class="text-center actions-col sticky-col sticky-actions-col">
                        <span class="row-menu" uib-dropdown dropdown-append-to-body style="min-width: 50px">
                            <i class="fa fa-ellipsis-h dropdown-toggle" uib-dropdown-toggle></i>
                            <ul uib-dropdown-menu class="dropdown-menu dropdown-menu-right" role="menu"
                                style="z-index: 9999 !important;">
                                <li>
                                    <a href=""
                                       ng-click="showPrintOptions(instrument.id,'INSTRUMENT')" translate>PREVIEW_AND_PRINT</a>
                                </li>
                                <li title="{{hasPermission('instrument','delete') ? '' : noPermission}}"
                                    ng-class="{'cursor-override': !hasPermission('instrument','delete')}">
                                    <a ng-click="allInstrumentVm.deleteInstrument(instrument)"
                                       ng-class="{'permission-text-disabled': !hasPermission('instrument','delete')}"
                                       translate>
                                        DELETE
                                    </a>
                                </li>
                                <plugin-table-actions context="instrument.all" object-value="instrument"></plugin-table-actions>
                            </ul>
                        </span>
                    </td>
                </tr>


                </tbody>
            </table>
        </div>
        <div class="table-footer">
            <table-footer objects="allInstrumentVm.instruments" pageable="allInstrumentVm.pageable"
                          page-size="allInstrumentVm.pageSize"
                          previous-page="allInstrumentVm.previousPage"
                          next-page="allInstrumentVm.nextPage"></table-footer>
        </div>
    </div>
</div>
