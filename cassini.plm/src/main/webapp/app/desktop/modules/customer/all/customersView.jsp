<div>
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

        a.disabled {
            cursor: not-allowed;
            color: lightgrey;
        }

    </style>
    <div class="view-container" fitcontent>
        <div class="view-toolbar">
            <span style="font-weight: 600;font-size: 20px;padding-right: 10px;" translate>CUSTOMERS_TITLE</span>

            <button class="btn btn-sm new-button" id="new-customer" ng-click="customersVm.newCustomer()"
                    ng-if="hasPermission('customer','create')"
                    title="{{createCustomerTitle}}">
                <i class="las la-plus" aria-hidden="true"></i>
                <span>{{'NEW' | translate}} {{'CUSTOMER' | translate }}</span>
            </button>
            <button class="btn btn-sm btn-default" id="preferredPageButton"
                    style="" title="{{preferredPage}}"
                    ng-click="savePreferredPage()">
                <i class="fa fa fa-anchor" style=""></i>
            </button>

            <free-text-search on-clear="customersVm.resetPage" search-term="customersVm.searchText"
                              on-search="customersVm.freeTextSearch"
                              filter-search="customersVm.filterSearch"></free-text-search>
        </div>
        <div class="view-content no-padding" style="overflow-y: auto;padding: 10px;">
            <div class="responsive-table" style="padding: 10px;">
                <table class="table table-striped highlight-row">
                    <thead>
                    <tr>
                        <th class="col-width-150" translate>NAME</th>
                        <th class="col-width-150" translate>PHONE_NUMBER</th>
                        <th class="col-width-150" translate>EMAIL</th>
                        <th class="col-width-150" translate>ADDRESS</th>
                        <th class="col-width-150" translate>CONTACT_PERSON</th>
                        <th class="col-width-150" translate>PHONE_NUMBER</th>
                        <th class="col-width-150" translate>EMAIL</th>
                        <th class="col-width-250" translate>NOTES</th>
                        <th class="actions-col sticky-col sticky-actions-col" style="text-align:center; width: 80px;"
                            translate>ACTIONS
                        </th>
                    </tr>
                    </thead>
                    <tbody>
                    <tr ng-if="customersVm.loading == true">
                        <td colspan="25">
                        <span style="font-size: 15px;">
                            <img src="app/assets/bower_components/cassini-platform/images/loaders/loader19.gif"
                                 class="mr5">
                            <span translate>LOADING_CUSTOMERS</span>
                        </span>
                        </td>
                    </tr>

                    <tr ng-if="customersVm.loading == false && customersVm.customers.content.length == 0">
                        <td colspan="25" style="background-color: #f9fbfe  !important;color: unset !important;">
                            <div class="no-data">
                                <img src="app/assets/no_data_images/Customers.png" alt="" class="image">

                                <div class="message">{{ 'NO_CUSTOMERS' | translate}}</div>
                                <div class="message ng-scope" style="font-size: 14px;color: #8f979d;" translate>
                                    NO_PERMISSION_MESSAGE
                                </div>
                            </div>
                        </td>
                    </tr>

                    <tr ng-repeat="customer in customersVm.customers.content">
                        <td class="col-width-150">
                            <a href="" ng-click="customersVm.showCustomer(customer)"
                               title="{{'CLICK_TO_SHOW_DETAILS' | translate}}">
                                <span ng-bind-html="customer.name | highlightText: freeTextQuery"></span>
                            </a>
                        </td>
                        <td class="col-width-150">
                            <span ng-bind-html="customer.phone | highlightText: freeTextQuery"></span>
                        </td>
                        <td class="col-width-150"><span
                                ng-bind-html="customer.email | highlightText: freeTextQuery"></span></td>
                        <td class="col-width-150" title="{{customer.address}}">
                            <span ng-bind-html="customer.address  | highlightText: freeTextQuery"></span>
                        </td>
                        <td class="col-width-150">{{customer.contactPersonObject.fullName}}</td>
                        <td class="col-width-150">{{customer.contactPersonObject.phoneMobile}}</td>
                        <td class="col-width-150">{{customer.contactPersonObject.email}}</td>
                        <td class="col-width-250" title="{{customer.notes}}">
                            <span ng-bind-html="customer.notes  | highlightText: freeTextQuery"></span>
                        </td>
                        <td class="actions-col sticky-col sticky-actions-col" style="text-align:center; width: 80px;">
                        <span class="row-menu" uib-dropdown dropdown-append-to-body style="">
                         <i class="fa fa-ellipsis-h dropdown-toggle" uib-dropdown-toggle></i>
                            <ul uib-dropdown-menu class="dropdown-menu dropdown-menu-right" role="menu"
                                style="z-index: 9999 !important;">
                                <tags-button object-type="'CUSTOMER'" object="customer.id"
                                             tags-count="customer.tags.length"></tags-button>
                                <li ng-click="customersVm.editCustomer(customer)"
                                    ng-if="hasPermission('customer','edit')">
                                    <a href="" translate>EDIT</a>
                                </li>
                                <li title="{{customer.used ? cannotDeleteUsedCustomer:''}}">
                                    <a href="" ng-class="{'disabled':customer.used}"
                                       ng-click="customersVm.deleteCustomer(customer)"
                                       translate>DELETE</a>
                                </li>
                            </ul>
                        </span>
                        </td>
                    </tr>
                    </tbody>
                </table>
            </div>
            <div class="table-footer">
                <table-footer objects="customersVm.customers" pageable="customersVm.pageable"
                              previous-page="customersVm.previousPage"
                              next-page="customersVm.nextPage" page-size="customersVm.pageSize"></table-footer>
            </div>
        </div>
    </div>
</div>