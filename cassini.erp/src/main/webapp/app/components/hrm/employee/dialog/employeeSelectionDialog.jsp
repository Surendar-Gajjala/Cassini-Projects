<div class="modal-header"
     style="background: #E4E7EA none repeat scroll 0% 0%; border-bottom: 1px solid #CCC;">
  <h3 style="margin-top:0px; margin-bottom:0px;">{{dialogTitle}}</h3>
</div>
<div class="modal-body" style="max-height:500px; overflow: auto;">
  <div class="row">
    <div class="col-md-12"  style="text-align: center" ng-show="employees.numberOfElements > 0">
      <div>
        <pagination total-items="employees.totalElements"
                    items-per-page="20"
                    max-size="5"
                    boundary-links="true"
                    ng-model="pageable.page"
                    ng-change="loadEmployees()">
        </pagination>
      </div>

      <div style="margin-top: -25px;">
        <small>Total {{employees.totalElements}} employees</small>
      </div>
    </div>
  </div>

  <br/>
  <div class="row">
    <div class="col-md-12" style="padding:0px">
      <table class="table table-striped">
        <thead>
        <tr>
          <th style="width: 80px; text-align: center">
            <input id="empColumn" name="empSelected" type="radio" value="" style="display:none">
            <label for="empColumn"></label>
          </th>
          <th style="">FirstName</th>
          <th style="">Last Name</th>
          <th style="">Job Title</th>
          <th style="">Department</th>
          <th style="">Manager</th>
        </tr>
        </thead>
        <tbody>
        <tr>
          <td style="width:100px; text-align: center; vertical-align: middle;">
            <div class="btn-group" style="margin-bottom: 0px;">
              <button type="button" class="btn btn-sm btn-success" ng-click="applyCriteria()" title="Search"><i class="fa fa-search"></i></button>
              <button type="button" class="btn btn-sm btn-default" ng-click="resetCriteria()" title="Clear Search"><i class="fa fa-times"></i></button>
            </div>
          </td>

          <td style="vertical-align: middle;">
            <input placeholder="First name" class="form-control" type="text"
                   ng-model="filters.firstName" ng-enter="applyCriteria()" >
          </td>

          <td style="vertical-align: middle;">
            <input placeholder="Last name" class="form-control" type="text"
                   ng-model="filters.lastName" ng-enter="applyCriteria()" >
          </td>
          <td style="vertical-align: middle;">
            <input placeholder="Job title" class="form-control" type="text"
                   ng-model="filters.jobTitle" ng-enter="applyCriteria()" >
          </td>
          <td style="vertical-align: middle;">
            <input placeholder="Department" class="form-control" type="text"
                   ng-model="filters.deparment" ng-enter="applyCriteria()" >
          </td>
          <td style="vertical-align: middle;">
            <input placeholder="Manager" class="form-control" type="text"
                   ng-model="filters.manager" ng-enter="applyCriteria()" >
          </td>
        </tr>

        <tr ng-if="em.content.length == 0">
          <td colspan="6">
            <span ng-hide="loading">No Employees</span>
                            <span ng-show="loading">
                                <img src="app/assets/images/loaders/loader19.gif" class="mr5">Loading employees...
                            </span>
          </td>
        </tr>


        <tr ng-repeat="employee in employees.content">
          <td style="width:50px; text-align:center; vertical-align: middle;" align="center">
            <div class="rdio rdio-default" style="margin-left: 25px;">
              <input id="emp{{$index}}" name="empSelected" ng-value="true" type="radio"
                     ng-model="employee.selected" ng-click="selectEmployee(employee)">
              <label for="emp{{$index}}"></label>
            </div>
          </td>

          <td style="vertical-align: middle;">
            {{ employee.firstName }}
          </td>

          <td style="vertical-align: middle;">
            {{ employee.lastName }}
          </td>

          <td style="vertical-align: middle;">
            {{ employee.jobTitle }}
          </td>

          <td style="vertical-align: middle;">
            {{ employee.department.name }}
          </td>

          <td style="vertical-align: middle;">
            {{ employee.manager.firstName }}
          </td>
        </tr>
        </tbody>
      </table>
    </div>
  </div>
  <br/>
</div>
<div class="modal-footer" style="background-color: #F9F9F9;text-align: left;">
  <div class="row">
    <div class="col-md-6" style="text-align: left">
      <h4>Selected Employee: <span class="text-success">{{selectedEmployee.firstName}}</span></h4>
    </div>
    <div class="col-md-12" style="text-align: center">
      <button type="button" class="btn btn-default" style="min-width: 80px"
              ng-click="cancel()">Cancel</button>
      <button class="btn btn-primary" style="min-width: 80px"
              ng-click="ok()" ng-disabled="selectedEmployee == null">Select</button>
    </div>
  </div>
</div>
