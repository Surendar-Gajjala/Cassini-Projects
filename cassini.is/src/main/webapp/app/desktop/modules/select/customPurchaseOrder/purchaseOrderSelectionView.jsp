<style scoped>

    a.disabled {
        cursor: not-allowed;
        color: lightgrey;
    }

</style>
<div class="view-container" fitcontent>
    <div class="view-toolbar selector" style="top: 30px; background-color: white">
        <free-text-search on-clear="purchaseOrderSelectVm.resetPage"
                          on-search="purchaseOrderSelectVm.freeTextSearch"></free-text-search>
        <div class="pull-right text-center" style="padding: 10px;margin-top: -5px;">
            <div>
                        <span>
                            <medium>
                                <span style="margin-right: 10px;">
                                        Displaying {{purchaseOrderSelectVm.purchaseOrders.numberOfElements}} of
                                            {{purchaseOrderSelectVm.purchaseOrders.numberOfElements}}
                                    </span>
                            </medium>
                        </span>
                <span class="mr10">Page {{purchaseOrderSelectVm.purchaseOrders.totalElements != 0 ? purchaseOrderSelectVm.purchaseOrders.number+1:0}} of {{purchaseOrderSelectVm.purchaseOrders.totalPages}}</span>
                <a href="" ng-click="purchaseOrderSelectVm.previousPage()"
                   ng-class="{'disabled': purchaseOrderSelectVm.purchaseOrders.first}"><i
                        class="fa fa-arrow-circle-left mr10"></i></a>
                <a href="" ng-click="purchaseOrderSelectVm.nextPage()"
                   ng-class="{'disabled': purchaseOrderSelectVm.purchaseOrders.last}"><i
                        class="fa fa-arrow-circle-right"></i></a>
            </div>
        </div>
    </div>

    <div class="view-content no-padding" style="overflow-y: auto;padding: 10px !important;">

        <table class="table table-striped highlight-row">
            <thead>
            <tr>
                <th style="width: 40px;">Select</th>
                <th>Purchase Order Number</th>
                <th>Store</th>
                <th>Supplier</th>
                <th>Status</th>
                <th>Notes</th>
            </tr>
            </thead>

            <tbody>
            <tr ng-if="purchaseOrderSelectVm.loading == true">
                <td colspan="12">
                    <span style="font-size: 15px;">
                        <img src="app/assets/bower_components/cassini-platform/images/loaders/loader19.gif"
                             class="mr5">
                        <span translate>Loading Purchase Orders..</span>
                    </span>
                </td>
            </tr>
            <tr ng-if="purchaseOrderSelectVm.loading == false && purchaseOrderSelectVm.purchaseOrders.content.length == 0">
                <td colspan="12">
                    <span translate>No Purchase Orders are available to view</span>
                </td>
            </tr>

            <tr ng-repeat="purchaseOrder in purchaseOrderSelectVm.purchaseOrders.content | filter: search"
                ng-click="purchaseOrder.isChecked = !purchaseOrder.isChecked; purchaseOrderSelectVm.radioChange(purchaseOrder, $event)"
                ng-dblClick="purchaseOrder.isChecked = !purchaseOrder.isChecked; purchaseOrderSelectVm.selectRadioChange(purchaseOrder, $event)">
                <td style="width: 40px;">
                    <input type="radio" ng-checked="purchaseOrder.isChecked" name="purchaseOrder" value="purchaseOrder"
                           ng-dblClick="purchaseOrderSelectVm.selectRadioChange(purchaseOrder, $event)"
                           ng-click="purchaseOrderSelectVm.radioChange(purchaseOrder, $event)">
                </td>
                <td>{{purchaseOrder.poNumber}}</td>
                <td>{{purchaseOrder.storeObject.storeName}}</td>
                <td>{{purchaseOrder.supplier}}</td>
                <td>
              <span style="color: white;font-size: 12px;" class="label" ng-class=" {
                                    'label-success': purchaseOrder.status == 'NEW',
                                    'label-info': purchaseOrder.status == 'APPROVED'}">
                            {{purchaseOrder.status}}
                        </span>
                </td>
                <td title="{{purchaseOrder.notes}}">{{purchaseOrder.notes | limitTo: 12}}{{purchaseOrder.notes.length >
                    12 ? '...' : ''}}
                </td>
            </tr>
            </tbody>
        </table>
    </div>
</div>