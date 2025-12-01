<script type="text/ng-template" id="pendingshipments-view-tb">
    <div>
        <button class="btn btn-sm btn-default" ng-click="loadShipments()"><i class="fa fa-refresh"></i></button>
        <button class="btn btn-sm btn-primary" ng-disabled="selectedShipments.length == 0" ng-if="selection == 'selected' && $parent.mode == 'all'"
                ng-click="shipSelectedItems()">Ship Selected Items</button>
        <button class="btn btn-sm btn-primary" ng-click="Back()">Home</button>
    </div>
</script>

<div ng-if="$parent.mode == 'all'">
    <div class="row">
        <div class="col-xs-12">
            <div class="row">
                <div class="col-md-4" style="padding-top: 30px;">
                    <span class="text-success" ng-if="shipments.totalElements > 0">Select one or more shipments to create a consignment</span>
                </div>
                <div class="col-md-8"  style="text-align: right" ng-show="shipments.numberOfElements > 0">
                    <div style="text-align: right;">
                        <pagination total-items="shipments.totalElements"
                                    items-per-page="pageable.size"
                                    max-size="5"
                                    boundary-links="true"
                                    ng-model="pageable.page"
                                    ng-change="pageChanged()">
                        </pagination>
                    </div>

                    <div style="margin-top: -25px;">
                        <small>Total {{shipments.totalElements}} pending {{shipments.totalElements > 1 ? 'shipments' : 'shipment'}}</small>
                    </div>
                </div>
            </div>
            <div class="responsive-table">
                <table class="table table-striped">
                    <thead>
                    <tr>
                        <th style="width:100px; text-align: center;">Actions</th>
                        <th style="width: 150px;">Status</th>
                        <th>Date</th>
                        <th style="width: 200px;">Invoice Number</th>
                        <th>PO Number</th>
                        <th>Invoice Amount</th>
                        <th>Order Number</th>
                        <th>Customer</th>
                        <th>Ship To</th>
                    </tr>
                    </thead>
                    <tbody>
                    <tr>
                        <td style="width:100px; text-align: center;">
                            <div class="btn-group" style="margin-bottom: 0px;">
                                <button title="Apply Filters"  type="button" class="btn btn-sm btn-success" ng-click="applyFilters()">
                                    <i class="fa fa-search"></i>
                                </button>
                                <button title="Clear Filters" type="button" class="btn btn-sm btn-default" ng-click="resetFilters()">
                                    <i class="fa fa-times"></i>
                                </button>
                            </div>
                        </td>

                        <td style="width: 100px;">
                            <table-filter placeholder="Status" filters="filters" disabled="true"
                                          property="status" apply-filters="applyFilters()" />
                        </td>
                        <td style="text-align: center;width: 200px;">
                            <input placeholder="Date"
                                   style="width: 100%; text-align: left"
                                   options="dateRangeOptions"
                                   date-range-picker
                                   clearable="true"
                                   class="form-control date-picker"
                                   ng-enter="applyFilters()"
                                   ng-class="{ hasFilter: (filters.createdDate.startDate != null && filters.createdDate.endDate != '') }"
                                   type="text" ng-model="filters.createdDate" />
                        </td>
                        <td style="width: 200px;">
                            <table-filter placeholder="Invoice Number" filters="filters"
                                          property="invoiceNumber" apply-filters="applyFilters()" />
                        </td>
                        <td style="width: 200px;">
                            <table-filter placeholder="PO Number" filters="filters"
                                          property="poNumber" apply-filters="applyFilters()" />
                        </td>
                        <td style="width: 200px;">
                            <table-filter placeholder="Invoice Amount" filters="filters"
                                          property="invoiceAmount" apply-filters="applyFilters()" />
                        </td>
                        <td style="width: 200px;">
                            <table-filter placeholder="Order Number" filters="filters"
                                          property="orderNumber" apply-filters="applyFilters()" />
                        </td>
                        <td>
                            <table-filter placeholder="Customer" filters="filters"
                                          property="customer" apply-filters="applyFilters()" />
                        </td>
                        <td>
                            <table-filter placeholder="Ship To" filters="filters"
                                          property="shipTo" apply-filters="applyFilters()" />
                        </td>
                    </tr>

                    <tr ng-if="shipments.totalElements == 0 || loading == true">
                        <td colspan="9">
                            <span ng-hide="loading">There are no pending shipments</span>
                            <span ng-show="loading">
                                <img src="app/assets/images/loaders/loader19.gif" class="mr5">Loading shipments...
                            </span>
                        </td>
                    </tr>

                    <tr ng-repeat="shipment in shipments.content" class="order-row">
                        <td style="background-color:#F2F7F9" colspan="9"
                            ng-include="shipment.showDetails ? 'app/components/crm/order/shipment/shipmentDetails.jsp' : null">
                        </td>

                        <td class="actions-col" ng-if="!shipment.showDetails">
                            <span ng-click="toggleShipmentDetails(shipment);" style="cursor: pointer">
                                <span title="Show Details" ng-if="shipment.detailsOpen == false"><i class="fa fa-plus-circle mr5"></i></span>
                                <span title="Hide Details" ng-if="shipment.detailsOpen == true"><i class="fa fa-minus-circle mr5"></i></span>
                            </span>
                            <div class="ckbox ckbox-default" style="margin-left: 12px; display: inline-block;">
                                <input id="ship{{$index}}" name="shipSelected" ng-value="true" type="checkbox"
                                       ng-model="shipment.selected" ng-click="toggleSelection(shipment)">
                                <label for="ship{{$index}}" class="shipment-selection-checkbox"></label>
                            </div>
                        </td>0
                        <td ng-if="!shipment.showDetails" style="width: 150px; text-align: center;">
                            <span ng-class="{'shipment-pending': shipment.status == 'PENDING',
                                    'shipment-shipped': shipment.status == 'SHIPPED'}">{{shipment.status}}</span>
                        </td>
                        <td ng-if="!shipment.showDetails" style="width: 200px">{{shipment.createdDate}}</td>
                        <td ng-if="!shipment.showDetails" style="width: 200px">
                            <a href="#" editable-text="shipment.invoiceNumber"
                               onaftersave="saveShipment(shipment)">
                                {{shipment.invoiceNumber  || 'Click to set value'}}
                            </a>
                        </td>
                        <td ng-if="!shipment.showDetails" style="width: 200px">{{shipment.order.poNumber}}</td>
                        <td ng-if="!shipment.showDetails" style="width: 200px">{{shipment.invoiceAmount | currency:"&#x20b9; ":0}}</td>
                        <td ng-if="!shipment.showDetails" style="width: 200px;text-align: left">{{shipment.order.orderNumber}}</td>
                        <td ng-if="!shipment.showDetails" style="text-align: left">{{shipment.order.customer.name}}</td>
                        <td ng-if="!shipment.showDetails" style="text-align: left">{{shipment.order.shipTo}}</td>
                    </tr>
                    </tbody>
                </table>
            </div>
        </div>
    </div>
</div>


<div ng-if="$parent.mode == 'new'" ng-include="tpls.newConsignment" ng-controller="NewConsignmentController">

</div>