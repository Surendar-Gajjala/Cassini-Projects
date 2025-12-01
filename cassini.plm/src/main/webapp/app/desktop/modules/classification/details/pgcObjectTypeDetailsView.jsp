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
                <div class="itemtype-view" ng-if="selectedPgcObjectType != null && selectedPgcObjectType != undefined">
                    <uib-tabset active="pgcObjectTypeVm.active">
                        <uib-tab heading="{{'DETAILS_TAB_BASIC' | translate}}"
                                 active="pgcObjectTypeVm.tabs.basic.active"
                                 select="pgcObjectTypeVm.pgcObjectDetailsTabActivated(pgcObjectTypeVm.tabs.basic.id)"
                                 style="">
                            <div ng-include="pgcObjectTypeVm.tabs.basic.template"
                                 ng-controller="PGCObjectTypeBasicController as pgcObjectTypeBasicVm"></div>
                        </uib-tab>

                        <uib-tab heading="{{'ATTRIBUTES' | translate}}"
                                 active="pgcObjectTypeVm.tabs.attributes.active"
                                 select="pgcObjectTypeVm.pgcObjectDetailsTabActivated(pgcObjectTypeVm.tabs.attributes.id)"
                                 style="">
                            <div ng-include="pgcObjectTypeVm.tabs.attributes.template"
                                 ng-controller="PGCObjectTypeAttributesController as pgcObjectTypeAttributesVm"></div>
                        </uib-tab>

                        <uib-tab heading="{{'TIMELINE' | translate}}"
                                 active="pgcObjectTypeVm.tabs.history.active"
                                 select="pgcObjectTypeVm.pgcObjectDetailsTabActivated(pgcObjectTypeVm.tabs.history.id)"
                                 style="">
                            <div ng-include="pgcObjectTypeVm.tabs.history.template"
                                 ng-controller="PGCObjectTypeHistoryController as pgcObjectTypeHistoryVm"></div>
                        </uib-tab>
                    </uib-tabset>
                </div>
            </div>
        </div>
    </div>
</div>