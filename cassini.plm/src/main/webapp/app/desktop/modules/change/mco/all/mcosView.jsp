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
        <span style="font-weight: 600;font-size: 20px;padding-right: 10px;" translate>MCOs</span>
        <button class="btn btn-sm new-button" ng-click="mcosVm.newMco()" id="newMcoButton"
                title="{{'MCO_ALL_NEW_MCO' | translate}}"
                ng-if="hasPermission('change','mco','create') || hasPermission('change','create')">
            <i class="las la-plus" aria-hidden="true"></i>
            <span>{{'NEW' | translate}} {{'MCO' | translate }}</span>
        </button>

        <div class="btn-group">
            <button class="btn btn-sm btn-success" ng-click="mcosVm.showTypeAttributes()" id="attributesButton"
                    title="{{mcosVm.showAttributes}}">
                <i class="fa fa-newspaper-o" style=""></i>
            </button>
            <button class="btn btn-sm btn-default" id="preferedPageButton"
                    style="" title="{{preferredPage}}"
                    ng-click="savePreferredPage()">
                <i class="fa fa fa-anchor" style=""></i>
            </button>
        </div>
        <span class="form-check" style="padding:10px;border-right: none;">
            <label class="form-check-label" style="margin-right: 5px">
                <input class="form-check-input" type="radio" name="itemMcoTypes"
                       id="itemMcoType"
                       ng-click="mcosVm.selectMCOType('ITEMMCO', $event)"
                       checked>
                <span style="padding: 2px;margin-left: 5px;" translate>PRODUCTS</span>
            </label>
            <label class="form-check-label" style="margin-right: 5px">
                <input class="form-check-input" type="radio" name="manufacturerMcoTypes"
                       id="manufacturerMcoType"
                       ng-click="mcosVm.selectMCOType('OEMPARTMCO', $event)">
                <span style="padding: 2px;margin-left: 5px;" translate>MATERIALS</span>
            </label>
        </span>
        <free-text-search on-clear="mcosVm.resetPage" search-term="mcosVm.searchText"
                          on-search="mcosVm.freeTextSearch"
                          filter-search="mcosVm.filterSearch"></free-text-search>
    </div>
    <div class="view-content no-padding" style="overflow-y: auto;padding: 10px;">
        <div class="responsive-table" style="padding: 10px;">
            <table class="table table-striped highlight-row">
                <thead>
                <tr>
                    <th style="width: 100px" translate>NUMBER</th>
                    <!-- <th class="col-width-150" translate>TYPE</th> -->
                    <th style="width: 150px;z-index: auto !important;">
                                <span ng-show="allMCOType == 'ITEMMCO'">
                                    <span ng-if="mcosVm.selectedMcoType != null"
                                          style="font-weight:normal;font-size: 13px;cursor: pointer !important;">
                                      ({{mcosVm.selectedMcoType.name}})
                                      <i class="fa fa-times-circle" ng-click="mcosVm.clearTypeSelection()"
                                         title="{{removeTitle}}"></i>
                              </span>
                              <br>
                                  <div class="dropdown" uib-dropdown style="display: inline-block"
                                       ng-if="mcosVm.search.searchType != 'advanced'">
                                      <span uib-dropdown-toggle><span translate>TYPE</span>
                                          <i class="caret dropdown-toggle"
                                             style="margin-left: 5px;cursor: pointer;"></i>
                                      </span>

                                      <div class="dropdown-menu" role="menu"
                                           style="height: auto !important;max-height: 500px !important;overflow-x: hidden !important;min-width: 250px !important; min-height: 300px !important;margin-top:5px;font-weight: normal;">
                                          <mco-type-tree
                                                  ng-if="allMCOType == 'ITEMMCO'"
                                                  on-select-type="mcosVm.onSelectType"
                                                  change-type="ITEMMCO"></mco-type-tree>
                                      </div>
                                  </div>
                                </span>

                                
                                 <span ng-show="allMCOType == 'OEMPARTMCO'">
                                    <span ng-if="mcosVm.selectedMcoType != null"
                                          style="font-weight:normal;font-size: 13px;cursor: pointer !important;">
                                      ({{mcosVm.selectedMcoType.name}})
                                      <i class="fa fa-times-circle" ng-click="mcosVm.clearTypeSelection()"
                                         title="{{removeTitle}}"></i>
                              </span>
                              <br>
                                  <div class="dropdown" uib-dropdown style="display: inline-block"
                                       ng-if="mcosVm.search.searchType != 'advanced'">
                                      <span uib-dropdown-toggle><span translate>TYPE</span>
                                          <i class="caret dropdown-toggle"
                                             style="margin-left: 5px;cursor: pointer;"></i>
                                      </span>

                                      <div class="dropdown-menu" role="menu"
                                           style="height: auto !important;max-height: 500px !important;overflow-x: hidden !important;min-width: 250px !important; min-height: 300px !important;margin-top:5px;font-weight: normal;">
                                          <change-object-type-tree
                                                  ng-if="allMCOType == 'OEMPARTMCO'"
                                                  on-select-type="mcosVm.onSelectType"
                                                  change-type="OEMPARTMCO"></change-object-type-tree>
                                      </div>
                                  </div>
                                </span>
                    </th>
                    <th class="col-width-200" translate>TITLE</th>
                    <th class="col-width-250" translate>DESCRIPTION</th>
                    <th class="col-width-250" translate>REASON_FOR_CHANGE</th>
                    <!-- <th style="width: 150px" translate>CHANGE_ANALYST</th> -->
                    <th style="width: 150px;z-index: auto !important;">
                            <span ng-show="allMCOType == 'ITEMMCO'">
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
                            </span>
                           <span ng-show="allMCOType == 'OEMPARTMCO'">
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
                            </span>
                    </th>
                    <th></th>
                    <th style="width: 150px;z-index: auto !important;">
                            <span ng-show="allMCOType == 'ITEMMCO'">
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
                            </span>
                            <span ng-show="allMCOType == 'OEMPARTMCO'">
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

                            </span>
                    </th>
                    <th style="width: 150px" translate>RELEASED_DATE</th>
                    <th style="width: 150px" translate>MODIFIED_BY</th>
                    <th style="width: 150px" translate>MODIFIED_DATE</th>
                    <th class='added-column' style="width: 150px;z-index: auto !important;"
                        ng-repeat="selectedAttribute in mcosVm.selectedAttributes">
                        {{selectedAttribute.name}}
                        <i class="fa fa-times-circle" ng-click="mcosVm.removeAttribute(selectedAttribute)"
                           title={{mcosVm.RemoveColumnTitle}}></i>
                    </th>
                    <th class="actions-col sticky-col sticky-actions-col" style="text-align:center; width: 80px;"
                        translate>ACTIONS
                    </th>
                </tr>
                </thead>
                <tbody>
                <tr ng-if="mcosVm.loading == true">
                    <td colspan="25">
                            <span style="font-size: 15px;">
                                <img src="app/assets/bower_components/cassini-platform/images/loaders/loader19.gif"
                                     class="mr5">
                                <span translate>LOADING_MCOS</span>
                            </span>
                    </td>
                </tr>

                <tr ng-if="mcosVm.loading == false && mcosVm.mcos.content.length == 0">
                    <%--<td colspan="25" translate>NO_MCOS</td>--%>
                    <td colspan="25" style="background-color:  #f9fbfe  !important;color: unset !important;">
                        <div class="no-data">
                            <img src="app/assets/no_data_images/MCO.png" alt="" class="image">

                            <div class="message" translate>NO_MCOS</div>
                            <div class="message ng-scope" style="font-size: 14px;color: #8f979d;" translate>
                                NO_PERMISSION_MESSAGE
                            </div>
                        </div>
                    </td>
                </tr>

                <tr ng-repeat="mco in mcosVm.mcos.content">
                    <td>
                        <a href="" ng-click="mcosVm.showMco(mco)" title="{{'CLICK_TO_SHOW_DETAILS' | translate}}">
                            <span ng-bind-html="mco.mcoNumber | highlightText: freeTextQuery"></span>
                        </a>
                    </td>
                    <td class="col-width-150">
                        <span ng-bind-html="mco.mcoType.name | highlightText: freeTextQuery"></span>
                    </td>
                    <td class="col-width-200" title="{{mco.title}}">
                        <span ng-bind-html="mco.title  | highlightText: freeTextQuery"></span>
                    </td>
                    <td title="{{mco.description}}" class="col-width-250">
                        <span ng-bind-html="mco.description |  highlightText: freeTextQuery"></span>
                    </td>
                    <td title="{{mco.reasonForChange}}" class="col-width-250">
                        <span ng-bind-html="mco.reasonForChange | highlightText: freeTextQuery"></span>
                    </td>
                    <td>{{mco.changeAnalystObject.fullName}}</td>
                    <td style="width: 1% !important;white-space: nowrap">
                        <i ng-if='mco.startWorkflow && !mco.finishWorkflow && !mco.cancelWorkflow'
                           style='font-size: 18px;color: #ffa800' class='la la-clock-o' title="In Progress"></i>
                        <i ng-if='mco.finishWorkflow' style='font-size: 18px;color: #1CAF9A'
                           class='la la-check-circle-o' title="Finished"></i>
                        <i ng-if='mco.cancelWorkflow' style='font-size: 18px;color: #f64e60'
                           class='la la-times-circle-o' title="Rejected"></i>
                    </td>
                    <td>
                        <workflow-status-settings workflow="mco"></workflow-status-settings>
                        <span class="label label-warning" ng-if="mco.onHold">HOLD</span>
                    </td>
                    <td>{{mco.releasedDate}}</td>
                    <td>{{mco.modifiedByObject.fullName}}</td>
                    <td>{{mco.modifiedDate}}</td>
                    <td class="added-column" ng-repeat="objectAttribute in mcosVm.selectedAttributes">
                        <all-view-attributes object="mco" object-attribute="objectAttribute"></all-view-attributes>
                    </td>
                    <td class="text-center actions-col sticky-col sticky-actions-col"
                        style="text-align:center; width: 80px;">
                            <span class="row-menu" uib-dropdown dropdown-append-to-body
                                  ng-if="hasPermission('change','mco','delete') || hasPermission('change','delete')">
                                <i class="fa fa-ellipsis-h dropdown-toggle" uib-dropdown-toggle></i>
                                <ul uib-dropdown-menu class="dropdown-menu dropdown-menu-right" role="menu"
                                    style="z-index: 9999 !important;">
                                    <li>
                                        <a href="" ng-click="showPrintOptions(mco.id,'MCO')"
                                           translate>PREVIEW_AND_PRINT</a>
                                    </li>
                                    <tags-button object-type="'CHANGE'" object="mco.id" tags-count="mco.tags.length">
                                    </tags-button>
                                    <li
                                            title="{{ mco.released || mco.statusType == 'REJECTED' ? cannotDeleteApprovedMco:''}}">
                                        <a href="" ng-click="mcosVm.deleteMCO(mco)"
                                           ng-class="{'disabled':mco.released || mco.statusType == 'REJECTED'}"
                                           translate>DELETE</a>
                                    </li>
                                    <plugin-table-actions context="mco.all" object-value="mco"></plugin-table-actions>
                                </ul>
                            </span>
                    </td>
                </tr>
                </tbody>
            </table>
        </div>
        <div class="table-footer">
            <table-footer objects="mcosVm.mcos" pageable="mcosVm.pageable" previous-page="mcosVm.previousPage"
                          next-page="mcosVm.nextPage" page-size="mcosVm.pageSize"></table-footer>
        </div>
    </div>
</div>