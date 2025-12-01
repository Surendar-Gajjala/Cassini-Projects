<style scoped>
    .view-content {
        position: relative;
    }

    .view-content .responsive-table {
        padding: 10px;
        position: absolute;
        bottom: 40px;
        top: 0;
        overflow: auto;
    }

    .view-content .responsive-table table thead th {
        position: -webkit-sticky;
        position: sticky;
        top: -10px;
        z-index: 5;
        background-color: #fff;
    }

    .view-content .table-footer {
        padding: 0 10px 0 10px;
        position: absolute;
        bottom: 0px !important;
        height: 40px;
        width: 100%;
        border-top: 1px solid #D3D7DB;
        display: table;
    }

    .view-content .table-footer > div {
        display: table-row;
        line-height: 30px;
    }

    .view-content .table-footer > div h5 {
        margin: 0;
    }

    .view-content .table-footer > div > div {
        display: table-cell;
        vertical-align: middle;
    }

    .view-content .table-footer > div > div > i {
        font-size: 16px;
    }

    a.disabled {
        cursor: not-allowed;
        color: lightgrey;
    }

    .gatePassLength {
        display: run-in;
        word-wrap: break-word;
        width: 200px;
        white-space: normal !important;
        text-align: left;
    }

    .widget1 {
        padding: 5px 30px !important;
        cursor: pointer;
    }

    .notification1 {
        text-decoration: none;
        position: relative;
        display: inline-block;
        border-radius: 2px;
    }

    .notification1 .badge1 {
        position: absolute;
        top: -10px;
        right: -13px;
        padding: 0px 6px;
        border-radius: 50%;
        background-color: green;
        color: white;
        font-size: 14px;
    }

    a.activeView {
        text-decoration: none;
        font-weight: bold;
        color: #121C25;
        font-size: 20px !important;
    }

    #freeTextSearchDirective {
        top: 7px !important;
        right: 0 !important;
    }
</style>
<div class="view-container" fitcontent>
    <div class="view-toolbar">
        <div class="btn-group">
            <button ng-if="hasPermission('permission.manufacturers.new') || hasPermission('permission.suppliers.new')"
                    class="btn btn-sm btn-success" ng-click="procurementVm.newProcurement()">
                {{procurementVm.buttonName}}
            </button>
            <%--<button class="btn btn-sm btn-success" ng-click="procurementVm.newManufacturer()"
                    ng-if="hasPermission('permission.inward.new') && procurementVm.manufacturersView">New Manufacturer
            </button>--%>

            <span class="widget1"
                  ng-if="hasPermission('permission.admin.all') || hasPermission('permission.suppliers.view')">
                <a class="notification1" ng-class="{'activeView':procurementVm.suppliersView}"
                   title="Click for Suppliers"
                   ng-click="procurementVm.changeView('supplier')" style="top: 5px;">
                    <span ng-if="!procurementVm.suppliersView"
                          class="badge1">{{procurementVm.suppliers.totalElements}}</span>
                    Suppliers
                </a>
            </span>
            <span class="widget1"
                  ng-if="hasPermission('permission.admin.all') || hasPermission('permission.manufacturers.view')">
                <a class="notification1" ng-class="{'activeView':procurementVm.manufacturersView}"
                   title="Click for Manufacturers"
                   ng-click="procurementVm.changeView('manufacturer')" style="top: 5px;">
                    <span ng-if="!procurementVm.manufacturersView"
                          class="badge1">{{procurementVm.manufacturers.totalElements}}</span>
                    Manufacturers
                </a>
            </span>
        </div>
        <free-text-search on-clear="procurementVm.resetPage" search-term="procurementVm.searchText"
                          on-search="procurementVm.freeTextSearch"></free-text-search>
    </div>
    <div class="view-content no-padding" style="padding: 10px;">
        <div class="responsive-table" style="padding: 10px;" ng-if="procurementVm.suppliersView">
            <table class="table table-striped highlight-row">
                <thead>
                <tr>
                    <th>Supplier Name</th>
                    <th>Supplier Code</th>
                    <th>Contact person</th>
                    <th class="description-column">Address</th>
                    <th style="width: 70px;">Actions</th>
                </tr>
                </thead>
                <tbody>
                <tr ng-if="procurementVm.loading == true">
                    <td colspan="25">
                        <span style="font-size: 15px;">
                            <img src="app/assets/bower_components/cassini-platform/images/loaders/loader19.gif"
                                 class="mr5">
                            <span>Loading Suppliers...</span>
                        </span>
                    </td>
                </tr>

                <tr ng-if="procurementVm.loading == false && procurementVm.suppliers.content.length == 0">
                    <td colspan="25">No Suppliers</td>
                </tr>

                <tr ng-repeat="supplier in procurementVm.suppliers.content">
                    <td>
                        <span ng-bind-html="supplier.supplierName | highlightText: freeTextQuery"></span>
                    </td>
                    <td>
                        <span ng-bind-html="supplier.supplierCode | highlightText: freeTextQuery"></span>
                    </td>
                    <td>
                        <span ng-bind-html="supplier.contactPerson | highlightText: freeTextQuery"></span>
                        <span ng-if="supplier.phoneNumber != null">,</span>{{supplier.phoneNumber}}
                    </td>
                    <td class="description-column">
                        {{supplier.address.addressText}},{{supplier.address.city}},{{supplier.state.name}}
                        <span ng-if="supplier.pincode != null">,</span>
                        {{supplier.pincode}}
                    </td>
                    <td class="btn-group">
                        <button class="btn btn-xs btn-warning" title="Edit"
                                ng-click="procurementVm.editSupplier(supplier)">
                            <i class="fa fa-edit"></i>
                        </button>
                        <button title="Delete Supplier" class="btn btn-xs btn-danger"
                                ng-click="procurementVm.deleteSupplier(supplier)">
                            <i class="fa fa-trash"></i>
                        </button>
                    </td>
                </tr>
                </tbody>
            </table>
        </div>

        <div class="responsive-table" style="padding: 10px;" ng-if="procurementVm.manufacturersView">
            <table class="table table-striped highlight-row">
                <thead>
                <tr>
                    <th>Manufacturer Name</th>
                    <th>Manufacturer Code</th>
                    <th>Description</th>
                    <th>Phone Number</th>
                    <th>Email</th>
                    <th>Actions</th>
                </tr>
                </thead>
                <tbody>
                <tr ng-if="procurementVm.loading == true">
                    <td colspan="25">
                        <span style="font-size: 15px;">
                            <img src="app/assets/bower_components/cassini-platform/images/loaders/loader19.gif"
                                 class="mr5">
                            <span>Loading Manufacturers...</span>
                        </span>
                    </td>
                </tr>

                <tr ng-if="procurementVm.loading == false && procurementVm.manufacturers.content.length == 0">
                    <td colspan="25">No Manufacturers</td>
                </tr>

                <tr ng-repeat="manufacturer in procurementVm.manufacturers.content">
                    <td><span ng-bind-html="manufacturer.name | highlightText: freeTextQuery"></span></td>
                    <td><span ng-bind-html="manufacturer.mfrCode | highlightText: freeTextQuery"></span></td>
                    <td><span ng-bind-html="manufacturer.description | highlightText: freeTextQuery"></span></td>
                    <td><span ng-bind-html="manufacturer.phoneNumber | highlightText: freeTextQuery"></span></td>
                    <td><span ng-bind-html="manufacturer.email | highlightText: freeTextQuery"></span></td>
                    <td class="btn-group">
                        <button class="btn btn-xs btn-warning" title="Edit"
                                ng-click="procurementVm.editManufacturer(manufacturer)">
                            <i class="fa fa-edit"></i>
                        </button>
                        <button title="Delete Manufacturer" class="btn btn-xs btn-danger"
                                ng-click="procurementVm.deleteManufacturer(manufacturer)">
                            <i class="fa fa-trash"></i>
                        </button>
                    </td>
                </tr>
                </tbody>
            </table>
        </div>

        <div class="table-footer">
            <div>
                <div>
                    <h5 ng-show="procurementVm.suppliersView">
                        Displaying {{procurementVm.suppliers.numberOfElements}} of
                        {{procurementVm.suppliers.totalElements}}
                    </h5>
                    <h5 ng-show="procurementVm.manufacturersView">
                        Displaying {{procurementVm.manufacturers.numberOfElements}} of
                        {{procurementVm.manufacturers.totalElements}}
                    </h5>
                </div>

                <div class="text-right">
                    <span ng-show="procurementVm.suppliersView">
                        <span class="mr10">Page {{procurementVm.suppliers.totalElements != 0 ? procurementVm.suppliers.number+1:0}} of {{procurementVm.suppliers.totalPages}}</span>
                            <a href="" ng-click="procurementVm.previousPage()"
                               ng-class="{'disabled': procurementVm.suppliers.first}">
                                <i class="fa fa-arrow-circle-left mr10"></i>
                            </a>
                            <a href="" ng-click="procurementVm.nextPage()"
                               ng-class="{'disabled': procurementVm.suppliers.last}">
                                <i class="fa fa-arrow-circle-right"></i>
                            </a>
                    </span>
                    <span ng-show="procurementVm.manufacturersView">
                        <span class="mr10">Page {{procurementVm.manufacturers.totalElements != 0 ? procurementVm.manufacturers.number+1:0}} of {{procurementVm.manufacturers.totalPages}}</span>
                            <a href="" ng-click="procurementVm.previousPage()"
                               ng-class="{'disabled': procurementVm.manufacturers.first}">
                                <i class="fa fa-arrow-circle-left mr10"></i>
                            </a>
                            <a href="" ng-click="procurementVm.nextPage()"
                               ng-class="{'disabled': procurementVm.manufacturers.last}">
                                <i class="fa fa-arrow-circle-right"></i>
                            </a>
                    </span>
                </div>
            </div>
        </div>
    </div>
</div>
