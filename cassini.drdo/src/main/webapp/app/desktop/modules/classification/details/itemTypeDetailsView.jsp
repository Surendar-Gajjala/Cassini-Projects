<style scoped>
    select {
        display: inline-block;
        width: auto;
    }

    .itemTypeInfo {
        overflow-y: auto;
        position: absolute;
        bottom: 0px;
        top: 42px;
        right: 0px;
        left: 10px;
    }

    .btn-group > .btn:first-child {
        margin-left: 20px;
    }
</style>
<div ng-if="itemTypeVm.itemType == null || itemTypeVm.itemType == undefined" style="padding: 10px">
    <span>Select an Item Type to see/edit details</span>
</div>
<div ng-if="itemTypeVm.itemType != null && !newItemTypeCreating" style="overflow-y: hidden;">
    <div style="width: 100px">
        <button class="btn btn-xs btn-success min-width" ng-click="itemTypeVm.onSave()"
                ng-if="hasPermission('permission.classification.edit')">Save
        </button>
    </div>
    <hr style="margin: 5px;">
    <div class="itemTypeInfo">
        <h4 class="section-title">Basic Info</h4>

        <div class="row">
            <div class="col-xs-12 col-sm-8 col-sm-offset-1 col-md-6 col-md-offset-1">

                <form class="form-horizontal">
                    <div class="form-group">
                        <label class="col-sm-4 control-label">
                            <span>Name</span>: </label>

                        <div class="col-sm-8">
                            <input type="text" class="form-control" name="title" ng-model="itemTypeVm.itemType.name"
                                   ng-disabled="itemTypeVm.itemType.parentNode || itemTypeVm.itemTypeItemExist == true || !hasPermission('permission.classification.edit')">
                        </div>
                    </div>

                    <div class="form-group">
                        <label class="col-sm-4 control-label">
                            <span>Description</span> : </label>

                        <div class="col-sm-8">
                            <textarea name="description" rows="5" class="form-control" style="resize: none"
                                      ng-model="itemTypeVm.itemType.description"
                                      ng-disabled="!hasPermission('permission.classification.edit')"></textarea>
                        </div>
                    </div>

                    <div class="form-group">
                        <label class="col-sm-4 control-label">
                            <span>Code</span>: </label>

                        <div class="col-sm-8">
                            <input type="text" class="form-control" ng-model="itemTypeVm.itemType.typeCode"
                                   placeholder="Enter 2 digit Code" maxlength="2"
                                   ng-disabled="itemTypeVm.itemType.parentNode || itemTypeVm.itemTypeItemExist == true || !hasPermission('permission.classification.edit')">
                        </div>
                    </div>

                    <div class="form-group">
                        <label class="col-sm-4 control-label">
                            <span>Auto Number Source</span> : </label>

                        <div class="col-sm-8">
                            <ui-select ng-model="itemTypeVm.itemType.itemNumberSource" theme="bootstrap"
                                       style="width:100%"
                                       ng-disabled="itemTypeVm.itemType.parentNode || itemTypeVm.itemTypeItemExist == true || !hasPermission('permission.classification.edit')">
                                <ui-select-match placeholder="Select">{{$select.selected.name}}</ui-select-match>
                                <ui-select-choices repeat="source in itemTypeVm.autoNumbers | filter: $select.search">
                                    <div ng-bind="source.name"></div>
                                </ui-select-choices>
                            </ui-select>
                        </div>
                    </div>

                    <div class="form-group">
                        <label class="col-sm-4 control-label">
                            <span>Revision Sequence</span> : </label>

                        <div class="col-sm-8">
                            <ui-select ng-model="itemTypeVm.itemType.revisionSequence" theme="bootstrap"
                                       style="width:100%"
                                       ng-disabled="itemTypeVm.itemType.parentNode || itemTypeVm.itemTypeItemExist == true">
                                <ui-select-match placeholder="Select">{{$select.selected.name}}</ui-select-match>
                                <ui-select-choices repeat="lov in itemTypeVm.revSequences | filter: $select.search">
                                    <div ng-bind="lov.name"></div>
                                </ui-select-choices>
                            </ui-select>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-4 control-label">
                            <span>Units</span> : </label>

                        <div class="col-sm-8">
                            <ui-select ng-model="itemTypeVm.itemType.units" theme="bootstrap"
                                       style="width:100%">
                                <ui-select-match placeholder="Select">{{$select.selected}}</ui-select-match>
                                <ui-select-choices repeat="value in itemTypeVm.unitsOfMeasures.values track by value">
                                    <div ng-bind="value"></div>
                                </ui-select-choices>
                            </ui-select>
                        </div>
                    </div>

                    <div class="form-group" ng-if="itemTypeVm.itemType.parentNodeItemType != 'Part'">
                        <label class="col-sm-4 control-label">Has BOM :</label>

                        <div class="col-sm-7" style="padding-top: 8px;text-align: left">
                            <input type="checkbox" class="form-control input-sm"
                                   style="width: 24px;" ng-model="itemTypeVm.itemType.hasBom"
                                   ng-disabled="itemTypeVm.itemType.parentNode || itemTypeVm.itemTypeItemExist == true">
                        </div>
                    </div>

                    <div class="form-group"
                         ng-if="itemTypeVm.itemType.parentNodeItemType == 'Part'">
                        <label class="col-sm-4 control-label">Has Lots :</label>

                        <div class="col-sm-7" style="padding-top: 8px;text-align: left">
                            <input type="checkbox" class="form-control input-sm"
                                   style="width: 24px;" ng-model="itemTypeVm.itemType.hasLots"
                                   ng-disabled="itemTypeVm.itemType.parentNode || itemTypeVm.itemTypeItemExist == true">
                        </div>
                    </div>

                    <div class="form-group"
                         ng-if="itemTypeVm.itemType.parentNodeItemType == 'Part'">
                        <label class="col-sm-4 control-label">Has Specs :</label>

                        <div class="col-sm-7" style="padding-top: 8px;text-align: left">
                            <input type="checkbox" class="form-control input-sm"
                                   style="width: 24px;" ng-model="itemTypeVm.itemType.hasSpec"
                                   ng-disabled="itemTypeVm.itemType.parentNode || itemTypeVm.itemTypeItemExist == true">
                        </div>
                    </div>
                </form>

            </div>

            <div class="col-xs-12 col-sm-8 col-md-4">
                <style scoped>
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
                        margin-left: 60px;
                    }

                    .lov-container {
                        width: 300px;
                        min-width: 300px;
                        height: 400px;
                        border: 1px solid #ddd;
                        border-radius: 5px;
                    }

                    .lov-add-container {
                        width: 50px;
                        min-width: 50px;
                        border: 4px solid #ddd;
                        border-radius: 5px;
                        margin-bottom: 10px;
                    }

                    .lov-container > .lov-header {
                        padding: 10px;
                        border-bottom: 1px solid #ddd;
                        display: flex;
                    }

                    .lov-container > .lov-header > h5 {
                        margin: 0 !important;
                        display: inline-block;
                    }

                    .lov-container > .lov-header input {
                    }

                    .lov-container > .lov-header > input {
                        flex-grow: 1;
                    }

                    .lov-container > .lov-header > .buttons {
                        display: inline-block;
                        flex-grow: 1;
                        text-align: right;
                    }

                    .lov-container .buttons i:hover {
                        color: #0390fd;
                        cursor: pointer;
                    }

                    .lov-container > .lov-body {
                        padding: 10px;
                        height: 350px;
                        max-height: 350px;
                        overflow-y: auto;
                    }

                    .lov-container .lov-body .lov-value {
                        cursor: pointer;
                    }

                    .lov-container > .lov-body > .lov-value > div {
                        border-bottom: 1px dotted #e3e3e3;
                        padding: 5px;
                        display: flex;
                        justify-content: left;
                    }

                    .lov-container .lov-body .lov-value .name,
                    .lov-container .lov-body .lov-value .type {
                        flex-grow: 1;
                        justify-content: left;
                        text-align: left;
                    }

                    .lov-container .lov-body .lov-value .buttons {
                        visibility: hidden;
                        text-align: right;
                    }

                    .lov-container .lov-body .lov-value:hover .buttons {
                        visibility: visible;
                    }

                    .lov-container .lov-body .lov-value.edit-mode .buttons {
                        visibility: visible;
                    }

                    .lov-container input {
                        border: 0;
                        outline: 0;
                        background: transparent;
                        border-bottom: 1px dashed lightgrey;
                    }

                    .lov-container .lov-body .lov-value.edit-mode .type {
                        border-bottom: 1px dashed lightgrey;
                        margin-right: 10px;
                        text-align: left;
                    }

                    .lov-container .lov-body .lov-value .type a {
                        text-decoration: none;
                        color: inherit;
                    }

                    .lov-container .lov-body .lov-value .type .caret {
                        border-top: 5px dashed;
                        border-right: 5px solid transparent;
                        border-left: 5px solid transparent;
                    }

                    .lov-container .lov-body .lov-value:hover {
                        background-color: #0081c2;
                        color: #fff;
                    }

                    .lov-container .lov-body .lov-value.edit-mode {
                        background-color: transparent;
                        color: inherit;
                    }

                    .lov-container .lov-body .lov-value:hover .buttons i {
                        cursor: pointer;
                    }

                    .lov-container .lov-body .lov-value:hover .buttons i {
                        color: #fff;
                    }

                    .lov-container .lov-body .lov-value.edit-mode .buttons i:hover {
                        color: #0390fd;
                    }

                    .lov-container .lov-body .lov-value.edit-mode .buttons i {
                        color: inherit;
                    }

                    .lov-container .lov-body .lov-value .name input {
                        width: 98%;
                    }

                    .lov-container .lov-container-mask {
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

                    .lovp-dropdown {
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

                    #lovAddButton:hover {
                        background-color: #0081c2;
                        cursor: pointer;
                    }

                    #lovAddButton:hover i {
                        color: #fff !important;
                    }

                    h3 {
                        color: black;
                        font-weight: bolder;
                    }

                    .type-selector {
                        position: absolute;
                        right: 20px;
                        top: 15px;
                        cursor: pointer;
                        font-size: 18px;
                        color: #86a1b7;
                    }
                </style>

                <div class="flex-row flex-row-center"
                     ng-if='itemTypeVm.itemType.id != null && itemTypeVm.itemType.id != undefined && itemTypeVm.itemType.hasSpec'>
                    <div id="lov" class="flex-col lov-container">
                        <div id="lovValueMask" class="lov-container-mask centered-container">
                            <div class="centered-child">
                                <h5>Are you sure you want to delete this Spec?</h5>

                                <div>
                                    <button class='btn btn-xs btn-default' ng-click='itemTypeVm.hideMask()'>No
                                    </button>
                                    <button class='btn btn-xs btn-danger' ng-click='itemTypeVm.deleteLovValue()'>Yes
                                    </button>
                                </div>
                            </div>
                        </div>
                        <div class="lov-header">
                            <span>{{itemTypeVm.itemType.name}} Specifications</span>

                            <div class="buttons">
                                <i title="Add Value" class="fa fa-plus mr10"
                                   ng-click="itemTypeVm.addLovValue($index)"></i>
                            </div>
                        </div>

                        <div class="progress progress-striped active"
                             style="border-radius: 0;height: 5px;"
                             ng-if="lov.showBusy">
                            <div class="progress-bar"
                                 role="progressbar" aria-valuenow="100" aria-valuemin="0"
                                 aria-valuemax="100" style="width: 100%">
                            </div>
                        </div>


                        <div class="lov-body" id="lov-body">
                            <div class="lov-value" ng-repeat="value in itemTypeVm.itemTypeSpecs"
                                 ng-class="{'edit-mode': value.editMode}">
                                <div ng-if="value.editMode" style="display: flex">
                                    <span class="name" style="flex-grow: 1">
                                        <input type="text"
                                               ng-model="value.newSpecName"
                                               ng-enter="value.editMode = false;itemTypeVm.applyLovChanges(value)"
                                               focus="someFocusVariable">
                                    </span>
                                    <span class="buttons">
                                        <i title="Save" class="fa fa-check mr5"
                                           ng-click="itemTypeVm.applyLovChanges(value)">
                                        </i>
                                        <i title="Cancel"
                                           ng-click="value.editMode = false;value.newSpecName = value.specName; itemTypeVm.cancelLovChanges(value)"
                                           class="fa fa-times"></i>
                                     </span>
                                </div>

                                <div ng-if="!value.editMode">
                                    <span class="name" ng-dblclick="value.editMode = true">{{value.specName}}</span>
                                        <span class="buttons">
                                            <i title="Edit" class="fa fa-edit mr5"
                                               ng-click="itemTypeVm.toDeleteValue = null;value.newSpecName = value.specName;value.editMode = true">
                                            </i>
                                             <i title="Delete Lov"
                                                ng-click="itemTypeVm.toDeleteValue = value;itemTypeVm.promptDeleteLovValue(value)"
                                                class="fa fa-times">
                                             </i>
                                     </span>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>

        <br>
        <h4 class="section-title">Attributes</h4>
        <br>

        <div class="row">
            <div style="margin-left: 10px;">
                <div>
                    <button class="btn btn-xs btn-primary" ng-click="itemTypeVm.addAttribute()"
                            ng-if="hasPermission('permission.classification.new')">
                        New Attribute
                    </button>
                    <span class="btn btn-sm btn-default" ng-hide="itemTypeVm.hide == false"
                          style="margin-left: 249px; color: red;width:auto;font-size: 14px;">{{itemTypeVm.error}}
                    </span>
                </div>
                <br>

                <div class="responsive-table">
                    <table class="table table-striped highlight-row">
                        <thead>
                        <tr>
                            <th style="width: 200px">Name</th>
                            <th>Description</th>
                            <th style="width: auto">Data Type</th>
                            <th style="width: 100px; text-align: center">Revision Specific</th>
                            <th style="width: 100px; text-align: center">Change Controlled</th>
                            <th style="width: 100px; text-align: center">Required</th>
                            <th style="width: 100px; text-align: center">Actions</th>
                        </tr>
                        </thead>

                        <tbody>
                        <tr ng-if="itemTypeVm.itemType.attributes.length == 0">
                            <td colspan="7"><span>No Attributes</span></td>
                        </tr>
                        <tr ng-repeat="attribute in itemTypeVm.itemType.attributes">
                            <td style="width: 200px">
                                <span ng-if="attribute.showValues == true">{{attribute.name}}</span>
                                <input ng-if="attribute.editMode == true" type="text" class="form-control input-sm"
                                       ng-model="attribute.newName">
                            </td>
                            <td>
                                <span ng-if="attribute.showValues == true">{{attribute.description}}</span>
                                <input ng-if="attribute.editMode == true" type="text" class="form-control input-sm"
                                       ng-model="attribute.newDescription">
                            </td>
                            <td style="width: 120px">
                                <span ng-if="attribute.showValues == true">
                                    <span>{{attribute.dataType}}</span>
                                    <span ng-if="attribute.dataType == 'OBJECT'" style="margin-left: 5px;">( {{attribute.refType}} )</span>
                                    <span ng-if="attribute.dataType == 'LIST'" style="margin-left: 5px;">( {{attribute.lov.name}} )</span>
                                </span>
                                <select ng-if="attribute.editMode == true" class="form-control input-sm"
                                        ng-model="attribute.newDataType"
                                        ng-options="dataType for dataType in itemTypeVm.dataTypes">
                                </select>
                                <select ng-if="attribute.editMode == true && attribute.newDataType == 'OBJECT'"
                                        class="form-control input-sm"
                                        ng-model="attribute.newRefType"
                                        ng-options="refType for refType in itemTypeVm.refTypes">
                                </select>
                                <select ng-if="attribute.editMode == true && attribute.newDataType == 'LIST'"
                                        class="form-control input-sm"
                                        ng-model="attribute.newLov"
                                        ng-options="lov.name for lov in itemTypeVm.lovs">
                                </select>
                            </td>
                            <td style="width: auto; text-align: center">
                                <input type="checkbox" class="form-control input-sm"
                                       ng-model="attribute.revisionSpecific" ng-disabled="attribute.editMode == false">
                            </td>
                            <td style="width: 100px; text-align: center">
                                <input type="checkbox" class="form-control input-sm"
                                       ng-model="attribute.changeControlled" ng-disabled="attribute.editMode == false">
                            </td>
                            <td style="width: 100px; text-align: center">
                                <input type="checkbox" class="form-control input-sm" ng-model="attribute.required"
                                       ng-disabled="attribute.editMode == false">
                            </td>
                            <td class="btn-group" style="width: 100px; text-align: center">
                                <button ng-if="attribute.editMode == true" class="btn btn-xs btn-success"
                                        ng-click="itemTypeVm.applyChanges(attribute)"><i class="fa fa-check"></i>
                                </button>
                                <button ng-if="attribute.editMode == true" class="btn btn-xs btn-default"
                                        ng-click="itemTypeVm.cancelChanges(attribute)"><i class="fa fa-times"></i>
                                </button>
                                <button title="Edit"
                                        ng-if="attribute.showValues == true && hasPermission('permission.classification.edit')"
                                        class="btn btn-xs btn-warning"
                                        ng-click="itemTypeVm.editAttribute(attribute)"><i class="fa fa-edit"></i>
                                </button>
                                <button title="Delete"
                                        ng-if="attribute.showValues == true && hasPermission('permission.classification.delete')"
                                        class="btn btn-xs btn-danger" ng-click="itemTypeVm.deleteAttribute(attribute)">
                                    <i class="fa fa-trash"></i></button>
                            </td>
                        </tr>
                        </tbody>
                    </table>
                </div>
            </div>
        </div>
    </div>
</div>