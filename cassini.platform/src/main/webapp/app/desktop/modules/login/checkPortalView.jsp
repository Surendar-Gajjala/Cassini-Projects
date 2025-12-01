
<div class="cassini-logo text-center">
    <img src="app/assets/bower_components/cassini-platform/images/cassini-logo.png" alt="" height="130">
</div>

<form id="signinForm" onsubmit="return false;" role="form" style="margin:0;">
    <p class="lead" style="margin-bottom: 10px;">Cassini Portal Info</p>
    <p ng-if="checkPortalVm.errorMessage != null" style="color: #ff1559;">{{checkPortalVm.errorMessage}}</p>
    <div class="form-group">
        <div class="input-group input-group-in">
            <span class="input-group-addon"><i class="fa fa-envelope"></i></span>
            <input type="email" name="email" id="email" class="form-control" placeholder="Email"
                   ng-model="checkPortalVm.portalAccount.email">
        </div>
        <br>
        <div class="input-group input-group-in">
            <span class="input-group-addon"><i class="fa fa-key"></i></span>
            <input type="password" name="password" id="password" class="form-control" placeholder="Password"
                   ng-model="checkPortalVm.portalAccount.password">
        </div>
        <br>
        <div class="input-group input-group-in">
            <span class="input-group-addon"><i class="fa fa-gear"></i></span>
            <input name="authKey" id="authkey" class="form-control" placeholder="Auth Key"
                   ng-model="checkPortalVm.portalAccount.authKey">
        </div>
    </div>
    <br>
    <div class="form-group clearfix">
        <div class="pull-right" style="margin-top: -15px !important;">
            <button id="btnReset" class="btn btn-sm btn-default"
                    ng-click="checkPortalVm.cancel()" translate>CANCEL
            </button>
            <button class="btn btn-sm btn-primary" style="margin-left: 10px;"
                    ng-click="checkPortalVm.setupPortalAccount()"><span translate>SUBMIT</span>
            </button>
        </div>
    </div>

</form>