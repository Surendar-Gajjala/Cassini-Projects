<div style="position: relative;">
    <style scoped>
        table td {
            padding-top: 0px !important;
            padding-bottom: 0px !important;
        }
    </style>
    <div style="padding: 20px;">
        <div class="row" style="margin: 0;">
            <div ng-if="relatedItemAttributesVm.attributes.length == 0">
                <p translate>NO_ATTRIBUTES</p>
            </div>
            <div class="form-group" ng-repeat="attribute in relatedItemAttributesVm.attributes">
                <label class="col-sm-4 control-label" style="margin-top: 12px;text-align: right;font-size: 16px;">
                    {{attribute.attributeDef.name}} :
                </label>

                <div class="col-sm-8" style="margin-top: 12px;font-size: 16px;">
                    <%----------  String Attribute  --------%>

                    <div ng-if="attribute.attributeDef.dataType == 'TEXT'">
                        <a ng-if="relatedItemAttributesVm.lifeCycleStatus == 'PRELIMINARY' && attribute.editMode == false && attribute.value.stringValue != null"
                           href="" title="{{changeValue}}" ng-click="changeAttribute(attribute)">
                            {{attribute.value.stringValue}}
                        </a>
                        <a ng-if="relatedItemAttributesVm.lifeCycleStatus == 'PRELIMINARY' && attribute.editMode == false"
                           href="" ng-click="changeAttribute(attribute)">
                            <i style="padding-left: 7px" title="{{'CLICK_TO_SET_VALUE' | translate}}"
                               class="fa fa-pencil"></i>
                        </a>

                <span ng-if="relatedItemAttributesVm.lifeCycleStatus == 'RELEASED' || relatedItemAttributesVm.lifeCycleStatus == 'OBSOLETE'">
                    {{attribute.value.stringValue}}
                </span>
                    </div>

                    <%----------  longText Attribute  --------%>

                    <div ng-if="attribute.attributeDef.dataType == 'LONGTEXT'">
                        <a ng-if="relatedItemAttributesVm.lifeCycleStatus == 'PRELIMINARY' && attribute.editMode == false && attribute.value.longTextValue != null"
                           href="" title="{{changeValue}}" ng-click="changeAttribute(attribute)">
                            {{attribute.value.longTextValue}}
                        </a>
                        <a ng-if="relatedItemAttributesVm.lifeCycleStatus == 'PRELIMINARY' && attribute.editMode == false"
                           href="" ng-click="changeAttribute(attribute)">
                            <i style="padding-left: 7px" title="{{'CLICK_TO_SET_VALUE' | translate}}"
                               class="fa fa-pencil"></i>
                        </a>

                <span ng-if="relatedItemAttributesVm.lifeCycleStatus == 'RELEASED' || relatedItemAttributesVm.lifeCycleStatus == 'OBSOLETE'">
                    {{attribute.value.longTextValue}}
                </span>
                    </div>

                    <%----------  Integer Attribute  --------%>

                    <div id="intClick" ng-if="attribute.attributeDef.dataType == 'INTEGER'">
                        <a ng-if="relatedItemAttributesVm.lifeCycleStatus == 'PRELIMINARY' && attribute.editMode == false && attribute.value.integerValue != null"
                           href="" title="{{changeValue}}" ng-click="changeAttribute(attribute)">
                            {{attribute.value.integerValue}}
                        </a>
                        <a ng-if="relatedItemAttributesVm.lifeCycleStatus == 'PRELIMINARY' && attribute.editMode == false"
                           href="" ng-click="changeAttribute(attribute)">
                            <i style="padding-left: 7px" title="{{'CLICK_TO_SET_VALUE' | translate}}"
                               class="fa fa-pencil"></i>
                        </a>
                <span ng-if="relatedItemAttributesVm.lifeCycleStatus == 'RELEASED' || relatedItemAttributesVm.lifeCycleStatus == 'OBSOLETE'">
                    {{attribute.value.integerValue}}</span>
                    </div>

                    <%----------  Double Attribute  --------%>

                    <div ng-if="attribute.attributeDef.dataType == 'DOUBLE'">
                        <a ng-if="relatedItemAttributesVm.lifeCycleStatus == 'PRELIMINARY' && attribute.editMode == false && attribute.value.doubleValue != null"
                           href="" title="{{changeValue}}" ng-click="changeAttribute(attribute)">
                            {{attribute.value.doubleValue || 'CLICK_TO_SET_VALUE' | translate}}
                        </a>
                        <a ng-if="relatedItemAttributesVm.lifeCycleStatus == 'PRELIMINARY' && attribute.editMode == false"
                           href="" ng-click="changeAttribute(attribute)">
                            <i style="padding-left: 7px" title="{{'CLICK_TO_SET_VALUE' | translate}}"
                               class="fa fa-pencil"></i>
                        </a>
                <span ng-if="relatedItemAttributesVm.lifeCycleStatus == 'RELEASED' || relatedItemAttributesVm.lifeCycleStatus == 'OBSOLETE'">
                    {{attribute.value.doubleValue}}</span>
                    </div>

                    <%----------  Date Attribute  --------%>

                    <div ng-if="attribute.attributeDef.dataType == 'DATE' && attribute.editMode == false">
                        <a ng-if="attribute.editMode == false && relatedItemAttributesVm.lifeCycleStatus == 'PRELIMINARY' && attribute.value.dateValue != null"
                           href="" title="{{changeDate}}" ng-click="changeAttribute(attribute)">
                            {{ (attribute.value.dateValue | date:"dd/MM/yyyy")}}
                        </a>
                        <a ng-if="attribute.editMode == false && relatedItemAttributesVm.lifeCycleStatus == 'PRELIMINARY'"
                           href="" title="{{clickToChangeValue}}" ng-click="changeAttribute(attribute)">
                            <i style="padding-left: 7px" title="{{'CLICK_TO_SET_VALUE' | translate}}"
                               class="fa fa-pencil"></i>
                        </a>
                <span ng-if="relatedItemAttributesVm.lifeCycleStatus == 'RELEASED' || relatedItemAttributesVm.lifeCycleStatus == 'OBSOLETE'">
                    {{attribute.value.dateValue}}</span>
                    </div>

                    <%---------  Boolean Attribute  --------%>

                    <div ng-if="attribute.attributeDef.dataType == 'BOOLEAN'">
                        <a href=""
                           ng-if="relatedItemAttributesVm.lifeCycleStatus == 'PRELIMINARY' && attribute.editMode == false"
                           title="{{clickToChangeValue}}" ng-click="changeAttribute(attribute)">
                            {{attribute.value.booleanValue}}
                        </a>
                        <a href="">
                            <i style="padding-left: 7px" ng-click="changeAttribute(attribute)"
                               ng-hide="attribute.editMode == true"
                               title="{{'CLICK_TO_EDIT_VALUE' | translate}}"
                               class="fa fa-pencil"></i>
                        </a>

                <span ng-if="relatedItemAttributesVm.lifeCycleStatus == 'RELEASED' || relatedItemAttributesVm.lifeCycleStatus == 'OBSOLETE'">
                    {{attribute.value.booleanValue}}</span>
                    </div>


                    <div ng-if="attribute.editMode == true && relatedItemAttributesVm.lifeCycleStatus == 'PRELIMINARY' &&
            (attribute.attributeDef.dataType == 'TEXT' || attribute.attributeDef.dataType == 'LONGTEXT' || attribute.attributeDef.dataType == 'INTEGER' ||
             attribute.attributeDef.dataType == 'DOUBLE' || attribute.attributeDef.dataType == 'BOOLEAN' ||
             attribute.attributeDef.dataType == 'DATE' || attribute.attributeDef.dataType == 'CURRENCY')">
                        <input type="text" class="form-control" name="title"
                               ng-if="attribute.attributeDef.dataType == 'TEXT'"
                               placeholder="{{'ENTER_VALUE' | translate}}"
                               ng-model="attribute.value.stringValue" style="width:200px;">
                        <input type="text" class="form-control" name="title"
                               ng-if="attribute.attributeDef.dataType == 'LONGTEXT'"
                               placeholder="{{'ENTER_VALUE' | translate}}"
                               ng-model="attribute.value.longTextValue" style="width:300px;">
                        <input type="number" class="form-control" name="title"
                               ng-if="attribute.attributeDef.dataType == 'INTEGER'"
                               placeholder="{{'ENTER_NUMBER' | translate}}"
                               ng-model="attribute.value.integerValue" style="width:200px;">
                        <input type="number" class="form-control" name="title"
                               ng-if="attribute.attributeDef.dataType == 'DOUBLE'"
                               placeholder="{{'ENTER_NUMBER' | translate}}"
                               ng-model="attribute.value.doubleValue" style="width:200px;">
                        <label class="radio-inline"
                               ng-if="attribute.attributeDef.dataType == 'BOOLEAN'">
                            <input type="checkbox" name="change_{{attribute.attributeDef.id}}"
                                   ng-click="relatedItemAttributesVm.saveRelatedItemAttribute(attribute)"
                                   ng-model="attribute.value.booleanValue">
                        </label>

                        <div class="input-group" ng-if="attribute.attributeDef.dataType == 'DATE'"
                             style="width: 200px;">
                            <input type="text" class="form-control" date-picker-edit
                                   ng-model="attribute.value.dateValue"
                                   name="attDate" placeholder="dd/mm/yyyy">
                                    <span class="input-group-addon">
                                        <i class="glyphicon glyphicon-calendar"></i></span>
                        </div>
                <span ng-if="attribute.editMode == true && relatedItemAttributesVm.lifeCycleStatus == 'PRELIMINARY' && attribute.attributeDef.dataType == 'CURRENCY'"
                      ng-bind-html="attribute.value.encodedCurrencyType">
                </span>
                        <input type="text" class="form-control" name="title"
                               ng-if="attribute.attributeDef.dataType == 'CURRENCY'"
                               ng-model="attribute.value.currencyValue"
                               style="width:185px;margin-top:-25px;margin-left:15px;">
                        <button class="btn btn-sm btn-primary" type="button" title="{{saveAttributeTitle}}"
                                ng-if="attribute.attributeDef.dataType != 'BOOLEAN'"
                                style="margin-top:-65px;margin-left:205px;padding: 8px;width:33px;"
                                ng-click="relatedItemAttributesVm.saveRelatedItemAttribute(attribute)"><i
                                class="fa fa-check"></i>
                        </button>

                        <button class="btn btn-sm btn-default" type="button" title="{{cancelChangesTitle}}"
                                ng-if="attribute.attributeDef.dataType != 'BOOLEAN'"
                                style="margin-top:-65px;padding: 8px;width:33px;"
                                ng-click="cancelChanges(attribute)"><i class="fa fa-times"></i>
                        </button>
                    </div>

                    <%---------  List Attribute  --------%>

                    <div ng-if="attribute.attributeDef.dataType == 'LIST' && !attribute.attributeDef.listMultiple">
                        <a ng-if="relatedItemAttributesVm.lifeCycleStatus == 'PRELIMINARY'"
                           title="{{clickToChangeValue}}" editable-select="attribute.value.listValue"
                           href="" e-style="width:200px;"
                           e-ng-options="value for value in attribute.attributeDef.lov.values track by value"
                           onaftersave="relatedItemAttributesVm.saveRelatedItemAttribute(attribute)">
                            {{attribute.value.listValue}}
                            <i style="padding: 6px;color: #337ab7" class="fa fa-pencil"
                               title="{{'CLICK_TO_SET_VALUE' | translate}}"></i>
                        </a>

                <span ng-if="relatedItemAttributesVm.lifeCycleStatus == 'RELEASED' || relatedItemAttributesVm.lifeCycleStatus == 'OBSOLETE'">
                    {{attribute.value.listValue}}</span>
                    </div>


                    <div ng-if="attribute.attributeDef.dataType == 'LIST' && attribute.attributeDef.listMultiple == true">
                        <div ng-if="attribute.value.mlistValue.length > 0" ng-hide="attribute.editMode == true"
                             ng-repeat="listValue in attribute.value.mlistValue">
                            <ul>
                                <li>{{listValue}}</li>
                            </ul>
                        </div>
                        <a ng-if="attribute.editMode == false && attribute.value.mlistValue.length == 0"
                           href="" ng-click="relatedItemAttributesVm.editListValue(attribute)">
                            <i style="padding: 6px;color: #337ab7" class="fa fa-pencil"
                               ng-click="changeAttribute(attribute)"
                               title="{{'CLICK_TO_SET_VALUE' | translate}}"></i>
                        </a>

                        <a ng-if="attribute.editMode == false && attribute.value.mlistValue.length > 0"
                           href="" ng-click="relatedItemAttributesVm.editListValue(attribute)">
                            <i style="padding: 6px;color: #337ab7" class="fa fa-pencil"
                               title="{{'CLICK_TO_SET_VALUE' | translate}}"></i>
                        </a>

                        <div ng-if="attribute.editMode" style="width: 400px; display: inline-flex">
                            <div style="width: 350px">
                                <ui-select multiple ng-model="attribute.value.mlistValue" theme="bootstrap"
                                           close-on-select="false" title="Choose a List" remove-selected="true">
                                    <ui-select-match placeholder="Select list...">{{$item}}
                                    </ui-select-match>
                                    <ui-select-choices
                                            repeat="value in attribute.attributeDef.lov.values track by value">
                                        <div ng-bind="value"></div>
                                    </ui-select-choices>
                                </ui-select>
                            </div>
                            <div style="width: 25px">
                                <a ng-if="attribute.attributeDef.dataType == 'LIST'"
                                   ng-click="relatedItemAttributesVm.saveRelatedItemAttribute(attribute)"
                                   title="{{saveAttributeTitle}}">
                                    <i class="fa fa-check-circle" style="padding: 8px;cursor:pointer;"></i>
                                </a>
                            </div>
                            <div style="width: 25px;">
                                <a ng-if="attribute.attributeDef.dataType == 'LIST'"
                                   ng-click="relatedItemAttributesVm.cancelChanges(attribute)"
                                   title="{{cancelChangesTitle}}"
                                   style="padding: 8px;cursor:pointer;">
                                    <i class="fa fa-times-circle" style="padding: 8px;cursor:pointer;"></i>
                                </a>
                            </div>
                        </div>

                    </div>

                    <%------  Time Attribute  --------%>

                    <div ng-if="attribute.attributeDef.dataType == 'TIME'">
                        <a href="" title="{{changeTime}}"
                           ng-show="attribute.showTimeAttribute == false && relatedItemAttributesVm.lifeCycleStatus == 'PRELIMINARY' && attribute.value.timeValue != null"
                           ng-click="relatedItemAttributesVm.changeTime(attribute)">
                            {{attribute.value.timeValue}}
                        </a>
                        <a href=""
                           ng-show="attribute.showTimeAttribute == false && relatedItemAttributesVm.lifeCycleStatus == 'PRELIMINARY'"
                           ng-click="relatedItemAttributesVm.changeTime(attribute)">
                            <i style="padding: 6px;color: #337ab7" class="fa fa-pencil"
                               title="{{'CLICK_TO_SET_VALUE' | translate}}"></i>
                        </a>

                <span ng-show="attribute.showTimeAttribute == true && relatedItemAttributesVm.lifeCycleStatus == 'PRELIMINARY'">
                    {{attribute.value.timeValue}}
                </span>
                <span ng-if="relatedItemAttributesVm.lifeCycleStatus == 'RELEASED' || relatedItemAttributesVm.lifeCycleStatus == 'OBSOLETE'">
                    {{attribute.value.timeValue}}
                </span>

                        <div ng-if="attribute.showTimeAttribute == true && relatedItemAttributesVm.lifeCycleStatus == 'PRELIMINARY'">
                            <div uib-timepicker
                                 ng-model="attribute.timeValue">
                            </div>
                            <button class="btn btn-sm btn-primary" type="button"
                                    style="margin-top:-195px;margin-left:165px;"
                                    ng-click="relatedItemAttributesVm.saveTime(attribute)"><i class="fa fa-check"></i>
                            </button>

                            <button class="btn btn-sm btn-default" type="button" style="margin-top:-195px;"
                                    ng-click="relatedItemAttributesVm.cancelTime(attribute)"><i class="fa fa-times"></i>
                            </button>

                        </div>
                    </div>

                    <%------  TimeStamp Attribute  --------%>

                    <div ng-if="attribute.attributeDef.dataType == 'TIMESTAMP'">
                        <a href="" title="{{changeTime}}"
                           ng-if="relatedItemAttributesVm.lifeCycleStatus == 'PRELIMINARY' && attribute.showTimestamp == false"
                           ng-click="relatedItemAttributesVm.changeTimestamp(attribute)">
                            {{attribute.value.timestampValue}}
                        </a>
                        <a href=""
                           ng-if="relatedItemAttributesVm.lifeCycleStatus == 'PRELIMINARY' && attribute.showTimestamp == false"
                           ng-click="relatedItemAttributesVm.changeTimestamp(attribute)">
                            <i style="padding: 6px;color: #337ab7" class="fa fa-pencil"
                               title="{{'CLICK_TO_SET_VALUE' | translate}}"></i>
                        </a>
                <span ng-if="relatedItemAttributesVm.lifeCycleStatus == 'RELEASED' || relatedItemAttributesVm.lifeCycleStatus == 'OBSOLETE'">
                    {{attribute.value.timestampValue}}
                </span>

                        <div ng-if="attribute.showTimestamp == true">
                            <div uib-timepicker
                                 ng-model="attribute.timestampValue">
                            </div>
                            <button class="btn btn-sm btn-primary" type="button"
                                    style="margin-top:-195px;margin-left:165px;"
                                    ng-click="relatedItemAttributesVm.saveTime(attribute)"><i class="fa fa-check"></i>
                            </button>

                            <button class="btn btn-sm btn-default" type="button" style="margin-top:-195px;"
                                    ng-click="relatedItemAttributesVm.cancelTime(attribute)"><i class="fa fa-times"></i>
                            </button>
                        </div>
                    </div>

                    <%-------  Currency Attribute  --------%>

                    <div ng-if="attribute.attributeDef.dataType == 'CURRENCY'">
                        <div ng-if="attribute.value.encodedCurrencyType != null">
                            <span ng-if="attribute.editMode == false && relatedItemAttributesVm.lifeCycleStatus == 'PRELIMINARY'"
                                  ng-bind-html="attribute.value.encodedCurrencyType">
                            </span>
                            <a href=""
                               ng-if="attribute.editMode == false && relatedItemAttributesVm.lifeCycleStatus == 'PRELIMINARY' && attribute.value.currencyValue != null"
                               title="{{changeCurrencyValueTitle}}"
                               ng-click="changeAttribute(attribute)">
                                {{attribute.value.currencyValue}}
                            </a>
                            <a href=""
                               ng-if="attribute.editMode == false && relatedItemAttributesVm.lifeCycleStatus == 'PRELIMINARY'"
                               ng-click="changeAttribute(attribute)">
                                <i style="padding: 6px;color: #337ab7" class="fa fa-pencil"
                                   title="{{setCurrencyValue}}"></i>
                            </a>
                        </div>

                        <%----------  For newly added 'CURRENCY' attribute after ITEM Creation ---------------%>

                        <div ng-if="attribute.value.encodedCurrencyType == null || attribute.value.currencyType == null">
                            <div ng-if="attribute.changeCurrency == true" style="width: 150px;">
                                <select class="form-control" ng-model="attribute.value.currencyType"
                                        ng-init="attribute.value.currencyType = relatedItemAttributesVm.currencies[0].id"
                                        ng-options="currency.id as currency.name for currency in relatedItemAttributesVm.currencies">
                                </select>
                            </div>
                            <a href=""
                               ng-if="relatedItemAttributesVm.lifeCycleStatus == 'PRELIMINARY' && attribute.changeCurrency == false && attribute.value.currencyValue != null"
                               ng-click="relatedItemAttributesVm.changeCurrencyValue(attribute)"
                               title="{{changeCurrencyValueTitle}}">
                                {{attribute.value.currencyValue}}
                            </a>
                            <a href=""
                               ng-if="relatedItemAttributesVm.lifeCycleStatus == 'PRELIMINARY' && attribute.changeCurrency == false && attribute.value.currencyValue == null"
                               ng-click="relatedItemAttributesVm.changeCurrencyValue(attribute)">
                                <i style="padding: 6px;color: #337ab7" class="fa fa-pencil"
                                   title="{{setCurrencyValue}}"></i>
                            </a>

                            <div ng-if="attribute.changeCurrency == true"
                                 style="width: 100px;margin-top: -40px;margin-left: 155px;">
                                <input class="form-control" name="currencyValue" type="number"
                                       placeholder="{{'ENTER_VALUE' | translate}}"
                                       ng-model="attribute.value.currencyValue"/>
                            </div>
                            <button ng-if="attribute.changeCurrency == true" class="btn btn-sm btn-primary"
                                    title="{{saveCurrencyTitle}}"
                                    ng-click="relatedItemAttributesVm.saveRelatedItemAttribute(attribute)"
                                    style="margin-top:-64px;margin-left: 260px;padding-top:10px;">
                                <i class="fa fa-check"></i>
                            </button>
                            <button ng-if="attribute.changeCurrency == true" class="btn btn-sm btn-default"
                                    title="{{cancelChangesTitle}}"
                                    ng-click="relatedItemAttributesVm.cancelChanges(attribute)"
                                    style="margin-top:-64px;padding-top:10px;">
                                <i class="fa fa-times"></i>
                            </button>
                        </div>

                        <span ng-if="relatedItemAttributesVm.lifeCycleStatus == 'RELEASED' || relatedItemAttributesVm.lifeCycleStatus == 'OBSOLETE'"
                              ng-bind-html="attribute.value.encodedCurrencyType">
                        </span>
                        <span ng-if="relatedItemAttributesVm.lifeCycleStatus == 'RELEASED' || relatedItemAttributesVm.lifeCycleStatus == 'OBSOLETE'">
                            {{attribute.value.currencyValue}}
                        </span>

                    </div>


                    <%--------------- RichTextValue --------------%>

                    <div ng-if="attribute.attributeDef.dataType == 'RICHTEXT'">
                <span>
                    <a ng-if="attribute.editMode == false && attribute.value.encodedRichTextValue == null"
                       href="" ng-click="relatedItemAttributesVm.editRichText(attribute)">
                        <i style="padding: 6px;color: #337ab7" class="fa fa-pencil"
                           title="{{'CLICK_TO_SET_VALUE' | translate}}"></i>
                    </a>

                       <span ng-if="attribute.editMode == false && attribute.value.encodedRichTextValue != null"
                             ng-bind-html="attribute.value.encodedRichTextValue">
                       </span>
                   <span>
                        <a ng-if="attribute.editMode == false && attribute.value.encodedRichTextValue != null" href=""
                           ng-click="relatedItemAttributesVm.editRichText(attribute)">
                            <i style="padding: 6px;color: #337ab7" class="fa fa-pencil"
                               title="{{'CLICK_TO_SET_VALUE' | translate}}"></i>
                        </a>
                    </span>

                </span>

                        <div ng-show="attribute.editMode == true">
                            <summernote ng-model="attribute.value.richTextValue"></summernote>
                        </div>

                        <button ng-show="attribute.editMode == true"
                                ng-click="relatedItemAttributesVm.saveRichText(attribute)"><i class="fa fa-check"></i>
                        </button>

                        <button ng-show="attribute.editMode == true"
                                ng-click="relatedItemAttributesVm.cancelRichText(attribute)"><i class="fa fa-times"></i>
                        </button>


                    </div>


                    <%-------  Object Attribute  --------%>

                    <div class="row" ng-if="attribute.attributeDef.dataType == 'OBJECT'">

                        <a ng-if="attribute.value.refValue.objectType == 'ITEM'" href=""
                           ng-click="relatedItemAttributesVm.showRefValueDetails(attribute)"
                           title="{{'CLICK_TO_SHOW_DETAILS' | translate}}">
                            {{attribute.value.refValue.itemNumber}}
                        </a>

                        <a ng-if="attribute.value.refValue.objectType == 'ITEMREVISION'" href=""
                           ng-click="relatedItemAttributesVm.showRefValueDetails(attribute)"
                           title="{{'CLICK_TO_SHOW_DETAILS' | translate}}">
                            {{attribute.value.refValue.itemMaster+" "+attribute.value.refValue.revision+"
                            "+attribute.value.refValue.lifeCyclePhase.phase}}
                        </a>


                        <a ng-if="attribute.value.refValue.objectType == 'CHANGE'" href="">
                            <span ng-if="attribute.value.refValue.changeType == 'ECO'"
                                  ng-click="showRefValueDetails(attribute)"
                                  title="{{'CLICK_TO_SHOW_DETAILS' | translate}}"
                                  ng-bind-html="attribute.value.refValue.ecoNumber">
                            </span>
                            <span ng-if="attribute.value.refValue.changeType == 'DCO'"
                                  ng-click="showRefValueDetails(attribute)"
                                  title="{{'CLICK_TO_SHOW_DETAILS' | translate}}"
                                  ng-bind-html="attribute.value.refValue.dcoNumber">
                            </span>
                            <span ng-if="attribute.value.refValue.changeType == 'ECR' || attribute.value.refValue.changeType == 'DCR'"
                                  ng-click="showRefValueDetails(attribute)"
                                  title="{{'CLICK_TO_SHOW_DETAILS' | translate}}"
                                  ng-bind-html="attribute.value.refValue.crNumber">
                            </span>
                            <span ng-if="attribute.value.refValue.changeType == 'MCO'"
                                  ng-click="showRefValueDetails(attribute)"
                                  title="{{'CLICK_TO_SHOW_DETAILS' | translate}}"
                                  ng-bind-html="attribute.value.refValue.mcoNumber">
                            </span>
                        </a>

                        <a ng-if="attribute.value.refValue.objectType == 'PLMWORKFLOWDEFINITION'" href=""
                           ng-click="relatedItemAttributesVm.showRefValueDetails(attribute)"
                           title="{{'CLICK_TO_SHOW_DETAILS' | translate}}">
                            {{attribute.value.refValue.name}}
                        </a>

                        <a ng-if="attribute.value.refValue.objectType == 'MANUFACTURER'" href=""
                           ng-click="relatedItemAttributesVm.showRefValueDetails(attribute)"
                           title="{{'CLICK_TO_SHOW_DETAILS' | translate}}">
                            {{attribute.value.refValue.name}}
                        </a>

                        <a ng-if="attribute.value.refValue.objectType == 'MANUFACTURERPART'" href=""
                           ng-click="relatedItemAttributesVm.showRefValueDetails(attribute)"
                           title="{{'CLICK_TO_SHOW_DETAILS' | translate}}">
                            {{attribute.value.refValue.partName}}
                        </a>
                        <a ng-if="attribute.value.refValue.objectType == 'PERSON'">
                            {{attribute.value.refValue.firstName}}
                        </a>
                        <a href=""
                           ng-if="attribute.value.refValue == null && relatedItemAttributesVm.lifeCycleStatus == 'PRELIMINARY'"
                           title="{{addAttribute}}"
                           ng-click="relatedItemAttributesVm.showSelectionDialog(attribute.attributeDef.refType, attribute)">
                            <i style="padding: 6px;color: #337ab7" class="fa fa-pencil"
                               title="{{'ADD_ATTRIBUTE' | translate}}"></i>

                        </a>
                        <button ng-if="attribute.editMode == false && attribute.value.refValue != null && relatedItemAttributesVm.lifeCycleStatus == 'PRELIMINARY'"
                                class="btn btn-xs btn-warning"
                                title="{{changeAttributeTitle}}" style="margin-left:5px;"
                                ng-click="relatedItemAttributesVm.showSelectionDialog(attribute.attributeDef.refType, attribute)">
                            <i class="fa fa-edit"></i>
                        </button>
                        <button ng-if="attribute.editMode == true" class="btn btn-xs btn-primary"
                                title="{{'SAVE_ATTRIBUTE' | translate}}"
                                style="margin-left:5px;"
                                ng-click="relatedItemAttributesVm.saveObject(attribute)">
                            <i class="fa fa-check"></i>
                        </button>

                        <button ng-if="attribute.editMode == true" class="btn btn-xs btn-default"
                                title="{{cancelChangesTitle}}"
                                ng-click="cancelChanges(attribute)">
                            <i class=" fa fa-times"></i>
                        </button>
                    </div>


                    <%------  Image Attribute  --------%>

                    <div ng-if="attribute.attributeDef.dataType == 'IMAGE'">
                        <a href="" ng-click="relatedItemAttributesVm.showImage(attribute)"
                           title="{{'CLICK_TO_SHOW_LARGE_IMAGE' | translate}}">
                            <img ng-if="attribute.value.imageValue != null"
                                 ng-src="{{attribute.value.itemImagePath}}"
                                 style="height: 100px;width: 100px;margin-bottom: 5px;">
                        </a>
                        <%-- To show large image --%>
                        <div id="myModal2" class="img-model modal">
                            <span class="closeImage">&times;</span>
                            <img class="modal-content" id="img03">
                        </div>

                        <div class="inline">
                            <a href=""
                               ng-if="attribute.value.imageValue == null && relatedItemAttributesVm.lifeCycleStatus == 'PRELIMINARY'"
                               ng-click="relatedItemAttributesVm.change(attribute)"
                               ng-hide="attribute.changeImage == true" title="{{addImage}}">
                                <span translate>
                                    <i style="padding: 6px;color: #337ab7" class="fa fa-pencil"
                                       title="{{'ADD_IMAGE' | translate}}"></i>
                                </span>
                            </a>
                            <button ng-if="attribute.value.imageValue != null && relatedItemAttributesVm.lifeCycleStatus == 'PRELIMINARY'"
                                    class="btn btn-sm btn-warning"
                                    ng-click="relatedItemAttributesVm.change(attribute)"
                                    ng-hide="attribute.changeImage == true" title="{{changeImageMessage}}"><i
                                    class="fa fa-edit"></i>
                            </button>
                            <input ng-show="attribute.changeImage == true" class="browse-control" name="file"
                                   type="file" ng-file-model="attribute.newImageValue" style="width: 250px;">
                            <button class="btn btn-sm btn-primary"
                                    ng-click="relatedItemAttributesVm.saveImage(attribute)"
                                    ng-show="attribute.changeImage == true" title="{{'SAVE_IMAGE' | translate}}"
                                    style="margin-top:-62px;margin-left: 255px;padding-top:8px;"><i
                                    class="fa fa-check"></i>
                            </button>
                            <button class="btn btn-sm btn-default"
                                    ng-click="relatedItemAttributesVm.cancel(attribute)" title="{{cancelChangesTitle}}"
                                    ng-show="attribute.changeImage == true" style="margin-top:-62px;padding-top:8px;"><i
                                    class="fa fa-times"></i>
                            </button>
                        </div>
                    </div>

                    <%------  Attachment Attribute  --------%>

                    <div ng-if="attribute.attributeDef.dataType == 'ATTACHMENT'">
                        <div ng-if="attribute.value.attachmentValues.length > 0"
                             ng-repeat="attachment in attribute.value.attachmentValues">
                            <ul style="margin-left:-30px;">
                                <li>
                                    <a href="" ng-click="relatedItemAttributesVm.openAttachment(attachment)"
                                       title="{{'CLICK_TO_DOWNLOAD_ATTACHMENT' | translate}}"
                                       style="margin-bottom: 5px;width:250px;color: #002451;">
                                        {{attachment.name}}
                                    </a>
                                    <a href="" ng-if="relatedItemAttributesVm.lifeCycleStatus == 'PRELIMINARY'"
                                       title="{{deleteAttachment}}"
                                       ng-click="relatedItemAttributesVm.deleteAttachments(attribute,attachment)">
                                        <i class="fa fa-times"
                                           style="margin-left: 5px;font-size: 14px;color: darkred"></i>
                                    </a>
                                </li>
                            </ul>
                        </div>

                        <div ng-if="relatedItemAttributesVm.lifeCycleStatus == 'PRELIMINARY'">
                        <span ng-show="attribute.showAttachment == false"
                              ng-click="relatedItemAttributesVm.addAttachment(attribute)"
                              title="{{addAttachments}}">
                                <i style="padding: 6px;color: #337ab7" class="fa fa-pencil"
                                   title="{{'ADD_ATTACHMENTS' | translate}}"></i>
                        </span>

                            <div ng-show="attribute.showAttachment == true">
                                <input class="browse-control" name="file" multiple="true"
                                       type="file" ng-file-model="attribute.attachmentValues" style="width: 250px;"/>
                                <button class="btn btn-sm btn-primary"
                                        ng-click="relatedItemAttributesVm.saveAttachments(attribute)"
                                        title="{{saveAttachmentsTitle}}"
                                        style="margin-top:-62px;margin-left: 255px;padding-top:8px;">
                                    <i class="fa fa-check"></i>
                                </button>
                                <button class="btn btn-sm btn-default"
                                        ng-click="relatedItemAttributesVm.cancelAttachment(attribute)"
                                        title="{{cancelChangesTitle}}"
                                        style="margin-top:-62px;padding-top:8px;">
                                    <i class="fa fa-times"></i>
                                </button>
                                <p ng-repeat="file in attribute.attachmentValues">
                                    {{file.name}}
                                </p>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
