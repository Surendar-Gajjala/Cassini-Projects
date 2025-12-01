<div style="padding: 10px; height: auto;">
    <div class="row">
        <div class="col-md-12" style="padding:0px; height: auto;overflow: auto;">
            <table class="table table striped">
                <thead>
                <tr>
                    <th style="width: 80px; text-align: center">
                        <input ng-if="userDialogVm.userList.length > 1" type="checkbox"
                               ng-model="userDialogVm.selectAllCheck"
                               ng-click="userDialogVm.selectAll(check);" ng-checked="check">
                    </th>
                    <th translate>FIRST_NAME</th>
                    <th translate>LAST_NAME</th>
                    <th translate>LOGIN_NAME</th>
                    <th translate>PHONE_NUMBER</th>
                    <th translate>EMAIL</th>
                </tr>
                </thead>
                <tbody>
                <tr ng-if="userDialogVm.loading == true">
                    <td colspan="6">
                <span style="font-size: 15px;">
                    <img src="app/assets/bower_components/cassini-platform/images/loaders/loader19.gif" class="mr5">
                    <span translate>LOADING_USERS</span>
                </span>
                    </td>
                </tr>

                <tr ng-if="userDialogVm.loading == false && userDialogVm.userList.length == 0">
                    <td colspan="6"><span translate>NO_USERS</span></td>
                </tr>


                <tr ng-repeat="user in userDialogVm.userList">
                    <td style="width: 80px; text-align: center">
                        <input type="checkbox" ng-click="userDialogVm.selectCheck(user)" ng-model="user.isSelected">
                    </td>
                    <td style="vertical-align: middle;">{{user.person.firstName}}</td>
                    <td style="vertical-align: middle;">{{user.person.lastName}}</td>
                    <td style="vertical-align: middle;">{{user.loginName}}</td>
                    <td style="vertical-align: middle;">{{user.person.phoneMobile}}</td>
                    <td style="vertical-align: middle;">{{user.person.email}}</td>
                </tr>
                </tbody>
            </table>
        </div>
    </div>
    <br>
    <br>
</div>
