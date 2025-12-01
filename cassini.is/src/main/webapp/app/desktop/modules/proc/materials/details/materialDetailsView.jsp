<style>
    .nav-tabs > .uib-tab.active > a, .nav-tabs > .uib-tab.active > a:focus, .nav-tabs > .uib-tab.active > a:hover {
        border: 0 !important;
        border-color: #30a82a !important;
        border-bottom: 3px solid #2a6fa8 !important;
    }

    .tab-content {
        padding: 0px !important;
    }

    .tab-content .tab-pane {
        overflow: auto !important;
    }

    .tab-pane {
        position: relative;
    }

    .tab-content .tab-pane .responsive-table {
        height: 100%;
        position: absolute;
        overflow: auto !important;
        padding: 5px;
    }

    .tab-content .tab-pane .responsive-table table thead th {
        position: -webkit-sticky;
        position: sticky;
        top: -10px !important;
        z-index: 5;
        background-color: #fff;
    }
</style>

<div class="view-container" fitcontent>
    <div class="view-toolbar" style="margin: 0px;">
        <div class="row" style="margin: 0">
            <div class="btn-group">
                <button class="btn btn-sm btn-default min-width" ng-click="materialDetailsVm.back()">Back</button>
            </div>
            <h4 style="float: right;margin: 0;padding-right: 10px;"> {{viewInfo.title}}</h4>
        </div>

    </div>
    <div class="view-content no-padding" style="overflow-y: hidden;">
        <div class="row row-eq-height" style="margin: 0;">
            <div class="col-sm-12" style="padding: 10px;">
                <div class="item-details-tabs">
                    <uib-tabset active="materialDetailsVm.activeTab">
                        <uib-tab heading="{{materialDetailsVm.tabs.basic.heading}}"
                                 select="materialDetailsVm.materialDetailsTabActivated(materialDetailsVm.tabs.basic.id)">
                            <div ng-include="materialDetailsVm.tabs.basic.template"
                                 ng-controller="MaterialBasicDetailsController as materialBasicVm"></div>
                        </uib-tab>
                        <uib-tab heading="{{materialDetailsVm.tabs.attributes.heading}}"
                                 select="materialDetailsVm.materialDetailsTabActivated(materialDetailsVm.tabs.attributes.id)">
                            <div ng-include="materialDetailsVm.tabs.attributes.template"
                                 ng-controller="MaterialAttributesController as materialAttributeVm"></div>
                        </uib-tab>
                        <uib-tab heading="{{materialDetailsVm.tabs.inventory.heading}}"
                                 select="materialDetailsVm.materialDetailsTabActivated(materialDetailsVm.tabs.inventory.id)">
                            <div ng-include="materialDetailsVm.tabs.inventory.template"
                                 ng-controller="MaterialInventoryController as materialInventoryVm"></div>
                        </uib-tab>
                        <uib-tab heading="{{materialDetailsVm.tabs.stockMovement.heading}}"
                                 select="materialDetailsVm.materialDetailsTabActivated(materialDetailsVm.tabs.stockMovement.id)">
                            <div ng-include="materialDetailsVm.tabs.stockMovement.template"
                                 ng-controller="MaterialStockMovementController as materialMovementVm"></div>
                        </uib-tab>
                    </uib-tabset>
                </div>
            </div>
        </div>
    </div>
</div>
