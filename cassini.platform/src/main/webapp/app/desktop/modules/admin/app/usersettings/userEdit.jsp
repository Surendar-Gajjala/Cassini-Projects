<style>
    .btn.disabled, .btn[disabled], fieldset[disabled] .btn {
        opacity: 1;
    }

    meter {
        /* Reset the default appearance */
        -moz-appearance: none;
        appearance: none;

        margin: 0 auto 1em;
        width: 100%;
        height: 0.7em;

        /* Applicable only to Firefox */
        background: none;
        background-color: rgba(0, 0, 0, 0.1);
    }

    meter::-webkit-meter-bar {
        background: none;
        background-color: rgba(0, 0, 0, 0.1);
    }

    meter[value="1"]::-webkit-meter-optimum-value {
        background: red;
    }

    meter[value="2"]::-webkit-meter-optimum-value {
        background: yellow;
    }

    meter[value="3"]::-webkit-meter-optimum-value {
        background: orange;
    }

    meter[value="4"]::-webkit-meter-optimum-value {
        background: green;
    }

    meter[value="1"]::-moz-meter-bar {
        background: red;
    }

    meter[value="2"]::-moz-meter-bar {
        background: yellow;
    }

    meter[value="3"]::-moz-meter-bar {
        background: orange;
    }

    meter[value="4"]::-moz-meter-bar {
        background: green;
    }

    .panel-heading {
        height: 50px !important;
        background-color: white !important;
        border: 1px solid lightgrey !important;
        padding: 8px;
    }
</style>

<div class="panel panel-primary">
    <div id="admin-rightView-bar" class="panel-heading">
        <div class="btn-group">
            <button class="btn btn-sm btn-default" ng-show="userVm.isUserEdit"
                    ng-click="userVm.editUser()" translate>CANCEL
            </button>
            <button class="btn btn-sm btn-info" ng-show="!userVm.isUserEdit" ng-if="hasPermission('admin','user','edit')"
                    ng-click="userVm.editUser()" translate>EDIT_USER
            </button>
            <button class="btn btn-sm btn-success min-width" ng-hide="!userVm.isUserEdit"
                    ng-click="userVm.updateUser()" translate>UPDATE_USER
            </button>
            <button class="btn btn-sm btn-primary" ng-if="!userVm.login.external"
                    ng-click="userVm.selectGroups()" translate>ADD_GROUP
            </button>
            <button class="btn btn-sm btn-warning" ng-show="hasPermission('admin','all')"
                    ng-click="userVm.resetPassword()" translate>RESET_PASSWORD
            </button>
            <button class="btn btn-sm btn-danger" ng-show="hasPermission('admin','user','delete')
                    && userVm.selectedUserNode.text1 != 'admin' && loginPersonDetails.loginName != userVm.selectedUserNode.text1"
                    ng-click="userVm.deleteUsers()" translate>DELETE_USER
            </button>
        </div>
    </div>

    <div id="admin-rightView" class="panel-body"
         style="overflow-x: hidden;overflow-y: auto;font-size: 14px;">
        <div class="col-xs-12 col-sm-10 col-sm-offset-1 col-md-8 col-md-offset-1">
            <form class="form-horizontal" style="margin-top: 20px;">

                <div class="form-group">
                    <label class="col-sm-4 control-label" for="fName"><span translate>FIRST_NAME</span><span
                            class="asterisk">*</span> : </label>

                    <div class="col-sm-7">
                        <input type="text" class="form-control" ng-disabled="!userVm.isUserEdit" id="fName"
                               placeholder="Name" ng-model="userVm.person.firstName">
                    </div>
                </div>
                <div class="form-group">
                    <label class="col-sm-4 control-label" for="lName"><span translate>LAST_NAME</span> : </label>

                    <div class="col-sm-7">
                        <input type="text" class="form-control" ng-disabled="!userVm.isUserEdit" id="lName"
                               placeholder="Lastname" ng-model="userVm.person.lastName">
                    </div>
                </div>
                <div class="form-group" id="content">
                    <label class="col-sm-4 control-label" for="Username"><span translate>USERNAME</span><span
                            class="asterisk">*</span> :</label>

                    <div class="col-sm-7">
                        <input type="text" class="form-control" disabled id="Username" placeholder="LoginName"
                               ng-model="userVm.login.loginName">
                    </div>
                </div>


                <div ng-if="safeCharsUserName == true" class="form-group"
                     style="margin-top: -16px;">
                    <div class="col-sm-4"></div>
                    <div class="col-sm-7">
                        <div class="alert alert-danger" style="margin: 0px;">
                            <%--<i class="fa fa-danger mr10"></i>--%>
                            <span ng-bind-html="invalidUsername"></span>
                        </div>
                    </div>
                </div>

                <div class="form-group">
                    <label class="col-sm-4 control-label" for="password"><span translate>PASSWORD</span><span
                            class="asterisk">*</span> :</label>

                    <div class="col-sm-7">
                        <input type="password" class="form-control"
                               ng-disabled="userVm.isUserEdit || !userVm.isUserEdit" id="password"
                               ng-change="editPasswordStrengthValid()"
                               placeholder="Password" ng-model="userVm.login.newPassword">
                    </div>
                </div>

                <%--<div class="form-group">
                    <label class="col-sm-4 control-label"><span translate>DEFAULT_GROUP</span><span
                            class="asterisk">*</span> : </label>

                    <div class="col-sm-7" style="border: 0;">
                        <ui-select ng-model="userVm.personDefaultGrp" ng-disabled="!userVm.isUserEdit"
                                   theme="bootstrap">
                            <ui-select-match placeholder="Select Group">{{$select.selected.name}}
                            </ui-select-match>
                            <ui-select-choices repeat="group in userVm.groups | filter: $select.search"
                                               style="max-height: 120px;">
                                <div ng-if="group.isActive" ng-bind="group.name | highlight: $select.name.search"></div>
                            </ui-select-choices>
                        </ui-select>
                    </div>
                </div>--%>


                <div class="form-group">
                    <label class="col-sm-4 control-label"><span translate>DEFAULT_GROUP</span><span
                            class="asterisk">*</span> : </label>

                    <div class="col-sm-7" style="border: 0;">
                        <ui-select ng-model="userVm.personDefaultGrp"
                                   on-select="userVm.onSelectGroup($item)"
                                   ng-disabled="!userVm.isUserEdit || userVm.login.loginName == 'admin'" theme="bootstrap"
                                   style="width: 100%;">
                            <ui-select-match placeholder="Select Group">{{$select.selected.name}}
                            </ui-select-match>
                            <ui-select-choices style="max-height: 191px;"
                                               repeat="group in userVm.groups | filter: $select.search">
                                {{group.name}}
                            </ui-select-choices>
                        </ui-select>
                    </div>

                </div>


                <div class="form-group">
                    <label class="col-sm-4 control-label" for="mobile"><span translate>PHONE_NUMBER</span>: </label>

                    <div class="col-sm-7">
                        <input type="tel" class="form-control" ng-disabled="!userVm.isUserEdit"
                               id="mobile" <%--maxlength="14"--%>
                               placeholder="Mobile no"
                               ng-model="userVm.person.phoneMobile"> <%--ng-pattern="/[0-9]{10}$/">--%>
                    </div>
                </div>
                <div class="form-group">
                    <label class="col-sm-4 control-label" for="mail"><span translate>EMAIL</span><span
                            class="asterisk">*</span> :</label>

                    <div class="col-sm-7">
                        <input type="email" class="form-control" ng-disabled="!userVm.isUserEdit" id="mail"
                               placeholder="email" ng-model="userVm.person.email">
                    </div>
                </div>
                <div class="form-group">
                    <label class="col-sm-4 control-label" for="isActive"><span translate>IS_ACTIVE</span> :</label>

                    <div class="col-sm-2">
                        <input style="margin-top: 10px" type="checkbox" class="form-control"
                               ng-disabled="!userVm.isUserEdit" id="isActive"
                               ng-change="userVm.userActivation()"
                               ng-model="userVm.login.isActive">
                    </div>
                </div>
                <div class="form-group">
                    <label class="col-sm-4 control-label" for="external"><span translate>EXTERNAL</span> :</label>

                    <div class="col-sm-2">
                        <input style="margin-top: 10px"
                               type="checkbox" class="form-control"
                               ng-disabled="!userVm.isUserEdit || !userVm.personDefaultGrp.external" id="external"
                               ng-model="userVm.login.external">
                    </div>
                </div>
            </form>
            <hr>
        </div>

        <div class="row" style="margin-top: 20px;">

            <div class="col-xs-12">
                <h4 translate>USER_GROUPS</h4>
                <table class="table table striped">
                    <thead>
                    <tr>
                        <th translate>GROUP_NAME</th>
                        <th translate>DESCRIPTION</th>
                    </tr>
                    </thead>

                    <tbody>
                    <tr></tr>
                    <tr ng-if=" userVm.userPersonGroups.length == 0">
                        <td colspan="6"><span translate>USER_NOT_IN_ANY_GROUPS</span></td>
                    </tr>
                    <tr ng-repeat="userPersonGrp in userVm.userPersonGroups">
                        <td>{{userPersonGrp.name}}</td>
                        <td>{{userPersonGrp.description}}</td>
                    </tr>
                    </tbody>
                </table>
            </div>

        </div>
    </div>
</div>