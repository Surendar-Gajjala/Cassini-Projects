<script type="text/ng-template" id="employees-view-tb">
    <div>
        <button class="btn btn-primary mr5 btn-sm" data-ng-click="addNewEmployee();">
            <i class="glyphicon glyphicon-plus"></i> Add New Employee
        </button>
    </div>
</script>
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
            <small>Total {{employees.totalElements}} employees</small>
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
                    <th>First Name</th>
                    <th>Last Name</th>
                    <th>Middle Name</th>
                    <th>Job Title</th>
                    <th>Phone-Office</th>
                    <th>Phone-Mobile</th>
                    <th>Email</th>

                </tr>
                </thead>

                <tbody>
                <tr ng-repeat="emp in employees.content">
                    <td><a ui-sref="app.hrm.employee({ employeeId: emp.id })">{{emp.employeeNumber != null ? emp.employeeNumber: emp.id}}</a></td>
                    <td>{{emp.firstName}}</td>
                    <td>{{emp.lastName}}</td>
                    <td>{{emp.middleName}}</td>
                    <td>{{emp.jobTitle}}</td>
                    <td>{{emp.phoneOffice}}</td>
                    <td>{{emp.phoneMobile}}</td>
                    <td>{{emp.email}}</td>
                </tr>
                </tbody>
            </table>
        </div>
    </div>
</div>