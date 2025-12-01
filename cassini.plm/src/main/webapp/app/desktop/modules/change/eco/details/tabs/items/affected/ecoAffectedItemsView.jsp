<style>
    .table .ui-select-choices {
    }

    select {
        display: inline-block !important;
        width: auto !important;
    }

    .strike {
        text-decoration: line-through;
    }

    .red_color {
        /* font-weight: bold !important;*/
        color: red !important;
    }

    .green_color {
        color: #1CAF9A !important;
    }

    .white_color {
        /*font-weight: bold !important;*/
        color: #5c6876 !important;
    }

    .orange_color {
        /* font-weight: bold !important;*/
        color: #FFCE07 !important;
    }

    .dialogue {
        width: 97% !important;
    }

    .legend {
        list-style: none;
        text-align: center;
    }

    .legend li {
        display: inline-block;
        margin-left: 10px;
    }

    .legend span {
        border: 1px solid #ccc;
        float: left;
        width: 12px;
        height: 12px;
        margin: 2px;
    }

    /* your colors */
    .legend .nonChanges {
        margin: 4px !important;
        cursor: pointer !important;
    }

    .legend .deleted {
        background-color: #D43F3A !important;
        margin: 4px !important;
        cursor: pointer !important;
    }

    .legend .added {
        background-color: #1CAF9A !important;
        margin: 4px !important;
        cursor: pointer !important;
    }

    .legend .changes {
        margin: 4px !important;
        background-color: #ec8c04 !important;
        cursor: pointer !important;
    }

    .bomRedLine {
        display: none; /* Hidden by default */
        position: fixed; /* Stay in place */
        padding-top: 20px; /* Location of the box */
        left: 0;
        top: 0;
        width: 100%; /* Full width */
        height: 100%; /* Full height */
        overflow: auto; /* Enable scroll if needed */
        background-color: rgb(0, 0, 0); /* Fallback color */
        background-color: rgba(0, 0, 0, 0.4); /* Black w/ opacity */
    }

    .bomRedLine .compareContentBomRedLine {
        margin: auto;
        display: block;
        height: 94%;
        width: 97%;
        background-color: white;
        border-radius: 7px !important;
    }

    .compareHeadreBomRedLine {
        padding: 5px;
        text-align: center;
        border-bottom: 1px solid lightgrey;
        height: 50px;

    }

    .bomRedLineBody {
        padding: 10px;
        overflow: auto;
        min-width: 100%;
        width: auto;
    }

    .exclusion-close {
        position: absolute;
        right: 35px;
        top: 25px;
        width: 38px;
        height: 38px;
        opacity: 0.3;
    }

    .exclusion-close:hover {
        opacity: 0.6;
        border-radius: 50%;
        background-color: #ddd;
    }

    .exclusion-close:before, .exclusion-close:after {
        position: absolute;
        top: 7px;
        left: 18px;
        content: ' ';
        height: 22px;
        width: 2px;
        background-color: #333;
    }

    .exclusion-close:before {
        transform: rotate(45deg) !important;
    }

    .exclusion-close:after {
        transform: rotate(-45deg) !important;
    }

    .legends .compareFilter {
        margin: -33px 0 0 0 !important;
    }

    .legends .filter-message {
        margin: -33px 0 0 17px !important;
    }

</style>


<div class="modal fade bomRedLine" id="myModal1" data-backdrop="static"
     data-keyboard="true">

    <div class="compareContentBomRedLine">
        <div class="compareHeadreBomRedLine">
            <div class="text-secondary" style=" text-align: center;
        font-weight: bold !important;">
                    <span style="text-align:center !important;"><h4>
                        {{ecoAffectedItemsVm.compareTitle}}</h4></span>

            </div>
            <a href="" data-dismiss="modal" class="exclusion-close pull-right"
               style="display: inline-block;"></a>
        </div>

        <div class="modal-body bomRedLineBody">
            <%--Legends--%>


            <div class="legends">
                <ul class="legend">
                    <li ng-click="ecoAffectedItemsVm.showAddedItems()"><span class="added"></span> Item added</li>
                    <li ng-click="ecoAffectedItemsVm.showChangedItems()"><span class="changes"></span> Item has changes
                    </li>
                    <li ng-click="ecoAffectedItemsVm.showDeletedItems()"><span class="deleted"></span> Item does not
                        exist
                    </li>
                    <li ng-click="ecoAffectedItemsVm.showNoChangeItems()"><span class="nonChanges"></span>Item has no
                        changes
                    </li>
                </ul>

                <div class="pull-left filter-message" ng-if="ecoAffectedItemsVm.bomCompareFilter==true">
                    <span>
                        {{ecoAffectedItemsVm.filterMessage}}
                    </span>
                </div>

                <div class="pull-right compareFilter" ng-if="ecoAffectedItemsVm.bomCompareFilter==true">
                    <button class="btn btn-xs btn-warning"
                            ng-click="ecoAffectedItemsVm.clearBomCompareFilter()"
                            translate>
                        BOM_COMPARE_CLEAR_FILTER
                    </button>
                </div>
            </div>


            <div style="height: 33em !important;">
                <div class="row">
                    <div class="" style="padding: 10px;">
                        <table class="table" style="width: 100% !important;"
                               id="specifications1">

                            <thead>
                            <tr class="noBorder">
                                <th style="width: 1% !important;white-space: nowrap;" translate>ITEM_NUMBER</th>
                                <th class="" translate style="z-index: auto !important;">ITEM_TYPE</th>
                                <th class="col-width-250" translate style="z-index: auto !important;">ITEM_NAME</th>
                                <th class="col-width-250" translate>DESCRIPTION</th>
                                <th class="" translate style="z-index: auto !important;text-align: center">REVISION</th>
                                <th class="" translate style="z-index: auto !important;">LIFE_CYCLE_PHASE</th>
                                <th class="text-center" translate style="z-index: auto !important;">QUANTITY</th>
                                <th style="text-align: center" translate>UNITS</th>
                                <th class="col-width-250-compare" style="z-index: auto !important;" translate>REF_DES
                                </th>
                            </tr>
                            </thead>
                            <tbody>
                            <tr ng-if="itemList.length == 0">
                                <td colspan="100"><span translate>NO_ITEMS</span></td>
                            </tr>
                            <tr ng-repeat="value in itemList" ng-init="color = value.color">
                                <td style="width: 1% !important;white-space: nowrap;">
                                    <span class="{{color}} level{{value.level}}">{{value.item.itemNumber}}</span>
                                </td>

                                <td>
                                    <span class="{{color}}">{{value.item.itemType.name}}</span>
                                </td>
                                <td>
                                    <span class="{{color}}">{{value.item.itemName}}</span>
                                </td>
                                <td>
                                    <span class="{{color}}">{{value.item.description}}</span>
                                </td>
                                <td style="text-align: center;">
                                    <span class="strike {{color}}"
                                          ng-if="value.updatedRevision !=null">{{value.rev}}</span>&nbsp;
                                        <span class="{{color}}" ng-if="value.updatedRevision != null">
                                            {{value.updatedRevision}}
                                        </span>
                                         <span class="{{color}}" ng-if="value.updatedRevision == null">
                                            {{value.rev}}
                                        </span>
                                </td>
                                <td>
                                    <span class="strike {{color}}" ng-if="value.updatedLifeCycle !=null">{{value.lifeCycle}}</span>
                                    &nbsp;
                                        <span class="{{color}}" ng-if="value.updatedLifeCycle != null">
                                            {{value.updatedLifeCycle}}
                                        </span>
                                         <span class="{{color}}" ng-if="value.updatedLifeCycle == null">
                                            {{value.lifeCycle}}
                                        </span>
                                </td>
                                <td style="text-align: center !important;">
                                        <span class="strike {{color}}" ng-if="value.updatedQty != null">
                                            {{value.quantity}}
                                        </span>
                                        <span class="{{color}}" ng-if="value.updatedQty == null">
                                            {{value.quantity}}
                                        </span> &nbsp;
                                        <span class="{{color}}"
                                              ng-if="value.updatedQty !=null">{{value.updatedQty}}</span>
                                </td>
                                <td style="text-align: center !important;">
                                            <span class="{{color}}">
                                              {{value.item.units}}
                                            </span>

                                </td>

                                <td class="col-width-250-compare">
                                        <span class="strike {{color}}"
                                              ng-if="value.updatedDefdes != null && value.refdes !=null">
                                            {{value.refdes}}
                                        </span>
                                    <span class="{{color}}"
                                          ng-if="value.updatedDefdes !=null">{{value.updatedDefdes}}</span>
                                        <span class="{{color}}"
                                              ng-if="value.updatedDefdes ==null && value.refdes !=null">{{value.refdes}}</span>
                                </td>
                            </tr>

                            </tbody>

                        </table>
                    </div>


                </div>
            </div>
        </div>

    </div>
</div>

<div class='responsive-table'>
    <table class='table table-striped highlight-row' style="margin-bottom: 60px">
        <thead>
        <tr>
            <th style="width: 20px;" ng-if="!eco.released && !eco.cancelled">
                <i class="la la-plus dropdown-toggle" ng-click="ecoVm.showMultiple()"
                   title="{{ecoAffectedItemsVm.addItems}}" style="cursor: pointer"
                   ng-if="(hasPermission('change','eco','edit') || hasPermission('change','edit')) && !eco.startWorkflow"></i>
                <%--<span uib-dropdown dropdown-append-to-body
                      ng-if="hasPermission('change','eco','edit') && ecoAffectedItemsVm.copiedItems.length > 0">
                        <i class="la la-plus dropdown-toggle" uib-dropdown-toggle
                           title="{{ecoAffectedItemsVm.addItems}}"
                           style="cursor: pointer"></i>
                        <ul uib-dropdown-menu class="dropdown-menu" role="menu" style="z-index: 9999 !important;">
                            <li><a href="" ng-click="ecoAffectedItemsVm.addMultipleItems()" translate>SELECT_ITEMS</a>
                            </li>
                            <li>
                                <a href
                                   ng-click="ecoAffectedItemsVm.pasteItemsFromClipboard()" translate>PASTE_FROM_CLIPBOARD</a>
                            </li>
                        </ul>
                </span>--%>
            </th>
            <th style="width: 10px" translate></th>
            <th style="width: 1% !important;white-space: nowrap;" translate>ITEM_NUMBER</th>
            <th style="width: 150px" translate>ITEM_TYPE</th>
            <th class="col-width-250" translate>ITEM_NAME</th>
            <th translate>ECR</th>
            <th style="width: 150px;text-align: center" translate>FROM_REVISION</th>
            <%--<th style="width: 150px" translate>FROM_LIFECYCLE</th>--%>
            <th style="width: 150px;text-align: center" translate>TO_REVISION</th>
            <%--<th style="width: 150px" translate>TO_LIFECYCLE</th>--%>
            <th style="width: 150px" translate>EFFECTIVE_DATE</th>
            <th class="description-column" translate>NOTES</th>
            <th class="actions-col sticky-col sticky-actions-col" style="text-align:center; width: 80px;">
                <span translate>ACTIONS</span>
                <i class="fa fa-check-circle" ng-click="ecoAffectedItemsVm.saveAll()"
                   ng-if="ecoAffectedItemsVm.addedAffectedItemsToEco.length > 1"
                   title="Save"
                   style="font-size: 16px;cursor: pointer;padding: 5px;"></i>
                <i class="fa fa-minus-circle" style="font-size: 16px;cursor: pointer;"
                   ng-click="ecoAffectedItemsVm.removeAddedItems()" title="Remove"
                   ng-if="ecoAffectedItemsVm.addedAffectedItemsToEco.length > 1"></i>
            </th>
        </tr>
        </thead>
        <tbody>
        <tr ng-if="ecoAffectedItemsVm.loading == true">
            <td colspan="15">
                    <span style="font-size: 15px;">
                        <img src="app/assets/bower_components/cassini-platform/images/loaders/loader19.gif" class="mr5">
                        <span translate>LOADING_ITEMS</span>
                    </span>
            </td>
        </tr>
        <tr ng-if="ecoAffectedItemsVm.loading == false && ecoAffectedItemsVm.items.length == 0">
            <td colspan="25" style="background-color: #f9fbfe  !important;color: unset !important;">
                <div class="no-data">
                    <img src="app/assets/no_data_images/affectedItems.png" alt="" class="image">

                    <div class="message">{{ 'NO_ITEMS' | translate}}</div>
                    <div class="message ng-scope" style="font-size: 14px;color: #8f979d;" translate>
                        NO_PERMISSION_MESSAGE
                    </div>
                </div>
            </td>
        </tr>
        <tr ng-if="ecoAffectedItemsVm.loading == false" ng-repeat="item in ecoAffectedItemsVm.items track by $index">
            <td ng-if="!eco.released && !eco.cancelled"></td>
            <td><span ng-if="item.bomAvailable == true && item.toItem != null && item.toItem != ''"
                      style="font-size: 17px !important;cursor:pointer !important;"
                      ng-click="ecoAffectedItemsVm.compareBom(item,item)"
                      title="{{redLineTitle}}"><i class="fa fa-exchange" aria-hidden="true"></i></span>
            </td>
            <td style="width: 1% !important;white-space: nowrap;">
                <a ng-if="item.isNew == false && hasPermission('item','view')" href=""
                   ng-click="ecoAffectedItemsVm.showItem(item)" title="{{clickToShowDetails}}">
                    {{item.itemNumber}}
                </a>
                <span ng-if="!hasPermission('item','view') && item.isNew == false">{{item.itemNumber}}</span>
                <span ng-if="item.isNew == true && item.editItemNumber == false">{{item.itemNumber}}</span>
            </td>
            <td style="width: 150px">{{item.itemType.name}}</td>
            <td class="col-width-250">{{item.itemName}}</td>
            <td>
                <span ng-repeat="ecr in item.ecrList">
                    <a href="" ui-sref="app.changes.ecr.details({ecrId: ecr.id})"
                       title="{{clickToShowDetails}}" ng-click="ecoAffectedItemsVm.showEcr()">{{ecr.crNumber}}<span
                            ng-if="($index + 1) < item.ecrList.length"> , </span></a>
                </span>
            </td>
            <td style="width: 150px; text-align: center">
                <a href="" ng-click="ecoAffectedItemsVm.showFromRevision(item)" title="{{clickToShowDetails}}">{{item.fromRevision}}</a>
            </td>
            <%--<td style="width: 150px">
                <affected-status item="item.fromLifeCycle"></affected-status>
            </td>--%>
            <td style="width: 150px;text-align: center;">
                <span ng-if="(item.toItem == null || item.toItem == '') || item.editMode">{{item.toRevision}}</span>
                <a href="" ng-if="item.toItem != null && item.toItem != '' && !item.editMode"
                   ng-click="ecoAffectedItemsVm.showToRevision(item)"
                   title="{{clickToShowDetails}}">{{item.toRevision}}</a>
            </td>
            <%--<td>
                <span ng-if="item.isNew == true || (item.editMode == true && !eco.startWorkflow)">
                    <select class="form-control input-sm" ng-model="item.newToLifeCycle"
                            ng-options="lifeCyclePhase.phase for lifeCyclePhase in item.toLifecyclePhases track by lifeCyclePhase.phase">
                        <div ng-bind="lifeCyclePhase.phase"></div>
                    </select>
                </span>
                <affected-status ng-if="item.isNew == false && item.editMode == true && eco.startWorkflow"
                                 item="item.newToLifeCycle"></affected-status>
                <affected-status ng-if="item.isNew == false && item.editMode == false"
                                 item="item.newToLifeCycle"></affected-status>
            </td>--%>
            <td>
                <span ng-if="item.isNew == true || item.editMode == true">
                    <input style="margin: 0;width: 125px;" type="text" class="form-control input-sm"
                           placeholder="{{effectiveDatePlaceholder}}" start-date-picker
                           ng-model="item.newEffectiveDate" ng-enter="ecoAffectedItemsVm.onOk(item)"/>
                    <i class="fa fa-times" ng-if="item.newEffectiveDate != null"
                       style="float: right;margin-top: -25px;margin-right: 10px;"
                       ng-click="item.newEffectiveDate = null"></i>
                </span>
                <span ng-if="item.isNew == false && item.editMode == false">{{item.effectiveDate}}</span>
            </td>
            <td class="description-column">
                <form>
                    <span ng-if="item.isNew == true || item.editMode == true">
                        <input autocomplete="off" id="notes" style="margin: 0" type="text" class="form-control input-sm"
                               name="notes"
                               ng-model="item.newNotes" ng-enter="ecoAffectedItemsVm.onOk(item)">
                    </span>
                    <span ng-if="item.isNew == false && item.editMode == false">
                        {{item.newNotes}}
                    </span>
                </form>
            </td>
            <td class="text-center actions-col sticky-col sticky-actions-col">
                 <span class="btn-group" ng-if="item.isNew == true && item.editMode == true && item.flag == true"
                       style="margin: 0">
                    <i title="{{ 'SAVE' | translate }}"
                       ng-click="ecoAffectedItemsVm.save(item)"
                       class="la la-check">
                    </i>
                    <i title="{{cancelChangesTitle}}"
                       ng-click="ecoAffectedItemsVm.onCancel(item)"
                       class="la la-times">
                    </i>
                </span>
                <span class="btn-group" ng-if="item.isNew != true && item.editMode == true && item.flag != true"
                      style="margin: 0">
                    <i title="{{ 'SAVE' | translate }}"
                       ng-click="ecoAffectedItemsVm.onOk(item)"
                       class="la la-check">
                    </i>
                    <i title="{{cancelChangesTitle}}"
                       ng-click="ecoAffectedItemsVm.onCancel(item)"
                       class="la la-times">
                    </i>
                </span>
                <span class="row-menu" uib-dropdown dropdown-append-to-body
                      ng-if="item.isNew != true && item.editMode != true"
                      ng-hide="ecoAffectedItemsVm.ecoStatus || eco.startWorkflow"
                      style="min-width: 50px">
                    <i class="fa fa-ellipsis-h dropdown-toggle" uib-dropdown-toggle></i>
                    <ul uib-dropdown-menu class="dropdown-menu dropdown-menu-right" role="menu"
                        style="z-index: 9999 !important;">
                        <li ng-if="item.editMode == false  && (hasPermission('change','eco','edit') || hasPermission('change','edit'))"
                            ng-click="ecoAffectedItemsVm.editItem(item)"
                            ng-class="{'disabled': ecoAffectedItemsVm.ecoStatus == true}">
                            <a translate>EDIT_ITEM</a>
                        </li>
                        <li title="{{ecoAffectedItemsVm.ecoStatus == true || item.ecrList.length > 0 || ecoAffectedItemsVm.eco.revisionsCreated ? getTooltip(item):''}}">
                            <a ng-if="item.isNew == false && item.editMode == false  && (hasPermission('change','eco','delete') || hasPermission('change','delete'))"
                               ng-click="ecoAffectedItemsVm.deleteItem(item)"
                               ng-class="{'disabled': ecoAffectedItemsVm.ecoStatus == true || item.ecrList.length > 0 || ecoAffectedItemsVm.eco.revisionsCreated}"
                               translate>REMOVE_ITEM</a>
                        </li>

                        <li ng-if="item.isNew == true && item.editMode == true && item.flag == true"
                            ng-click="ecoAffectedItemsVm.save(item)">
                            <a translate>SAVE</a>
                        </li>

                        <li ng-if="item.flag == false"
                            ng-click="ecoAffectedItemsVm.onOk(item)"><a translate>SAVE</a></li>


                        <li ng-if="item.isNew == true || item.editMode == true && item.flag == false" type="button"
                            ng-click="ecoAffectedItemsVm.onCancel(item)">
                            <a translate>CANCEL</a>
                        </li>
                        <plugin-table-actions context="eco.affected" object-value="item"></plugin-table-actions>
                    </ul>
                </span>


            </td>
        </tr>
        </tbody>
    </table>
</div>