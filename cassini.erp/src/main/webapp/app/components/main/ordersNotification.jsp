<li ng-if="hasRole('Administrator') && ordersNotification.newOrders.totalElements > 0">
    <div class="btn-group" dropdown>
        <button title="New Orders" class="btn btn-default dropdown-toggle tp-icon" dropdown-toggle>
            <i class="fa fa-list"></i>
            <span class="badge new-orders-badge">{{ordersNotification.newOrders.totalElements}}</span>
        </button>
        <div class="widget-messaging dropdown-menu dropdown-menu-head pull-right"
             style="width: 600px;">
            <div class="panel-heading"
                 style="background-color: #E4E7EA;border: 1px solid #D7D7D7; padding:0; height: 56px">
                <div class="row">
                    <div class="panel-title col-sm-8" style="font-size:15px; padding: 20px 0 0 20px">New Orders</div>
                    <div class="col-sm-4" style="padding: 10px 26px 0 0; text-align: right"
                         ng-if="ordersNotification.newOrders.totalElements > 0">
                        <button style="width: 128px;margin-top: 5px;margin-right: 10px;"
                                class="btn btn-xs btn-primary" ng-click="approveAll()">Approve All
                        </button>
                    </div>
                </div>
            </div>

            <div style="max-height: 400px; overflow: auto;border-bottom: 1px solid #DDD;">
                <ul class="dropdown-list gen-list new-orders" style="padding: 10px">
                    <li ng-if="ordersNotification.newOrders.totalElements == 0" style="padding-left: 10px">
                        <span>No new orders</span>
                    </li>
                    <li ng-repeat="order in ordersNotification.newOrders.content" class="order-item"
                        style="margin-bottom:10px;border-left: 10px solid #5BC0DE !important;">
                        <div class="row" style="padding: 0px 10px;">
                            <div class="col-sm-2 text-center" style="padding-top: 0px">
                                <h4 class="text-primary" style="margin:-3px;">
                                    <span ng-if="order.starred == true"><i class="fa fa-star"
                                                                           style="color: #ffa331"></i></span>
                                    <a href="" ng-click="openOrder(order)" style="color: #428BCA; font-size: 16px;">{{order.orderNumber}}</a>
                                </h4>
                                <span class="text-muted">{{lapsedTime(order.orderedDate)}}</span>
                            </div>
                            <div class="col-sm-4">
                                <h4 class="sender" style="margin-bottom: 3px">
                                    <a style="color:#428BCA !important;" href=""
                                       ng-click="showCustomerDetails(order.customer)">{{order.customer.name}}</a>
                                </h4>
                                <small>Region: <strong class="mr10">{{order.customer.salesRegion.name}}</strong>
                                    Sales Rep: <strong>{{order.customer.salesRep.firstName}}</strong></small>

                            </div>
                            <div class="col-sm-2" style="text-align: right">
                                <span class="label"
                                      ng-class="{'label-product': order.orderType == 'PRODUCT',
                                        'label-sample': order.orderType == 'SAMPLE'}">{{order.orderType}}</span>
                                <h5 class="pull-right" style="margin: 5px 0">{{order.orderTotal | currency:"&#x20b9;
                                    ":0}}</h5>
                            </div>
                            <div class="col-sm-4" style="text-align: right" ng-if="order.onhold == true">
                                <span ng-if="order.onhold == true" class="blink"
                                      style="margin-left: 50px;font-size: 12px; color: red">ORDER IS ON HOLD</span>
                            </div>
                            <div class="col-sm-4" style="text-align: right" ng-if="order.onhold == false">
                                <button class="btn btn-xs btn-primary" ng-click="approveOrder(order)"
                                        ng-hide="order.status == 'APPROVED'">Approve
                                </button>
                                <button class="btn btn-xs btn-danger" ng-click="cancelOrder(order)">Cancel</button>
                            </div>
                        </div>
                    </li>
                    <li style="display:none"></li>
                    <li ng-if="ordersNotification.newOrders.totalElements > 10">
                        <a href="" ng-click="showMore('NEW')"><h4 class="text-primary text-center">Show More</h4></a>
                    </li>
                </ul>
            </div>
        </div>
    </div>
</li>
<li ng-if="ordersNotification.approvedOrders.totalElements > 0">
    <div class="btn-group" dropdown>
        <button title="Approved Orders" class="btn btn-default dropdown-toggle tp-icon" dropdown-toggle>
            <i class="fa fa-list"></i>
            <span class="badge approved-orders-badge">{{ordersNotification.approvedOrders.totalElements}}</span>
        </button>
        <div class="widget-messaging dropdown-menu dropdown-menu-head pull-right"
             style="width: 600px;">
            <div class="panel-heading"
                 style="background-color: #E4E7EA;border: 1px solid #D7D7D7; padding:0; height: 56px">
                <div class="row">
                    <div class="panel-title col-sm-8" style="font-size:15px; padding: 20px 0 0 20px">Approved Orders
                    </div>
                </div>
            </div>

            <div style="max-height: 400px; overflow: auto;border-bottom: 1px solid #DDD;">
                <ul class="dropdown-list gen-list new-orders" style="padding: 10px">
                    <li ng-if="ordersNotification.approvedOrders.totalElements == 0" style="padding-left: 10px">
                        <span>No approved orders</span>
                    </li>
                    <li ng-repeat="order in ordersNotification.approvedOrders.content" class="order-item"
                        style="margin-bottom:10px;border-left: 10px solid #1CAF9A !important;">
                        <div class="row" style="padding: 0px 10px;">
                            <div class="col-sm-3 text-center" style="padding-top: 0px">
                                <h4 class="text-primary" style="margin:-3px;">
                                    <span ng-if="order.starred == true"><i class="fa fa-star"
                                                                           style="color: #ffa331"></i></span>
                                    <a href="" ng-click="openOrder(order)" style="color: #428BCA; font-size: 16px;">{{order.orderNumber}}</a>
                                </h4>
                                <span class="text-muted">{{lapsedTime(order.orderedDate)}}</span>
                            </div>
                            <div class="col-sm-7">
                                <h4 class="sender" style="margin-bottom: 3px">
                                    <a style="color:#428BCA !important;" href=""
                                       ng-click="showCustomerDetails(order.customer)">{{order.customer.name}}</a>
                                </h4>
                                <small>Region: <strong class="mr10">{{order.customer.salesRegion.name}}</strong>
                                    Sales Rep: <strong>{{order.customer.salesRep.firstName}}</strong></small>

                            </div>
                            <div class="col-sm-2" style="text-align: right">
                                <span class="label"
                                      ng-class="{'label-product': order.orderType == 'PRODUCT',
                                        'label-sample': order.orderType == 'SAMPLE'}">{{order.orderType}}</span>
                                <h5 class="pull-right" style="margin: 5px 0">{{order.orderTotal | currency:"&#x20b9;
                                    ":0}}</h5>
                            </div>
                        </div>

                        <div class="text-center">
                            <span ng-if="order.onhold == true" class="blink"
                                  style="margin-left: 50px;font-size: 15px; color: red">ORDER IS ON HOLD</span>
                        </div>
                    </li>
                    <li style="display:none"></li>
                    <li ng-if="ordersNotification.approvedOrders.totalElements > 10">
                        <a href="" ng-click="showMore('APPROVED')"><h4 class="text-primary text-center">Show More</h4>
                        </a>
                    </li>
                </ul>
            </div>
        </div>
    </div>
</li>

<li ng-if="ordersNotification.pendingOrders.totalElements > 0">
    <div class="btn-group" dropdown>
        <button title="Pending Orders" class="btn btn-default dropdown-toggle tp-icon" dropdown-toggle>
            <i class="fa fa-list"></i>
            <span class="badge pending-orders-badge">{{ordersNotification.pendingOrders.totalElements}}</span>
        </button>
        <div class="widget-messaging dropdown-menu dropdown-menu-head pull-right"
             style="width: 600px;">
            <div class="panel-heading"
                 style="background-color: #E4E7EA;border: 1px solid #D7D7D7; padding:0; height: 56px">
                <div class="row">
                    <div class="panel-title col-sm-8" style="font-size:15px; padding: 20px 0 0 20px">Pending Orders
                    </div>
                </div>
            </div>

            <div style="max-height: 400px; overflow: auto;border-bottom: 1px solid #DDD;">
                <ul class="dropdown-list gen-list new-orders" style="padding: 10px">
                    <li ng-if="ordersNotification.pendingOrders.totalElements == 0" style="padding-left: 10px">
                        <span>No pending orders</span>
                    </li>
                    <li ng-repeat="order in ordersNotification.pendingOrders.content" class="order-item"
                        style="margin-bottom:10px;border-left: 10px solid #F0AD4E !important;">
                        <div class="row" style="padding: 0px 10px;">
                            <div class="col-sm-3 text-center" style="padding-top: 0px">
                                <h4 class="text-primary" style="margin:-3px;">
                                    <span ng-if="order.starred == true"><i class="fa fa-star"
                                                                           style="color: #ffa331"></i></span>
                                    <a href="" ng-click="openOrder(order)" style="color: #428BCA; font-size: 16px;">{{order.orderNumber}}</a>
                                </h4>
                                <span class="text-muted">{{lapsedTime(order.orderedDate)}}</span>
                            </div>
                            <div class="col-sm-7">
                                <h4 class="sender" style="margin-bottom: 3px">
                                    <a style="color:#428BCA !important;" href=""
                                       ng-click="showCustomerDetails(order.customer)">{{order.customer.name}}</a>
                                </h4>
                                <small>Region: <strong class="mr10">{{order.customer.salesRegion.name}}</strong>
                                    Sales Rep: <strong>{{order.customer.salesRep.firstName}}</strong></small>

                            </div>
                            <div class="col-sm-2" style="text-align: center">
                                <span class="label"
                                      ng-class="{'label-product': order.orderType == 'PRODUCT',
                                        'label-sample': order.orderType == 'SAMPLE'}">{{order.orderType}}</span>
                                <h5 class="pull-right" style="margin: 5px 0">{{order.orderTotal | currency:"&#x20b9;
                                    ":0}}</h5>
                            </div>
                        </div>
                    </li>
                    <li style="display:none"></li>
                    <li ng-if="ordersNotification.pendingOrders.totalElements > 10">
                        <a href="" ng-click="showMore('PROCESSED')"><h4 class="text-primary text-center">Show More</h4>
                        </a>
                    </li>
                </ul>
            </div>
        </div>
    </div>
</li>

<li ng-if="ordersNotification.partiallyShippedOrders.totalElements > 0">
    <div class="btn-group" dropdown>
        <button title="Partially Shipped Orders" class="btn btn-default dropdown-toggle tp-icon" dropdown-toggle>
            <i class="fa fa-list"></i>
            <span class="badge partial-orders-badge">{{ordersNotification.partiallyShippedOrders.totalElements}}</span>
        </button>
        <div class="widget-messaging dropdown-menu dropdown-menu-head pull-right"
             style="width: 600px;">
            <div class="panel-heading"
                 style="background-color: #E4E7EA;border: 1px solid #D7D7D7; padding:0; height: 56px">
                <div class="row">
                    <div class="panel-title col-sm-8" style="font-size:15px; padding: 20px 0 0 20px">Partially Shipped
                        Orders
                    </div>
                </div>
            </div>

            <div style="max-height: 400px; overflow: auto;border-bottom: 1px solid #DDD;">
                <ul class="dropdown-list gen-list new-orders" style="padding: 10px">
                    <li ng-if="ordersNotification.partiallyShippedOrders.totalElements == 0" style="padding-left: 10px">
                        <span>No orders</span>
                    </li>
                    <li ng-repeat="order in ordersNotification.partiallyShippedOrders.content" class="order-item"
                        style="margin-bottom:10px;border-left: 10px solid #815F32 !important;">
                        <div class="row" style="padding: 0px 10px;">
                            <div class="col-sm-3 text-center" style="padding-top: 0px">
                                <h4 class="text-primary" style="margin:-3px;">
                                    <span ng-if="order.starred == true"><i class="fa fa-star"
                                                                           style="color: #ffa331"></i></span>
                                    <a href="" ng-click="openOrder(order)" style="color: #428BCA; font-size: 16px;">{{order.orderNumber}}</a>
                                </h4>
                                <span class="text-muted">{{lapsedTime(order.orderedDate)}}</span>
                            </div>
                            <div class="col-sm-7">
                                <h4 class="sender" style="margin-bottom: 3px">
                                    <a style="color:#428BCA !important;" href=""
                                       ng-click="showCustomerDetails(order.customer)">{{order.customer.name}}</a>
                                </h4>
                                <small>Region: <strong class="mr10">{{order.customer.salesRegion.name}}</strong>
                                    Sales Rep: <strong>{{order.customer.salesRep.firstName}}</strong></small>

                            </div>
                            <div class="col-sm-2" style="text-align: center">
                                <span class="label"
                                      ng-class="{'label-product': order.orderType == 'PRODUCT',
                                        'label-sample': order.orderType == 'SAMPLE'}">{{order.orderType}}</span>
                                <h5 class="pull-right" style="margin: 5px 0">{{order.orderTotal | currency:"&#x20b9;
                                    ":0}}</h5>
                            </div>
                        </div>
                    </li>
                    <li style="display:none"></li>
                    <li ng-if="ordersNotification.partiallyShippedOrders.totalElements > 10">
                        <a href="" ng-click="showMore('PARTIALLYSHIPPED')"><h4 class="text-primary text-center">Show
                            More</h4></a>
                    </li>
                </ul>
            </div>
        </div>
    </div>
</li>


<li ng-if="ordersNotification.lateApprovedOrders.totalElements > 0">
    <div class="btn-group" dropdown>
        <button title="Approved Pending Orders" class="btn btn-default dropdown-toggle tp-icon" dropdown-toggle>
            <i class="fa fa-list"></i>
            <span class="badge approved-pending-orders-badge">{{ordersNotification.lateApprovedOrders.totalElements}}</span>
        </button>
        <div class="widget-messaging dropdown-menu dropdown-menu-head pull-right"
             style="width: 600px;">
            <div class="panel-heading"
                 style="background-color: #E4E7EA;border: 1px solid #D7D7D7; padding:0; height: 56px">
                <div class="row">
                    <div class="panel-title col-sm-8" style="font-size:15px; padding: 20px 0 0 20px">Approved Pending
                        Orders
                    </div>
                </div>
            </div>
            <div style="max-height: 400px; overflow: auto;border-bottom: 1px solid #DDD;">
                <ul class="dropdown-list gen-list new-orders" style="padding: 10px">
                    <li ng-if="ordersNotification.lateApprovedOrders.totalElements == 0" style="padding-left: 10px">
                        <span>No approved Pending orders</span>
                    </li>
                    <li ng-repeat="order in ordersNotification.lateApprovedOrders.content" class="order-item"
                        style="margin-bottom:10px;border-left: 10px solid #1CAF9A !important;">
                        <div class="row" style="padding: 0px 10px;">
                            <div class="col-sm-3 text-center" style="padding-top: 0px">
                                <h4 class="text-primary" style="margin:-3px;">
                                    <span ng-if="order.starred == true"><i class="fa fa-star-o"></i></span>
                                    <a href="" ng-click="openOrder(order)" style="color: #428BCA; font-size: 16px;">{{order.orderNumber}}</a>
                                </h4>
                                <span class="text-muted">{{lapsedTime(order.orderedDate)}}</span>
                            </div>
                            <div class="col-sm-7">
                                <h4 class="sender" style="margin-bottom: 3px">
                                    <a style="color:#428BCA !important;" href=""
                                       ng-click="showCustomerDetails(order.customer)">{{order.customer.name}}</a>
                                </h4>
                                <small>Region: <strong class="mr10">{{order.customer.salesRegion.name}}</strong>
                                    Sales Rep: <strong>{{order.customer.salesRep.firstName}}</strong></small>

                            </div>
                            <div class="col-sm-2" style="text-align: right">
                                <span class="label"
                                      ng-class="{'label-product': order.orderType == 'PRODUCT',
                                        'label-sample': order.orderType == 'SAMPLE'}">{{order.orderType}}</span>
                                <h5 class="pull-right" style="margin: 5px 0">{{order.orderTotal | currency:"&#x20b9;
                                    ":0}}</h5>
                            </div>
                        </div>

                        <div class="text-center">
                            <span ng-if="order.onhold == true" class="blink"
                                  style="margin-left: 50px;font-size: 15px; color: red">ORDER IS ON HOLD</span>
                        </div>
                    </li>
                    <li style="display:none"></li>
                    <li ng-if="ordersNotification.lateApprovedOrders.totalElements > 10">
                        <a href="" ng-click="showMore('LATERAPPROVED')"><h4 class="text-primary text-center">Show More</h4>
                        </a>
                    </li>
                </ul>
            </div>
        </div>
    </div>
</li>
