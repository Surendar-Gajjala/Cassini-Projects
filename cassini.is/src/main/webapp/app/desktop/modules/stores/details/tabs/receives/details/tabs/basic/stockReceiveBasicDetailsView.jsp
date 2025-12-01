<style scoped>

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
        /*overflow: auto; /!* Enable scroll if needed *!/*/
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

<div ng-if="basicVm.loading == true" style="padding: 30px;">
    <br/>
                <span style="font-size: 15px;">
                    <img src="app/assets/bower_components/cassini-platform/images/loaders/loader6.gif" class="mr5">Loading...
                </span>
    <br/>
</div>
<div class="row row-eq-height" style="margin: 0;">
    <div class="item-details col-sm-10 col-sm-offset-1" style="padding: 30px;">

        <div class="row">
            <div class="label col-xs-4 col-sm-3 text-right">
                <span>Receive Number: </span>
            </div>
            <div class="value col-xs-8 col-sm-9">
                <span>{{basicVm.stockReceive.receiveNumberSource}}</span>
            </div>
        </div>
        <div class="row">
            <div class="label col-xs-4 col-sm-3 text-right">
                <span>Receive Type: </span>
            </div>
            <div class="value col-xs-8 col-sm-9">
                <span>{{basicVm.stockReceive.materialReceiveType.name}}</span>
            </div>
        </div>
        <div class="row" ng-if="basicVm.stockReceive.projectName != null">
            <div class="label col-xs-4 col-sm-3 text-right">
                <span>Project: </span>
            </div>
            <div class="value col-xs-8 col-sm-9">
                <span>{{basicVm.stockReceive.projectName}}</span>
            </div>
        </div>

        <div class="row" ng-if="basicVm.stockReceive.supplierName != null">
            <div class="label col-xs-4 col-sm-3 text-right">
                <span>Supplier: </span>
            </div>
            <div class="value col-xs-8 col-sm-9">
                <span>{{basicVm.stockReceive.supplierName}}</span>
            </div>
        </div>

        <div class="row" ng-if="basicVm.stockReceive.supplierName == null">
            <div class="label col-xs-4 col-sm-3 text-right">
                <span>Supplier: </span>
            </div>
            <div class="value col-xs-8 col-sm-3">
                <ui-select class="required-field" ng-model="basicVm.stockReceive.newSupplier"
                           on-select="basicVm.showButtons()"
                           theme="bootstrap">
                    <ui-select-match placeholder="Select Supplier ">{{$select.selected.name}}
                    </ui-select-match>
                    <ui-select-choices repeat="supplier in basicVm.supplierList | filter: $select.search"
                                       style="max-height: 120px;">
                        <div ng-bind="supplier.name | highlight: $select.name.search"></div>
                    </ui-select-choices>
                </ui-select>
            </div>
            <div class="btn-group" ng-if="basicVm.show">
                <button class="btn btn-primary btn-sm" title="Update" aria-label="Submit"
                        ng-click="basicVm.basicVm.updateStockReceive()"><span class="glyphicon glyphicon-ok"></span>
                </button>
                <button class="btn btn-default btn-sm" ng-click="basicVm.basicVm.cancelEdit()" title="Cancel"
                        aria-label="Cancel"><span class="glyphicon glyphicon-remove"></span></button>
            </div>
        </div>

        <div class="row">
            <div class="label col-xs-4 col-sm-3 text-right">
                <span>Notes: </span>
            </div>
            <div class="value col-xs-8 col-sm-9">
                <a style="color:#428bca;" href="#" ng-if="hasPermission('permission.receives.editReceive')"
                   editable-text="basicVm.stockReceive.notes"
                   onaftersave="basicVm.updateStockReceive()">
                    {{basicVm.stockReceive.notes || 'Click to enter notes'}}</a>

                <p ng-if="hasPermission('permission.receives.editReceive') == false">
                    {{basicVm.stockReceive.notes}}</p>
            </div>
        </div>

        <div class="row">
            <div class="label col-xs-4 col-sm-3 text-right">
                <span>Created On: </span>
            </div>
            <div class="value col-xs-8 col-sm-9">
                <span>{{basicVm.stockReceive.createdDate}}</span>
            </div>
        </div>
        <div class="row">
            <div class="label col-xs-4 col-sm-3 text-right">
                <span>Created By: </span>
            </div>
            <div class="value col-xs-8 col-sm-9">
                <span>{{basicVm.stockReceive.createdByObject.fullName}}</span>
            </div>
        </div>
        <div class="row">
            <div class="label col-xs-4 col-sm-3 text-right">
                <span>Modified On: </span>
            </div>
            <div class="value col-xs-8 col-sm-9">
                <span>{{basicVm.stockReceive.modifiedDate}}</span>
            </div>
        </div>
        <div class="row">
            <div class="label col-xs-4 col-sm-3 text-right">
                <span>Modified By: </span>
            </div>
            <div class="value col-xs-8 col-sm-9">
                <span>{{basicVm.stockReceive.modifiedByObject.fullName}}</span>
            </div>
        </div>

        <attributes-details-view attribute-id="receiveId" attribute-type="RECEIVE"
                                 has-permission="hasPermission('permission.receives.editReceive')"></attributes-details-view>

        <div class="row" ng-repeat="attribute in basicVm.objectTypeAttributes track by $index">


            <div class="label col-xs-4 col-sm-3" style="text-align: right;">
                <span>{{attribute.name}}</span>
                <span ng-if="attribute.required == true" class="asterisk">*</span>:
            </div>

            <div class="value col-xs-8 col-sm-9">


                <%-- String Attribute --%>
                <a style="color: #428bca;" href="#"
                   ng-if="attribute.dataType == 'TEXT' && hasPermission('permission.receives.editReceive')"
                   editable-text="attribute.attributeValue.stringValue"
                   onaftersave="basicVm.updateObjectAttribute(attribute)">{{attribute.attributeValue.stringValue ||
                    'Click
                    to set
                    value'}}</a>
                <span ng-if="attribute.dataType == 'TEXT' && hasPermission('permission.receives.editReceive') == false">{{attribute.attributeValue.stringValue}}</span>

                <%--  Integer Attribute --%>
                <a style="color: #428bca;" href="#"
                   ng-if="attribute.dataType == 'INTEGER' && hasPermission('permission.receives.editReceive')"
                   editable-number="attribute.attributeValue.integerValue" e-step="any" e-min="1"
                   onbeforesave=""
                   onaftersave="basicVm.updateObjectAttribute(attribute)">
                    {{attribute.attributeValue.integerValue || 'Click to set value'}}
                </a>

                <p ng-if="attribute.dataType == 'INTEGER' && !hasPermission('permission.receives.editReceive')">
                    {{attribute.attributeValue.integerValue}}</p>
                <%--  Double Attribute --%>

                <a style="color: #428bca;" href="#"
                   ng-if="attribute.dataType == 'DOUBLE' && hasPermission('permission.receives.editReceive')"
                   style="color: #7676c6;"
                   editable-number="attribute.attributeValue.doubleValue" e-step="any" e-min="0.1"
                   onaftersave="basicVm.updateObjectAttribute(attribute)">
                    {{attribute.attributeValue.doubleValue || 'Click to set value'}}
                </a>
                <span ng-if="attribute.dataType == 'DOUBLE' && !hasPermission('permission.receives.editReceive')">{{attribute.attributeValue.doubleValue}}</span>
                <%--  Date Attribute --%>

                <a style="color: #428bca;" href=""
                   ng-if="attribute.dataType == 'DATE' && hasPermission('permission.receives.editReceive')"
                   ng-hide="attribute.editMode == true"
                   ng-click="basicVm.updateToEditMode(attribute)"
                   title="Change Date">
                    {{(attribute.attributeValue.dateValue | date:"dd/MM/yyyy") || 'Click to set value'}}
                </a>
                <span ng-if="attribute.dataType == 'DATE' && !hasPermission('permission.receives.editReceive')">{{attribute.attributeValue.dateValue}}</span>

                <div ng-if="attribute.editMode == true && attribute.dataType == 'DATE'" class="input-group"
                     style="width: 200px;">
                    <input type="text" class="form-control" date-picker
                           ng-model="attribute.attributeValue.dateValue"
                           name="attDate" placeholder="dd/mm/yyyy">
            <span class="input-group-addon">
                    <i class="glyphicon glyphicon-calendar"></i>
                </span>
                </div>
                <div ng-if="attribute.editMode == true && attribute.dataType == 'DATE'"
                     style="margin-left: 3px;margin-top: -41px;">
                    <button class="btn btn-md btn-primary" type="button"
                            style="margin-left:202px;padding: 8px 12px;"
                            ng-click="basicVm.updateObjectAttribute(attribute)"><i
                            class="fa fa-check"></i>
                    </button>

                    <button class="btn btn-md btn-default" type="button"
                            style="padding: 8px 12px;"
                            ng-click="basicVm.cancelChanges(attribute)"><i class="fa fa-times"></i>
                    </button>
                </div>

                <%--  Boolean Attribute --%>

                <a style="color: #428bca;" href=""
                   ng-if="attribute.dataType == 'BOOLEAN' && attribute.attributeValue.booleanValue == null && hasPermission('permission.receives.editReceive')"
                   editable-radiolist="attribute.attributeValue.booleanValue"
                   e-ng-options="s.value as s.name for s in basicVm.flags track by s.value"
                   onaftersave="basicVm.saveBooleanProperties(attribute)"
                   ng-model="attribute.attributeValue.booleanValue">
                    {{'Click to set value' }}
                </a>

                <a style="color: #428bca;" href=""
                   ng-if="attribute.dataType == 'BOOLEAN' && attribute.attributeValue.booleanValue != null && hasPermission('permission.receives.editReceive')"
                   editable-radiolist="attribute.attributeValue.booleanValue"
                   e-ng-options="s.value as s.name for s in flags track by s.value"
                   onaftersave="basicVm.saveBooleanProperties(attribute)"
                   ng-model="attribute.attributeValue.booleanValue">
                    {{attribute.attributeValue.booleanValue}}
                </a>
                <span ng-if="attribute.dataType == 'BOOLEAN' && !hasPermission('permission.receives.editReceive')">{{attribute.attributeValue.booleanValue}}</span>
                <%--  List Attribute --%>

                <a style="color: #428bca;"
                   ng-if="attribute.dataType == 'LIST' && hasPermission('permission.receives.editReceive')"
                   title="Click to change list value"
                   editable-select="attribute.attributeValue.listValue"
                   href="" e-style="width:200px;" style="color: #7676c6;"
                   e-ng-options="value for value in attribute.lov.values track by value"
                   onaftersave="basicVm.updateObjectAttribute(attribute)">
                    {{attribute.attributeValue.listValue || 'Click to set value' }}
                </a>
                <span ng-if="attribute.dataType == 'LIST' && !hasPermission('permission.receives.editReceive')">{{attribute.attributeValue.listValue}}</span>
                <%--  Time Attribute --%>

                <a style="color: #428bca;"
                   ng-if="attribute.dataType == 'TIME' && hasPermission('permission.receives.editReceive') && attribute.showTimeAttribute == false"
                   href="" title="Click to change time"
                   ng-click="basicVm.changeTime(attribute)">
                    {{attribute.attributeValue.timeValue || 'Click to set value'}}
                </a>
                <span ng-if="attribute.dataType == 'TIME' && !hasPermission('permission.receives.editReceive')">{{attribute.attributeValue.timeValue}}</span>

                <div ng-if="attribute.dataType == 'TIME' && attribute.showTimeAttribute == true">

                    <input style="width: 200px;" class="form-control" type="text" ng-model="attribute.editTimeValue"
                           time-picker>

                    <button class="btn btn-md btn-primary" type="button"
                            style="margin-top:-63px;margin-left:22%;padding: 8px 12px;"
                            ng-click="basicVm.saveTimeProperty(attribute)"><i
                            class="fa fa-check"></i>
                    </button>

                    <button class="btn btn-md btn-default" type="button"
                            style="margin-top:-63px;padding: 8px 12px;"
                            ng-click="basicVm.cancelTime(attribute)"><i class="fa fa-times"></i>
                    </button>
                </div>

                <%--  TimeStamp Attribute --%>

                <a style="color: #428bca;"
                   ng-if="attribute.dataType == 'TIMESTAMP' && hasPermission('permission.receives.editReceive') && attribute.showTimestamp == false"
                   href="" title="Click to change timestamp"
                   ng-click="basicVm.changeTimestamp(attribute)">
                    {{attribute.attributeValue.timestampValue || 'Click to set value'}}
                </a>
                <span ng-if="attribute.dataType == 'TIMESTAMP' && !hasPermission('permission.receives.editReceive')">{{attribute.attributeValue.timestampValue}}</span>

                <div ng-if="attribute.showTimestamp == true">
                    <div uib-timepicker
                         ng-model="attribute.timestampValue">
                    </div>
                    <button class="btn btn-md btn-primary" type="button"
                            style="margin-top:-150px;margin-left:21%;padding: 8px 12px;"
                            ng-click="basicVm.saveTimeProperty(attribute)"><i
                            class="fa fa-check"></i>
                    </button>

                    <button class="btn btn-md btn-default" type="button"
                            style="margin-top:-150px;padding: 8px 12px;"
                            ng-click="basicVm.cancelTime(attribute)"><i class="fa fa-times"></i>
                    </button>
                </div>

                <%--  Image Attribute --%>

                <a style="color: #428bca;" href="" ng-click="basicVm.showImage(attribute)"
                   ng-if="attribute.dataType == 'IMAGE'"
                   title="Click to show large image">
                    <img ng-if="attribute.attributeValue.imageValue != null"
                         ng-src="{{attribute.attributeValue.imagePath}}"
                         style="height: 100px;width: 100px;margin-bottom: 5px;">
                </a>

                <div id="myModal2" class="img-model modal">
                    <span class="closeImage">&times;</span>
                    <img class="modal-content" id="img03">
                </div>

                <div ng-if="attribute.dataType == 'IMAGE'">
                    <a style="color: #428bca;" href=""
                       ng-if="attribute.attributeValue.imageValue == null && hasPermission('permission.receives.editReceive')"
                       ng-click="basicVm.change(attribute)"
                       ng-hide="attribute.changeImage == true" title="Add Image">
                        Add Image
                    </a>
                    <button ng-if="attribute.attributeValue.imageValue != null && hasPermission('permission.receives.editReceive')"
                            class="btn btn-xs btn-warning"
                            ng-click="basicVm.change(attribute)"
                            ng-hide="attribute.changeImage == true"
                            title="Change image"><i
                            class="fa fa-edit"></i>
                    </button>
                    <input ng-show="attribute.changeImage == true"
                           class="browse-control" name="file"
                           accept="image/*"
                           type="file" ng-file-model="attribute.newImageValue"
                           ng-if="basicVm.clearBrowse">
                    <button class="btn btn-xs btn-primary"
                            ng-click="basicVm.updateObjectAttribute(attribute)"
                            ng-show="attribute.changeImage == true && basicVm.clearBrowse"
                            title="Save image"
                            style="margin-top: -62px;margin-left: 33%;padding-top: 8px;"><i
                            class="fa fa-check"></i>
                    </button>
                    <button class="btn btn-xs btn-default"
                            ng-click="basicVm.cancel(attribute)" title="Cancel Changes"
                            ng-show="attribute.changeImage == true && basicVm.clearBrowse"
                            style="margin-top:-62px;padding-top:8px;"><i
                            class="fa fa-times"></i>
                    </button>
                </div>

                <%--  Currency Attribute --%>

                <div>

            <span ng-init="currencyType = attribute.name+'type'"
                  ng-if="attribute.dataType == 'CURRENCY' && attribute.changeCurrency == false"
                  ng-bind-html="attribute.attributeValue[currencyType]" style="color: #6c6cbc">
                        </span>
                    <a style="color: #428bca;" href=""
                       ng-if="attribute.dataType == 'CURRENCY' && attribute.changeCurrency == false && hasPermission('permission.receives.editReceive')"
                       ng-click="basicVm.changeCurrencyValue(attribute)"
                       title="Change currency value">
                        {{attribute.attributeValue.currencyValue || 'Click to set value'}}
                    </a>
                    <span ng-if="!hasPermission('permission.receives.editReceive')">{{attribute.attributeValue.currencyValue}}</span>

                    <div ng-if="attribute.changeCurrency == true" style="width: 100px;">
                        <select class="form-control" ng-model="attribute.attributeValue.currencyType"
                                ng-init="currencies[attribute.attributeValue.currencyType].id"
                                ng-options="currency.id as currency.name for basicVm.currency in currencies">
                        </select>
                    </div>

                    <div ng-if="attribute.changeCurrency == true"
                         style="width: 150px;margin-top: -40px;margin-left: 105px;">
                        <input class="form-control" name="currencyValue" type="number"
                               placeholder="Enter value"
                               ng-model="attribute.attributeValue.currencyValue"/>
                    </div>

                    <button ng-if="attribute.changeCurrency == true" class="btn btn-sm btn-primary"
                            title="Apply Changes"
                            ng-click="basicVm.updateObjectAttribute(attribute)"
                            style="margin-top:-65px;margin-left: 262px;padding:8px 11px;">
                        <i class="fa fa-check"></i>
                    </button>

                    <button ng-if="attribute.changeCurrency == true" class="btn btn-sm btn-default"
                            title="Cancel Changes"
                            ng-click="basicVm.cancelCurrencyChanges(attribute)"
                            style="margin-top:-65px;padding:8px 11px;">
                        <i class="fa fa-times"></i>
                    </button>

                </div>

                <%--  Attachment Attribute --%>

                <div ng-if="attribute.dataType == 'ATTACHMENT'">
                    <div ng-if="attribute.attributeValue.attachmentValues.length > 0"
                         ng-repeat="attachment in attribute.attributeValue.attachmentValues track by $index">
                        <ul>
                            <li>
                                <a href="" ng-click="basicVm.openAttachment(attachment)"
                                   title="Click to download attachment"
                                   style="margin-bottom: 5px;color: #428bca;">
                                    {{attachment.name}}
                                </a>
                                <a style="color: #428bca;" href="" title="Delete attachment"
                                   ng-if="hasPermission('permission.receives.editReceive')"
                                   ng-click="basicVm.deleteAttachments(attribute,attachment)">
                                    <i class="fa fa-times"
                                       style="margin-left: 5px;font-size: 14px;color: darkred"></i>
                                </a>
                            </li>
                        </ul>
                    </div>

                    <a style="color: #428bca;" href="">
                         <span ng-show="attribute.showAttachment == false && hasPermission('permission.receives.editReceive')"
                               ng-click="basicVm.addAttachment(attribute)">Add Attachments
                         </span>
                    </a>

                    <div ng-show="attribute.showAttachment == true">
                        <input class="browse-control" name="file" multiple="true"
                               type="file" ng-file-model="attribute.attachmentValues"/>
                        <button class="btn btn-xs btn-primary"
                                ng-click="basicVm.saveAttachments(attribute)"
                                title="Save attachments"
                                style="margin-top: -7%;padding-top: 8px;margin-left: 33%;">
                            <i class="fa fa-check"></i>
                        </button>
                        <button class="btn btn-xs btn-default"
                                ng-click="basicVm.cancelAttachment(attribute)"
                                title="Cancel changes"
                                style="margin-top:-7%;padding-top:8px;">
                            <i class="fa fa-times"></i>
                        </button>
                    </div>
                </div>
                <!-- Object ref attribute -->
                <div ng-if="attribute.dataType == 'OBJECT'">
                    <a href="" ng-click="basicVm.showSelectionDialog(attribute.refType, attribute)">
                        <span ng-if="attribute.attributeValue.refValue == null && hasPermission('permission.receives.editReceive')">Click to select</span>
                        <span ng-if="attribute.attributeValue.refValue != null && hasPermission('permission.receives.editReceive')">{{attribute.refValueString}}</span>
                    </a>
                    <span ng-if="attribute.dataType == 'OBJECT' && !hasPermission('permission.receives.editReceive')">{{attribute.refValueString}}</span>
                </div>
            </div>
        </div>
    </div>
</div>
