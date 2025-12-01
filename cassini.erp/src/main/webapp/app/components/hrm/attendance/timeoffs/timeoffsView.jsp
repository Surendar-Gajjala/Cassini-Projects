
<div class="row" style="margin-bottom: 20px">
    <div class="col-md-12"  style="text-align: right" ng-show="employees.totalElements > 0">
        <div style="text-align: right;">
            <pagination total-items="employees.totalElements"
                        items-per-page="pageable.size"
                        max-size="5"
                        boundary-links="true"
                        ng-model="pageable.page"
                        ng-change="pageChanged()">
            </pagination>
        </div>

        <div style="margin-top: -25px;">
            <small>Total {{employees.totalElements}} entries</small>
        </div>
    </div>
</div>
<div class="row">
    <div class="col-md-12">
        <div class="table-responsive">
            <table class="table table-striped mb20">
                <thead>
                <tr>
                    <th>Emp Number</th>
                    <th>Emp Name</th>                    
                    <th>TimeOff Type</th>
                    <th>Start Date</th>
                    <th>End Date</th>
                    <th>No. Of days</th>
                    <th>Status</th>
                    <th>Reason</th>

                </tr>
                </thead>

                <tbody>
                <tr ng-repeat="emp in employees.content">
                    <td><a ui-sref="app.hrm.employee({ employeeId: emp.id })">{{emp.employeeId}}</a></td>
                    <td>{{emp.empName}}</td>                    
                    <td>{{emp.leaveType}}</td>
                    <td>{{emp.startDate}}</td>
                    <td>{{emp.endDate}}</td>                    
                    <td>{{emp.numOfDays}}</td>
                    <td>{{emp.status}}</td>
                    <td>{{emp.reason}}</td>
                </tr>
                </tbody>
            </table>
        </div>
    </div>
</div>