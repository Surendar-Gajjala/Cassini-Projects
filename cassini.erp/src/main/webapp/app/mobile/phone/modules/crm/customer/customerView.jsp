<div class="view-container">
    <md-content style="padding: 10px;background-color: #eeeeee;">
        <md-list>
            <md-list-item layout="column" class="md-whiteframe-z3"
                          style="background-color: #FFF; border-radius: 3px; padding: 0">
                <div style="width: 100%;">
                    <div layout="column">
                        <div style="line-height: 20px;background-color: #ff1744">
                            <h2 class="md-flex" style="font-size: 20px;text-align: center; color: #fff">Details</h2>
                        </div>
                        <div style="padding: 10px">
                            <div layout="row" style="width: 100%;">
                                <div flex style="text-align: left">
                                    <div style="font-size: 12px; color: gray">
                                        Name
                                    </div>
                                    <div style="font-size: 15px;">
                                        {{customer.name}}
                                    </div>
                                </div>
                            </div>

                            <div layout="row" style="width: 100%; margin-top: 10px">
                                <div flex style="text-align: left">
                                    <div style="font-size: 12px; color: gray">
                                        Total Orders
                                    </div>
                                    <div style="font-size: 15px;">
                                        {{productOrdersAmount | currency:"&#x20b9; ":0}}
                                    </div>
                                </div>
                                <div flex style="text-align: right">
                                    <div style="font-size: 12px; color: gray">
                                        Sample Orders
                                    </div>
                                    <div style="font-size: 15px;">
                                        {{sampleOrdersAmount | currency:"&#x20b9; ":0}}
                                    </div>
                                </div>
                            </div>
                            <div layout="row" style="width: 100%; margin-top: 10px">
                                <div flex style="text-align: left">
                                    <div style="font-size: 12px; color: gray">
                                        Region
                                    </div>
                                    <div style="font-size: 15px;">
                                        {{customer.salesRegion.name}}
                                    </div>
                                </div>
                                <div flex style="text-align: right">
                                    <div style="font-size: 12px; color: gray">
                                        Sales Rep
                                    </div>
                                    <div style="font-size: 15px;">
                                        {{customer.salesRep.firstName}}
                                    </div>
                                </div>
                            </div>
                            <div layout="row" style="margin-top: 10px;">
                                <div flex style="text-align: left">
                                    <div style="font-size: 12px; color: gray">
                                        Contact
                                    </div>
                                    <div style="font-size: 15px;">
                                        {{customer.contactPerson.firstName}}
                                    </div>
                                </div>
                                <div flex style="text-align: right">
                                    <div style="font-size: 12px; color: gray">
                                        Phone Number
                                    </div>
                                    <div class="phone-number" style="font-size: 15px;">
                                        <!--<span ng-if="$parent.isPhoneAvailable() == false">{{result.contactPerson.phoneMobile}}</span>-->
                                        <md-menu style="padding: 0;">
                                            <a href="" ng-click="$mdOpenMenu()">
                                                {{customer.contactPerson.phoneMobile}}
                                            </a>
                                            <md-menu-content width="4">
                                                <md-menu-item>
                                                    <md-button ng-click="$parent.callPhoneNumber(result.contactPerson.phoneMobile)">
                                                        <ng-md-icon style="fill:gray" icon="phone"></ng-md-icon>
                                                        Make a call
                                                    </md-button>
                                                </md-menu-item>
                                                <md-menu-item>
                                                    <md-button ng-click="$parent.addToContacts(result.contactPerson)">
                                                        <ng-md-icon style="fill:gray" icon="perm_contact_cal"></ng-md-icon>
                                                        Add to contacts
                                                    </md-button>
                                                </md-menu-item>
                                            </md-menu-content>
                                        </md-menu>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </md-list-item>
            <md-list-item layout="column" class="md-whiteframe-z3"
                          style="background-color: #FFF; border-radius: 3px; padding: 0;margin-top: 20px;">
                <div style="width: 100%;">
                    <div layout="column">
                        <div style="line-height: 20px;background-color: #ff1744">
                            <h2 class="md-flex" style="font-size: 20px;text-align: center; color: #fff">Orders</h2>
                        </div>
                        <div ng-if="loading == true" style="text-align: center">
                            <md-progress-circular md-mode="indeterminate" class="md-accent" style="display: inline-block"></md-progress-circular>
                        </div>
                        <div style="width: 100px; line-height: 50px; margin: auto" ng-if="loading == false && orders.length == 0">
                            <h3 style="color: lightgrey;">No orders</h3>
                        </div>
                        <div layout="column" style="padding: 10px;">
                            <div ng-repeat="order in orders" layout="column" class="order-item">
                                <div layout="row">
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
                                            Status
                                        </div>
                                        <div style="font-size: 15px;">
                                            {{order.status}}
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
                                <div layout="row" style="margin-top: 10px">
                                    <div flex style="text-align: left">
                                        <div style="font-size: 12px; color: gray">
                                            Order Total
                                        </div>
                                        <div style="font-size: 15px;">
                                            {{order.orderTotal | currency:"&#x20b9; ":0}}
                                        </div>
                                    </div>
                                    <div flex style="text-align: center">
                                    </div>
                                    <div flex style="text-align: right">
                                        <md-button flex md-no-link class="md-primary"
                                                   style="text-align: right;margin:0"
                                                   ng-click="$parent.showOrderDetails(order)">Details</md-button>
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