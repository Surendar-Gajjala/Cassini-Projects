<div class="btn-group">
    <button class="btn btn-sm btn-default" ng-click="userVm.cancel()" translate>CANCEL</button>
    <button class="btn btn-sm btn-success" ng-click="userVm.createRole()" translate>CREATE</button>
    <button class="btn btn-sm btn-primary" ng-click="userVm.selectUsers(true)" translate>SELECT_USERS</button>
    <button class="btn btn-sm btn-warning" ng-click="userVm.selectGroups(true)" translate>SELECT_GROUPS</button>
</div>
<h3 style="text-align: center; padding-right: 100px;" translate>NEW_ROLE</h3>
<hr>
<div class="alert alert-danger" ng-hide="userVm.valid" ng-cloak>
    {{ userVm.error }}
</div>
<div class="row">
    <div class="col-xs-12 col-sm-8 col-sm-offset-1 col-md-6 col-md-offset-2">
        <form class="form-horizontal">
            <div class="form-group">
                <label class="col-sm-4 control-label" for="name"><span translate>NAME</span>
                    <span class="asterisk">*</span>:</label>

                <div class="col-sm-7">
                    <input type="text" class="form-control" id="name"
                           ng-model="userVm.role.name">
                </div>
            </div>
            <div class="form-group">
                <label class="col-sm-4 control-label"><span translate>DESCRIPTION</span> :</label>

                <div class="col-sm-7">
                    <textarea type="text" class="form-control" rows="5" cols="5"
                              ng-model="userVm.role.description"></textarea>
                </div>
            </div>
        </form>
    </div>
</div>

<div class="modal-body" style="padding: 20px; max-height: 500px;">

    <h4 translate>SELECTED_USERS</h4>
    <table class="table table striped">
        <thead>
        <tr>
            <th translate>FIRST_NAME</th>
            <th translate>LAST_NAME</th>
            <th translate>LOGIN_NAME</th>
            <th translate>PHONE_NUMBER</th>
            <th translate>EMAIL</th>
        </tr>
        </thead>

        <tbody>
        <tr ng-if="userVm.selectedUserList.length == 0">
            <td colspan="6"><span translate>NO_USERS_SELECTED</span></td>
        </tr>

        <tr ng-repeat="user in userVm.selectedUserList">
            <td>{{user.person.firstName}}</td>
            <td>{{user.person.lastName}}</td>
            <td>{{user.loginName}}</td>
            <td>{{user.person.phoneMobile}}</td>
            <td>{{user.person.email}}</td>
        </tr>
        </tbody>
    </table>
    <h4 translate>SELECTED_GROUPS</h4>

    <table class="table table striped">
        <thead>
        <tr>
            <th translate>NAME</th>
            <th translate>DESCRIPTION</th>
        </tr>
        </thead>

        <tbody>
        <tr ng-if="userVm.selectedGroupList.length == 0">
            <td colspan="6"><span translate>NO_GROUPS_SELECTED</span></td>
        </tr>

        <tr ng-repeat="group in userVm.selectedGroupList">
            <td>{{group.name}}</td>
            <td>{{group.description}}</td>
        </tr>
        </tbody>
    </table>
</div>
