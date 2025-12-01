<div class="row" style="margin-bottom: 20px">
    <div class="col-md-4" style="margin-top: 20px;">

    </div>
    <div class="col-md-8"  style="text-align: right" ng-show="verifications.numberOfElements > 0">
        <div style="text-align: right;">
            <pagination total-items="verifications.totalElements"
                        items-per-page="pageable.size"
                        max-size="5"
                        boundary-links="true"
                        ng-model="pageable.page"
                        ng-change="pageChanged()">
            </pagination>
        </div>

        <div style="margin-top: -25px;">
            <small>Total {{verifications.totalElements}} orders</small>
        </div>
    </div>
</div>

<div class="row">
    <div class="col-md-12 responsive-table">
        <table class="table table-striped">
            <thead>
                <tr>
                    <th style="">Order Number</th>
                    <th style="">Status</th>
                    <th style="">PO Number</th>
                    <th style="">Customer</th>
                    <th style="">Assigned To</th>
                </tr>
            </thead>
            <tbody>
                <tr>
                    <td style="">
                        <table-filter placeholder="Order Number" filters="filters"
                                      property="orderNumber" apply-filters="applyFilters()" />
                    </td>
                    <td style="text-align:center;" ng-class="{hasFilter: ((filters.status != null && filters.status != ''))}">
                        <ui-select ng-model="filters.status" on-select="applyFilters()" theme="bootstrap" style="width:100%">
                            <ui-select-match allow-clear="true" placeholder="Status">{{$select.selected}}</ui-select-match>
                            <ui-select-choices repeat="status in orderStatusList | filter: $select.search">
                                <div ng-bind-html="status | highlight: $select.search"></div>
                            </ui-select-choices>
                        </ui-select>
                    </td>
                    <td style="">
                        <table-filter placeholder="PO Number" filters="filters"
                                      property="poNumber" apply-filters="applyFilters()" />
                    </td>
                    <td style="">
                        <table-filter placeholder="Customer" filters="filters"
                                      property="customer" apply-filters="applyFilters()" />
                    </td>
                    <td style="">
                        <table-filter placeholder="Assigned To" filters="filters"
                                      property="assignedTo" apply-filters="applyFilters()" />
                    </td>
                </tr>

                <tr ng-if="verifications.totalElements == 0 || loading == true">
                    <td colspan="9">
                        <span ng-hide="loading">No data</span>
                        <span ng-show="loading">
                            <img src="app/assets/images/loaders/loader19.gif" class="mr5">Loading...
                        </span>
                    </td>
                </tr>
                <tr ng-repeat="verification in verifications.content"
                    ng-class="{
                        'order-row-new': verification.order.status == 'NEW',
                        'order-row-approved': verification.order.status == 'APPROVED',
                        'order-row-canceled': verification.order.status == 'CANCELLED',
                        'order-row-processing': verification.order.status == 'PROCESSING',
                        'order-row-processed': verification.order.status == 'PROCESSED',
                        'order-row-partiallyshipped': verification.order.status == 'PARTIALLYSHIPPED',
                        'order-row-shipped': verification.order.status == 'SHIPPED'
                     }">
                    <td>{{verification.order.orderNumber}}</td>
                    <td>
                        <span class="status-label">
                            {{verification.order.status}}
                        </span>
                    </td>
                    <td>{{verification.order.poNumber}}</td>
                    <td>{{verification.order.customer.name}}</td>
                    <td>{{verification.verifiedBy.firstName}}</td>
                </tr>
            </tbody>
        </table>
    </div>
</div>