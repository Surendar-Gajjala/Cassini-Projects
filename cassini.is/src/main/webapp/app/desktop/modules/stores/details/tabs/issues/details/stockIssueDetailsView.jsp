<style>
    .nav-tabs > .uib-tab.active > a, .nav-tabs > .uib-tab.active > a:focus, .nav-tabs > .uib-tab.active > a:hover {
        border: 0 !important;
        border-color: #30a82a !important;
        border-bottom: 3px solid #2a6fa8 !important;
    }

    #stockIssue-container #stockIssue-content {
        padding: 10px;
        position: relative;
        bottom: 40px;
        top: 0;
        overflow: auto;
    }

    #stockIssue-container .table-footer {
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
<div id="stockIssue-container" class="view-container" fitcontent>
    <div class="view-toolbar" style="margin: 0px;">
        <div class="row" style="margin: 0">
            <div class="btn-group">
                <button class="btn btn-sm btn-default min-width" ng-click="stockIssueDetailsVm.back()">Back</button>
                <button ng-if="stockIssueDetailsVm.addItem == true && stockIssueItemsList.content.length == 0"
                        class="min-width btn btn-sm btn-warning"
                        ng-click="stockIssueDetailsVm.addItems()">Issue Items
                </button>
                <button ng-if="stockIssueDetailsVm.addItem == true && stockIssueItemsList.content.length > 0"
                        ng-click="stockIssueDetailsVm.printIssuChallan()" class="min-width btn btn-sm btn-warning">Print
                </button>
                <button ng-if="stockIssueDetailsVm.addItem == true && stockIssueItemsList.content.length > 0"
                        ng-click="stockIssueDetailsVm.showAttributes()" class="min-width btn btn-sm btn-warning">Show
                    Attributes
                </button>
            </div>
            <h4 style="float: right;margin: 0;padding-right: 10px;">Store Details : {{title}}</h4>
        </div>

    </div>
    <div id="stockIssue-content" class="view-content no-padding" style="overflow-y: hidden;padding:10px;">
        <div class="item-details-tabs">
            <uib-tabset active="stockIssueDetailsVm.activeTab">
                <uib-tab heading="{{stockIssueDetailsVm.tabs.basic.heading}}"
                         select="stockIssueDetailsVm.detailsTabActivated(stockIssueDetailsVm.tabs.basic.id)">
                    <div ng-include="stockIssueDetailsVm.tabs.basic.template"
                         ng-controller="StockIssueBasicController as issueBasicVm"></div>
                </uib-tab>
                <uib-tab heading="{{stockIssueDetailsVm.tabs.items.heading}}"
                         ng-if="hasPermission('permission.stockIssues.viewIssueItems')"
                         select="stockIssueDetailsVm.detailsTabActivated(stockIssueDetailsVm.tabs.items.id)">
                    <div ng-include="stockIssueDetailsVm.tabs.items.template"
                         ng-controller="StoreStockIssuedController as storeStockIssuedVm"></div>
                </uib-tab>
            </uib-tabset>
        </div>
    </div>
    <div class="table-footer" id="detailsFooter">
        <div class="col-md-12" style="margin-top: 5px;" ng-if="selectedstockIssueDetailsTab != 'details.basic'">
            <div class="col-md-6">
        <span ng-if="selectedstockIssueDetailsTab == 'details.items'" style="font-weight: 700">
                    Displaying {{stockIssueItemsList.numberOfElements}} of {{stockIssueItemsList.totalElements}}</span>
            </div>
            <div class="col-md-6 text-right">
        <span ng-if="selectedstockIssueDetailsTab == 'details.items'">
                    <span class="mr10">Page {{stockIssueItemsList.totalElements != 0 ? stockIssueItemsList.number+1:0}} of {{stockIssueItemsList.totalPages}}</span>
                        <a href="" ng-click="stockIssueDetailsVm.previousPage()"
                           ng-class="{'disabled': stockIssueItemsList.first}"><i
                                class="fa fa-arrow-circle-left mr10"></i></a>
                        <a href="" ng-click="stockIssueDetailsVm.nextPage()"
                           ng-class="{'disabled': stockIssueItemsList.last}"><i
                                class="fa fa-arrow-circle-right"></i></a>
        </span>
            </div>
        </div>
    </div>
</div>
