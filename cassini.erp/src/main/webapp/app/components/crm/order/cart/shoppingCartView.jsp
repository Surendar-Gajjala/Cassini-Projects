<script type="text/ng-template" id="shopping-cart-tb">
    <div>
        <button class="btn btn-sm btn-primary" ng-click="performCreateOrder()" ng-hide="orderCreated"
                ng-if="shoppingCart.items.length > 0" ng-disabled="$parent.creatingOrder">Create</button>
        <button class="btn btn-sm btn-default" ng-click="cancelShoppingCart()" ng-hide="orderCreated">Cancel</button>
        <button class="btn btn-sm btn-primary" ng-click="showOrders()" ng-show="orderCreated"
                ng-if="hasRole('Administrator') || hasRole('Sales Administrator')">Show All Orders</button>
        <button class="btn btn-sm btn-success" ng-click="createAnotherOrder()" ng-show="createAnother">Create Another Order</button>
    </div>
</script>
<div class="row">
    <div class="col-xs-12 text-right">
        <span>Order Type:</span>
        <span class="order-type" ng-class="{'order-type-product': shoppingCart.orderType == 'PRODUCT',
                    'order-type-sample': shoppingCart.orderType == 'SAMPLE'}" style="margin-left: 10px;">
            {{shoppingCart.orderType}}
        </span>
    </div>
</div>
<div class="row" style="padding-left: 10px;padding-right: 10px;">
    <div class="col-xs-12 col-sm-12 col-md-3" ng-class="{'styled-panel': shoppingCart.customer != null}" style="padding-top: 5px;">
        <div class="row">
            <div class="col-xs-12 col-sm-12">
                <h4 class="text-primary">Customer</h4>
                <div ng-show="shoppingCart.customer != null">
                    <h4>Name: <span class="text-success">{{shoppingCart.customer.name}}</span></h4>
                    <h4>Region: <span class="text-success">{{shoppingCart.customer.salesRegion.name}}</span></h4>
                    <h4>Sales Rep: <span class="text-success">{{shoppingCart.customer.salesRep.firstName}}</span></h4>
                </div>
                <button class="btn btn-sm btn-default mr10" ng-hide="orderCreated"
                        ng-click="showCustomerSelectionDialog()">{{selectText}}</button>
                <button class="btn btn-sm btn-success" ng-hide="orderCreated"
                        ng-click="newCustomer()">Create</button>
            </div>
        </div>
        <div class="row" ng-if="shoppingCart.customer != null">
            <div class="col-xs-12">
                <h4 class="text-primary">Additional Information</h4>
                <div class="styled-panel">
                    <div class="form-group">
                        <label class="control-label">PO Number:</label>
                        <div class="">
                            <input type="text" class="form-control" ng-model="shoppingCart.poNumber" ng-disabled="orderCreated">
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="control-label">Delivery Date:</label>
                        <div class="">
                            <input type="text" class="form-control" date-picker ng-model="shoppingCart.deliveryDate" ng-disabled="orderCreated">
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="control-label">Notes:</label>
                        <div class="">
                            <textarea class="form-control" rows="3" style="resize: none;"
                                      ng-model="shoppingCart.notes" ng-disabled="orderCreated"></textarea>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="control-label">Phone Number(s):</label>
                        <div class="">
                            <input type="text" class="form-control" ng-model="shoppingCart.contactPhone" ng-disabled="orderCreated">
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <div class="row" ng-if="shoppingCart.customer != null">
            <div class="col-xs-12 col-sm-12 col-md-12">
                <h4 class="text-primary">Addresses</h4>
                <div style="background-color:#FFF">
                    <tabset>
                        <tab heading="Billing Address" active="true" ng-init="bAddress = shoppingCart.billingAddress">
                            <div class="styled-panel">
                                <div class="form-group">
                                    <label class="control-label">Address:</label>
                                    <div class="">
                                        <textarea class="form-control" rows="5" style="resize: none;"
                                                  ng-model="bAddress.addressText" ng-disabled="orderCreated"></textarea>
                                    </div>
                                </div>
                                <div class="form-group">
                                    <label class="control-label">City/Town:</label>
                                    <div class="">
                                        <input type="text" class="form-control" ng-model="bAddress.city" ng-disabled="orderCreated">
                                    </div>
                                </div>
                                <div class="form-group">
                                    <label class="control-label">State:</label>
                                    <div class="">
                                        <ui-select ng-model="bAddress.state" theme="bootstrap" style="width:100%" ng-disabled="orderCreated">
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
                                        <input type="text" class="form-control" ng-model="bAddress.pincode" ng-disabled="orderCreated">
                                    </div>
                                </div>
                            </div>
                        </tab>
                        <tab heading="Shipping Address">
                            <div class="styled-panel">
                                <div>
                                    <form style="display: inline-block; margin-left: 10px;" class="form-inline">
                                        <div class="checkbox">
                                            <label>
                                                <input type="checkbox"  ng-disabled="orderCreated" ng-change="toggleShippingSame()"
                                                          ng-model="shoppingCart.shippingSameAsBilling"> Same as billing
                                            </label>
                                        </div>
                                    </form>
                                </div>
                                <div ng-if="shoppingCart.shippingSameAsBilling == false">
                                    <div class="form-group">
                                        <label class="control-label">Ship To:</label>
                                        <div class="">
                                            <input type="text" class="form-control" ng-model="shoppingCart.shipTo" ng-disabled="orderCreated">
                                        </div>
                                    </div>
                                    <div class="form-group">
                                        <label class="control-label">Address:</label>
                                        <div class="">
                                            <textarea class="form-control" rows="5" style="resize: none;"  ng-disabled="orderCreated"
                                                      ng-model="shoppingCart.shippingAddress.addressText"></textarea>
                                        </div>
                                    </div>
                                    <div class="form-group">
                                        <label class="control-label">City/Town:</label>
                                        <div class="">
                                            <input type="text" class="form-control" ng-model="shoppingCart.shippingAddress.city" ng-disabled="orderCreated">
                                        </div>
                                    </div>
                                    <div class="form-group">
                                        <label class="control-label">State:</label>
                                        <div class="">
                                            <ui-select ng-model="shoppingCart.shippingAddress.state" theme="bootstrap" style="width:100%" ng-disabled="orderCreated">
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
                                            <input type="text" class="form-control" ng-model="shoppingCart.shippingAddress.pincode" ng-disabled="orderCreated">
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </tab>
                    </tabset>
                </div>
            </div>
        </div>
    </div>
    <div class="col-xs-12 col-sm-12 col-md-9" ng-if="shoppingCart.customer != null || shoppingCart.items.length > 0">
        <h4 class="text-primary">Order Items:</h4>
        <div>
            <button class="btn btn-success" ng-click="updateOrder()" ng-show="orderCreated && orderUpdated">Save Order</button>
            <button class="btn btn-primary" ng-click="buyProducts()" ng-hide="orderCreated">Select</button>
            <button class="btn btn-success" ng-click="showQuickOrderForm()"
                    ng-hide="orderCreated">{{quickOrderForm == false ? 'Order Form' : 'Hide Order Form'}}</button>
            <button class="btn btn-danger" ng-click="clearCart()"
                    ng-hide="orderCreated" ng-disabled="shoppingCart.items.length == 0">Clear Cart</button>
        </div>

        <br><br>
        <div class="table-responsive" ng-if="quickOrderForm == false">
            <table class="table table-striped">
                <thead>
                <tr>
                    <th style="text-align: center; width: 50px">#</th>
                    <th style="width: 100px">SKU</th>
                    <th>Item</th>
                    <th style="width: 100px;text-align: center">Quantity</th>
                    <th style="width: 100px;text-align: center">Unit Price</th>
                    <th style="width: 200px;text-align: right">Total Price</th>
                </tr>
                </thead>

                <tbody>
                <tr ng-show="shoppingCart.items.length == 0">
                    <td colspan="5" style="text-align: left">
                        <span>Select items or use quick order form.</span>
                    </td>
                </tr>
                <tr ng-repeat="item in shoppingCart.items">
                    <td style="text-align: center; width: 50px">{{item.serialNumber != null ? item.serialNumber : $index+1}}</td>
                    <td style="vertical-align: middle; width: 100px">
                        <span>{{item.product.sku}}</span>
                    </td>
                    <td style="vertical-align: middle; text-align: left !important">
                        <div class="text-primary"><strong>{{item.product.name}}</strong></div>
                        <%--Remove Button--%>
                        <small class="text-danger">
                            <a title="Remove this item" href="" ng-click="removeItem(item)" style="color: #FF4500">Remove</a>
                        </small>
                    </td>
                    <td align="center" style="text-align: center; vertical-align: middle;width: 100px;">
                        <%--<input type="number" class="form-control"
                               style="display: inline; width: 80px; text-align: center"
                               ng-model="item.quantity" value="{{item.quantity}}"/>--%>
                            <div class="text-primary">{{item.quantity}}</div>
                    </td>
                    <td style="vertical-align: middle;width: 100px;text-align: center">
                        <%--<input type="number" class="form-control"
                               style="display: inline; width: 80px; text-align: center"
                               ng-model="item.unitPrice" value="{{item.unitPrice}}"/>--%>
                        <div class="text-primary">{{item.unitPrice}}</div>
                    </td>
                    <td style="vertical-align: middle;width: 200px;text-align: right">
                        {{(item.quantity * item.unitPrice) | currency:"&#x20b9; ":0}}
                    </td>
                </tr>
                </tbody>
            </table>
        </div>

        <div class="table-responsive" ng-if="quickOrderForm == true">
            <table class="table table-striped">
                <thead>
                <tr>
                    <th style="text-align: center; width: 50px">#</th>
                    <th style="width: 100px">SKU</th>
                    <th>Item</th>
                    <th style="width: 100px;text-align: center">Quantity</th>
                    <th style="width: 100px;text-align: center">Unit Price</th>
                    <th style="width: 200px;text-align: right">Total Price</th>
                </tr>
                </thead>

                <tbody>
                <tr ng-show="shoppingCart.items.length == 0">
                    <td colspan="5" style="text-align: left">
                        <span>No items in the shopping cart.</span>
                    </td>
                </tr>
                <tr ng-repeat="item in shoppingCart.items">
                    <td style="text-align: center; width: 50px">{{item.serialNumber != null ? item.serialNumber : $index+1}}</td>
                    <td style="vertical-align: middle; width: 100px">
                        <input id="sku{{$index}}" type="text" class="form-control" ng-focus="item.error = false"
                               ng-blur="getProductBySku(item)"
                               style="display: inline; width: 100px; text-align: left"
                               ng-model="item.product.sku" value="{{item.product.sku}}" ng-hide="orderCreated"/>
                    </td>
                    <td style="vertical-align: middle; text-align: left !important">
                        <div class="text-primary" ng-if="item.error == false"><strong>{{item.product.name}}</strong></div>
                        <div class="text-danger blink" ng-if="item.error == true"><strong>{{item.errorMessage}}</strong></div>
                        <small class="text-danger" ng-if="item.product.name != null || item.error == true">
                            <a title="Remove this item" href="" ng-click="removeItem($index)" style="color: #FF4500">Remove</a>
                        </small>
                    </td>
                    <td align="center" style="text-align: center; vertical-align: middle;width: 100px;">
                        <span ng-show="orderCreated">{{item.quantity}}</span>
                        <input type="number" class="form-control" ng-disabled="item.error == true"
                               style="display: inline; width: 100px; text-align: center"
                               ng-model="item.quantity" value="{{item.quantity}}" ng-hide="orderCreated"/>
                    </td>
                    <td style="vertical-align: middle;width: 100px;text-align: center">
                        <!--{{item.unitPrice | currency:"&#x20b9; ":0}}-->
                        <span ng-show="orderCreated">{{item.unitPrice}}</span>
                        <input type="number" class="form-control"
                               ng-focus="addNewRowToOrderForm(item)"
                               ng-disabled="item.error == true"
                               style="display: inline; width: 100px; text-align: center"
                               ng-model="item.unitPrice" value="{{item.unitPrice}}" ng-hide="orderCreated"/>
                    </td>
                    <td style="vertical-align: middle;width: 200px;text-align: right">
                        {{(item.quantity * item.unitPrice) | currency:"&#x20b9; ":0}}
                    </td>
                </tr>
                </tbody>
            </table>
        </div>

        <table class="table table-total">
            <tbody>
                <tr>
                    <td><strong>TOTAL :</strong></td>
                    <td><h3 class="text-primary">{{getSubTotal() | currency:"&#x20b9; ":0}}</h3></td>
                </tr>
            </tbody>
        </table>
    </div>
</div>