<div style="position: relative;">
    <div style="overflow-y: auto; overflow-x: hidden; padding: 20px;">
        <div class="row" style="margin: 0;">
            <div>
                <h4 class="section-title" style="color: black;">Basic Info</h4>

                <form class="form-horizontal">

                    <div class="form-group">
                        <label class="col-sm-4 control-label">
                            <span>Manufacturer Name</span>
                            <span class="asterisk">*</span> : </label>

                        <div class="col-sm-7">
                            <input type="text" class="form-control"
                                   placeholder="Enter Manufacturer Name"
                                   ng-model="newManufacturerVm.newManufacturer.name"/>
                        </div>
                    </div>

                    <div class="form-group">
                        <label class="col-sm-4 control-label">
                            <span>Manufacturer Code</span>
                            <span class="asterisk">*</span> : </label>

                        <div class="col-sm-7">
                            <input type="text" class="form-control"
                                   ng-blur="newManufacturerVm.checkMfrCode()"
                                   placeholder="Enter Manufacturer Code"
                                   ng-model="newManufacturerVm.newManufacturer.mfrCode">
                        </div>
                    </div>

                    <div class="form-group" ng-if="newManufacturerVm.newManufacturer != null">
                        <label class="col-sm-4 control-label">
                            <span>Description</span> : </label>

                        <div class="col-sm-7">
                            <textarea type="text" class="form-control" rows="5" style="resize: none;"
                                      placeholder="Enter Description"
                                      ng-model="newManufacturerVm.newManufacturer.description">
                            </textarea>
                        </div>
                    </div>

                    <div class="form-group">
                        <label class="col-sm-4 control-label">
                            <span>Phone Number</span> : </label>

                        <div class="col-sm-7">
                            <input type="text" class="form-control" placeholder="Enter Phone Number"
                                   ng-model="newManufacturerVm.newManufacturer.phoneNumber">
                        </div>
                    </div>

                    <div class="form-group">
                        <label class="col-sm-4 control-label">
                            <span>Email</span> : </label>

                        <div class="col-sm-7">
                            <input type="text" class="form-control" placeholder="Enter Email"
                                   ng-model="newManufacturerVm.newManufacturer.email">
                        </div>
                    </div>
                </form>
            </div>
        </div>
    </div>
</div>
