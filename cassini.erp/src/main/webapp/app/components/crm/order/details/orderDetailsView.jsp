<script type="text/ng-template" id="order-deatils-tb">
    <div>
        <button class="btn btn-sm btn-primary"
                ng-show="hasRole('Order Entry') == true || hasRole('Administrator') == true" ng-click="showOrders()">
            Show All Orders
        </button>
        <button class="btn btn-sm btn-default" ng-click="setEditOrder()" ng-show="editOrder == false"
                ng-if="order.onhold == false">Edit Order
        </button>
        <button class="btn btn-sm btn-success" ng-click="saveOrder()" ng-show="editOrder == true">Save Order</button>
        <button class="btn btn-sm btn-success"
                ng-if="order.onhold == true &&
                    hasPermission('permission.crm.order.approve') == true" ng-click="removeHold()">Remove Hold
        </button>

        <div ng-if="order.onhold == false" style="display: inline-block;">
            <button class="btn btn-sm btn-danger"
                    ng-click="holdOrder()" ng-if="order.status != 'SHIPPED'
                            && order.status != 'CANCELLED'
                            && hasPermission('permission.crm.order.approve') == true">Hold Order
            </button>

            <button class="btn btn-sm btn-success"
                    ng-show="order.status == 'NEW'"
                    authorization permission="permission.crm.order.approve"
                    ng-hide="order.status == 'APPROVED' || order.status == 'SHIPPED'||order.status == 'PROCESSING'|| order.status == 'CANCELLED'|| order.status == 'PROCESSED' ||
                     order.status == 'PARTIALLYSHIPPED'|| $parent.approvingOrder"
                    ng-click="approveOrder()">Approve Order

            </button>

            <button class="btn btn-sm btn-danger"
                    ng-show="order.status == 'NEW' || order.status == 'APPROVED' || order.status == 'PROCESSING' || order.status == 'PARTIALLYSHIPPED' ||
                    order.status == 'PROCESSED'"
                    authorization permission="permission.crm.order.approve"
                    ng-click="cancelOrder()">Cancel Order
            </button>

            <button class="btn btn-sm btn-lightblue"
                    ng-show="order.status == 'APPROVED' || order.status == 'PARTIALLYSHIPPED'"
                    authorization permission="permission.crm.order.dispatch"
                    ng-click="beginOrderProcessing()">Begin Processing
            </button>

            <div class="btn-group" style="margin-bottom: 0">
                <button type="button" class="btn btn-sm btn-warning"
                        authorization permission="permission.crm.order.create"
                        ng-show="(order.status == 'PROCESSING')
                                        && isEntireOrderProcessed() == false
                                        && authorizationFactory.hasPermission('permission.crm.order.create')"
                        ng-click="newShipment()">Invoice Order
                </button>
            </div>
        </div>
        <div class="btn-group" style="margin-left: 20px; margin-bottom: 0;">
            <button class="btn btn-sm btn-default" ng-click="printOrder()">
                <i class="glyphicon glyphicon-print"></i>
            </button>
            <button class="btn btn-sm btn-default" ng-click="starOrder()">
                <i ng-if="order.starred == false" class="fa fa-star"></i>
                <i ng-if="order.starred == true" class="fa fa-star" style="color: #ffa331"></i>
            </button>
        </div>
    </div>
</script>


<div ng-if="showNewShipment == true">
    <div ng-include="templates.newShipment" ng-controller="ShipmentController"></div>
</div>

<div ng-show="showNewShipment == false">
    <div style="padding-bottom: 20px;margin-bottom: 20px;">
        <div style="margin-top:10px">
            <span>Order Type:</span>
            <span class="order-type mr20" ng-class="{'order-type-product': order.orderType == 'PRODUCT',
                        'order-type-sample': order.orderType == 'SAMPLE'}" style="margin-left: 10px;">
                {{order.orderType}}
            </span>

            <span style="font-size: 16px; padding-right:10px">Status:</span>
            <!--<span class="text-success" style="font-size: 28px">{{order.status}}</span>-->
            <span class="status-label" style="margin-top: -5px;"
                  ng-class="{
                        'status-label-new': order.status == 'NEW',
                        'status-label-approved': order.status == 'APPROVED',
                        'status-label-canceled': order.status == 'CANCELLED',
                        'status-label-processing': order.status == 'PROCESSING',
                        'status-label-processed': order.status == 'PROCESSED',
                        'status-label-partiallyshipped': order.status == 'PARTIALLYSHIPPED',
                        'status-label-shipped': order.status == 'SHIPPED'
                     }">
                {{order.status}}
            </span>
            <span ng-if="order.onhold == true" class="blink" style="margin-left: 50px;font-size: 20px; color: red">ORDER IS ON HOLD</span>
        </div>
    </div>

    <div ng-if="loading" stye="padding-top: 30px; text-align: center">
        <br/>
        <span style="font-size: 18px;">
            <img src="app/assets/images/loaders/loader6.gif" class="mr5">Loading order details...
        </span>
        <br/>
    </div>

    <div ng-hide="loading" style="padding-left: 10px;padding-right: 10px;">
        <div class="row">
            <div class="col-sm-12 col-md-4 col-lg-3 styled-panel">
                <div style="">
                    <h3 style="padding-bottom: 10px;border-bottom: 1px solid #EEE;margin-top: 0;">Customer</h3>

                    <div class="row">
                        <div class="col-sm-12">
                            <h4>Name:
                                <span class="text-success">
                                    <a href=""
                                       ng-click="showCustomerDetails(order.customer)">{{order.customer.name}}</a>
                                </span>
                            </h4>
                            <h4>Region: <span class="text-primary">{{order.customer.salesRegion.name}}</span></h4>
                            <h4>Sales Rep: <span class="text-primary">
                                <a href="" ng-click="showSalesRep(order.customer.salesRep)">
                                    {{order.customer.salesRep.firstName}}
                                </a></span>
                            </h4>
                        </div>
                    </div>

                    <div class="row">
                        <div class="col-sm-12 order-customer-map">
                            <div style="background-color: #FFF; border: 1px solid #DDD;border-radius: 3px;padding: 5px;">
                                <ui-gmap-google-map
                                        center="map.center"
                                        zoom="map.zoom"
                                        dragging="map.dragging"
                                        bounds="map.bounds"
                                        events="map.events"
                                        options="map.options"
                                        pan="true"
                                        control="map.control">
                                    <ui-gmap-marker idkey="marker.id" coords="marker"></ui-gmap-marker>
                                </ui-gmap-google-map>
                            </div>
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-xs-12">
                            <h4 class="text-primary">Additional Information</h4>

                            <div class="styled-panel">
                                <div class="form-group">
                                    <label class="control-label">PO Number:</label>

                                    <div class="">
                                        <input type="text" class="form-control" ng-model="order.poNumber"
                                               ng-disabled="editOrder == false">
                                    </div>
                                </div>
                                <div class="form-group">
                                    <label class="control-label">Notes:</label>

                                    <div class="">
                                    <textarea class="form-control" rows="3" style="resize: none;"
                                              ng-model="order.notes" ng-disabled="editOrder == false"></textarea>
                                    </div>
                                </div>
                                <div class="form-group">
                                    <label class="control-label">Phone Number(s):</label>

                                    <div class="">
                                        <input type="text" class="form-control" ng-model="order.contactPhone"
                                               ng-disabled="editOrder == false">
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-sm-12">
                            <h4 class="text-primary">Addresses</h4>

                            <div style="background-color: #FFF">
                                <tabset>
                                    <tab active="true" heading="Billing Address">
                                        <div class="styled-panel">
                                            <div class="form-group">
                                                <label class="control-label">Address:</label>

                                                <div class="">
                                                <textarea class="form-control" rows="5" style="resize: none;"
                                                          ng-disabled="editOrder == false"
                                                          ng-model="order.billingAddress.addressText"></textarea>
                                                </div>
                                            </div>
                                            <div class="form-group">
                                                <label class="control-label">City/Town:</label>

                                                <div class="">
                                                    <input type="text" class="form-control"
                                                           ng-model="order.billingAddress.city"
                                                           ng-disabled="editOrder == false">
                                                </div>
                                            </div>
                                            <div class="form-group">
                                                <label class="control-label">State:</label>

                                                <div class="">
                                                    <ui-select ng-model="order.billingAddress.state" theme="bootstrap"
                                                               style="width:100%" ng-disabled="editOrder == false">
                                                        <ui-select-match placeholder="Select state">
                                                            {{$select.selected.name}}
                                                        </ui-select-match>
                                                        <ui-select-choices
                                                                repeat="state in states | filter: $select.search">
                                                            <div ng-bind-html="state.name | highlight: $select.search"></div>
                                                        </ui-select-choices>
                                                    </ui-select>
                                                </div>
                                            </div>
                                            <div class="form-group">
                                                <label class="control-label">Pincode:</label>

                                                <div class="">
                                                    <input type="text" class="form-control"
                                                           ng-model="order.billingAddress.pincode"
                                                           ng-disabled="editOrder == false">
                                                </div>
                                            </div>
                                        </div>
                                    </tab>
                                    <tab heading="Shipping Address">
                                        <div class="styled-panel">
                                            <div class="form-group">
                                                <label class="control-label">Ship To:</label>

                                                <div class="">
                                                    <input type="text" class="form-control" ng-model="order.shipTo"
                                                           ng-disabled="editOrder == false">
                                                </div>
                                            </div>

                                            <div class="form-group">
                                                <label class="control-label">Address:</label>

                                                <div class="">
                                                <textarea class="form-control" rows="5" style="resize: none;"
                                                          ng-model="order.shippingAddress.addressText"
                                                          ng-disabled="editOrder == false"></textarea>
                                                </div>
                                            </div>
                                            <div class="form-group">
                                                <label class="control-label">City/Town:</label>

                                                <div class="">
                                                    <input type="text" class="form-control"
                                                           ng-model="order.shippingAddress.city"
                                                           ng-disabled="editOrder == false">
                                                </div>
                                            </div>
                                            <div class="form-group">

                                                <label class="control-label">State:</label>

                                                <div class="">
                                                    <ui-select ng-model="order.shippingAddress.state" theme="bootstrap"
                                                               style="width:100%" ng-disabled="editOrder == false">
                                                        <ui-select-match placeholder="Select state">
                                                            {{$select.selected.name}}
                                                        </ui-select-match>
                                                        <ui-select-choices
                                                                repeat="state in states | filter: $select.search">
                                                            <div ng-bind-html="state.name | highlight: $select.search"></div>
                                                        </ui-select-choices>
                                                    </ui-select>
                                                </div>
                                            </div>
                                            <div class="form-group">
                                                <label class="control-label">Pincode:</label>

                                                <div class="">
                                                    <input type="text" class="form-control"
                                                           ng-model="order.shippingAddress.pincode"
                                                           ng-disabled="editOrder == false">
                                                </div>
                                            </div>
                                        </div>
                                    </tab>
                                </tabset>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
            <div class="col-sm-12 col-md-8 col-lg-9" style="padding-left: 30px;">
                <div style="margin-bottom: 50px;">
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
                <br>

                <div style="margin-bottom: 50px;">
                    <h3 style="padding-bottom: 10px;border-bottom: 1px solid #EEE;">Processing</h3>

                    <table class="table table-striped">
                        <thead>
                        <tr>
                            <th>Assigned Date</th>
                            <th>Assigned By</th>
                            <th>Assigned To</th>
                        </tr>
                        </thead>

                        <tbody>
                        <tr ng-if="verifications != null && verifications != undefined && verifications.length == 0">
                            <td colspan="3">
                                No data
                            </td>
                        </tr>
                        <tr ng-repeat="item in verifications">
                            <td>{{item.assignedDate}}</td>
                            <td>{{item.assignedBy.firstName}}</td>
                            <td>{{item.verifiedBy.firstName}}</td>
                        </tr>
                        </tbody>
                    </table>
                </div>

                <br>

                <div style="margin-top: 0px;" class="responsive-table">
                    <h3 style="padding-bottom: 10px;border-bottom: 1px solid #EEE;">Items</h3>

                    <div ng-if="order != null">
                        <button class="btn btn-sm btn-success" style="width: 60px;"
                                ng-show="(order.status == 'NEW' || order.status == 'APPROVED' || order.status == 'PROCESSING' ||order.status == 'PROCESSED' || order.status == 'PARTIALLYSHIPPED') && order.onhold == false"
                                ng-click="addItems()">Add
                        </button>

                        <button class="btn btn-sm btn-danger" ng-show="(order.status == 'NEW' || order.status == 'APPROVED' || order.status == 'PROCESSING' ||
                            order.status == 'PROCESSED' || order.status == 'PARTIALLYSHIPPED') && order.onhold == false"
                                ng-click="modifyOrder()">Modify
                        </button>
                        <button class="btn btn-sm btn-success" ng-if="editMode" ng-click="updateOrder()">Save</button>
                        <button class="btn btn-sm btn-default" ng-if="editMode" ng-click="cancelModifyOrder()">Cancel
                        </button>
                    </div>
                    <br/>

                    <table class="table table-striped table-invoice">
                        <thead>
                        <tr>
                            <th style="text-align: center; width: 50px">#</th>
                            <th style="text-align: left">Item</th>
                            <th style="text-align: center">Ordered Quantity</th>
                            <th style="text-align: center">Quantity Shipped</th>
                            <th style="text-align: center" ng-show="order.status == 'PROCESSING' || order.status == 'PROCESSED'">Boxes</th>
                            <th style="text-align: center">Quantity Processed</th>
                            <th style="text-align: center">Pending Quantity</th>
                            <th>Unit Price</th>
                            <th>Total Price</th>
                        </tr>
                        </thead>

                        <tbody>
                        <tr ng-show="order.details.length == 0">
                            <td colspan="8" style="text-align: left">
                                <span>No items in the shopping cart.</span>
                            </td>
                        </tr>
                        <tr ng-repeat="item in order.details"
                            ng-class="{'pending-items-row': ((item.quantity - item.quantityProcessed) > 0)}">
                            <td style="text-align: center; width: 50px">{{item.serialNumber != null ? item.serialNumber
                                : $index+1}}
                            </td>
                            <td style="vertical-align: middle; text-align: left">
                                <div class="text-primary">
                                    <strong class="mr20">{{item.product.name}}</strong><br>
                                    <small class="text-danger">
                                        <a title="Remove this item" href=""
                                           ng-show="order.onhold == false && isRemove(item)"
                                           ng-click="removeItem(item)" style="color: #FF4500">Remove</a>
                                    </small>
                                        <span ng-if="item.inventory != null"
                                              class="label-danger low-inventory animated infinite flash"
                                              style="color: #FFF;font-size: 16px;padding: 5px; font-size: 16px">
                                            Inventory is {{item.inventory.inventory | currency:"":0}} </span>
                                </div>
                            </td>
                            <td align="center" style="text-align: center; vertical-align: middle;">
                                <span ng-if="!editMode">{{item.quantity}}</span>
                                <%-- <input class="form-control" type="number" ng-change="quantityChanged()"
                                        ng-model="item.quantity" ng-if="editMode" style="width: 75px;"/>--%>

                                <input class="form-control" type="number" ng-change="quantityChanged()"
                                       ng-model="item.quantity" ng-if="editMode" style="width: 75px;"/>
                            </td>
                            <td style="text-align: center; vertical-align: middle;">{{item.quantityShipped}}</td>
                            <td style="text-align: center; vertical-align: middle;"><input style="width: 50px"
                                    ng-show="order.status == 'PROCESSING'" type="number" ng-model="item.boxes">
                                <span ng-show="order.status == 'PROCESSED'">{{item.boxes}}</span>
                            </td>
                            <td style="text-align: center; vertical-align: middle;">{{item.quantityProcessed}}</td>
                            <td style="text-align: center; vertical-align: middle;">{{item.quantity -
                                item.quantityProcessed}}
                            </td>
                            <td style="vertical-align: middle;">{{item.unitPrice | currency:"&#x20b9; ":0}}</td>
                            <td style="vertical-align: middle;">{{(item.quantity * item.unitPrice) | currency:"&#x20b9;
                                ":0}}
                            </td>
                        </tr>
                        </tbody>
                    </table>


                    <table class="table table-total" style="float: none">
                        <tbody>
                        <tr>
                            <td><strong>TOTAL :</strong></td>
                            <td><h4 class="text-primary">{{order.orderTotal | currency:"&#x20b9; ":0}}</h4></td>
                        </tr>
                        </tbody>
                    </table>
                </div>

                <br/>

                <div style="margin-top: 30px;margin-bottom: 50px;" class="responsive-table">
                    <h3 style="padding-bottom: 10px;border-bottom: 1px solid #EEE;">Shipments</h3>
                    <button class="btn btn-sm btn-danger" style="width: 60px;" ng-if="selection == 'selected'">Cancel
                    </button>

                    <table class="table table-striped">
                        <thead>
                        <tr>
                            <th style="width:18px"></th>
                            <th>Status</th>
                            <th>Invoice Number</th>
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

                        <tr ng-if="orderShipments == null || orderShipments.length == 0">
                            <td colspan="11">
                                No shipments
                            </td>
                        </tr>

                        <tr ng-repeat="shipment in orderShipments" ng-if="shipment.status != 'CANCELLED'">

                            <td style="width: 18px" ng-if="!shipment.showDetails">
                                <span ng-click="toggleShipmentDetails(shipment);" style="cursor: pointer">
                                    <span title="Show Details" ng-if="shipment.detailsOpen == false"><i
                                            class="fa fa-plus-circle mr5"></i></span>
                                    <span title="Hide Details" ng-if="shipment.detailsOpen == true"><i
                                            class="fa fa-minus-circle mr5"></i></span>
                                </span>
                            </td>

                            <td ng-if="!shipment.showDetails">
                                <span ng-class="{'shipment-pending': shipment.status == 'PENDING',
                                    'shipment-shipped': shipment.status == 'SHIPPED'}">{{shipment.status}}</span>
                            </td>
                            <td style="background-color:#F2F7F9" colspan="11"
                                ng-include="shipment.showDetails ? 'app/components/crm/order/shipment/shipmentDetails.jsp' : null">
                            </td>

                            <td ng-if="!shipment.showDetails">
                                {{shipment.invoiceNumber}}
                            </td>
                            <td ng-if="!shipment.showDetails">{{shipment.invoiceAmount | currency:"&#x20b9; ":0}}</td>
                            <td ng-if="!shipment.showDetails">{{shipment.consignment.shipper.name}}</td>
                            <td ng-if="!shipment.showDetails">
                                <a href="" ng-click="showConsignment(shipment.consignment)">{{shipment.consignment.number}}</a>
                            </td>
                            <td ng-if="!shipment.showDetails">{{shipment.consignment.confirmationNumber}}</td>
                            <td ng-if="!shipment.showDetails">{{shipment.consignment.vehicle.number}}</td>
                            <td ng-if="!shipment.showDetails">{{shipment.consignment.driver.firstName}}</td>
                            <td ng-if="!shipment.showDetails">{{shipment.consignment.shippedDate}}</td>
                            <td ng-if="!shipment.showDetails">{{shipment.consignment.shippedBy.firstName}}</td>
                        </tr>
                        </tbody>
                    </table>
                </div>

            </div>
        </div>
    </div>
</div>
