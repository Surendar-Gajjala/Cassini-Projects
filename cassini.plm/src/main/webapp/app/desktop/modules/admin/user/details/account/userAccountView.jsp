<div>
    <style scoped>

        .view-details {

        }

        .view-details .view-header {
            padding: 20px;
            border-bottom: 1px solid #eee;
        }

        .view-details .view-header .view-title {
            font-size: 16px;
            font-weight: 600;
        }

        .view-details .view-header .view-summary {
            color: #707d91;
            font-weight: 300;
        }

        .view-details .view-content {

        }

        .switch {
            position: relative;
            display: inline-block;
            width: 60px;
            height: 34px;
        }

        .switch input {
            opacity: 0;
            width: 0;
            height: 0;
        }

        .switch .slider {
            position: absolute;
            cursor: pointer;
            top: 0;
            left: 0;
            right: 0;
            bottom: 0;
            background-color: #ccc;
            -webkit-transition: .4s;
            transition: .4s;
        }

        .switch .slider:before {
            position: absolute;
            content: "";
            height: 26px;
            width: 26px;
            left: 4px;
            bottom: 4px;
            background-color: white;
            -webkit-transition: .4s;
            transition: .4s;
        }

        .switch input:checked + .slider {
            background-color: #2196F3;
        }

        .switch input:focus + .slider {
            box-shadow: 0 0 1px #2196F3;
        }

        .switch input:checked + .slider:before {
            -webkit-transform: translateX(26px);
            -ms-transform: translateX(26px);
            transform: translateX(26px);
        }

        /* Rounded sliders */
        .switch .slider.round {
            border-radius: 34px;
        }

        .switch .slider.round:before {
            border-radius: 50%;
        }

    </style>

    <div class="view-details">
        <div class="view-header d-flex">
            <div style="flex: 1">
                <div class="view-title">Account Information</div>
                <div class="view-summary">View and update user account information</div>
            </div>
            <div class="header-buttons">
                <button class="btn btn-sm btn-new" ng-click="userAccountVm.updateUser()"
                        ng-disabled="userAccountVm.person.id != loginPersonDetails.person.id && loginPersonDetails.person.isSuperUser">
                    Save
                </button>
            </div>
        </div>
        <div class="view-content">
            <form class="form-horizontal" style="margin-top: 30px;">
                <div class="form-group">
                    <label class="col-sm-4 control-label" for="fName"><span translate>FIRST_NAME</span><span
                            class="asterisk"> *</span> : </label>

                    <div class="col-sm-6">
                        <input type="text" class="form-control" id="fName"
                               placeholder="{{'FIRST_NAME_TITLE' | translate}}"
                               ng-model="userAccountVm.person.firstName"
                               ng-disabled="userAccountVm.person.id != loginPersonDetails.person.id">
                    </div>
                </div>
                <div class="form-group">
                    <label class="col-sm-4 control-label" for="lName"><span translate>LAST_NAME</span> : </label>

                    <div class="col-sm-6">
                        <input type="text" class="form-control" id="lName"
                               placeholder="{{'LAST_NAME' | translate}}" ng-model="userAccountVm.person.lastName"
                               ng-disabled="userAccountVm.person.id != loginPersonDetails.person.id">
                    </div>
                </div>
                <div class="form-group">
                    <label class="col-sm-4 control-label" for="Username"><span translate>USERNAME</span><span
                            class="asterisk"> *</span> :</label>

                    <div class="col-sm-6">
                        <input type="text" class="form-control" id="Username"
                               placeholder="{{'USER_NAME_TITLE' | translate}}"
                               ng-change="loginDetails.loginName = (loginDetails.loginName | lowercase)" ng-model="loginDetails.loginName"
                               ng-disabled="userAccountVm.person.id != loginPersonDetails.person.id">
                    </div>
                </div>

                <div class="form-group">
                    <label class="col-sm-4 control-label" for="Phone"><span translate>PHONE_NUMBER</span> :</label>

                    <div class="col-sm-6">
                        <input type="text" class="form-control" id="Phone"
                               placeholder="{{'PHONE_NUMBER' | translate}}"
                               ng-model="userAccountVm.person.phoneMobile"
                               ng-disabled="userAccountVm.person.id != loginPersonDetails.person.id" valid-number
                               pattern="[0-9]*">
                    </div>
                </div>

                <div class="form-group">
                    <label class="col-sm-4 control-label" for="Email"><span translate>EMAIL</span><span
                            class="asterisk"> *</span> :</label>

                    <div class="col-sm-6">
                        <input type="text" class="form-control" id="Email" placeholder="{{'EMAIL' | translate}}"
                               ng-model="userAccountVm.person.email"
                               ng-disabled="!loginPersonDetails.isSuperUser && userAccountVm.person.id != loginPersonDetails.person.id">
                    </div>
                </div>
                <div class="form-group">
                    <label class="col-sm-4 control-label">
                        <span translate>DEFAULT_ROLE</span>
                        <span class="asterisk">*</span>: </label>

                    <div class="col-sm-6">
                        <ui-select ng-model="userAccountVm.person.defaultGroup" theme="bootstrap"
                                   style="width:100%" on-select="userAccountVm.onSelectGroup($item)"
                                   ng-disabled="!loginPersonDetails.isSuperUser">
                            <ui-select-match placeholder="{{selectGroup}}">
                                {{$select.selected.name}}
                            </ui-select-match>
                            <ui-select-choices
                                    repeat="group.groupId as group in userAccountVm.groups | filter: $select.search | orderBy:'name'">
                                <div ng-bind-html="group.name | highlight: $select.name.search"></div>
                            </ui-select-choices>
                        </ui-select>
                    </div>
                </div>
                <div class="form-group">
                    <label class="col-sm-4 control-label"><span translate>IS_ADMIN</span> :</label>

                    <div class="col-sm-6">
                        <input type="checkbox" id="isAdmin" switch="none" checked="" ng-model="loginDetails.isSuperUser"
                               ng-disabled="loginDetails.external || loginPersonDetails.id == loginDetails.id">
                        <label for="isAdmin" data-on-label="{{yes}}" data-off-label="{{no}}"></label>
                    </div>
                </div>

                <div class="form-group">
                    <label class="col-sm-4 control-label"><span translate>IS_ACTIVE</span> :</label>

                    <div class="col-sm-6">
                        <input type="checkbox" id="isActive" switch="none" checked="" ng-model="loginDetails.isActive"
                               ng-change="userAccountVm.userActivation(loginDetails)"
                               ng-disabled="!loginPersonDetails.isSuperUser || loginPersonDetails.id == loginDetails.id">
                        <label for="isActive" data-on-label="{{yes}}" data-off-label="{{no}}"></label>
                    </div>
                </div>

                <div class="form-group">
                    <label class="col-sm-4 control-label" for="external"><span translate>EXTERNAL</span> :</label>

                    <div class="col-sm-6">
                        <input type="checkbox" id="external" switch="none" checked="" ng-model="loginDetails.external"
                               ng-disabled="true">
                        <label for="external" data-on-label="{{yes}}" data-off-label="{{no}}"></label>
                    </div>
                </div>
            </form>
        </div>
    </div>

</div>