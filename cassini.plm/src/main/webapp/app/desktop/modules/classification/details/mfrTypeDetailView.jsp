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
                <div class="itemtype-view" ng-if="selectedmfrType != null && selectedmfrType != undefined">
                    <uib-tabset active="mfrTypesVm.active">
                        <uib-tab heading="{{'DETAILS_TAB_BASIC' | translate}}"
                                 active="mfrTypesVm.tabs.basic.active"
                                 select="mfrTypesVm.mfrDetailsTabActivated(mfrTypesVm.tabs.basic.id)"
                                 style="">
                            <div ng-include="mfrTypesVm.tabs.basic.template"
                                 ng-controller="MfrTypeBasicController as mfrTypeBasicVm"></div>
                        </uib-tab>

                        <uib-tab heading="{{'ATTRIBUTES' | translate}}"
                                 active="mfrTypesVm.tabs.attributes.active"
                                 select="mfrTypesVm.mfrDetailsTabActivated(mfrTypesVm.tabs.attributes.id)"
                                 style="">
                            <div ng-include="mfrTypesVm.tabs.attributes.template"
                                 ng-controller="MfrTypeAttributeController as mfrTypeAtributesVm"></div>
                        </uib-tab>

                        <uib-tab heading="{{'TIMELINE' | translate}}"
                                 active="mfrTypesVm.tabs.history.active"
                                 select="mfrTypesVm.mfrDetailsTabActivated(mfrTypesVm.tabs.history.id)"
                                 style="">
                            <div ng-include="mfrTypesVm.tabs.history.template"
                                 ng-controller="MfrTypeHistoryController as mfrTypeHistoryVm"></div>
                        </uib-tab>
                    </uib-tabset>
                </div>
            </div>
        </div>
    </div>
</div>