<div class="cassini-logo text-center">
    <img src="app/assets/bower_components/cassini-platform/images/cassini-logo.png" alt="" height="100">
</div>

<form id="signinForm" ng-submit="myFunc()" onsubmit="return false;" role="form" style="margin:20px;">

    <div class="alert alert-danger" ng-show="twoFactorAuthenticationVm.hasError" ng-cloak>
        {{twoFactorAuthenticationVm.errorMessage }}
    </div>
    <div class="alert alert-success" ng-show="twoFactorAuthenticationVm.hasSuccess" ng-cloak>
        {{twoFactorAuthenticationVm.successMessage }}
    </div>

    <p class="lead"><span translate>TWO_FACTOR_AUTHENTICATION</span></p>

    <div class="form-group">
        <p>
            <span translate>PASSCODE_MESSAGE</span>
        </p>

        <div>
            <i class="fa fa-lock field-icon"></i>
            <input name="passcode" id="passcode" class="form-control input-sm form-field"
                   placeholder="{{enterPasscodePlaceHolder}}" autocomplete="off"
                   ng-model="twoFactorAuthenticationVm.passcode" maxlength="6" valid-number
                   ng-enter="twoFactorAuthenticationVm.checkTwoFactorAuthentication()">
        </div>
    </div>

    <br>

    <div class="form-group clearfix">
        <div style="margin-top: -15px !important;">
            <a href="" ng-click="resetTwoFactorAuthentication()" style="cursor: pointer"
               ng-disabled="twoFactorAuthenticationVm.disableResend"
               translate>RESEND_CODE
            </a>
        </div>

        <div class="pull-right" style="margin-top: -33px !important;">
            <button id="btnReset" class="btn btn-sm btn-default"
                    ng-click="twoFactorAuthenticationVm.cancel()" translate>CANCEL
            </button>
            <button class="btn btn-sm btn-success"
                    ng-disabled="twoFactorAuthenticationVm.passcode == null || twoFactorAuthenticationVm.passcode == ''"
                    ng-click="twoFactorAuthenticationVm.checkTwoFactorAuthentication()"><span translate>VERIFY</span>
            </button>
        </div>
    </div>

</form>