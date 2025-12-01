<style>
    .table {
        border-spacing: 0;
        border-collapse: unset;
    }

    .ui-select-bootstrap > .ui-select-choices, .ui-select-bootstrap > .ui-select-no-choice {
        height: auto;
        max-height: 200px;
        overflow-x: hidden;
        margin-top: -1px;
        width: auto !important;
    }
</style>
<div style="position: relative;">
    <div style="overflow-y: auto;overflow-x: hidden;height: 100%;">
        <div style="border-bottom: 1px solid #D3D7DB;background-color: #F7F7F7;padding: 4px;">
            <ui-select class="required-field" style="width: 200px;display: block; margin-left: auto; margin-right: auto"
                       ng-change="purchaseOrderRequestsVm.projectRequisitionChanged(purchaseOrderRequestsVm.requisitionObject)"
                       ng-model="purchaseOrderRequestsVm.requisitionObject"
                       theme="bootstrap">
                <ui-select-match placeholder="Select Request">{{$select.selected.requisitionNumber}}
                </ui-select-match>
                <ui-select-choices
                        repeat="requisition in purchaseOrderRequestsVm.projectRequisitions | filter: $select.search"
                        style="max-height: 120px;">
                    <div ng-bind="requisition.requisitionNumber | highlight: $select.requisitionNumber.search"></div>
                </ui-select-choices>
            </ui-select>

        </div>
        <div class="responsive-table" style="padding: 10px;margin-bottom: 50px !important;">
            <table class='table table-striped highlight-row'>
                <thead>
                <tr>
                    <th></th>
                    <th>Item Number</th>
                    <th>Item Description</th>
                    <th style="text-align: center">Requested Qty</th>
                </tr>
                </thead>
                <tbody>
                <tr ng-if="purchaseOrderRequestsVm.loading == true">
                    <td colspan="10">
                                                     <span style="font-size: 15px;">
                                                         <img src="app/assets/bower_components/cassini-platform/images/loaders/loader19.gif"
                                                              class="mr5">Loading Items..
                                                     </span>
                    </td>
                </tr>

                <tr ng-if="purchaseOrderRequestsVm.requisitionObject == null">
                    <td colspan="10">Select Requisition</td>
                </tr>

                <tr ng-if="purchaseOrderRequestsVm.loading == false && purchaseOrderRequestsVm.requisitionObject.customRequisitionItems.length == 0">
                    <td colspan="10">No Items are available to view</td>
                </tr>
                <tr ng-repeat="requestItem in purchaseOrderRequestsVm.requisitionObject.customRequisitionItems">
                    <td>
                        <i class="fa fa-plus-circle"
                           ng-click="purchaseOrderRequestsVm.addToPurchaseItems(purchaseOrderRequestsVm.requisitionObject,requestItem)"
                           aria-hidden="true"></i>
                    </td>
                    <td>
                        <span>{{requestItem.materialItem.itemNumber}}</span>
                    </td>
                    <td ng-if="requestItem.materialItem.description != null"
                        style="vertical-align: middle;width: 350px;max-width:350px !important;text-overflow: ellipsis;white-space: nowrap;overflow: hidden;"
                        title="{{requestItem.materialItem.description}}">
                        <span>{{requestItem.materialItem.description}}</span>
                    </td>
                    <td ng-if="requestItem.materialItem.description == null"
                        style="vertical-align: middle;width: 350px;max-width:350px !important;text-overflow: ellipsis;white-space: nowrap;overflow: hidden;"
                        title="{{requestItem.materialItem.itemName}}">
                        <span>{{requestItem.materialItem.itemName}}</span>
                    </td>
                    <td style="text-align: center">
                        <span ng-hide="item.item.hasBom">{{requestItem.quantity}}</span>
                    </td>
                </tr>
                </tbody>
            </table>
        </div>
    </div>
</div>
