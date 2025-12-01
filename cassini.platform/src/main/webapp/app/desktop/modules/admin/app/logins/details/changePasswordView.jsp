<style>
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
</style>

<h3 class="section-title text-center text-primary" style="padding: 20px" translate>CHANGE_PASSWORD</h3>

<div class="row">
    <div class="col-md-8 col-md-offset-2 styled-panel" style="margin-top: 20px; margin-bottom: 20px;">
        <div class="alert alert-warning">
            <i class="fa fa-warning mr10"></i><span translate>CHANGE_PASSWORD_TITLE</span>
        </div>

        <div class="alert alert-danger" ng-show="chPassVm.error.hasError" ng-cloak>
            {{ chPassVm.error.errorMessage }}
            <span class="pull-right" style="cursor: pointer;" ng-click="chPassVm.cross()">X</span>
        </div>

        <div class="row">
            <div class="col-md-12">
                <div class="form-group">
                    <label class="control-label" translate>OLD_PASSWORD</label> :
                    <input class="form-control" type="password" ng-model="chPassVm.password.oldPassword"/>
                </div>
            </div>
        </div>

        <div class="row">
            <div class="col-md-12">
                <div class="form-group">
                    <label class="control-label" translate>NEW_PASSWORD</label> :
                    <input class="form-control" id="newPassword" name="password" type="password"
                           ng-change="passwordStrengthValid()"
                           ng-model="chPassVm.password.newPassword">
                    <span id="showPassword" class="fa fa-fw fa-eye-slash" ng-click="chPassVm.showPassword()"
                          title="{{chPassVm.passwordTitle}}"
                          style="float: right;z-index: 2;margin-top: -28px;position: relative;cursor: pointer;font-size: 18px;margin-right: 5px;color: black;"></span>
                    <meter max="4" id="password-strength-meter"></meter>
                </div>
            </div>
        </div>


        <div ng-if="chPassVm.passwordSafeCharacters == true" class="alert alert-danger">
            <span ng-bind-html="chPassVm.invalidPassword" <%--ng-bind-html="chPassVm.invalidPassword"--%>></span>
        </div>


        <div ng-show="chPassVm.password.newPassword != null && chPassVm.password.newPassword != ''"
             class="alert alert-info">
            <i class="fa fa-info mr10"></i>
            <span ng-bind-html="passwordInformation"></span>
        </div>

        <div class="row">
            <div class="col-md-12">
                <div class="form-group">
                    <label class="control-label" translate>CONFIRM_PASSWORD</label> :
                    <input class="form-control" id="confirmPassword" type="password"
                           ng-model="chPassVm.password.confirmPassword"/>
                    <span id="showConfirmPassword" class="fa fa-fw fa-eye-slash"
                          ng-click="chPassVm.showConfirmPassword()"
                          title="{{chPassVm.confirmPasswordTitle}}"
                          style="float: right;z-index: 2;margin-top: -28px;position: relative;cursor: pointer;font-size: 18px;margin-right: 5px;color: black;"></span>
                </div>
            </div>
        </div>
    </div>

</div>

<hr/>
<div class="text-center" style="margin-bottom: 20px; padding-bottom: 20px;">
    <button class="btn btn-sm btn-default mr10" ng-click="chPassVm.cancelChangePassword()" translate>CANCEL</button>
    <button class="btn btn-sm btn-primary" ng-click="chPassVm.submitChangePassword()" translate>SUBMIT</button>
</div>