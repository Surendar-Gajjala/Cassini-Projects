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
        <span style="font-weight: 600;font-size: 20px;padding-right: 10px;" translate>ECRs</span>

        <button class="btn btn-sm new-button" ng-click="ecrsVm.newEcr()" id="newEcrButton"
                ng-if="(hasPermission('change','ecr','create') || hasPermission('change','create'))"
                title="{{'ECR_ALL_NEW_ECR' | translate}}">
            <i class="las la-plus" aria-hidden="true"></i>
            <span>{{'NEW' | translate}} {{'ECR' | translate }}</span>
        </button>

        <div class="btn-group">
            <button class="btn btn-sm btn-success" ng-click="ecrsVm.showTypeAttributes()" id="attributesButton"
                    title="{{ecrsVm.showAttributes}}">
                <i class="fa fa-newspaper-o" style=""></i>
            </button>
            <button class="btn btn-sm btn-default" id="preferedPageButton"
                    style="" title="{{preferredPage}}"
                    ng-click="savePreferredPage()">
                <i class="fa fa fa-anchor" style=""></i>
            </button>
        </div>
        <free-text-search on-clear="ecrsVm.resetPage" search-term="ecrsVm.searchText"
                          on-search="ecrsVm.freeTextSearch"
                          filter-search="ecrsVm.filterSearch"></free-text-search>
    </div>
    <div class="view-content no-padding" style="overflow-y: auto;padding: 10px;">
        <div class="responsive-table" style="padding: 10px;">
            <table class="table table-striped highlight-row">
                <thead id="theadRow">
                <tr>
                    <th style="width: 20px;"></th>
                    <th style="width: 100px" translate>ECR_NUMBER</th>
                    <!-- <th class="col-width-150" translate>TYPE</th> -->
                    <th>
                        <span ng-if="ecrsVm.selectedEcrType != null"
                              style="font-weight:normal;font-size: 13px;cursor: pointer !important;">
                            ({{ecrsVm.selectedEcrType.name}})
                            <i class="fa fa-times-circle" ng-click="ecrsVm.clearTypeSelection()"
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
                                    <ecr-type-tree
                                            on-select-type="ecrsVm.onSelectType"
                                            change-type="ECR"></ecr-type-tree>
                                </div>
                            </div>
                        </div>
                    </th>
                    <th class="col-width-200" translate>TITLE</th>
                    <th class="col-width-250" translate>DESCRIPTION_OF_CHANGE</th>
                    <!-- <th class="col-width-200" translate>CHANGE_REASON_TYPE</th> -->
                    <th style="width: 150px;z-index: auto !important;">
                        <span ng-if="selectedChangeReasonType != null"
                              style="font-weight:normal;font-size: 13px;cursor: pointer !important;">
                                    ({{selectedChangeReasonType}})
                                    <i class="fa fa-times-circle" ng-click="clearChangeReasonType()"
                                       title="{{removeTitle}}"></i>
                            </span>
                        <br>

                        <div class="dropdown" uib-dropdown style="display: inline-block">
                                <span uib-dropdown-toggle><span translate>CHANGE_REASON_TYPE</span>
                                    <i class="caret dropdown-toggle" style="margin-left: 5px;cursor: pointer;"></i>
                                </span>
                            <ul uib-dropdown-menu class="page-size-dropdown dropdown-menu dropdown-menu-right"
                                style="max-height:250px;overflow-y: auto;right:auto;margin-top:5px;">
                                <li ng-repeat="changeReasonType in changeReasonTypes"
                                    ng-click="onSelectChangeReasonType(changeReasonType)"
                                    style="text-transform: uppercase;"><a
                                        href="">{{changeReasonType}}</a>
                                </li>
                            </ul>
                        </div>
                    </th>
                    <th class="col-width-250" translate>REASON_FOR_CHANGE</th>
                    <th class="col-width-250" translate>PROPOSED_CHANGES</th>
                    <!-- <th style="width: 100px" translate>URGENCY</th> -->
                    <th style="width: 150px;z-index: auto !important;">
                        <span ng-if="selectedUrgency != null"
                              style="font-weight:normal;font-size: 13px;cursor: pointer !important;">
                                    ({{selectedUrgency}})
                                    <i class="fa fa-times-circle" ng-click="clearUrgency()"
                                       title="{{removeTitle}}"></i>
                            </span>
                        <br>

                        <div class="dropdown" uib-dropdown style="display: inline-block">
                                <span uib-dropdown-toggle><span translate>URGENCY</span>
                                    <i class="caret dropdown-toggle" style="margin-left: 5px;cursor: pointer;"></i>
                                </span>
                            <ul uib-dropdown-menu class="page-size-dropdown dropdown-menu dropdown-menu-right"
                                style="max-height:250px;overflow-y: auto;left:0;margin-top:5px;">
                                <li ng-repeat="urgency in allUrgency"
                                    ng-click="onSelectUrgency(urgency)" style="text-transform: uppercase;"><a
                                        href="">{{urgency}}</a>
                                </li>
                            </ul>
                        </div>
                    </th>
                    <!-- <th style="width: 150px" translate>ORIGINATOR</th> -->
                    <th style="width: 150px;z-index: auto !important;">
                        <span ng-if="selectedOriginator != null"
                              style="font-weight:normal;font-size: 13px;cursor: pointer !important;">
                                ({{selectedOriginator.fullName}})
                                <i class="fa fa-times-circle" ng-click="clearOriginator()"
                                   title="{{removeTitle}}"></i>
                        </span>
                        <br>

                        <div class="dropdown" uib-dropdown style="display: inline-block">
                            <span uib-dropdown-toggle><span translate>ORIGINATOR</span>
                                <i class="caret dropdown-toggle" style="margin-left: 5px;cursor: pointer;"></i>
                            </span>
                            <ul uib-dropdown-menu class="page-size-dropdown dropdown-menu dropdown-menu-right"
                                style="max-height:250px;overflow-y: auto;right:auto;margin-top:5px;">
                                <li ng-repeat=" person in originators"
                                    ng-click="onSelectOriginator(person)"><a
                                        href="">{{person.fullName}}</a>
                                </li>
                            </ul>
                        </div>
                    </th>
                    <th style="width: 150px" translate>REQUESTER_TYPE</th>
                    <!-- <th style="width: 150px" translate>REQUESTED_BY</th> -->
                    <th style="width: 150px;z-index: auto !important;">
                        <span ng-if="selectedRequestedBy != null"
                              style="font-weight:normal;font-size: 13px;cursor: pointer !important;">
                                ({{selectedRequestedBy.fullName}})
                                <i class="fa fa-times-circle" ng-click="clearRequestedBy()"
                                   title="{{removeTitle}}"></i>
                        </span>
                        <br>

                        <div class="dropdown" uib-dropdown style="display: inline-block">
                            <span uib-dropdown-toggle><span translate>REQUESTED_BY</span>
                                <i class="caret dropdown-toggle" style="margin-left: 5px;cursor: pointer;"></i>
                            </span>
                            <ul uib-dropdown-menu class="page-size-dropdown dropdown-menu dropdown-menu-right"
                                style="max-height:250px;overflow-y: auto;right:auto;margin-top:5px;">
                                <li ng-repeat=" person in requesters"
                                    ng-click="onSelectRequestedBy(person)"><a
                                        href="">{{person.fullName}}</a>
                                </li>
                            </ul>
                        </div>
                    </th>
                    <th style="width: 150px" translate>REQUESTED_DATE</th>
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
                    <th style="width: 150px" translate>MODIFIED_BY</th>
                    <th style="width: 150px" translate>MODIFIED_DATE</th>
                    <th class='added-column'
                        style="width: 150px;z-index: auto !important;"
                        ng-repeat="selectedAttribute in ecrsVm.selectedAttributes">
                        {{selectedAttribute.name}}
                        <i class="fa fa-times-circle"
                           ng-click="ecrsVm.removeAttribute(selectedAttribute)"
                           title={{ecrsVm.RemoveColumnTitle}}></i>
                    </th>
                    <th class="actions-col sticky-col sticky-actions-col" style="text-align:center; width: 80px;"
                        translate>ACTIONS
                    </th>
                </tr>
                </thead>
                <tbody>
                <tr ng-if="ecrsVm.loading == true">
                    <td colspan="25">
                        <span style="font-size: 15px;">
                            <img src="app/assets/bower_components/cassini-platform/images/loaders/loader19.gif"
                                 class="mr5">
                            <span translate>LOADING_ECRS</span>
                        </span>
                    </td>
                </tr>

                <tr ng-if="ecrsVm.loading == false && ecrsVm.ecrs.content.length == 0">
                    <%-- <td colspan="25" translate>NO_ECRS</td>--%>
                    <td colspan="25" style="background-color:  #f9fbfe   !important;color: unset !important;">
                        <div class="no-data">
                            <img src="app/assets/no_data_images/ECR.png" alt="" class="image">

                            <div class="message" translate>NO_ECRS</div>
                            <div class="message ng-scope" style="font-size: 14px;color: #8f979d;" translate>
                                NO_PERMISSION_MESSAGE
                            </div>
                        </div>
                    </td>
                </tr>

                <tr ng-repeat="ecr in ecrsVm.ecrs.content">
                    <td style="width: 20px;">
                        <i class="fa fa-ils nav-icon-font" title="Implemented" ng-if="ecr.isImplemented"></i>
                    </td>
                    <td>
                        <a href="" ng-click="ecrsVm.showEcr(ecr)"
                           title="{{'CLICK_TO_SHOW_DETAILS' | translate}}">
                            <span ng-bind-html="ecr.crNumber | highlightText: freeTextQuery"></span>
                        </a>
                    </td>
                    <td class="col-width-150">
                        <span ng-bind-html="ecr.type | highlightText: freeTextQuery"></span>
                    </td>
                    <td class="col-width-200">
                        <span ng-bind-html="ecr.title | highlightText: freeTextQuery"></span>
                    </td>
                    <td class="col-width-250">
                        <span ng-bind-html="ecr.descriptionOfChange | highlightText: freeTextQuery"
                              title="{{ecr.descriptionOfChange}}"></span>
                    </td>
                    <td class="col-width-200">
                        <span ng-bind-html="ecr.changeReasonType | highlightText: freeTextQuery"></span>
                    </td>
                    <td class="col-width-250">
                        <span ng-bind-html="ecr.reasonForChange  | highlightText: freeTextQuery"
                              title="{{ecr.reasonForChange}}"></span>
                    </td>
                    <td class="col-width-250">
                        <span ng-bind-html="ecr.proposedChanges | highlightText: freeTextQuery"
                              title="{{ecr.proposedChanges}}"></span>
                    </td>
                    <td>
                        {{ecr.urgency}}
                        <%--<dcr-urgency dcr="ecr"></dcr-urgency>--%>
                    </td>
                    <td>{{ecr.originator}}</td>
                    <td>
                        <reporter-type reporter-type="ecr.requesterType"></reporter-type>
                    </td>
                    <td>{{ecr.requestedBy || ecr.otherRequested}}</td>
                    <td>{{ecr.requestedDate}}</td>
                    <td style="width: 1% !important;white-space: nowrap">
                        <i ng-if='ecr.startWorkflow && !ecr.finishWorkflow && !ecr.cancelWorkflow'
                           style='font-size: 18px;color: #ffa800' class='la la-clock-o' title="In Progress"></i>
                        <i ng-if='ecr.finishWorkflow' style='font-size: 18px;color: #1CAF9A'
                           class='la la-check-circle-o' title="Finished"></i>
                        <i ng-if='ecr.cancelWorkflow' style='font-size: 18px;color: #f64e60'
                           class='la la-times-circle-o' title="Rejected"></i>
                    </td>
                    <td>
                        <workflow-status-settings workflow="ecr"></workflow-status-settings>
                        <span class="label label-warning" ng-if="ecr.onHold">HOLD</span>
                    </td>
                    <td>{{ecr.changeAnalyst}}</td>
                    <td>{{ecr.modifiedBy}}</td>
                    <td>{{ecr.modifiedDate}}</td>
                    <td class="added-column"
                        ng-repeat="objectAttribute in ecrsVm.selectedAttributes">
                        <all-view-attributes object="ecr" object-attribute="objectAttribute"></all-view-attributes>
                    </td>
                    <td class="text-center actions-col sticky-col sticky-actions-col"
                        style="text-align:center; width: 80px;">
                        <span class="row-menu" uib-dropdown dropdown-append-to-body style=""
                              ng-if="hasPermission('change','ecr','delete')">
                         <i class="fa fa-ellipsis-h dropdown-toggle" uib-dropdown-toggle></i>
                            <ul uib-dropdown-menu class="dropdown-menu dropdown-menu-right" role="menu"
                                style="z-index: 9999 !important;">
                                <li>
                                    <a href=""
                                       ng-click="showPrintOptions(ecr.id,'ECR')" translate>PREVIEW_AND_PRINT</a>
                                </li>
                                <tags-button object-type="'CHANGE'" object="ecr.id"
                                             tags-count="ecr.tagsCount"></tags-button>
                                <li title="{{ecr.isApproved || ecr.statusType == 'REJECTED' ? cannotDeleteApprovedEcr:''}}">
                                    <a href="" ng-click="ecrsVm.deleteECR(ecr)"
                                       ng-class="{'disabled':ecr.isApproved || ecr.statusType == 'REJECTED'}"
                                       translate>DELETE</a>
                                </li>
                                <plugin-table-actions context="ecr.all" object-value="ecr"></plugin-table-actions>
                            </ul>
                        </span>
                    </td>
                </tr>
                </tbody>
            </table>
        </div>
        <div class="table-footer">
            <table-footer objects="ecrsVm.ecrs" pageable="ecrsVm.pageable"
                          previous-page="ecrsVm.previousPage" next-page="ecrsVm.nextPage"
                          page-size="ecrsVm.pageSize"></table-footer>
        </div>
    </div>
</div>
