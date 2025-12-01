<div style="position: relative;">
    <div style="overflow-y: auto; overflow-x: hidden; padding: 20px">
        <div class="row" style="margin: 0;">
            <div>
                <form>
                    <div class="form-group">
                        <label class="control-label" translate>NEW_PASSWORD</label><span>*</span> :
                        <input class="form-control" id="newPassword" name="newPassword" type="password"
                               ng-change="strengthPassword()"
                               ng-model="resetPasswordVm.resetPassword.newPassword">
                    <span id="showPassword" class="fa fa-fw fa-eye-slash" ng-click="resetPasswordVm.showPassword()" title="{{'SHOW_PASSWORD' | translate}}"
                          style="float: right;z-index: 2;margin-top: -28px;position: relative;cursor: pointer;font-size: 18px;margin-right: 5px;color: black;"></span>
                    </div>


                    <div class="form-group">
                        <label class="control-label" translate>CONFIRM_PASSWORD</label><span>*</span> :
                        <input class="form-control" id="confirmPassword" type="password"
                               ng-model="resetPasswordVm.resetPassword.confirmPassword"/>
                    <span id="showConfirmPassword" class="fa fa-fw fa-eye-slash" title="{{'SHOW_PASSWORD' | translate}}"
                          ng-click="resetPasswordVm.showConfirmPassword()"
                          style="float: right;z-index: 2;margin-top: -28px;position: relative;cursor: pointer;font-size: 18px;margin-right: 5px;color: black;"></span>
                    </div>
                </form>
            </div>
        </div>
    </div>
</div>
