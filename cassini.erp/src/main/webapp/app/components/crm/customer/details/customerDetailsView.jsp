<script type="text/ng-template" id="customer-details-tb">
    <div>
        <button class="btn btn-sm btn-default" ng-click="edit()" ng-if="editMode == false" style="width: 115px">Update Info</button>
        <button class="btn btn-sm btn-danger" ng-click="blacklistCustomer()" ng-if="hasRole('Administrator') == true && customer.blacklisted == false" style="min-width: 115px">Blacklist</button>
        <button class="btn btn-sm btn-info" ng-click="removeBlacklist()" ng-if="hasRole('Administrator') == true && customer.blacklisted == true" style="min-width: 115px">Remove from Blacklist</button>
        <button class="btn btn-sm btn-default" ng-click="saveEdits()" ng-if="editMode == true" style="width: 115px">Save Info</button>

        <!--<button class="btn btn-primary" ng-click="createOrder(customer)">Create Order</button>-->
        <div class="btn-group" style="margin-bottom: 0" fix-drop-down>
            <button class="btn btn-sm btn-primary" type="button">
                <span class="mr10">Create Order</span><span class="caret"></span>
            </button>
            <ul class="dropdown-menu">
                <li><a href="" ng-click="createOrder($parent.customer, 'PRODUCT')">Product Order</a></li>
                <li><a href="" ng-click="createOrder($parent.customer, 'SAMPLE')">Sample Order</a></li>
            </ul>
        </div>

        <button class="btn btn-sm btn-warning" ng-click="newReturn()">Create Return</button>
        <button class="btn btn-sm btn-success" ng-click="createReport()">Create Report</button>
        <span class="mr20"></span>
        <strong><span class="blink" style="color: red;" ng-if="customer.blacklisted == true">WARNING: This customer has been blacklisted</span></strong>
    </div>
</script>
<div class="row" style="margin-bottom: 20px;">
    <div class="col-sm-4 col-md-5 col-lg-3 customer-details-map styled-panel">
        <div style="border: 1px solid #DDD;border-radius: 3px;padding: 5px;background-color: #FFF">
            <ui-gmap-google-map
                    center="map.center"
                    zoom="map.zoom"
                    dragging="map.dragging"
                    bounds="map.bounds"
                    events="map.events"
                    options="map.options"
                    pan="true"
                    control="map.control">
                <ui-gmap-marker idkey="marker.id" coords="marker"></ui-gmap-marker>
            </ui-gmap-google-map>
        </div>
        <div style="text-align: center;"><a href="">Other customers nearby</a></div>

        <div ng-if="editMode == true">
            <br/>
            <h5 class="subtitle">Geo Location</h5>
            <div class="row">
                <div class="col-sm-6">
                    <input placeholder="Latitude" class="form-control" type="number" ng-model="geoLocation.latitude"/>
                </div>
                <div class="col-sm-6">
                    <input placeholder="Longitude" class="form-control" type="number" ng-model="geoLocation.longitude"/>
                </div>
            </div>
        </div>

        <br/>
        <div>
            <h5 class="subtitle">Address</h5>
            <div ng-if="editMode == false">
                <address>
                    {{customer.customerAddresses[0].addressText}}
                </address>
            </div>
            <div ng-if="editMode == true">
                <div class="row" style="margin-bottom: 5px">
                    <div class="col-sm-12">
                        <textarea placeholder="Address" class="form-control" rows="5" style="resize: none"
                                  ng-model="customer.customerAddresses[0].addressText"></textarea>
                    </div>
                </div>
            </div>
            <div ng-if="editMode == false">
                <div class="row" style="margin-bottom: 5px">
                    <div class="col-sm-12">
                        <strong class="mr5">City: </strong>{{customer.customerAddresses[0].city}} <span class="mr20"></span>
                        <strong class="mr5">Pincode: </strong>{{customer.customerAddresses[0].pincode}}
                    </div>
                </div>
            </div>
            <div ng-if="editMode == true">
                <div class="row" style="margin-bottom: 5px">
                    <div class="col-sm-6">
                        <input class="form-control" type="text" placeholder="City" ng-model="customer.customerAddresses[0].city"/>
                    </div>
                    <div class="col-sm-6">
                        <input class="form-control" type="text" placeholder="Pincode" ng-model="customer.customerAddresses[0].pincode"/>
                    </div>
                </div>
            </div>
        </div>
        <br/>

        <h5 class="subtitle">Contact Information</h5>
        <div ng-if="editMode == false">
            <div style="margin-bottom: 10px">
                <strong class="mr5">Office Phone: </strong>{{customer.officePhone}}
            </div>
            <div style="margin-bottom: 10px">
                <strong class="mr5">Office Fax: </strong>{{customer.officeFax}}
            </div>
            <div style="margin-bottom: 10px">
                <strong class="mr5">Office Email: </strong>{{customer.officeEmail}}
            </div>
            <br/>
            <div style="margin-bottom: 10px">
                <strong class="mr5">Contact Person: </strong>{{customer.contactPerson.firstName}}
            </div>
            <div style="margin-bottom: 10px">
                <strong class="mr5">Phone: </strong>{{customer.contactPerson.phoneMobile}}
            </div>
            <div style="margin-bottom: 10px">
                <strong class="mr5">Email: </strong>{{customer.contactPerson.email}}
            </div>
        </div>
        <div ng-if="editMode == true">
            <div class="row" style="margin-bottom: 5px">
                <div class="col-sm-12">
                    <input placeholder="Office Phone" class="form-control" type="text" ng-model="customer.officePhone"/>
                </div>
            </div>
            <div class="row" style="margin-bottom: 5px">
                <div class="col-sm-12">
                    <input placeholder="Office Fax" class="form-control" type="text" ng-model="customer.officeFax"/>
                </div>
            </div>
            <div class="row" style="margin-bottom: 5px">
                <div class="col-sm-12">
                    <input placeholder="Office Email" class="form-control" type="text" ng-model="customer.officeEmail"/>
                </div>
            </div>
            <br/>
            <div class="row" style="margin-bottom: 5px">
                <div class="col-sm-12">
                    <input placeholder="Contact Person" class="form-control" type="text" ng-model="customer.contactPerson.firstName"/>
                </div>
            </div>
            <div class="row" style="margin-bottom: 5px">
                <div class="col-sm-12">
                    <input placeholder="Phone" class="form-control" type="text" ng-model="customer.contactPerson.phoneMobile"/>
                </div>
            </div>
            <div class="row" style="margin-bottom: 5px">
                <div class="col-sm-12">
                    <input placeholder="Email" class="form-control" type="text" ng-model="customer.contactPerson.email"/>
                </div>
            </div>
        </div>
    </div>
    <div class="col-sm-8 col-md-7 col-lg-9">
        <div>
            <h2 class="profile-name" style="padding-top: 0" ng-if="editMode == false">{{customer.name}}</h2>
            <div ng-if="editMode == true">
                <span><strong class="mr5">Customer Name: </strong></span>
                <div ng-if="editMode == true" style="display: inline-block;">
                    <input placeholder="Customer Name" style="width: 400px; padding: 5px; height: 40px; border: 1px solid gainsboro" type="text" ng-model="customer.name"/>
                </div>
                <br><br>
            </div>

            <div ng-if="editMode == false">
                <strong class="mr5">Region: </strong><span class="mr20">{{customer.salesRegion.name}}</span>
                <strong class="mr5">District: </strong><span class="mr20">{{customer.salesRegion.district}}</span>
                <strong class="mr5">State: </strong>{{customer.salesRegion.state.name}}<br/>
            </div>

            <div ng-if="editMode == true">
                <strong class="mr5">Region: </strong>
                <div ng-if="editMode == true" style="display: inline-block;">
                    <ui-select ng-model="customer.salesRegion" theme="bootstrap" style="width:150px">
                        <ui-select-match placeholder="Select region">{{$select.selected.name}}</ui-select-match>
                        <ui-select-choices repeat="region in salesRegions | filter: $select.search">
                            <div ng-bind-html="region.name | highlight: $select.search"></div>
                        </ui-select-choices>
                    </ui-select>
                </div>
                <br><br>
            </div>
            <strong class="mr5">Sales Rep: </strong>
                <a href="" ng-click="showSalesRep(customer.salesRep)" ng-if="editMode == false">{{customer.salesRep.firstName}}</a>
                <div ng-if="editMode == true" style="display: inline-block;">
                    <ui-select ng-model="customer.salesRep" theme="bootstrap" style="width:150px">
                        <ui-select-match placeholder="Select sales rep">{{$select.selected.firstName}}</ui-select-match>
                        <ui-select-choices repeat="salesRep in salesReps | filter: $select.search">
                            <div ng-bind-html="salesRep.firstName | highlight: $select.search"></div>
                        </ui-select-choices>
                    </ui-select>
                </div>
        </div>

        <br/>

        <br/>
        <div style="margin-bottom: 30px;">
            <tabset>
                <tab heading="Orders" active="tabs.orders.active">
                    <div ng-include="tabs.orders.template" ng-controller="CustomerOrdersController"></div>
                </tab>
                <tab heading="Returns" active="tabs.returns.active">
                    <div ng-include="tabs.returns.template" ng-controller="CustomerReturnsController"></div>
                </tab>
                <tab heading="Reports" active="tabs.reports.active">
                    <div ng-include="tabs.reports.template" ng-controller="CustomerReportsController"></div>
                </tab>
            </tabset>
        </div>
    </div>
</div>