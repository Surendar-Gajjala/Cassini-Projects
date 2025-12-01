<style scoped>
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
<div class="view-container" fitcontent>
    <div class="view-content no-padding" style="overflow-y: auto;padding: 10px;">
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
        <div class="row" style="padding:20px;">
            <div class="responsive-table">
                <table class="table table-striped highlight-row">
                    <thead>
                    <tr>
                        <th style="width: 50px;"></th>
                        <th style="width: 150px; text-align: left">Item Number</th>
                        <th style="width: 200px; text-align: left">Item Name</th>
                        <th style="width: 200px; text-align: left">Item Description</th>
                        <th style="width: 150px; text-align: left">Type</th>
                    </tr>
                    </thead>
                    <tbody>
                    <tr ng-if="reqItemsVm.loading == true">
                        <td colspan="25">
                                    <span style="font-size: 15px;">
                                        <img src="app/assets/bower_components/cassini-platform/images/loaders/loader19.gif"
                                             class="mr5">Loading Items..
                                    </span>
                        </td>
                    </tr>
                    <tr ng-if="reqItemsVm.itemList.length == 0 && reqItemsVm.loading == false">
                        <td colspan="11">No Items are available to view</td>
                    </tr>
                    <tr data-ng-repeat="item in reqItemsVm.itemList | filter: searchAll">

                        <td width="50px;">
                            <i class="fa fa-plus-circle"
                               title="Add Items"
                               ng-click="reqItemsVm.addToRequisitionItems(item)"
                               aria-hidden="true"></i>
                        </td>

                        <td style="width: 150px; text-align: left">
                            <span>{{item.itemNumber}}</span>
                        </td>

                        <td style="vertical-align: middle;width: 150px;max-width:150px !important;text-overflow: ellipsis;white-space: nowrap;overflow: hidden;"
                            title="{{item.itemName}}">
                            {{item.itemName}}
                        </td>

                        <td style="vertical-align: middle;width: 200px;max-width:200px !important;text-overflow: ellipsis;white-space: nowrap;overflow: hidden;"
                            title="{{item.description}}">
                            <span>{{item.description}}</span>

                        </td>

                        <td style="width: 150px; text-align: left">
                            <span>{{item.itemType.name}}</span>

                        </td>

                    </tr>
                    </tbody>
                </table>
            </div>
        </div>
    </div>
</div>
