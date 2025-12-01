<style>
    .label {
        font-weight: normal;
        font-size: 15px;
        color: #8e8181;
    }

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

<br>
<h4 ng-if="objectTypeAttributes.length > 0" class="section-title" style="color: #8e8181;padding-left: 36px;">
    Attributes</h4>
<br>

<div class="row" ng-repeat="attribute in objectTypeAttributes track by $index">


    <div class="label col-xs-4 col-sm-3" style="text-align: right;">
        <span>{{attribute.name}}</span>
        <span ng-if="attribute.required == true" class="asterisk">*</span>:
    </div>

    <div class="value col-xs-8 col-sm-9">


        <%-- String Attribute --%>
        <a style="color: #428bca;" href="#" ng-if="attribute.dataType == 'TEXT' && hasPermission"
           editable-text="attribute.attributeValue.stringValue"
           onaftersave="updateObjectAttribute(attribute)">{{attribute.attributeValue.stringValue || 'Click to set
            value'}}</a>
        <span ng-if="attribute.dataType == 'TEXT' && hasPermission == false">{{attribute.attributeValue.stringValue}}</span>

        <%--  Integer Attribute --%>
        <a style="color: #428bca;" href="#" ng-if="attribute.dataType == 'INTEGER' && hasPermission"
           editable-number="attribute.attributeValue.integerValue" e-step="any" e-min="1"
           onbeforesave=""
           onaftersave="updateObjectAttribute(attribute)">
            {{attribute.attributeValue.integerValue || 'Click to set value'}}
        </a>

        <p ng-if="attribute.dataType == 'INTEGER' && !hasPermission">{{attribute.attributeValue.integerValue}}</p>
        <%--  Double Attribute --%>

        <a style="color: #428bca;" href="#" ng-if="attribute.dataType == 'DOUBLE' && hasPermission"
           style="color: #7676c6;"
           editable-number="attribute.attributeValue.doubleValue" e-step="any" e-min="0.1"
           onaftersave="updateObjectAttribute(attribute)">
            {{attribute.attributeValue.doubleValue || 'Click to set value'}}
        </a>
        <span ng-if="attribute.dataType == 'DOUBLE' && !hasPermission">{{attribute.attributeValue.doubleValue}}</span>
        <%--  Date Attribute --%>

        <a style="color: #428bca;" href="" ng-if="attribute.dataType == 'DATE' && hasPermission"
           ng-hide="attribute.editMode == true"
           ng-click="updateToEditMode(attribute)"
           title="change Date">
            {{(attribute.attributeValue.dateValue | date:"dd/MM/yyyy") || 'Click to set value'}}
        </a>
        <span ng-if="attribute.dataType == 'DATE' && !hasPermission">{{attribute.attributeValue.dateValue}}</span>

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
                    ng-click="updateObjectAttribute(attribute)"><i
                    class="fa fa-check"></i>
            </button>

            <button class="btn btn-md btn-default" type="button"
                    style="padding: 8px 12px;"
                    ng-click="cancelChanges(attribute)"><i class="fa fa-times"></i>
            </button>
        </div>

        <%--  Boolean Attribute --%>

        <a style="color: #428bca;" href=""
           ng-if="attribute.dataType == 'BOOLEAN' && attribute.attributeValue.booleanValue == null && hasPermission"
           editable-radiolist="attribute.attributeValue.booleanValue"
           e-ng-options="s.value as s.name for s in flags track by s.value"
           onaftersave="saveBooleanProperties(attribute)"
           ng-model="attribute.attributeValue.booleanValue">
            {{'Click to set value' }}
        </a>

        <a style="color: #428bca;" href=""
           ng-if="attribute.dataType == 'BOOLEAN' && attribute.attributeValue.booleanValue != null && hasPermission"
           editable-radiolist="attribute.attributeValue.booleanValue"
           e-ng-options="s.value as s.name for s in flags track by s.value"
           onaftersave="saveBooleanProperties(attribute)"
           ng-model="attribute.attributeValue.booleanValue">
            {{attribute.attributeValue.booleanValue}}
        </a>
        <span ng-if="attribute.dataType == 'BOOLEAN' && !hasPermission">{{attribute.attributeValue.booleanValue}}</span>
        <%--  List Attribute --%>

        <a style="color: #428bca;" ng-if="attribute.dataType == 'LIST' && hasPermission"
           title="Click to change listValue"
           editable-select="attribute.attributeValue.listValue"
           href="" e-style="width:200px;" style="color: #7676c6;"
           e-ng-options="value for value in attribute.lov.values track by value"
           onaftersave="updateObjectAttribute(attribute)">
            {{attribute.attributeValue.listValue || 'Click to set value' }}
        </a>
        <span ng-if="attribute.dataType == 'LIST' && !hasPermission">{{attribute.attributeValue.listValue}}</span>
        <%--  Time Attribute --%>

        <a style="color: #428bca;"
           ng-if="attribute.dataType == 'TIME' && hasPermission && attribute.showTimeAttribute == false"
           href="" title="Click to change time"
           ng-click="changeTime(attribute)">
            {{attribute.attributeValue.timeValue || 'Click to set value'}}
        </a>
        <span ng-if="attribute.dataType == 'TIME' && !hasPermission">{{attribute.attributeValue.timeValue}}</span>

        <div ng-if="attribute.dataType == 'TIME' && attribute.showTimeAttribute == true">

            <input style="width: 200px;" class="form-control" type="text" ng-model="attribute.editTimeValue"
                   time-picker>

            <button class="btn btn-md btn-primary" type="button"
                    style="margin-top:-63px;margin-left:22%;padding: 8px 12px;"
                    ng-click="saveTimeProperty(attribute)"><i
                    class="fa fa-check"></i>
            </button>

            <button class="btn btn-md btn-default" type="button"
                    style="margin-top:-63px;padding: 8px 12px;"
                    ng-click="cancelTime(attribute)"><i class="fa fa-times"></i>
            </button>
        </div>

        <%--  TimeStamp Attribute --%>

        <a style="color: #428bca;"
           ng-if="attribute.dataType == 'TIMESTAMP' && hasPermission && attribute.showTimestamp == false"
           href="" title="Click to change timeStamp"
           ng-click="changeTimestamp(attribute)">
            {{attribute.attributeValue.timestampValue || 'Click to set value'}}
        </a>
        <span ng-if="attribute.dataType == 'TIMESTAMP' && !hasPermission">{{attribute.attributeValue.timestampValue}}</span>

        <div ng-if="attribute.showTimestamp == true">
            <div uib-timepicker
                 ng-model="attribute.timestampValue">
            </div>
            <button class="btn btn-md btn-primary" type="button"
                    style="margin-top:-150px;margin-left:21%;padding: 8px 12px;"
                    ng-click="saveTimeProperty(attribute)"><i
                    class="fa fa-check"></i>
            </button>

            <button class="btn btn-md btn-default" type="button"
                    style="margin-top:-150px;padding: 8px 12px;"
                    ng-click="cancelTime(attribute)"><i class="fa fa-times"></i>
            </button>
        </div>

        <%--  Image Attribute --%>

        <a style="color: #428bca;" href="" ng-click="showImage(attribute)" ng-if="attribute.dataType == 'IMAGE'"
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
               ng-if="attribute.attributeValue.imageValue == null && hasPermission"
               ng-click="change(attribute)"
               ng-hide="attribute.changeImage == true" title="Add image">
                Add Image
            </a>
            <button ng-if="attribute.attributeValue.imageValue != null && hasPermission"
                    class="btn btn-xs btn-warning"
                    ng-click="change(attribute)"
                    ng-hide="attribute.changeImage == true"
                    title="Change image"><i
                    class="fa fa-edit"></i>
            </button>
            <input ng-show="attribute.changeImage == true"
                   class="browse-control" name="file"
                   accept="image/*"
                   type="file" ng-file-model="attribute.newImageValue"
                   ng-if="clearBrowse">
            <button class="btn btn-xs btn-primary"
                    ng-click="updateObjectAttribute(attribute)"
                    ng-show="attribute.changeImage == true && clearBrowse"
                    title="Save image"
                    style="margin-top: -62px;margin-left: 33%;padding-top: 8px;"><i
                    class="fa fa-check"></i>
            </button>
            <button class="btn btn-xs btn-default"
                    ng-click="cancel(attribute)" title="Cancel changes"
                    ng-show="attribute.changeImage == true && clearBrowse"
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
               ng-if="attribute.dataType == 'CURRENCY' && attribute.changeCurrency == false && hasPermission"
               ng-click="changeCurrencyValue(attribute)"
               title="Change currency value">
                {{attribute.attributeValue.currencyValue || 'Click to set value'}}
            </a>
            <span ng-if="!hasPermission">{{attribute.attributeValue.currencyValue}}</span>

            <div ng-if="attribute.changeCurrency == true" style="width: 100px;">
                <select class="form-control" ng-model="attribute.attributeValue.currencyType"
                        ng-init="currencies[attribute.attributeValue.currencyType].id"
                        ng-options="currency.id as currency.name for currency in currencies">
                </select>
            </div>

            <div ng-if="attribute.changeCurrency == true"
                 style="width: 150px;margin-top: -40px;margin-left: 105px;">
                <input class="form-control" name="currencyValue" type="number"
                       placeholder="Enter value"
                       ng-model="attribute.attributeValue.currencyValue"/>
            </div>

            <button ng-if="attribute.changeCurrency == true" class="btn btn-sm btn-primary"
                    title="Save currency value"
                    ng-click="updateObjectAttribute(attribute)"
                    style="margin-top:-65px;margin-left: 262px;padding:8px 11px;">
                <i class="fa fa-check"></i>
            </button>

            <button ng-if="attribute.changeCurrency == true" class="btn btn-sm btn-default"
                    title="Cancel changes"
                    ng-click="cancelCurrencyChanges(attribute)"
                    style="margin-top:-65px;padding:8px 11px;">
                <i class="fa fa-times"></i>
            </button>

        </div>

        <%--  Attachment Attribute --%>

        <div ng-if="attribute.dataType == 'ATTACHMENT'">
            <div ng-if="attribute.attributeValue.attachmentValues.length > 0"
                 ng-repeat="attachment in attribute.attributeValue.attachmentValues">
                <ul>
                    <li>
                        <a href="" ng-click="openAttachment(attachment)"
                           title="Click to download attachment"
                           style="margin-bottom: 5px;color: #428bca;">
                            {{attachment.name}}
                        </a>
                        <a style="color: #428bca;" href="" title="Delete attachment" ng-if="hasPermission"
                           ng-click="deleteAttachments(attribute,attachment)">
                            <i class="fa fa-times"
                               style="margin-left: 5px;font-size: 14px;color: darkred"></i>
                        </a>
                    </li>
                </ul>
            </div>

            <a style="color: #428bca;" href="">
                         <span ng-show="attribute.showAttachment == false && hasPermission"
                               ng-click="addAttachment(attribute)">Add attachments
                         </span>
            </a>

            <div ng-show="attribute.showAttachment == true">
                <input class="browse-control" name="file" multiple="true"
                       type="file" id="file" ng-file-model="attribute.attachmentValues"/>
                <button class="btn btn-xs btn-primary"
                        ng-click="saveAttachments(attribute)"
                        title="Save attachments"
                        style="margin-top: -7%;padding-top: 8px;margin-left: 33%;">
                    <i class="fa fa-check"></i>
                </button>
                <button class="btn btn-xs btn-default"
                        ng-click="cancelAttachment(attribute)"
                        title="Cancel changes"
                        style="margin-top:-7%;padding-top:8px;">
                    <i class="fa fa-times"></i>
                </button>
            </div>
        </div>
        <!-- Object ref attribute -->
        <div ng-if="attribute.dataType == 'OBJECT'">
            <a href="" ng-click="showSelectionDialog(attribute.refType, attribute)">
                <span ng-if="attribute.attributeValue.refValue == null && hasPermission">Click to select</span>
                <span ng-if="attribute.attributeValue.refValue != null && hasPermission">{{attribute.refValueString}}</span>
            </a>
            <span ng-if="attribute.dataType == 'OBJECT' && !hasPermission">{{attribute.refValueString}}</span>

        </div>
        <hr>
    </div>
</div>




