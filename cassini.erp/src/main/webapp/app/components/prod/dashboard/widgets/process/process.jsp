<div class="panel panel-default panel-alt widget-messaging" style="height: 350px">
    <div class="panel-heading" style="background-color: #E4E7EA; padding:0; height: 56px">
        <div class="row" style="padding-right: 10px;">
            <div class="panel-title col-sm-8" style="font-size:15px; padding: 20px 0 0 20px">Process</div>
            <div style="float:right;padding-right: 15px;padding-top: 12px;">
                <button class="btn btn-primary mr5 btn-sm" data-ng-click="addNewProcess();">
                    <i class="glyphicon glyphicon-plus"></i> Add Process
                </button>
            </div>
        </div>
    </div>
    <div class="panel-body" style=" min-height: 250px; max-height: 250px; overflow: auto">

        <table class="table table-striped">
            <thead>
            <tr>
                <th>Actions</th>
                <th>Name</th>
                <th>Description</th>
            </tr>
            </thead>
            <tbody>
            <tr ng-if="processList.length == 0">
                <td colspan="7">No Records</td>
            </tr>

            <tr data-ng-repeat="process in processList">
                <td>
                    <div class="btn-group" dropdown ng-hide="process.editMode">
                        <button type="button" class="btn btn-sm btn-default dropdown-toggle actionbtn" dropdown-toggle>
                            <i class="fa fa-cog fa-fw"></i></span>
                        </button>
                        <ul class="dropdown-menu" role="menu">
                            <li><a href="" ng-click="showEditMode(process)">Edit</a></li>
                            <li><a href="" ng-click="removeItem($index,process);">Delete</a></li>
                        </ul>
                    </div>

                    <div class="btn-group" ng-show="process.editMode">
                        <button type="button" class="btn btn-sm btn-success" ng-click="acceptChanges(process)"><i
                                class="fa fa-check"></i></button>
                        <button type="button" class="btn btn-sm btn-default" ng-click="hideEditMode(process);"><i
                                class="fa fa-times"></i></button>
                    </div>
                </td>
                <td>
                    <span ng-hide="process.editMode">{{process.name}}</span>
                    <input ng-show="process.editMode" placeholder="Enter name" class="form-control" type="text"
                           data-ng-model="process.name">
                </td>
                <td>
                    <span ng-hide="process.editMode">{{process.description}}</span>
                    <input ng-show="process.editMode" placeholder="Enter description" class="form-control" type="text"
                           data-ng-model="process.description">
                </td>
            </tr>
            </tbody>
        </table>
    </div>
</div>


