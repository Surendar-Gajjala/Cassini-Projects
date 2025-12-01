<style>
    .tab-content {
        padding: 0px !important;
    }

    .tab-content .tab-pane {
        overflow-y: auto !important;
    }

    .tab-pane {
        position: relative;
    }

    .itemtype-view .nav-tabs li {
        cursor: pointer;
        border: 0 !important;
        min-width: 100px;
        text-align: center
    }

    /*
        .itemtype-view .nav-tabs li:hover {
            background-color: inherit !important;
        }*/
</style>
<div class="view-container" fitcontent>
    <div class="view-content no-padding" style="overflow-y: hidden;">
        <div class="row row-eq-height" style="margin: 0;">
            <div class="col-sm-12">
                <div class="itemtype-view" ng-if="selectedWfType != null && selectedWfType != undefined">
                    <uib-tabset active="workflowTypesVm.active">
                        <uib-tab heading="{{'DETAILS_TAB_BASIC' | translate}}"
                                 active="workflowTypesVm.tabs.basic.active"
                                 select="workflowTypesVm.workflowDetailsTabActivated(workflowTypesVm.tabs.basic.id)"
                                 style="">
                            <div ng-include="workflowTypesVm.tabs.basic.template"
                                 ng-controller="WorkflowTypeBasicController as workflowTypeBasicVm"></div>
                        </uib-tab>

                        <uib-tab heading="Attributes"
                                 active="workflowTypesVm.tabs.attributes.active"
                                 select="workflowTypesVm.workflowDetailsTabActivated(workflowTypesVm.tabs.attributes.id)"
                                 style="">
                            <div ng-include="workflowTypesVm.tabs.attributes.template"
                                 ng-controller="WorkflowTypeAttributeController as workflowTypeAttributeVm"></div>
                        </uib-tab>

                        <uib-tab heading="{{'TIMELINE' | translate}}"
                                 active="workflowTypesVm.tabs.history.active"
                                 select="workflowTypesVm.workflowDetailsTabActivated(workflowTypesVm.tabs.history.id)"
                                 style="">
                            <div ng-include="workflowTypesVm.tabs.history.template"
                                 ng-controller="WorkflowTypeHistoryController as workflowTypeHistoryVm"></div>
                        </uib-tab>
                    </uib-tabset>
                </div>
            </div>
        </div>
    </div>
</div>