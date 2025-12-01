<style scoped>
    .item-details > div.row.master-att:last-child,
    .item-details > div.row.revision-att:last-child {
        border-bottom: 0 !important;
    }

    /* The Close Button */
    .img-model .closeImage1 {
        position: absolute;
        top: 50px;
        right: 50px;
        color: black;
        font-size: 40px;
        font-weight: bold;
        transition: 0.3s;
    }

    .img-model .closeImage1:hover,
    .img-model .closeImage1:focus {
        color: #bbb;
        text-decoration: none;
        cursor: pointer;
    }

    .img-model .closeimage1 {
        position: absolute;
        top: 50px;
        right: 50px;
        color: black;
        font-size: 40px;
        font-weight: bold;
        transition: 0.3s;
    }

    .img-model .closeimage1:hover,
    .img-model .closeimage1:focus {
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
<div class="item-details" style="padding: 30px;margin-left: 0;">

    <%------------------------ Item Master Attributes  ---------------------%>

    <div>
        <h4 class="section-title"> Master Attributes</h4>
    </div>
    <div ng-if="itemAttributesVm.loading == true" style="padding: 30px;">

        <br/>
    <span style="font-size: 15px;">
        <img src="app/assets/bower_components/cassini-platform/images/loaders/loader6.gif" class="mr5">
        <span>Loading Attributes</span>
    </span>
        <br/>
    </div>
    <div ng-if="itemAttributesVm.attributes.length == 0 && itemAttributesVm.loading == false">
        <span>No Attributes</span>
    </div>
    <div class="row master-att" ng-if="itemAttributesVm.attributes.length != 0"
         ng-repeat="attribute in itemAttributesVm.attributes">
        <div class="label col-sm-2 text-right">
            <span>{{attribute.attributeDef.name}}: </span>
        </div>
        <div class="value col-sm-10">

            <%----------  String Attribute  --------%>

            <%------  String Attribute  ------%>

                <div ng-if="attribute.attributeDef.dataType == 'TEXT'">
                <span>
                    <a ng-if="attribute.editMode == false"
                       href="" ng-click="itemAttributesVm.changeAttribute(attribute)"
                       title="Click to change value">
                        {{attribute.value.stringValue || 'Click to set value'}}
                    </a>
                </span>
            </div>

            <%------  Integer Attribute  ------%>

            <div id="intClick" ng-if="attribute.attributeDef.dataType == 'INTEGER'">
                <a ng-if="attribute.editMode == false"
                   href="" ng-click="itemAttributesVm.changeAttribute(attribute)"
                   title="Click to change value">
                    {{attribute.value.integerValue || 'Click to set value'}}
                </a>
            </div>

            <%------  Double Attribute  ------%>

            <div ng-if="attribute.attributeDef.dataType == 'DOUBLE'">
                <a ng-if="attribute.editMode == false"
                   href="" ng-click="itemAttributesVm.changeAttribute(attribute)"
                   title="Click to change value">
                    {{attribute.value.doubleValue || 'Click to set value'}}
                </a>
            </div>

            <%------  Date Attribute  ------%>

            <div ng-if="attribute.attributeDef.dataType == 'DATE'">
                <a ng-if="attribute.editMode == false"
                   href="" ng-click="itemAttributesVm.changeAttribute(attribute)"
                   title="Click to change date">
                    {{ (attribute.value.dateValue | date:"dd/MM/yyyy") || 'Click to set value'}}
                </a>
            </div>

            <%------  Boolean Attribute  ------%>

            <div ng-if="attribute.attributeDef.dataType == 'BOOLEAN'">
                <a ng-if="attribute.editMode == false && attribute.value.booleanValue != null"
                   href="" ng-click="itemAttributesVm.changeAttribute(attribute)">
                    {{attribute.value.booleanValue || 'Click to set value'}}
                </a>
            </div>

            <div ng-if="attribute.editMode == true &&
                                    (attribute.attributeDef.dataType == 'TEXT' || attribute.attributeDef.dataType == 'INTEGER' ||
                                     attribute.attributeDef.dataType == 'DOUBLE' || attribute.attributeDef.dataType == 'BOOLEAN' ||
                                     attribute.attributeDef.dataType == 'DATE' || attribute.attributeDef.dataType == 'CURRENCY')">

                <input type="text" class="form-control" name="title"
                       ng-if="attribute.attributeDef.dataType == 'TEXT'"
                       placeholder="Enter value"
                       ng-model="attribute.value.stringValue" style="width:200px;">
                <input type="number" class="form-control" name="title"
                       placeholder="Enter value"
                       ng-if="attribute.attributeDef.dataType == 'INTEGER'"
                       ng-model="attribute.value.integerValue" style="width:200px;">
                <input type="text" class="form-control" name="title"
                       ng-if="attribute.attributeDef.dataType == 'DOUBLE'"
                       placeholder="Enter value"
                       ng-model="attribute.value.doubleValue" style="width:200px;">
                <label class="radio-inline" ng-repeat="choice in [true, false]"
                       ng-if="attribute.attributeDef.dataType == 'BOOLEAN'">
                    <input type="radio" name="change_{{attribute.attributeDef.id}}" ng-value="choice"
                           ng-model="attribute.value.booleanValue">{{choice}}
                </label>

                <div class="input-group" ng-if="attribute.attributeDef.dataType == 'DATE'"
                     style="width: 200px;">
                    <input type="text" class="form-control" date-picker
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

                <button ng-if="attribute.attributeDef.dataType == 'BOOLEAN'" class="btn btn-sm btn-primary"
                        type="button" title="Save"
                        style="padding: 8px;width:33px;"
                        ng-click="itemAttributesVm.saveItemAttribute(attribute)"><i class="fa fa-check"></i>
                </button>
                <button ng-if="attribute.attributeDef.dataType == 'BOOLEAN'" class="btn btn-sm btn-default"
                        type="button" title="Cancel"
                        style="padding: 8px;width:33px;"
                        ng-click="itemAttributesVm.cancelChanges(attribute)"><i class="fa fa-times"></i>
                </button>

                <button ng-if="attribute.attributeDef.dataType != 'BOOLEAN'" class="btn btn-sm btn-primary"
                        type="button" title="Save"
                        style="margin-top:-65px;margin-left:205px;padding: 8px;width:33px;"
                        ng-click="itemAttributesVm.saveItemAttribute(attribute)"><i class="fa fa-check"></i>
                </button>
                <button ng-if="attribute.attributeDef.dataType != 'BOOLEAN'" class="btn btn-sm btn-default"
                        type="button" title="Cancel"
                        style="margin-top:-65px;padding: 8px;width:33px;"
                        ng-click="itemAttributesVm.cancelChanges(attribute)"><i class="fa fa-times"></i>
                </button>
            </div>

            <%-------  List Attribute  ------%>

            <div ng-if="attribute.attributeDef.dataType == 'LIST'">
                <a title="Click to change value"
                   editable-select="attribute.value.listValue"
                   href="" e-style="width:200px;"
                   e-ng-options="value for value in attribute.attributeDef.lov.values track by value"
                   onaftersave="itemAttributesVm.saveItemAttribute(attribute)">
                    {{attribute.value.listValue || 'Click to set value'}}
                </a>
            </div>

            <%-----  Image Attribute  -----%>

            <div ng-if="attribute.attributeDef.dataType == 'IMAGE'">
                <a href=""
                   ng-click="itemAttributesVm.showImage(attribute)"
                   title="Click to show large image">
                    <img ng-if="attribute.value.imageValue != null"
                         ng-src="{{attribute.value.itemImagePath}}"
                         style="height: 100px;width: 100px;margin-bottom: 5px;">
                </a><br ng-if="attribute.value.imageValue != null">

                <%-- To show large Image--%>

                <div id="myModal2" class="img-model modal">
                    <span class="closeimage1">&times;</span>
                    <img class="modal-content" id="img03">
                </div>

                <div>
                    <a href=""
                       ng-if="attribute.value.imageValue == null"
                       ng-click="itemAttributesVm.change(attribute)"
                       ng-hide="attribute.changeImage == true"
                       title="Click to add image">Add Image
                    </a>
                    <button ng-if="attribute.value.imageValue != null"
                            class="btn btn-sm btn-warning"
                            ng-click="itemAttributesVm.change(attribute)"
                            ng-hide="attribute.changeImage == true"
                            title="Click to change image">
                        <i class="fa fa-edit"></i>
                    </button>
                    <input ng-show="attribute.changeImage == true" class="browse-control" name="file"
                           id="attributeImageFile"
                           type="file" ng-file-model="attribute.newImageValue" style="width: 250px;">
                    <button class="btn btn-sm btn-primary"
                            ng-click="itemAttributesVm.saveImage(attribute)"
                            ng-show="attribute.changeImage == true" title="Save Image"
                            style="margin-top:-62px;margin-left: 255px;padding-top:8px;">
                        <i class="fa fa-check"></i>
                    </button>
                    <button class="btn btn-sm btn-default"
                            ng-click="itemAttributesVm.cancel(attribute)"
                            ng-show="attribute.changeImage == true" title="Cancel"
                            style="margin-top:-62px;padding-top:8px;">
                        <i class="fa fa-times"></i>
                    </button>
                </div>
            </div>

            <%-----  Attachment Attribute  ------%>

            <div ng-if="attribute.attributeDef.dataType == 'ATTACHMENT'">
                <div ng-if="attribute.value.attachmentValues.length > 0"
                     ng-repeat="attachment in attribute.value.attachmentValues">
                    <ul style="margin-left:-30px;">
                        <li>
                            <a href="" ng-click="itemAttributesVm.openPropertyAttachment(attachment)"
                               title="Click to download attachment"
                               style="margin-bottom: 5px;width:250px;color: #002451;">
                                {{attachment.name}}
                            </a>
                            <a href=""
                               title="Delete Attachment"
                               ng-click="itemAttributesVm.deleteAttachments(attribute,attachment)">
                                <i class="fa fa-times"
                                   style="margin-left: 5px;font-size: 14px;color: darkred">
                                </i>
                            </a>
                        </li>
                    </ul>
                </div>

                <div>
                    <a href="">
                        <div ng-show="attribute.showAttachment == false" title="Add Atachments"
                             ng-click="itemAttributesVm.addAttachment(attribute)">
                            <span>Add Attachments</span>
                        </div>
                    </a>

                    <div ng-show="attribute.showAttachment == true">
                        <input class="browse-control" name="file"
                               multiple="true" id="itemAttachmentFile"
                               type="file" ng-file-model="attribute.attachmentValues"
                               style="width: 250px;"/>
                        <button class="btn btn-sm btn-primary" title="Save Atachments"
                                ng-click="itemAttributesVm.saveAttachments(attribute)"
                                style="margin-top:-62px;margin-left: 255px;padding-top:8px;">
                            <i class="fa fa-check"></i>
                        </button>
                        <button class="btn btn-sm btn-default" title="Cancel"
                                ng-click="itemAttributesVm.cancelAttachment(attribute)"
                                style="margin-top:-62px;padding-top:8px;">
                            <i class="fa fa-times"></i>
                        </button>
                        <p ng-repeat="file in attribute.attachmentValues">
                            {{file.name}}
                        </p>
                    </div>
                </div>
            </div>

            <%------  Time Attribute  ------%>

            <div ng-if="attribute.attributeDef.dataType == 'TIME'">
                <span>
                    <a ng-if="attribute.showTimeAttribute == false"
                       href="" ng-click="itemAttributesVm.changeTime(attribute)" title="Click to change">
                        {{attribute.value.timeValue || 'Click to set value'}}
                    </a>
                </span>

                <div ng-if="attribute.showTimeAttribute == true">
                    <div uib-timepicker
                         ng-model="attribute.timeValue">
                    </div>
                    <button class="btn btn-sm btn-primary" type="button" title="Save"
                            style="margin-top:-195px;margin-left:165px;"
                            ng-click="itemAttributesVm.saveTime(attribute)"><i class="fa fa-check"></i>
                    </button>

                    <button class="btn btn-sm btn-default" type="button" style="margin-top:-195px;"
                            title="Cancel"
                            ng-click="itemAttributesVm.cancelTime(attribute)"><i class="fa fa-times"></i>
                    </button>
                </div>
            </div>

            <%------  Timestamp Attribute  ------%>

            <div ng-if="attribute.attributeDef.dataType == 'TIMESTAMP'">
                <span>
                    <span>{{attribute.value.timestampValue}}</span>
                </span>

                <div ng-if="attribute.showTimestamp == true">
                    <div uib-timepicker
                         ng-model="attribute.timestampValue">
                    </div>
                    <button class="btn btn-sm btn-primary" type="button" title="Save"
                            style="margin-top:-195px;margin-left:165px;"
                            ng-click="itemAttributesVm.saveTime(attribute)"><i
                            class="fa fa-check"></i>
                    </button>

                    <button class="btn btn-sm btn-default" type="button" style="margin-top:-195px;"
                            title="Cancel"
                            ng-click="itemAttributesVm.cancelTime(attribute)"><i class="fa fa-times"></i>
                    </button>
                </div>
            </div>

            <%-----------------------------  Currency Attribute  ------------------------%>

            <div ng-if="attribute.attributeDef.dataType == 'CURRENCY'">
                <div ng-if="attribute.value.encodedCurrencyType != null">
                    <a ng-if="attribute.editMode == false"
                       href=""
                       ng-bind-html="attribute.value.encodedCurrencyType">
                    </a>
                    <span>
                        <a href=""
                           ng-if="attribute.editMode == false"
                           title="Click to change"
                           ng-click="itemAttributesVm.changeAttribute(attribute)">
                            {{attribute.value.currencyValue || 'Click to set value'}}
                        </a>
                    </span>
                </div>


                <%----------  For newly added 'CURRENCY' property after ITEM Creation ---------------%>

                <div ng-if="attribute.value.encodedCurrencyType == null || attribute.value.currencyType == null">
                    <div ng-if="attribute.changeCurrency == true" style="width: 150px;">
                        <select class="form-control" ng-model="attribute.value.currencyType"
                                ng-init="attribute.value.currencyType = itemAttributesVm.currencies[0].id"
                                ng-options="currency.id as currency.name for currency in itemAttributesVm.currencies">
                        </select>
                    </div>
                    <span>
                        <a href=""
                           ng-if="attribute.changeCurrency == false"
                           ng-click="itemAttributesVm.changeCurrencyValue(attribute)"
                           title="Click to change">
                            {{attribute.value.currencyValue || 'Click to set value'}}
                        </a>
                    </span>

                    <div ng-if="attribute.changeCurrency == true"
                         style="width: 100px;margin-top: -40px;margin-left: 155px;">
                        <input class="form-control" name="currencyValue" type="number"
                               placeholder="Enter value"
                               ng-model="attribute.value.currencyValue"/>
                    </div>
                    <button ng-if="attribute.changeCurrency == true" class="btn btn-sm btn-primary"
                            title="Save"
                            ng-click="itemAttributesVm.saveItemAttribute(attribute)"
                            style="margin-top:-64px;margin-left: 260px;padding-top:10px;">
                        <i class="fa fa-check"></i>
                    </button>
                    <button ng-if="attribute.changeCurrency == true" class="btn btn-sm btn-default"
                            title="Cancel"
                            ng-click="itemAttributesVm.cancelChanges(attribute)"
                            style="margin-top:-64px;padding-top:10px;">
                        <i class="fa fa-times"></i>
                    </button>
                </div>
            </div>
        </div>
    </div>
    <br>

    <%---------------------  Item Revision Attributes  -----------------------%>

    <div>
        <h4 class="section-title">Revision Attributes</h4>
    </div>
    <div ng-if="itemAttributesVm.loading == true" style="padding: 30px;">

        <br/>
    <span style="font-size: 15px;">
        <img src="app/assets/bower_components/cassini-platform/images/loaders/loader6.gif" class="mr5">
        <span>Loading Attributes...</span>
    </span>
        <br/>
    </div>
    <div ng-if="itemAttributesVm.revisionAttributes.length == 0 && itemAttributesVm.loading == false">
        <span>No Attributes</span>
    </div>
    <div class="row revision-att" ng-if="itemAttributesVm.revisionAttributes.length != 0"
         ng-disabled="external.external == true"
         ng-repeat="attribute in itemAttributesVm.revisionAttributes">
        <div class="label col-sm-2 text-right">
            <span>{{attribute.attributeDef.name}}: </span>
        </div>
        <div class="value col-sm-10">

            <%----------  String Attribute  --------%>

                <div ng-if="attribute.attributeDef.dataType == 'TEXT'">
                <span>
                    <a ng-if="attribute.editMode == false"
                       href="" ng-click="itemAttributesVm.changeAttribute(attribute)"
                       title="Click to change value">
                        {{attribute.value.stringValue || 'Click to set value'}}
                    </a>
                </span>
            </div>

            <%-----  Integer Attribute -----%>

            <div id="intClick" ng-if="attribute.attributeDef.dataType == 'INTEGER'">
                <span>
                    <a ng-if="attribute.editMode == false"
                       href="" ng-click="itemAttributesVm.changeAttribute(attribute)"
                       title="Click to change value">
                        {{attribute.value.integerValue || 'Click to set value'}}
                    </a>
                </span>
            </div>

            <%-----  Double Attribute -----%>

            <div ng-if="attribute.attributeDef.dataType == 'DOUBLE'">
                <span>
                    <a ng-if="attribute.editMode == false" href=""
                       ng-click="itemAttributesVm.changeAttribute(attribute)"
                       title="Click to change value"> {{attribute.value.doubleValue || 'Click to set value'}}
                    </a>
                </span>
            </div>

            <%-----  Date Attribute  -----%>

            <div ng-if="attribute.attributeDef.dataType == 'DATE'">
                <span>
                    <a ng-if="attribute.editMode == false" href=""
                       ng-click="itemAttributesVm.changeAttribute(attribute)"
                       title="Click to change"> {{ (attribute.value.dateValue | date:"dd/MM/yyyy") || 'Click to set
                        value'}}
                    </a>
                </span>
            </div>

            <%-----  Boolean Attribute  -----%>

            <div ng-if="attribute.attributeDef.dataType == 'BOOLEAN'">
                <span>
                    <a ng-if="attribute.editMode == false && attribute.value.booleanValue != null"
                       href="" ng-click="itemAttributesVm.changeAttribute(attribute)">
                        {{attribute.value.booleanValue || 'Click to set value'}}
                    </a>
                </span>
            </div>

            <div ng-if="attribute.editMode == true &&
                                    (attribute.attributeDef.dataType == 'TEXT' || attribute.attributeDef.dataType == 'INTEGER' ||
                                     attribute.attributeDef.dataType == 'DOUBLE' || attribute.attributeDef.dataType == 'BOOLEAN' ||
                                     attribute.attributeDef.dataType == 'DATE' || attribute.attributeDef.dataType == 'CURRENCY')">

                <input type="text" class="form-control" name="title"
                       ng-if="attribute.attributeDef.dataType == 'TEXT'"
                       placeholder="Enter value"
                       ng-model="attribute.value.stringValue" style="width:200px;">
                <input type="number" class="form-control" name="title"
                       ng-if="attribute.attributeDef.dataType == 'INTEGER'"
                       placeholder="Enter value"
                       ng-model="attribute.value.integerValue" style="width:200px;">
                <input type="text" class="form-control" name="title"
                       ng-if="attribute.attributeDef.dataType == 'DOUBLE'"
                       placeholder="Enter value"
                       ng-model="attribute.value.doubleValue" style="width:200px;">
                <label class="radio-inline" ng-repeat="choice in [true, false]"
                       ng-if="attribute.attributeDef.dataType == 'BOOLEAN'">
                    <input type="radio" name="change_{{attribute.attributeDef.id}}" ng-value="choice"
                           ng-model="attribute.value.booleanValue">{{choice}}
                </label>

                <div class="input-group" ng-if="attribute.attributeDef.dataType == 'DATE'"
                     style="width: 200px;">
                    <input type="text" class="form-control" date-picker
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
                <button ng-if="attribute.attributeDef.dataType == 'BOOLEAN'" class="btn btn-sm btn-primary"
                        type="button" title="Save"
                        style="padding: 8px;width:33px;"
                        ng-click="itemAttributesVm.saveItemAttribute(attribute)"><i class="fa fa-check"></i>
                </button>

                <button ng-if="attribute.attributeDef.dataType == 'BOOLEAN'" class="btn btn-sm btn-default"
                        type="button" title="Cancel"
                        style="padding: 8px;width:33px;"
                        ng-click="itemAttributesVm.cancelChanges(attribute)"><i class="fa fa-times"></i>
                </button>

                <button ng-if="attribute.attributeDef.dataType != 'BOOLEAN'" class="btn btn-sm btn-primary"
                        type="button" title="Save"
                        style="margin-top:-65px;margin-left:205px;padding: 8px;width:33px;"
                        ng-click="itemAttributesVm.saveItemAttribute(attribute)"><i class="fa fa-check"></i>
                </button>

                <button ng-if="attribute.attributeDef.dataType != 'BOOLEAN'" class="btn btn-sm btn-default"
                        type="button" title="Cancel"
                        style="margin-top:-65px;padding: 8px;width:33px;"
                        ng-click="itemAttributesVm.cancelChanges(attribute)"><i class="fa fa-times"></i>
                </button>
            </div>

            <%-----  List Attribute  -----%>

            <div ng-if="attribute.attributeDef.dataType == 'LIST'">
                <span>
                    <a title="Click to change value"
                       editable-select="attribute.value.listValue"
                       href="" e-style="width:200px;"
                       e-ng-options="value for value in attribute.attributeDef.lov.values track by value"
                       onaftersave="itemAttributesVm.saveItemAttribute(attribute)">
                        {{attribute.value.listValue || 'Click to set value'}}
                    </a>
                </span>
            </div>

            <%------  Image Attribute  ------%>

            <div ng-if="attribute.attributeDef.dataType == 'IMAGE'">
                <a href=""
                   ng-click="itemAttributesVm.showRevisionImage(attribute)"
                   title="Click to show large image">
                    <img ng-if="attribute.value.imageValue != null"
                         ng-src="{{attribute.value.revisionImagePath}}"
                         style="height: 100px;width: 100px;margin-bottom: 5px;">
                </a><br ng-if="attribute.value.imageValue != null">

                <%---- To show large Image ------%>

                <div id="myModal3" class="img-model modal">
                    <span class="closeImage1">&times;</span>
                    <img class="modal-content" id="img04">
                </div>
                <div>
                    <a href="" ng-if="attribute.value.imageValue == null"
                       ng-click="itemAttributesVm.change(attribute)"
                       ng-hide="attribute.changeImage == true"
                       title="Click to add image">
                        <span>Add Image</span>
                    </a>
                    <button ng-if="attribute.value.imageValue != null"
                            class="btn btn-sm btn-warning"
                            ng-click="itemAttributesVm.change(attribute)"
                            ng-hide="(attribute.changeImage == true)"
                            title="Click to change image"><i
                            class="fa fa-edit"></i>
                    </button>
                    <input ng-show="attribute.changeImage == true" class="browse-control" name="file"
                           id="itemRevisionImageFile"
                           type="file" ng-file-model="attribute.newImageValue" style="width: 250px;">
                    <button class="btn btn-sm btn-primary"
                            ng-click="itemAttributesVm.saveImage(attribute)"
                            ng-show="attribute.changeImage == true" title="Save"
                            style="margin-top:-62px;margin-left: 255px;padding-top:8px;">
                        <i class="fa fa-check"></i>
                    </button>
                    <button class="btn btn-sm btn-default"
                            ng-click="itemAttributesVm.cancel(attribute)"
                            ng-show="attribute.changeImage == true" title="Cancel"
                            style="margin-top:-62px;padding-top:8px;">
                        <i class="fa fa-times"></i>
                    </button>
                </div>
            </div>

            <%-----  Attachment Attribute  -----%>

            <div ng-if="attribute.attributeDef.dataType == 'ATTACHMENT'">

                <div ng-if="attribute.value.attachmentValues.length > 0"
                     ng-repeat="attachment in attribute.value.attachmentValues">
                    <ul style="margin-left:-30px;">
                        <li>
                            <a href="" ng-click="itemAttributesVm.openPropertyAttachment(attachment)"
                               title="Click to download attachment"
                               style="margin-bottom: 5px;width:250px;color: #002451;">
                                {{attachment.name}}
                            </a>
                            <a href=""
                               title="Delete Attachment"
                               ng-click="itemAttributesVm.deleteAttachments(attribute,attachment)">
                                <i class="fa fa-times"
                                   style="margin-left: 5px;font-size: 14px;color: darkred">
                                </i>
                            </a>
                        </li>
                    </ul>
                </div>

                <div>
                    <a href="">
                        <div ng-show="attribute.showAttachment == false" title="Add Atachments"
                             ng-click="itemAttributesVm.addAttachment(attribute)">
                            <span>Add Attachments</span>
                        </div>
                    </a>

                    <div ng-show="attribute.showAttachment == true">
                        <input class="browse-control" name="file"
                               multiple="true" id="itemRevisionAttachmentFile"
                               type="file" ng-file-model="attribute.revAttachmentValues"
                               style="width: 250px;"/>
                        <button class="btn btn-sm btn-primary" title="Save Attachments"
                                ng-click="itemAttributesVm.saveRevisionAttachments(attribute)"
                                style="margin-top:-62px;margin-left: 255px;padding-top:8px;">
                            <i class="fa fa-check"></i>
                        </button>
                        <button class="btn btn-sm btn-default" title="Cancel"
                                ng-click="itemAttributesVm.cancelAttachment(attribute)"
                                style="margin-top:-62px;padding-top:8px;">
                            <i class="fa fa-times"></i>
                        </button>
                        <p ng-repeat="file in attribute.revAttachmentValues">
                            {{file.name}}
                        </p>
                    </div>
                </div>
            </div>

            <%------  Time Attribute  ------%>

            <div ng-if="attribute.attributeDef.dataType == 'TIME'">
                <span>
                    <a href="" ng-if="attribute.showTimeAttribute == false" title="Click to change"
                       ng-show="attribute.showTimeAttribute == false"
                       ng-click="itemAttributesVm.changeTime(attribute)">
                        {{attribute.value.timeValue || 'Click to set value'}}
                    </a>
                </span>

                <div ng-if="attribute.showTimeAttribute == true">
                    <div uib-timepicker
                         ng-model="attribute.timeValue">
                    </div>
                    <button class="btn btn-sm btn-primary" type="button" title="Save"
                            style="margin-top:-195px;margin-left:165px;"
                            ng-click="itemAttributesVm.saveTime(attribute)"><i class="fa fa-check"></i>
                    </button>

                    <button class="btn btn-sm btn-default" type="button"
                            title="Cancel"
                            style="margin-top:-195px;"
                            ng-click="itemAttributesVm.cancelTime(attribute)"><i class="fa fa-times"></i>
                    </button>
                </div>
            </div>

            <%------  Timestamp Attribute  ------%>

            <div ng-if="attribute.attributeDef.dataType == 'TIMESTAMP'">
                <span>{{attribute.value.timestampValue}}</span>

                <div ng-if="attribute.showTimestamp == true">
                    <div uib-timepicker
                         ng-model="attribute.timestampValue">
                    </div>
                    <button class="btn btn-sm btn-primary" type="button" title="Save"
                            style="margin-top:-195px;margin-left:165px;"
                            ng-click="itemAttributesVm.saveTime(attribute)"><i class="fa fa-check"></i>
                    </button>

                    <button class="btn btn-sm btn-default" type="button"
                            title="Cancel"
                            style="margin-top:-195px;"
                            ng-click="itemAttributesVm.cancelTime(attribute)"><i class="fa fa-times"></i>
                    </button>
                </div>
            </div>

            <%------  Currency Attribute  ------%>

            <div ng-if="attribute.attributeDef.dataType == 'CURRENCY'">
                <div ng-if="attribute.value.encodedCurrencyType != null">
                    <a ng-if="attribute.editMode == false"
                       href=""
                       ng-bind-html="attribute.value.encodedCurrencyType">
                    </a>
                    <span> 
                        <a href=""
                           ng-if="attribute.editMode == false"
                           title="Click to change"
                           ng-click="itemAttributesVm.changeAttribute(attribute)">
                            {{attribute.value.currencyValue || 'Click to set value'}}
                        </a>
                    </span>
                </div>

                <%----------  For newly added 'CURRENCY' property after ITEM Creation ---------------%>

                <div ng-if="attribute.value.encodedCurrencyType == null || attribute.value.currencyType == null">
                    <div ng-if="attribute.changeCurrency == true" style="width: 150px;">
                        <select class="form-control" ng-model="attribute.value.currencyType"
                                ng-init="attribute.value.currencyType = itemAttributesVm.currencies[0].id"
                                ng-options="currency.id as currency.name for currency in itemAttributesVm.currencies">
                        </select>
                    </div>
                    <span>
                        <a href="" ng-if="attribute.changeCurrency == false"
                           ng-click="itemAttributesVm.changeCurrencyValue(attribute)"
                           title="Click to change">
                            {{attribute.value.currencyValue || 'Click to set value'}}
                        </a>
                    </span>

                    <div ng-if="attribute.changeCurrency == true"
                         style="width: 100px;margin-top: -40px;margin-left: 155px;">
                        <input class="form-control" name="currencyValue" type="number"
                               placeholder="Enter value"
                               ng-model="attribute.value.currencyValue"/>
                    </div>
                    <button ng-if="attribute.changeCurrency == true" class="btn btn-sm btn-primary"
                            title="Save"
                            ng-click="itemAttributesVm.saveItemAttribute(attribute)"
                            style="margin-top:-64px;margin-left: 260px;padding-top:10px;">
                        <i class="fa fa-check"></i>
                    </button>
                    <button ng-if="attribute.changeCurrency == true" class="btn btn-sm btn-default"
                            title="Cancel"
                            ng-click="itemAttributesVm.cancelChanges(attribute)"
                            style="margin-top:-64px;padding-top:10px;">
                        <i class="fa fa-times"></i>
                    </button>
                </div>
            </div>
        </div>
    </div>
</div>
