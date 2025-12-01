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
</style>
<div class="row row-eq-height" style="margin: 0;height: 100%">
    <div class="col-sm-12">
        <div class="itemtype-view" style="height: 100%;"
             ng-if="selectedQualityType != null && selectedQualityType != undefined">
            <uib-tabset active="qualityTypeVm.active">
                <uib-tab heading="{{'DETAILS_TAB_BASIC' | translate}}"
                         active="qualityTypeVm.tabs.basic.active"
                         select="qualityTypeVm.qualityDetailsTabActivated(qualityTypeVm.tabs.basic.id)"
                         style="">
                    <div ng-include="qualityTypeVm.tabs.basic.template"
                         ng-controller="QualityTypeBasicController as qualityTypeBasicVm"></div>
                </uib-tab>

                <uib-tab heading="{{'ATTRIBUTES' | translate}}"
                         active="qualityTypeVm.tabs.attributes.active"
                         select="qualityTypeVm.qualityDetailsTabActivated(qualityTypeVm.tabs.attributes.id)"
                         style="">
                    <div ng-include="qualityTypeVm.tabs.attributes.template"
                         ng-controller="QualityTypeAttributesController as qualityTypeAttributesVm"></div>
                </uib-tab>

                <uib-tab heading="{{'TIMELINE' | translate}}"
                         active="qualityTypeVm.tabs.history.active"
                         select="qualityTypeVm.qualityDetailsTabActivated(qualityTypeVm.tabs.history.id)"
                         style="">
                    <div ng-include="qualityTypeVm.tabs.history.template"
                         ng-controller="QualityTypeHistoryController as qualityTypeHistoryVm"></div>
                </uib-tab>
            </uib-tabset>
        </div>
    </div>
</div>