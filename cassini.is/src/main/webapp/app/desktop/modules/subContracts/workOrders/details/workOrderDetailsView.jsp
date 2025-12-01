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

    a.disabled {
        cursor: not-allowed;
        color: lightgrey;
    }

</style>
<div id="loan-container" class="view-container" fitcontent>
    <div class="view-toolbar" style="margin: 0px;">
        <div class="row" style="margin: 0">
            <div class="btn-group">
                <button class="btn btn-sm btn-default min-width" ng-click="workOrderVm.back()">Back</button>
                <button ng-if="workOrderVm.tabs.items.active && workOrder.status != 'FINISHED' "
                        class="min-width btn btn-sm btn-warning"
                        ng-click="workOrderVm.newWOItem()">Add
                </button>
                <button class="min-width btn btn-sm btn-warning"
                        ng-click="workOrderVm.showAttributes()">Show Attributes
                </button>
            </div>
        </div>
    </div>
    <div id="loan-content" class="view-content" style="overflow-y: hidden;padding: 10px;">
        <div class="item-details-tabs">
            <uib-tabset active="workOrderVm.tabActive">
                <uib-tab heading="{{workOrderVm.tabs.basic.heading}}"
                         active="workOrderVm.tabs.basic.active"
                         select="workOrderVm.workOrderDetailsTabActivated(workOrderVm.tabs.basic.id)">
                    <div ng-include="workOrderVm.tabs.basic.template"
                         ng-controller="WorkOrderBasicController as basicVm"></div>
                </uib-tab>
                <uib-tab heading="{{workOrderVm.tabs.items.heading}}"
                         active="workOrderVm.tabs.items.active"
                         disable="!hasPermission('permission.workOrders.items')"
                         select="workOrderVm.workOrderDetailsTabActivated(workOrderVm.tabs.items.id)">
                    <div ng-include="workOrderVm.tabs.items.template"
                         ng-controller="WorkOrderItemsController as itemsVm"></div>
                </uib-tab>
            </uib-tabset>
        </div>
    </div>
    <div class="table-footer" id="detailsFooter">
        <div class="col-md-12" style="margin-top: 5px;" ng-if="selectedWorkOrderDetailsTab != 'details.basic'">
            <div class="col-md-6">
        <span ng-if="selectedWorkOrderDetailsTab == 'details.items'" style="font-weight: 700">
                    Displaying {{workOrderItems.numberOfElements}} of {{workOrderItems.totalElements}}</span>
            </div>
            <div class="col-md-6 text-right">
        <span ng-if="selectedWorkOrderDetailsTab == 'details.items'">
          <span class="mr10">Page {{workOrderItems.totalElements != 0 ? workOrderItems.number+1:0}} of {{workOrderItems.totalPages}}</span>
          <a href="" ng-click="workOrderVm.previousPage()" ng-class="{'disabled': workOrderItems.first}"><i
                  class="fa fa-arrow-circle-left mr10"></i></a>
          <a href="" ng-click="workOrderVm.nextPage()" ng-class="{'disabled': workOrderItems.last}"><i
                  class="fa fa-arrow-circle-right"></i></a>
        </span>
            </div>
        </div>
    </div>
</div>

