<div class="view-container" fitcontent>
    <div class="view-toolbar">
        <div class="btn-group">
            <button class="btn btn-sm btn-default" ng-click="receivedVm.back()">Back</button>
            <button class="btn btn-sm btn-success" ng-click="receivedVm.select()">Select Items</button>
            <button class="btn btn-sm btn-info" ng-if="receivedVm.selectedItems.length > 0"
                    ng-click="receivedVm.update()">
                Update
            </button>
        </div>
    </div>
    <div class="view-content no-padding" style="overflow-y: auto;">
        <div class="responsive-table" style="padding: 10px;">
            <table class="table table-striped">
                <thead>
                <tr>
                    <th ng-show="onViewOrder">Actions</th>
                    <th style="min-width: 150px;">Item Number</th>
                    <th style="min-width: 150px;">Item Name</th>
                    <th style="min-width: 150px;">Item Type</th>
                    <th style="min-width: 150px;">Boq Qty</th>
                    <th style="min-width: 150px;">Total Received Qty</th>
                    <th style="min-width: 150px;">Balance Qty</th>
                    <th style="min-width: 150px;">Qty</th>
                    <th style="min-width: 150px;">Note</th>
                </tr>
                </thead>
                <tbody>
                <tr ng-if="receivedVm.selectedItems.length == 0">
                    <td colspan="11">No items</td>
                </tr>
                <tr data-ng-repeat="item in receivedVm.selectedItems">
                    <td style="vertical-align: middle;text-align: center">
                        <span>{{item.itemNumber}}</span>

                    </td>
                    <td style="vertical-align: middle;text-align: center">
                        <span>{{item.itemName}}</span>

                    </td>
                    <td style="vertical-align: middle;text-align: center">
                        <span>{{item.itemType}}</span>

                    </td>

                    <td style="vertical-align: middle;text-align: center">
                        {{item.quantity}}
                    </td>
                    <td style="vertical-align: middle;text-align: center">
                        {{item.totalReceivedQty}}
                    </td>
                    <td style="vertical-align: middle; text-align: center">
                        {{item.balanceQty}}
                    </td>
                    <td style="vertical-align: middle;text-align: center">
                        <input placeholder="Enter Qty" class="form-control" type="text"
                               data-ng-model="item.Qty">
                    </td>
                    <td>
                        <input placeholder="Enter Note" class="form-control" type="text" data-ng-model="item.notes">
                    </td>

                </tr>
                </tbody>
            </table>
        </div>
    </div>
</div>
