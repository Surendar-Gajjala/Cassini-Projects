<div class="row" style="margin-top: 100px; margin-bottom: 100px;">
    <div class="text-center styled-panel"
         style="width: 800px; height: 290px;padding-top: 60px;margin-left: auto;margin-right: auto;">

        <div class="row">
            <div class="col-sm-12">
                <button class="create-product-order-btn" style="margin-right: 50px;"
                        ng-click="goTo('app.crm.shippinghome.approvedorders')">
                    Approved Orders
                    <span ng-if="approvedOrders != null">({{approvedOrders}})</span>
                </button>
                <button class="create-sample-order-btn" ng-click="goTo('app.crm.shippinghome.pending')">
                    Pending Shipments
                    <span ng-if="pendingShipments != null">({{pendingShipments}})</span>
                </button>
            </div>
        </div>
        <br>
        <div class="row">
            <div class="col-sm-12">
                <button class="approved-orders-btn" style="margin-right: 50px;"
                        ng-click="goTo('app.crm.shippinghome.shipped')">Shipped Consignments</button>
                <button class="finished-consignments-btn"
                        ng-click="goTo('app.crm.shippinghome.finished')">Finished Consignments</button>
            </div>
        </div>
    </div>
</div>