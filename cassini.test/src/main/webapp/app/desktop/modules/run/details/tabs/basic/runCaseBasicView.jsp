<style>
    /* The Close Button */
    .img-model .closeimage {
        position: absolute;
        top: 50px;
        right: 50px;
        color: black;
        font-size: 40px;
        font-weight: bold;
        transition: 0.3s;
    }

    .img-model .closeimage:hover,
    .img-model .closeimage:focus {
        color: #bbb;
        text-decoration: none;
        cursor: pointer;
    }

    .img-model .closeImage {
        position: absolute;
        top: 50px;
        right: 50px;
        color: black;
        font-size: 40px;
        font-weight: bold;
        transition: 0.3s;
    }

    .img-model .closeImage:hover,
    .img-model .closeImage:focus {
        color: #bbb;
        text-decoration: none;
        cursor: pointer;
    }

    .browse-control {
        -moz-border-radius: 3px;
        -webkit-border-radius: 3px;
        border-radius: 3px;
        padding: 5px;
        height: auto;
        -moz-box-shadow: none;
        -webkit-box-shadow: none;
        box-shadow: none;
        font-size: 13px;
        border: 1px solid #ccc;
    }
</style>

<div>
    <div class="row row-eq-height">
        <div class="col-sm-12">
            <div class="item-details" style="padding: 30px">
                <h4 ng-if="runCaseAttributes.length > 0"
                    class="section-title" style="color: black;">Basic Info :
                </h4>

                <div class="row">
                    <div class="label col-xs-4 col-sm-3 text-right">
                        <span>Test Case Id</span> :
                    </div>
                    <div class="value col-xs-8 col-sm-9">
                        <span>{{runCase.id}}</span>
                    </div>
                </div>
                <div class="row">
                    <div class="label col-xs-4 col-sm-3 text-right">
                        <span>Name</span> :
                    </div>
                    <div class="value col-xs-8 col-sm-9">
                        <span>{{runCase.name}}</span>
                    </div>
                </div>
                <div class="row">
                    <div class="label col-xs-4 col-sm-3 text-right">
                        <span>Description</span> :
                    </div>
                    <div class="value col-xs-8 col-sm-9">

                        <span style="word-wrap:break-word;">{{runCase.description}}</span>
                    </div>
                </div>


                <div class="row">
                    <div class="label col-xs-4 col-sm-3 text-right">
                        <span>Result</span> :
                    </div>

                    <div class="col-sm-8" style="margin-top: 10px;">
                                                    <span ng-if="runCase.result == true"
                                                          style="background: green;padding: 5px 10px;color: white;border-radius: 3px">PASS</span>
                                                    <span ng-if="runCase.result == false"
                                                          style="background: darkred;padding: 5px 10px;color: white;border-radius: 3px">FAIL</span>
                    </div>
                </div>


                <br>
                <h4 ng-if="runCaseAttributes.length > 0"
                    class="section-title" style="color: black;">Run Case Attributes :
                </h4>
                <br>

                <%----------------  Run Case Properties  ------------------------%>
                <div class="row"
                     ng-if="runCaseAttributes.length != 0"
                     ng-repeat="attribute in runCaseAttributes">
                    <div class="label col-xs-4 col-sm-3 text-right">
                        <span>{{attribute.attributeDef.name}}: </span>
                    </div>
                    <div class="value col-xs-8 col-sm-9">

                        <%------  String Attribute  ------%>

                        <div ng-if="attribute.attributeDef.dataType == 'STRING'">
                                            <span>
                                                <a ng-if="attribute.editMode == false" href=""
                                                   ng-click="runCaseBasicVm.changeAttribute(attribute)"
                                                   title="{{changeValue}}">
                                                    {{attribute.value.stringValue || 'CLICK_TO_SET_VALUE' | translate}}
                                                </a>
                                            </span>
                        </div>

                        <%------  Integer Attribute  ------%>

                        <div id="intClick" ng-if="attribute.attributeDef.dataType == 'INTEGER'">
                                            <span>
                                                <a ng-if="attribute.editMode == false" href=""
                                                   ng-click="runCaseBasicVm.changeAttribute(attribute)"
                                                   title="{{changeValue}}">
                                                    {{attribute.value.integerValue || 'CLICK_TO_SET_VALUE' | translate}}
                                                </a>
                                              </span>

                        </div>

                        <%------  Double Attribute  ------%>

                        <div ng-if="attribute.attributeDef.dataType == 'DOUBLE'">
                                            <span>
                                                <a ng-if="attribute.editMode == false" href=""
                                                   ng-click="runCaseBasicVm.changeAttribute(attribute)"
                                                   title="{{changeValue}}">
                                                    {{attribute.value.doubleValue || 'CLICK_TO_SET_VALUE' | translate}}
                                                </a>
                                                </span>

                        </div>


                        <%------  Timestamp Attribute  ------%>

                        <div ng-if="attribute.attributeDef.dataType == 'TIMESTAMP'">
                            <span ng-show="attribute.showTimestamp == false">
                                <a href="" title="{{changeTime}}"
                                   ng-if="attribute.editMode == false"
                                   ng-click="runCaseBasicVm.changeTimestamp(attribute)">
                                    {{attribute.value.timestampValue || 'CLICK_TO_SET_TIME' | translate}}
                                </a>
                            </span>
                        </div>

                        <%------  Time Attribute  ------%>

                        <div ng-if="attribute.attributeDef.dataType == 'TIME'">
                            <span ng-show="attribute.showTimeAttribute == false">
                                <a href="" title="{{changeTime}}"
                                   ng-if="attribute.editMode == false"
                                   ng-click="runCaseBasicVm.changeTime(attribute)">
                                    {{attribute.value.timeValue || 'CLICK_TO_SET_TIME' | translate}}
                                </a>
                            </span>
                        </div>

                        <%------  Date Attribute  ------%>

                        <div ng-if="attribute.attributeDef.dataType == 'DATE'">
                                            <span>
                                                <a ng-if="attribute.editMode == false" href=""
                                                   ng-click="runCaseBasicVm.changeAttribute(attribute)"
                                                   title="{{changeDate}}">
                                                    {{ (attribute.value.dateValue | date:"dd/MM/yyyy") || 'Click to set
                                                    Date' }}
                                                </a>
                                            </span>
                        </div>


                        <div ng-if="attribute.editMode == true &&
                                                    (attribute.attributeDef.dataType == 'STRING' || attribute.attributeDef.dataType == 'INTEGER' ||
                                                     attribute.attributeDef.dataType == 'DOUBLE' ||
                                                     attribute.attributeDef.dataType == 'DATE')">

                            <input type="text" class="form-control" name="title"
                                   ng-if="attribute.attributeDef.dataType == 'STRING'"
                                   placeholder="{{'ENTER_VALUE' | translate}}"
                                   ng-model="attribute.value.stringValue" style="width:200px;">
                            <input type="text" class="form-control" name="title"
                                   placeholder="{{'ENTER_NUMBER' | translate}}"
                                   ng-if="attribute.attributeDef.dataType == 'INTEGER'" numbers-only
                                   ng-model="attribute.value.integerValue" style="width:200px;">
                            <input class="form-control" name="title"
                                   ng-if="attribute.attributeDef.dataType == 'DOUBLE'"
                                   placeholder="{{'ENTER_NUMBER' | translate}}"
                                   type="text" valid-number
                                   ng-model="attribute.value.doubleValue" style="width:200px;">

                            <div class="input-group" ng-if="attribute.attributeDef.dataType == 'DATE'"
                                 style="width: 200px;">
                                <input type="text" class="form-control" date-picker style="background: white;"
                                       ng-model="attribute.value.dateValue"
                                       name="attDate" placeholder="dd/mm/yyyy">
                                                    <span class="input-group-addon">
                                                        <i class="glyphicon glyphicon-calendar"></i></span>
                            </div>

                            <button class="btn btn-sm btn-primary"
                                    type="button" title="save Attribute"
                                    style="margin-top:-65px;margin-left:205px;padding: 8px;width:33px;"
                                    ng-click="saveRunCaseProperties(attribute)"><i class="fa fa-check"></i>
                            </button>
                            <button class="btn btn-sm btn-default"
                                    type="button" title="cancel Changes"
                                    style="margin-top:-65px;padding: 8px;width:33px;"
                                    ng-click="cancelRunCaseChanges(attribute)"><i class="fa fa-times"></i>
                            </button>
                        </div>
                        <div ng-if="attribute.showTimestamp == true">
                            <div>
                                <div uib-timepicker
                                     ng-model="attribute.value.timestampValue">
                                </div>
                                <button class="btn btn-sm btn-primary" type="button" title="{{'SAVE_TIME' | translate}}"
                                        style="margin-top:-195px;margin-left:165px;"
                                        ng-click="saveTimeProperty(attribute)"><i
                                        class="fa fa-check"></i>
                                </button>

                                <button class="btn btn-sm btn-default" type="button" style="margin-top:-195px;"
                                        title="{{cancelChangesTitle}}"
                                        ng-click="runCaseBasicVm.cancelTime(attribute)"><i class="fa fa-times"></i>
                                </button>
                            </div>
                        </div>

                        <div ng-if="attribute.showTimeAttribute == true">
                            <div uib-timepicker
                                 ng-model="attribute.value.timeValue">
                            </div>
                            <button class="btn btn-sm btn-primary" type="button"
                                    style="margin-top:-195px;margin-left:165px;"
                                    ng-click="saveTimeProperty(attribute)"><i class="fa fa-check"></i>
                            </button>

                            <button class="btn btn-sm btn-default" type="button" style="margin-top:-195px;"
                                    ng-click="runCaseBasicVm.cancelTime(attribute)"><i class="fa fa-times"></i>
                            </button>

                        </div>

                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
