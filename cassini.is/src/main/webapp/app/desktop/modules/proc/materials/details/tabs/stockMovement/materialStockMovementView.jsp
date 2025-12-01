<div class="view-container">
    <div class="view-content no-padding">
        <div class="responsive-table">
            <table class="table table-striped">
                <thead>
                <tr>
                    <th>Store</th>
                    <th>MovementType</th>
                    <th>Project</th>
                    <th>Reference</th>
                    <th>Quantity</th>
                    <th>Timestamp</th>
                </tr>
                </thead>
                <tbody>
                <tr ng-if="materialMovementVm.loading == false && materialMovementVm.itemStockMovement.length == 0">
                    <td colspan="6">No data</td>
                </tr>
                <tr ng-repeat="row in materialMovementVm.itemStockMovement">
                    <td>{{row.store.storeName}}</td>
                    <td>{{row.movementType}}</td>
                    <td>{{row.projectObject.name}}</td>
                    <td>{{row.reference}}</td>
                    <td>{{row.quantity}}</td>
                    <td>{{row.timeStamp}}</td>
                </tr>
                </tbody>
            </table>
        </div>
        <br>
    </div>


</div>
</div>