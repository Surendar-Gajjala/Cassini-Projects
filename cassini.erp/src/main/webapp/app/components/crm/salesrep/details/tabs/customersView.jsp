<div class="row" style="margin-bottom: 20px">
    <div class="col-md-4" style="margin-top: 20px;">
        <div class="btn-group" style="margin-top: 0px;">
            <a class="btn btn-default" ng-class="{ 'active': viewType == 'grid' }" ng-click="setViewType('grid')"><i class="fa fa-th"></i></a>
            <a class="btn btn-default" ng-class="{ 'active': viewType == 'map' }" ng-click="setViewType('map')"><i class="glyphicon glyphicon-map-marker"></i></a>
        </div>
    </div>
    <div class="col-md-8"  style="text-align: right" ng-show="customers.numberOfElements > 0">
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

<div class="row">
    <div class="col-sm-12">
        <table class="table table-striped">
            <thead>
            <tr>
                <th style="width:100px; text-align: center">Actions</th>
                <th style="cursor: pointer;" ng-click="sortColumn('name')">Name
                    <table-sorter label="name" sort="pageable.sort" />
                </th>
                <th style="cursor: pointer;" ng-click="sortColumn('region')">Region
                    <table-sorter label="region" sort="pageable.sort" />
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

                <td>
                    <table-filter placeholder="Name" filters="filters" property="name" apply-filters="applyFilters()" />
                </td>
                <td>
                    <table-filter placeholder="Region" filters="filters" property="region" apply-filters="applyFilters()" />
                </td>
                <td>
                    <table-filter placeholder="Contact Person" filters="filters" property="contactPerson" apply-filters="applyFilters()" />
                </td>
                <td>
                    <table-filter placeholder="Contact Phone" filters="filters" property="contactPhone" apply-filters="applyFilters()" />
                </td>
            </tr>
            <tr ng-if="viewType != 'map' && (customers.totalElements == 0 || loading == true)">
                <td colspan="5">
                    <span ng-hide="loading">There are no customers</span>
                            <span ng-show="loading">
                                <img src="app/assets/images/loaders/loader19.gif" class="mr5">Loading customers...
                            </span>
                </td>
            </tr>

            <tr class="customers-view-type-map" ng-show="viewType == 'map'" style="height: 500px">
                <td colspan="6">
                    <div ng-include="mapViewTemplate" ng-controller="CustomersMapViewController"></div>
                </td>
            </tr>

            <tr ng-repeat="customer in customers.content" ng-show="viewType == 'grid'">
                <td class="col-center actions-col">
                    <div class="btn-group" dropdown style="margin-bottom: 0px;">
                        <button type="button" class="btn btn-sm btn-default dropdown-toggle" dropdown-toggle>
                            <i class="fa fa-cog fa-fw"></i></span>
                        </button>
                        <ul class="dropdown-menu" role="menu">
                            <%--<li><a href="" ng-click="openOrderDetials(order)">Open</a></li>--%>
                                <li><a href="" ng-click="showCustomerDetails(customer)">Open</a></li>
                        </ul>
                    </div>
                </td>
                <td><a href="" ng-click="showCustomerDetails(customer)">{{customer.name}}</a></td>
                <td>{{customer.salesRegion.name}}</td>
                <td>{{customer.contactPerson.firstName}}</td>
                <td>{{getContactPhone(customer)}}</td>
            </tr>
            </tbody>
        </table>
    </div>
</div>