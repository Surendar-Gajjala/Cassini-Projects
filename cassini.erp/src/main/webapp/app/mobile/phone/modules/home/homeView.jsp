<flippy id="homeView"
        flip-duration="800"
        ng-class="{'flipped': flipped}"
        style="position: fixed;top: 56px;bottom: 0;left: 0;right: 0;"
        timing-function="ease-in-out">

    <flippy-front>
        <div class="view-container md-whiteframe-z5" style="bottom: 56px;">
            <div style="padding: 10px" class="home-view">
                <div layout="row">
                    <md-button flex class="md-whiteframe-z3 order-count new" style="margin-right: 10px;"
                               ng-click="showOrders('NEW')">
                        <div class="count">{{orderCounts.newOrders}}</div>
                        <div class=status>New Orders</div>
                    </md-button>

                    <md-button flex class="md-whiteframe-z3 order-count approved" ng-click="showOrders('APPROVED')">
                        <div class="count">{{orderCounts.approvedOrders}}</div>
                        <div class=status>Approved Orders</div>
                    </md-button>
                </div>

                <div layout="row" style="margin-top: 10px;">
                    <md-button flex class="md-whiteframe-z3 order-count processing" ng-click="showOrders('PROCESSING')"
                               style="margin-right: 10px;">
                        <div class="count">{{orderCounts.processingOrders}}</div>
                        <div class=status>Processing Orders</div>
                    </md-button>

                    <md-button flex class="md-whiteframe-z3 order-count pending" ng-click="showOrders('PROCESSED,PARTIALLYSHIPPED')">
                        <div class="count">{{orderCounts.pendingOrders}}</div>
                        <div class=status>Pending Orders</div>
                    </md-button>
                </div>
                <div layout="row" style="margin-top: 10px;">
                    <md-button flex class="md-whiteframe-z3 order-count shipped" ng-click="showOrders('SHIPPED')"
                               style="margin-right: 10px;">
                        <div class="count">{{orderCounts.shippedOrders}}</div>
                        <div class=status>Shipped Orders</div>
                    </md-button>

                    <md-button flex class="md-whiteframe-z3 order-count canceled" ng-click="showOrders('CANCELLED')">
                        <div class="count">{{orderCounts.canceledOrders}}</div>
                        <div class=status>Canceled Orders</div>
                    </md-button>
                </div>
            </div>
        </div>
    </flippy-front>

    <flippy-back>
        <div infinite-scroll load-more="nextPage()" class="view-container md-whiteframe-z5" ng-if="inited == true" style="bottom: 56px;">
            <div layout="row">
                <md-progress-linear flex md-mode="indeterminate"
                                    class="md-accent ng-hide"
                                    ng-show="loading">
                </md-progress-linear>
            </div>
            <div layout="column">
                <div layout="row">
                    <md-button md-no-link class="md-primary"  style="margin: 0px;" ng-click="flip()">Go Back</md-button>
                    <div flex style="text-align: right">
                        <!--<md-button md-no-link class="md-primary"  style="margin: 0px;" ng-click="refreshOrders()">Refresh</md-button>-->
                    </div>
                </div>

                <div flex layout="column" style="padding: 10px;">
                    <div>
                        <div class="orders-list" ng-repeat="order in orders">
                            <div class="md-whiteframe-z3" style="background-color:#FFFFFF; padding: 10px; border-radius: 4px; margin-bottom: 10px;"
                                 layout="column">
                                <div layout="row">
                                    <div flex style="text-align: left">
                                        <div style="font-size: 12px; color: gray">
                                            Customer
                                        </div>
                                        <div style="font-size: 15px;">
                                            <a href="" ui-sref="app.crm.customer({customerId: order.customer.id})"
                                               style="text-decoration: none;color: dodgerblue;">{{order.customer.name}}</a>
                                        </div>
                                    </div>
                                </div>
                                <div layout="row" style="margin-top: 10px;">
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
                                            Order Total
                                        </div>
                                        <div style="font-size: 15px;">
                                            {{order.orderTotal | currency:"&#x20b9; ":0}}
                                        </div>
                                    </div>
                                    <div flex style="text-align: right">
                                        <div style="font-size: 12px; color: gray">
                                            When
                                        </div>
                                        <div style="font-size: 15px;">
                                            {{lapsedTime(order.orderedDate)}}
                                        </div>
                                    </div>
                                </div>
                                <div layout="row" style="margin-top: 10px;">
                                    <div flex style="text-align: left">
                                        <div style="font-size: 12px; color: gray">
                                            Region
                                        </div>
                                        <div style="font-size: 15px;">
                                            {{order.customer.salesRegion.name}}
                                        </div>
                                    </div>
                                    <div flex style="text-align: center">
                                        <div style="font-size: 12px; color: gray">
                                            State
                                        </div>
                                        <div style="font-size: 15px;">
                                            {{order.customer.salesRegion.state.name}}
                                        </div>
                                    </div>
                                    <div flex style="text-align: right">
                                        <div style="font-size: 12px; color: gray">
                                            Sales Rep
                                        </div>
                                        <div style="font-size: 15px;">
                                            {{order.customer.salesRep.firstName}}
                                        </div>
                                    </div>
                                </div>
                                <div layout="row" class="order-view">
                                    <div flex style="text-align: left;" ng-if="order.status == 'NEW'" >
                                        <md-button md-no-link class="md-primary" ng-click="approveOrder(order)"
                                                   ng-if="order.onhold == false">Approve</md-button>
                                        <div ng-if="order.onhold == true" class="blink" style="color: red; padding-top: 15px;">ON HOLD</div>
                                    </div>
                                    <div flex style="text-align: center;" ng-if="order.status == 'NEW'" >
                                        <md-button flex md-no-link class="md-primary" ng-if="order.onhold == true" ng-click="$parent.removeHold(order)">
                                            Remove Hold
                                        </md-button>
                                        <md-button flex md-no-link class="md-warn" ng-if="order.onhold == false" ng-click="$parent.cancelOrder(order)">Cancel</md-button>
                                    </div>
                                    <div flex style="text-align: center;" ng-if="order.status == 'NEW'" >
                                        <md-button flex md-no-link class="md-warn" ng-if="order.onhold == false" ng-click="$parent.holdOrder(order)">Hold</md-button>
                                    </div>
                                    <div flex style="text-align: {{order.status == 'NEW' ? 'right' : 'center'}};">
                                        <md-button flex md-no-link class="md-primary" ng-click="showOrderDetails(order)">Details</md-button>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </flippy-back>
</flippy>
