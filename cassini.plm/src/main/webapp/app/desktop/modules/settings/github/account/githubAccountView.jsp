<div style="padding: 20px">
    <div>
        <form class="form-horizontal">
            <div class="form-group">
                <label class="col-sm-4 control-label">
                    <span translate>NAME</span>
                    <span class="asterisk">*</span>: </label>

                <div class="col-sm-7">
                    <input type="text" class="form-control input-sm" name="type"
                           ng-model="githubAccountVm.githubAccount.name">
                </div>
            </div>

            <div class="form-group">
                <label class="col-sm-4 control-label">
                    <span translate>DESCRIPTION</span>
                    <span class="asterisk">*</span>: </label>

                <div class="col-sm-7">
                    <textarea name="description" rows="3" class="form-control" style="resize: none"
                              ng-model="githubAccountVm.githubAccount.description"></textarea>
                </div>
            </div>

            <div class="form-group">
                <label class="col-sm-4 control-label">
                    <span>Authentication Type</span>
                    <span class="asterisk">*</span> : </label>

                <div class="col-sm-7" style="margin: 12px 0 0 0 !important;">
                    <div class="form-check"
                         style="border: 1px solid #ddd;padding:8px 8px 3px 8px;margin-top: -10px !important;border-radius: 3px;">
                        <label class="form-check-label" style="margin-right: 5px">
                            <input class="form-check-input" type="radio" name="authType"
                                   ng-model="githubAccountVm.githubAccount.authType" id="token" value="TOKEN">
                            <span style="padding: 2px;margin-left: 5px;">OAuth Token</span>
                        </label>
                        <label class="form-check-label" style="margin-right: 5px">
                            <input class="form-check-input" type="radio" name="authType"
                                   ng-model="githubAccountVm.githubAccount.authType" id="password" value="PASSWORD">
                            <span style="padding: 2px;margin-left: 5px;">Password</span>
                        </label>
                    </div>
                </div>
            </div>
            <div class="form-group" ng-if="githubAccountVm.githubAccount.authType === 'TOKEN'">
                <label class="col-sm-4 control-label">
                    <span>OAuth Token</span>
                    <span class="asterisk">*</span>: </label>

                <div class="col-sm-7">
                    <input type="text" class="form-control input-sm" name="type" ng-model="githubAccountVm.githubAccount.oauthToken">
                </div>
            </div>

            <div class="form-group" ng-if="githubAccountVm.githubAccount.authType === 'PASSWORD'">
                <label class="col-sm-4 control-label">
                    <span>User Name</span>
                    <span class="asterisk">*</span>: </label>

                <div class="col-sm-7">
                    <input type="text" class="form-control input-sm" name="type" ng-model="githubAccountVm.githubAccount.username">
                </div>
            </div>

            <div class="form-group" ng-if="githubAccountVm.githubAccount.authType === 'PASSWORD'">
                <label class="col-sm-4 control-label">
                    <span>Password</span>
                    <span class="asterisk">*</span>: </label>

                <div class="col-sm-7">
                    <input type="password" class="form-control input-sm" name="type" ng-model="githubAccountVm.githubAccount.password">
                </div>
            </div>

            <div class="form-group">
                <label class="col-sm-4 control-label">
                    <span>Organization</span>
                    <span class="asterisk">*</span>: </label>

                <div class="col-sm-7">
                    <input type="text" class="form-control input-sm" name="type"
                           ng-model="githubAccountVm.githubAccount.organization">
                </div>
            </div>
        </form>
    </div>
</div>