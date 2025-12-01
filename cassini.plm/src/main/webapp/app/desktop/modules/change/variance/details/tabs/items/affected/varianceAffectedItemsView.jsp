<style>
    .table .ui-select-choices {
    }

    select {
        display: inline-block !important;
        width: auto !important;
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

</style>

<div class='responsive-table'>
    <table class='table table-striped highlight-row'>
        <thead>
        <tr>
            <th style="width: 20px;" ng-if="varianceAffectedItemsVm.varianceStatus == false">
                <i class="la la-plus dropdown-toggle" ng-click="varianceDetailsVm.showMultiple()"
                   title="{{varianceAffectedItemsVm.addItems}}" style="cursor: pointer"
                   ng-if="hasPermission('change','variance','edit') && varianceAffectedItemsVm.copiedItems.length == 0"></i>
                <span uib-dropdown dropdown-append-to-body
                      ng-if="hasPermission('change','variance','edit') && varianceAffectedItemsVm.copiedItems.length > 0">
                        <i class="la la-plus dropdown-toggle" uib-dropdown-toggle
                           title="{{varianceAffectedItemsVm.addItems}}"
                           style="cursor: pointer"></i>
                        <ul uib-dropdown-menu class="dropdown-menu" role="menu" style="z-index: 9999 !important;">
                            <li><a href="" ng-show="varianceAffectedItemsVm.variance.varianceFor == 'ITEMS'"
                                   ng-click="varianceAffectedItemsVm.addMultipleItems()"
                                   translate>SELECT_ITEMS</a>
                            </li>
                        </ul>
                </span>
            </th>
            <th style="width: 20px;" ng-if="varianceAffectedItemsVm.varianceStatus == true"></th>
            <th style="width: 1% !important;white-space: nowrap;"
                ng-if="varianceAffectedItemsVm.variance.varianceFor == 'ITEMS'" translate>ITEM_NUMBER
            </th>
            <th class="" ng-if="varianceAffectedItemsVm.variance.varianceFor == 'MATERIALS'" translate>PART_NUMBER</th>
            <th style="width: 150px" ng-if="varianceAffectedItemsVm.variance.varianceFor == 'ITEMS'" translate>
                ITEM_TYPE
            </th>
            <th style="width: 150px" ng-if="varianceAffectedItemsVm.variance.varianceFor == 'MATERIALS'" translate>
                PART_TYPE
            </th>
            <th class="col-width-250" ng-if="varianceAffectedItemsVm.variance.varianceFor == 'ITEMS'" translate>
                ITEM_NAME
            </th>
            <th class="col-width-250" ng-if="varianceAffectedItemsVm.variance.varianceFor == 'MATERIALS'" translate>
                PART_NAME
            </th>
            <th style="width: 50px" translate>QUANTITY</th>
            <th class="description-column" translate>SERIALS_OR_LOTS</th>
            <th class="description-column" translate>NOTES</th>
            <th class="text-center" style="width: 70px;">
                <span translate>ACTIONS</span>
                <i class="fa fa-check-circle" style="font-size: 16px;cursor: pointer;padding: 5px;"
                   ng-if="varianceAffectedItemsVm.newItems"
                   ng-click="varianceAffectedItemsVm.saveAllItems()"
                   title="{{varianceAffectedItemsVm.saveAffectedItems}}"></i>
                <i class="fa fa-minus-circle" style="font-size: 16px;cursor: pointer;"
                   ng-if="varianceAffectedItemsVm.newItems"
                   ng-click="varianceAffectedItemsVm.removeAllItems()"
                   title="{{varianceAffectedItemsVm.removeAffedItems}}"></i>
            </th>
        </tr>
        </thead>
        <tbody>
        <tr ng-if="varianceAffectedItemsVm.loading == true">
            <td colspan="10">
                    <span style="font-size: 15px;">
                        <img src="app/assets/bower_components/cassini-platform/images/loaders/loader19.gif" class="mr5">
                        <span translate>LOADING_ITEMS</span>
                    </span>
            </td>
        </tr>
        <tr ng-if="varianceAffectedItemsVm.loading == false && varianceAffectedItemsVm.items.length == 0">
            <td colspan="25" style="background-color: #f9fbfe  !important;color: unset !important;">
                <div class="no-data">
                    <img src="app/assets/no_data_images/affectedItems.png" alt="" class="image">

                    <div class="message">{{ 'NO_ITEMS' | translate}} {{itemsTitle}}</div>
                    <div class="message ng-scope" style="font-size: 14px;color: #8f979d;" translate>
                        NO_PERMISSION_MESSAGE
                    </div>
                </div>
            </td>
        </tr>
        <tr ng-repeat="item in varianceAffectedItemsVm.items track by $index">
            <td style="width: 30px;">
                        <span ng-if="item.recurring == true">
                        <i class="fa fa-repeat" title="{{varianceAffectedItemsVm.recurringItem}}"
                           aria-hidden="true"></i>
                        </span>
            </td>
            <td style="width: 1% !important;white-space: nowrap;">
                <a ng-if="hasPermission('item','view')" href=""
                   ng-click="varianceAffectedItemsVm.showItem(item)" title="{{'CLICK_TO_SHOW_DETAILS' | translate}}">
                    {{item.itemNumber}}
                </a>
            </td>
            <td style="width: 150px">{{item.itemType}}</td>
            <td class="col-width-250">{{item.itemName}}</td>
            <td style="width: 50px" ng-if="varianceAffectedItemsVm.variance.startWorkflow == false">
                    <span ng-if="item.isNew == true || item.editMode == true">
                        <input style="margin: 0" type="number" name="quantity"
                               ng-model="item.quantity" ng-enter="varianceAffectedItemsVm.onOk(item)">
                    </span>
                <span ng-if="item.isNew == false && item.editMode == false">{{item.quantity}}</span>
            </td>
            <td class="description-column" ng-if="varianceAffectedItemsVm.variance.startWorkflow == true">
                <span>{{item.quantity}}</span>
            </td>
            <td class="description-column" ng-if="varianceAffectedItemsVm.variance.startWorkflow == false">
                    <span ng-if="item.isNew == true || item.editMode == true">
                        <textarea style="margin: 0;resize: none;" cols="3" class="form-control input-sm"
                                  name="serialsOrLots"
                                  ng-model="item.serialsOrLots"
                                  ng-enter="varianceAffectedItemsVm.onOk(item)"></textarea>
                    </span>
                <span ng-if="item.isNew == false && item.editMode == false">{{item.serialsOrLots}}</span>
            </td>
            <td class="description-column" ng-if="varianceAffectedItemsVm.variance.startWorkflow == true">
                <span>{{item.serialsOrLots}}</span>
            </td>
            <td class="description-column">
                <form>
                    <span ng-if="item.isNew == true || item.editMode == true">
                        <input autocomplete="off" id="notes" style="margin: 0" type="text" class="form-control input-sm"
                               name="notes"
                               ng-model="item.notes" ng-enter="varianceAffectedItemsVm.onOk(item)">
                    </span>
                    <span ng-if="item.isNew == false && item.editMode == false">{{item.notes}}</span>
                </form>
            </td>
            <td class="text-center">
                 <span class="btn-group" class="btn-group"
                       ng-if="item.isNew == true && item.editMode == true"
                       style="margin: 0">
                    <i title="{{ 'SAVE' | translate}}"
                       ng-click="varianceAffectedItemsVm.save(item)"
                       class="la la-check">
                    </i>
                    <i title="{{cancelChangesTitle}}"
                       ng-click="varianceAffectedItemsVm.onCancel(item)"
                       class="la la-times">
                    </i>
                </span>
                <span class="btn-group" class="btn-group"
                      ng-if="item.isNew != true && item.editMode == true"
                      style="margin: 0">
                    <i title="{{ 'SAVE' | translate}}"
                       ng-click="varianceAffectedItemsVm.onOk(item)"
                       class="la la-check">
                    </i>
                    <i title="{{cancelChangesTitle}}"
                       ng-click="varianceAffectedItemsVm.onCancel(item)"
                       class="la la-times">
                    </i>
                </span>
                <span class="row-menu" uib-dropdown dropdown-append-to-body
                      ng-if="item.isNew != true && item.editMode != true"
                      style="min-width: 50px">
                    <i class="fa fa-ellipsis-h dropdown-toggle" uib-dropdown-toggle></i>
                    <ul uib-dropdown-menu class="dropdown-menu dropdown-menu-right" role="menu"
                        style="z-index: 9999 !important;">
                        <li ng-if="item.editMode == false  && hasPermission('change','variance','edit')"
                            ng-click="varianceAffectedItemsVm.editItem(item)"
                            ng-class="{'disabled': varianceAffectedItemsVm.varianceStatus == true}">
                            <a translate>EDIT_ITEM</a>
                        </li>
                        <li ng-if="item.isNew == false && item.editMode == false  && hasPermission('change','variance','delete')"
                            ng-click="varianceAffectedItemsVm.deleteItem(item)"
                            ng-class="{'disabled': varianceAffectedItemsVm.varianceStatus == true}">
                            <a translate>REMOVE_ITEM</a>
                        </li>

                        <li ng-if="item.isNew == true && item.editMode == true && item.flag == true"
                            ng-click="varianceAffectedItemsVm.save(item)">
                            <a translate>SAVE</a>
                        </li>

                        <li ng-if="item.flag == false"
                            ng-click="varianceAffectedItemsVm.onOk(item)"><a translate>SAVE</a></li>


                        <li ng-if="item.isNew == true || item.editMode == true && item.flag == false" type="button"
                            ng-click="varianceAffectedItemsVm.onCancel(item)">
                            <a translate>CANCEL</a>
                        </li>
                        <plugin-table-actions context="variance.affected" object-value="item"></plugin-table-actions>
                    </ul>
                </span>


            </td>
        </tr>
        </tbody>
    </table>
</div>