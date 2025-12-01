<div class="view-container" fitcontent>
    <style scoped>
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

        .lovs-node {
            background: transparent url("app/assets/images/lovs1.png") no-repeat !important;
            height: 16px;
        }

        .properties-node {
            background: transparent url("app/assets/images/attributes.png") no-repeat !important;
            height: 16px;
        }

        .partTracking-node {
            background: transparent url("app/assets/images/partTracking.png") no-repeat !important;
            height: 16px;
        }

        .failurelist-node {
            background: transparent url("app/assets/images/failure.png") no-repeat !important;
            height: 16px;
        }

    </style>

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
                <div ng-if="settingsVm.selectedNode == 'AUTONUMBERS'">
                    <div><h3 class="" style="font-size: 22px;margin-left: 5px;">Auto Numbers</h3></div>
                    <hr>
                    <div onload="settingsVm.onLoad()"
                         ng-include="'app/desktop/modules/settings/autonumbers/autonumbersView.jsp'"
                         ng-controller='AutonumbersController as autoVm'>
                    </div>
                </div>
                <div ng-if="settingsVm.selectedNode == 'LOVS'">
                    <div><h3 class="" style="font-size: 22px;margin-left: 5px;">List of Values</h3></div>
                    <hr>
                    <div onload="settingsVm.onLoad()" ng-include="'app/desktop/modules/settings/lovs/lovsView.jsp'"
                         ng-controller='LovsController as lovsVm'>
                    </div>
                </div>
                <div ng-if="settingsVm.selectedNode == 'PROPERTIES'">
                    <div><h3 class="" style="font-size: 22px;margin-left: 5px;">System Attributes</h3></div>
                    <hr>
                    <div onload="settingsVm.onLoad()"
                         ng-include="'app/desktop/modules/settings/properties/propertiesView.jsp'"
                         ng-controller='PropertiesController as propsVm'>
                    </div>
                </div>
                <div ng-if="settingsVm.selectedNode == 'BOMGROUP'">
                    <div><h3 class="classificationlables" style="font-size: 22px;margin-left: 5px;">
                        BOM Group</h3></div>
                    <hr>
                    <div onload="settingsVm.onLoad()"
                         ng-include="'app/desktop/modules/settings/bomGroup/bomGroupView.jsp'"
                         ng-controller='BomGroupController as bomGroupVm'>
                    </div>
                </div>
                <div ng-if="settingsVm.selectedNode == 'PARTTRACKING'">
                    <div><h3 class="classificationlables" style="font-size: 22px;margin-left: 5px;">
                        Part Tracking</h3></div>
                    <hr>
                    <div onload="settingsVm.onLoad()"
                         ng-include="'app/desktop/modules/settings/partTracking/partTrackingView.jsp'"
                         ng-controller='PartTrackingController as listsVm'>
                    </div>
                </div>
                <div ng-if="settingsVm.selectedNode == 'FAILURELISTS'">
                    <div><h3 class="classificationlables" style="font-size: 22px;margin-left: 5px;" translate>
                        Failure List</h3></div>
                    <hr>
                    <div onload="settingsVm.onLoad()"
                         ng-include="'app/desktop/modules/settings/failurelist/failureListsView.jsp'"
                         ng-controller='FailureListsController as fListsVm'>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
