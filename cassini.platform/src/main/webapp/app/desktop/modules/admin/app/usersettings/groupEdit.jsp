<style>
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
            <button class="btn btn-sm btn-default"
                    ng-show="userVm.isGroupEdit"
                    ng-click="userVm.editGroup()" translate>CANCEL
            </button>
            <button class="btn btn-sm btn-info"
                    ng-show="!userVm.isGroupEdit" ng-if="hasPermission('admin','group','edit')"
                    ng-click="userVm.editGroup()" translate>EDIT_GROUP
            </button>
            <button class="btn btn-sm btn-success" ng-hide="!userVm.isGroupEdit"
                    ng-click="userVm.updateGroup()" translate>UPDATE_GROUP
            </button>
            <button class="btn btn-sm btn-primary"
                    ng-show="userVm.inActive == false"
                    ng-click="userVm.selectUsers(false)" translate>ADD_USERS
            </button>

            <button class="btn btn-sm btn-success" ng-show="hasPermission('admin','group','add')"
                    ng-click="userVm.createGroups()" translate>NEW_GROUP
            </button>
            <button class="btn btn-sm btn-danger"
                    ng-show="hasPermission('admin','group','delete') && userVm.selectedUserNode.text != 'Administrator'"
                    ng-click="userVm.deleteGroups()" translate>DELETE_GROUP
            </button>
            <%--<button class="btn btn-sm btn-primary"
                    ng-show="userVm.inActive == false"
                    ng-click="userVm.selectProfile()" translate>ADD_PROFILE
            </button>--%>
        </div>
    </div>

    <div id="admin-rightView" class="panel-body"
         style="overflow-x: hidden;overflow-y: auto;font-size: 14px;">
        <div class="col-xs-12 col-sm-10 col-sm-offset-1 col-md-8 col-md-offset-1" style="margin-top: 10px;">
            <form class="form-horizontal">
                <div class="form-group">
                    <label class="col-sm-4 control-label" for="name"><span translate>NAME</span><span class="asterisk"> *</span>
                        :</label>

                    <div class="col-sm-7">
                        <input type="text"
                               ng-disabled="!userVm.isGroupEdit || userVm.personGroup.name =='Administrator'"
                               class="form-control" id="name"
                               placeholder="Name" ng-model="userVm.personGroup.name">
                    </div>
                </div>
                <div class="form-group">
                    <label class="col-sm-4 control-label" for="description"><span translate>DESCRIPTION</span> :</label>

                    <div class="col-sm-7">
                        <input type="text" ng-disabled="!userVm.isGroupEdit" class="form-control" id="description"
                               placeholder="Description" ng-model="userVm.personGroup.description">
                    </div>

                </div>
                <div class="form-group">
                    <label class="col-sm-4 control-label"><span translate>PROFILE_LABEL</span>
                        <span class="asterisk">*</span>:</label>

                    <div class="col-sm-7" style="border: 0;">
                        <ui-select ng-model="userVm.personGroup.profile"
                                   on-select="userVm.onSelectProfile($item)"
                                   ng-disabled="!userVm.isGroupEdit" theme="bootstrap"
                                   style="width: 100%;">
                            <ui-select-match placeholder="Select Profile">{{$select.selected.name}}
                            </ui-select-match>
                            <ui-select-choices style="max-height: 191px;"
                                               repeat="profile in userVm.allProfiles | filter: $select.search">
                                {{profile.name}}
                            </ui-select-choices>
                        </ui-select>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-4 control-label" for="isActive"><span translate>IS_ACTIVE</span> :</label>

                        <div class="col-sm-1">
                            <input style="margin-top: 15px" type="checkbox" class="form-control"
                                   ng-disabled="!userVm.isGroupEdit" id="isActive"
                                   ng-model="userVm.personGroup.isActive">
                        </div>
                    </div>

                    <div class="form-group">
                        <label class="col-sm-4 control-label" for="external"><span translate>EXTERNAL</span> :</label>

                        <div class="col-sm-1">
                            <input style="margin-top: 15px" type="checkbox" class="form-control"
                                   ng-disabled="!userVm.isGroupEdit" id="external"
                                   ng-model="userVm.personGroup.external">
                        </div>
                    </div>

                </div>
            </form>
        </div>

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
                <tr ng-if=" userVm.personGroup.groupMember.length == 0">
                    <td colspan="6"><span translate>NO_USERS</span></td>
                </tr>
                <tr ng-repeat="login in userVm.personGroup.groupMember">
                    <td>{{login.person.firstName}}</td>
                    <td>{{login.person.lastName}}</td>
                    <td>{{login.personObject.loginName}}</td>
                    <td>{{login.person.phoneMobile}}</td>
                    <td>{{login.person.email}}</td>
                    <td>
                        <button title="{{userVm.removeUser}}"
                                class="btn btn-xs btn-danger"
                                ng-click="userVm.deleteGroupMember(login)"><i class="fa fa-trash"></i></button>
                    </td>
                </tr>
                </tbody>
            </table>
        </div>
        <%--<div class="col-xs-12">
            <h4 translate>PROFILES</h4>
            <table class="table table striped">
                <thead>
                <tr>
                    <th translate>NAME</th>
                    <th translate>CREATED_BY</th>
                    <th translate>CREATED_DATE</th>
                    <th translate>ACTIONS</th>
                </tr>
                </thead>

                <tbody>
                <tr></tr>
                <tr ng-if="userVm.personGroup.profile == null">
                    <td colspan="6"><span translate>NO_PROFILE</span></td>
                </tr>
                <tr ng-if="userVm.personGroup.profile != null">
                    <td>{{userVm.personGroup.profile.name}}</td>
                    <td style="vertical-align: middle;">{{userVm.personGroup.profile.createdByObject.firstName}}</td>
                    <td style="vertical-align: middle;">{{userVm.personGroup.profile.createdDate}}</td>
                    <td>
                        <button class="btn btn-xs btn-danger"
                                ng-click="userVm.deleteGroupProfile(userVm.personGroup.profile)"><i
                                class="fa fa-trash"></i></button>
                    </td>
                </tr>
                </tbody>
            </table>
        </div>--%>
    </div>
</div>
