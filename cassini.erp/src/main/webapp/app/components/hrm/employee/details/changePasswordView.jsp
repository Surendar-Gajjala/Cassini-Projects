<div class="row">
    <div class="col-md-4 col-md-offset-4 styled-panel" style="margin-top: 20px; margin-bottom: 20px;">
        <h4 class="section-title text-center text-primary">Change Password</h4>

        <div class="alert alert-warning">
            <i class="fa fa-warning mr10"></i>You will be required to relogin!
        </div>

        <div class="alert alert-danger" ng-show="error.hasError" ng-cloak>
            {{ error.errorMessage }}
        </div>

        <div class="row">
            <div class="col-md-12">
                <div class="form-group">
                    <label class="control-label">Old Password</label>
                    <input class="form-control" type="password" ng-model="password.oldPassword"/>
                </div>
            </div>
        </div>

        <div class="row">
            <div class="col-md-12">
                <div class="form-group">
                    <label class="control-label">New Password</label>
                    <input class="form-control" type="password" ng-model="password.newPassword"/>
                </div>
            </div>
        </div>

        <div class="row">
            <div class="col-md-12">
                <div class="form-group">
                    <label class="control-label">Confirm Password</label>
                    <input class="form-control" type="password" ng-model="password.confirmPassword"/>
                </div>
            </div>
        </div>

        <hr/>
        <div class="text-center">
            <button class="btn btn-sm btn-default mr10" ng-click="cancelChangePassword()">Cancel</button>
            <button class="btn btn-sm btn-primary" ng-click="submitChangePassword()">Submit</button>
        </div>
    </div>
</div>