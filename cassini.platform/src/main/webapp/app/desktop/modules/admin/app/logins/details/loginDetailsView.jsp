<div class="view-container" fitcontent>
    <div class="view-toolbar">
        <span style="font-weight: 600;font-size: 20px;padding-right: 10px;">{{'LOGIN_DETAILS' | translate}}</span>

        <button class="btn btn-sm btn-default" ng-click="loginDetailsVm.back()" title="{{loginDetailsVm.backTitle}}">
            <i class="fa fa-arrow-left" aria-hidden="true"></i>
        </button>

        <button class="btn btn-sm btn-success" ng-click="loginDetailsVm.updateLogin()"
                title="{{loginDetailsVm.saveTitle}}">
            <i class="fa fa-save" aria-hidden="true"></i>
        </button>

        <button class="btn btn-sm btn-danger" ng-click="loginDetailsVm.changePassword()"
                title="{{loginDetailsVm.changePasswordTitle}}">
            <i class="fa fa-key" aria-hidden="true"></i>
        </button>
    </div>

    <div class="view-content no-padding">
        <div ng-if="loginDetailsVm.loading == true" style="padding: 30px;">
            <br/>
                <span style="font-size: 15px;">
                    <img src="app/assets/bower_components/cassini-platform/images/loaders/loader6.gif" class="mr5">
                    <span translate>LOADING_LOGIN_DETAILS</span>
                </span>
            <br/>
        </div>
        <div class="row row-eq-height" style="margin: 0;" ng-if="loginDetailsVm.loading == false">
            <div class="item-details col-sm-10" style="padding: 30px;">
                <div class="row">
                    <div class="label col-xs-5 col-sm-3 text-right">
                        <span translate>FIRST_NAME</span> :
                    </div>
                    <div class="value col-xs-7 col-sm-9">
                        <a href="#" onaftersave="loginDetailsVm.updateLogin(login)"
                           editable-text="loginDetailsVm.login.person.firstName">{{loginDetailsVm.login.person.firstName}}</a>
                    </div>
                </div>
                <div class="row">
                    <div class="label col-xs-5 col-sm-3 text-right">
                        <span translate>LAST_NAME</span> :
                    </div>
                    <div class="value col-xs-7 col-sm-9">
                        <a href="#" editable-text="loginDetailsVm.login.person.lastName"
                           onaftersave="loginDetailsVm.updateLogin(login)">
                            {{loginDetailsVm.login.person.lastName || addLastName}}</a>
                    </div>
                </div>
                <div class="row">
                    <div class="label col-xs-5 col-sm-3 text-right">
                        <span translate>LOGIN_NAME</span> :
                    </div>
                    <div class="value col-xs-7 col-sm-9">
                        {{loginDetailsVm.login.loginName}}
                    </div>
                </div>
                <div class="row">
                    <div class="label col-xs-5 col-sm-3 text-right">
                        <span translate>PHONE_NUMBER</span> :
                    </div>
                    <div class="value col-xs-7 col-sm-9">
                        <a href="#" editable-text="loginDetailsVm.login.person.phoneMobile"
                           onaftersave="loginDetailsVm.updateLogin(login)">
                            {{loginDetailsVm.login.person.phoneMobile || addPhoneNumber}}</a>
                    </div>
                </div>
                <div class="row">
                    <div class="label col-xs-5 col-sm-3 text-right">
                        <span translate>EMAIL</span> :
                    </div>
                    <div class="value col-xs-7 col-sm-9">
                        <a href="#"
                           onaftersave="loginDetailsVm.updateLogin(login)"
                           editable-text="loginDetailsVm.login.person.email">{{loginDetailsVm.login.person.email}}</a>
                    </div>
                </div>

                <div class="row">
                    <div class="label col-xs-5 col-sm-3 text-right">
                        <span translate>THEME</span> :
                    </div>
                    <div class="value col-xs-7 col-sm-9">
                        <a href="#" e-style="width: 50%"
                           onaftersave="loginDetailsVm.updateUserPreference()"
                           editable-select="loginDetailsVm.userPreferences.userTheme"
                           e-ng-options="theme for theme in loginDetailsVm.themes track by theme">
                            {{loginDetailsVm.userPreferences.userTheme || 'Click to add Theme'}}
                        </a>
                    </div>
                </div>

                <div class="row">
                    <div class="label col-xs-5 col-sm-3 text-right">
                        <span translate>DATE_FORMAT</span> :
                    </div>
                    <div class="value col-xs-7 col-sm-9">
                        <ui-select ng-model="loginDetailsVm.userPreferences.userDateFormat" theme="bootstrap"
                                   on-select="loginDetailsVm.updateDateFormat()" style="width:280px" id="minLen"
                                   name="minLen">
                            <ui-select-match placeholder="Select">{{$select.selected}}</ui-select-match>
                            <ui-select-choices repeat="format in formats">
                                <div ng-bind="format"></div>
                            </ui-select-choices>
                        </ui-select>
                    </div>
                </div>

                <div class="row"
                     ng-if="loginDetailsVm.changeApprovalPreference.booleanValue">
                    <div class="label col-xs-5 col-sm-3 text-right">
                        <span translate>CHANGE_APPROVAL_PASSWORD</span> :
                    </div>
                    <div class="value col-xs-7 col-sm-9">
                        <a href="#" ng-hide="loginDetailsVm.updatingApproval" id="changePassword"
                           onaftersave="loginDetailsVm.createChangeApprovalPassword()"
                           editable-password="loginDetailsVm.preference.jsonValue">
                            {{loginDetailsVm.preference.stringValue || updateApprovalPassword}}</a>
                    </div>
                </div>

                <div class="row" ng-if="!loginDetailsVm.login.external">
                    <div class="label col-xs-5 col-sm-3 text-right">
                        <span translate>PREFERRED_PAGE_TITLE</span> :
                    </div>
                    <div class="value col-xs-7 col-sm-9">
                        <button class="btn btn-sm btn-info"
                                ng-click="loginDetailsVm.resetPreferredPage()">
                            <span>{{'SET_DEFAULT_PAGE' | translate}}</span>
                        </button>
                    </div>
                </div>
            </div>
            <%--<div class="col-sm-4 col-sm-offset-1 activities-comments">
                <activity-stream object-type="LOGIN" object-id="loginDetailsVm.loginId"></activity-stream>
                <br><br>
                <comments object-type="LOGIN" object-id="loginDetailsVm.loginId"></comments>
            </div>--%>

        </div>
    </div>
</div>