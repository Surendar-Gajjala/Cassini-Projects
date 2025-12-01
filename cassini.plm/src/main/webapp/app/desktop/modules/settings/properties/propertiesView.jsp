<div style="height: 100%;overflow: auto!important;">
    <style scoped>
        .custom-dropdown {
            position: fixed;
        }
        .open>.dropdown-menu {
            display: block !important;
            position: absolute !important;
            left: auto !important;
            top: auto !important;
        }

        .flex-row {
            display: -webkit-box;
            display: -moz-box;
            display: -ms-flexbox;
            display: -webkit-flex;
            display: flex;
            margin-bottom: 10px;
            flex-wrap: wrap;
        }

        .flex-row-center {
            justify-content: center;
        }

        .flex-row > .flex-col {
            margin-left: 10px;
        }

        .prop-container {
            width: 300px;
            min-width: 300px;
            border: 1px solid #ddd;
            border-radius: 5px;
            margin-bottom: 5px;
            height: 230px;
        }

        .prop-container > .prop-header {
            padding: 8px;
            border-bottom: 1px solid #ddd;
            display: flex;
        }

        .prop-container > .prop-header > h5 {
            margin: 0 !important;
            font-size: 18px;
            display: inline-block;
        }

        .prop-container > .prop-header input {
            font-size: 18px;
        }

        .prop-container > .prop-header > input {
            flex-grow: 1;
        }

        .prop-container > .prop-header > .buttons {
            display: inline-block;
            flex-grow: 1;
            text-align: right;
        }

        .prop-container .buttons i:hover {
            color: #0390fd;
            cursor: pointer;
        }

        .prop-container > .prop-body {
            padding: 10px;
            height: 200px;
            max-height: 200px;
            overflow-y: auto;
        }

        .prop-container .prop-body .text-small {
            font-size: 12px;
            color: #82888a;
        }

        .prop-container .prop-container-mask {
            position: absolute;
            display: none;
            opacity: 1;
            background-color: #7d7d7d;;
            z-index: 9999;

            width: 300px;
            min-width: 300px;
            border: 1px solid #ddd;
            border-top-left-radius: 5px;
            border-top-right-radius: 5px;
            margin-bottom: 10px;
            margin-left: -1px;
            margin-top: -1px;
        }

        .prop-container input[type="text"] {
            border: 0;
            outline: 0;
            background: transparent;
            border-bottom: 1px dotted lightgrey;
        }

        .prop-container textarea {
            border: 0;
            outline: 0;
            background: transparent;
            border-bottom: 1px dotted lightgrey;
        }

        .prop-dropdown {
            position: fixed;
        }

        .centered-container {
            position: absolute;
            height: 100%;
            width: 100%;
            display: table;
            z-index: 9999;
        }

        .centered-child {
            display: table-cell;
            vertical-align: middle;
            text-align: center;
            color: #fff;
        }

        #propAddButton:hover {
            background-color: #0081c2;
            cursor: pointer;
        }

        #propAddButton:hover i {
            color: #fff !important;
        }

        .text-muted {
            color: #c0c0c0;
        }

        .form-row {
            height: 29px;
            vertical-align: middle;
            padding-top: 5px;
            border-bottom: 1px dotted #ddd;
        }

        .form-row:last-child {
            border-bottom: 0;
        }

        .type-selector {
            width: 250px;
            border-right: 1px solid #eee;
            padding-right: 12px;
            padding-top: 10px;
        }

        .type-selector .object-type {

        }

        .type-selector .object-type {
            padding-left: 10px;
            cursor: pointer;
            height: 32px;
            line-height: 30px;
            border-bottom: 1px dotted #ddd;
        }

        .type-selector .object-type:hover {
            background-color: #d6e1e0 !important;
        }

        .type-selector .object-type:nth-child(2n+1) {
            /*background-color: #f5f5f5;*/
        }

        .type-selector .object-type.selected-object-type,.selected-object-type:hover {
            color: white;
            background-color: #0081c2 !important;
        }

        .type-attributes {
            width: calc(100% - 300px);
            padding-top: 20px;
        }
        .mr0 {
            margin: 0;
        }

        .selected-object-type-label {
            position: absolute;
            right: 10px;
            top: 10px;
        }
    </style>

    <div style="display: flex;height: 100%;">
        <div class="type-selector">
            <div class="object-type"
                 ng-class="{'selected-object-type': oType.label == propsVm.selectedType.label}"
                 ng-repeat="oType in propsVm.objectTypes"
                 ng-click="propsVm.selectedType = oType;propsVm.loadProperties()">
                <span>{{oType.label}}</span>
            </div>
        </div>

        <div class="type-attributes">
            <!--
            <div class="selected-object-type-label text-right" ng-if="propsVm.selectedType != null">
                <h4 class="mr0">{{propsVm.selectedType.label}}</h4>
            </div>
            -->
            <div ng-if="propsVm.selectedType == null" style="text-align: center;">
                <h4 style="color: lightgrey; margin: 0" translate>SELECT_OBJECT_TYPE</h4>
            </div>
            <div class="flex-row flex-row-center" ng-if="propsVm.selectedType != null">
                <div id="propAddButton" ng-if="hasPermission('settings','create')"
                     title="{{'NEW_ATTRIBUTE' | translate}}"
                     ng-click='propsVm.addProperty()'
                     class="flex-col prop-container"
                     style="display:table;">
                    <div style="display: table-cell;vertical-align: middle;text-align: center;">
                        <i class="la la-plus" style="font-size: 100px;vertical-align: middle;color: #adadad;"></i>
                    </div>
                </div>

                <div id="property{{$index}}" class="flex-col prop-container" ng-repeat="prop in propsVm.properties">
                    <div id="propertyMask{{$index}}" class="prop-container-mask centered-container">
                        <div class="centered-child">
                            <h5 translate>DELETE_CUSTOM_PROPERTY_MESSAGE</h5>

                            <div>
                                <button class='btn btn-xs btn-default' ng-click='propsVm.hideMask($index)' translate>NO
                                </button>
                                <button class='btn btn-xs btn-danger' ng-click='propsVm.deleteProperty(prop)' translate>
                                    YES
                                </button>
                            </div>
                        </div>
                    </div>
                    <div class="prop-header">
                        <h5 ng-if="!prop.editMode"
                            style="width: 236px;word-wrap: break-word !important;white-space: normal !important;"
                            ng-dblclick="prop.editMode = true;
                        $('#nameInput{{$index}}').setSelectionRange(0, this.value.length)">

                            {{prop.ppName }}{{prop.ppName.length > 20 ? '...' : ''}}</h5>

                        <div ng-if="prop.editMode" style="display: flex">
                            <input type="text" style="width: 236px"
                                   id="nameInput{{$index}}"
                                   ng-model="prop.newName"
                                   ng-enter="propsVm.applyChanges(prop)"
                                   onfocus="this.setSelectionRange(0, this.value.length)">

                            <div class="buttons" style="display: inline-block; margin-left: 3px;">
                                <i title="{{saveAttributeTitle}}" class="la la-check"
                                   ng-click="propsVm.applyChanges(prop)"></i>
                                <i title="{{cancelChanges}}" class="la la-times"
                                   ng-click="propsVm.cancelChanges(prop);prop.editMode = false;"></i>
                            </div>
                        </div>
                        <div class="buttons" ng-if="!prop.editMode">
                            <i title="{{'EDIT_ATTRIBUTE' | translate}}" class="la la-edit"
                               ng-if="hasPermission('settings','edit')"
                               ng-click="propsVm.editProperty(prop)"></i>
                            <i title="{{deleteAttributeTitle}}" class="la la-times"
                               ng-if="hasPermission('settings','delete')"
                               ng-click="propsVm.promptDeleteProperty($index)"></i>
                        </div>
                    </div>
                    <div class="progress progress-striped active"
                         style="border-radius: 0;height: 5px;"
                         ng-if="prop.showBusy">
                        <div class="progress-bar"
                             role="progressbar" aria-valuenow="100" aria-valuemin="0"
                             aria-valuemax="100" style="width: 100%">
                        </div>
                    </div>
                    <div class="prop-body" id="prop-body{{$index}}">
                        <div style="height: 50px;">
                            <span class="text-small text-muted" style="" translate>DESCRIPTION</span> :

                            <div ng-if="!prop.editMode"
                                 style="resize: none;width: 100%;overflow: auto;height: 32px;;
                             border-bottom: 1px dotted #ddd;">{{prop.description}}
                            </div>
                            <textarea ng-if="prop.editMode"
                                      ng-model="prop.newDescription"
                                      style="resize: none;width: 100%; height: 32px;"></textarea>
                        </div>
                        <div class="dropdown-append-to-body form-row">
                            <span class="text-small text-muted" style="" translate>DATA_TYPE</span> :
                            <span type="button" id="dropdownMenuButton{{$index}}"
                                  style="margin-left: 10px; cursor: pointer"
                                  ng-click="propsVm.initDatatypeDropdown($index)"
                                  data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
                            <span ng-if="prop.newDataType != null">{{prop.newDataType}}</span>
                            <span ng-if="prop.newDataType == null" style=""
                                  translate>SELECT_DATA_TYPE</span>
                            <span class="caret" ng-if="prop.dataTypeMode"></span>
                        </span>
                            <ul ng-if="prop.dataTypeMode" id="dropdownMenu{{$index}}" class="dropdown-menu prop-dropdown"
                                aria-labelledby="dropdownMenuButton{{$index}}" style="height: 175px !important;overflow: auto">
                                <li ng-repeat="dType in propsVm.dataTypes">
                                    <a class="dropdown-item" href="" ng-click="prop.newDataType = dType"
                                       style="padding: 2px;font-size: 12px;">{{dType}}</a>
                                </li>
                            </ul>
                            <span ng-if="prop.newDataType == 'OBJECT'" type="button" id="dropdownMenuButton1{{$index}}"
                                  style="margin-left: 10px; cursor: pointer"
                                  ng-click="propsVm.initReftypeDropdown($index)"
                                  data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
                            <span ng-if="prop.newRefType != null" title="{{prop.newRefType}}">{{prop.newRefType | limitTo: 10}}</span>
                            <span ng-if="prop.newRefType == null" style="" translate>SELECT_REF_TYPE</span>
                            <span class="caret" ng-if="prop.refTypeMode"></span>
                        </span>
                            <ul ng-if="prop.refTypeMode && prop.newDataType == 'OBJECT'" id="dropdownMenu1{{$index}}"
                                class="dropdown-menu prop-dropdown"
                                aria-labelledby="dropdownMenuButton1{{$index}}" style="height: 175px;overflow: auto;">
                                <li ng-repeat="refType in propsVm.refTypes">
                                    <a class="dropdown-item" href="" ng-click="prop.newRefType = refType"
                                       style="padding: 2px;font-size: 12px;">{{refType}}</a>
                                </li>
                            </ul>
                            <span ng-if="prop.newDataType == 'LIST'" type="button" id="dropdownMenuButton2{{$index}}"
                                  style="margin-left: 10px; cursor: pointer"
                                  ng-click="propsVm.initLovtypeDropdown($index)"
                                  data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
                            <span ng-if="prop.newLov != null" title="{{prop.newLov.name}}">{{prop.newLov.name| limitTo: 21}}</span>
                            <span ng-if="prop.newLov == null" translate>SELECT_LOV</span>
                            <span class="caret" ng-if="prop.lovTypeMode"></span>
                        </span>
                            <ul ng-if="prop.lovTypeMode && prop.newDataType == 'LIST'" id="dropdownMenu2{{$index}}"
                                class="dropdown-menu prop-dropdown"
                                aria-labelledby="dropdownMenuButton2{{$index}}" style="height: 175px;overflow: auto;">
                                <li ng-repeat="lov in propsVm.lovs">
                                    <a class="dropdown-item" href="" ng-click="prop.newLov = lov"
                                       style="padding: 2px;font-size: 12px;">{{lov.name}}</a>
                                </li>
                            </ul>

                        </div>

                        <div class="form-row" ng-if="prop.newDataType == 'LIST'">
                            <span class="text-small text-muted" style="" translate>Multiple</span> :
                            <div style="display: inline-block; margin-left: 20px;">
                                <input value="1" id="checkbox1{{index}}"
                                       ng-disabled="!prop.editMode"
                                       ng-model="prop.listMultiple"
                                       checked="checked" type="checkbox">
                                <label for="checkbox1{{index}}"></label>
                            </div>

                        </div>

                        <div class="form-row" ng-if="prop.newDataType == 'TEXT'">
                            <span class="text-small text-muted" style="" translate>DEFAULT_VALUE</span> :
                            <div style="display: inline-block;margin-right: -110px;">
                                <input type="text" ng-disabled="!prop.editMode" style="width: 157px;"
                                       ng-model="prop.defaultTextValue">
                            </div>
                        </div>


                        <div class="form-row" ng-if="prop.newDataType == 'LIST'">
                        <span class="text-small text-muted" style=""
                              translate>DEFAULT_VALUE</span> :
                            <span ng-if="prop.newDataType == 'LIST'" type="button"
                                  id="dropdownMenuButton3{{$index}}"
                                  style="margin-left: 10px; cursor: pointer"
                                  ng-click="propsVm.initLovtypeDropdowns($index)"
                                  data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
                            <span ng-if="prop.newLov != null && prop.defaultListValue == null"
                                  translate>DEFAULT_VALUE</span>
                            <span ng-if="prop.defaultListValue != null" title="{{prop.defaultListValue}}">{{prop.defaultListValue | limitTo: 21}}</span>
                            <span class="caret" ng-if="prop.lovTypeMode"></span>
                        </span>
                            <ul ng-if="prop.lovTypeMode && prop.newDataType == 'LIST'" id="dropdownMenu3{{$index}}"
                                class="dropdown-menu prop-dropdown"
                                aria-labelledby="dropdownMenuButton3{{$index}}" style="height: 175px;overflow: auto;">
                                <li ng-repeat="value in prop.newLov.values">
                                    <a class="dropdown-item" href="" ng-click="prop.defaultListValue = value"
                                       style="padding: 2px;font-size: 12px;">{{value}}</a>
                                </li>
                            </ul>
                        </div>

                        <div class="form-row" style="height: 27px !important;">
                            <span class="text-small text-muted" style="" translate>REQUIRED</span> :

                            <div class="ckbox ckbox-primary" style="display: inline-block; margin-left: 15px;">
                                <input ng-value="true" id="prop{{$index}}"
                                       ng-disabled="!prop.editMode"
                                       ng-model="prop.required"
                                       checked="checked" type="checkbox">
                                <label for="prop{{$index}}"></label>
                            </div>
                            <%-- </div>
                             <div class="form-row" style="height: 27px !important;">--%>
                            <span class="text-small text-muted" style=";margin-left: 46px;"
                                  translate>VISIBLE</span> :

                            <div class="ckbox ckbox-primary" style="display: inline-block; margin-left: 15px;">
                                <input ng-value="true" id="prop1{{$index}}"
                                       ng-disabled="!prop.editMode"
                                       ng-model="prop.visible"
                                       checked="checked" type="checkbox">
                                <label for="prop1{{$index}}"></label>
                            </div>
                        </div>

                    </div>
                </div>
            </div>
        </div>
    </div>
</div>