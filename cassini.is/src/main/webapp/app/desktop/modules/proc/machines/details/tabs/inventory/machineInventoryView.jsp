<div class="responsive-table">
    <table class="table table-striped">
        <thead>
        <tr>
            <th>Store</th>
            <th style="text-align: center">Received Quantity</th>
            <th style="text-align: center">Inventory Quantity</th>
            <th style="text-align: center">Issued Quantity</th>
        </tr>
        </thead>
        <tbody>
        <tr ng-if="machineInventoryVm.loading == false && machineInventoryVm.itemInventory.length == 0">
            <td colspan="6">No data</td>
        </tr>
        <tr ng-repeat="row in machineInventoryVm.itemInventory">
            <td>{{row.store.storeName}}</td>
            <td style="text-align: center">{{row.stockMovementDTO.receivedQty}}</td>
            <td style="text-align: center">{{row.storeOnHand}}</td>
            <td style="text-align: center">{{row.stockMovementDTO.issuedQty}}</td>
        </tr>
        </tbody>
    </table>
</div>