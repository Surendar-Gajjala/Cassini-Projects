<div ng-controller="LoginRolesController as loginRolesVm">
    <br/>
    <div class="row">
        <div class="col-lg-6 col-lg-offset-3 col-md-8 col-md-offset-2 col-sm-12"
             style="padding: 20px;border: 1px solid #D0DDE1;background-color: white;margin-bottom: 20px;">
            <h4 class="text-center">Assign Roles</h4>
            <hr/>
            <div ng-if="loginRolesVm.loading">
                <span style="font-size: 15px;">
                    <img src="app/assets/images/loaders/loader9.gif" class="mr5">Loading roles...
                </span>
            </div>

            <div class="alert alert-warning" ng-if="loginRolesVm.selectedLogin.loginName == 'admin'">
                <i class="fa fa-warning mr10"></i>Roles for super admin cannot be modified!
            </div>

            <div class="alert alert-warning" ng-if="loginRolesVm.canModifyOtherAdmin() == false" >
                <i class="fa fa-warning mr10"></i>You cannot modify roles of another administrator!
            </div>


            <div ng-if="!loginRolesVm.loading">

                <table class="table table-striped">
                    <tbody ng-init="chunks = loginRolesVm.roles.chunk(2)">
                    <tr ng-repeat="chunk in chunks">
                        <td>
                            <input id="roleChk{chunk[0].id}" type="checkbox" ng-model="chunk[0].selected"
                                   ng-disabled="loginRolesVm.isRoleDisabled(chunk[0])"/>
                            <label for="roleChk{chunk[0].id}"
                                   ng-disabled="isRoleDisabled(chunk[0])">{{chunk[0].name}}</label>
                        </td>
                        <td>
                            <div ng-if="chunk[1].name != null">
                                <input id="roleChk{chunk[1].id}" type="checkbox" ng-model="chunk[1].selected"
                                       ng-disabled="loginRolesVm.isRoleDisabled(chunk[1])"/>
                                <label for="roleChk{chunk[1].id}"
                                       ng-disabled="loginRolesVm.isRoleDisabled(chunk[1])">{{chunk[1].name}}</label>
                            </div>
                        </td>
                    </tr>
                    </tbody>
                </table>
            </div>
            <hr/>
            <div style="text-align: center">
                <button class="btn btn-sm btn-default" ng-click="$parent.closeRoles()">Close</button>
                <button class="btn btn-sm btn-primary" ng-click="loginRolesVm.saveLoginRoles()"
                        ng-if="loginRolesVm.selectedLogin.loginName != 'admin'">Save</button>
            </div>
        </div>
    </div>
</div>