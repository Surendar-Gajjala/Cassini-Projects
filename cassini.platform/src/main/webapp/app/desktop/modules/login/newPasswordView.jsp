<div class="cassini-logo text-center">
    <img src="app/assets/bower_components/cassini-platform/images/cassini-logo.png" alt="" height="100">
</div>
<form id="signinForm" onsubmit="return false;" role="form" style="margin:20px;">

    <p class="lead"><span translate>NEW_PASSWORD</span></p>

    <div class="form-group">
        <p>
            <span translate>ENTER_PASSWORD</span>
        </p>

        <div>
            <i class="fa fa-lock field-icon"></i>
            <input name="password" id="newPwd" class="form-control input-sm form-field"
                   type="password" ng-change="passwordStrengthValid()"
                   placeholder="{{'NEW_PASSWORD' | translate}}" ng-model="newPwdVm.newPwd" autofocus>
            <span id="showPassword" class="fa fa-fw fa-eye-slash" ng-click="newPwdVm.showUserPassword()"
                  title="{{newPwdVm.title}}"
                  style="float: right;z-index: 2;margin-top: -28px;position: relative;cursor: pointer;
                              font-size: 18px;margin-right: 5px;color: black;"></span>
        </div>
    </div>


    <div class="form-group">
        <div>
            <i class="fa fa-lock field-icon"></i>
            <input name="verifyPwd" id="verifyPwd" class="form-control input-sm form-field"
                   type="password"
                   placeholder="{{newPwdVm.verifyPass}}" ng-model="newPwdVm.verifyPwd" autofocus>
        </div>
        <%--<span title="{{'PASSWORD_INFORMATION' | translate}}" style="position: absolute;right: 55px;bottom: 162px;z-index: 999;width: 20px;text-align: center;">
            <i class="fa fa-info-circle" style="position: absolute;top: -4px;left: 10px;"></i>
        </span>--%>
    </div>

    <br>

    <div ng-show="newPwdVm.processing" ng-cloak>
        <span translate>LOGIN_PROCESSING_MSG</span>

        <div class="progress progress-striped active">
            <div class="progress-bar"
                 role="progressbar" aria-valuenow="100" aria-valuemin="0"
                 aria-valuemax="100" style="width: 100%">
            </div>
        </div>
        <br>
    </div>

    <div class="form-group clearfix">
        <div class="pull-right">
            <%-- <button ng-disabled="newPwdVm.processing"
                     class="btn btn-sm btn-primary" style="margin-right: 10px"
                     ng-click="newPwdVm.cancel()" translate>CANCEL
             </button>--%>

            <button id="btnSubmit"
                    ng-disabled="newPwdVm.processing"
                    class="btn btn-sm btn-success"
                    ng-click="newPwdVm.setNewPassword()" translate>SUBMIT
            </button>
        </div>
    </div>
    <%--

        <div>
            <div class="alert alert-info" style="margin: 0px;">
                <i class="fa fa-info mr10"></i>
                <span ng-bind-html="passwordInformation"></span>
            </div>
        </div>
    --%>

    <div style="color: red;overflow: auto;position: absolute;" ng-show="newPwdVm.error != null" ng-cloak>
        <span ng-bind-html="newPwdVm.error"></span>
    </div>
</form>