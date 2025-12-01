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
        <free-text-search on-clear="stockIssueItemsVm.resetPage"
                          on-search="stockIssueItemsVm.freeTextSearch"></free-text-search>
        <div class="pull-right text-center" style="padding: 10px;">
            <div ng-if="stockIssueItemsVm.itemList.content.length > 0">
                        <span>
                            <medium>
                                <span style="margin-right: 10px;">
                                        Displaying {{stockIssueItemsVm.itemList.content.length}} of
                                            {{stockIssueItemsVm.itemList.content[0].totalElements}}
                                    </span>
                            </medium>
                        </span>
                <span class="mr10">Page {{stockIssueItemsVm.itemList[0].totalElements != 0 ? stockIssueItemsVm.pageable.page+1:0}} of {{stockIssueItemsVm.itemList.content[0].totalPages}}</span>
                <a href="" ng-click="stockIssueItemsVm.previousPage()"
                   ng-class="{'disabled': stockIssueItemsVm.pageable.page < 1 || stockIssueItemsVm.loading}"><i
                        class="fa fa-arrow-circle-left mr10"></i></a>
                <a href="" ng-click="stockIssueItemsVm.nextPage()"
                   ng-class="{'disabled': stockIssueItemsVm.disableNext || stockIssueItemsVm.loading}"><i
                        class="fa fa-arrow-circle-right"></i></a>
            </div>
        </div>
    </div>
    <div class="view-content" style="overflow-y: auto;padding: 10px !important;height: 185px">
        <div class="row" style="padding:10px;">
            <div class="responsive-table">
                <table class="table table-striped highlight-row">
                    <thead>
                    <tr>
                        <th style="width: 50px;"></th>
                        <th style="width: 150px; text-align: left">Item Number</th>
                        <th style="width: 150px; text-align: left">Type</th>
                        <th style="width: 200px; text-align: left">Item Name</th>
                        <th style="width: 100px; text-align: left">Units</th>
                    </tr>
                    </thead>
                    <tbody>
                    <tr ng-if="stockIssueItemsVm.loading == true">
                        <td colspan="10">
                            <span style="font-size: 15px;">
                                <img src="app/assets/images/loaders/loader19.gif" class="mr5">Loading Items..
                            </span>
                        </td>
                    </tr>
                    <tr ng-if="stockIssueItemsVm.loading == false && stockIssueItemsVm.itemList.content.length == 0">
                        <td colspan="15">No Items</td>
                    </tr>

                    <tr data-ng-repeat="item in stockIssueItemsVm.itemList.content "
                        ng-if="stockIssueItemsVm.loading == false">

                        <td style="width: 50px;">
            <span title="Add Items" ng-class="{'disabled': item.isAdded}"
                  ng-click="stockIssueItemsVm.addToIssueItems(item)" aria-hidden="true">
            <i class="fa fa-plus-circle"></i></span>

                        </td>

                        <td style="width: 150px; text-align: left">
                            <span ng-bind-html="item.itemNumber | highlightText: freeTextQuery"></span>
                        </td>

                        <td style="width: 150px; text-align: left">
                            <span ng-bind-html="item.materialType.name | highlightText: freeTextQuery"></span>
                        </td>

                        <td ng-if="item.itemName == null">
                            <span ng-bind-html="item.itemName | highlightText: freeTextQuery"></span>
                        </td>
                        <td ng-if="item.itemName != null"
                            style="vertical-align: middle;width: 200px;max-width:250px !important;text-overflow: ellipsis;white-space: nowrap;overflow: hidden;"
                            title="{{item.itemName}}">
                            <span ng-bind-html="item.itemName | highlightText: freeTextQuery"></span>

                        </td>
                        <td style="width: 100px; text-align: left">
                            <span ng-bind-html="item.units | highlightText: freeTextQuery"></span>

                        </td>

                    </tr>
                    </tbody>
                </table>
            </div>
        </div>
    </div>
</div>
