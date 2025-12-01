<div class="view-container" applicationfitcontent>
    <style scoped>
        .settings-pane .tree-node .tree-title {
            padding: 0 0 0 5px;
        }

        .group-root {
            background: transparent url("app/assets/images/class.png") no-repeat !important;
        }

        .users-node {
            background: transparent url("app/assets/bower_components/cassini-platform/images/users.png") no-repeat !important;
            height: 16px;
        }

        .settings-node {
            background: transparent url("app/assets/images/settings1.png") no-repeat !important;
            height: 16px;
        }

        .autonumbers-node {
            background: transparent url("app/assets/images/autonumbers.png") no-repeat !important;
            height: 16px;
        }

        .lovs-node {
            background: transparent url("app/assets/images/lovs1.png") no-repeat !important;
            height: 16px;
        }

    </style>
    <%--<div class="view-toolbar">
        <div style="margin-top: 15px;margin-left: 15px;color: #002451;font-weight: bold;font-size: 20px;">
            <span>Settings</span>
        </div>
    </div>--%>
    <div class="view-content  no-padding" style="overflow-y: hidden;padding: 10px;">
        <div class="split-pane fixed-left">
            <div class="split-pane-component split-left-pane">
                <div id="classificationContainer" class="classification-pane settings-pane">
                    <ul id="settingsTree" class="easyui-tree">
                    </ul>
                </div>
            </div>
            <div class="split-pane-divider"></div>
            <div id="settingsPane" class="split-pane-component split-right-pane">
                <div style="padding: 20px; font-size: 18px;color: lightgrey;"
                     ng-if="settingsVm.selectedNode == null || settingsVm.selectedNode == 'ROOT'">
                    <span>Select a setting on the left</span>
                </div>
                <div ng-if="settingsVm.selectedNode == 'LOVS'">
                    <div><h3 class="" style="font-size: 22px;margin-left: 5px;">Lovs</h3></div>
                    <hr>
                    <div onload="settingsVm.onLoad()"
                         ng-include="'app/desktop/modules/settings/lovs/lovsView.jsp'"
                         ng-controller='LovsController as lovsVm'>
                    </div>
                </div>
                <div ng-if="settingsVm.selectedNode == 'GROUPLOCATIONS'">
                    <div><h3 class="" style="font-size: 22px;margin-left: 5px;">Group Locations</h3></div>

                    <hr>
                    <div onload="settingsVm.onLoad()"
                         ng-include="'app/desktop/modules/settings/groupLocations/groupLocationsView.jsp'"
                         ng-controller='GroupLocationsController as groupVm'>
                    </div>
                </div>
                <div ng-if="settingsVm.selectedNode == 'LOCATIONUTILITIES'">
                    <div><h3 class="classificationlables" style="font-size: 22px;margin-left: 5px;" translate>
                        Location/Utility</h3></div>
                    <hr>
                    <div onload="settingsVm.onLoad()" id="locationUtilitiesId"
                         ng-include="'app/desktop/modules/settings/utilityLocations/utilityLocationsView.jsp'"
                         ng-controller='UtilityLocationController as ulsVm'>
                    </div>
                </div>
                <div ng-if="settingsVm.selectedNode == 'RESPONDERUTILITIES'">
                    <div><h3 class="classificationlables" style="font-size: 22px;margin-left: 5px;" translate>
                        Responder/Utility</h3></div>
                    <hr>
                    <div onload="settingsVm.onLoad()" id="responderUtilitiesId"
                         ng-include="'app/desktop/modules/settings/responderUtilities/responderUtilitiesView.jsp'"
                         ng-controller='ResponderUtilitiesController as pusVm'>
                    </div>
                </div>
                <div ng-if="settingsVm.selectedNode == 'ASSISTORUTILITIES'">
                    <div><h3 class="classificationlables" style="font-size: 22px;margin-left: 5px;" translate>
                        Assistor/Utility</h3></div>
                    <hr>
                    <div onload="settingsVm.onLoad()" id="assistorUtilitiesId"
                         ng-include="'app/desktop/modules/settings/assistorUtilities/assistorUtilitiesView.jsp'"
                         ng-controller='AssistorUtilitiesController as pusVm'>
                    </div>
                </div>
                <div ng-if="settingsVm.selectedNode == 'FACILITATORUTILITIES'">
                    <div><h3 class="classificationlables" style="font-size: 22px;margin-left: 5px;" translate>
                        Facilitator/Utility</h3></div>
                    <hr>
                    <div onload="settingsVm.onLoad()" id="facilitatorUtilitiesId"
                         ng-include="'app/desktop/modules/settings/facilitatorUtilities/facilitatorUtilitiesView.jsp'"
                         ng-controller='FacilitatorUtilitiesController as pusVm'>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
