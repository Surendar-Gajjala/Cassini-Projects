<div class="view-container">
    <div class="view-content">
        <div class="responsive-table">
            <table class="table table-striped">
                <thead>
                <tr>
                    <th style="width: 200px;">Item Number</th>
                    <th style="width: 200px;">Item Name</th>
                    <th style="width: 200px;">Item Type</th>
                    <th style="width: 200px;">Units</th>
                    <th style="width: 200px;">Type</th>
                    <th style="width: 200px; text-align: center">Quantity</th>
                    <th style="width: 200px;">Timestamp</th>
                </tr>
                </thead>
                <tbody>
                <tr ng-if="stockMovVm.historyList.length == 0">
                    <td colspan="10">No data</td>
                </tr>
                <tr ng-repeat="row in stockMovVm.historyList">
                    <td style="width: 200px;">{{row.boqItemObject.itemNumber}}</td>
                    <td style="width: 200px;">{{row.boqItemObject.itemName}}</td>
                    <td style="width: 200px;">{{row.boqItemObject.itemType}}</td>
                    <td style="width: 200px;">{{row.boqItemObject.units}}</td>
                    <td style="width: 200px;">{{row.movementType}}</td>
                    <td style="width: 200px; text-align: center">{{row.quantity}}</td>
                    <td style="width: 200px;">{{row.timeStamp}}</td>
                </tr>
                </tbody>
            </table>
        </div>
    </div>
</div>