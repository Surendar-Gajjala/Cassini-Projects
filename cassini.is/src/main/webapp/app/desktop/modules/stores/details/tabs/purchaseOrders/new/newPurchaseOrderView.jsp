<div class="view-container" fitcontent>
    <style>
        .split-left-pane {
            width: 400px;
            padding: 10px;
        }

        .split-pane-divider {
            background: #EEE;
            left: 400px;
            width: 5px;
        }
    </style>

    <div class="view-toolbar">
        <div class="btn-group">
            <button class="min-width btn btn-sm btn-default" ng-click="newPurchaseOrderVm.back()">Back</button>
            <button ng-if="newPurchaseOrderVm.newPurchaseOrder.status != 'APPROVED' && (newPurchaseOrderVm.showAddRequestsButton || newPurchaseOrderVm.editMode)"
                    class="min-width btn btn-sm btn-default" ng-click="newPurchaseOrderVm.openRequestsDialogue()">Add
                Items
            </button>
            <button ng-if="newPurchaseOrderVm.groupedItems.length > 0 && newPurchaseOrderVm.newPurchaseOrder.status != 'APPROVED' && (newPurchaseOrderVm.showCreatePurchaseButton || newPurchaseOrderVm.editMode)"
                    class="min-width btn btn-sm btn-default"
                    ng-click="newPurchaseOrderVm.createPurchaseOrder()">{{newPurchaseOrderVm.buttonText}}
            </button>

        </div>
    </div>

    <div class="view-content no-padding">
        <div class="split-pane fixed-left">

            <div class="split-pane-component split-left-pane" style="overflow-x: auto">
                <form ng-if="newPurchaseOrderVm.editMode || newPurchaseOrderVm.mode == 'new'" class="form-horizontal"
                      style="width:400px;padding: 20px;padding-top: 26px; margin-left: -22px;">

                    <div class="form-group">
                        <label class="col-sm-5 control-label">Autonumber :</label>

                        <div class="col-sm-7">

                            <div class="input-group mb15">
                                <div class="input-group-btn">
                                    <button class="btn btn-default" type="button" style="width: 85px"
                                            ng-click="newPurchaseOrderVm.generateAutoNumber()">Auto
                                    </button>
                                </div>
                                <input type="text" class="form-control" name="title"
                                       ng-model="newPurchaseOrderVm.newPurchaseOrder.poNumber">
                            </div>
                        </div>
                    </div>

                    <div class="form-group">
                        <label class="col-sm-5 control-label" style="margin-top: -9px;">Supplier :<span
                                class="asterisk">*</span></label>

                        <div class="col-sm-7" style="margin-top: -9px;">
                            <ui-select class="required-field"
                                       ng-change="newPurchaseOrderVm.supplierChanged()"
                                       ng-model="newPurchaseOrderVm.newPurchaseOrder.supplierObject"
                                       theme="bootstrap">
                                <ui-select-match placeholder="Select Supplier">{{$select.selected.name}}
                                </ui-select-match>
                                <ui-select-choices
                                        repeat="supplier in newPurchaseOrderVm.suppliers | filter: $select.search |orderBy: 'name'"
                                        style="max-height: 120px;">
                                    <div ng-bind="supplier.name | highlight: $select.name.search"></div>
                                </ui-select-choices>
                            </ui-select>
                        </div>
                    </div>

                    <div class="form-group"
                         ng-if="newPurchaseOrderVm.newPurchaseOrder.status == 'NEW' || newPurchaseOrderVm.newPurchaseOrder.status == 'APPROVED'">
                        <label class="col-sm-5 control-label">Status :<span class="asterisk">*</span></label>

                        <div class="col-sm-7" style="margin-top: 10px;">
                           <span class="label" ng-class=" {
                                    'label-success': newPurchaseOrderVm.newPurchaseOrder.status == 'NEW',
                                    'label-primary': newPurchaseOrderVm.newPurchaseOrder.status == 'APPROVED'}">
                            {{newPurchaseOrderVm.newPurchaseOrder.status}}
                        </span>
                        </div>
                    </div>

                    <div class="form-group">
                        <label class="col-sm-5 control-label" style="margin-top: -7px;">PO Date :<span class="asterisk">*</span></label>

                        <div class="col-sm-7">
                            <input class="form-control" style="margin-top: -3px;"
                                   ng-model="newPurchaseOrderVm.newPurchaseOrder.poDateObject"
                                   placeholder="dd-mm-yyyy" type="text" inward-date-picker/>
                        </div>
                    </div>

                    <div class="form-group"
                         ng-if="newPurchaseOrderVm.newPurchaseOrder.status == 'NEW' || newPurchaseOrderVm.newPurchaseOrder.status == 'APPROVED'">
                        <label class="col-sm-5 control-label">ApprovedBy :<span class="asterisk">*</span></label>

                        <div class="col-sm-7">
                            <input type="text" class="form-control"
                                   ng-model="newPurchaseOrderVm.newPurchaseOrder.approvedByObject">
                        </div>
                    </div>

                    <div class="form-group">
                        <label class="col-sm-5 control-label" style="margin-top: -7px;">Notes : </label>

                        <div class="col-sm-7" ng-if="newPurchaseOrderVm.editMode || newPurchaseOrderVm.mode == 'new'">
                            <textarea class="form-control" st
                                      ng-model="newPurchaseOrderVm.newPurchaseOrder.notesObject"
                                      style="width: 202px; height: 39px;margin-top: -2px;">
                            </textarea>
                        </div>
                    </div>

                    <br>
                    <h4 ng-if="newPurchaseOrderVm.purchaseOrderAttributes.length  > 0 || newPurchaseOrderVm.attributes.length > 0"
                        class="section-title" style="color: black;">Attributes
                    </h4>
                    <br>

                    <div>
                        <form class="form-horizontal">
                            <attributes-view show-objects="selectObjectValues"
                                             attributes="newPurchaseOrderVm.requiredAttributes"></attributes-view>
                            <br>
                            <attributes-view show-objects="selectObjectValues"
                                             attributes="newPurchaseOrderVm.attributes"></attributes-view>
                            <br>
                            <br>
                        </form>
                    </div>

                </form>

                <div ng-if="!newPurchaseOrderVm.editMode && newPurchaseOrderVm.mode != 'new'"
                     class="item-details col-sm-12" style="padding: 30px;">
                    <div class="row">
                        <div class="label col-sm-2">
                            <span>Supplier : </span>
                        </div>
                        <div class="value col-sm-10">
                            <p style="margin-left: 45px">{{newPurchaseOrderVm.newPurchaseOrder.supplierObject.name}}</p>
                        </div>
                    </div>
                    <div class="row"
                         ng-if="newPurchaseOrderVm.newPurchaseOrder.status == 'NEW' || newPurchaseOrderVm.newPurchaseOrder.status == 'APPROVED'">
                        <div class="label col-sm-2">
                            <span>Status : </span>
                        </div>
                        <div class="value col-sm-10">
                            <span class="label" style="margin-left: 45px" ng-class=" {
                                    'label-success': newPurchaseOrderVm.newPurchaseOrder.status == 'NEW',
                                    'label-primary': newPurchaseOrderVm.newPurchaseOrder.status == 'APPROVED'}">
                            {{newPurchaseOrderVm.newPurchaseOrder.status}}</span>
                        </div>
                    </div>
                    <div class="row">
                        <div class="label col-sm-2">
                            <span>PO Date: </span>
                        </div>
                        <div class="value col-sm-10">
                            <p style="margin-left: 45px">{{newPurchaseOrderVm.newPurchaseOrder.poDateObject}}</p>
                        </div>
                    </div>
                    <div class="row form-group"
                         ng-if="newPurchaseOrderVm.newPurchaseOrder.status == 'NEW' || newPurchaseOrderVm.newPurchaseOrder.status == 'APPROVED'">
                        <div class="label col-sm-2">
                            <span>Approved By:<span ng-if="newPurchaseOrderVm.newPurchaseOrder.status == 'NEW'"
                                                    class="asterisk">*</span></span>
                        </div>
                        <div class="value col-sm-10">
                            <input type="text" style="margin-left: 53px;width: 192px;margin-top: -6px;"
                                   class="form-control" ng-if="newPurchaseOrderVm.newPurchaseOrder.status == 'NEW'"
                                   ng-model="newPurchaseOrderVm.newPurchaseOrder.approvedByObject">

                            <p ng-if="newPurchaseOrderVm.newPurchaseOrder.status == 'APPROVED'"
                               style="margin-left: 45px">
                                {{newPurchaseOrderVm.newPurchaseOrder.approvedByObject}}</p>
                        </div>
                    </div>
                    <div class="row">
                        <div class="label col-sm-2">
                            <span>Notes : </span>
                        </div>
                        <div class="value col-sm-10">
                            <p title="{{newPurchaseOrderVm.newPurchaseOrder.notesObject}}" style="margin-left: 45px">
                                {{newPurchaseOrderVm.newPurchaseOrder.notesObject | limitTo: 8
                                }}{{newPurchaseOrderVm.newPurchaseOrder.notesObject.length > 8 ? '...' : ''}}</p>
                        </div>
                    </div>
                </div>
            </div>

            <div class="split-pane-divider"></div>

            <div class="split-pane-component split-right-pane" style="left: 400px; cursor: pointer">
                <table class='table table-striped highlight-row'>
                    <thead>
                    <tr>
                        <th>Item Number</th>
                        <th>Item Name</th>
                        <th>Requested Qty</th>
                        <th>Notes</th>
                        <th>Purchase Qty</th>
                        <th ng-hide="newPurchaseOrderVm.mode == 'approved'" style="text-align: center;">Actions</th>
                    </tr>
                    </thead>
                    <tbody>
                    <tr ng-if="newPurchaseOrderVm.loading == true">
                        <td colspan="10">
                                                     <span style="font-size: 15px;">
                                                         <img src="app/assets/bower_components/cassini-platform/images/loaders/loader19.gif"
                                                              class="mr5">Loading Items..
                                                     </span>
                        </td>
                    </tr>

                    <tr ng-if="newPurchaseOrderVm.loading == false && newPurchaseOrderVm.groupedItems.length == 0">
                        <td colspan="10">No Items are available to view</td>
                    </tr>
                    <tr ng-repeat="purchaseItem in newPurchaseOrderVm.groupedItems">
                        <td ng-if="purchaseItem.rowType == 'req'" colspan="12">
                            <h5 style="margin: 0;font-weight: bold;">{{purchaseItem.materialItem.itemNumber}}</h5>
                        </td>
                        <td ng-if="purchaseItem.rowType == 'item'">
                            <span>{{purchaseItem.materialItem.itemNumber}}</span>
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

                            <span ng-if="!purchaseItem.editMode && !purchaseItem.isNew">{{purchaseItem.purchaseItemNotesObject}}</span>
                        </td>

                        <td ng-if="purchaseItem.rowType == 'item'" style="text-align: center;width:100px;">
                            <input ng-if="purchaseItem.editMode || (purchaseItem.editMode && !purchaseItem.isNew)"
                                   type="number" min="1"
                                   class="form-control input-sm"
                                   ng-model="purchaseItem.purchaseItemQuantity"
                                   ng-change="newPurchaseOrderVm.onChangePurchaseQty(purchaseItem)"
                                   style="text-align: center;">
                            <span ng-show="!purchaseItem.editMode && !purchaseItem.isNew">{{purchaseItem.purchaseItemQuantity}}</span>
                        </td>

                        <td ng-hide="newPurchaseOrderVm.mode == 'approved'" ng-if="purchaseItem.rowType == 'item'"
                            style="text-align: center;vertical-align: middle">
                            <div class="btn-group btn-group-xs">
                                <button ng-if="!purchaseItem.showEditButton && !purchaseItem.isNew"
                                        class="btn btn-xs btn-success"
                                        ng-click="newPurchaseOrderVm.applyChanges(purchaseItem)" title="Apply Changes">
                                    <i class="fa fa-check"></i></button>

                                <button ng-if="!purchaseItem.showEditButton && !purchaseItem.isNew"
                                        class="btn btn-xs btn-default"
                                        ng-click="newPurchaseOrderVm.cancelChanges(purchaseItem)"
                                        title="Cancel Changes"><i class="fa fa-times"></i></button>

                                <button ng-if="purchaseItem.showEditButton && !purchaseItem.isNew"
                                        title="Edit This Item"
                                        class="btn btn-xs btn-warning"
                                        ng-click="newPurchaseOrderVm.editPurchaseItem(purchaseItem)"><i
                                        class="fa fa-edit"></i></button>

                                <button ng-if="purchaseItem.showEditButton && !purchaseItem.isNew"
                                        title="Delete This item"
                                        class="btn btn-xs btn-danger"
                                        ng-click="newPurchaseOrderVm.deletePurchaseItem(purchaseItem)"><i
                                        class="fa fa-trash"></i></button>

                                <i title="Remove" ng-if="purchaseItem.isNew"
                                   class="fa fa-minus-circle"
                                   style="font-size: 20px;"
                                   ng-click="newPurchaseOrderVm.removeFromPurchaseItems(purchaseItem)"
                                   aria-hidden="true"></i>
                            </div>
                        </td>
                    </tr>
                    </tbody>
                </table>
            </div>
        </div>
    </div>
</div>





