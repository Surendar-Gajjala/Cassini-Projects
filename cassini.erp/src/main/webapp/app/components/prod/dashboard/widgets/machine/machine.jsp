<div class="panel panel-default panel-alt widget-messaging" style="height: 350px">
    <div class="panel-heading" style="background-color: #E4E7EA; padding:0; height: 56px">
        <div class="row" style="padding-right: 10px;">
            <div class="panel-title col-sm-8" style="font-size:15px; padding: 20px 0 0 20px">Machines</div>
            <div style="float:right;padding-right: 15px;padding-top: 12px;">
                <button class="btn btn-primary mr5 btn-sm" data-ng-click="addNewMachine();">
                    <i class="glyphicon glyphicon-plus"></i> Add Machine
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
                <th>Workcenter</th>
            </tr>
            </thead>
            <tbody>
            <tr ng-if="machineList.length == 0">
                <td colspan="7">No Records</td>
            </tr>

            <tr data-ng-repeat="machine in machineList">
                <td>
                    <div class="btn-group" dropdown ng-hide="machine.editMode">
                        <button type="button" class="btn btn-sm btn-default dropdown-toggle actionbtn" dropdown-toggle>
                            <i class="fa fa-cog fa-fw"></i></span>
                        </button>
                        <ul class="dropdown-menu" role="menu">
                            <li><a href="" ng-click="showEditMode(machine)">Edit</a></li>
                            <li><a href="" ng-click="removeItem($index,machine);">Delete</a></li>
                        </ul>
                    </div>
                    <div class="btn-group" ng-show="machine.editMode">
                        <button type="button" class="btn btn-sm btn-success" ng-click="acceptChanges(machine)"><i
                                class="fa fa-check"></i></button>
                        <button type="button" class="btn btn-sm btn-default" ng-click="hideEditMode(machine);"><i
                                class="fa fa-times"></i></button>
                    </div>
                </td>

                <td>
                    <span ng-hide="machine.editMode">{{machine.name}}</span>
                    <input ng-show="machine.editMode" placeholder="Enter name" class="form-control" type="text"
                           data-ng-model="machine.name">
                </td>
                <td>
                    <span ng-hide="machine.editMode">{{machine.description}}</span>
                    <input ng-show="machine.editMode" placeholder="Enter description" class="form-control" type="text"
                           data-ng-model="machine.description">
                </td>
                <td>
                    <span ng-hide="machine.editMode">{{machine.workcenter.name}}</span>

                    <div ng-show="machine.editMode">
                        <ui-select ng-model="machine.workcenter" theme="bootstrap" style="width: 100%;"
                                   title="Choose a Workcenter">
                            <ui-select-match placeholder="Choose a Workcenter">{{$select.selected.name}}
                            </ui-select-match>
                            <ui-select-choices repeat="workcenter in workcenterList | filter: $select.search">
                                <div ng-bind-html="workcenter.name | highlight: $select.search"></div>
                            </ui-select-choices>
                        </ui-select>
                    </div>

                    <%-- <input ng-show="machine.editMode" placeholder="Enter Work Center" class="form-control" type="text" data-ng-model="machine.workcenter">--%>
                </td>
            </tr>
            </tbody>
        </table>
    </div>
</div>
