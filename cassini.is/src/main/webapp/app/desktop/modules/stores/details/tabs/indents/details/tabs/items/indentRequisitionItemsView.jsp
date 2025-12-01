<style scoped>
    i.disabled {
        cursor: not-allowed;
        color: lightgrey;
    }
</style>
<div class="view-container" fitcontent>
    <div class="view-content no-padding" style="overflow-y: hidden">
        <table class='table table-striped highlight-row'>
            <thead>
            <tr>
                <th>Item Number</th>
                <th>Item Name</th>
                <th>Requested Qty</th>
                <th>Notes</th>
                <th>Indent Qty</th>
                <th style="text-align: center;">Actions</th>
            </tr>
            </thead>
            <tbody>
            <tr ng-if="indentRequisitionItemsVm.loading == true">
                <td colspan="10">
                    <span style="font-size: 15px;">
                        <img src="app/assets/bower_components/cassini-platform/images/loaders/loader19.gif" class="mr5">Loading Items..
                    </span>
                </td>
            </tr>

            <tr ng-if="indentRequisitionItemsVm.groupedItems.length == 0">
                <td colspan="10">No Items are available to view</td>
            </tr>
            <tr ng-repeat="indentItem in indentRequisitionItemsVm.groupedItems">
                <td ng-if="indentItem.rowType == 'req'" colspan="12">
                    <h5 style="margin: 0;font-weight: bold;">{{indentItem.materialItem.itemNumber}}</h5>
                </td>
                <td ng-if="indentItem.rowType == 'item'">
                    <span><%--<a href="" ng-click="indentRequisitionItemsVm.showItemDetails(indentItem)" title="Click to show Details">--%>{{indentItem.materialItem.itemNumber}}<%--</a>--%></span>
                </td>
                <td ng-if="indentItem.rowType == 'item'">
                    <span>{{indentItem.materialItem.itemName}}</span>
                </td>
                <td ng-if="indentItem.rowType == 'item'">
                    <span>{{indentItem.reqItemQuantity}}</span>
                </td>

                <td ng-if="indentItem.rowType == 'item'">
                    <input ng-if="indentItem.editMode || (indentItem.editMode && !indentItem.isNew)"
                           ng-model="indentItem.indentItemNotesObject" type="text"
                           class="form-control input-sm">

                    <span title="{{indentItem.indentItemNotesObject}}"
                          ng-if="!indentItem.editMode && !indentItem.isNew">{{indentItem.indentItemNotesObject | limitTo: 12}}{{indentItem.indentItemNotesObject.length > 12 ? '...' : ''}}</span>
                </td>

                <td ng-if="indentItem.rowType == 'item'" style="text-align: center;width:100px;">
                    <input ng-if="indentItem.editMode || (indentItem.editMode && !indentItem.isNew)"
                           type="number" min="1"
                           class="form-control input-sm"
                           ng-model="indentItem.indentItemQuantity"
                           ng-change="indentRequisitionItemsVm.onChangeIndentQty(indentItem)"
                           style="text-align: center;">
                    <span ng-show="!indentItem.editMode && !indentItem.isNew">{{indentItem.indentItemQuantity}}</span>
                </td>
                <td class="text-center" style="vertical-align: middle;" ng-if="(!indentItem.showEditButton) == false">
                                    <span class="row-menu" uib-dropdown dropdown-append-to-body style="min-width: 50px"
                                          ng-if="indent.status != 'APPROVED'">
                                        <i class="fa fa-ellipsis-h dropdown-toggle" uib-dropdown-toggle></i>
                                    <ul uib-dropdown-menu class="dropdown-menu dropdown-menu-right"
                                        role="menu" style="z-index: 9999 !important;">
                                        <li ng-click="!hasPermission('permission.indents.editIndentItems') || indentRequisitionItemsVm.editIndentItem(indentItem)"
                                            ng-class="{'disabled': !hasPermission('permission.indents.editIndentItems')}">
                                            <a class="dropdown-item" type="button">
                                                <span style="padding-left: 3px;">Edit</span>
                                            </a>
                                        </li>
                                        <li ng-click="!hasPermission('permission.indents.removeIndentItems') || indentRequisitionItemsVm.deleteIndentItem(indentItem)"
                                            ng-class="{'disabled': !hasPermission('permission.indents.removeIndentItems')}">
                                            <a class="dropdown-item" type="button">
                                                <span style="padding-left: 3px;">Delete</span>
                                            </a>
                                        </li>
                                    </ul>
                                        </span>
                </td>
                <td class="text-center" style="vertical-align: middle">
                                <span class="row-menu" uib-dropdown dropdown-append-to-body style="min-width: 50px"
                                      ng-if="indentItem.rowType == 'item' && (!indentItem.showEditButton && !indentItem.isNew)">
                                        <i class="fa fa-ellipsis-h dropdown-toggle" uib-dropdown-toggle></i>
                                    <ul uib-dropdown-menu class="dropdown-menu dropdown-menu-right"
                                        role="menu" style="z-index: 9999 !important;">
                                        <li ng-click="indentRequisitionItemsVm.applyChanges(indentItem)">
                                            <a class="dropdown-item" type="button">
                                                <span style="padding-left: 3px;">Apply Changes</span>
                                            </a>
                                        </li>
                                        <li ng-click="indentRequisitionItemsVm.cancelChanges(indentItem)">
                                            <a class="dropdown-item" type="button">
                                                <span style="padding-left: 3px;">Cancel Changes</span>
                                            </a>
                                        </li>
                                    </ul>
                                        </span>
                    <i title="Remove" ng-if="indentItem.isNew"
                       class="fa fa-minus-circle"
                       style="font-size: 20px;"
                       ng-click="indentRequisitionItemsVm.removeFromIndentItems(indentItem)"
                       aria-hidden="true"></i>
                </td>

            </tr>
            </tbody>
        </table>
    </div>
</div>


