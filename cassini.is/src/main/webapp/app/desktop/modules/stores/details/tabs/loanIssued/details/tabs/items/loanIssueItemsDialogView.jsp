<style scoped>
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
                <i class="fa fa-times-circle clear-search" title="Clear search"
                   ng-click="searchAll = null"></i>
            </div>
        </div>
    </form>
    <div class="responsive-table">
        <table class="table table-striped">
            <thead>
            <tr>
                <th ng-show="onViewOrder">Actions</th>
                <th style="width: 150px; text-align: left">Item Number</th>
                <th style="width: 150px; text-align: left">Item Name</th>
                <th style="width: 150px; text-align: left">Item Description</th>
                <th style="width: 120px; text-align: left">Item Type</th>
                <th style="width: 100px; text-align: center">Inventory</th>
                <th style="width: 150px; text-align: left">Loan <br> Qty</th>
            </tr>
            </thead>
            <tbody>
            <tr ng-if="loanItemsDialogVm.items.length == 0">
                <td colspan="11">No Items</td>
            </tr>
            <tr data-ng-repeat="item in loanItemsDialogVm.items | filter: searchAll">
                <td style="width: 150px; text-align: left">
                    {{item.itemDTO.itemNumber}}
                </td>

                <td style="width: 150px;max-width:150px !important;text-overflow: ellipsis;white-space: nowrap;overflow: hidden;"
                    title="{{item.itemDTO.itemName}}">
                    <span>{{item.itemDTO.itemName}}</span>
                </td>

                <td style="width: 200px;max-width:200px !important;text-overflow: ellipsis;white-space: nowrap;overflow: hidden;"
                    title="{{item.itemDTO.description}}">
                    {{item.itemDTO.description}}
                </td>

                <td style="width: 120px;max-width:120px !important;text-overflow: ellipsis;white-space: nowrap;overflow: hidden;"
                    title="{{item.itemDTO.resourceType}}">
                    <span>{{item.itemDTO.resourceType}}</span>
                </td>

                <td style="width: 100px; text-align: center">
                    <span>{{item.storeOnHand}}</span>
                </td>

                <td style="width: 150px;">
                    <input placeholder="Qty" class="btn-sm form-control" type="number" min="0" style="width: 75px;"
                           data-ng-model="item.Qty">
                </td>

            </tr>
            </tbody>
        </table>
    </div>
</div>
