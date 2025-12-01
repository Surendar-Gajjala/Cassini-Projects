<div ng-if="shiftBasicVm.loading == true" style="padding: 30px;">
    <br/>
    <span style="font-size: 15px;">
         <img src="app/assets/bower_components/cassini-platform/images/loaders/loader6.gif" class="mr5">
     <span translate>LOADING_SHIFT_DETAILS</span>
    </span>
    <br/>
</div>

<div class="item-details" style="padding: 30px" ng-if="shiftBasicVm.loading == false">
    <div class="row">
        <div class="label col-xs-4 col-sm-3 text-right">
            <span translate>NUMBER</span> :
        </div>
        <div class="value col-xs-8 col-sm-9">
            <span>{{shiftBasicVm.shift.number}}</span>
        </div>
    </div>
    <div class="row">
        <div class="label col-xs-4 col-sm-3 text-right">
            <span translate>NAME</span> :
        </div>
        <div class="value col-xs-8 col-sm-9" ng-class="{'cursor-override': !hasPermission('shift','edit')}"
             title="{{hasPermission('shift','edit') ? '' : noPermission}}">
            <a href="" e-style="width:250px" ng-class="{'permission-text-disabled': !hasPermission('shift','edit')}"
               onaftersave="shiftBasicVm.updateShift()"
               editable-text="shiftBasicVm.shift.name">
                <span ng-bind-html="shiftBasicVm.shift.name"></span>
            </a>
        </div>
    </div>
    <div class="row">
        <div class="label col-xs-4 col-sm-3 text-right">
            <span translate>DESCRIPTION</span> :
        </div>
        <div class="value col-xs-8 col-sm-9" ng-class="{'cursor-override': !hasPermission('shift','edit')}"
             title="{{hasPermission('shift','edit') ? '' : noPermission}}">

            <a href="" ng-class="{'permission-text-disabled': !hasPermission('shift','edit')}"
               onaftersave="shiftBasicVm.updateShift()"
               editable-textarea="shiftBasicVm.shift.description"><span ng-bind-html="(shiftBasicVm.shift.description ) || 'CLICK_TO_ENTER_DESCRIPTION' |
                translate" title="{{shiftBasicVm.shift.description}}"></span>
            </a>
        </div>
    </div>

    <div class="row">
        <div class="label col-xs-4 col-sm-3 text-right">
            <span translate>START_TIME</span> :
        </div>
        <div class="value col-xs-8 col-sm-9" ng-class="{'cursor-override': !hasPermission('shift','edit')}"
             title="{{hasPermission('shift','edit') ? '' : noPermission}}">
            <a href="" e-style="width:250px" ng-class="{'permission-text-disabled': !hasPermission('shift','edit')}"
               ng-if="shiftBasicVm.shift.editMode == false"
               ng-click="shiftBasicVm.changeStartTime()">
                <span>{{shiftBasicVm.shift.startTime}}</span>
            </a>

            <div ng-if="shiftBasicVm.shift.editMode == true" class="btn-group" style="width: 300px;">
                <div class="input-group" style="width: 200px;">
                    <input type="text" class="form-control" placeholder="HH:mm" shift-time-picker
                           ng-model="shiftBasicVm.shift.localStartTime"
                           name="attDate">

                </div>
                <div class="btn-group" style="margin-left: 202px;margin-top: -38px;">
                    <button ng-show="shiftBasicVm.shift.editMode == true"
                            class="btn btn-sm btn-primary"
                            type="button" title="{{'SUBMIT' | translate}}"
                            ng-click="shiftBasicVm.updateShift()"><i class="fa fa-check"></i>
                    </button>

                    <button ng-show="shiftBasicVm.shift.editMode == true"
                            class="btn btn-sm btn-default"
                            type="button" title="{{'CANCEL' | translate}}"
                            ng-click="shiftBasicVm.cancelStartTime()"><i class="fa fa-times"></i>
                    </button>
                </div>
            </div>


        </div>
    </div>


    <div class="row">
        <div class="label col-xs-4 col-sm-3 text-right">
            <span translate>END_TIME</span> :
        </div>
        <div class="value col-xs-8 col-sm-9" ng-class="{'cursor-override': !hasPermission('shift','edit')}"
             title="{{hasPermission('shift','edit') ? '' : noPermission}}">
            <a href="" e-style="width:250px" ng-class="{'permission-text-disabled': !hasPermission('shift','edit')}"
               ng-if="shiftBasicVm.shift.endEditMode == false"
               ng-click="shiftBasicVm.changeEndTime()"
               editable-text="shiftBasicVm.shift.localEndTime">
                <span>{{shiftBasicVm.shift.endTime}}</span>
            </a>

            <div ng-if="shiftBasicVm.shift.endEditMode == true" class="btn-group" style="width: 300px;">
                <div class="input-group" style="width: 200px;">
                    <input type="text" class="form-control" placeholder="HH:mm" shift-time-picker
                           ng-model="shiftBasicVm.shift.localEndTime"
                           name="attDate">

                </div>
                <div class="btn-group" style="margin-left: 202px;margin-top: -38px;">
                    <button ng-show="shiftBasicVm.shift.endEditMode == true"
                            class="btn btn-sm btn-primary"
                            type="button" title="{{'SUBMIT' | translate}}"
                            ng-click="shiftBasicVm.updateShift()"><i class="fa fa-check"></i>
                    </button>

                    <button ng-show="shiftBasicVm.shift.endEditMode == true"
                            class="btn btn-sm btn-default"
                            type="button" title="{{'CANCEL' | translate}}"
                            ng-click="shiftBasicVm.cancelEndTime()"><i class="fa fa-times"></i>
                    </button>
                </div>
            </div>


        </div>
    </div>

    <div class="row">
        <div class="label col-xs-4 col-sm-3 text-right">
            <span translate>CREATED_BY</span> :
        </div>
        <div class="value col-xs-8 col-sm-9">
            <span>{{shiftBasicVm.shift.createdByObject.fullName}}</span>
        </div>
    </div>
    <div class="row">
        <div class="label col-xs-4 col-sm-3 text-right">
            <span translate>CREATED_DATE</span> :
        </div>
        <div class="value col-xs-8 col-sm-9">
            <span>{{shiftBasicVm.shift.createDateDe}}</span>
        </div>
    </div>
    <div class="row">
        <div class="label col-xs-4 col-sm-3 text-right">
            <span translate>MODIFIED_BY</span> :
        </div>
        <div class="value col-xs-8 col-sm-9">
            <span>{{shiftBasicVm.shift.modifiedByObject.fullName}}</span>
        </div>
    </div>
    <div class="row">
        <div class="label col-xs-4 col-sm-3 text-right">
            <span translate>MODIFIED_DATE</span> :
        </div>
        <div class="value col-xs-8 col-sm-9">
            <span>{{shiftBasicVm.shift.modifiedDate}}</span>
        </div>
    </div>


</div>
