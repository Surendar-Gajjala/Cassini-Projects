<div class="view-container" style="overflow: hidden;">
    <div layout="column">
        <div layout="row"
             style="background-color: #FFF;padding: 10px 0 10px 10px;">
            <form flex id="search" autocomplete="off" ng-submit="search()" style="width: 100%;">
                <md-input-container class="md-icon-float" style="width: 96%; margin: 8px 8px 3px 0;">
                    <input placeholder="Search..." ng-model="searchText">
                </md-input-container>
                <md-button ng-click="search()"
                           style="min-width: 30px;margin: 8px 0 0 0;position: absolute;
                                right: 5px;top: 15px;line-height: 0 !important;min-height: 8px;">
                    <ng-md-icon icon="search" style="fill: grey"></ng-md-icon>
                </md-button>
                <label style="font-size: 14px; color: #B3B3B3;position: absolute;right: 40px;top: 28px">
                    {{totalResults}} orders</label>
            </form>
        </div>
        <div flex>
            <md-tabs class="orders-tab" md-dynamic-height md-border-bottom>
                <md-tab ng-repeat="tab in tabs" label="{{tab.title}}" md-on-select="tabChanged(tab)">
                    <md-content class="md-padding"  style="overflow-x: hidden;overflow-y: hidden;">
                        <div flex layout="column"
                             infinite-scroll load-more="nextPage()"
                                style="position: fixed;
                                        top: 170px;
                                        left: 0;
                                        bottom: 0;
                                        right: 0;
                                        overflow-y: auto;
                                        padding: 10px;">
                            <div style="width: 100px; line-height: 50px; margin: auto" ng-if="tab.orders.length == 0">
                                <h3 style="color: lightgrey;">No orders</h3>
                            </div>

                            <div ng-if="tab.orders.length > 0" class="orders-list" ng-repeat="order in tab.orders">
                                <div class="md-whiteframe-z3" style="background-color:#FFFFFF; padding: 10px; border-radius: 4px; margin-bottom: 10px;"
                                     layout="column">
                                    <div layout="row">
                                        <div flex style="text-align: left">
                                            <div style="font-size: 12px; color: gray">
                                                Order Number
                                            </div>
                                            <div style="font-size: 15px;">
                                                {{order.orderNumber}}
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
                                                Customer
                                            </div>
                                            <div style="font-size: 15px;">
                                                <a href="" ui-sref="app.crm.customer({customerId: order.customer.id})"
                                                   style="text-decoration: none;color: dodgerblue;">{{order.customer.name}}</a>
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
                                    <div layout="row" style="margin-top: 10px;">
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
                                        <div flex style="text-align: {{order.status == 'NEW' ? 'right' : 'center'}};">
                                            <md-button flex md-no-link class="md-primary" ng-click="$parent.showOrderDetails(order)">Details</md-button>
                                        </div>
                                    </div>
                                </div>
                            </div>
                            <br>
                        </div>
                    </md-content>
                </md-tab>
            </md-tabs>
        </div>
    </div>
</div>