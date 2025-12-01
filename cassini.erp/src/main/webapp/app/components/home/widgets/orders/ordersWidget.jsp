
<div class="panel panel-default panel-alt widget-messaging" style="height: 400px">
    <div class="panel-heading" style="background-color: #E4E7EA; padding:0; height: 56px">
        <div class="row">
            <div class="panel-title col-xs-12 col-sm-12 col-md-3 col-lg-3" style="font-size:15px; padding: 20px 0 0 20px">
                {{headerText}}
            </div>
            <div class="col-xs-12 col-sm-12 col-md-6 col-lg-6" style="padding-top: 12px;">
                <div class="btn-group" style="margin-top: 3px;">
                    <button class="btn btn-xs btn-info" ng-click="setMode('new')">
                        <span class="pull-right badge"
                              style="margin: 3px 0 0 10px; background-color:#D43F3A">{{ordersNotification.newOrders.totalElements}}</span>
                        <span>New</span>
                    </button>
                    <button class="btn btn-xs btn-success" ng-click="setMode('approved')">
                        <span class="pull-right badge"
                              style="margin: 3px 0 0 10px; background-color:#D43F3A">{{ordersNotification.approvedOrders.totalElements}}</span>
                        <span>Approved</span>
                    </button>
                    <button class="btn btn-xs btn-warning" ng-click="setMode('pending')">
                        <span class="pull-right badge"
                              style="margin: 3px 0 0 10px; background-color:#D43F3A">{{ordersNotification.pendingOrders.totalElements}}</span>
                        <span>Pending</span>
                    </button>
                </div>
            </div>
            <div class="col-xs-12 col-sm-12 col-md-3 col-lg-3" style="padding: 10px 25px 0 0; text-align: right">
                <button style="width: 128px;margin-top: 3px;" class="btn btn-xs btn-primary" ng-if="ordersNotification.newOrders.length > 0"
                        ng-click="approveAll()" ng-show="mode == 'new'">Approve All</button>
            </div>
        </div>
    </div>
    <div class="panel-body">
        <div style="max-height: 400px; overflow: auto;">
            <ul ng-if="mode == 'new'" style="max-height: 400px; overflow: auto; padding: 5px;height: 330px;">
                <li ng-if="ordersNotification.newOrders.totalElements == 0">
                    <span>No new orders</span>
                </li>
                <li ng-repeat="order in ordersNotification.newOrders.content"
                    class="new-customer-order" style="padding: 10px; border-left: 10px solid #5BC0DE">
                    <div class="row">
                        <div class="col-sm-2 text-center" style="padding-top: 12px">
                            <h4 class="text-primary" style="margin:0">
                                <span ng-if="order.starred == true"><i class="fa fa-star" style="color: #ffa331"></i></span>
                                <a href="" ng-click="openOrder(order)">{{order.orderNumber}}</a>
                            </h4>
                            <span class="text-muted">{{lapsedTime(order.orderedDate)}}</span>
                        </div>
                        <div class="col-sm-4">
                            <div class="text-primary" style="font-size: 16px; margin-bottom: 5px">
                                <a href="" ng-click="showCustomerDetails(order.customer)">{{order.customer.name}}</a>
                            </div>
                            <small><strong class="mr5">Region:</strong> {{order.customer.salesRegion.name}} <span class="mr10"></span>
                                <strong class="mr5">Sales Rep:</strong>
                                <a href="" ng-click="showSalesRep(order.customer.salesRep)">{{order.customer.salesRep.firstName}}</a></small>
                            <br/><small class="mr20"><strong class="mr5">Ordered Date:</strong> {{order.history[0].modifiedDate}}</small>

                        </div>
                        <div class="col-sm-2" style="text-align: center;padding-top: 10px;">
                            <span class="label"
                                  ng-class="{'label-product': order.orderType == 'PRODUCT',
                                    'label-sample': order.orderType == 'SAMPLE'}">{{order.orderType}}</span>
                            <h4 style="margin: 0;">{{order.orderTotal | currency:"&#x20b9; ":0}}</h4>
                        </div>
                        <div class="col-sm-4" style="text-align: right;padding-top: 20px;" ng-if="order.onhold == true">
                            <span ng-if="order.onhold == true" class="blink" style="margin-left: 50px;font-size: 15px; color: red">ORDER IS ON HOLD</span>
                        </div>
                        <div class="col-sm-4" style="text-align: right;padding-top: 15px;" ng-if="order.onhold == false">
                            <button class="btn btn-xs btn-primary" ng-click="approveOrder(order)">Approve</button>
                            <button class="btn btn-xs btn-danger" ng-click="cancelOrder(order)">Cancel</button>
                            <br/>
                            <span class="pull-right" ng-if="order.lowInventory == true" style="color: red;margin-right: 20px;">Low inventory</span>
                        </div>
                    </div>
                </li>
                <li ng-if="ordersNotification.newOrders.totalElements > 10">
                    <a href="" ng-click="showMore('NEW')"><h4 class="text-primary text-center">Show More</h4></a>
                </li>
            </ul>
            <ul ng-if="mode == 'approved'" style="max-height: 400px; overflow: auto; padding: 5px;height: 330px;">
                <li ng-if="ordersNotification.approvedOrders.totalElements == 0">
                    <span>No approved orders</span>
                </li>
                <li ng-repeat="order in ordersNotification.approvedOrders.content"
                    class="new-customer-order" style="padding: 10px; border-left: 10px solid #1CAF9A">
                    <div class="row">
                        <div class="col-sm-2 text-center" style="padding-top: 12px">
                            <h4 class="text-primary" style="margin:0">
                                <span ng-if="order.starred == true"><i class="fa fa-star" style="color: #ffa331"></i></span>
                                <a href="" ng-click="openOrder(order)">{{order.orderNumber}}</a>
                            </h4>
                            <span class="text-muted">{{lapsedTime(order.orderedDate)}}</span>
                        </div>
                        <div class="col-sm-7">
                            <div class="text-primary" style="font-size: 16px; margin-bottom: 5px">
                                <a href="" ng-click="showCustomerDetails(order.customer)">{{order.customer.name}}</a>
                            </div>
                            <small><strong class="mr5">Region:</strong> {{order.customer.salesRegion.name}} <span class="mr10"></span>
                                <strong class="mr5">Sales Rep:</strong>
                                <a href="" ng-click="showSalesRep(order.customer.salesRep)">{{order.customer.salesRep.firstName}}</a></small>
                            <br/><small class="mr20"><strong class="mr5">Ordered Date:</strong> {{order.history[0].modifiedDate}}</small>

                        </div>
                        <div class="col-sm-3" style="text-align: center;padding-top: 10px;">
                            <span class="label"
                                  ng-class="{'label-product': order.orderType == 'PRODUCT',
                                    'label-sample': order.orderType == 'SAMPLE'}">{{order.orderType}}</span>
                            <h4 style="margin: 0;">{{order.orderTotal | currency:"&#x20b9; ":0}}</h4>
                        </div>
                    </div>
                    <div class="text-center">
                        <span ng-if="order.onhold == true" class="blink" style="margin-left: 50px;font-size: 15px; color: red">ORDER IS ON HOLD</span>
                    </div>
                </li>
                <li ng-if="ordersNotification.approvedOrders.totalElements > 10">
                    <a href="" ng-click="showMore('APPROVED')"><h4 class="text-primary text-center">Show More</h4></a>
                </li>
            </ul>
            <ul ng-if="mode == 'pending'" style="max-height: 400px; overflow: auto; padding: 5px;height: 330px;">
                <li ng-if="ordersNotification.pendingOrders.totalElements == 0">
                    <span>No pending orders</span>
                </li>
                <li ng-repeat="order in ordersNotification.pendingOrders.content"
                    class="new-customer-order" style="padding: 10px; border-left: 10px solid #F0AD4E">
                    <div class="row">
                        <div class="col-sm-2 text-center" style="padding-top: 12px">
                            <h4 class="text-primary" style="margin:0">
                                <span ng-if="order.starred == true"><i class="fa fa-star" style="color: #ffa331"></i></span>
                                <a href="" ng-click="openOrder(order)">{{order.orderNumber}}</a>
                            </h4>
                            <span class="text-muted">{{lapsedTime(order.orderedDate)}}</span>
                        </div>
                        <div class="col-sm-7">
                            <div class="text-primary" style="font-size: 16px; margin-bottom: 5px">
                                <a href="" ng-click="showCustomerDetails(order.customer)">{{order.customer.name}}</a>
                            </div>
                            <small><strong class="mr5">Region:</strong> {{order.customer.salesRegion.name}} <span class="mr10"></span>
                                <strong class="mr5">Sales Rep:</strong>
                                <a href="" ng-click="showSalesRep(order.customer.salesRep)">{{order.customer.salesRep.firstName}}</a></small>
                            <br/><small class="mr20"><strong class="mr5">Ordered Date:</strong> {{order.history[0].modifiedDate}}</small>

                        </div>
                        <div class="col-sm-3" style="text-align: center;padding-top: 10px;">
                            <span class="label"
                                  ng-class="{'label-product': order.orderType == 'PRODUCT',
                                    'label-sample': order.orderType == 'SAMPLE'}">{{order.orderType}}</span>
                            <h4 style="margin: 0;">{{order.orderTotal | currency:"&#x20b9; ":0}}</h4>
                        </div>
                    </div>
                </li>
                <li ng-if="ordersNotification.pendingOrders.totalElements > 10">
                    <a href="" ng-click="showMore('PROCESSED')"><h4 class="text-primary text-center">Show More</h4></a>
                </li>
            </ul>
        </div>
    </div><!-- panel-body -->
</div><!-- panel -->