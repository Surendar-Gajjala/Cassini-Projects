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

    #freeTextSearchDirective {
        top: 7px !important;
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
        <span style="font-weight: 600;font-size: 20px;padding-right: 10px;" translate>DCOs</span>

        <button class="btn btn-sm new-button" ng-click="dcosVm.newDCO()" id="newButton"
                ng-if="hasPermission('change','dco','create') || hasPermission('change','create')"
                title="{{'DCO_ALL_NEW_DCO' | translate}}">
            <i class="las la-plus" aria-hidden="true"></i>
            <span>{{'NEW' | translate}} {{'DCO' | translate }}</span>
        </button>

        <div class="btn-group">
            <button class="btn btn-sm btn-success" ng-click="dcosVm.showTypeAttributes()" id="attributesButton"
                    title="{{dcosVm.showAttributes}}">
                <i class="fa fa-newspaper-o" style=""></i>
            </button>
            <button class="btn btn-sm btn-default" id="preferedPageButton"
                    style="" title="{{preferredPage}}"
                    ng-click="savePreferredPage()">
                <i class="fa fa fa-anchor" style=""></i>
            </button>
        </div>
        <free-text-search on-clear="dcosVm.resetPage" search-term="dcosVm.searchText"
                          on-search="dcosVm.freeTextSearch"
                          filter-search="dcosVm.filterSearch"></free-text-search>
    </div>
    <div class="view-content no-padding" style="overflow-y: auto;padding: 10px;">
        <div class="responsive-table" style="padding: 10px;">
            <table class="table table-striped highlight-row">
                <thead>
                <tr>
                    <th style="width: 100px" translate>NUMBER</th>
                    <!-- <th class="col-width-150" translate>TYPE</th> -->
                    <th>
                        <span ng-if="dcosVm.selectedDcoType != null"
                          style="font-weight:normal;font-size: 13px;cursor: pointer !important;">
                            ({{dcosVm.selectedDcoType.name}})
                            <i class="fa fa-times-circle" ng-click="dcosVm.clearTypeSelection()"
                               title="{{removeTitle}}"></i>
                      </span>
                    <br>
                            <div class="dropdown" uib-dropdown style="display: inline-block">
                                <span uib-dropdown-toggle><span translate>TYPE</span>
                                    <i class="caret dropdown-toggle" style="margin-left: 5px;cursor: pointer;"></i>
                                </span>
                                            <div class="dropdown-menu" role="menu">
                                                <div
                                                style="overflow: auto;padding: 5px; border: 1px solid #FFF;width: 300px;height: 200px;">
                                                <dco-type-tree
                                                    on-select-type="dcosVm.onSelectType"
                                                    change-type="DCO"></dco-type-tree>
                                        </div>
                                            </div>
                            </div>
                    </th>
                    <th class="col-width-200" translate>TITLE</th>
                    <th class="col-width-250" translate>DESCRIPTION</th>
                    <th class="col-width-250" translate>REASON_FOR_CHANGE</th>
                    <!-- <th style="width: 150px" translate>CHANGE_ANALYST</th> -->
                    <th style="width: 150px;z-index: auto !important;">
                        <span ng-if="selectedPerson != null"
                              style="font-weight:normal;font-size: 13px;cursor: pointer !important;">
                                ({{selectedPerson.fullName}})
                                <i class="fa fa-times-circle" ng-click="clearChangeAnalyst()"
                                   title="{{removeTitle}}"></i>
                        </span>
                        <br>

                        <div class="dropdown" uib-dropdown style="display: inline-block">
                            <span uib-dropdown-toggle><span translate>CHANGE_ANALYST</span>
                                <i class="caret dropdown-toggle" style="margin-left: 5px;cursor: pointer;"></i>
                            </span>
                            <ul uib-dropdown-menu class="page-size-dropdown dropdown-menu dropdown-menu-right"
                                style="max-height:250px;overflow-y: auto;right:auto;margin-top:5px;">
                                <li ng-repeat=" person in changeAnalysts"
                                    ng-click="onSelectChangeAnalyst(person)"><a
                                        href="">{{person.fullName}}</a>
                                </li>
                            </ul>
                        </div>
                    </th>
                    <th></th>
                    <th style="width: 150px;z-index: auto !important;">
                        <span ng-if="selectedStatus != null"
                                  style="font-weight:normal;font-size: 13px;cursor: pointer !important;">
                                    ({{selectedStatus}})
                                    <i class="fa fa-times-circle" ng-click="clearStatus()"
                                       title="{{removeTitle}}"></i>
                            </span>
                        <br>
                        <div class="dropdown" uib-dropdown style="display: inline-block">
                                <span uib-dropdown-toggle><span translate>STATUS</span>
                                    <i class="caret dropdown-toggle" style="margin-left: 5px;cursor: pointer;"></i>
                                </span>
                            <ul uib-dropdown-menu class="page-size-dropdown dropdown-menu dropdown-menu-right"
                                style="max-height:250px;overflow-y: auto;left:0;margin-top:5px;">
                                <li ng-repeat="status in statuses"
                                    ng-click="onSelectStatus(status)" style="text-transform: uppercase;"><a
                                        href="">{{status}}</a>
                                </li>
                            </ul>
                        </div>
                    </th>
                    <th style="width: 150px" translate>Workflow</th>
                    <th style="width: 150px" translate>RELEASED_REJECTED_DATE</th>
                    <th style="width: 150px" translate>MODIFIED_BY</th>
                    <th style="width: 150px" translate>MODIFIED_DATE</th>
                    <th class='added-column'
                        style="width: 150px;z-index: auto !important;"
                        ng-repeat="selectedAttribute in dcosVm.selectedAttributes">
                        {{selectedAttribute.name}}
                        <i class="fa fa-times-circle"
                           ng-click="dcosVm.removeAttribute(selectedAttribute)"
                           title={{dcosVm.RemoveColumnTitle}}></i>
                    </th>
                    <th class="actions-col sticky-col sticky-actions-col" style="text-align:center; width: 80px;"
                        translate="ITEM_ALL_ACTIONS"></th>
                </tr>
                </thead>
                <tbody>
                <tr ng-if="dcosVm.loading == true">
                    <td colspan="25">
                        <span style="font-size: 15px;">
                            <img src="app/assets/bower_components/cassini-platform/images/loaders/loader19.gif"
                                 class="mr5">
                            <span translate>LOADING_DCOS</span>
                        </span>
                    </td>
                </tr>

                <tr ng-if="dcosVm.loading == false && dcosVm.dcos.content.length == 0">
                    <td colspan="25" style="background-color: #f9fbfe   !important;color: unset !important;">
                        <div class="no-data">
                            <img src="app/assets/no_data_images/DCO.png" alt="" class="image">

                            <div class="message">{{noDcos}}</div>
                            <div class="message ng-scope" style="font-size: 14px;color: #8f979d;" translate>NO_PERMISSION_MESSAGE</div>
                        </div>
                    </td>

                </tr>

                <tr ng-repeat="dco in dcosVm.dcos.content">
                    <td style="width: 100px;">
                        <a href="" ng-click="dcosVm.showDCO(dco)"
                           title="{{'CLICK_TO_SHOW_DETAILS' | translate}}">
                            <span ng-bind-html="dco.dcoNumber | highlightText: freeTextQuery"></span>
                        </a>
                    </td>
                    <td class="col-width-150">
                        <span ng-bind-html="dco.dcoType | highlightText: freeTextQuery"></span>
                    </td>
                    <td class="col-width-200">
                        <span ng-bind-html="dco.title | highlightText: freeTextQuery"></span>
                    </td>
                    <td class="col-width-250" title="{{dco.description}}">
                        <span ng-bind-html="dco.description | highlightText: freeTextQuery"></span>
                    </td>
                    <td class="col-width-250" title="{{dco.reasonForChange}}">
                        <span ng-bind-html="dco.reasonForChange | highlightText: freeTextQuery"></span>
                    </td>
                    <td>{{dco.changeAnalyst}}</td>
                    <td style="width: 1% !important;white-space: nowrap">
                        <i ng-if='dco.startWorkflow && !dco.finishWorkflow && !dco.cancelWorkflow'
                           style='font-size: 18px;color: #ffa800' class='la la-clock-o' title="In Progress"></i>
                        <i ng-if='dco.finishWorkflow' style='font-size: 18px;color: #1CAF9A'
                           class='la la-check-circle-o' title="Finished"></i>
                        <i ng-if='dco.cancelWorkflow' style='font-size: 18px;color: #f64e60'
                           class='la la-times-circle-o' title="Rejected"></i>
                    </td>
                    <td>
                        <workflow-status-settings workflow="dco"></workflow-status-settings>
                        <span class="label label-warning" ng-if="dco.onHold">HOLD</span>
                    </td>
                    <td>{{dco.workflow}}</td>
                    <td>{{dco.releasedDate}}</td>
                    <td>{{dco.modifiedBy}}</td>
                    <td>{{dco.modifiedDate}}</td>
                    <td class="added-column"
                        ng-repeat="objectAttribute in dcosVm.selectedAttributes">
                        <all-view-attributes object="dco" object-attribute="objectAttribute"></all-view-attributes>
                    </td>
                    <td class="text-center actions-col sticky-col sticky-actions-col">
                        <span class="row-menu" uib-dropdown dropdown-append-to-body style="min-width: 50px"
                              ng-if="hasPermission('change','dco','delete') || hasPermission('change','delete')">
                           <i class="fa fa-ellipsis-h dropdown-toggle" uib-dropdown-toggle></i>
                            <ul uib-dropdown-menu class="dropdown-menu dropdown-menu-right" role="menu"
                                style="z-index: 9999 !important;">
                                <li>
                                    <a href=""
                                       ng-click="showPrintOptions(dco.id,'DCO')" translate>PREVIEW_AND_PRINT</a>
                                </li>
                                <tags-button object-type="'CHANGE'" object="dco.id"
                                             tags-count="dco.tagsCount"></tags-button>
                                <li title="{{ dco.isReleased || dco.statusType == 'REJECTED' ? cannotDeleteApprovedDco:''}}">
                                    <a ng-click="dcosVm.deleteDco(dco)"
                                       ng-class="{'disabled': dco.isReleased || dco.statusType == 'REJECTED'}"
                                       translate>
                                        DELETE
                                    </a>
                                </li>
                                <plugin-table-actions context="dco.all" object-value="dco"></plugin-table-actions>
                            </ul>
                         </span>
                    </td>
                </tr>
                </tbody>
            </table>
        </div>
        <div class="table-footer">
            <table-footer objects="dcosVm.dcos" pageable="dcosVm.pageable"
                          previous-page="dcosVm.previousPage" next-page="dcosVm.nextPage"
                          page-size="dcosVm.pageSize"></table-footer>
        </div>
    </div>
</div>
