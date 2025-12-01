<div class="row settings-panel">
    <div class="col-md-12 bhoechie-tab-container" v-tabs>
        <div>
            <div class="bhoechie-tab-menu">
                <div class="list-group">
                    <a href="#" class="list-group-item active">
                        <h5>Employee Type</h5>
                    </a>

                    <a href="#" class="list-group-item">
                        <h5>Department</h5>
                    </a>
                    <a href="#" class="list-group-item">
                        <h5>Allowance Type</h5>
                    </a>
                    <a href="#" class="list-group-item">
                        <h5>Deduction Type</h5>
                    </a>
                    <a href="#" class="list-group-item">
                        <h5>Timeoff Type</h5>
                    </a>
                    <%--<a href="#" class="list-group-item">
                        <h5>Loan Type</h5>
                    </a>--%>
                    <a href="#" class="list-group-item">
                        <h5>Holidays</h5>
                    </a>
                </div>
            </div>
            <div class="bhoechie-tab" style="">
                <div class="bhoechie-tab-content active">
                    <div class="row">
                        <div class="col-sm-12">
                            <div class="row">
                                <div class="col-md-6"><h3>Employee Type</h3></div>
                                <div class="col-md-6 text-right mrtop"><button class="btn btn-sm btn-primary" data-ng-click="addEmployeeType($event,settingTypes.employee);">New Type</button></div>
                            </div>
                            <table class="table table-striped">
                                <thead>
                                <tr>
                                    <th>Actions</th>
                                    <th>Name</th>
                                    <th>Description</th>
                                </tr>
                                </thead>
                                <tbody>
                                <tr data-ng-repeat="employeeType in employeeTypes">
                                    <td>
                                        <div class="btn-group" dropdown ng-hide="employeeType.editMode">
                                            <button type="button" class="btn btn-sm btn-default dropdown-toggle actionbtn" dropdown-toggle>
                                                <i class="fa fa-cog fa-fw"></i></span>
                                            </button>
                                            <ul class="dropdown-menu" role="menu">
                                                <li><a href="" ng-click="showEditMode(employeeType)">Edit</a></li>
                                                <li><a href="" ng-click="removeItem($index,employeeType,settingTypes.employee);">Delete</a></li>
                                            </ul>
                                        </div>
                                        <div class="btn-group" ng-show="employeeType.editMode">
                                            <button type="button" class="btn btn-sm btn-success" ng-click="acceptChanges(employeeType,settingTypes.employee)"><i
                                                    class="fa fa-check"></i></button>
                                            <button type="button" class="btn btn-sm btn-default" ng-click="hideEditMode(employeeType,settingTypes.employee);"><i
                                                    class="fa fa-times"></i></button>
                                        </div>
                                    </td>
                                    <td>
                                        <span ng-hide="employeeType.editMode">{{employeeType.name}}</span>
                                        <input ng-show="employeeType.editMode" placeholder="Enter name" class="form-control" type="text" data-ng-model="employeeType.newName">
                                    </td>
                                    <td>
                                        <span ng-hide="employeeType.editMode">{{employeeType.description}}</span>
                                        <input ng-show="employeeType.editMode" placeholder="Enter description" class="form-control" type="text" data-ng-model="employeeType.newDescription">
                                    </td>
                                </tr>
                                </tbody>
                            </table>
                        </div>
                    </div>

                </div>
                <div class="bhoechie-tab-content">
                    <div class="row">
                        <div class="col-sm-12">
                            <div class="row">
                                <div class="col-md-6"><h3>Department</h3></div>

                                <div class="col-md-6 text-right mrtop"><button class="btn btn-sm btn-primary" data-ng-click="addEmployeeType($event,settingTypes.department);">New Department</button></div>


                            </div>
                            <table class="table table-striped">
                                <thead>
                                <tr>
                                    <th>Actions</th>
                                    <th>Name</th>
                                    <th>Description</th>
                                </tr>
                                </thead>
                                <tbody>
                                <tr data-ng-repeat="department in departments">
                                    <td>
                                        <div class="btn-group" dropdown ng-hide="department.editMode">
                                            <button type="button" class="btn btn-sm btn-default dropdown-toggle actionbtn" dropdown-toggle>
                                                <i class="fa fa-cog fa-fw"></i></span>
                                            </button>
                                            <ul class="dropdown-menu" role="menu">
                                                <li><a href="" ng-click="showEditMode(department)">Edit</a></li>
                                                <li><a href="" ng-click="removeItem($index,department,settingTypes.department);">Delete</a></li>
                                            </ul>
                                        </div>
                                        <div class="btn-group" ng-show="department.editMode">
                                            <button type="button" class="btn btn-sm btn-success" ng-click="acceptChanges(department,settingTypes.department)"><i
                                                    class="fa fa-check"></i></button>
                                            <button type="button" class="btn btn-sm btn-default" ng-click="hideEditMode(department,settingTypes.department);"><i
                                                    class="fa fa-times"></i></button>
                                        </div>
                                    </td>
                                    <td>
                                        <span ng-hide="department.editMode">{{department.name}}</span>
                                        <input ng-show="department.editMode" placeholder="Enter name" class="form-control" type="text" data-ng-model="department.newName">
                                    </td>
                                    <td>
                                        <span ng-hide="department.editMode">{{department.description}}</span>
                                        <input ng-show="department.editMode" placeholder="Enter description" class="form-control" type="text" data-ng-model="department.newDescription">
                                    </td>
                                </tr>
                                </tbody>
                            </table>
                        </div>
                    </div>
                </div>
                <div class="bhoechie-tab-content">
                    <div class="row">
                        <div class="col-sm-12">
                            <div class="row">
                                <div class="col-md-6"><h3>Allowance Type</h3></div>

                                <div class="col-md-6 text-right mrtop"><button class="btn btn-sm btn-primary" data-ng-click="addEmployeeType($event,settingTypes.allowance);">New Type</button></div>


                            </div>
                            <table class="table table-striped">
                                <thead>
                                <tr>
                                    <th>Actions</th>
                                    <th>Name</th>
                                    <th>Description</th>
                                </tr>
                                </thead>
                                <tbody>
                                <tr data-ng-repeat="allowance in allowances">
                                    <td>
                                        <div class="btn-group" dropdown ng-hide="allowance.editMode">
                                            <button type="button" class="btn btn-sm btn-default dropdown-toggle actionbtn" dropdown-toggle>
                                                <i class="fa fa-cog fa-fw"></i></span>
                                            </button>
                                            <ul class="dropdown-menu" role="menu">
                                                <li><a href="" ng-click="showEditMode(allowance)">Edit</a></li>
                                                <li><a href="" ng-click="removeItem($index,allowance,settingTypes.allowance);">Delete</a></li>
                                            </ul>
                                        </div>
                                        <div class="btn-group" ng-show="allowance.editMode">
                                            <button type="button" class="btn btn-sm btn-success" ng-click="acceptChanges(allowance,settingTypes.allowance)"><i
                                                    class="fa fa-check"></i></button>
                                            <button type="button" class="btn btn-sm btn-default" ng-click="hideEditMode(allowance,settingTypes.allowance);"><i
                                                    class="fa fa-times"></i></button>
                                        </div>
                                    </td>
                                    <td>
                                        <span ng-hide="allowance.editMode">{{allowance.name}}</span>
                                        <input ng-show="allowance.editMode" placeholder="Enter name" class="form-control" type="text" data-ng-model="allowance.newName">
                                    </td>
                                    <td>
                                        <span ng-hide="allowance.editMode">{{allowance.description}}</span>
                                        <input ng-show="allowance.editMode" placeholder="Enter description" class="form-control" type="text" data-ng-model="allowance.newDescription">
                                    </td>
                                </tr>
                                </tbody>
                            </table>
                        </div>
                    </div>
                </div>
                <div class="bhoechie-tab-content">
                    <div class="row">
                        <div class="col-sm-12">
                            <div class="row">
                                <div class="col-md-6"><h3>Deduction Type</h3></div>

                                <div class="col-md-6 text-right mrtop"><button class="btn btn-sm btn-primary" data-ng-click="addEmployeeType($event,settingTypes.deduction);">New Type</button></div>


                            </div>
                            <table class="table table-striped">
                                <thead>
                                <tr>
                                    <th>Actions</th>
                                    <th>Name</th>
                                    <th>Description</th>
                                </tr>
                                </thead>
                                <tbody>
                                <tr data-ng-repeat="deduction in deductions">
                                    <td>
                                        <div class="btn-group" dropdown ng-hide="deduction.editMode">
                                            <button type="button" class="btn btn-sm btn-default dropdown-toggle actionbtn" dropdown-toggle>
                                                <i class="fa fa-cog fa-fw"></i></span>
                                            </button>
                                            <ul class="dropdown-menu" role="menu">
                                                <li><a href="" ng-click="showEditMode(deduction)">Edit</a></li>
                                                <li><a href="" ng-click="removeItem($index,deduction,settingTypes.deduction);">Delete</a></li>
                                            </ul>
                                        </div>
                                        <div class="btn-group" ng-show="deduction.editMode">
                                            <button type="button" class="btn btn-sm btn-success" ng-click="acceptChanges(deduction,settingTypes.deduction)"><i
                                                    class="fa fa-check"></i></button>
                                            <button type="button" class="btn btn-sm btn-default" ng-click="hideEditMode(deduction,settingTypes.deduction);"><i
                                                    class="fa fa-times"></i></button>
                                        </div>
                                    </td>
                                    <td>
                                        <span ng-hide="deduction.editMode">{{deduction.name}}</span>
                                        <input ng-show="deduction.editMode" placeholder="Enter name" class="form-control" type="text" data-ng-model="deduction.newName">
                                    </td>
                                    <td>
                                        <span ng-hide="deduction.editMode">{{deduction.description}}</span>
                                        <input ng-show="deduction.editMode" placeholder="Enter description" class="form-control" type="text" data-ng-model="deduction.newDescription">
                                    </td>
                                </tr>
                                </tbody>
                            </table>
                        </div>
                    </div>
                </div>
                <div class="bhoechie-tab-content">
                    <div class="row">
                        <div class="col-sm-12">
                            <div class="row">
                                <div class="col-md-6"><h3>Timeoff Type</h3></div>

                                <div class="col-md-6 text-right mrtop"><button class="btn btn-sm btn-primary" data-ng-click="addEmployeeType($event,settingTypes.timeOff);">New Type</button></div>


                            </div>
                            <table class="table table-striped">
                                <thead>
                                <tr>
                                    <th>Actions</th>
                                    <th>Name</th>
                                    <th>Description</th>
                                </tr>
                                </thead>
                                <tbody>
                                <tr data-ng-repeat="timeoff in timeofftypes">
                                    <td>
                                        <div class="btn-group" dropdown ng-hide="timeoff.editMode">
                                            <button type="button" class="btn btn-sm btn-default dropdown-toggle actionbtn" dropdown-toggle>
                                                <i class="fa fa-cog fa-fw"></i></span>
                                            </button>
                                            <ul class="dropdown-menu" role="menu">
                                                <li><a href="" ng-click="showEditMode(timeoff)">Edit</a></li>
                                                <li><a href="" ng-click="removeItem($index,timeoff,settingTypes.timeOff);">Delete</a></li>
                                            </ul>
                                        </div>
                                        <div class="btn-group" ng-show="timeoff.editMode">
                                            <button type="button" class="btn btn-sm btn-success" ng-click="acceptChanges(timeoff,settingTypes.timeOff)"><i
                                                    class="fa fa-check"></i></button>
                                            <button type="button" class="btn btn-sm btn-default" ng-click="hideEditMode(timeoff,settingTypes.timeOff);"><i
                                                    class="fa fa-times"></i></button>
                                        </div>
                                    </td>
                                    <td>
                                        <span ng-hide="timeoff.editMode">{{timeoff.name}}</span>
                                        <input ng-show="timeoff.editMode" placeholder="Enter name" class="form-control" type="text" data-ng-model="timeoff.newName">
                                    </td>
                                    <td>
                                        <span ng-hide="timeoff.editMode">{{timeoff.description}}</span>
                                        <input ng-show="timeoff.editMode" placeholder="Enter description" class="form-control" type="text" data-ng-model="timeoff.newDescription">
                                    </td>
                                </tr>
                                </tbody>
                            </table>
                        </div>
                    </div>
                </div>
                <%--<div class="bhoechie-tab-content">
                    <div class="row">
                        <div class="col-sm-12">
                            <div class="row">
                                <div class="col-md-6"><h3>Loan Type</h3></div>

                                <div class="col-md-6 text-right mrtop"><button class="btn btn-sm btn-primary" data-ng-click="addEmployeeType($event,settingTypes.loan);">New Type</button></div>


                            </div>
                            <table class="table table-striped">
                                <thead>
                                <tr>
                                    <th>Actions</th>
                                    <th>Name</th>
                                    <th>Description</th>
                                </tr>
                                </thead>
                                <tbody>
                                <tr data-ng-repeat="loanType in loanTypes">
                                    <td>
                                        <div class="btn-group" dropdown ng-hide="loanType.editMode">
                                            <button type="button" class="btn btn-sm btn-default dropdown-toggle actionbtn" dropdown-toggle>
                                                <i class="fa fa-cog fa-fw"></i></span>
                                            </button>
                                            <ul class="dropdown-menu" role="menu">
                                                <li><a href="" ng-click="showEditMode(loanType)">Edit</a></li>
                                                <li><a href="" ng-click="removeItem($index,loanType,settingTypes.loan);">Delete</a></li>
                                            </ul>
                                        </div>
                                        <div class="btn-group" ng-show="loanType.editMode">
                                            <button type="button" class="btn btn-sm btn-success" ng-click="acceptChanges(loanType,settingTypes.loan)"><i
                                                    class="fa fa-check"></i></button>
                                            <button type="button" class="btn btn-sm btn-default" ng-click="hideEditMode(loanType,settingTypes.loan);"><i
                                                    class="fa fa-times"></i></button>
                                        </div>
                                    </td>
                                    <td>
                                        <span ng-hide="loanType.editMode">{{loanType.name}}</span>
                                        <input ng-show="loanType.editMode" placeholder="Enter name" class="form-control" type="text" data-ng-model="loanType.name">
                                    </td>
                                    <td>
                                        <span ng-hide="loanType.editMode">{{loanType.description}}</span>
                                        <input ng-show="loanType.editMode" placeholder="Enter description" class="form-control" type="text" data-ng-model="loanType.description">
                                    </td>
                                </tr>
                                </tbody>
                            </table>
                        </div>
                    </div>
                </div>--%>
                <div class="bhoechie-tab-content">
                    <div class="row">
                        <div class="col-sm-12">
                            <div class="row">
                                <div class="col-md-6"><h3>Holidays</h3></div>

                                <div class="col-md-6 text-right mrtop"><button class="btn btn-sm btn-primary" data-ng-click="addEmployeeType($event,settingTypes.holiday);">Add Holiday</button></div>


                            </div>
                            <table class="table table-striped">
                                <thead>
                                <tr>
                                    <th>Actions</th>
                                    <th>Date</th>
                                    <th>Name</th>
                                    <th>Description</th>
                                </tr>
                                </thead>
                                <tbody>
                                <tr data-ng-repeat="holiday in holidays">
                                    <td>
                                        <div class="btn-group" dropdown ng-hide="holiday.editMode">
                                            <button type="button" class="btn btn-sm btn-default dropdown-toggle actionbtn" dropdown-toggle>
                                                <i class="fa fa-cog fa-fw"></i></span>
                                            </button>
                                            <ul class="dropdown-menu" role="menu">
                                                <li><a href="" ng-click="showEditMode(holiday)">Edit</a></li>
                                                <li><a href="" ng-click="removeItem($index,holiday,settingTypes.holiday);">Delete</a></li>
                                            </ul>
                                        </div>
                                        <div class="btn-group" ng-show="holiday.editMode">
                                            <button type="button" class="btn btn-sm btn-success" ng-click="acceptChanges(holiday,settingTypes.holiday)"><i
                                                    class="fa fa-check"></i></button>
                                            <button type="button" class="btn btn-sm btn-default" ng-click="hideEditMode(holiday,settingTypes.holiday);"><i
                                                    class="fa fa-times"></i></button>
                                        </div>
                                    </td>
                                    <td>
                                        <span ng-hide="holiday.editMode">{{holiday.date}}</span>
                                        <div ng-show="holiday.editMode" class="input-group">
                                            <input type="text" class="form-control datepickercls" ng-model="holiday.newHdate" placeholder="dd/mm/yyyy" date-picker>
                                            <span class="input-group-addon"><i class="glyphicon glyphicon-calendar"></i></span>
                                        </div>
                                    </td>
                                    <td>
                                        <span ng-hide="holiday.editMode">{{holiday.name}}</span>
                                        <input ng-show="holiday.editMode" placeholder="Enter name" class="form-control" type="text" data-ng-model="holiday.newName">
                                    </td>
                                    <td>
                                        <span ng-hide="holiday.editMode">{{holiday.description}}</span>
                                        <input ng-show="holiday.editMode" placeholder="Enter description" class="form-control" type="text" data-ng-model="holiday.newDescription">
                                    </td>
                                </tr>
                                </tbody>
                            </table>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>

