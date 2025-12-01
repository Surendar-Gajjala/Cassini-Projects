<div class="cassini-logo text-center">
    <img src="app/assets/bower_components/cassini-platform/images/cassini-logo.png" alt="" height="100">
</div>

<form id="signinForm" ng-submit="myFunc()" onsubmit="return false;" role="form" style="margin:20px;">

    <div class="alert alert-danger" ng-show="updateEmailVm.hasError" ng-cloak>
        {{updateEmailVm.errorMessage }}
    </div>
    <div class="alert alert-success" ng-show="updateEmailVm.hasSuccess" ng-cloak>
        {{updateEmailVm.successMessage }}
    </div>

    <p class="lead" ng-if="emailFormType == 'update'"><span translate>UPDATE_EMAIL</span></p>

    <p class="lead" ng-if="emailFormType == 'verify'"><span translate>VERIFY_EMAIL</span></p>

    <div class="form-group" ng-if="emailFormType == 'update'">

        <div>
            <i class="fa fa-envelope field-icon"></i>
            <input name="loginName" id="loginName" class="form-control input-sm form-field" placeholder="Email"
                   ng-model="updateEmailVm.email"
                   ng-change="updateEmailVm.emailChanged = true;updateEmailVm.submitText = submitTitle"
                   ng-enter="updateEmailVm.resetEmail()">
        </div>
    </div>

    <div class="form-group"
         ng-if="emailFormType == 'update' && personObject.email != null && personObject.email != '' && !updateEmailVm.emailChanged">
        <div>
            <i class="fa fa-lock field-icon"></i>
            <input name="verifyPasscode" id="verifyPasscode" class="form-control input-sm form-field"
                   placeholder="{{enterPasscodePlaceHolder}}"
                   ng-model="updateEmailVm.verifyPasscode" maxlength="6" valid-number
                   ng-enter="updateEmailVm.verifyEmail()">
        </div>
    </div>

    <div class="form-group" ng-if="emailFormType == 'verify'">
        <p>
            <span translate>PASSCODE_MESSAGE</span>
        </p>

        <div>
            <i class="fa fa-lock field-icon"></i>
            <input name="passcode" id="passcode" class="form-control input-sm form-field"
                   placeholder="{{enterPasscodePlaceHolder}}"
                   ng-model="updateEmailVm.verifyPasscode" maxlength="6" valid-number
                   ng-enter="updateEmailVm.verifyEmail()">
        </div>
    </div>

    <br>

    <div class="form-group clearfix">
        <div style="margin-top: -15px !important;"
             ng-if="emailFormType == 'verify' || (emailFormType == 'update' && !updateEmailVm.emailChanged && personObject.email != null && personObject.email != '')">
            <a href="" ng-click="resendVerificationCode()" style="cursor: pointer"
               ng-disabled="updateEmailVm.disableResend"
               translate>RESEND_CODE
            </a>
        </div>
        <div class="pull-right" style="margin-top: -33px !important;">
            <button id="btnReset" class="btn btn-sm btn-default"
                    ng-click="updateEmailVm.cancel()" translate>CANCEL
            </button>
            <button class="btn btn-sm btn-primary"
                    ng-disabled="(emailFormType == 'verify' && (updateEmailVm.verifyPasscode == null || updateEmailVm.verifyPasscode == ''))
                     || (emailFormType == 'update' && (updateEmailVm.email == null || updateEmailVm.email == ''))"
                    ng-click="updateEmailVm.resetEmail()">
                <span ng-if="emailFormType == 'update' && updateEmailVm.submitText == submitTitle">{{submitTitle}}</span>
                <span ng-if="emailFormType == 'update' && updateEmailVm.submitText == verifyTitle">{{verifyTitle}}</span>
                <span ng-if="emailFormType == 'verify'">{{verifyTitle}}</span>
            </button>
        </div>
    </div>

</form>