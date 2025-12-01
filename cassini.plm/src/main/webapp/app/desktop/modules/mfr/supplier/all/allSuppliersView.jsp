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

    .sticky-col {
        position: sticky !important;
        position: -webkit-sticky !important;
    }

    .sticky-actions-col {
        right: -10px !important;
    }

    #freeTextSearchDirective {
        top: 7px !important;
    }

    .attributeTooltip {
        position: relative;
        display: inline-block;
    }

    .attributeTooltip .attributeTooltiptext {
        visibility: hidden;
        width: 200px;
        background-color: #7BB7EB;
        color: #141f9f;
        text-align: left;
        border-radius: 6px;
        padding: 5px 0;
        position: absolute;
        z-index: 1;
        top: -5px;
        bottom: auto;
        right: 100%;
        opacity: 0;
        transition: opacity 1s;
    }

    .attributeTooltip .attributeTooltiptext::after {
        content: "";
        position: absolute;
        top: 25%;
        left: 102%;
        margin-left: -5px;
        border-width: 5px;
        border-style: solid;
        border-color: transparent transparent transparent #7BB7EB;
    }

    .attributeTooltip:hover .attributeTooltiptext {
        visibility: visible;
        opacity: 1;
    }
</style>
<div class="view-container" fitcontent>
    <div class="view-toolbar">
        <span style="font-weight: 600;font-size: 20px;padding-right: 10px;" translate>SUPPLIERS</span>

        <button class="btn btn-sm new-button" ng-click="allSuppliersVm.newSupplier()" id="newSupplier"
                title="{{'NEW_SUPPLIER' | translate}}" ng-if="hasPermission('mfrsupplier','create')">
            <i class="las la-plus" aria-hidden="true"></i>
            <span>{{'NEW' | translate}} {{'SUPPLIER' | translate }}</span>
        </button>

        <div class="btn-group">
            <button class="btn btn-sm btn-success" ng-click="allSuppliersVm.showTypeAttributes()" id="attributesButton"
                    title="{{'ALL_VIEW_ADD_ATTRIBUTES' | translate}}">
                <i class="fa fa-newspaper-o" style=""></i>
            </button>
            <button class="btn btn-sm btn-default" id="preferedPageButton"
                    style="" title="{{preferredPage}}"
                    ng-click="savePreferredPage()">
                <i class="fa fa fa-anchor" style=""></i>
            </button>
        </div>
        <free-text-search on-clear="allSuppliersVm.resetPage" search-term="allSuppliersVm.searchText"
                          on-search="allSuppliersVm.freeTextSearch"
                          filter-search="allSuppliersVm.filterSearch"></free-text-search>
    </div>
    <div class="view-content no-padding" style="overflow-y: auto;padding: 10px;">
        <div class="responsive-table" style="padding: 10px;">
            <table class="table table-striped highlight-row">
                <thead>
                <tr>
                    <th class="col-width-150" translate>NUMBER</th>
                    <th class="col-width-150" translate>TYPE</th>
                    <th class="col-width-150" translate>NAME</th>
                    <th class="col-width-250" translate>DESCRIPTION</th>
                    <th class="col-width-150" translate>LIFECYCLE</th>
                    <th class="col-width-150" translate>ADDRESS</th>
                    <th class="col-width-150" translate>CITY</th>
                    <th class="col-width-150" translate>COUNTRY</th>
                    <th class="col-width-150" translate>PHONE_NUMBER</th>
                    <th class="col-width-150" translate>MOBILE_NUMBER</th>
                    <th class="col-width-150" translate>FAX_NUMBER</th>
                    <th class="col-width-150" translate>EMAIL</th>
                    <th class="col-width-150" translate>WEBSITE</th>
                    <th class='added-column'
                        style="width: 200px;z-index: auto !important;"
                        ng-repeat="selectedAttribute in allSuppliersVm.selectedAttributes">
                        {{selectedAttribute.name}}
                        <i class="fa fa-times-circle"
                           ng-click="allSuppliersVm.removeAttribute(selectedAttribute)"
                           title={{allSuppliersVm.RemoveColumnTitle}}></i>
                    </th>
                    <th class="actions-col sticky-col sticky-actions-col" style="text-align:center; width: 80px;"
                        translate>ACTIONS
                    </th>
                </tr>
                </thead>
                <tbody>
                <tr ng-if="allSuppliersVm.loading == true">
                    <td colspan="25">
            <span style="font-size: 15px;">
              <img src="app/assets/bower_components/cassini-platform/images/loaders/loader19.gif"
                   class="mr5">
              <span translate>LOADING_SUPPLIERS</span>
            </span>
                    </td>
                </tr>

                <tr ng-if="allSuppliersVm.loading == false && allSuppliersVm.suppliers.content.length == 0">
                    <td colspan="25" style="background-color: #f9fbfe  !important;color: unset !important;">
                        <div class="no-data">
                            <img src="app/assets/no_data_images/Supplierr.png" alt="" class="image">

                            <div class="message" translate>NO_SUPPLIERS</div>
                            <div class="message ng-scope" style="font-size: 14px;color: #8f979d;" translate>
                                NO_PERMISSION_MESSAGE
                            </div>
                        </div>
                    </td>
                </tr>
                <tr ng-repeat="supplier in allSuppliersVm.suppliers.content">

                    <td class="col-width-150">
                        <a href="" ng-click="allSuppliersVm.showSupplier(supplier)"
                           title="{{'CLICK_TO_SHOW_DETAILS' | translate}}">
                            <span ng-bind-html="supplier.number | highlightText: freeTextQuery"></span>
                        </a>
                    </td>
                    <td class="col-width-150">
                        <span ng-bind-html="supplier.supplierType.name | highlightText: freeTextQuery"></span>
                    </td>

                    <td class="col-width-150">
                        <a href="" ng-click="allSuppliersVm.showSupplier(supplier)"
                           title="{{'CLICK_TO_SHOW_DETAILS' | translate}}">
                            <span ng-bind-html="supplier.name | highlightText: freeTextQuery"></span>
                        </a>
                    </td>

                    <td class="col-width-250" title="{{supplier.description}}">
                        <span ng-bind-html="supplier.description  | highlightText: freeTextQuery"></span>
                    </td>
                    <td class="col-width-150">
                        <item-status item="supplier"></item-status>
                    </td>
                    <td class="col-width-150">
                        <span ng-bind-html="supplier.address | highlightText: freeTextQuery"></span>
                    </td>
                    <td class="col-width-150"><span ng-bind-html="supplier.city | highlightText: freeTextQuery"></span>
                    </td>
                    <td class="col-width-150"><span
                            ng-bind-html="supplier.country | highlightText: freeTextQuery"></span></td>
                     <td class="col-width-150"><span
                                ng-bind-html="supplier.mobileNumber | highlightText: freeTextQuery"></span></td> 
                    <td class="col-width-150"><span
                            ng-bind-html="supplier.phoneNumber | highlightText: freeTextQuery"></span></td> 
                    <td class="col-width-150"><span
                                ng-bind-html="supplier.faxNumber | highlightText: freeTextQuery"></span></td>   
                    <td class="col-width-150">
                        <span ng-bind-html="supplier.email | highlightText: freeTextQuery"></span>
                    </td>
                    <td class="col-width-150"><span
                            ng-bind-html="supplier.webSite | highlightText: freeTextQuery"></span></td>
                    <td class="added-column"
                        ng-repeat="objectAttribute in allSuppliersVm.selectedAttributes">
                        <all-view-attributes object="supplier"
                                             object-attribute="objectAttribute"></all-view-attributes>
                    </td>
                    <td class="text-center actions-col sticky-col sticky-actions-col">
            <span class="row-menu" uib-dropdown dropdown-append-to-body style="min-width: 50px">
              <i class="fa fa-ellipsis-h dropdown-toggle" uib-dropdown-toggle></i>
              <ul uib-dropdown-menu class="dropdown-menu dropdown-menu-right" role="menu"
                  style="z-index: 9999 !important;">
                  <li>
                      <a href=""
                         ng-click="showPrintOptions(supplier.id,'MFRSUPPLIER')" translate>PREVIEW_AND_PRINT</a>
                  </li>
                  <tags-button object-type="'MFRSUPPLIER'" object="supplier.id"
                               tags-count="supplier.tags.length"></tags-button>

                               <li title="{{supplier.lifeCyclePhase.phaseType == 'RELEASED'? cannotDeleteApprovedSupplier:''}}">
                                <a href="" ng-click="allSuppliersVm.deleteSupplier(supplier)"
                            ng-if="hasPermission('supplier','delete')"
                         ng-class="{'disabled': supplier.lifeCyclePhase.phaseType == 'RELEASED'}" translate>DELETE</a>
            
                            </li>
                            <plugin-table-actions context="supplier.all" object-value="supplier"></plugin-table-actions>
              </ul>
            </span>
                    </td>
                </tr>
                </tbody>
            </table>
        </div>
        <div class=" table-footer">
            <table-footer objects="allSuppliersVm.suppliers" pageable="allSuppliersVm.pageable"
                          previous-page="allSuppliersVm.previousPage" page-size="allSuppliersVm.pageSize"
                          next-page="allSuppliersVm.nextPage"></table-footer>
        </div>
    </div>
</div>
