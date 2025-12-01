<div class='responsive-table'>
    <table class='table table-striped'>
        <thead>
        <tr>
            <th translate>ITEM_NUMBER</th>
            <th translate>ITEM_TYPE</th>
            <th class="col-width-250" translate>DESCRIPTION</th>
            <th translate>ITEM_ALL_REVISION</th>
            <th translate>STATUS</th>
            <th translate>NOTES</th>
            <th style="text-align: center" translate>ACTIONS</th>
        </tr>
        </thead>
        <tbody>
        <tr ng-if="itemReferencesVm.loading == true">
            <td colspan="10">
                    <span style="font-size: 15px;">
                        <img src="app/assets/bower_components/cassini-platform/images/loaders/loader19.gif" class="mr5">Loading items...
                    </span>
            </td>
        </tr>

        <tr ng-if="itemReferencesVm.loading == false && itemReferencesVm.references.length == 0">
            <td colspan="10">No references</td>
        </tr>

        <tr ng-if="itemReferencesVm.references.length > 0"
            ng-repeat="reference in itemReferencesVm.references">
            <td>
                <span>
                    <a href="" ng-if="reference.isNew == false">
                        <span>{{reference.itemObject.itemNumber}}</span>
                    </a>
                    <span ng-if="reference.isNew == true">

                        <div class="input-group">
                            <div class="input-group-addon" ng-click="itemReferencesVm.refItemSelection(reference)">
                                <i class="fa fa-search"></i>
                            </div>
                            <input style="width: 150px;margin: 0" type="text" class="form-control input-sm"
                                   name="referenceNumber"
                                   ng-model="reference.itemObject.itemNumber"
                                   ng-blur="itemReferencesVm.findItem(reference)">
                        </div>
                    </span>

                </span>
            </td>
            <td>{{reference.itemObject.itemType.name}}</td>
            <td class="col-width-250">{{reference.itemObject.description}}</td>

            <td>{{reference.itemObject.revision}}</td>
            <td>{{reference.itemObject.status}}</td>

            <td>
                <span ng-if="reference.isNew != true && reference.editMode != true">{{reference.notes}}</span>
                <span ng-if="reference.isNew == true || reference.editMode == true">
                    <input style="width: 150px;margin: 0" type="text" class="form-control input-sm" name="notes"
                           ng-model="reference.newNotes">
                </span>
            </td>

            <td style="text-align: center">
                    <span class="btn-group" ng-if="reference.isNew == true || reference.editMode == true"
                          style="margin: 0">
                        <button class="btn btn-xs btn-default" type="button"
                                ng-click="itemReferencesVm.onOk(reference)">
                            <i class="fa fa-check"></i>
                        </button>
                        <button class="btn btn-xs btn-default" type="button"
                                ng-click="itemReferencesVm.onCancel(reference)">
                            <i class="fa fa-times"></i>
                        </button>
                    </span>

                    <span ng-if="reference.editMode == false && reference.isRoot == true">
                        <button title="Edit this reference" class="btn btn-xs btn-warning"
                                ng-click="itemReferencesVm.editItem(reference)">
                            <i class="fa fa-edit"></i>
                        </button>

                        <button title="Delete this reference" class="btn btn-xs btn-danger"
                                ng-click="itemReferencesVm.deleteItem(reference)">
                            <i class="fa fa-trash"></i>
                        </button>
                    </span>

                    <span ng-if="item.editMode == false && reference.isRoot == true">
                        <button title="Edit this item" class="btn btn-xs btn-warning"
                                ng-click="itemReferencesVm.editItem(reference)">
                            <i class="fa fa-edit"></i>
                        </button>

                        <button title="Delete this item" class="btn btn-xs btn-danger"
                                ng-click="itemReferencesVm.deleteItem(reference)">
                            <i class="fa fa-trash"></i>
                        </button>
                    </span>
            </td>
        </tr>
        </tbody>
    </table>
</div>