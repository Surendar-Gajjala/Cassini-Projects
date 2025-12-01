<style scoped>
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
                <button class="btn btn-sm btn-default min-width" ng-click="scrapDetailsVm.back()">Back</button>
                <button ng-if="scrapDetailsVm.addItem == true" class="min-width btn btn-sm btn-warning"
                        ng-click="scrapDetailsVm.addItems()">Add Items
                </button>
                <button ng-if="(scrapItems.length > 0 && scrapDetailsVm.addItem)"
                        class="min-width btn btn-sm btn-warning"
                        ng-click="scrapDetailsVm.printScrapChallan()">Print Scrap Challan
                </button>
            </div>
        </div>
    </div>

    <div class="view-content no-padding" style="overflow-y: auto;">
        <div class="row row-eq-height" style="margin: 0;">
            <div class="col-sm-12" style="padding: 10px;">
                <div class="item-details-tabs">
                    <uib-tabset active="scrapDetailsVm.activeTab">
                        <uib-tab heading="{{scrapDetailsVm.tabs.basic.heading}}"
                                 active="scrapDetailsVm.tabs.basic.active"
                                 select="scrapDetailsVm.detailsTabActivated(scrapDetailsVm.tabs.basic.id)">
                            <div ng-include="scrapDetailsVm.tabs.basic.template"
                                 ng-controller="ScrapBasicController as basicVm"></div>
                        </uib-tab>
                        <uib-tab heading="{{scrapDetailsVm.tabs.items.heading}}"
                                 ng-if="hasPermission('permission.topStores.viewScrapRequestItems')"
                                 select="scrapDetailsVm.detailsTabActivated(scrapDetailsVm.tabs.items.id)">
                            <div ng-include="scrapDetailsVm.tabs.items.template"
                                 ng-controller="ScrapRequestItemsController as scrapItemVm"></div>
                        </uib-tab>
                    </uib-tabset>
                </div>
            </div>
        </div>
    </div>
</div>