<div class="modal-header"
     style="background: #E4E7EA none repeat scroll 0% 0%; border-bottom: 1px solid #CCC;">
    <h3 style="margin-top:0px; margin-bottom:0px;">Leave request</h3>
</div>
<div class="modal-body styled-panel" style="margin: 20px;">
    <form class="form" angular-validator-submit="timeOfRequest()" name="timeOfForm" novalidate angular-validator>
        <div class="row">
            <div class="col-sm-12">
                <div class="form-group">
                    <label class="control-label">Leave Type</label>
                    <select class="form-control" ng-model="timeoff.timeOffType" data-placeholder="Select" ng-options="timeOffType.id as timeOffType.name for timeOffType in timeOffTypes"
                            name ="timeoftypes"
                            required-message="constants.REQUIRED" validate-on="dirty" required>
                        <option value="">-Select-</option>
                    </select>
                </div>
            </div>
        </div>
        <div class="row">
            <div class="col-sm-6">
                <div class="form-group">
                    <label class="control-label">From</label>
                    <div class="input-group">
                        <input type="text" class="form-control datepickercls" ng-model="timeoff.startDate" placeholder="dd/mm/yyyy" date-picker>
                        <span class="input-group-addon"><i class="glyphicon glyphicon-calendar"></i></span>
                    </div>
                    <input class="hide" ng-model="timeoff.startDate" name ="startDate"
                           required-message="constants.REQUIRED" validate-on="dirty" validator = "startEndValidator()" invalid-message="constants.STARTENDDATE" required>
                </div>
            </div><!-- col-sm-6 -->
            <div class="col-sm-6">
                <div class="form-group">
                    <label class="control-label">To</label>
                    <div class="input-group">
                        <input type="text" class="form-control datepickercls" ng-model="timeoff.endDate" placeholder="dd/mm/yyyy" date-picker>
                        <span class="input-group-addon"><i class="glyphicon glyphicon-calendar"></i></span>
                    </div>
                    <input class="hide" ng-model="timeoff.endDate" name ="endDate"
                           required-message="constants.REQUIRED" validate-on="dirty" validator = "startEndValidator()" invalid-message="constants.STARTENDDATE" required>
                </div>
            </div><!-- col-sm-6 -->
        </div><!-- row -->
        <div class="row">
            <div class="col-sm-12">
                <div class="form-group">
                    <label class="control-label">Num of days</label>
                    <span>{{timeoff.numOfDays}}</span>
                </div>
            </div>
        </div>
        <div class="row">
            <div class="col-sm-12">
                <div class="form-group">
                    <label class="control-label">Reason For Leave</label>
                    <textarea ng-model="timeoff.reason" class="form-control" rows="3"></textarea>
                </div>
            </div>
        </div>
    </form>
</div>

<div class="modal-footer text-right" style="background-color: #F9F9F9;">
    <button class="btn btn-link" data-ng-click="close();">Cancel</button>
    <button class="btn btn-primary" data-ng-click="validateTimeOf(timeOfForm);">Submit</button>
</div>