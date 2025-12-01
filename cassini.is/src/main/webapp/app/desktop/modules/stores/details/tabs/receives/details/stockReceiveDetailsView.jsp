<style>
    .nav-tabs > .uib-tab.active > a, .nav-tabs > .uib-tab.active > a:focus, .nav-tabs > .uib-tab.active > a:hover {
        border: 0 !important;
        border-color: #30a82a !important;
        border-bottom: 3px solid #2a6fa8 !important;
    }

    #stockReceive-container #stockReceive-content {
        padding: 10px;
        position: relative;
        bottom: 40px;
        top: 0;
        overflow: auto;
    }

    #stockReceive-container .table-footer {
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
<div id="stockReceive-container" class="view-container" fitcontent>
    <div class="view-toolbar" style="margin: 0px;">
        <div class="row" style="margin: 0">
            <div class="btn-group">
                <button class="btn btn-sm btn-default min-width" ng-click="stockReceiveDetailsVm.back()">Back</button>

                <button ng-if="(stockReceiveItemsList.content.length > 0 && stockReceiveDetailsVm.addItem == true)"
                        class="min-width btn btn-sm btn-warning"
                        ng-click="stockReceiveDetailsVm.printReceiveChallan()">Print
                </button>
                <button ng-if="stockReceiveDetailsVm.addItem == true && stockReceiveDetailsVm.addItem == true"
                        class="min-width btn btn-sm btn-warning"
                        ng-click="stockReceiveDetailsVm.showAttributes()">Show Attributes
                </button>
            </div>
            <h4 style="float: right;margin: 0;padding-right: 10px;">Store Details : {{title}}</h4>
        </div>


    </div>
    <div id="stockReceive-content" class="view-content" style="overflow-y: hidden;padding: 10px;">
        <div class="item-details-tabs">
            <uib-tabset active="stockReceiveDetailsVm.activeTab">
                <uib-tab heading="{{stockReceiveDetailsVm.tabs.basic.heading}}"
                         select="stockReceiveDetailsVm.detailsTabActivated(stockReceiveDetailsVm.tabs.basic.id)">
                    <div ng-include="stockReceiveDetailsVm.tabs.basic.template"
                         ng-controller="StockReceiveBasicController as basicVm"></div>
                </uib-tab>
                <uib-tab heading="{{stockReceiveDetailsVm.tabs.items.heading}}"
                         ng-if="hasPermission('permission.receives.viewReceiveItems')"
                         select="stockReceiveDetailsVm.detailsTabActivated(stockReceiveDetailsVm.tabs.items.id)">
                    <div ng-include="stockReceiveDetailsVm.tabs.items.template"
                         ng-controller="StockReceiveItemsController as stockReceivedItemVm"></div>
                </uib-tab>
            </uib-tabset>
        </div>
    </div>
    <div class="table-footer" id="detailsFooter">
        <div class="col-md-12" style="margin-top: 5px;" ng-if="selectedStockReceiveDetailsTab != 'details.basic'">
            <div class="col-md-6">
        <span ng-if="selectedStockReceiveDetailsTab == 'details.items'" style="font-weight: 700">
                    Displaying {{stockReceiveItemsList.numberOfElements}} of {{stockReceiveItemsList.totalElements}}</span>
            </div>
            <div class="col-md-6 text-right">
        <span ng-if="selectedStockReceiveDetailsTab == 'details.items'">
                    <span class="mr10">Page {{stockReceiveItemsList.totalElements != 0 ? stockReceiveItemsList.number+1:0}} of {{stockReceiveItemsList.totalPages}}</span>
                        <a href="" ng-click="stockReceiveDetailsVm.previousPage()"
                           ng-class="{'disabled': stockReceiveItemsList.first}"><i
                                class="fa fa-arrow-circle-left mr10"></i></a>
                        <a href="" ng-click="stockReceiveDetailsVm.nextPage()"
                           ng-class="{'disabled': stockReceiveItemsList.last}"><i
                                class="fa fa-arrow-circle-right"></i></a>
                    </span>
            </div>
        </div>
    </div>
</div>
