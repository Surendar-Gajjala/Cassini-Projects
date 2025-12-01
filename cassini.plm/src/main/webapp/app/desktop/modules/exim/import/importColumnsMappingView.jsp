<div style="position: relative;">
    <div style="overflow-y: auto; overflow-x: hidden; padding: 10px">
        <div class="row" style="margin: 0;">
            <div ng-if="mapVm.headers.length > 0">
                <%--<div style="padding-left:30px">
                    <span> Note : Please select </span>
                    <span style="color: red"> * </span>
                    <span> marked Header mappings</span>
                </div>--%>
                <div style="padding-top: 20px" class="col-md-12">
                    <div class="col-md-5">
                        <label class="control-label"><span style="font-weight: bold;font-size: 17px" translate>APPLICATION_COLUMN</span></label>
                    </div>
                    <div class="col-md-6">
                        <label class="control-label"><span style="font-weight: bold;font-size: 17px" translate>IMPORTED_FILE_COLUMN</span></label>
                    </div>
                    </br></br>
                </div>
                <div style="padding-top: 20px" class="col-md-12" ng-repeat="header in mapVm.headerObjs">
                    <div class="col-md-5">
                        <label ng-if="header.mapHeader == ''" class="control-label"><span style="font-weight: bold">{{header.header}}</span></label>
                        <label ng-if="header.mapHeader != ''"
                               class="control-label"><span>{{header.header}}</span></label>
                        <span> : </span></label>
                    </div>
                    <div class="col-md-6">
                        <ui-select ng-model="header.mapHeader"
                                   theme="bootstrap"
                                   style="width:95%">
                            <ui-select-match placeholder="Select Mapping Header">{{$select.selected}}
                            </ui-select-match>
                            <ui-select-choices
                                    repeat="header in mapVm.headers1 track by $index">
                                <div ng-bind="header"></div>
                            </ui-select-choices>
                        </ui-select>
                    </div>
                    </br></br>
                </div>
            </div>
        </div>
    </div>
</div>