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
                <div class="itemtype-view" ng-if="selectedObjectType != null && selectedObjectType != undefined">
                    <uib-tabset active="pmObjectTypeVm.active">
                        <uib-tab heading="{{'DETAILS_TAB_BASIC' | translate}}"
                                 active="pmObjectTypeVm.tabs.basic.active"
                                 select="pmObjectTypeVm.pmObjectDetailsTabActivated(pmObjectTypeVm.tabs.basic.id)"
                                 style="">
                            <div ng-include="pmObjectTypeVm.tabs.basic.template"
                                 ng-controller="PMObjectTypeBasicController as pmObjectTypeBasicVm"></div>
                        </uib-tab>

                        <uib-tab heading="{{'ATTRIBUTES' | translate}}"
                                 active="pmObjectTypeVm.tabs.attributes.active"
                                 select="pmObjectTypeVm.pmObjectDetailsTabActivated(pmObjectTypeVm.tabs.attributes.id)"
                                 style="">
                            <div ng-include="pmObjectTypeVm.tabs.attributes.template"
                                 ng-controller="PMObjectTypeAttributesController as pmObjectTypeAttributesVm"></div>
                        </uib-tab>

                        <uib-tab heading="{{'TIMELINE' | translate}}"
                                 active="pmObjectTypeVm.tabs.history.active"
                                 select="pmObjectTypeVm.pmObjectDetailsTabActivated(pmObjectTypeVm.tabs.history.id)"
                                 style="">
                            <div ng-include="pmObjectTypeVm.tabs.history.template"
                                 ng-controller="PMObjectTypeHistoryController as pmObjectTypeHistoryVm"></div>
                        </uib-tab>
                    </uib-tabset>
                </div>
            </div>
        </div>
    </div>
</div>