<div class="row">
    <div class="col-md-12 mb20">
        <button class="btn btn-primary mr5 btn-sm" data-ng-click="navigate();"><i class="fa fa-chevron-left mr10"></i> Back</button>
    </div>
</div>
<div class="tab-pane" id="employeepay">
    <div class="row">
        <div class="col-md-12">
            <div class="responsive-table">
                <table class="table table-striped mb20">
                    <thead>
                    <tr>
                        <th colspan="4" class="text-center">Info</th>
                        <th colspan="{{payRollObj.allowanceTypes.length}}" class="text-center">Allowances</th>
                        <th colspan="{{payRollObj.deductionTypes.length}}" class="text-center">Deductions</th>
                        <th class="text-right">Net Amount</th>
                    </tr>
                    <thead>
                    <tbody>
                    <tr>
                        <td style="width:100px"><strong>Emp Num</strong></td>
                        <td><strong>Name</strong></td>
                        <td><strong>Attendance</strong></td>
                        <td class="text-right"><strong>Salary</strong></td>
                        <td class="text-right"><strong>Bonus</strong></td>
                        <td class="text-center" ng-if="newPayrollObj.allowanceTypes.length == 0"><strong>No allowances</strong></td>
                        <td class="text-center" ng-if="newPayrollObj.deductionTypes.length == 0"><strong>No deductions</strong></td>
                        <td class="text-right" ng-repeat="allowance in newPayrollObj.allowanceTypes"><strong>{{allowance.name}}</strong></td>
                        <td class="text-right" ng-repeat="deduction in newPayrollObj.deductionTypes"><strong>{{deduction.name}}</strong></td>
                        <td class="text-right"><strong>Amount</strong></td>
                    </tr>
                    <tr ng-repeat="payroll in newPayrollObj.salaryList">
                        <td style="width:100px">{{payroll.emp.employeeNumber}}</td>
                        <td>{{payroll.emp.firstName}}</td>
                        <td class="text-right">
                            <span>{{payroll.attendence}}</span>
                        </td>
                        <td class="text-right">
                            <span>{{payroll.empSalHist.salary | rupee}}</span>
                        </td>
                        <td class="text-right">
                            <span>{{payroll.empSalHist.bonus | rupee}}</span>
                        </td>
                        <td ng-if="newPayrollObj.allowanceTypes.length == 0"></td>
                        <td ng-if="newPayrollObj.deductionTypes.length == 0"></td>
                        <td class="text-right" ng-repeat="allowance in payroll.empAllowancesHist">
                            <span>{{allowance.allowancePaid | rupee}}</span>
                        </td>
                        <td class="text-right" ng-repeat="deduction in payroll.empDeductionsHist">
                            <span>{{deduction.deductionAmt | rupee}}</span>
                        </td>
                        <td class="text-right">{{payroll.netProfit | rupee}}</td>
                    </tr>

                    <tr>
                        <td colspan="3" class="text-right" style="width:100px"><strong>Total</strong></td>
                        <td class="text-right"><strong>{{newPayrollObj.totalSalary | rupee}}</strong></td>
                        <td class="text-right"><strong>{{newPayrollObj.totalbonus | rupee}}</strong></td>
                        <td class="text-center" ng-if="newPayrollObj.allowanceTypes.length == 0">0</td>
                        <td class="text-center" ng-if="newPayrollObj.deductionTypes.length == 0">0</td>
                        <td class="text-right" ng-repeat="allowance in newPayrollObj.allowanceTypes"><strong>{{allowance.total | rupee}}</strong></td>
                        <td class="text-right" ng-repeat="deduction in newPayrollObj.deductionTypes"><strong>{{deduction.total | rupee}}</strong></td>
                        <td class="text-right">{{newPayrollObj.totalNetProfit | rupee}}</td>
                    </tr>
                    </tbody>
                </table>
            </div>
        </div>
    </div>
</div>
