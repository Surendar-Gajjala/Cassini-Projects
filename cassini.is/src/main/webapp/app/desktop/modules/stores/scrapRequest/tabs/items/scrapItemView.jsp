<%--
  Created by IntelliJ IDEA.
  User: Varsha Malgireddy
  Date: 8/29/2018
  Time: 10:23 AM
  To change this template use File | Settings | File Templates.
--%>
<style>
    .table {
        border-spacing: 0;
        border-collapse: unset;
    }

</style>
<div class="responsive-table">
    <table class="table table-striped">
        <thead>
        <tr>
            <th ng-show="onViewOrder">Actions</th>
            <th style="width: 150px; text-align: left">Item Number</th>
            <th style="width: 150px; text-align: left">Item Name</th>
            <th style="width: 200px; text-align: left">Item Description</th>
            <th style="width: 150px; text-align: center">Scrap Qty</th>
        </tr>
        </thead>
        <tbody>
        <tr ng-if="scrapItemVm.items.length == 0">
            <td colspan="25">No Items are available to view</td>
        </tr>
        <tr data-ng-repeat="item in scrapItemVm.items">
            <td style="width: 150px; text-align: left">
                {{item.itemObject.itemNumber}}
            </td>
            <td style="vertical-align: middle;width: 150px;max-width:150px !important;text-overflow: ellipsis;white-space: nowrap;overflow: hidden;"
                title="{{item.itemObject.itemName}}">
                <span>{{item.itemObject.itemName}}</span>

            </td>
            <td style="vertical-align: middle;width: 200px;max-width:200px !important;text-overflow: ellipsis;white-space: nowrap;overflow: hidden;"
                title="{{item.itemObject.description}}">
                <span>{{item.itemObject.description}}</span>
            </td>

            <td style="width: 150px; text-align: center" ng-if="hasPermission('permission.topStores.editScrapRequest')">
                {{item.quantity}}
            </td>
            <td style="width: 150px; text-align: center"
                ng-if="!hasPermission('permission.topStores.editScrapRequest')">
                <a href="#" editable-text="item.quantity"
                   onaftersave="scrapItemVm.updateScrap(item)">{{item.quantity}}</a>

            </td>

        </tr>
        </tbody>
    </table>
</div>
