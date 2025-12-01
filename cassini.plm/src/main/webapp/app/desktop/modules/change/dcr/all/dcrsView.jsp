<div>
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
            <span style="font-weight: 600;font-size: 20px;padding-right: 10px;" translate>DCRs</span>

            <button class="btn btn-sm new-button" ng-click="dcrsVm.newDCR()" id="newDcrButton"
                    ng-if="hasPermission('change','dcr','create') || hasPermission('change','create')"
                    title="{{'DCR_ALL_NEW_DCR' | translate}}">
                <i class="las la-plus" aria-hidden="true"></i>
                <span>{{'NEW' | translate}} {{'DCR' | translate }}</span>
            </button>

            <div class="btn-group">
                <button class="btn btn-sm btn-success" ng-click="dcrsVm.showTypeAttributes()" id="attributesButton"
                        title="{{dcrsVm.showAttributes}}">
                    <i class="fa fa-newspaper-o" style=""></i>
                </button>
                <button class="btn btn-sm btn-default" id="preferedPageButton"
                        style="" title="{{preferredPage}}"
                        ng-click="savePreferredPage()">
                    <i class="fa fa fa-anchor" style=""></i>
                </button>
            </div>
            <free-text-search on-clear="dcrsVm.resetPage" search-term="dcrsVm.searchText"
                              on-search="dcrsVm.freeTextSearch"
                              filter-search="dcrsVm.filterSearch"></free-text-search>
        </div>
        <div class="view-content no-padding" style="overflow-y: auto;padding: 10px;">
            <div class="responsive-table" style="padding: 10px;">
                <table class="table table-striped highlight-row">
                    <thead>
                    <tr>
                        <th style="width: 10px"></th>
                        <th style="width: 100px" translate>NUMBER</th>
                        <!-- <th class="col-width-150" translate>TYPE</th> -->
                        <!-- <th style="width: 150px;z-index: auto !important;">
                                <span ng-if="dcrsVm.selecteDcrType != null"
                                style="font-weight:normal;font-size: 13px;cursor: pointer !important;">
                                  ({{dcrsVm.selectedDcrType.name}})
                                  <i class="fa fa-times-circle" ng-click="dcrsVm.clearTypeSelection()"
                                     title="{{removeTitle}}"></i>
                          </span>
                          <br>
                              <div class="dropdown" uib-dropdown style="display: inline-block"
                                   ng-if="dcrsVm.search.searchType != 'advanced'">
                                  <span uib-dropdown-toggle><span translate>TYPE</span>
                                      <i class="caret dropdown-toggle" style="margin-left: 5px;cursor: pointer;"></i>
                                  </span>
                                  <div class="dropdown-menu" role="menu"
                                
                                       style="height: auto !important;max-height: 500px !important;overflow-x: hidden !important;min-width: 250px !important; min-height: 300px !important;margin-top:5px;font-weight: normal;">
                                           <dcr-type-tree 
                                                      on-select-type="dcrsVm.onSelectType"
                                                      change-type="DCR"></dcr-type-tree>     
                                  </div>   
                                </div> 
                            
                </th> -->


                <th>
                     <span ng-if="dcrsVm.selectedDcrType != null"
                    style="font-weight:normal;font-size: 13px;cursor: pointer !important;">
                    ({{dcrsVm.selectedDcrType.name}})
                    <i class="fa fa-times-circle" ng-click="dcrsVm.clearTypeSelection()"
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
                    <dcr-type-tree
                   on-select-type="dcrsVm.onSelectType"
                     change-type="DCR"></dcr-type-tree>
                  </div>
               </div>
                     </div>
                    </th>

                        <th class="col-width-200" translate>TITLE</th>
                        <th class="col-width-250" translate>DESCRIPTION_OF_CHANGE</th>
                        <!-- <th class="col-width-250" translate>CHANGE_REASON_TYPE</th> -->
                    <th style="width: 150px;z-index: auto !important;">
                        <span ng-if="selectedChangeReasonType!= null" 
                              style="font-weight:normal;font-size: 13px;cursor: pointer !important;text-transform: uppercase;">
                                ({{selectedChangeReasonType}})
                                <i class="fa fa-times-circle" ng-click="clearChangeReasonType()"
                                   title="{{removeTitle}}"></i>
                        </span>
                    <br>
                    <div class="dropdown" uib-dropdown style="display: inline-block">
                            <span uib-dropdown-toggle><span translate>CHANGE_REASON_TYPE</span>
                                <i class="caret dropdown-toggle" style="margin-left: 5px;cursor: pointer;"></i>
                            </span>
                        <ul uib-dropdown-menu class="page-size-dropdown dropdown-menu dropdown-menu-right">
                            <li ng-repeat="changeReasonType in changeReasonTypes"
                                ng-click="onSelectChangeReasonType(changeReasonType)" style="text-transform: uppercase;"><a
                                    href="">{{changeReasonType}}</a>
                            </li>
                        </ul>
                    </div>
                </th>
                        <th class="col-width-200" translate>REASON_FOR_CHANGE</th>
                        <th class="col-width-200" translate>PROPOSED_CHANGES</th>
                        <!-- <th style="width: 150px" translate>URGENCY</th> -->
                        <th style="width: 150px;z-index: auto !important;">
                            <span ng-if="selectedUrgency != null" 
                                  style="font-weight:normal;font-size: 13px;cursor: pointer !important;text-transform: uppercase;">
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
                                <li ng-repeat="urgency in urgencyes"
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
                                style="max-height:250px;overflow-y: auto;left:0;margin-top:5px;">
                                <li ng-repeat=" person in originators" ng-click="onSelectOriginator(person)">
                                    <a href="">{{person.fullName}}</a>
                                </li>
                            </ul>
                        </div>
                    </th>
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
                                style="max-height:250px;overflow-y: auto;left:0;margin-top:5px;">
                                <li ng-repeat=" person in requestedBy" ng-click="onSelectRequestedBy(person)">
                                    <a href="">{{person.fullName}}</a>
                                </li>
                            </ul>
                        </div>
                    </th>
                        <th style="width: 150px" translate>REQUESTED_DATE</th>
                        <th style="width: 150px" translate>APPROVED_REJECTED_DATE</th>
                        <th></th>
                        <th style="width: 150px;z-index: auto !important;">
                                <span ng-if="selectedStatus != null" 
                                      style="font-weight:normal;font-size: 13px;cursor: pointer !important;text-transform: uppercase;">
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
                                    style="max-height:250px;overflow-y: auto;left:0;margin-top:5px;">
                                    <li ng-repeat=" person in changeAnalysts" ng-click="onSelectChangeAnalyst(person)">
                                        <a href="">{{person.fullName}}</a>
                                    </li>
                                </ul>
                            </div>
                        </th>
                        <th class='added-column'
                            style="width: 150px;z-index: auto !important;"
                            ng-repeat="selectedAttribute in dcrsVm.selectedAttributes">
                            {{selectedAttribute.name}}
                            <i class="fa fa-times-circle"
                               ng-click="dcrsVm.removeAttribute(selectedAttribute)"
                               title={{dcrsVm.RemoveColumnTitle}}></i>
                        </th>
                        <th class="actions-col sticky-col sticky-actions-col" style="text-align:center; width: 80px;"
                            translate="ITEM_ALL_ACTIONS"></th>
                    </tr>
                    </thead>
                    <tbody>
                    <tr ng-if="dcrsVm.loading == true">
                        <td colspan="25">
                        <span style="font-size: 15px;">
                            <img src="app/assets/bower_components/cassini-platform/images/loaders/loader19.gif"
                                 class="mr5">
                            <span translate>LOADING_DCRS</span>
                        </span>
                        </td>
                    </tr>

                    <tr ng-if="dcrsVm.loading == false && dcrsVm.dcrs.content.length == 0">
                        <%-- <td colspan="25" translate>NO_DCRS</td>--%>
                        <td colspan="25" style="background-color: #f9fbfe  !important;color: unset !important;">
                            <div class="no-data">
                                <img src="app/assets/no_data_images/DCR.png" alt="" class="image">

                                <div class="message">{{noDcr}}</div>
                                <div class="message ng-scope" style="font-size: 14px;color: #8f979d;" translate>
                                    NO_PERMISSION_MESSAGE
                                </div>
                            </div>
                        </td>
                    </tr>

                    <tr ng-repeat="dcr in dcrsVm.dcrs.content">
                        <td style="width: 10px">
                            <span ng-if=dcr.isImplemented title="DCR Implemented">
                                <i class="fa flaticon-prize3 nav-icon-font"></i>
                            </span>
                        </td>
                        <td style="width: 150px;">
                            <a href="" ng-click="dcrsVm.showDCR(dcr)"
                               title="{{'CLICK_TO_SHOW_DETAILS' | translate}}">
                                <span ng-bind-html="dcr.crNumber | highlightText: freeTextQuery"></span>
                            </a>
                        </td>
                        <td class="col-width-150"><span ng-bind-html="dcr.crType | highlightText: freeTextQuery"></span>
                        </td>
                        <td class="col-width-200" title="{{dcr.title}}">
                            <span ng-bind-html="dcr.title  | highlightText: freeTextQuery"></span>
                            {{dcr.title.length > 20 ? '...' : ''}}
                        </td>
                        <td title="{{dcr.descriptionOfChange}}" class="col-width-250">
                            <span ng-bind-html="dcr.descriptionOfChange  | highlightText: freeTextQuery"></span>
                        </td>
                        <td class="col-width-250">
                            <span ng-bind-html="dcr.changeReasonType | highlightText: freeTextQuery"></span>
                        </td>
                        <td title="{{dcr.reasonForChange}}" class="col-width-200">
                            <span ng-bind-html="dcr.reasonForChange  | highlightText: freeTextQuery"></span>
                        </td>
                        <td class="col-width-200">{{dcr.proposedChanges}}</td>
                        <td>
                            <%--<dcr-urgency dcr="dcr"></dcr-urgency>--%>
                            {{dcr.urgency}}
                        </td>
                        <td>{{dcr.originator}}</td>
                        <td>{{dcr.requestedBy}}</td>
                        <td>{{dcr.requestedDate}}</td>
                        <td>{{dcr.approvedDate}}</td>
                        <td style="width: 1% !important;white-space: nowrap">
                            <i ng-if='dcr.startWorkflow && !dcr.finishWorkflow && !dcr.cancelWorkflow'
                               style='font-size: 18px;color: #ffa800' class='la la-clock-o' title="In Progress"></i>
                            <i ng-if='dcr.finishWorkflow' style='font-size: 18px;color: #1CAF9A'
                               class='la la-check-circle-o' title="Finished"></i>
                            <i ng-if='dcr.cancelWorkflow' style='font-size: 18px;color: #f64e60'
                               class='la la-times-circle-o' title="Rejected"></i>
                        </td>
                        <td>
                            <workflow-status-settings workflow="dcr"></workflow-status-settings>
                            <span class="label label-warning" ng-if="dcr.onHold">HOLD</span>
                        </td>
                        <td>{{dcr.workflow}}</td>
                        <td>{{dcr.changeAnalyst}}</td>
                        <td class="added-column"
                            ng-repeat="objectAttribute in dcrsVm.selectedAttributes">
                            <all-view-attributes object="dcr" object-attribute="objectAttribute"></all-view-attributes>
                        </td>
                        <td class="text-center actions-col sticky-col sticky-actions-col">
                          <span class="row-menu" uib-dropdown dropdown-append-to-body style="min-width: 50px">
                           <i class="fa fa-ellipsis-h dropdown-toggle" uib-dropdown-toggle></i>
                            <ul uib-dropdown-menu class="dropdown-menu dropdown-menu-right" role="menu"
                                style="z-index: 9999 !important;">
                                <li>
                                    <a href=""
                                       ng-click="showPrintOptions(dcr.id,'DCR')" translate>PREVIEW_AND_PRINT</a>
                                </li>
                                <tags-button object-type="'CHANGE'" object="dcr.id"
                                             tags-count="dcr.tagsCount"></tags-button>
                                <li title="{{ dcr.isApproved ? cannotDeleteApprovedDcr:''}}">
                                    <a ng-click="dcrsVm.deleteDcr(dcr)"
                                       ng-class="{'disabled':dcr.isApproved || dcr.statusType == 'REJECTED'}" translate>DELETE</a>
                                </li>
                                <plugin-table-actions context="dcr.all" object-value="dcr"></plugin-table-actions>
                            </ul>
                          </span>
                        </td>
                    </tr>
                    </tbody>
                </table>
            </div>
            <div class="table-footer">
                <table-footer objects="dcrsVm.dcrs" pageable="dcrsVm.pageable"
                              previous-page="dcrsVm.previousPage" next-page="dcrsVm.nextPage"
                              page-size="dcrsVm.pageSize"></table-footer>
            </div>
        </div>
    </div>
</div>
