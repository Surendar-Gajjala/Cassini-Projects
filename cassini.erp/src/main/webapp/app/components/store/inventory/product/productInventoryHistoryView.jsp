<br/>
<div ng-controller="ProductInventoryHistoryController" style="height: auto">
    <div class="row">
        <div class="col-md-8 col-md-offset-2" style="background-color: white;border: 1px solid #EEE;padding: 50px;margin-bottom: 20px;">
            <div class="row" style="margin-bottom: 20px">
                <div class="col-md-2 pull-right" style="text-align: right;margin-top: -40px;margin-right: -40px;font-size: 20px;">
                    <span title="Close" style="cursor: pointer" ng-click="closeProductInventoryHistory()">
                        <i class="fa fa-times-circle"></i>
                    </span>

                </div>
                <div class="col-md-5" style="text-decoration:underline; margin-top: 30px; margin-left: 10px; font-size: 20px">
                    <a href="#" ng-click="loadProductInventoryHistoryBYStockType('null')">All</a>
                </div>
                <div class="col-md-5"  style="margin-top: -20px; text-align: right;" ng-show="history.numberOfElements > 0">
                    <div>
                        <pagination total-items="history.totalElements"
                                    items-per-page="pageable.size"
                                    max-size="5"
                                    boundary-links="true"
                                    ng-model="pageable.page"
                                    ng-change="pageChanged()">
                        </pagination>
                    </div>

                    <div style="margin-top: -25px;">
                        <small>Total {{history.totalElements}} records</small>
                    </div>
                </div>
            </div>

            <div class="row">
                <div class="col-sm-12">
                    <table class="table table-striped">
                        <thead>
                        <tr>
                            <th style="width:100px;">
                                <a style="text-decoration: underline;" href="" ng-click="loadProductInventoryHistoryBYStockType('STOCKIN')">Stock IN</a>/
                                <a style="text-decoration: underline;" href="" ng-click="loadProductInventoryHistoryBYStockType('STOCKOUT')">Stock OUT</a></th>
                            <th style="width:150px;">Quantity</th>
                            <th style="width:150px;">Reference</th>
                            <th style="width:150px;">Timestamp</th>
                        </tr>
                        </thead>

                        <tbody>
                            <tr ng-if="history.totalElements == 0 || loading == true">
                                <td colspan="4">
                                    <span ng-hide="loading">There are no records</span>
                                    <span ng-show="loading">
                                        <img src="app/assets/images/loaders/loader19.gif" class="mr5">Loading history...
                                    </span>
                                </td>
                            </tr>

                            <tr ng-repeat="historyItem in history.content">
                                <td>{{historyItem.type}}</td>
                                <td>{{historyItem.quantity}}</td>
                                <td>
                                    <span ng-if="historyItem.type == 'STOCKOUT' && historyItem.referenceObject != null &&
                                                historyItem.referenceObject != undefined">
                                        <span ng-show="loadingReferences">
                                            <img src="app/assets/images/loaders/loader2.gif" class="mr5">
                                        </span>
                                        <span ng-show="!loadingReferences">
                                            {{historyItem.referenceObject.order.customer.name != null ? historyItem.referenceObject.order.customer.name : 'N/A'}}
                                        </span>
                                    </span>
                                </td>
                                <td>{{historyItem.timestamp}}</td>
                            </tr>
                        </tbody>
                    </table>
                </div>
            </div>
        </div>
    </div>
</div>