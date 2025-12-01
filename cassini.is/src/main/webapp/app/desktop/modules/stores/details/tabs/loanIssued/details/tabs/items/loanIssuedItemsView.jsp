<div class="responsive-table">
    <table class="table table-striped">
        <thead>
        <tr>
            <th ng-show="onViewOrder">Actions</th>
            <th style="width: 150px; text-align: left">Item Number</th>
            <th style="width: 150px; text-align: left">Item Name</th>
            <th style="width: 200px; text-align: left">Item Description</th>
            <th style="width: 150px; text-align: left">Type</th>
            <%--<th style="width: 150px; text-align: center">Inventory</th>--%>
            <th style="width: 150px; text-align: center">Loan Qty</th>
        </tr>
        </thead>
        <tbody>
        <tr ng-if="loanIssuedItemsList.content.length == 0">
            <td colspan="11">No Items</td>
        </tr>
        <tr data-ng-repeat="item in loanIssuedItemsList.content">
            <td style="width: 150px; text-align: left">
                <%--<a href="" ng-click="loanItemsVm.showItemDetails(item)" title="Click to show Details">--%>{{item.itemDTO.itemNumber}}<%--</a>--%>
            </td>

            <td style="vertical-align: middle;width: 150px;max-width:150px !important;text-overflow: ellipsis;white-space: nowrap;overflow: hidden;"
                title="{{item.itemDTO.itemName}}">
                <span>{{item.itemDTO.itemName}}</span>

            </td>

            <td style="vertical-align: middle;width: 200px;max-width:200px !important;text-overflow: ellipsis;white-space: nowrap;overflow: hidden;"
                title="{{item.itemDTO.description}}">
                <span>{{item.itemDTO.description}}</span>
            </td>

            <td style="width: 150px; text-align: left">
                <span>{{item.itemDTO.itemType}}</span>
            </td>

            <%--<td style="width: 150px; text-align: center">--%>
            <%--<span>{{item.inventory}}</span>--%>
            <%--</td>--%>

            <td style="width: 150px; text-align: center">
                <span>{{item.quantity}}</span>

            </td>

        </tr>
        </tbody>
    </table>
</div>
