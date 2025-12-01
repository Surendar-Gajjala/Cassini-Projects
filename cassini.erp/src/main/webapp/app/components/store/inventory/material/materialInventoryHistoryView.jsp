<div ng-controller="MaterialInventoryHistoryController" style="height: auto">
    <div class="row">
        <div class="col-md-8 col-md-offset-2"
             style="background-color: white;border: 1px solid #EEE;padding: 50px;margin-bottom: 20px;">
            <div class="row" style="margin-bottom: 20px">
                <div class="col-md-2 pull-right"
                     style="text-align: right;margin-top: -40px;margin-right: -40px;font-size: 20px;">
                    <span title="Close" style="cursor: pointer" ng-click="closeMaterialInventoryHistory()">
                        <i class="fa fa-times-circle"></i>
                    </span>

                </div>
                <div class="col-md-10" style="margin-top: -20px;" ng-show="history.numberOfElements > 0">
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
                            <th style="width:100px;">Stock IN/OUT</th>
                            <th style="width:100px;">Material PO</th>
                            <th style="width:150px;">Quantity</th>
                            <th style="width:150px;">Processed By</th>
                            <th style="width:150px;">Timestamp</th>
                        </tr>
                        </thead>

                        <tbody>
                        <%--<tr>
                            <th style="width:100px;">
                                <ui-select ng-model="filters.status" on-select="applyFilters()" theme="bootstrap"
                                           style="width:100%">
                                    <ui-select-match allow-clear="true" placeholder="Status">{{$select.selected}}
                                    </ui-select-match>
                                    <ui-select-choices repeat="status in statuses | filter: $select.search">
                                        <div ng-bind-html="status | highlight: $select.search"></div>
                                    </ui-select-choices>
                                </ui-select>
                            </th>
                            <th style="width:100px;">
                                <table-filter placeholder="MaterialPO" filters="filters"
                                              property="materialPO" apply-filters="applyFilters()"/>
                            </th>
                            <th style="width:150px;">
                                <table-filter placeholder="Number" filters="filters"
                                              property="quantity" apply-filters="applyFilters()"/>
                            </th>
                            <th style="width:150px;">
                                <table-filter placeholder="Processed By" filters="filters"
                                              property="processedBy" apply-filters="applyFilters()"/>
                            </th>
                            <th style="width:150px;"><input placeholder="Date"
                                                            style="width: 100%; text-align: left"
                                                            options="dateRangeOptions"
                                                            date-range-picker
                                                            clearable="true"
                                                            class="form-control date-picker"
                                                            ng-enter="applyFilters()"
                                                            ng-class="{ hasFilter: (filters.time.startDate != null && filters.orderedDate.endDate != '') }"
                                                            type="text" ng-model="filters.orderedDate" /></th>
                        </tr>
                        <tr ng-if="history.totalElements == 0 || loading == true">
                            <td colspan="4">
                                <span ng-hide="loading">There are no records</span>
                                    <span ng-show="loading">
                                        <img src="app/assets/images/loaders/loader19.gif" class="mr5">Loading history...
                                    </span>
                            </td>
                        </tr>--%>

                        <tr ng-repeat="historyItem in history.content | orderBy: '-timestamp'">
                            <td>{{historyItem.type}}</td>
                            <td>{{historyItem.materialPO.orderNumber}}</td>
                            <td>{{historyItem.quantity}}</td>
                            <td>{{historyItem.employee.firstName}}</td>
                            <td>{{historyItem.timestamp}}</td>
                        </tr>
                        </tbody>
                    </table>
                </div>
            </div>
        </div>
    </div>
</div>