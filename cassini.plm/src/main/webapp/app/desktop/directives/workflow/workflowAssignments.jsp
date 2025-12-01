<div>
    <style scoped>
        .workflow-assignments-panel {
            position: absolute;
            left: 10px;
            right: 10px;
            bottom: 0;
            overflow-y: auto;
            top: 10px;
            border-top: 1px solid #ddd;
        }
    </style>
    <div class="workflow-assignments-panel" ng-if="selectedStatus != null">
        <div class="row row-eq-height" style="margin: 0;">
            <div class="col-sm-12" style="padding: 0;">
                <div class="workflow-tabs">
                    <uib-tabset active="activeTab">
                        <uib-tab heading="Sign Off"
                                 active="activityTabs.persons.active"
                                 select="selectWorkflowActivityTab(activityTabs.persons.id)">
                            <div ng-include="activityTabs.persons.template"></div>
                        </uib-tab>
                        <uib-tab heading="Form Data"
                                 active="activityTabs.formData.active"
                                 select="selectWorkflowActivityTab(activityTabs.formData.id)">
                            <div ng-include="activityTabs.formData.template"></div>
                        </uib-tab>
                    </uib-tabset>
                </div>
            </div>
        </div>
    </div>


    <div ng-if="selectedStatus == null && workflow != null">
        <div class="row row-eq-height" style="margin: 0;">
            <div class="col-sm-12" style="padding: 0;">
                <div class="workflow-tabs">
                    <uib-tabset active="active">
                        <uib-tab heading="History"
                                 active="tabs.history.active"
                                 select="selectWorkflowTab(tabs.history.id)">
                            <div ng-include="tabs.history.template"></div>
                        </uib-tab>
                        <uib-tab heading="Attributes"
                                 active="tabs.attributes.active"
                                 select="selectWorkflowTab(tabs.attributes.id)">
                            <div ng-include="tabs.attributes.template"></div>
                        </uib-tab>
                        <uib-tab heading="Events"
                                 active="tabs.events.active"
                                 select="selectWorkflowTab(tabs.events.id)">
                            <div ng-include="tabs.events.template"></div>
                        </uib-tab>
                    </uib-tabset>
                </div>
            </div>
        </div>
    </div>
</div>