<div class="panel panel-default panel-alt" style="height: 350px">
    <div class="panel-heading" style="background-color: #E4E7EA; padding:0; height: 56px">
        <div class="row" style="padding-right: 10px;">
            <div class="panel-title col-sm-8" style="font-size:15px; padding: 20px 0 0 20px">Employee Work Shifts</div>
            <div style="float:right;padding-right: 15px;padding-top: 12px;">
                <button class="btn btn-primary mr5 btn-sm" data-ng-click="addWorkShiftEmps();">
                    <i class="glyphicon glyphicon-plus"></i> Add Emp WorkShift
                </button>
            </div>
        </div>
    </div>
    <div class="panel-heading" style="background-color: #F6F6F6; padding-top:15px; height: 56px">
        <div class="row" style="padding-left: 60px;padding-right: 10px;">
            <label data-ng-repeat="workShift in workShifts" class="col-md-4">
                <input type="radio" name="shiftEmp" data-ng-model="workShift.defaultSelect" data-ng-value="true"
                       data-ng-click="init(workShift.shiftId)"/>
                {{workShift.name}}
            </label>
        </div>
    </div>
    <div class="panel-body" style=" min-height: 250px; max-height: 250px; overflow: auto">

        <table class="table table-striped">
            <thead>
            <tr>
                <th>Actions</th>
                <th>First Name</th>
                <th>Last Name</th>
                <th>Work Center</th>
            </tr>
            </thead>
            <tbody>
            <tr ng-if="workShiftEmpList.length == 0">
                <td colspan="7">No Records</td>
            </tr>

            <tr data-ng-repeat="workShiftEmp in workShiftEmpList">
                <td>
                    <div class="btn-group" dropdown ng-hide="workShiftEmp.editMode">
                        <button type="button" class="btn btn-sm btn-default dropdown-toggle actionbtn" dropdown-toggle>
                            <i class="fa fa-cog fa-fw"></i></span>
                        </button>
                        <ul class="dropdown-menu" role="menu">
                            <li><a href="" ng-click="showEditMode(workShiftEmp)">Edit</a></li>
                            <li><a href="" ng-click="removeItem($index,workShiftEmp);">Delete</a></li>
                        </ul>
                    </div>

                    <div class="btn-group" ng-show="workShiftEmp.editMode">
                        <button type="button" class="btn btn-sm btn-success" ng-click="acceptChanges(workShiftEmp)"><i
                                class="fa fa-check"></i></button>
                        <button type="button" class="btn btn-sm btn-default" ng-click="hideEditMode(workShiftEmp);"><i
                                class="fa fa-times"></i></button>
                    </div>
                </td>
                <td>
                    <span ng-hide="workShiftEmp.editMode">{{workShiftEmp.employee.firstName}}</span>
                    <input ng-show="workShiftEmp.editMode" placeholder="Enter name" class="form-control"
                           data-ng-disabled="true" type="text" data-ng-model="workShiftEmp.employee.firstName">
                </td>
                <td>
                    <span ng-hide="workShiftEmp.editMode">{{workShiftEmp.employee.lastName}}</span>
                    <input ng-show="workShiftEmp.editMode" placeholder="Enter Name" data-ng-disabled="true"
                           class="form-control" type="text" data-ng-model="workShiftEmp.employee.lastName">
                </td>
                <td>
                    <span ng-hide="workShiftEmp.editMode">{{workShiftEmp.workcenter.name}}</span>

                    <div ng-show="workShiftEmp.editMode">
                        <ui-select ng-model="workShiftEmp.workcenter" theme="bootstrap" style="width: 100%;"
                                   title="Choose a Workcenter">
                            <ui-select-match placeholder="Choose a Workcenter">{{$select.selected.name}}
                            </ui-select-match>
                            <ui-select-choices repeat="workcenter in workcenterList | filter: $select.search">
                                <div ng-bind-html="workcenter.name | highlight: $select.search"></div>
                            </ui-select-choices>
                        </ui-select>
                    </div>

                </td>

            </tr>
            </tbody>
        </table>
    </div>
</div>