
<div class="cassini-logo text-center">
    <img src="app/assets/bower_components/cassini-platform/images/cassini-logo.png" alt="" height="100">
</div>

<form id="signinForm" onsubmit="return false;" role="form"
      style="margin:20px !important;/*max-height:300px !important;overflow-y:auto !important;overflow-x: hidden !important;*/">

    <p class="lead"><span translate>RESET_PASSWORD</span></p>

    <div class="form-group">
        <p>
            <span translate>LOGIN_RESET_MESSAGE</span>
        </p>

        <div>
            <i class="fa fa-user field-icon"></i>
            <input name="loginName" id="loginName" class="input-sm form-control form-field"
                   placeholder="{{'USERNAME@INSTANCE' | translate}}" ng-model="resetPwdVm.loginName" autofocus>
        </div>
    </div>

    <div class="form-group" ng-if="resetPwdVm.showPasscode">
        <p>
            <span translate>ONE_TIME_PASSCODE</span>
        </p>

        <div>
            <i class="fa fa-lock field-icon"></i>
            <input name="resetPwd" id="resetPwd" class="input-sm form-control form-field"
                   type="password"
                   ng-enter="resetPwdVm.verifyOtp()"
                   ng-disabled="resetPwdVm.processing"
                   placeholder="Passcode" ng-model="resetPwdVm.otp">
        </div>
    </div>

    <br>

    <div ng-show="resetPwdVm.processing" ng-cloak>
        <span translate>LOGIN_PROCESSING_MSG</span>

        <div class="progress progress-striped active">
            <div class="progress-bar"
                 role="progressbar" aria-valuenow="100" aria-valuemin="0"
                 aria-valuemax="100" style="width: 100%">
            </div>
        </div>
        <br>
    </div>

    <div class="form-group clearfix" style="margin-top: -6%;" ng-show="resetPwdVm.processing == false">
        <div ng-class="{'pull-right': !resetPwdVm.showPasscode, 'pull-left': resetPwdVm.showPasscode || resetPwdVm.resend}">
            <button id="btnReset" class="btn btn-sm btn-primary"
                    ng-disabled="resetPwdVm.processing"
                    ng-click="resetPwdVm.resetPassword()" translate>{{resetPwdVm.submitText}}
            </button>
        </div>
        <div class="pull-right">
            <button ng-disabled="resetPwdVm.processing"
                    class="btn btn-sm btn-default" style="margin-right: 10px"
                    ng-click="resetPwdVm.cancel()"><span translate>CANCEL</span>
            </button>

            <button id="btnSubmit"
                    ng-if="resetPwdVm.showPasscode || resetPwdVm.resend"
                    ng-disabled="resetPwdVm.processing"
                    class="btn btn-sm btn-success"
                    ng-click="resetPwdVm.verifyOtp()" translate>SUBMIT
            </button>
        </div>
    </div>


    <div style="color: red;overflow: auto;height: 100px;" ng-show="resetPwdVm.hasError" ng-cloak>
        {{ resetPwdVm.errorMessage }}
    </div>


</form>