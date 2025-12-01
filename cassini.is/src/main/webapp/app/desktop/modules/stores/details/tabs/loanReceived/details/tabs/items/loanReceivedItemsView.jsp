<style scoped>

    .popover table {
        width: 497px;
        max-width: 100% !important;
    }

    .popover.bottom > .arrow::after {
        border-bottom-color: #f7f7f7;
    }

</style>
<div class="responsive-table">
    <table class="table table-striped highlight-row">
        <thead>
        <tr>
            <th ng-show="onViewOrder">Actions</th>
            <th style="width: 150px; text-align: left">Item Number</th>
            <th style="width: 150px; text-align: left">Item Name</th>
            <th style="width: 200px; text-align: left">Item Description</th>
            <th style="width: 150px; text-align: left">Type</th>
            <%--<th style="width: 150px; text-align: center">Inventory</th>--%>
            <th style="width: 150px; text-align: center">Loan Quantity</th>
            <th style="width: 150px; text-align: center">Returned Quantity</th>
            <th style="width: 150px; text-align: center">Balance Quantity</th>
        </tr>
        </thead>
        <tbody>
        <tr ng-if="loanReceiveItemsList.content.length == 0">
            <td colspan="11">No items</td>
        </tr>
        <tr data-ng-repeat="item in loanReceiveItemsList.content">
            <td style="width: 150px; text-align: left">
                <%--<a href="" ng-click="loanItemsVm.showItemDetails(item)" title="Click to show Details">--%>{{item.itemNumber}}<%--</a>--%>
            </td>
            <td style="vertical-align: middle;width: 200px;max-width:200px !important;text-overflow: ellipsis;white-space: nowrap;overflow: hidden;"
                title="{{item.itemName}}">
                <span>{{item.itemName}}</span>

            </td>

            <td style="vertical-align: middle;width: 200px;max-width:200px !important;text-overflow: ellipsis;white-space: nowrap;overflow: hidden;"
                title="{{item.description}}">
                <span>{{item.description}}</span>
            </td>

            <td style="width: 150px; text-align: left">
                <span>{{item.itemType}}</span>
            </td>

            <%--<td style="width: 150px; text-align: center">--%>
            <%--<span>{{item.inventory}}</span>--%>
            <%--</td>--%>

            <td style="width: 150px; text-align: center">
                <span>{{item.itemIssueQuantity}}</span>
            </td>

            <td style="width: 150px; text-align: center; cursor: pointer" ng-if="item.hasHistory">
                <a title="Click to view item return history" class="text-danger"
                   ng-if="item.hasHistory"
                   uib-popover-template="loanItemsVm.itemReurnedHistoryFilePopover.templateUrl"
                   popover-append-to-body="true"
                   popover-popup-delay="50"
                   popover-placement="bottom-left"
                   popover-title="Item returned history"
                   popover-trigger="'outsideClick'"><span>{{item.itemReturnQuantity}}</span></a>
            </td>

            <td style="width: 150px; text-align: center" ng-if="!item.hasHistory">
                <span>{{item.itemReturnQuantity}}</span>
            </td>

            <td style="width: 150px; text-align: center">
                <span>{{item.itemIssueQuantity - item.itemReturnQuantity}}</span>
            </td>

        </tr>
        </tbody>
    </table>
</div>
