<div class="panel panel-default panel-alt widget-messaging" style="height: 400px">
  <div class="panel-heading" style="background-color: #E4E7EA; padding:0; height: 56px">
    <div class="row">
      <div class="panel-title col-xs-12 col-sm-12 col-md-6 col-lg-6" style="font-size:15px; padding: 20px 0 0 20px">
        Customer Returns
      </div>
      <div class="col-xs-12 col-sm-12 col-md-6 col-lg-6" style="padding-top: 12px;">
        <div class="btn-group" style="margin-top: 3px;">
          <button class="btn btn-xs btn-info" ng-click="setMode('pending')">
                        <span class="pull-right badge"
                              style="margin: 3px 0 0 10px; background-color:#D43F3A">{{returnsNotification.pendingReturns.totalElements}}</span>
            <span>Pending</span>
          </button>
          <button class="btn btn-xs btn-success" ng-click="setMode('approved')">
                        <span class="pull-right badge"
                              style="margin: 3px 0 0 10px; background-color:#D43F3A">{{returnsNotification.approvedReturns.totalElements}}</span>
            <span>Approved</span>
          </button>
        </div>
      </div>
      <div class="col-xs-12 col-sm-12 col-md-3 col-lg-3" style="padding: 10px 25px 0 0; text-align: right">
        <button style="width: 128px;margin-top: 3px;" class="btn btn-xs btn-primary" ng-if="returnsNotification.pendingReturns.length > 0"
                ng-click="returnsApprove()" ng-show="mode == 'pending'">Approve All</button>
      </div>
    </div>
  </div>
  <div class="panel-body">
    <div style="max-height: 400px; overflow: auto;">
      <ul ng-if="mode == 'pending'" style="max-height: 400px; overflow: auto; padding: 5px;height: 330px;">
        <li ng-if="returnsNotification.pendingReturns.totalElements == 0">
          <span>No pending Returns</span>
        </li>
        <li ng-repeat="return in returnsNotification.pendingReturns.content"
            class="new-customer-order" style="padding: 10px; border-left: 10px solid #5BC0DE">
          <div class="row">
            <div class="col-sm-2 text-center" style="padding-top: 12px">
              <h4 class="text-primary" style="margin:0"><a href="" ng-click="openReturn(return)">{{return.customer.id}}</a></h4>
              <span class="text-muted">{{return.returnDate}}</span>
            </div>
            <div class="col-sm-4">
              <div class="text-primary" style="font-size: 16px; margin-bottom: 5px">
                <a href="" ng-click="showCustomerDetails(return.customer)">{{return.customer.name}}</a>
              </div>
              <small><strong class="mr5">Region:</strong> {{return.customer.salesRegion.name}} <span class="mr10"></span>
                <strong class="mr5">Sales Rep:</strong>
                <a href="" ng-click="showSalesRep(return.customer.salesRep)">{{return.customer.salesRep.firstName}}</a></small>
              <br/><small class="mr20"><strong class="mr5">Returned Date:</strong> {{return.history[0].modifiedDate}}</small>

            </div>
            <div class="col-sm-4" style="text-align: right;padding-top: 15px;">
              <button class="btn btn-xs btn-primary" ng-click="approveReturn(return)">Approve</button>
              <button class="btn btn-xs btn-danger" ng-click="cancelReturn(return)">Reject</button>
            </div>
          </div>
        </li>
        <li ng-if="returnsNotification.pendingReturns.totalElements > 10">
          <a href="" ng-click="showMore('PENDING')"><h4 class="text-primary text-center">Show More</h4></a>
        </li>
      </ul>
      <ul ng-if="mode == 'approved'" style="max-height: 400px; overflow: auto; padding: 5px;height: 330px;">
        <li ng-if="returnsNotification.approvedReturns.totalElements  == 0">
          <span>No approved returns</span>
        </li>
        <li ng-repeat="return in returnsNotification.approvedReturns.content"
            class="new-customer-order" style="padding: 10px; border-left: 10px solid #1CAF9A">
          <div class="row">
            <div class="col-sm-2 text-center" style="padding-top: 12px">
              <h4 class="text-primary" style="margin:0"><a href="" ng-click="openReturn(return)">{{return.customer.id}}</a></h4>
              <span class="text-muted">{{return.customer.id}}</span>
            </div>
            <div class="col-sm-7">
              <div class="text-primary" style="font-size: 16px; margin-bottom: 5px">
                <a href="" ng-click="showCustomerDetails(return.customer)">{{return.customer.name}}</a>
              </div>
              <small><strong class="mr5">Region:</strong> {{return.customer.salesRegion.name}} <span class="mr10"></span>
                <strong class="mr5">Sales Rep:</strong>
                <a href="" ng-click="showSalesRep(return.customer.salesRep)">{{return.customer.salesRep.firstName}}</a></small>
              <br/><small class="mr20"><strong class="mr5">Returned Date:</strong> {{return.history[0].modifiedDate}}</small>

            </div>
          </div>
        </li>
        <li ng-if="returnsNotification.approvedReturns.totalElements > 10">
          <a href="" ng-click="showMore('APPROVED')"><h4 class="text-primary text-center">Show More</h4></a>
        </li>
      </ul>
    </div>
  </div><!-- panel-body -->
</div><!-- panel -->