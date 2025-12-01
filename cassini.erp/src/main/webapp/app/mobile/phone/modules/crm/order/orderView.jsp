<div class="view-container">
    <md-content style="padding: 10px;background-color: #eeeeee;">
        <div layout="row">
            <div flex style="text-align: left;" ng-if="order.status == 'NEW'" >
                <md-button md-no-link class="md-primary" ng-click="$parent.approveOrder(order)"
                           ng-if="order.onhold == false">Approve</md-button>
                <div ng-if="order.onhold == true" class="blink" style="color: red; padding-top: 15px;">ON HOLD</div>
            </div>
            <div flex style="text-align: center;" ng-if="order.status == 'NEW'" >
                <md-button flex md-no-link class="md-primary" ng-if="order.onhold == true" ng-click="$parent.removeHold(order)">
                    Remove Hold
                </md-button>
                <md-button flex md-no-link class="md-warn" ng-if="order.onhold == false" ng-click="$parent.cancelOrder(order)">Cancel</md-button>
            </div>
            <div flex style="text-align: right;" ng-if="order.status == 'NEW'" >
                <md-button flex md-no-link class="md-warn" ng-if="order.onhold == false" ng-click="$parent.holdOrder(order)">Hold</md-button>
            </div>
        </div>
        <md-list>
            <md-list-item layout="column" class="md-whiteframe-z3"
                          style="background-color: #FFF; border-radius: 3px; padding: 0">
                <div style="width: 100%;">
                    <div layout="column">
                        <div style="line-height: 20px;background-color: #00bcd4">
                            <h2 class="md-flex" style="font-size: 20px;text-align: center; color: #fff">Details</h2>
                        </div>
                        <div style="padding: 10px">
                            <!--
                            <div ng-if="order.onhold == true"  layout="row" style="width: 100%;">
                                <div flex style="text-align: center;padding-top: 15px;">
                                    <div class="blink" style="color: red;">ON HOLD</div>
                                </div>
                                <div flex style="text-align: right">
                                    <md-button flex md-no-link class="md-primary" ng-if="order.onhold == true" ng-click="removeHold()">
                                        Remove Hold
                                    </md-button>
                                </div>
                            </div>
                            -->
                            <div layout="row" style="width: 100%;">
                                <div flex style="text-align: left">
                                    <div style="font-size: 12px; color: gray">
                                        Order Number
                                    </div>
                                    <div style="font-size: 15px;">
                                        {{order.orderNumber}}
                                    </div>
                                </div>
                                <div flex style="text-align: center">
                                    <div style="font-size: 12px; color: gray">
                                        When
                                    </div>
                                    <div style="font-size: 15px;">
                                        {{lapsedTime(order.orderedDate)}}
                                    </div>
                                </div>
                                <div flex style="text-align: right">
                                    <div style="font-size: 12px; color: gray">
                                        Status
                                    </div>
                                    <div style="font-size: 15px;">
                                        {{order.status}}
                                    </div>
                                </div>
                            </div>
                            <div layout="column" style="width: 100%; margin-top: 10px">
                                <div style="font-size: 12px; color: gray">
                                    Customer
                                </div>
                                <div style="font-size: 18px">
                                    <a href="" ui-sref="app.crm.customer({customerId: order.customer.id})"
                                       style="text-decoration: none;color: dodgerblue;">{{order.customer.name}}</a>
                                </div>
                            </div>
                            <div layout="row" style="width: 100%; margin-top: 10px">
                                <div flex style="text-align: left">
                                    <div style="font-size: 12px; color: gray">
                                        PO Number
                                    </div>
                                    <div style="font-size: 15px;">
                                        {{order.poNumber}}
                                    </div>
                                </div>
                                <div flex style="text-align: right">
                                    <div style="font-size: 12px; color: gray">
                                        Order Total
                                    </div>
                                    <div style="font-size: 15px;">
                                        {{order.orderTotal | currency:"&#x20b9; ":0}}
                                    </div>
                                </div>
                            </div>
                            <div layout="row" style="width: 100%; margin-top: 10px">
                                <div flex style="text-align: left">
                                    <div style="font-size: 12px; color: gray">
                                        Billing Address
                                    </div>
                                    <div style="font-size: 15px;">
                                        {{order.billingAddress.addressText}} <br>
                                        {{order.billingAddress.city}},
                                        {{order.billingAddress.state.name}} -  {{order.billingAddress.pincode}}
                                    </div>
                                </div>
                            </div>
                            <div layout="row" style="width: 100%; margin-top: 10px">
                                <div flex style="text-align: left">
                                    <div style="font-size: 12px; color: gray">
                                        Shipping Address
                                    </div>
                                    <div style="font-size: 15px;">
                                        {{order.shippingAddress.addressText}} <br>
                                        {{order.shippingAddress.city}},
                                        {{order.shippingAddress.state.name}} -  {{order.shippingAddress.pincode}}
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </md-list-item>
            <md-list-item layout="column" class="md-whiteframe-z3"
                          style="background-color: #FFF; border-radius: 3px; padding: 0; margin-top: 20px;">
                <div style="width: 100%;">
                    <div layout="column">
                        <div style="line-height: 20px;background-color: #00bcd4">
                            <h2 class="md-flex" style="font-size: 20px;text-align: center; color: #fff">Invoices</h2>
                        </div>
                        <div layout="column" style="padding: 10px;">
                            <div ng-repeat="invoice in invoices" layout="column" class="order-item">
                                <div layout="row" style="margin-top: 10px;">
                                    <div flex style="text-align: left">
                                        <div style="font-size: 12px; color: gray">
                                            Invoice Number
                                        </div>
                                        <div style="font-size: 15px;">
                                            {{invoice.invoiceNumber}}
                                        </div>
                                    </div>
                                    <div flex style="text-align: center">
                                        <div style="font-size: 12px; color: gray">
                                            Amount
                                        </div>
                                        <div style="font-size: 15px;">
                                            {{invoice.invoiceAmount | currency:"&#x20b9; ":0}}
                                        </div>
                                    </div>
                                    <div flex style="text-align: right">
                                        <div style="font-size: 12px; color: gray">
                                            Status
                                        </div>
                                        <div style="font-size: 15px;">
                                            {{invoice.status}}
                                        </div>
                                    </div>
                                </div>
                            </div>
                            <div ng-if="loading == true" style="text-align: center">
                                <md-progress-circular md-mode="indeterminate" class="md-accent" style="display: inline-block"></md-progress-circular>
                            </div>
                            <div style="width: 100px; line-height: 50px; margin: auto" ng-if="loading == false && invoices.length == 0">
                                <h3 style="color: lightgrey;">No invoices</h3>
                            </div>
                        </div>
                    </div>
                </div>
            </md-list-item>
            <md-list-item layout="column" class="md-whiteframe-z3"
                          style="background-color: #FFF; border-radius: 3px; padding: 0; margin-top: 20px;">
                <div style="width: 100%;">
                    <div layout="column">
                        <div style="line-height: 20px;background-color: #00bcd4">
                            <h2 class="md-flex" style="font-size: 20px;text-align: center; color: #fff">History</h2>
                        </div>
                        <div layout="column" style="padding: 10px;">
                            <div ng-repeat="item in order.history" layout="column" class="order-item">
                                <div layout="row">
                                    <div flex style="text-align: left">
                                        <div style="font-size: 12px; color: gray">
                                            Status
                                        </div>
                                        <div style="font-size: 15px;">
                                            {{item.newStatus}}
                                        </div>
                                    </div>
                                    <div flex style="text-align: center">
                                        <div style="font-size: 12px; color: gray">
                                            Who
                                        </div>
                                        <div style="font-size: 15px;">
                                            {{item.modifiedBy.firstName}}
                                        </div>
                                    </div>
                                    <div flex style="text-align: right">
                                        <div style="font-size: 12px; color: gray">
                                            When
                                        </div>
                                        <div style="font-size: 15px;">
                                            {{dateOnly(item.modifiedDate)}}
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </md-list-item>
            <md-list-item layout="column" class="md-whiteframe-z3"
                          style="background-color: #FFF; border-radius: 3px; padding: 0; margin-top: 20px;">
                <div style="width: 100%;">
                    <div layout="column">
                        <div style="line-height: 20px;background-color: #00bcd4">
                            <h2 class="md-flex" style="font-size: 20px;text-align: center; color: #fff">Items</h2>
                        </div>
                        <div layout="column" style="padding: 10px;">
                            <div ng-repeat="item in order.details" layout="column" class="order-item">
                                <div flex style="text-align: left">
                                    <div style="font-size: 18px;">
                                        {{item.product.name}}
                                    </div>
                                </div>
                                <div layout="row" style="margin-top: 10px;">
                                    <div flex style="text-align: left">
                                        <div style="font-size: 12px; color: gray">
                                            Unit Price
                                        </div>
                                        <div style="font-size: 15px;">
                                            {{item.unitPrice | currency:"&#x20b9; ":0}}
                                        </div>
                                    </div>
                                    <div flex style="text-align: center">
                                        <div style="font-size: 12px; color: gray">
                                            Quantity
                                        </div>
                                        <div style="font-size: 15px;">
                                            {{item.quantity}}
                                        </div>
                                    </div>
                                    <div flex style="text-align: right">
                                        <div style="font-size: 12px; color: gray">
                                            Item Total
                                        </div>
                                        <div style="font-size: 15px;">
                                            {{item.itemTotal | currency:"&#x20b9; ":0}}
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </md-list-item>
        </md-list>
    </md-content>
</div>