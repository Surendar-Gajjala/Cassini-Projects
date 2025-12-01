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
                    <uib-tabset active="customTypesVm.active">
                        <uib-tab heading="{{'DETAILS_TAB_BASIC' | translate}}"
                                 active="customTypesVm.tabs.basic.active"
                                 select="customTypesVm.customDetailsTabActivated(customTypesVm.tabs.basic.id)"
                                 style="">
                            <div ng-include="customTypesVm.tabs.basic.template"
                                 ng-controller="CustomObjectTypeBasicController as customTypeBasicVm"></div>
                        </uib-tab>

                        <uib-tab heading="{{'ATTRIBUTES' | translate}}"
                                 active="customTypesVm.tabs.attributes.active"
                                 select="customTypesVm.customDetailsTabActivated(customTypesVm.tabs.attributes.id)"
                                 style="">
                            <div ng-include="customTypesVm.tabs.attributes.template"
                                 ng-controller="CustomObjectTypeAttributesController as customTypeAtributesVm"></div>
                        </uib-tab>

                        <uib-tab heading="{{'TIMELINE' | translate}}"
                                 active="customTypesVm.tabs.history.active"
                                 select="customTypesVm.customDetailsTabActivated(customTypesVm.tabs.history.id)"
                                 style="">
                            <div ng-include="customTypesVm.tabs.history.template"
                                 ng-controller="CustomObjectTypeHistoryController as customTypeHistoryVm"></div>
                        </uib-tab>
                    </uib-tabset>
                </div>
            </div>
        </div>
    </div>
</div>