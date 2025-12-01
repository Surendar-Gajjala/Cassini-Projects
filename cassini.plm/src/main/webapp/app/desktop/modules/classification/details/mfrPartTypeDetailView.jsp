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
                <div class="itemtype-view"
                     ng-if="selectedmfrPartType != null && selectedmfrPartType != undefined"
                        >
                    <uib-tabset active="mfrPartTypesVm.active">
                        <uib-tab heading="{{'DETAILS_TAB_BASIC' | translate}}"
                                 active="mfrPartTypesVm.tabs.basic.active"
                                 select="mfrPartTypesVm.mfrPartDetailsTabActivated(mfrPartTypesVm.tabs.basic.id)"
                                 style="">
                            <div ng-include="mfrPartTypesVm.tabs.basic.template"
                                 ng-controller="MfrPartTypeBasicController as mfrPartTypeBasicVm"></div>
                        </uib-tab>

                        <uib-tab heading="{{'ATTRIBUTES' | translate}}"
                                 active="mfrPartTypesVm.tabs.attributes.active"
                                 select="mfrPartTypesVm.mfrPartDetailsTabActivated(mfrPartTypesVm.tabs.attributes.id)"
                                 style="">
                            <div ng-include="mfrPartTypesVm.tabs.attributes.template"
                                 ng-controller="MfrPartTypeAttributeController as mfrPartTypeAtributesVm"></div>
                        </uib-tab>

                        <uib-tab heading="{{'TIMELINE' | translate}}"
                                 active="mfrPartTypesVm.tabs.history.active"
                                 select="mfrPartTypesVm.mfrPartDetailsTabActivated(mfrPartTypesVm.tabs.history.id)"
                                 style="">
                            <div ng-include="mfrPartTypesVm.tabs.history.template"
                                 ng-controller="MfrPartTypeHistoryController as mfrPartTypeHistoryVm"></div>
                        </uib-tab>
                    </uib-tabset>
                </div>
            </div>
        </div>
    </div>
</div>