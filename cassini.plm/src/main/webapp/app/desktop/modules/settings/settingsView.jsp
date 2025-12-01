<div class="view-container" fitcontent>

    <style scoped>
        #settingsPane {
            overflow: hidden !important;
        }

        .settings-pane .tree-node .tree-title {
            padding: 0 0 0 5px;
        }

        .settings-node {
            background: transparent url("app/assets/images/settings1.png") no-repeat !important;
            height: 16px;
        }

        .autonumbers-node {
            background: transparent url("app/assets/images/autonumbers.png") no-repeat !important;
            height: 16px;
        }

        .lifecycles-node {
            background: transparent url("app/assets/images/lifecycles.png") no-repeat !important;
            height: 16px;
        }

        .lovs-node {
            background: transparent url("app/assets/images/lovs1.png") no-repeat !important;
            height: 16px;
        }

        .properties-node {
            background: transparent url("app/assets/images/attributes.png") no-repeat !important;
            height: 16px;
        }

        .preferences-node {
            background: transparent url("app/assets/images/preferences.png") no-repeat !important;
            height: 16px;
        }

        .relationships-node {
            background: transparent url("app/assets/images/prefs1.png") no-repeat !important;
            height: 16px;
        }

        .templates-node {
            background: url("app/assets/images/email-templates-icon.png") no-repeat !important;
            height: 16px;
        }

        .plugins-node {
            background: url("app/assets/images/plugin.png") no-repeat !important;
            height: 16px;
        }

        .miscellaneous-node {
            background: url("app/assets/images/cog-gears.png") no-repeat !important;
            height: 16px;
        }

        .measurements-node {
            background: url("app/assets/images/measure.png") no-repeat !important;
            height: 16px;
        }

        .github-node {
            background: url("app/assets/images/github.png") no-repeat !important;
            height: 16px;
        }

        .profiles-node {
            background: transparent url("app/assets/bower_components/cassini-platform/images/usergroups.png") no-repeat !important;
            height: 16px;
        }

        .settings-header {
            font-size: 22px;
            margin: 0;
            display: inline;
            padding-right: 10px;
            border-right: 1px solid #eee;
        }

        .new-button {
            margin-left: 10px !important;
            background-color: #0060df !important;
            min-width: 100px;
            color: #fff;
            font-size: 14px !important;
            padding: 1px 10px 3px 5px !important;
            border-radius: 3px;
            height: 28px !important;
            text-align: left;
            font-weight: normal !important;
            display: inline;
            margin-top: -5px;
        }

        .new-button .la-plus {
            font-size: 16px;
        }

        .new-button:hover {
            background-color: #003eaa !important;
            color: #fff;
        }
    </style>

    <div class="view-content  no-padding" style="overflow-y: hidden;padding: 10px;">
        <div class="split-pane fixed-left">
            <div class="split-pane-component split-left-pane" style="width: 300px;">
                <div id="classificationContainer" class="classification-pane settings-pane">
                    <ul id="settingsTree" class="easyui-tree">
                    </ul>
                </div>
            </div>
            <div class="split-pane-divider"></div>
            <div id="settingsPane" class="split-pane-component split-right-pane">
                <div style="padding: 20px; font-size: 18px;color: lightgrey;"
                     ng-if="settingsVm.selectedNode == null || settingsVm.selectedNode == 'ROOT'">
                    <span translate>SELECT_SETTING_ON_THE_LEFT</span>
                </div>
                <div ng-if="settingsVm.selectedNode == 'AUTONUMBERS'">
                    <h3 class="settings-header" style="" translate>AUTO_NUMBERS</h3>
                    <button class="btn btn-sm new-button" ng-click="settingsVm.newAutonumber()"
                            ng-if="hasPermission('settings','create')"
                            title="{{'NEW_AUTO_NUMBER' | translate}}">
                        <i class="las la-plus" aria-hidden="true"></i>
                        <span>New Autonumber</span>
                    </button>
                    <hr style="margin-top: 10px;">
                    <div onload="settingsVm.onLoad()"
                         ng-include="'app/assets/bower_components/cassini-platform/app/desktop/modules/common/autonumber/autonumbersView.jsp'"
                         ng-controller='AutonumbersController as autoVm'>
                    </div>
                </div>
                <div ng-if="settingsVm.selectedNode == 'LIFECYCLES'">
                    <h3 class="settings-header" style="" translate>LIFE_CYCLES</h3>
                    <button class="btn btn-sm new-button" ng-click="settingsVm.newLifecycle()"
                            ng-if="hasPermission('settings','create')"
                            title="{{'NEW_LIFE_CYCLE' | translate}}">
                        <i class="las la-plus" aria-hidden="true"></i>
                        <span>New Lifecycle</span>
                    </button>
                    <hr style="margin-top: 10px;">
                    <div onload="settingsVm.onLoad()"
                         ng-include="'app/desktop/modules/settings/lifecycles/lifecyclesView.jsp'"
                         ng-controller='LifecyclesController as lcsVm'>
                    </div>
                </div>
                <div ng-if="settingsVm.selectedNode == 'LOVS'">
                    <h3 class="settings-header" style="" translate>LIST_OF_VALUES</h3>
                    <button class="btn btn-sm new-button" ng-click="settingsVm.newLov()"
                            ng-if="hasPermission('settings','create')"
                            title="{{'NEW_LOV' | translate}}">
                        <i class="las la-plus" aria-hidden="true"></i>
                        <span>New LOV</span>
                    </button>
                    <hr style="margin-top: 10px;">
                    <div onload="settingsVm.onLoad()" ng-include="'app/desktop/modules/settings/lovs/lovsView.jsp'"
                         ng-controller='LovsController as lovsVm'>
                    </div>
                </div>
                <div ng-if="settingsVm.selectedNode == 'PREFERENCES'">
                    <div><h3 class="" style="font-size: 22px;margin: 0;" translate>PREFERENCES</h3></div>
                    <hr style="margin-top: 10px;">
                    <div onload="settingsVm.onLoad()"
                         ng-include="'app/assets/bower_components/cassini-platform/app/desktop/modules/common/preferences/preferencesView.jsp'"
                         ng-controller='PreferencesController as prefsVm'>
                    </div>
                </div>
                <div ng-if="settingsVm.selectedNode == 'RELATIONSHIPS'">
                    <h3 class="settings-header" style="" translate>RELATIONSHIPS</h3>
                    <button class="btn btn-sm new-button" ng-click="settingsVm.newRelationship()"
                            ng-if="hasPermission('settings','create')"
                            title="{{'NEW_RELATIONSHIP' | translate}}">
                        <i class="las la-plus" aria-hidden="true"></i>
                        <span>New Relationship</span>
                    </button>

                    <hr style="margin-top: 10px;">
                    <div onload="settingsVm.onLoad()"
                         ng-include="'app/desktop/modules/settings/relationships/relationshipsView.jsp'"
                         ng-controller='RelationshipsController as relationshipVm'>
                    </div>
                </div>

                <div ng-if="settingsVm.selectedNode == 'EMAIL_TEMPLATES'">
                    <div><h3 class="" style="font-size: 22px;margin: 0;" translate>EMAIL_TEMPLATES</h3></div>
                    <hr style="margin-top: 10px;margin-bottom: 0;">
                    <div onload="settingsVm.onLoad()"
                         ng-include="'app/desktop/modules/settings/templates/templatesView.jsp'"
                         ng-controller='TemplatesController as tempsVm'>
                    </div>
                </div>

                <div ng-if="settingsVm.selectedNode == 'GITHUB'" style="height: 100%;">
                    <div><h3 class="" style="font-size: 22px;margin: 0;" translate>GitHub</h3></div>
                    <hr style="margin-top: 10px;margin-bottom: 0;">
                    <div onload="settingsVm.onLoad()" style="height: calc(100% - 35px);overflow: hidden"
                         ng-include="'app/desktop/modules/settings/github/githubView.jsp'"
                         ng-controller='GitHubController as githubVm'>
                    </div>
                </div>

                <div ng-if="settingsVm.selectedNode == 'PLUGINS'">
                    <div><h3 class="" style="font-size: 22px;margin: 0;" translate>Plugins</h3></div>
                    <hr style="margin-top: 10px;">
                    <div onload="settingsVm.onLoad()"
                         ng-include="'app/desktop/modules/settings/plugins/pluginsView.jsp'"
                         ng-controller='PluginsController as pluginsVm'>
                    </div>
                </div>
                <div ng-if="settingsVm.selectedNode == 'MEASUREMENTS'" style="height: 100%;">
                    <div><h3 class="" style="font-size: 22px;margin: 0;" translate>Quantities of Measurement</h3></div>
                    <hr style="margin-top: 10px;margin-bottom: 0;">
                    <div onload="settingsVm.onLoad()" style="height: calc(100% - 35px);"
                         ng-include="'app/desktop/modules/settings/measurement/measurementsView.jsp'"
                         ng-controller='MeasurementsController as measurementsVm'>
                    </div>
                </div>
                <div ng-if="settingsVm.selectedNode == 'MISCELLANEOUS'" style="height: 100%;">
                    <div><h3 class="" style="font-size: 22px;margin: 0;" translate>MISCELLANEOUS</h3></div>
                    <hr style="margin-top: 10px;margin-bottom: 0;">
                    <div onload="settingsVm.onLoad()" style="height: calc(100% - 35px);"
                         ng-include="'app/desktop/modules/settings/miscellaneous/miscellaneousView.jsp'"
                         ng-controller='MiscellaneousController as miscellaneousVm'>
                    </div>
                </div>
                <div ng-if="settingsVm.selectedNode == 'PROFILES'" style="height: 100%;">
                    <div><h3 class="" style="font-size: 22px;margin: 0;" translate>PROFILES</h3></div>
                    <hr style="margin-top: 10px;margin-bottom: 0;">
                    <div onload="settingsVm.onLoad()" style="height: calc(100% - 35px);"
                         ng-include="'app/desktop/modules/settings/profiles/profilesView.jsp'"
                         ng-controller='ProfilesController as profilesVm'>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
