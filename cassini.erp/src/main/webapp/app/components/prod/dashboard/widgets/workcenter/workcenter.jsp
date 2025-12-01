<div class="panel panel-default panel-alt widget-messaging" style="height: 350px">
    <div class="panel-heading" style="background-color: #E4E7EA; padding:0; height: 56px">
        <div class="row" style="padding-right: 10px;">
            <div class="panel-title col-sm-8" style="font-size:15px; padding: 20px 0 0 20px">Work Centers</div>
            <div style="float:right;padding-right: 15px;padding-top: 12px;">
                <button class="btn btn-primary mr5 btn-sm" data-ng-click="addNewWorkcenter();">
                    <i class="glyphicon glyphicon-plus"></i> Add Workcenter
                </button>
            </div>
        </div>
    </div>
    <div class="panel-body" style=" min-height: 250px; max-height: 300px; overflow: auto">
        <table class="table table-striped">
            <thead>
            <tr>
                <th>Actions</th>
                <th>Name</th>
                <th>Description</th>
            </tr>
            </thead>
            <tbody>
            <tr ng-if="workcenterObj.length == 0">
                <td colspan="7">No Records</td>
            </tr>

            <tr data-ng-repeat="workcenter in workcenterObj">
                <td>
                    <div class="btn-group" dropdown ng-hide="workcenter.editMode">
                        <button type="button" class="btn btn-sm btn-default dropdown-toggle actionbtn" dropdown-toggle>
                            <i class="fa fa-cog fa-fw"></i></span>
                        </button>
                        <ul class="dropdown-menu" role="menu">
                            <li><a href="" ng-click="showEditMode(workcenter)">Edit</a></li>
                            <li><a href="" ng-click="removeItem($index,workcenter);">Delete</a></li>
                        </ul>
                    </div>

                    <div class="btn-group" ng-show="workcenter.editMode">
                        <button type="button" class="btn btn-sm btn-success" ng-click="acceptChanges(workcenter)"><i
                                class="fa fa-check"></i></button>
                        <button type="button" class="btn btn-sm btn-default" ng-click="hideEditMode(workcenter);"><i
                                class="fa fa-times"></i></button>
                    </div>
                </td>
                <td>
                    <span ng-hide="workcenter.editMode">{{workcenter.name}}</span>
                    <input ng-show="workcenter.editMode" placeholder="Enter name" class="form-control" type="text"
                           data-ng-model="workcenter.name">
                </td>
                <td>
                    <span ng-hide="workcenter.editMode">{{workcenter.description}}</span>
                    <input ng-show="workcenter.editMode" placeholder="Enter description" class="form-control"
                           type="text" data-ng-model="workcenter.description">
                </td>
            </tr>
            </tbody>
        </table>
    </div>
</div>

