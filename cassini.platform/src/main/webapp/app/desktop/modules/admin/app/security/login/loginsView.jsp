<div style="margin:10px;" class="view-toolbar">
    <button class="btn btn-sm btn-primary" ng-click="loginVm.showNewLogin()">New Login</button>
</div>
<div class="row" style="padding-bottom: 10px;">
    <div class="col-lg-8 col-lg-offset-2 col-md-10 col-md-offset-1 col-sm-col-12">
        <div ng-include="loginVm.showNewLoginForm ? 'app/assets/bower_components/cassini-platform/app/desktop/modules/admin/app/security/login/newLogin.jsp' : null">

        </div>
        <div ng-if="!loginVm.showNewLoginForm">
            <div class="row" style="margin-bottom: 20px">
                <div class="col-md-4" style="margin-top: 20px;">

                </div>
                <div class="col-md-8"  style="text-align: right" ng-show="loginVm.logins.numberOfElements > 0">
                    <div style="text-align: right;">
                        <pagination total-items="loginVm.logins.totalElements"
                                    items-per-page="loginVm.pageable.size"
                                    max-size="5"
                                    boundary-links="true"
                                    ng-model="loginVm.pageable.page"
                                    ng-change="loginVm.pageChanged()">
                        </pagination>
                    </div>
                    <div>
                        <small>Total {{loginVm.logins.totalElements}} logins</small>
                    </div>
                </div>
            </div>


            <div>
                <table class="table table-striped">
                    <thead>
                    <tr>
                        <th style="width:100px; text-align: center">Actions</th>
                        <th>Login</th>
                        <th>User</th>
                    </tr>
                    </thead>
                    <tbody>
                    <tr ng-if="loginVm.logins.totalElements == 0 || loginVm.loading == true">
                        <td colspan="3">
                            <span ng-hide="loading">There are no logins</span>
                                <span ng-show="loading">
                                    <img src="app/assets/images/loaders/loader19.gif" class="mr5">Loading logins...
                                </span>
                        </td>
                    </tr>

                    <tr ng-repeat="login in loginVm.logins.content">
                        <td style="background-color:#F2F7F9" colspan="3"
                            ng-include="login.showRoles ? 'app/assets/bower_components/cassini-platform/app/desktop/modules/admin/app/security/login/loginRolesView.jsp' : null">
                        </td>

                        <td ng-if="!login.showRoles" style="width:100px;" class="col-center actions-col">
                            <div class="btn-group" uib-dropdown style="margin-bottom: 0px;">
                                <button type="button" class="btn btn-sm btn-default dropdown-toggle" uib-dropdown-toggle>
                                    <i class="fa fa-cog fa-fw"></i></span>
                                </button>
                                <ul class="dropdown-menu" role="menu">
                                    <li><a href="" ng-click="loginVm.showRoles(login)">Assign Roles</a></li>
                                    <li><a href="" ng-click="loginVm.resetPassword(login)">Reset Password</a></li>
                                </ul>
                            </div>
                        </td>
                        <td ng-if="!login.showRoles" style="text-align: left">{{login.loginName}}</td>
                        <td ng-if="!login.showRoles" style="text-align: left">{{login.person.firstName}}</td>
                    </tr>
                    </tbody>
                </table>
            </div>
        </div>
    </div>
</div>
