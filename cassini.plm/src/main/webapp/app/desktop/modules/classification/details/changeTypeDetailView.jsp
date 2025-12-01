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
                <div class="itemtype-view" ng-hide="selectedChangeType == null || selectedChangeType == undefined">
                    <uib-tabset active="changeTypesVm.active">
                        <uib-tab heading="{{'DETAILS_TAB_BASIC' | translate}}"
                                 active="changeTypesVm.tabs.basic.active"
                                 select="changeTypesVm.changeDetailsTabActivated(changeTypesVm.tabs.basic.id)"
                                 style="">
                            <div ng-include="changeTypesVm.tabs.basic.template"
                                 ng-controller="ChangeTypeBasicController as changeTypeBasicVm"></div>
                        </uib-tab>

                        <uib-tab heading="{{'ATTRIBUTES' | translate}}"
                                 active="changeTypesVm.tabs.attributes.active"
                                 select="changeTypesVm.changeDetailsTabActivated(changeTypesVm.tabs.attributes.id)"
                                 style="">
                            <div ng-include="changeTypesVm.tabs.attributes.template"
                                 ng-controller="ChangeTypeAttributesController as changeTypeAtributesVm"></div>
                        </uib-tab>

                        <uib-tab heading="{{'TIMELINE' | translate}}"
                                 active="changeTypesVm.tabs.history.active"
                                 select="changeTypesVm.changeDetailsTabActivated(changeTypesVm.tabs.history.id)"
                                 style="">
                            <div ng-include="changeTypesVm.tabs.history.template"
                                 ng-controller="ChangeTypeHistoryController as changeTypeHistoryVm"></div>
                        </uib-tab>
                    </uib-tabset>
                </div>
            </div>
        </div>
    </div>
</div>