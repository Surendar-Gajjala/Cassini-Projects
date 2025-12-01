<div style="position: relative;">
    <div style="overflow-y: auto; overflow-x: hidden; padding: 20px;">
        <div class="row" style="margin: 0;">
            <div>
                <h4 class="section-title" style="color: black;">Basic Info</h4>

                <form class="form-horizontal">
                    <div class="form-group">
                        <label class="col-sm-4 control-label">
                            <span>Supplier Name</span>
                            <span class="asterisk">*</span> : </label>

                        <div class="col-sm-7">
                            <input type="text" class="form-control"
                                   placeholder="Enter Supplier Name"
                                   ng-model="editSupplierVm.editSupplier.supplierName"/>
                        </div>
                    </div>

                    <div class="form-group">
                        <label class="col-sm-4 control-label">
                            <span>Supplier Code</span>
                            <span class="asterisk">*</span> : </label>

                        <div class="col-sm-7">
                            <input type="text" class="form-control"
                                   placeholder="Enter Supplier Code"
                                   ng-model="editSupplierVm.editSupplier.supplierCode">
                        </div>
                    </div>

                    <div class="form-group">
                        <label class="col-sm-4 control-label">
                            <span>Address</span>
                            <span class="asterisk">*</span>: </label>

                        <div class="col-sm-7">
                            <textarea type="text" class="form-control" rows="5" style="resize: none;"
                                      placeholder="Enter Address"
                                      ng-model="editSupplierVm.editSupplier.address.addressText">
                            </textarea>
                        </div>
                    </div>

                    <div class="form-group">
                        <label class="col-sm-4 control-label">
                            <span>City</span>
                            <span class="asterisk">*</span>: </label>

                        <div class="col-sm-7">
                            <input type="text" class="form-control" placeholder="Enter City"
                                   ng-model="editSupplierVm.editSupplier.address.city">
                        </div>
                    </div>

                    <div class="form-group">
                        <label class="col-sm-4 control-label">
                            <span>State</span>
                            <span class="asterisk">*</span>: </label>

                        <div class="col-sm-7">
                            <ui-select ng-model="editSupplierVm.editSupplier.address.state" theme="bootstrap"
                                       style="width:100%">
                                <ui-select-match placeholder="Select state">{{$select.selected.name}}
                                </ui-select-match>
                                <ui-select-null-choice></ui-select-null-choice>
                                <ui-select-choices
                                        repeat="state.id as state in editSupplierVm.states | filter: $select.search">
                                    <div ng-bind="state.name"></div>
                                </ui-select-choices>
                            </ui-select>
                        </div>
                    </div>

                    <div class="form-group">
                        <label class="col-sm-4 control-label">
                            <span>Pincode</span> : </label>

                        <div class="col-sm-7">
                            <input type="text" class="form-control" placeholder="Enter Pincode"
                                   ng-model="editSupplierVm.editSupplier.address.pincode">
                        </div>
                    </div>

                    <h4>Contact Details</h4>

                    <div class="form-group">
                        <label class="col-sm-4 control-label">
                            <span>Name</span>
                            <span class="asterisk">*</span>: </label>

                        <div class="col-sm-7">
                            <input type="text" class="form-control" placeholder="Enter Name"
                                   ng-model="editSupplierVm.editSupplier.contactPerson">
                        </div>
                    </div>

                    <div class="form-group">
                        <label class="col-sm-4 control-label">
                            <span>Phone Number</span> : </label>

                        <div class="col-sm-7">
                            <input type="text" class="form-control" placeholder="Enter Phone Number"
                                   ng-model="editSupplierVm.editSupplier.phoneNumber">
                        </div>
                    </div>

                    <div class="form-group">
                        <label class="col-sm-4 control-label">
                            <span>Email</span> : </label>

                        <div class="col-sm-7">
                            <input type="text" class="form-control" placeholder="Enter Email"
                                   ng-model="editSupplierVm.editSupplier.email">
                        </div>
                    </div>
                </form>
            </div>
        </div>
    </div>
</div>
