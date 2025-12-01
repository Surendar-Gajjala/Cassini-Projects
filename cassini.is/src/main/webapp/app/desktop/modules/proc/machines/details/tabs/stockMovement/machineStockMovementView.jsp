<div class="responsive-table">
    <table class="table table-striped">
        <thead>
        <tr>
            <th>Store</th>
            <th>Movement Type</th>
            <th>Quantity</th>
            <th>Timestamp</th>
        </tr>
        </thead>
        <tbody>
        <tr ng-if="machineMovementVm.loading == false && machineMovementVm.itemStockMovement.length == 0">
            <td colspan="6">No data</td>
        </tr>
        <tr ng-repeat="row in machineMovementVm.itemStockMovement">
            <td>{{row.store.storeName}}</td>
            <td>{{row.movementType}}</td>
            <td>{{row.quantity}}</td>
            <td>{{row.timeStamp}}</td>
        </tr>
        </tbody>
    </table>
</div>