<div>
    <div>
        <h4 class="text-primary text-center">
            <span class="mr20">Product Orders: {{productOrdersAmount | currency:"&#x20b9; ":0}}</span>
            <span class="mr20">Sample Orders: {{sampleOrdersAmount | currency:"&#x20b9; ":0}}</span>

        </h4>
        <br>
        <div>
            <div class="row">
                <div class="col-sm-4">
                    <div class="row">
                        <div class="col-sm-4" style="padding-top: 10px; text-align: right">
                            PO Number:
                        </div>
                        <div class="col-sm-8">
                            <input type="text" class="form-control input-sm"
                                   ng-model="filters.poNumber"
                                    ng-enter="getCustomerOrders()">
                        </div>
                    </div>
                </div>
                <div class="col-sm-4">
                    <div class="row">
                        <div class="col-sm-4" style="padding-top: 10px; text-align: right">
                            Order Type:
                        </div>
                        <div class="col-sm-8">
                            <ui-select ng-model="filters.orderType" on-select="getCustomerOrders()" theme="bootstrap" style="width:100%">
                                <ui-select-match allow-clear="true" placeholder="Type">{{$select.selected}}</ui-select-match>
                                <ui-select-choices repeat="orderType in ['ALL', 'PRODUCT', 'SAMPLE', 'QUESTIONPAPER'] | filter: $select.search">
                                    <div ng-bind-html="orderType | highlight: $select.search"></div>
                                </ui-select-choices>
                            </ui-select>
                        </div>
                    </div>
                </div>
                <div class="col-sm-4">
                    <div class="row">
                        <div class="col-sm-4" style="padding-top: 10px; text-align: right">
                            Status:
                        </div>
                        <div class="col-sm-8">
                            <ui-select ng-model="filters.status" on-select="getCustomerOrders()" theme="bootstrap" style="width:100%">
                                <ui-select-match allow-clear="true" placeholder="Status">{{$select.selected}}</ui-select-match>
                                <ui-select-choices repeat="status in ['ALL','NEW', 'APPROVED', 'PROCESSING','PROCESSED','PARTIALLYSHIPPED','SHIPPED', 'CANCELLED'] | filter: $select.search">
                                    <div ng-bind-html="status | highlight: $select.search"></div>
                                </ui-select-choices>
                            </ui-select>
                        </div>
                    </div>
                </div>
            </div>
            <div class="row" style="text-align: right">
                <br>
                <div clas="btn-group">
                    <button class='btn btn-md btn-default' ng-click='previousPage()'  ng-disabled="orders.first">
                        <i class="fa fa-chevron-left"></i>
                    </button>
                    <button class='btn btn-md btn-default' ng-click='nextPage()' ng-disabled="orders.last">
                        <i class="fa fa-chevron-right"></i>
                    </button>
                    <div ng-if="orders.totalElements > 0">
                        <span>{{pageable.page}} of {{orders.totalPages}}</span>
                    </div>
                </div>
            </div>
        </div>
        <br>
    </div>

    <div ng-if="orders.totalElements == 0 || loading == true" style="padding: 10px;">
        <span ng-hide="loading">There are no orders</span>
        <span ng-show="loading">
            <img src="app/assets/images/loaders/loader19.gif" class="mr5">Loading customer orders...
        </span>
    </div>
    <div ng-repeat="order in orders.content" class="customer-order">
        <div class="row">
            <div class="col-sm-4">
                <div ng-click="toggleDetails(order)" style="cursor: pointer;float: left;margin-top: 25px;">
                    <span title="Show Details" ng-if="order.showDetails == false"><i class="fa fa-plus-circle"></i></span>
                    <span title="Hide Details" ng-if="order.showDetails == true"><i class="fa fa-minus-circle"></i></span>
                </div>
                <div style="margin-left: 30px;">
                    <h4 class="text-primary" style="display: inline-block; margin-bottom: 5px;">
                        <a class="mr5" href="" ng-click="showOrder(order)">{{order.orderNumber}}</a>
                        <h5 style="display: inline-block">(PO Number: {{order.poNumber}})</h5>
                    </h4>
                    <br/>
                    <span class="text-muted">{{order.orderedDate}}</span>

                </div>
            </div>
            <div class="col-sm-3" style="padding-top: 10px; text-align: center">
                <span class="text-muted">Order type</span>
                <h4 style="margin-top: 5px; display: block; margin-left: auto;margin-right: auto;" class="status-label"
                    ng-class="{'order-type-product': order.orderType == 'PRODUCT',
                        'order-type-sample': order.orderType == 'SAMPLE',
                        'order-type-questionpaper': order.orderType == 'QUESTIONPAPER'}" style="margin-left: 10px;">
                {{order.orderType}}
                </h4>
            </div>
            <div class="col-sm-3" style="padding-top: 10px; text-align: center">
                <span class="text-muted mr5">Status</span>
                <span class="text-muted" ng-if="order.status == 'SHIPPED'">(LR Number(s):
                    <img ng-if="loadingLrNumbers == true" src="app/assets/images/loaders/loader2.gif" class="mr5">{{order.lrNumbers}})</span>

                <h4 style="margin-top: 5px; display: block; margin-left: auto;margin-right: auto;" class="status-label"
                    ng-class="{
                        'status-label-new': order.status == 'NEW',
                        'status-label-approved': order.status == 'APPROVED',
                        'status-label-processed': order.status == 'PROCESSED',
                        'status-label-partiallyshipped': order.status == 'PARTIALLYSHIPPED',
                        'status-label-canceled': order.status == 'CANCELLED',
                        'status-label-processing': order.status == 'PROCESSING',
                        'status-label-shipped': order.status == 'SHIPPED'
                     }">{{order.status}}</h4>
            </div>
            <div class="col-sm-2" style="padding-top: 10px;text-align: right">
                <span class="text-muted">Amount</span>
                <h4 style="margin-top: 5px">{{order.orderTotal | currency:"&#x20b9; ":0}}</h4>
            </div>
        </div>
        <div ng-if="order.showDetails" class="order-details">
            <div>
                <h3 style="padding-bottom: 10px;border-bottom: 1px solid #EEE;">Items</h3>
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
                    <tr ng-repeat="item in order.details"
                        ng-class="{'pending-items-row': ((item.quantity - item.quantityProcessed) > 0)}">
                        <td style="vertical-align: middle;">
                            <div class="text-primary">
                                <strong class="mr20">{{item.product.name}}</strong>
                            </div>
                        </td>
                        <td align="center" style="text-align: center; vertical-align: middle;">
                            <span>{{item.quantity}}</span>
                        </td>
                        <td style="vertical-align: middle;">{{item.unitPrice | currency:"&#x20b9; ":0}}</td>
                        <td style="vertical-align: middle;">{{(item.quantity * item.unitPrice) | currency:"&#x20b9; ":0}}</td>
                    </tr>
                    </tbody>
                </table>


                <table class="table table-total" style="float: none">
                    <tbody>
                    <!--
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
                    -->
                    <tr>
                        <td><strong>TOTAL :</strong></td>
                        <td>{{order.orderTotal | currency:"&#x20b9; ":0}}</td>
                    </tr>
                    </tbody>
                </table>
            </div>

            <br/>
            <div style="margin-top: 30px;margin-bottom: 50px;">
                <h3 style="padding-bottom: 10px;border-bottom: 1px solid #EEE;">Shipments</h3>

                <table class="table table-striped">
                    <thead>
                    <tr>
                        <th>Status</th>
                        <th>Invoice number</th>
                        <th>Invoice Amount</th>
                        <th>Shipper</th>
                        <th>Tracking Number</th>
                        <th>LR Number</th>
                        <th>Vehicle Number</th>
                        <th>Driver</th>
                        <th>Shipped Date</th>
                        <th>Shipped By</th>
                    </tr>
                    </thead>

                    <tbody>
                    <tr ng-if="order.orderShipments == null || order.orderShipments.length == 0">
                        <td colspan="10">
                            No shipments
                        </td>
                    </tr>
                    <tr ng-repeat="shipment in order.orderShipments">
                        <td>
                            <span ng-class="{'shipment-pending': shipment.status == 'PENDING',
                                    'shipment-shipped': shipment.status == 'SHIPPED'}">{{shipment.status}}</span>
                        </td>
                        <td>{{shipment.invoiceNumber}}</td>
                        <td>{{shipment.invoiceAmount | currency:"&#x20b9; ":0}}</td>
                        <td>{{shipment.consignment.shipper.name}}</td>
                        <td>{{shipment.consignment.number}}</td>
                        <td>{{shipment.consignment.confirmationNumber}}</td>
                        <td>{{shipment.consignment.vehicle.number}}</td>
                        <td>{{shipment.consignment.driver.firstName}}</td>
                        <td>{{shipment.consignment.shippedDate}}</td>
                        <td>{{shipment.consignment.shippedBy.firstName}}</td>
                    </tr>
                    </tbody>
                </table>
            </div>

            <br/>
            <div style="margin-top: 30px;margin-bottom: 50px;">
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
                    <tr ng-repeat="item in order.orderHistory">
                        <td>{{item.newStatus}}</td>
                        <td>{{item.modifiedBy.firstName}}</td>
                        <td>{{item.modifiedDate}}</td>
                    </tr>
                    </tbody>
                </table>
            </div>
        </div>
    </div>
</div>