<div class="row">
    <div class="col-md-8 col-md-offset-2 styled-panel" style="margin-top: 20px; margin-bottom: 20px;">
        <h4 class="section-title text-center text-primary">Change Password</h4>

        <div class="alert alert-danger" ng-show="changePasswordVm.error.hasError" ng-cloak>
            {{ changePasswordVm.error.errorMessage }}
        </div>

        <div class="row">
            <div class="col-md-12">
                <div class="form-group">
                    <label class="control-label">Old Password</label>
                    <input class="form-control" type="password" ng-model="changePasswordVm.password.oldPassword"/>
                </div>
            </div>
        </div>

        <div class="row">
            <div class="col-md-12">
                <div class="form-group">
                    <label class="control-label">New Password</label>
                    <input class="form-control" type="password" ng-model="changePasswordVm.password.newPassword"/>
                </div>
            </div>
        </div>

        <div class="row">
            <div class="col-md-12">
                <div class="form-group">
                    <label class="control-label">Confirm Password</label>
                    <input class="form-control" type="password" ng-model="changePasswordVm.password.confirmPassword"/>
                </div>
            </div>
        </div>

        <hr/>
        <div class="text-center">
            <button class="btn btn-sm btn-default mr10" ng-click="changePasswordVm.cancelChangePassword()">Cancel</button>
            <button class="btn btn-sm btn-primary" ng-click="changePasswordVm.submitChangePassword()">Submit</button>
        </div>
    </div>
</div>