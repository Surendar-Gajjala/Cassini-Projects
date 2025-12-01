<div>
    <div class="row">
        <div class="pull-left" style="margin-top: -5px;">
            <button class="btn btn-sm btn-primary mr10" style="width: 75px;margin-top: 20px;" ng-click="shipConsignment()" ng-hide="shipped == true">Ship</button>
            <button class="btn btn-sm btn-default mr10" style="width: 75px;margin-top: 20px;" ng-click="closeConsignment()">Close</button>
            <button class="btn btn-sm btn-default mr10" style="margin-top: 20px;" ng-click="printConsignment()" ng-if="shipped == true">
                <i class="glyphicon glyphicon-print"></i>
            </button>
            <button class="btn btn-sm btn-default" ng-click="printConsignmentShipments()" title="Print Invoices" ng-if="shipped == true">
                <i class="glyphicon glyphicon-print" style="color: blue"></i>
            </button>
        </div>
        <h3 class="text-primary text-center section-title" style="padding-right: 130px;">
            New Consignment
        </h3>
        <div class="col-md-8 col-md-offset-2 col-lg-8 col-lg-offset-2">
            <div class="row">
                <div class="col-md-12">
                    <br><br>
                    <div>
                        <h4 style="margin-bottom: 0">Shipments</h4>
                        <button class="btn btn-sm btn-primary mr10" ng-click="addShipments()">AddShipment</button>
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
                <div class="col-md-6">
                    <h4>Consignment Information</h4>
                    <div style="padding: 20px;border: 1px solid #D0DDE1; border-radius: 5px; background-color: #F2F7F9;">
                        <div class="form-group">
                            <div class="col-sm-3" style="padding: 0">
                                <label>Mode of Payment:</label>
                            </div>
                            <div class="col-sm-3">
                                <label>
                                    <input type="radio" ng-model="consignment.paidBy"
                                           value='CUSTOMER' checked>&nbsp;&nbsp;To Pay
                                </label>
                            </div>
                            <div class="col-sm-3">
                                <label>
                                    <input type="radio" ng-model="consignment.paidBy" value='SELF'>&nbsp;&nbsp;Paid
                                </label>
                            </div>
                        </div>
                        <div class="form-group" ng-if="consignment.paidBy == 'SELF'">
                            <label class="control-label">Shipping Cost</label>
                            <input type="number" class="form-control" ng-model="consignment.shippingCost" ng-disabled="shipped == true">
                        </div>

                        <div class="form-group">
                            <div class="col-sm-3" style="padding: 0">
                                <label>Delivery Mode:</label>
                            </div>
                            <div class="col-sm-3">
                                <label>
                                    <input type="radio" ng-model="consignment.deliveryMode"
                                           value='WITH_PASS' checked>&nbsp;&nbsp;With Pass
                                </label>
                            </div>
                            <div class="col-sm-3">
                                <label>
                                    <input type="radio" ng-model="consignment.deliveryMode"
                                           value='DOOR_DELIVERY'>&nbsp;&nbsp;Door Delivery
                                </label>
                            </div>
                            <div class="col-sm-3">
                                <label>
                                    <input type="radio" ng-model="consignment.deliveryMode"
                                           value='ORDINARY'>&nbsp;&nbsp;Ordinary
                                </label>
                            </div>
                        </div>

                        <div class="form-group">
                            <label class="control-label">Shipper</label>
                            <ui-select ng-model="consignment.shipper" theme="bootstrap" style="width:100%" ng-disabled="shipped == true">
                                <ui-select-match placeholder="Select shipper">{{$select.selected.name}}</ui-select-match>
                                <ui-select-choices repeat="shipper in shippers | filter: $select.search">
                                    <div ng-bind-html="shipper.name | highlight: $select.search"></div>
                                </ui-select-choices>
                            </ui-select>
                        </div>
                        <div class="form-group">
                            <label class="control-label">Consignee</label>
                            <input type="text" class="form-control" ng-model="consignment.consignee" ng-disabled="shipped == true">
                        </div>
                        <div class="row">
                            <div class="form-group col-md-6">
                                <label class="control-label">Mobile Phone</label>
                                <input type="text" class="form-control" ng-model="consignment.mobilePhone" ng-disabled="shipped == true">
                            </div>
                            <div class="form-group col-md-6">
                                <label class="control-label">Office Phone</label>
                                <input type="text" class="form-control" ng-model="consignment.officePhone" ng-disabled="shipped == true">
                            </div>
                        </div>
                        <div class="row">
                            <div class="form-group col-md-6">
                                <label class="control-label">Contents</label>
                                <input type="text" class="form-control" ng-model="consignment.contents" ng-disabled="shipped == true">
                            </div>
                            <div class="form-group col-md-6">
                                <label class="control-label">Value</label>
                                <input type="number" class="form-control" ng-model="consignment.value" ng-disabled="shipped == true">
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="control-label">Description</label>
                            <div class="">
                                <textarea class="form-control" rows="2" style="resize: none;" ng-disabled="shipped == true"
                                          ng-model="consignment.description"></textarea>
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="control-label">Through</label>
                            <input type="text" class="form-control" ng-model="consignment.through" ng-disabled="shipped == true">
                        </div>
                    </div>
                </div>
                <div class="col-md-6">
                    <h4>Shipping Address</h4>

                    <div style="padding: 20px;border: 1px solid #D0DDE1; border-radius: 5px; background-color: #F2F7F9;">
                        <div class="form-group">
                            <label class="control-label">Address</label>
                            <div class="">
                                <textarea class="form-control" rows="15" style="resize: none;" ng-disabled="shipped == true"
                                          ng-model="consignment.shippingAddress.addressText"></textarea>
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="control-label">City/Town</label>
                            <div class="">
                                <input type="text" class="form-control" ng-model="consignment.shippingAddress.city" ng-disabled="shipped == true">
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="control-label">State</label>
                            <div class="">
                                <ui-select ng-model="consignment.shippingAddress.state" theme="bootstrap" style="width:100%" ng-disabled="shipped == true">
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
                                <input type="text" class="form-control" ng-model="consignment.shippingAddress.pincode" ng-disabled="shipped == true">
                            </div>
                        </div>
                    </div>
                </div>
            </div>
            <br><br>
        </div>
    </div>
</div>