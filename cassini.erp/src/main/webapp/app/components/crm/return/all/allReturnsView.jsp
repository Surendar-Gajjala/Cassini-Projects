<script type="text/ng-template" id="returns-view-tb">
    <div>
        <button class="btn btn-sm btn-primary" ng-click="newReturn()">New Return</button>
    </div>
</script>
<div class="row" style="margin-bottom: 20px">
    <div class="col-md-4" style="margin-top: 20px;">

    </div>
    <div class="col-md-8"  style="text-align: right" ng-show="returns.numberOfElements > 0">
        <div style="text-align: right;">
            <pagination total-items="returns.totalElements"
                        items-per-page="pageable.size"
                        max-size="5"
                        boundary-links="true"
                        ng-model="pageable.page"
                        ng-change="pageChanged()">
            </pagination>
        </div>

        <div style="margin-top: -25px;">
            <small>Total {{returns.totalElements}} returns</small>
        </div>
    </div>
</div>

<div class="row">
    <div class=col-md-12>
        <table class="table table-striped">
            <thead>
            <tr>
                <th style="width:100px; text-align: center;">Actions</th>
                <th style="cursor: pointer; text-align: left;width: 200px;" ng-click="sortColumn('returnDate', 'returnDate')">Date Returned
                    <table-sorter label="returnDate" sort="pageable.sort" />
                </th>
                <th style="cursor: pointer;" ng-click="sortColumn('customer', 'customer.name')">Customer
                    <table-sorter label="customer" sort="pageable.sort" />
                </th>
                <th style="cursor: pointer; width: 200px;" ng-click="sortColumn('region', 'customer.salesRegion.name')">Region
                    <table-sorter label="region" sort="pageable.sort" />
                </th>
                <th style="cursor: pointer; width: 200px;" ng-click="sortColumn('district', 'customer.salesRegion.district')">District
                    <table-sorter label="district" sort="pageable.sort" />
                </th>
                <th style="cursor: pointer; width: 200px;" ng-click="sortColumn('salesRep', 'customer.salesRep.firstName')">Sales Rep
                    <table-sorter label="salesRep" sort="pageable.sort" />
                </th>
                <th style="width: 250px; text-align: left">Reason</th>
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

                <td style="text-align: center">
                    <input placeholder="Date"
                           style="width: 100%; text-align: left"
                           options="dateRangeOptions"
                           date-range-picker
                           clearable="true"
                           class="form-control date-picker"
                           ng-enter="applyFilters()"
                           ng-class="{ hasFilter: (filters.returnDate.startDate != null && filters.returnDate.endDate != '') }"
                           type="text" ng-model="filters.returnDate" />
                </td>

                <td>
                    <table-filter placeholder="Customer" filters="filters"
                                  property="customer" apply-filters="applyFilters()" />
                </td>
                <td style="width: 200px;">
                    <table-filter placeholder="Region" filters="filters"
                                  property="region" apply-filters="applyFilters()" />
                </td>
                <td style="width: 200px;">
                    <table-filter placeholder="District" filters="filters"
                                  property="district" apply-filters="applyFilters()" />
                </td>
                <td style="width: 200px;">
                    <table-filter placeholder="Sales rep" filters="filters"
                                  property="salesRep" apply-filters="applyFilters()" />
                </td>
                <td style="width: 200px;">
                    <table-filter placeholder="Reason" filters="filters"
                                  property="reason" apply-filters="applyFilters()" />
                </td>
            </tr>

            <tr ng-if="returns.totalElements == 0 || loading == true">
                <td colspan="8">
                    <span ng-hide="loading">There are no returns</span>
                        <span ng-show="loading">
                            <img src="app/assets/images/loaders/loader19.gif" class="mr5">Loading customer returns...
                        </span>
                </td>
            </tr>

            <tr ng-repeat="return in returns.content">
                <td style="width:100px; text-align: center;">
                    <div class="btn-group" dropdown style="margin-bottom: 0px;">
                        <button type="button" class="btn btn-sm btn-default dropdown-toggle" dropdown-toggle>
                            <i class="fa fa-cog fa-fw"></i></span>
                        </button>
                        <ul class="dropdown-menu" role="menu">
                            <li><a href="" ng-click="openReturnDetails(return)">Open Return</a></li>
                        </ul>
                    </div>
                </td>
                <td>{{return.returnDate}}</td>
                <td>{{return.customer.name}}</td>
                <td>{{return.customer.salesRegion.name}}</td>
                <td>{{return.customer.salesRegion.district}}</td>
                <td>{{return.customer.salesRep.firstName}}</td>
                <td style="width: 250px;">{{return.reason}}</td>
            </tr>
            </tbody>
        </table>
    </div>
</div>