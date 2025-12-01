<style>
    a.disabled {
        cursor: not-allowed;
        color: lightgrey;
    }

    span.disabled {
        cursor: not-allowed;
        color: lightgrey;
    }

    #rightSidePanelContent {
        overflow: hidden !important;
        height: auto !important;
    }

    .app-side-panel {
        overflow: hidden !important;
    }
</style>
<div class="view-container" fitcontent>
    <div class="view-toolbar selector" style="top: 30px; background-color: white">
        <free-text-search on-clear="newReqItemsVm.resetPage"
                          on-search="newReqItemsVm.freeTextSearch"></free-text-search>
        <div class="pull-right text-center" style="padding: 10px;margin-top: -5px;">
            <div>
                        <span>
                            <medium>
                                <span style="margin-right: 10px;">
                                        Displaying {{newReqItemsVm.itemList.content.length}} of
                                            {{newReqItemsVm.itemList.totalElements}}
                                    </span>
                            </medium>
                        </span>
                <span class="mr10">Page {{newReqItemsVm.itemList.totalElements != 0 ? newReqItemsVm.itemList.number+1:0}} of {{newReqItemsVm.itemList.totalPages}}</span>
                <a href="" ng-click="newReqItemsVm.previousPage()"
                   ng-class="{'disabled': newReqItemsVm.itemList.first || newReqItemsVm.loading}"><i
                        class="fa fa-arrow-circle-left mr10"></i></a>
                <a href="" ng-click="newReqItemsVm.nextPage()"
                   ng-class="{'disabled': newReqItemsVm.itemList.last || newReqItemsVm.loading}"><i
                        class="fa fa-arrow-circle-right"></i></a>
            </div>
        </div>
    </div>
    <div class="view-content" style="overflow-y: auto;padding: 10px !important;height: 185px">
        <div class="row" style="padding:20px;">
            <div class="responsive-table">
                <table class="table table-striped">
                    <thead>
                    <tr>
                        <th style="width: 50px;"></th>
                        <th style="width: 150px; text-align: left">Item Number</th>
                        <th style="width: 150px; text-align: left">Type</th>
                        <th style="width: 200px; text-align: left">Item Name</th>
                    </tr>
                    </thead>
                    <tbody>
                    <tr ng-if="newReqItemsVm.loading == true">
                        <td colspan="10">
                            <span style="font-size: 15px;">
                                <img src="app/assets/images/loaders/loader19.gif" class="mr5">Loading Items..
                            </span>
                        </td>
                    </tr>
                    <tr ng-if="newReqItemsVm.itemList.content.length == 0 && !newReqItemsVm.loading">
                        <td colspan="11">No Items are available to view</td>
                    </tr>
                    <tr data-ng-repeat="item in newReqItemsVm.itemList.content"
                        ng-if="!newReqItemsVm.loading && !item.isAdded ">

                        <td style="width: 50px;">
                        <span title="Add Item" ng-click="newReqItemsVm.addToRequisitionItems(item)"
                              ng-class="{'disabled': item.isAdded}" aria-hidden="true">
                            <i class="fa fa-plus-circle"></i>
                        </span>
                        </td>

                        <td style="width: 150px; text-align: left">
                            <span>{{item.itemNumber}}</span>
                        </td>

                        <td style="width: 150px; text-align: left">
                            <span>{{item.itemType.name}}</span>
                        </td>

                        <td ng-if="item.description == null"
                            style="vertical-align: middle;width: 200px;max-width:250px !important;text-overflow: ellipsis;white-space: nowrap;overflow: hidden;"
                            title="{{item.itemName}}">
                            {{item.itemName}}
                        </td>
                        <td ng-if="item.description != null"
                            style="vertical-align: middle;width: 200px;max-width:250px !important;text-overflow: ellipsis;white-space: nowrap;overflow: hidden;"
                            title="{{item.description}}">
                            <span>{{item.description}}</span>

                        </td>

                    </tr>
                    </tbody>
                </table>
            </div>
        </div>
    </div>
</div>
