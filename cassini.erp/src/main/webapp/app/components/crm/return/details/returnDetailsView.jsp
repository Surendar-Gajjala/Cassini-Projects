<button class="btn btn-default" onclick="window.history.back()">Go Back</button>
<hr>
<div class="row" style="margin-left: 2px;margin-right: 2px;">
    <div class="col-sm-2 styled-panel">
        <div>
            <h4 class="section-title text-primary" style="margin-top: 0">Return Date:</h4>
            <h5>{{return.returnDate}}</h5>
        </div>
        <br/>

        <div>
            <h4 class="section-title text-primary">Customer:</h4>

            <div>
                <h5>Name: <span class="text-success">{{return.customer.name}}</span></h5>
                <h5>Region: <span class="text-success">{{return.customer.salesRegion.name}}</span></h5>
                <h5>Sales Rep: <span class="text-success">{{return.customer.salesRep.firstName}}</span></h5>
            </div>
        </div>
        <br/>

        <div>
            <h4 class="section-title text-primary">Reason:</h4>
            <h5>{{return.reason}}</h5>
        </div>
    </div>
    <div class="col-sm-10">
        <div class="table-responsive">
            <h4 class="section-title text-primary">Items:</h4>
            <table class="table table-striped">
                <thead>
                <tr>
                    <th>SKU</th>
                    <th>Item</th>
                    <th style="width:120px;text-align: center">Quantity</th>
                </tr>
                </thead>

                <tbody>
                <tr ng-repeat="item in return.details">
                    <td>
                        <div class="text-primary"><strong>{{item.product.sku}}</strong></div>
                    </td>
                    <td style="vertical-align: middle;">
                        <div class="text-primary"><strong>{{item.product.name}}</strong></div>
                    </td>
                    <td align="center" style="width:120px; text-align: center; vertical-align: middle;">
                        <span>{{item.quantity}}</span>
                    </td>
                </tr>
                </tbody>
            </table>
        </div>
    </div>
</div>