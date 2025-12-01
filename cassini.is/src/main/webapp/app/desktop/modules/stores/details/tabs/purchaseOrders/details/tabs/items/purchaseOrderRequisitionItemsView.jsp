<div class="view-container" fitcontent>
    <div class="view-content no-padding" style="overflow-y: hidden">
        <table class='table table-striped highlight-row'>
            <thead>
            <tr>
                <th>Item Number</th>
                <th>Item Name</th>
                <th>Requested Qty</th>
                <th>Notes</th>
                <th>Purchase Qty</th>
                <th ng-hide="purchaseOrderRequisitionItemsVm.mode == 'approved'" style="text-align: center;">Actions
                </th>
            </tr>
            </thead>
            <tbody>
            <tr ng-if="purchaseOrderRequisitionItemsVm.loading == true" class="ng-scope">
                <td colspan="10">
                                                     <span style="font-size: 15px;">
                                                         <img src="app/assets/bower_components/cassini-platform/images/loaders/loader19.gif"
                                                              class="mr5">Loading Items..
                                                     </span>
                </td>
            </tr>

            <tr ng-if="purchaseOrderRequisitionItemsVm.loading == false && purchaseOrderRequisitionItemsVm.groupedItemsPurchaseOrder.length == 0">
                <td colspan="10">No Items are available to view</td>
            </tr>
            <tr ng-repeat="purchaseItem in purchaseOrderRequisitionItemsVm.groupedItemsPurchaseOrder">
                <td ng-if="purchaseItem.rowType == 'req'" colspan="6">
                    <h5 style="margin: 0;font-weight: bold;">{{purchaseItem.materialItem.itemNumber}}</h5>
                </td>
                <td ng-if="purchaseItem.rowType == 'item'">
                    <%-- <a ng-click="purchaseOrderRequisitionItemsVm.openPurchaseItemDetails(purchaseItem)"
                        title="Click to open details">--%>
                    <span><%--<a href="" ng-click="purchaseOrderRequisitionItemsVm.openPurchaseItemDetails(purchaseItem)" title="Click to show Details">--%>{{purchaseItem.materialItem.itemNumber}}<%--</a>--%></span>
                    <%--  </a>--%>
                </td>
                <td ng-if="purchaseItem.rowType == 'item'">
                    <span>{{purchaseItem.materialItem.itemName}}</span>
                </td>
                <td ng-if="purchaseItem.rowType == 'item'">
                    <span>{{purchaseItem.reqItemQuantity}}</span>
                </td>

                <td ng-if="purchaseItem.rowType == 'item'">

                    <input ng-if="purchaseItem.editMode || (purchaseItem.editMode && !purchaseItem.isNew)"
                           ng-model="purchaseItem.purchaseItemNotesObject" type="text"
                           class="form-control input-sm">

                    <span title="{{purchaseItem.purchaseItemNotesObject}}"
                          ng-if="!purchaseItem.editMode && !purchaseItem.isNew">
                         {{purchaseItem.purchaseItemNotesObject | limitTo: 12}}{{purchaseItem.purchaseItemNotesObject.length > 12 ? '...' : ''}}</span>
                </td>

                <td ng-if="purchaseItem.rowType == 'item'" style="text-align: center;width:100px;">
                    <input ng-if="purchaseItem.editMode || (purchaseItem.editMode && !purchaseItem.isNew)" type="number"
                           min="1"
                           class="form-control input-sm"
                           ng-model="purchaseItem.purchaseItemQuantity"
                           ng-change="purchaseOrderRequisitionItemsVm.onChangePurchaseQty(purchaseItem)"
                           style="text-align: center;">
                    <span ng-show="!purchaseItem.editMode && !purchaseItem.isNew">{{purchaseItem.purchaseItemQuantity}}</span>
                </td>


                <td class="text-center" ng-if="(!purchaseItem.showEditButton) == false"
                    ng-if="purchaseItem.rowType == 'item'" ng-hide="purchaseOrderRequisitionItemsVm.mode == 'approved'"
                    style="text-align: center;">
                                    <span class="row-menu" uib-dropdown dropdown-append-to-body style="min-width: 50px"
                                          ng-if="purchaseOrder.status != 'APPROVED'">
                                        <i class="fa fa-ellipsis-h dropdown-toggle" uib-dropdown-toggle></i>
                                    <ul uib-dropdown-menu class="dropdown-menu dropdown-menu-right"
                                        role="menu" style="z-index: 9999 !important;">
                                        <li ng-click="!hasPermission('permission.purchaseOrders.editPurchaseOrderItem') || purchaseOrderRequisitionItemsVm.editPurchaseItem(purchaseItem)"
                                            ng-class="{'disabled': !purchaseItem.showEditButton && purchaseItem.isNew}"
                                        <%--ng-disabled="hasPermission('permission.purchaseOrders.editPurchaseOrderItemQty')"--%>>
                                            <a class="dropdown-item" type="button">
                                                <span style="padding-left: 3px;">Edit</span>
                                            </a>
                                        </li>
                                        <li ng-click="!hasPermission('permission.purchaseOrders.removePurchaseOrderItems') || purchaseOrderRequisitionItemsVm.deletePurchaseItem(purchaseItem)"
                                            ng-if="purchaseItem.showEditButton && !purchaseItem.isNew"
                                        <%-- ng-disabled="hasPermission('permission.purchaseOrders.removePurchaseOrderItems')"--%>>
                                            <a class="dropdown-item" type="button">
                                                <span style="padding-left: 3px;">Delete</span>
                                            </a>
                                        </li>
                                    </ul>
                                        </span>
                </td>
                <td class="text-center" style="vertical-align: middle">
                                <span class="row-menu" uib-dropdown dropdown-append-to-body style="min-width: 50px"
                                      ng-if="purchaseItem.rowType == 'item' && !purchaseItem.showEditButton && !purchaseItem.isNew">
                                        <i class="fa fa-ellipsis-h dropdown-toggle" uib-dropdown-toggle></i>
                                    <ul uib-dropdown-menu class="dropdown-menu dropdown-menu-right"
                                        role="menu" style="z-index: 9999 !important;">
                                        <li ng-click="purchaseOrderRequisitionItemsVm.applyChanges(purchaseItem)">
                                            <a class="dropdown-item" type="button">
                                                <span style="padding-left: 3px;">Apply Changes</span>
                                            </a>
                                        </li>
                                        <li ng-click="purchaseOrderRequisitionItemsVm.cancelChanges(purchaseItem)">
                                            <a class="dropdown-item" type="button">
                                                <span style="padding-left: 3px;">Cancel Changes</span>
                                            </a>
                                        </li>
                                    </ul>
                                        </span>
                    <i title="Remove" ng-if="purchaseItem.isNew"
                       class="fa fa-minus-circle"
                       style="font-size: 20px;"
                       ng-click="purchaseOrderRequisitionItemsVm.removeFromPurchaseItems(purchaseItem)"
                       aria-hidden="true"></i>
                </td>


            </tr>
            </tbody>
        </table>
    </div>
</div>

