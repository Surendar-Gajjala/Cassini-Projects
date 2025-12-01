<style scoped>
    .view-content {
        position: relative;
    }

    .view-content .responsive-table {
        padding: 10px;
        /*position: absolute;*/
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

    tr.provAccept-item-row {
        background: rgba(210, 120, 39, 0.75); /* fallback for old browsers */
        background: -webkit-linear-gradient(to left, rgba(210, 128, 7, 0.75), rgba(210, 148, 25, 0.75)); /* Chrome 10-25, Safari 5.1-6 */
        background: linear-gradient(to left, rgba(210, 128, 7, 0.75), rgba(210, 148, 25, 0.75)); /* W3C, IE 10+/ Edge, Firefox 16+, Chrome 26+, Opera 12+, Safari 7+ */

        color: black !important;
    }

    tr.provAccept-item-row td {
        background: transparent !important;
        color: black !important;
    }

    tr.provAccept-item-row td a {
        color: black !important;
    }

</style>
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
            padding: 10px;
        }

        .split-right-pane {
            left: 400px !important; /* Same as left component width */
            margin-left: 5px; /* Same as divider width */
            padding: 10px;
        }

        .p-approve.modal {
            display: none; /* Hidden by default */
            position: fixed; /* Stay in place */
            z-index: 10; /* Sit on top */
            padding-top: 15px; /* Location of the box */
            left: 0;
            top: 0;
            width: 100%; /* Full width */
            height: 100%; /* Full height */
            overflow: auto; /* Enable scroll if needed */
            background-color: rgb(0, 0, 0); /* Fallback color */
            background-color: rgba(0, 0, 0, 0.4); /* Black w/ opacity */
        }

        .p-approve .modal-content {
            margin-left: auto;
            margin-right: auto;
            top: 50px;
            display: block;
            /*height: 50%;*/
            width: 50%;
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
            overflow-y: auto;
        }

    </style>

    <div class="view-toolbar">
        <div class="btn-group">
            <button class="btn btn-sm btn-default"
                    ng-click="issueDetailsVm.back()">Back
            </button>
            <button class="btn btn-sm btn-default"
                    ng-click="issueDetailsVm.showIssueInfoPanel(!issueDetailsVm.sidePanel)">Issue Information
            </button>
            <%--<button class="btn btn-sm btn-default"
                    ng-if="hasPermission('permission.issued.BDLQCApprove') && issueDetailsVm.issue.status == 'BDL_QC'"
                    ng-hide="issueDetailsVm.hideApprove"
                    ng-click="issueDetailsVm.approveIssue()">Approve
            </button>--%>

            <%--<button class="btn btn-sm btn-default"
                    ng-if="hasPermission('permission.inward.storeApprove') && issueDetailsVm.issue.status == 'STORE'"
                    ng-click="issueDetailsVm.issueItems()">Issue
            </button>--%>
            <%--<button class="btn btn-sm" ng-show="issueDetailsVm.issue.status == 'ISSUED'"
                    ng-click="issueDetailsVm.generateReport()" title="Click for Issue Report">
                <i class="fa fa-print" style="font-size: 16px;color: black;"></i>
            </button>--%>
            <button class="btn btn-sm" ng-click="issueDetailsVm.receiveItems()"
                    ng-if="hasPermission('permission.bdlPcc.receive') || hasPermission('permission.versityPpc.receive')"
                    ng-hide="issueDetailsVm.issue.status == 'RECEIVED'"
                    title="Click to Receive Items">Receive
                Items
            </button>
        </div>
    </div>
    <div class="view-content no-padding" id="a" style="overflow-y: auto;">
        <div class="issue-info-panel" id="issueInfoPanel" style="padding: 0px">
            <div class="info-panel-header">
                <h3>Issue Information</h3>
                <a href="" ng-click="issueDetailsVm.showIssueInfoPanel(false)" class="close pull-right"
                   style="display: inline-block"></a>
            </div>
            <div class="info-panel-details">
                <div class="item-details highlight-row" style="margin-left: 0px;">
                    <div class="row">
                        <div class="label col-sm-5 text-right">
                            <span>Request Number: </span>
                        </div>
                        <div class="value col-sm-7">
                            {{issueDetailsVm.issue.request.reqNumber}}
                        </div>
                    </div>
                    <div class="row">
                        <div class="label col-sm-5 text-right">
                            <span>Missile : </span>
                        </div>
                        <div class="value col-sm-7">
                            {{issueDetailsVm.issue.request.bomInstance.item.instanceName}}
                        </div>
                    </div>

                    <div class="row">
                        <div class="label col-sm-5 text-right">
                            <span>Requested Date : </span>
                        </div>
                        <div class="value col-sm-7">
                            {{issueDetailsVm.issue.request.requestedDate}}
                        </div>
                    </div>

                    <div class="row">
                        <div class="label col-sm-5 text-right">
                            <span>Issue Number: </span>
                        </div>
                        <div class="value col-sm-7">
                            {{issueDetailsVm.issue.number}}
                        </div>
                    </div>

                    <div class="row">
                        <div class="label col-sm-5 text-right">
                            <span>Status : </span>
                        </div>
                        <div class="value col-sm-7">
                            <issue-status issue="issueDetailsVm.issue"></issue-status>
                        </div>
                    </div>

                    <div class="row" ng-if="issueDetailsVm.issue.status == 'ISSUE'">
                        <div class="label col-sm-5 text-right">
                            <span>Issued To : </span>
                        </div>
                        <div class="value col-sm-7">
                            {{issueDetailsVm.issue.request.requestedBy.fullName}}
                        </div>
                    </div>

                    <div class="row">
                        <div class="label col-sm-5 text-right">
                            <span>Last Updated By : </span>
                        </div>
                        <div class="value col-sm-7">
                            {{issueDetailsVm.issue.modifiedByObject.fullName}}
                        </div>
                    </div>

                    <div class="row">
                        <div class="label col-sm-5 text-right">
                            <span>Last Updated Date : </span>
                        </div>
                        <div class="value col-sm-7">
                            {{issueDetailsVm.issue.modifiedDate}}
                        </div>
                    </div>

                    <div>
                        <br>
                        <h4 class="section-title" style="color: black;font-size:20px;">Issue History</h4>
                        <table class="table table-striped highlight-row">
                            <thead>
                            <tr>
                                <th>Status</th>
                                <th>Updated By</th>
                                <th>Updated Date</th>
                            </tr>
                            </thead>
                            <tbody>

                            <tr ng-if="issueDetailsVm.request.statusHistories.length == 0">
                                <td colspan="12">No Request History</td>
                            </tr>
                            <tr ng-repeat="history in issueDetailsVm.issueHistories">
                                <td>
                                    <issue-status issue="history"></issue-status>
                                </td>
                                <td>{{history.updatedBy.fullName}}</td>
                                <td>{{history.updatedDate}}</td>
                            </tr>
                            </tbody>
                        </table>
                    </div>
                </div>
            </div>
        </div>


        <div class="responsive-table" style="padding: 10px;">
            <table class="table table-striped highlight-row">
                <thead>
                <tr>
                    <th ng-if="(hasPermission('permission.bdlPcc.receive') || hasPermission('permission.versityPpc.receive')) && issueDetailsVm.issue.status != 'RECEIVED'">
                        Select
                    </th>
                    <th>Nomenclature</th>
                    <th>Units</th>
                    <th>BOM Qty</th>
                    <th>Issued Qty</th>
                    <th>Path</th>
                    <th>UPN</th>
                    <th>Serial Number</th>
                    <th>Certificate</th>
                    <th>Status</th>
                    <th>Actions</th>
                </tr>
                </thead>

                <tbody>
                <tr ng-repeat="issuedItem in issueDetailsVm.issuedItems | orderBy :'itemInstance.item.itemMaster.itemName'"
                    ng-class="{'provAccept-item-row': issuedItem.itemInstance.provisionalAccept}">
                    <td style="text-align: center;"
                        ng-if="(hasPermission('permission.bdlPcc.receive') || hasPermission('permission.versityPpc.receive')) && issueDetailsVm.issue.status != 'RECEIVED'">
                        <input type="checkbox"
                               ng-if="issuedItem.type == 'ISSUEITEM' && (issuedItem.issueItem.status == 'APPROVED' || issuedItem.issueItem.status == 'P_APPROVED') && (hasPermission('permission.bdlPcc.receive') || hasPermission('permission.versityPpc.receive'))"
                               ng-model="issuedItem.receive"
                               ng-change="issueDetailsVm.selectReceiveItem(issuedItem)"/>
                    </td>
                    <td>
                        <p class="level{{issuedItem.level}}" ng-if="issuedItem.type == 'SECTION'"
                           style="margin: 0px;">
                            <i ng-if="issuedItem.children.length > 0" class="mr5 fa fa-caret-down"
                               style="cursor: pointer; color: #909090;font-size: 18px;"></i>
                            <span style="font-weight: 600;color: black;">{{issuedItem.name}}
                                <span ng-if="issuedItem.versity"> ( VSPL )</span>
                            </span>
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
                                      ng-if="issuedItem.itemInstance.item.itemMaster.itemType.hasLots">{{issuedItem.issueItem.fractionalQuantity}}</span>
                            </span>
                    </td>
                    <td>
                        <span ng-if="issuedItem.type == 'ISSUEITEM'">{{issuedItem.requestItem.item.namePath}}</span>
                    </td>
                    <td>
                        <a href="" ng-if="!issuedItem.itemInstance.item.itemMaster.itemType.hasLots"
                           ng-click="showUpnHistory(issuedItem.itemInstance,'right')" title="Click to show details">
                            <span class="badge badge-success" style="font-size: 14px;">{{issuedItem.itemInstance.upnNumber}}</span>
                        </a>

                        <a href="" ng-if="issuedItem.itemInstance.item.itemMaster.itemType.hasLots"
                           ng-click="showLotUpnHistory(issuedItem.itemInstance,'INWARD')"
                           title="Click to show details">
                            {{issuedItem.itemInstance.upnNumber}}
                        </a>
                    </td>
                    <td>
                        {{issuedItem.itemInstance.oemNumber}}
                        <%--<span ng-if="issuedItem.itemInstance.item.itemMaster.itemType.hasLots">{{issuedItem.itemInstance.oemNumber}}</span>--%>
                    </td>
                    <td>
                        <div ng-if="issuedItem.type == 'ISSUEITEM'"
                             ng-repeat="certificate in issuedItem.certificates">
                            <a href="" ng-click="issueDetailsVm.openPropertyAttachment(certificate)">
                                {{certificate.name}}
                            </a>
                        </div>
                    </td>
                    <td>
                        <issue-item-status item="issuedItem.issueItem"
                                           title="{{issuedItem.itemInstance.reason}}"></issue-item-status>
                    </td>
                    <td>
                            <span ng-if="issuedItem.type == 'ISSUEITEM'">
                                <i class="fa fa-check-circle-o" style="color: green;font-size: 18px;padding: 0 4px;"
                                   title="Click to Approve"
                                   ng-click="issueDetailsVm.approveIssueItem(issuedItem.issueItem)"
                                   ng-if="(hasPermission('permission.issued.BDLQCApprove') || hasPermission('permission.issued.versityQCApprove')) && issuedItem.issueItem.status == 'PENDING'"></i>
                                <i class="fa fa-shield" style="color: orange;font-size: 18px;padding: 0 4px;"
                                   title="Click to Provisional Approve"
                                   ng-click="issueDetailsVm.showProvisionalApproveDialog(issuedItem)"
                                   ng-if="(hasPermission('permission.issued.BDLQCApprove') || hasPermission('permission.issued.versityQCApprove')) && issuedItem.issueItem.status == 'PENDING'"></i>
                                <i class="fa fa-reply" style="color: black;font-size: 18px;padding: 0 4px;"
                                   title="Click to Reject Item"
                                   ng-click="issueDetailsVm.showRejectDialog(issuedItem)"
                                   ng-if="(hasPermission('permission.issued.BDLQCApprove') || hasPermission('permission.issued.versityQCApprove')) && issuedItem.issueItem.status == 'PENDING'">
                                </i>
                                <i class="fa fa-undo" style="color: black;font-size: 18px;padding: 0 4px;"
                                   title="Click to Reset Return" ng-click="issueDetailsVm.resetReturnItem(issuedItem)"
                                   ng-if="hasPermission('permission.admin.all')  && issuedItem.issueItem.status == 'REJECTED'">
                                </i>
                            </span>

                            <span ng-if="issuedItem.type == 'ISSUEITEM'">
                                 <i class="fa fa-check-circle-o" style="color: white;font-size: 18px;padding: 0 4px;"
                                    title="Click to Approve"
                                    ng-click="issueDetailsVm.approveItem(issuedItem.issueItem)"
                                    ng-if="(hasPermission('permission.issued.BDLQCApprove') || hasPermission('permission.issued.versityQCApprove')) && issuedItem.itemInstance.provisionalAccept">
                                 </i>
                            </span>
                        <%--<span ng-if="issuedItem.type == 'ISSUEITEM' && issuedItem.issueItem.status == 'P_APPROVED' && (issueDetailsVm.issue.status == 'STORE' || issueDetailsVm.issue.status == 'BDL_QC')">
                            <span class="badge badge-warning" style="font-size: 14px;"
                                  title="{{issuedItem.reason}}">Provisional Approved</span>
                        </span>
                        <span ng-if="issuedItem.type == 'ISSUEITEM' && issuedItem.issueItem.status == 'APPROVED' && (issueDetailsVm.issue.status == 'STORE' || issueDetailsVm.issue.status == 'BDL_QC')">
                            <span class="badge badge-success" style="font-size: 14px;">Approved</span>
                        </span>
                        <span ng-if="issuedItem.type == 'ISSUEITEM' && issuedItem.issueItem.status == 'REJECTED' && (issueDetailsVm.issue.status == 'STORE' || issueDetailsVm.issue.status == 'BDL_QC')">
                            <span class="badge badge-danger" style="font-size: 14px;" title="{{issuedItem.reason}}">Rejected</span>
                        </span>
                        <span ng-if="issuedItem.type == 'ISSUEITEM' && issueDetailsVm.issue.status == 'ISSUED' && (issuedItem.issueItem.status == 'P_APPROVED' || issuedItem.issueItem.status == 'APPROVED')">
                            <span class="badge badge-success" style="font-size: 14px;">Issued</span>
                        </span>
                        <span ng-if="issuedItem.type == 'ISSUEITEM' && issueDetailsVm.issue.status == 'ISSUED' && issuedItem.issueItem.status == 'REJECTED'">
                            <span class="badge badge-danger" style="font-size: 14px;">Rejected</span>
                        </span>--%>
                    </td>
                </tr>
                </tbody>
            </table>
        </div>
        <hr>

        <div ng-repeat="receive in issueDetailsVm.receives" style="width: 95%;margin: auto;">
            <p style="color: black;"><span style="font-size: 16px;font-weight: bold;">Issue Voucher : {{receive.receive.receiveSequence}}</span>
                <i class="fa fa-print" ng-if="hasPermission('permission.issued.new')"
                   style="padding: 0 5px;font-size: 18px;cursor: pointer" title="Click to print"
                   ng-click="issueDetailsVm.printReceive(receive)"></i>
            </p>

            <%--<div class="responsive-table">

            </div>--%>
            <table class="table table-striped highlight-row" id="receive{{receive.receive.id}}">
                <thead>
                <tr>
                    <th class="five-column">S.No</th>
                    <th class="twentyPercent-column">Nomenclature</th>
                    <th class="tenPercent-column">Units</th>
                    <th class="tenPercent-column">BOM Qty</th>
                    <th class="tenPercent-column">Issued Qty</th>
                    <th class="tenPercent-column">UPN</th>
                    <th class="fifteenPercent-column">Serial Number</th>
                    <th class="fifteenPercent-column">Certificate Number</th>
                    <th class="five-column">Remarks</th>
                </tr>
                </thead>

                <tbody>
                <tr ng-repeat="receiveItem in receive.receiveItems  | orderBy: 'item.item.itemMaster.itemName'">
                    <td>{{$index + 1}}</td>
                    <td>
                        {{receiveItem.item.item.itemMaster.itemName}}
                    </td>
                    <td>
                        {{receiveItem.item.item.itemMaster.itemType.units}}
                    </td>
                    <td>
                           <span class="badge badge-primary" style="font-size: 14px;"
                                 ng-if="!receiveItem.item.item.itemMaster.itemType.hasLots">{{receiveItem.item.quantity}}</span>
                            <span class="badge badge-primary" style="font-size: 14px;"
                                  ng-if="receiveItem.item.item.itemMaster.itemType.hasLots">{{receiveItem.item.fractionalQuantity}}</span>
                    </td>
                    <td>
                        <span class="badge badge-success" style="font-size: 14px;">{{receiveItem.issuedQuantity}}</span>
                    </td>
                    <td>
                        <a href="" ng-if="!receiveItem.item.item.itemMaster.itemType.hasLots"
                           ng-click="showUpnHistory(receiveItem.itemInstances[0],'right')"
                           title="Click to show details">
                            <span class="badge badge-success" style="font-size: 14px;">{{receiveItem.itemInstances[0].upnNumber}}</span>
                        </a>

                        <a href=""
                           ng-click="showLotUpnHistory(receiveItem.lotInstances[0],'ISSUE')"
                           title="Click to show details">
                            <span ng-if="receiveItem.item.item.itemMaster.itemType.hasLots">{{receiveItem.lotInstances[0].upnNumber}}</span>
                        </a>
                    </td>
                    <td>
                        <%--<div ng-repeat="itemInstance in receiveItem.itemInstances" style="padding: 3px 0">
                            {{itemInstance.oemNumber}}{{$last ? '' : ($index==Users.length-2) ? ' and ' : ', '}}
                        </div>

                        <div ng-repeat="lotInstance in receiveItem.lotInstances" style="padding: 3px 0">
                            {{lotInstance.itemInstance.oemNumber}} / {{lotInstance.sequence}}{{$last ? '' : ($index==Users.length-2) ? ' and ' : ', '}}
                        </div>--%>
                        {{receiveItem.itemInstanceText}}
                    </td>
                    <td>
                        <div ng-repeat="certificateNumber in receiveItem.certificateNumbers" style="padding: 3px 0">
                            {{certificateNumber}}
                        </div>
                        <%--<div ng-repeat="itemInstance in receiveItem.itemInstances" style="padding: 3px 0">
                            {{itemInstance.certificateNumber}}
                        </div>

                        <div ng-repeat="lotInstance in receiveItem.lotInstances" style="padding: 3px 0">
                            {{lotInstance.itemInstance.certificateNumber}}
                        </div>--%>
                    </td>
                    <td></td>
                </tr>

                <%--<tr ng-repeat="issueItem in receive.issueItems">
                    <td>{{$index + 1}}</td>
                    <td>
                        {{issueItem.itemInstance.item.itemMaster.itemName}}
                    </td>
                    <td>
                        {{issueItem.itemInstance.item.itemMaster.itemType.units}}
                    </td>
                    <td>
                           <span class="badge badge-primary" style="font-size: 14px;"
                                 ng-if="!issueItem.itemInstance.item.itemMaster.itemType.hasLots">{{issueItem.requestItem.item.quantity}}</span>
                            <span class="badge badge-primary" style="font-size: 14px;"
                                  ng-if="issueItem.itemInstance.item.itemMaster.itemType.hasLots">{{issueItem.requestItem.item.fractionalQuantity}}</span>
                    </td>
                    <td>
                           <span class="badge badge-success" style="font-size: 14px;"
                                 ng-if="!issueItem.itemInstance.item.itemMaster.itemType.hasLots">{{issueItem.issueItem.quantity}}</span>
                            <span class="badge badge-success" style="font-size: 14px;"
                                  ng-if="issueItem.itemInstance.item.itemMaster.itemType.hasLots">{{issueItem.issueItem.fractionalQuantity}}</span>
                    </td>
                    <td>
                        <a href="" ng-if="!issueItem.itemInstance.item.itemMaster.itemType.hasLots"
                           ng-click="showUpnHistory(issueItem.itemInstance,'right')" title="Click to show details">
                            <span class="badge badge-success" style="font-size: 14px;">{{issueItem.itemInstance.upnNumber}}</span>
                        </a>

                        <a href=""
                           ng-click="showLotUpnHistory(issueItem.lotInstance,'ISSUE')"
                           title="Click to show details">
                            <span ng-if="issueItem.itemInstance.item.itemMaster.itemType.hasLots">{{issueItem.lotInstance.upnNumber}}</span>
                        </a>
                    </td>
                    <td>
                        {{issueItem.itemInstance.oemNumber}}
                        <span ng-if="issueItem.itemInstance.item.itemMaster.itemType.hasLots"> / {{issueItem.lotInstance.sequence}}</span>
                        &lt;%&ndash;<span ng-if="!issueItem.itemInstance.item.itemMaster.itemType.hasLots">{{issueItem.itemInstance.oemNumber}}</span>
                        <span ng-if="issueItem.itemInstance.item.itemMaster.itemType.hasLots">{{issueItem.itemInstance.lotNumber}}</span>&ndash;%&gt;
                    </td>
                    <td>
                        {{issueItem.itemInstance.certificateNumber}}
                    </td>
                    <td></td>
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
                    <td>{{issueDetailsVm.system}}</td>
                    <td>{{issueDetailsVm.issue.request.bomInstance.item.instanceName}}</td>
                    <td>{{issueDetailsVm.issue.request.reqNumber}}</td>
                    <td>{{issueDetailsVm.issue.modifiedByObject.fullName}}</td>
                    <td>{{issueDetailsVm.issue.issuedTo.fullName}}</td>
                    <td>{{issueDetailsVm.issue.createdDate}}</td>
                </tr>
                </tbody>
            </table>

            <table class="table table-striped highlight-row" id="issueDetailsReport">
                <thead>
                <tr>
                    <th>S.No</th>
                    <th>Nomenclature</th>
                    <th>Units</th>
                    <th>BOM Qty</th>
                    <th>Issued Qty</th>
                    <th>UPN</th>
                    <th>Serial Number</th>
                    <th>Certificate Number</th>
                    <th>Remarks</th>
                </tr>
                </thead>

                <tbody>
                <tr ng-repeat="issuedItem in issueDetailsVm.issuedBomItems">
                    <td>{{$index + 1}}</td>
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
                                      ng-if="!issuedItem.requestItem.item.itemMaster.itemType.hasLots">{{issuedItem.requestItem.item.quantity}}</span>
                                <span class="badge badge-primary" style="font-size: 14px;"
                                      ng-if="issuedItem.requestItem.item.itemMaster.itemType.hasLots">{{issuedItem.requestItem.item.fractionalQuantity}}</span>
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
                        <a href="" ng-if="!issuedItem.itemInstance.item.itemMaster.itemType.hasLots"
                           ng-click="showUpnHistory(issuedItem.itemInstance,'right')" title="Click to show details">
                            <span class="badge badge-success" style="font-size: 14px;">{{issuedItem.itemInstance.upnNumber}}</span>
                        </a>

                        <a href="" ng-if="issuedItem.itemInstance.item.itemMaster.itemType.hasLots"
                           ng-click="showLotUpnHistory(issuedItem.lotInstance,'ISSUE')"
                           title="Click to show details">
                            <span class="badge badge-success" style="font-size: 14px;">{{issuedItem.lotInstance.upnNumber}}</span>
                        </a>
                    </td>
                    <td>
                        <span ng-if="issuedItem.type == 'ISSUEITEM'">{{issuedItem.itemInstance.oemNumber}} - {{issuedItem.lotInstance.sequence}}</span>
                    </td>
                    <td>
                        <span ng-if="issuedItem.type == 'ISSUEITEM'">{{issuedItem.itemInstance.certificateNumber}}</span>
                    </td>
                    <td>

                    </td>
                </tr>
                </tbody>
            </table>
        </div>
    </div>

    <div id="pApprove-view" class="p-approve modal">
        <div class="modal-content">
            <div style="height:50px;text-align: center">
                <h3 style="margin: 0;padding: 10px;border-bottom: 1px solid lightgrey;">Provisional Approve Item</h3>
            </div>
            <div style="width: 100%;">

                <form class="form-horizontal" style="padding: 10px;">
                    <div class="form-group">
                        <label class="col-sm-4 control-label">
                            <span>Reason</span>
                            <span class="asterisk">*</span> : </label>

                        <div class="col-sm-7">
                            <textarea rows="5" class="form-control"
                                      placeholder="Enter Reason"
                                      ng-model="issueDetailsVm.selectedIssueItem.itemInstance.reason"
                                      style="resize: none"></textarea>
                        </div>
                    </div>
                </form>
                <p style="text-align: center;color: red;font-weight: 600;font-size: 16px;">
                    {{issueDetailsVm.errorMessage}}</p>
            </div>
            <div style="height: 50px;;text-align: center;background: lightgrey;">
                <button class="btn btn-sm btn-default" ng-click="issueDetailsVm.closeProvisionalApproveDialog()"
                        style="margin-top: 5px;">Cancel
                </button>
                <button class="btn btn-sm btn-primary" ng-click="issueDetailsVm.provisionalApproveIssueItem()"
                        style="margin-top: 5px;">Submit
                </button>
            </div>
        </div>
    </div>

    <div id="reject-view" class="p-approve modal">
        <div class="modal-content">
            <div style="height:50px;text-align: center">
                <h3 style="margin: 0;padding: 10px;border-bottom: 1px solid lightgrey;">Reject Item</h3>
            </div>
            <div style="width: 100%;">

                <form class="form-horizontal" style="padding: 10px;">
                    <div class="form-group">
                        <label class="col-sm-4 control-label">
                            <span>Reason</span>
                            <span class="asterisk">*</span> : </label>

                        <div class="col-sm-7">
                            <textarea rows="5" class="form-control"
                                      placeholder="Enter Reason"
                                      ng-model="issueDetailsVm.selectedIssueItem.itemInstance.reason"
                                      style="resize: none"></textarea>
                        </div>
                    </div>
                </form>
                <p style="text-align: center;color: red;font-weight: 600;font-size: 16px;">
                    {{issueDetailsVm.errorMessage}}</p>
            </div>
            <div style="height: 50px;;text-align: center;background: lightgrey;">
                <button class="btn btn-sm btn-default" ng-click="issueDetailsVm.closeRejectDialog()"
                        style="margin-top: 5px;">Cancel
                </button>
                <button class="btn btn-sm btn-primary" ng-click="issueDetailsVm.rejectIssueItem()"
                        style="margin-top: 5px;">Submit
                </button>
            </div>
        </div>
    </div>
</div>