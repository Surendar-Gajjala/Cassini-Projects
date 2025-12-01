<div class="view-container" fitcontent>
    <div class="view-toolbar">

    </div>
    <div class="view-content no-padding" style="overflow-y: auto;">
        <div class="responsive-table" style="padding:10px;">
            <table class="table table-striped highlight-row">
                <thead>
                <tr>
                    <th style="width: 150px; text-align: center">Item Number</th>
                    <th style="width: 150px; text-align: center">Item Name</th>
                    <th style="width: 150px; text-align: center">Item Qty</th>
                    <th style="width: 150px; text-align: center">Received Qty</th>
                    <th style="width: 150px; text-align: center"
                        ng-repeat="storeDetail in inventoryVm.inventories[0].storeDetailsList">
                        <div>Qty in {{storeDetail.storeName}}</div>
                    </th>
                </tr>
                </thead>
                <tbody>
                <tr ng-if="inventoryVm.inventories.length == 0">
                    <td colspan="6">No Inventory data available to view</td>
                </tr>
                <tr ng-repeat="invStoreDetails in inventoryVm.inventories">
                    <td style="width: 150px; text-align: center">{{invStoreDetails.itemNumber}}</td>
                    <td style="width: 150px; text-align: center">{{invStoreDetails.itemName}}</td>
                    <td style="width: 150px; text-align: center">{{invStoreDetails.boqQuantity}}</td>
                    <td style="width: 150px; text-align: center">{{invStoreDetails.storeInvQtyTotal}}</td>
                    <td style="width: 150px; text-align: center"
                        ng-repeat="invStore in invStoreDetails.storeDetailsList">
                        <div>{{invStore.quantity}}</div>
                    </td>
                </tr>
                </tbody>
            </table>
        </div>
    </div>
</div>
