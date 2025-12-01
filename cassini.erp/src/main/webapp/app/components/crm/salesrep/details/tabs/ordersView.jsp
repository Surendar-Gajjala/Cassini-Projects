<div class="row" style="height: 80px; margin-bottom: 20px"  ng-if="orders.numberOfElements > 0">
    <div class="col-md-12"  style="text-align: right">
        <div style="text-align: right;">
            <pagination total-items="orders.totalElements"
                        items-per-page="pageable.size"
                        max-size="5"
                        boundary-links="true"
                        ng-model="pageable.page"
                        ng-change="pageChanged()">
            </pagination>
        </div>

        <div style="margin-top: -25px;">
            <small>Total {{orders.totalElements}} orders</small>
        </div>
    </div>
</div>

<div class="row">
    <div class="col-md-12">
        <table class="table table-striped">
            <thead>
            <tr>
                <th style="width:100px; text-align: center;">Actions</th>
                <th style="cursor: pointer; width: 120px" ng-click="sortColumn('orderType', 'orderType')">Order Type
                    <table-sorter label="orderType" sort="pageable.sort" />
                </th>
                <th style="cursor: pointer; width: 120px" ng-click="sortColumn('orderNumber', 'orderNumber')">Order Number
                    <table-sorter label="orderNumber" sort="pageable.sort" />
                </th>
                <th style="cursor: pointer; width: 150px;text-align:center;" ng-click="sortColumn('status', 'status')">Status
                    <table-sorter label="status" sort="pageable.sort" />
                </th>
                <th style="cursor: pointer; text-align: left;width: 200px;" ng-click="sortColumn('orderedDate', 'orderedDate')">Date Ordered
                    <table-sorter label="orderedDate" sort="pageable.sort" />
                </th>
                <th style="cursor: pointer;" ng-click="sortColumn('customer', 'customer.name')">Customer
                    <table-sorter label="customer" sort="pageable.sort" />
                </th>
                <th style="cursor: pointer; width: 200px;" ng-click="sortColumn('region', 'customer.salesRegion.name')">Region
                    <table-sorter label="region" sort="pageable.sort" />
                </th>
                <th style="cursor: pointer; width: 150px;text-align: right;" ng-click="sortColumn('orderTotal', 'orderTotal')">Order Total
                    <table-sorter label="orderTotal" sort="pageable.sort" />
                </th>
            </tr>
            </thead>
            <tbody>
            <tr>
                <td style="width:100px; text-align: center;">
                    <div class="btn-group" style="margin-bottom: 0px;">
                        <button title="Apply Filters"  type="button" class="btn btn-sm btn-success" ng-click="applyFilters()">
                            <i class="fa fa-search"></i>
                        </button>
                        <button title="Clear Filters" type="button" class="btn btn-sm btn-default" ng-click="resetFilters()">
                            <i class="fa fa-times"></i>
                        </button>
                    </div>
                </td>

                <td style="text-align:center;width: 120px;" ng-class="{hasFilter: ((filters.orderType != null && filters.orderType != ''))}">
                    <ui-select ng-model="filters.orderType" on-select="applyFilters()" theme="bootstrap" style="width:100%">
                        <ui-select-match allow-clear="true" placeholder="Type">{{$select.selected}}</ui-select-match>
                        <ui-select-choices repeat="orderType in orderTypes | filter: $select.search">
                            <div ng-bind-html="orderType | highlight: $select.search"></div>
                        </ui-select-choices>
                    </ui-select>
                </td>
                <td style="width: 120px;">
                    <table-filter placeholder="Number" filters="filters"
                                  property="orderNumber" apply-filters="applyFilters()" />
                </td>

                <td style="width: 150px;text-align:center;" ng-class="{hasFilter: ((filters.status != null && filters.status != ''))}">
                    <ui-select ng-model="filters.status" on-select="applyFilters()" theme="bootstrap" style="width:100%">
                        <ui-select-match allow-clear="true" placeholder="Status">{{$select.selected}}</ui-select-match>
                        <ui-select-choices repeat="status in orderStatusList | filter: $select.search">
                            <div ng-bind-html="status | highlight: $select.search"></div>
                        </ui-select-choices>
                    </ui-select>
                </td>

                <td style="text-align: center">
                    <input placeholder="Date"
                           style="width: 100%; text-align: left"
                           options="dateRangeOptions"
                           date-range-picker
                           clearable="true"
                           class="form-control date-picker"
                           ng-enter="applyFilters()"
                           ng-class="{ hasFilter: (filters.orderedDate.startDate != null && filters.orderedDate.endDate != '') }"
                           type="text" ng-model="filters.orderedDate" />
                </td>

                <td>
                    <table-filter placeholder="Customer" filters="filters"
                                  property="customer" apply-filters="applyFilters()" />
                </td>
                <td style="width: 120px;">
                    <table-filter placeholder="Region" filters="filters"
                                  property="region" apply-filters="applyFilters()" />
                </td>
                <td style="text-align: right">
                    <table-filter placeholder="Amount" align="right" filters="filters" property="orderTotal" apply-filters="applyFilters()" />
                </td>
            </tr>

            <tr ng-if="orders.totalElements == 0 || loading == true">
                <td colspan="8">
                    <span ng-hide="loading">There are no orders</span>
                        <span ng-show="loading">
                            <img src="app/assets/images/loaders/loader19.gif" class="mr5">Loading customer orders...
                        </span>
                </td>
            </tr>


            <tr ng-repeat="order in orders.content"
                class="order-row"
                ng-class="{
                        'order-row-new': order.status == 'NEW',
                        'order-row-approved': order.status == 'APPROVED',
                        'order-row-processed': order.status == 'PROCESSED',
                        'order-row-partiallyshipped': order.status == 'PARTIALLYSHIPPED',
                        'order-row-shipped': order.status == 'SHIPPED'
                     }">
                <td class="col-center actions-col" style="width:100px;">
                    <div class="btn-group" dropdown style="margin-bottom: 0px;">
                        <button type="button" class="btn btn-sm btn-default dropdown-toggle" dropdown-toggle>
                            <i class="fa fa-cog fa-fw"></i></span>
                        </button>
                        <ul class="dropdown-menu" role="menu">
                            <li><a href="" ng-click="openOrderDetials(order)">Open</a></li>
                        </ul>
                    </div>
                </td>
                <td style="text-align:center;width: 120px;">
                        <span class="order-type" ng-class="{'order-type-product': order.orderType == 'PRODUCT',
                                    'order-type-sample': order.orderType == 'SAMPLE'}">
                            {{order.orderType}}
                        </span>
                </td>
                <td style="width: 120px"><a href="" ng-click="openOrderDetials(order)">{{order.orderNumber}}</a></td>
                <td style="text-align:center;width: 150px;">
                    <span class="status-label">
                        {{order.status}}
                    </span>
                </td>
                <td style="text-align: left">{{order.orderedDate}}</td>
                <td><a href="" ng-click="showCustomerDetails(order.customer)">{{order.customer.name}}</a></td>
                <td>{{order.customer.salesRegion.name}}</td>

                <td style="width: 100px; text-align: right;">{{order.orderTotal | currency:"&#x20b9; ":0}}</td>
            </tr>
            </tbody>
        </table>
    </div>
</div>