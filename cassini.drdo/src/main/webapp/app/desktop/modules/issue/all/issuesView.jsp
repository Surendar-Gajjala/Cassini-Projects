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

    a.disabled {
        cursor: not-allowed;
        color: lightgrey;
    }

    .widget {
        padding: 10px 20px 0 20px !important;
        cursor: pointer;
        vertical-align: middle;
        color: black;
    }

    a.activeView {
        text-decoration: none;
        font-weight: bold;
        color: #121C25;
        font-size: 20px !important;
    }

    #issuedItemsView .responsive-table {
        padding: 10px;
        position: absolute;
        bottom: 0px;
        top: 62px;
        overflow: auto;
    }

    #issuedItemsView .responsive-table table thead th {
        position: -webkit-sticky;
        position: sticky;
        top: -10px;
        z-index: 5;
        background-color: #fff;
    }

    .ui-select-bootstrap > .ui-select-match > .btn {
        width: 100% !important;
    }

    .missile-dropDown {
        display: none;
        position: absolute;
        border: 1px solid lightgrey;
        z-index: 100;
        background-color: #fff;
        padding: 5px;
        width: 300px;
        height: 120px;
        box-shadow: 0px 3px 6px rgba(0, 0, 0, 0.16);
        font-weight: normal;
    }

    tr.section {
        background: lightcoral !important;
    }

    tr.section td {
        background: lightcoral !important;
    }

    tr.subsystem {
        background: lightpink !important;
    }

    tr.subsystem td {
        background: lightpink !important;
    }

    #freeTextSearchDirective {
        top: 7px !important;
        right: 0 !important;
    }
</style>
<div class="view-container" fitcontent>
    <div class="view-toolbar">
        <div class="btn-group">
            <button ng-if="hasPermission('permission.issued.new')" class="btn btn-sm btn-success"
                    ng-click="issuesVm.createNewIssue()">New Issue
            </button>

            <span class="widget"
                  ng-if="hasPermission('permission.admin.all') || hasPermission('permission.issued.view')">
                <a ng-class="{'activeView':issuesVm.issueFilter.report}"
                   title="{{issuesVm.issueFilter.report ? 'Click for Issue Report' : 'Click for Issues'}}"
                   ng-click="issuesVm.showIssueReport()" style="text-decoration: none !important;">
                    <i ng-if="!issuesVm.issueFilter.report" class="fa fa-square-o"></i>
                    <i ng-if="issuesVm.issueFilter.report" class="fa fa-check-square-o"></i>
                    Issue Report
                </a>
            </span>
        </div>
        <a class="pull-right" style="cursor: pointer;margin-right: 325px;margin-top: 5px;"
           ng-click="issuesVm.addFilter()" ng-if="!issuesVm.filterMode && !issuesVm.issueFilter.report">
            <i style="font-size: 24px;" title="Date Filter" class="fa fa-filter"></i>
        </a>
        <a class="pull-right" style="cursor: pointer;margin-right: 325px;"
           ng-click="issuesVm.clearDateFilter()" ng-if="issuesVm.filterMode">
            <i title="Clear Filter" style="font-size: 30px;color:red;" class="fa fa-filter"></i>
        </a>
        <free-text-search ng-if="issuesVm.showSearchBox" on-clear="issuesVm.resetPage" search-term="issuesVm.searchText"
                          on-search="issuesVm.freeTextSearch"></free-text-search>
    </div>
    <div class="view-content no-padding" style="padding: 10px;" ng-show="issuesVm.issueFilter.issues">
        <div class="responsive-table" style="padding: 10px;">
            <table class="table table-striped highlight-row">
                <thead>
                <tr>
                    <th>Issue Number</th>
                    <th>
                        Instance
                        <i class="fa fa-caret-down" ng-click="issuesVm.showInstances()" style="cursor: pointer;"></i>
                        <span ng-if="issuesVm.selectedMissile != null">
                            ( {{issuesVm.selectedMissile.item.instanceName}} )
                            <i class="fa fa-times-circle" ng-click="issuesVm.resetMissileSearch()" title="Clear Search"
                               style="cursor: pointer"></i>
                        </span>

                        <div id="missileDropDown" class="missile-dropDown">
                            <div class="form-group" style="margin-right: 0px;">

                                <label class="col-sm-4 control-label">BOM <span class="asterisk">*</span> : </label>

                                <div class="col-sm-7">
                                    <ui-select ng-model="issuesVm.selectedBom" theme="bootstrap" style="width:100%"
                                               on-select="issuesVm.onSelectBom($item)">
                                        <ui-select-match
                                                placeholder="{{issuesVm.selectBomTitle}}">
                                            {{$select.selected.item.itemMaster.itemName}}
                                        </ui-select-match>
                                        <ui-select-choices
                                                repeat="bom in issuesVm.boms track by bom.id">
                                            <div ng-bind="bom.item.itemMaster.itemName"></div>
                                        </ui-select-choices>
                                    </ui-select>
                                </div>
                            </div>

                            <div class="form-group" style="margin-right: 0px;">

                                <label class="col-sm-4 control-label">Instance <span class="asterisk">*</span> :
                                </label>

                                <div class="col-sm-7">
                                    <ui-select ng-model="issuesVm.selectedMissile" theme="bootstrap"
                                               style="width:100%" ng-disabled="issuesVm.selectedBom == null"
                                               on-select="issuesVm.onSelectInstanceSearch($item)">
                                        <ui-select-match
                                                placeholder="{{issuesVm.bomInstanceTitle}}">
                                            {{$select.selected.item.instanceName}}
                                        </ui-select-match>
                                        <ui-select-choices
                                                repeat="instance in issuesVm.bomInstances | filter: $select.search">
                                            <div ng-bind="instance.item.instanceName"></div>
                                        </ui-select-choices>
                                    </ui-select>
                                </div>
                            </div>
                        </div>
                    </th>
                    <th>Status</th>
                    <th>Last Updated By</th>
                    <th>Last Updated</th>
                    <th>Request Number</th>
                    <th>Requested By</th>
                    <th>Notes</th>
                    <%--<th style="width: 70px;">Actions</th>--%>
                </tr>
                </thead>
                <tbody>
                <tr ng-if="issuesVm.loading == true">
                    <td colspan="25">
                        <span style="font-size: 15px;">
                            <img src="app/assets/bower_components/cassini-platform/images/loaders/loader19.gif"
                                 class="mr5">
                            <span>Loading Issues...</span>
                        </span>
                    </td>
                </tr>

                <tr ng-if="issuesVm.loading == false && issuesVm.issues.content.length == 0">
                    <td colspan="25" ng-if="!issuesVm.filterMode">No Issues</td>
                    <td colspan="25"
                        ng-if="issuesVm.filterMode && issuesVm.issueFilter.fromDate != null && issuesVm.issueFilter.toDate != null">
                        No Issues found between {{issuesVm.issueFilter.fromDate}} - {{issuesVm.issueFilter.toDate}}
                    </td>
                    <td colspan="25" ng-if="issuesVm.filterMode && issuesVm.issueFilter.month != null">No Issues found
                        in {{issuesVm.issueFilter.month}}
                    </td>
                </tr>

                <tr ng-repeat="issue in issuesVm.issues.content">
                    <td>
                        <a href="" ng-click="issuesVm.showIssue(issue)" title="Click to show details">
                            <span ng-bind-html="issue.number | highlightText: freeTextQuery"></span>
                        </a>
                    </td>
                    <td>
                        <span ng-bind-html="issue.bomInstance.item.instanceName | highlightText: freeTextQuery"></span>
                    </td>
                    <td>
                        <span class="badge badge-warning"
                              ng-if="issue.status == 'BDL_QC' || issue.status == 'BDL_PPC' || issue.status == 'VERSITY_QC' || issue.status == 'VERSITY_PPC'"
                              style="font-size: 13px;">
                            <i ng-if="issue.provisionalApprove" class="fa fa-shield"
                               style="padding: 0 3px;color: white;font-size: 14px"
                               title="Issue has Provisionally Approved Items"></i>{{issue.status}}
                        </span>
                        <span class="badge badge-danger"
                              ng-if="issue.status == 'REJECTED' || issue.status == 'PARTIALLY_REJECTED'"
                              style="font-size: 13px;">
                            <i ng-if="issue.provisionalApprove" class="fa fa-shield"
                               style="padding: 0 3px;color: white;font-size: 14px"
                               title="Issue has Provisionally Approved Items"></i>{{issue.status}}
                        </span>
                        <span class="badge badge-primary"
                              ng-if="issue.status == 'PARTIALLY_RECEIVED' || issue.status == 'PARTIALLY_APPROVED'"
                              style="font-size: 13px;">
                            <i ng-if="issue.provisionalApprove" class="fa fa-shield"
                               style="padding: 0 3px;color: white;font-size: 14px"
                               title="Issue has Provisionally Approved Items"></i>{{issue.status}}
                        </span>
                        <span class="badge badge-success"
                              ng-if="issue.status == 'RECEIVED' || issue.status == 'APPROVED'" style="font-size: 13px;">
                            <i ng-if="issue.provisionalApprove" class="fa fa-shield"
                               style="padding: 0 3px;color: white;font-size: 14px"
                               title="Issue has Provisionally Approved Items"></i>{{issue.status}}
                        </span>
                        <span class="badge badge-secondary"
                              ng-if="issue.status == 'ITEM_RESET'" style="font-size: 13px;">
                            {{issue.status}}
                        </span>
                        <%--<issue-status issue="issue"></issue-status>--%>
                    </td>
                    <td>
                        <span ng-bind-html="issue.modifiedByObject.fullName | highlightText: freeTextQuery"></span>
                    </td>
                    <td>
                        <span ng-bind-html="issue.modifiedDate"></span>
                    </td>
                    <td>
                        <span ng-bind-html="issue.request.reqNumber | highlightText: freeTextQuery"></span>
                    </td>
                    <td>
                        <span ng-bind-html="issue.request.requestedBy.fullName | highlightText: freeTextQuery"></span>
                    </td>
                    <td>{{issue.notes}}</td>
                    <%--<td style="width: 70px">
                        <div class="btn-group">
                            &lt;%&ndash;<button title="Edit Item" class="btn btn-xs btn-warning"
                                    ng-click="issuesVm.saveAs(issue)">
                                <i class="fa fa-edit"></i>
                            </button>&ndash;%&gt;
                            &lt;%&ndash;<button title="Delete Inward"
                                    class="btn btn-xs btn-danger"
                                    ng-click="issuesVm.deleteInward(issue)">
                                <i class="fa fa-trash"></i>
                            </button>&ndash;%&gt;
                        </div>
                    </td>--%>
                </tr>
                </tbody>
            </table>
        </div>
        <div class="table-footer">
            <div>
                <div>
                    <%--<h5>Displaying {{issuesVm.issues.numberOfElements}} of {{issuesVm.issues.totalElements}}</h5>--%>
                    <h5>
                        <span style="padding-right: 5px">Displaying</span>
                        <span ng-if="issuesVm.issues.totalElements == 0">
                            {{(issuesVm.pageable.page*issuesVm.pageable.size)}}
                        </span>
                        <span ng-if="issuesVm.issues.totalElements > 0">
                            {{(issuesVm.pageable.page*issuesVm.pageable.size)+1}}
                        </span>
                        -
                        <span ng-if="issuesVm.issues.last == false">{{((issuesVm.pageable.page+1)*issuesVm.pageable.size)}}</span>
                        <span ng-if="issuesVm.issues.last == true">{{issuesVm.issues.totalElements}}</span>


                        <span> of </span>{{issuesVm.issues.totalElements}}
                    </h5>
                </div>

                <div class="text-right">
                    <span class="mr10">Page {{issuesVm.issues.totalElements != 0 ? issuesVm.issues.number+1:0}} of {{issuesVm.issues.totalPages}}</span>
                    <a href="" ng-click="issuesVm.previousPage()"
                       ng-class="{'disabled': issuesVm.issues.first}"><i class="fa fa-arrow-circle-left mr10"></i></a>
                    <a href="" ng-click="issuesVm.nextPage()"
                       ng-class="{'disabled': issuesVm.issues.last}"><i class="fa fa-arrow-circle-right"></i></a>
                </div>
            </div>
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
                        <div class="form-group" style="width: 35%;margin-right: 0px !important;">
                            <label class="col-sm-5 control-label"
                                   style="margin-top: 10px;text-align: right;">From Date : <span
                                    class="asterisk">*</span> : </label>

                            <div class="col-sm-7">
                                <input type="text" ng-model="issuesVm.issueFilter.fromDate"
                                       placeholder="Select From Date" style="width: 100%;"
                                       class="form-control" date-time-picker>
                            </div>
                        </div>
                        <div class="form-group" style="width: 34%;margin-right: 0px !important;">
                            <label class="col-sm-5 control-label"
                                   style="margin-top: 10px;text-align: right;">To Date : <span
                                    class="asterisk">*</span> : </label>

                            <div class="col-sm-7">
                                <input type="text" ng-model="issuesVm.issueFilter.toDate"
                                       placeholder="Select To Date" style="width: 100%;"
                                       class="form-control" date-time-picker>
                            </div>
                        </div>
                        <div class="form-group" style="margin-right: 0px !important;">
                            <button class="btn btn-primary btn-sm"
                                    ng-click="issuesVm.getFilterResults()">Search
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
                                <input type="text" ng-model="issuesVm.issueFilter.month"
                                       placeholder="Select From Date" style="width: 100%;"
                                       class="form-control" month-picker>
                            </div>
                        </div>
                        <div class="form-group" style="width: 25%;margin-right: 0px !important;">

                        </div>
                        <div class="form-group" style="margin-right: 0px !important;">
                            <button class="btn btn-primary btn-sm"
                                    ng-click="issuesVm.getMonthResults()">Search
                            </button>
                        </div>
                    </form>
                </div>

                <p ng-show="issuesVm.errorMessage != null"
                   style="color: darkred;font-weight: 600;font-size: 14px;margin-left:15px">
                    {{issuesVm.errorMessage}}</p>
            </div>
            <div style="height: 10%;background-color: lightgrey;text-align: center;">
                <div class="btn-group" style="margin-top: 5px;">
                    <button ng-click="issuesVm.cancelFilter()"
                            class="btn btn-xs btn-danger">Close
                    </button>
                </div>
            </div>
        </div>
    </div>


    <div class="view-content" id="issuedItemsView" ng-show="issuesVm.issueFilter.report">
        <form class="form-inline" style="border: 1px solid lightgrey;padding: 10px;">
            <div class="form-group" style="margin-right: 0px;width: 25%">
                <label class="col-sm-4 control-label" style="margin-top: 10px;text-align: right;">BOM <span
                        class="asterisk">*</span> : </label>

                <div class="col-sm-7">
                    <ui-select ng-model="issuesVm.selectedBom" theme="bootstrap" style="width:100%"
                               on-select="issuesVm.onSelectBom($item)">
                        <ui-select-match
                                placeholder="{{issuesVm.selectBomTitle}}">
                            {{$select.selected.item.itemMaster.itemName}}
                        </ui-select-match>
                        <ui-select-choices
                                repeat="bom in issuesVm.boms track by bom.id">
                            <div ng-bind="bom.item.itemMaster.itemName"></div>
                        </ui-select-choices>
                    </ui-select>
                </div>
            </div>
            <div class="form-group" style="margin-right: 0px;width: 25%;" ng-if="issuesVm.selectedBom != null">
                <label class="col-sm-4 control-label" style="margin-top: 10px;text-align: right;">Instance <span
                        class="asterisk">*</span> : </label>

                <div class="col-sm-7">
                    <ui-select ng-model="issuesVm.bomInstance" theme="bootstrap"
                               style="width:100%"
                               on-select="issuesVm.onSelectInstance($item)">
                        <ui-select-match
                                placeholder="{{issuesVm.bomInstanceTitle}}">
                            {{$select.selected.item.instanceName}}
                        </ui-select-match>
                        <ui-select-choices
                                repeat="instance in issuesVm.bomInstances">
                            <div ng-bind="instance.item.instanceName"></div>
                        </ui-select-choices>
                    </ui-select>
                </div>
            </div>

            <div class="form-group" style="margin-right: 0px;width: 25%;"
                 ng-if="issuesVm.selectedBomInstance != null">

                <label class="col-sm-4 control-label">Section <span class="asterisk">*</span> : </label>

                <div class="col-sm-7">
                    <ui-select ng-model="issuesVm.selectedSection" theme="bootstrap"
                               style="width:100%"
                               on-select="issuesVm.selectSection($item)">
                        <ui-select-match
                                placeholder="Select Section">
                            {{$select.selected.typeRef.name}}
                        </ui-select-match>
                        <ui-select-choices
                                repeat="section in issuesVm.instanceSections | filter: $select.search">
                            <div>
                                <span>{{section.typeRef.name}}</span>
                                <span ng-if="section.typeRef.versity"> ( VSPL )</span>
                            </div>
                        </ui-select-choices>
                    </ui-select>
                </div>
            </div>
        </form>

        <div class="responsive-table" style="padding: 10px;" ng-show="issuesVm.selectedSection != null">
            <table class="table table-striped">
                <thead>
                <tr>
                    <th>Nomenclature</th>
                    <th>Bom Qty</th>
                    <th>InProcess Qty</th>
                    <th>Issued Qty</th>
                    <th>Serial Number</th>
                </tr>
                </thead>
                <tbody>
                <tr ng-if="issuesVm.loading == true">
                    <td colspan="25">
                        <span style="font-size: 15px;">
                            <img src="app/assets/bower_components/cassini-platform/images/loaders/loader19.gif"
                                 class="mr5">
                            <span>Loading Issue Report...</span>
                        </span>
                    </td>
                </tr>

                <tr ng-if="issuesVm.loading == false && issuesVm.issueReport.length == 0">
                    <td colspan="25">
                        <span>No Issues found</span>
                    </td>
                </tr>
                <tr ng-if="issuesVm.loading == false && issuesVm.issueReport.length > 0"
                    ng-repeat="issuedPart in issuesVm.issueReport"
                    ng-class="{'section': issuedPart.bomItemType == 'SECTION' || issuedPart.bomItemType == 'COMMONPART','subsystem':issuedPart.bomItemType == 'SUBSYSTEM'}">
                    <td ng-if="issuedPart.bomItemType != 'PART'" colspan="5" style="text-align: center;">
                        <span ng-if="issuedPart.bomItemType == 'SECTION' || issuedPart.bomItemType == 'COMMONPART'"
                              style="font-weight: bold;color: black;">{{issuedPart.typeRef.name}}
                            <span ng-if="issuedPart.typeRef.versity"> ( VSPL )</span>
                        </span>
                        <span ng-if="issuedPart.bomItemType == 'SUBSYSTEM' || issuedPart.bomItemType == 'UNIT'"
                              style="font-weight: bold;color: black;">{{issuedPart.typeRef.name}}</span>
                    </td>
                    <td ng-if="issuedPart.bomItemType == 'PART'">{{issuedPart.itemRevision.itemMaster.itemName}}</td>
                    <td ng-if="issuedPart.bomItemType == 'PART'">
                        <span class="badge badge-primary" style="font-size: 14px;">{{issuedPart.quantity}}</span>
                    </td>
                    <td ng-if="issuedPart.bomItemType == 'PART'">
                        <span class="badge badge-warning" ng-if="issuedPart.inProcessQty > 0"
                              style="font-size: 14px;">{{issuedPart.inProcessQty}}</span>
                        <span ng-if="issuedPart.inProcessQty == 0"
                              style="font-size: 14px;padding: 7px;">{{issuedPart.inProcessQty}}</span>
                    </td>
                    <td ng-if="issuedPart.bomItemType == 'PART'">
                        <span class="badge badge-success" ng-if="issuedPart.issuedQuantity > 0"
                              style="font-size: 14px;">{{issuedPart.issuedQuantity}}</span>
                        <span ng-if="issuedPart.issuedQuantity == 0" style="font-size: 14px;padding: 7px;">{{issuedPart.issuedQuantity}}</span>
                    </td>
                    <td ng-if="issuedPart.bomItemType == 'PART'">
                        <div ng-repeat="itemInstance in issuedPart.itemInstances">
                            {{itemInstance.oemNumber}}
                            <span ng-if="itemInstance.issuedDate != null">( {{itemInstance.issuedDate}} )</span>
                        </div>

                        <div ng-repeat="lotInstance in issuedPart.lotInstances">
                            {{lotInstance.itemInstance.oemNumber}} / {{lotInstance.sequence}} ( {{lotInstance.lotQty}} )
                            ( {{lotInstance.createdDate}} )
                        </div>
                    </td>
                </tr>
                </tbody>
            </table>
        </div>

    </div>
</div>
