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

    /* The Modal (background) */
    .img-model.modal {
        display: none; /* Hidden by default */
        position: fixed; /* Stay in place */
        z-index: 1; /* Sit on top */
        padding-top: 100px; /* Location of the box */
        left: 0;
        top: 0;
        width: 100%; /* Full width */
        height: 100%; /* Full height */
        overflow: auto; /* Enable scroll if needed */
        background-color: rgb(0, 0, 0); /* Fallback color */
        background-color: rgba(0, 0, 0, 0.4); /* Black w/ opacity */
    }

    /* Modal Content (image) */
    .img-model .modal-content {
        margin: auto;
        display: block;
        height: 90%;
        width: 60%;
        /*max-width: 70%;*/
    }

    /* Caption of Modal Image */
    .img-model #caption {
        margin: auto;
        display: block;
        width: 80%;
        max-width: 700px;
        text-align: center;
        color: #ccc;
        padding: 10px 0;
        height: 150px;
    }

    /* Add Animation */
    .img-model .modal-content, #caption {
        -webkit-animation-name: zoom;
        -webkit-animation-duration: 0.6s;
        animation-name: zoom;
        animation-duration: 0.6s;
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
    <div ng-if="manpowerAttributeVm.attributes.length == 0 && manpowerAttributeVm.loading == false">
        <span translate>No Attributes available to view</span>
    </div>
    <div class="row master-att" ng-if="manpowerAttributeVm.attributes.length != 0"
         ng-repeat="attribute in manpowerAttributeVm.attributes">
        <div class="label col-sm-2 text-right">
            <span>{{attribute.attributeDef.name}}: </span>
        </div>
        <div class="value col-xs-8 col-sm-9">
            <%------  String Attribute  --------%>

                <div ng-if="attribute.attributeDef.dataType == 'TEXT'">
                    <a href="#"
                       editable-text="attribute.value.stringValue" ng-if="hasPermission('permission.manpower.edit')"
                       onaftersave="manpowerAttributeVm.saveManpowerAttribute(attribute)">
                        {{attribute.value.stringValue || 'Click to set value'}}
                    </a>

                    <p ng-if="hasPermission('permission.manpower.edit') == false">
                        {{attribute.value.stringValue}}</p>
                </div>

                <%------  Integer Attribute  --------%>

                <div id="intClick"
                     ng-if="attribute.attributeDef.dataType == 'INTEGER'">
                    <a href="#" ng-if="hasPermission('permission.manpower.edit')"
                       editable-number="attribute.value.integerValue" e-step="any"
                       onbeforesave="manpowerAttributeVm.validateAttribute($data)"
                       onaftersave="manpowerAttributeVm.saveManpowerAttribute(attribute)">
                        {{attribute.value.integerValue || 'Click to set value'}}
                    </a>

                    <p ng-if="hasPermission('permission.manpower.edit') == false">
                        {{attribute.value.integerValue}}</p>
                </div>

                <%------  Double Attribute  --------%>

                <div ng-if="attribute.attributeDef.dataType == 'DOUBLE'">
                    <a href="#" ng-if="hasPermission('permission.manpower.edit')"
                       editable-number="attribute.value.doubleValue" e-step="any"
                       onaftersave="manpowerAttributeVm.saveManpowerAttribute(attribute)">
                        {{attribute.value.doubleValue || 'Click to set value'}}
                    </a>

                    <p ng-if="hasPermission('permission.manpower.edit') == false">
                        {{attribute.value.doubleValue}}</p>
                </div>

                <%------  Date Attribute  --------%>

                <div ng-if="attribute.attributeDef.dataType == 'DATE'">
                    <a ng-if="attribute.editMode == false && hasPermission('permission.manpower.edit')"
                       href="" ng-click="manpowerAttributeVm.changeAttribute(attribute)"
                       title="{{changeDate}}">
                        {{ (attribute.value.dateValue | date:"dd/MM/yyyy") || 'Click to set value'}}
                    </a>

                    <p ng-if="hasPermission('permission.manpower.edit') == false">{{ (attribute.value.dateValue |
                        date:"dd/MM/yyyy")}}</p>

                    <div ng-if="attribute.editMode == true " class="input-group"
                         ng-if="attribute.attributeDef.dataType == 'DATE'" style="width: 200px;">
                        <input type="text" class="form-control" date-picker
                               ng-model="attribute.value.dateValue"
                               name="attDate" placeholder="dd/mm/yyyy">
                                <span class="input-group-addon">
                                    <i class="glyphicon glyphicon-calendar"></i>
                                </span>
                    </div>
                    <div ng-if="attribute.editMode == true" style="margin-left: 3px;margin-top: -41px;">
                        <button class="btn btn-md btn-primary" type="button"
                                style="margin-left:202px;padding: 8px 12px;"
                                ng-click="manpowerAttributeVm.saveManpowerAttribute(attribute)"><i
                                class="fa fa-check"></i>
                        </button>

                        <button class="btn btn-md btn-default" type="button"
                                style="padding: 8px 12px;"
                                ng-click="manpowerAttributeVm.cancelChanges(attribute)"><i class="fa fa-times"></i>
                        </button>
                </div>
                </div>

                <%------  Boolean Attribute  --------%>

                <div ng-if="attribute.attributeDef.dataType == 'BOOLEAN' && attribute.value.booleanValue == null && hasPermission('permission.manpower.edit')">
                    <a href="" editable-radiolist="attribute.value.booleanValue"
                       e-ng-options="s.value as s.name for s in manpowerAttributeVm.flags track by s.value"
                       onaftersave="manpowerAttributeVm.saveManpowerAttribute(attribute)"
                       ng-model="attribute.value.booleanValue">
                        {{'Click to set value' }}
                    </a>
                </div>

                <div ng-if="attribute.attributeDef.dataType == 'BOOLEAN' && attribute.value.booleanValue != null">
                    <a href="" editable-radiolist="attribute.value.booleanValue"
                       ng-if="hasPermission('permission.manpower.edit')"
                       e-ng-options="s.value as s.name for s in manpowerAttributeVm.flags track by s.value"
                       onaftersave="manpowerAttributeVm.saveManpowerAttribute(attribute)"
                       ng-model="attribute.value.booleanValue">
                        {{attribute.value.booleanValue}}
                    </a>

                    <p ng-if="hasPermission('permission.manpower.edit') == false">{{attribute.value.booleanValue}}</p>
                </div>

                <%------  List Attribute  --------%>

                <div ng-if="attribute.attributeDef.dataType == 'LIST'">
                    <a title="Click to change listValue" editable-select="attribute.value.listValue"
                       href="" e-style="width:200px;" ng-if="hasPermission('permission.manpower.edit')"
                       e-ng-options="value for value in attribute.attributeDef.lov.values track by value"
                       onaftersave="manpowerAttributeVm.saveManpowerAttribute(attribute)">
                        {{attribute.value.listValue || 'Click to set value' }}
                    </a>

                    <p ng-if="hasPermission('permission.manpower.edit') == false">{{attribute.value.listValue}}</p>
                </div>

                <%------ Time Attribute  --------%>
                <div ng-if="attribute.attributeDef.dataType == 'TIME'">
                    <a ng-if="attribute.showTimeAttribute == false && hasPermission('permission.manpower.edit')"
                       href="" title="Click to change time"
                       ng-click="manpowerAttributeVm.changeTime(attribute)">
                        {{attribute.value.timeValue || 'Click to set value'}}
                    </a>

                    <p ng-if="hasPermission('permission.manpower.edit') == false">{{attribute.value.timeValue}}</p>

                    <div ng-if="attribute.showTimeAttribute == true">
                        <div uib-timepicker
                             ng-model="attribute.editTimeValue">
                    </div>
                        <button class="btn btn-md btn-primary" type="button"
                                style="margin-top:-150px;margin-left:165px;padding: 8px 12px;"
                                ng-click="manpowerAttributeVm.saveTimeProperty(attribute)"><i
                                class="fa fa-check"></i>
                        </button>

                        <button class="btn btn-md btn-default" type="button"
                                style="margin-top:-150px;padding: 8px 12px;"
                                ng-click="manpowerAttributeVm.cancelTime(attribute)"><i class="fa fa-times"></i>
                        </button>
                </div>
                </div>

                <%------  TimeStamp Attribute  --------%>

                <div ng-if="attribute.attributeDef.dataType == 'TIMESTAMP'">

                    <a ng-if="attribute.showTimestamp == false && hasPermission('permission.manpower.edit')"
                       href="" title="Click to change timeStamp"
                       ng-click="manpowerAttributeVm.changeTimestamp(attribute)">
                        {{attribute.value.timestampValue || 'Click to set value'}}
                    </a>

                    <p ng-if="hasPermission('permission.manpower.edit') == false">{{attribute.value.timestampValue}}</p>

                    <div ng-if="attribute.showTimestamp == true">
                        <div uib-timepicker
                             ng-model="attribute.editTimestampValue">
                    </div>
                        <button class="btn btn-md btn-primary" type="button"
                                style="margin-top:-150px;margin-left:165px;padding: 8px 12px;"
                                ng-click="manpowerAttributeVm.saveTimeProperty(attribute)"><i
                                class="fa fa-check"></i>
                        </button>

                        <button class="btn btn-md btn-default" type="button"
                                style="margin-top:-150px;padding: 8px 12px;"
                                ng-click="manpowerAttributeVm.cancelTime(attribute)"><i class="fa fa-times"></i>
                        </button>
                </div>
                </div>

                <%------  Object Attribute  --------%>

                <div class="row" ng-if="attribute.attributeDef.dataType == 'OBJECT'">

                    <a ng-if="attribute.value.refValue.objectType == 'MATERIALTYPE'" href=""
                       ng-click="manpowerAttributeVm.showRefValueDetails(attribute)"
                       title="Click to show attribute details">
                        {{attribute.value.refValue.itemNumber}}
                    </a>


                    <a ng-if="attribute.value.refValue.objectType == 'MACHINETYPE'" href=""
                       ng-click="manpowerAttributeVm.showRefValueDetails(attribute)"
                       title="Click to show attribute details">
                        {{attribute.value.refValue.itemNumber}}
                    </a>

                    <a ng-if="attribute.value.refValue.objectType == 'MANPOWERTYPE'" href=""
                       ng-click="manpowerAttributeVm.showRefValueDetails(attribute)"
                       title="Click to show attribute details">
                        {{attribute.value.refValue.itemNumber}}
                    </a>
                    <a href=""
                       ng-if="attribute.value.refValue == null && attribute.editMode == false && hasPermission('permission.manpower.edit')"
                       title="Add attribute"
                       ng-click="showObjectValues(attribute)">
                        Add attribute
                    </a>
                    <button ng-if="attribute.value.refValue != null && attribute.editMode == false && hasPermission('permission.manpower.edit')"
                            class="btn btn-xs btn-warning"
                            title="Change attribute"
                            ng-click="showObjectValues(attribute)">
                        <i class="fa fa-edit"></i>
                    </button>
                    <button ng-if="attribute.editMode == true" class="btn btn-xs btn-primary"
                            title="Save attribute"
                            ng-click="manpowerAttributeVm.saveObject(attribute)">
                        <i class="fa fa-check"></i>
                    </button>

                    <button ng-if="attribute.editMode == true" class="btn btn-xs btn-default"
                            title="Cancel changes"
                            ng-click="manpowerAttributeVm.cancelChanges(attribute)">
                        <i class=" fa fa-times"></i>
                    </button>
                </div>

                <%------  Image Attribute  --------%>

                <div ng-if="attribute.attributeDef.dataType == 'IMAGE'">
                    <a href="" ng-click="manpowerAttributeVm.showImage(attribute)"
                       title="Click to show large image">
                        <img ng-if="attribute.value.imageValue != null"
                             ng-src="{{attribute.value.imagePath}}"
                             style="height: 100px;width: 100px;margin-bottom: 5px;">
                    </a>
                    <%-- To show large image --%>
                    <div id="myModal2" class="img-model modal">
                        <span class="closeImage">&times;</span>
                        <img class="modal-content" id="img03">
                    </div>

                    <div class="inline">
                        <a href=""
                           ng-if="attribute.value.imageValue == null && hasPermission('permission.manpower.edit')"
                           ng-click="manpowerAttributeVm.change(attribute)"
                           ng-hide="attribute.changeImage == true" title="Add Image">
                            Add Image
                    </a>
                        <button ng-if="attribute.value.imageValue != null && hasPermission('permission.manpower.edit')"
                            class="btn btn-xs btn-warning"
                                ng-click="manpowerAttributeVm.change(attribute)"
                                ng-hide="attribute.changeImage == true"
                                title="Change image"><i
                                class="fa fa-edit"></i>
                    </button>
                        <input ng-show="attribute.changeImage == true && hasPermission('permission.manpower.edit')"
                               class="browse-control" name="file"
                               accept="image/*"
                               type="file" ng-file-model="attribute.newImageValue" style="width: 250px;"
                               ng-if="manpowerAttributeVm.clearBrowse">
                        <button class="btn btn-xs btn-primary"
                                ng-click="manpowerAttributeVm.saveImage(attribute)"
                                ng-show="attribute.changeImage == true && manpowerAttributeVm.clearBrowse"
                                title="Save image"
                                style="margin-top:-62px;margin-left: 255px;padding-top:8px;"><i
                                class="fa fa-check"></i>
                    </button>
                        <button class="btn btn-xs btn-default"
                                ng-click="manpowerAttributeVm.cancel(attribute)" title="Cancel Changes"
                                ng-show="attribute.changeImage == true && manpowerAttributeVm.clearBrowse"
                                style="margin-top:-62px;padding-top:8px;"><i
                                class="fa fa-times"></i>
                    </button>
                </div>
                </div>

                <%------  Currency Property  ------%>

                <div ng-if="attribute.attributeDef.dataType == 'CURRENCY'">
                    <div ng-if="attribute.value.encodedCurrencyType != null">
                                <span>
                                     <span ng-bind-html="attribute.value.encodedCurrencyType"></span>
                                </span>
                        <a href="" ng-if="hasPermission('permission.manpower.edit')"
                           editable-text="attribute.value.currencyValue"
                           onaftersave="manpowerAttributeVm.saveManpowerAttribute(attribute)"
                           title="Change currency value">
                            {{attribute.value.currencyValue || 'Click to set value'}}
                    </a>
                        <span ng-if="hasPermission('permission.manpower.edit') == false">{{attribute.value.currencyValue}}</span>
                    </div>

                    <%----------  For newly added 'CURRENCY' attribute after ECO Creation ---------------%>

                    <div ng-if="attribute.value.encodedCurrencyType == null || attribute.value.currencyType == null">
                        <div ng-if="attribute.changeCurrency == true" style="width: 100px;">
                            <select class="form-control" ng-model="attribute.value.currencyType"
                                    ng-init="attribute.value.currencyType = manpowerAttributeVm.currencies[0].id"
                                    ng-options="currency.id as currency.name for currency in manpowerAttributeVm.currencies">
                            </select>
                    </div>
                        <a href=""
                           ng-if="attribute.changeCurrency == false && hasPermission('permission.manpower.edit')"
                           ng-click="manpowerAttributeVm.changeCurrencyValue(attribute)"
                           title="Change currency value">
                            {{attribute.value.currencyValue || 'Click to set value'}}
                        </a>
                        <span ng-if="hasPermission('permission.manpower.edit') == false">{{attribute.value.currencyValue}}</span>

                        <div ng-if="attribute.changeCurrency == true"
                             style="width: 150px;margin-top: -40px;margin-left: 105px;">
                            <input class="form-control" name="currencyValue" type="number"
                                   placeholder="Enter value"
                                   ng-model="attribute.value.currencyValue"/>
                    </div>
                        <button ng-if="attribute.changeCurrency == true" class="btn btn-sm btn-primary"
                                title="Save currency value"
                                ng-click="manpowerAttributeVm.saveManpowerAttribute(attribute)"
                                style="margin-top:-65px;margin-left: 262px;padding:8px 11px;">
                            <i class="fa fa-check"></i>
                        </button>
                        <button ng-if="attribute.changeCurrency == true" class="btn btn-sm btn-default"
                                title="Cancel changes"
                                ng-click="manpowerAttributeVm.cancelChanges(attribute)"
                                style="margin-top:-65px;padding:8px 11px;">
                            <i class="fa fa-times"></i>
                        </button>
                </div>
                </div>
                <%------  Attachment Attribute  --------%>

                <div ng-if="attribute.attributeDef.dataType == 'ATTACHMENT'">
                    <div ng-if="attribute.value.attachmentValues.length > 0"
                         ng-repeat="attachment in attribute.value.attachmentValues">
                        <ul style="margin-left:-30px;">
                            <li>
                                <a href="" ng-click="manpowerAttributeVm.openAttachment(attachment)"
                                   ng-if="hasPermission('permission.manpower.edit') == true"
                                   title="Click to download attachment"
                                   style="margin-bottom: 5px;width:250px;color: #002451;">
                                    {{attachment.name}}
                                </a>

                                <p ng-if="hasPermission('permission.manpower.edit') == false">{{attachment.name}}</p>
                                <a href="" title="Delete attachment"
                                   ng-if="hasPermission('permission.manpower.edit') == true"
                                   ng-click="manpowerAttributeVm.deleteAttachments(attribute,attachment)">
                                    <i class="fa fa-times"
                                       style="margin-left: 5px;font-size: 14px;color: darkred"></i>
                                </a>
                            </li>
                        </ul>
                    </div>

                    <div>
                        <a href="">
                        <span ng-show="attribute.showAttachment == false && hasPermission('permission.manpower.edit') == true"
                              ng-click="manpowerAttributeVm.addAttachment(attribute)">Add attachments
                        </span>
                        </a>

                        <div ng-show="attribute.showAttachment == true">
                            <input class="browse-control" name="file" id="file" multiple="true"
                                   ng-if="manpowerAttributeVm.clearBrowse"
                                   type="file" ng-file-model="attribute.attachmentValues"
                                   style="width: 250px;"/>
                            <button class="btn btn-xs btn-primary"
                                    ng-click="manpowerAttributeVm.saveAttachments(attribute)"
                                    title="Save attachments"
                                    style="margin-top:-62px;margin-left: 255px;padding-top:8px;">
                            <i class="fa fa-check"></i>
                        </button>
                            <button class="btn btn-xs btn-default"
                                    ng-click="manpowerAttributeVm.cancelAttachment(attribute)"
                                title="Cancel changes"
                                    style="margin-top:-62px;padding-top:8px;">
                            <i class="fa fa-times"></i>
                        </button>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
<br>
