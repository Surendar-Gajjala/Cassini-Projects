<div style="position: relative;">
    <div style="overflow-y: hidden; overflow-x: hidden; padding: 20px">
        <div class="row" style="margin: 0;">
            <div style="padding-bottom: 100px;">
                <h4 class="section-title" style="margin-top: 0;" translate>BASIC_INFO</h4>

                <form class="form-horizontal">

                    <div class="form-group">
                        <label class="col-sm-4 control-label">
                            <span translate>TYPE</span>
                            <span class="asterisk">*</span> : </label>

                        <div class="col-sm-7">
                            <div class="input-group mb15">
                                <div class="input-group-btn" uib-dropdown>
                                    <button uib-dropdown-toggle class="btn btn-default dropdown-toggle"
                                            type="button">
                                        <span id="Select" translate>SELECT</span> <span class="caret"
                                                                                        style="margin-left: 4px;"></span>
                                    </button>
                                    <div class="dropdown-menu" role="menu">
                                        <div style="overflow: auto;padding: 5px; border: 1px solid #FFF;width: 300px;height: 200px;">

                                            <manufacturing-type-tree
                                                    on-select-type="newMeterVm.onSelectType"
                                                    object-type="METERTYPE"></manufacturing-type-tree>
                                        </div>
                                    </div>
                                </div>
                                <input type="text" class="form-control" name="title"
                                       ng-model="newMeterVm.type.name" readonly>

                            </div>
                        </div>
                    </div>

                    <div class="form-group">
                        <label class="col-sm-4 control-label">
                            <span translate>NUMBER</span>
                            <span class="asterisk">*</span> :
                        </label>

                        <div class="col-sm-7">
                            <div class="input-group mb15">
                                <div class="input-group-btn">
                                    <button class="btn btn-default" type="button"
                                            ng-click="newMeterVm.autoNumber()" translate>AUTO
                                    </button>
                                </div>
                                <input type="text" class="form-control" name="title"
                                       ng-model="newMeterVm.newMeter.number">
                            </div>
                        </div>
                    </div>

                    <div class="form-group">
                        <label class="col-sm-4 control-label"><span translate>NAME</span>
                            <span class="asterisk">*</span> : </label>

                        <div class="col-sm-7">
                            <input type="text" class="form-control" placeholder="{{'ENTER_NAME' | translate}}"
                                   name="title" ng-model="newMeterVm.newMeter.name">
                        </div>
                    </div>

                    <div class="form-group">
                        <label class="col-sm-4 control-label">
                            <span translate>DESCRIPTION</span>: </label>

                        <div class="col-sm-7">
                            <textarea class="form-control" rows="3" style="resize: none"
                                      placeholder="{{'ENTER_DESCRIPTION_TITLE' | translate}}"
                                      ng-model="newMeterVm.newMeter.description"></textarea>
                        </div>
                    </div>

                    <div class="form-group">
                        <label class="col-sm-4 control-label">
                            <span translate>METER_TYPE</span>
                            <span class="asterisk">*</span> : </label>

                        <div class="col-sm-7">
                            <ui-select ng-model="newMeterVm.newMeter.meterType" theme="bootstrap"
                                       style="width:100%">
                                <ui-select-match>
                                    {{$select.selected}}
                                </ui-select-match>
                                <ui-select-choices
                                        repeat="value in newMeterVm.types | filter: $select.search">
                                    <div ng-bind="value"></div>
                                </ui-select-choices>
                            </ui-select>
                        </div>
                    </div>

                    <div class="form-group">
                        <label class="col-sm-4 control-label">
                            <span translate>METER_READING_TYPE</span>
                            <span class="asterisk">*</span> : </label>

                        <div class="col-sm-7">
                            <ui-select ng-model="newMeterVm.newMeter.meterReadingType" theme="bootstrap"
                                       style="width:100%">
                                <ui-select-match>
                                    {{$select.selected}}
                                </ui-select-match>
                                <ui-select-choices
                                        repeat="value in newMeterVm.meterReadingTypes | filter: $select.search">
                                    <div ng-bind="value"></div>
                                </ui-select-choices>
                            </ui-select>
                        </div>
                    </div>

                    <div class="form-group">
                        <label class="col-sm-4 control-label">
                            <span translate>QOM</span>
                            <span class="asterisk">*</span> : </label>

                        <div class="col-sm-7">
                            <ui-select ng-model="newMeterVm.newMeter.qomObject" theme="bootstrap"
                                       on-select="newMeterVm.selectQOM($item)"
                                       style="width:100%">
                                <ui-select-match placeholder="{{selectQom}}">{{$select.selected.name}}
                                </ui-select-match>
                                <ui-select-choices
                                        repeat="measurements in newMeterVm.measurements | filter: $select.search">
                                    <div ng-bind="measurements.name"></div>
                                </ui-select-choices>
                            </ui-select>
                        </div>
                    </div>

                    <div class="form-group" ng-show="newMeterVm.newMeter.qomObject != null">
                        <label class="col-sm-4 control-label">
                            <span translate>UOM</span>
                            <span class="asterisk">*</span> : </label>

                        <div class="col-sm-7">
                            <ui-select ng-model="newMeterVm.newMeter.uomObject" theme="bootstrap"
                                       style="width:100%">
                                <ui-select-match placeholder="{{selectUom}}">{{$select.selected.name}}
                                </ui-select-match>
                                <ui-select-choices style="max-height: 110px !important;"
                                                   repeat="units in newMeterVm.newMeter.qomObject.measurementUnits | filter: $select.search">
                                    <div ng-bind="units.name"></div>
                                </ui-select-choices>
                            </ui-select>
                        </div>
                    </div>

                    <attributes-view show-objects="selectObjectValues" ng-if="newMeterVm.attributes.length > 0"
                                     attributes="newMeterVm.attributes"></attributes-view>
                </form>
            </div>
        </div>
    </div>
</div>
