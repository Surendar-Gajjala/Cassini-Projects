<style scoped>
    .table {
        border-spacing: 0;
        border-collapse: unset;
    }

</style>

<div class="responsive-table" style="overflow: auto;">
    <table class="table table-striped highlight-row">
        <thead>
        <tr>
            <th style="width: 150px; text-align: left">Item Number</th>
            <th style="width: 150px; text-align: left">Item Name</th>
            <th style="width: 200px; text-align: left">Item Description</th>
            <th style="width: 150px; text-align: left">Item Type</th>
            <th style="width: 150px; text-align: center">Requested Qty</th>
            <th style="width: 150px; text-align: left">Notes</th>
            <th ng-if="item.isNew"></th>
        </tr>
        </thead>
        <tbody>
        <tr ng-if="storeRequisitionItemsList.content.length == 0 && reqItemsVm.loading == false">
            <td colspan="25">No Items are available to view</td>
        </tr>
        <tr ng-if="reqItemsVm.loading == true">
            <td colspan="25">
                                    <span style="font-size: 15px;">
                                        <img src="app/assets/bower_components/cassini-platform/images/loaders/loader19.gif"
                                             class="mr5">Loading Items..
                                    </span>
            </td>
        </tr>
        <tr data-ng-repeat="item in storeRequisitionItemsList.content">

            <td style="width: 150px; text-align: left;vertical-align: middle">
                <span><%--<a href="" ng-click="reqItemsVm.showItemDetails(item)" title="Click to show Details">--%>{{item.materialItem.itemNumber}}<%--</a>--%></span>
            </td>

            <td style="vertical-align: middle;width: 200px;max-width:200px !important;text-overflow: ellipsis;white-space: nowrap;overflow: hidden;"
                title="{{item.materialItem.itemName}}">
                <span>{{item.materialItem.itemName}}</span>
            </td>

            <td style="vertical-align: middle;width: 200px;max-width:250px !important;text-overflow: ellipsis;white-space: nowrap;overflow: hidden;"
                title="{{item.materialItem.description}}">
        <span>
          {{item.materialItem.description | limitTo: 12}}{{item.materialItem.description.length > 12 ? '...' : ''}}
        </span>

            </td>

            <td style="width: 150px; text-align: left">
                <span>{{item.materialItem.itemType.name}}</span>
            </td>

            <td ng-if="!item.isNew" style="width: 150px; text-align: center;vertical-align: middle">
                <span>{{item.quantity}}</span>
            </td>

            <td title="{{item.notes}}" ng-if="!item.isNew"
                style="vertical-align: middle;width: 200px;max-width:200px !important;text-overflow: ellipsis;white-space: nowrap;overflow: hidden;"
                title="{{item.materialItem.description}}">
                {{item.notes | limitTo: 12}}{{item.notes.length > 12 ? '...' : ''}}
            </td>

            <td ng-if="item.isNew" style="text-align: center">
                <input placeholder="Quantity" class="btn-sm form-control" type="number" min="1"
                       data-ng-model="item.quantity">
            </td>
            <td style="min-width: 10px;text-align: center" ng-if="item.isNew">
                <input placeholder="Notes" class="form-control btn-sm" type="text"
                       data-ng-model="item.notes">
            </td>

            <td ng-if="item.isNew" style="vertical-align: middle">
                <i title="Remove"
                   class="fa fa-minus-circle"
                   style="font-size: 20px;"
                   ng-click="reqItemsVm.removeFromItemList(item)"
                   aria-hidden="true"></i>
            </td>

        </tr>
        </tbody>
    </table>
</div>
