<ul class="nav nav-tabs nav-profile">

    <li class="active"><a href="#payroll" data-toggle="tab"><strong>Payroll</strong></a></li>
    <li><a href="#employeepay" data-toggle="tab"><strong>Employee Pay</strong></a></li>
</ul>

<div class="tab-content payroll-calendar">
    <div class="tab-pane active" id="payroll" style="text-align:center">
        <div class="row">
            <div class="col-md-12 col-lg-8 col-lg-offset-2 styled-panel">
                <div class="row">
                    <div class="pull-left"  style="vertical-align: middle;padding-top: 20px;text-align: right;">
                        <div class="btn-group" style="padding-left: 14px;">
                            <button class="btn btn-default" style="width:120px" ng-click="previousYear()">
                                <i class="fa fa-arrow-left mr10"></i>PREVIOUS
                            </button>
                        </div>
                    </div>
                    <div class="pull-right"  style="vertical-align: middle;padding-top: 20px;text-align: right;">
                        <div class="btn-group" style="padding-right: 14px;">
                            <button class="btn btn-default" style="width:120px" ng-disabled="isYearCurrent()" ng-click="nextYear()">
                                NEXT<i class="fa fa-arrow-right" style="margin-left:10px"></i>
                            </button>
                        </div>
                    </div>
                    <div style="padding-bottom: 20px;text-align: center;">
                        <h3 style="font-size: 40px; text-align: center;margin-left: 150px;">{{currentYear}}</h3>
                    </div>
                </div>

                <div ng-repeat="row in months">
                    <div class="row">
                        <div ng-repeat="month in row">
                            <div class="col-md-4">
                                <div class="panel panel-default" style="height:230px">
                                    <div class="panel-heading" style="background-color: #E4E7EA;">
                                        <div class="panel-title" style="text-align: center">{{month.name}}</div>
                                    </div>
                                    <div class="panel-body" style="text-align: center;">
                                        <h1 ng-show="((!isMonthCurrent(month.index) && isMonthBeforeCurrentMonth(month.index)) || isPayrollRun(month.index)) || !isYearCurrent()"><span>&#8377</span>{{getMonthTotalSalary(month.index) | rupee}}</h1>
                                        <button data-ng-click="viewPayRoll(month.index);" class="btn btn-sm btn-default"ng-show="((!isMonthCurrent(month.index) && isMonthBeforeCurrentMonth(month.index)) || isPayrollRun(month.index)) || !isYearCurrent()">View Payroll</button>
                                        <button data-ng-click="runPayRoll(month.index);" class="btn btn-sm btn-primary" ng-show="isMonthCurrent(month.index) && !isPayrollRun(month.index) && isYearCurrent()" style="margin-top:70px">Run Payroll</button>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>

    </div>

    <div class="tab-pane" id="employeepay">
        <div class="row">
            <div class="col-md-12">
                <div class="responsive-table">
                    <table class="table table-striped mb20">
                        <thead>
                        <tr>
                            <th class="text-center" style="width:100px">Actions</th>
                            <th colspan="4" class="text-center">Info</th>
                            <th colspan="{{payRollObj.allowanceTypes.length}}" class="text-center">Allowances</th>
                            <th class="text-center">Deductions</th>
                            <th class="text-right">Net Amount</th>
                        </tr>
                        <thead>
                        <tbody>
                        <tr>
                            <td class="text-center" style="width:100px">&nbsp;</td>
                            <td style="width:100px"><strong>Emp Num</strong></td>
                            <td><strong>Name</strong></td>
                            <td class="text-right"><strong>Salary</strong></td>
                            <td class="text-right"><strong>Bonus</strong></td>
                            <td class="text-center" ng-if="payRollObj.allowanceTypes.length == 0"><strong>No allowances</strong></td>
                            <td class="text-center" ng-if="payRollObj.deductionTypes.length == 0"><strong>No deductions</strong></td>
                            <td class="text-right" ng-repeat="allowance in payRollObj.allowanceTypes"><strong>{{allowance.name}}</strong></td>
                            <td class="text-right" ng-repeat="deduction in payRollObj.deductionTypes"><strong>{{deduction.name}}</strong></td>
                            <td class="text-right"><strong>Amount</strong></td>
                        </tr>
                        <tr ng-repeat="payroll in payRollObj.salaryList" ng-if="payroll.emp.firstName != 'Administrator'">
                            <td class="text-center" style="width:100px">
                                <div class="btn-group" dropdown style="margin-bottom: 0px;" ng-hide="payroll.editMode">
                                    <button type="button" class="btn btn-default dropdown-toggle actionbtn"
                                            dropdown-toggle>
                                        <i class="fa fa-cog fa-fw"></i></span>
                                    </button>
                                    <ul class="dropdown-menu" role="menu">
                                        <li><a href="" ng-click="showEditMode(payroll)">Edit</a></li>
                                    </ul>
                                </div>

                                <div class="btn-group" style="margin-bottom: 0px;" ng-show="payroll.editMode">
                                    <button type="button" class="btn btn-sm btn-success"
                                            ng-click="acceptChanges(payroll)"><i class="fa fa-check"></i></button>
                                    <button type="button" class="btn btn-sm btn-default"
                                            ng-click="hideEditMode(payroll);"><i class="fa fa-times"></i></button>
                                </div>
                            </td>
                            <td style="width:100px">{{payroll.emp.employeeNumber}}</td>
                            <td>{{payroll.emp.firstName}}</td>
                            <td class="text-right">
                                <span ng-hide="payroll.editMode">{{payroll.empSal.salary | rupee}}</span>
                                <input placeholder="Enter Salary" class="form-control" type="text"
                                       ng-show="payroll.editMode" ng-model="payroll.empSal.newSalary">
                            </td>
                            <td class="text-right">
                                <span ng-hide="payroll.editMode">{{payroll.empSal.bonus | rupee}}</span>
                                <input placeholder="Enter Bonus" class="form-control" type="text"
                                       ng-show="payroll.editMode" ng-model="payroll.empSal.newBonus">
                            </td>
                            <td ng-if="payRollObj.allowanceTypes.length == 0"></td>
                            <td ng-if="payRollObj.deductionTypes.length == 0"></td>
                            <td class="text-right" ng-repeat="allowance in payroll.empAllowances">
                                <span ng-hide="payroll.editMode">{{allowance.allowanceValue | rupee}}</span>
                                <input placeholder="Enter Allowance" class="form-control" type="text"
                                       ng-show="payroll.editMode" ng-model="allowance.allowanceValue">
                            </td>
                            <td class="text-right" ng-repeat="deduction in payroll.empDeductions">
                                <span ng-hide="payroll.editMode">{{deduction.deductionValue | rupee}}</span>
                                <input placeholder="Enter Deduction" class="form-control" type="text"
                                       ng-show="payroll.editMode" ng-model="deduction.deductionValue">
                            </td>
                            <td class="text-right">{{payroll.netProfit | rupee}}</td>
                        </tr>

                        <tr>
                            <td style="width:100px">&nbsp;</td>
                            <td colspan="2" class="text-right" style="width:100px"><h4>Total</h4></td>
                            <td class="text-right"><h4>{{payRollObj.totalSalary | rupee}}</h4></td>
                            <td class="text-right"><h4>{{payRollObj.totalbonus | rupee}}</h4></td>
                            <td class="text-center" ng-if="payRollObj.allowanceTypes.length == 0">0</td>
                            <td class="text-center" ng-if="payRollObj.deductionTypes.length == 0">0</td>
                            <td class="text-right" ng-repeat="allowance in payRollObj.allowanceTypes"><h4>{{allowance.total | rupee}}</h4></td>
                            <td class="text-right" ng-repeat="deduction in payRollObj.deductionTypes"><h4>{{deduction.total | rupee}}</h4></td>
                            <td class="text-right"><h3 class="text-primary">{{payRollObj.totalNetProfit | rupee}}</h3></td>
                        </tr>
                        </tbody>
                    </table>
                </div>
            </div>
        </div>
    </div>
</div>



