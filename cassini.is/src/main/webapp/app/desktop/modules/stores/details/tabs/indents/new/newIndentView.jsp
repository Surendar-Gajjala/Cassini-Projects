<div class="view-container" fitcontent>
    <style>
        .split-left-pane {
            width: 400px;
            padding: 10px;
        }

        .split-pane-divider {
            background: #EEE;
            left: 400px;
            width: 5px;
        }
    </style>
    <div class="view-toolbar">
        <div class="btn-group">
            <button class="min-width btn btn-sm btn-default" ng-click="newIndentVm.back()">Back</button>
            <button ng-if="newIndentVm.newIndent.projectObject != null && newIndentVm.newIndent.status != 'APPROVED' && (newIndentVm.showAddRequestsButton || newIndentVm.editMode)"
                    class="min-width btn btn-sm btn-default" ng-click="newIndentVm.openRequestsDialogue()">Add Items
            </button>
            <button ng-if="newIndentVm.groupedItems.length > 0"
                    class="min-width btn btn-sm btn-default"
                    ng-click="newIndentVm.createIndent()">Create
            </button>
        </div>
    </div>

    <div class="view-content no-padding">

        <div class="split-pane fixed-left">

            <div class="split-pane-component split-left-pane" style="overflow-x: auto">

                <form class="form-horizontal"
                      style="width:400px;padding-right: 49px;padding-top: 9px; margin-left: -22px;">

                    <div class="form-group">
                        <label class="col-sm-5 control-label">Autonumber :</label>

                        <div class="col-sm-7">

                            <div class="input-group mb15">
                                <div class="input-group-btn">
                                    <button class="btn btn-default" type="button" style="width: 85px"
                                            ng-click="newIndentVm.generateAutoNumber()">Auto
                                    </button>
                                </div>
                                <input type="text" class="form-control" name="title"
                                       ng-model="newIndentVm.newIndent.indentNumber">
                            </div>

                        </div>
                    </div>

                    <div class="form-group">
                        <label class="col-sm-5 control-label">Project :<span class="asterisk">*</span></label>

                        <div class="col-sm-7">
                            <ui-select class="required-field"
                                       ng-disabled="newIndentVm.groupedItems.length > 0"
                                       ng-change="newIndentVm.projectChanged()"
                                       ng-model="newIndentVm.newIndent.projectObject"
                                       theme="bootstrap">
                                <ui-select-match placeholder="Select Project">{{$select.selected.name}}
                                </ui-select-match>
                                <ui-select-choices
                                        repeat="project in newIndentVm.projects | filter: $select.search |orderBy: 'name'"
                                        style="max-height: 120px;">
                                    <div ng-bind="project.name | highlight: $select.name.search"></div>
                                </ui-select-choices>
                            </ui-select>
                        </div>
                    </div>

                    <div class="form-group">
                        <label class="col-sm-5 control-label">Raised Date :<span class="asterisk">*</span></label>

                        <div class="col-sm-7">
                            <input class="form-control"
                                   ng-model="newIndentVm.newIndent.raisedDateObject"
                                   placeholder="dd-mm-yyyy" type="text" inward-date-picker/>
                        </div>
                    </div>

                    <div class="form-group">
                        <label class="col-sm-5 control-label">Raised By :<span class="asterisk">*</span></label>

                        <div class="col-sm-7">
                            <input type="text" class="form-control" ng-model="newIndentVm.newIndent.raisedByObject">
                        </div>
                    </div>

                    <div class="form-group">
                        <label class="col-sm-5 control-label">Notes : </label>

                        <div class="col-sm-7">
                            <textarea class="form-control ng-pristine ng-valid ng-touched"
                                      ng-model="newIndentVm.newIndent.notesObject"
                                      style="width: 202px; height: 39px;margin-top: -2px;">
                            </textarea>
                        </div>
                    </div>

                    <br>
                    <h4 ng-if="newIndentVm.indentAttributes.length  > 0 || newIndentVm.attributes.length > 0"
                        class="section-title" style="color: black; margin-left: 42px;">Attributes
                    </h4>
                    <br>

                    <div>
                        <form class="form-horizontal">
                            <attributes-view show-objects="selectObjectValues"
                                             attributes="newIndentVm.requiredAttributes"></attributes-view>
                            <br>
                            <attributes-view show-objects="selectObjectValues"
                                             attributes="newIndentVm.attributes"></attributes-view>
                            <br>
                            <br>
                        </form>
                    </div>

                </form>

            </div>

            <div class="split-pane-divider"></div>

            <div class="split-pane-component split-right-pane" style="left: 400px; cursor: pointer">
                <table class='table table-striped highlight-row'>
                    <thead>
                    <tr>
                        <th>Item Number</th>
                        <th>Item Name</th>
                        <th>Requested Qty</th>
                        <th>Notes</th>
                        <th>Indent Qty</th>
                        <th style="text-align: center;">Actions</th>
                    </tr>
                    </thead>
                    <tbody>
                    <tr ng-if="newIndentVm.loading == true">
                        <td colspan="10">
                                                     <span style="font-size: 15px;">
                                                         <img src="app/assets/bower_components/cassini-platform/images/loaders/loader19.gif"
                                                              class="mr5">Loading Items...
                                                     </span>
                        </td>
                    </tr>

                    <tr ng-if="newIndentVm.loading == false && newIndentVm.groupedItems.length == 0">
                        <td colspan="10">No Items are available to view</td>
                    </tr>
                    <tr ng-repeat="indentItem in newIndentVm.groupedItems">
                        <td ng-if="indentItem.rowType == 'req'" colspan="12">
                            <h5 style="margin: 0;font-weight: bold;">{{indentItem.materialItem.itemNumber}}</h5>
                        </td>
                        <td ng-if="indentItem.rowType == 'item'">
                            <span>{{indentItem.materialItem.itemNumber}}</span>
                        </td>
                        <td ng-if="indentItem.rowType == 'item'">
                            <span>{{indentItem.materialItem.itemName}}</span>
                        </td>
                        <td ng-if="indentItem.rowType == 'item'">
                            <span>{{indentItem.reqItemQuantity}}</span>
                        </td>

                        <td ng-if="indentItem.rowType == 'item'">
                            <input ng-if="indentItem.editMode || (indentItem.editMode && !indentItem.isNew)"
                                   ng-model="indentItem.indentItemNotesObject" type="text"
                                   class="form-control input-sm">
                            <span ng-if="!indentItem.editMode && !indentItem.isNew">{{indentItem.indentItemNotesObject}}</span>
                        </td>

                        <td ng-if="indentItem.rowType == 'item'" style="text-align: center;width:100px;">
                            <input ng-if="indentItem.editMode || (indentItem.editMode && !indentItem.isNew)"
                                   type="number" min="1"
                                   class="form-control input-sm"
                                   ng-model="indentItem.indentItemQuantity"
                                   ng-change="newIndentVm.onChangeIndentQty(indentItem)"
                                   style="text-align: center;">
                            <span ng-show="!indentItem.editMode && !indentItem.isNew">{{indentItem.indentItemQuantity}}</span>
                        </td>

                        <td ng-hide="newIndentVm.mode == 'approved'" ng-if="indentItem.rowType == 'item'"
                            style="text-align: center;vertical-align: middle;">
                            <div class="btn-group btn-group-xs">
                                <button ng-if="!indentItem.showEditButton && !indentItem.isNew"
                                        class="btn btn-xs btn-success"
                                        ng-click="newIndentVm.applyChanges(indentItem)" title="Apply"><i
                                        class="fa fa-check"></i></button>

                                <button ng-if="!indentItem.showEditButton && !indentItem.isNew"
                                        class="btn btn-xs btn-default"
                                        ng-click="newIndentVm.cancelChanges(indentItem)" title="Cancel Changes"><i
                                        class="fa fa-times"></i></button>

                                <button ng-if="indentItem.showEditButton && !indentItem.isNew"
                                        title="Edit This Item"
                                        class="btn btn-xs btn-warning"
                                        ng-click="newIndentVm.editIndentItem(indentItem)"><i class="fa fa-edit"></i>
                                </button>

                                <button ng-if="indentItem.showEditButton && !indentItem.isNew"
                                        title="Delete This item"
                                        class="btn btn-xs btn-danger"
                                        ng-click="newIndentVm.deleteIndentItem(indentItem)"><i class="fa fa-trash"></i>
                                </button>

                                <i title="Remove" ng-if="indentItem.isNew"
                                   class="fa fa-minus-circle"
                                   style="font-size: 20px;"
                                   ng-click="newIndentVm.removeFromIndentItems(indentItem)"
                                   aria-hidden="true"></i>
                            </div>
                        </td>
                    </tr>
                    </tbody>
                </table>
            </div>

        </div>
    </div>
</div>

