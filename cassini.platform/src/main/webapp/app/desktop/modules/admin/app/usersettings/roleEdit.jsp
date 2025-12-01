<div class="btn-group">
    <button class="btn btn-sm btn-default" ng-show="userVm.isRoleEdit"
            ng-click="userVm.editRole()" translate>CANCEL
    </button>
    <button class="btn btn-sm btn-danger" ng-show="!userVm.isRoleEdit"
            ng-click="userVm.editRole()" translate>EDIT_ROLE
    </button>
    <button class="btn btn-sm btn-success" ng-hide="!userVm.isRoleEdit"
            ng-click="userVm.updateRole()" translate>UPDATE_ROLE
    </button>
    <button class="btn btn-sm btn-primary"
            ng-hide="!userVm.isRoleEdit" ng-click="userVm.selectUsers(false)" translate>SELECT_USERS
    </button>
    <button class="btn btn-sm btn-warning"
            ng-hide="!userVm.isRoleEdit" ng-click="userVm.selectGroups(false)" translate>SELECT_GROUPS
    </button>
    <p ng-show="updateModeType == true && userVm.isRoleEdit"
       style="color: #0390fd;margin-top: 0px;margin-left: 450px; font-size: 16px;" translate>UPDATE_ROLE_ALERT_MESSAGE
    </p>
</div>
<hr>
<div class="alert alert-danger" ng-hide="userVm.valid" ng-cloak>
    {{ userVm.error }}
</div>
<div class="row">
    <div class="col-xs-12 col-sm-8 col-sm-offset-1 col-md-6 col-md-offset-2">
        <form class="form-horizontal">
            <div class="form-group">
                <label class="col-sm-4 control-label" for="name"><span translate>NAME</span> :</label>

                <div class="col-sm-7">
                    <input type="text" ng-disabled="!userVm.isRoleEdit" class="form-control" id="name"
                           placeholder="Name" ng-model="userVm.role.name">
                </div>
            </div>
            <div class="form-group">
                <label class="col-sm-4 control-label" for="description"><span translate>DESCRIPTION</span> :</label>

                <div class="col-sm-7">
                    <input type="text" ng-disabled="!userVm.isRoleEdit" class="form-control" id="description"
                           placeholder="Description" ng-model="userVm.role.description">
                </div>
            </div>
        </form>
    </div>
</div>
<div class="row" style="margin-top: 20px;">
    <div class="col-xs-12">
        <h4 translate>USERS</h4>
        <table class="table table striped">
            <thead>
            <tr>
                <th translate>FIRST_NAME</th>
                <th translate>LAST_NAME</th>
                <th translate>LOGIN_NAME</th>
                <th translate>PHONE_NUMBER</th>
                <th translate>EMAIL</th>
                <th translate>ACTIONS</th>
            </tr>
            </thead>

            <tbody>
            <tr></tr>
            <tr ng-if=" userVm.role.personRoles.length == 0">
                <td colspan="6"><span translate>NO_USERS</span></td>
            </tr>
            <tr ng-repeat="login in userVm.role.personRoles">
                <td>{{login.person.firstName}}</td>
                <td>{{login.person.lastName}}</td>
                <td>{{login.login.loginName}}</td>
                <td>{{login.person.phoneMobile}}</td>
                <td>{{login.person.email}}</td>
                <td>
                    <button title="Delete This User"
                            class="btn btn-xs btn-danger"
                            ng-click="userVm.deletePersonRole(login)"><i class="fa fa-trash"></i></button>
                </td>
            </tr>
            </tbody>
        </table>

        <h4 translate>PERSON_GROUPS</h4>
        <table class="table table striped">
            <thead>
            <tr>
                <th translate>NAME</th>
                <th translate>DESCRIPTION</th>
                <th translate>ACTIONS</th>
            </tr>
            </thead>

            <tbody>
            <tr></tr>
            <tr ng-if=" userVm.role.personGroupRoles.length == 0">
                <td colspan="6"><span translate>NO_GROUPS</span></td>
            </tr>
            <tr ng-repeat="group in userVm.role.personGroupRoles">
                <td>{{group.personGroup.name}}</td>
                <td>{{group.personGroup.description}}</td>
                <td>
                    <button title="Delete This Group"
                            class="btn btn-xs btn-danger"
                            ng-click="userVm.deletePersonGroupRole(group)"><i class="fa fa-trash"></i></button>
                </td>
            </tr>
            </tbody>
        </table>

    </div>
</div>
