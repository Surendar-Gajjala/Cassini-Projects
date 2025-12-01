<div style="position: relative;height: 100%">
    <div style="overflow-y: auto;padding: 20px;height: 100%;">
        <div class="row" style="margin: 0;">
            <div style="padding-bottom: 100px;">
                <form class="form-horizontal">

                    <div class="form-group">
                        <label class="col-sm-4 control-label">
                            <span translate>Shift Number</span><span class="asterisk">*</span> : </label>

                        <div class="col-sm-7">
                            <ui-select ng-model="productionOrderAssignedPersonVm.shift"
                                       theme="bootstrap" style="width:100%">
                                <ui-select-match placeholder="Select Shift Number">
                                    {{$select.selected.number}}
                                </ui-select-match>
                                <ui-select-choices
                                        repeat="shift in productionOrderAssignedPersonVm.shifts | filter: $select.search">
                                    <div>{{shift.number}}</div>
                                </ui-select-choices>
                            </ui-select>
                        </div>
                    </div>

                    <div class="form-group">
                        <label class="col-sm-4 control-label"><span translate>Shift Name</span>: </label>

                        <div class="col-sm-7">
                            <input type="text" class="form-control" name="title" placeholder="Name"
                                   ng-model="productionOrderAssignedPersonVm.shift.name" readonly>
                        </div>
                    </div>

                    <div class="form-group">
                        <label class="col-sm-4 control-label"><span translate>Start Time</span>: </label>

                        <div class="col-sm-7">
                            <input type="text" class="form-control" name="title" placeholder="StartTime"
                                   ng-model="productionOrderAssignedPersonVm.shift.startTime" readonly>
                        </div>
                    </div>

                    <div class="form-group">
                        <label class="col-sm-4 control-label"><span translate>End Time</span>: </label>

                        <div class="col-sm-7">
                            <input type="text" class="form-control" name="title" placeholder="EndTime"
                                   ng-model="productionOrderAssignedPersonVm.shift.endTime" readonly>
                        </div>
                    </div>


                    <div class="form-group">
                        <label class="col-sm-4 control-label">
                            <span translate>Assigned To</span><span class="asterisk">*</span> : </label>

                        <div class="col-sm-7">
                            <ui-select ng-model="productionOrderAssignedPersonVm.productionOrder.assignedTo"
                                       theme="bootstrap" style="width:100%">
                                <ui-select-match placeholder="Select Person">
                                    {{$select.selected.firstName}}
                                </ui-select-match>
                                <ui-select-choices
                                        repeat="person.id as person in productionOrderAssignedPersonVm.shift.persons | filter: $select.search">
                                    <div>{{person.firstName}}</div>
                                </ui-select-choices>
                            </ui-select>
                        </div>
                    </div>
                </form>
            </div>
        </div>
    </div>
</div>
