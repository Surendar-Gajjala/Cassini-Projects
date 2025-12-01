<div>
    <button class="btn btn-success mr10" style="width: 100px" ng-click="processShipment(false)">Process</button>
    <!--<button class="btn btn-primary mr10" style="width: 100px" ng-click="processShipment(false)">Save</button>-->
    <button class="btn btn-default" style="width: 100px" ng-click="closeNewShipment()">Cancel</button>
</div>
<hr/>
<div class="row">
    <div class="col-sm-12 col-md-4 col-lg-3 styled-panel" style="padding-top: 0;">
        <h4 class="text-primary">Shipment Information</h4>
        <div class="row" style="padding-left: 10px;padding-right: 10px;">
            <div class="col-sm-12 form-group styled-panel">
                <label class="control-label">Invoice Number</label>
                <input class="form-control" type="text" ng-model="shipment.invoiceNumber"/>
            </div>
        </div>
        <div class="row">
            <div class="col-sm-12">
                <h4 class="text-primary">Addresses</h4>
                <div style="background-color:#FFF">
                    <tabset>
                        <tab heading="Shipping Address" active="true">
                            <div class="styled-panel">
                                <div class="form-group">
                                    <label class="control-label">Address:</label>
                                    <div class="">
                                        <textarea class="form-control" rows="5" style="resize: none;"
                                                  ng-model="order.shippingAddress.addressText" ng-disabled="true"></textarea>
                                    </div>
                                </div>
                                <div class="form-group">
                                    <label class="control-label">City/Town:</label>
                                    <div class="">
                                        <input type="text" class="form-control" ng-model="order.shippingAddress.city" ng-disabled="true">
                                    </div>
                                </div>
                                <div class="form-group">
                                    <label class="control-label">State:</label>
                                    <div class="">
                                        <ui-select ng-model="order.shippingAddress.state" theme="bootstrap" style="width:100%" ng-disabled="true">
                                            <ui-select-match placeholder="Select state">{{$select.selected.name}}</ui-select-match>
                                            <ui-select-choices repeat="state in states | filter: $select.search">
                                                <div ng-bind-html="state.name | highlight: $select.search"></div>
                                            </ui-select-choices>
                                        </ui-select>
                                    </div>
                                </div>
                                <div class="form-group">
                                    <label class="control-label">Pincode:</label>
                                    <div class="">
                                        <input type="text" class="form-control" ng-model="order.shippingAddress.pincode" ng-disabled="true">
                                    </div>
                                </div>
                                <div class="form-group">
                                    <label class="control-label">Phone:</label>
                                    <div class="">
                                        <input type="text" class="form-control" ng-model="order.contactPhone" ng-disabled="true">
                                    </div>
                                </div>
                            </div>
                        </tab>
                        <tab heading="Billing Address">
                            <div class="styled-panel">
                                <div class="form-group">
                                    <label class="control-label">Address:</label>
                                    <div class="">
                                        <textarea class="form-control" rows="5" style="resize: none;"  ng-disabled="true"
                                                  ng-model="order.billingAddress.addressText"></textarea>
                                    </div>
                                </div>
                                <div class="form-group">
                                    <label class="control-label">City/Town:</label>
                                    <div class="">
                                        <input type="text" class="form-control" ng-model="order.billingAddress.city" ng-disabled="true">
                                    </div>
                                </div>
                                <div class="form-group">
                                    <label class="control-label">State:</label>
                                    <div class="">
                                        <ui-select ng-model="order.billingAddress.state" theme="bootstrap" style="width:100%" ng-disabled="true">
                                            <ui-select-match placeholder="Select state">{{$select.selected.name}}</ui-select-match>
                                            <ui-select-choices repeat="state in states | filter: $select.search">
                                                <div ng-bind-html="state.name | highlight: $select.search"></div>
                                            </ui-select-choices>
                                        </ui-select>
                                    </div>
                                </div>
                                <div class="form-group">
                                    <label class="control-label">Pincode:</label>
                                    <div class="">
                                        <input type="text" class="form-control" ng-model="order.billingAddress.pincode" ng-disabled="true">
                                    </div>
                                </div>
                            </div>
                        </tab>
                    </tabset>
                </div>
            </div>
        </div>
    </div>
    <div class="col-sm-12 col-md-8 col-lg-9">
        <h4 class="text-primary">Shipment Items</h4>
        <table class="table table-striped">
            <thead>
                <tr>
                    <th style="text-align: center; width: 50px">#</th>
                    <th>Item</th>
                    <th style="text-align: center; width: 150px">Order Qty</th>
                    <th style="text-align: center; width: 150px">Processed Qty</th>
                    <th style="text-align: center; width: 150px">Balance Qty</th>
                    <th style="text-align: center; width: 150px">Boxes</th>
                    <th style="text-align: center; width: 100px">Ship Qty</th>
                    <th style="text-align: center; width: 100px">Unit Price</th>
                    <th style="text-align: center; width: 100px">Item Total</th>
                </tr>
            </thead>
            <tbody>
                <tr ng-repeat="item in shipment.details" ng-if="item.balanceQuantity > 0">
                    <td style="text-align: center; width: 50px">{{item.serialNumber != null ? item.serialNumber : $index+1}}</td>
                    <td style="vertical-align: middle;">
                        <div class="text-primary">
                            <strong class="mr20">{{item.product.name}}</strong>
                            <span ng-if="item.inventory != null"
                                  class="label-danger low-inventory animated infinite flash"
                                  style="color: #FFF;font-size: 16px;padding: 5px; font-size: 16px">
                                Inventory is {{item.inventory.inventory | currency:"":0}} </span>
                        </div>
                    </td>
                    <td style="text-align: center; vertical-align: middle;">
                        <span>{{item.quantity}}</span>
                    </td>
                    <td style="text-align: center; vertical-align: middle;"><span>{{item.processedQuantity}}</span></td>
                    <td style="text-align: center; vertical-align: middle;"><span>{{item.balanceQuantity}}</span></td>
                    <td style="text-align: center; vertical-align: middle;">
                        <input type="number" style="width:50px;" ng-model="item.boxes"/>
                    </td>
                    <td style="text-align: center; vertical-align: middle; width: 100px;">
                        <input class="form-control" type="number" style="width: 100%; text-align: center;"
                               ng-model="item.quantityShipped"/>
                    </td>
                    <td style="text-align: center; vertical-align: middle; width: 100px;">
                        <input class="form-control" type="number" style="width: 100%; text-align: center;"
                               ng-model="item.unitPrice"/>
                    </td>
                    <td style="text-align: center; vertical-align: middle;">
                        <span>{{item.quantityShipped * item.unitPrice | currency:"&#x20b9; ":0}}</span>
                    </td>
                </tr>
            </tbody>
        </table>
        <table class="table table-total">
            <tbody>
                <tr>
                    <td><strong>Discount (%) :</strong></td>
                    <td style="border: 0;width: 100px !important; ">
                        <input class="form-control" type="number" style="width: 100%; text-align: center;"
                           ng-model="shipment.discount"/>
                    </td>
                </tr>
                <tr>
                    <td><strong>Special Discount (%) :</strong></td>
                    <td style="border: 0;width: 100px !important; ">
                        <input class="form-control" type="number" style="width: 100%; text-align: center;"
                           ng-model="shipment.specialDiscount"/>
                    </td>
                </tr>
                <tr>
                    <td><strong>INVOICE AMOUNT :</strong></td>
                    <td>{{calculateInvoiceAmount() | currency:"&#x20b9; ":0}}</td>
                </tr>
            </tbody>
        </table>
    </div>
</div>