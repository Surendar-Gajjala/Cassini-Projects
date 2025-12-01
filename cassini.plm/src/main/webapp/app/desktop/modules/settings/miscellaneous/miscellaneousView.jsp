<div style="height: 100%;">
    <style scoped>
        .miscellaneous-left {
            width: 300px;
            border-right: 1px solid #eee;
            padding-right: 12px;
            padding-top: 10px;
        }

        .miscellaneous-right {
            width: calc(100% - 300px);
        }

        .miscellaneous {
            padding-left: 10px;
            cursor: pointer;
            height: 32px;
            line-height: 30px;
            border-bottom: 1px dotted #ddd;
        }

        .miscellaneous:hover {
            background-color: #d6e1e0 !important;
        }

        .selected-miscellaneous, .selected-miscellaneous:hover {
            color: white;
            background-color: #0081c2 !important;
        }

        tr.base-unit td {
            font-weight: bolder !important;
            color: #337ab7 !important;
        }
    </style>

    <div style="display: flex;height: 100%;">
        <div class="miscellaneous-left">
            <div class="miscellaneous"
                 ng-class="{'selected-miscellaneous': miscellaneousVm.selectedMiscellaneous == 'systemsettings'}"
                 ng-click="miscellaneousVm.selectMiscellaneous('systemsettings')">
                <span>System Settings</span>
            </div>
            <div class="miscellaneous"
                 ng-class="{'selected-miscellaneous': miscellaneousVm.selectedMiscellaneous == 'appsettings'}"
                 ng-click="miscellaneousVm.selectMiscellaneous('appsettings')">
                <span>Application Settings</span>
            </div>
            <div class="miscellaneous"
                 ng-class="{'selected-miscellaneous': miscellaneousVm.selectedMiscellaneous == 'cadsettings'}"
                 ng-click="miscellaneousVm.selectMiscellaneous('cadsettings')">
                <span>CAD Settings</span>
            </div>
        </div>
        <div class="miscellaneous-right">
            <div ng-if="miscellaneousVm.selectedMiscellaneous == 'systemsettings'" style="height: 100%;">
                <div style="height: 100%;" ng-include="'app/assets/bower_components/cassini-platform/app/desktop/modules/common/system/systemsettingsView.jsp'"
                     ng-controller='SystemsettingsController as systemVm'>
                </div>
            </div>
            <div ng-if="miscellaneousVm.selectedMiscellaneous == 'appsettings'" style="height: 100%;">
                <div style="height: 100%;" ng-include="'app/assets/bower_components/cassini-platform/app/desktop/modules/common/application/applicationsettingsView.jsp'"
                     ng-controller='ApplicationSettingsController as appSettingsVm'>
                </div>
            </div>
            <div ng-if="miscellaneousVm.selectedMiscellaneous == 'cadsettings'" style="height: 100%;">
                <div style="height: 100%;" ng-include="'app/desktop/modules/settings/miscellaneous/cadSettings/cadSettingsView.jsp'"
                     ng-controller='CadSettingsController as cadSettingsVm'>
                </div>
            </div>
        </div>
    </div>
</div>
