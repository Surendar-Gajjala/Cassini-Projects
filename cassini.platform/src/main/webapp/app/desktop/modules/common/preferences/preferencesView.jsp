<style>
    .stickyheader {
        height: 473px !important;
        overflow: auto !important;
    }

    .tab-content {
        padding: 0px !important;
    }

    .tab-content .tab-pane {
        overflow-y: auto !important;
    }

    .tab-pane {
        position: relative;
    }

    .preferences-view .nav-tabs li {
        cursor: pointer;
        border: 0 !important;
        min-width: 150px;
        text-align: center
    }

    .preferences-view .nav-tabs li:hover {
        background-color: inherit !important;
    }
</style>
<div class="row row-eq-height" style="margin: 0;">
    <div class="col-sm-12" ng-if="isAdminGroup">
        <div class="preferences-view">
            <uib-tabset active="prefsVm.active">
                <uib-tab heading="System" active="prefsVm.tabs.system.active"
                         select="prefsVm.itemDetailsTabActivated(prefsVm.tabs.system.id)"
                         style="">
                    <div ng-include="prefsVm.tabs.system.template"
                         ng-controller="SystemsettingsController as systemVm"></div>
                </uib-tab>

                <uib-tab heading="Application"
                         active="prefsVm.tabs.application.active"
                         select="prefsVm.itemDetailsTabActivated(prefsVm.tabs.application.id)"
                         style="">
                    <div ng-include="prefsVm.tabs.application.template"
                         ng-controller="ApplicationSettingsController as appSettingsVm"></div>
                </uib-tab>
            </uib-tabset>
        </div>
    </div>
    <div class="col-sm-12 stickyheader" ng-if="!isAdminGroup">
        <div class="preferences-view">
            <uib-tabset active="prefsVm.active">
                <uib-tab heading="User Settings" active="prefsVm.tabs.system.active"
                         select="prefsVm.itemDetailsTabActivated(prefsVm.tabs.system.id)"
                         style="">
                    <div ng-include="prefsVm.tabs.system.template"
                         ng-controller="SystemsettingsController as systemVm"></div>
                </uib-tab>
            </uib-tabset>
        </div>
    </div>
</div>

