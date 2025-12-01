<style>
    a.disabled {
        cursor: not-allowed;
        color: lightgrey;
    }

    span.disabled {
        cursor: not-allowed;
        color: lightgrey;
    }

    .receiveView .view-toolbar {
        padding-top: 4px !important;
    }

    #rightSidePanelContent {
        overflow: hidden !important;
        height: auto !important;
    }

    .app-side-panel {
        overflow: hidden !important;
    }
</style>
<div class="view-container receiveView" fitcontent>
    <div class="view-toolbar selector" style="background-color: white" ng-if="stockReceiveItemsVm.viewToolbar">
        <div>
            <free-text-search on-clear="stockReceiveItemsVm.resetPage"
                              on-search="stockReceiveItemsVm.freeTextSearch"></free-text-search>
            <div class="pull-right text-center" style="padding: 10px;">
                <div>
                        <span>
                            <medium>
                                <span style="margin-right: 10px;">
                                        Displaying {{stockReceiveItemsVm.itemList.content.length}} of
                                            {{stockReceiveItemsVm.itemList.totalElements}}
                                    </span>
                            </medium>
                        </span>
                    <span class="mr10">Page {{stockReceiveItemsVm.itemList.totalElements != 0 ? stockReceiveItemsVm.itemList.number+1:0}} of {{stockReceiveItemsVm.itemList.totalPages}}</span>
                    <a href="" ng-click="stockReceiveItemsVm.previousPage()"
                       ng-class="{'disabled': stockReceiveItemsVm.itemList.first || stockReceiveItemsVm.loading}"><i
                            class="fa fa-arrow-circle-left mr10"></i></a>
                    <a href="" ng-click="stockReceiveItemsVm.nextPage()"
                       ng-class="{'disabled': stockReceiveItemsVm.itemList.last || stockReceiveItemsVm.loading}"><i
                            class="fa fa-arrow-circle-right"></i></a>
                </div>
            </div>
        </div>
    </div>
    <div class="view-content" style="overflow-y: auto;padding: 10px !important;height: 185px">
        <div class="row" style="padding:10px;">
            <div class="responsive-table">
                <table class="table table-striped">
                    <thead>
                    <tr>
                        <th></th>
                        <th style="width: 150px; text-align: left">Item Number</th>
                        <th style="width: 150px; text-align: left">Item Name</th>
                        <th style="width: 150px; text-align: left">Type</th>
                        <th style="width: 200px; text-align: left">Units</th>
                    </tr>
                    </thead>
                    <tbody>
                    <tr ng-if="stockReceiveItemsVm.loading == true">
                        <td colspan="10">
                            <span style="font-size: 15px;">
                                <img src="app/assets/images/loaders/loader19.gif" class="mr5">Loading Items..
                            </span>
                        </td>
                    </tr>
                    <tr ng-if="stockReceiveItemsVm.loading == false && stockReceiveItemsVm.itemList.content.length == 0">
                        <td colspan="15">No Items are available to view</td>
                    </tr>

                    <tr data-ng-repeat="item in stockReceiveItemsVm.itemList.content"
                        ng-if="stockReceiveItemsVm.loading == false && !item.isAdded ">

                        <td>
                        <span title="Add Item" ng-click="stockReceiveItemsVm.addToReceiveItems(item)"
                              ng-class="{'disabled': item.isAdded}" aria-hidden="true">
                            <i class="fa fa-plus-circle"></i>
                        </span>
                        </td>

                        <td style="width: 150px; text-align: left">
                            <span>{{item.itemNumber}}</span>
                        </td>

                        <td style="vertical-align: middle;width: 150px;max-width:150px !important;text-overflow: ellipsis;white-space: nowrap;overflow: hidden;"
                            title="{{item.itemName}}">
                            <span>{{item.itemName}}</span>
                        </td>

                        <td style="width: 150px; text-align: left">
                            <span>{{item.itemType.name}}</span>
                        </td>

                        <td style="width: 150px; text-align: left">
                            <span>{{item.units}}</span>
                        </td>

                    </tr>
                    </tbody>
                </table>
            </div>
        </div>
    </div>
</div>
