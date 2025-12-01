<div class="row settings-panel">
    <div class="col-md-12 bhoechie-tab-container" v-tabs>
        <div>
            <div class="bhoechie-tab-menu">
                <div class="list-group">
                    <a href="#" class="list-group-item active">
                        <h5>Customer Type</h5>
                    </a>

                    <a href="#" class="list-group-item">
                        <h5>Shippers</h5>
                    </a>

                    <a href="#" class="list-group-item">
                        <h5>Vehicles</h5>
                    </a>
                </div>
            </div>
            <div class="bhoechie-tab" style="">
                <div class="bhoechie-tab-content active">
                    <div ng-controller="CustomerTypesController">
                        <div>
                            <div class="row">
                                <div class="col-md-6"><h3>Customer Type</h3></div>

                                <div class="col-md-6 text-right mrtop">
                                    <button class="btn btn-sm btn-primary" ng-click="addCustomerType()">New Type</button>
                                </div>
                            </div>

                            <table class="table table-striped">
                                <thead>
                                <tr>
                                    <th style="width: 100px; text-align: center">Actions</th>
                                    <th>Name</th>
                                    <th>Description</th>
                                </tr>
                                </thead>
                                <tbody>
                                <tr ng-show="customerTypes.length == 0">
                                    <td colspan="3">
                                        <span ng-hide="loading">No customer types</span>
                                        <span ng-show="loading">
                                            <img src="app/assets/images/loaders/loader19.gif" class="mr5">Loading customer types...
                                        </span>
                                    </td>
                                </tr>

                                <tr ng-repeat="customerType in customerTypes">
                                    <td style="width:100px; text-align: center; vertical-align: middle;">
                                        <div class="btn-group" dropdown style="margin-bottom: 0px;" ng-hide="customerType.editMode">
                                            <button type="button" class="btn btn-sm btn-default dropdown-toggle" dropdown-toggle>
                                                <i class="fa fa-cog fa-fw"></i>
                                            </button>
                                            <ul class="dropdown-menu" role="menu">
                                                <li><a href="" ng-click="showEditMode(customerType)">Edit</a></li>
                                            </ul>
                                        </div>


                                        <div class="btn-group" style="margin-bottom: 0px;" ng-show="customerType.editMode">
                                            <button type="button" class="btn btn-sm btn-success" ng-click="acceptChanges(customerType)"><i class="fa fa-check"></i></button>
                                            <button type="button" class="btn btn-sm btn-default" ng-click="hideEditMode(customerType);"><i class="fa fa-times"></i></button>
                                        </div>
                                    </td>

                                    <td style="vertical-align: middle;">
                                        <span ng-show="customerType.showValues">{{ customerType.name }}</span>
                                        <input placeholder="Enter name" class="form-control" type="text"
                                               ng-show="customerType.editMode" ng-model="customerType.newName">
                                    </td>

                                    <td style="vertical-align: middle;">
                                        <span ng-show="customerType.showValues">{{ customerType.description }}</span>
                                        <input placeholder="Enter description" class="form-control" type="text"
                                               ng-show="customerType.editMode" ng-model="customerType.newDescription">
                                    </td>
                                </tr>
                                </tbody>
                            </table>
                        </div>
                    </div>
                </div>
                <div class="bhoechie-tab-content">
                    <div ng-controller="ShippersController">
                        <div>
                            <div class="row">
                                <div class="col-md-6"><h3>Shippers</h3></div>

                                <div class="col-md-6 text-right mrtop">
                                    <button class="btn btn-sm btn-primary" ng-click="createNewShipper()">New Shipper</button>
                                </div>
                            </div>

                            <table class="table table-striped" ng-hide="showForm">
                                <thead>
                                <tr>
                                    <th style="width: 100px; text-align: center">Actions</th>
                                    <th>Name</th>
                                    <th>Address</th>
                                    <th>Office Phone</th>
                                    <th>Office Fax</th>
                                    <th>Office Email</th>
                                </tr>
                                </thead>
                                <tbody>
                                <tr ng-show="shippers.length == 0">
                                    <td colspan="6">
                                        <span ng-hide="loading">No shippers</span>
                                        <span ng-show="loading">
                                            <img src="app/assets/images/loaders/loader19.gif" class="mr5">Loading shippers...
                                        </span>
                                    </td>
                                </tr>

                                <tr ng-repeat="shipper in shippers">
                                    <td style="width: 100px; text-align: center">
                                        <div class="btn-group" dropdown ng-hide="employeeType.editMode">
                                            <button type="button" class="btn btn-sm btn-default dropdown-toggle actionbtn" dropdown-toggle>
                                                <i class="fa fa-cog fa-fw"></i></span>
                                            </button>
                                            <ul class="dropdown-menu" role="menu">
                                                <li><a href="" ng-click="updateShipper(shipper)">Edit</a></li>
                                            </ul>
                                        </div>
                                    </td>
                                    <td>{{shipper.name}}</td>
                                    <td>{{shipper.address.addressText}},
                                        {{shipper.address.city}},
                                        {{shipper.address.state.name}}-
                                        {{shipper.address.pincode}}
                                    </td>
                                    <td>{{shipper.officePhone}}</td>
                                    <td>{{shipper.officeFax}}</td>
                                    <td>{{shipper.officeEmail}}</td>
                                </tr>
                                </tbody>
                            </table>

                            <div ng-show="showForm">
                                <div class="row">
                                    <div class="col-sm-12 col-md-6 col-md-offset-3 shadow-form styled-panel" style="margin-bottom: 50px;">
                                        <h4 class="section-title text-center text-primary">{{mode == 'new' ? 'New' : 'Update'}} Shipper</h4>
                                        <br>

                                        <div class="row">
                                            <div class="form-group col-sm-12">
                                                <label class="control-label">Name</label>
                                                <input type="text" class="form-control" ng-model="shipper.newName">
                                            </div>
                                        </div>
                                        <div class="row">
                                            <div class="form-group col-sm-4">
                                                <label class="control-label">Office Phone</label>
                                                <input type="text" class="form-control" ng-model="shipper.newOfficePhone">
                                            </div>
                                            <div class="form-group col-sm-4">
                                                <label class="control-label">Office Fax</label>
                                                <input type="text" class="form-control" ng-model="shipper.newOfficeFax">
                                            </div>
                                            <div class="form-group col-sm-4">
                                                <label class="control-label">Office Email</label>
                                                <input type="text" class="form-control" ng-model="shipper.newOfficeEmail">
                                            </div>
                                        </div>
                                        <br>

                                        <div class="row">
                                            <div class="form-group col-sm-12">
                                                <label class="control-label">Address</label>
                                                <textarea rows="5" style="resize: none" class="form-control"
                                                    ng-model="shipper.address.newAddressText"></textarea>
                                            </div>
                                        </div>
                                        <div class="row">
                                            <div class="form-group col-sm-4">
                                                <label class="control-label">City</label>
                                                <input type="text" class="form-control" ng-model="shipper.address.newCity">
                                            </div>
                                            <div class="form-group col-sm-4">
                                                <label class="control-label">State</label>
                                                <ui-select ng-model="shipper.address.newState" theme="bootstrap" style="width:100%" ng-if="states.length > 0">
                                                    <ui-select-match placeholder="Select state">{{$select.selected.name}}</ui-select-match>
                                                    <ui-select-choices repeat="state in states | filter: $select.search">
                                                        <div ng-bind-html="state.name | highlight: $select.search"></div>
                                                    </ui-select-choices>
                                                </ui-select>
                                            </div>
                                            <div class="form-group col-sm-4">
                                                <label class="control-label">Pincode</label>
                                                <input type="text" class="form-control" ng-model="shipper.address.newPincode">
                                            </div>
                                        </div>
                                        <hr>
                                        <div class="text-center">
                                            <button class="btn btn-sm btn-default mr10" ng-click="closeForm()">Cancel</button>
                                            <button class="btn btn-sm btn-primary" ng-click="processShipper()">
                                                {{mode == 'new' ? "Create" : "Update"}}
                                            </button>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="bhoechie-tab-content">
                    <div ng-controller="VehiclesController">
                        <div>
                            <div class="row">
                                <div class="col-md-6"><h3>Vehicles</h3></div>

                                <div class="col-md-6 text-right mrtop">
                                    <button class="btn btn-sm btn-primary" ng-click="addVehicle()">New Vehicle</button>
                                </div>
                            </div>

                            <table class="table table-striped">
                                <thead>
                                <tr>
                                    <th style="width: 100px; text-align: center">Actions</th>
                                    <th>Vehicle Number</th>
                                    <th>Vehicle Description</th>
                                </tr>
                                </thead>
                                <tbody>
                                <tr ng-show="customerTypes.length == 0">
                                    <td colspan="3">
                                        <span ng-hide="loading">No vehicles</span>
                                        <span ng-show="loading">
                                            <img src="app/assets/images/loaders/loader19.gif" class="mr5">Loading customer types...
                                        </span>
                                    </td>
                                </tr>

                                <tr ng-repeat="vehicle in vehicles track by $index">
                                    <td style="width:100px; text-align: center; vertical-align: middle;">
                                        <div class="btn-group" dropdown style="margin-bottom: 0px;" ng-hide="vehicle.editMode">
                                            <button type="button" class="btn btn-sm btn-default dropdown-toggle" dropdown-toggle>
                                                <i class="fa fa-cog fa-fw"></i>
                                            </button>
                                            <ul class="dropdown-menu" role="menu">
                                                <li><a href="" ng-click="showEditMode(vehicle)">Edit</a></li>
                                            </ul>
                                        </div>


                                        <div class="btn-group" style="margin-bottom: 0px;" ng-show="vehicle.editMode">
                                            <button type="button" class="btn btn-sm btn-success" ng-click="acceptChanges(vehicle)"><i class="fa fa-check"></i></button>
                                            <button type="button" class="btn btn-sm btn-default" ng-click="hideEditMode(vehicle);"><i class="fa fa-times"></i></button>
                                        </div>
                                    </td>

                                    <td style="vertical-align: middle;">
                                        <span ng-show="vehicle.showValues">{{ vehicle.number }}</span>
                                        <input placeholder="Enter number" class="form-control" type="text"
                                               ng-show="vehicle.editMode" ng-model="vehicle.newNumber">
                                    </td>

                                    <td style="vertical-align: middle;">
                                        <span ng-show="vehicle.showValues">{{ vehicle.description }}</span>
                                        <input placeholder="Enter description" class="form-control" type="text"
                                               ng-show="vehicle.editMode" ng-model="vehicle.newDescription">
                                    </td>
                                </tr>
                                </tbody>
                            </table>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>