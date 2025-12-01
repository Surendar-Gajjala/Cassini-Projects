<style scoped>
    .view-content {
        position: relative;
    }

    .view-content .responsive-table {
        padding: 10px;
        position: absolute;
        bottom: 0;
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

    a.disabled {
        cursor: not-allowed;
        color: lightgrey;
    }

    table {
        table-layout: fixed;
    }
</style>
<div id="newIssueView" class="view-container" fitcontent>

    <style scoped>
        .split-pane-divider {
            background: #EEE;
            left: 400px; /* Same as left component width */
            width: 5px;
        }

        .split-left-pane {
            min-width: 400px;
            max-width: 400px;
            padding: 10px;
        }

        .split-right-pane {
            left: 400px !important; /* Same as left component width */
            margin-left: 5px; /* Same as divider width */
            padding: 10px;
        }

        .upn-scan {
            width: 200px;
            max-width: 200px;
        }

        .upn-scan input {
            width: 90%;
        }

        .issue-info-panel {
            display: none;
            z-index: 101;
            width: 500px;
            position: absolute;
            top: 0px;
            left: 0px;
            background-color: #fff;
            bottom: 0px;
            border: 1px solid #ddd;
            overflow-y: auto;
        }

        .issue-info-panel .info-panel-header {
            border-bottom: 1px solid #ddd;
            padding-left: 10px;
        }

        .issue-info-panel .info-panel-header h3 {
            line-height: 50px;
            margin: 0;
        }

        .info-panel-header .close {
            position: absolute;
            right: 10px;
            top: 10px;
            width: 32px;
            line-height: 50px;
            height: 50px;
            padding-top: 7px;
            margin-right: -5px;
            opacity: 0.3;

        }

        .info-panel-header .close:hover {
            opacity: 1;
        }

        .info-panel-header .close:before, .info-panel-header .close:after {
            position: absolute;
            left: 15px;
            content: ' ';
            height: 15px;
            width: 2px;
            background-color: #333;
        }

        .info-panel-header .close:before {
            transform: rotate(45deg);
        }

        .info-panel-header .close:after {
            transform: rotate(-45deg);
        }

        .info-panel-details {
            padding: 10px;
        }

        .ui-select-bootstrap .ui-select-choices-row > span {
            white-space: normal !important;
        }
    </style>

    <div class="view-toolbar">
        <div class="btn-group">
            <button class="btn btn-sm btn-default"
                    ng-click="newIssueVm.back()">Back
            </button>
            <button class="btn btn-sm" ng-click="newIssueVm.showIssueInfoPanel()">Issue
                Info
            </button>
            <button class="btn btn-sm btn-success" ng-click="newIssueVm.issueItems()">Create
            </button>
        </div>
    </div>
    <div class="view-content no-padding" style="overflow-y: auto;">

        <div class="issue-info-panel" id="issueInfoPanel" style="padding: 0px">
            <div class="info-panel-header">
                <h3>New Issue</h3>
                <a href="" ng-click="newIssueVm.showIssueInfoPanel()" class="close pull-right"
                   style="display: inline-block"></a>
            </div>
            <div class="info-panel-details">
                <div class="item-details highlight-row" style="margin-left: 0px;">
                    <div class="row">
                        <div class="label col-sm-5 text-right">
                            <span>Request Number: </span>
                        </div>
                        <div class="value col-sm-7" ng-if="newIssueVm.mode == 'ISSUE'">
                            <ui-select ng-model="newIssueVm.newIssue.request" theme="bootstrap" style="width:100%"
                                       on-select="newIssueVm.onSelectRequest($item)">
                                <ui-select-match
                                        placeholder="{{newIssueVm.selectRequestTitle}}">
                                    {{$select.selected.reqNumber}}
                                </ui-select-match>
                                <ui-select-choices
                                        repeat="request in newIssueVm.requests | filter: $select.search">
                                    <div ng-bind="request.reqNumber"></div>
                                </ui-select-choices>
                            </ui-select>
                        </div>

                        <div class="value col-sm-7" ng-if="newIssueVm.mode == 'REQ'">
                            <p>{{newIssueVm.newIssue.request.reqNumber}}</p>
                        </div>
                    </div>

                    <div class="row" ng-if="newIssueVm.newIssue.request != null">
                        <div class="label col-sm-5 text-right">
                            <span>Instance: </span>
                        </div>
                        <div class="value col-sm-7">
                            {{newIssueVm.newIssue.request.bomInstance.item.instanceName}}
                        </div>
                    </div>

                    <div class="row">
                        <div class="label col-sm-5 text-right">
                            <span>Issued By: </span>
                        </div>
                        <div class="value col-sm-7">
                            {{loginPersonDetails.person.fullName}}
                        </div>
                    </div>

                    <div class="row">
                        <div class="label col-sm-5 text-right">
                            <span>Notes : </span>
                        </div>
                        <div class="value col-sm-7">
                            <textarea type="text" class="form-control" ng-model="newIssueVm.newIssue.notes"></textarea>
                        </div>
                    </div>
                </div>
            </div>
        </div>

        <div class="responsive-table" style="padding: 10px;">
            <table class="table table-striped highlight-row">
                <thead>
                <tr>
                    <th class="name1-column">Nomenclature</th>
                    <%--<th style="width: 150px;">Type</th>--%>
                    <th style="width: 100px;">Units</th>
                    <th style="width: 100px;">Req Qty</th>
                    <th style="width: 100px;">Allocate Qty</th>
                    <th class="name1-column">Path</th>
                    <th class="name-column">Locations</th>
                    <%--<th style="width: 200px;">Scan/Enter UPN</th>--%>
                    <th style="width: 200px;">Serial Number</th>
                    <th style="width: 200px;">Root Card Number</th>
                    <th style="width: 100px;">Actions</th>
                </tr>
                </thead>

                <tbody>
                <tr ng-if="newIssueVm.newIssue.request == null">
                    <td colspan="25">Select Request</td>
                </tr>
                <tr ng-if="newIssueVm.newIssue.request != null"
                    ng-repeat="reqItem in newIssueVm.requestedItemRows">
                    <td class="name1-column">
                        <i ng-if="reqItem.issuedItemDto.length > 0" class="mr5 fa"
                           style="cursor: pointer; color: #909090"
                           ng-class="{'fa-plus-square': (reqItem.expanded == false || reqItem.expanded == null || reqItem.expanded == undefined),
                                           'fa-minus-square': reqItem.expanded == true}"></i>
                        <span class="level{{reqItem.level}}">{{reqItem.requestItem.item.item.itemMaster.itemName}}</span>
                    </td>
                    <%--<td style="width: 200px;">{{reqItem.requestItem.item.item.itemMaster.itemType.name}}</td>--%>
                    <td style="width: 100px;">{{reqItem.requestItem.item.item.itemMaster.itemType.units}}</td>
                    <td style="width: 200px;">
                        <span class="badge badge-primary" style="font-size: 14px;"
                              ng-if="reqItem.type == 'REQITEM' && !reqItem.requestItem.item.item.itemMaster.itemType.hasLots">{{reqItem.requestItem.quantity}}</span>
                        <span class="badge badge-primary" style="font-size: 14px;"
                              ng-if="reqItem.type == 'REQITEM' && reqItem.requestItem.item.item.itemMaster.itemType.hasLots">{{reqItem.requestItem.fractionalQuantity}}</span>

                        <span ng-if="reqItem.type == 'REQPART' && !reqItem.requestItem.item.item.itemMaster.itemType.hasLots">{{reqItem.requestQuantity}}</span>
                        <span ng-if="reqItem.type == 'REQPART' && reqItem.requestItem.item.item.itemMaster.itemType.hasLots">{{reqItem.requestItem.fractionalQuantity}}</span>
                    </td>
                    <td>
                        <span class="badge badge-success" style="font-size: 14px;"
                              ng-if="reqItem.type == 'REQITEM'">{{reqItem.allocateQty}}</span>
                    </td>
                    <td class="name1-column">
                        <span ng-if="reqItem.type == 'REQITEM'">{{reqItem.requestItem.item.namePath}}</span>
                    </td>
                    <td class="name-column">
                        <span ng-if="reqItem.type == 'REQPART'" ng-repeat="location in reqItem.storageLocations">
                            {{location}}<br>
                        </span>
                    </td>
                    <td class="upn-scan">

                        <ui-select
                                ng-if="reqItem.type == 'REQPART' && !reqItem.verified && reqItem.status == null"
                                ng-model="reqItem.selectInstanceToIssue" theme="bootstrap" style="width:100%">
                            <ui-select-match
                                    placeholder="Select Item">
                                {{$select.selected.oemNumber}}
                            </ui-select-match>
                            <ui-select-choices
                                    repeat="itemInstance in reqItem.inventoryInstances | filter: $select.search">
                                <div ng-bind="itemInstance.oemNumber"></div>
                            </ui-select-choices>
                        </ui-select>

                        <span ng-if="reqItem.type == 'REQPART' && reqItem.selectInstanceToIssue != null && reqItem.verified && reqItem.status == null">{{reqItem.selectInstanceToIssue.oemNumber}}</span>

                        <%--<input ng-if="reqItem.type == 'REQPART' && !reqItem.verified && reqItem.status == null"
                               type="text"
                               class="form-control"
                               ng-model="reqItem.scanUpn" placeholder="Enter Serial Number"
                               ng-disabled="reqItem.allocateQty == 0.0 || !reqItem.requestItem.approved"
                               ng-hide="reqItem.notYetApproved || reqItem.approved"/>--%>
                        <span ng-if="reqItem.type == 'REQPART' && reqItem.verified && reqItem.status == null">{{reqItem.serialNumber}}</span>

                        <span ng-if="reqItem.type == 'REQPART' && reqItem.status != null">
                            {{reqItem.issuedItemInstance.oemNumber}}</span>
                    </td>
                    <td>{{reqItem.selectInstanceToIssue.rootCardNo}}</td>
                    <td style="width: 100px;">
                        <i ng-if="reqItem.type == 'REQPART' && !reqItem.verified && reqItem.selectInstanceToIssue != null"
                           class="fa fa-check-circle-o"
                           ng-click="newIssueVm.addItemInstance(reqItem)"
                           style="font-size: 18px;padding: 0 4px;" title="Click to Issue"
                           ng-hide="reqItem.allocateQty == 0.0"></i>

                        <i ng-if="reqItem.type == 'REQPART' && reqItem.verified" class="fa fa-check-circle"
                           style="font-size: 18px;padding: 0 4px;color: green;" title="Added"
                           ng-hide="reqItem.allocateQty == 0.0"></i>
                        <i ng-if="reqItem.type == 'REQPART' && reqItem.verified" class="fa fa-times"
                           style="font-size: 18px;padding: 0 4px;color: green;" title="Click to Remove"
                           ng-click="newIssueVm.removeItemInstance(reqItem)"
                           ng-hide="reqItem.allocateQty == 0.0"></i>
                        <%--<i ng-if="reqItem.type == 'REQPART' && reqItem.verified" class="fa fa-print"
                           ng-click="newIssueVm.printIssuedUpn(reqItem.scanUpn)" title="Click to Print Scanned UPN"
                           style="font-size: 16px;padding: 0 4px;cursor: pointer"></i>--%>
                        <issue-item-status item="reqItem"></issue-item-status>
                    </td>
                </tr>
                </tbody>
            </table>
        </div>
        <div id="issueItemBarcode" style="display: none;" ng-if="newIssueVm.showPrintIssueUpn">
            <p style="text-align:center;margin-bottom: 0px;width: 195px">
                {{newIssueVm.selectedItemToIssue}}</p>
            <img ng-src="api/drdo/inwards/instance/barcode/{{newIssueVm.selectedItemToIssue}}?bust={{newIssueVm.barcodeBust}}"
                 alt="Barcode"
                 style="width: 200px;height: 50px;">
        </div>
    </div>
</div>