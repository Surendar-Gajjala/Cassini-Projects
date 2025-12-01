<div class="modal-header">
    <h3>Select Shipments..</h3>
</div>
<div class="modal-body" style="padding: 20px; max-height: 500px;overflow-y: auto;">
    <div>
        <div class="pull-right text-center">
            <div class="btn-group" style="margin-bottom: 0">
                <button class='btn btn-md btn-default' ng-click='previousPage()' ng-disabled="shipments.first">
                    <i class="fa fa-chevron-left"></i>
                </button>
                <button class='btn btn-md btn-default' ng-click='nextPage()' ng-disabled="shipments.last">
                    <i class="fa fa-chevron-right"></i>
                </button>
            </div>
            <div ng-if="shipments.totalElements > 0">
                <span>{{pageable.page}} of {{shipments.totalPages}}</span>
            </div>
        </div>
    </div>

    Search : <input type="text" ng-model="searchText" ng-enter="searchShipments(searchText)">
    <div class="row" style="margin-bottom: 20px">
        <div class="col-md-4" style="margin-top: 20px;">
        </div>
    </div>

  <table class="table table-striped">
    <thead>
    <tr>
      <th style="width:100px; text-align: center;">Actions</th>
      <th>Order Number</th>
      <th>Customer</th>
      <th>Invoice Number</th>
      <th>PO Number</th>
    </tr>
    </thead>
    <tbody>
    <tr ng-repeat="shipment in shipments.content">
      <td class="actions-col">
        <div class="ckbox ckbox-default" style="margin-left: 12px; display: inline-block;">
          <input id="ship{{$index}}" name="shipSelected" ng-value="true" type="checkbox" ng-model="shipment.selected">
          <label for="ship{{$index}}" class="shipment-selection-checkbox"></label>
        </div>
      </td>
      <td>{{shipment.order.orderNumber}}</td>
      <td>{{shipment.order.customer.name}}</td>
      <td>{{shipment.invoiceNumber}}</td>
      <td>{{shipment.order.poNumber}}</td>
    </tr>
    </tbody>
  </table>
</div>
<div class="modal-footer">
    <div class="row">
        <div class="col-md-6">
        </div>
        <div class="modal-buttons" class="col-md-6">
            <button type="button" class="btn btn-sm btn-default" ng-click="cancel()">Cancel</button>
            <button type="button" class="btn btn-sm btn-success" ng-click="ok()">OK</button>
        </div>
    </div>
</div>