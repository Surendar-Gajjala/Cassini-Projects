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
            <th style="width: 150px; text-align: center">Returned Qty</th>
            <th style="width: 150px; text-align: left">Notes</th>
        </tr>
        </thead>
        <tbody>
        <tr ng-if="stockReturnItemsList.content.length == 0 && stockReturnItemsVm.loading == false">
            <td colspan="25">No Items are available to view</td>
        </tr>
        <tr ng-if="stockReturnItemsVm.loading == true">
            <td colspan="25">
                                    <span style="font-size: 15px;">
                                        <img src="app/assets/bower_components/cassini-platform/images/loaders/loader19.gif"
                                             class="mr5">Loading Items..
                                    </span>
            </td>
        </tr>
        <tr data-ng-repeat="item in stockReturnItemsList.content" ng-if="stockReturnItemsVm.loading == false">

            <td style="width: 150px; text-align: left;vertical-align: middle">
                <span><%--<a href="" ng-click="stockReturnItemsVm.showItemDetails(item)" title="Click to show Details">--%>{{item.itemNumber}}<%--</a>--%></span>
            </td>

            <td style="vertical-align: middle;width: 200px;max-width:200px !important;text-overflow: ellipsis;white-space: nowrap;overflow: hidden;"
                title="{{item.itemName}}">
                <span>{{item.itemName}}</span>
            </td>

            <td style="vertical-align: middle;width: 200px;max-width:250px !important;text-overflow: ellipsis;white-space: nowrap;overflow: hidden;"
                title="{{item.description}}">
        <span>
          {{item.description | limitTo: 12}}{{item.description.length > 12 ? '...' : ''}}
        </span>

            </td>

            <td style="width: 150px; text-align: left">
                <span>{{item.itemType}}</span>
            </td>

            <td style="width: 150px; text-align: center;vertical-align: middle">
                <span>{{item.itemReturnQuantity}}</span>
            </td>

            <td title="{{item.notes}}"
                style="vertical-align: middle;width: 200px;max-width:200px !important;text-overflow: ellipsis;white-space: nowrap;overflow: hidden;"
                title="{{item.description}}">
                {{item.notes | limitTo: 12}}{{item.notes.length > 12 ? '...' : ''}}
            </td>

        </tr>
        </tbody>
    </table>
</div>
