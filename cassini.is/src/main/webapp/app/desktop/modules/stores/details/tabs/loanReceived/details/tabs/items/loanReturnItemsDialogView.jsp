<style>
    .table {
        border-spacing: 0;
        border-collapse: unset;
    }

    .search-box > input {
        width: 295px;
        border-radius: 15px !important;
        padding-left: 30px;
        height: 30px;
        display: inline-block !important;
        background-color: rgb(241, 243, 244);
        border: 1px solid #ddd;
        box-shadow: none;
    }

    .search-box > input:hover {
        background-color: rgb(229, 231, 234);
    }

    .search-box > input:focus {
        border: 1px solid #ddd !important;
        box-shadow: none;
    }

    .search-box i.fa-search {
        z-index: 4 !important;
        position: absolute;
        margin-top: 11px;
        margin-left: 10px;
        color: grey;
        opacity: 0.5;
    }

    .search-box i.clear-search {
        margin-left: -20px;
        color: gray;
        cursor: pointer;
        z-index: 4 !important;
        position: absolute;
        margin-top: 11px;
    }
</style>
<div style="overflow-y: auto;overflow-x: hidden;height: 100%;">
    <form class="col-md-12 form-inline" style="padding: 5px;border: 1px solid lightgrey; height: 50px;">
        <div style="text-align:center; top: 38px;">
            <div class="search-box">
                <i class="fa fa-search"></i>
                <input type="search" style="width:300px;"
                       class="form-control input-sm"
                       ng-model="searchAll"
                       ng-enter="onSearch(searchTerm)">
                <i class="fa fa-times-circle clear-search" title="Clear Search"
                   ng-click="searchAll = null"></i>
            </div>
        </div>
    </form>
    <div class="responsive-table">
        <table class="table table-striped">
            <thead>
            <tr>
                <th style="width: 150px; text-align: left">Item Number</th>
                <th style="width: 150px; text-align: left">Item Name</th>
                <th style="width: 150px; text-align: center">Inventory</th>
                <th style="width: 150px; text-align: center">Loan Qty</th>
                <th style="width: 150px; text-align: center">Returned <br> Qty</th>
                <th style="width: 150px; text-align: center">Balance <br> Qty</th>
                <th style="width: 150px; text-align: left">Return <br> Qty</th>
            </tr>
            </thead>
            <tbody>
            <tr ng-if="returnItemsDialogVm.items.length == 0">
                <td colspan="11">No Items</td>
            </tr>
            <tr data-ng-repeat="item in returnItemsDialogVm.items | filter: searchAll"
                ng-if="!(item.itemIssueQuantity == item.itemReturnQuantity)">

                <td style="width: 150px; text-align: left;vertical-align: middle">
                    <span>{{item.itemNumber}}</span>

                </td>
                <td style="width: 150px;max-width:150px !important;text-overflow: ellipsis;white-space: nowrap;overflow: hidden;vertical-align: middle"
                    title="{{item.itemName}}">
                    <span>{{item.itemName}}</span>
                </td>

                <td style="width: 150px; text-align: center;vertical-align: middle">
                    <span>{{item.storeInventory}}</span>
                </td>

                <td style="width: 150px; text-align: center;vertical-align: middle">
                    <span>{{item.itemIssueQuantity}}</span>
                </td>

                <td style="width: 150px; text-align: center;vertical-align: middle">
                    <span>{{item.itemReturnQuantity}}</span>
                </td>

                <td style="width: 150px; text-align: center;vertical-align: middle">
                    <span>{{item.itemIssueQuantity - item.itemReturnQuantity}}</span>
                </td>

                <td style="width: 50px">
                    <input placeholder="Qty" class="form-control input-sm" type="number"
                           style="width: 70px;text-align: center"
                           data-ng-model="item.Qty" ng-change="returnItemsDialogVm.validate(item)">
                </td>

            </tr>
            </tbody>
        </table>
    </div>
</div>
