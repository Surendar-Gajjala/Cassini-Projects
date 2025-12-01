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
                    title="New Auto Number"
                    style="position: absolute;right: 10px;top: -65px;width: 40px;"
                    ng-click='autoVm.addAutonumber()'>
                <i class="fa fa-plus"
                   style="font-size: 20px;vertical-align: middle;color: #adadad;"></i>
            </button>
        </div>
        <div class="responsive-table">
            <table class="table table-striped highlight-row">
                <thead>
                <tr>
                    <th style="">NAME</th>
                    <th style="">DESCRIPTION</th>
                    <th style="width:100px; text-align: center">Numbers</th>
                    <th style="width:100px; text-align: center">Start</th>
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
                    <td colspan="10"><span>No Auto Numbers</span></td>
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

                    <td style="width:120px; text-align: center; vertical-align: middle;">
                        <div class="btn-group" ng-if="!autonumber.editMode">
                            <button type="button" class="btn btn-xs btn-warning"
                                    ng-click="autoVm.showEditMode(autonumber)" title="Edit">
                                <i class="fa fa-edit"></i></span>
                            </button>
                            <button type="button" class="btn btn-xs btn-danger"
                                    ng-click="autoVm.deleteAutonumber(autonumber)" title="Delete">
                                <i class="fa fa-trash"></i></span>
                            </button>
                        </div>


                        <div class="btn-group" style="margin-bottom: 0px;" ng-if="autonumber.editMode">
                            <button type="button" class="btn btn-xs btn-success"
                                    ng-click="autoVm.acceptChanges(autonumber)" title="Save"><i
                                    class="fa fa-check"></i></button>
                            <button type="button" class="btn btn-xs btn-default"
                                    title="Cancel Changes"
                                    ng-click="autoVm.hideEditMode(autonumber);"><i class="fa fa-times"></i></button>
                        </div>
                    </td>
                </tr>
                </tbody>
            </table>
        </div>
    </div>
</div>

