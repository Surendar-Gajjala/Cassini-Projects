
<div class="view-container">
  <div class="row">
    <div class="col-md-6">
      <div class="view-toolbar">
        <button type="button" class="btn btn-sm btn-success" ng-click="loginsVm.showNewLoginDialog()">New Login</button>
      </div>
    </div>
    <div class="col-md-6">
      <div class="view-toolbar">
        <button type="button" class="btn btn-sm btn-success" ng-click="loginsVm.showNewGroupDialog()">New Group</button>
      </div>
    </div>
  </div>

  <div class="view-content">
    <div class="row">

      <div class="col-md-6">
        <table class="table table-striped">
          <thead>
          <tr>
            <th>First Name</th>
            <th>Last Name</th>
            <th>Login Name</th>
            <th>Phone Num</th>
            <th>Email</th>
          </tr>
          </thead>
          <tbody>
          <tr ng-if="loginsVm.loadingLogins == true">
            <td colspan="6">
                        <span style="font-size: 15px;">
                            <img src="app/assets/bower_components/cassini-platform/images/loaders/loader19.gif" class="mr5">Loading Logins....
                        </span>
            </td>
          </tr>
          <tr ng-if="loginsVm.loadingLogins == false && loginsVm.logins.length == 0">
            <td colspan="6">No Logins</td>
          </tr>

          <tr ng-repeat="login in loginsVm.logins">
            <td><a href="" ng-click="loginsVm.openLogin(login)">{{login.person.firstName}}</a></td>
            <td>{{login.person.lastName}}</td>
            <td>{{login.loginName}}</td>
            <td>{{login.person.phoneMobile}}</td>
            <td>{{login.person.email}}</td>
            <td></td>
          </tr>
          </tbody>
        </table>
      </div>

      <div class="col-md-6">

        <table class="table table-striped">
          <thead>
          <tr>
            <th>Name</th>
            <th>Description</th>
          </tr>
          </thead>
          <tbody>
          <tr>
            <td colspan="6" ng-if="loginsVm.loadingGroups == true">
                        <span style="font-size: 15px;">
                            <img src="app/assets/bower_components/cassini-platform/images/loaders/loader19.gif" class="mr5">Loading Logins....
                        </span>
            </td>
          </tr>
          <tr ng-if="loginsVm.loadingGroups == false && loginsVm.logins.groups == 0">
            <td colspan="6">No Groups</td>
          </tr>

          <tr  ng-repeat="group in loginsVm.groups">
            <td>{{group.name}}</td>
            <td>{{group.description}}</td>
          </tr>
          </tbody>
        </table>
      </div>
    </div>
  </div>
</div>
</div>
</div>


