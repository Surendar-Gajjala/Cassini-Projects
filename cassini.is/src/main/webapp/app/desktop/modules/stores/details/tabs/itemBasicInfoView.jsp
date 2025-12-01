<div class="row row-eq-height" style="margin: 0;">
    <div class="item-details col-sm-10 col-sm-offset-1" style="padding: 30px;">
        <div class="row">
            <div class="label col-xs-4 col-sm-4 text-right">
                <span>Item Number: </span>
            </div>
            <div class="value col-xs-8 col-sm-8">
                <span>{{itemBasicInfoVm.receivedItem.itemNumber}}</span>
            </div>
        </div>
        <div class="row">
            <div class="label col-xs-4 col-sm-4 text-right">
                <span>Item Name: </span>
            </div>
            <div class="value col-xs-8 col-sm-8">
                {{itemBasicInfoVm.receivedItem.itemName}}
            </div>
        </div>
        <div class="row">
            <div class="label col-xs-4 col-sm-4 text-right">
                <span>Description: </span>
            </div>
            <div class="value col-xs-8 col-sm-8">
                {{itemBasicInfoVm.receivedItem.description}}
            </div>
        </div>

        <div class="row">
            <div class="label col-xs-4 col-sm-4 text-right">
                <span>Units: </span>
            </div>
            <div class="value col-xs-8 col-sm-8">
                {{itemBasicInfoVm.receivedItem.units}}
            </div>
        </div>

        <div class="row">
            <div class="label col-xs-4 col-sm-4 text-right">
                <span>Qty :</span>
            </div>
            <div class="value col-xs-8 col-sm-8">
                <span>{{itemBasicInfoVm.receivedItem.quantity}}</span>
            </div>
        </div>
        <div class="row" ng-if="itemBasicInfoVm.receivedItem.receivedOn != null">
            <div class="label col-xs-4 col-sm-4 text-right">
                <span>Received On: </span>
            </div>
            <div class="value col-xs-8 col-sm-8">
                <span>{{itemBasicInfoVm.receivedItem.timeStamp}}</span>
            </div>
        </div>
        <div class="row" ng-if="itemBasicInfoVm.receivedItem.issuedOn != null">
            <div class="label col-xs-4 col-sm-4 text-right">
                <span>Issued On: </span>
            </div>
            <div class="value col-xs-8 col-sm-8">
                <span>{{itemBasicInfoVm.receivedItem.timeStamp}}</span>
            </div>
        </div>
        <div class="row" ng-if="itemBasicInfoVm.receivedItem.loanReceivedOn != null">
            <div class="label col-xs-4 col-sm-4 text-right">
                <span>Loan Received On: </span>
            </div>
            <div class="value col-xs-8 col-sm-8">
                <span>{{itemBasicInfoVm.receivedItem.timeStamp}}</span>
            </div>
        </div>
        <div class="row" ng-if="itemBasicInfoVm.receivedItem.loanIssuedOn != null">
            <div class="label col-xs-4 col-sm-4 text-right">
                <span>Loan Issued On: </span>
            </div>
            <div class="value col-xs-8 col-sm-8">
                <span>{{itemBasicInfoVm.receivedItem.timeStamp}}</span>
            </div>
        </div>
        <div class="row" ng-if="itemBasicInfoVm.receivedItem.requestedOn != null">
            <div class="label col-xs-4 col-sm-4 text-right">
                <span>Requested On: </span>
            </div>
            <div class="value col-xs-8 col-sm-8">
                <span>{{itemBasicInfoVm.receivedItem.timeStamp}}</span>
            </div>
        </div>

        <br>
        <h4 class="section-title" style="margin-left: -10px; margin-right: -10px;">System Attributes</h4>

        <div class="row" ng-repeat="attribute in itemBasicInfoVm.materialProperties">
            <div class="label col-xs-4 col-sm-3 text-right">
                <span>{{attribute.attributeDef.name}}: </span>
            </div>
            <div class="value col-xs-8 col-sm-9">
                <%------  String Attribute  --------%>

                <div ng-if="attribute.attributeDef.dataType == 'STRING'">
                    <a href="#"
                       editable-text="attribute.value.stringValue" ng-if="hasPermission('permission.materials.edit')"
                       onaftersave="itemBasicInfoVm.saveMaterialProperties(attribute)">
                        {{attribute.value.stringValue || 'Click to set value'}}
                    </a>

                    <p ng-if="hasPermission('permission.materials.edit') == false">
                        {{attribute.value.stringValue}}</p>
                </div>

                <%------  Integer Attribute  --------%>

                <div id="intClick"
                     ng-if="attribute.attributeDef.dataType == 'INTEGER'">
                    <a href="#" ng-if="hasPermission('permission.materials.edit')"
                       editable-number="attribute.value.integerValue" e-step="any"
                       onbeforesave="itemBasicInfoVm.validateAttribute($data)"
                       onaftersave="itemBasicInfoVm.saveMaterialProperties(attribute)">
                        {{attribute.value.integerValue || 'Click to set value'}}
                    </a>

                    <p ng-if="hasPermission('permission.materials.edit') == false">
                        {{attribute.value.integerValue}}</p>
                </div>

                <%------  Double Attribute  --------%>

                <div ng-if="attribute.attributeDef.dataType == 'DOUBLE'">
                    <a href="#" ng-if="hasPermission('permission.materials.edit')"
                       editable-number="attribute.value.doubleValue" e-step="any"
                       onaftersave="itemBasicInfoVm.saveMaterialProperties(attribute)">
                        {{attribute.value.doubleValue || 'Click to set value'}}
                    </a>

                    <p ng-if="hasPermission('permission.materials.edit') == false">
                        {{attribute.value.doubleValue}}</p>
                </div>

                <%------  Date Attribute  --------%>

                <div ng-if="attribute.attributeDef.dataType == 'DATE'">
                    <a ng-if="attribute.editMode == false && hasPermission('permission.materials.edit')"
                       href="" ng-click="itemBasicInfoVm.changeAttribute(attribute)"
                       title="{{changeDate}}">
                        {{ (attribute.value.dateValue | date:"dd/MM/yyyy") || 'Click to set value'}}
                    </a>

                    <p ng-if="hasPermission('permission.materials.edit') == false">{{ (attribute.value.dateValue |
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
                                ng-click="itemBasicInfoVm.saveMaterialProperties(attribute)"><i
                                class="fa fa-check"></i>
                        </button>

                        <button class="btn btn-md btn-default" type="button"
                                style="padding: 8px 12px;"
                                ng-click="itemBasicInfoVm.cancelChanges(attribute)"><i class="fa fa-times"></i>
                        </button>
                    </div>
                </div>

                <%------  Boolean Attribute  --------%>

                <div ng-if="attribute.attributeDef.dataType == 'BOOLEAN' && attribute.value.booleanValue == null && hasPermission('permission.materials.edit')">
                    <a href="" editable-radiolist="attribute.value.booleanValue"
                       e-ng-options="s.value as s.name for s in itemBasicInfoVm.flags track by s.value"
                       onaftersave="itemBasicInfoVm.saveMaterialProperties(attribute)"
                       ng-model="attribute.value.booleanValue">
                        {{'Click to set value' }}
                    </a>
                </div>

                <div ng-if="attribute.attributeDef.dataType == 'BOOLEAN' && attribute.value.booleanValue != null">
                    <a href="" editable-radiolist="attribute.value.booleanValue"
                       ng-if="hasPermission('permission.materials.edit')"
                       e-ng-options="s.value as s.name for s in itemBasicInfoVm.flags track by s.value"
                       onaftersave="itemBasicInfoVm.saveMaterialProperties(attribute)"
                       ng-model="attribute.value.booleanValue">
                        {{attribute.value.booleanValue}}
                    </a>

                    <p ng-if="hasPermission('permission.materials.edit') == false">{{attribute.value.booleanValue}}</p>
                </div>

                <%------  List Attribute  --------%>

                <div ng-if="attribute.attributeDef.dataType == 'LIST'">
                    <a title="Click to change listValue" editable-select="attribute.value.listValue"
                       href="" e-style="width:200px;" ng-if="hasPermission('permission.materials.edit')"
                       e-ng-options="value for value in attribute.attributeDef.lov.values track by value"
                       onaftersave="itemBasicInfoVm.saveMaterialProperties(attribute)">
                        {{attribute.value.listValue || 'Click to set value' }}
                    </a>

                    <p ng-if="hasPermission('permission.materials.edit') == false">{{attribute.value.listValue}}</p>
                </div>

                <%------ Time Attribute  --------%>
                <div ng-if="attribute.attributeDef.dataType == 'TIME'">
                    <a ng-if="attribute.showTimeAttribute == false && hasPermission('permission.materials.edit')"
                       href="" title="Click to change time"
                       ng-click="itemBasicInfoVm.changeTime(attribute)">
                        {{attribute.value.timeValue || 'Click to set value'}}
                    </a>

                    <p ng-if="hasPermission('permission.materials.edit') == false">{{attribute.value.timeValue}}</p>

                    <div ng-if="attribute.showTimeAttribute == true">
                        <div uib-timepicker
                             ng-model="attribute.editTimeValue">
                        </div>
                        <button class="btn btn-md btn-primary" type="button"
                                style="margin-top:-150px;margin-left:165px;padding: 8px 12px;"
                                ng-click="itemBasicInfoVm.saveTimeProperty(attribute)"><i
                                class="fa fa-check"></i>
                        </button>

                        <button class="btn btn-md btn-default" type="button"
                                style="margin-top:-150px;padding: 8px 12px;"
                                ng-click="itemBasicInfoVm.cancelTime(attribute)"><i class="fa fa-times"></i>
                        </button>
                    </div>
                </div>

                <%------  TimeStamp Attribute  --------%>

                <div ng-if="attribute.attributeDef.dataType == 'TIMESTAMP'">

                    <a ng-if="attribute.showTimestamp == false && hasPermission('permission.materials.edit')"
                       href="" title="Click to change timeStamp"
                       ng-click="itemBasicInfoVm.changeTimestamp(attribute)">
                        {{attribute.value.timestampValue || 'Click to set value'}}
                    </a>

                    <p ng-if="hasPermission('permission.materials.edit') == false">
                        {{attribute.value.timestampValue}}</p>

                    <div ng-if="attribute.showTimestamp == true">
                        <div uib-timepicker
                             ng-model="attribute.timestampValue">
                        </div>
                        <button class="btn btn-md btn-primary" type="button"
                                style="margin-top:-150px;margin-left:165px;padding: 8px 12px;"
                                ng-click="itemBasicInfoVm.saveTimeProperty(attribute)"><i
                                class="fa fa-check"></i>
                        </button>

                        <button class="btn btn-md btn-default" type="button"
                                style="margin-top:-150px;padding: 8px 12px;"
                                ng-click="itemBasicInfoVm.cancelTime(attribute)"><i class="fa fa-times"></i>
                        </button>
                    </div>
                </div>

                <%------  Object Attribute  --------%>

                <div class="row" ng-if="attribute.attributeDef.dataType == 'OBJECT'">

                    <a ng-if="attribute.value.refValue.objectType == 'MATERIALTYPE'" href=""
                       ng-click="itemBasicInfoVm.showRefValueDetails(attribute)"
                       title="Click to show attribute details">
                        {{attribute.value.refValue.itemNumber}}
                    </a>


                    <a ng-if="attribute.value.refValue.objectType == 'MACHINETYPE'" href=""
                       ng-click="itemBasicInfoVm.showRefValueDetails(attribute)"
                       title="Click to show attribute details">
                        {{attribute.value.refValue.itemNumber}}
                    </a>

                    <a ng-if="attribute.value.refValue.objectType == 'MANPOWERTYPE'" href=""
                       ng-click="itemBasicInfoVm.showRefValueDetails(attribute)"
                       title="Click to show attribute details">
                        {{attribute.value.refValue.itemNumber}}
                    </a>
                    <a href=""
                       ng-if="attribute.value.refValue == null && attribute.editMode == false && hasPermission('permission.materials.edit')"
                       title="Add Attribute"
                       ng-click="showObjectValues(attribute)">
                        Add attribute
                    </a>
                    <button ng-if="attribute.value.refValue != null && attribute.editMode == false && hasPermission('permission.materials.edit')"
                            class="btn btn-xs btn-warning"
                            title="Change Attribute"
                            ng-click="showObjectValues(attribute)">
                        <i class="fa fa-edit"></i>
                    </button>
                    <button ng-if="attribute.editMode == true" class="btn btn-xs btn-primary"
                            title="Save Attribute"
                            ng-click="itemBasicInfoVm.saveObject(attribute)">
                        <i class="fa fa-check"></i>
                    </button>

                    <button ng-if="attribute.editMode == true" class="btn btn-xs btn-default"
                            title="Cancel Changes"
                            ng-click="itemBasicInfoVm.cancelChanges(attribute)">
                        <i class=" fa fa-times"></i>
                    </button>
                </div>

                <%------  Image Attribute  --------%>

                <div ng-if="attribute.attributeDef.dataType == 'IMAGE'">
                    <a href="" ng-click="itemBasicInfoVm.showImage(attribute)"
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
                           ng-if="attribute.value.imageValue == null && hasPermission('permission.materials.edit')"
                           ng-click="itemBasicInfoVm.change(attribute)"
                           ng-hide="attribute.changeImage == true" title="Add Image">
                            Add Image
                        </a>
                        <button ng-if="attribute.value.imageValue != null && hasPermission('permission.materials.edit')"
                                class="btn btn-xs btn-warning"
                                ng-click="itemBasicInfoVm.change(attribute)"
                                ng-hide="attribute.changeImage == true"
                                title="Change Image"><i
                                class="fa fa-edit"></i>
                        </button>
                        <input ng-show="attribute.changeImage == true && hasPermission('permission.materials.edit')"
                               class="browse-control" name="file"
                               accept="image/*"
                               type="file" ng-file-model="attribute.newImageValue" style="width: 250px;"
                               ng-if="itemBasicInfoVm.clearBrowse">
                        <button class="btn btn-xs btn-primary"
                                ng-click="itemBasicInfoVm.saveImage(attribute)"
                                ng-show="attribute.changeImage == true && itemBasicInfoVm.clearBrowse"
                                title="Save Image"
                                style="margin-top:-62px;margin-left: 255px;padding-top:8px;"><i
                                class="fa fa-check"></i>
                        </button>
                        <button class="btn btn-xs btn-default"
                                ng-click="itemBasicInfoVm.cancel(attribute)" title="Cancel Changes"
                                ng-show="attribute.changeImage == true && itemBasicInfoVm.clearBrowse"
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
                        <a href="" ng-if="hasPermission('permission.materials.edit')"
                           editable-text="attribute.value.currencyValue"
                           onaftersave="itemBasicInfoVm.saveMaterialProperties(attribute)"
                           title="Change currency value">
                            {{attribute.value.currencyValue || 'Click to set value'}}
                        </a>
                        <span ng-if="hasPermission('permission.materials.edit') == false">{{attribute.value.currencyValue}}</span>
                    </div>

                    <%----------  For newly added 'CURRENCY' attribute after ECO Creation ---------------%>

                    <div ng-if="attribute.value.encodedCurrencyType == null || attribute.value.currencyType == null">
                        <div ng-if="attribute.changeCurrency == true" style="width: 100px;">
                            <select class="form-control" ng-model="attribute.value.currencyType"
                                    ng-init="attribute.value.currencyType = itemBasicInfoVm.currencies[0].id"
                                    ng-options="currency.id as currency.name for currency in itemBasicInfoVm.currencies">
                            </select>
                        </div>
                        <a href=""
                           ng-if="attribute.changeCurrency == false && hasPermission('permission.materials.edit')"
                           ng-click="itemBasicInfoVm.changeCurrencyValue(attribute)"
                           title="Change currency value">
                            {{attribute.value.currencyValue || 'Click to set value'}}
                        </a>
                        <span ng-if="hasPermission('permission.materials.edit') == false">{{attribute.value.currencyValue}}</span>

                        <div ng-if="attribute.changeCurrency == true"
                             style="width: 150px;margin-top: -40px;margin-left: 105px;">
                            <input class="form-control" name="currencyValue" type="number"
                                   placeholder="Enter Value"
                                   ng-model="attribute.value.currencyValue"/>
                        </div>
                        <button ng-if="attribute.changeCurrency == true" class="btn btn-sm btn-primary"
                                title="Save Currency Value"
                                ng-click="itemBasicInfoVm.saveMaterialProperties(attribute)"
                                style="margin-top:-65px;margin-left: 262px;padding:8px 11px;">
                            <i class="fa fa-check"></i>
                        </button>
                        <button ng-if="attribute.changeCurrency == true" class="btn btn-sm btn-default"
                                title="Cancel Changes"
                                ng-click="itemBasicInfoVm.cancelChanges(attribute)"
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
                                <a href="" ng-click="itemBasicInfoVm.openAttachment(attachment)"
                                   ng-if="hasPermission('permission.materials.edit') == true"
                                   title="Click to download attachment"
                                   style="margin-bottom: 5px;width:250px;color: #002451;">
                                    {{attachment.name}}
                                </a>

                                <p ng-if="hasPermission('permission.materials.edit') == false">{{attachment.name}}</p>
                                <a href="" title="Delete Attachment"
                                   ng-if="hasPermission('permission.materials.edit') == true"
                                   ng-click="itemBasicInfoVm.deleteAttachments(attribute,attachment)">
                                    <i class="fa fa-times"
                                       style="margin-left: 5px;font-size: 14px;color: darkred"></i>
                                </a>
                            </li>
                        </ul>
                    </div>

                    <div>
                        <a href="">
                        <span ng-show="attribute.showAttachment == false && hasPermission('permission.materials.edit') == true"
                              ng-click="itemBasicInfoVm.addAttachment(attribute)">Add attachments
                        </span>
                        </a>

                        <div ng-show="attribute.showAttachment == true">
                            <input class="browse-control" name="file" multiple="true"
                                   ng-if="itemBasicInfoVm.clearBrowse"
                                   type="file" ng-file-model="attribute.attachmentValues"
                                   style="width: 250px;"/>
                            <button class="btn btn-xs btn-primary"
                                    ng-click="itemBasicInfoVm.saveAttachments(attribute)"
                                    title="Save Attachment"
                                    style="margin-top:-62px;margin-left: 255px;padding-top:8px;">
                                <i class="fa fa-check"></i>
                            </button>
                            <button class="btn btn-xs btn-default"
                                    ng-click="itemBasicInfoVm.cancelAttachment(attribute)"
                                    title="Cancel Changes"
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
            <br>
            <br>
            <br>
        </div>
    </div>
</div>
