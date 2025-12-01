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
                <i class="fa fa-times-circle clear-search" title="Clear search"
                   ng-click="searchAll = null"></i>
            </div>
        </div>
    </form>
    <div class="responsive-table">
        <table class="table table-striped highlight-row">
            <thead>
            <tr>
                <th style="vertical-align: middle;">Item Number</th>
                <th class="description" style="vertical-align: middle;">Item Name</th>
                <th class="description" style="vertical-align: middle;">Item <br> Description</th>
                <th style="vertical-align: middle;">Resource <br> Qty</th>
                <th style="vertical-align: middle;">Inventory</th>
                <th style="vertical-align: middle;">Issue <br> Qty</th>
                <th style="vertical-align: middle;">Shortage</th>
                <th style="vertical-align: middle;">Issued <br> Qty</th>
            </tr>
            </thead>
            <tbody>

            <tr ng-if="stockIssuedDialogVm.itemList.length == 0 && stockIssuedDialogVm.loading == false">
                <td colspan="10">
                    <span>No Items</span>
                </td>
            </tr>

            <tr ng-if="stockIssuedDialogVm.loading == true">
                <td colspan="25">
                                    <span style="font-size: 15px;">
                                        <img src="app/assets/bower_components/cassini-platform/images/loaders/loader19.gif"
                                             class="mr5">Loading Items..
                                    </span>
                </td>
            </tr>

            <tr ng-repeat="resource in stockIssuedDialogVm.itemList | filter: searchAll"
                ng-if="resource.resourceQuantity > resource.itemIssueQuantity && resource.storeInventory > 0">

                <td style="vertical-align: middle;">
                    {{resource.itemNumber }}
                </td>
                <td style="vertical-align: middle;width: 200px;max-width:200px !important;text-overflow: ellipsis;white-space: nowrap;overflow: hidden;"
                    title="{{resource.itemName}}">
                    {{resource.itemName }}
                </td>
                <td style="vertical-align: middle;width: 200px;max-width:250px !important;text-overflow: ellipsis;white-space: nowrap;overflow: hidden;"
                    title="{{resource.description}}">
                    {{resource.description}}
                </td>
                <td style="vertical-align: middle; text-align: center">
                    {{resource.resourceQuantity}}
                </td>
                <td style="vertical-align: middle; text-align: center">
                    {{resource.storeInventory}}
                </td>
                <td style="width: 50px; text-align: center; vertical-align: middle">
                    <input placeholder="Qty" class="btn-sm form-control" type="number"
                           data-ng-model="resource.Qty" style="width: 75px;"
                           ng-change="stockIssuedDialogVm.changedResourceQuantity(resource)">
                </td>

                <td style="vertical-align: middle; text-align: center">
                    {{resource.shortage }}
                </td>
                <td style="vertical-align: middle; text-align: center">
                    {{resource.itemIssueQuantity}}
                </td>
            </tr>
            </tbody>
        </table>
    </div>
</div>