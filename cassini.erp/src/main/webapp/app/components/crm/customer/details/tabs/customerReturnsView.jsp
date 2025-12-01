<div>
    <div ng-if="returns.length == 0" style="padding: 10px;">
        There are no returns
    </div>
    <div ng-repeat="return in returns" class="customer-order">
        <div class="row">
            <div class="col-sm-3">
                <div ng-click="toggleDetails(return)" style="cursor: pointer;float: left;margin-top: 25px;">
                    <span title="Show Details" ng-if="return.showDetails == false"><i
                            class="fa fa-plus-circle"></i></span>
                    <span title="Hide Details" ng-if="return.showDetails == true"><i
                            class="fa fa-minus-circle"></i></span>
                </div>
                <div style="margin-left: 30px;">
                    <h4 class="text-primary" style="margin-bottom: 5px;">
                        <a href="" ng-click="showReturn(return)">{{return.customer.id}}</a>
                    </h4>
                    <span class="text-muted">{{return.returnDate}}</span>
                </div>
            </div>
            <div class="col-sm-3" style="padding-top: 10px; text-align: center">
                <p>Return reason</p>
                <span class="text-muted">{{return.reason}}</span>
                </h4>
            </div>
        </div>
        <div ng-if="return.showDetails" class="order-details">
            <div>
                <h3 style="padding-bottom: 10px;border-bottom: 1px solid #EEE;">Items</h3>
                <table class="table table-striped table-invoice">
                    <thead>
                    <tr>
                        <th>Item</th>
                        <th style="text-align: center">Quantity</th>
                        <th>sku</th>
                    </tr>
                    </thead>

                    <tbody>
                    <tr ng-show="return.details.length == 0">
                        <td colspan="4" style="text-align: left">
                            <span>No items in the shopping cart.</span>
                        </td>
                    </tr>
                    <tr ng-repeat="item in return.details">
                        <td style="vertical-align: middle;">
                            <div class="text-primary">
                                <strong class="mr20">{{item.product.name}}</strong>
                            </div>
                        </td>
                        <td align="center" style="text-align: center; vertical-align: middle;">
                            <span>{{item.quantity}}</span>
                        </td>
                        <td>
                            <span>{{item.product.sku}}</span>
                        </td>
                    </tr>
                    </tbody>
                </table>
            </div>
            <br/>
            <br/>
        </div>
    </div>
</div>