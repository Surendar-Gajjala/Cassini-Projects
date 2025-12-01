<style>
    .nav-tabs > .uib-tab.active > a, .nav-tabs > .uib-tab.active > a:focus, .nav-tabs > .uib-tab.active > a:hover {
        border: 0 !important;
        border-color: #30a82a !important;
        border-bottom: 3px solid #2a6fa8 !important;
    }

    #loan-container #loan-content {
        padding: 10px;
        position: relative;
        bottom: 40px;
        top: 0;
        overflow: auto;
    }

    #loan-container .table-footer {
        padding: 0 10px 0 10px;
        position: relative;
        bottom: 0;
        height: 40px;
        width: 100%;
        border-top: 1px solid #D3D7DB;
        display: table;
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
<div id="loan-container" class="view-container" fitcontent>
    <div class="view-toolbar" style="margin: 0px;">
        <div class="row" style="margin: 0">
            <div class="btn-group">
                <button class="btn btn-sm btn-default min-width" ng-click="loanDetailsVm.back()">Back</button>
                <%--<button ng-if="loanDetailsVm.addItem == true" class="min-width btn btn-sm btn-warning"--%>
                <%--ng-disabled="loanDetailsVm.loan.status == 'RETURNED'"--%>
                <%--ng-disabled="hasPermission('permission.loanIssued.issueLoanItems') == false"--%>
                <%--ng-click="loanDetailsVm.addItems()">Add Items--%>
                <%--</button>--%>
                <button ng-if="loanDetailsVm.addItem == true && loanIssuedItemsList.content.length > 0"
                        class="min-width btn btn-sm btn-warning"
                        ng-click="loanDetailsVm.printLoanIssueChallan()">Print
                </button>
            </div>
            <h4 style="float: right;margin: 0;padding-right: 10px;">Store Details : {{title}}</h4>
        </div>


    </div>
    <div id="loan-content" class="view-content no-padding" style="overflow-y: hidden;padding: 10px;">
        <div class="item-details-tabs">
            <uib-tabset active="loanDetailsVm.activeTab">
                <uib-tab heading="{{loanDetailsVm.tabs.basic.heading}}"
                         select="loanDetailsVm.detailsTabActivated(loanDetailsVm.tabs.basic.id)">
                    <div ng-include="loanDetailsVm.tabs.basic.template"
                         ng-controller="LoanBasicController as loanBasicVm"></div>
                </uib-tab>
                <uib-tab heading="{{loanDetailsVm.tabs.items.heading}}"
                         select="loanDetailsVm.detailsTabActivated(loanDetailsVm.tabs.items.id)">
                    <div ng-include="loanDetailsVm.tabs.items.template"
                         ng-controller="LoanItemsController as loanItemsVm"></div>
                </uib-tab>
            </uib-tabset>
        </div>
    </div>
    <div class="table-footer" id="detailsFooter">
        <div class="col-md-12" style="margin-top: 5px;" ng-if="selectedLoanIssuedDetailsTab != 'details.basic'">
            <div class="col-md-6">
        <span ng-if="selectedLoanIssuedDetailsTab == 'details.items'" style="font-weight: 700">
                    Displaying {{loanIssuedItemsList.numberOfElements}} of {{loanIssuedItemsList.totalElements}}</span>
            </div>
            <div class="col-md-6 text-right">
        <span ng-if="selectedLoanIssuedDetailsTab == 'details.items'">
                    <span class="mr10">Page {{loanIssuedItemsList.totalElements != 0 ? loanIssuedItemsList.number+1:0}} of {{loanIssuedItemsList.totalPages}}</span>
                        <a href="" ng-click="loanDetailsVm.previousPage()"
                           ng-class="{'disabled': loanIssuedItemsList.first}"><i
                                class="fa fa-arrow-circle-left mr10"></i></a>
                        <a href="" ng-click="loanDetailsVm.nextPage()"
                           ng-class="{'disabled': loanIssuedItemsList.last}"><i
                                class="fa fa-arrow-circle-right"></i></a>
        </span>
            </div>
        </div>
    </div>
</div>
