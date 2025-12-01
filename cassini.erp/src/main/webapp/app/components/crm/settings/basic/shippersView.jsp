<div>
    <div class="row">
        <div class="col-md-6"><h3>Employee Type</h3></div>

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
                        <input type="text" class="form-control" ng-model="shipper.name">
                    </div>
                </div>
                <div class="row">
                    <div class="form-group col-sm-4">
                        <label class="control-label">Office Phone</label>
                        <input type="text" class="form-control" ng-model="shipper.officePhone">
                    </div>
                    <div class="form-group col-sm-4">
                        <label class="control-label">Office Fax</label>
                        <input type="text" class="form-control" ng-model="shipper.officeFax">
                    </div>
                    <div class="form-group col-sm-4">
                        <label class="control-label">Office Email</label>
                        <input type="text" class="form-control" ng-model="shipper.officeEmail">
                    </div>
                </div>
                <br>

                <div class="row">
                    <div class="form-group col-sm-12">
                        <label class="control-label">Address</label>
                        <textarea rows="5" style="resize: none" class="form-control"
                                  ng-model="shipper.address.addressText"></textarea>
                    </div>
                </div>
                <div class="row">
                    <div class="form-group col-sm-4">
                        <label class="control-label">City</label>
                        <input type="text" class="form-control" ng-model="shipper.address.city">
                    </div>
                    <div class="form-group col-sm-4">
                        <label class="control-label">State</label>
                        <ui-select ng-model="shipper.address.state" theme="bootstrap" style="width:100%">
                            <ui-select-match placeholder="Select state">{{$select.selected.name}}</ui-select-match>
                            <ui-select-choices repeat="state in states | filter: $select.search">
                                <div ng-bind-html="state.name | highlight: $select.search"></div>
                            </ui-select-choices>
                        </ui-select>
                    </div>
                    <div class="form-group col-sm-4">
                        <label class="control-label">Pincode</label>
                        <input type="text" class="form-control" ng-model="shipper.address.pincode">
                    </div>
                </div>
                <hr>
                <div class="text-center">
                    <button class="btn btn-sm btn-default mr10" ng-click="closeForm()">Cancel</button>
                    <button class="btn btn-sm btn-primary" ng-click="processShipper();">
                        {{mode == 'new' ? "Create" : "Update"}}
                    </button>
                </div>
            </div>
        </div>
    </div>
</div>