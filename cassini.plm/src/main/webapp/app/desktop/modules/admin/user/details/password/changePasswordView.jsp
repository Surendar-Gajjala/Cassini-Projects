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

        .view-details .view-content a {
            color: #3699ff;
            font-size: 12px;
        }

        .view-details .view-content .reset-btn {
            margin-top: 50px;
            padding: 10px 20px;
            text-align: center;
        }

        .view-details .view-content .reset-message {
            color: #707d91;
            font-size: 13px;
            margin-top: 5px;
        }

        .view-details .view-content .show-password {
            color: var(--cassini-font-color);
            opacity: 0.7;
            float: right;
            z-index: 2;
            margin-top: -28px;
            position: relative;
            cursor: pointer;
            font-size: 16px;
            margin-right: 5px;
        }

    </style>

    <div class="view-details">
        <div class="view-header d-flex">
            <div style="flex: 1">
                <div class="view-title" translate>CHANGE_PASSWORD</div>
                <div class="view-summary" translate>CHANGE_USER_ACCOUNT_PASSWORD</div>
            </div>

            <div class="header-buttons">
                <button class="btn btn-sm btn-new"
                        ng-disabled="!changePasswordVm.userPassword.newPassword || !changePasswordVm.userPassword.confirmPassword"
                        ng-click="changePasswordVm.saveLoginPassword()"
                        ng-if="changePasswordVm.isLoginSameAsUser" translate>SAVE</button>
            </div>
        </div>
        <div class="view-content">
            <form class="form-horizontal" style="margin-top: 30px;"
                  ng-if="!changePasswordVm.loading && (changePasswordVm.isLoginSameAsUser || !loginPersonDetails.isSuperUser)">
                <div class="form-group">
                    <label class="col-sm-4 control-label" for="newPassword"><span translate>NEW_PASSWORD</span><span
                            class="asterisk"> *</span> : </label>

                    <div class="col-sm-6">
                        <input type="password" class="form-control" id="newPassword" name="password"
                               placeholder="New password" ng-model="changePasswordVm.userPassword.newPassword">
                          <span id="showPassword" class="fa fa-fw fa-eye-slash show-password"
                                ng-click="changePasswordVm.showPassword()"
                                title="{{'SHOW_PASSWORD' | translate}}"></span>
                        <div ng-show="!validPassword" class="alert alert-info">
                            <i class="fa fa-info mr10"></i>
                            <span ng-bind-html="passwordInformation"></span>
                        </div>
                    </div>
                </div>

                <div class="form-group">
                    <label class="col-sm-4 control-label" for="confirmPassword"><span translate>CONFIRM_NEW_PASSWORD</span><span
                            class="asterisk"> *</span> :</label>

                    <div class="col-sm-6">
                        <input type="password" class="form-control" id="confirmPassword"
                               placeholder="Confirm new password"
                               ng-model="changePasswordVm.userPassword.confirmPassword">
                          <span id="showConfirmPassword" class="fa fa-fw fa-eye-slash show-password"
                                title="{{'SHOW_PASSWORD' | translate}}"
                                ng-click="changePasswordVm.showConfirmPassword()"></span>
                    </div>
                </div>
            </form>
            <div style="text-align: center" ng-if="!changePasswordVm.loading && (!changePasswordVm.isLoginSameAsUser && loginPersonDetails.isSuperUser)">
                <div class="btn btn-light-primary reset-btn" ng-click="changePasswordVm.resetPassword()" translate>
                    RESET_PASSWORD
                </div>
                <div class="reset-message" translate>
                    EMAIL_SENDING_MESSAGE
                </div>
            </div>
        </div>
    </div>

</div>