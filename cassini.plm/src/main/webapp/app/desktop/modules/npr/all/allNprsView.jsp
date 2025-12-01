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
            <span style="font-weight: 600;font-size: 20px;padding-right: 10px;" translate="NEW_PART_REQUESTS"></span>

            <button class="btn btn-sm new-button" id="new-npr" ng-click="allNprsVm.newPartRequest()"
                    ng-if="hasPermission('plmnpr','create')"
                    title="{{newPartRequest}}">
                <i class="las la-plus" aria-hidden="true"></i>
                <span>{{'NEW_PART_REQUEST' | translate}}</span>
            </button>
            <button class="btn btn-sm btn-default"
                    style="" title="{{preferredPage}}"
                    ng-click="savePreferredPage()">
                <i class="fa fa fa-anchor" style=""></i>
            </button>

            <free-text-search on-clear="allNprsVm.resetPage" search-term="allNprsVm.searchText"
                              on-search="allNprsVm.freeTextSearch"
                              filter-search="allNprsVm.filterSearch"></free-text-search>
        </div>
        <div class="view-content no-padding" style="overflow-y: auto;padding: 10px;">
            <div class="responsive-table" style="padding: 10px;">
                <table class="table table-striped highlight-row">
                    <thead>
                    <tr>
                        <th class="col-width-150" translate>NUMBER</th>
                        <th style="width: 150px" translate>REQUESTER</th>
                        <th class="col-width-250" translate>DESCRIPTION</th>
                        <th style="width: 100px" translate>STATUS</th>
                        <th class="col-width-250" translate>REASON_FOR_REQUEST</th>
                        <th class="col-width-250" translate>NOTES</th>
                        <th class="actions-col sticky-col sticky-actions-col" style="text-align:center; width: 80px;"
                            translate>ACTIONS
                        </th>
                    </tr>
                    </thead>
                    <tbody>
                    <tr ng-if="allNprsVm.loading == true">
                        <td colspan="25">
                        <span style="font-size: 15px;">
                            <img src="app/assets/bower_components/cassini-platform/images/loaders/loader19.gif"
                                 class="mr5">
                            <span translate>LOADING_PART_REQUESTS</span>
                        </span>
                        </td>
                    </tr>

                    <tr ng-if="allNprsVm.loading == false && allNprsVm.nprs.content.length == 0">
                        <td colspan="25" style="background-color: #f9fbfe  !important;color: unset !important;">
                            <div class="no-data">
                                <img src="app/assets/no_data_images/Customers.png" alt="" class="image">

                                <div class="message">{{ 'NO_PART_REQUESTS' | translate}}</div>
                                <div class="message ng-scope" style="font-size: 14px;color: #8f979d;" translate>NO_PERMISSION_MESSAGE</div>
                            </div>
                        </td>
                    </tr>

                    <tr ng-repeat="npr in allNprsVm.nprs.content">
                        <td style="width: 150px;">
                            <a href="" ng-click="allNprsVm.showNpr(npr)"
                               title="{{'CLICK_TO_SHOW_DETAILS' | translate}}">
                                <span ng-bind-html="npr.number | highlightText: freeTextQuery"></span>
                            </a>
                        </td>
                        <td class="col-width-150">
                            <span ng-bind-html="npr.requesterObject.fullName | highlightText: freeTextQuery"></span>
                        </td>
                        <td class="col-width-250" title="{{npr.description}}">
                            <span ng-bind-html="npr.description  | highlightText: freeTextQuery"></span>
                        </td>
                        <td>
                            <span class="label label-outline bg-light-warning" ng-if="npr.status == 'HOLD'">PENDING</span>
                            <object-status object="npr"></object-status>
                        </td>

                        <td class="col-width-250" title="{{npr.reasonForRequest}}">
                            <span ng-bind-html="npr.reasonForRequest  | highlightText: freeTextQuery"></span>
                        </td>
                        <td class="col-width-250" title="{{npr.notes}}">
                            <span ng-bind-html="npr.notes  | highlightText: freeTextQuery"></span>
                        </td>
                        <td class="actions-col sticky-col sticky-actions-col" style="text-align:center; width: 80px;">
                        <span class="row-menu" uib-dropdown dropdown-append-to-body style="">
                         <i class="fa fa-ellipsis-h dropdown-toggle" uib-dropdown-toggle></i>
                            <ul uib-dropdown-menu class="dropdown-menu dropdown-menu-right" role="menu"
                                style="z-index: 9999 !important;">
                                <li title="{{npr.titleMsg}}">
                                    <a href="" ng-class="{'disabled':npr.status != 'OPEN'}"
                                       ng-if="hasPermission('plmnpr','delete')"
                                       ng-click="allNprsVm.deleteNpr(npr)"
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
                <table-footer objects="allNprsVm.nprs" pageable="allNprsVm.pageable"
                              previous-page="allNprsVm.previousPage"
                              next-page="allNprsVm.nextPage"
                              page-size="allNprsVm.pageSize"></table-footer>
            </div>
        </div>
    </div>
</div>