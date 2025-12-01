<style scoped>
    .item-details > div.row.master-att:last-child,
    .item-details > div.row.revision-att:last-child {
        border-bottom: 0 !important;
    }

    /* The Close Button */
    .img-model .closeImage {
        position: absolute;
        top: 50px;
        right: 50px;

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


<div class="row row-eq-height" style="margin: 0">
    <div class="item-details col-sm-12" style="padding: 30px;">
        <div class="row" ng-if="editEntryVm.glossaryEntryAttributes.length != 0"
             ng-repeat="attribute in editEntryVm.glossaryEntryAttributes">
            <div class="label col-xs-4 col-sm-3 text-right">
                <span>{{attribute.attributeDef.name}}: </span>
            </div>
            <div class="value col-xs-8 col-sm-9">

                <%----------------  Text Value  -------------------%>

                <div ng-if="attribute.attributeDef.dataType == 'TEXT'">
                    <a ng-if="attribute.editMode == false && attribute.value.stringValue != null" href=""
                       ng-click="changeAttribute(attribute)">
                        {{attribute.value.stringValue}}
                        <i class="fa fa-pencil row-edit-btn" title="{{changeValue}}" style="padding-left: 5px"></i>
                    </a>
                    <a ng-if="attribute.editMode == false && attribute.value.stringValue == null" href=""
                       ng-click="changeAttribute(attribute)">
                        <i class="fa fa-pencil row-edit-btn" title="{{'CLICK_TO_SET_VALUE' | translate}}"
                           style="padding-left: 5px"></i>
                    </a>
                </div>


                <%----------------  longText Value  -------------------%>

                <div ng-if="attribute.attributeDef.dataType == 'LONGTEXT'">
                    <a ng-if="attribute.editMode == false && attribute.value.longTextValue != null" href=""
                       ng-click="changeAttribute(attribute)">
                        {{attribute.value.longTextValue}}
                        <i class="fa fa-pencil row-edit-btn" title="{{changeValue}}"
                           style="padding-left: 5px"></i>
                    </a>
                    <a ng-if="attribute.editMode == false && attribute.value.longTextValue == null" href=""
                       ng-click="changeAttribute(attribute)">
                        <i class="fa fa-pencil row-edit-btn" title="{{'CLICK_TO_SET_VALUE' | translate}}"
                           style="padding-left: 5px"></i>
                    </a>
                </div>


                <%----------------  HyperLink Attribute  -------------------%>

                <div ng-if="attribute.attributeDef.dataType == 'HYPERLINK'">
                    <a ng-if="attribute.editMode == false" href=""
                       ng-click="showHyperLink(attribute.value.hyperLinkValue)" title="{{changeValue}}">
                        {{attribute.value.hyperLinkValue}}

                    </a>
                    <a ng-if="attribute.editMode == false" href=""
                       ng-click="changeAttribute(attribute)" title="{{'CLICK_TO_SET_VALUE' | translate}}"><i
                            class="fa fa-pencil row-edit-btn"></i>
                    </a>
                </div>


                <%--------------- RichTextValue --------------%>

                <div ng-if="attribute.attributeDef.dataType == 'RICHTEXT'">
                <span>
                    <a ng-if="attribute.editMode == false && attribute.value.encodedRichTextValue == null"
                       href="" ng-click="editEntryVm.editRichText(attribute)">
                        <i class="fa fa-pencil row-edit-btn" title="{{'CLICK_TO_SET_VALUE' | translate}}"
                           style="padding-left: 5px"></i>
                    </a>

                       <span ng-if="attribute.editMode == false && attribute.value.encodedRichTextValue != null"
                             ng-bind-html="attribute.value.encodedRichTextValue">
                       </span>
                   <span>
                        <a ng-if="attribute.editMode == false && attribute.value.encodedRichTextValue != null" href=""
                           ng-click="editEntryVm.editRichText(attribute)">
                            <i class="fa fa-pencil row-edit-btn" title="{{'CLICK_TO_EDIT_VALUE' | translate}}"
                               style="padding-left: 5px"></i>
                        </a>
                    </span>

                </span>

                    <div ng-show="attribute.editMode == true">
                        <summernote ng-model="attribute.value.richTextValue"></summernote>
                    </div>

                    <button ng-show="attribute.editMode == true"
                            ng-click="editEntryVm.saveRichText(attribute)"><i class="fa fa-check"></i>
                    </button>

                    <button ng-show="attribute.editMode == true"
                            ng-click="editEntryVm.cancelRichText(attribute)"><i class="fa fa-times"></i>
                    </button>


                </div>

                <%----------------  Integer Value  -------------------%>


                <div id="intClick" ng-if="attribute.attributeDef.dataType == 'INTEGER'">
                    <a ng-if="attribute.editMode == false && attribute.value.integerValue != null"
                       href="" ng-click="changeAttribute(attribute)">
                        {{attribute.value.integerValue}}
                        <i class="fa fa-pencil row-edit-btn" title="{{changeValue}}"
                           style="padding-left: 5px"></i>
                    </a>
                    <a ng-if="attribute.editMode == false && attribute.value.integerValue == null"
                       href="" ng-click="changeAttribute(attribute)">
                        <i class="fa fa-pencil row-edit-btn" title="{{'CLICK_TO_SET_VALUE' | translate}}"
                           style="padding-left: 5px"></i>
                    </a>
                </div>

                <%----------------  DOUBLE Value  -------------------%>

                <div ng-if="attribute.attributeDef.dataType == 'DOUBLE'">
                    <a ng-if="attribute.editMode == false && attribute.value.doubleValue != null" href=""
                       title="{{changeValue}}" ng-click="changeAttribute(attribute)">
                        {{attribute.value.doubleValue}}
                        <i class="fa fa-pencil row-edit-btn" title="{{'CLICK_TO_SET_VALUE' | translate}}"
                           style="padding-left: 5px"></i>
                    </a>
                    <a ng-if="attribute.editMode == false && attribute.value.doubleValue == null" href=""
                       ng-click="changeAttribute(attribute)">
                        <i class="fa fa-pencil row-edit-btn" title="{{'CLICK_TO_SET_VALUE' | translate}}"
                           style="padding-left: 5px"></i>
                    </a>
                </div>

                <%----------------  DATE Value  -------------------%>

                <div ng-if="attribute.attributeDef.dataType == 'DATE'">
                    <a href="" ng-if="attribute.value.dateValue != null && attribute.editMode == false"
                       title="{{changeDate}}" ng-click="changeAttribute(attribute)">
                        {{ (attribute.value.dateValue | date:"dd/MM/yyyy")}}
                        <i class="fa fa-pencil row-edit-btn" title="{{'CLICK_TO_SET_VALUE' | translate}}"
                           style="padding-left: 5px"></i>
                    </a>
                    <a href="" ng-if="attribute.value.dateValue == null && attribute.editMode == false"
                       ng-click="changeAttribute(attribute)">
                        <i class="fa fa-pencil row-edit-btn" title="{{'CLICK_TO_SET_VALUE' | translate}}"
                           style="padding-left: 5px"></i>
                    </a>
                </div>

                <%----------------  BOOLEAN Value  -------------------%>

                <div ng-if="attribute.attributeDef.dataType == 'BOOLEAN'">
                    <a href=""
                       ng-if="attribute.editMode == false && attribute.value.booleanValue != null"
                       title="{{clickToChangeValue}}" ng-click="changeAttribute(attribute)">
                        {{attribute.value.booleanValue}}
                    </a>
                    <a href=""
                       ng-if="attribute.editMode == false && attribute.value.booleanValue == null"
                       title="{{clickToChangeValue}}" ng-click="changeAttribute(attribute)">
                        <i class="fa fa-pencil row-edit-btn" title="{{'CLICK_TO_SET_VALUE' | translate}}"
                           style="padding-left: 5px"></i>
                    </a>
                </div>

                <%----------------  LIST Value  -------------------%>
                <div ng-if="attribute.attributeDef.dataType == 'LIST' && attribute.attributeDef.listMultiple == false">
                    <%-- <div>
                         <a title="{{clickToChangeValue}}" editable-select="attribute.value.listValue"
                            href="" e-style="width:200px;"
                            e-ng-options="value for value in attribute.attributeDef.lov.values track by value"
                            onaftersave="editEntryVm.saveProperties(attribute)">
                             {{attribute.value.listValue || 'CLICK_TO_SET_VALUE' | translate}}
                         </a>
                         <i class="fa fa-times-circle"
                            ng-if="attribute.value.listValue != null && attribute.editMode == false"
                            ng-hide="attribute.editMode == true"
                            title="{{'REMOVE_ATTRIBUTE_VALUE' | translate}}"
                            ng-click="editEntryVm.deleteAttribute(attribute)"
                            style="margin-left: 5px;font-size: 14px;cursor:pointer;"></i>
                     </div>--%>
                    <div>
                        <ui-select ng-show="attribute.editMode == true" ng-model="attribute.value.listValue"
                                   theme="bootstrap" style="width:70%">
                            <ui-select-match placeholder="Select">{{$select.selected}}</ui-select-match>
                            <ui-select-choices
                                    repeat="value in attribute.attributeDef.lov.values">
                                <div ng-bind="value"></div>
                            </ui-select-choices>
                        </ui-select>
                    </div>
                    <div style="margin-left: 287px;margin-top: -24px">
                        <i class="fa fa-check-circle" style="padding: 4px;cursor: pointer"
                           ng-show="attribute.editMode == true"
                           title="{{saveAttributeTitle}}"
                           ng-click="editEntryVm.saveProperties(attribute)">
                        </i>
                        <i class="fa fa-times-circle" style="cursor: pointer"
                           ng-show="attribute.editMode == true"
                           title="{{cancelChangesTitle}}"
                           ng-click="editEntryVm.cancelListValue(attribute)">
                        </i>
                    </div>

                    <div style="margin-top: 23px">
                        <a href="" ng-if="attribute.editMode == false" ng-click="editEntryVm.editList(attribute)">
                            {{attribute.value.listValue}}
                        </a>
                        <i style="padding: 6px;color: #337ab7" class="fa fa-pencil row-edit-btn"
                           ng-click="changeAttribute(attribute)"
                           ng-if="attribute.editMode == false"
                           title="{{'CLICK_TO_SET_VALUE' | translate}}"></i>
                        <i class="fa fa-times-circle"
                           ng-if="attribute.value.listValue != null && attribute.editMode == false"
                           ng-hide="attribute.editMode == true"
                           title="{{'REMOVE_ATTRIBUTE_VALUE' | translate}}"
                           ng-click="editEntryVm.deleteAttribute(attribute)"
                           style="margin-left: 5px;font-size: 14px;cursor:pointer;"></i>
                    </div>
                </div>

                <div ng-if="attribute.attributeDef.dataType == 'LIST' && attribute.attributeDef.listMultiple == true">
                    <div ng-if="attribute.value.mlistValue.length > 0" ng-hide="attribute.editMode == true"
                         ng-repeat="listValue in attribute.value.mlistValue">
                        <ul>
                            <li>{{listValue}}</li>
                        </ul>
                    </div>
                    <a ng-if="attribute.editMode == false && attribute.value.mlistValue.length == 0"
                       href="" ng-click="editEntryVm.editListValue(attribute)">
                        <i class="fa fa-pencil row-edit-btn" title="{{'CLICK_TO_SET_VALUE' | translate}}"></i>
                    </a>

                    <a ng-if="attribute.editMode == false && attribute.value.mlistValue.length > 0"
                       href="" ng-click="editEntryVm.editListValue(attribute)">
                        <i class="fa fa-pencil row-edit-btn" title="{{'CLICK_TO_EDIT_VALUE' | translate}}"></i>
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
                            <i class="fa fa-check-circle"
                               ng-if="attribute.attributeDef.dataType == 'LIST'"
                               ng-click="editEntryVm.saveProperties(attribute)"
                               title="{{cancelChangesTitle}}"
                               style="padding: 8px;cursor:pointer;"></i>
                        </div>
                        <div style="width: 25px;">
                            <i class="fa fa-times-circle"
                               ng-if="attribute.attributeDef.dataType == 'LIST'"
                               ng-click="editEntryVm.listCancelChanges(attribute)" title="{{cancelChangesTitle}}"
                               style="padding: 8px;cursor:pointer;"></i>
                        </div>
                    </div>

                </div>


                <div style="width: 284px;" ng-if="attribute.editMode == true &&
                                    (attribute.attributeDef.dataType == 'TEXT' || attribute.attributeDef.dataType == 'LONGTEXT' || attribute.attributeDef.dataType == 'INTEGER' ||
                                     attribute.attributeDef.dataType == 'DOUBLE' || attribute.attributeDef.dataType == 'BOOLEAN' || attribute.attributeDef.dataType == 'HYPERLINK' ||
                                     attribute.attributeDef.dataType == 'DATE' || attribute.attributeDef.dataType == 'CURRENCY')">

                    <input type="text" class="form-control" name="title"
                           ng-if="attribute.attributeDef.dataType == 'TEXT'"
                           placeholder="{{'ENTER_VALUE' | translate}}"
                           ng-model="attribute.value.stringValue" style="width:200px;">
                    <textarea type="text" class="form-control" name="title"
                              ng-if="attribute.attributeDef.dataType == 'LONGTEXT'"
                              placeholder="{{'ENTER_VALUE' | translate}}"
                              ng-model="attribute.value.longTextValue" style="width:200px;"></textarea>
                    <input type="number" class="form-control" name="title"
                           placeholder="{{'ENTER_NUMBER' | translate}}"
                           ng-if="attribute.attributeDef.dataType == 'INTEGER'"
                           ng-model="attribute.value.integerValue" style="width:200px;">
                    <input type="number" class="form-control" name="title"
                           ng-if="attribute.attributeDef.dataType == 'DOUBLE'"
                           placeholder="{{'ENTER_NUMBER' | translate}}"
                           ng-model="attribute.value.doubleValue" style="width:200px;">
                    <input type="text" class="form-control" name="title"
                           ng-if="attribute.attributeDef.dataType == 'HYPERLINK'"
                           placeholder="{{'ENTER_VALUE' | translate}}"
                           ng-model="attribute.value.hyperLinkValue" style="width:250px;">
                    <label class="radio-inline" style="width:74px;"
                           ng-if="attribute.attributeDef.dataType == 'BOOLEAN'">
                        <input type="checkbox"
                               ng-click="editEntryVm.saveProperties(attribute)"
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
                    <div ng-if="attribute.editMode == true && attribute.attributeDef.dataType == 'CURRENCY'"
                         ng-bind-html="attribute.value.encodedCurrencyType">
                    </div>
                    <input type="text" class="form-control" name="title"
                           ng-if="attribute.attributeDef.dataType == 'CURRENCY'"
                           ng-model="attribute.value.currencyValue"
                           style="width:185px;margin-top:-25px;margin-left:15px;">
                    <button class="btn btn-sm btn-primary" type="button" title="{{saveAttributeTitle}}"
                            ng-if="attribute.attributeDef.dataType != 'BOOLEAN' && attribute.attributeDef.dataType != 'HYPERLINK'"
                            style="margin-top:-65px;margin-left:205px;padding: 8px;width:33px;"
                            ng-click="editEntryVm.saveProperties(attribute)"><i class="fa fa-check"></i>
                    </button>

                    <button class="btn btn-sm btn-default" type="button" title="{{cancelChangesTitle}}"
                            ng-if="attribute.attributeDef.dataType != 'BOOLEAN' && attribute.attributeDef.dataType != 'HYPERLINK'"
                            style="margin-top:-65px;padding: 8px;width:33px;"
                            ng-click="cancelChanges(attribute)"><i class="fa fa-times"></i>
                    </button>
                    <button class="btn btn-sm btn-primary" type="button" title="{{saveAttributeTitle}}"
                            ng-if="attribute.attributeDef.dataType == 'HYPERLINK'"
                            style="margin-top:-65px;margin-left:253px;padding: 8px;width:33px;"
                            ng-click="editEntryVm.saveProperties(attribute)"><i class="fa fa-check"></i>
                    </button>

                    <button class="btn btn-sm btn-default" type="button" title="{{cancelChangesTitle}}"
                            ng-if="attribute.attributeDef.dataType == 'HYPERLINK'"
                            style="margin-top:-107px;padding: 8px;width:33px; margin-left: 288px;"
                            ng-click="cancelChanges(attribute)"><i class="fa fa-times"></i>
                    </button>
                </div>

                <%----------------  TIMESTAMP Property  -------------------%>

                <div ng-if="attribute.attributeDef.dataType == 'TIMESTAMP'">
                    <a href=""
                       ng-show="attribute.showTimestamp == false && attribute.value.timestampValue != null"
                       ng-click="editEntryVm.changeTimestamp(attribute)">
                        {{attribute.value.timestampValue}}
                        <i class="fa fa-pencil row-edit-btn" title="{{changeTime}}"></i>
                    </a>
                    <a href=""
                       ng-show="attribute.showTimestamp == false && attribute.value.timestampValue == null"
                       ng-click="editEntryVm.changeTimestamp(attribute)">
                        <i class="fa fa-pencil row-edit-btn" title="{{'CLICK_TO_SET_TIME' | translate}}"></i>
                    </a>

                    <div ng-if="attribute.showTimestamp == true">
                        <div>{{attribute.value.timestampValue}}</div>
                        <div uib-timepicker
                             ng-model="attribute.timestampValue">
                        </div>
                        <button class="btn btn-xs btn-primary" type="button"
                                style="margin-top:-188px;margin-left:158px;"
                                ng-click="editEntryVm.saveTimeProperty(attribute)"><i
                                class="fa fa-check"></i>
                        </button>

                        <button class="btn btn-xs btn-default" type="button" style="margin-top:-188px;"
                                ng-click="editEntryVm.cancelTime(attribute)"><i class="fa fa-times"></i>
                        </button>
                    </div>
                </div>

                <%----------------  TIME Property  -------------------%>

                <div ng-if="attribute.attributeDef.dataType == 'TIME'">
                    <a ng-show="attribute.showTimeAttribute == false && attribute.value.timeValue != null"
                       href="" title="{{changeTime}}"
                       ng-click="editEntryVm.changeTime(attribute)">
                        {{attribute.value.timeValue}}
                        <i class="fa fa-pencil row-edit-btn" title="{{changeTime}}"></i>
                    </a>
                    <a ng-show="attribute.showTimeAttribute == false && attribute.value.timeValue == null"
                       href="" ng-click="editEntryVm.changeTime(attribute)">
                        <i class="fa fa-pencil row-edit-btn" title="{{'CLICK_TO_SET_TIME' | translate}}"></i>
                    </a>

                    <div ng-if="attribute.showTimeAttribute == true">
                        <input class="form-control" type="text" ng-model="attribute.timeValue" time-picker
                               style="width: 240px;">

                        <button class="btn btn-xs btn-primary" type="button"
                                style="margin-top:-66px;margin-left:253px;"
                                ng-click="editEntryVm.saveTimeProperty(attribute)"><i class="fa fa-check"></i>
                        </button>

                        <button class="btn btn-xs btn-default" type="button" style="margin-top:-65px;"
                                ng-click="editEntryVm.cancelTime(attribute)"><i class="fa fa-times"></i>
                        </button>
                    </div>
                </div>


                <%----------------  IMAGE Property  -------------------%>

                <div ng-if="attribute.attributeDef.dataType == 'IMAGE'">
                    <a href="" title="{{Clicktoshowlargeimage}}"
                       ng-click="editEntryVm.showImageProperty(attribute)">
                        <img ng-if="attribute.value.imageValue != null"
                             ng-src="{{attribute.value.mfrImagePath}}"
                             style="height: 100px;width: 100px;">
                    </a><br ng-if="attribute.value.imageValue != null">

                    <div id="myModal" class="img-model modal">
                        <span class="closeimage">&times;</span>
                        <img class="modal-content" id="img01">
                    </div>

                    <div>
                        <a href="" ng-if="attribute.value.imageValue == null"
                           ng-click="editEntryVm.change(attribute)"
                           ng-hide="attribute.changeImage == true"
                           title="{{addImage}}">
                            <i class="fa fa-pencil row-edit-btn" title="{{'ADD_IMAGE' | translate}}"></i>
                        </a>
                        <i ng-if="attribute.value.imageValue != null" class="fa fa-pencil row-edit-btn"
                           ng-click="editEntryVm.change(attribute)"
                           style="padding-left: 5px;color: #337ab7"
                           ng-hide="attribute.changeImage == true"
                           title="{{changeImageMessage}}">
                        </i>
                        <input ng-show="attribute.changeImage == true" class="browse-control" name="file"
                               type="file" ng-file-model="attribute.newImageValue" style="width: 250px;">
                        <button class="btn btn-xs btn-primary"
                                ng-click="editEntryVm.saveImage(attribute)"
                                ng-show="attribute.changeImage == true" title="save image"
                                style="margin-top:-62px;margin-left: 255px;padding-top:8px;">
                            <i class="fa fa-check"></i>
                        </button>
                        <button class="btn btn-xs btn-default"
                                ng-click="editEntryVm.cancel(attribute)"
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
                                <a href="" ng-click="editEntryVm.openPropertyAttachment(attachment)"
                                   title="{{'CLICK_TO_DOWNLOAD_ATTACHMENT' | translate}}"
                                   style="margin-bottom: 5px;width:250px;color: #002451;">
                                    {{attachment.name}}
                                </a>
                                <a href="" title="{{deleteAttachment}}"
                                   ng-click="editEntryVm.deleteAttachments(attribute,attachment)">
                                    <i class="fa fa-times"
                                       style="margin-left: 5px;font-size: 14px;color: darkred"></i>
                                </a>
                            </li>
                        </ul>
                    </div>

                    <div>
                        <a href="">
                            <div ng-show="attribute.showAttachment == false"
                                 ng-click="editEntryVm.addAttachment(attribute)"
                                 title="{{addAttachments}}">
                                <i class="fa fa-pencil row-edit-btn" title="{{'ADD_ATTACHMENTS' | translate}}"></i>
                            </div>
                        </a>

                        <div ng-show="attribute.showAttachment == true">
                            <input class="browse-control" name="file"
                                   multiple="true"
                                   type="file" ng-file-model="attribute.attachmentValues"
                                   style="width: 250px;"/>
                            <button class="btn btn-xs btn-primary" title="{{saveAttachmentsTitle}}"
                                    ng-click="editEntryVm.saveAttachments(attribute)"
                                    style="margin-top:-62px;margin-left: 255px;padding-top:8px;">
                                <i class="fa fa-check"></i>
                            </button>
                            <button class="btn btn-xs btn-default" title="{{cancelChangesTitle}}"
                                    ng-click="editEntryVm.cancelAttachment(attribute)"
                                    style="margin-top:-62px;padding-top:8px;">
                                <i class="fa fa-times"></i>
                            </button>
                            <p ng-repeat="file in attribute.attachmentValues">
                                {{file.name}}
                            </p>
                        </div>
                    </div>
                </div>

                <%------  Currency Property  ------%>

                <div ng-if="attribute.attributeDef.dataType == 'CURRENCY'">
                    <div ng-if="attribute.value.encodedCurrencyType != null">
                    <span ng-if="attribute.editMode == false"
                          ng-bind-html="attribute.value.encodedCurrencyType">
                    </span>
                        <a href="" ng-if="attribute.editMode == false && attribute.value.currencyValue != null"
                           title="{{changeCurrencyValueTitle}}"
                           ng-click="changeAttribute(attribute)">
                            {{attribute.value.currencyValue}}
                            <i class="fa fa-pencil row-edit-btn" title="{{changeCurrencyValueTitle}}"></i>
                        </a>
                        <a href="" ng-if="attribute.editMode == false && attribute.value.currencyValue == null"
                           ng-click="changeAttribute(attribute)">
                            <i class="fa fa-pencil row-edit-btn" title="{{setCurrencyValue}}"></i>
                        </a>
                    </div>

                    <%----------  For newly added 'CURRENCY' attribute after ECO Creation ---------------%>

                    <div ng-if="attribute.value.encodedCurrencyType == null || attribute.value.currencyType == null">
                        <div ng-if="attribute.changeCurrency == true" style="width: 150px;">
                            <select class="form-control" ng-model="attribute.value.currencyType"
                                    ng-init="attribute.value.currencyType = editEntryVm.currencies[0].id"
                                    ng-options="currency.id as currency.name for currency in editEntryVm.currencies">
                            </select>
                        </div>
                        <a href="" ng-if="attribute.changeCurrency == false && attribute.value.currencyValue != null"
                           ng-click="editEntryVm.changeCurrencyValue(attribute)"
                           title="{{changeCurrencyValueTitle}}">
                            {{attribute.value.currencyValue}}
                            <i class="fa fa-pencil row-edit-btn" title="{{changeCurrencyValueTitle}}"></i>
                        </a>
                        <a href="" ng-if="attribute.changeCurrency == false && attribute.value.currencyValue == null"
                           ng-click="editEntryVm.changeCurrencyValue(attribute)">
                            <i class="fa fa-pencil row-edit-btn" title="{{setCurrencyValue}}"></i>
                        </a>

                        <div ng-if="attribute.changeCurrency == true"
                             style="width: 100px;margin-top: -40px;margin-left: 155px;">
                            <input class="form-control" name="currencyValue" type="number"
                                   placeholder="{{'ENTER_VALUE' | translate}}"
                                   ng-model="attribute.value.currencyValue"/>
                        </div>
                        <button ng-if="attribute.changeCurrency == true" class="btn btn-xs btn-primary"
                                title="{{saveCurrencyTitle}}"
                                ng-click="editEntryVm.saveProperties(attribute)"
                                style="margin-top:-64px;margin-left: 260px;padding-top:10px;">
                            <i class="fa fa-check"></i>
                        </button>
                        <button ng-if="attribute.changeCurrency == true" class="btn btn-xs btn-default"
                                title="{{cancelChangesTitle}}"
                                ng-click="cancelChanges(attribute)"
                                style="margin-top:-64px;padding-top:10px;">
                            <i class="fa fa-times"></i>
                        </button>
                    </div>
                </div>

                <%------------------------  OBJECT Property  -------------------------%>

                <div class="row" ng-if="attribute.attributeDef.dataType == 'OBJECT'">

                    <a ng-if="attribute.value.refValue.objectType == 'ITEM'" href=""
                       ng-click="editEntryVm.showRefValueDetails(attribute)"
                       title="{{'CLICK_TO_SHOW_DETAILS' | translate}}">
                        {{attribute.value.refValue.itemNumber}}
                    </a>

                    <a ng-if="attribute.value.refValue.objectType == 'ITEMREVISION'" href=""
                       ng-click="editEntryVm.showRefValueDetails(attribute)"
                       title="{{'CLICK_TO_SHOW_DETAILS' | translate}}">
                        {{attribute.value.refValue.itemMaster+" "+attribute.value.refValue.revision+"
                        "+attribute.value.refValue.lifeCyclePhase.phase}}
                    </a>

                    <a ng-if="attribute.value.refValue.objectType == 'CHANGE'" href=""
                       ng-click="editEntryVm.showRefValueDetails(attribute)"
                       title="{{'CLICK_TO_SHOW_DETAILS' | translate}}">
                        {{attribute.value.refValue.ecoNumber}}
                    </a>

                    <a ng-if="attribute.value.refValue.objectType == 'PLMWORKFLOWDEFINITION'" href=""
                       ng-click="editEntryVm.showRefValueDetails(attribute)"
                       title="{{'CLICK_TO_SHOW_DETAILS' | translate}}">
                        {{attribute.value.refValue.name}}
                    </a>

                    <a ng-if="attribute.value.refValue.objectType == 'MANUFACTURER'" href=""
                       ng-click="editEntryVm.showRefValueDetails(attribute)"
                       title="{{'CLICK_TO_SHOW_DETAILS' | translate}}">
                        {{attribute.value.refValue.name}}
                    </a>

                    <a ng-if="attribute.value.refValue.objectType == 'PROJECT'" href=""
                       ng-click="editEntryVm.showRefValueDetails(attribute)"
                       title="{{'CLICK_TO_SHOW_DETAILS' | translate}}">
                        {{attribute.value.refValue.name}}
                    </a>

                    <a ng-if="attribute.value.refValue.objectType == 'MANUFACTURERPART'" href=""
                       ng-click="editEntryVm.showRefValueDetails(attribute)"
                       title="{{'CLICK_TO_SHOW_DETAILS' | translate}}">
                        {{attribute.value.refValue.partName}}
                    </a>
                    <a href=""
                       ng-if="attribute.value.refValue == null && attribute.editMode == false"
                       title="{{addAttribute}}"
                       ng-click="showSelectionDialog(attribute.attributeDef.refType, attribute)">
                        <i class="fa fa-pencil row-edit-btn" title="{{'ADD_ATTRIBUTE' | translate}}"></i>
                    </a>
                    <i ng-if="attribute.value.refValue != null && attribute.editMode == false"
                       class="fa fa-pencil row-edit-btn"
                       title="{{changeAttributeTitle}}" style="margin-left:5px; color: #337ab7"
                       ng-click="showSelectionDialog(attribute.attributeDef.refType, attribute)">
                    </i>
                    <button ng-if="attribute.editMode == true" class="btn btn-xs btn-primary"
                            title="{{saveAttributeTitle}}" style="margin-left: 5px;"
                            ng-click="editEntryVm.saveObject(attribute)">
                        <i class="fa fa-check"></i>
                    </button>

                    <button ng-if="attribute.editMode == true" class="btn btn-xs btn-default"
                            title="{{cancelChangesTitle}}"
                            ng-click="cancelChanges(attribute)">
                        <i class="fa fa-times"></i>
                    </button>
                </div>

            </div>
        </div>
    </div>
</div>
