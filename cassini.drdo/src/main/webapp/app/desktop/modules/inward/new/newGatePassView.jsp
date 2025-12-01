<div style="position: relative;">
    <div style="overflow-y: auto; overflow-x: hidden; padding: 20px;">
        <div class="row" style="margin: 0;">
            <div>
                <h4 class="section-title" style="color: black;">Basic Info</h4>

                <form class="form-horizontal">

                    <div class="form-group">
                        <label class="col-sm-4 control-label">
                            <span>Received By</span>
                            <span class="asterisk">*</span> : </label>

                        <div class="col-sm-7">
                            <h4>{{loginPersonDetails.person.fullName}}</h4>
                        </div>
                    </div>

                    <div class="form-group">
                        <label class="col-sm-4 control-label">
                            <span>Gate Pass</span>
                            <span class="asterisk">*</span> : </label>

                        <div class="col-sm-7">
                            <input type="file" id="inwardGatePassFile" value="file" class="form-control"
                                   ng-model="newGatePassVm.gatePass"
                                   accept="application/pdf">
                        </div>
                    </div>

                    <div class="form-group" ng-if="newGatePassVm.gatePass != null">
                        <label class="col-sm-4 control-label">
                            <span>Gate Pass Number</span>
                            <span class="asterisk">*</span> : </label>

                        <div class="col-sm-7">
                            <input type="text" class="form-control" ng-model="newGatePassVm.newGatePass.gatePassNumber"
                                   readonly/>
                        </div>
                    </div>

                    <div class="form-group">
                        <label class="col-sm-4 control-label">
                            <span>Gate Pass Date</span>
                            <span class="asterisk">*</span> : </label>

                        <div class="col-sm-7">
                            <input type="text" class="form-control" placeholder="Select Gate Pass Date"
                                   ng-model="newGatePassVm.newGatePass.gatePassDate"
                                   inward-date-picker>
                        </div>
                    </div>
                </form>
            </div>
        </div>
    </div>
</div>
