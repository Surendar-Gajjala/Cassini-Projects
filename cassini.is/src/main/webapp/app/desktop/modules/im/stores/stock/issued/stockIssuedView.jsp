<div class="view-container" fitcontent>
    <div class="view-toolbar">
        <div class="btn-group">
            <button class="btn btn-sm btn-default" ng-click="issuedVm.back()">Back</button>
            <button class="btn btn-sm btn-success" ng-click="issuedVm.select()">Select Items</button>
            <button class="btn btn-sm btn-info" ng-if="issuedVm.selectedItems.length > 0"
                    ng-click="issuedVm.update()">
                Update
            </button>
        </div>
    </div>
    <div class="view-content no-padding" style="overflow-y: auto;">
        <div class="responsive-table" style="padding: 10px;;">
            <table class="table table-striped">
                <thead>
                <tr>
                    <th ng-show="onViewOrder">Actions</th>
                    <th>Item Number</th>
                    <th>Item Name</th>
                    <th>Item Type</th>
                    <th>Inventory</th>
                    <th>Quantity</th>
                    <th>Note</th>
                </tr>
                </thead>
                <tbody>
                <tr ng-if="issuedVm.selectedItems.length == 0">
                    <td colspan="11">No items</td>
                </tr>
                <tr data-ng-repeat="item in issuedVm.selectedItems">
                    <td>
                        <span>{{item.referenceIdObject.itemNumber}}</span>

                    </td>
                    <td>
                        <span>{{item.referenceIdObject.itemName}}</span>

                    </td>
                    <td>
                        <span>{{item.referenceIdObject.itemType}}</span>

                    </td>
                    <td>
                        <span>{{item.inventory}}</span>

                    </td>
                    <td>
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
        <br>
        <br>
    </div>
</div>
