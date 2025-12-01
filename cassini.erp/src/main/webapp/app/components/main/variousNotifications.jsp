<li ng-if="ordersNotification.lateProcessingOrders.totalElements > 0" style="list-style: none;">
    <div class="btn-group" dropdown style="margin-top: -13px;">
        <button title="Pending Processed Orders" class="btn btn-default dropdown-toggle tp-icon" dropdown-toggle style="margin-left: -70px;">
            <i class="fa fa-list"></i>
            <span class="badge lateprocessed-orders-badge">{{ordersNotification.lateProcessingOrders.totalElements}}</span>
        </button>
        <div class="widget-messaging dropdown-menu dropdown-menu-head pull-right"
             style="width: 600px;">
            <div class="panel-heading"
                 style="background-color: #E4E7EA;border: 1px solid #D7D7D7; padding:0; height: 56px">
                <div class="row">
                    <div class="panel-title col-sm-8" style="font-size:15px; padding: 20px 0 0 20px">Pending Processed Orders
                    </div>
                </div>
            </div>

            <div style="max-height: 400px; overflow: auto;border-bottom: 1px solid #DDD;">
                <ul class="dropdown-list gen-list new-orders" style="padding: 10px">
                    <li ng-if="ordersNotification.lateProcessingOrders.totalElements == 0" style="padding-left: 10px">
                        <span>No Late Processed orders</span>
                    </li>
                    <li ng-repeat="order in ordersNotification.lateProcessingOrders.content" class="order-item"
                        style="margin-bottom:10px;border-left: 10px solid #1CAF9A !important;">
                        <div class="row" style="padding: 0px 10px;">
                            <div class="col-sm-3 text-center" style="padding-top: 0px">
                                <h4 class="text-primary" style="margin:-3px;">
                                    <span ng-if="order.starred == true"><i class="fa fa-star"
                                                                           style="color: #ffa331"></i></span>
                                    <a ui-sref="app.crm.orders.details({orderId: order.id})" style="color: #428BCA; font-size: 16px;">{{order.orderNumber}}</a>
                                </h4>
                                <div class="text-center">
                            <span class="blink"
                                  style="margin-left: 6px;font-size: 12px; color: mediumblue;">PROCESSING FROM 2hr's </span>
                                </div>
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
                                  style="margin-left: 50px;font-size: 15px; color: #1c11ff">ORDER IS ON HOLD</span>
                        </div>
                    </li>
                    <li style="display:none"></li>
                    <li ng-if="ordersNotification.lateProcessingOrders.totalElements > 10">
                        <a href="" ng-click="showMore('PROCESSING')"><h4 class="text-primary text-center">Show More</h4>
                        </a>
                    </li>
                </ul>
            </div>
        </div>
    </div>
</li>

<li ng-if="ordersNotification.lateShippedOrders.totalElements > 0" style="list-style: none;">
    <div class="btn-group" dropdown style="margin-top: -13px;">
        <button title="Pending Shipped Orders" class="btn btn-default dropdown-toggle tp-icon" dropdown-toggle style="margin-left: -128px; margin-top: -52px;">
            <i class="fa fa-list"></i>
            <span class="badge lateshipped-orders-badge">{{ordersNotification.lateShippedOrders.totalElements}}</span>
        </button>
        <div class="widget-messaging dropdown-menu dropdown-menu-head pull-right"
             style="width: 600px;">
            <div class="panel-heading"
                 style="background-color: #E4E7EA;border: 1px solid #D7D7D7; padding:0; height: 56px">
                <div class="row">
                    <div class="panel-title col-sm-8" style="font-size:15px; padding: 20px 0 0 20px">Late Shipped Orders
                    </div>
                </div>
            </div>

            <div style="max-height: 400px; overflow: auto;border-bottom: 1px solid #DDD;">
                <ul class="dropdown-list gen-list new-orders" style="padding: 10px">
                    <li ng-if="ordersNotification.lateShippedOrders .totalElements == 0" style="padding-left: 10px">
                        <span>No Late Shipped orders</span>
                    </li>
                    <li ng-repeat="order in ordersNotification.lateShippedOrders.content" class="order-item"
                        style="margin-bottom:10px;border-left: 10px solid #1CAF9A !important;">
                        <div class="row" style="padding: 0px 10px;">
                            <div class="col-sm-3 text-center" style="padding-top: 0px">
                                <h4 class="text-primary" style="margin:-3px;">
                                    <span ng-if="order.starred == true"><i class="fa fa-star"
                                                                           style="color: #ffa331"></i></span>
                                    <a ui-sref="app.crm.orders.details({orderId: order.id})" style="color: #428BCA; font-size: 16px;">{{order.orderNumber}}</a>
                                </h4>
                                <div class="text-center">
                            <span class="blink"
                                  style="margin-left: 6px;font-size: 12px; color: red;">PROCESSED FROM 2Days </span>
                                </div>
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
                                  style="margin-left: 50px;font-size: 15px; color: #1c11ff">ORDER IS ON HOLD</span>
                        </div>
                    </li>
                    <li style="display:none"></li>
                    <li ng-if="ordersNotification.lateProcessingOrders.totalElements > 10">
                        <a href="" ng-click="showMore('PROCESSED')"><h4 class="text-primary text-center">Show More</h4>
                        </a>
                    </li>
                </ul>
            </div>
        </div>
    </div>
</li>