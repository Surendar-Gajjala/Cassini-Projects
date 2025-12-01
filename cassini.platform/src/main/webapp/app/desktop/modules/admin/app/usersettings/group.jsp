<div>
    <style scoped>
        .panel-heading {
            height: 50px !important;
            background-color: white !important;
            border: 1px solid lightgrey !important;
            padding: 8px;
            width: 100%;
            display: inline-flex !important;
        }
    </style>

    <div class="panel panel-primary">
        <div id="admin-rightView-bar" class="panel-heading">
            <div class="btn-group" style="width: 40%;">
                <button type="button" class="btn btn-sm btn-default" ng-click="userVm.cancel()" translate>CANCEL
                </button>
                <button class="btn btn-sm btn-success" ng-click="userVm.createGroup()" translate>CREATE</button>
            </div>
            <div>
                <h3 style="margin-top: 3px;" translate>NEW_GROUP</h3>
            </div>
        </div>

        <div id="admin-rightView" class="panel-body"
             style="overflow-x: hidden;overflow-y: auto;font-size: 14px;">

            <div class="col-xs-12 col-sm-10 col-sm-offset-1 col-md-8 col-md-offset-1">

                <form class="form-horizontal" style="margin-top: 10px;">
                    <div class="form-group">
                        <label class="col-sm-4 control-label" for="name">
                            <span translate>NAME</span><span class="asterisk"> *</span> :</label>

                        <div class="col-sm-7">
                            <input type="text" class="form-control" id="name" placeholder="{{'NAME' | translate}}"
                                   ng-model="userVm.personGroup.name">
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-4 control-label"><span translate>DESCRIPTION</span> :</label>

                        <div class="col-sm-7">
                    <textarea type="text" class="form-control" rows="5" cols="5"
                              placeholder="{{'DESCRIPTION' | translate}}"
                              ng-model="userVm.personGroup.description"></textarea>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-4 control-label"><span translate>PROFILE_LABEL</span>
                            <span class="asterisk">*</span>:</label>

                        <div class="col-sm-7" style="border: 0;">
                            <ui-select ng-model="userVm.personGroup.profile"
                                       on-select="userVm.onSelectProfile($item)"
                                       theme="bootstrap"
                                       style="width: 100%;">
                                <ui-select-match placeholder="Select Profile">{{$select.selected.name}}
                                </ui-select-match>
                                <ui-select-choices style="max-height: 191px;"
                                                   repeat="profile in userVm.allProfiles | filter: $select.search">
                                    {{profile.name}}
                                </ui-select-choices>
                            </ui-select>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-4 control-label"><span translate>IS_ACTIVE</span> :</label>

                        <div class="col-sm-1">
                            <input style="margin-top: 12px" type="checkbox" class="form-control" checked="checked"
                                   ng-init="userVm.personGroup.isActive = true"
                                   ng-model="userVm.personGroup.isActive">
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-4 control-label"><span translate>EXTERNAL</span> :</label>

                        <div class="col-sm-1">
                            <input style="margin-top: 12px" type="checkbox" class="form-control"
                                   ng-init="userVm.personGroup.external = false"
                                   ng-model="userVm.personGroup.external">
                        </div>
                    </div>

                </form>
            </div>

            <div class="col-xs-12">
                <h4 translate>SELECTED_USERS</h4>

                <div class="responsive-table" style="padding: 5px;">
                    <table class="table table striped highlight-row">
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
                </div>
            </div>
        </div>
    </div>
</div>