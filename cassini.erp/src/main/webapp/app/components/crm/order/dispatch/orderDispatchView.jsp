<div>
    <div class="pull-right" style="margin-top:10px">
        <span style="font-size: 20px; padding-right:10px">Status:</span>
        <span class="text-success" style="font-size: 28px">{{order.status}}</span>
    </div>
    <button  class="btn btn-primary"
             ng-disabled="order.status != 'PROCESSED' || dispatched == true"
             authorization permission="permission.crm.order.dispatch"
             ng-click="dispatchFull()">Dispatch</button>
</div>
<br/><br/>
<div ng-if="loading">
    <br/>
    <span style="font-size: 18px;">
        <img src="app/assets/images/loaders/loader6.gif" class="mr5">Loading order details...
    </span>
    <br/>
</div>

<div>
    <h3 style="padding-bottom: 10px;border-bottom: 1px solid #EEE;">Customer</h3>
    <div ng-show="order.customer != null">
        <h4>Name: <span class="text-success">{{order.customer.name}}</span></h4>
        <h4>Region: <span class="text-success">{{order.customer.salesRegion.name}}</span></h4>
        <h4>Sales Rep: <span class="text-success">{{order.customer.salesRep.firstName}}</span></h4>
    </div>
</div>
<br/><br/>

<div>
    <h3 style="padding-bottom: 10px;border-bottom: 1px solid #EEE;">Details</h3>
    <table class="table table-striped table-invoice">
        <thead>
        <tr>
            <th>Item</th>
            <th style="text-align: center">Quantity</th>
            <th>Unit Price</th>
            <th>Total Price</th>
        </tr>
        </thead>

        <tbody>
        <tr ng-show="order.details.length == 0">
            <td colspan="4" style="text-align: left">
                <span>No items in the shopping cart.</span>
            </td>
        </tr>
        <tr ng-repeat="item in order.details">
            <td style="vertical-align: middle;">
                <div class="text-primary">
                    <strong>{{item.product.name}}</strong>
                </div>
            </td>
            <td align="center" style="text-align: center; vertical-align: middle;">
                <span>{{item.quantity}}</span>
            </td>
            <td style="vertical-align: middle;">{{item.product.unitPrice | currency:"&#x20b9; ":0}}</td>
            <td style="vertical-align: middle;">{{(item.quantity * item.product.unitPrice) | currency:"&#x20b9; ":0}}</td>
        </tr>
        </tbody>
    </table>


    <table class="table table-total" style="float: none">
        <tbody>
        <tr>
            <td><strong>Sub Total :</strong></td>
            <td>{{order.orderTotal | currency:"&#x20b9; ":0}}</td>
        </tr>
        <tr>
            <td><strong>VAT (14.5%) :</strong></td>
            <td>{{order.orderTotal*0.145 | currency:"&#x20b9; ":0}}</td>
        </tr>
        <tr>
            <td><strong>TOTAL :</strong></td>
            <td>{{order.orderTotal*1.145 | currency:"&#x20b9; ":0}}</td>
        </tr>
        </tbody>
    </table>
</div>
<br/>
<div style="margin-top: 30px">
    <h3 style="padding-bottom: 10px;border-bottom: 1px solid #EEE;">History</h3>

    <table class="table table-striped">
        <thead>
            <tr>
                <th>Status</th>
                <th>Updated By</th>
                <th>Timestamp</th>
            </tr>
        </thead>

        <tbody>
            <tr ng-repeat="item in orderHistory">
                <td>{{item.newStatus}}</td>
                <td>{{item.modifiedBy.firstName}}</td>
                <td>{{item.modifiedDate}}</td>
            </tr>
        </tbody>
    </table>
</div>