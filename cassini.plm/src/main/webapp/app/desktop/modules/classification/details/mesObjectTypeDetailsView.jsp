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
                <div class="itemtype-view" ng-if="selectedProductionResType != null && selectedProductionResType != undefined">
                    <uib-tabset active="mesObjectTypeVm.active">
                        <uib-tab heading="{{'DETAILS_TAB_BASIC' | translate}}"
                                 active="mesObjectTypeVm.tabs.basic.active"
                                 select="mesObjectTypeVm.mesObjectDetailsTabActivated(mesObjectTypeVm.tabs.basic.id)"
                                 style="">
                            <div ng-include="mesObjectTypeVm.tabs.basic.template"
                                 ng-controller="MESObjectTypeBasicController as resourceTypeBasicVm"></div>
                        </uib-tab>

                        <uib-tab heading="{{'ATTRIBUTES' | translate}}"
                                 active="mesObjectTypeVm.tabs.attributes.active"
                                 select="mesObjectTypeVm.mesObjectDetailsTabActivated(mesObjectTypeVm.tabs.attributes.id)"
                                 style="">
                            <div ng-include="mesObjectTypeVm.tabs.attributes.template"
                                 ng-controller="MESObjectTypeAttributesController as resourceTypeAttributesVm"></div>
                        </uib-tab>

                        <uib-tab heading="{{'TIMELINE' | translate}}"
                                 active="mesObjectTypeVm.tabs.history.active"
                                 select="mesObjectTypeVm.mesObjectDetailsTabActivated(mesObjectTypeVm.tabs.history.id)"
                                 style="">
                            <div ng-include="mesObjectTypeVm.tabs.history.template"
                                 ng-controller="MESObjectTypeHistoryController as resourceTypeHistoryVm"></div>
                        </uib-tab>
                    </uib-tabset>
                </div>
            </div>
        </div>
    </div>
</div>