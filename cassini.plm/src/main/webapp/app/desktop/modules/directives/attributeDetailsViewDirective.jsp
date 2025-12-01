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

</style>

<br>
<h4 ng-if="objectProperties.length > 0" class="section-title" style="color: #8e8181;padding-left: 36px;">
    Attributes</h4>
<br>

<div class="row row-eq-height">
    <div class="col-sm-12">
        <div class="item-details">
            <div class="row" ng-repeat="attribute in objectProperties track by $index">


                <%--<div class="label label-name col-xs-4 col-sm-3" style="text-align: right;">
                    <span>{{attribute.name}}</span>
                    <span ng-if="attribute.required == true" class="asterisk">*</span>:
                </div>--%>
                <div class="label label-name col-xs-4 col-sm-3 text-right">
           <span ng-bind-html="attribute.attributeDef.name| limitTo: 24 "
                 title="{{attribute.attributeDef.name}}">
            </span>:
                </div>

                <div class="value col-xs-8 col-sm-9">

                    <div ng-if="attribute.attributeDef.dataType == 'TEXT'">
                        <span ng-bind-html="attribute.value.stringValue" ng-if="!attribute.editMode"></span>
                        <a href="" class="fa fa-pencil row-edit-btn" ng-click="changeAttribute(attribute)"
                           title="{{'CLICK_TO_SET_VALUE' | translate}}" style="cursor:pointer;"
                           ng-if="!attribute.editMode"></a>
                    </div>

                    <%----------------  longText Value  -------------------%>

                    <div ng-if="attribute.attributeDef.dataType == 'LONGTEXT'">
                        <span ng-if="!attribute.editMode" ng-bind-html="attribute.value.longTextValue"></span>
                        <a href="" class="fa fa-pencil row-edit-btn" ng-if="!attribute.editMode"
                           ng-click="changeAttribute(attribute)"
                           title="{{changeValue}}"
                           style="padding-left: 7px"></a>
                    </div>

                    <%--------------- RichTextValue --------------%>

                    <div ng-if="attribute.attributeDef.dataType == 'RICHTEXT'">
                        <span ng-if="attribute.editMode == false"
                              ng-bind-html="attribute.value.encodedRichTextValue">
                        </span>
                        <a href="" class="fa fa-pencil row-edit-btn" style="padding-left: 7px;cursor: pointer;"
                           ng-if="!attribute.editMode"
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
                        <a href="" class="g" style="padding-left: 7px;"
                           title="{{'CLICK_TO_SET_VALUE' | translate}}" ng-if="!attribute.editMode"
                           ng-click="changeAttribute(attribute)"></a>
                    </div>


                    <%------------ HyperLink Attribute ----%>


                    <div id="intClick" ng-if="attribute.attributeDef.dataType == 'HYPERLINK'">
                        <a href="" ng-click="showHyperLink(attribute.value.hyperLinkValue)"
                           ng-if="attribute.editMode == false">{{attribute.value.hyperLinkValue}}</a>
                        <i class="fa fa-pencil row-edit-btn" ng-if="attribute.editMode == false"
                           href="" ng-click="changeAttribute(attribute)"
                           title="{{'CLICK_TO_SET_VALUE' | translate}}"></i>
                    </div>


                    <%----------------  DOUBLE Value  -------------------%>

                    <div ng-if="attribute.attributeDef.dataType == 'DOUBLE'">
                        <span ng-if="attribute.editMode == false ">
                            {{attribute.value.doubleValue}}
                        </span>
                        <i class="fa fa-pencil row-edit-btn" ng-if="attribute.editMode == false"
                           href="" ng-click="changeAttribute(attribute)" title="{{changeValue}}"></i>
                    </div>

                    <%----------------  DATE Value  -------------------%>

                    <div ng-if="attribute.attributeDef.dataType == 'DATE'">
                        <span ng-if="attribute.editMode == false">
                            {{ (attribute.value.dateValue | date:"dd/MM/yyyy")}}
                        </span>
                        <i class="fa fa-pencil row-edit-btn" style="padding-left: 7px;"
                           ng-if="attribute.editMode == false" ng-click="changeAttribute(attribute)"
                           title="{{'CLICK_TO_SET_DATE' | translate}}"></i>
                    </div>

                    <%----------------  BOOLEAN Value  -------------------%>

                    <div ng-if="attribute.attributeDef.dataType == 'BOOLEAN'">
                        <a href="" ng-if="attribute.editMode == false"
                           ng-click="changeAttribute(attribute)"
                           title="{{changeValue}}" ng-bind-html="attribute.value.booleanValue">
                        </a>
                        <i style="padding-left: 7px" ng-click="changeAttribute(attribute)"
                           ng-hide="attribute.editMode == true && !hasPermission"
                           title="{{'CLICK_TO_EDIT_VALUE' | translate}}"
                           class="fa fa-pencil row-edit-btn"></i>
                    </div>


                    <div ng-if="attribute.editMode == true &&
            (attribute.attributeDef.dataType == 'TEXT' || attribute.attributeDef.dataType == 'INTEGER' || attribute.attributeDef.dataType == 'LONGTEXT' ||
             attribute.attributeDef.dataType == 'DOUBLE' || attribute.attributeDef.dataType == 'BOOLEAN' || attribute.attributeDef.dataType == 'HYPERLINK' ||
             attribute.attributeDef.dataType == 'DATE' || attribute.attributeDef.dataType == 'CURRENCY')">

                        <input type="text" class="form-control" name="title"
                               ng-if="attribute.attributeDef.dataType == 'TEXT'"
                               placeholder="{{'ENTER_VALUE' | translate}}"
                               ng-model="attribute.value.stringValue" style="width:200px;">
                <textarea type="text" class="form-control" name="title"
                          ng-if="attribute.attributeDef.dataType == 'LONGTEXT'"
                          placeholder="{{'ENTER_VALUE' | translate}}"
                          ng-model="attribute.value.longTextValue" style="width:320px;"></textarea>
                        <input type="number" class="form-control" name="title"
                               ng-if="attribute.attributeDef.dataType == 'INTEGER'"
                               placeholder="{{'ENTER_NUMBER' | translate}}"
                               ng-model="attribute.value.integerValue" style="width:200px;">
                        <input type="text" class="form-control" name="title" valid-number
                               ng-if="attribute.attributeDef.dataType == 'DOUBLE'"
                               placeholder="{{'ENTER_NUMBER' | translate}}"
                               ng-model="attribute.value.doubleValue" style="width:200px;">
                        <input type="text" class="form-control" name="title"
                               ng-if="attribute.attributeDef.dataType == 'HYPERLINK'"
                               placeholder="{{'ENTER_NUMBER' | translate}}"
                               ng-model="attribute.value.hyperLinkValue" style="width:350px;">
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
                        <input type="text" class="form-control" name="title"
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
                                style="margin-top:-65px;margin-left:325px;padding: 8px;width:33px;"
                                ng-click="saveObjectProperties(attribute)"><i class="fa fa-check"></i>
                        </button>
                        <button ng-if="attribute.attributeDef.dataType == 'LONGTEXT'" class="btn btn-sm btn-default"
                                type="button" title="{{cancelChangesTitle}}"
                                style="margin-top:-65px;padding: 8px;width:33px;"
                                ng-click="cancelChanges(attribute)"><i class="fa fa-times"></i>
                        </button>

                        <button ng-if="attribute.attributeDef.dataType == 'HYPERLINK'" class="btn btn-sm btn-primary"
                                type="button" title="{{saveAttributeTitle}}"
                                style="margin-top:-65px;margin-left:353px;padding: 8px;width:33px;"
                                ng-click="saveObjectProperties(attribute)"><i class="fa fa-check"></i>
                        </button>
                        <button ng-if="attribute.attributeDef.dataType == 'HYPERLINK'" class="btn btn-sm btn-default"
                                type="button" title="{{cancelChangesTitle}}"
                                style="margin-top:-65px;padding: 8px;width:33px;"
                                ng-click="cancelChanges(attribute)"><i class="fa fa-times"></i>
                        </button>
                    </div>

                    <%----------------  LIST Value  -------------------%>

                    <div ng-if="attribute.attributeDef.dataType == 'LIST' && attribute.attributeDef.listMultiple == false">
                        <div style="display: flex">
                            <ui-select ng-show="attribute.editMode == true" ng-model="attribute.value.listValue"
                                       theme="bootstrap" style="width:30%">
                                <ui-select-match placeholder="Select">{{$select.selected}}</ui-select-match>
                                <ui-select-choices
                                        repeat="value in attribute.attributeDef.lov.values">
                                    <div ng-bind="value"></div>
                                </ui-select-choices>
                            </ui-select>

                            <span ng-show="attribute.editMode == false && attribute.value.listValue != null">
                                        {{attribute.value.listValue}}
                            </span>

                            <div ng-show="attribute.editMode == false">
                                <i style="padding: 6px;color: #337ab7" class="fa fa-pencil row-edit-btn"
                                   ng-click="changeAttribute(attribute)"
                                   title="{{'CLICK_TO_SET_VALUE' | translate}}"></i>

                                <i ng-if="attribute.value.listValue != null && attribute.value.deleteFlag != true"
                                   ng-hide="attribute.editMode == true"
                                   title="{{'REMOVE_ATTRIBUTE_VALUE' | translate}}"
                                   ng-click="deleteAttribute(attribute)">
                                    <i class="fa fa-times-circle"
                                       style="margin-left: 25px;font-size: 14px;cursor:pointer;"></i></i>
                            </div>

                        </div>
                        <div style="margin-left: 294px;margin-top: -20px">
                            <i class="fa fa-check-circle" style="padding: 4px;cursor: pointer"
                               ng-show="attribute.editMode == true"
                               title="{{saveAttributeTitle}}"
                               ng-click="saveObjectProperties(attribute)">
                            </i>
                            <i class="fa fa-times-circle" style="cursor: pointer"
                               ng-show="attribute.editMode == true"
                               title="{{cancelChangesTitle}}"
                               ng-click="cancelListValue(attribute)">
                            </i>
                        </div>
                    </div>


                    <div ng-if="attribute.attributeDef.dataType == 'LIST' && attribute.attributeDef.listMultiple == true">
                        <div ng-if="attribute.value.mlistValue.length > 0" ng-hide="attribute.editMode == true"
                             ng-repeat="listValue in attribute.value.mlistValue">
                            <ul>
                                <li>{{listValue}}</li>
                            </ul>
                        </div>
                        <a ng-if="attribute.editMode == false"
                           href="" ng-click="changeAttribute(attribute)" title="{{'CLICK_TO_SET_VALUE' | translate}}"><i
                                class="fa fa-pencil row-edit-btn"></i>
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

                    <%----------------  TIMESTAMP Property  -------------------%>

                    <div ng-if="attribute.attributeDef.dataType == 'TIMESTAMP'">
                        <span ng-if="attribute.showTimestamp == false" ng-click="changeTimestamp(attribute)">
                            {{attribute.value.timestampValue}}
                        </span>
                        <i class="fa fa-pencil row-edit-btn" ng-click="changeTimestamp(attribute)"
                           ng-if="attribute.showTimestamp == false"
                           title="{{'CLICK_TO_SET_TIME' | translate}}"
                           style="padding-left: 7px;"></i>

                        <div ng-if="attribute.showTimestamp == true">
                            <div>
                                {{attribute.value.timestampValue}}
                            </div>
                            <div uib-timepicker
                                 ng-model="attribute.timestampValue">
                            </div>
                            <button class="btn btn-sm btn-primary" type="button" title="{{'SAVE_TIME' | translate}}"
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
                        <i class="fa fa-pencil row-edit-btn" ng-if="attribute.showTimeAttribute == false"
                           ng-click="changeTime(attribute)" title="{{'CLICK_TO_SET_TIME' | translate}}"
                           style="padding-left: 7px;"></i>

                        <div ng-if="attribute.showTimeAttribute == true">

                            <input class="form-control" type="text" ng-model="attribute.timeValue" time-picker
                                   style="width: 240px;">


                            <button class="btn btn-sm btn-primary" type="button"
                                    title="{{'SAVE_ATTRIBUTE' | translate}}"
                                    style="margin-top:-66px;margin-left:253px;"
                                    ng-click="saveTimeProperty(attribute)"><i class="fa fa-check"></i>
                            </button>

                            <button class="btn btn-sm btn-default" type="button" style="margin-top:-66px;"
                                    title="{{cancelChangesTitle}}"
                                    ng-click="cancelTime(attribute)"><i class="fa fa-times"></i>
                            </button>
                        </div>
                    </div>

                    <%----------------  IMAGE Property  -------------------%>

                    <div ng-if="attribute.attributeDef.dataType == 'IMAGE'">
                        <a href="" title="{{'CLICK_TO_SHOW_LARGE_IMAGE' | translate}}"
                           ng-click="showImageProperty(attribute)">
                            <img ng-if="attribute.value.imageValue != null"
                                 ng-src="{{attribute.value.ecoImagePath}}"
                                 style="height: 100px;width: 100px;">
                        </a><br ng-if="attribute.value.imageValue != null">

                        <%---- To show large image  ------%>
                        <div id="myModal" class="img-model modal">
                            <span class="closeimage">&times;</span>
                            <img class="modal-content" id="img01">
                        </div>

                        <div>
                            <a href="" ng-if="attribute.value.imageValue == null"
                               ng-click="change(attribute)"
                               ng-hide="attribute.changeImage == true"
                               title="{{addImage}}">
                                <i class="fa fa-pencil row-edit-btn" title="{{'ADD_IMAGE' | translate}}"
                                   style="padding-left: 7px"></i>
                            </a>
                            <i ng-if="attribute.value.imageValue != null"
                               class="fa fa-pencil row-edit-btn"
                               ng-click="change(attribute)" style="margin-top: 5px;color:#337ab7"
                               ng-hide="attribute.changeImage == true"
                               title="{{changeImageMessage}}">
                            </i>
                            <input ng-show="attribute.changeImage == true" class="browse-control" name="file"
                                   type="file" ng-file-model="attribute.newImageValue" style="width: 250px;">
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
                                       ng-click="deleteAttachments(attribute,attachment)">
                                        <i class="fa fa-times"
                                           style="margin-left: 5px;font-size: 14px;color: darkred"></i>
                                    </a>
                                </li>
                            </ul>
                        </div>

                        <div>
                            <a href="">
                                <div ng-show="attribute.showAttachment == false"
                                     title="{{addAttachments}}"
                                     ng-click="addAttachment(attribute)">
                                    <i class="fa fa-pencil row-edit-btn" title="" style="padding-left: 7px"></i>
                                </div>
                            </a>

                            <div ng-show="attribute.showAttachment == true">
                                <input class="browse-control" name="file"
                                       multiple="true"
                                       type="file" ng-file-model="attribute.attachmentValues"
                                       style="width: 250px;"/>
                                <button class="btn btn-sm btn-primary" title="{{saveAttachmentsTitle}}"
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

                        <a ng-if="attribute.value.refValue.objectType == 'CHANGE'" href=""
                           ng-click="showRefValueDetails(attribute)"
                           title="{{'CLICK_TO_SHOW_DETAILS' | translate}}"
                           ng-bind-html="attribute.value.refValue.ecoNumber">
                        </a>

                        <a ng-if="attribute.value.refValue.objectType == 'PLMWORKFLOWDEFINITION'" href=""
                           ng-click="showRefValueDetails(attribute)"
                           title="{{'CLICK_TO_SHOW_DETAILS' | translate}}"
                           ng-bind-html="attribute.value.refValue.name">
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

                        <a ng-if="attribute.value.refValue.objectType == 'REQUIREMENT'" href=""
                           ng-click="showRefValueDetails(attribute)"
                           title="{{'CLICK_TO_SHOW_DETAILS' | translate}}"
                           ng-bind-html="attribute.value.refValue.number">
                        </a>

                        <a ng-if="attribute.value.refValue.objectType == 'PERSON'"
                           ng-bind-html="attribute.value.refValue.fullName">
                        </a>
                        <a href=""
                           ng-if="attribute.value.refValue == null && attribute.editMode == false"
                           title="{{addAttribute}}"
                           ng-click="showObjectValues(attribute.attributeDef.refType, attribute)">
                            <i class="fa fa-pencil row-edit-btn"
                               title="{{'ADD_ATTRIBUTE' | translate}}"
                               style="padding-left: 7px;"></i>
                        </a>
                        <i ng-if="attribute.value.refValue != null && attribute.editMode == false"
                           class="fa fa-pencil row-edit-btn"
                           title="{{changeAttributeTitle}}" style="margin-left:5px;color:#337ab7"
                           ng-click="showSelectionDialog(attribute.attributeDef.refType, attribute)">
                        </i>
                        <button ng-if="attribute.editMode == true" class="btn btn-sm btn-primary"
                                title="{{'SAVE_ATTRIBUTE' | translate}}"
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
                            <span ng-if="attribute.editMode == false">
                                {{setCurrencyValue}}
                            </span>
                            <i class="fa fa-pencil row-edit-btn" ng-if="attribute.editMode == false"
                               title="{{'CLICK_TO_SET_TIME' | translate}}" ng-click="changeAttribute(attribute)"
                               style="padding-left: 7px;cursor: pointer;"></i>

                        </div>

                        <%----------  For newly added 'CURRENCY' property after ECO Creation ---------------%>

                        <div ng-if="attribute.value.encodedCurrencyType == null || attribute.value.currencyType == null">
                            <div ng-if="attribute.changeCurrency == true" style="width: 150px;">
                                <select class="form-control" ng-model="attribute.value.currencyType"
                                        ng-init="attribute.value.currencyType = currencies[0].id"
                                        ng-options="currency.id as currency.name for currency in currencies">
                                </select>
                            </div>
                            <span ng-if="attribute.changeCurrency == false">
                                {{attribute.value.currencyValue}}
                            </span>
                            <i class="fa fa-pencil row-edit-btn" title="{{'CLICK_TO_SET_VALUE' | translate}}"
                               ng-if="attribute.changeCurrency == false"
                               ng-click="changeCurrencyValue(attribute)" style="padding-left: 7px;cursor: pointer">
                            </i>

                            <div ng-if="attribute.changeCurrency == true"
                                 style="width: 100px;margin-top: -40px;margin-left: 155px;">
                                <input class="form-control" name="currencyValue" type="number"
                                       placeholder="{{enterValue}}"
                                       ng-model="attribute.value.currencyValue"/>
                            </div>
                            <button ng-if="attribute.changeCurrency == true" class="btn btn-sm btn-primary"
                                    title="{{saveCurrencyTitle}}"
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
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>




