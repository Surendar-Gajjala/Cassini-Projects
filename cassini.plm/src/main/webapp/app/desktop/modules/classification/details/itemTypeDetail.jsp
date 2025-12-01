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
                <div class="itemtype-view" ng-if="selectedItemType != null && selectedItemType != undefined">
                    <uib-tabset active="itemTypesVm.active">
                        <uib-tab heading="{{'DETAILS_TAB_BASIC' | translate}}"
                                 active="itemTypesVm.tabs.basic.active"
                                 select="itemTypesVm.itemDetailsTabActivated(itemTypesVm.tabs.basic.id)"
                                 style="">
                            <div ng-include="itemTypesVm.tabs.basic.template"
                                 ng-controller="ItemTypeBasicController as itemTypeBasicVm"></div>
                        </uib-tab>

                        <uib-tab heading="{{'ATTRIBUTES' | translate}}"
                                 active="itemTypesVm.tabs.attributes.active"
                                 select="itemTypesVm.itemDetailsTabActivated(itemTypesVm.tabs.attributes.id)"
                                 style="">
                            <div ng-include="itemTypesVm.tabs.attributes.template"
                                 ng-controller="ItemTypeAttributesController as itemTypeAtributesVm"></div>
                        </uib-tab>

                        <uib-tab heading="{{'TIMELINE' | translate}}"
                                 active="itemTypesVm.tabs.history.active"
                                 select="itemTypesVm.itemDetailsTabActivated(itemTypesVm.tabs.history.id)"
                                 style="">
                            <div ng-include="itemTypesVm.tabs.history.template"
                                 ng-controller="ItemTypeHistoryController as itemTypeHistoryVm"></div>
                        </uib-tab>
                    </uib-tabset>
                </div>
            </div>
        </div>
    </div>
</div>