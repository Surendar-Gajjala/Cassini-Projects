<div class="classification-attributes-view">
    <style scoped>
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
    <div class="attributeExclusions modal fade" id="myModal1" data-backdrop="static"
         data-keyboard="true">

        <div class="compareContentAttribte">
            <div class="compareHeadre">
                <h4 translate style="text-align: center!important;" class="text-secondary">EXCLUSION_RULES</h4>

                <a href="" data-dismiss="modal" class="exclusion-close pull-right"
                   style="display: inline-block;"></a>
            </div>

            <div class="exclusionBody">
                <p style="text-align: center !important;color: #1CAF9A !important;"
                   ng-if="attecl == true">
                    {{exclSaveMessage}}</p>

                <div class="col-sm-12" style="display: inline !important;">
                    <div class="row">

                        <table class="attribute-table" style="margin: 0 auto !important;">
                            <thead>
                            <tr>
                                <th rowspan="2" colspan="2"></th>
                                <th class="att-name" ng-repeat="header in nameHeaders"
                                    style=""
                                    colspan="{{header.numValues}}">
                                    {{header.name}}
                                </th>
                            </tr>
                            <tr>
                                <th class="vertical"
                                    ng-repeat="value in finalValues track by $index">
                                    <div class="vertical" style="" title="{{value.value}}">
                                        <span>{{value.value}}</span>
                                    </div>
                                </th>
                            </tr>
                            </thead>
                            <tbody>
                            <tr ng-repeat="val in finalValues track by $index"
                                ng-init="pair = shouldAddRowHeader($index)">
                                <td class="att-name" rowspan="{{pair[1]}}" ng-if="pair[1] > 0"
                                    style="font-weight: bold !important;">{{pair[0]}}
                                </td>
                                <td class="att-value" style="font-weight: bold;"
                                    title="{{val.value}}">
                                    {{val.value}}

                                </td>
                                <td ng-repeat="val1 in finalValues track by $index"
                                    class="sameWidth"
                                    style="cursor: pointer;"
                                    ng-init="columnIndex = $index"
                                    ng-click="val.key == val1.key || createExclusionObj(val,$index,val1,columnIndex)"

                                    ng-disabled="val.key == val1.key"

                                    ng-class="[checkExclude(val,val1), {'myOwnBg': val.key == val1.key}]">
                                        <span ng-if="val.key != val1.key"
                                              title="{{val1.key}}({{val.value}}-{{val1.value}})"> {{val.value|
                                        limitTo:
                                        1}}{{val1.value| limitTo: 1}}</span>
                                </td>
                            </tr>
                            </tbody>
                        </table>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <%--<div id="action-buttons" style="padding: 5px;">
        <button class="btn btn-sm btn-default" ng-click="addAttribute()"
                ng-if="hasPermission"
                title="{{'NEW_ATTRIBUTE' | translate}}">
            <i class="la la-plus"></i>
        </button>
    </div>--%>

    <div id="attributes-table" class="responsive-table" style="padding: 0 10px;">
        <table class="table table-striped highlight-row">
            <thead>
            <tr>
                <%--<th>Seq</th>--%>
                <th>
                    <i class="la la-plus" ng-if="hasPermission" style="cursor: pointer;"
                       ng-click="addAttribute()"></i>
                </th>
                <th style="width: 200px;min-width: 150px">
                    <span translate>GROUP</span>
                </th>
                <th style="width: 200px;min-width: 150px" translate>NAME</th>
                <th translate>DESCRIPTION</th>
                <th style="width: auto ; min-width: 150px;" translate>DATA_TYPE</th>
                <th ng-if="classificationType.objectType == 'ITEMTYPE'" style="width: auto ; min-width: 150px;"
                    title="Quantity of Measurment" translate>QoM
                </th>
                <th style="width: auto" translate>DEFAULT_VALUE</th>
                <th style="width: auto" translate>Multi List</th>
                <th ng-if="classificationType.objectType == 'ITEMTYPE'" style="width: 100px; text-align: center"
                    translate>CONFIGURABLE
                </th>
                <th ng-if="classificationType.objectType == 'CUSTOMOBJECTTYPE'"
                    style="width: 100px; text-align: center"
                    translate>Show In Table
                </th>
                <th ng-if="classificationType.objectType == 'ITEMTYPE' || classificationType.objectType == 'CUSTOMOBJECTTYPE'"
                    style="width: 100px; text-align: center"
                    translate>REVISION_SPECIFIC
                </th>
                <th ng-if="classificationType.qualityType == 'PRODUCTINSPECTIONPLANTYPE' || classificationType.qualityType == 'MATERIALINSPECTIONPLANTYPE'"
                    style="width: 100px; text-align: center" translate>REVISION_SPECIFIC
                </th>
                <th ng-if="classificationType.objectType == 'ITEMTYPE'" style="width: 100px; text-align: center"
                    translate>CHANGE_CONTROLLED
                </th>
                <th ng-if="classificationType.objectType == 'ITEMTYPE'" style="width: 100px; text-align: center"
                    translate>ALLOW_EDIT_AFTER_RELEASE
                </th>
                <th style="width: 100px; text-align: center" translate>IS_REQUIRED</th>
                <th style="width: 100px; text-align: center" translate>VISIBLE</th>
                <th translate>VALIDATIONS</th>
                <th style="width: 100px; text-align: center"
                    class="actions-column sticky-column sticky-actions-column" translate>ACTIONS
                </th>
            </tr>
            </thead>

            <tbody>
            <tr ng-if="classificationType.attributes.length == 0">
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
            <tr id="{{$index}}" ng-repeat="attribute in classificationType.attributes"
                dragattribute attributes="classificationType.attributes" update-attribute="updateAttributeSeq">
                <td></td>
                <td>
                    <ui-select ng-model="attribute.attributeGroup" theme="bootstrap"
                               style="width:150px"
                               ng-if="(attribute.editMode || attribute.defaultValue) && attributeGroups.length > 0 && !attribute.newGroup"
                               on-select="selectAttributeGroup(attribute,$item)">
                        <ui-select-match placeholder="Select Group">{{$select.selected}}
                        </ui-select-match>
                        <ui-select-choices
                                repeat="attributeGroup in attributeGroups | filter: $select.search">
                            <div ng-bind="attributeGroup"></div>
                        </ui-select-choices>
                    </ui-select>
                    <input ng-if="(attribute.editMode || attribute.defaultValue) && attribute.newGroup"
                           type="text" class="form-control input-sm" placeholder="{{'ENTER_GROUP' | translate }}"
                           ng-model="attribute.attributeGroup" ng-enter="applyChanges(attribute)">
                    <i class="fa fa-times" ng-if="attribute.newGroup && attributeGroups.length > 0"
                       ng-click="attribute.newGroup = false;attribute.attributeGroup = null"
                       style="position: relative;float: right;margin-top: -22px;margin-right: 5px;"></i>
                    <span ng-if="!attribute.editMode && !attribute.defaultValue">{{attribute.attributeGroup}}</span>
                    <i class="fa fa-pencil"
                       ng-if="attributeGroups.length > 0 && !attribute.editMode && !attribute.defaultValue && attribute.attributeGroup != null && attributeGroup != ''"
                       ng-click="editAttributeGroup(attribute)"></i>
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
                    <a class="icon fa fa-info-circle"
                       ng-if="attribute.validations != null && attribute.editMode == false"
                       style="padding:0 10px;"
                       title="Attribute validations"
                       uib-popover-template="attributeValidationsPopover.templateUrl"
                       popover-append-to-body="true"
                       popover-popup-delay="50"
                       popover-placement="right"
                       popover-title="{{attribute.name}} validations"
                       popover-trigger="'outsideClick'">
                    </a>
                    </span>

                    <div style="display: flex;">
                        <select ng-if="attribute.editMode == true" class="form-control input-sm"
                                ng-model="attribute.newDataType"
                                title="{{attribute.newDataType}}"
                                ng-change="selectDataType(attribute)"
                                ng-options="dataType for dataType in dataTypes">
                            <option value="" selected disabled hidden>Select Data Type</option>
                        </select>
                    </div>
                    <select ng-if="attribute.editMode == true && attribute.newDataType == 'OBJECT'"
                            class="form-control input-sm"
                            ng-model="attribute.newRefType"
                            ng-change="onSelectObjectType(attribute)"
                            ng-options="refType for refType in refTypes">
                        <option value="" selected disabled hidden>Select Object</option>
                    </select>

                    <div class="form-group"
                         ng-if="attribute.newRefType == 'ITEM' && attribute.editMode == true && attribute.newDataType == 'OBJECT'">
                        <div class="input-group" style="margin-top: 2px">
                            <div class="input-group-btn" uib-dropdown>
                                <button uib-dropdown-toggle class="btn btn-default dropdown-toggle"
                                        type="button" style="padding: 3px 7px !important;">
                                    <span translate>SELECT</span> <span class="caret"></span>
                                </button>
                                <div class="dropdown-menu" role="menu">
                                    <div style="overflow: auto;padding: 5px; border: 1px solid #FFF;width: 300px;height: 200px;">
                                        <item-classification obj="attribute"
                                                             on-select-type="onSelectType"></item-classification>
                                    </div>
                                </div>
                            </div>
                            <input type="text" class="form-control" name="title"
                                   ng-model="attribute.subType.name" readonly>
                        </div>
                    </div>
                    <div class="form-group"
                         ng-if="attribute.newRefType == 'CHANGE' && attribute.editMode == true && attribute.newDataType == 'OBJECT'">
                        <div class="input-group" style="margin-top: 2px">
                            <div class="input-group-btn" uib-dropdown>
                                <button uib-dropdown-toggle class="btn btn-default dropdown-toggle"
                                        type="button" style="padding: 3px 7px !important;">
                                    <span translate>SELECT</span> <span class="caret"></span>
                                </button>
                                <div class="dropdown-menu" role="menu">
                                    <div style="overflow: auto;padding: 5px; border: 1px solid #FFF;width: 300px;height: 200px;">
                                        <workflow-change obj="attribute"
                                                         on-select-type="onSelectType"></workflow-change>
                                    </div>
                                </div>
                            </div>
                            <input type="text" class="form-control" name="title"
                                   ng-model="attribute.subType.name" readonly>


                        </div>
                    </div>
                    <div class="form-group"
                         ng-if="attribute.newRefType == 'WORKFLOW' && attribute.editMode == true && attribute.newDataType == 'OBJECT'">
                        <div class="input-group" style="margin-top: 2px">
                            <div class="input-group-btn" uib-dropdown>
                                <button uib-dropdown-toggle class="btn btn-default dropdown-toggle"
                                        type="button" style="padding: 3px 7px !important;">
                                    <span translate>SELECT</span> <span class="caret"></span>
                                </button>
                                <div class="dropdown-menu" role="menu">
                                    <div style="overflow: auto;padding: 5px; border: 1px solid #FFF;width: 300px;height: 200px;">
                                        <workflow-type-tree obj="attribute"
                                                            on-select-type="onSelectType"></workflow-type-tree>
                                    </div>
                                </div>
                            </div>
                            <input type="text" class="form-control" name="title"
                                   ng-model="attribute.subType.name" readonly>
                        </div>
                    </div>
                    <div class="form-group"
                         ng-if="attribute.newRefType == 'MANUFACTURER' && attribute.editMode == true && attribute.newDataType == 'OBJECT'">
                        <div class="input-group" style="margin-top: 2px">
                            <div class="input-group-btn" uib-dropdown>
                                <button uib-dropdown-toggle class="btn btn-default dropdown-toggle"
                                        type="button" style="padding: 3px 7px !important;">
                                    <span translate>SELECT</span> <span class="caret"></span>
                                </button>
                                <div class="dropdown-menu" role="menu">
                                    <div style="overflow: auto;padding: 5px; border: 1px solid #FFF;width: 300px;height: 200px;">
                                        <workflow-manufacturer obj="attribute"
                                                               on-select-type="onSelectType"></workflow-manufacturer>
                                    </div>
                                </div>
                            </div>
                            <input type="text" class="form-control" name="title"
                                   ng-model="attribute.subType.name" readonly>


                        </div>
                    </div>
                    <div class="form-group"
                         ng-if="attribute.newRefType == 'MANUFACTURERPART' && attribute.editMode == true && attribute.newDataType == 'OBJECT'">
                        <div class="input-group" style="margin-top: 2px">
                            <div class="input-group-btn" uib-dropdown>
                                <button uib-dropdown-toggle class="btn btn-default dropdown-toggle"
                                        type="button" style="padding: 3px 7px !important;">
                                    <span translate>SELECT</span> <span class="caret"></span>
                                </button>
                                <div class="dropdown-menu" role="menu">
                                    <div style="overflow: auto;padding: 5px; border: 1px solid #FFF;width: 300px;height: 200px;">
                                        <workflow-part obj="attribute"
                                                       on-select-type="onSelectType"></workflow-part>
                                    </div>
                                </div>
                            </div>
                            <input type="text" class="form-control" name="title"
                                   ng-model="attribute.subType.name" readonly>


                        </div>
                    </div>
                    <div class="form-group"
                         ng-if="attribute.newRefType == 'MESOBJECT' && attribute.editMode == true && attribute.newDataType == 'OBJECT'">
                        <div class="input-group" style="margin-top: 2px">
                            <div class="input-group-btn" uib-dropdown>
                                <button uib-dropdown-toggle class="btn btn-default dropdown-toggle"
                                        type="button" style="padding: 3px 7px !important;">
                                    <span translate>SELECT</span> <span class="caret"></span>
                                </button>
                                <div class="dropdown-menu" role="menu">
                                    <div style="overflow: auto;padding: 5px; border: 1px solid #FFF;width: 300px;height: 200px;">
                                        <manufacturing-resource-type-tree resource-type="false"
                                                                          obj="attribute"
                                                                          on-select-type="onSelectType"></manufacturing-resource-type-tree>
                                    </div>
                                </div>
                            </div>
                            <input type="text" class="form-control" name="title"
                                   ng-model="attribute.subType.name" readonly>
                        </div>
                    </div>
                    <div class="form-group"
                         ng-if="attribute.newRefType == 'MROOBJECT' && attribute.editMode == true && attribute.newDataType == 'OBJECT'">
                        <div class="input-group" style="margin-top: 2px">
                            <div class="input-group-btn" uib-dropdown>
                                <button uib-dropdown-toggle class="btn btn-default dropdown-toggle"
                                        type="button" style="padding: 3px 7px !important;">
                                    <span translate>SELECT</span> <span class="caret"></span>
                                </button>
                                <div class="dropdown-menu" role="menu">
                                    <div style="overflow: auto;padding: 5px; border: 1px solid #FFF;width: 300px;height: 200px;">
                                        <mro-resource-type-tree obj="attribute"
                                                                on-select-type="onSelectType"></mro-resource-type-tree>
                                    </div>
                                </div>
                            </div>
                            <input type="text" class="form-control" name="title"
                                   ng-model="attribute.subType.name" readonly>


                        </div>
                    </div>
                    <div class="form-group"
                         ng-if="attribute.newRefType == 'CUSTOMOBJECT' && attribute.editMode == true && attribute.newDataType == 'OBJECT'">
                        <div class="input-group" style="margin-top: 2px">
                            <div class="input-group-btn" uib-dropdown>
                                <button uib-dropdown-toggle class="btn btn-default dropdown-toggle"
                                        type="button" style="padding: 3px 7px !important;">
                                    <span translate>SELECT</span> <span class="caret"></span>
                                </button>
                                <div class="dropdown-menu" role="menu">
                                    <div style="overflow: auto;padding: 5px; border: 1px solid #FFF;width: 300px;height: 200px;">
                                        <custom-type-tree obj="attribute"
                                                          on-select-type="onSelectType"></custom-type-tree>
                                    </div>
                                </div>
                            </div>
                            <input type="text" class="form-control" name="title"
                                   ng-model="attribute.subType.name" readonly>
                        </div>
                    </div>
                    <div class="form-group"
                         ng-if="attribute.newRefType == 'REQUIREMENTDOCUMENT' && attribute.editMode == true && attribute.newDataType == 'OBJECT'">
                        <div class="input-group" style="margin-top: 2px">
                            <div class="input-group-btn" uib-dropdown>
                                <button uib-dropdown-toggle class="btn btn-default dropdown-toggle"
                                        type="button" style="padding: 3px 7px !important;">
                                    <span translate>SELECT</span> <span class="caret"></span>
                                </button>
                                <div class="dropdown-menu" role="menu">
                                    <div style="overflow: auto;padding: 5px; border: 1px solid #FFF;width: 300px;height: 200px;">
                                        <project-management-type-tree obj="attribute"
                                                                      on-select-type="onSelectType"
                                                                      object-type="REQUIREMENTDOCUMENTTYPE"></project-management-type-tree>
                                    </div>
                                </div>
                            </div>
                            <input type="text" class="form-control" name="title"
                                   ng-model="attribute.subType.name" readonly>


                        </div>
                    </div>
                    <div class="form-group"
                         ng-if="attribute.newRefType == 'REQUIREMENT' && attribute.editMode == true && attribute.newDataType == 'OBJECT'">
                        <div class="input-group" style="margin-top: 2px">
                            <div class="input-group-btn" uib-dropdown>
                                <button uib-dropdown-toggle class="btn btn-default dropdown-toggle"
                                        type="button" style="padding: 3px 7px !important;">
                                    <span translate>SELECT</span> <span class="caret"></span>
                                </button>
                                <div class="dropdown-menu" role="menu">
                                    <div style="overflow: auto;padding: 5px; border: 1px solid #FFF;width: 300px;height: 200px;">
                                        <requirement-wf-tree obj="attribute"
                                                             on-select-type="onSelectType"></requirement-wf-tree>
                                    </div>
                                </div>
                            </div>
                            <input type="text" class="form-control" name="title"
                                   ng-model="attribute.subType.name" readonly>


                        </div>
                    </div>

                    <div class="form-group"
                         ng-if="attribute.newRefType == 'QUALITY' && attribute.editMode == true && attribute.newDataType == 'OBJECT'">
                        <div class="input-group" style="margin-top: 2px">
                            <div class="input-group-btn" uib-dropdown>
                                <button uib-dropdown-toggle class="btn btn-default dropdown-toggle"
                                        type="button" style="padding: 3px 7px !important;">
                                    <span translate>SELECT</span> <span class="caret"></span>
                                </button>
                                <div class="dropdown-menu" role="menu">
                                    <div style="overflow: auto;padding: 5px; border: 1px solid #FFF;width: 300px;height: 200px;">
                                        <workflow-quality obj="attribute"
                                                          on-select-type="onSelectType"></workflow-quality>
                                    </div>
                                </div>
                            </div>
                            <input type="text" class="form-control" name="title"
                                   ng-model="attribute.subType.name" readonly>
                        </div>
                    </div>

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
                <td style="width: auto" ng-if="classificationType.objectType == 'ITEMTYPE'">
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
                <td style="width: auto; text-align: center" ng-if="classificationType.objectType == 'CUSTOMOBJECTTYPE'">
                    <span>
                        <input type="checkbox" class="form-control input-sm"
                               ng-model="attribute.showInTable" ng-hide="attribute.inVisible == true"
                               ng-disabled="attribute.editMode == false">
                        <input type="checkbox" class="form-control input-sm" ng-model="attribute.showInTable"
                               ng-if="attribute.inVisible == true">

                    </span>

                </td>
                <td style="width: auto; text-align: center" ng-if="classificationType.objectType == 'ITEMTYPE'">
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

                <td style="width: auto; text-align: center"
                    ng-if="classificationType.objectType == 'ITEMTYPE' || classificationType.objectType == 'CUSTOMOBJECTTYPE'">
                    <input type="checkbox" class="form-control input-sm"
                           ng-model="attribute.revisionSpecific"
                           ng-disabled="attribute.editMode == false || attribute.changeControlled == true">
                </td>
                <td style="width: auto; text-align: center"
                    ng-if="classificationType.qualityType == 'PRODUCTINSPECTIONPLANTYPE' || classificationType.qualityType == 'MATERIALINSPECTIONPLANTYPE'">
                    <input type="checkbox" class="form-control input-sm"
                           ng-model="attribute.revisionSpecific"
                           ng-disabled="attribute.editMode == false || attribute.changeControlled == true">
                </td>
                <td style="width: 100px; text-align: center" ng-if="classificationType.objectType == 'ITEMTYPE'">
                    <input type="checkbox" class="form-control input-sm"
                           ng-model="attribute.changeControlled"
                           ng-click="selectController(attribute)"
                           ng-disabled="attribute.editMode == false">
                </td>
                <td style="width: 100px; text-align: center" ng-if="classificationType.objectType == 'ITEMTYPE'">
                    <input type="checkbox" class="form-control input-sm"
                           ng-model="attribute.allowEditAfterRelease"
                           ng-disabled="attribute.editMode == false">
                </td>
                <td style="width: 100px; text-align: center">
                    <input type="checkbox" class="form-control input-sm" ng-model="attribute.required"
                           ng-disabled="attribute.editMode == false || attribute.configurable"
                           ng-hide="attribute.inVisible == true" ng-click="checkRequiredAttributes(attribute)">
                    <input type="checkbox" class="form-control input-sm" ng-model="attribute.required"
                           ng-click="checkRequiredAttributes(attribute)"
                           ng-if="attribute.inVisible == true && !attribute.configurable">
                </td>
                <td style="width: 100px; text-align: center">
                    <input type="checkbox" class="form-control input-sm" ng-model="attribute.visible"
                           ng-disabled="attribute.editMode == false"
                           ng-hide="attribute.inVisible == true">
                    <input type="checkbox" class="form-control input-sm" ng-model="attribute.visible"
                           ng-if="attribute.inVisible == true">
                </td>
                <td>
                    <i class="fa fa-pencil" title="Attribute validations" ng-click="showValidations(attribute)"
                       style="padding: 10px;"
                       ng-if="attribute.editMode == true && (attribute.newDataType == 'TEXT' || attribute.newDataType == 'INTEGER' || attribute.newDataType == 'LONGTEXT'
                        || attribute.newDataType == 'DOUBLE' || attribute.newDataType == 'TIME' || attribute.newDataType == 'DATE' || attribute.newDataType == 'CURRENCY')"></i>
                </td>
                <td class="actions-column sticky-column sticky-actions-column">
                    <i ng-if="attribute.editMode == true"
                       ng-hide="attribute.itemMode == true"
                       title="{{'SAVE' | translate}}"
                       ng-click="applyChanges(attribute)"
                       class="la la-check">
                    </i>
                    <i ng-show="attribute.itemMode == true && attribute.inVisible == false"
                       title="{{'SAVE' | translate}}"
                       ng-click="applyChange(attribute)"
                       class="la la-check">
                    </i>
                    <i ng-if="attribute.editMode == true"
                       title="{{'CANCEL' | translate}}"
                       ng-click="cancelChanges(attribute)"
                       class="la la-times">
                    </i>

                    <i ng-show="attribute.inVisible == true"
                       title="{{'SAVE' | translate}}"
                       ng-click="applyChanges(attribute)"
                       class="la la-check">
                    </i>
                    <i ng-if="attribute.inVisible == true"
                       title="{{'CANCEL' | translate}}"
                       ng-click="cancelChanges(attribute)"
                       class="la la-times">
                    </i>
                    <i title="{{'EDIT' | translate}}" ng-hide="attribute.inVisible == true"
                       ng-if="attribute.showValues == true && hasPermission"

                       ng-click="editAttribute(attribute)"
                       class="la la-pencil">
                    </i>
                    <i title="{{'DELETE' | translate}}" ng-hide="attribute.inVisible == true"
                       ng-if="attribute.showValues == true && hasPermission"

                       ng-click="deleteAttribute(attribute)"
                       class="la la-trash"></i>
                </td>
            </tr>
            </tbody>
        </table>
    </div>

    <div id="lov-view" class="new-lov modal">
        <div class="lov-content">
            <div id="lov-header">
                <span class="configuration-header">New Value List</span>
            </div>
            <div id="lovView-content">
                <div class="lov-header">
                <span ng-if="!newLov.editTitle" ng-dblclick="newLov.editTitle = true;
                $('#titleInput{{$index}}').setSelectionRange(0, this.value.length)"
                      title="{{newLov.newName}}">
                {{newLov.newName}}</span>

                    <div ng-if="newLov.editTitle" style="display: flex">
                        <input type="text" style="width: 230px"
                               id="titleInput{{$index}}"
                               ng-model="newLov.newName"
                               ng-enter="applyLovChanges()"
                               onfocus="this.setSelectionRange(0, this.value.length)">

                        <div class="buttons" style="display: inline-block; margin-left: 3px;">
                            <i title="{{saveChangesTitle}}" class="la la-check"
                               ng-click="newLov.editTitle = false;applyLovChanges(lov)"></i>

                        </div>
                    </div>
                    <div class="buttons" ng-if="!newLov.editTitle">
                        <i ng-if="newLov.id != null && hasPermission"
                           title="{{addNewValueTitle}}"
                           class="la la-plus mr10"
                           ng-click="addLovValue()"></i>
                        <i title="{{deleteLovTitle}}" class="la la-times"
                           ng-if="hasPermission"
                           ng-click="deleteLov()"></i>
                    </div>
                </div>
                <div class="lov-body">
                    <div class="lov-value" ng-repeat="value in newLov.valueObjects"
                         ng-class="{'edit-mode': value.editMode}">
                        <div ng-if="value.editMode" style="display: flex">
                        <span class="name" style="flex-grow: 1">
                            <input type="text"
                                   ng-model="value.newString"
                                   ng-enter="value.editMode = false;value.string = value.newString;applyChangesList(value)"
                                   onfocus="this.setSelectionRange(0, this.value.length)">
                        </span>
                        <span class="buttons">
                            <i title="{{saveChangesTitle}}" class="la la-check"
                               ng-click="value.editMode = false;value.newMode = false;value.string = value.newString;applyChangesList(value)">
                            </i>
                            <i title="{{lovCancel}}"
                               ng-click="value.editMode = false;value.newString = value.string; cancelChangesList(value)"
                               class="la la-times"></i>
                        </span>
                        </div>

                        <div ng-if="!value.editMode">
                            <span class="name" ng-dblclick="value.editMode = true">{{value.string}}</span>
                        <span class="buttons">
                            <i title="{{'EDIT_VALUE' | translate}}" class="la la-edit"
                               ng-if="hasPermission"
                               ng-click="toDeleteValue = null;value.editMode = true"></i>
                            <i title="{{deleteValueTitle}}" ng-if="hasPermission"
                               ng-click="toDeleteValue = value;deleteLovValue()"
                               class="la la-times"></i>
                        </span>
                        </div>
                    </div>
                </div>
            </div>
            <div id="lov-footer" class="lov-footer">
                <div class="form-group">
                    <button class="btn btn-sm btn-success" ng-click="addLovToAttribute()">Add
                    </button>
                    <button class="btn btn-sm btn-default" ng-click="hideLov()">Cancel</button>
                </div>
            </div>
        </div>
    </div>
</div>

