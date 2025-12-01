<script type="text/ng-template" id="consignment-view-tb">
    <button class="btn btn-sm btn-primary mr10" ng-click="goBack()">Go Back</button>
    <button class="btn btn-sm btn-success mr10" ng-click="saveChanges()" ng-show="editConsignment == true">Save</button>
    <button class="btn btn-sm btn-success mr10" ng-click="setEditConsignment()"  ng-show="editConsignment == false">Edit</button>
    <button class="btn btn-sm btn-default" ng-click="printConsignment()" title="Print Consignment">
        <i class="glyphicon glyphicon-print"></i>
    </button>
    <button class="btn btn-sm btn-default" ng-click="printConsignmentShipments()" title="Print Invoices">
        <i class="glyphicon glyphicon-print" style="color: blue"></i>
    </button>
</script>

<div>
    <div class="row" ng-show="shipment == false">
        <h3 class="text-primary text-center section-title" style="padding-right: 130px;">
            Consignment Details ({{consignment.number}})
        </h3>
        <div class="col-md-12 col-lg-8 col-lg-offset-2">
            <div class="row">
                <div class="col-md-12">
                    <br>
                        <h4 style="margin-bottom: 0">Shipments</h4>
                        <button class="btn btn-sm btn-primary mr10" ng-click="addShipments()">Add Shipment</button>
                        <table class="table table-striped">
                            <thead>
                            <tr>
                                <th>Order Number</th>
                                <th>Invoice Number</th>
                                <th>PO Number</th>
                                <th>Invoiced Amount</th>
                                <th>Date Processed</th>
                            </tr>
                            </thead>
                            <tbody>
                            <tr ng-if="consignment.shipments.length == 0">
                                <td colspan="5">
                                    Select one or more shipments for this consignment
                                </td>
                            </tr>
                            <tr ng-repeat="shipment in consignment.shipments">
                                <td ng-if="!shipment.showDetails" >
                                    <span ng-click="toggleShipmentDetails(shipment);" style="cursor: pointer">
                                        <span title="Show Details" ng-if="shipment.detailsOpen == false"><i class="fa fa-plus-circle mr5"></i></span>
                                        <span title="Hide Details" ng-if="shipment.detailsOpen == true"><i class="fa fa-minus-circle mr5"></i></span>
                                    </span>
                                    {{shipment.order.orderNumber}}
                                    </br>
                                    <span>
                                    <small class="text-danger" style="margin-left: 22px;">
                                        <a title="Remove this item" href=""
                                           ng-click="removeItem(shipment)" style="color: #FF4500">Remove</a>
                                    </small>
                                    </span>
                                </td>
                                <td style="background-color:#F2F7F9" colspan="5"
                                    ng-include="shipment.showDetails ? 'app/components/crm/order/shipment/shipmentDetails.jsp' : null">
                                </td>
                                <td ng-if="!shipment.showDetails" >{{shipment.invoiceNumber}}</td>
                                <td ng-if="!shipment.showDetails" >{{shipment.order.poNumber}}</td>
                                <td ng-if="!shipment.showDetails" >{{shipment.invoiceAmount | currency:"&#x20b9; ":0}}</td>
                                <td ng-if="!shipment.showDetails" >{{shipment.modifiedDate}}</td>
                            </tr>
                            </tbody>
                        </table>
                    </div>
                </div>
            </div>

            <hr>

            <div class="row">
                <div class="col-md-4">
                    <h4>Consignment Information</h4>
                    <div style="height: 450px; padding: 20px;border: 1px solid #D0DDE1; border-radius: 5px; background-color: #F2F7F9;">
                        <div class="form-group">
                            <label class="control-label">Consignee</label>
                            <input type="text" class="form-control" ng-model="consignment.consignee" ng-disabled="editConsignment == false">
                        </div>
                        <div class="row">
                            <div class="form-group col-md-6">
                                <label class="control-label">Mobile Phone</label>
                                <input type="text" class="form-control" ng-model="consignment.mobilePhone"  ng-disabled="editConsignment == false">
                            </div>
                            <div class="form-group col-md-6">
                                <label class="control-label">Office Phone</label>
                                <input type="text" class="form-control" ng-model="consignment.officePhone"  ng-disabled="editConsignment == false">
                            </div>
                        </div>
                        <div class="row">
                            <div class="form-group col-md-6">
                                <label class="control-label">Contents</label>
                                <input type="text" class="form-control" ng-model="consignment.contents"  ng-disabled="editConsignment == false">
                            </div>
                            <div class="form-group col-md-6">
                                <label class="control-label">Value</label>
                                <input type="number" class="form-control" ng-model="consignment.value"  ng-disabled="editConsignment == false">
                            </div>
                        </div>
                        <div class="form-group" style="margin-bottom: 9px;">
                            <label class="control-label">Description</label>
                            <div class="">
                                <textarea class="form-control" rows="2" style="resize: none;"  ng-disabled="editConsignment == false"
                                          ng-model="consignment.description"></textarea>
                            </div>
                        </div>

                        <div class="form-group">
                            <label class="control-label">Payment Mode: </label>
                            <strong class="text-primary" style="font-size: 16px">{{consignment.paidBy == 'CUSTOMER' ? 'TO-PAY' : 'PAID'}}</strong>
                        </div>

                        <div class="form-group">
                            <label class="control-label">Delivery Mode: </label>
                            <strong class="text-primary" style="font-size: 16px" ng-if="consignment.deliveryMode == 'WITH_PASS'">With Pass</strong>
                            <strong class="text-primary" style="font-size: 16px" ng-if="consignment.deliveryMode == 'DOOR_DELIVERY'">Door Delivery</strong>
                            <strong class="text-primary" style="font-size: 16px" ng-if="consignment.deliveryMode == 'ORDINARY'">Ordinary</strong>
                        </div>
                    </div>
                </div>
                <div class="col-md-4">
                    <h4>Transportation Information</h4>
                    <div style="height: 450px; padding: 20px;border: 1px solid #D0DDE1; border-radius: 5px; background-color: #F2F7F9;">
                        <div class="form-group">
                            <label class="control-label">Shipper</label>
                            <ui-select ng-model="consignment.shipper" theme="bootstrap" style="width:100%">
                                <ui-select-match placeholder="Select shipper">{{$select.selected.name}}</ui-select-match>
                                <ui-select-choices repeat="shipper in shippers | filter: $select.search">
                                    <div ng-bind-html="shipper.name | highlight: $select.search"></div>
                                </ui-select-choices>
                            </ui-select>
                        </div>

                        <div class="form-group">
                            <label class="control-label">Through</label>
                            <input type="text" class="form-control" ng-model="consignment.through"  ng-disabled="editConsignment == false">
                        </div>
                        <div class="form-group">
                            <label class="control-label">LR Number</label>
                            <div class="">
                                <input type="text" class="form-control" ng-model="consignment.confirmationNumber"  ng-disabled="editConsignment == false">
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="control-label">Vehicle</label>
                            <div class="">
                                <input type="text" class="form-control" ng-model="consignment.vehicle.number"  ng-disabled="editConsignment == false">
                            </div>
                        </div>

                        <div class="form-group">
                            <label class="control-label">Driver</label>
                            <div class="">
                                <input type="text" class="form-control" ng-model="consignment.driver.firstName"  ng-disabled="editConsignment == false">
                            </div>
                        </div>
                    </div>
                </div>
                <div class="col-md-4">
                    <h4>Shipping Address</h4>

                    <div style="height: 450px; padding: 20px;border: 1px solid #D0DDE1; border-radius: 5px; background-color: #F2F7F9;">
                        <div class="form-group">
                            <label class="control-label">Address</label>
                            <div class="">
                                <textarea class="form-control" rows="4" style="resize: none;"  ng-disabled="editConsignment == false""
                                          ng-model="consignment.shippingAddress.addressText"></textarea>
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="control-label">City/Town</label>
                            <div class="">
                                <input type="text" class="form-control" ng-model="consignment.shippingAddress.city"  ng-disabled="editConsignment == false">
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="control-label">State</label>
                            <div class="">
                                <ui-select ng-model="consignment.shippingAddress.state" theme="bootstrap" style="width:100%"  ng-disabled="editConsignment == false">
                                    <ui-select-match placeholder="Select state">{{$select.selected.name}}</ui-select-match>
                                    <ui-select-choices repeat="state in states | filter: $select.search">
                                        <div ng-bind-html="state.name | highlight: $select.search"></div>
                                    </ui-select-choices>
                                </ui-select>
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="control-label">Pincode</label>
                            <div class="">
                                <input type="text" class="form-control" ng-model="consignment.shippingAddress.pincode"  ng-disabled="editConsignment == false">
                            </div>
                        </div>
                    </div>
                </div>
            </div>
            <br><br>
        </div>
    </div>
</div>