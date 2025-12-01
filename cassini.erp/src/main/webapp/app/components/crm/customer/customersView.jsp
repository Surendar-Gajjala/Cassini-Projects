<script type="text/ng-template" id="customers-view-tb">
    <div>
        <button class="btn btn-sm btn-primary" ng-click="newCustomer()">New Customer</button>
        <button class="btn btn-sm btn-default mr20" ng-click="geoSearch()">{{showGeoSearch ? 'Hide Geo Search' : 'Geo Search'}}</button>
        <div class="btn-group" ng-if="!showGeoSearch">
            <a class="btn btn-sm btn-default" ng-class="{ 'active': viewType == 'grid' }" ng-click="setViewType('grid')"><i class="fa fa-th"></i></a>
            <a class="btn btn-sm btn-default" ng-class="{ 'active': viewType == 'map' }" ng-click="setViewType('map')"><i class="glyphicon glyphicon-map-marker"></i></a>
        </div>
    </div>
</script>
<div>
    <div class="row" style="margin-bottom: 20px">
        <div class="col-md-4" style="margin-top: 20px;">

        </div>
        <div class="col-md-8"  style="text-align: right" ng-show="customers.numberOfElements > 0" ng-hide="showGeoSearch">
            <div style="text-align: right;">
                <pagination total-items="customers.totalElements"
                            items-per-page="pageable.size"
                            max-size="5"
                            boundary-links="true"
                            ng-model="pageable.page"
                            ng-change="pageChanged()">
                </pagination>
            </div>

            <div style="margin-top: -25px;">
                <small>Total {{customers.totalElements}} customers</small>
            </div>
        </div>
    </div>

    <div ng-if="showGeoSearch" class="geo-search" style="border: 1px solid #DDD;border-radius: 3px;padding: 5px;">
        <div ng-include="templates.geoSearch" ng-controller="GeoSearchController"></div>
    </div>

    <div class="row" ng-hide="showGeoSearch">
        <div class="col-sm-12 responsive-table">
            <table class="table table-striped">
                <thead>
                    <tr>
                        <th style="width:100px; text-align: center">Actions</th>
                        <th style="width:100px; text-align: center">Blacklisted</th>
                        <th style="cursor: pointer;" ng-click="sortColumn('name')">Name
                            <table-sorter label="name" sort="pageable.sort" />
                        </th>
                        <th style="cursor: pointer;" ng-click="sortColumn('customerType')">CustomerType
                            <table-sorter label="customerType" sort="pageable.sort" />
                        </th>
                        <th style="cursor: pointer;" ng-click="sortColumn('region')">Region
                            <table-sorter label="region" sort="pageable.sort" />
                        </th>
                        <th style="cursor: pointer;" ng-click="sortColumn('salesRep')">Sales Rep
                            <table-sorter label="salesRep" sort="pageable.sort" />
                        </th>
                        <th style="cursor: pointer;" ng-click="sortColumn('contactPerson')">Contact Person
                            <table-sorter label="contactPerson" sort="pageable.sort" />
                        </th>
                        <th>Contact Phone</th>
                    </tr>
                </thead>

                <tbody>
                    <tr>
                        <td style="width:100px; text-align: center;">
                            <table-filters-actions apply-filters="applyFilters()" reset-filters="resetFilters()"/>
                        </td>

                        <td style="width:100px; text-align: center;">
                            <input type="checkbox" ng-model="filters.blacklisted" ng-change="applyFilters()">
                        </td>

                        <td>
                            <table-filter placeholder="Name" filters="filters" property="name" apply-filters="applyFilters()" />
                        </td>
                        <td style="text-align:center;width: 150px;" ng-class="{hasFilter: ((filters.customerType != null && filters.customerType != ''))}">
                            <ui-select ng-model="filters.customerType" on-select="applyFilters()" theme="bootstrap" style="width:100%">
                                <ui-select-match allow-clear="true" placeholder="CustomerType">{{$select.selected}}</ui-select-match>
                                <ui-select-choices repeat="customerType in customerTypes | filter: $select.search">
                                    <div ng-bind-html="customerType | highlight: $select.search"></div>
                                </ui-select-choices>
                            </ui-select>
                        </td>
                        <td>
                            <table-filter placeholder="Region" filters="filters" property="region" apply-filters="applyFilters()" />
                        </td>
                        <td>
                            <table-filter placeholder="Sale Rep" filters="filters" property="salesRep" apply-filters="applyFilters()" />
                        </td>
                        <td>
                            <table-filter placeholder="Contact Person" filters="filters" property="contactPerson" apply-filters="applyFilters()" />
                        </td>
                        <td>
                            <table-filter placeholder="Contact Phone" filters="filters" property="contactPhone" apply-filters="applyFilters()" />
                        </td>
                    </tr>

                    <tr ng-if="viewType != 'map' && (customers.totalElements == 0 || loading == true)">
                        <td colspan="6">
                            <span ng-hide="loading">There are no customers</span>
                            <span ng-show="loading">
                                <img src="app/assets/images/loaders/loader19.gif" class="mr5">Loading customers...
                            </span>
                        </td>
                    </tr>

                    <tr ng-show="viewType == 'map'">
                        <td colspan="6"></td>
                    </tr>

                    <tr class="customers-view-type-map" ng-show="viewType == 'map'" style="height: 500px">
                        <td colspan="6">
                            <div ng-include="templates.mapViewTemplate" ng-controller="CustomersMapViewController"></div>
                        </td>
                    </tr>

                    <tr ng-repeat="customer in customers.content" ng-show="viewType == 'grid'">
                        <td class="col-center actions-col">
                            <div class="btn-group" dropdown style="margin-bottom: 0px;">
                                <button type="button" class="btn btn-sm btn-default dropdown-toggle" dropdown-toggle>
                                    <i class="fa fa-cog fa-fw"></i></span>
                                </button>
                                <ul class="dropdown-menu" role="menu">
                                    <li><a href="" ng-click="showCustomerDetails(customer)">Open</a></li>
                                    <li><a href="" ng-click="createOrder(customer, 'PRODUCT')">Product Order</a></li>
                                    <li><a href="" ng-click="createOrder(customer, 'SAMPLE')">Sample Order</a></li>
                                </ul>
                            </div>
                        </td>
                        <td class="text-center">
                            <input ng-disabled="hasRole('Administrator') == false" type="checkbox" ng-model="customer.blacklisted" ng-change="blacklistCustomer(customer)">
                        </td>
                        <td>
                            <a href="" ng-click="showCustomerDetails(customer)" class="mr10">{{customer.name}}</a>
                            <strong><span class="blink" ng-if="customer.blacklisted == true" style="color: red"><i class="fa fa-bullseye"></i></span></strong>
                        </td>
                        <td>{{customer.customerType.name}}</td>
                        <td>{{customer.salesRegion.name}}</td>
                        <td><a href="" ng-click="showSalesRep(customer.salesRep)">{{customer.salesRep.firstName}}</a></td>
                        <td>{{customer.contactPerson.firstName}}</td>
                        <td>{{getContactPhone(customer)}}</td>
                    </tr>
                </tbody>
            </table>
        </div>
    </div>
</div>