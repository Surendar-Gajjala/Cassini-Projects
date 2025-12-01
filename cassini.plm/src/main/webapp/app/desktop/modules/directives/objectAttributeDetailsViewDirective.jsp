<style>
    .label-name {
        font-weight: normal;
        font-size: 15px;
        color: #8e8181;
    }

    .datetimepicker .table-condensed {
        width: 300px !important;
        font-size: 14px;
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

<div class="row row-eq-height">
    <div class="col-sm-12" style="padding: 0">
        <form>
            <div class="item-details">
                <div class="row" ng-repeat="attribute in objectProperties track by $index">
                    <div class="label col-xs-4 col-sm-3 text-right" style="white-space: normal">
                        <span ng-bind-html="attribute.attributeDef.name "
                              title="{{attribute.attributeDef.name}}"></span>
                        <span ng-if="attribute.attributeDef.required" class="asterisk">*</span> :
                    </div>
                    <div class="value col-xs-8 col-sm-9">

                        <%----------------  Text Value  -------------------%>

                        <div ng-if="attribute.attributeDef.dataType == 'TEXT'">
                        <span ng-if="!attribute.editMode" ng-bind-html="attribute.value.stringValue"
                              title="{{attribute.value.stringValue}}"></span>
                            <a class="fa fa-pencil row-edit-btn" ng-click="changeAttribute(attribute)"
                               title="{{'CLICK_TO_UPDATE_VALUE' | translate}}" style="cursor:pointer;"
                               ng-if="(hasPermission || (attribute.attributeDef.allowEditAfterRelease && hasReleased)) && !attribute.editMode"></a>
                        </div>

                        <%----------------  Formula Value  -------------------%>

                        <div ng-if="attribute.attributeDef.dataType == 'FORMULA'">
                        <span ng-if="!attribute.editMode"
                              title="{{attribute.value.formulaValue}}">{{attribute.value.formulaValue}}</span>
                        </div>

                        <%----------------  longText Value  -------------------%>

                        <div ng-if="attribute.attributeDef.dataType == 'LONGTEXT'">
                        <span ng-if="!attribute.editMode" ng-bind-html="attribute.value.longTextValue "
                              title="{{attribute.value.longTextValue}}"></span>
                            <a class="fa fa-pencil row-edit-btn"
                               ng-if="!attribute.editMode && (hasPermission || (attribute.attributeDef.allowEditAfterRelease && hasReleased))"
                               ng-click="changeAttribute(attribute)"
                               title="{{changeValue}}"
                               style="padding-left: 7px"></a>
                        </div>

                        <%--------------- RichTextValue --------------%>

                        <div ng-if="attribute.attributeDef.dataType == 'RICHTEXT'">
                        <span ng-if="attribute.editMode == false"
                              ng-bind-html="attribute.value.encodedRichTextValue">
                        </span>
                            <a class="fa fa-pencil row-edit-btn" style="padding-left: 7px;cursor: pointer;"
                               ng-if="!attribute.editMode && (hasPermission || (attribute.attributeDef.allowEditAfterRelease && hasReleased))"
                               ng-click="editRichText(attribute)" title="{{'CLICK_TO_EDIT_VALUE' | translate}}">
                            </a>

                            <div ng-show="attribute.editMode == true">
                                <summernote ng-model="attribute.value.richTextValue"></summernote>
                            </div>

                            <button ng-show="attribute.editMode == true" title="{{saveAttributeTitle}}"
                                    ng-click="saveRichText(attribute)"><i class="fa fa-check"></i>
                            </button>

                            <button ng-show="attribute.editMode == true" title="{{cancelChangesTitle}}"
                                    ng-click="cancelRichText(attribute)"><i class="fa fa-times"></i>
                            </button>
                        </div>

                        <%----------------  Integer Value  -------------------%>

                        <div id="intClick" ng-if="attribute.attributeDef.dataType == 'INTEGER'">
                            <span ng-if="!attribute.editMode">{{attribute.value.integerValue}}</span>
                            <a class="fa fa-pencil row-edit-btn" style="padding-left: 7px;"
                               title="{{'CLICK_TO_UPDATE_VALUE' | translate}}"
                               ng-if="!attribute.editMode && (hasPermission || (attribute.attributeDef.allowEditAfterRelease && hasReleased)) && checkToHideEditButton(attribute)"
                               ng-click="changeAttribute(attribute)"></a>
                        </div>

                        <%------------ HyperLink Attribute ----%>

                        <div id="intClick" ng-if="attribute.attributeDef.dataType == 'HYPERLINK'">
                            <a href="" ng-click="showHyperLink(attribute.value.hyperLinkValue)"
                               ng-if="attribute.editMode == false">{{attribute.value.hyperLinkValue}}</a>
                            <a class="fa fa-pencil row-edit-btn"
                               ng-if="attribute.editMode == false && (hasPermission || (attribute.attributeDef.allowEditAfterRelease && hasReleased))"
                               href="" ng-click="changeAttribute(attribute)" style="cursor: pointer;"
                               title="{{'CLICK_TO_UPDATE_VALUE' | translate}}"></a>
                        </div>

                        <%----------------  DOUBLE Value  -------------------%>

                        <div ng-if="attribute.attributeDef.dataType == 'DOUBLE'">
                        <span ng-if="attribute.editMode == false ">
                             {{attribute.value.doubleValue | number:2}}
                            <span ng-if="attribute.value.measurementUnit != null"> {{attribute.value.measurementUnit.symbol}}</span>
                        </span>
                            <a class="fa fa-pencil row-edit-btn"
                               ng-if="attribute.editMode == false && (hasPermission || (attribute.attributeDef.allowEditAfterRelease && hasReleased))"
                               href="" ng-click="changeAttribute(attribute)" title="{{changeValue}}"></a>
                        </div>

                        <%----------------  DATE Value  -------------------%>

                        <div ng-if="attribute.attributeDef.dataType == 'DATE'">
                        <span ng-if="attribute.editMode == false">
                            {{ (attribute.value.dateValue | date:"dd/MM/yyyy")}}
                        </span>
                            <a class="fa fa-pencil row-edit-btn" style="padding-left: 7px;"
                               ng-if="attribute.editMode == false && (hasPermission || (attribute.attributeDef.allowEditAfterRelease && hasReleased))"
                               ng-click="changeAttribute(attribute)"
                               title="{{'CLICK_TO_SET_DATE' | translate}}"></a>
                        </div>

                        <%----------------  BOOLEAN Value  -------------------%>

                        <div ng-if="attribute.attributeDef.dataType == 'BOOLEAN'">
                            <span>{{attribute.value.booleanValue}}</span>
                            <a style="padding-left: 7px" ng-click="changeAttribute(attribute)"
                               ng-if="attribute.editMode == false && (hasPermission || (attribute.attributeDef.allowEditAfterRelease && hasReleased))"
                               title="{{'CLICK_TO_EDIT_VALUE' | translate}}"
                               class="fa fa-pencil row-edit-btn"></a>
                        </div>

                        <div ng-if="attribute.editMode == true &&
            (attribute.attributeDef.dataType == 'TEXT' || attribute.attributeDef.dataType == 'INTEGER' || attribute.attributeDef.dataType == 'LONGTEXT' ||
             (attribute.attributeDef.dataType == 'DOUBLE' && attribute.attributeDef.measurement == null) || attribute.attributeDef.dataType == 'BOOLEAN' || attribute.attributeDef.dataType == 'HYPERLINK' ||
             attribute.attributeDef.dataType == 'DATE' || attribute.attributeDef.dataType == 'CURRENCY')">

                            <input type="text" class="form-control" name="title"
                                   ng-if="attribute.attributeDef.dataType == 'TEXT'"
                                   placeholder="{{'ENTER_VALUE' | translate}}" ng-change="checkValidations(attribute)"
                                   ng-model="attribute.value.stringValue" style="width:200px;">
                <textarea type="text" class="form-control" name="title"
                          ng-if="attribute.attributeDef.dataType == 'LONGTEXT'"
                          placeholder="{{'ENTER_VALUE' | translate}}"
                          ng-change="checkValidations(attribute)" rows="5"
                          ng-model="attribute.value.longTextValue" style="width:250px;resize: none"></textarea>
                            <input type="text" class="form-control" name="title"
                                   ng-if="attribute.attributeDef.dataType == 'INTEGER'"
                                   placeholder="{{'ENTER_NUMBER' | translate}}"
                                   ng-model="attribute.value.integerValue" style="width:200px;" numbers-only>
                            <input type="text" class="form-control" name="title"
                                   ng-if="attribute.attributeDef.dataType == 'DOUBLE'"
                                   placeholder="{{'ENTER_NUMBER' | translate}}" valid-number
                                   ng-model="attribute.value.doubleValue" style="width:200px;">
                            <input type="text" class="form-control" name="title"
                                   ng-if="attribute.attributeDef.dataType == 'HYPERLINK'"
                                   placeholder="{{'ENTER_VALUE' | translate}}"
                                   ng-model="attribute.value.hyperLinkValue" style="width:250px;">
                            <label class="radio-inline"
                                   ng-if="attribute.attributeDef.dataType == 'BOOLEAN'">
                                <input type="checkbox" name="change_{{attribute.attributeDef.id}}"
                                       ng-click="saveObjectProperties(attribute)"
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
                <span ng-if="attribute.editMode == true && attribute.attributeDef.dataType == 'CURRENCY'"
                      ng-bind-html="attribute.value.encodedCurrencyType">
                </span>
                            <input type="number" class="form-control" name="title"
                                   ng-if="attribute.attributeDef.dataType == 'CURRENCY'"
                                   title="{{'ENTER_VALUE' | translate}}"
                                   ng-model="attribute.value.currencyValue"
                                   style="width:185px;margin-top:-25px;margin-left:15px;">
                            <button class="btn btn-sm btn-primary" type="button" title="{{saveAttributeTitle}}"
                                    ng-if="attribute.attributeDef.dataType != 'LONGTEXT' && attribute.attributeDef.dataType != 'BOOLEAN' && attribute.attributeDef.dataType != 'HYPERLINK'"
                                    style="margin-top:-65px;margin-left:205px;padding: 8px;width:33px;"
                                    ng-click="saveObjectProperties(attribute)"><i class="fa fa-check"></i>
                            </button>

                            <button class="btn btn-sm btn-default"
                                    ng-if="attribute.attributeDef.dataType != 'LONGTEXT' && attribute.attributeDef.dataType != 'BOOLEAN' && attribute.attributeDef.dataType != 'HYPERLINK'"
                                    type="button" title="{{cancelChangesTitle}}"
                                    style="margin-top:-65px;padding: 8px;width:33px;"
                                    ng-click="cancelChanges(attribute)"><i class="fa fa-times"></i>
                            </button>
                            <i class="fa fa-times-circle" ng-if="attribute.attributeDef.dataType == 'BOOLEAN'"
                               ng-click="cancelChanges(attribute)" title="{{cancelChangesTitle}}"
                               style="padding: 8px;width:33px;cursor:pointer;"></i>

                            <button ng-if="attribute.attributeDef.dataType == 'LONGTEXT'" class="btn btn-sm btn-primary"
                                    type="button" title="{{saveAttributeTitle}}"
                                    style="margin-top:-65px;margin-left:255px;padding: 8px;width:33px;"
                                    ng-click="saveObjectProperties(attribute)"><i class="fa fa-check"></i>
                            </button>
                            <button ng-if="attribute.attributeDef.dataType == 'LONGTEXT'" class="btn btn-sm btn-default"
                                    type="button" title="{{cancelChangesTitle}}"
                                    style="margin-top:-65px;padding: 8px;width:33px;"
                                    ng-click="cancelChanges(attribute)"><i class="fa fa-times"></i>
                            </button>

                            <button ng-if="attribute.attributeDef.dataType == 'HYPERLINK'"
                                    class="btn btn-sm btn-primary"
                                    type="button" title="{{saveAttributeTitle}}"
                                    style="margin-top:-65px;margin-left:253px;padding: 8px;width:33px;"
                                    ng-click="saveObjectProperties(attribute)"><i class="fa fa-check"></i>
                            </button>
                            <button ng-if="attribute.attributeDef.dataType == 'HYPERLINK'"
                                    class="btn btn-sm btn-default"
                                    type="button" title="{{cancelChangesTitle}}"
                                    style="margin-top:-65px;padding: 8px;width:33px;"
                                    ng-click="cancelChanges(attribute)"><i class="fa fa-times"></i>
                            </button>
                            <a class="icon fa fa-info-circle"
                               ng-if="attribute.attributeDef.validations != null"
                               style="padding:0 10px;position: absolute;margin-top: -25px;"
                               title="Attribute validations"
                               uib-popover-template="attributeValidationsPopover.templateUrl"
                               popover-append-to-body="true"
                               popover-popup-delay="50"
                               popover-placement="{{validationPopover}}"
                               popover-title="{{attribute.attributeDef.name}} validations"
                               popover-trigger="'outsideClick'">
                            </a>
                        </div>

                        <div ng-if="attribute.editMode == true && attribute.attributeDef.dataType == 'DOUBLE' && attribute.attributeDef.measurement != null"
                             style="display: flex;">
                            <div style="width:150px;padding-right: 5px;">
                                <select class="form-control" ng-model="attribute.value.measurementUnit"
                                        style="padding: 8px;"
                                        ng-options="measurementUnit as measurementUnit.name for measurementUnit in attribute.attributeDef.measurement.measurementUnits track by measurementUnit.name">
                                </select>
                            </div>
                            <div style="width: 150px;padding-right: 5px;">
                                <input class="form-control" type="text" valid-number
                                       placeholder="{{ 'ENTER_VALUE' | translate }}"
                                       ng-model="attribute.value.doubleValue" style="padding: 7px 10px;"
                                       ng-disabled="attribute.value.measurementUnit == null"/>
                            </div>
                            <button class="btn btn-sm btn-primary"
                                    type="button" title="{{'SAVE' | translate}}"
                                    style="width:33px;"
                                    ng-click="saveObjectProperties(attribute)"><i class="fa fa-check"></i>
                            </button>

                            <button class="btn btn-sm btn-default"
                                    type="button" title="{{'CANCEL' | translate}}"
                                    style="width:33px;"
                                    ng-click="cancelChanges(attribute)"><i class="fa fa-times"></i>
                            </button>
                        </div>

                        <%----------------  LIST Value  -------------------%>

                        <div ng-if="attribute.attributeDef.dataType == 'LIST' && attribute.attributeDef.listMultiple == false && !attribute.attributeDef.configurable">
                            <div style="display: flex">
                                <ui-select ng-show="attribute.editMode == true" ng-model="attribute.value.listValue"
                                           theme="bootstrap" style="width:300px;">
                                    <ui-select-match placeholder="{{ 'SELECT' | translate }}">{{$select.selected}}
                                    </ui-select-match>
                                    <ui-select-choices
                                            repeat="value in attribute.attributeDef.lov.values">
                                        <div ng-bind="value"></div>
                                    </ui-select-choices>
                                </ui-select>

                            <span ng-show="attribute.editMode == false && attribute.value.listValue != null">
                                        {{attribute.value.listValue}}
                            </span>

                                <div ng-show="attribute.editMode == false && (hasPermission || (attribute.attributeDef.allowEditAfterRelease && hasReleased))">
                                    <a style="padding: 6px;color: #337ab7;cursor: pointer;  "
                                       class="fa fa-pencil row-edit-btn"
                                       ng-click="changeAttribute(attribute)"
                                       title="{{'CLICK_TO_UPDATE_VALUE' | translate}}"></a>

                                    <i ng-if="attribute.value.listValue != null && attribute.value.deleteFlag != true"
                                       ng-hide="attribute.editMode == true"
                                       title="{{'REMOVE_ATTRIBUTE_VALUE' | translate}}"
                                       ng-click="deleteAttribute(attribute)">
                                        <i class="fa fa-times-circle"
                                           style="margin-left: 25px;font-size: 14px;cursor:pointer;"></i></i>
                                </div>
                                <div ng-show="attribute.editMode == true">
                                    <i class="fa fa-check-circle" style="padding: 4px;cursor: pointer"
                                       title="{{saveAttributeTitle}}"
                                       ng-click="saveObjectProperties(attribute)">
                                    </i>
                                    <i class="fa fa-times-circle" style="cursor: pointer"
                                       title="{{cancelChangesTitle}}"
                                       ng-click="cancelListValue(attribute)">
                                    </i>
                                </div>
                            </div>
                        </div>

                        <div ng-if="attribute.attributeDef.dataType == 'LIST' && attribute.attributeDef.listMultiple == false && attribute.attributeDef.configurable">
                            <a class="icon fa fa-paperclip" style="padding-right: 4px; overflow-y: hidden !important;"
                               title="{{possibleTitle}}"
                               ng-if="attribute.attributeDef.configurableAttr.length > 0"
                               uib-popover-template="itemAttributePopover.templateUrl"
                               popover-append-to-body="true"
                               popover-popup-delay="50"
                               popover-placement="right"
                               popover-trigger="'outsideClick'">
                            </a>
                        </div>

                        <div ng-if="attribute.attributeDef.dataType == 'LIST' && attribute.attributeDef.listMultiple == true">
                            <div ng-if="attribute.value.mlistValue.length > 0" ng-hide="attribute.editMode == true"
                                 ng-repeat="listValue in attribute.value.mlistValue">
                                <ul>
                                    <li>{{listValue}}</li>
                                </ul>
                            </div>
                            <a ng-if="attribute.editMode == false && (hasPermission || (attribute.attributeDef.allowEditAfterRelease && hasReleased))"
                               href="" ng-click="changeAttribute(attribute)"
                               title="{{'CLICK_TO_UPDATE_VALUE' | translate}}"><i
                                    class="fa fa-pencil row-edit-btn"></i>
                            </a>

                            <div ng-if="attribute.editMode" style="width: 400px; display: inline-flex">
                                <div style="width: 350px">
                                    <ui-select multiple ng-model="attribute.value.mlistValue" theme="bootstrap"
                                               close-on-select="false" title="Choose a List" remove-selected="true">
                                        <ui-select-match placeholder="{{ 'SELECT_LIST' | translate }}">{{$item}}
                                        </ui-select-match>
                                        <ui-select-choices
                                                repeat="value in attribute.attributeDef.lov.values track by value">
                                            <div ng-bind="value"></div>
                                        </ui-select-choices>
                                    </ui-select>
                                </div>
                                <div style="width: 25px">
                                    <a ng-if="attribute.attributeDef.dataType == 'LIST'"
                                       ng-click="saveObjectProperties(attribute)"
                                       title="{{saveAttributeTitle}}">
                                        <i class="fa fa-check-circle" style="padding: 8px;cursor:pointer;"></i>
                                    </a>
                                </div>
                                <div style="width: 25px;">
                                    <a ng-if="attribute.attributeDef.dataType == 'LIST'"
                                       ng-click="cancelChanges(attribute)"
                                       title="{{cancelChangesTitle}}">
                                        <i class="fa fa-times-circle" style="padding: 8px;cursor:pointer;"></i></a>
                                </div>
                            </div>

                        </div>

                        <div ng-if="attribute.attributeDef.dataType == 'MULTILISTCHECKBOX'">
                            <div ng-repeat="value in attribute.attributeDef.lov.values">
                                <input type="checkbox" ng-checked="checkValue(attribute,value)"
                                       ng-disabled="!attribute.editMode"
                                       ng-click="selectMultiListCheckBox(attribute,value)"
                                       style="margin-right: 5px;">{{value}}<br>
                            </div>
                            <a ng-if="attribute.editMode == false && (hasPermission || (attribute.attributeDef.allowEditAfterRelease && hasReleased))"
                               href="" ng-click="editListValue(attribute)"
                               title="{{'CLICK_TO_UPDATE_VALUE' | translate}}"><i
                                    class="fa fa-pencil row-edit-btn"></i>
                            </a>

                            <div ng-if="attribute.editMode" style="width: 400px; display: inline-flex">
                                <div style="width: 25px">
                                    <a ng-click="saveObjectProperties(attribute)"
                                       title="{{saveAttributeTitle}}">
                                        <i class="fa fa-check-circle" style="padding: 8px;cursor:pointer;"></i>
                                    </a>
                                </div>
                                <div style="width: 25px;">
                                    <a ng-click="cancelListValue(attribute)"
                                       title="{{cancelChangesTitle}}">
                                        <i class="fa fa-times-circle" style="padding: 8px;cursor:pointer;"></i></a>
                                </div>
                            </div>
                        </div>

                        <%----------------  TIMESTAMP Property  -------------------%>

                        <div ng-if="attribute.attributeDef.dataType == 'TIMESTAMP'">
                        <span ng-if="attribute.showTimestamp == false" ng-click="changeTimestamp(attribute)">
                            {{attribute.value.timestampValue}}
                        </span>
                            <a class="fa fa-pencil row-edit-btn" ng-click="changeTimestamp(attribute)"
                               ng-if="attribute.showTimestamp == false && (hasPermission || (attribute.attributeDef.allowEditAfterRelease && hasReleased))"
                               title="{{'CLICK_TO_SET_TIME' | translate}}"
                               style="padding-left: 7px;"></a>

                            <div ng-if="attribute.showTimestamp == true">
                                <div>
                                    {{attribute.value.timestampValue}}
                                </div>
                                <div uib-timepicker
                                     ng-model="attribute.timestampValue">
                                </div>
                                <button class="btn btn-sm btn-primary" type="button" title="{{'SAVE' | translate}}"
                                        style="margin-top:-195px;margin-left:165px;"
                                        ng-click="saveTimeProperty(attribute)"><i
                                        class="fa fa-check"></i>
                                </button>

                                <button class="btn btn-sm btn-default" type="button" style="margin-top:-195px;"
                                        title="{{'CANCEL' | translate}}"
                                        ng-click="cancelTime(attribute)"><i class="fa fa-times"></i>
                                </button>
                            </div>
                        </div>

                        <%----------------  TIME Property  -------------------%>

                        <div ng-if="attribute.attributeDef.dataType == 'TIME'">
                            <span ng-if="attribute.showTimeAttribute == false">{{attribute.value.timeValue}}</span>
                            <a class="fa fa-pencil row-edit-btn"
                               ng-if="attribute.showTimeAttribute == false && (hasPermission || (attribute.attributeDef.allowEditAfterRelease && hasReleased))"
                               ng-click="changeTime(attribute)" title="{{'CLICK_TO_SET_TIME' | translate}}"
                               style="padding-left: 7px;"></a>

                            <div ng-if="attribute.showTimeAttribute == true" style="display: flex;">

                                <input class="form-control" type="text" ng-model="attribute.timeValue" time-picker
                                       style="width: 240px;">

                                <button class="btn btn-sm btn-primary" type="button"
                                        title="{{'SAVE' | translate}}"
                                        ng-click="saveTimeProperty(attribute)"><i class="fa fa-check"></i>
                                </button>

                                <button class="btn btn-sm btn-default" type="button"
                                        title="{{'CANCEL' | translate}}"
                                        ng-click="cancelTime(attribute)"><i class="fa fa-times"></i>
                                </button>
                                <a class="icon fa fa-info-circle"
                                   ng-if="attribute.attributeDef.validations != null"
                                   style="padding:0 10px;margin-top: 10px;"
                                   title="Attribute validations"
                                   uib-popover-template="attributeValidationsPopover.templateUrl"
                                   popover-append-to-body="true"
                                   popover-popup-delay="50"
                                   popover-placement="{{validationPopover}}"
                                   popover-title="{{attribute.attributeDef.name}} validations"
                                   popover-trigger="'outsideClick'">
                                </a>
                            </div>
                        </div>

                        <%----------------  IMAGE Property  -------------------%>

                        <div ng-if="attribute.attributeDef.dataType == 'IMAGE'">
                            <a href="" title="{{'CLICK_TO_SHOW_LARGE_IMAGE' | translate}}"
                               ng-click="showThumbnailImage(attribute)">
                                <img ng-if="attribute.value.imageValue != null"
                                     ng-src="{{attribute.value.imagePath}}"
                                     style="height: 100px;width: 100px;">
                            </a><br ng-if="attribute.value.imageValue != null">

                            <%---- To show large image  ------%>
                            <%--<div id="myModal" class="img-model modal">
                                <span class="closeimage">&times;</span>
                                <img class="modal-content" id="img01">
                            </div>--%>
                            <div id="item-thumbnail-basic{{attribute.attributeDef.id}}" class="item-thumbnail modal">
                                <div class="item-thumbnail-content">
                                    <div class="thumbnail-content" style="display: flex;width: 100%;">
                                        <div class="thumbnail-view"
                                             id="thumbnail-view-basic{{attribute.attributeDef.id}}">
                                            <div id="thumbnail-image-basic{{attribute.attributeDef.id}}"
                                                 style="display: table-cell;vertical-align: middle;text-align: center;">
                                                <img ng-src="{{attribute.value.imagePath}}"
                                                     style="height: auto;width: auto;max-width: 100%;max-height: 90%"/>
                                            <span class="thumbnail-close"
                                                  id="thumbnail-close-basic{{attribute.attributeDef.id}}"></span>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>

                            <div>
                                <a ng-if="(hasPermission || (attribute.attributeDef.allowEditAfterRelease && hasReleased))"
                                   class="fa fa-pencil row-edit-btn"
                                   ng-click="change(attribute)" style="margin-top: 5px;color:#337ab7"
                                   ng-hide="attribute.changeImage == true"
                                   title="{{changeImageMessage}}">
                                </a>
                                <input ng-show="attribute.changeImage == true" class="browse-control" name="file"
                                       type="file" ng-file-model="attribute.newImageValue" style="width: 250px;"
                                       accept="image/*">
                                <button class="btn btn-sm btn-primary"
                                        ng-click="saveImage(attribute)"
                                        ng-show="attribute.changeImage == true" title="{{'SAVE_IMAGE' | translate}}"
                                        style="margin-top:-62px;margin-left: 255px;padding-top:8px;">
                                    <i class="fa fa-check"></i>
                                </button>
                                <button class="btn btn-sm btn-default"
                                        ng-click="cancel(attribute)"
                                        ng-show="attribute.changeImage == true" title="{{cancelChangesTitle}}"
                                        style="margin-top:-62px;padding-top:8px;">
                                    <i class="fa fa-times"></i>
                                </button>
                            </div>
                        </div>

                        <%----------------  ATTACHMENT Property  -------------------%>

                        <div ng-if="attribute.attributeDef.dataType == 'ATTACHMENT'">
                            <div ng-if="attribute.value.attachmentValues.length > 0"
                                 ng-repeat="attachment in attribute.value.attachmentValues">
                                <ul style="margin-left:-30px;">
                                    <li>
                                        <a href="" ng-click="openPropertyAttachment(attachment)"
                                           title="{{'CLICK_TO_DOWNLOAD_ATTACHMENT' | translate}}"
                                           style="margin-bottom: 5px;width:250px;color: #002451;"
                                           ng-bind-html="attachment.name">
                                        </a>
                                        <a href="" title="{{deleteAttachment}}"
                                           ng-if="(hasPermission || (attribute.attributeDef.allowEditAfterRelease && hasReleased))"
                                           ng-click="deleteAttachments(attribute,attachment)">
                                            <i class="fa fa-times row-edit-btn"
                                               style="margin-left: 5px;font-size: 14px;color: darkred"></i>
                                        </a>
                                    </li>
                                </ul>
                            </div>

                            <div>
                                <a href="">
                                    <div ng-show="attribute.showAttachment == false && (hasPermission || (attribute.attributeDef.allowEditAfterRelease && hasReleased))"
                                         title="{{addAttachments}}"
                                         ng-click="addAttachment(attribute)">
                                        <a class="fa fa-pencil row-edit-btn" title="" style="padding-left: 7px"></a>
                                    </div>
                                </a>

                                <div ng-show="attribute.showAttachment == true">
                                    <input class="browse-control" name="file"
                                           multiple="true"
                                           type="file" ng-file-model="attribute.attachmentValues"
                                           style="width: 250px;"/>
                                    <button class="btn btn-sm btn-primary" title="{{'SAVE' | translate}}"
                                            ng-click="saveAttachments(attribute)"
                                            style="margin-top:-62px;margin-left: 255px;padding-top:8px;">
                                        <i class="fa fa-check"></i>
                                    </button>
                                    <button class="btn btn-sm btn-default" title="{{'CANCEL' | translate}}"
                                            ng-click="cancelAttachment(attribute)"
                                            style="margin-top:-62px;padding-top:8px;">
                                        <i class="fa fa-times"></i>
                                    </button>
                                    <p ng-repeat="file in attribute.attachmentValues">
                                        <span ng-bind-html="file.name"></span>
                                    </p>
                                </div>
                            </div>
                        </div>

                        <%------------------------  OBJECT Property  -------------------------%>

                        <div class="row" ng-if="attribute.attributeDef.dataType == 'OBJECT'">

                            <a ng-if="attribute.value.refValue.objectType == 'ITEM'" href=""
                               ng-click="showRefValueDetails(attribute)"
                               title="{{'CLICK_TO_SHOW_DETAILS' | translate}}">
                                {{attribute.value.refValue.itemNumber+"
                                "+attribute.value.refValue.itemRevision.revision+"
                                "+attribute.value.refValue.itemRevision.lifeCyclePhase.phase}}
                            </a>

                            <a ng-if="attribute.value.refValue.objectType == 'ITEMREVISION'" href=""
                               ng-click="showRefValueDetails(attribute)"
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
                                <span ng-if="attribute.value.refValue.changeType == 'DEVIATION' || attribute.value.refValue.changeType == 'WAIVER'"
                                      ng-click="showRefValueDetails(attribute)"
                                      title="{{'CLICK_TO_SHOW_DETAILS' | translate}}"
                                      ng-bind-html="attribute.value.refValue.varianceNumber">
                                </span>
                            </a>
                            <a ng-if="attribute.value.refValue.objectType == 'OEMPARTMCO'||  attribute.value.refValue.objectType == 'ITEMMCO'"
                               href="">
                                <span ng-click="showRefValueDetails(attribute)"
                                      title="{{'CLICK_TO_SHOW_DETAILS' | translate}}"
                                      ng-bind-html="attribute.value.refValue.mcoNumber">
                                </span>
                            </a>

                            <a ng-if="attribute.value.refValue.objectType == 'PLMWORKFLOWDEFINITION'" href=""
                               ng-click="showRefValueDetails(attribute)"
                               title="{{'CLICK_TO_SHOW_DETAILS' | translate}}"
                               ng-bind-html="attribute.value.refValue.master.number">
                            </a>

                            <a ng-if="attribute.value.refValue.objectType == 'MANUFACTURER'" href=""
                               ng-click="showRefValueDetails(attribute)"
                               title="{{'CLICK_TO_SHOW_DETAILS' | translate}}"
                               ng-bind-html="attribute.value.refValue.name">
                            </a>

                            <a ng-if="attribute.value.refValue.objectType == 'MANUFACTURERPART'" href=""
                               ng-click="showRefValueDetails(attribute)"
                               title="{{'CLICK_TO_SHOW_DETAILS' | translate}}"
                               ng-bind-html="attribute.value.refValue.partName">
                            </a>

                            <a ng-if="attribute.value.refValue.objectType == 'PLANT' || attribute.value.refValue.objectType == 'ASSEMBLYLINE' ||
                                attribute.value.refValue.objectType == 'WORKCENTER' || attribute.value.refValue.objectType == 'MACHINE' ||
                                attribute.value.refValue.objectType == 'JIGFIXTURE' || attribute.value.refValue.objectType == 'MANPOWER' ||
                                attribute.value.refValue.objectType == 'INSTRUMENT' || attribute.value.refValue.objectType == 'EQUIPMENT' ||
                                attribute.value.refValue.objectType == 'PRODUCTIONORDER' || attribute.value.refValue.objectType == 'OPERATION' ||
                                attribute.value.refValue.objectType == 'MATERIAL' || attribute.value.refValue.objectType == 'TOOL'"
                               href=""
                               ng-click="showRefValueDetails(attribute)"
                               title="{{'CLICK_TO_SHOW_DETAILS' | translate}}"
                               ng-bind-html="attribute.value.refValue.number">
                            </a>

                            <a ng-if="attribute.value.refValue.objectType == 'MROASSET' || attribute.value.refValue.objectType == 'MROMETER' ||
                                attribute.value.refValue.objectType == 'MROSPAREPART' || attribute.value.refValue.objectType == 'MROWORKREQUEST' || attribute.value.refValue.objectType == 'MROWORKORDER'"
                               href=""
                               ng-click="showRefValueDetails(attribute)"
                               title="{{'CLICK_TO_SHOW_DETAILS' | translate}}"
                               ng-bind-html="attribute.value.refValue.number">
                            </a>

                            <a ng-if="attribute.value.refValue.objectType == 'REQUIREMENT' || attribute.value.refValue.objectType == 'REQUIREMENTDOCUMENT' || attribute.value.refValue.objectType == 'CUSTOMOBJECT'"
                               href=""
                               ng-click="showRefValueDetails(attribute)"
                               title="{{'CLICK_TO_SHOW_DETAILS' | translate}}"
                               ng-bind-html="attribute.value.refValue.number">
                            </a>

                            <a ng-if="attribute.value.refValue.objectType == 'PERSON'"
                               ng-bind-html="attribute.value.refValue.fullName">
                            </a>
                            <a ng-if="attribute.value.refValue.objectType == 'PROJECT'"
                               ng-bind-html="attribute.value.refValue.name">
                            </a>
                            <a ng-if="attribute.attributeDef.refType == 'QUALITY'"
                               ng-click="showRefValueDetails(attribute)">
                                <span ng-if="attribute.value.refValue.objectType == 'PRODUCTINSPECTIONPLAN' || attribute.value.refValue.objectType == 'MATERIALINSPECTIONPLAN'">
                                    {{attribute.value.refValue.number}}
                                </span>
                                <span ng-if="attribute.value.refValue.objectType == 'PROBLEMREPORT'">{{attribute.value.refValue.prNumber}}</span>
                                <span ng-if="attribute.value.refValue.objectType == 'NCR'">{{attribute.value.refValue.ncrNumber}}</span>
                                <span ng-if="attribute.value.refValue.objectType == 'QCR'">{{attribute.value.refValue.qcrNumber}}</span>
                            </a>
                            <a ng-if="attribute.editMode == false && (hasPermission || (attribute.attributeDef.allowEditAfterRelease && hasReleased))"
                               class="fa fa-pencil row-edit-btn" title="{{changeAttributeTitle}}"
                               style="margin-left:5px;color:#337ab7"
                               ng-click="showSelectionDialog(attribute.attributeDef.refType, attribute)">
                            </a>
                            <button ng-if="attribute.editMode == true" class="btn btn-sm btn-primary"
                                    title="{{'SAVE' | translate}}"
                                    ng-click="saveObject(attribute)">
                                <i class="fa fa-check"></i>
                            </button>

                            <button ng-if="attribute.editMode == true" class="btn btn-sm btn-default"
                                    title="{{'CANCEL' | translate}}"
                                    ng-click="objectCancelChanges(attribute)">
                                <i class="fa fa-times"></i>
                            </button>
                        </div>

                        <%------  Currency Property  ------%>

                        <div ng-if="attribute.attributeDef.dataType == 'CURRENCY'">
                            <div ng-if="attribute.value.encodedCurrencyType != null">
                                <a ng-if="attribute.editMode == false" href=""
                                   ng-bind-html="attribute.value.encodedCurrencyType">
                                </a>
                            <span ng-if="attribute.editMode == false && !attribute.changeCurrency">
                                {{attribute.value.currencyValue}}
                            </span>
                                <a class="fa fa-pencil row-edit-btn"
                                   ng-if="attribute.editMode == false && (hasPermission || (attribute.attributeDef.allowEditAfterRelease && hasReleased))"
                                   title="{{'CLICK_TO_SET_TIME' | translate}}" ng-click="changeAttribute(attribute)"
                                   style="padding-left: 7px;cursor: pointer;"></a>

                            </div>

                            <%----------  For newly added 'CURRENCY' property after ECO Creation ---------------%>

                            <div ng-if="attribute.value.encodedCurrencyType == null || attribute.value.currencyType == null">
                            <span ng-if="attribute.editMode == false && !attribute.changeCurrency">
                                {{attribute.value.currencyValue}}
                            </span>
                                <a class="fa fa-pencil row-edit-btn" title="{{'CLICK_TO_UPDATE_VALUE' | translate}}"
                                   ng-if="attribute.changeCurrency == false && (hasPermission || (attribute.attributeDef.allowEditAfterRelease && hasReleased))"
                                   ng-click="changeCurrencyValue(attribute)" style="padding-left: 7px;cursor: pointer">
                                </a>

                                <div ng-if="attribute.changeCurrency == true" style="display: flex">
                                    <div ng-if="attribute.changeCurrency == true" style="margin-right: 5px;">
                                        <select class="form-control" ng-model="attribute.value.currencyType"
                                                style="height: auto;"
                                                ng-init="attribute.value.currencyType = currencies[0].id"
                                                ng-options="currency.id as currency.name for currency in currencies">
                                        </select>
                                    </div>
                                    <div ng-if="attribute.changeCurrency == true"
                                         style="width: 100px;margin-right: 5px;">
                                        <input class="form-control" name="currencyValue" type="number"
                                               placeholder="{{enterValue}}"
                                               ng-model="attribute.value.currencyValue"/>
                                    </div>
                                    <button ng-if="attribute.changeCurrency == true" class="btn btn-sm btn-primary"
                                            title="{{'SAVE' | translate}}" style="margin-right: 5px;"
                                            ng-click="saveObjectProperties(attribute)">
                                        <i class="fa fa-check"></i>
                                    </button>
                                    <button ng-if="attribute.changeCurrency == true" class="btn btn-sm btn-default"
                                            title="{{'CANCEL' | translate}}"
                                            ng-click="cancelChanges(attribute)">
                                        <i class="fa fa-times"></i>
                                    </button>
                                    <a class="icon fa fa-info-circle"
                                       ng-if="attribute.attributeDef.validations != null && attribute.changeCurrency == true"
                                       style="padding:0 10px;"
                                       title="Attribute validations"
                                       uib-popover-template="attributeValidationsPopover.templateUrl"
                                       popover-append-to-body="true"
                                       popover-popup-delay="50"
                                       popover-placement="{{validationPopover}}"
                                       popover-title="{{attribute.attributeDef.name}} validations"
                                       popover-trigger="'outsideClick'">
                                    </a>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
            <div ng-repeat="groupAttribute in groupAttributes">
                <div>
                    <h4 class="section-title" translate>{{groupAttribute.groupName}}</h4>
                </div>
                <div class="item-details">
                    <div class="row" ng-repeat="attribute in groupAttribute.objectProperties track by $index">
                        <div class="label col-xs-4 col-sm-3 text-right" style="white-space: normal">
                        <span ng-bind-html="attribute.attributeDef.name"
                              title="{{attribute.attributeDef.name}}"></span>
                            <span ng-if="attribute.attributeDef.required" class="asterisk">*</span> :
                        </div>
                        <div class="value col-xs-8 col-sm-9">

                            <%----------------  Text Value  -------------------%>

                            <div ng-if="attribute.attributeDef.dataType == 'TEXT'">
                        <span ng-if="!attribute.editMode" ng-bind-html="attribute.value.stringValue"
                              title="{{attribute.value.stringValue}}"></span>
                                <a class="fa fa-pencil row-edit-btn" ng-click="changeAttribute(attribute)"
                                   title="{{'CLICK_TO_UPDATE_VALUE' | translate}}" style="cursor:pointer;"
                                   ng-if="(hasPermission || (attribute.attributeDef.allowEditAfterRelease && hasReleased)) && !attribute.editMode"></a>
                            </div>

                            <%----------------  Formula Value  -------------------%>

                            <div ng-if="attribute.attributeDef.dataType == 'FORMULA'">
                        <span ng-if="!attribute.editMode"
                              title="{{attribute.value.formulaValue}}">{{attribute.value.formulaValue}}</span>
                            </div>

                            <%----------------  longText Value  -------------------%>

                            <div ng-if="attribute.attributeDef.dataType == 'LONGTEXT'">
                        <span ng-if="!attribute.editMode" ng-bind-html="attribute.value.longTextValue "
                              title="{{attribute.value.longTextValue}}"></span>
                                <a class="fa fa-pencil row-edit-btn"
                                   ng-if="!attribute.editMode && (hasPermission || (attribute.attributeDef.allowEditAfterRelease && hasReleased))"
                                   ng-click="changeAttribute(attribute)"
                                   title="{{changeValue}}"
                                   style="padding-left: 7px"></a>
                            </div>

                            <%--------------- RichTextValue --------------%>

                            <div ng-if="attribute.attributeDef.dataType == 'RICHTEXT'">
                        <span ng-if="attribute.editMode == false"
                              ng-bind-html="attribute.value.encodedRichTextValue">
                        </span>
                                <a class="fa fa-pencil row-edit-btn" style="padding-left: 7px;cursor: pointer;"
                                   ng-if="!attribute.editMode && (hasPermission || (attribute.attributeDef.allowEditAfterRelease && hasReleased))"
                                   ng-click="editRichText(attribute)" title="{{'CLICK_TO_EDIT_VALUE' | translate}}">
                                </a>

                                <div ng-show="attribute.editMode == true">
                                    <summernote ng-model="attribute.value.richTextValue"></summernote>
                                </div>

                                <button ng-show="attribute.editMode == true" title="{{'SAVE' | translate}}"
                                        ng-click="saveRichText(attribute)"><i class="fa fa-check"></i>
                                </button>

                                <button ng-show="attribute.editMode == true" title="{{'CANCEL' | translate}}"
                                        ng-click="cancelRichText(attribute)"><i class="fa fa-times"></i>
                                </button>
                            </div>

                            <%----------------  Integer Value  -------------------%>

                            <div id="intClick" ng-if="attribute.attributeDef.dataType == 'INTEGER'">
                                <span ng-if="!attribute.editMode">{{attribute.value.integerValue}}</span>
                                <a class="fa fa-pencil row-edit-btn" style="padding-left: 7px;"
                                   title="{{'CLICK_TO_UPDATE_VALUE' | translate}}"
                                   ng-if="!attribute.editMode && (hasPermission || (attribute.attributeDef.allowEditAfterRelease && hasReleased)) && checkToHideEditButton(attribute)"
                                   ng-click="changeAttribute(attribute)"></a>
                            </div>

                            <%------------ HyperLink Attribute ----%>

                            <div id="intClick" ng-if="attribute.attributeDef.dataType == 'HYPERLINK'">
                                <a href="" ng-click="showHyperLink(attribute.value.hyperLinkValue)"
                                   ng-if="attribute.editMode == false">{{attribute.value.hyperLinkValue}}</a>
                                <a class="fa fa-pencil row-edit-btn"
                                   ng-if="attribute.editMode == false && (hasPermission || (attribute.attributeDef.allowEditAfterRelease && hasReleased))"
                                   href="" ng-click="changeAttribute(attribute)" style="cursor: pointer;"
                                   title="{{'CLICK_TO_UPDATE_VALUE' | translate}}"></a>
                            </div>

                            <%----------------  DOUBLE Value  -------------------%>

                            <div ng-if="attribute.attributeDef.dataType == 'DOUBLE'">
                        <span ng-if="attribute.editMode == false ">
                           {{attribute.value.doubleValue | number:2}}
                            <span ng-if="attribute.value.measurementUnit != null"> {{attribute.value.measurementUnit.symbol}}</span>
                        </span>
                                <a class="fa fa-pencil row-edit-btn"
                                   ng-if="attribute.editMode == false && (hasPermission || (attribute.attributeDef.allowEditAfterRelease && hasReleased))"
                                   href="" ng-click="changeAttribute(attribute)" title="{{changeValue}}"></a>
                            </div>

                            <%----------------  DATE Value  -------------------%>

                            <div ng-if="attribute.attributeDef.dataType == 'DATE'">
                        <span ng-if="attribute.editMode == false">
                            {{ (attribute.value.dateValue | date:"dd/MM/yyyy")}}
                        </span>
                                <a class="fa fa-pencil row-edit-btn" style="padding-left: 7px;"
                                   ng-if="attribute.editMode == false && (hasPermission || (attribute.attributeDef.allowEditAfterRelease && hasReleased))"
                                   ng-click="changeAttribute(attribute)"
                                   title="{{'CLICK_TO_SET_DATE' | translate}}"></a>
                            </div>

                            <%----------------  BOOLEAN Value  -------------------%>

                            <div ng-if="attribute.attributeDef.dataType == 'BOOLEAN'">
                                <span>{{attribute.value.booleanValue}}</span>
                                <a style="padding-left: 7px" ng-click="changeAttribute(attribute)"
                                   ng-if="attribute.editMode == false && (hasPermission || (attribute.attributeDef.allowEditAfterRelease && hasReleased))"
                                   title="{{'CLICK_TO_EDIT_VALUE' | translate}}"
                                   class="fa fa-pencil row-edit-btn"></a>
                            </div>

                            <div ng-if="attribute.editMode == true &&
            (attribute.attributeDef.dataType == 'TEXT' || attribute.attributeDef.dataType == 'INTEGER' || attribute.attributeDef.dataType == 'LONGTEXT' ||
             (attribute.attributeDef.dataType == 'DOUBLE' && attribute.attributeDef.measurement == null) || attribute.attributeDef.dataType == 'BOOLEAN' || attribute.attributeDef.dataType == 'HYPERLINK' ||
             attribute.attributeDef.dataType == 'DATE' || attribute.attributeDef.dataType == 'CURRENCY')">

                                <input type="text" class="form-control" name="title"
                                       ng-if="attribute.attributeDef.dataType == 'TEXT'"
                                       placeholder="{{'ENTER_VALUE' | translate}}"
                                       ng-change="checkValidations(attribute)"
                                       ng-model="attribute.value.stringValue" style="width:200px;">
                <textarea type="text" class="form-control" name="title"
                          ng-if="attribute.attributeDef.dataType == 'LONGTEXT'"
                          placeholder="{{'ENTER_VALUE' | translate}}"
                          ng-change="checkValidations(attribute)" rows="5"
                          ng-model="attribute.value.longTextValue" style="width:250px;resize: none"></textarea>
                                <input type="text" class="form-control" name="title"
                                       ng-if="attribute.attributeDef.dataType == 'INTEGER'"
                                       placeholder="{{'ENTER_NUMBER' | translate}}"
                                       ng-model="attribute.value.integerValue" style="width:200px;" numbers-only>
                                <input type="text" class="form-control" name="title"
                                       ng-if="attribute.attributeDef.dataType == 'DOUBLE'"
                                       placeholder="{{'ENTER_NUMBER' | translate}}" valid-number
                                       ng-model="attribute.value.doubleValue" style="width:200px;">
                                <input type="text" class="form-control" name="title"
                                       ng-if="attribute.attributeDef.dataType == 'HYPERLINK'"
                                       placeholder="{{'ENTER_HYPERLINK' | translate}}"
                                       ng-model="attribute.value.hyperLinkValue" style="width:250px;">
                                <label class="radio-inline"
                                       ng-if="attribute.attributeDef.dataType == 'BOOLEAN'">
                                    <input type="checkbox" name="change_{{attribute.attributeDef.id}}"
                                           ng-click="saveObjectProperties(attribute)"
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
                <span ng-if="attribute.editMode == true && attribute.attributeDef.dataType == 'CURRENCY'"
                      ng-bind-html="attribute.value.encodedCurrencyType">
                </span>
                                <input type="number" class="form-control" name="title"
                                       ng-if="attribute.attributeDef.dataType == 'CURRENCY'"
                                       title="{{'ENTER_VALUE' | translate}}"
                                       ng-model="attribute.value.currencyValue"
                                       style="width:185px;margin-top:-25px;margin-left:15px;">
                                <button class="btn btn-sm btn-primary" type="button" title="{{saveAttributeTitle}}"
                                        ng-if="attribute.attributeDef.dataType != 'LONGTEXT' && attribute.attributeDef.dataType != 'BOOLEAN' && attribute.attributeDef.dataType != 'HYPERLINK'"
                                        style="margin-top:-65px;margin-left:205px;padding: 8px;width:33px;"
                                        ng-click="saveObjectProperties(attribute)"><i class="fa fa-check"></i>
                                </button>

                                <button class="btn btn-sm btn-default"
                                        ng-if="attribute.attributeDef.dataType != 'LONGTEXT' && attribute.attributeDef.dataType != 'BOOLEAN' && attribute.attributeDef.dataType != 'HYPERLINK'"
                                        type="button" title="{{cancelChangesTitle}}"
                                        style="margin-top:-65px;padding: 8px;width:33px;"
                                        ng-click="cancelChanges(attribute)"><i class="fa fa-times"></i>
                                </button>
                                <i class="fa fa-times-circle" ng-if="attribute.attributeDef.dataType == 'BOOLEAN'"
                                   ng-click="cancelChanges(attribute)" title="{{cancelChangesTitle}}"
                                   style="padding: 8px;width:33px;cursor:pointer;"></i>

                                <button ng-if="attribute.attributeDef.dataType == 'LONGTEXT'"
                                        class="btn btn-sm btn-primary"
                                        type="button" title="{{saveAttributeTitle}}"
                                        style="margin-top:-65px;margin-left:255px;padding: 8px;width:33px;"
                                        ng-click="saveObjectProperties(attribute)"><i class="fa fa-check"></i>
                                </button>
                                <button ng-if="attribute.attributeDef.dataType == 'LONGTEXT'"
                                        class="btn btn-sm btn-default"
                                        type="button" title="{{cancelChangesTitle}}"
                                        style="margin-top:-65px;padding: 8px;width:33px;"
                                        ng-click="cancelChanges(attribute)"><i class="fa fa-times"></i>
                                </button>

                                <button ng-if="attribute.attributeDef.dataType == 'HYPERLINK'"
                                        class="btn btn-sm btn-primary"
                                        type="button" title="{{saveAttributeTitle}}"
                                        style="margin-top:-65px;margin-left:253px;padding: 8px;width:33px;"
                                        ng-click="saveObjectProperties(attribute)"><i class="fa fa-check"></i>
                                </button>
                                <button ng-if="attribute.attributeDef.dataType == 'HYPERLINK'"
                                        class="btn btn-sm btn-default"
                                        type="button" title="{{cancelChangesTitle}}"
                                        style="margin-top:-65px;padding: 8px;width:33px;"
                                        ng-click="cancelChanges(attribute)"><i class="fa fa-times"></i>
                                </button>
                                <a class="icon fa fa-info-circle"
                                   ng-if="attribute.attributeDef.validations != null"
                                   style="padding:0 10px;position: absolute;margin-top: -25px;"
                                   title="Attribute validations"
                                   uib-popover-template="attributeValidationsPopover.templateUrl"
                                   popover-append-to-body="true"
                                   popover-popup-delay="50"
                                   popover-placement="{{validationPopover}}"
                                   popover-title="{{attribute.attributeDef.name}} validations"
                                   popover-trigger="'outsideClick'">
                                </a>
                            </div>

                            <div ng-if="attribute.editMode == true && attribute.attributeDef.dataType == 'DOUBLE' && attribute.attributeDef.measurement != null"
                                 style="display: flex;">
                                <div style="width:150px;padding-right: 5px;">
                                    <select class="form-control" ng-model="attribute.value.measurementUnit"
                                            style="padding: 8px;"
                                            ng-options="measurementUnit as measurementUnit.name for measurementUnit in attribute.attributeDef.measurement.measurementUnits track by measurementUnit.name">
                                    </select>
                                </div>
                                <div style="width: 150px;padding-right: 5px;">
                                    <input class="form-control" type="text" valid-number
                                           placeholder="{{'ENTER_VALUE' | translate}}"
                                           ng-model="attribute.value.doubleValue" style="padding: 7px 10px;"
                                           ng-disabled="attribute.value.measurementUnit == null"/>
                                </div>
                                <button class="btn btn-sm btn-primary"
                                        type="button" title="{{saveAttributeTitle}}"
                                        style="width:33px;"
                                        ng-click="saveObjectProperties(attribute)"><i class="fa fa-check"></i>
                                </button>

                                <button class="btn btn-sm btn-default"
                                        type="button" title="{{cancelChangesTitle}}"
                                        style="width:33px;"
                                        ng-click="cancelChanges(attribute)"><i class="fa fa-times"></i>
                                </button>
                            </div>

                            <%----------------  LIST Value  -------------------%>

                            <div ng-if="attribute.attributeDef.dataType == 'LIST' && attribute.attributeDef.listMultiple == false && !attribute.attributeDef.configurable">
                                <div style="display: flex">
                                    <ui-select ng-show="attribute.editMode == true" ng-model="attribute.value.listValue"
                                               theme="bootstrap" style="width:300px;">
                                        <ui-select-match placeholder="{{'SELECT' | translate}}">{{$select.selected}}
                                        </ui-select-match>
                                        <ui-select-choices
                                                repeat="value in attribute.attributeDef.lov.values">
                                            <div ng-bind="value"></div>
                                        </ui-select-choices>
                                    </ui-select>

                            <span ng-show="attribute.editMode == false && attribute.value.listValue != null">
                                        {{attribute.value.listValue}}
                            </span>

                                    <div ng-show="attribute.editMode == false && (hasPermission || (attribute.attributeDef.allowEditAfterRelease && hasReleased))">
                                        <a style="padding: 6px;color: #337ab7;cursor: pointer;  "
                                           class="fa fa-pencil row-edit-btn"
                                           ng-click="changeAttribute(attribute)"
                                           title="{{'CLICK_TO_UPDATE_VALUE' | translate}}"></a>

                                        <i ng-if="attribute.value.listValue != null && attribute.value.deleteFlag != true"
                                           ng-hide="attribute.editMode == true"
                                           title="{{'REMOVE_ATTRIBUTE_VALUE' | translate}}"
                                           ng-click="deleteAttribute(attribute)">
                                            <i class="fa fa-times-circle"
                                               style="margin-left: 25px;font-size: 14px;cursor:pointer;"></i></i>
                                    </div>
                                    <div ng-show="attribute.editMode == true">
                                        <i class="fa fa-check-circle" style="padding: 4px;cursor: pointer"
                                           title="{{saveAttributeTitle}}"
                                           ng-click="saveObjectProperties(attribute)">
                                        </i>
                                        <i class="fa fa-times-circle" style="cursor: pointer"
                                           title="{{cancelChangesTitle}}"
                                           ng-click="cancelListValue(attribute)">
                                        </i>
                                    </div>
                                </div>
                            </div>

                            <div ng-if="attribute.attributeDef.dataType == 'LIST' && attribute.attributeDef.listMultiple == false && attribute.attributeDef.configurable">
                                <a class="icon fa fa-paperclip"
                                   style="padding-right: 4px; overflow-y: hidden !important;"
                                   title="{{possibleTitle}}"
                                   ng-if="attribute.attributeDef.configurableAttr.length > 0"
                                   uib-popover-template="itemAttributePopover.templateUrl"
                                   popover-append-to-body="true"
                                   popover-popup-delay="50"
                                   popover-placement="right"
                                   popover-trigger="'outsideClick'">
                                </a>
                            </div>

                            <div ng-if="attribute.attributeDef.dataType == 'LIST' && attribute.attributeDef.listMultiple == true">
                                <div ng-if="attribute.value.mlistValue.length > 0" ng-hide="attribute.editMode == true"
                                     ng-repeat="listValue in attribute.value.mlistValue">
                                    <ul>
                                        <li>{{listValue}}</li>
                                    </ul>
                                </div>
                                <a ng-if="attribute.editMode == false && (hasPermission || (attribute.attributeDef.allowEditAfterRelease && hasReleased))"
                                   href="" ng-click="changeAttribute(attribute)"
                                   title="{{'CLICK_TO_UPDATE_VALUE' | translate}}"><i
                                        class="fa fa-pencil row-edit-btn"></i>
                                </a>

                                <div ng-if="attribute.editMode" style="width: 400px; display: inline-flex">
                                    <div style="width: 350px">
                                        <ui-select multiple ng-model="attribute.value.mlistValue" theme="bootstrap"
                                                   close-on-select="false" title="Choose a List" remove-selected="true">
                                            <ui-select-match placeholder="{{'SELECT_LIST' | translate}}">{{$item}}
                                            </ui-select-match>
                                            <ui-select-choices
                                                    repeat="value in attribute.attributeDef.lov.values track by value">
                                                <div ng-bind="value"></div>
                                            </ui-select-choices>
                                        </ui-select>
                                    </div>
                                    <div style="width: 25px">
                                        <a ng-if="attribute.attributeDef.dataType == 'LIST'"
                                           ng-click="saveObjectProperties(attribute)"
                                           title="{{saveAttributeTitle}}">
                                            <i class="fa fa-check-circle" style="padding: 8px;cursor:pointer;"></i>
                                        </a>
                                    </div>
                                    <div style="width: 25px;">
                                        <a ng-if="attribute.attributeDef.dataType == 'LIST'"
                                           ng-click="cancelChanges(attribute)"
                                           title="{{cancelChangesTitle}}">
                                            <i class="fa fa-times-circle" style="padding: 8px;cursor:pointer;"></i></a>
                                    </div>
                                </div>

                            </div>

                            <div ng-if="attribute.attributeDef.dataType == 'MULTILISTCHECKBOX'">
                                <div ng-repeat="value in attribute.attributeDef.lov.values">
                                    <input type="checkbox" ng-checked="checkValue(attribute,value)"
                                           ng-disabled="!attribute.editMode"
                                           ng-click="selectMultiListCheckBox(attribute,value)"
                                           style="margin-right: 5px;">{{value}}<br>
                                </div>
                                <a ng-if="attribute.editMode == false && (hasPermission || (attribute.attributeDef.allowEditAfterRelease && hasReleased))"
                                   href="" ng-click="editListValue(attribute)"
                                   title="{{'CLICK_TO_UPDATE_VALUE' | translate}}"><i
                                        class="fa fa-pencil row-edit-btn"></i>
                                </a>

                                <div ng-if="attribute.editMode" style="width: 400px; display: inline-flex">
                                    <div style="width: 25px">
                                        <a ng-click="saveObjectProperties(attribute)"
                                           title="{{saveAttributeTitle}}">
                                            <i class="fa fa-check-circle" style="padding: 8px;cursor:pointer;"></i>
                                        </a>
                                    </div>
                                    <div style="width: 25px;">
                                        <a ng-click="cancelListValue(attribute)"
                                           title="{{cancelChangesTitle}}">
                                            <i class="fa fa-times-circle" style="padding: 8px;cursor:pointer;"></i></a>
                                    </div>
                                </div>
                            </div>

                            <%----------------  TIMESTAMP Property  -------------------%>

                            <div ng-if="attribute.attributeDef.dataType == 'TIMESTAMP'">
                        <span ng-if="attribute.showTimestamp == false" ng-click="changeTimestamp(attribute)">
                            {{attribute.value.timestampValue}}
                        </span>
                                <a class="fa fa-pencil row-edit-btn" ng-click="changeTimestamp(attribute)"
                                   ng-if="attribute.showTimestamp == false && (hasPermission || (attribute.attributeDef.allowEditAfterRelease && hasReleased))"
                                   title="{{'CLICK_TO_SET_TIME' | translate}}"
                                   style="padding-left: 7px;"></a>

                                <div ng-if="attribute.showTimestamp == true">
                                    <div>
                                        {{attribute.value.timestampValue}}
                                    </div>
                                    <div uib-timepicker
                                         ng-model="attribute.timestampValue">
                                    </div>
                                    <button class="btn btn-sm btn-primary" type="button"
                                            title="{{'SAVE_TIME' | translate}}"
                                            style="margin-top:-195px;margin-left:165px;"
                                            ng-click="saveTimeProperty(attribute)"><i
                                            class="fa fa-check"></i>
                                    </button>

                                    <button class="btn btn-sm btn-default" type="button" style="margin-top:-195px;"
                                            title="{{cancelChangesTitle}}"
                                            ng-click="cancelTime(attribute)"><i class="fa fa-times"></i>
                                    </button>
                                </div>
                            </div>

                            <%----------------  TIME Property  -------------------%>

                            <div ng-if="attribute.attributeDef.dataType == 'TIME'">
                                <span ng-if="attribute.showTimeAttribute == false">{{attribute.value.timeValue}}</span>
                                <a class="fa fa-pencil row-edit-btn"
                                   ng-if="attribute.showTimeAttribute == false && (hasPermission || (attribute.attributeDef.allowEditAfterRelease && hasReleased))"
                                   ng-click="changeTime(attribute)" title="{{'CLICK_TO_SET_TIME' | translate}}"
                                   style="padding-left: 7px;"></a>

                                <div ng-if="attribute.showTimeAttribute == true" style="display: flex;">

                                    <input class="form-control" type="text" ng-model="attribute.timeValue" time-picker
                                           style="width: 240px;">

                                    <button class="btn btn-sm btn-primary" type="button"
                                            title="{{'SAVE' | translate}}"
                                            ng-click="saveTimeProperty(attribute)"><i class="fa fa-check"></i>
                                    </button>

                                    <button class="btn btn-sm btn-default" type="button"
                                            title="{{cancelChangesTitle}}"
                                            ng-click="cancelTime(attribute)"><i class="fa fa-times"></i>
                                    </button>
                                    <a class="icon fa fa-info-circle"
                                       ng-if="attribute.attributeDef.validations != null"
                                       style="padding:0 10px;margin-top: 10px;"
                                       title="Attribute validations"
                                       uib-popover-template="attributeValidationsPopover.templateUrl"
                                       popover-append-to-body="true"
                                       popover-popup-delay="50"
                                       popover-placement="{{validationPopover}}"
                                       popover-title="{{attribute.attributeDef.name}} validations"
                                       popover-trigger="'outsideClick'">
                                    </a>
                                </div>
                            </div>

                            <%----------------  IMAGE Property  -------------------%>

                            <div ng-if="attribute.attributeDef.dataType == 'IMAGE'">
                                <a href="" title="{{'CLICK_TO_SHOW_LARGE_IMAGE' | translate}}"
                                   ng-click="showThumbnailImage(attribute)">
                                    <img ng-if="attribute.value.imageValue != null"
                                         ng-src="{{attribute.value.imagePath}}"
                                         style="height: 100px;width: 100px;">
                                </a><br ng-if="attribute.value.imageValue != null">

                                <%---- To show large image  ------%>
                                <%--<div id="myModal" class="img-model modal">
                                    <span class="closeimage">&times;</span>
                                    <img class="modal-content" id="img01">
                                </div>--%>
                                <div id="item-thumbnail-basic{{attribute.attributeDef.id}}"
                                     class="item-thumbnail modal">
                                    <div class="item-thumbnail-content">
                                        <div class="thumbnail-content" style="display: flex;width: 100%;">
                                            <div class="thumbnail-view"
                                                 id="thumbnail-view-basic{{attribute.attributeDef.id}}">
                                                <div id="thumbnail-image-basic{{attribute.attributeDef.id}}"
                                                     style="display: table-cell;vertical-align: middle;text-align: center;">
                                                    <img ng-src="{{attribute.value.imagePath}}"
                                                         style="height: auto;width: auto;max-width: 100%;max-height: 90%"/>
                                            <span class="thumbnail-close"
                                                  id="thumbnail-close-basic{{attribute.attributeDef.id}}"></span>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                </div>

                                <div>
                                    <a ng-if="(hasPermission || (attribute.attributeDef.allowEditAfterRelease && hasReleased))"
                                       class="fa fa-pencil row-edit-btn"
                                       ng-click="change(attribute)" style="margin-top: 5px;color:#337ab7"
                                       ng-hide="attribute.changeImage == true"
                                       title="{{changeImageMessage}}">
                                    </a>
                                    <input ng-show="attribute.changeImage == true" class="browse-control" name="file"
                                           type="file" ng-file-model="attribute.newImageValue" style="width: 250px;"
                                           accept="image/*">
                                    <button class="btn btn-sm btn-primary"
                                            ng-click="saveImage(attribute)"
                                            ng-show="attribute.changeImage == true" title="{{'SAVE' | translate}}"
                                            style="margin-top:-62px;margin-left: 255px;padding-top:8px;">
                                        <i class="fa fa-check"></i>
                                    </button>
                                    <button class="btn btn-sm btn-default"
                                            ng-click="cancel(attribute)"
                                            ng-show="attribute.changeImage == true" title="{{cancelChangesTitle}}"
                                            style="margin-top:-62px;padding-top:8px;">
                                        <i class="fa fa-times"></i>
                                    </button>
                                </div>
                            </div>

                            <%----------------  ATTACHMENT Property  -------------------%>

                            <div ng-if="attribute.attributeDef.dataType == 'ATTACHMENT'">
                                <div ng-if="attribute.value.attachmentValues.length > 0"
                                     ng-repeat="attachment in attribute.value.attachmentValues">
                                    <ul style="margin-left:-30px;">
                                        <li>
                                            <a href="" ng-click="openPropertyAttachment(attachment)"
                                               title="{{'CLICK_TO_DOWNLOAD_ATTACHMENT' | translate}}"
                                               style="margin-bottom: 5px;width:250px;color: #002451;"
                                               ng-bind-html="attachment.name">
                                            </a>
                                            <a href="" title="{{deleteAttachment}}"
                                               ng-if="(hasPermission || (attribute.attributeDef.allowEditAfterRelease && hasReleased))"
                                               ng-click="deleteAttachments(attribute,attachment)">
                                                <i class="fa fa-times row-edit-btn"
                                                   style="margin-left: 5px;font-size: 14px;color: darkred"></i>
                                            </a>
                                        </li>
                                    </ul>
                                </div>

                                <div>
                                    <a href="">
                                        <div ng-show="attribute.showAttachment == false && (hasPermission || (attribute.attributeDef.allowEditAfterRelease && hasReleased))"
                                             title="{{addAttachments}}"
                                             ng-click="addAttachment(attribute)">
                                            <a class="fa fa-pencil row-edit-btn" title="" style="padding-left: 7px"></a>
                                        </div>
                                    </a>

                                    <div ng-show="attribute.showAttachment == true">
                                        <input class="browse-control" name="file"
                                               multiple="true"
                                               type="file" ng-file-model="attribute.attachmentValues"
                                               style="width: 250px;"/>
                                        <button class="btn btn-sm btn-primary" title="{{'SAVE' | translate}}"
                                                ng-click="saveAttachments(attribute)"
                                                style="margin-top:-62px;margin-left: 255px;padding-top:8px;">
                                            <i class="fa fa-check"></i>
                                        </button>
                                        <button class="btn btn-sm btn-default" title="{{cancelChangesTitle}}"
                                                ng-click="cancelAttachment(attribute)"
                                                style="margin-top:-62px;padding-top:8px;">
                                            <i class="fa fa-times"></i>
                                        </button>
                                        <p ng-repeat="file in attribute.attachmentValues">
                                            <span ng-bind-html="file.name"></span>
                                        </p>
                                    </div>
                                </div>
                            </div>

                            <%------------------------  OBJECT Property  -------------------------%>

                            <div class="row" ng-if="attribute.attributeDef.dataType == 'OBJECT'">

                                <a ng-if="attribute.value.refValue.objectType == 'ITEM'" href=""
                                   ng-click="showRefValueDetails(attribute)"
                                   title="{{'CLICK_TO_SHOW_DETAILS' | translate}}">
                                    {{attribute.value.refValue.itemNumber+"
                                    "+attribute.value.refValue.itemRevision.revision+"
                                    "+attribute.value.refValue.itemRevision.lifeCyclePhase.phase}}
                                </a>

                                <a ng-if="attribute.value.refValue.objectType == 'ITEMREVISION'" href=""
                                   ng-click="showRefValueDetails(attribute)"
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
                                <span ng-if="attribute.value.refValue.changeType == 'DEVIATION' || attribute.value.refValue.changeType == 'WAIVER'"
                                      ng-click="showRefValueDetails(attribute)"
                                      title="{{'CLICK_TO_SHOW_DETAILS' | translate}}"
                                      ng-bind-html="attribute.value.refValue.varianceNumber">
                                </span>
                                </a>

                                <a ng-if="attribute.value.refValue.objectType == 'OEMPARTMCO'||  attribute.value.refValue.objectType == 'ITEMMCO'"
                                   href="">
                                <span ng-click="showRefValueDetails(attribute)"
                                      title="{{'CLICK_TO_SHOW_DETAILS' | translate}}"
                                      ng-bind-html="attribute.value.refValue.mcoNumber">
                                </span>
                                </a>

                                <a ng-if="attribute.value.refValue.objectType == 'PLMWORKFLOWDEFINITION'" href=""
                                   ng-click="showRefValueDetails(attribute)"
                                   title="{{'CLICK_TO_SHOW_DETAILS' | translate}}"
                                   ng-bind-html="attribute.value.refValue.master.number">
                                </a>

                                <a ng-if="attribute.value.refValue.objectType == 'MANUFACTURER'" href=""
                                   ng-click="showRefValueDetails(attribute)"
                                   title="{{'CLICK_TO_SHOW_DETAILS' | translate}}"
                                   ng-bind-html="attribute.value.refValue.name">
                                </a>

                                <a ng-if="attribute.value.refValue.objectType == 'MANUFACTURERPART'" href=""
                                   ng-click="showRefValueDetails(attribute)"
                                   title="{{'CLICK_TO_SHOW_DETAILS' | translate}}"
                                   ng-bind-html="attribute.value.refValue.partName">
                                </a>


                                <a ng-if="attribute.value.refValue.objectType == 'PLANT' || attribute.value.refValue.objectType == 'ASSEMBLYLINE' ||
                                attribute.value.refValue.objectType == 'WORKCENTER' || attribute.value.refValue.objectType == 'MACHINE' ||
                                attribute.value.refValue.objectType == 'JIGFIXTURE' || attribute.value.refValue.objectType == 'MANPOWER' ||
                                attribute.value.refValue.objectType == 'INSTRUMENT' || attribute.value.refValue.objectType == 'EQUIPMENT' ||
                                attribute.value.refValue.objectType == 'PRODUCTIONORDER' || attribute.value.refValue.objectType == 'OPERATION' ||
                                attribute.value.refValue.objectType == 'MATERIAL' || attribute.value.refValue.objectType == 'TOOL'"
                                   href=""
                                   ng-click="showRefValueDetails(attribute)"
                                   title="{{'CLICK_TO_SHOW_DETAILS' | translate}}"
                                   ng-bind-html="attribute.value.refValue.number">
                                </a>

                                <a ng-if="attribute.value.refValue.objectType == 'MROASSET' || attribute.value.refValue.objectType == 'MROMETER' ||
                                attribute.value.refValue.objectType == 'MROSPAREPART' || attribute.value.refValue.objectType == 'MROWORKREQUEST' || attribute.value.refValue.objectType == 'MROWORKORDER'"
                                   href=""
                                   ng-click="showRefValueDetails(attribute)"
                                   title="{{'CLICK_TO_SHOW_DETAILS' | translate}}"
                                   ng-bind-html="attribute.value.refValue.number">
                                </a>

                                <a ng-if="attribute.value.refValue.objectType == 'REQUIREMENT' || attribute.value.refValue.objectType == 'REQUIREMENTDOCUMENT' || attribute.value.refValue.objectType == 'CUSTOMOBJECT'"
                                   href=""
                                   ng-click="showRefValueDetails(attribute)"
                                   title="{{'CLICK_TO_SHOW_DETAILS' | translate}}"
                                   ng-bind-html="attribute.value.refValue.number">
                                </a>

                                <a ng-if="attribute.value.refValue.objectType == 'PERSON'"
                                   ng-bind-html="attribute.value.refValue.fullName">
                                </a>
                                <a ng-if="attribute.value.refValue.objectType == 'PROJECT'"
                                   ng-bind-html="attribute.value.refValue.name">
                                </a>
                                <a ng-if="attribute.attributeDef.refType == 'QUALITY'"
                                   ng-click="showRefValueDetails(attribute)">
                                    <span ng-if="attribute.value.refValue.objectType == 'PRODUCTINSPECTIONPLAN' || attribute.value.refValue.objectType == 'MATERIALINSPECTIONPLAN'">
                                        {{attribute.value.refValue.number}}
                                    </span>
                                    <span ng-if="attribute.value.refValue.objectType == 'PROBLEMREPORT'">{{attribute.value.refValue.prNumber}}</span>
                                    <span ng-if="attribute.value.refValue.objectType == 'NCR'">{{attribute.value.refValue.ncrNumber}}</span>
                                    <span ng-if="attribute.value.refValue.objectType == 'QCR'">{{attribute.value.refValue.qcrNumber}}</span>
                                </a>
                                <a ng-if="attribute.editMode == false && (hasPermission || (attribute.attributeDef.allowEditAfterRelease && hasReleased))"
                                   class="fa fa-pencil row-edit-btn" title="{{changeAttributeTitle}}"
                                   style="margin-left:5px;color:#337ab7"
                                   ng-click="showSelectionDialog(attribute.attributeDef.refType, attribute)">
                                </a>
                                <button ng-if="attribute.editMode == true" class="btn btn-sm btn-primary"
                                        title="{{'SAVE' | translate}}"
                                        ng-click="saveObject(attribute)">
                                    <i class="fa fa-check"></i>
                                </button>

                                <button ng-if="attribute.editMode == true" class="btn btn-sm btn-default"
                                        title="{{cancelChangesTitle}}"
                                        ng-click="objectCancelChanges(attribute)">
                                    <i class="fa fa-times"></i>
                                </button>
                            </div>

                            <%------  Currency Property  ------%>

                            <div ng-if="attribute.attributeDef.dataType == 'CURRENCY'">
                                <div ng-if="attribute.value.encodedCurrencyType != null">
                                    <a ng-if="attribute.editMode == false" href=""
                                       ng-bind-html="attribute.value.encodedCurrencyType">
                                    </a>
                            <span ng-if="attribute.editMode == false && !attribute.changeCurrency">
                                {{attribute.value.currencyValue}}
                            </span>
                                    <a class="fa fa-pencil row-edit-btn"
                                       ng-if="attribute.editMode == false && (hasPermission || (attribute.attributeDef.allowEditAfterRelease && hasReleased))"
                                       title="{{'CLICK_TO_SET_TIME' | translate}}" ng-click="changeAttribute(attribute)"
                                       style="padding-left: 7px;cursor: pointer;"></a>

                                </div>

                                <%----------  For newly added 'CURRENCY' property after ECO Creation ---------------%>

                                <div ng-if="attribute.value.encodedCurrencyType == null || attribute.value.currencyType == null">
                                    <div ng-if="attribute.changeCurrency == true" style="width: 150px;">
                                        <select class="form-control" ng-model="attribute.value.currencyType"
                                                style="height: auto;"
                                                ng-init="attribute.value.currencyType = currencies[0].id"
                                                ng-options="currency.id as currency.name for currency in currencies">
                                        </select>
                                    </div>
                            <span ng-if="attribute.editMode == false && !attribute.changeCurrency">
                                {{attribute.value.currencyValue}}
                            </span>
                                    <a class="fa fa-pencil row-edit-btn" title="{{'CLICK_TO_UPDATE_VALUE' | translate}}"
                                       ng-if="attribute.changeCurrency == false && (hasPermission || (attribute.attributeDef.allowEditAfterRelease && hasReleased))"
                                       ng-click="changeCurrencyValue(attribute)"
                                       style="padding-left: 7px;cursor: pointer">
                                    </a>

                                    <div ng-if="attribute.changeCurrency == true"
                                         style="width: 100px;margin-top: -40px;margin-left: 155px;">
                                        <input class="form-control" name="currencyValue" type="number"
                                               placeholder="{{enterValue}}"
                                               ng-model="attribute.value.currencyValue"/>
                                    </div>
                                    <button ng-if="attribute.changeCurrency == true" class="btn btn-sm btn-primary"
                                            title="{{'SAVE' | translate}}"
                                            ng-click="saveObjectProperties(attribute)"
                                            style="margin-top:-64px;margin-left: 260px;padding-top:10px;">
                                        <i class="fa fa-check"></i>
                                    </button>
                                    <button ng-if="attribute.changeCurrency == true" class="btn btn-sm btn-default"
                                            title="{{cancelChangesTitle}}"
                                            ng-click="cancelChanges(attribute)"
                                            style="margin-top:-64px;padding-top:10px;">
                                        <i class="fa fa-times"></i>
                                    </button>
                                    <a class="icon fa fa-info-circle"
                                       ng-if="attribute.attributeDef.validations != null && attribute.changeCurrency == true"
                                       style="padding:0 10px;position:absolute;margin-top: -25px;"
                                       title="Attribute validations"
                                       uib-popover-template="attributeValidationsPopover.templateUrl"
                                       popover-append-to-body="true"
                                       popover-popup-delay="50"
                                       popover-placement="{{validationPopover}}"
                                       popover-title="{{attribute.attributeDef.name}} validations"
                                       popover-trigger="'outsideClick'">
                                    </a>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </form>
    </div>
</div>





