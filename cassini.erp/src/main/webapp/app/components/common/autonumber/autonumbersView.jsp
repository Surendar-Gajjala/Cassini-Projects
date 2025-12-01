<div classs="row">
    <div class="col-md-12">
        <button type="button" class="mr5 btn btn-primary pull-right" ng-click="saveAutonumbers()">Save</button>
        <button type="button" class="mr5 btn btn-default" ng-click="addAutonumber()">New Autonumber</button>

        <br/><br/>
        <div class="table-responsive">
            <table class="table table-striped table-bordered">
                <thead>
                <tr>
                    <th style="width:120px; text-align: center">Action</th>
                    <th style="">Name</th>
                    <th style="">Description</th>
                    <th style="width:100px; text-align: center">Numbers</th>
                    <th style="width:100px; text-align: center">Start</th>
                    <th style="width:100px; text-align: center">Increment</th>
                    <th style="width:100px; text-align: center">Pad With</th>
                    <th style="width:100px; text-align: center">Prefix</th>
                    <th style="width:100px; text-align: center">Suffix</th>
                    <th style="width:250px; text-align: center">Examples</th>
                </tr>
                </thead>
                <tbody>
                <tr ng-if="autonumbers.length == 0">
                    <td colspan="10">No Autonumbers</td>
                </tr>

                <tr ng-repeat="autonumber in autonumbers">
                    <td style="width:120px; text-align: center; vertical-align: middle;">
                        <div class="btn-group" dropdown style="margin-bottom: 0px;" ng-hide="autonumber.editMode">
                            <button type="button" class="btn btn-sm btn-default dropdown-toggle" dropdown-toggle>
                                <i class="fa fa-cog fa-fw"></i></span>
                            </button>
                            <ul class="dropdown-menu" role="menu">
                                <li><a href="" ng-click="showEditMode(autonumber)">Edit</a></li>
                                <li><a href="" ng-click="removeItem($index);">Delete</a></li>
                            </ul>
                        </div>


                        <div class="btn-group" style="margin-bottom: 0px;" ng-show="autonumber.editMode">
                            <button type="button" class="btn btn-sm btn-success" ng-click="acceptChanges(autonumber)"><i class="fa fa-check"></i></button>
                            <button type="button" class="btn btn-sm btn-default" ng-click="hideEditMode(autonumber);"><i class="fa fa-times"></i></button>
                        </div>
                    </td>

                    <td style="vertical-align: middle;">
                        <span ng-show="autonumber.showValues">{{ autonumber.name }}</span>
                        <input placeholder="Enter name" class="form-control" type="text"
                               ng-show="autonumber.editMode" ng-model="autonumber.name">
                    </td>

                    <td style="vertical-align: middle;">
                        <span ng-show="autonumber.showValues">{{ autonumber.description }}</span>
                        <input placeholder="Enter description" class="form-control" type="text"
                               ng-show="autonumber.editMode" ng-model="autonumber.description">
                    </td>

                    <td style="width:100px; text-align: center; vertical-align: middle;">
                        <span ng-show="autonumber.showValues">{{ autonumber.numbers }}</span>
                        <input placeholder="" class="form-control" type="number"
                               ng-show="autonumber.editMode" ng-model="autonumber.numbers">
                    </td>

                    <td style="width:100px; text-align: center; vertical-align: middle;">
                        <span ng-show="autonumber.showValues">{{ autonumber.start }}</span>
                        <input placeholder="" class="form-control" type="number"
                               ng-show="autonumber.editMode" ng-model="autonumber.start">
                    </td>

                    <td style="width:100px; text-align: center; vertical-align: middle;">
                        <span ng-show="autonumber.showValues">{{ autonumber.increment }}</span>
                        <input placeholder="" class="form-control" type="number"
                               ng-show="autonumber.editMode" ng-model="autonumber.increment">
                    </td>

                    <td style="width:100px; text-align: center; vertical-align: middle;">
                        <span ng-show="autonumber.showValues">{{ autonumber.padwith }}</span>
                        <input placeholder="" class="form-control" type="text"
                               ng-show="autonumber.editMode" ng-model="autonumber.padwith">
                    </td>

                    <td style="width:100px; text-align: center; vertical-align: middle;">
                        <span ng-show="autonumber.showValues">{{ autonumber.prefix }}</span>
                        <input placeholder="" class="form-control" type="text"
                               ng-show="autonumber.editMode" ng-model="autonumber.prefix">
                    </td>

                    <td style="width:100px; text-align: center; vertical-align: middle;">
                        <span ng-show="autonumber.showValues">{{ autonumber.suffix }}</span>
                        <input placeholder="" class="form-control" type="text"
                               ng-show="autonumber.editMode" ng-model="autonumber.suffix">
                    </td>
                    <td style="width:250px; text-align: center; vertical-align: middle;">
                        {{ getAutonumberExamples(autonumber) }}
                    </td>
                </tr>
                </tbody>
            </table>
        </div>
    </div>
</div>