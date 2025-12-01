<style>
    a.disabled {
        cursor: not-allowed;
        color: lightgrey;
    }

    span.disabled {
        cursor: not-allowed;
        color: lightgrey;
    }

</style>
<div class="view-container" fitcontent>
    <div class="view-toolbar selector" style="background-color: white">
        <free-text-search on-clear="scrapItemsVm.resetPage"
                          on-search="scrapItemsVm.freeTextSearch"></free-text-search>
        <div class="pull-right text-center" style="padding: 10px;">
            <div>
        <span>
          <medium>
            <span style="margin-right: 10px;">
              Displaying {{scrapItemsVm.itemList.content.length}} of {{scrapItemsVm.itemList.totalElements}}
            </span>
          </medium>
        </span>
                <span class="mr10">Page {{scrapItemsVm.itemList.totalElements != 0 ? scrapItemsVm.itemList.number+1:0}} of {{scrapItemsVm.itemList.totalPages}}</span>
                <a href="" ng-click="scrapItemsVm.previousPage()"
                   ng-class="{'disabled': scrapItemsVm.itemList.first || scrapItemsVm.loading}"><i
                        class="fa fa-arrow-circle-left mr10"></i></a>
                <a href="" ng-click="scrapItemsVm.nextPage()"
                   ng-class="{'disabled': scrapItemsVm.itemList.last || scrapItemsVm.loading}"><i
                        class="fa fa-arrow-circle-right"></i></a>
            </div>
        </div>
    </div>
    <div class="row" style="padding:20px;">
        <div class="responsive-table">
            <table class="table table-striped highlight-row">
                <thead>
                <tr>
                    <th style="width: 50px;"></th>
                    <th style="width: 150px; text-align: left">Item Number</th>
                    <th style="width: 150px; text-align: left">Item Name</th>
                    <th style="width: 150px; text-align: left">Type</th>
                </tr>
                </thead>
                <tbody>
                <tr ng-if="scrapItemsVm.itemList.content.length == 0">
                    <td colspan="11">No Items available to view</td>
                </tr>
                <tr data-ng-repeat="item in scrapItemsVm.itemList.content">

                    <td style="width: 50px;">
            <span title="Add Items" ng-class="{'disabled': item.isAdded}"
                  ng-click="scrapItemsVm.addToScrapItems(item)" aria-hidden="true">
            <i class="fa fa-plus-circle"></i></span>
                    </td>

                    <td style="width: 150px; text-align: left">
                        <span ng-bind-html="item.itemNumber | highlightText: freeTextQuery"></span>
                    </td>

                    <td style="width: 150px; text-align: left"
                        style="vertical-align: middle;width: 200px;max-width:250px !important;text-overflow: ellipsis;white-space: nowrap;overflow: hidden;"
                        title="{{item.itemName}}">
                        <span ng-bind-html="item.itemName | highlightText: freeTextQuery"></span>
                    </td>

                    <td style="width: 150px; text-align: left">
                        <span ng-bind-html="item.itemType | highlightText: freeTextQuery"></span>
                    </td>

                </tr>
                </tbody>
            </table>
        </div>
    </div>
</div>
