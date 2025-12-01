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
        <span style="font-weight: 600;font-size: 20px;padding-right: 10px;" translate>INSPECTIONPLANS</span>

        <button class="btn btn-sm new-button" ng-click="allInspectionPlansVm.newInspectionPlan()"
                id="newInspectionPlanButton"
                title="{{createPlanTitle}}" ng-if="hasPermission('inspectionplan','create')">
            <i class="las la-plus" aria-hidden="true"></i>
            <span>{{'NEW' | translate}} {{'INSPECTION_PLAN' | translate }}</span>
        </button>

        <div class="btn-group">
            <button class="btn btn-sm btn-success" ng-click="allInspectionPlansVm.showTypeAttributes()"
                    id="attributesButton"
                    title="{{allInspectionPlansVm.showAttributes}}">
                <i class="fa fa-newspaper-o" style=""></i>
            </button>
            <button class="btn btn-sm btn-default" id="preferredPageButton"
                    style="" title="{{preferredPage}}"
                    ng-click="savePreferredPage()">
                <i class="fa fa fa-anchor" style=""></i>
            </button>
        </div>
        <span class="form-check" style="padding:10px;border-right: none;">
            <label class="form-check-label" style="margin-right: 5px">
                <input class="form-check-input" type="radio" name="inspectionPlanTypes"
                       id="productType"
                       ng-click="allInspectionPlansVm.selectInspectionPlanType('PRODUCTINSPECTIONPLAN', $event)"
                       checked>
                <span style="padding: 2px;margin-left: 5px;" translate>PRODUCTS</span>
            </label>
            <label class="form-check-label" style="margin-right: 5px">
                <input class="form-check-input" type="radio" name="inspectionPlanTypes"
                       id="materialType"
                       ng-click="allInspectionPlansVm.selectInspectionPlanType('MATERIALINSPECTIONPLAN', $event)">
                <span style="padding: 2px;margin-left: 5px;" translate>MATERIALS</span>
            </label>
        </span>

        <free-text-search on-clear="allInspectionPlansVm.resetPage" search-term="allInspectionPlansVm.searchText"
                          on-search="allInspectionPlansVm.freeTextSearch"
                          filter-search="allInspectionPlansVm.filterSearch"></free-text-search>
    </div>
    <div class="view-content no-padding" style="overflow-y: auto;padding: 10px;">
        <div class="responsive-table" style="padding: 10px;">
            <table class="table table-striped highlight-row">
                <thead>
                <tr>
                    <th style="width: 100px;" translate>NUMBER</th>
                    <!-- <th class="col-width-150" translate>TYPE</th> -->

                    <!-- <th class="col-width-200" translate>TYPE</th> -->
                    <th>
                                            <span ng-if="allInspectionPlansVm.selectedProductType != null"
                                                  style="font-weight:normal;font-size: 13px;cursor: pointer !important;">
                                                ({{allInspectionPlansVm.selectedProductType.name}})
                                                <i class="fa fa-times-circle"
                                                   ng-click="allInspectionPlansVm.clearTypeSelection()"
                                                   title="{{removeTitle}}"></i>
                                          </span>
                        <br>

                        <div class="dropdown" uib-dropdown style="display: inline-block">
                                                    <span uib-dropdown-toggle><span translate>TYPE</span>
                                                        <i class="caret dropdown-toggle"
                                                           style="margin-left: 5px;cursor: pointer;"></i>
                                                    </span>

                            <div class="dropdown-menu" role="menu">
                                <div
                                        style="overflow: auto;padding: 5px; border: 1px solid #FFF;width: 300px;height: 200px;">
                                    <quality-object-type-tree
                                            ng-if="allInspectionPlanType == 'PRODUCTINSPECTIONPLAN'"
                                            on-select-type="allInspectionPlansVm.onSelectType"
                                            quality-type="PRODUCTINSPECTIONPLANTYPE"></quality-object-type-tree>
                                    <quality-object-tree
                                            ng-if="allInspectionPlanType == 'MATERIALINSPECTIONPLAN'"
                                            on-select-type="allInspectionPlansVm.onSelectType"
                                            quality-type="MATERIALINSPECTIONPLANTYPE"></quality-object-tree>
                                </div>
                            </div>
                        </div>
                    </th>


                    <th class="col-width-150">
                        <span ng-show="allInspectionPlanType == 'PRODUCTINSPECTIONPLAN'">
                               <span ng-if="allInspectionPlansVm.selectedProduct != null"
                                     style="font-weight:normal;font-size: 13px;cursor: pointer !important;">
                                        ({{allInspectionPlansVm.selectedProduct}})
                                        <i class="fa fa-times-circle" ng-click="allInspectionPlansVm.clearProduct()"
                                           title="{{removeTitle}}"></i>
                                </span>
                        <br>

                        <div class="dropdown" uib-dropdown style="display: inline-block">
                                    <span uib-dropdown-toggle><span translate>PRODUCT</span>
                                        <i class="caret dropdown-toggle" style="margin-left: 5px;cursor: pointer;"></i>
                                    </span>
                            <ul uib-dropdown-menu class="page-size-dropdown dropdown-menu dropdown-menu-right"
                                style="max-height:250px;overflow-y: auto;left:0;margin-top:5px;">
                                <li ng-repeat="inspectionPlan in allInspectionPlansVm.inspectionPlans.content"
                                    ng-click="allInspectionPlansVm.onSelectProduct(inspectionPlan)"><a
                                        href="">{{inspectionPlan.productName}}</a>
                                </li>
                            </ul>
                        </div>
                        </span>
                        <span ng-show="allInspectionPlanType == 'MATERIALINSPECTIONPLAN'">

                               <span ng-if="allInspectionPlansVm.selectedMaterial != null"
                                     style="font-weight:normal;font-size: 13px;cursor: pointer !important;">
                                        ({{allInspectionPlansVm.selectedMaterial}})
                                        <i class="fa fa-times-circle" ng-click="allInspectionPlansVm.clearMaterial()"
                                           title="{{removeTitle}}"></i>
                                </span>
                        <br>

                        <div class="dropdown" uib-dropdown style="display: inline-block">
                                    <span uib-dropdown-toggle><span translate>MATERIAL</span>
                                        <i class="caret dropdown-toggle" style="margin-left: 5px;cursor: pointer;"></i>
                                    </span>
                            <ul uib-dropdown-menu class="page-size-dropdown dropdown-menu dropdown-menu-right"
                                style="max-height:250px;overflow-y: auto;left:0;margin-top:5px;">
                                <li ng-repeat="material in allInspectionPlansVm.inspectionPlans.content"
                                    ng-click="allInspectionPlansVm.onSelectMaterial(material)"><a
                                        href="">{{material.materialName}}</a>
                                </li>
                            </ul>
                        </div>
                        </span>


                    </th>

                    <th class="col-width-200" translate>NAME</th>
                    <th class="col-width-250" translate>DESCRIPTION</th>
                    <th style="width: 100px;text-align: center;z-index: auto !important;"
                        translate="ITEM_ALL_REVISION"></th>
                    <!-- <th style="width: 150px" translate>LIFECYCLE</th> -->

                    <th style="width: 150px;z-index: auto !important;">
                            <span ng-if="allInspectionPlansVm.selectedPhase != null"
                                  style="font-weight:normal;font-size: 13px;cursor: pointer !important;">
                                        ({{allInspectionPlansVm.selectedPhase}})
                                        <i class="fa fa-times-circle" ng-click="allInspectionPlansVm.clearPhase()"
                                           title="{{removeTitle}}"></i>
                                </span>
                        <br>

                        <div class="dropdown" uib-dropdown style="display: inline-block">
                                    <span uib-dropdown-toggle><span translate>ITEM_ALL_LIFECYCLE</span>
                                        <i class="caret dropdown-toggle" style="margin-left: 5px;cursor: pointer;"></i>
                                    </span>
                            <ul uib-dropdown-menu class="page-size-dropdown dropdown-menu dropdown-menu-right"
                                style="max-height:250px;overflow-y: auto;left:0;margin-top:5px;">
                                <li ng-repeat="phase in allInspectionPlansVm.lifecyclePhases"
                                    ng-click="allInspectionPlansVm.onSelectPhase(phase)"
                                    style="text-transform: uppercase;"><a
                                        href="">{{phase}}</a>
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
                    <th class="col-width-200" translate>NOTES</th>
                    <th style="width: 150px" translate>MODIFIED_BY</th>
                    <th style="width: 150px" translate>MODIFIED_DATE</th>
                    <th class='added-column'
                        style="width: 150px;z-index: auto !important;"
                        ng-repeat="selectedAttribute in allInspectionPlansVm.selectedAttributes">
                        {{selectedAttribute.name}}
                        <i class="fa fa-times-circle"
                           ng-click="allInspectionPlansVm.removeAttribute(selectedAttribute)"
                           title={{allInspectionPlansVm.RemoveColumnTitle}}></i>
                    </th>
                    <th class="actions-col sticky-col sticky-actions-col" style="text-align:center; width: 80px;"
                        translate>ACTIONS
                    </th>
                </tr>
                </thead>
                <tbody>
                <tr ng-if="allInspectionPlansVm.loading == true">
                    <td colspan="25">
                        <span style="font-size: 15px;">
                            <img src="app/assets/bower_components/cassini-platform/images/loaders/loader19.gif"
                                 class="mr5">
                            <span translate>LOADING_PLANS</span>
                        </span>
                    </td>
                </tr>

                <tr ng-if="allInspectionPlansVm.loading == false && allInspectionPlansVm.inspectionPlans.content.length == 0">

                    <td colspan="25" style="background-color: #f9fbfe  !important;color: unset !important;">
                        <div class="no-data">
                            <img src="app/assets/no_data_images/inspectionPlan.png" alt="" class="image">

                            <div class="message">{{noPlans}}</div>
                            <div class="message ng-scope" style="font-size: 14px;color: #8f979d;" translate>
                                NO_PERMISSION_MESSAGE
                            </div>
                        </div>
                    </td>

                </tr>

                <tr ng-repeat="inspectionPlan in allInspectionPlansVm.inspectionPlans.content">
                    <td style="width: 100px;">
                        <a href="" ng-click="allInspectionPlansVm.showPlan(inspectionPlan)"
                           title="{{'CLICK_TO_SHOW_DETAILS' | translate}}">
                            <span ng-bind-html="inspectionPlan.number | highlightText: freeTextQuery"></span>
                        </a>
                    </td>
                    <td class="col-width-150">
                        <span ng-bind-html="inspectionPlan.type | highlightText: freeTextQuery"></span>
                    </td>
                    <td class="col-width-150" ng-show="allInspectionPlanType == 'PRODUCTINSPECTIONPLAN'">
                        <span ng-bind-html="inspectionPlan.productName | highlightText: freeTextQuery"></span>
                    </td>
                    <td class="col-width-150" ng-show="allInspectionPlanType == 'MATERIALINSPECTIONPLAN'">
                        <span ng-bind-html="inspectionPlan.materialName | highlightText: freeTextQuery"></span>
                    </td>
                    <td class="col-width-200" title="{{inspectionPlan.name}}">
                        <span ng-bind-html="inspectionPlan.name  | highlightText: freeTextQuery"></span>
                    </td>
                    <td class="col-width-250" title="{{inspectionPlan.description}}">
                        <span ng-bind-html="inspectionPlan.description  | highlightText: freeTextQuery"></span>
                    </td>
                    <td style="width: 100px;text-align: center">
                        <a href="" ng-click="allInspectionPlansVm.showPlanRevisionHistory(inspectionPlan)"
                           title="{{'ITEM_DETAILS_REVISION_HISTORY' | translate}}">
                            {{inspectionPlan.revision}}
                        </a>
                    </td>
                    <td style="width: 100px;">
                        <item-status item="inspectionPlan"></item-status>
                    </td>
                    <td style="width: 1% !important;white-space: nowrap">
                        <i ng-if='inspectionPlan.startWorkflow && !inspectionPlan.finishWorkflow && !inspectionPlan.cancelWorkflow'
                           style='font-size: 18px;color: #ffa800' class='la la-clock-o' title="In Progress"></i>
                        <i ng-if='inspectionPlan.finishWorkflow' style='font-size: 18px;color: #1CAF9A'
                           class='la la-check-circle-o' title="Finished"></i>
                        <i ng-if='inspectionPlan.cancelWorkflow' style='font-size: 18px;color: #f64e60'
                           class='la la-times-circle-o' title="Rejected"></i>
                    </td>
                    <td>
                        <workflow-status-settings workflow="inspectionPlan"></workflow-status-settings>
                        <span class="label label-warning" ng-if="inspectionPlan.onHold">HOLD</span>
                    </td>
                    <td title="{{inspectionPlan.notes}}" class="col-width-250">
                        <span ng-bind-html="inspectionPlan.notes | highlightText: freeTextQuery"></span>
                    </td>
                    <td>{{inspectionPlan.modifiedBy}}</td>
                    <td>{{inspectionPlan.modifiedDate}}</td>
                    <td class="added-column"
                        ng-repeat="objectAttribute in allInspectionPlansVm.selectedAttributes">
                        <all-view-attributes object="inspectionPlan"
                                             object-attribute="objectAttribute"></all-view-attributes>
                    </td>
                    <td class="text-center actions-col sticky-col sticky-actions-col"
                        style="text-align:center; width: 80px;">
                        <span class="row-menu" uib-dropdown dropdown-append-to-body style="">
                         <i class="fa fa-ellipsis-h dropdown-toggle" uib-dropdown-toggle></i>
                            <ul uib-dropdown-menu class="dropdown-menu dropdown-menu-right" role="menu"
                                style="z-index: 9999 !important;">
                                <li>
                                    <a href=""
                                       ng-click="showPrintOptions(inspectionPlan.latestRevision, allInspectionPlanType)"
                                       translate>PREVIEW_AND_PRINT</a>
                                </li>
                                <tags-button object-type="inspectionPlan.objectType" object="inspectionPlan.id"
                                             tags-count="inspectionPlan.tagsCount"></tags-button>
                                <li title="{{inspectionPlan.released || inspectionPlan.statusType == 'REJECTED' ? cannotDeleteApprovedPlan:''}}">
                                    <a href="" ng-click="allInspectionPlansVm.deletePlan(inspectionPlan)"
                                       ng-class="{'disabled':inspectionPlan.released || inspectionPlan.statusType == 'REJECTED'}"
                                       translate>DELETE</a>
                                </li>
                                <plugin-table-actions context="inspectionPlan.all"
                                                      object-value="inspectionPlan"></plugin-table-actions>
                            </ul>
                        </span>
                    </td>
                </tr>
                </tbody>
            </table>
        </div>
        <div class="table-footer">
            <table-footer objects="allInspectionPlansVm.inspectionPlans" pageable="allInspectionPlansVm.pageable"
                          previous-page="allInspectionPlansVm.previousPage"
                          next-page="allInspectionPlansVm.nextPage"
                          page-size="allInspectionPlansVm.pageSize"></table-footer>
        </div>
    </div>
</div>
