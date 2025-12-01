<div ng-if="itemTypeVm.itemType == null" style="padding: 10px">
    <span>Select an item type to see/edit details</span>
</div>
<div ng-if="itemTypeVm.itemType != null">
    <div>
        <button class="btn btn-xs btn-success min-width" ng-click="itemTypeVm.onSave()">Save</button>
    </div>
    <br>
    <h4 class="section-title">Basic Info</h4>


    <div class="row">
        <div class="col-xs-12 col-sm-8 col-sm-offset-1 col-md-6 col-md-offset-2">

            <form class="form-horizontal">
                <div class="form-group">
                    <label class="col-sm-4 control-label">Name: </label>
                    <div class="col-sm-7">
                        <input type="text" class="form-control" name="title" ng-model="itemTypeVm.itemType.name">
                    </div>
                </div>

                <div class="form-group">
                    <label class="col-sm-4 control-label">Description: </label>
                    <div class="col-sm-7">
                        <textarea name="description" rows="5" class="form-control" style="resize: none"
                                  ng-model="itemTypeVm.itemType.description"></textarea>
                    </div>
                </div>

                <div class="form-group">
                    <label class="col-sm-4 control-label">Auto Number Source: </label>
                    <div class="col-sm-7">
                        <ui-select ng-model="itemTypeVm.itemType.itemNumberSource" theme="bootstrap" style="width:100%">
                            <ui-select-match placeholder="Select">{{$select.selected.name}}</ui-select-match>
                            <ui-select-choices repeat="source in itemTypeVm.autoNumbers | filter: $select.search">
                                <div ng-bind="source.name"></div>
                            </ui-select-choices>
                        </ui-select>
                    </div>
                </div>

                <div class="form-group">
                    <label class="col-sm-4 control-label">Revision Sequence: </label>
                    <div class="col-sm-7">
                        <ui-select ng-model="itemTypeVm.itemType.revisionSequence" theme="bootstrap" style="width:100%">
                            <ui-select-match placeholder="Select">{{$select.selected.name}}</ui-select-match>
                            <ui-select-choices repeat="lov in itemTypeVm.revSequences | filter: $select.search">
                                <div ng-bind="lov.name"></div>
                            </ui-select-choices>
                        </ui-select>
                    </div>
                </div>
                <div class="form-group">
                    <label class="col-sm-4 control-label">Revision Sequence: </label>
                    <div class="col-sm-7">
                        <ui-select ng-model="itemTypeVm.itemType.lifeCycleStates" theme="bootstrap" style="width:100%">
                            <ui-select-match placeholder="Select">{{$select.selected.name}}</ui-select-match>
                            <ui-select-choices repeat="state in itemTypeVm.lifecycleStates | filter: $select.search">
                                <div ng-bind="state.name"></div>
                            </ui-select-choices>
                        </ui-select>
                    </div>
                </div>
            </form>
        </div>
    </div>

    <br>
    <h4 class="section-title">Attributes</h4>
    <br>

    <div class="row">
        <div style="margin-left: 10px;">
            <div>
                <button class="btn btn-xs btn-primary" ng-click="itemTypeVm.addAttribute()">New Attribute</button>
                <span style="margin-left: 249px; color: red;">{{itemTypeVm.error}}</span>
            </div>
            <div class="responsive-table">
                <table class="table table-striped">
                    <thead>
                    <tr>
                        <th style="width: 200px">Name</th>
                        <th>Description</th>
                        <th style="width: 120px">Data Type</th>
                        <th style="width: 100px; text-align: center">Is Required</th>
                        <th style="width: 100px; text-align: center">Actions</th>
                    </tr>
                    </thead>

                    <tbody>
                    <tr ng-if="itemTypeVm.itemType.attributes.length == 0">
                        <td colspan="7">No attributes</td>
                    </tr>
                    <tr ng-repeat="attribute in itemTypeVm.itemType.attributes">
                        <td style="width: 200px">
                            <span ng-if="attribute.showValues == true">{{attribute.name}}</span>
                            <input ng-if="attribute.editMode == true" type=" text" class="form-control input-sm" ng-model="attribute.newName">
                        </td>
                        <td>
                            <span ng-if="attribute.showValues == true">{{attribute.description}}</span>
                            <input ng-if="attribute.editMode == true" type="text" class="form-control input-sm" ng-model="attribute.newDescription">
                        </td>
                        <td style="width: 120px">
                            <span ng-if="attribute.showValues == true">{{attribute.dataType}}</span>
                            <select ng-if="attribute.editMode == true" class="form-control input-sm" ng-model="attribute.newDataType">
                                <option value="STRING">STRING</option>
                                <option value="INTEGER">INTEGER</option>
                                <option value="DOUBLE">DOUBLE</option>
                                <option value="DATE">DATE</option>
                                <option value="BOOLEAN">BOOLEAN</option>
                                <option value="LIST">LIST</option>
                            </select>
                        </td>
                        <td style="width: 100px; text-align: center">
                            <input type="checkbox" class="form-control input-sm" ng-model="attribute.required" ng-disabled="attribute.editMode == false">
                        </td>
                        <td style="width: 100px; text-align: center">
                            <button ng-if="attribute.editMode == true" class="btn btn-xs btn-success" ng-click="itemTypeVm.applyChanges(attribute)"><i class="fa fa-check"></i></button>
                            <button ng-if="attribute.editMode == true" class="btn btn-xs btn-default" ng-click="itemTypeVm.cancelChanges(attribute)"><i class="fa fa-times"></i></button>
                            <button title="Edit" ng-if="attribute.showValues == true" class="btn btn-xs btn-warning"ng-click="itemTypeVm.editAttribute(attribute)"><i class="fa fa-edit"></i></button>
                            <button title="Delete" ng-if="attribute.showValues == true" class="btn btn-xs btn-danger" ng-click="itemTypeVm.deleteAttribute(attribute)"><i class="fa fa-trash"></i></button>
                        </td>
                    </tr>
                    </tbody>
                </table>
            </div>
        </div>
    </div>
</div>