<div class="cassini-logo text-center">
    <img src="app/assets/bower_components/cassini-platform/images/cassini-logo.png" alt="" height="100">
</div>

<form id="signinForm" onsubmit="return false;" role="form" style="margin:20px;">

    <div ng-if="showLicenceEnterForm">
        <div class="alert alert-danger" ng-show="hasLicenseError" ng-cloak>
            {{errorLicenseMessage }}
        </div>

        <p class="lead"><span translate>ENTER_LICENCE_KEY</span></p>

        <div class="form-group">

            <div>
            <textarea style="resize: none" rows="5" name="licenceKey" id="loginName"
                      class="form-control input-sm"
                      placeholder="Licence Key"
                      autocomplete="off" ng-model="updateLicenceVm.licenceKey"
                      ng-enter="updateLicenceVm.resetLicence()"></textarea>
            </div>
        </div>
        <br>

        <div class="form-group clearfix">
            <div class="pull-right" style="margin-top: -15px !important;">
                <button id="btnReset" class="btn btn-sm btn-default"
                        ng-click="updateLicenceVm.cancel()" translate>CANCEL
                </button>
                <button class="btn btn-sm btn-primary"
                        ng-click="updateLicenceVm.resetLicence()"><span translate>SUBMIT</span>
                </button>
            </div>
        </div>
    </div>

    <div ng-if="showGracePeriod">
        <div class="alert alert-danger" ng-show="gracePeriod > 0" ng-cloak>
            Your license has expired. You have {{gracePeriod}} days of grace period. Please renew your license.
        </div>
        <div class="alert alert-danger" ng-show="gracePeriod == 0" ng-cloak>
            Your license has expired. Today is last day for grace period. Please renew your license.
        </div>

        <br>

        <div class="form-group clearfix">
            <div class="pull-right" style="margin-top: -15px !important;">
                <button class="btn btn-sm btn-primary"
                        ng-click="updateLicenceVm.continueNextStep()"><span translate>CONTINUE</span>
                </button>
            </div>
        </div>
    </div>

</form>