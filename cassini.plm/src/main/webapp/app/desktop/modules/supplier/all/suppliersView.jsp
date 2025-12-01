<style>
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

    #freeTextSearchDirective {
        top: 7px !important;
    }
    .sticky-col {
        position: sticky !important;
        position: -webkit-sticky !important;
    }

    .sticky-actions-col {
        right: -10px !important;
    }
</style>
<div class="view-container" fitcontent>
    <div class="view-toolbar">
        <span style="font-weight: 600;font-size: 20px;padding-right: 10px;" translate>SUPPLIERS_TITLE</span>

        <div class="btn-group">
            <button class="btn btn-sm btn-success" ng-click="suppliersVm.newSupplier()"
                     title="{{createSupplierTitle}}">
                <i class="la la-plus" style="" aria-hidden="true"></i>
            </button>
        </div>
        <free-text-search on-clear="suppliersVm.resetPage" search-term="suppliersVm.searchText"
                          on-search="suppliersVm.freeTextSearch"
                          filter-search="suppliersVm.filterSearch"></free-text-search>
    </div>
    <div class="view-content no-padding" style="overflow-y: auto;padding: 10px;">
        <div class="responsive-table" style="padding: 10px;">
            <table class="table table-striped highlight-row">
                <thead>
                <tr>
                    <th style="width: 150px" translate>NAME</th>
                    <th style="width: 150px" translate>PHONE_NUMBER</th>
                    <th style="width: 150px" translate>ADDRESS</th>
                    <th style="width: 150px" translate>EMAIL</th>
                    <th style="width: 150px" translate>CONTACT_PERSON</th>
                    <th style="width: 150px" translate>PHONE_NUMBER</th>
                    <th style="width: 150px" translate>EMAIL</th>
                    <th style="width: 150px" translate>NOTES</th>
                    <th class="actions-col sticky-col sticky-actions-col" style="text-align:center; width: 80px;"
                        translate>ACTIONS
                    </th>
                </tr>
                </thead>
                <tbody>
                <tr ng-if="suppliersVm.loading == true">
                    <td colspan="25">
                        <span style="font-size: 15px;">
                            <img src="app/assets/bower_components/cassini-platform/images/loaders/loader19.gif"
                                 class="mr5">
                            <span translate>LOADING_SUPPLIERS</span>
                        </span>
                    </td>
                </tr>

                <tr ng-if="suppliersVm.loading == false && suppliersVm.suppliers.content.length == 0">
                    <td colspan="25" translate>NO_SUPPLIERS</td>
                </tr>

                <tr ng-repeat="supplier in suppliersVm.suppliers.content">
                    <td>
                        <span ng-bind-html="supplier.name | highlightText: freeTextQuery"></span>
                    </td>
                    <td>
                        <span ng-bind-html="supplier.phone | highlightText: freeTextQuery"></span>
                    </td>
                    <td>
                        <span ng-bind-html="supplier.address | highlightText: freeTextQuery"></span>
                    </td>
                    <td>
                        <span ng-bind-html="supplier.email | highlightText: freeTextQuery"></span>
                    </td>
                    <td>{{supplier.contactPersonObject.fullName}}</td>
                    <td>{{supplier.contactPersonObject.phoneMobile}}
                    </td>
                    <td>{{supplier.contactPersonObject.email}}</td>
                    <td><span ng-bind-html="supplier.notes | highlightText: freeTextQuery"></span></td>
                    <td class="actions-col sticky-col sticky-actions-col" style="text-align:center; width: 80px;">
                        <span class="row-menu" uib-dropdown dropdown-append-to-body style="">
                         <i class="fa fa-ellipsis-h dropdown-toggle" uib-dropdown-toggle></i>
                            <ul uib-dropdown-menu class="dropdown-menu dropdown-menu-right" role="menu"
                                style="z-index: 9999 !important;">
                                <li ng-click="suppliersVm.editSupplier(supplier)"
                                    ng-if="hasPermission('item','edit')">
                                    <a href="" translate>EDIT</a>
                                </li>
                                <li ng-click="suppliersVm.deleteSupplier(supplier)">
                                    <a href="" translate>DELETE</a>
                                </li>
                            </ul>
                        </span>
                    </td>
                </tr>
                </tbody>
            </table>
        </div>
        <div class="table-footer">
            <div>
                <div>
                    <h5><span style="padding: 5px" translate>DISPLAYING</span>
                        <span ng-if="suppliersVm.suppliers.totalElements ==0">{{(suppliersVm.pageable.page*suppliersVm.pageable.size)}}</span>
                        <span ng-if="suppliersVm.suppliers.totalElements > 0">{{(suppliersVm.pageable.page*suppliersVm.pageable.size)+1}}</span>
                        -
                        <span ng-if="suppliersVm.suppliers.last ==false">{{((suppliersVm.pageable.page+1)*suppliersVm.pageable.size)}}</span>
                        <span ng-if="suppliersVm.suppliers.last == true">{{suppliersVm.suppliers.totalElements}}</span>
                        <span translate>OF</span> &nbsp;{{suppliersVm.suppliers.totalElements}} <span
                                translate>AN</span>
                    </h5>
                </div>

                <div class="text-right">
                    <span class="mr10"><span translate>PAGE</span> {{suppliersVm.suppliers.totalElements != 0 ? suppliersVm.suppliers.number+1:0}} <sapn
                            translate>OF
                    </sapn> {{suppliersVm.suppliers.totalPages}}</span>
                    <a href="" ng-click="suppliersVm.previousPage()"
                       ng-class="{'disabled': suppliersVm.suppliers.first}"><i
                            class="fa fa-arrow-circle-left mr10"></i></a>
                    <a href="" ng-click="suppliersVm.nextPage()"
                       ng-class="{'disabled': suppliersVm.suppliers.last}"><i
                            class="fa fa-arrow-circle-right"></i></a>
                </div>


            </div>
        </div>
    </div>
</div>
