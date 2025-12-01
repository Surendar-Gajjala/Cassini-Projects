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

    #inward-report-view .responsive-table {
        padding: 10px;
        position: absolute;
        bottom: 40px;
        top: 62px;
        overflow: auto;
    }

    #inward-report-view .responsive-table table thead th {
        position: -webkit-sticky;
        position: sticky;
        top: -10px;
        z-index: 5;
        background-color: #fff;
    }

    a.disabled {
        cursor: not-allowed;
        color: lightgrey;
    }

    table {
        table-layout: fixed;
    }

    .gatePassLength {
        display: run-in;
        word-wrap: break-word;
        width: 200px;
        white-space: normal !important;
        text-align: left;
    }

    .forty-percent {
        display: run-in;
        word-wrap: break-word;
        width: 40%;
        white-space: normal !important;
        text-align: left;
    }

    .twenty-percent {
        display: run-in;
        word-wrap: break-word;
        width: 20%;
        white-space: normal !important;
        text-align: left;
    }

    .attribute-length {
        display: run-in;
        word-wrap: break-word;
        width: 150px;
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

    .new-gatePass {
        font-weight: 600;
    }

    .form-group {
        margin-right: 0px !important;
    }

    .ui-select-bootstrap > .ui-select-match > .btn {
        width: 100%;
    }

    .ui-select-toggle {
        width: 100%;
    }

    #freeTextSearchDirective {
        top: 7px !important;
        right: 0 !important;
    }
</style>
<div class="view-container" fitcontent>
    <div class="view-toolbar">
        <div class="btn-group">
            <button class="btn btn-sm btn-success" ng-click="inwardsVm.showNewInward()"
                    ng-if="hasPermission('permission.inward.new')">New Inward
            </button>

            <span class="widget1"
                  ng-if="hasPermission('permission.admin.all') || hasPermission('permission.inward.view')">
                <a class="notification1" ng-class="{'activeView':inwardsVm.inwardFilter.finishedPage}"
                   title="{{inwardsVm.inwardFilter.finishedPage ? 'Click for Inwards' : 'Click for Finished Inwards'}}"
                   ng-click="inwardsVm.changeView('finish')" style="top: 5px;">
                    <span ng-if="!inwardsVm.inwardFilter.finishedPage" class="badge1">{{inwardsVm.inwardDto.finishedInwards}}</span>
                    <i ng-if="!inwardsVm.inwardFilter.finishedPage" class="fa fa-square-o"></i>
                    <i ng-if="inwardsVm.inwardFilter.finishedPage" class="fa fa-check-square-o"></i>
                    Finished
                </a>
            </span>
            <span class="widget1"
                  ng-if="hasPermission('permission.admin.all') || hasPermission('permission.gatePass.view')">
                <a class="notification1" ng-class="{'activeView':inwardsVm.inwardFilter.gatePassView}"
                   title="{{inwardsVm.inwardFilter.gatePassView ? 'Click for Inwards' : 'Click for Gate Passes'}}"
                   ng-click="inwardsVm.changeView('gatePass')" style="top: 5px;">
                    <span ng-if="!inwardsVm.inwardFilter.gatePassView" class="badge1">{{inwardsVm.inwardDto.gatePassLength}}</span>
                    <i ng-if="!inwardsVm.inwardFilter.gatePassView" class="fa fa-square-o"></i>
                    <i ng-if="inwardsVm.inwardFilter.gatePassView" class="fa fa-check-square-o"></i>
                    Gate Passes
                </a>
            </span>
            <span class="widget1"
                  ng-if="hasPermission('permission.admin.all') || hasPermission('permission.inward.view')">
                <a class="notification1" ng-class="{'activeView':inwardsVm.inwardReportView}"
                   title="{{inwardsVm.inwardFilter.inwardReportView ? 'Click for Inwards' : 'Click for Inward Report'}}"
                   ng-click="inwardsVm.changeView('inwardReport')" style="top: 5px;">
                    <i ng-if="!inwardsVm.inwardReportView" class="fa fa-square-o"></i>
                    <i ng-if="inwardsVm.inwardReportView" class="fa fa-check-square-o"></i>
                    Inward Report
                </a>
            </span>
        </div>
        <a class="pull-right" style="cursor: pointer;margin-right: 325px;margin-top: 5px;"
           ng-click="inwardsVm.addFilter()" ng-if="!inwardsVm.filterMode"
           ng-hide="inwardsVm.inwardReportView">
            <i style="font-size: 24px;" title="Date Filter" class="fa fa-filter"></i>
        </a>
        <a class="pull-right" style="cursor: pointer;margin-right: 325px;" ng-click="inwardsVm.clearDateFilter()"
           ng-if="inwardsVm.filterMode"
           ng-hide="inwardsVm.inwardReportView">
            <i title="Clear Filter" style="font-size: 30px;color:red;" class="fa fa-filter"></i>
        </a>
        <free-text-search on-clear="inwardsVm.resetPage" search-term="inwardsVm.searchText"
                          on-search="inwardsVm.freeTextSearch"></free-text-search>
    </div>

    <div class="view-content" ng-show="!inwardsVm.inwardReportView">
        <div class="responsive-table" style="padding: 10px;" ng-if="inwardsVm.inwardDto.inwardsPage">
            <table class="table table-striped highlight-row">
                <thead>
                <tr>
                    <th style="width: 150px;">Inward Number</th>
                    <th style="width: 100px;">BOM</th>
                    <th style="width: 100px;">Status</th>
                    <th class="gatePassLength">Gate Pass</th>
                    <%--<th class="gatePassLength" style="width: 200px;">Gate Pass Number</th>--%>
                    <th class="attribute-length">Supplier Name</th>
                    <th class="attribute-length">Created By</th>
                    <th class="gatePassLength">Created Date</th>
                    <th class="attribute-length"
                        ng-repeat="selectedAttribute in inwardsVm.requiredInwardAttributes">
                        {{selectedAttribute.name}}
                    </th>
                    <th class="gatePassLength">Notes</th>
                    <th style="width: 70px;">Actions</th>
                </tr>
                </thead>
                <tbody>
                <tr ng-if="inwardsVm.loading == true">
                    <td colspan="25">
                        <span style="font-size: 15px;">
                            <img src="app/assets/bower_components/cassini-platform/images/loaders/loader19.gif"
                                 class="mr5">
                            <span>Loading Inwards...</span>
                        </span>
                    </td>
                </tr>

                <tr ng-if="inwardsVm.loading == false && inwardsVm.inwards.content.length == 0">
                    <td ng-if="!inwardsVm.filterMode && inwardsVm.inwardFilter.searchQuery == null" colspan="25">No
                        Inwards
                    </td>
                    <td ng-if="inwardsVm.filterMode && inwardsVm.inwardFilter.fromDate != null && inwardsVm.inwardFilter.toDate != null"
                        colspan="25">
                        No Inwards found between {{inwardsVm.inwardFilter.fromDate}} - {{inwardsVm.inwardFilter.toDate}}
                    </td>
                    <td ng-if="inwardsVm.filterMode && inwardsVm.inwardFilter.month != null"
                        colspan="25">No Inwards found in {{inwardsVm.inwardFilter.month}}
                    </td>
                    <td ng-if="inwardsVm.inwardFilter.searchQuery != null" colspan="25">No Search Results</td>
                </tr>

                <tr ng-repeat="inward in inwardsVm.inwardDto.inwards.content"
                    ng-class="{'new-gatePass':!inward.itemsExist}">
                    <td>
                        <a href="" ng-click="inwardsVm.showInward(inward)" title="Click to show details">
                            <span ng-bind-html="inward.number | highlightText: freeTextQuery"></span>
                            <span ng-if="inward.showNew && !inward.itemsExist"
                                  style="background: orange;color: white;font-size: 10px;font-weight: 600;padding: 3px;border-radius: 50%;">NEW</span>
                            <span ng-if="inward.showNewBadge"
                                  style="background: orange;color: white;font-size: 10px;font-weight: 600;padding: 3px;border-radius: 50%;">NEW</span>
                        </a>
                    </td>
                    <td><span ng-bind-html="inward.bom.item.itemMaster.itemCode
                     | highlightText: freeTextQuery"></span>
                    </td>
                    <td>
                        <span class="badge badge-success" ng-if="inward.status == 'SSQAG'" style="font-size: 13px;">
                            <i ng-if="inward.underReview" class="fa fa-eye" style="padding: 0 3px;color: black;"
                               title="Inward has under review Items"></i>
                            <i ng-if="inward.provisionalAcceptItems" class="fa fa-shield"
                               style="padding: 0 3px;color: orangered;"
                               title="Inward has Provisionally Accepted Items"></i>
                            {{inward.status}}</span>
                        <inward-status ng-if="inward.status != 'SSQAG'" inward="inward"></inward-status>
                    </td>
                    <td class="gatePassLength">
                        <a href="" ng-click="inwardsVm.downloadGatePass(inward.gatePass)"
                           title="Click to download Gate Pass">
                            <span ng-bind-html="inward.gatePass.gatePass.name | highlightText: freeTextQuery"></span>
                        </a>
                    </td>
                    <%--<td class="gatePassLength">
                        <span ng-bind-html="inward.gatePass.gatePassNumber | highlightText: freeTextQuery"></span></td>--%>
                    <td class="attribute-length">{{inward.supplier.supplierName}}</td>
                    <td class="attribute-length">{{inward.createdByObject.fullName}}</td>
                    <td class="gatePassLength">{{inward.createdDate}}</td>
                    <td class="attribute-length"
                        ng-repeat="objectAttribute in inwardsVm.requiredInwardAttributes">
                        <div class="attributeTooltip"
                             ng-if="objectAttribute.dataType != 'CURRENCY' && objectAttribute.dataType == 'ATTACHMENT'"
                             ng-init="attrName = objectAttribute.name">
                            <p>
                                <a ng-if="inward[attrName].length > 1" href="">
                                    {{inward[attrName].length}} Attachments
                                </a>
                            </p>

                            <div class="attributeTooltiptext" ng-show="inward[attrName].length > 1">
                                <ul>
                                    <li ng-repeat="attachment in inward[attrName]">
                                        <a href="" ng-click="inwardsVm.openAttachment(attachment)"
                                           title="{{'CLICK_TO_DOWNLOAD_ATTACHMENT' | translate}}"
                                           style="margin-bottom: 5px;width:200px;display: run-in;white-space: normal;word-wrap: break-word;color: #141f9f !important;">
                                            {{attachment.name}}
                                        </a>
                                    </li>
                                </ul>
                            </div>
                        </div>

                        <div ng-if="objectAttribute.dataType != 'CURRENCY' && objectAttribute.dataType == 'ATTACHMENT'"
                             ng-init="attrName = objectAttribute.name">
                            <p ng-if="inward[attrName].length == 1">
                                <a href="" ng-click="inwardsVm.openAttachment(inward[attrName][0])"
                                   title="{{'CLICK_TO_DOWNLOAD_ATTACHMENT' | translate}}"
                                   style="margin-bottom: 5px;width:200px;display: run-in;white-space: normal;word-wrap: break-word;color: #141f9f !important;">
                                    {{inward[attrName][0].name}}
                                </a>
                            </p>
                        </div>

                        <div ng-if="objectAttribute.dataType != 'CURRENCY' && objectAttribute.dataType == 'IMAGE'"
                             ng-init="attrName = objectAttribute.name">
                            <a href="" ng-click="inwardsVm.showImage(inward[attrName])"
                               title="{{'CLICK_TO_SHOW_IMAGE' | translate}}">
                                <img ng-if="inward[attrName] != null"
                                     ng-src="{{inward[attrName]}}"
                                     style="height: 30px;width: 40px;margin-bottom: 5px;">
                            </a>

                            <div id="myModal2" class="img-model modal">
                                <span class="closeImage1">&times;</span>
                                <img class="modal-content" id="img03">
                            </div>
                        </div>

                        <span ng-if="objectAttribute.dataType != 'ATTACHMENT'
                        && objectAttribute.dataType != 'IMAGE' && objectAttribute.dataType != 'CURRENCY'"
                              ng-init="attrName = objectAttribute.name">
                            {{inward[attrName]}}
                        </span>

                        <span ng-init="currencyType = objectAttribute.name+'type'"
                              ng-if="objectAttribute.dataType == 'CURRENCY'" ng-bind-html="inward[currencyType]">
                        </span>
                        <span ng-init="attrName = objectAttribute.name"
                              ng-if="objectAttribute.dataType == 'CURRENCY'">
                            {{inward[attrName]}}
                        </span>
                    </td>
                    <td class="gatePassLength">{{inward.notes}}</td>
                    <td style="width: 70px">
                        <div class="btn-group" ng-if="inward.status == 'STORE'">
                            <button title="Delete Inward"
                                    class="btn btn-xs btn-danger"
                                    ng-click="inwardsVm.deleteInward(inward)">
                                <i class="fa fa-trash"></i>
                            </button>
                        </div>
                    </td>
                </tr>
                </tbody>
            </table>
        </div>

        <div class="responsive-table" style="padding: 10px;" ng-if="inwardsVm.inwardDto.gatePassView">
            <table class="table table-striped highlight-row">
                <thead>
                <tr>
                    <th class="gatePassLength">Gate Pass Name</th>
                    <th class="gatePassLength">Gate Pass Number</th>
                    <th class="gatePassLength">Gate Pass Date</th>
                    <th class="gatePassLength">Created By</th>
                    <th class="gatePassLength">Created Date</th>
                    <th style="width: 75px;">Actions</th>
                </tr>
                </thead>
                <tbody>
                <tr ng-if="inwardsVm.loading == true">
                    <td colspan="25">
                        <span style="font-size: 15px;">
                            <img src="app/assets/bower_components/cassini-platform/images/loaders/loader19.gif"
                                 class="mr5">
                            <span>Loading GatePass...</span>
                        </span>
                    </td>
                </tr>

                <tr ng-if="inwardsVm.loading == false && inwardsVm.inwardDto.gatePasses.content.length == 0">
                    <td colspan="25">No GatePass</td>
                </tr>

                <tr ng-repeat="gatePass in inwardsVm.inwardDto.gatePasses.content"
                    ng-class="{'new-gatePass':gatePass.inwards == 0}">
                    <td class="gatePassLength">
                        <a href="" ng-click="inwardsVm.downloadGatePass(gatePass)"
                           title="Click to download Gate Pass">{{gatePass.gatePass.name}}
                            <span ng-if="gatePass.showNew && gatePass.inwards == 0"
                                  style="background: orange;color: white;font-size: 10px;font-weight: 600;padding: 3px;border-radius: 50%;">NEW</span>
                        </a>
                    </td>
                    <td class="gatePassLength">{{gatePass.gatePassNumber}}</td>
                    <td class="gatePassLength">{{gatePass.gatePassDate}}</td>
                    <td class="gatePassLength">{{gatePass.createdByObject.fullName}}</td>
                    <td class="gatePassLength">{{gatePass.createdDate}}</td>
                    <td style="width: 75px;">
                        <button class="btn btn-xs btn-primary" ng-click="inwardsVm.showGatePassItems(gatePass)">
                            <i class="fa fa-list" title="Click to show Details"></i>
                        </button>
                    </td>
                </tr>
                </tbody>
            </table>
        </div>


        <div class="table-footer">
            <div>
                <div>
                    <%--<h5 ng-show="inwardsVm.inwardDto.inwardsPage && !inwardsVm.inwardReportView">
                        Displaying {{inwardsVm.inwards.numberOfElements}} of
                        {{inwardsVm.inwards.totalElements}}
                    </h5>--%>
                    <%--<h5 ng-show="!inwardsVm.inwardDto.inwardsPage && !inwardsVm.inwardReportView">
                        Displaying {{inwardsVm.gatePasses.numberOfElements}} of
                        {{inwardsVm.gatePasses.totalElements}}
                    </h5>--%>
                    <h5 ng-show="inwardsVm.inwardDto.inwardsPage && !inwardsVm.inwardReportView">
                        <span style="padding-right: 5px">Displaying</span>
                        <span ng-if="inwardsVm.inwards.totalElements == 0">
                            {{(inwardsVm.pageable.page*inwardsVm.pageable.size)}}
                        </span>
                        <span ng-if="inwardsVm.inwards.totalElements > 0">
                            {{(inwardsVm.pageable.page*inwardsVm.pageable.size)+1}}
                        </span>
                        -
                        <span ng-if="inwardsVm.inwards.last == false">{{((inwardsVm.pageable.page+1)*inwardsVm.pageable.size)}}</span>
                        <span ng-if="inwardsVm.inwards.last == true">{{inwardsVm.inwards.totalElements}}</span>


                        <span> of </span>{{inwardsVm.inwards.totalElements}}
                    </h5>
                    <h5 ng-show="!inwardsVm.inwardDto.inwardsPage && !inwardsVm.inwardReportView">
                        <span style="padding-right: 5px">Displaying</span>
                        <span ng-if="inwardsVm.gatePasses.totalElements == 0">
                            {{(inwardsVm.pageable.page*inwardsVm.pageable.size)}}
                        </span>
                        <span ng-if="inwardsVm.gatePasses.totalElements > 0">
                            {{(inwardsVm.pageable.page*inwardsVm.pageable.size)+1}}
                        </span>
                        -
                        <span ng-if="inwardsVm.gatePasses.last == false">{{((inwardsVm.pageable.page+1)*inwardsVm.pageable.size)}}</span>
                        <span ng-if="inwardsVm.gatePasses.last == true">{{inwardsVm.gatePasses.totalElements}}</span>


                        <span> of </span>{{inwardsVm.gatePasses.totalElements}}
                    </h5>
                </div>

                <div class="text-right">
                    <span ng-show="inwardsVm.inwardDto.inwardsPage && !inwardsVm.inwardReportView">
                        <span class="mr10">Page {{inwardsVm.inwards.totalElements != 0 ? inwardsVm.inwards.number+1:0}} of {{inwardsVm.inwards.totalPages}}</span>
                            <a href="" ng-click="inwardsVm.previousPage()"
                               ng-class="{'disabled': inwardsVm.inwards.first}">
                                <i class="fa fa-arrow-circle-left mr10"></i>
                            </a>
                            <a href="" ng-click="inwardsVm.nextPage()" ng-class="{'disabled': inwardsVm.inwards.last}">
                                <i class="fa fa-arrow-circle-right"></i>
                            </a>
                    </span>
                    <span ng-show="!inwardsVm.inwardDto.inwardsPage && !inwardsVm.inwardReportView">
                        <span class="mr10">Page {{inwardsVm.gatePasses.totalElements != 0 ? inwardsVm.gatePasses.number+1:0}} of {{inwardsVm.gatePasses.totalPages}}</span>
                            <a href="" ng-click="inwardsVm.previousPage()"
                               ng-class="{'disabled': inwardsVm.gatePasses.first}">
                                <i class="fa fa-arrow-circle-left mr10"></i>
                            </a>
                            <a href="" ng-click="inwardsVm.nextPage()"
                               ng-class="{'disabled': inwardsVm.gatePasses.last}">
                                <i class="fa fa-arrow-circle-right"></i>
                            </a>
                    </span>
                </div>
            </div>
        </div>
    </div>

    <div class="view-content" id="inward-report-view" ng-show="inwardsVm.inwardReportView">
        <form class="form-inline" style="border: 1px solid lightgrey;padding: 10px;" ng-if="inwardsVm.inwardReportView">
            <div class="form-group" style="margin-right: 0px;width: 25%">
                <label class="col-sm-4 control-label" style="margin-top: 10px;text-align: right;">BOM <span
                        class="asterisk">*</span> : </label>

                <div class="col-sm-7">
                    <ui-select ng-model="inwardsVm.selectedSystem" theme="bootstrap" style="width:100%"
                               on-select="inwardsVm.onSelectBom($item)">
                        <ui-select-match
                                placeholder="Select BOM">
                            {{$select.selected.item.itemMaster.itemName}}
                        </ui-select-match>
                        <ui-select-choices
                                repeat="bom in inwardsVm.boms track by bom.id">
                            <div ng-bind="bom.item.itemMaster.itemName"></div>
                        </ui-select-choices>
                    </ui-select>
                </div>
            </div>
        </form>
        <div class="responsive-table" style="padding: 10px;">
            <table class="table table-striped highlight-row">
                <thead>
                <tr>
                    <th class="forty-percent">Nomenclature</th>
                    <th class="twenty-percent">Total Inward</th>
                    <th class="forty-percent">Gate Pass</th>
                </tr>
                </thead>
                <tbody>
                <tr ng-if="inwardsVm.loading == true">
                    <td colspan="25">
                        <span style="font-size: 15px;">
                            <img src="app/assets/bower_components/cassini-platform/images/loaders/loader19.gif"
                                 class="mr5">
                            <span>Loading Inward Report...</span>
                        </span>
                    </td>
                </tr>

                <tr ng-if="inwardsVm.loading == false && inwardsVm.inwardReport.content.length == 0">
                    <td colspan="25">No Inward Report</td>
                </tr>

                <tr ng-if="inwardsVm.loading == false" ng-repeat="report in inwardsVm.inwardReport.content">
                    <td class="forty-percent">{{report.itemRevision.itemMaster.itemName}}</td>
                    <td class="twenty-percent">
                        <span ng-if="!report.itemRevision.itemMaster.itemType.hasLots">{{report.totalInward}}</span>
                        <span ng-if="report.itemRevision.itemMaster.itemType.hasLots">{{report.totalFractionalInward}}</span>
                    </td>
                    <td class="forty-percent">
                        <div ng-repeat="gatePass in report.gatePasses" style="padding: 3px 0">
                            {{gatePass.gatePass.gatePass.name}}
                            <span style="color:black;">( {{gatePass.quantity}} )</span>
                        </div>
                    </td>
                </tr>
                </tbody>
            </table>
        </div>
        <div class="table-footer">
            <div>
                <div>
                    <h5>
                        Displaying {{inwardsVm.inwardReport.numberOfElements}} of
                        {{inwardsVm.inwardReport.totalElements}}
                    </h5>
                </div>

                <div class="text-right">
                    <span>
                        <span class="mr10">Page {{inwardsVm.inwardReport.totalElements != 0 ? inwardsVm.inwardReport.number+1:0}} of {{inwardsVm.inwardReport.totalPages}}</span>
                            <a href="" ng-click="inwardsVm.previousPage()"
                               ng-class="{'disabled': inwardsVm.inwardReport.first}">
                                <i class="fa fa-arrow-circle-left mr10"></i>
                            </a>
                            <a href="" ng-click="inwardsVm.nextPage()"
                               ng-class="{'disabled': inwardsVm.inwardReport.last}">
                                <i class="fa fa-arrow-circle-right"></i>
                            </a>
                    </span>
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
                                <input type="text" ng-model="inwardsVm.inwardFilter.fromDate"
                                       placeholder="Select From Date" style="width: 100%;"
                                       class="form-control" date-time-picker>
                            </div>
                        </div>
                        <div class="form-group" style="width: 34%;margin-right: 0px !important;">
                            <label class="col-sm-5 control-label"
                                   style="margin-top: 10px;text-align: right;">To Date : <span
                                    class="asterisk">*</span> : </label>

                            <div class="col-sm-7">
                                <input type="text" ng-model="inwardsVm.inwardFilter.toDate"
                                       placeholder="Select To Date" style="width: 100%;"
                                       class="form-control" date-time-picker>
                            </div>
                        </div>
                        <div class="form-group" style="margin-right: 0px !important;">
                            <button class="btn btn-primary btn-sm"
                                    ng-click="inwardsVm.getFilterResults()">Search
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
                                <input type="text" ng-model="inwardsVm.inwardFilter.month"
                                       placeholder="Select From Date" style="width: 100%;"
                                       class="form-control" month-picker>
                            </div>
                        </div>
                        <div class="form-group" style="width: 25%;margin-right: 0px !important;">

                        </div>
                        <div class="form-group" style="margin-right: 0px !important;">
                            <button class="btn btn-primary btn-sm"
                                    ng-click="inwardsVm.getMonthResults()">Search
                            </button>
                        </div>
                    </form>
                </div>


                <p ng-show="inwardsVm.errorMessage != null"
                   style="color: darkred;font-weight: 600;font-size: 14px;margin-left:15px">
                    {{inwardsVm.errorMessage}}</p>
            </div>
            <div style="height: 10%;background-color: lightgrey;text-align: center;">
                <div class="btn-group" style="margin-top: 5px;">
                    <button ng-click="inwardsVm.cancelFilter()"
                            class="btn btn-xs btn-danger">Close
                    </button>
                </div>
            </div>
        </div>
    </div>
</div>
