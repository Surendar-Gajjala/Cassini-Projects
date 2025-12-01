<style>
    .panel-heading {
        height: 50px !important;
        background-color: white !important;
        border: 1px solid lightgrey !important;
        padding: 8px;
        color: #42526E !important;
    }

    a.disabled {
        cursor: not-allowed;
        color: lightgrey;
    }

    .view-toolbar .btn {
        height: 30px !important;
        background-color: #0275d8 !important;
        color: #fff !important;
        border: 0 !important;
        font-weight: bold !important;
        font-size: 14px !important;
        border-color: #255625 !important;
    }

    .view-toolbar .btn:hover {
        background-color: #0275d8 !important;
        border-radius: 3px !important;
    }

</style>

<div class="panel panel-primary">
    <div class="view-toolbar">
        <div class="row" ng-if="hasPermission('admin','user','add')">
            <div class="col-sm-1" style="width: 40px;">
                <div class="btn-group">
                    <button class="btn btn-sm btn-success" ng-click="userVm.selectType('USER')"
                            title="{{newUserTitle}}" translate>NEW_USER
                        <%--<i class="la la-plus" style="" aria-hidden="true"></i>--%>
                    </button>
                </div>
            </div>
        </div>
    </div>
    <div id="admin-rightView-bar" class="panel-heading">
        <div class="col-md-12">
            <div class="col-md-4">
                <h3 style="margin: 0px;" translate>USERS</h3>
            </div>
            <div class="col-md-8" style="top: 5px;">
                <div class="pull-right text-center">
                    <div>
                        <span>
                           <medium>
                                <span><span translate>DISPLAYING</span>{{userVm.logins.numberOfElements}} of
                                            {{userVm.logins.totalElements}}
                                    </span>
                           </medium>
                        </span>
                        <span class="mr10"><span translate>PAGE</span> {{userVm.logins.totalElements != 0 ? userVm.logins.number+1:0}} of {{userVm.logins.totalPages}}</span>
                        <a href="" ng-click="userVm.previousUsersPage()"
                           ng-class="{'disabled': userVm.logins.first}"><i
                                class="fa fa-arrow-circle-left mr10"></i></a>
                        <a href="" ng-click="userVm.nextUsersPage()"
                           ng-class="{'disabled': userVm.logins.last}"><i
                                class="fa fa-arrow-circle-right"></i></a>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <div id="admin-rightView" class="panel-body" style="font-size: 14px;">
        <div class="responsive-table" style="padding: 5px;">
            <table class="table table striped highlight-row">
                <thead>
                <tr>
                    <th translate>FIRST_NAME</th>
                    <th translate>LAST_NAME</th>
                    <th translate>LOGIN_NAME</th>
                    <th translate>PHONE_NUMBER</th>
                    <th translate>EMAIL</th>
                    <th translate>IS_ACTIVE</th>
                    <th translate>EXTERNAL</th>
                    <%--<th style="text-align: center; width: 200px;">Actions</th>--%>
                </tr>
                </thead>

                <tbody>
                <tr ng-if="userVm.loadingUsers == true">
                    <td colspan="6">
                    <span style="font-size: 15px;">
                        <img src="app/assets/bower_components/cassini-platform/images/loaders/loader19.gif"
                             class="mr5">
                        <span translate>LOADING_USERS</span>
                    </span>
                    </td>
                </tr>

                <tr ng-if="userVm.loadingUsers == false && userVm.logins.content.length == 0">
                    <td colspan="6"><span translate>NO_USERS</span></td>
                </tr>

                <tr ng-repeat="login in userVm.logins.content">
                    <td>{{login.person.firstName}}</td>
                    <td>{{login.person.lastName}}</td>
                    <td>{{login.loginName}}</td>
                    <td>{{login.person.phoneMobile}}</td>
                    <td>{{login.person.email}}</td>
                    <td>
                        <div class="ckbox ckbox-default" style="display: inline-block;">
                            <input ng-disabled="true" id="iogin{{$index}}" name="loginSelected" ng-value="true"
                                   type="checkbox"
                                   ng-model="login.isActive">
                            <label for="iogin{{$index}}" class="item-selection-checkbox"></label>
                        </div>
                    </td>
                    <td>
                        <div class="ckbox ckbox-default" style="display: inline-block;">
                            <input ng-disabled="true" id="iogin{{$index}}" name="loginSelected" ng-value="true"
                                   type="checkbox"
                                   ng-model="login.external">
                            <label for="iogin{{$index}}" class="item-selection-checkbox"></label>
                        </div>
                    </td>
                </tr>
                </tbody>
            </table>
        </div>
    </div>
</div>
