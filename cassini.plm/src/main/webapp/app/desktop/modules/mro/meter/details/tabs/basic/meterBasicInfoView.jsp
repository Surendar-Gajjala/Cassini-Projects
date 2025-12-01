<div ng-if="meterBasicVm.loading == true" style="padding: 30px;">
    <br/>
    <span style="font-size: 15px;">
        <img src="app/assets/bower_components/cassini-platform/images/loaders/loader6.gif" class="mr5">
       <span translate>LOADING_METERS_DETAILS</span>
    </span>
    <br/>
</div>

<div class="item-details" style="padding: 30px" ng-if="meterBasicVm.loading == false">

    <div class="row">
        <div class="label col-xs-4 col-sm-3 text-right">
            <span translate>NUMBER</span> :
        </div>
        <div class="value col-xs-8 col-sm-9">
            <span>{{meterBasicVm.meter.number}}</span>
        </div>
    </div>

    <div class="row">
        <div class="label col-xs-4 col-sm-3 text-right">
            <span translate>TYPE</span> :
        </div>
        <div class="value col-xs-8 col-sm-9">
            <span>{{meterBasicVm.meter.type.name}}</span>
        </div>
    </div>

    <div class="row">
        <div class="label col-xs-4 col-sm-3 text-right">
            <span translate>NAME</span> :
        </div>
        <div class="value col-xs-8 col-sm-9" ng-class="{'cursor-override': !hasPermission('mrometer','edit')}"
             title="{{hasPermission('mrometer','edit') ? '' : noPermission}}">
            <a href="" e-style="width:250px" ng-class="{'permission-text-disabled': !hasPermission('mrometer','edit')}"
               onaftersave="meterBasicVm.updateMeter()"
               editable-text="meterBasicVm.meter.name">
                <span ng-bind-html="meterBasicVm.meter.name" title="{{meterBasicVm.meter.name}}"></span>
            </a>
        </div>
    </div>

    <div class="row">
        <div class="label col-xs-4 col-sm-3 text-right">
            <span translate>DESCRIPTION</span> :
        </div>
        <div class="value col-xs-8 col-sm-9" ng-class="{'cursor-override': !hasPermission('mrometer','edit')}"
             title="{{hasPermission('mrometer','edit') ? '' : noPermission}}">

            <a href="" ng-class="{'permission-text-disabled': !hasPermission('mrometer','edit')}"
               onaftersave="meterBasicVm.updateMeter()"
               editable-textarea="meterBasicVm.meter.description"><span ng-bind-html="(meterBasicVm.meter.description) || 'CLICK_TO_ENTER_DESCRIPTION' |
                translate" title="{{meterBasicVm.meter.description}}"></span>
            </a>
        </div>
    </div>

    <div class="row">
        <div class="label col-xs-4 col-sm-3 text-right">
            <span translate>METER_READING_TYPE</span> :
        </div>
        <div class="value col-xs-8 col-sm-9" ng-class="{'cursor-override': !hasPermission('mrometer','edit')}"
             title="{{hasPermission('mrometer','edit') ? '' : noPermission}}">

            <reading-type ng-if="!meterBasicVm.editMeterReading" object="meterBasicVm.meter"></reading-type>

            <a href="" class="fa fa-pencil row-edit-btn" ng-if="!meterBasicVm.editMeterReading"
               ng-class="{'permission-text-disabled': !hasPermission('mrometer','edit')}"
               ng-click="meterBasicVm.changeMeterReading()" title="{{EDIT | translate}}"></a>

            <div style="display: flex;" ng-if="meterBasicVm.editMeterReading">
                <ui-select ng-model="meterBasicVm.meter.meterReadingType" theme="bootstrap"
                           style="width:200px">
                    <ui-select-match placeholder="Select">{{$select.selected.label}}
                    </ui-select-match>
                    <ui-select-choices
                            repeat="status.value as status in meterBasicVm.meterReadings | filter: $select.search">
                        <div>{{status.label}}</div>
                    </ui-select-choices>
                </ui-select>
                <button class="btn btn-sm btn-primary" title="{{SAVE | translate}}"
                        ng-click="meterBasicVm.updateMeter()">
                    <i class="fa fa-check"></i>
                </button>
                <button class="btn btn-sm btn-default" title="{{CANCEL | translate}}"
                        ng-click="meterBasicVm.cancelMeterReading()">
                    <i class="fa fa-times"></i>
                </button>
            </div>
        </div>
    </div>

    <div class="row">
        <div class="label col-xs-4 col-sm-3 text-right">
            <span translate>METER_TYPE</span> :
        </div>
        <div class="value col-xs-8 col-sm-9" ng-class="{'cursor-override': !hasPermission('mrometer','edit')}"
             title="{{hasPermission('mrometer','edit') ? '' : noPermission}}">
            <meter-type ng-if="!meterBasicVm.editType" object="meterBasicVm.meter"></meter-type>

            <a href="" class="fa fa-pencil row-edit-btn" ng-if="!meterBasicVm.editType"
               ng-class="{'permission-text-disabled': !hasPermission('mrometer','edit')}"
               ng-click="meterBasicVm.changeType()" title="{{EDIT | translate}}"></a>

            <div style="display: flex;" ng-if="meterBasicVm.editType">
                <ui-select ng-model="meterBasicVm.meter.meterType" theme="bootstrap"
                           style="width:200px">
                    <ui-select-match placeholder="{{meterBasicVm.selectTitle}}">{{$select.selected}}
                    </ui-select-match>
                    <ui-select-choices
                            repeat="type in meterBasicVm.types | filter: $select.search">
                        <div ng-bind="type"></div>
                    </ui-select-choices>
                </ui-select>
                <button class="btn btn-sm btn-primary" title="{{SAVE | translate}}"
                        ng-click="meterBasicVm.updateMeter()">
                    <i class="fa fa-check"></i>
                </button>
                <button class="btn btn-sm btn-default" title="{{CANCEL | translate}}"
                        ng-click="meterBasicVm.cancelType()">
                    <i class="fa fa-times"></i>
                </button>
            </div>
        </div>


    </div>

    <div class="row">
        <div class="label col-xs-4 col-sm-3 text-right">
            <span translate>Measurement</span> :
        </div>
        <div class="value col-xs-8 col-sm-9">
            <span>{{meterBasicVm.meter.measurementName}}</span>
        </div>
    </div>
    <div class="row">
        <div class="label col-xs-4 col-sm-3 text-right">
            <span translate>Measurement Unit</span> :
        </div>
        <div class="value col-xs-8 col-sm-9">
            <span>{{meterBasicVm.meter.unitName}}</span>
        </div>
    </div>

    <div class="row">
        <div class="label col-xs-4 col-sm-3 text-right">
            <span translate>CREATED_BY</span> :
        </div>
        <div class="value col-xs-8 col-sm-9">
            <span>{{meterBasicVm.meter.createdByObject.fullName}}</span>
        </div>
    </div>
    <div class="row">
        <div class="label col-xs-4 col-sm-3 text-right">
            <span translate>CREATED_DATE</span> :
        </div>
        <div class="value col-xs-8 col-sm-9">
            <span>{{meterBasicVm.meter.createDateDe}}</span>
        </div>
    </div>
    <div class="row">
        <div class="label col-xs-4 col-sm-3 text-right">
            <span translate>MODIFIED_BY</span> :
        </div>
        <div class="value col-xs-8 col-sm-9">
            <span>{{meterBasicVm.meter.modifiedByObject.fullName}}</span>
        </div>
    </div>
    <div class="row">
        <div class="label col-xs-4 col-sm-3 text-right">
            <span translate>MODIFIED_DATE</span> :
        </div>
        <div class="value col-xs-8 col-sm-9">
            <span>{{meterBasicVm.meter.modifiedDate}}</span>
        </div>
    </div>
    <object-attribute-details-view object-type="MROOBJECTTYPE" show-attributes="true"
                                   actual-object-type="{{meter.objectType}}"
                                   has-permission="true"
                                   object-type-id="meter.type.id"
                                   object-id="meter.id"></object-attribute-details-view>
</div>
