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
        <free-text-search on-clear="loanItemsVm.resetPage"
                          on-search="loanItemsVm.freeTextSearch"></free-text-search>
        <div class="pull-right text-center" style="padding: 10px;margin-top: -5px;">
            <div>
                        <span>
                            <medium>
                                <span style="margin-right: 10px;">
                                        Displaying {{loanItemsVm.itemList.content.length}} of
                                            {{loanItemsVm.itemList.totalElements}}
                                    </span>
                            </medium>
                        </span>
                <span class="mr10">Page {{loanItemsVm.itemList.totalElements != 0 ? loanItemsVm.itemList.number+1:0}} of {{loanItemsVm.itemList.totalPages}}</span>
                <a href="" ng-click="loanItemsVm.previousPage()"
                   ng-class="{'disabled': loanItemsVm.itemList.first || loanItemsVm.loading}"><i
                        class="fa fa-arrow-circle-left mr10"></i></a>
                <a href="" ng-click="loanItemsVm.nextPage()"
                   ng-class="{'disabled': loanItemsVm.itemList.last || loanItemsVm.loading}"><i
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
                        <th style="width: 150px; text-align: left">Item Name</th>
                        <th style="width: 150px; text-align: left">Type</th>
                        <th style="width: 200px; text-align: left">Item Description</th>
                    </tr>
                    </thead>
                    <tbody>
                    <tr ng-if="loanItemsVm.loading == true">
                        <td colspan="10">
                            <span style="font-size: 15px;">
                                <img src="app/assets/images/loaders/loader19.gif" class="mr5">Loading Items..
                            </span>
                        </td>
                    </tr>
                    <tr ng-if="loanItemsVm.itemList.content.length == 0 && !loanItemsVm.loading">
                        <td colspan="11">No Items</td>
                    </tr>
                    <tr data-ng-repeat="item in loanItemsVm.itemList.content" ng-if="!loanItemsVm.loading">

                        <td style="width: 50px;">
            <span title="Add Items" ng-click="loanItemsVm.addToLoanItems(item)"
                  ng-class="{'disabled': item.isAdded}" aria-hidden="true">
                            <i class="fa fa-plus-circle"></i>
                        </span>
                        </td>

                        <td style="width: 150px; text-align: left">
                            <span>{{item.itemDTO.itemNumber}}</span>
                        </td>

                        <td style="vertical-align: middle;width: 150px;max-width:150px !important;text-overflow: ellipsis;white-space: nowrap;overflow: hidden;"
                            title="{{item.itemDTO.itemName}}">
                            <span>{{item.itemDTO.itemName}}</span>
                        </td>

                        <td style="width: 150px; text-align: left">
                            <span>{{item.itemDTO.resourceType}}</span>
                        </td>
                        <td style="vertical-align: middle;width: 200px;max-width:250px !important;text-overflow: ellipsis;white-space: nowrap;overflow: hidden;"
                            title="{{item.itemDTO.description}}">
                            <span>{{item.itemDTO.description}}</span>

                        </td>

                    </tr>
                    </tbody>
                </table>
            </div>
        </div>
    </div>
</div>
