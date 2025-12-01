<div style="margin-top: 15px;">
    <style scoped>
        .datetimepicker .table-condensed {
            width: 300px !important;
            font-size: 14px;
        }

        .datetimepicker thead tr:first-child th .glyphicon-arrow-right::before {
            display: none !important;
        }

        .datetimepicker thead tr:first-child th .glyphicon-arrow-left::before {
            display: none !important;
        }

        .datetimepicker thead tr:first-child th:hover .glyphicon-arrow-right::before {
            display: none !important;
        }

        .datetimepicker thead tr:first-child th:hover .glyphicon-arrow-left::before {
            display: none !important;
        }

        .datetimepicker thead tr:first-child th:hover, .datetimepicker tfoot th:hover, .datetimepicker table tr td.day:hover {
            visibility: hidden !important;
        }
    </style>
    <div class="form-group" ng-repeat="attribute in objectProperties track by $index"
         ng-hide="checkForHideAttribute(attribute) == true">
        <label ng-if="attribute.attributeDef.dataType != 'FORMULA'" ng-init="index = $index"
               class="col-sm-4 control-label"><span style="word-wrap: break-word; !important;">{{attribute.attributeDef.name}}</span>
            <span ng-if="attribute.attributeDef.required == true" class="asterisk">*</span><span
                    style="position: inherit !important;">:</span>
        </label>

        <div class="col-sm-7">
            <input type="text" class="form-control" name="title"
                   ng-change="checkValidations(attribute)"
                   ng-if="attribute.attributeDef.dataType == 'TEXT'"
                   ng-model="attribute.stringValue">
        <textarea type="text" class="form-control" name="title"
                  ng-if="attribute.attributeDef.dataType == 'LONGTEXT'"
                  ng-change="checkValidations(attribute)"
                  ng-model="attribute.longTextValue"></textarea>
            <input type="text" class="form-control" name="title" maxlength="9"
                   ng-if="attribute.attributeDef.dataType == 'INTEGER'"
                   ng-model="attribute.integerValue" numbers-only>
            <input type="text" class="form-control" name="title"
                   ng-if="attribute.attributeDef.dataType == 'DOUBLE' && attribute.attributeDef.measurement == null"
                   ng-model="attribute.doubleValue" valid-number>
            <input type="text" class="form-control" name="title"
                   ng-if="attribute.attributeDef.dataType == 'HYPERLINK'"
                   ng-model="attribute.hyperLinkValue">

            <summernote ng-if="attribute.attributeDef.dataType == 'RICHTEXT'"
                        ng-model="attribute.richTextValue"></summernote>

            <div class="input-group" ng-if="attribute.attributeDef.dataType == 'DATE'">
                <input type="text" class="form-control" date-picker-edit
                       ng-model="attribute.dateValue"
                       name="attDate" placeholder="dd/mm/yyyy">
                                    <span class="input-group-addon">
                                        <i class="glyphicon glyphicon-calendar"></i></span>
            </div>

            <div ng-if="attribute.attributeDef.dataType == 'DOUBLE' && attribute.attributeDef.measurement != null">
                <div class="col-sm-5"
                     style="margin-left: -10px;padding-right: 0;">
                    <select class="form-control" ng-model="attribute.measurementUnit" style="padding: 9px;"
                            ng-options="measurementUnit as measurementUnit.name for measurementUnit in attribute.attributeDef.measurement.measurementUnits">
                    </select>
                </div>
                <div class="col-sm-offset-5">
                    <input class="form-control input-sm" type="text" placeholder="Enter value"
                           valid-number
                           ng-model="attribute.doubleValue" ng-disabled="attribute.measurementUnit == null"/>
                </div>
            </div>

            <div ng-if="object.configurable == true" style="margin-top: 14px;">
                <div ng-if="attribute.attributeDef.dataType == 'LIST' && attribute.attributeDef.configurable == true && attribute.attributeDef.listMultiple == false"
                     style="padding-right: 0px;">
                    <a class="icon fa fa-paperclip" style="padding-right: 4px; overflow-y: hidden !important;"
                       ng-if="attribute.lovValues.length > 0"
                       uib-popover-template="showitemAttributePopoverUrl"
                       popover-popup-delay="50"
                       popover-placement="right"
                       popover-trigger="'outsideClick'">
                    </a>
                </div>
            </div>

            <div ng-if="object.configurable != true || object.configured == true" style="margin-top: 7px;">
                <div ng-if="attribute.attributeDef.dataType == 'LIST' && attribute.attributeDef.listMultiple == false"
                     style="padding-right: 0px;">
                    <%--  <select class="form-control" ng-model="attribute.listValue"
                              ng-disabled="object.configured && object.bomConfig != null"
                              placeholder="select"
                              ng-options="value for value in attribute.lovValues">
                      </select>--%>
                    <ui-select ng-disabled="object.configured && object.bomConfig != null"
                               ng-model="attribute.listValue"
                               theme="bootstrap" style="width:100%">
                        <ui-select-match placeholder="Select">{{$select.selected}}</ui-select-match>
                        <ui-select-choices repeat="value in attribute.lovValues | filter: $select.search">
                            <div ng-bind="value"></div>
                        </ui-select-choices>
                    </ui-select>
                </div>
            </div>


            <div ng-if="attribute.attributeDef.dataType == 'LIST' && attribute.attributeDef.listMultiple == true"
                 style="padding-right: 0px;">
                <ui-select multiple ng-model="attribute.mlistValue" theme="bootstrap" checkboxes="true"
                           close-on-select="false" title="Choose a List" remove-selected="true">
                    <ui-select-match placeholder="Select listValues...">{{$item}}</ui-select-match>
                    <ui-select-choices repeat="value in attribute.lovValues">
                        <div ng-bind="value"></div>
                    </ui-select-choices>
                </ui-select>
            </div>

            <div ng-if="attribute.attributeDef.dataType == 'MULTILISTCHECKBOX'" style="padding-right: 0;">
                <div ng-repeat="value in attribute.lovValues">
                    <input type="checkbox"
                           ng-click="selectMultiListCheckBox(attribute,value)" style="margin-right: 5px;">{{value}}<br>
                </div>
            </div>

            <div class="input-group" ng-if="attribute.attributeDef.dataType == 'TIMESTAMP'">
                <span class="form-control">{{attribute.timestampValue}}</span>
            </div>


            <input class="form-control" type="text" ng-model="attribute.timeValue"
                   ng-if="attribute.attributeDef.dataType == 'TIME'" time-picker>

            <div ng-if="attribute.attributeDef.dataType == 'IMAGE'">
                <input class="form-control" name="file" accept="image/*"
                       type="file" ng-file-model="attribute.imageValue"/>
            </div>
            <div ng-if="attribute.attributeDef.dataType == 'ATTACHMENT'">
                <input class="form-control" name="file" multiple="true"
                       type="file" ng-file-model="attribute.attachmentValues"/>

                <p ng-if="attribute.attachmentValues.length > 1" ng-repeat="file in attribute.attachmentValues">
                    {{file.name}}
                </p>
            </div>
            <div>
                <div class="col-sm-5" ng-if="attribute.attributeDef.dataType == 'CURRENCY'"
                     style="margin-left: -10px;padding-right: 0px;">
                    <select class="form-control" ng-model="attribute.currencyType"
                            ng-init="attribute.currencyType = currencies[0].id"
                            ng-options="currency.id as currency.name for currency in currencies" style="height: 40px;">
                    </select>
                </div>
                <div class="col-sm-offset-5" ng-if="attribute.attributeDef.dataType == 'CURRENCY'">
                    <input class="form-control" name="currencyValue" type="number" placeholder="Enter currency value"
                           ng-model="attribute.currencyValue"/>
                </div>
            </div>
            <div ng-if="attribute.attributeDef.dataType == 'OBJECT'">
                <a ng-if="attribute.refValue == null" style="width: 85px;margin-top: 10px"
                   ng-click="showSelectionDialog(attribute.attributeDef.refType, attribute)">Select
                </a>
            <span style="margin-top:10px" class="input-group"
                  ng-if="attribute.refValue != null">
                <a href="" ng-click="showSelectionDialog(attribute.attributeDef.refType, attribute)">
                    {{attribute.refValueString}}
                </a></span>
            </div>
            <a class="icon fa fa-info-circle"
               ng-if="attribute.attributeDef.validations != null"
               style="position: relative;margin-top: -35px;margin-right: -25px;float: right;"
               title="Attribute validations"
               uib-popover-template="attributeValidationsPopover.templateUrl"
               popover-append-to-body="true"
               popover-popup-delay="50"
               popover-placement="left"
               popover-title="{{attribute.attributeDef.name}} validations"
               popover-trigger="'outsideClick'">
            </a>
        </div>
        <div class="col-sm-4">
            <label class="radio-inline"
                   ng-if="attribute.attributeDef.dataType == 'BOOLEAN'">
                <input type="checkbox" name="change_{{attribute.attributeDef.id}}"
                       ng-model="attribute.booleanValue">
            </label>
        </div>
    </div>

    <div ng-repeat="groupAttribute in groupAttributes">
        <div>
            <h4 class="section-title" translate>{{groupAttribute.groupName}}</h4>
        </div>
        <div class="form-group" ng-repeat="attribute in groupAttribute.objectProperties track by $index">
            <label ng-init="index = $index" class="col-sm-4 control-label"><span
                    style="word-wrap: break-word; !important;">{{attribute.attributeDef.name}}</span>
                <span ng-if="attribute.attributeDef.required == true" class="asterisk">*</span><span
                        style="position: inherit !important;">:</span>
            </label>

            <div class="col-sm-7">
                <input type="text" class="form-control" name="title"
                       ng-change="checkValidations(attribute)"
                       ng-if="attribute.attributeDef.dataType == 'TEXT'"
                       ng-model="attribute.stringValue">
        <textarea type="text" class="form-control" name="title"
                  ng-if="attribute.attributeDef.dataType == 'LONGTEXT'"
                  ng-change="checkValidations(attribute)"
                  ng-model="attribute.longTextValue"></textarea>
                <input type="text" class="form-control" name="title" maxlength="9"
                       ng-if="attribute.attributeDef.dataType == 'INTEGER'"
                       ng-model="attribute.integerValue" numbers-only>
                <input type="text" class="form-control" name="title"
                       ng-if="attribute.attributeDef.dataType == 'DOUBLE' && attribute.attributeDef.measurement == null"
                       ng-model="attribute.doubleValue" valid-number>
                <input type="text" class="form-control" name="title"
                       ng-if="attribute.attributeDef.dataType == 'HYPERLINK'"
                       ng-model="attribute.hyperLinkValue">

                <summernote ng-if="attribute.attributeDef.dataType == 'RICHTEXT'"
                            ng-model="attribute.richTextValue"></summernote>

                <div class="input-group" ng-if="attribute.attributeDef.dataType == 'DATE'">
                    <input type="text" class="form-control" date-picker-edit
                           ng-model="attribute.dateValue"
                           name="attDate" placeholder="dd/mm/yyyy">
                                    <span class="input-group-addon">
                                        <i class="glyphicon glyphicon-calendar"></i></span>
                </div>

                <div ng-if="attribute.attributeDef.dataType == 'DOUBLE' && attribute.attributeDef.measurement != null">
                    <div class="col-sm-5"
                         style="margin-left: -10px;padding-right: 0;">
                        <select class="form-control" ng-model="attribute.measurementUnit" style="padding: 9px;"
                                ng-options="measurementUnit as measurementUnit.name for measurementUnit in attribute.attributeDef.measurement.measurementUnits">
                        </select>
                    </div>
                    <div class="col-sm-offset-5">
                        <input class="form-control input-sm" type="text" placeholder="Enter value"
                               valid-number
                               ng-model="attribute.doubleValue" ng-disabled="attribute.measurementUnit == null"/>
                    </div>
                </div>

                <div ng-if="object.configurable == true" style="margin-top: 14px;">
                    <div ng-if="attribute.attributeDef.dataType == 'LIST' && attribute.attributeDef.configurable == true && attribute.attributeDef.listMultiple == false"
                         style="padding-right: 0px;">
                        <a class="icon fa fa-paperclip" style="padding-right: 4px; overflow-y: hidden !important;"
                           ng-if="attribute.lovValues.length > 0"
                           uib-popover-template="showitemAttributePopoverUrl"
                           popover-popup-delay="50"
                           popover-placement="right"
                           popover-trigger="'outsideClick'">
                        </a>
                    </div>
                </div>

                <div ng-if="object.configurable != true || object.configured == true" style="margin-top: 7px;">
                    <div ng-if="attribute.attributeDef.dataType == 'LIST' && attribute.attributeDef.listMultiple == false"
                         style="padding-right: 0px;">
                        <%--  <select class="form-control" ng-model="attribute.listValue"
                                  ng-disabled="object.configured && object.bomConfig != null"
                                  placeholder="select"
                                  ng-options="value for value in attribute.lovValues">
                          </select>--%>
                        <ui-select ng-disabled="object.configured && object.bomConfig != null"
                                   ng-model="attribute.listValue"
                                   theme="bootstrap" style="width:100%">
                            <ui-select-match placeholder="Select">{{$select.selected}}</ui-select-match>
                            <ui-select-choices repeat="value in attribute.lovValues | filter: $select.search">
                                <div ng-bind="value"></div>
                            </ui-select-choices>
                        </ui-select>
                    </div>
                </div>


                <div ng-if="attribute.attributeDef.dataType == 'LIST' && attribute.attributeDef.listMultiple == true"
                     style="padding-right: 0px;">
                    <ui-select multiple ng-model="attribute.mlistValue" theme="bootstrap" checkboxes="true"
                               close-on-select="false" title="Choose a List" remove-selected="true">
                        <ui-select-match placeholder="Select listValues...">{{$item}}</ui-select-match>
                        <ui-select-choices repeat="value in attribute.lovValues">
                            <div ng-bind="value"></div>
                        </ui-select-choices>
                    </ui-select>
                </div>

                <div ng-if="attribute.attributeDef.dataType == 'MULTILISTCHECKBOX'" style="padding-right: 0;">
                    <div ng-repeat="value in attribute.lovValues">
                        <input type="checkbox"
                               ng-click="selectMultiListCheckBox(attribute,value)"
                               style="margin-right: 5px;">{{value}}<br>
                    </div>
                </div>

                <div class="input-group" ng-if="attribute.attributeDef.dataType == 'TIMESTAMP'">
                    <span class="form-control">{{attribute.timestampValue}}</span>
                </div>


                <input class="form-control" type="text" ng-model="attribute.timeValue"
                       ng-if="attribute.attributeDef.dataType == 'TIME'" time-picker>

                <div ng-if="attribute.attributeDef.dataType == 'IMAGE'">
                    <input class="form-control" name="file" accept="image/*"
                           type="file" ng-file-model="attribute.imageValue"/>
                </div>
                <div ng-if="attribute.attributeDef.dataType == 'ATTACHMENT'">
                    <input class="form-control" name="file" multiple="true"
                           type="file" ng-file-model="attribute.attachmentValues"/>

                    <p ng-if="attribute.attachmentValues.length > 1" ng-repeat="file in attribute.attachmentValues">
                        {{file.name}}
                    </p>
                </div>
                <div>
                    <div class="col-sm-5" ng-if="attribute.attributeDef.dataType == 'CURRENCY'"
                         style="margin-left: -10px;padding-right: 0px;">
                        <select class="form-control" ng-model="attribute.currencyType"
                                ng-init="attribute.currencyType = currencies[0].id"
                                ng-options="currency.id as currency.name for currency in currencies"
                                style="height: 40px;">
                        </select>
                    </div>
                    <div class="col-sm-offset-5" ng-if="attribute.attributeDef.dataType == 'CURRENCY'">
                        <input class="form-control" name="currencyValue" type="number"
                               placeholder="Enter currency value"
                               ng-model="attribute.currencyValue"/>
                    </div>
                </div>
                <div ng-if="attribute.attributeDef.dataType == 'OBJECT'">
                    <a ng-if="attribute.refValue == null"
                       style="width: 85px;margin-top: 10px"
                       ng-click="showSelectionDialog(attribute.attributeDef.refType, attribute)">Select
                    </a>
            <span style="margin-top:10px" class="input-group"
                  ng-if="attribute.refValue != null">
                <a href="" ng-click="showSelectionDialog(attribute.attributeDef.refType, attribute)">
                    {{attribute.refValueString}}
                </a></span>
                </div>
                <a class="icon fa fa-info-circle"
                   ng-if="attribute.attributeDef.validations != null"
                   style="position: relative;margin-top: -35px;margin-right: -25px;float: right;"
                   title="Attribute validations"
                   uib-popover-template="attributeValidationsPopover.templateUrl"
                   popover-append-to-body="true"
                   popover-popup-delay="50"
                   popover-placement="left"
                   popover-title="{{attribute.attributeDef.name}} validations"
                   popover-trigger="'outsideClick'">
                </a>
            </div>
            <div class="col-sm-4">
                <label class="radio-inline"
                       ng-if="attribute.attributeDef.dataType == 'BOOLEAN'">
                    <input type="checkbox" name="change_{{attribute.attributeDef.id}}"
                           ng-model="attribute.booleanValue">
                </label>
            </div>
        </div>
    </div>
</div>