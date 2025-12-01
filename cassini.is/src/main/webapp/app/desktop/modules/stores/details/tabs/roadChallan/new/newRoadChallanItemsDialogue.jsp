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
        <free-text-search on-clear="roadChallanItemsVm.resetPage"
                          on-search="roadChallanItemsVm.freeTextSearch"></free-text-search>
        <div class="pull-right text-center" style="padding: 10px;margin-top: -5px;"
        <%-- ng-if="!roadChallanItemsVm.clear"--%>>
            <div>
                        <span>
                            <medium>
                                <span style="margin-right: 10px;">
                                        Displaying {{roadChallanItemsVm.items.content.length}} of
                                            {{roadChallanItemsVm.items.totalElements}}
                                    </span>
                            </medium>
                        </span>
                <span class="mr10">Page {{roadChallanItemsVm.items.totalElements != 0 ? roadChallanItemsVm.items.number+1:0}} of {{roadChallanItemsVm.items.totalPages}}</span>
                <a href="" ng-click="roadChallanItemsVm.previousPage()"
                   ng-class="{'disabled': roadChallanItemsVm.items.first || roadChallanItemsVm.loading}"><i
                        class="fa fa-arrow-circle-left mr10"></i></a>
                <a href="" ng-click="roadChallanItemsVm.nextPage()"
                   ng-class="{'disabled': roadChallanItemsVm.items.last || roadChallanItemsVm.loading}"><i
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
                    <tr ng-if="roadChallanItemsVm.loading == true">
                        <td colspan="10">
                            <span style="font-size: 15px;">
                                <img src="app/assets/images/loaders/loader19.gif" class="mr5">Loading Items..
                            </span>
                        </td>
                    </tr>
                    <tr ng-if="roadChallanItemsVm.roadChallanItems.length == 0 && roadChallanItemsVm.loading == false">
                        <td colspan="25">No Items are available to view</td>
                    </tr>

                    <tr data-ng-repeat="item in roadChallanItemsVm.roadChallanItems"
                        ng-if="roadChallanItemsVm.loading == false && !item.isAdded ">

                        <td style="width: 50px;">
                        <span title="Add Items" ng-click="roadChallanItemsVm.addToRoadChallanItems(item)"
                              ng-class="{'disabled': item.isAdded}" aria-hidden="true">
                            <i class="fa fa-plus-circle"></i>
                        </span>
                        </td>

                        <td style="width: 150px; text-align: left">
                            <span>{{item.stockMovementDTO.itemDTO.itemNumber}}</span>
                        </td>

                        <td style="width: 150px; text-align: left">
                            <span>{{item.stockMovementDTO.itemDTO.materialType.name}}</span>
                        </td>

                        <td style="vertical-align: middle;width: 200px;max-width:250px !important;text-overflow: ellipsis;white-space: nowrap;overflow: hidden;"
                            title="{{item.stockMovementDTO.itemDTO.itemName}}">
                            <span>{{item.stockMovementDTO.itemDTO.itemName}}</span>

                        </td>


                    </tr>
                    </tbody>
                </table>
            </div>
        </div>
    </div>
</div>
