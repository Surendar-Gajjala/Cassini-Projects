<li ng-if="hasRole('Administrator') && returnsNotification.pendingReturns.totalElements > 0">
  <div class="btn-group" dropdown>
    <button title="New Returns" class="btn btn-default dropdown-toggle tp-icon"  dropdown-toggle>
      <i class="fa fa-list"></i>
      <span class="badge pending-return-badge">{{returnsNotification.pendingReturns.totalElements}}</span>
    </button>
    <div class="widget-messaging dropdown-menu dropdown-menu-head pull-right"
         style="width: 600px;">
      <div class="panel-heading" style="background-color: #E4E7EA;border: 1px solid #D7D7D7; padding:0; height: 56px">
        <div class="row">
          <div class="panel-title col-sm-8" style="font-size:15px; padding: 20px 0 0 20px">New Returns</div>
          <div class="col-sm-4" style="padding: 10px 26px 0 0; text-align: right" ng-if="returnsNotification.pendingReturns.totalElements > 0">
            <button style="width: 128px;margin-top: 5px;margin-right: 10px;"
                    class="btn btn-xs btn-primary" ng-click="returnsApprove();">Approve All</button>
          </div>
        </div>
      </div>

      <div style="max-height: 400px; overflow: auto;border-bottom: 1px solid #DDD;">
        <ul class="dropdown-list gen-list new-orders" style="padding: 10px">
          <li ng-if="returnsNotification.pendingReturns.totalElements == 0" style="padding-left: 10px">
            <span>No pending Returns</span>
          </li>
          <li ng-repeat="return in returnsNotification.pendingReturns.content" class="order-item"
              style="margin-bottom:10px;border-left: 10px solid #5BC0DE !important;">
            <div class="row" style="padding: 0px 10px;">
              <div class="col-sm-2 text-center" style="padding-top: 0px">
                <h4 class="text-primary" style="margin:-3px;">
                  <a href="" ng-click="openReturn(return)" style="color: #428BCA; font-size: 16px;">{{return.customer.id}}</a>
                </h4>
                <span class="text-muted">{{return.returnDate}}</span>
              </div>
              <div class="col-sm-4">
                <h4 class="sender" style="margin-bottom: 3px">
                  <a style="color:#428BCA !important;" href=""
                     ng-click="showCustomerDetails(return.customer)">{{return.customer.name}}</a>
                </h4>
                <small>Region: <strong class="mr10">{{return.customer.salesRegion.name}}</strong>
                  Sales Rep: <strong>{{return.customer.salesRep.firstName}}</strong></small>
              </div>

              <div class="col-sm-4" style="text-align: right">
                <button class="btn btn-xs btn-primary" ng-click="approveReturn(return)">Approve</button>
                <button class="btn btn-xs btn-danger" ng-click="cancelReturn(return)">Reject</button>
              </div>
            </div>
          </li>
          <li style="display:none"></li>
          <li ng-if="returnsNotification.pendingReturns.totalElements > 10">
            <a href="" ng-click="showMoreReturns('PENDING')"><h4 class="text-primary text-center">Show More</h4></a>
          </li>
        </ul>
      </div>
    </div>
  </div>
</li>



