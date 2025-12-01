<div style="padding: 10px;">
    <style scoped>
        .lots-panel .rdio {
            display: inline-block;
            width: auto;
            margin-right: 50px;
        }

        .lots-panel .rdio input {
            width: 0;
        }

        .lots-panel .rdio label {
            padding: 0;
            margin-left: 15px;
        }

        .lots-table {
            width: 200px;
            margin-left: auto;
            margin-right: auto;
            border: 1px solid #ddd;
        }
    </style>

    <div ng-if="lotAllocationVm.lotMode == 'NEW'">
        <div class="text-center lots-panel">
            <h5 class="mb20">Inward item quantity :
                {{lotAllocationVm.inwardItem.fractionalQuantity}}
            </h5>

            <%--<div class="rdio rdio-primary" ng-click="lotAllocationVm.setMode('single')">
                <input name="lots" id="singleLot" type="radio" checked>
                <label for="singleLot">Single Lot</label>
            </div>

            <div class="rdio rdio-primary" ng-click="lotAllocationVm.setMode('multiple')">
                <input name="lots" id="multipleLots" type="radio">
                <label for="multipleLots">Multiple Lot</label>
            </div>--%>
        </div>

        <%--<hr style="margin-top: 0">
        <div ng-if="lotAllocationVm.mode == 'multiple'">
            <div class="row" style="margin-bottom: 10px;">
                <div class="col-sm-4 col-sm-offset-2 text-right" style="padding-top: 3px;">
                    <label>Number of Lots : </label>
                </div>
                <div class="col-sm-4">
                    <input type="number" class="form-control text-center" style="width: 50px;padding: 5px;" min="2"
                           ng-change="lotAllocationVm.createLots()"
                           ng-model="lotAllocationVm.numberOfLots">
                </div>
            </div>

            <div class="mt10 text-center" ng-if="lotAllocationVm.lots.length > 0">
                <table class="table table-striped lots-table">
                    <thead>
                    <th class="text-center">Lot</th>
                    <th class="text-center">Qty</th>
                    </thead>
                    <tbody>
                    <tr ng-repeat="lot in lotAllocationVm.lots">
                        <td>{{lot.name}}</td>
                        <td>
                            <input type="number" ng-model="lot.qty" class="form-control" min="1" style="width: 75px;">
                        </td>
                    </tr>
                    </tbody>
                </table>
            </div>
        </div>--%>

        <div>
            <form class="form-horizontal">
                <div class="form-group" ng-if="lotAllocationVm.mode == 'single'">
                    <label class="col-sm-4 control-label">
                        <span>Lot Quantity</span>
                        <span class="asterisk">*</span> : </label>

                    <div class="col-sm-7">
                        <h5>{{lotAllocationVm.inwardItem.fractionalQuantity}}</h5>
                    </div>
                </div>

                <attributes-view attributes="lotAllocationVm.newItemInstanceAttributes"></attributes-view>
            </form>
        </div>
    </div>

    <div ng-if="lotAllocationVm.lotMode == 'EDIT'">
        <div class="row row-eq-height">
            <div class="col-sm-12">
                <div class="item-details">
                    <div class="row" ng-if="lotAllocationVm.inwardItem.instances.length == 1">
                        <div class="label col-xs-4 col-sm-4 text-right">
                            <p>Inward Quantity : </p>
                        </div>
                        <div class="value col-xs-8 col-sm-8">
                            <span>{{lotAllocationVm.inwardItem.fractionalQuantity}}</span>
                        </div>
                    </div>
                    <div class="row" ng-if="lotAllocationVm.inwardItem.instances.length == 1">
                        <div class="label col-xs-4 col-sm-4 text-right">
                            Lot Quantity :
                        </div>
                        <div class="value col-xs-8 col-sm-8">
                            <span>{{lotAllocationVm.inwardItem.fractionalQuantity}}</span>
                        </div>
                    </div>

                    <div class="row" ng-if="lotAllocationVm.inwardItem.instances.length > 1">
                        <div class="label col-xs-4 col-sm-4 text-right">
                            <p>Inward Quantity : </p>
                        </div>
                        <div class="value col-xs-8 col-sm-8">
                            <span>{{lotAllocationVm.inwardItem.fractionalQuantity}}</span>
                        </div>
                    </div>
                    <div class="row" ng-if="lotAllocationVm.inwardItem.instances.length > 1"
                         ng-repeat="instance in lotAllocationVm.inwardItem.instances">
                        <div class="label col-xs-4 col-sm-4 text-right">
                            Lot {{$index + 1}} :
                        </div>
                        <div class="value col-xs-8 col-sm-8">
                            <span>{{instance.item.lotSize}}</span>
                        </div>
                    </div>


                    <div class="row"
                         ng-if="lotAllocationVm.instanceProperties.length != 0"
                         ng-repeat="attribute in lotAllocationVm.instanceProperties">
                        <div class="label col-xs-4 col-sm-4 text-right">
                            <span>{{attribute.attributeDef.name}}: </span>
                        </div>
                        <div class="value col-xs-8 col-sm-8">

                            <%------  String Attribute  ------%>

                                <div ng-if="attribute.attributeDef.dataType == 'TEXT'">
                                <span>
                                    <a ng-if="attribute.editMode == false"
                                       href="" ng-click="lotAllocationVm.changeAttribute(attribute)"
                                       title="Click to change value">
                                        {{attribute.value.stringValue || 'Click to set value'}}
                                    </a>
                                </span>
                            </div>

                            <%------  Integer Attribute  ------%>

                            <div id="intClick" ng-if="attribute.attributeDef.dataType == 'INTEGER'">
                                <span>
                                    <a ng-if="attribute.editMode == false"
                                       href="" ng-click="lotAllocationVm.changeAttribute(attribute)"
                                       title="Click to change value">
                                        {{attribute.value.integerValue || 'Click to set value'}}
                                    </a>
                                </span>
                            </div>

                            <%------  Double Attribute  ------%>

                            <div ng-if="attribute.attributeDef.dataType == 'DOUBLE'">
                                <span>
                                    <a ng-if="attribute.editMode == false"
                                       href="" ng-click="lotAllocationVm.changeAttribute(attribute)"
                                       title="Click to change value">
                                        {{attribute.value.doubleValue || 'Click to set value'}}
                                    </a>
                                </span>
                            </div>

                            <%------  Date Attribute  ------%>

                            <div ng-if="attribute.attributeDef.dataType == 'DATE'">
                                <span>
                                    <a ng-if="attribute.editMode == false"
                                       href="" ng-click="lotAllocationVm.changeAttribute(attribute)"
                                       title="Click to change date">
                                        {{ (attribute.value.dateValue | date:"dd/MM/yyyy") || 'Click to set value'}}
                                    </a>
                                </span>
                            </div>

                            <%------  Boolean Attribute  ------%>

                            <div ng-if="attribute.attributeDef.dataType == 'BOOLEAN'">
                                <span>
                                    <a ng-if="attribute.editMode == false && attribute.value.booleanValue != null"
                                       href="" ng-click="lotAllocationVm.changeAttribute(attribute)">
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

                                <button ng-if="attribute.attributeDef.dataType == 'BOOLEAN'"
                                        class="btn btn-sm btn-primary"
                                        type="button" title="Save"
                                        style="padding: 8px;width:33px;"
                                        ng-click="lotAllocationVm.saveInstanceProperties(attribute)"><i
                                        class="fa fa-check"></i>
                                </button>
                                <button ng-if="attribute.attributeDef.dataType == 'BOOLEAN'"
                                        class="btn btn-sm btn-default"
                                        type="button" title="Cancel"
                                        style="padding: 8px;width:33px;"
                                        ng-click="lotAllocationVm.cancelChanges(attribute)"><i class="fa fa-times"></i>
                                </button>

                                <button ng-if="attribute.attributeDef.dataType != 'BOOLEAN'"
                                        class="btn btn-sm btn-primary"
                                        type="button" title="Save"
                                        style="margin-top:-65px;margin-left:205px;padding: 8px;width:33px;"
                                        ng-click="lotAllocationVm.saveInstanceProperties(attribute)"><i
                                        class="fa fa-check"></i>
                                </button>
                                <button ng-if="attribute.attributeDef.dataType != 'BOOLEAN'"
                                        class="btn btn-sm btn-default"
                                        type="button" title="Cancel"
                                        style="margin-top:-65px;padding: 8px;width:33px;"
                                        ng-click="lotAllocationVm.cancelChanges(attribute)"><i class="fa fa-times"></i>
                                </button>
                            </div>

                            <%-------  List Attribute  ------%>

                            <div ng-if="attribute.attributeDef.dataType == 'LIST'">
                            <span>
                                <a title="Click to change value"
                                   editable-select="attribute.value.listValue"
                                   href="" e-style="width:200px;"
                                   e-ng-options="value for value in attribute.attributeDef.lov.values track by value"
                                   onaftersave="lotAllocationVm.saveInstanceProperties(attribute)">
                                    {{attribute.value.listValue || 'Click to set value'}}
                                </a>
                            </span>
                            </div>

                            <%-----  Image Attribute  -----%>

                            <div ng-if="attribute.attributeDef.dataType == 'IMAGE'">
                                <a href=""
                                   ng-click="lotAllocationVm.showImageProperty(attribute)"
                                   title="Click to show large image">
                                    <img ng-if="attribute.value.imageValue != null"
                                         ng-src="{{attribute.value.itemImagePath}}"
                                         style="height: 100px;width: 100px;margin-bottom: 5px;">
                                </a><br ng-if="attribute.value.imageValue != null">

                                <%-- To show large Image--%>

                                <div id="myModal22" class="img-model modal">
                                    <span class="closeimage">&times;</span>
                                    <img class="modal-content" id="img01">
                                </div>

                                <div>
                                    <a href=""
                                       ng-if="attribute.value.imageValue == null"
                                       ng-click="lotAllocationVm.change(attribute)"
                                       ng-hide="attribute.changeImage == true"
                                       title="Click to add image">Add Image
                                    </a>
                                    <button ng-if="attribute.value.imageValue != null"
                                            class="btn btn-sm btn-warning"
                                            ng-click="lotAllocationVm.change(attribute)"
                                            ng-hide="attribute.changeImage == true"
                                            title="Click to change image">
                                        <i class="fa fa-edit"></i>
                                    </button>
                                    <input ng-show="attribute.changeImage == true" class="browse-control" name="file"
                                           id="itemImageFile"
                                           type="file" ng-file-model="attribute.newImageValue" style="width: 250px;">
                                    <button class="btn btn-sm btn-primary"
                                            ng-click="lotAllocationVm.saveImage(attribute)"
                                            ng-show="attribute.changeImage == true" title="Save Image"
                                            style="margin-top:-62px;margin-left: 255px;padding-top:8px;">
                                        <i class="fa fa-check"></i>
                                    </button>
                                    <button class="btn btn-sm btn-default"
                                            ng-click="lotAllocationVm.cancel(attribute)"
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
                                            <a href="" ng-click="lotAllocationVm.openPropertyAttachment(attachment)"
                                               title="Click to download attachment"
                                               style="margin-bottom: 5px;width:250px;color: #002451;">
                                                {{attachment.name}}
                                            </a>
                                            <a href=""
                                               title="Delete Attachment"
                                               ng-click="lotAllocationVm.deleteAttachments(attribute,attachment)">
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
                                             ng-click="lotAllocationVm.addAttachment(attribute)">
                                            <span>Add Attachments</span>
                                        </div>
                                    </a>

                                    <div ng-show="attribute.showAttachment == true">
                                        <input class="form-control" name="file"
                                               id="instanceFile" multiple="true"
                                               type="file" ng-file-model="attribute.attachmentValues"
                                               style="width: 200px;padding: 5px;"/>
                                        <button class="btn btn-sm btn-primary" title="Save Atachments"
                                                ng-click="lotAllocationVm.saveAttachments(attribute)"
                                                style="margin-top:-62px;margin-left: 205px;padding-top:8px;">
                                            <i class="fa fa-check"></i>
                                        </button>
                                        <button class="btn btn-sm btn-default" title="Cancel"
                                                ng-click="lotAllocationVm.cancelAttachment(attribute)"
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
                                   href="" ng-click="lotAllocationVm.changeTime(attribute)" title="Click to change">
                                    {{attribute.value.timeValue || 'Click to set value'}}
                                </a>
                            </span>

                                <div ng-if="attribute.showTimeAttribute == true">
                                    <input time-picker type="text" class="form-control"
                                           style="width: 163px;padding: 7px;"
                                           placeholder="Select Time" ng-model="attribute.timeValue"/>
                                    <button class="btn btn-sm btn-primary" type="button" title="Save"
                                            style="margin-top:-60px;margin-left:165px;"
                                            ng-click="lotAllocationVm.saveTimeProperty(attribute)"><i
                                            class="fa fa-check"></i>
                                    </button>

                                    <button class="btn btn-sm btn-default" type="button" style="margin-top:-60px;"
                                            title="Cancel"
                                            ng-click="lotAllocationVm.cancelTime(attribute)"><i class="fa fa-times"></i>
                                    </button>
                                </div>
                            </div>

                            <%------  Timestamp Attribute  ------%>

                            <div ng-if="attribute.attributeDef.dataType == 'TIMESTAMP'">
                            <span>
                                {{attribute.value.timestampValue}}
                            </span>
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
                                       ng-click="lotAllocationVm.changeAttribute(attribute)">
                                        {{attribute.value.currencyValue || 'Click to set value'}}
                                    </a>
                                </span>
                                </div>


                                <%----------  For newly added 'CURRENCY' property after ITEM Creation ---------------%>

                                <div ng-if="attribute.value.encodedCurrencyType == null || attribute.value.currencyType == null">
                                    <div ng-if="attribute.changeCurrency == true" style="width: 150px;">
                                        <select class="form-control" ng-model="attribute.value.currencyType"
                                                ng-init="attribute.value.currencyType = lotAllocationVm.currencies[0].id"
                                                ng-options="currency.id as currency.name for currency in lotAllocationVm.currencies">
                                        </select>
                                    </div>
                                <span>
                                    <a href=""
                                       ng-if="attribute.changeCurrency == false"
                                       ng-click="lotAllocationVm.changeCurrencyValue(attribute)"
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
                                            ng-click="lotAllocationVm.saveInstanceProperties(attribute)"
                                            style="margin-top:-64px;margin-left: 260px;padding-top:10px;">
                                        <i class="fa fa-check"></i>
                                    </button>
                                    <button ng-if="attribute.changeCurrency == true" class="btn btn-sm btn-default"
                                            title="Cancel"
                                            ng-click="lotAllocationVm.cancelChanges(attribute)"
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
    </div>
</div>