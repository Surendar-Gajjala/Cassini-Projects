<div>
    <div class="report-close-button pull-right">
        <span title="Close" style="cursor: pointer" ng-click="closeShipments()"><i class="fa fa-times-circle"></i></span>
    </div>
</div>
<div>
    <span class="text-muted">Select one or more shipments</span>
    <br>
    <table class="table table-striped">
        <thead>
        <tr>
            <th style="width:100px; text-align: center;">Actions</th>
            <th>Invoice Number</th>
            <th>Order Number</th>
            <th>Customer</th>
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
            <td style="width: 200px;">
                <table-filter placeholder="Invoice Number" filters="filters"
                              property="invoiceNumber" apply-filters="applyFilters()" />
            </td>
            <td>
                <table-filter placeholder="Order Number" filters="filters"
                              property="orderNumber" apply-filters="applyFilters()" />
            </td>
            <td>
                <table-filter placeholder="Customer" filters="filters"
                              property="customer" apply-filters="applyFilters()" />
            </td>
        </tr>

        <tr ng-if="shipments.totalElements == 0 || loading == true">
            <td colspan="7">
                <span ng-hide="loading">There are no shipments to ship</span>
                <span ng-show="loading">
                    <img src="app/assets/images/loaders/loader19.gif" class="mr5">Loading shipments...
                </span>
            </td>
        </tr>

        <tr ng-repeat="shipment in shipments.content"
            class="order-row">
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
            </td>
            <td style="background-color:#F2F7F9" colspan="4"
                ng-include="shipment.showDetails ? 'app/components/crm/order/shipment/shipmentDetails.jsp' : null">
            </td>
            <td ng-if="!shipment.showDetails" style="width: 200px">{{shipment.invoiceNumber}}</td>
            <td ng-if="!shipment.showDetails" style="text-align: left">{{shipment.order.orderNumber}}</td>
            <td ng-if="!shipment.showDetails" style="text-align: left">{{shipment.order.customer.name}}</td>
        </tr>
        </tbody>
    </table>
</div>