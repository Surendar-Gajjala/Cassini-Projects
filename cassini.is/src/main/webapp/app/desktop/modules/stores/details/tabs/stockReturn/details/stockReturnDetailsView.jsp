<style scoped>
    .nav-tabs > .uib-tab.active > a, .nav-tabs > .uib-tab.active > a:focus, .nav-tabs > .uib-tab.active > a:hover {
        border: 0 !important;
        border-color: #30a82a !important;
        border-bottom: 3px solid #2a6fa8 !important;
    }

    #requisition-container #requisition-content {
        padding: 10px;
        position: relative;
        bottom: 40px;
        top: 0;
        overflow: auto;
    }

    #requisition-container .table-footer {
        padding: 0 10px 0 10px;
        position: relative;
        bottom: 0;
        height: 40px;
        width: 100%;
        border-top: 1px solid #D3D7DB;
        display: table;
    }

    a.disabled {
        cursor: not-allowed;
        color: lightgrey;
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
<div id="requisition-container" class="view-container" fitcontent>
    <div class="view-toolbar" style="margin: 0px;">
        <div class="row" style="margin: 0">
            <div class="btn-group">
                <button class="btn btn-sm btn-default min-width" ng-click="stockReturnDetailsVm.back()">Back</button>
                <button ng-hide="stockReturn.status == 'APPROVED' || !hasPermission('permission.stockReturn.approve') || stockReturnDetailsVm.addItem"
                        class="btn btn-sm btn-default min-width" ng-click="stockReturnDetailsVm.approveStockReturn()">
                    Approve
                </button>
            </div>
            <h4 style="float: right;margin: 0;padding-right: 10px;">Store Details : {{title}}</h4>
        </div>
    </div>
    <div id="requisition-content" class="view-content no-padding" style="overflow-y: hidden;padding: 10px;">
        <div class="item-details-tabs">
            <uib-tabset active="stockReturnDetailsVm.activeTab">
                <uib-tab heading="{{stockReturnDetailsVm.tabs.basic.heading}}"
                         select="stockReturnDetailsVm.detailsTabActivated(stockReturnDetailsVm.tabs.basic.id)">
                    <div ng-include="stockReturnDetailsVm.tabs.basic.template"
                         ng-controller="StockReturnBasicController as basicVm"></div>
                </uib-tab>
                <uib-tab heading="{{stockReturnDetailsVm.tabs.items.heading}}"
                         select="stockReturnDetailsVm.detailsTabActivated(stockReturnDetailsVm.tabs.items.id)">
                    <div ng-include="stockReturnDetailsVm.tabs.items.template"
                         ng-controller="StockReturnItemsController as stockReturnItemsVm"></div>
                </uib-tab>
            </uib-tabset>
        </div>
    </div>
    <div class="table-footer" id="detailsFooter">
        <div class="col-md-12" style="margin-top: 5px;" ng-if="selectedRequisitionDetailsTab != 'details.basic'">
            <div class="col-md-6">
        <span ng-if="selectedRequisitionDetailsTab == 'details.items'" style="font-weight: 700">
                    Displaying {{stockReturnItemsList.numberOfElements}} of {{stockReturnItemsList.totalElements}}</span>
            </div>
            <div class="col-md-6 text-right">
        <span ng-if="selectedRequisitionDetailsTab == 'details.items'">
                    <span class="mr10">Page {{stockReturnItemsList.totalElements != 0 ? stockReturnItemsList.number+1:0}} of {{stockReturnItemsList.totalPages}}</span>
                        <a href="" ng-click="stockReturnDetailsVm.previousPage()"
                           ng-class="{'disabled': stockReturnItemsList.first}"><i
                                class="fa fa-arrow-circle-left mr10"></i></a>
                        <a href="" ng-click="stockReturnDetailsVm.nextPage()"
                           ng-class="{'disabled': stockReturnItemsList.last}"><i
                                class="fa fa-arrow-circle-right"></i></a>
        </span>
            </div>
        </div>
    </div>
</div>
