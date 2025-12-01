<div class="tab-pane" id="employeepay">
    <div class="row">
        <div class="col-md-12">
            <div class="responsive-table">
                <table class="table table-striped mb20">
                    <thead>
                    <tr>
                        <th class="text-center" style="width:100px">Actions</th>
                        <th colspan="2" class="text-center">Info</th>
                        <th colspan="3" class="text-center">Salary & Bonus</th>
                        <th colspan="{{payRollObj.allowanceTypes.length}}" class="text-center">Allowances</th>
                        <th colspan="{{payRollObj.deductionTypes.length}}" class="text-center">Deductions</th>
                        <th class="text-right">Net Amount</th>
                    </tr>
                    <thead>
                    <tbody>
                    <tr>
                        <td class="text-center" style="width:100px">&nbsp;</td>
                        <td style="width:100px"><strong>Emp Num</strong></td>
                        <td><strong>Name</strong></td>
                        <td class="text-center" style="width:120px"><strong>Attendance</strong></td>
                        <td class="text-right" style="width:120px"><strong>Salary</strong></td>
                        <td class="text-right" style="width:120px"><strong>Bonus</strong></td>
                        <td class="text-center" style="width:120px" ng-if="newPayrollObj.allowanceTypes.length == 0">
                            <strong>No allowances</strong></td>
                        <td class="text-center" style="width:120px" ng-if="newPayrollObj.deductionTypes.length == 0">
                            <strong>No deductions</strong></td>
                        <td class="text-right" style="width:120px"
                            ng-repeat="allowance in newPayrollObj.allowanceTypes"><strong>{{allowance.name}}</strong>
                        </td>
                        <td class="text-right" style="width:120px"
                            ng-repeat="deduction in newPayrollObj.deductionTypes"><strong>{{deduction.name}}</strong>
                        </td>
                        <td class="text-right" style="width:120px"><strong>Amount</strong></td>
                    </tr>
                    <tr ng-repeat="payroll in newPayrollObj.salaryList"
                        ng-if="payroll.emp.firstName != 'Administrator'">
                        <td class="text-center" style="width:100px">
                            <div class="btn-group" dropdown style="margin-bottom: 0px;" ng-hide="payroll.editMode">
                                <button type="button" class="btn btn-default dropdown-toggle actionbtn"
                                        dropdown-toggle>
                                    <i class="fa fa-cog fa-fw"></i></span>
                                </button>
                                <ul class="dropdown-menu" role="menu">
                                    <li><a href="" ng-click="showEditMode(payroll)">Edit</a></li>
                                    <%--<li><a href="" ng-click="removeItem($index);">Delete</a></li>--%>
                                </ul>
                            </div>

                            <div class="btn-group" style="margin-bottom: 0px;" ng-show="payroll.editMode">
                                <button type="button" class="btn btn-sm btn-success"
                                        ng-click="acceptNewPayChanges(payroll)"><i class="fa fa-check"></i></button>
                                <button type="button" class="btn btn-sm btn-default"
                                        ng-click="hideEditMode(payroll);"><i class="fa fa-times"></i></button>
                            </div>
                        </td>
                        <td style="width:100px">{{payroll.emp.employeeNumber}}</td>
                        <td>{{payroll.emp.firstName}}</td>
                        <td class="text-center" style="width:120px">
                            <span>{{payroll.attendence}}</span>
                        </td>
                        <td class="text-right" style="width:120px">
                            <span ng-hide="payroll.editMode">{{payroll.empSalHist.salary | rupee}}</span>
                            <input placeholder="Enter Salary" class="form-control" type="text"
                                   ng-show="payroll.editMode" ng-model="payroll.empSalHist.salary">
                        </td>
                        <td class="text-right" style="width:120px">
                            <span ng-hide="payroll.editMode">{{payroll.empSalHist.bonus | rupee}}</span>
                            <input placeholder="Enter Bonus" class="form-control" type="text"
                                   ng-show="payroll.editMode" ng-model="payroll.empSalHist.bonus">
                        </td>
                        <td style="width:120px" ng-if="newPayrollObj.allowanceTypes.length == 0"></td>
                        <td style="width:120px" ng-if="newPayrollObj.deductionTypes.length == 0"></td>
                        <td style="width:120px" class="text-right" ng-repeat="allowance in payroll.empAllowancesHist">
                            <span ng-hide="payroll.editMode">{{allowance.allowancePaid | rupee}}</span>
                            <input placeholder="Enter Allowance" class="form-control" type="text"
                                   ng-show="payroll.editMode" ng-model="allowance.allowancePaid">
                        </td>
                        <td style="width:120px" class="text-right" ng-repeat="deduction in payroll.empDeductionsHist">
                            <span ng-hide="payroll.editMode">{{deduction.deductionAmt | rupee}}</span>
                            <input placeholder="Enter Deduction" class="form-control" type="text"
                                   ng-show="payroll.editMode" ng-model="deduction.deductionAmt">
                        </td>
                        <td style="width:120px" class="text-right">{{payroll.netProfit | rupee}}</td>
                    </tr>
                    <tr>
                        <td colspan="4" class="text-right" style="width:100px"><h4>Total</h4></td>
                        <td class="text-right"><h4>{{newPayrollObj.totalSalary | rupee}}</h4></td>
                        <td class="text-right"><h4>{{newPayrollObj.totalbonus | rupee}}</h4></td>
                        <td class="text-center" ng-if="newPayrollObj.allowanceTypes.length == 0"><h4>0</h4></td>
                        <td class="text-center" ng-if="newPayrollObj.deductionTypes.length == 0"><h4>0</h4></td>
                        <td class="text-right" ng-repeat="allowance in newPayrollObj.allowanceTypes"><h4>
                            {{allowance.total | rupee}}</h4></td>
                        <td class="text-right" ng-repeat="deduction in newPayrollObj.deductionTypes"><h4>
                            {{deduction.total | rupee}}</h4></td>
                        <td class="text-right"><h3 class="text-primary">{{newPayrollObj.totalNetProfit | rupee}}</h3>
                        </td>
                    </tr>
                    </tbody>
                </table>
            </div>
        </div>
    </div>
</div>