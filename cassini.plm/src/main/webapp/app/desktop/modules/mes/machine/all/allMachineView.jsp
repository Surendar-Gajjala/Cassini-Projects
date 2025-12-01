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
        <%--<span style="font-weight: 600;font-size: 20px;padding-right: 10px;" translate>MACHINES</span>--%>

        <button class="btn btn-sm new-button" ng-click="allMachineVm.newMachine()"
                title="{{'NEW_MACHINE_TYPE' | translate}}" ng-if="hasPermission('machine','create')">
            <i class="las la-plus" aria-hidden="true"></i>
            <span>{{'NEW' | translate}} {{'MACHINE' | translate }}</span>
        </button>

        <div class="btn-group">
            <button class="btn btn-sm btn-success" ng-click="allMachineVm.showTypeAttributes()"
                    title="{{'ALL_VIEW_ADD_ATTRIBUTES' | translate}}">
                <i class="fa fa-newspaper-o" style=""></i>
            </button>
        </div>
        <free-text-search on-clear="allMachineVm.resetPage" search-term="allMachineVm.searchText"
                          on-search="allMachineVm.freeTextSearch"
                          filter-search="allMachineVm.filterSearch"></free-text-search>
    </div>
    <div class="view-content no-padding" style="overflow-y: auto;padding: 10px;">
        <div class="responsive-table" style="padding: 10px;">
            <table class="table table-striped highlight-row">
                <thead>
                <tr>
                    <th style="width:1px;white-space:nowrap;"></th>
                    <th style="width: 100px" translate>NUMBER</th>
                    <th class="col-width-150" translate>TYPE</th>
                    <th class="col-width-150" translate>WORKCENTER</th>
                    <th class="col-width-200" translate>NAME</th>
                    <th class="col-width-250" translate>DESCRIPTION</th>
                    <th style="width: 100px" translate>STATUS</th>
                    <th style="width: 150px" translate>REQUIRESMAINTENANCE</th>
                    <th style="width: 100px;text-align: center" translate>IMAGE_TITLE</th>
                    <th style="width: 150px" translate>MODIFIED_BY</th>
                    <th style="width: 150px" translate>MODIFIED_DATE</th>
                    <th class='added-column'
                        style="width: 150px;z-index: auto !important;"
                        ng-repeat="selectedAttribute in allMachineVm.selectedAttributes">
                        {{selectedAttribute.name}}
                        <i class="fa fa-times-circle"
                           ng-click="allMachineVm.removeAttribute(selectedAttribute)"
                           title={{allMachineVm.RemoveColumnTitle}}></i>
                    </th>
                    <th class="actions-col sticky-col sticky-actions-col" style="text-align:center; width: 80px;"
                        translate>ACTIONS
                    </th>
                </tr>
                </thead>
                <tbody>
                <tr ng-if="allMachineVm.loading == true">
                    <td colspan="25">
                        <span style="font-size: 15px;">
                            <img src="app/assets/bower_components/cassini-platform/images/loaders/loader19.gif"
                                 class="mr5">
                            <span translate>LOADING_MACHINES</span>
                        </span>
                    </td>
                </tr>

                <tr ng-if="allMachineVm.loading == false && allMachineVm.machines.content.length == 0">
                    <td colspan="25" style="background-color: #f9fbfe  !important;color: unset !important;">
                        <div class="no-data">
                            <img src="app/assets/no_data_images/Machine.png" alt="" class="image">

                            <div class="message" translate>NO_MACHINES</div>
                            <div class="message ng-scope" style="font-size: 14px;color: #8f979d;" translate>NO_PERMISSION_MESSAGE</div>
                        </div>
                    </td>
                </tr>

                <tr ng-repeat="machine in allMachineVm.machines.content">
                    <td style="width:1px !important;white-space: nowrap;text-align: left;">
                        <all-view-icons object="machine"></all-view-icons>
                    </td>
                    <td class="col-width-100">
                        <a href="" ng-click="allMachineVm.showMachine(machine)"
                           title="{{'CLICK_TO_SHOW_DETAILS' | translate}}">
                            <span ng-bind-html="machine.number | highlightText: freeTextQuery"></span>
                        </a>
                    </td>

                    <td class="col-width-150">
                        <span ng-bind-html="machine.type.name | highlightText: freeTextQuery"></span>
                    </td>

                    <td class="col-width-150">
                        <a href="" ng-click="allMachineVm.showWorkCenter(machine)"
                           title="{{'CLICK_TO_SHOW_DETAILS' | translate}}">
                            <span ng-bind-html="machine.workCenterName"></span>
                        </a>
                    </td>

                    <td class="col-width-200">
                        <span ng-bind-html="machine.name | highlightText: freeTextQuery"></span>
                    </td>
                    <td class="col-width-250" title="{{machine.description}}">
                        <span ng-bind-html="machine.description  | highlightText: freeTextQuery"></span>
                    </td>

                    <td class="col-width-100">
                        <span ng-if="machine.active == true" class="label label-outline bg-light-success" translate>C_ACTIVE</span>
                        <span ng-if="machine.active == false" class="label label-outline bg-light-danger" translate>C_INACTIVE</span>
                    </td>
                    <td class="col-width-150">
                        <span ng-if="machine.requiresMaintenance == true" class="label label-outline bg-light-success"
                              translate>YES</span>
                        <span ng-if="machine.requiresMaintenance == false" class="label label-outline bg-light-danger"
                              translate>NO</span>
                    </td>
                    <td style="width: 100px;text-align: center">
                        <div>
                            <a ng-if="machine.image != null && machine.image != ''"
                               href="" ng-click="allMachineVm.showImage(machine)"
                               title="{{clickToShowLargeImage}}">
                                <img ng-src="{{machine.imagePath}}"
                                     style="height: 20px;width: 30px;">
                            </a>
                            <a ng-if="machine.image == null && machine.image != ''" href="">
                                <img src="app/assets/images/cassini-logo-greyscale.png" title="No image" alt=""
                                     class="no-image-preview" style="height: 20px;width: 30px;">
                            </a>

                            <div id="item-thumbnail{{machine.id}}" class="item-thumbnail modal">
                                <div class="item-thumbnail-content">
                                    <div class="thumbnail-content" style="display: flex;width: 100%;">
                                        <div class="thumbnail-view" id="thumbnail-view{{machine.id}}">
                                            <div id="thumbnail-image{{machine.id}}"
                                                 style="display: table-cell;vertical-align: middle;text-align: center;">
                                                <img ng-src="{{machine.imagePath}}"
                                                     style="height: auto;width: auto;max-width: 100%;max-height: 90%"/>
                                                    <span class="thumbnail-close"
                                                          id="thumbnail-close{{machine.id}}"></span>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </td>
                    <td>{{machine.modifiedByObject.fullName}}</td>
                    <td>{{machine.modifiedDate}}</td>
                    <td class="added-column"
                        ng-repeat="objectAttribute in allMachineVm.selectedAttributes">
                        <all-view-attributes object="machine"
                                             object-attribute="objectAttribute"></all-view-attributes>
                    </td>
                    <td class="text-center actions-col sticky-col sticky-actions-col">
                        <span class="row-menu" uib-dropdown dropdown-append-to-body style="min-width: 50px">
                        <i class="fa fa-ellipsis-h dropdown-toggle" uib-dropdown-toggle></i>
                        <ul uib-dropdown-menu class="dropdown-menu dropdown-menu-right" role="menu"
                            style="z-index: 9999 !important;">
                            <li>
                                <a href=""
                                   ng-click="showPrintOptions(machine.id,'MACHINE')" translate>PREVIEW_AND_PRINT</a>
                            </li>
                            <li title="{{hasPermission('machine','delete') ? '' : noPermission}}"
                                ng-class="{'cursor-override': !hasPermission('machine','delete')}">
                                <a ng-click="allMachineVm.deleteMachine(machine)"
                                   ng-class="{'permission-text-disabled': machine.isReleased || machine.statusType == 'REJECTED' || !hasPermission('machine','delete')}"
                                   translate>
                                    DELETE
                                </a>
                            </li>
                            <plugin-table-actions context="machine.all" object-value="machine"></plugin-table-actions>
                        </ul>
                        </span>
                    </td>
                </tr>


                </tbody>
            </table>
        </div>
        <div class="table-footer">
            <table-footer objects="allMachineVm.machines" pageable="allMachineVm.pageable"
                          page-size="allManpowerVm.pageSize"
                          previous-page="allMachineVm.previousPage" next-page="allMachineVm.nextPage"></table-footer>
        </div>
    </div>
</div>
