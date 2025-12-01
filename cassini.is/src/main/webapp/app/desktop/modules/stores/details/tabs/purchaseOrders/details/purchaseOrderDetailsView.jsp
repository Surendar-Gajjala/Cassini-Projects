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
                <button class="btn btn-sm btn-default min-width" ng-click="purchaseOrderDetailsVm.back()">Back</button>
                <button ng-if="purchaseOrderDetailsVm.addItem == true && purchaseOrderDetailsVm.purchaseOrder.status != 'APPROVED'"
                        ng-disabled="hasPermission('permission.purchaseOrders.addPurchaseOrderItems') == false"
                        class="min-width btn btn-sm btn-warning" ng-click="purchaseOrderDetailsVm.addItems()">Add Items
                </button>
                <button ng-if="groupedItemsPurchaseOrder.length > 0 && purchaseOrderDetailsVm.purchaseOrder.status != 'APPROVED' && !purchaseOrderDetailsVm.showApproveButton"
                        class="min-width btn btn-sm btn-default"
                        ng-click="purchaseOrderDetailsVm.updatePurchaseOrder()">Update
                </button>

                <button ng-if="purchaseOrderDetailsVm.addItem == true" class="min-width btn btn-sm btn-warning"
                        ng-click="purchaseOrderDetailsVm.printPurchaseOrderChallan()">Print
                </button>
                <button ng-if="groupedItemsPurchaseOrder.length > 0  && purchaseOrderDetailsVm.showApproveButton"
                        ng-show="purchaseOrder.status != 'APPROVED'"
                        class="min-width btn btn-sm btn-default"
                        ng-disabled="hasPermission('permission.purchaseOrders.approvePurchaseOrder') == false"
                        ng-click="purchaseOrderDetailsVm.approvePurchaseOrder()">Approve
                </button>
                <%-- <button class="btn btn-sm btn-default"
                         ng-show="purchaseOrderDetailsVm.tabs.basic.active"
                         &lt;%&ndash;ng-disabled="groupedItemsPurchaseOrder.length > 0 || purchaseOrder.status != 'APPROVED' || purchaseOrderDetailsVm.showApproveButton"&ndash;%&gt;
                         ng-click="purchaseOrderDetailsVm.approvePurchaseOrder()">Approve
                 </button>--%>
            </div>
        </div>
        <h4 style="float: right;margin: 0;padding-right: 10px;">Store Details : {{title}}</h4>
    </div>
</div>
<div class="view-content no-padding" style="overflow-y: hidden;">
    <div class="row row-eq-height" style="margin: 0;">
        <div class="col-sm-12" style="padding: 10px;">
            <div class="item-details-tabs">
                <uib-tabset active="purchaseOrderDetailsVm.activeTab">
                    <uib-tab heading="{{purchaseOrderDetailsVm.tabs.basic.heading}}"
                             select="purchaseOrderDetailsVm.detailsTabActivated(purchaseOrderDetailsVm.tabs.basic.id)">
                        <div ng-include="purchaseOrderDetailsVm.tabs.basic.template"
                             ng-controller="PurchaseOrderBasicDetailsController as purchaseOrderBasicDetailsVm"></div>
                    </uib-tab>
                    <uib-tab heading="{{purchaseOrderDetailsVm.tabs.items.heading}}" ng-if="hasPermission('permission.purchaseOrders.viewPurchaseOrderItems') || hasPermission('permission.purchaseOrders.addPurchaseOrderItems') ||
                                                                                                   hasPermission('permission.purchaseOrders.removePurchaseOrderItems') || hasPermission('permission.purchaseOrders.editPurchaseOrderItemQty')"
                             select="purchaseOrderDetailsVm.detailsTabActivated(purchaseOrderDetailsVm.tabs.items.id)">
                        <div ng-include="purchaseOrderDetailsVm.tabs.items.template"
                             ng-controller="PurchaseOrderRequisitionItemsController as purchaseOrderRequisitionItemsVm"></div>
                    </uib-tab>
                </uib-tabset>
            </div>
        </div>
    </div>
</div>
</div>
