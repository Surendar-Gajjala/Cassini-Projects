<div class="modal-header">
    <h3>New Shift</h3>
</div>
<div class="modal-body" style="padding: 20px; max-height: 500px;">

    <form class="form-horizontal">
        <div ng-if="newShiftVm.valid==false"><span class="text-danger">{{newShiftVm.error}}</span></div>

        <div class="form-group">
            <label class="col-sm-3 control-label">Name:</label>

            <div class="col-sm-3" style="padding-left: 0px">
                <input type="text" class="form-control" placeholder="name" name="Name"
                       ng-model="newShiftVm.newShift.name" style="width: 250px;">
            </div>
        </div>

       <div class="form-group">
            <label class="col-sm-3 control-label">Start Time:</label>
            <div class="input-group">
                <div class="bootstrap-timepicker">
                    <input id="timepicker1" data-format="hh:mm:ss" type="text"  ng-model="newShiftVm.newShift.startTime"/>
                </div>
            </div>
        </div>


        <div class="form-group">
            <label class="col-sm-3 control-label">End Time:</label>
            <div class="input-group">
                <div class="bootstrap-timepicker">
                    <input id="timepicker2" data-format="hh:mm:ss" type="text" ng-model="newShiftVm.newShift.endTime">
                </div>
            </div>
        </div>
    </form>

</div>
<div class="modal-footer">
    <div class="row">
        <div class="col-md-6">
        </div>
        <div class="modal-buttons" class="col-md-6">
            <button type="button" class="btn btn-sm btn-default" ng-click=" newShiftVm.cancel();">Cancel</button>
            <button type="button" class="btn btn-sm btn-success" ng-click="newShiftVm.create()">Create</button>
        </div>
    </div>
</div>