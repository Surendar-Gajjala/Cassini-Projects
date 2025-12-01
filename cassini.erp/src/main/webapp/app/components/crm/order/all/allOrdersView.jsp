<script type="text/ng-template" id="orders-view-tb">
    <div>
        <div class="btn-group" fix-drop-down>
            <button class="btn btn-sm btn-primary" type="button">
                <span class="mr10">New Order</span><span class="caret"></span>
            </button>
            <ul class="dropdown-menu" role="menu" style="display: none">
                <li><a href="" ng-click="createOrder(null, 'PRODUCT')">Product Order</a></li>
                <li><a href="" ng-click="createOrder(null, 'SAMPLE')">Sample Order</a></li>
            </ul>
        </div>
        <button class="btn btn-sm btn-warning" type="button" ng-click="$parent.showOrderVerifications()">Order
            Verifications
        </button>
    </div>
</script>

<div class="row" style="margin-bottom: 20px">
    <div class="col-md-4" style="margin-top: 20px;">

    </div>
    <div class="col-md-8" style="text-align: right" ng-show="orders.numberOfElements > 0">
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
    <div class="col-md-12 responsive-table">
        <table class="table table-striped">
            <thead>
            <tr>
                <th style="width:100px; text-align: center;">Actions</th>
                <th style="cursor: pointer; width: 150px" ng-click="sortColumn('orderType', 'orderType')">Order Type
                    <table-sorter label="orderType" sort="pageable.sort"/>
                </th>
                <th style="cursor: pointer; width: 120px" ng-click="sortColumn('orderNumber', 'orderNumber')">Order
                    Number
                    <table-sorter label="orderNumber" sort="pageable.sort"/>
                </th>
                <th style="cursor: pointer; width: 150px;text-align:center;" ng-click="sortColumn('status', 'status')">
                    Status
                    <table-sorter label="status" sort="pageable.sort"/>
                </th>
                <th style="cursor: pointer; text-align: left;width: 200px;"
                    ng-click="sortColumn('orderedDate', 'orderedDate')">Date Ordered
                    <table-sorter label="orderedDate" sort="pageable.sort"/>
                </th>
                <th style="cursor: pointer; text-align: left;width: 200px;"
                    ng-click="sortColumn('deliveryDate', 'deliveryDate')">Delivery Date
                    <table-sorter label="deliveryDate" sort="pageable.sort"/>
                </th>
                <th style="cursor: pointer;" ng-click="sortColumn('customer', 'customer.name')">Customer
                    <table-sorter label="customer" sort="pageable.sort"/>
                </th>
                <th style="cursor: pointer; width: 200px;" ng-click="sortColumn('shipTo', 'shipTo')">shipTo
                    <table-sorter label="shipTo" sort="pageable.sort"/>
                </th>
                <th style="cursor: pointer; width: 200px;" ng-click="sortColumn('region', 'customer.salesRegion.name')">
                    Region
                    <table-sorter label="region" sort="pageable.sort"/>
                </th>
                <th style="cursor: pointer; width: 200px;"
                    ng-click="sortColumn('salesRep', 'customer.salesRep.firstName')">Sales Rep
                    <table-sorter label="salesRep" sort="pageable.sort"/>
                </th>
                <th style="cursor: pointer; width: 150px;text-align: right;"
                    ng-click="sortColumn('orderTotal', 'orderTotal')">Order Total
                    <table-sorter label="orderTotal" sort="pageable.sort"/>
                </th>
                <th style="width: 150px">
                    Invoice number(s)
                </th>
                <th style="width: 150px">
                    Tracking number(s)
                </th>
            </tr>
            </thead>
            <tbody>
            <tr>
                <td style="width:100px; text-align: center;">
                    <div class="btn-group" style="margin-bottom: 0px;">
                        <button title="Apply Filters" type="button" class="btn btn-sm btn-success"
                                ng-click="applyFilters()">
                            <i class="fa fa-search"></i>
                        </button>
                        <button title="Clear Filters" type="button" class="btn btn-sm btn-default"
                                ng-click="resetFilters()">
                            <i class="fa fa-times"></i>
                        </button>
                    </div>
                </td>
                <td style="text-align:center;width: 150px;"
                    ng-class="{hasFilter: ((filters.orderType != null && filters.orderType != ''))}">
                    <ui-select ng-model="filters.orderType" on-select="applyFilters()" theme="bootstrap"
                               style="width:100%">
                        <ui-select-match allow-clear="true" placeholder="Type">{{$select.selected}}</ui-select-match>
                        <ui-select-choices repeat="orderType in orderTypes | filter: $select.search">
                            <div ng-bind-html="orderType | highlight: $select.search"></div>
                        </ui-select-choices>
                    </ui-select>
                </td>
                <td style="width: 120px;">
                    <table-filter placeholder="Number" filters="filters"
                                  property="orderNumber" apply-filters="applyFilters()"/>
                </td>
                <td style="text-align:center;width: 150px;"
                    ng-class="{hasFilter: ((filters.status != null && filters.status != ''))}">
                    <ui-select ng-model="filters.status" on-select="applyFilters()" theme="bootstrap"
                               style="width:100%">
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
                           type="text" ng-model="filters.orderedDate"/>
                </td>

                <td>
                    <input placeholder="Delivery Date"
                           style="width: 100%; text-align: left"
                           options="dateRangeOptions"
                           date-range-picker
                           clearable="true"
                           class="form-control date-picker"
                           ng-enter="applyFilters()"
                           ng-class="{ hasFilter: (filters.deliveryDate.startDate != null && filters.deliveryDate.endDate != '') }"
                           type="text" ng-model="filters.deliveryDate"/>
                </td>

                <td>
                    <table-filter placeholder="Customer" filters="filters"
                                  property="customer" apply-filters="applyFilters()"/>
                </td>
                <td style="width: 120px;">
                    <table-filter placeholder="shipTo" filters="filters"
                                  property="shipTo" apply-filters="applyFilters()"/>
                </td>
                <td style="width: 120px;">
                    <table-filter placeholder="Region" filters="filters"
                                  property="region" apply-filters="applyFilters()"/>
                </td>
                <td style="width: 150px;">
                    <table-filter placeholder="Sales rep" filters="filters"
                                  property="salesRep" apply-filters="applyFilters()"/>
                </td>
                <td style="text-align: right">
                    <table-filter placeholder="Amount" align="right" filters="filters" property="orderTotal"
                                  apply-filters="applyFilters()"/>
                </td>
                <td style="width: 150px;">
                    <table-filter placeholder="Invoice" filters="filters"
                                  property="invoiceNumber" apply-filters="applyFilters()"/>
                </td>
                <td style="width: 150px;">
                    <table-filter placeholder="Tracking" filters="filters"
                                  property="trackingNumber" apply-filters="applyFilters()"/>
                </td>
            </tr>

            <tr ng-if="orders.totalElements == 0 || loading == true">
                <td colspan="9">
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
                        'order-row-canceled': order.status == 'CANCELLED',
                        'order-row-processing': order.status == 'PROCESSING',
                        'order-row-processed': order.status == 'PROCESSED',
                        'order-row-partiallyshipped': order.status == 'PARTIALLYSHIPPED',
                        'order-row-shipped': order.status == 'SHIPPED'
                     }">
                <td class="col-center actions-col">
                    <div class="btn-group" dropdown style="margin-bottom: 0px;">
                        <button type="button" popover-template="'app/components/crm/order/all/orderAction.html'"
                                class="btn btn-sm btn-default dropdown-toggle" dropdown-toggle>
                            <i class="fa fa-cog fa-fw"></i>
                        </button>

                        <%--<ul class="dropdown-menu" role="menu">
                            <li><a href="" ng-click="openOrderDetails(order)">Open Order</a></li>
                            <li><a href="" ng-click="bookmarkOrder(order)">Bookmark Order</a></li>
                        </ul>--%>
                    </div>
                </td>


                <td style="text-align:center;width: 150px;">
                        <span class="order-type" ng-class="{'order-type-product': order.orderType == 'PRODUCT',
                                    'order-type-sample': order.orderType == 'SAMPLE'}">
                            {{order.orderType}}
                        </span>
                </td>
                <td style="width: 120px">
                    <a href="" ng-click="openOrderDetails(order)">{{order.orderNumber}}</a>
                    <span ng-if="order.starred == true" style="margin-left: 10px"><i class="fa fa-star"
                                                                                     style="color: #ffa331"></i></span>
                        <span ng-if="order.onhold == true" style="margin-left: 10px">
                            <i class="fa fa-hand-stop-o"></i>
                        </span>
                </td>
                <td style="text-align:center;width: 150px;">
                        <span class="status-label">
                            {{order.status}}
                        </span>
                </td>
                <td style="text-align: left">{{order.orderedDate}}</td>
                <td style="text-align: left">{{order.deliveryDate}}</td>
                <td><a href="" ng-click="showCustomerDetails(order.customer)">{{order.customer.name}}</a></td>
                <td style="width: 150px;">
                    <a href="" ng-click="showSalesRep(order.customer.salesRep)">
                        {{order.shipTo}}
                    </a>
                </td>
                <td style="width: 120px;">{{order.customer.salesRegion.name}}</td>
                <td style="width: 150px;">
                    <a href="" ng-click="showSalesRep(order.customer.salesRep)">
                        {{order.customer.salesRep.firstName}}
                    </a>
                </td>
                <td style="width: 100px; text-align: right;">{{order.orderTotal | currency:"&#x20b9; ":0}}</td>
                <td style="text-align: center">
                    <span ng-if="order.shipments.length == 0">N/A</span>

                    <div ng-if="order.shipments.length > 0" class="btn-group" dropdown fix-drop-down
                         style="margin-bottom: 0px;">
                            <span ng-if="order.shipments.length == 1">
                                    <a ui-sref="app.crm.orders.details({orderId: order.id})">{{order.shipments[0].invoiceNumber}}</a>
                                </span>
                        <a href class="dropdown-toggle" dropdown-toggle ng-if="order.shipments.length > 1">
                            {{order.shipments.length}} invoices
                        </a>
                        <ul class="dropdown-menu" role="menu">
                            <li ng-repeat="shipment in order.shipments">
                                <a ui-sref="app.crm.orders.details({orderId: order.id})">{{shipment.invoiceNumber}}</a>
                            </li>
                        </ul>
                    </div>
                </td>
                <td style="text-align: center">
                    <span ng-if="order.consignments.length == 0">N/A</span>

                    <div ng-if="order.consignments.length > 0" class="btn-group" dropdown fix-drop-down
                         style="margin-bottom: 0px;">
                            <span ng-if="order.consignments.length == 1">
                                    <a href="" ng-click="showConsignment(order.consignments[0])">{{order.consignments[0].number}}</a>
                                </span>
                        <a href class="dropdown-toggle" dropdown-toggle ng-if="order.consignments.length > 1">
                            {{order.consignments.length}} consignments
                        </a>
                        <ul class="dropdown-menu" role="menu">
                            <li ng-repeat="consignment in order.consignments track by $index">
                                <a href="" ng-click="showConsignment(consignment)">{{consignment.number}}</a>
                            </li>
                        </ul>
                    </div>
                </td>
            </tr>
            </tbody>
        </table>
    </div>
</div>
