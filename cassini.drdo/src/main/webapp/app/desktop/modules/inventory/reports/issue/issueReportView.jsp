<div class="view-container" fitcontent>
    <style scoped>
        .split-pane-divider {
            background: #EEE;
            left: 400px; /* Same as left component width */
            width: 5px;
        }

        .split-left-pane {
            min-width: 400px;
            max-width: 400px;
            padding: 0px;
        }

        .split-right-pane {
            left: 400px; /* Same as left component width */
            margin-left: 5px; /* Same as divider width */
            padding: 10px;
        }

        .item-container {
            overflow-y: auto;
        }

        .item-container .container-item {
            cursor: pointer;
            padding: 10px;
            border-bottom: 1px solid #ddd;
        }

        .item-container .container-item.active-item {
            background-image: -webkit-linear-gradient(top, #337ab7 0, #2b669a 100%);
            background-image: -o-linear-gradient(top, #337ab7 0, #2b669a 100%);
            background-image: -webkit-gradient(linear, left top, left bottom, from(#337ab7), to(#2b669a));
            background-image: linear-gradient(to bottom, #337ab7 0, #2b669a 100%);
            filter: progid:DXImageTransform.Microsoft.gradient(startColorstr='#ff337ab7', endColorstr='#ff2b669a', GradientType=0);
            background-repeat: repeat-x;
            border-color: #2b669a;
            color: #fff !important;
        }

        .item-container .container-item:hover {

        }

        .item-container .container-item .request-number {
            font-size: 20px;
        }

        .item-container .container-item .prop-name {
            color: #8A8A8A;
            font-size: 14px;
        }

        .item-container .container-item.active-item .prop-name {
            color: #d8d8d8;
        }

        .item-container .container-item .prop-value {
            font-size: 16px;
        }

        .item-container .container-item .margin-bottom {
            margin-bottom: 10px;
        }

        .row {
            margin-left: 0;
            margin-right: 0;
        }

        .section-title {
            padding-bottom: 10px;
            border-bottom: 1px solid #EEE;
            color: #64a0d5;
            margin-left: -10px;
            cursor: pointer;
        }

        .section-btn {
            margin-right: 10px;
        }
    </style>

    <div class="view-toolbar">
        <button class="btn btn-success btn-sm backButton" ng-click="issueReportVm.back()">Back</button>
        <button class="btn btn-sm" ng-click="issueReportVm.generateReport()" title="Click to PDF Print">
            <i class="fa fa-print" style="font-size: 16px;color: black;"></i>
        </button>
    </div>

    <div class="view-content no-padding">
        <div ng-show="issueReportVm.loading == true" style="padding: 20px;">
            <h4>Loading...</h4>
        </div>

        <div ng-show="issueReportVm.loading == false && issueReportVm.issues.length == 0"
             style="padding: 20px;">
            <h4>No Issues found for this BOM Instance</h4>
        </div>

        <div class="split-pane fixed-left"
             ng-show="issueReportVm.loading == false && issueReportVm.issues.length > 0">
            <div class="split-pane-component split-left-pane" style="min-width: 350px;max-width: 350px;">
                <div class="item-container">

                    <div ng-repeat="issue in issueReportVm.issues" class="container-item"
                         ng-class="{'active-item': issue.number == issueReportVm.issue.number}"
                         ng-click="issueReportVm.getIssueReport(issue)">
                        <div class="row margin-bottom">
                            <div class="col-sm-4  text-left">
                                <div style="font-weight: 600;">Issue Number</div>
                                <div>{{issue.number}}</div>
                            </div>
                            <div class="col-sm-4  text-center">
                                <div style="font-weight: 600;">Missile</div>
                                <div>
                                    {{issue.bomInstance.item.instanceName}}
                                </div>
                            </div>
                            <div class="col-sm-4 text-right">
                                <div style="font-weight: 600;">Status</div>
                                <div>
                                    <issue-status issue="issue"></issue-status>
                                </div>
                            </div>
                        </div>
                        <div class="row">
                            <div class="col-sm-6 text-left">
                                <div style="font-weight: 600;">Request Number</div>
                                <div>
                                    {{issue.request.reqNumber}}
                                </div>
                            </div>
                            <div class="col-sm-6 text-right">
                                <div style="font-weight: 600;">Last Updated Date</div>
                                <div>
                                    {{issue.modifiedDate}}
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
            <div class="split-pane-divider" style="left: 350px !important;"></div>
            <div id="requestReportView" class="split-pane-component split-right-pane noselect"
                 style="left: 350px !important;">
                <div class="item-details">
                    <div>
                        <h4 style="color: black;font-size: 18px;font-weight: bolder;" class="section-title"
                            ng-click="issueReportVm.toggleSection('basic')">
                            <span class="section-btn">
                                <i class="fa" ng-class="{'fa-plus-square': !issueReportVm.sections.basic,
                                                            'fa-minus-square': issueReportVm.sections.basic}"></i>
                            </span>Basic Information
                        </h4>
                    </div>

                    <div class="item-details" ng-show="issueReportVm.sections.basic">
                        <div class="row">
                            <div class="label col-xs-4 col-sm-3 text-right">
                                <span>Issue Number : </span>
                            </div>
                            <div class="value col-xs-8 col-sm-9" style="font-size: 15px;">
                                <span>{{issueReportVm.issue.number}}</span>
                            </div>
                        </div>
                        <div class="row">
                            <div class="label col-xs-4 col-sm-3 text-right">
                                <span>Missile : </span>
                            </div>
                            <div class="value col-xs-8 col-sm-9" style="font-size: 15px;">
                                <span>{{issueReportVm.issue.bomInstance.item.instanceName}}</span>
                            </div>
                        </div>
                        <div class="row">
                            <div class="label col-xs-4 col-sm-3 text-right">
                                <span>Status : </span>
                            </div>
                            <div class="value col-xs-8 col-sm-9" style="font-size: 15px;">
                                <issue-status issue="issueReportVm.issue"></issue-status>
                            </div>
                        </div>

                        <div class="row" ng-if="issueReportVm.issue.status == 'ISSUED'">
                            <div class="label col-xs-4 col-sm-3 text-right">
                                <span>Issued To : </span>
                            </div>
                            <div class="value col-xs-8 col-sm-9" style="font-size: 15px;">
                                <span>{{issueReportVm.issue.request.requestedBy.fullName}}</span>
                            </div>
                        </div>

                        <div class="row">
                            <div class="label col-xs-4 col-sm-3 text-right">
                                <span>Last Updated Date :</span>
                            </div>
                            <div class="value col-xs-8 col-sm-9" style="font-size: 15px;">
                                <span>{{issueReportVm.issue.createdDate}}</span>
                            </div>
                        </div>

                        <div class="row">
                            <div class="label col-xs-4 col-sm-3 text-right">
                                <span>Last Updated By : </span>
                            </div>
                            <div class="value col-xs-8 col-sm-9" style="font-size: 15px;">
                                <span>{{issueReportVm.issue.modifiedByObject.fullName}}
                                </span>
                            </div>
                        </div>

                        <div class="row">
                            <div class="label col-xs-4 col-sm-3 text-right">
                                <span>Request Number :</span>
                            </div>
                            <div class="value col-xs-8 col-sm-9" style="font-size: 15px;">
                                <span>{{issueReportVm.issue.request.reqNumber}}</span>
                            </div>
                        </div>

                        <div class="row">
                            <div class="label col-xs-4 col-sm-3 text-right">
                                <span>Requested Date :</span>
                            </div>
                            <div class="value col-xs-8 col-sm-9" style="font-size: 15px;">
                                <span>{{issueReportVm.issue.request.requestedDate}}</span>
                            </div>
                        </div>

                        <div class="row">
                            <div class="label col-xs-4 col-sm-3 text-right">
                                <span>Notes: </span>
                            </div>
                            <div class="value col-xs-8 col-sm-9" style="font-size: 15px;">
                                <span>{{issueReportVm.issue.notes}}</span>
                            </div>
                        </div>
                    </div>

                    <br>

                    <div>
                        <h4 class="section-title" style="color: black;font-size: 18px;font-weight: bolder;"
                            ng-click="issueReportVm.toggleSection('items')">
                            <span class="section-btn">
                            <i class="fa" ng-class="{'fa-plus-square': !issueReportVm.sections.items,
                                                        'fa-minus-square': issueReportVm.sections.items}"></i>
                            </span>Issue Items <span style="color: green;">
                            ( {{issueReportVm.issuedItemsLength}} )</span>
                        </h4>
                    </div>
                    <div ng-show="issueReportVm.sections.items">
                        <table class="table table-striped highlight-row" id="issueDetailsReport">
                            <thead>
                            <tr>
                                <th>Nomenclature</th>
                                <th>Units</th>
                                <th>BOM Qty</th>
                                <th>Issue Qty</th>
                                <th>Path</th>
                                <th>UPN</th>
                                <th>Serial Number</th>
                                <th>Certificate Number</th>
                                <th>Remarks</th>
                            </tr>
                            </thead>

                            <tbody>
                            <tr ng-if="issueReportVm.issuedItems.length == 0">
                                <td colspan="25">No Issue Items</td>
                            </tr>
                            <tr ng-repeat="issuedItem in issueReportVm.issuedItems">
                                <td>
                                    <p class="level{{issuedItem.level}}" ng-if="issuedItem.type == 'SECTION'"
                                       style="margin: 0px;">
                                        <i ng-if="issuedItem.children.length > 0" class="mr5 fa fa-caret-down"
                                           style="cursor: pointer; color: #909090;font-size: 18px;"></i>
                                        <span style="font-weight: 600;color: black;">{{issuedItem.name}}</span>
                                    </p>

                                    <p class="level{{issuedItem.level}}" ng-if="issuedItem.type == 'ISSUEITEM'"
                                       style="margin: 0px;">
                                        {{issuedItem.itemInstance.item.itemMaster.itemName}}</p>
                                </td>
                                <td>
                                    <span ng-if="issuedItem.type == 'ISSUEITEM'">{{issuedItem.itemInstance.item.itemMaster.itemType.units}}</span>
                                </td>
                                <td style="text-align: center">
                            <span ng-if="issuedItem.type == 'ISSUEITEM'">
                                <span class="badge badge-primary" style="font-size: 14px;"
                                      ng-if="!issuedItem.itemInstance.item.itemMaster.itemType.hasLots">{{issuedItem.requestItem.item.quantity}}</span>
                                <span class="badge badge-primary" style="font-size: 14px;"
                                      ng-if="issuedItem.itemInstance.item.itemMaster.itemType.hasLots">{{issuedItem.requestItem.item.fractionalQuantity}}</span>
                            </span>
                                </td>
                                <td style="text-align: center">
                            <span ng-if="issuedItem.type == 'ISSUEITEM'">
                                <span class="badge badge-success" style="font-size: 14px;"
                                      ng-if="!issuedItem.itemInstance.item.itemMaster.itemType.hasLots">{{issuedItem.issueItem.quantity}}</span>
                                <span class="badge badge-success" style="font-size: 14px;"
                                      ng-if="issuedItem.itemInstance.item.itemMaster.itemType.hasLots">{{issuedItem.lotInstance.lotQty}}</span>
                            </span>
                                </td>
                                <td>
                                    <span ng-if="issuedItem.type == 'ISSUEITEM'">{{issuedItem.requestItem.item.namePath}}</span>
                                </td>
                                <td>
                                    <a href=""
                                       ng-if="issuedItem.type == 'ISSUEITEM' && !issuedItem.itemInstance.item.itemMaster.itemType.hasLots"
                                       ng-click="showUpnHistory(issuedItem.itemInstance,'right')"
                                       title="Click to show details">
                                        <span class="badge badge-success" style="font-size: 14px;">{{issuedItem.itemInstance.upnNumber}}</span>
                                    </a>

                                    <a href=""
                                       ng-if="issuedItem.type == 'ISSUEITEM' && issuedItem.itemInstance.item.itemMaster.itemType.hasLots"
                                       ng-click="showLotUpnHistory(issuedItem.lotInstance,'ISSUE')"
                                       title="Click to show details">
                                        <span class="badge badge-success" style="font-size: 14px;">{{issuedItem.lotInstance.upnNumber}}</span>
                                    </a>
                                </td>
                                <td>
                                    <span ng-if="issuedItem.type == 'ISSUEITEM'"><%--{{issuedItem.itemInstance.manufacturer.mfrCode}} - --%>{{issuedItem.itemInstance.oemNumber}}</span>
                                </td>
                                <td>
                                    <span ng-if="issuedItem.type == 'ISSUEITEM'">{{issuedItem.itemInstance.certificateNumber}}</span>
                                </td>
                                <td>

                                </td>
                            </tr>
                            <%--<tr class="bom"
                                ng-repeat="issuedItem in issueReportVm.issueItems | orderBy:'item.item.itemMaster.itemName'">
                                <td>{{issuedItem.itemInstance.item.itemMaster.itemName}}</td>
                                <td>{{issuedItem.itemInstance.item.itemMaster.itemType.units}}</td>
                                <td>
                                    <span class="badge badge-success" style="font-size: 14px;"
                                          ng-if="!issuedItem.itemInstance.item.itemMaster.itemType.hasLots">{{issuedItem.issueItem.quantity}}</span>
                                    <span class="badge badge-success" style="font-size: 14px;"
                                          ng-if="issuedItem.itemInstance.item.itemMaster.itemType.hasLots">{{issuedItem.lotInstance.lotQty}}</span>
                                </td>
                                <td>{{issuedItem.requestItem.item.namePath}}</td>
                                <td>
                                    <a href="" ng-if="!issuedItem.itemInstance.item.itemMaster.itemType.hasLots"
                                       ng-click="showUpnHistory(issuedItem.itemInstance,'right')"
                                       title="Click to show details">
                                        <span class="badge badge-success" style="font-size: 14px;">{{issuedItem.itemInstance.upnNumber}}</span>
                                    </a>

                                    <a href="" ng-if="issuedItem.itemInstance.item.itemMaster.itemType.hasLots"
                                       ng-click="showLotUpnHistory(issuedItem.lotInstance,'ISSUE')"
                                       title="Click to show details">
                                        <span class="badge badge-success" style="font-size: 14px;">{{issuedItem.lotInstance.upnNumber}}</span>
                                    </a>
                                </td>
                                <td>{{issuedItem.itemInstance.manufacturer.mfrCode}} -
                                    {{issuedItem.itemInstance.oemNumber}}
                                </td>
                            </tr>--%>
                            </tbody>
                        </table>
                    </div>

                    <div style="display: none">
                        <table class="table table-striped highlight-row" style="margin-bottom: 20px;" id="issueReport">
                            <thead>
                            <tr>
                                <th class="threeHundred-column">System</th>
                                <th class="oneFifty-column">Missile</th>
                                <th class="hundred-column" style="text-align: center;">Request Number</th>
                                <th class="hundred-column">Last Updated By</th>
                                <th class="hundred-column">Issued To</th>
                                <th class="oneFifty-column">Last Updated Date</th>
                            </tr>
                            </thead>
                            <tbody>
                            <tr>
                                <td>{{issueReportVm.system}}</td>
                                <td>{{issueReportVm.issue.request.bomInstance.item.instanceName}}</td>
                                <td>{{issueReportVm.issue.request.reqNumber}}</td>
                                <td>{{issueReportVm.issue.modifiedByObject.fullName}}</td>
                                <td>{{issueReportVm.issue.issuedTo.fullName}}</td>
                                <td>{{issueReportVm.issue.modifiedDate}}</td>
                            </tr>
                            </tbody>
                        </table>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
