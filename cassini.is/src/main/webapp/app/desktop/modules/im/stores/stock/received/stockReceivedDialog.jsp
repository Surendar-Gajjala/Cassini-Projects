<div style="position: relative;">
    <div style="overflow-y: auto; padding: 20px">
        <div class="row" style="margin: 0;">
            <span style="color: red; margin-left: 180px;"> {{stockReceivedVm.errorMsg}}</span>

            <div class="pull-right text-center">
                <span ng-if="stockReceivedVm.loading == false"><small>pages {{stockReceivedVm.itemList.number+1}} of
                    {{stockReceivedVm.itemList.totalPages}}
                </small></span>
                <br>

                <div align="right">
                    <div class="btn-group" style="margin-bottom: 0">
                        <button class="btn btn-xs btn-default" ng-click='stockReceivedVm.previousPage()'
                                ng-disabled="stockReceivedVm.itemList.first"><i class="fa fa-chevron-left"></i></button>
                        <button class="btn btn-xs btn-default" ng-click='stockReceivedVm.nextPage()'
                                ng-disabled="stockReceivedVm.itemList.last"><i
                                class="fa fa-chevron-right"></i></button>
                    </div>
                    <br>
                    <span><small>{{stockReceivedVm.itemList.totalElements}} Items</small></span>
                </div>
            </div>

            <div class="input-group input-group-sm mb15"
                 style="margin-top: 2px;width: 400px;margin-right: 10px;position: absolute;top: 40px;left: 25px;">
                <span class="input-group-btn">
                    <button type="button" ng-click="stockReceivedVm.resetCriteria()" class="btn btn-danger"
                            title="Clear search"
                            style="height: 30px !important;">
                        <i class="fa fa-times-circle" style="font-size:16px"></i>
                    </button>
                </span>
                <input class="form-control" type="text" ng-enter="stockReceivedVm.applyCriteria()"
                       placeholder="Enter something to search"
                       ng-model="stockReceivedVm.searchQuery">
                <span class="input-group-btn">
                    <button type="button" ng-click="stockReceivedVm.applyCriteria()" class="btn btn-primary"
                            title="Search"
                            style="height: 30px !important;">
                        <i class="fa fa-search" style="font-size:15px"></i>
                    </button>
                </span>
            </div>

            <div class="row">
                <div class="responsive-table">
                    <table class="table table-striped">
                        <thead>
                        <tr>
                            <th>
                                <div class="ckbox ckbox-default" style="display: inline-block;">
                                    <input id="item{{$index}}" name="itemSelected" ng-value="true" type="checkbox"
                                           ng-model="stockReceivedVm.selectedAll"
                                           ng-click="stockReceivedVm.checkAll();">
                                    <label for="item{{$index}}" class="item-selection-checkbox"></label>
                                </div>
                            </th>
                            <th style="vertical-align: middle;">ItemNumber</th>
                            <th style="vertical-align: middle;">ItemName</th>
                            <th style="vertical-align: middle;">ItemType</th>
                            <th style="vertical-align: middle;">Boq <br> Quantity</th>
                            <th style="vertical-align: middle;">Received<br> Quantity</th>
                            <th style="vertical-align: middle;">Balance<br> Quantity</th>
                            <th style="vertical-align: middle;">Group</th>
                        </tr>
                        </thead>
                        <tbody>

                        <tr ng-if="stockReceivedVm.itemList.content.length == 0">
                            <td colspan="2">
                                <span ng-hide="stockReceivedVm.loading">No Items</span>
                            <span ng-show="stockReceivedVm.loading">
                                <img src="app/assets/images/loaders/loader19.gif" class="mr5">Loading Items...
                            </span>
                            </td>
                        </tr>

                        <tr ng-repeat="item in stockReceivedVm.itemList.content">
                            <td>
                                <div class="ckbox ckbox-default">
                                    <input id="item{{$index}}" name="itemSelected" ng-value="true" type="checkbox"
                                           ng-model="item.selected" ng-click="stockReceivedVm.select(item)">
                                    <label for="item{{$index}}" class="item-selection-checkbox"></label>
                                </div>
                            </td>

                            <td style="vertical-align: middle;">
                                {{item.itemNumber }}
                            </td>
                            <td style="vertical-align: middle;">
                                {{item.itemName }}
                            </td>
                            <td style="vertical-align: middle;">
                                {{item.itemType}}
                            </td>
                            <td style="vertical-align: middle;text-align: center">
                                {{item.quantity}}
                            </td>
                            <td style="vertical-align: middle;text-align: center">
                                {{item.totalReceivedQty}}
                            </td>
                            <td style="vertical-align: middle;text-align: center">
                                {{item.balanceQty}}
                            </td>
                            <td style="vertical-align: middle;">
                                {{item.boqName}}
                            </td>
                        </tr>
                        </tbody>
                    </table>
                </div>
            </div>
        </div>
    </div>
    <div id="appSidePanelButtonsPanel" class='buttons-panel' style="display: none">
        <button class="btn btn-sm btn-success" style="min-width: 80px"
                ng-click="stockReceivedVm.create()">Select
        </button>
    </div>
</div>
