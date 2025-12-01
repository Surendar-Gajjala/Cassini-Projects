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

    #requestedItemsView .responsive-table {
        padding: 10px;
        position: absolute;
        bottom: 0px;
        top: 62px;
        overflow: auto;
    }

    #requestedItemsView .responsive-table table thead th {
        position: -webkit-sticky;
        position: sticky;
        top: -10px;
        z-index: 5;
        background-color: #fff;
    }

    #requestReportView .responsive-table {
        padding: 10px;
        position: absolute;
        bottom: 0px;
        top: 62px;
        overflow: auto;
    }

    #requestReportView .responsive-table table thead th {
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

    .widget {
        padding: 10px 20px 0 20px !important;
        cursor: pointer;
        vertical-align: middle;
    }

    .filter-model.modal {
        display: none; /* Hidden by default */
        position: fixed; /* Stay in place */
        z-index: 10; /* Sit on top */
        padding-top: 50px; /* Location of the box */
        left: 0;
        top: 0;
        width: 100%; /* Full width */
        height: 100%; /* Full height */
        overflow: auto; /* Enable scroll if needed */
        background-color: rgb(0, 0, 0); /* Fallback color */
        background-color: rgba(0, 0, 0, 0.4); /* Black w/ opacity */
    }

    .filter-model .report-content {
        margin-left: auto;
        margin-right: auto;
        top: 50px;
        display: block;
        height: 60%;
        width: 60%;
    }

    a.activeView {
        text-decoration: none;
        font-weight: bold;
        color: #121C25;
        font-size: 20px !important;
    }

    .ui-select-bootstrap > .ui-select-match > .btn {
        width: 100% !important;
    }

    .sections-model.modal {
        display: none;
        position: fixed;
        z-index: 1;
        padding-top: 15px;
        left: 0;
        top: 0;
        width: 100%;
        height: 100%;
        overflow: auto;
        background-color: rgb(0, 0, 0);
        background-color: rgba(0, 0, 0, 0.4);
    }

    .sections-model .modal-content {
        margin-left: auto;
        margin-right: auto;
        top: 111px;
        display: block;
        height: 40%;
        width: 50%;
    }

    .requestNumberLength {
        display: run-in;
        word-wrap: break-word;
        width: 350px;
        white-space: normal !important;
        text-align: left;
    }

    .notesLength {
        display: run-in;
        word-wrap: break-word;
        width: 200px;
        white-space: normal !important;
        text-align: left;
    }

    #freeTextSearchDirective {
        top: 7px !important;
        right: 0 !important;
    }

</style>
<div class="view-container" fitcontent>
    <div class="view-toolbar">
        <div class="btn-group">
            <button ng-if="hasPermission('permission.requests.new')" class="btn btn-sm btn-success"
                    ng-click="requestsVm.createNewRequest()">New Request
            </button>

            <%--<button ng-if="hasPermission('permission.admin.all')" class="btn btn-sm btn-success"
                    ng-click="requestsVm.showRequestUpdateWindow()">Update Request
            </button>--%>

            <span class="widget"
                  ng-if="hasPermission('permission.admin.all') || hasPermission('permission.requests.view')">
                <a ng-class="{'activeView':requestsVm.requestFilter.issued}"
                   title="{{requestsVm.requestFilter.issued ? 'Click for Requests' : 'Click for Issued Requests'}}"
                   ng-click="requestsVm.loadIssuedRequestsView()" style="text-decoration: none !important;">
                    <i ng-if="!requestsVm.requestFilter.issued" class="fa fa-square-o"></i>
                    <i ng-if="requestsVm.requestFilter.issued" class="fa fa-check-square-o"></i>
                    Issued Requests
                </a>
            </span>

            <span class="widget"
                  ng-if="hasPermission('permission.admin.all') || hasPermission('permission.requests.view')">
                <a ng-class="{'activeView':requestsVm.requestFilter.requested}"
                   title="{{requestsVm.requestFilter.requested ? 'Click for Requested items' : 'Click for Issued Requests'}}"
                   ng-click="requestsVm.loadRequestedItemsView()" style="text-decoration: none !important;">
                    <i ng-if="!requestsVm.requestFilter.requested" class="fa fa-square-o"></i>
                    <i ng-if="requestsVm.requestFilter.requested" class="fa fa-check-square-o"></i>
                    Requested Items
                </a>
            </span>
            <span class="widget"
                  ng-if="hasPermission('permission.admin.all') || hasPermission('permission.requests.view')">
                <a ng-class="{'activeView':requestsVm.requestFilter.report}"
                   title="{{requestsVm.requestFilter.report ? 'Click for Request Summary' : 'Click for Requests'}}"
                   ng-click="requestsVm.showRequestReport()" style="text-decoration: none !important;">
                    <i ng-if="!requestsVm.requestFilter.report" class="fa fa-square-o"></i>
                    <i ng-if="requestsVm.requestFilter.report" class="fa fa-check-square-o"></i>
                    Request Report
                </a>
                <i class="fa fa-print" ng-if="requestsVm.requestFilter.report"
                   ng-click="requestsVm.printRequestSummary()"></i>
            </span>
        </div>
        <a class="pull-right" style="cursor: pointer;margin-right: 325px;margin-top: 5px;"
           ng-click="requestsVm.addFilter()"
           ng-if="!requestsVm.filterMode && !requestsVm.requestFilter.requested && requestsVm.showSearchBox">
            <i title="Date Filter" style="font-size: 24px;" class="fa fa-filter"></i>
        </a>
        <a class="pull-right" style="cursor: pointer;margin-right: 325px;" ng-click="requestsVm.clearDateFilter()"
           ng-if="requestsVm.filterMode && !requestsVm.requestFilter.requested">
            <i title="Clear Filter" style="font-size: 30px;color:red;" class="fa fa-filter"></i>
        </a>
        <free-text-search ng-if="requestsVm.showSearchBox" on-clear="requestsVm.resetPage"
                          search-term="requestsVm.requestFilter.searchQuery"
                          on-search="requestsVm.freeTextSearch"></free-text-search>
    </div>
    <div class="view-content no-padding" style="padding: 10px;"
         ng-show="!requestsVm.requestFilter.requested && !requestsVm.requestFilter.report">
        <div class="responsive-table" style="padding: 10px;">
            <table class="table table-striped highlight-row">
                <thead>
                <tr>
                    <th class="header-row requestNumberLength">Request Number</th>
                    <th class="header-row">Instance</th>
                    <th class="header-row">Status</th>
                    <th class="header-row">Requested By</th>
                    <th class="header-row">Requested Date</th>
                    <th class="header-row notesLength">Notes</th>
                    <th class="header-row" style="width: 70px;">Actions</th>
                </tr>
                </thead>
                <tbody>
                <tr ng-if="requestsVm.loading == true">
                    <td colspan="25">
                        <span style="font-size: 15px;">
                            <img src="app/assets/bower_components/cassini-platform/images/loaders/loader19.gif"
                                 class="mr5">
                            <span>Loading Requests...</span>
                        </span>
                    </td>
                </tr>

                <tr ng-if="requestsVm.loading == false && requestsVm.requests.content.length == 0">
                    <td colspan="25">
                        <span ng-if="!requestsVm.filterMode && !requestsVm.requestFilter.issued">No Requests</span>
                        <span ng-if="requestsVm.filterMode && !requestsVm.requestFilter.issued && requestsVm.requestFilter.fromDate != null && requestsVm.requestFilter.toDate != null">
                            No Requests found between {{requestsVm.requestFilter.fromDate}} - {{requestsVm.requestFilter.toDate}}</span>
                        <span ng-if="requestsVm.filterMode && !requestsVm.requestFilter.issued && requestsVm.requestFilter.month != null">No Requests found in {{requestsVm.requestFilter.month}}</span>

                        <span ng-if="requestsVm.requestFilter.issued">No Issued Requests</span>
                    </td>
                </tr>

                <tr ng-repeat="request in requestsVm.requests.content">
                    <td class="requestNumberLength">
                        <a href="" ng-click="requestsVm.showRequest(request)" title="Click to show details">
                            <span ng-bind-html="request.reqNumber | highlightText: freeTextQuery"></span>
                            <span ng-if="request.newRequest && !requestsVm.requestFilter.issued"
                                  style="background: orange;color: white;font-size: 10px;font-weight: 600;padding: 3px;border-radius: 50%;">NEW</span>
                        </a>
                    </td>
                    <td>
                        <span ng-bind-html="request.bomInstance.item.instanceName | highlightText: freeTextQuery"></span>
                    </td>
                    <td>
                        <request-status request="request" title="{{request.reason}}"></request-status>
                    </td>
                    <td>
                        <span ng-bind-html="request.requestedBy.fullName | highlightText: freeTextQuery"></span>
                    </td>
                    <td>
                        <span ng-bind-html="request.requestedDate | highlightText: freeTextQuery"></span>
                    </td>
                    <td class="notesLength">{{request.notes}}</td>
                    <td style="width: 70px">
                        <button title="Delete Request"
                                class="btn btn-xs btn-danger"
                                ng-click="requestsVm.deleteRequest(request)">
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
                    <%-- <h5>Displaying {{requestsVm.requests.numberOfElements}} of
                         {{requestsVm.requests.totalElements}}</h5>--%>
                    <h5>
                        <span style="padding-right: 5px">Displaying</span>
                        <span ng-if="requestsVm.requests.totalElements == 0">
                            {{(requestsVm.pageable.page*requestsVm.pageable.size)}}
                        </span>
                        <span ng-if="requestsVm.requests.totalElements > 0">
                            {{(requestsVm.pageable.page*requestsVm.pageable.size)+1}}
                        </span>
                        -
                        <span ng-if="requestsVm.requests.last == false">{{((requestsVm.pageable.page+1)*requestsVm.pageable.size)}}</span>
                        <span ng-if="requestsVm.requests.last == true">{{requestsVm.requests.totalElements}}</span>


                        <span> of </span>{{requestsVm.requests.totalElements}}
                    </h5>
                </div>

                <div class="text-right">
                    <span class="mr10">Page {{requestsVm.requests.totalElements != 0 ? requestsVm.requests.number+1:0}} of {{requestsVm.requests.totalPages}}</span>
                    <a href="" ng-click="requestsVm.previousPage()"
                       ng-class="{'disabled': requestsVm.requests.first}"><i
                            class="fa fa-arrow-circle-left mr10"></i></a>
                    <a href="" ng-click="requestsVm.nextPage()"
                       ng-class="{'disabled': requestsVm.requests.last}"><i class="fa fa-arrow-circle-right"></i></a>
                </div>
            </div>
        </div>
    </div>

    <div class="view-content" id="requestedItemsView" ng-show="requestsVm.requestFilter.requested">
        <form class="form-inline" style="border: 1px solid lightgrey;padding: 10px;">
            <div class="form-group" style="margin-right: 0px;width: 25%">
                <label class="col-sm-4 control-label" style="margin-top: 10px;text-align: right;">BOM <span
                        class="asterisk">*</span> : </label>

                <div class="col-sm-7">
                    <ui-select ng-model="requestsVm.selectedBom" theme="bootstrap" style="width:100%"
                               on-select="requestsVm.onSelectBom($item)">
                        <ui-select-match
                                placeholder="{{requestsVm.selectBomTitle}}">
                            {{$select.selected.item.itemMaster.itemName}}
                        </ui-select-match>
                        <ui-select-choices
                                repeat="bom in requestsVm.boms track by bom.id">
                            <div ng-bind="bom.item.itemMaster.itemName"></div>
                        </ui-select-choices>
                    </ui-select>
                </div>
            </div>
            <div class="form-group" style="margin-right: 0px;width: 25%;" ng-if="requestsVm.selectedBom != null">
                <label class="col-sm-4 control-label" style="margin-top: 10px;text-align: right;">Instance <span
                        class="asterisk">*</span> : </label>

                <div class="col-sm-7">
                    <ui-select ng-model="requestsVm.bomInstance" theme="bootstrap"
                               style="width:100%"
                               on-select="requestsVm.onSelectInstance($item)">
                        <ui-select-match
                                placeholder="{{requestsVm.bomInstanceTitle}}">
                            {{$select.selected.item.instanceName}}
                        </ui-select-match>
                        <ui-select-choices
                                repeat="instance in requestsVm.bomInstances | filter: $select.search">
                            <div ng-bind="instance.item.instanceName"></div>
                        </ui-select-choices>
                    </ui-select>
                </div>
            </div>
            <div class="form-group" style="margin-right: 0px;width: 25%;" ng-if="requestsVm.bomInstance != null">
                <label class="col-sm-4 control-label" style="margin-top: 10px;text-align: right;">Section <span
                        class="asterisk">*</span> : </label>

                <div class="col-sm-7">
                    <ui-select ng-model="requestsVm.instanceSection" theme="bootstrap"
                               style="width:100%"
                               on-select="requestsVm.onSelectSection($item)">
                        <ui-select-match
                                placeholder="Section Section">
                            {{$select.selected.typeRef.name}}
                        </ui-select-match>
                        <ui-select-choices
                                repeat="section in requestsVm.instanceSections | filter: $select.search">
                            <div>
                                <span>{{section.typeRef.name}}</span>
                                <span ng-if="section.typeRef.versity"> ( VSPL )</span>
                            </div>
                        </ui-select-choices>
                    </ui-select>
                </div>
            </div>
        </form>

        <div class="responsive-table" style="padding: 10px;">
            <table class="table table-striped highlight-row">
                <thead>
                <tr>
                    <th class="twentyFivePercent-column">Nomenclature</th>
                    <th class="tenPercent-column">Type</th>
                    <th style="text-align: center">BOM Qty</th>
                    <th style="text-align: center">Requested Qty</th>
                    <th style="text-align: center">Issued Qty</th>
                    <th style="text-align: center">Failed Qty</th>
                    <th style="text-align: center">Balance Qty</th>
                    <th class="twentyFivePercent-column">Request(s)</th>
                </tr>
                </thead>
                <tbody>
                <tr ng-if="requestsVm.loading == true">
                    <td colspan="25">
                        <span style="font-size: 15px;">
                            <img src="app/assets/bower_components/cassini-platform/images/loaders/loader19.gif"
                                 class="mr5">
                            <span>Loading Requested Items...</span>
                        </span>
                    </td>
                </tr>

                <tr ng-if="requestsVm.loading == false && requestsVm.requestItems.length == 0">
                    <td colspan="25">
                        <span>No Requested Items found</span>
                    </td>
                </tr>

                <tr ng-if="requestsVm.loading == false"
                    ng-repeat="requestItem in requestsVm.requestItems | orderBy:'item.item.itemMaster.itemName'">
                    <td class="twentyFivePercent-column">
                        <%--<p class="level{{requestItem.level}}" ng-if="requestItem.type == 'SECTION'"
                           title="{{requestItem.expanded ? 'Collapse':'Expand'}}" style="margin:0;"
                           ng-click="requestsVm.toggleSection(requestItem)">
                            <i ng-if="requestItem.children.length > 0" class="mr5 fa"
                               style="cursor: pointer; color: #909090;font-size: 18px;"
                               ng-class="{'fa-caret-right': (requestItem.expanded == false || requestItem.expanded == null || requestItem.expanded == undefined),
                                          'fa-caret-down': requestItem.expanded == true}"></i>
                            <span style="font-weight: 600;color: black;">{{requestItem.name}}</span>
                        </p>--%>

                        <p style="margin:0;">
                            {{requestItem.item.item.itemMaster.itemName}}
                        </p>
                    </td>
                    <td class="tenPercent-column">
                        <span>{{requestItem.item.item.itemMaster.parentType.name}}</span>
                    </td>
                    <td style="text-align: center;">
                        <span class="badge badge-primary" style="font-size: 14px;"
                              ng-if="!requestItem.item.item.itemMaster.itemType.hasLots">{{requestItem.item.quantity}}</span>
                        <span class="badge badge-primary" style="font-size: 14px;"
                              ng-if="requestItem.item.item.itemMaster.itemType.hasLots">{{requestItem.item.fractionalQuantity}}</span>
                    </td>
                    <td style="text-align: center;">
                        <span class="badge badge-warning" style="font-size: 14px;"
                              ng-if="!requestItem.item.item.itemMaster.itemType.hasLots && requestItem.item.requestedQuantity > 0">{{requestItem.item.requestedQuantity}}</span>
                        <span class="badge badge-warning" style="font-size: 14px;"
                              ng-if="requestItem.item.item.itemMaster.itemType.hasLots && requestItem.item.fractionalRequestedQuantity > 0">{{requestItem.item.fractionalRequestedQuantity}}</span>
                    </td>
                    <td style="text-align: center;">
                        <span class="badge badge-success" ng-if="requestItem.item.issuedQuantity > 0"
                              style="font-size: 14px;">{{requestItem.item.issuedQuantity}}</span>
                    </td>
                    <td style="text-align: center;">
                        <span class="badge badge-danger" ng-if="requestItem.item.failedQuantity > 0"
                              style="font-size: 14px;">{{requestItem.item.failedQuantity}}</span>
                    </td>
                    <td style="text-align: center;">
                        <span class="badge badge-warning" style="font-size: 14px;"
                              ng-if="!requestItem.item.item.itemMaster.itemType.hasLots && (requestItem.item.requestedQuantity - requestItem.item.issuedQuantity > 0)">
                            {{requestItem.item.requestedQuantity - requestItem.item.issuedQuantity}}
                        </span>
                        <span class="badge badge-warning" style="font-size: 14px;"
                              ng-if="requestItem.item.item.itemMaster.itemType.hasLots && (requestItem.item.fractionalRequestedQuantity - requestItem.item.issuedQuantity > 0)">
                            {{requestItem.item.fractionalRequestedQuantity - requestItem.item.issuedQuantity}}
                        </span>
                    </td>
                    <td class="twentyFivePercent-column">
                        <div ng-repeat="request in requestItem.requests">
                            <ul style="margin: 0;padding: 2px 0">
                                <li>
                                    <a href="" ng-click="requestsVm.showRequest(request)" title="Click to show details">
                                        {{request.reqNumber}}
                                    </a>
                                </li>
                            </ul>
                        </div>
                    </td>
                </tr>
                </tbody>
            </table>
        </div>
    </div>

    <div class="view-content" id="requestReportView" ng-show="requestsVm.requestFilter.report">

        <form class="form-inline" style="border: 1px solid lightgrey;padding: 10px;">
            <div class="form-group" style="margin-right: 0px;width: 25%">
                <label class="col-sm-4 control-label" style="margin-top: 10px;text-align: right;">BOM <span
                        class="asterisk">*</span> : </label>

                <div class="col-sm-7">
                    <ui-select ng-model="requestsVm.selectedBom" theme="bootstrap" style="width:100%"
                               on-select="requestsVm.onSelectBom($item)">
                        <ui-select-match
                                placeholder="{{requestsVm.selectBomTitle}}">
                            {{$select.selected.item.itemMaster.itemName}}
                        </ui-select-match>
                        <ui-select-choices
                                repeat="bom in requestsVm.boms track by bom.id">
                            <div ng-bind="bom.item.itemMaster.itemName"></div>
                        </ui-select-choices>
                    </ui-select>
                </div>
            </div>
            <div class="form-group" style="margin-right: 0px;width: 25%;" ng-if="requestsVm.selectedBom != null">
                <label class="col-sm-4 control-label" style="margin-top: 10px;text-align: right;">Instance <span
                        class="asterisk">*</span> : </label>

                <div class="col-sm-7">
                    <ui-select ng-model="requestsVm.bomInstance" theme="bootstrap"
                               style="width:100%"
                               on-select="requestsVm.onSelectInstanceReport($item)">
                        <ui-select-match
                                placeholder="{{requestsVm.bomInstanceTitle}}">
                            {{$select.selected.item.instanceName}}
                        </ui-select-match>
                        <ui-select-choices
                                repeat="instance in requestsVm.bomInstances | filter: $select.search">
                            <div ng-bind="instance.item.instanceName"></div>
                        </ui-select-choices>
                    </ui-select>
                </div>
            </div>
        </form>

        <div class="responsive-table" style="padding: 10px;">
            <table class="table table-striped highlight-row" id="requestSummary">
                <thead>
                <tr>
                    <th>Request</th>
                    <th style="text-align: center">Requested Qty</th>
                    <th style="text-align: center">BDL</th>
                    <th style="text-align: center">CAS</th>
                    <th style="text-align: center">Store</th>
                    <th style="text-align: center">BDL QC</th>
                    <th style="text-align: center">BDL PPC</th>
                    <th style="text-align: center">Rejected</th>
                </tr>
                </thead>
                <tbody>
                <tr ng-if="requestsVm.loading == true">
                    <td colspan="25">
                        <span style="font-size: 15px;">
                            <img src="app/assets/bower_components/cassini-platform/images/loaders/loader19.gif"
                                 class="mr5">
                            <span>Loading Request Summary...</span>
                        </span>
                    </td>
                </tr>

                <tr ng-if="requestsVm.loading == false && requestsVm.requestSummaries.length == 0">
                    <td colspan="25">
                        <span>No Requests found</span>
                    </td>
                </tr>

                <tr ng-if="requestsVm.loading == false" ng-repeat="summary in requestsVm.requestSummaries">
                    <td>
                        <a href="" ng-click="requestsVm.showRequestSummary(summary.request)">{{summary.request.reqNumber}}</a>
                    </td>
                    <td style="text-align: center">
                        <span class="badge badge-info" style="font-size: 14px"
                              ng-show="summary.requestedQty > 0">{{summary.requestedQty}}</span>
                    </td>
                    <td style="text-align: center">
                        <span class="badge badge-primary" style="font-size: 14px"
                              ng-show="summary.acceptedQty > 0">{{summary.acceptedQty}}</span>
                    </td>
                    <td style="text-align: center">
                        <span class="badge badge-secondary" style="font-size: 14px"
                              ng-show="summary.approvedQty > 0">{{summary.approvedQty}}</span>
                    </td>
                    <td style="text-align: center">
                        <span class="badge badge-primary" style="font-size: 14px" ng-show="summary.issuedQty > 0">{{summary.issuedQty}}</span>
                    </td>
                    <td style="text-align: center">
                        <span class="badge badge-warning" style="font-size: 14px"
                              ng-show="summary.qcApprovedQty > 0">{{summary.qcApprovedQty}}</span>
                    </td>
                    <td style="text-align: center">
                        <span class="badge badge-success" style="font-size: 14px"
                              ng-show="summary.receivedQty > 0">{{summary.receivedQty}}</span>
                    </td>
                    <td style="text-align: center">
                        <span class="badge badge-danger" style="font-size: 14px"
                              ng-show="summary.rejectedQty > 0">{{summary.rejectedQty}}</span>
                    </td>
                </tr>
                </tbody>
            </table>
        </div>
    </div>

    <div id="add-filter" class="filter-model modal">
        <div class="report-content" style="background: white;">

            <div style="height: 10%;background: #dad1d1;">
                <h3 style="margin: 0;text-align: center;">Filters</h3>
            </div>
            <div style="height: 80%">

                <div style="display: inline-flex;width: 98%;border: 1px solid lightgrey;padding: 10px;margin: 1%;">
                    <form class="form-inline">
                        <div class="form-group"
                             style="border-right: 1px solid black;padding-right: 10px;margin-right: 0px !important;width: 21%;">
                            <h3 style="margin-top: 5px;">Date Range</h3>
                        </div>
                        <div class="form-group" style="margin-right: 0px !important;width: 35%;">
                            <label class="col-sm-5 control-label"
                                   style="margin-top: 10px;text-align: right;">From Date : <span
                                    class="asterisk">*</span> : </label>

                            <div class="col-sm-7">
                                <input type="text" ng-model="requestsVm.requestFilter.fromDate"
                                       placeholder="Select From Date" style="width: 100%"
                                       class="form-control" date-time-picker/>
                            </div>
                        </div>
                        <div class="form-group" style="margin-right: 0px !important;width: 34%;">
                            <label class="col-sm-5 control-label"
                                   style="margin-top: 10px;text-align: right;">To Date : <span
                                    class="asterisk">*</span> : </label>

                            <div class="col-sm-7">
                                <input type="text" ng-model="requestsVm.requestFilter.toDate"
                                       placeholder="Select To Date" style="width: 100%"
                                       class="form-control" date-time-picker>
                            </div>


                        </div>
                        <div class="form-group" style="margin-right: 0px !important;">
                            <button class="btn btn-primary btn-sm"
                                    ng-click="requestsVm.getFilterResults()">Search
                            </button>
                        </div>
                    </form>
                </div>

                <div style="display: inline-flex;width: 98%;border: 1px solid lightgrey;padding: 10px;margin: 1%;">
                    <form class="form-inline" style="width: 100%;">
                        <div class="form-group"
                             style="border-right: 1px solid black;padding-right: 10px;margin-right: 0px !important;width: 21%;">
                            <h3 style="margin-top: 5px;">Month Range</h3>
                        </div>
                        <div class="form-group" style="width: 40%;margin-right: 0px !important;">
                            <label class="col-sm-5 control-label"
                                   style="margin-top: 10px;text-align: right;">Select Month : <span
                                    class="asterisk">*</span> : </label>

                            <div class="col-sm-7">
                                <input type="text" ng-model="requestsVm.requestFilter.month"
                                       placeholder="Select From Date" style="width: 100%;"
                                       class="form-control" month-picker>
                            </div>
                        </div>
                        <div class="form-group" style="width: 25%;margin-right: 0px !important;">

                        </div>
                        <div class="form-group" style="margin-right: 0px !important;">
                            <button class="btn btn-primary btn-sm"
                                    ng-click="requestsVm.getMonthResults()">Search
                            </button>
                        </div>
                    </form>
                </div>

                <p ng-show="requestsVm.errorMessage != null"
                   style="color: darkred;font-weight: 600;font-size: 14px;margin-left:15px">
                    {{requestsVm.errorMessage}}</p>
            </div>
            <div style="height: 10%;background-color: lightgrey;text-align: center;">
                <div class="btn-group" style="margin-top: 5px;">
                    <button ng-click="requestsVm.cancelFilter()"
                            class="btn btn-xs btn-danger">Close
                    </button>
                </div>
            </div>
        </div>
    </div>


    <div id="requestUpdate-view" class="sections-model modal">
        <div class="modal-content">
            <h3 style="text-align: center;background: lavender;height: 20%;margin: 0px;padding-top: 10px;">
                Update Request
            </h3>

            <div style="height: 60%;">
                <div class="form-group" style="margin-right: 0px;width: 100%;margin-top: 10px;">

                    <label class="col-sm-3 control-label" style="text-align: right;margin-top: 10px;">Request
                        Number <span class="asterisk">*</span> : </label>

                    <div class="col-sm-9">
                        <input class="form-control" type="number" ng-model="requestsVm.requestNumber"
                               placeholder="Enter Request Number"/>
                    </div>
                </div>

                <div class="form-group" style="margin-right: 0px;width: 100%;margin-top: 10px;">

                    <label class="col-sm-3 control-label" style="text-align: right;margin-top: 10px;">Enter Part Name
                        <span class="asterisk">*</span> : </label>

                    <div class="col-sm-9">
                        <input class="form-control" type="text" ng-model="requestsVm.partName"
                               placeholder="Enter Part Name"/>
                    </div>
                </div>
            </div>
            <div style="height: 20%;text-align: center;background: lightgrey;">
                <button class="btn btn-sm btn-primary" ng-click="requestsVm.updateRequest()"
                        style="margin-top: 5px;">Submit
                </button>
                <button class="btn btn-sm btn-default" ng-click="requestsVm.closeWindow()"
                        style="margin-top: 5px;">Close
                </button>
            </div>
        </div>
    </div>
</div>
