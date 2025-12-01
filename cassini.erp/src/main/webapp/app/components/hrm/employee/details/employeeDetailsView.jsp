<div class="row">
    <div class="col-sm-2 styled-panel">
        <img src="app/assets/images/user-alt-256.png" class="thumbnail img-responsive" alt=""/>

        <h5 class="subtitle">Address</h5>
        <address>
            {{employeeDetails.addresses[0].addressText}}
            <br>
            {{employeeDetails.addresses[0].city}} - {{employeeDetails.addresses[0].pincode}}<br>
            <abbr title="Office Phone">O :</abbr> {{employeeDetails.phoneOffice}}
            <br/>
            <abbr title="Phone">P :</abbr> {{employeeDetails.phoneMobile}}
        </address>
    </div>

    <div class="col-sm-10">
        <div class="profile-header">
            <h2 class="profile-name">{{employeeDetails.firstName}}</h2>

            <div><i class="fa fa-group"></i> Department : {{departmentDetails.name}}</div>
            <div><i class="fa fa-user"></i> Role : {{employeeDetails.jobTitle}}</div>

            <div class="mb20"></div>
            <div class="row">
                <div class="col-md-12 mb20">
                    <button class="btn btn-danger btn-sm" data-ng-click="showChangePassword();"><i class="fa fa-key"></i> Change Password</button>
                    <button class="btn btn-sm btn-info" data-ng-click="editEmpInfo();"><i class="fa fa-edit"></i> Edit Info</button>
                    <button class="btn btn-success btn-sm" data-ng-click="timeOffRequest();" ng-if="isCurrentUserSameAsEmployee()">
                        <i class="fa fa-clock-o"></i> Time off Request
                    </button>
                    <%--<button class="btn btn-warning btn-sm" data-ng-click="loanRequest();" ng-if="isCurrentUserSameAsEmployee()">
                        <i class="fa fa-money"></i> Loan Request
                    </button>--%>
                </div>
            </div>

            <ul class="nav nav-tabs nav-justified nav-profile">

                <li class="active"><a href="#empdetails" data-toggle="tab"><strong>Employee Info</strong></a></li>
                <li><a href="#emtimeoff" data-toggle="tab"><strong>Timeoff</strong></a></li>
                <%--<li><a href="#emloan" data-toggle="tab" data-ng-click="loanDetails();"><strong>LoanDetails</strong></a></li>--%>
                <li><a href="#emcalender" data-toggle="tab"><strong>Attendance</strong></a></li>
                <li><a href="#empayroll" data-toggle="tab"><strong>Payroll</strong></a></li>
            </ul>

            <div class="tab-content">
                <div class="tab-pane active" id="empdetails">

                    <div ng-if="changePassword">
                        <div ng-include="changePasswordTemplate" ng-controller="ChangePasswordController"></div>
                    </div>
                    <div ng-hide="changePassword">
                        <form class="form" angular-validator-submit="updateEmpInfo()" name="employee" novalidate angular-validator>
                            <div class="row mb15">

                                <div class="col-sm-6 employee-details">
                                    <h4 class="text-primary">Person Details</h4>

                                    <div ng-hide="employeeDetails.isEdit" class="employee-details">
                                        <div class="row">
                                            <div class="col-md-3">
                                                <b>FullName</b> :
                                            </div>
                                            <div class="col-md-9">
                                                {{employeeDetails.firstName}} {{employeeDetails.lastName}} {{employeeDetails.middleName}}
                                            </div>
                                        </div>
                                        <div class="row">
                                            <div class="col-md-3">
                                                <b>Email</b> :
                                            </div>
                                            <div class="col-md-9">
                                                {{employeeDetails.email}}
                                            </div>
                                        </div>
                                        <div class="row">
                                            <div class="col-md-3">
                                                <b>Phone (Mobile)</b> :
                                            </div>
                                            <div class="col-md-9">
                                                {{employeeDetails.phoneMobile}}
                                            </div>
                                        </div>
                                        <div class="row">
                                            <div class="col-md-3">
                                                <b>Phone (Office)</b> :
                                            </div>
                                            <div class="col-md-9">
                                                {{employeeDetails.phoneOffice}}
                                            </div>
                                        </div>
                                    </div>

                                    <div ng-show="employeeDetails.isEdit">
                                        <div class="row">
                                            <div class="col-md-8 col-md-offset-2 form-group">
                                                <label class="control-label">First Name</label>
                                                <input ng-model="employeeDetails.firstName" placeholder="" class="form-control" type="text"
                                                       name="firstname" required-message="constants.REQUIRED" validate-on="dirty" required>
                                            </div>
                                        </div>
                                        <div class="row">
                                            <div class="col-md-8 col-md-offset-2 form-group">
                                                <label class="control-label">Middle Name</label>
                                                <input ng-model="employeeDetails.middleName" placeholder="" class="form-control" type="text">
                                            </div>
                                        </div>
                                        <div class="row">
                                            <div class="col-md-8 col-md-offset-2 form-group">
                                                <label class="control-label">Last Name</label>
                                                <input ng-model="employeeDetails.lastName" placeholder="" class="form-control" type="text"
                                                       name="lastname" required-message="constants.REQUIRED" validate-on="dirty" required>
                                            </div>
                                        </div>
                                        <br/>

                                        <div class="row">
                                            <div class="col-md-8 col-md-offset-2 form-group">
                                                <label class="control-label">Email</label>
                                                <input ng-show="employeeDetails.isEdit" name="email" ng-model="employeeDetails.email" placeholder="" class="form-control" type="email"
                                                       invalid-message="constants.INVALIDEMAIL" required-message="constants.REQUIRED" validate-on="dirty" required>
                                            </div>
                                            <div class="col-md-8 col-md-offset-2 form-group">
                                                <label class="control-label">Phone (Mobile)</label>
                                                <input ng-show="employeeDetails.isEdit" ng-model="employeeDetails.phoneMobile" name="mobilenumber" placeholder="" class="form-control" type="text"
                                                       validate-on="dirty" required-message="constants.REQUIRED" required>
                                            </div>
                                            <div class="col-md-8 col-md-offset-2 form-group">
                                                <label class="control-label">Phone (Office)</label>
                                                <input ng-show="employeeDetails.isEdit" ng-model="employeeDetails.phoneOffice" placeholder="" class="form-control" type="text">
                                            </div>
                                        </div>
                                    </div>

                                </div>

                                <div class="col-sm-6 employee-details">

                                    <h4 class="text-primary">Employee Details</h4>

                                    <%--start--%>
                                    <div class="employee-details">
                                        <div class="row">
                                            <div class="col-md-3">
                                                <b>Job Title</b> :
                                            </div>
                                            <div class="col-md-9">
                                                {{employeeDetails.jobTitle}}
                                            </div>
                                        </div>

                                        <div class="row">
                                            <div class="col-md-3">
                                                <b>Department</b> :
                                            </div>
                                            <div class="col-md-9">
                                                {{employeeDetails.department.name}}
                                            </div>
                                        </div>

                                        <div class="row">
                                            <div class="col-md-3">
                                                <b>Manager</b> :
                                            </div>
                                            <div class="col-md-9">
                                                {{employeeDetails.managerName}}
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                            <div class="row">
                                <div class="col-sm-6">
                                    <h4 class="text-primary">Address Details - Home</h4>

                                    <div ng-hide="employeeDetails.isEdit" class="employee-details">
                                        <div class="row">
                                            <div class="col-md-3">
                                                <b>Address</b> :
                                            </div>
                                            <div class="col-md-9">
                                                {{employeeDetails.addresses[0].addressText}}
                                            </div>
                                        </div>
                                        <div class="row">
                                            <div class="col-md-3">
                                                <b>City</b> :
                                            </div>
                                            <div class="col-md-9">
                                                {{employeeDetails.addresses[0].city}}
                                            </div>
                                        </div>
                                        <div class="row">
                                            <div class="col-md-3">
                                                <b>State</b> :
                                            </div>
                                            <div class="col-md-9">
                                                {{employeeDetails.addresses[0].state.name}}
                                            </div>
                                        </div>
                                        <div class="row">
                                            <div class="col-md-3">
                                                <b>Pincode</b> :
                                            </div>
                                            <div class="col-md-9">
                                                {{employeeDetails.addresses[0].pincode}}
                                            </div>
                                        </div>
                                        <div class="row">
                                            <div class="col-md-3">
                                                <b>Country</b> :
                                            </div>
                                            <div class="col-md-9">
                                                {{employeeDetails.addresses[0].country.name}}
                                            </div>
                                        </div>
                                    </div>

                                    <div ng-show="employeeDetails.isEdit">

                                        <div class="row">
                                            <div class="col-md-8 col-md-offset-2 form-group">
                                                <label class="control-label">Address</label>
                                                <textarea rows="5" ng-model="employeeDetails.addresses[0].addressText" placeholder="" name="address" class="form-control" type="text"
                                                          required-message="constants.REQUIRED"
                                                          validate-on="dirty"
                                                          required></textarea>
                                            </div>
                                            <div class="col-md-8 col-md-offset-2 form-group">
                                                <label class="control-label">City</label>
                                                <input ng-model="employeeDetails.addresses[0].city" placeholder="" class="form-control" type="text"
                                                       name="city" required-message="constants.REQUIRED" validate-on="dirty" required>
                                            </div>
                                            <div class="col-md-8 col-md-offset-2 form-group">
                                                <label class="control-label">State</label>
                                                <ui-select ng-model="employeeDetails.addresses[0].state" theme="bootstrap" style="width: 100%;" title="Choose a State">
                                                    <ui-select-match placeholder="Choose a State">{{$select.selected.name}}</ui-select-match>
                                                    <ui-select-choices repeat="state in allStates | filter: $select.search">
                                                        <div ng-bind-html="state.name | highlight: $select.search"></div>
                                                    </ui-select-choices>
                                                </ui-select>
                                                <input type="text" name="statedummy" ng-model="employeeDetails.addresses[0].state.id" class="hide form-control"
                                                       required-message="constants.REQUIRED"
                                                       validator="stateValidator(employeeDetails.addresses[0].state)"
                                                       validate-on="dirty"
                                                       required>
                                            </div>
                                            <div class="col-md-8 col-md-offset-2 form-group">
                                                <label class="control-label">Country</label>
                                                <ui-select ng-model="employeeDetails.addresses[0].country" theme="bootstrap" style="width: 100%;" title="Choose a Country">
                                                    <ui-select-match placeholder="Choose a Country">{{$select.selected.name}}</ui-select-match>
                                                    <ui-select-choices repeat="country in allCountries | filter: $select.search">
                                                        <div ng-bind-html="country.name | highlight: $select.search"></div>
                                                    </ui-select-choices>
                                                </ui-select>
                                                <%--<div>
                                                    {{employeeDetails.addresses[0].country.name}}
                                                </div>--%>
                                            </div>
                                            <div class="col-md-8 col-md-offset-2 form-group">
                                                <label class="control-label">Pincode</label>
                                                <input ng-show="employeeDetails.isEdit" placeholder="" ng-model="employeeDetails.addresses[0].pincode" class="form-control" type="text">
                                            </div>
                                        </div>

                                        <br/>

                                        <div class="row">
                                            <div class="col-md-12 text-center mb20" ng-show="employeeDetails.isEdit">
                                                <button class="btn btn-sm btn-info" type="submit"><i class="fa fa-key"></i> Update Info</button>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </form>
                    </div>
                </div>


                <div class="tab-pane" id="emtimeoff">
                    <month-calender calender="tabtype.timeoff" on-month-click="currentMonth(data)"></month-calender>
                    <div class="row" style="margin: 4px;">
                        <div class="colm-md-12">
                            <span ng-show="!loading && timeOffList.length == 0">There are no records</span>
                            <span ng-show="loading">
                                <img src="app/assets/images/loaders/loader19.gif" class="mr5">Loading ...
                            </span>
                        </div>
                    </div>
                    <div class="row" ng-hide="loading || attendanceList.length == 0">
                        <div class="col-md-12">
                            <div class="table-responsive">
                                <table class="table table-striped mb20">

                                    <thead>
                                        <tr>
                                            <th class="col-sm-2">Leave Type</th>
                                            <th class="col-sm-2">From Date</th>
                                            <th class="col-sm-2">To Date</th>
                                            <th class="col-sm-2">Number of Days</th>
                                            <th class="col-sm-6">Reason for leave</th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        <tr ng-repeat="timeOff in timeOffList">
                                            <td>{{getTimeOffType(timeOff.timeOffType)}}</td>
                                            <td>{{formatDate(timeOff.startDate)}}</td>
                                            <td>{{formatDate(timeOff.endDate)}}</td>
                                            <td>{{timeOff.numOfDays}}</td>
                                            <td>{{timeOff.reason}}</td>
                                        </tr>
                                    </tbody>
                                </table>
                            </div>
                        </div>
                    </div>

                </div>
                <%--<div class="tab-pane" id="emloan">

                    <div class="row">
                        <div class="col-md-12">
                            <div class="table-responsive">
                                <table class="table table-striped mb20">
                                    <thead>
                                    <tr>
                                        <th class="col-sm-2">Date</th>
                                        <th class="col-sm-2">Amount</th>
                                        <th class="col-sm-8">Reason for loan</th>
                                    </tr>
                                    </thead>

                                    <tbody>
                                    <tr ng-repeat="loanDetails in loanDetailsList">
                                        <td>{{loanDetails.createdDate | date :'dd/MM/yyyy'}}</td>
                                        <td>Rs. {{loanDetails.amount | number}}</td>
                                        <td>Lorem ipsum dolor sit amet, consectetur adipisicing elit. Commodi deleniti distinctio</td>
                                    </tr>
                                    </tbody>
                                </table>
                            </div>
                        </div>
                    </div>
                </div>--%>

                <div class="tab-pane" id="emcalender">
                    <month-calender calender="tabtype.attendace" on-month-click="currentMonth(data)"></month-calender>
                    <div class="row" style="margin: 4px;">
                        <div class="colm-md-12">
                            <span ng-show="!loading && attendanceList.length == 0">There are no records</span>
                            <span ng-show="loading">
                                <img src="app/assets/images/loaders/loader19.gif" class="mr5">Loading ...
                            </span>
                        </div>
                    </div>
                    <div class="row" ng-hide="loading || attendanceList.length == 0">

                        <div class="col-md-12">

                            <div class="table-responsive">


                                <table class="table table-striped mb20">
                                    <thead>
                                    <tr>


                                        <th class="col-sm-6">Date</th>
                                        <th class="col-sm-2">In Time</th>
                                        <th class="col-sm-2">Out Time</th>
                                    </tr>
                                    <thead>
                                    <tbody>

                                    <tr ng-repeat="attendance in attendanceList">


                                        <td>{{attendance.inTime | convertDate | date:'yyyy-MM-dd'}}</td>
                                        <td>{{attendance.inTime | convertDate | date:"h:mma"}}</td>
                                        <td>{{attendance.outTime| convertDate | date:"h:mma"}}</td>

                                    </tr>


                                    </tbody>
                                </table>


                            </div>

                        </div>
                    </div>
                </div>

                <div class="tab-pane" id="empayroll">
                    <div class="row">
                        <div class="col-md-12">
                            <div class="table-responsive">
                                <table class="table table-striped mb20">
                                    <thead>
                                    <tr>
                                        <th colspan="3" class="text-center">Info</th>
                                        <th colspan="{{payRollObj.allowanceTypes.length}}" class="text-center">Allowances</th>
                                        <th colspan="{{payRollObj.deductionTypes.length}}" class="text-center">Deductions</th>
                                        <th class="text-right">Net Amount</th>
                                    </tr>
                                    <thead>
                                    <tbody>
                                    <tr>
                                        <td><strong>Month</strong></td>
                                        <td class="text-right"><strong>Salary</strong></td>
                                        <td class="text-right"><strong>Bonus</strong></td>
                                        <td class="text-center" ng-if="payRollObj.allowanceTypes.length == 0"><strong>No allowances</strong></td>
                                        <td class="text-center" ng-if="payRollObj.deductionTypes.length == 0"><strong>No deductions</strong></td>
                                        <td class="text-right" ng-repeat="allowance in payRollObj.allowanceTypes"><strong>{{allowance.name}}</strong></td>
                                        <td class="text-right" ng-repeat="deduction in payRollObj.deductionTypes"><strong>{{deduction.name}}</strong></td>
                                        <td class="text-right"><strong>Amount</strong></td>
                                    </tr>
                                    <tr ng-repeat="payroll in payRollObj.salaryList">
                                        <td><strong>{{monthNames[payroll.month-1]}}</strong></td>
                                        <td class="text-right">{{payroll.payrollPay.empSalHist.salary | rupee}}</td>
                                        <td class="text-right">{{payroll.payrollPay.empSalHist.bonus | rupee}}</td>
                                        <td ng-if="payRollObj.allowanceTypes.length == 0"></td>
                                        <td ng-if="payRollObj.deductionTypes.length == 0"></td>
                                        <td class="text-right" ng-repeat="allowance in payroll.payrollPay.empAllowancesHist">
                                            <span>{{allowance.allowancePaid | rupee}}</span>
                                        </td>
                                        <td class="text-right" ng-repeat="deduction in payroll.payrollPay.empDeductionsHist">
                                            <span>{{deduction.deductionAmt | rupee}}</span>
                                        </td>
                                        <td class="text-right">
                                            <span>{{payroll.payrollPay.netAmtPay}}</span>
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
</div>







