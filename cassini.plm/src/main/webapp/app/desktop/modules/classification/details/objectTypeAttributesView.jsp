<style scoped>
    .tab-content {
        padding: 0px !important;
    }

    .tab-content .tab-pane {
        overflow-y: auto !important;
    }

    .tab-pane {
        position: relative;
    }

    .itemtype-view .nav-tabs li {
        cursor: pointer;
        border: 0 !important;
        min-width: 100px;
        text-align: center
    }

    select {
        display: inline-block;
        width: auto;
    }

    .attributeExclusions {
        display: none; /* Hidden by default */
        position: fixed; /* Stay in place */
        padding-top: 20px; /* Location of the box */
        left: 0;
        top: 0;
        width: 100%; /* Full width */
        height: 100%; /* Full height */
        overflow: auto; /* Enable scroll if needed */
        background-color: rgb(0, 0, 0); /* Fallback color */
        background-color: rgba(0, 0, 0, 0.4); /* Black w/ opacity */
    }

    .attributeExclusions .compareContentAttribte {
        margin: auto;
        display: block;
        height: 94%;
        width: 97%;
        background-color: white;
        border-radius: 7px !important;
    }

    .compareHeadre {
        padding: 5px;
        text-align: center;
        border-bottom: 1px solid lightgrey;
        height: 50px;

    }

    .exclusionBody {
        padding: 10px;
        overflow: auto;
        min-width: 100%;
        width: auto;
        height: 88% !important;
    }

    .myOwnBg {
        /* background-color: red !important;*/
        cursor: not-allowed !important;
        /*pointer-events: none !important;*/
    }

    .myOwnBg1 {

        background-color: blue !important;
    }

    .attribute-table {
        font-family: "Trebuchet MS", Arial, Helvetica, sans-serif;
        border-collapse: collapse;
        table-layout: fixed !important;
    }

    .attribute-table td, .attribute-table th {
        border: 1px solid #ddd;
        padding: 8px;
    }

    .split-pane-divider {
        z-index: 1 !important;
    }

    td.sameWidth {
        width: 50px !important;
        text-align: center !important;
    }

    div.vertical {
        position: absolute;
        width: 100px;
        transform: rotate(-90deg);
        -webkit-transform: rotate(-90deg); /* Safari/Chrome */
        -moz-transform: rotate(-90deg); /* Firefox */
        -o-transform: rotate(-90deg); /* Opera */
        -ms-transform: rotate(-90deg); /* IE 9 */
        line-height: 40px;
        padding-left: 25px;
        margin-left: -35px;
        white-space: nowrap !important;

    }

    th.vertical {
        height: 100px;
        line-height: 40px;
        padding-bottom: 20px;
        text-align: left;
        width: 30px;
    }

    .att-name {
        text-align: center;
        font-size: 16px;
        color: #337ab7 !important;

    }

    .colorAdding {
        background-color: #f4b7c8;
        /*background: url("data:image/svg+xml;utf8,<svg xmlns='http://www.w3.org/2000/svg' version='1.1' preserveAspectRatio='none' viewBox='0 0 100 100'><path d='M1 0 L0 1 L99 100 L100 99' fill='black' /><path d='M0 99 L99 0 L100 1 L1 100' fill='black' /></svg>");*/
        background-repeat: no-repeat;
        background-position: center center;
        background-size: 100% 100%, auto;
    }

    .new-lov.modal {
        display: none; /* Hidden by default */
        position: fixed; /* Stay in place */
        padding-top: 20px; /* Location of the box */
        left: 0;
        top: 50px;
        width: 100%; /* Full width */
        height: 100%; /* Full height */
        overflow: auto; /* Enable scroll if needed */
        background-color: rgb(0, 0, 0); /* Fallback color */
        background-color: rgba(0, 0, 0, 0.4); /* Black w/ opacity */
    }

    .new-lov .lov-content {
        margin: auto;
        display: block;
        height: auto;
        width: 300px;
        background-color: white;
        border-radius: 7px !important;
    }

    #lov-header {
        padding: 5px;
        text-align: center;
        border-bottom: 1px solid lightgrey;
        height: 50px;
    }

    .configuration-header {
        font-weight: bold;
        font-size: 22px;
        position: absolute;
        display: inline-block;
        left: 44%;
        top: 32px;
    }

    #lov-footer {
        border-top: 1px solid lightgrey;
        padding: 5px;
        text-align: center;
        height: 50px;
        width: 100%;
        display: flex;
    }

    .lov-header {
        padding: 10px;
        border-bottom: 1px solid #ddd;
        display: flex;
        word-break: break-all;
    }

    .lov-header > h5 {
        margin: 0 !important;
        display: inline-block;
    }

    .lov-header input {
    }

    .lov-header > input {
        flex-grow: 1;
    }

    .lov-header > .buttons {
        display: inline-block;
        flex-grow: 1;
        text-align: right;
    }

    .lov-footer .form-group {
        margin-left: auto !important;
        margin-right: auto !important;
    }

    .lov-footer .form-group button {
        width: 75px !important;
    }

    .buttons i:hover {
        color: #0390fd;
        cursor: pointer;
    }

    .lov-body {
        padding: 10px;
        height: 200px;
        max-height: 200px;
        overflow-y: auto;
    }

    .lov-body .lov-value {
        cursor: pointer;
    }

    .lov-body > .lov-value > div {
        border-bottom: 1px dotted #e3e3e3;
        padding: 5px;
        display: flex;
        justify-content: left;
    }

    .lov-body .lov-value .name,
    .lov-body .lov-value .type {
        flex-grow: 1;
        justify-content: left;
        text-align: left;
    }

    .lov-body .lov-value .buttons {
        visibility: hidden;
        text-align: right;
    }

    .lov-body .lov-value:hover .buttons {
        visibility: visible;
    }

    .lov-body .lov-value.edit-mode .buttons {
        visibility: visible;
    }

    input {
        border: 0;
        outline: 0;
        background: transparent;
        border-bottom: 1px dashed lightgrey;
    }

    .lov-body .lov-value.edit-mode .type {
        border-bottom: 1px dashed lightgrey;
        margin-right: 10px;
        text-align: left;
    }

    .lov-body .lov-value .type a {
        text-decoration: none;
        color: inherit;
    }

    .lov-body .lov-value .type .caret {
        border-top: 5px dashed;
        border-right: 5px solid transparent;
        border-left: 5px solid transparent;
    }

    .lov-body .lov-value:hover {
        background-color: #0081c2;
        color: #fff;
    }

    .lov-body .lov-value.edit-mode {
        background-color: transparent;
        color: inherit;
    }

    .lov-body .lov-value:hover .buttons i {
        cursor: pointer;
    }

    .lov-body .lov-value:hover .buttons i {
        color: #fff;
    }

    .lov-body .lov-value.edit-mode .buttons i:hover {
        color: #0390fd;
    }

    .lov-body .lov-value.edit-mode .buttons i {
        color: inherit;
    }

    .lov-body .lov-value .name input {
        width: 98%;
    }

    .exclusion-close {
        position: absolute;
        right: 35px;
        top: 25px;
        width: 38px;
        height: 38px;
        opacity: 0.3;
    }

    .exclusion-close:hover {
        opacity: 0.6;
        border-radius: 50%;
        background-color: #ddd;
    }

    .exclusion-close:before, .exclusion-close:after {
        position: absolute;
        top: 7px;
        left: 18px;
        content: ' ';
        height: 22px;
        width: 2px;
        background-color: #333;
    }

    .exclusion-close:before {
        transform: rotate(45deg) !important;
    }

    .exclusion-close:after {
        transform: rotate(-45deg) !important;
    }

    .actions-column {
        width: 100px;
        text-align: center;
    }

    .sticky-column {
        position: sticky !important;
        position: -webkit-sticky !important;
    }

    .sticky-actions-column {
        right: -10px;
    }

    .responsive-table {
        overflow: auto !important;
    }

</style>
<div class="view-container" fitcontent>
    <div class="view-content no-padding" style="overflow-y: hidden;">

        <div id="action-buttons" style="padding: 5px; border-bottom: 1px solid gray;">
            <h4 style="margin: 0;padding-bottom: 5px">Attributes</h4>
        </div>

        <div class="responsive-table" style="padding: 0 10px;height: 500px !important;">
            <table class="table table-striped highlight-row">
                <thead>
                <tr>
                    <th title="{{'NEW_ATTRIBUTE' | translate}}">
                        <i class="la la-plus" ng-click="objectTypeAttributeVm.addAttribute()"></i>
                    </th>
                    <th style="width: 200px;min-width: 150px">
                        <span translate>GROUP</span>
                    </th>
                    <th style="width: 200px;min-width: 150px" translate>NAME</th>
                    <th translate>DESCRIPTION</th>
                    <th style="width: auto ; min-width: 150px;" translate>DATA_TYPE</th>
                    <th ng-if="selectedType.attributes.nodeType == 'ITEMTYPE'" style="width: auto ; min-width: 150px;"
                        title="Quantity of Measurment" translate>QoM
                    </th>
                    <th style="width: auto" translate>DEFAULT_VALUE</th>
                    <th style="width: auto" translate>Multi List</th>
                    <th style="width: auto" ng-if="selectedType.attributes.nodeType == 'ITEMTYPE'" translate>Revision
                    </th>
                    <th style="width: 100px; text-align: center" translate>IS_REQUIRED</th>
                    <%--<th ng-if="selectedType.attributes.nodeType == 'CUSTOMOBJECTTYPE'" style="width: 100px; text-align: center"
                        translate>Is Plugin
                    </th>--%>
                    <th style="width: 100px; text-align: center" translate>VISIBLE</th>
                    <th style="width: 100px; text-align: center"
                        class="actions-column sticky-column sticky-actions-column" translate>ACTIONS
                    </th>
                </tr>
                </thead>

                <tbody>
                <tr ng-if="objectTypeAttributeVm.attributes.length == 0">
                    <td colspan="12" style="background-color: #f9fbfe  !important;color: unset !important;">
                        <div class="no-data">
                            <img src="app/assets/no_data_images/Attribute.png" alt="" class="image">

                            <div class="message">{{ 'NO_ATTRIBUTES' | translate}}</div>
                            <div class="message ng-scope" style="font-size: 14px;color: #8f979d;" translate>
                                NO_PERMISSION_MESSAGE
                            </div>
                        </div>
                    </td>
                </tr>
                <tr id="{{$index}}" ng-repeat="attribute in objectTypeAttributeVm.attributes">
                    <td></td>
                    <td>
                        <ui-select ng-model="attribute.attributeGroup" theme="bootstrap"
                                   style="width:150px"
                                   ng-if="(attribute.editMode || attribute.defaultValue) && objectTypeAttributeVm.attributeGroups.length > 0 && !attribute.newGroup"
                                   on-select="objectTypeAttributeVm.selectAttributeGroup(attribute,$item)">
                            <ui-select-match placeholder="Select Group">{{$select.selected}}
                            </ui-select-match>
                            <ui-select-choices
                                    repeat="attributeGroup in objectTypeAttributeVm.attributeGroups | filter: $select.search">
                                <div ng-bind="attributeGroup"></div>
                            </ui-select-choices>
                        </ui-select>
                        <input ng-if="(attribute.editMode || attribute.defaultValue) && attribute.newGroup"
                               type="text" class="form-control input-sm" placeholder="{{'ENTER_GROUP' | translate }}"
                               ng-model="attribute.attributeGroup" ng-enter="applyChanges(attribute)">
                        <i class="la la-times"
                           ng-if="attribute.newGroup && objectTypeAttributeVm.attributeGroups.length > 0"
                           ng-click="attribute.newGroup = false;attribute.attributeGroup = null"
                           style="position: relative;float: right;margin-top: -22px;margin-right: 5px;"></i>
                        <span ng-if="!attribute.editMode && !attribute.defaultValue">{{attribute.attributeGroup}}</span>
                        <i class="la la-pencil"
                           ng-if="objectTypeAttributeVm.attributeGroups.length > 0 && !attribute.editMode && !attribute.defaultValue && attribute.attributeGroup != null && attributeGroup != ''"
                           ng-click="objectTypeAttributeVm.editAttributeGroup(attribute)"></i>
                    </td>
                    <td style="width: 200px">
                                <span class="col-width-250" style="cursor: move" ng-if="attribute.showValues == true"
                                      title="{{attribute.name}}">{{attribute.name}}</span>
                        <input ng-if="attribute.editMode == true" type=" text" class="form-control input-sm"
                               placeholder="{{'ENTER_NAME' | translate }}"
                               ng-model="attribute.newName "
                               ng-enter="applyChanges(attribute)" autofocus>
                    </td>
                    <td class="description-column">
                        <span ng-if="attribute.showValues == true" title="{{attribute.description}}">{{attribute.description}}</span>
                        <input ng-if="attribute.editMode == true" type="text" class="form-control input-sm"
                               placeholder="{{'ENTER_DESCRIPTION_TITLE' | translate }}"
                               ng-model="attribute.newDescription">
                    </td>
                    <td style="width: 120px">
                    <span ng-if="attribute.showValues == true">
                        <span>{{attribute.dataType}}</span>
                        <span ng-if="attribute.dataType == 'OBJECT'"
                              style="margin-left: 5px;">( {{attribute.refType}} )</span>
                        <span ng-if="attribute.dataType == 'OBJECT' && attribute.refSubType != null"
                              style=" margin-left: 5px;">( {{attribute.refSubTypeName}} )</span>
                        <span ng-if="attribute.dataType == 'LIST' || attribute.dataType == 'MULTILISTCHECKBOX'"
                              style="margin-left: 5px;">( {{attribute.lov.name}} )</span>
                        <span ng-if="attribute.dataType == 'FORMULA'"
                              style="margin-left: 5px;">( {{attribute.formula}} )</span>
                    </span>

                        <div style="display: flex;">
                            <select ng-if="attribute.editMode == true" class="form-control input-sm"
                                    ng-model="attribute.newDataType"
                                    title="{{attribute.newDataType}}"
                                    ng-change="selectDataType(attribute)"
                                    ng-options="dataType for dataType in objectTypeAttributeVm.dataTypes">
                                <option value="" selected disabled hidden>Select Data Type</option>
                            </select>
                        </div>
                        <select ng-if="attribute.editMode == true && attribute.newDataType == 'OBJECT'"
                                class="form-control input-sm"
                                ng-model="attribute.newRefType"
                                ng-options="refType for refType in objectTypeAttributeVm.refTypes">
                            <option value="" selected disabled hidden>Select Object</option>
                        </select>

                    <span ng-if="attribute.editMode == true && (attribute.newDataType == 'LIST' || attribute.newDataType == 'MULTILISTCHECKBOX')">
                        <select class="form-control"
                                ng-options="lov as getLimitedWord(lov.name, 50) for lov in lovs track by lov.name"
                                ng-model="attribute.newLov"
                                ng-change="addNewLov(attribute)"
                                title="{{attribute.newLov.name}}">
                            <option value="" selected disabled hidden>Select List</option>
                        </select>
                    </span>
                        <input ng-if="attribute.editMode == true && attribute.newDataType == 'FORMULA'"
                               type="text" class="form-control input-sm"
                               placeholder="Enter formula"
                               ng-model="attribute.formula">
                    </td>

                    <td style="width: auto" ng-if="selectedType.attributes.nodeType == 'ITEMTYPE'">
                        <span ng-if="attribute.editMode == false && attribute.dataType == 'DOUBLE'">{{attribute.measurement.name}}</span>
                    <span ng-if="attribute.editMode == true && attribute.newDataType == 'DOUBLE'">
                        <select class="form-control" ng-model="attribute.newMeasurement"
                                ng-options="measurement as measurement.name for measurement in measurements track by measurement.name"
                                title="{{attribute.newMeasurement.name}}">
                            <option value="" selected disabled hidden>Select QoM</option>
                        </select>
                    </span>
                    </td>

                    <td style="width: auto;">
                    <span ng-if="attribute.editMode == true && attribute.newDataType == 'TEXT'">
                        <input type="text" class="form-control input-sm" style="width: 140px !important;"
                               ng-model="attribute.defaultTextValue"
                               placeholder="{{'ENTER_DEFAULT_VALUE' | translate }}"
                               ng-disabled="attribute.editMode == false">
                    </span>
                    <span ng-if="attribute.defaultValue == true && attribute.newDataType == 'TEXT'">
                        <input type="text" class="form-control input-sm" style="width: 140px !important;"
                               ng-model="attribute.defaultTextValue">
                    </span>
                    <span ng-if="attribute.editMode == false && attribute.defaultValue == false && attribute.dataType == 'TEXT'">
                                       <span title="{{attribute.defaultTextValue}}">{{attribute.defaultTextValue}}</span>
                    </span>

                    <span ng-if="attribute.editMode == true && attribute.newDataType == 'LIST'">
                        <select ng-if="attribute.editMode == true && attribute.newDataType == 'LIST'"
                                style="width: 140px !important;"
                                class="form-control input-sm"
                                ng-model="attribute.defaultListValue"
                                title="{{attribute.defaultListValue}}"
                                ng-disabled="attribute.editMode == false"
                                ng-options="value as getLimitedWord(value, 20) for value in attribute.newLov.values track by value">
                            <option value="" selected disabled hidden>Select Default Value</option>
                        </select>
                    </span>

                    <span ng-if="attribute.defaultValue == true && attribute.newDataType == 'LIST'">
                        <select style="width: 140px !important;"
                                class="form-control input-sm"
                                ng-model="attribute.defaultListValues"
                                title="{{attribute.defaultListValue}}"
                                ng-options="value as getLimitedWord(value, 20) for value in attribute.newLov.values track by value">
                        </select>
                    </span>
                    <span ng-if="attribute.editMode == false && attribute.defaultValue == false && attribute.dataType == 'LIST'">
                        <span ng-disabled="attribute.editMode == false">{{attribute.defaultListValue}}</span>
                    </span>
                    </td>

                    <td style="width: auto; text-align: center">
                    <span ng-if="attribute.editMode == true && attribute.newDataType == 'LIST'">
                        <input type="checkbox" class="form-control input-sm"
                               ng-model="attribute.listMultiple" ng-disabled="attribute.editMode == false">
                    </span>
                    <span ng-if="attribute.editMode == false && attribute.dataType == 'LIST'">
                        <input type="checkbox" class="form-control input-sm"
                               ng-model="attribute.listMultiple" ng-disabled="attribute.editMode == false">
                    </span>

                    </td>
                    <td ng-if="selectedType.attributes.nodeType == 'ITEMTYPE'">
                        <input type="checkbox" class="form-control input-sm"
                               ng-model="attribute.isRevision" ng-disabled="attribute.editMode == false">
                    </td>
                    <td style="width: auto; text-align: center" ng-if="objectTypeAttributeVm.objectType == 'ITEMTYPE'">
                    <span>
                        <input type="checkbox" class="form-control input-sm"
                               ng-if="attribute.editMode == true && attribute.newDataType == 'LIST'"
                               ng-model="attribute.configurable" ng-disabled="attribute.editMode == false">

                        <input type="checkbox" class="form-control input-sm"
                               ng-if="attribute.editMode == false && attribute.dataType == 'LIST'"
                               ng-model="attribute.configurable"
                               ng-disabled="attribute.editMode == false">
                    </span>
                    </td>

                    <td style="width: 100px; text-align: center">
                        <input type="checkbox" class="form-control input-sm" ng-model="attribute.required"
                               ng-disabled="attribute.editMode == false || attribute.configurable"
                               ng-hide="attribute.inVisible == true" ng-click="checkRequiredAttributes(attribute)">
                        <input type="checkbox" class="form-control input-sm" ng-model="attribute.required"
                               ng-click="checkRequiredAttributes(attribute)"
                               ng-if="attribute.inVisible == true && !attribute.configurable">
                    </td>
                    <%--<td style="width: 100px; text-align: center" ng-if="selectedType.attributes.nodeType == 'CUSTOMOBJECTTYPE'">
                        <input type="checkbox" class="form-control input-sm" ng-model="attribute.plugin"
                               ng-disabled="attribute.editMode == false">
                    </td>--%>
                    <td style="width: 100px; text-align: center">
                        <input type="checkbox" class="form-control input-sm" ng-model="attribute.visible"
                               ng-disabled="attribute.editMode == false"
                               ng-hide="attribute.inVisible == true">
                        <input type="checkbox" class="form-control input-sm" ng-model="attribute.visible"
                               ng-if="attribute.inVisible == true">
                    </td>
                    <%--   <td>
                           <i class="fa fa-pencil" title="Attribute validations" ng-click="showValidations(attribute)"
                              style="padding: 10px;"
                              ng-if="attribute.editMode == true && (attribute.newDataType == 'TEXT' || attribute.newDataType == 'INTEGER' || attribute.newDataType == 'LONGTEXT'
                           || attribute.newDataType == 'DOUBLE' || attribute.newDataType == 'TIME' || attribute.newDataType == 'DATE' || attribute.newDataType == 'CURRENCY')"></i>
                       </td>--%>
                    <td class="actions-column sticky-column sticky-actions-column">
                        <i ng-if="attribute.editMode == true"
                           ng-click="objectTypeAttributeVm.applyChanges(attribute)"
                           title="{{saveTitle}}" class="la la-check">
                        </i>
                        <i ng-if="attribute.editMode == true"
                           ng-click="objectTypeAttributeVm.cancelChanges(attribute)"
                           title="{{cancelChangesTitle}}" class="la la-times">
                        </i>
                        <i ng-show="attribute.inVisible == true"
                           ng-click="objectTypeAttributeVm.applyChanges(attribute)"
                           title="{{saveTitle}}" class="la la-check">
                        </i>
                        <i ng-if="attribute.inVisible == true"
                           ng-click="objectTypeAttributeVm.cancelChanges(attribute)"
                           title="{{cancelChangesTitle}}" class="la la-times">
                        </i>
                        <i title="{{editTitle}}" ng-hide="attribute.inVisible == true"
                           ng-if="attribute.showValues == true"
                           ng-click="objectTypeAttributeVm.editAttribute(attribute)"
                           class="la la-pencil">
                        </i>
                        <i title="{{deleteTitle}}" title="{{saveTitle}}" ng-hide="attribute.inVisible == true"
                           ng-if="attribute.showValues == true"
                           ng-click="objectTypeAttributeVm.deleteAttribute(attribute)"
                           class="la la-trash"></i>
                    </td>
                </tr>
                </tbody>
            </table>
        </div>

    </div>
</div>


