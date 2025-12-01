<div classs="row">
    <style scoped>
        .addAutonumberButton:hover {
            background-color: #0081c2 !important;
            border-color: #028af3;
            cursor: pointer;
        }

        .addAutonumberButton:hover i {
            color: #fff !important;
        }
    </style>
    <div class="col-md-12">
        <div class="text-right">
            <button class='addAutonumberButton btn btn-sm btn-white'
                    title="New Autonumber"
                    style="position: absolute;right: 10px;top: -65px;width: 40px;"
                    ng-click='autoVm.addAutonumber()'
                    ng-disabled="hasPermission('permission.settings.addAutoNumbers') == false">
                <i class="fa fa-plus"
                   style="font-size: 20px;vertical-align: middle;color: #adadad;"></i>
            </button>
        </div>
        <div class="responsive-table">
            <table class="table table-striped">
                <thead>
                <tr>
                    <th style="">Name</th>
                    <th style="">Description</th>
                    <th style="width:100px; text-align: center">Numbers</th>
                    <th style="width:100px; text-align: center">&emsp;Start &emsp;</th>
                    <th style="width:100px; text-align: center">Increment</th>
                    <th style="width:100px; text-align: center">Pad With</th>
                    <th style="width:100px; text-align: left">Prefix</th>
                    <th style="width:100px; text-align: left">Suffix</th>
                    <th style="">Examples</th>
                    <th style="width:120px; text-align: center">Actions</th>
                </tr>
                </thead>
                <tbody>
                <tr ng-if="autoVm.autonumbers.length == 0">
                    <td colspan="10">No Auto Numbers are available to view</td>
                </tr>

                <tr ng-repeat="autonumber in autoVm.autonumbers">
                    <td style="vertical-align: middle;">
                        <span ng-show="autonumber.showValues">{{ autonumber.name }}</span>
                        <input placeholder="Enter name" class="form-control" type="text"
                               ng-show="autonumber.editMode" ng-model="autonumber.newName">
                    </td>

                    <td style="vertical-align: middle;">
                        <span ng-show="autonumber.showValues">{{ autonumber.description }}</span>
                        <input placeholder="Enter description" class="form-control" type="text"
                               ng-show="autonumber.editMode" ng-model="autonumber.newDescription">
                    </td>

                    <td style="width:100px; text-align: center; vertical-align: middle;">
                        <span ng-show="autonumber.showValues">{{ autonumber.numbers }}</span>
                        <input placeholder="" class="form-control" type="number"
                               ng-show="autonumber.editMode" ng-model="autonumber.newNumber">
                    </td>

                    <td style="width:100px; text-align: center; vertical-align: middle;">
                        <span ng-show="autonumber.showValues">{{ autonumber.start }}</span>
                        <input placeholder="" class="form-control" type="number"
                               ng-show="autonumber.editMode" ng-model="autonumber.newStart">
                    </td>

                    <td style="width:100px; text-align: center; vertical-align: middle;">
                        <span ng-show="autonumber.showValues">{{ autonumber.increment }}</span>
                        <input placeholder="" class="form-control" type="number"
                               ng-show="autonumber.editMode" ng-model="autonumber.newIncrement">
                    </td>

                    <td style="width:100px; text-align: center; vertical-align: middle;">
                        <span ng-show="autonumber.showValues">{{ autonumber.padwith }}</span>
                        <input placeholder="" class="form-control" type="text"
                               ng-show="autonumber.editMode" ng-model="autonumber.newPadwith">
                    </td>

                    <td style="width:100px; text-align: left; vertical-align: middle;">
                        <span ng-show="autonumber.showValues">{{ autonumber.prefix }}</span>
                        <input placeholder="" class="form-control" type="text"
                               ng-show="autonumber.editMode" ng-model="autonumber.newPrefix">
                    </td>

                    <td style="width:100px; text-align: left; vertical-align: middle;">
                        <span ng-show="autonumber.showValues">{{ autonumber.suffix }}</span>
                        <input placeholder="" class="form-control" type="text"
                               ng-show="autonumber.editMode" ng-model="autonumber.newSuffix">
                    </td>
                    <td style="vertical-align: middle;">
                        {{ autoVm.getAutonumberExamples(autonumber) }} ...
                    </td>

                    <td class="text-center" style="vertical-align: middle;">
                                    <span class="row-menu" uib-dropdown dropdown-append-to-body style="min-width: 50px">
                                        <i class="fa fa-ellipsis-h dropdown-toggle" uib-dropdown-toggle></i>
                                    <ul uib-dropdown-menu class="dropdown-menu dropdown-menu-right" role="menu"
                                        style="z-index: 9999 !important;">
                                        <li ng-click="autoVm.acceptChanges(autonumber)">
                                            <a class="dropdown-item" title="Apply Changes"
                                               ng-if="autonumber.editMode == true">
                                                Save
                                            </a></li>
                                        <li ng-click="autoVm.hideEditMode(autonumber);">
                                            <a class="dropdown-item" type="button"
                                               ng-if="autonumber.editMode == true"
                                               title="Cancel Changes">
                                                Cancel
                                            </a>
                                        </li>
                                        <li ng-click="!hasPermission('permission.settings.editAutoNumbers') || autoVm.showEditMode(autonumber)"
                                            ng-class="{'disabled':!hasPermission('permission.settings.editAutoNumbers')}"
                                            ng-if="!autonumber.editMode">
                                            <a title="Edit this autonumber"
                                               class="dropdown-item"
                                               ng-disabled="autonumber.itemsExist == true">
                                                Edit
                                            </a>
                                        </li>
                                        <li ng-click="!hasPermission('permission.settings.deleteAutoNumbers') || autoVm.deleteAutonumber($index)"
                                            ng-class="{'disabled':!hasPermission('permission.settings.deleteAutoNumbers')}"
                                            ng-if="!autonumber.editMode">
                                            <a title="Delete this autonumber"
                                               type="button"
                                               class="dropdown-item"
                                               ng-disabled="autonumber.itemsExist == true">
                                                Delete
                                            </a>
                                        </li>
                                    </ul>
                                        </span>
                    </td>

                </tr>
                </tbody>
            </table>
        </div>
    </div>
</div>