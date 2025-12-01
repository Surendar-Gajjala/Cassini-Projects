<div class="row">
    <div class="col-lg-3 col-md-5 col-sm-12" style="padding: 0px !important;">
        <treasure-overlay-spinner active='spinner.active'>
            <div style="background-color:#FFF;overflow: auto; height: 500px;padding: 0px 10px;margin-left: 10px;border: 1px solid #DDD;">
                <div id="custOnMap{{$index}}" class="map-results-customer-container box-shadow" ng-repeat="customer in customers.content"
                     ng-click="zoomSelectCustomer(customer)" ng-class="{'customer-selected-onmap': customer.marker.showWindow == true}">

                    <div class="customer-name text-primary">
                        <a href="" ng-click="showCustomerDetails(customer)">{{customer.name}}</a>

                        <span style="margin-left: 10px" ng-if="customer.geoLocation != null">
                            <i class="glyphicon glyphicon-map-marker"></i>
                        </span>
                    </div>
                    <div class="customer-location">
                        <span class="mr10"><strong class="mr5">Region:</strong>{{customer.salesRegion.name}}</span>
                        <span><strong class="mr5">District:</strong>{{customer.salesRegion.district}}</span>
                    </div>
                    <div class="customer-salesrep">
                        <span><strong class="mr5">Sales Rep:</strong>
                            <a href="" ng-click="showSalesRep(customer.salesRep)">{{customer.salesRep.firstName}}</a></span>
                    </div>
                </div>
            </div>
        </treasure-overlay-spinner>
    </div>
    <div class="col-lg-9 col-md-7 hidden-sm">
        <ui-gmap-google-map
                center="map.center"
                zoom="map.zoom"
                dragging="map.dragging"
                bounds="map.bounds"
                events="map.events"
                options="map.options"
                pan="true"
                control="map.control">
            <ui-gmap-marker ng-repeat="marker in map.markers" idkey="marker.id" coords="marker"
                            click="markerClicked(marker)"
                            ng-init="$parent.customer = getCustomerFromMarker(marker)">
                <ui-gmap-window show="marker.showWindow" closeClick="marker.closeClick">
                    <div class="map-results-customer-container" style="border: 0px; padding:0;margin:0">
                        <div style="margin-bottom: 10px;">
                            <div class="customer-name text-primary">
                                <a href="" ng-click="$parent.showCustomerDetails($parent.customer)">{{customer.name}}</a>
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
                            <!--<button class="btn btn-xs btn-success" ng-click="$parent.createOrder(customer)">Create Order</button>-->
                            <div class="btn-group btn-group-sm" fix-drop-down>
                                <button class="btn btn-success" type="button">
                                    <span class="mr10">Create Order</span><span class="caret"></span>
                                </button>
                                <ul class="dropdown-menu" role="menu">
                                    <li><a href="" ng-click="$parent.createOrder($parent.customer, 'PRODUCT')">Product Order</a></li>
                                    <li><a href="" ng-click="$parent.createOrder($parent.customer, 'SAMPLE')">Sample Order</a></li>
                                </ul>
                            </div>
                        </div>
                    </div>
                </ui-gmap-window>
            </ui-gmap-marker>
        </ui-gmap-google-map>
    </div>
</div>