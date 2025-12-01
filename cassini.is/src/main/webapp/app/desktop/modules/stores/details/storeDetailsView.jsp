<style scoped>
    .nav-tabs > .uib-tab.active > a, .nav-tabs > .uib-tab.active > a:focus, .nav-tabs > .uib-tab.active > a:hover {
        border: 0 !important;
        border-color: #30a82a !important;
        border-bottom: 3px solid #2a6fa8 !important;
    }

    #store-container #store-content {
        padding: 10px;
        position: relative;
        bottom: 40px;
        top: 0;
        overflow: auto;
    }

    #store-container .table-footer {
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
<div id="store-container" class="view-container" fitcontent>
    <div class="view-toolbar">
        <div class="btn-group">
            <button class="min-width btn btn-sm btn-default" ng-click="storeDetailsVm.back()">Back</button>
            <button ng-show="storeDetailsVm.tabs.inventory.active" class="min-width btn btn-sm btn-warning"
                    ng-click="storeDetailsVm.allocateItems()"
                    ng-disabled="hasPermission('permission.storeInventory.addItemsToProject') == false">Allocate Items
                to Project
            </button>
            <button ng-show="storeDetailsVm.tabs.inventory.active" class="btn btn-sm btn-primary"
                    ng-click="storeDetailsVm.showInventoryAttributes()"> Show Attributes
            </button>
            <button ng-show="storeDetailsVm.tabs.stockMovement.active" class="btn btn-sm btn-primary"
                    ng-click="storeDetailsVm.showStockMovementAttributes()"> Show Attributes
            </button>
            <button ng-show="storeDetailsVm.tabs.stockReceived.active" class="min-width btn btn-sm btn-success"
                    ng-click="storeDetailsVm.stockReceived()"
                    ng-disabled="hasPermission('permission.receives.receive') == false">New Receive
            </button>
            <button ng-show="storeDetailsVm.tabs.stockReceived.active" class="min-width btn btn-sm btn-warning"
                    ng-click="storeDetailsVm.showReceiveAttributes()">Show Attributes
            </button>
            <button ng-show="storeDetailsVm.tabs.stockIssued.active" class="min-width btn btn-sm btn-warning"
                    ng-click="storeDetailsVm.newIssue()"
                    ng-disabled="hasPermission('permission.stockIssues.issue') == false">New Issue
            </button>
            <button ng-show="storeDetailsVm.tabs.stockIssued.active" class="btn btn-sm btn-primary"
                    ng-click="storeDetailsVm.showIssueAttributes()">Show Attributes
            </button>
            <button ng-show="storeDetailsVm.tabs.loanIssued.active" class="min-width btn btn-sm btn-warning"
                    ng-click="storeDetailsVm.newLoan()"
                    ng-disabled="hasPermission('permission.loanIssued.addLoan') == false">Add Loan
            </button>
            <button ng-show="storeDetailsVm.tabs.loanIssued.active" class="min-width btn btn-sm btn-warning"
                    ng-click="storeDetailsVm.showLoanIssuedAttributes()">Show Attributes
            </button>
            <button ng-show="storeDetailsVm.tabs.loanReceived.active" class="min-width btn btn-sm btn-warning"
                    ng-click="storeDetailsVm.showLoanReceivedAttributes()">Show Attributes
            </button>
            <button ng-show="storeDetailsVm.tabs.requests.active" class="min-width btn btn-sm btn-warning"
                    ng-click="storeDetailsVm.newRequest()"
                    ng-disabled="!hasPermission('permission.requisitions.newRequest')">New Request
            </button>

            <button ng-show="storeDetailsVm.tabs.requests.active" class="btn btn-sm btn-primary"
                    ng-click="storeDetailsVm.showRequisitionAttributes()">Show Attributes
            </button>

            <button ng-show="storeDetailsVm.tabs.indent.active" class="min-width btn btn-sm btn-warning"
                    ng-disabled="!hasPermission('permission.indents.newIndent')"
                    ng-click="storeDetailsVm.newIndent()">New Indent
            </button>

            <button ng-show="storeDetailsVm.tabs.indent.active" class="btn btn-sm btn-primary"
                    ng-click="storeDetailsVm.showIndentAttributes()">Show Attributes
            </button>

            <button ng-show="storeDetailsVm.tabs.purchaseOrders.active" class="min-width btn btn-sm btn-warning"
                    ng-disabled="!hasPermission('permission.purchaseOrders.newPurchaseOrder')"
                    ng-click="storeDetailsVm.newPurchaseOrder()">New Purchase Order
            </button>

            <button ng-show="storeDetailsVm.tabs.purchaseOrders.active" class="btn btn-sm btn-primary"
                    ng-click="storeDetailsVm.showPurchaseOrderAttributes()">Show Attributes
            </button>

            <button ng-show="storeDetailsVm.tabs.scrapRequests.active" class="min-width btn btn-sm btn-warning"
                    ng-disabled="!hasPermission('permission.indents.newIndent')"
                    ng-click="storeDetailsVm.newScrapRequest()">New Scrap Request
            </button>
            <button ng-show="storeDetailsVm.tabs.roadChallan.active" class="min-width btn btn-sm btn-warning"
                    ng-disabled="!hasPermission('permission.roadChallans.createRoadChalan')"
                    ng-click="storeDetailsVm.newRoadChallan()">New Road Challan
            </button>
            <button ng-show="storeDetailsVm.tabs.roadChallan.active" class="min-width btn btn-sm btn-warning"
                    ng-click="storeDetailsVm.showRoadChallanAttributes()">Show Attributes
            </button>
            <button ng-show="storeDetailsVm.tabs.receiveChallan.active" class="min-width btn btn-sm btn-warning"
                    ng-click="storeDetailsVm.newReceiveChallan()">New Receive Challan
            </button>
            <button ng-show="storeDetailsVm.tabs.issueChallan.active" class="min-width btn btn-sm btn-warning"
                    ng-click="storeDetailsVm.newIssueChallan()">New Issue Challan
            </button>
            <button ng-show="storeDetailsVm.tabs.stockReturn.active" class="min-width btn btn-sm btn-warning"
                    ng-click="storeDetailsVm.newStockReturn()"
                    ng-disabled="!hasPermission('permission.stockReturn.create')">New Stock Return
            </button>
            <button ng-show="storeDetailsVm.tabs.stockReturn.active" class="btn btn-sm btn-primary"
                    ng-click="storeDetailsVm.showStockReturnAttributes()">Show Attributes
            </button>
        </div>
        <div ng-if="storeDetailsVm.activeTab != 0" class="input-group input-group-sm mb15"
             style="margin-top: 2px;width: 400px;margin-right: 10px;position: absolute;top: 40px;right: 30px;">
            <span class="input-group-btn">
                <button type="button" ng-click="storeDetailsVm.clear()" class="btn btn-danger"
                        title="Clear search"
                        style="height: 30px !important;">
                    <i class="fa fa-times-circle" style="font-size:16px"></i>
                </button>
            </span>
            <input class="form-control" type="text" ng-enter="storeDetailsVm.onSearch()"
                   ng-model="storeDetailsVm.searchTerm">
                <span class="input-group-btn">
                    <button type="button" ng-click="storeDetailsVm.onSearch()"
                            class="btn btn-primary"
                            title="Search"
                            style="height: 30px !important;">
                        <i class="fa fa-search" style="font-size:15px"></i>
                    </button>
                </span>
        </div>
        <h4 style="float: right;margin: 0;padding-right: 10px;">Store Details : {{title}}</h4>
    </div>
    <div id="store-content" class="view-content no-padding" style="overflow-y: hidden;">
        <div class="row row-eq-height" style="margin: 0;">
            <div class="col-sm-12" style="padding: 0px;">
                <div class="item-details-tabs">
                    <scrollable-tabset tooltip-left-placement="top" show-drop-down="false">
                        <uib-tabset active="storeDetailsVm.tabActive" style="height: 50px;">
                            <uib-tab heading="{{storeDetailsVm.tabs.basic.heading}}"
                                     active="storeDetailsVm.tabs.basic.active"
                                     select="storeDetailsVm.storeDetailsTabActivated(storeDetailsVm.tabs.basic.id)">
                                <div ng-include="storeDetailsVm.tabs.basic.template"
                                     ng-controller="StoreBasicController as storeBasicVm"></div>
                            </uib-tab>
                            <uib-tab heading="{{storeDetailsVm.tabs.inventory.heading}}"
                                     active="storeDetailsVm.tabs.inventory.active"
                                     disable="!hasPermission('permission.storeInventory.inventory')"
                                     select="storeDetailsVm.storeDetailsTabActivated(storeDetailsVm.tabs.inventory.id)">
                                <div ng-include="storeDetailsVm.tabs.inventory.template"
                                     ng-controller="StoreInventoryController as storeInventoryVm"></div>
                            </uib-tab>
                            <uib-tab heading="{{storeDetailsVm.tabs.stockReceived.heading}}"
                                     active="storeDetailsVm.tabs.stockReceived.active"
                                     disable="!(hasPermission('permission.receives.viewReceive') || hasPermission('permission.receives.receive') || hasPermission('permission.receives.viewReceiveItems'))"
                                     select="storeDetailsVm.storeDetailsTabActivated(storeDetailsVm.tabs.stockReceived.id)">
                                <div ng-include="storeDetailsVm.tabs.stockReceived.template"
                                     ng-controller="StoreStockReceivedController as storeStockReceivedVm"></div>
                            </uib-tab>
                            <uib-tab heading="{{storeDetailsVm.tabs.stockIssued.heading}}"
                                     active="storeDetailsVm.tabs.stockIssued.active"
                                     disable="!(hasPermission('permission.stockIssues.viewIssue') || hasPermission('permission.stockIssues.issue') || hasPermission('permission.stockIssues.viewIssueItems'))"
                                     select="storeDetailsVm.storeDetailsTabActivated(storeDetailsVm.tabs.stockIssued.id)">
                                <div ng-include="storeDetailsVm.tabs.stockIssued.template"
                                     ng-controller="StockIssuesController as stockIssuesVm"></div>
                            </uib-tab>
                            <uib-tab heading="{{storeDetailsVm.tabs.loanReceived.heading}}"
                                     active="storeDetailsVm.tabs.loanReceived.active"
                                     disable="!(hasPermission('permission.loanReceived.viewLoanReceive') || hasPermission('permission.loanReceived.viewReceiveLoanItems') || hasPermission('permission.loanReceived.returnLoanItems'))"
                                     select="storeDetailsVm.storeDetailsTabActivated(storeDetailsVm.tabs.loanReceived.id)">
                                <div ng-include="storeDetailsVm.tabs.loanReceived.template"
                                     ng-controller="LoanReceiveController as loanReceiveVm"></div>
                            </uib-tab>
                            <uib-tab heading="{{storeDetailsVm.tabs.loanIssued.heading}}"
                                     active="storeDetailsVm.tabs.loanIssued.active"
                                     disable="!(hasPermission('permission.loanIssued.loan') || hasPermission('permission.loanIssued.addLoan') || hasPermission('permission.loanIssued.issueLoanItems') || hasPermission('permission.loanIssued.viewLoanIssueItems'))"
                                     select="storeDetailsVm.storeDetailsTabActivated(storeDetailsVm.tabs.loanIssued.id)">
                                <div ng-include="storeDetailsVm.tabs.loanIssued.template"
                                     ng-controller="LoanIssuedController as loanVm"></div>
                            </uib-tab>
                            <uib-tab heading="{{storeDetailsVm.tabs.stockMovement.heading}}"
                                     active="storeDetailsVm.tabs.stockMovement.active"
                                     disable="!hasPermission('permission.topStores.stockMovement')"
                                     select="storeDetailsVm.storeDetailsTabActivated(storeDetailsVm.tabs.stockMovement.id)">
                                <div ng-include="storeDetailsVm.tabs.stockMovement.template"
                                     ng-controller="StockMovementController as stockMovementVm"></div>
                            </uib-tab>
                            <uib-tab heading="{{storeDetailsVm.tabs.requests.heading}}"
                                     active="storeDetailsVm.tabs.requests.active"
                                     disable="!(hasPermission('permission.requisitions.newRequest') || hasPermission('permission.requisitions.viewRequestItems') || hasPermission('permission.requisitions.addRequestItems') || hasPermission('permission.requisitions.approveRequest') || hasPermission('permission.requisitions.editRequest'))"
                                     select="storeDetailsVm.storeDetailsTabActivated(storeDetailsVm.tabs.requests.id)">
                                <div id="request" ng-include="storeDetailsVm.tabs.requests.template"
                                     ng-controller="RequestsController as requestsVm"></div>
                            </uib-tab>
                            <uib-tab heading="{{storeDetailsVm.tabs.indent.heading}}"
                                     active="storeDetailsVm.tabs.indent.active"
                                     disable="!(hasPermission('permission.indents.newIndent') || hasPermission('permission.indents.viewIndents') || hasPermission('permission.indents.viewIndentItems') || hasPermission('permission.indents.addIndentItems') ||
                                            hasPermission('permission.indents.removeIndentItems') || hasPermission('permission.indents.editIndentItems') || hasPermission('permission.indents.approveIndent'))"
                                     select="storeDetailsVm.storeDetailsTabActivated(storeDetailsVm.tabs.indent.id)">
                                <div ng-include="storeDetailsVm.tabs.indent.template"
                                     ng-controller="IndentsController as indentsVm"></div>
                            </uib-tab>
                            <uib-tab heading="{{storeDetailsVm.tabs.purchaseOrders.heading}}"
                                     active="storeDetailsVm.tabs.purchaseOrders.active"
                                     disable="!(hasPermission('permission.purchaseOrders.newPurchaseOrder') || hasPermission('permission.purchaseOrders.editPurchaseOrder') || hasPermission('permission.purchaseOrders.viewPurchaseOrder') || hasPermission('permission.purchaseOrders.viewPurchaseOrderItems') ||
                                            hasPermission('permission.purchaseOrders.addPurchaseOrderItems') || hasPermission('permission.purchaseOrders.removePurchaseOrderItems') || hasPermission('permission.purchaseOrders.editPurchaseOrderItemQty') || hasPermission('permission.purchaseOrders.approvePurchaseOrder'))"
                                     select="storeDetailsVm.storeDetailsTabActivated(storeDetailsVm.tabs.purchaseOrders.id)">
                                <div ng-include="storeDetailsVm.tabs.purchaseOrders.template"
                                     ng-controller="PurchaseOrdersController as purchaseOrdersVm"></div>
                            </uib-tab>
                            <uib-tab heading="{{storeDetailsVm.tabs.roadChallan.heading}}"
                                     active="storeDetailsVm.tabs.roadChallan.active"
                                     ng-disabled="!(hasPermission('permission.roadChallans.createRoadChalan') || hasPermission('permission.roadChallans.viewRoadChalan') || hasPermission('permission.roadChallans.addRoadChalanItems') || hasPermission('permission.roadChallans.editRoadChalanItems') || hasPermission('permission.roadChallans.viewRoadChalanItems'))"
                                     select="storeDetailsVm.storeDetailsTabActivated(storeDetailsVm.tabs.roadChallan.id)">
                                <div ng-include="storeDetailsVm.tabs.roadChallan.template"
                                     ng-controller="RoadChallansController as roadChallansVm"></div>
                            </uib-tab>
                            <uib-tab heading="{{storeDetailsVm.tabs.stockReturn.heading}}"
                                     active="storeDetailsVm.tabs.stockReturn.active"
                                     disable="!(hasPermission('permission.stockReturn.view') || hasPermission('permission.stockReturn.create') || hasPermission('permission.stockReturn.edit') || hasPermission('permission.stockReturn.approve'))"
                                     select="storeDetailsVm.storeDetailsTabActivated(storeDetailsVm.tabs.stockReturn.id)">
                                <div ng-include="storeDetailsVm.tabs.stockReturn.template"
                                     ng-controller="StockReturnsController as stockReturnsVm"></div>
                            </uib-tab>
                        </uib-tabset>
                    </scrollable-tabset>
                </div>
            </div>
        </div>
    </div>
    <div class="table-footer" id="detailsFooter">
        <div class="col-md-12" style="margin-top: 5px;" ng-if="selectedStoreDetailsTab != 'details.basic'">
            <div class="col-md-6">
                <span ng-if="selectedStoreDetailsTab == 'details.inventory'" style="font-weight: 700">
                    Displaying {{storeInventoryList.numberOfElements}} of {{storeInventoryList.totalElements}}</span>
                <span ng-if="selectedStoreDetailsTab == 'details.stockMovement'" style="font-weight: 700">
                    Displaying {{storeStockMovementList.numberOfElements}} of {{storeStockMovementList.totalElements}}</span>
                <span ng-if="selectedStoreDetailsTab == 'details.requests'" style="font-weight: 700">
                    Displaying {{storeRequisitionList.numberOfElements}} of {{storeRequisitionList.totalElements}}</span>
                <span ng-if="selectedStoreDetailsTab == 'details.indent'" style="font-weight: 700">
                    Displaying {{storeIndentsList.numberOfElements}} of {{storeIndentsList.totalElements}}</span>
                <span ng-if="selectedStoreDetailsTab == 'details.purchaseOrders'" style="font-weight: 700">
                    Displaying {{purchaseOrdersList.numberOfElements}} of {{purchaseOrdersList.totalElements}}</span>
                <span ng-if="selectedStoreDetailsTab == 'details.stockIssues'" style="font-weight: 700">
                    Displaying {{stockIssueList.numberOfElements}} of {{stockIssueList.totalElements}}</span>
                <span ng-if="selectedStoreDetailsTab == 'details.roadChallan'" style="font-weight: 700">
                    Displaying {{storeRoadChallanList.numberOfElements}} of {{storeRoadChallanList.totalElements}}</span>
                <span ng-if="selectedStoreDetailsTab == 'details.stockReceives'" style="font-weight: 700">
                    Displaying {{stockReceiveList.numberOfElements}} of {{stockReceiveList.totalElements}}</span>
                <span ng-if="selectedStoreDetailsTab == 'details.loanIssued'" style="font-weight: 700">
                    Displaying {{storeLoanIssueList.numberOfElements}} of {{storeLoanIssueList.totalElements}}</span>
                <span ng-if="selectedStoreDetailsTab == 'details.loanReceived'" style="font-weight: 700">
                    Displaying {{storeLoanReceiveList.numberOfElements}} of {{storeLoanReceiveList.totalElements}}</span>
                <span ng-if="selectedStoreDetailsTab == 'details.stockReturn'" style="font-weight: 700">
                    Displaying {{stockReturns.numberOfElements}} of {{stockReturns.totalElements}}</span>
            </div>
            <div class="col-md-6 text-right">
                <span ng-if="selectedStoreDetailsTab == 'details.inventory'">
                    <span class="mr10">Page {{storeInventoryList.totalElements != 0 ? storeInventoryList.number+1:0}} of {{storeInventoryList.totalPages}}</span>
                        <a href="" ng-click="storeDetailsVm.previousPage()"
                           ng-class="{'disabled': storeInventoryList.first}"><i
                                class="fa fa-arrow-circle-left mr10"></i></a>
                        <a href="" ng-click="storeDetailsVm.nextPage()"
                           ng-class="{'disabled': storeInventoryList.last}"><i
                                class="fa fa-arrow-circle-right"></i></a>
                </span>
                <span ng-if="selectedStoreDetailsTab == 'details.stockMovement'">
                    <span class="mr10">Page {{storeStockMovementList.totalElements != 0 ? storeStockMovementList.number+1:0}} of {{storeStockMovementList.totalPages}}</span>
                        <a href="" ng-click="storeDetailsVm.previousPage()"
                           ng-class="{'disabled': storeStockMovementList.first}"><i
                                class="fa fa-arrow-circle-left mr10"></i></a>
                        <a href="" ng-click="storeDetailsVm.nextPage()"
                           ng-class="{'disabled': storeStockMovementList.last}"><i
                                class="fa fa-arrow-circle-right"></i></a>
                </span>
                <span ng-if="selectedStoreDetailsTab == 'details.requests'">
                    <span class="mr10">Page {{storeRequisitionList.totalElements != 0 ? storeRequisitionList.number+1:0}} of {{storeRequisitionList.totalPages}}</span>
                        <a href="" ng-click="storeDetailsVm.previousPage()"
                           ng-class="{'disabled': storeRequisitionList.first}"><i
                                class="fa fa-arrow-circle-left mr10"></i></a>
                        <a href="" ng-click="storeDetailsVm.nextPage()"
                           ng-class="{'disabled': storeRequisitionList.last}"><i
                                class="fa fa-arrow-circle-right"></i></a>
                </span>
                <span ng-if="selectedStoreDetailsTab == 'details.indent'">
                    <span class="mr10">Page {{storeIndentsList.totalElements != 0 ? storeIndentsList.number+1:0}} of {{storeIndentsList.totalPages}}</span>
                        <a href="" ng-click="storeDetailsVm.previousPage()"
                           ng-class="{'disabled': storeIndentsList.first}"><i
                                class="fa fa-arrow-circle-left mr10"></i></a>
                        <a href="" ng-click="storeDetailsVm.nextPage()"
                           ng-class="{'disabled': storeIndentsList.last}"><i
                                class="fa fa-arrow-circle-right"></i>
                        </a>
                </span>
                <span ng-if="selectedStoreDetailsTab == 'details.purchaseOrders'">
                    <span class="mr10">Page {{purchaseOrdersList.totalElements != 0 ? purchaseOrdersList.number+1:0}} of {{purchaseOrdersList.totalPages}}</span>
                        <a href="" ng-click="storeDetailsVm.previousPage()"
                           ng-class="{'disabled': purchaseOrdersList.first}"><i
                                class="fa fa-arrow-circle-left mr10"></i></a>
                        <a href="" ng-click="storeDetailsVm.nextPage()"
                           ng-class="{'disabled': purchaseOrdersList.last}"><i
                                class="fa fa-arrow-circle-right"></i>
                        </a>
                </span>
                <span ng-if="selectedStoreDetailsTab == 'details.roadChallan'">
                    <span class="mr10">Page {{storeRoadChallanList.totalElements != 0 ? storeRoadChallanList.number+1:0}} of {{storeRoadChallanList.totalPages}}</span>
                        <a href="" ng-click="storeDetailsVm.previousPage()"
                           ng-class="{'disabled': storeRoadChallanList.first}"><i
                                class="fa fa-arrow-circle-left mr10"></i></a>
                        <a href="" ng-click="storeDetailsVm.nextPage()"
                           ng-class="{'disabled': storeRoadChallanList.last}"><i
                                class="fa fa-arrow-circle-right"></i>
                        </a>
                </span>
                <span ng-if="selectedStoreDetailsTab == 'details.stockReceives'">
                    <span class="mr10">Page {{stockReceiveList.totalElements != 0 ? stockReceiveList.number+1:0}} of {{stockReceiveList.totalPages}}</span>
                        <a href="" ng-click="storeDetailsVm.previousPage()"
                           ng-class="{'disabled': stockReceiveList.first}"><i
                                class="fa fa-arrow-circle-left mr10"></i></a>
                        <a href="" ng-click="storeDetailsVm.nextPage()"
                           ng-class="{'disabled': stockReceiveList.last}"><i
                                class="fa fa-arrow-circle-right"></i>
                        </a>
                </span>
                <span ng-if="selectedStoreDetailsTab == 'details.stockIssues'">
                    <span class="mr10">Page {{stockIssueList.totalElements != 0 ? stockIssueList.number+1:0}} of {{stockIssueList.totalPages}}</span>
                        <a href="" ng-click="storeDetailsVm.previousPage()"
                           ng-class="{'disabled': stockIssueList.first}"><i
                                class="fa fa-arrow-circle-left mr10"></i></a>
                        <a href="" ng-click="storeDetailsVm.nextPage()"
                           ng-class="{'disabled': stockIssueList.last}"><i
                                class="fa fa-arrow-circle-right"></i>
                        </a>
                </span>
                <span ng-if="selectedStoreDetailsTab == 'details.loanIssued'">
                    <span class="mr10">Page {{storeLoanIssueList.totalElements != 0 ? storeLoanIssueList.number+1:0}} of {{storeLoanIssueList.totalPages}}</span>
                        <a href="" ng-click="storeDetailsVm.previousPage()"
                           ng-class="{'disabled': storeLoanIssueList.first}"><i
                                class="fa fa-arrow-circle-left mr10"></i></a>
                        <a href="" ng-click="storeDetailsVm.nextPage()"
                           ng-class="{'disabled': storeLoanIssueList.last}"><i
                                class="fa fa-arrow-circle-right"></i>
                        </a>
                </span>
                <span ng-if="selectedStoreDetailsTab == 'details.loanReceived'">
                    <span class="mr10">Page {{storeLoanReceiveList.totalElements != 0 ? storeLoanReceiveList.number+1:0}} of {{storeLoanReceiveList.totalPages}}</span>
                        <a href="" ng-click="storeDetailsVm.previousPage()"
                           ng-class="{'disabled': storeLoanReceiveList.first}"><i
                                class="fa fa-arrow-circle-left mr10"></i></a>
                        <a href="" ng-click="storeDetailsVm.nextPage()"
                           ng-class="{'disabled': storeLoanReceiveList.last}"><i
                                class="fa fa-arrow-circle-right"></i>
                        </a>
                </span>
                <span ng-if="selectedStoreDetailsTab == 'details.stockReturn'">
                    <span class="mr10">Page {{stockReturns.totalElements != 0 ? stockReturns.number+1:0}} of {{stockReturns.totalPages}}</span>
                        <a href="" ng-click="storeDetailsVm.previousPage()"
                           ng-class="{'disabled': stockReturns.first}"><i
                                class="fa fa-arrow-circle-left mr10"></i></a>
                        <a href="" ng-click="storeDetailsVm.nextPage()"
                           ng-class="{'disabled': stockReturns.last}"><i
                                class="fa fa-arrow-circle-right"></i>
                        </a>
                </span>
            </div>
        </div>
    </div>
</div>
