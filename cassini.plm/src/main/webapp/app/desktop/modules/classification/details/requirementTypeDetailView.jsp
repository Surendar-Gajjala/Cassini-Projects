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
                <div class="itemtype-view" ng-hide="type == null || type == undefined">
                    <uib-tabset active="reqTypesVm.active">
                        <uib-tab heading="{{'DETAILS_TAB_BASIC' | translate}}"
                                 active="reqTypesVm.tabs.basic.active"
                                 select="reqTypesVm.requirementDetailsTabActivated(reqTypesVm.tabs.basic.id)"
                                 style="">
                            <div ng-include="reqTypesVm.tabs.basic.template"
                                 ng-controller="RequirementTypeBasicController as reqTypeBasicVm"></div>
                        </uib-tab>

                        <uib-tab heading="{{'ATTRIBUTES' | translate}}"
                                 active="reqTypesVm.tabs.attributes.active"
                                 select="reqTypesVm.requirementDetailsTabActivated(reqTypesVm.tabs.attributes.id)"
                                 style="">
                            <div ng-include="reqTypesVm.tabs.attributes.template"
                                 ng-controller="RequirementTypeAttributeController as reqTypeAtributesVm"></div>
                        </uib-tab>

                        <uib-tab heading="{{'TIMELINE' | translate}}"
                                 active="reqTypesVm.tabs.history.active"
                                 select="reqTypesVm.requirementDetailsTabActivated(reqTypesVm.tabs.history.id)"
                                 style="">
                            <div ng-include="reqTypesVm.tabs.history.template"
                                 ng-controller="RequirementTypeHistoryController as reqTypeHistoryVm"></div>
                        </uib-tab>
                    </uib-tabset>
                </div>
            </div>
        </div>
    </div>
</div>