<div>
<style scoped>
    .item-details > div.row.master-att:last-child,
    .item-details > div.row.revision-att:last-child {
        border-bottom: 0 !important;
    }

    /* The Close Button */
    .img-model .closeimage {
        position: absolute;
        top: 50px;
        right: 50px;

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
<div style="position: relative;">
    <div style="overflow-y: auto;padding: 10px;overflow-x: hidden;">
        <form class="form-horizontal">
            <div ng-repeat="glossaryEntryDetail in versionEntryDetailsVm.versionEntry.glossaryEntryDetails">
                <h3>{{glossaryEntryDetail.language.language}}</h3>

                <div class="form-group">
                    <label class="col-sm-3 control-label" style="text-align: left;margin-left: 5%;">
                        <span translate>NAME</span>
                        <span class="asterisk">*</span> : </label>
                </div>
                <div class="form-group">
                    <div class="col-sm-11" style="margin-left: 5%;">
                        <input type="text" class="form-control" name="title"
                               ng-model="glossaryEntryDetail.name">
                    </div>
                </div>
                <div class="form-group" style="margin-bottom: 0px;">
                    <label class="col-sm-3 control-label" style="text-align: left;margin-left: 5%;">
                        <span translate>DESCRIPTION</span>
                        <span class="asterisk">*</span> : </label>
                </div>
                <div class="form-group">

                    <div class="col-sm-11" style="margin-left: 5%;">
                            <textarea name="description" rows="3" class="form-control" style="resize: none"
                                      ng-model="glossaryEntryDetail.description">
                            </textarea>
                    </div>
                </div>

                <div class="form-group" style="margin-bottom: 0px;">
                    <label class="col-sm-3 control-label" style="text-align: left;margin-left: 5%;">
                        <span translate>Notes</span>
                        <span class="asterisk">*</span> : </label>
                </div>
                <div class="form-group">
                    <div class="col-sm-11" style="margin-left: 5%;">
                            <textarea name="description" rows="3" class="form-control" style="resize: none"
                                      ng-model="glossaryEntryDetail.notes">
                            </textarea>
                    </div>
                </div>
            </div>
        </form>
    </div>
</div>
<hr>

<h4 ng-if="versionEntryDetailsVm.glossaryEntryProperties.length > 0"
    class="section-title" style="margin-left: 36px;" translate>ATTRIBUTES
</h4>

<div class="row row-eq-height" style="margin: 0;">
    <div class="item-details col-sm-12 col-sm-offset-1" style="padding: 10px;">
        <div class="row" ng-if="versionEntryDetailsVm.glossaryEntryProperties.length != 0"
             ng-repeat="attribute in versionEntryDetailsVm.glossaryEntryProperties">
            <div class="label col-xs-4 col-sm-4 text-right">
                <span>{{attribute.attributeDef.name}}: </span>
            </div>
            <div class="value col-xs-8 col-sm-8">

                <%----------------  String Value  -------------------%>

                <div ng-if="attribute.attributeDef.dataType == 'TEXT'">
                    <a ng-if="attribute.editMode == false && attribute.value.stringValue != null" href=""
                       ng-hide="selectGlossaryLifeCyclePhase == 'Released'"
                       ng-click="changeAttribute(attribute)" title="{{changeValue}}">
                        {{attribute.value.stringValue}}
                    </a>
                    <a ng-if="attribute.editMode == false && attribute.value.stringValue == null" href=""
                       ng-hide="selectGlossaryLifeCyclePhase == 'Released'"
                       ng-click="changeAttribute(attribute)">
                        {{'CLICK_TO_SET_VALUE' | translate}}
                    </a>
                    <span ng-if="selectGlossaryLifeCyclePhase == 'Released'">{{attribute.value.stringValue}}</span>
                </div>


                <%----------------  long Text Value  -------------------%>

                <div ng-if="attribute.attributeDef.dataType == 'LONGTEXT'">
                    <a ng-if="attribute.editMode == false && attribute.value.longTextValue != null" href=""
                       ng-hide="selectGlossaryLifeCyclePhase == 'Released'"
                       ng-click="changeAttribute(attribute)" title="{{changeValue}}">
                        {{attribute.value.longTextValue}}
                    </a>
                    <a ng-if="attribute.editMode == false && attribute.value.longTextValue == null" href=""
                       ng-hide="selectGlossaryLifeCyclePhase == 'Released'"
                       ng-click="changeAttribute(attribute)">
                        {{'CLICK_TO_SET_VALUE' | translate}}
                    </a>
                    <span ng-if="selectGlossaryLifeCyclePhase == 'Released'">{{attribute.value.longTextValue}}</span>
                </div>


                <%--------------- RichTextValue --------------%>

                <div ng-if="attribute.attributeDef.dataType == 'RICHTEXT'">
                <span>
                    <a ng-if="attribute.editMode == false && attribute.value.encodedRichTextValue == null"
                       ng-hide="selectGlossaryLifeCyclePhase == 'Released'"
                       href="" ng-click="versionEntryDetailsVm.editRichText(attribute)">
                        {{'CLICK_TO_SET_VALUE' | translate}}
                    </a>

                       <span ng-if="attribute.editMode == false && attribute.value.encodedRichTextValue != null"
                             ng-bind-html="attribute.value.encodedRichTextValue">
                       </span>
                   <span>
                        <a ng-if="attribute.editMode == false && attribute.value.encodedRichTextValue != null" href=""
                           ng-hide="selectGlossaryLifeCyclePhase == 'Released'"
                           ng-click="versionEntryDetailsVm.editRichText(attribute)">{{'CLICK_TO_EDIT_VALUE' |
                            translate}}
                        </a>
                    </span>

                </span>

                    <div ng-show="attribute.editMode == true">
                        <summernote ng-model="attribute.value.richTextValue"></summernote>
                    </div>

                    <button ng-show="attribute.editMode == true"
                            ng-click="versionEntryDetailsVm.saveRichText(attribute)"><i class="fa fa-check"></i>
                    </button>

                    <button ng-show="attribute.editMode == true"
                            ng-click="versionEntryDetailsVm.cancelRichText(attribute)"><i class="fa fa-times"></i>
                    </button>


                </div>

                <%----------------  Integer Value  -------------------%>


                <div id="intClick" ng-if="attribute.attributeDef.dataType == 'INTEGER'">
                    <a ng-if="attribute.editMode == false && attribute.value.integerValue != null"
                       ng-hide="selectGlossaryLifeCyclePhase == 'Released'"
                       href="" ng-click="changeAttribute(attribute)"
                       title="{{changeValue}}">
                        {{attribute.value.integerValue}}
                    </a>
                    <a ng-if="attribute.editMode == false && attribute.value.integerValue == null"
                       ng-hide="selectGlossaryLifeCyclePhase == 'Released'"
                       href="" ng-click="changeAttribute(attribute)">
                        {{'CLICK_TO_SET_VALUE' | translate}}
                    </a>
                    <span ng-if="selectGlossaryLifeCyclePhase == 'Released'"
                          ng-bind-html="attribute.value.integerValue"></span>
                </div>

                <%----------------  DOUBLE Value  -------------------%>

                <div ng-if="attribute.attributeDef.dataType == 'DOUBLE'">
                    <a ng-if="attribute.editMode == false && attribute.value.doubleValue != null" href=""
                       ng-hide="selectGlossaryLifeCyclePhase == 'Released'"
                       title="{{changeValue}}" ng-click="changeAttribute(attribute)">
                        {{attribute.value.doubleValue}}
                    </a>
                    <a ng-if="attribute.editMode == false && attribute.value.doubleValue == null" href=""
                       ng-hide="selectGlossaryLifeCyclePhase == 'Released'"
                       ng-click="changeAttribute(attribute)">
                        {{'CLICK_TO_SET_VALUE' | translate}}
                    </a>
                    <span ng-if="selectGlossaryLifeCyclePhase == 'Released'"
                          ng-bind-html="attribute.value.doubleValue"></span>
                </div>

                <%----------------  DATE Value  -------------------%>

                <div ng-if="attribute.attributeDef.dataType == 'DATE'">
                    <a href="" ng-if="attribute.value.dateValue != null && attribute.editMode == false"
                       ng-hide="selectGlossaryLifeCyclePhase == 'Released'"
                       title="{{changeDate}}" ng-click="changeAttribute(attribute)">
                        {{ (attribute.value.dateValue | date:"dd/MM/yyyy")}}
                    </a>
                    <a href="" ng-if="attribute.value.dateValue == null && attribute.editMode == false"
                       ng-hide="selectGlossaryLifeCyclePhase == 'Released'"
                       ng-click="changeAttribute(attribute)">
                        {{'CLICK_TO_SET_DATE' | translate}}
                    </a>
                    <span ng-if="selectGlossaryLifeCyclePhase == 'Released'"
                          ng-bind-html="attribute.value.dateValue"></span>
                </div>

                <%----------------  BOOLEAN Value  -------------------%>

                <div ng-if="attribute.attributeDef.dataType == 'BOOLEAN'">
                    <a href="" ng-if="attribute.editMode == false"
                       ng-hide="selectGlossaryLifeCyclePhase == 'Released'"
                       title="{{clickToChangeValue}}" ng-click="changeAttribute(attribute)">
                        {{attribute.value.booleanValue}}
                    </a>
                    <span ng-if="selectGlossaryLifeCyclePhase == 'Released'"
                          ng-bind-html="attribute.value.booleanValue"></span>
                </div>

                <%----------------  LIST Value  -------------------%>
                <div ng-if="attribute.attributeDef.dataType == 'LIST' && attribute.attributeDef.listMultiple == false">
                    <a title="{{clickToChangeValue}}" editable-select="attribute.value.listValue"
                       ng-hide="selectGlossaryLifeCyclePhase == 'Released'"
                       href="" e-style="width:200px;"
                       e-ng-options="value for value in attribute.attributeDef.lov.values track by value"
                       onaftersave="versionEntryDetailsVm.saveProperties(attribute)">
                        {{attribute.value.listValue || 'CLICK_TO_SET_VALUE' | translate}}
                    </a>
                    <span ng-if="selectGlossaryLifeCyclePhase == 'Released'"
                          ng-bind-html="attribute.value.listValue"></span>
                </div>


                <div ng-if="attribute.attributeDef.dataType == 'LIST' && attribute.attributeDef.listMultiple == true">
                    <div ng-if="attribute.value.mlistValue.length > 0" ng-hide="attribute.editMode == true"
                         ng-repeat="listValue in attribute.value.mlistValue">
                        <ul>
                            <li>{{listValue}}</li>
                        </ul>
                    </div>
                    <a ng-if="attribute.editMode == false && attribute.value.mlistValue.length == 0"
                       ng-hide="selectGlossaryLifeCyclePhase == 'Released'"
                       href="" ng-click="versionEntryDetailsVm.editListValue(attribute)">
                        {{'CLICK_TO_SET_VALUE' | translate}}
                    </a>

                    <a ng-if="attribute.editMode == false && attribute.value.mlistValue.length > 0"
                       ng-hide="selectGlossaryLifeCyclePhase == 'Released'"
                       href="" ng-click="versionEntryDetailsVm.editListValue(attribute)">
                        {{'CLICK_TO_EDIT_VALUE' | translate}}
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
                               ng-click="versionEntryDetailsVm.saveProperties(attribute)"
                               title="{{cancelChangesTitle}}"
                               style="padding: 8px;cursor:pointer;"></i>
                        </div>
                        <div style="width: 25px;">
                            <i class="fa fa-times-circle"
                               ng-if="attribute.attributeDef.dataType == 'LIST'"
                               ng-click="versionEntryDetailsVm.listCancelChanges(attribute)"
                               title="{{cancelChangesTitle}}"
                               style="padding: 8px;cursor:pointer;"></i>
                        </div>
                    </div>

                </div>


                <div ng-if="attribute.editMode == true &&
                                    (attribute.attributeDef.dataType == 'TEXT' || attribute.attributeDef.dataType == 'LONGTEXT' || attribute.attributeDef.dataType == 'INTEGER' ||
                                     attribute.attributeDef.dataType == 'DOUBLE' || attribute.attributeDef.dataType == 'BOOLEAN' ||
                                     attribute.attributeDef.dataType == 'DATE' || attribute.attributeDef.dataType == 'CURRENCY')">

                    <input type="text" class="form-control" name="title"
                           ng-if="attribute.attributeDef.dataType == 'TEXT'"
                           placeholder="{{'ENTER_VALUE' | translate}}"
                           ng-model="attribute.value.stringValue" style="width:200px;">
                    <textarea type="text" class="form-control" name="title"
                              ng-if="attribute.attributeDef.dataType == 'LONGTEXT'"
                              placeholder="{{'ENTER_VALUE' | translate}}"
                              ng-model="attribute.value.longTextValue" style="width:250px;"></textarea>
                    <input type="number" class="form-control" name="title"
                           placeholder="{{'ENTER_NUMBER' | translate}}"
                           ng-if="attribute.attributeDef.dataType == 'INTEGER'"
                           ng-model="attribute.value.integerValue" style="width:200px;">
                    <input type="number" class="form-control" name="title"
                           ng-if="attribute.attributeDef.dataType == 'DOUBLE'"
                           placeholder="{{'ENTER_NUMBER' | translate}}"
                           ng-model="attribute.value.doubleValue" style="width:200px;">
                    <label class="radio-inline" ng-repeat="choice in [true, false]"
                           ng-if="attribute.attributeDef.dataType == 'BOOLEAN'">
                        <input type="radio" name="change_{{attribute.attributeDef.id}}" ng-value="choice"
                               ng-model="attribute.value.booleanValue">{{choice}}
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
                    <button class="btn btn-sm btn-primary" ng-if="attribute.attributeDef.dataType != 'LONGTEXT'"
                            type="button" title="{{saveAttributeTitle}}"
                            style="margin-top:-65px;margin-left:205px;"
                            ng-click="versionEntryDetailsVm.saveProperties(attribute)"><i class="fa fa-check"></i>
                    </button>

                    <button class="btn btn-sm btn-default" ng-if="attribute.attributeDef.dataType != 'LONGTEXT'"
                            type="button" title="{{cancelChangesTitle}}"
                            style="margin-top:-65px;"
                            ng-click="cancelChanges(attribute)"><i class="fa fa-times"></i>
                    </button>
                    <button class="btn btn-sm btn-primary" ng-if="attribute.attributeDef.dataType == 'LONGTEXT'"
                            type="button" title="{{saveAttributeTitle}}"
                            style="margin-top:-65px;margin-left:253px;"
                            ng-click="versionEntryDetailsVm.saveProperties(attribute)"><i class="fa fa-check"></i>
                    </button>

                    <button class="btn btn-sm btn-default" ng-if="attribute.attributeDef.dataType == 'LONGTEXT'"
                            type="button" title="{{cancelChangesTitle}}"
                            style="margin-top:-65px;margin-left: -2px;"
                            ng-click="cancelChanges(attribute)"><i class="fa fa-times"></i>
                    </button>
                </div>

                <%----------------  TIMESTAMP Property  -------------------%>

                <div ng-if="attribute.attributeDef.dataType == 'TIMESTAMP'">
                    <a href="" title="{{changeTime}}"
                       ng-hide="selectGlossaryLifeCyclePhase == 'Released'"
                       ng-show="attribute.showTimestamp == false && attribute.value.timestampValue != null"
                       ng-click="versionEntryDetailsVm.changeTimestamp(attribute)">
                        {{attribute.value.timestampValue}}
                    </a>
                    <a href=""
                       ng-show="attribute.showTimestamp == false && attribute.value.timestampValue == null"
                       ng-hide="selectGlossaryLifeCyclePhase == 'Released'"
                       ng-click="versionEntryDetailsVm.changeTimestamp(attribute)">
                        {{'CLICK_TO_SET_TIME' | translate}}
                    </a>
                    <span ng-if="selectGlossaryLifeCyclePhase == 'Released'"
                          ng-bind-html="attribute.value.timestampValue"></span>

                    <div ng-if="attribute.showTimestamp == true">
                        <div uib-timepicker
                             ng-model="attribute.timestampValue">
                        </div>
                        <button class="btn btn-xs btn-primary" type="button"
                                style="margin-top:-188px;margin-left:158px;"
                                ng-click="versionEntryDetailsVm.saveTimeProperty(attribute)"><i
                                class="fa fa-check"></i>
                        </button>

                        <button class="btn btn-xs btn-default" type="button" style="margin-top:-188px;"
                                ng-click="versionEntryDetailsVm.cancelTime(attribute)"><i class="fa fa-times"></i>
                        </button>
                    </div>
                </div>

                <%----------------  TIME Property  -------------------%>

                <div ng-if="attribute.attributeDef.dataType == 'TIME'">
                    <a ng-show="attribute.showTimeAttribute == false && attribute.value.timeValue != null"
                       ng-hide="selectGlossaryLifeCyclePhase == 'Released'"
                       href="" title="{{changeTime}}"
                       ng-click="versionEntryDetailsVm.changeTime(attribute)">
                        {{attribute.value.timeValue}}
                    </a>
                    <a ng-show="attribute.showTimeAttribute == false && attribute.value.timeValue == null"
                       ng-hide="selectGlossaryLifeCyclePhase == 'Released'"
                       href="" ng-click="versionEntryDetailsVm.changeTime(attribute)">
                        {{'CLICK_TO_SET_TIME' | translate}}
                    </a>
                    <span ng-if="selectGlossaryLifeCyclePhase == 'Released'"
                          ng-bind-html="attribute.value.timeValue"></span>

                    <div ng-if="attribute.showTimeAttribute == true">
                        <div uib-timepicker
                             ng-model="attribute.timeValue">
                        </div>
                        <button class="btn btn-xs btn-primary" type="button"
                                style="margin-top:-188px;margin-left:158px;"
                                ng-click="versionEntryDetailsVm.saveTimeProperty(attribute)"><i class="fa fa-check"></i>
                        </button>

                        <button class="btn btn-xs btn-default" type="button" style="margin-top:-188px;"
                                ng-click="versionEntryDetailsVm.cancelTime(attribute)"><i class="fa fa-times"></i>
                        </button>
                    </div>
                </div>


                <%----------------  IMAGE Property  -------------------%>

                <div ng-if="attribute.attributeDef.dataType == 'IMAGE'">
                    <a href="" title="{{Clicktoshowlargeimage}}"
                       ng-click="versionEntryDetailsVm.showImageProperty(attribute)">
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
                           ng-click="versionEntryDetailsVm.change(attribute)"
                           ng-hide="attribute.changeImage == true || selectGlossaryLifeCyclePhase == 'Released'"
                           title="{{addImage}}">
                            <span translate>ADD_IMAGE</span>
                        </a>
                        <button ng-if="attribute.value.imageValue != null" class="btn btn-sm btn-warning"
                                ng-click="versionEntryDetailsVm.change(attribute)"
                                ng-hide="attribute.changeImage == true"
                                title="{{changeImageMessage}}"><i
                                class="fa fa-edit"></i>
                        </button>
                        <input ng-show="attribute.changeImage == true" class="browse-control" name="file"
                               type="file" ng-file-model="attribute.newImageValue" style="width: 250px;">
                        <button class="btn btn-xs btn-primary"
                                ng-click="versionEntryDetailsVm.saveImage(attribute)"
                                ng-show="attribute.changeImage == true" title="save image"
                                style="margin-top:-62px;margin-left: 255px;padding-top:8px;">
                            <i class="fa fa-check"></i>
                        </button>
                        <button class="btn btn-xs btn-default"
                                ng-click="versionEntryDetailsVm.cancel(attribute)"
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
                                <a href="" ng-click="versionEntryDetailsVm.openPropertyAttachment(attachment)"
                                   title="{{'CLICK_TO_DOWNLOAD_ATTACHMENT' | translate}}"
                                   style="margin-bottom: 5px;width:250px;color: #002451;">
                                    {{attachment.name}}
                                </a>
                                <a href="" title="{{deleteAttachment}}"
                                   ng-show="selectGlossaryLifeCyclePhase != 'Released'"
                                   ng-click="versionEntryDetailsVm.deleteAttachments(attribute,attachment)">
                                    <i class="fa fa-times"
                                       style="margin-left: 5px;font-size: 14px;color: darkred"></i>
                                </a>
                            </li>
                        </ul>
                    </div>

                    <div>
                        <a href="">
                            <div ng-show="attribute.showAttachment == false"
                                 ng-click="versionEntryDetailsVm.addAttachment(attribute)"
                                 ng-hide="selectGlossaryLifeCyclePhase == 'Released'"
                                 title="{{addAttachments}}">
                                <span translate>ADD_ATTACHMENTS</span>
                            </div>
                        </a>

                        <div ng-show="attribute.showAttachment == true">
                            <input class="browse-control" name="file"
                                   multiple="true"
                                   type="file" ng-file-model="attribute.attachmentValues"
                                   style="width: 250px;"/>
                            <button class="btn btn-xs btn-primary" title="{{saveAttachmentsTitle}}"
                                    ng-click="versionEntryDetailsVm.saveAttachments(attribute)"
                                    style="margin-top:-62px;margin-left: 255px;padding-top:8px;">
                                <i class="fa fa-check"></i>
                            </button>
                            <button class="btn btn-xs btn-default" title="{{cancelChangesTitle}}"
                                    ng-click="versionEntryDetailsVm.cancelAttachment(attribute)"
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
                    <div ng-if="attribute.value.encodedCurrencyType != null"
                         ng-hide="selectGlossaryLifeCyclePhase == 'Released'">
                    <span ng-if="attribute.editMode == false"
                          ng-bind-html="attribute.value.encodedCurrencyType">
                    </span>
                        <a href="" ng-if="attribute.editMode == false && attribute.value.currencyValue != null"
                           title="{{changeCurrencyValueTitle}}"
                           ng-click="changeAttribute(attribute)">
                            {{attribute.value.currencyValue}}
                        </a>
                        <a href="" ng-if="attribute.editMode == false && attribute.value.currencyValue == null"
                           ng-click="changeAttribute(attribute)">
                            {{setCurrencyValue}}
                        </a>

                        <div ng-if="selectGlossaryLifeCyclePhase == 'Released'">
                              <span ng-if="attribute.editMode == false"
                                    ng-bind-html="attribute.value.encodedCurrencyType">
                              </span>
                            <span ng-bind-html="attribute.value.currencyValue"></span>
                        </div>
                    </div>

                    <%----------  For newly added 'CURRENCY' attribute after ECO Creation ---------------%>

                    <div ng-if="attribute.value.encodedCurrencyType == null || attribute.value.currencyType == null">
                        <div ng-if="attribute.changeCurrency == true" style="width: 150px;">
                            <select class="form-control" ng-model="attribute.value.currencyType"
                                    ng-init="attribute.value.currencyType = versionEntryDetailsVm.currencies[0].id"
                                    ng-options="currency.id as currency.name for currency in versionEntryDetailsVm.currencies">
                            </select>
                        </div>
                        <a href="" ng-if="attribute.changeCurrency == false && attribute.value.currencyValue != null"
                           ng-click="versionEntryDetailsVm.changeCurrencyValue(attribute)"
                           title="{{changeCurrencyValueTitle}}">
                            {{attribute.value.currencyValue}}
                        </a>
                        <a href="" ng-if="attribute.changeCurrency == false && attribute.value.currencyValue == null"
                           ng-click="versionEntryDetailsVm.changeCurrencyValue(attribute)">
                            {{setCurrencyValue}}
                        </a>

                        <div ng-if="attribute.changeCurrency == true"
                             style="width: 100px;margin-top: -40px;margin-left: 155px;">
                            <input class="form-control" name="currencyValue" type="number"
                                   placeholder="{{'ENTER_VALUE' | translate}}"
                                   ng-model="attribute.value.currencyValue"/>
                        </div>
                        <button ng-if="attribute.changeCurrency == true" class="btn btn-xs btn-primary"
                                title="{{saveCurrencyTitle}}"
                                ng-click="versionEntryDetailsVm.saveProperties(attribute)"
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
                       ng-click="versionEntryDetailsVm.showRefValueDetails(attribute)"
                       title="{{'CLICK_TO_SHOW_DETAILS' | translate}}">
                        {{attribute.value.refValue.itemNumber}}
                    </a>

                    <a ng-if="attribute.value.refValue.objectType == 'ITEMREVISION'" href=""
                       ng-click="versionEntryDetailsVm.showRefValueDetails(attribute)"
                       title="{{'CLICK_TO_SHOW_DETAILS' | translate}}">
                        {{attribute.value.refValue.itemMaster+" "+attribute.value.refValue.revision+"
                        "+attribute.value.refValue.lifeCyclePhase.phase}}
                    </a>

                    <a ng-if="attribute.value.refValue.objectType == 'CHANGE'" href=""
                       ng-click="versionEntryDetailsVm.showRefValueDetails(attribute)"
                       title="{{'CLICK_TO_SHOW_DETAILS' | translate}}">
                        {{attribute.value.refValue.ecoNumber}}
                    </a>

                    <a ng-if="attribute.value.refValue.objectType == 'PLMWORKFLOWDEFINITION'" href=""
                       ng-click="versionEntryDetailsVm.showRefValueDetails(attribute)"
                       title="{{'CLICK_TO_SHOW_DETAILS' | translate}}">
                        {{attribute.value.refValue.name}}
                    </a>

                    <a ng-if="attribute.value.refValue.objectType == 'MANUFACTURER'" href=""
                       ng-click="versionEntryDetailsVm.showRefValueDetails(attribute)"
                       title="{{'CLICK_TO_SHOW_DETAILS' | translate}}">
                        {{attribute.value.refValue.name}}
                    </a>

                    <a ng-if="attribute.value.refValue.objectType == 'PROJECT'" href=""
                       ng-click="versionEntryDetailsVm.showRefValueDetails(attribute)"
                       title="{{'CLICK_TO_SHOW_DETAILS' | translate}}">
                        {{attribute.value.refValue.name}}
                    </a>

                    <a ng-if="attribute.value.refValue.objectType == 'MANUFACTURERPART'" href=""
                       ng-click="versionEntryDetailsVm.showRefValueDetails(attribute)"
                       title="{{'CLICK_TO_SHOW_DETAILS' | translate}}">
                        {{attribute.value.refValue.partName}}
                    </a>
                    <a href=""
                       ng-if="attribute.value.refValue == null && attribute.editMode == false"
                       ng-hide="selectGlossaryLifeCyclePhase == 'Released'"
                       title="{{addAttribute}}"
                       ng-click="versionEntryDetailsVm.showObjectValues(attribute)">
                        <span translate>ADD_ATTRIBUTE</span>
                    </a>
                    <button ng-if="attribute.value.refValue != null && attribute.editMode == false"
                            class="btn btn-sm btn-warning"
                            title="{{changeAttributeTitle}}" style="margin-left:5px;"
                            ng-click="versionEntryDetailsVm.showObjectValues(attribute)">
                        <i class="fa fa-edit"></i>
                    </button>
                    <button ng-if="attribute.editMode == true" class="btn btn-xs btn-primary"
                            title="{{saveAttributeTitle}}" style="margin-left: 5px;"
                            ng-click="versionEntryDetailsVm.saveObject(attribute)">
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
</div>
