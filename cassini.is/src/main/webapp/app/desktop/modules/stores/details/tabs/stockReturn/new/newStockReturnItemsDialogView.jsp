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
    <div class="view-toolbar selector" style="background-color: white">
        <free-text-search on-clear="newStockReturnItemsVm.resetPage"
                          on-search="newStockReturnItemsVm.freeTextSearch"></free-text-search>
        <div class="pull-right text-center" style="padding: 10px;">
            <div>
        <span>
          <medium>
            <span style="margin-right: 10px;">
              Displaying {{newStockReturnItemsVm.itemList.content.length}} of {{newStockReturnItemsVm.itemList.totalElements}}
            </span>
          </medium>
        </span>
                <span class="mr10">Page {{newStockReturnItemsVm.itemList.totalElements != 0 ? newStockReturnItemsVm.itemList.number+1:0}} of {{newStockReturnItemsVm.itemList.totalPages}}</span>
                <a href="" ng-click="newStockReturnItemsVm.previousPage()"
                   ng-class="{'disabled': newStockReturnItemsVm.itemList.first || newStockReturnItemsVm.loading}"><i
                        class="fa fa-arrow-circle-left mr10"></i></a>
                <a href="" ng-click="newStockReturnItemsVm.nextPage()"
                   ng-class="{'disabled': newStockReturnItemsVm.itemList.last || newStockReturnItemsVm.loading}"><i
                        class="fa fa-arrow-circle-right"></i></a>
            </div>
        </div>
    </div>
    <div ng-if="newStockReturnItemsVm.loading == true" style="padding: 30px;">
        <br/>
                <span style="font-size: 15px;">
                    <img src="app/assets/bower_components/cassini-platform/images/loaders/loader6.gif" class="mr5">Loading Items...
                </span>
        <br/>
    </div>
    <div class="view-content" style="overflow-y: auto;padding: 10px !important;height: 185px">
        <div class="row" style="padding:20px;">
            <div class="responsive-table">
                <table class="table table-striped highlight-row" ng-if="!newStockReturnItemsVm.loading">
                    <thead>
                    <tr>
                        <th style="width: 50px;"></th>
                        <th style="width: 150px; text-align: left">Item Number</th>
                        <th style="width: 150px; text-align: left">Type</th>
                        <th style="width: 100px; text-align: left">Item Name</th>
                    </tr>
                    </thead>
                    <tbody>
                    <tr ng-if="newStockReturnItemsVm.itemList.content.length == 0 && !newStockReturnItemsVm.loading">
                        <td colspan="11">No Items are available to view</td>
                    </tr>
                    <tr data-ng-repeat="item in newStockReturnItemsVm.itemList.content "
                        ng-if="!newStockReturnItemsVm.loading">

                        <td style="width: 50px;">
            <span title="Add Items" ng-class="{'disabled': item.isAdded}"
                  ng-click="newStockReturnItemsVm.addToStockReturnItems(item)" aria-hidden="true">
            <i class="fa fa-plus-circle"></i></span>
                        </td>

                        <td style="width: 150px; text-align: left">
                            <span ng-bind-html="item.itemNumber | highlightText: freeTextQuery"></span>
                        </td>

                        <td style="width: 150px; text-align: left">
                            <span ng-bind-html="item.itemType | highlightText: freeTextQuery"></span>
                        </td>

                        <td style="width: 150px; text-align: left">
                            <span ng-bind-html="item.itemName | highlightText: freeTextQuery"></span>
                        </td>
                    </tr>
                    </tbody>
                </table>
            </div>
        </div>
    </div>
</div>
