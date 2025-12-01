<div style="margin: 10px;border: 1px solid #D0DDE1;padding: 10px;background-color: #FFF;">
    <div>
        <button class='btn btn-sm btn-success min-width' ng-click='editShipment()'
                ng-if="lastSelectedShipment.editMode == false">Edit</button>
        <button class='btn btn-sm btn-success' ng-click='updateShipment(lastSelectedShipment)'
                ng-if="lastSelectedShipment.editMode == true">Save</button>
        <button class='btn btn-sm btn-danger' ng-click='cancelShipment(lastSelectedShipment)'
                ng-if="lastSelectedShipment.status == 'PENDING'">Cancel</button>
    </div>
    <div ng-if="lastSelectedShipment.editMode == undefined || lastSelectedShipment.editMode == false">
        <table class="table table-striped">
            <thead>
            <tr>
                <th style="text-align: center; width: 50px">#</th>
                <th>Item</th>
                <th>Unit Price</th>
                <th style="text-align: center">Boxes</th>
                <th style="text-align: center">Quantity Shipped</th>
                <th style="width: 150px;text-align: right">Total Price</th>
            </tr>
            </thead>

            <tbody>
                <tr ng-show="lastSelectedShipment.details.length == 0">
                    <td colspan="5" style="text-align: left">
                        <span>No items in shipment.</span>
                    </td>
                </tr>
                <tr ng-repeat="item in lastSelectedShipment.details" ng-if="item.quantityShipped > 0">
                    <td style="text-align: center; width: 50px">{{item.serialNumber != null ? item.serialNumber : $index+1}}</td>
                    <td style="vertical-align: middle;">
                        <div class="text-primary">
                            <strong class="mr20">{{item.product.name}}</strong>
                        </div>
                        <small class="text-danger">
                            <a title="Remove this item" href="" ng-click="removeShipmentItem(item)" ng-if= "shipment.status == 'PENDING'" style="color: #FF4500">Remove</a>
                        </small>
                    </td>
                    <td style="vertical-align: middle;">{{item.unitPrice | currency:"&#x20b9; ":0}}</td>
                    <td style="text-align: center; vertical-align: middle;">{{item.boxes}}</td>
                    <td style="text-align: center; vertical-align: middle;">{{item.quantityShipped}}</td>
                    <td style="width: 150px;text-align: right;vertical-align: middle;">{{(item.itemTotal) | currency:"&#x20b9; ":0}}</td>
                </tr>

                <tr>
                    <td colspan="4" style="text-align: right">
                        <strong>Discount: </strong>
                    </td>
                    <td style="width: 150px;text-align: right">
                        {{lastSelectedShipment.discount != null ? lastSelectedShipment.discount + "%" : ""}}
                    </td>
                </tr>

                <tr>
                    <td colspan="4" style="text-align: right">
                        <strong>Special Discount: </strong>
                    </td>
                    <td style="width: 150px;text-align: right">
                        {{lastSelectedShipment.specialDiscount != null ? lastSelectedShipment.specialDiscount + "%" : ""}}
                    </td>
                </tr>

                <tr>
                    <td colspan="4" style="text-align: right">
                        <strong>TOTAL: </strong>
                    </td>
                    <td style="width: 150px;text-align: right">
                        <h4 class="text-primary">{{lastSelectedShipment.invoiceAmount | currency:"&#x20b9; ":0}}</h4>
                    </td>
                </tr>
            </tbody>
        </table>
    </div>
    <div ng-if="lastSelectedShipment.editMode == true">
        <table class="table table-striped">
            <thead>
            <tr>
                <th style="text-align: center; width: 50px">#</th>
                <th>Item</th>
                <th>Unit Price</th>
                <th style="text-align: center">Quantity Shipped</th>
                <th style="width: 150px;text-align: right">Total Price</th>
            </tr>
            </thead>

            <tbody>
                <tr ng-repeat="item in editingShipment.details">
                    <td style="text-align: center; width: 50px">{{item.serialNumber != null ? item.serialNumber : $index+1}}</td>
                    <td style="vertical-align: middle;">
                        <div class="text-primary">
                            <strong class="mr20">{{item.product.name}}</strong><br>
                            <small class="text-danger">
                                <a title="Remove this item" href=""
                                   ng-click="removeShipmentItem(item)" style="color: #FF4500">Remove</a>
                            </small>
                        </div>
                    </td>
                    <td style="text-align: center; vertical-align: middle; width: 100px;">
                        <input class="form-control" type="number" style="width: 100%; text-align: center;"
                               ng-model="item.unitPrice"/>
                    </td>
                    <td style="text-align: center; vertical-align: middle; width: 100px;">
                        <input class="form-control" type="number" style="width: 100%; text-align: center;"
                               ng-model="item.quantityShipped"/>
                    </td>
                    <td style="text-align: center; vertical-align: middle;">
                        <span>{{item.quantityShipped * item.unitPrice | currency:"&#x20b9; ":0}}</span>
                    </td>
                </tr>
            </tbody>
        </table>
    </div>
</div>