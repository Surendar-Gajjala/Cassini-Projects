<div ng-controller="CustomerInfoWindowController">
    <div class="map-results-customer-container" style="border: 0px; padding:0;margin:0">
        <div style="margin-bottom: 10px;">
            <div class="customer-name text-primary">
                <a href="" ng-click="showCustomerDetails(customer)">{{customer.name}}</a>
            </div>
            <div class="customer-location">
                <span class="mr10"><strong class="mr5">Region:</strong>{{customer.salesRegion.name}}</span>
                <span><strong class="mr5">Sales Rep:</strong>{{customer.salesRep.firstName}}</span>
            </div>
            <div class="customer-location">
                <span class="mr10"><strong class="mr5">Contact Person:</strong>{{customer.contactPerson.firstName}}</span>
                <span><strong class="mr5">Phone:</strong>{{getContactPhone(customer)}}</span>
            </div>
        </div>
        <div>
            <!--<button class="btn btn-xs btn-success" ng-click="createOrder(customer)">Create Order</button>-->
            <div class="btn-group btn-group-sm" fix-drop-down>
                <button dropdown-toggle class="btn btn-success"  type="button">
                    <span class="mr10">Create Order</span><span class="caret"></span>
                </button>
                <ul class="dropdown-menu" role="menu">
                    <li><a href="" ng-click="createOrder(customer, 'PRODUCT')">Product Order</a></li>
                    <li><a href="" ng-click="createOrder(customer, 'SAMPLE')">Sample Order</a></li>
                </ul>
            </div>
        </div>
    </div>
</div>