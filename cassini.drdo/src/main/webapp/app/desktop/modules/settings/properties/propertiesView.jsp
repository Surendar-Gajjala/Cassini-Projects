<div>
    <style scoped>
        .type-selector {
            position: absolute;
            right: 20px;
            top: 40px;
            cursor: pointer;
            font-size: 18px;
            color: #86a1b7;
        }

        .custom-dropdown {
            position: fixed;
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
            margin-bottom: 10px;
            height: 220px;
        }

        .prop-container > .prop-header {
            padding: 10px;
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
            height: 32px;
            vertical-align: middle;
            padding-top: 5px;
            border-bottom: 1px dotted #ddd;
        }

        .form-row:last-child {
            border-bottom: 0;
        }

    </style>

    <div>
        <div class="text-right type-selector">
            <div class="dropdown dropdown-append-to-body">
                <div type="button" id="dropdownMenuButton"
                     ng-click="propsVm.initDropdown()"
                     data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
                    <span ng-if="propsVm.selectedType != null">{{propsVm.selectedType.label}}</span>
                    <span ng-if="propsVm.selectedType == null">Select Object Type</span>
                    <span class="caret"></span>
                </div>
                <ul id="dropdownMenu" class="dropdown-menu custom-dropdown" aria-labelledby="dropdownMenuButton"
                    style="height: 350px !important;overflow-y: auto">
                    <li ng-repeat="oType in propsVm.objectTypes">
                        <a class="dropdown-item" href=""
                           ng-click="propsVm.selectedType = oType;propsVm.loadProperties()">{{oType.label}}</a>
                    </li>
                </ul>
            </div>
        </div>

        <div ng-if="propsVm.selectedType == null" style="text-align: center;">
            <h3 style="color: lightgrey">Select Object Type</h3>
        </div>
        <div class="flex-row flex-row-center" ng-if="propsVm.selectedType != null">
            <div id="propAddButton" ng-if="hasPermission('permission.settings.new')"
                 title="New Attribute"
                 ng-click='propsVm.addProperty()'
                 class="flex-col prop-container"
                 style="display:table;">
                <div style="display: table-cell;vertical-align: middle;text-align: center;">
                    <i class="fa fa-plus" style="font-size: 100px;vertical-align: middle;color: #adadad;"></i>
                </div>
            </div>

            <div id="property{{$index}}" class="flex-col prop-container" ng-repeat="prop in propsVm.properties">
                <div id="propertyMask{{$index}}" class="prop-container-mask centered-container">
                    <div class="centered-child">
                        <h5>Please confirm to delete this attribute</h5>

                        <div>
                            <button class='btn btn-xs btn-default' ng-click='propsVm.hideMask($index)'>No
                            </button>
                            <button class='btn btn-xs btn-danger' ng-click='propsVm.deleteProperty(prop)'>
                                Yes
                            </button>
                        </div>
                    </div>
                </div>
                <div class="prop-header">
                    <h5 ng-if="!prop.editMode" ng-dblclick="prop.editMode = true;
                        $('#nameInput{{$index}}').setSelectionRange(0, this.value.length)">{{prop.name}}</h5>

                    <div ng-if="prop.editMode" style="display: flex">
                        <input type="text" style="width: 270px"
                               id="nameInput{{$index}}"
                               ng-model="prop.newName"
                               ng-enter="propsVm.applyChanges(prop)"
                               onfocus="this.setSelectionRange(0, this.value.length)">

                        <div class="buttons" style="display: inline-block; margin-left: -35px;">
                            <i title="Save" class="fa fa-check mr10"
                               ng-click="propsVm.applyChanges(prop)"></i>
                            <i title="Cancel" class="fa fa-times"
                               ng-click="propsVm.cancelChanges(prop);prop.editMode = false;"></i>
                        </div>
                    </div>
                    <div class="buttons" ng-if="!prop.editMode">
                        <i title="Edit" class="fa fa-edit mr10"
                           ng-if="hasPermission('permission.settings.edit')"
                           ng-click="propsVm.editProperty(prop)"></i>
                        <i title="Delete" class="fa fa-times"
                           ng-if="hasPermission('permission.settings.delete')"
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
                    <div style="height: 100px;">
                        <span class="text-small text-muted">Description</span> :

                        <div ng-if="!prop.editMode"
                             style="resize: none;width: 100%; height: 70px;
                             border-bottom: 1px dotted #ddd;">{{prop.description}}
                        </div>
                        <textarea ng-if="prop.editMode"
                                  ng-model="prop.newDescription"
                                  style="resize: none;width: 100%; height: 70px;"></textarea>
                    </div>
                    <div class="dropdown dropdown-append-to-body form-row">
                        <span class="text-small text-muted">Data Type</span> :
                        <span type="button" id="dropdownMenuButton{{$index}}"
                              style="margin-left: 10px; cursor: pointer"
                              ng-click="propsVm.initDatatypeDropdown($index)"
                              data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
                            <span ng-if="prop.newDataType != null">{{prop.newDataType}}</span>
                            <span ng-if="prop.newDataType == null">Select Data Type</span>
                            <span class="caret" ng-if="prop.dataTypeMode"></span>
                        </span>
                        <ul ng-if="prop.dataTypeMode" id="dropdownMenu{{$index}}" class="dropdown-menu prop-dropdown"
                            aria-labelledby="dropdownMenuButton{{$index}}" style="height: 175px;overflow: auto;">
                            <li ng-repeat="dType in propsVm.dataTypes">
                                <a class="dropdown-item" href="" ng-click="prop.newDataType = dType"
                                   style="padding: 2px;font-size: 12px;">{{dType}}</a>
                            </li>
                        </ul>
                        <span ng-if="prop.newDataType == 'LIST'" type="button" id="dropdownMenuButton2{{$index}}"
                              style="margin-left: 10px; cursor: pointer"
                              ng-click="propsVm.initLovtypeDropdown($index)"
                              data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
                            <span ng-if="prop.newLov != null">{{prop.newLov.name}}</span>
                            <span ng-if="prop.newLov == null">Select Lov</span>
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
                    <div class="form-row">
                        <span class="text-small text-muted">Required</span> :

                        <div class="ckbox ckbox-primary" style="display: inline-block; margin-left: 15px;">
                            <input value="1" id="checkbox{{index}}"
                                   ng-disabled="!prop.editMode"
                                   ng-model="prop.newRequired"
                                   checked="checked" type="checkbox">
                            <label for="checkbox{{index}}"></label>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>