<script type="text/ng-template" id="newcustomer-view-tb">
    <div>
        <button class="btn btn-sm btn-default mr10" ng-click="showAllCustomers()">Show All Customers</button>
        <button class="btn btn-sm btn-primary mr10" ng-click="createNewCustomer()" ng-if="customerCreated == false" style="width: 80px;">Create</button>
        <button class="btn btn-sm btn-success" ng-click="createAnotherCustomer()" ng-if="createAnother">Create Another</button>
    </div>
</script>
<div>
    <hr/>
    <div style="">
        <br/>
        <div class="row">
            <div class="col-sm-12 col-md-12 col-lg-4 col-lg-offset-4">
                <div class="alert alert-danger" ng-show="error.hasError" ng-cloak>
                    {{ error.errorMessage }}
                </div>
            </div>
        </div>
        <div class="row">
            <div class="styled-panel col-sm-12 col-md-6 col-lg-4 col-lg-offset-2 mr20">
                <h4 class="text-primary">Geo Location</h4>
                <div style="border: 1px solid #DDD;border-radius: 3px;padding: 5px;" class="new-customer-map">
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
                <br/>
                <div class="row">
                    <div class="col-sm-6">
                        <input placeholder="Latitude" class="form-control" type="number" ng-model="customer.geoLocation.latitude"/>
                    </div>
                    <br class="hidden-lg hidden-md"/>
                    <div class="col-sm-6">
                        <input placeholder="Longitude" class="form-control" type="number" ng-model="customer.geoLocation.longitude"/>
                    </div>
                </div>
            </div>

            <br class="hidden-sm hidden-lg hidden-md"/>
            <div class="styled-panel col-sm-12 col-md-6 col-lg-4">
                <h4 class="text-primary">Customer Information</h4>
                <div class="row">
                    <div class="col-sm-12">
                        <div class="form-group">
                            <label class="control-label">Customer Name</label>
                            <input class="form-control required-field" type="text" ng-model="customer.name" ng-blur="validateCustmerName()"/>
                        </div>
                    </div>
                </div>
                <div class="row">
                    <div class="col-sm-4">
                        <div class="form-group">
                            <label class="control-label">Customer Type</label>
                            <ui-select class="required-field" ng-model="customer.customerType" theme="bootstrap" style="width:100%">
                                <ui-select-match placeholder="Select type">{{$select.selected.name}}</ui-select-match>
                                <ui-select-choices repeat="type in customerTypes | filter: $select.search">
                                    <div ng-bind-html="type.name | highlight: $select.search"></div>
                                </ui-select-choices>
                            </ui-select>
                        </div>
                    </div>
                    <div class="col-sm-4">
                        <div class="form-group">
                            <label class="control-label">Region</label>
                            <ui-select class="required-field"  ng-model="customer.salesRegion"
                                       on-select="onSelectRegion($item, $model)"
                                       theme="bootstrap" style="width:100%">
                                <ui-select-match placeholder="Select region"<%-- ng-blur="validateCustomernameWithRegion()"--%>>{{$select.selected.name}}</ui-select-match>
                                <ui-select-choices repeat="region in regions | filter: $select.search">
                                    <div ng-bind-html="region.name | highlight: $select.search"></div>
                                </ui-select-choices>
                            </ui-select>
                        </div>
                    </div>

                    <div class="col-sm-4">
                        <div class="form-group">
                            <label class="control-label">Sales Rep</label>
                            <ui-select class="required-field"  ng-model="customer.salesRep"
                                       <%--on-select="onSelectRegion($item, $model)"--%>
                                       theme="bootstrap" style="width:100%">
                                <ui-select-match placeholder="Select sales rep">{{$select.selected.firstName}}</ui-select-match>
                                <ui-select-choices repeat="salesRep in salesReps | filter: $select.search">
                                    <div ng-bind-html="salesRep.firstName | highlight: $select.search"></div>
                                </ui-select-choices>
                            </ui-select>
                        </div>
                    </div>
                </div>
                <div class="row" ng-if="showNewRegion" style="padding: 10px 0 10px 0; border: 1px solid #ddd;background-color: #FFF;">
                    <div class="col-sm-2" style="padding-top: 7px;">
                        <div class="btn-group" style="margin-bottom: 0px;">
                            <button type="button" class="btn btn-xs btn-success" ng-click="createNewSalesRegion()"><i class="fa fa-check"></i></button>
                            <button type="button" class="btn btn-xs btn-default" ng-click="cancelNewSalesRegion();"><i class="fa fa-times"></i></button>
                        </div>
                    </div>
                    <div class="col-sm-4">
                        <input class="form-control" type="text" placeholder="Name" ng-model="newSalesRegion.name"/>
                    </div>
                    <div class="col-sm-3">
                        <input class="form-control" type="text" placeholder="District" ng-model="newSalesRegion.district"/>
                    </div>
                    <div class="col-sm-3">
                        <ui-select ng-model="newSalesRegion.state" theme="bootstrap" style="width:100%">
                            <ui-select-match placeholder="Select state">{{$select.selected.name}}</ui-select-match>
                            <ui-select-choices repeat="state in states | filter: $select.search">
                                <div ng-bind-html="state.name | highlight: $select.search"></div>
                            </ui-select-choices>
                        </ui-select>
                    </div>
                </div>
                <div class="row">
                    <div class="col-sm-4">
                        <div class="form-group">
                            <label class="control-label">Office Phone</label>
                            <input class="form-control" type="text" ng-model="customer.officePhone" ng-blur="validatePhoneNumber(customer.officePhone)"/>
                        </div>
                    </div>
                    <div class="col-sm-4">
                        <div class="form-group">
                            <label class="control-label">Office Fax</label>
                            <input class="form-control" type="text" ng-model="customer.officeFax"/>
                        </div>
                    </div>
                    <div class="col-sm-4">
                        <div class="form-group">
                            <label class="control-label">Office Email</label>
                            <input class="form-control" type="text" ng-model="customer.officeEmail" ng-blur="validateEmail(customer.officeEmail)"/>
                        </div>
                    </div>
                </div>
                <div class="row">
                    <div class="col-sm-12">
                        <div class="form-group">
                            <label class="control-label">Address</label>
                            <textarea rows="5" class="form-control" style="resize: none" ng-model="customer.customerAddresses[0].addressText"></textarea>
                        </div>
                    </div>
                </div>
                <div class="row">
                    <div class="col-sm-4">
                        <div class="form-group">
                            <label class="control-label">City</label>
                            <input class="form-control" type="text"
                                   ng-class="{'required-field': (customer.customerAddresses[0].addressText != null &&
                                                customer.customerAddresses[0].addressText != '')}"
                                   ng-model="customer.customerAddresses[0].city"/>
                        </div>
                    </div>
                    <div class="col-sm-4">
                        <div class="form-group">
                            <label class="control-label">State</label>
                            <ui-select class="{{ (customer.customerAddresses[0].addressText != null &&
                                                customer.customerAddresses[0].addressText != '') ? 'required-field': '' }}"
                                    ng-model="customer.customerAddresses[0].state" theme="bootstrap" style="width:100%">
                                <ui-select-match placeholder="Select state">{{$select.selected.name}}</ui-select-match>
                                <ui-select-choices repeat="state in states | filter: $select.search">
                                    <div ng-bind-html="state.name | highlight: $select.search"></div>
                                </ui-select-choices>
                            </ui-select>
                        </div>
                    </div>
                    <div class="col-sm-4">
                        <div class="form-group">
                            <label class="control-label">Pincode</label>
                            <input class="form-control" type="text" ng-model="customer.customerAddresses[0].pincode"/>
                        </div>
                    </div>
                </div>
                <br/>
                <h4 class="text-primary">Contact Person</h4>
                <div class="row">
                    <div class="col-sm-12">
                        <div class="form-group">
                            <label class="control-label">Name</label>
                            <input class="form-control" type="text" ng-model="customer.contactPerson.firstName"/>
                        </div>
                    </div>
                </div>
                <div class="row">
                    <div class="col-sm-6">
                        <div class="form-group">
                            <label class="control-label">Phone Number</label>
                            <input class="form-control" type="text" ng-model="customer.contactPerson.phoneMobile" ng-blur="validatePhoneNumber(customer.contactPerson.phoneMobile)"/>
                        </div>
                    </div>
                    <div class="col-sm-6">
                        <div class="form-group">
                            <label class="control-label">Email</label>
                            <input class="form-control" type="text" ng-model="customer.contactPerson.email" ng-blur="validateEmail(customer.contactPerson.email)"/>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>