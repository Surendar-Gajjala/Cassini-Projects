<div id="requestDetailsView" class="view-container" fitcontent>

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

        .sections-model.modal {
            display: none; /* Hidden by default */
            position: fixed; /* Stay in place */
            z-index: 1; /* Sit on top */
            padding-top: 15px; /* Location of the box */
            left: 0;
            top: 0;
            width: 100%; /* Full width */
            height: 100%; /* Full height */
            overflow: auto; /* Enable scroll if needed */
            background-color: rgb(0, 0, 0); /* Fallback color */
            background-color: rgba(0, 0, 0, 0.4); /* Black w/ opacity */
        }

        .sections-model .modal-content {
            margin-left: auto;
            margin-right: auto;
            top: 111px;
            display: block;
            height: 40%;
            width: 50%;
        }


    </style>

    <div class="view-toolbar">
        <div class="btn-group">
            <button class="btn btn-sm btn-default"
                    ng-click="requestDetailsVm.back()">Back
            </button>
            <%--<button class="btn btn-sm btn-success"
                    ng-if="(hasPermission('permission.requests.bdlApprove') || hasPermission('permission.requests.versityApprove')) && requestDetailsVm.request.showAcceptAll"
                    ng-hide="requestDetailsVm.request.status == 'REJECTED' || requestDetailsVm.request.status == 'APPROVED'"
                    ng-click="requestDetailsVm.acceptAll()">Accept
            </button>
            <button class="btn btn-sm btn-success"
                    ng-if="(hasPermission('permission.requests.casApprove') && requestDetailsVm.request.showApprove)"
                    ng-hide="requestDetailsVm.request.status == 'REJECTED' || requestDetailsVm.request.status == 'APPROVED'"
                    ng-click="requestDetailsVm.approve()">Approve
            </button>
            <button class="btn btn-sm btn-danger"
                    ng-if="hasPermission('permission.requests.bdlApprove') || hasPermission('permission.requests.versityApprove') || hasPermission('permission.requests.casApprove')"
                    ng-hide="requestDetailsVm.request.status == 'REJECTED' || requestDetailsVm.request.status == 'APPROVED' || requestDetailsVm.hideRejectButton"
                    ng-click="requestDetailsVm.showRejectView()">Reject
            </button>--%>

            <button class="btn btn-sm" title="Click to Issue this Request"
                    ng-if="hasPermission('permission.issued.new') && !requestDetailsVm.request.issued"
                    ng-click="requestDetailsVm.issueRequest(requestDetailsVm.request)">Issue
            </button>

            <%--<button class="btn btn-xs" ng-click="requestDetailsVm.generateReport()" title="Click to PDF print"
                    ng-if="requestDetailsVm.request.status == 'APPROVED' || requestDetailsVm.request.status == 'REJECTED'">
                <i class="fa fa-print" style="color: black;font-size: 18px;"></i>
            </button>--%>
        </div>
    </div>
    <div class="view-content no-padding" id="a">
        <div class="split-pane fixed-left">
            <div class="split-pane-component split-left-pane">
                <div class="item-details highlight-row" style="margin-left: 0px;">
                    <div class="row">
                        <div class="label col-sm-5 text-right">
                            <span>Request Number: </span>
                        </div>
                        <div class="value col-sm-7">
                            {{requestDetailsVm.request.reqNumber}}
                        </div>
                    </div>
                    <div class="row">
                        <div class="label col-sm-5 text-right">
                            <span>Status: </span>
                        </div>
                        <div class="value col-sm-7">
                            <request-status request="requestDetailsVm.request"></request-status>
                        </div>
                    </div>

                    <div class="row">
                        <div class="label col-sm-5 text-right">
                            <span>Missile: </span>
                        </div>
                        <div class="value col-sm-7">
                            {{requestDetailsVm.request.bomInstance.item.instanceName}}
                        </div>
                    </div>

                    <div class="row">
                        <div class="label col-sm-5 text-right">
                            <span>Requested On: </span>
                        </div>
                        <div class="value col-sm-7">
                            {{requestDetailsVm.request.requestedDate}}
                        </div>
                    </div>

                    <div class="row">
                        <div class="label col-sm-5 text-right">
                            <span>Requested by: </span>
                        </div>
                        <div class="value col-sm-7">
                            {{requestDetailsVm.request.requestedBy.fullName}}
                        </div>
                    </div>

                    <div class="row">
                        <div class="label col-sm-5 text-right">
                            <span>Notes : </span>
                        </div>
                        <div class="value col-sm-7">
                            {{requestDetailsVm.request.notes}}
                        </div>
                    </div>

                    <div class="row" ng-if="requestDetailsVm.request.status == 'REJECTED'">
                        <div class="label col-sm-5 text-right">
                            <span>Reason : </span>
                        </div>
                        <div class="value col-sm-7">
                            {{requestDetailsVm.request.reason}}
                        </div>
                    </div>

                    <%--<div>
                        <br>
                        <h4 class="section-title" style="color: black;font-size:20px;">Request History</h4>
                        <table class="table table-striped highlight-row">
                            <thead>
                            <tr>
                                <th style="width: 30px;"></th>
                                <th>Status</th>
                                <th>Updated By</th>
                                <th>Updated Date</th>
                            </tr>
                            </thead>
                            <tbody>

                            <tr ng-if="requestDetailsVm.request.statusHistories.length == 0">
                                <td colspan="12">No Request History</td>
                            </tr>
                            <tr ng-repeat="history in requestDetailsVm.request.statusHistories">
                                <td style="width: 30px;">
                                    <i ng-if="history.result" class="fa fa-check-circle" title="Approved"
                                       style="font-size: 18px;color: green"></i>
                                    <i ng-if="!history.result" class="fa fa-times-circle" title="Rejected"
                                       style="font-size: 18px;color: darkred;"></i>
                                </td>
                                <td>
                                    <request-status request="history"></request-status>
                                </td>
                                <td>{{history.user.fullName}}</td>
                                <td>{{history.timestamp | date:'dd-MM-yyyy HH:mm:ss'}}</td>
                            </tr>
                            </tbody>
                        </table>
                    </div>--%>
                </div>
            </div>

            <div class="split-pane-divider"></div>

            <div class="split-pane-component split-right-pane noselect">
                <table class="table table-striped highlight-row">
                    <thead>
                    <tr>
                        <th>Nomenclature</th>
                        <%--<th>Type</th>--%>
                        <th>Units</th>
                        <th>Requested Qty</th>
                        <th>Allocated Qty</th>
                        <th>Path</th>
                        <%--<th>Status</th>--%>
                        <%--<th ng-if="requestDetailsVm.request.status != 'REJECTED'">Actions</th>--%>
                    </tr>
                    </thead>

                    <tbody>
                    <tr ng-repeat="reqItem in requestDetailsVm.requestItems | orderBy:'-allocatedQuantity'">
                        <td>

                            <p class="level{{reqItem.level}}" ng-if="reqItem.type == 'SECTION'"
                               title="" style="margin:0;">
                                <i ng-if="reqItem.children.length > 0" class="mr5 fa fa-caret-down"
                                   style="cursor: pointer; color: #909090;font-size: 18px;"
                                <%--ng-class="{'fa-caret-right': (reqItem.expanded == false || reqItem.expanded == null || reqItem.expanded == undefined),
                                           'fa-caret-down': reqItem.expanded == true}"--%>></i>
                                <span style="font-weight: 600;color: black;">{{reqItem.name}}
                                    <span ng-if="reqItem.versity"> ( VSPL )</span>
                                </span>
                            </p>

                            <p class="level{{reqItem.level}}" ng-if="reqItem.type == 'REQUESTITEM'" style="margin:0;">
                                {{reqItem.item.item.itemMaster.itemName}}
                            </p>
                            <%--<span ng-if="reqItem.type == 'SECTION'">{{reqItem.name}}</span>
                            <span ng-if="reqItem.type == 'REQUESTITEM'">{{reqItem.item.item.itemMaster.itemName}}</span>--%>
                        </td>
                        <%--<td>{{reqItem.item.item.itemMaster.itemType.name}}</td>--%>
                        <td>
                            <span ng-if="reqItem.type == 'REQUESTITEM'">{{reqItem.item.item.itemMaster.itemType.units}}</span>
                        </td>
                        <td>
                            <span ng-if="reqItem.type == 'REQUESTITEM'">
                                <span class="badge badge-primary" style="font-size: 14px;"
                                      ng-if="!reqItem.item.item.itemMaster.itemType.hasLots">{{reqItem.quantity}}</span>
                                <span class="badge badge-primary" style="font-size: 14px;"
                                      ng-if="reqItem.item.item.itemMaster.itemType.hasLots">{{reqItem.fractionalQuantity}}</span>
                            </span>
                        </td>
                        <td>
                            <span class="badge badge-success"
                                  ng-if="reqItem.type == 'REQUESTITEM' && reqItem.allocatedQuantity > 0"
                                  style="font-size: 14px;">{{reqItem.allocatedQuantity}}</span>
                        </td>
                        <td>
                            <span ng-if="reqItem.type == 'REQUESTITEM'">{{reqItem.item.namePath}}</span>
                        </td>
                        <%-- <td>
                             <span ng-if="reqItem.type == 'REQUESTITEM'">
                                 <request-item-status item="reqItem"></request-item-status>
                             </span>
                         </td>--%>
                        <%--<td>
                            <span ng-if="reqItem.type == 'REQUESTITEM'">
                                <i class="fa fa-check-circle-o"
                                   ng-if="reqItem.status == 'PENDING' && (hasPermission('permission.requests.bdlApprove') || hasPermission('permission.requests.versityApprove'))"
                                   title="Accept"
                                   ng-click="requestDetailsVm.acceptItem(reqItem)"
                                   style="font-size: 18px"></i>
                                <i class="fa fa-check-circle-o"
                                   ng-if="reqItem.status == 'ACCEPTED' && hasPermission('permission.requests.casApprove')"
                                   title="Approve"
                                   ng-click="requestDetailsVm.approveItem(reqItem)"
                                   style="font-size: 18px;"></i>

                                <i class="fa fa-reply"
                                   ng-if="reqItem.status == 'ACCEPTED' && hasPermission('permission.requests.casApprove')"
                                   title="Reject"
                                   ng-click="requestDetailsVm.showRejectItemView(reqItem)"
                                   style="font-size: 18px;padding-left: 4px;"></i>
                                <i class="fa fa-reply"
                                   ng-if="reqItem.status == 'PENDING' && (hasPermission('permission.requests.bdlApprove') || hasPermission('permission.requests.versityApprove'))"
                                   title="Reject"
                                   ng-click="requestDetailsVm.showRejectItemView(reqItem)"
                                   style="font-size: 18px;padding-left: 4px;"></i>
                            </span>
                        </td>--%>
                    </tr>
                    </tbody>
                </table>
            </div>
        </div>
    </div>

    <div style="display: none">
        <table class="table table-striped highlight-row" style="margin-bottom: 20px;" id="requestReport">
            <thead>
            <tr>
                <th class="threeHundred-column">System</th>
                <th class="oneFifty-column">Missile</th>
                <th class="hundred-column" style="text-align: center;">Request Number</th>
                <th class="oneFifty-column">Status</th>
                <th class="hundred-column">Requested By</th>
                <th class="oneFifty-column">Requested Date</th>
            </tr>
            </thead>
            <tbody>
            <tr>
                <td>{{requestDetailsVm.system}}</td>
                <td>{{requestDetailsVm.request.bomInstance.item.instanceName}}</td>
                <td>{{requestDetailsVm.request.reqNumber}}</td>
                <td>{{requestDetailsVm.request.status}}</td>
                <td>{{requestDetailsVm.request.requestedBy.fullName}}</td>
                <td>{{requestDetailsVm.request.requestedDate}}</td>
            </tr>
            </tbody>
        </table>

        <table class="table table-striped highlight-row" style="margin-bottom: 20px;" id="requestDetailsReport">
            <thead>
            <tr>
                <th>Nomenclature</th>
                <th>Type</th>
                <th>Units</th>
                <th>Requested Qty</th>
                <th>Path</th>
            </tr>
            </thead>

            <tbody>
            <tr ng-repeat="reqItem in requestDetailsVm.requestItems">
                <td>
                    <p class="level{{reqItem.level}}" ng-if="reqItem.type == 'SECTION'"
                       title="" style="margin:0;">
                        <i ng-if="reqItem.children.length > 0" class="mr5 fa fa-caret-down"
                           style="cursor: pointer; color: #909090;font-size: 18px;"
                        <%--ng-class="{'fa-caret-right': (reqItem.expanded == false || reqItem.expanded == null || reqItem.expanded == undefined),
                                   'fa-caret-down': reqItem.expanded == true}"--%>></i>
                        <span style="font-weight: 600;color: black;">{{reqItem.name}}
                            <span ng-if="reqItem.versity"> ( VSPL )</span>
                        </span>
                    </p>

                    <p class="level{{reqItem.level}}" ng-if="reqItem.type == 'REQUESTITEM'" style="margin:0;">
                        {{reqItem.item.item.itemMaster.itemName}}
                    </p>
                </td>
                <td>{{reqItem.item.item.itemMaster.parentType.name}}</td>
                <td>{{reqItem.item.item.itemMaster.itemType.units}}</td>
                <td>
                    <span class="badge badge-primary" style="font-size: 14px;"
                          ng-if="!reqItem.item.item.itemMaster.itemType.hasLots">{{reqItem.quantity}}</span>
                    <span class="badge badge-primary" style="font-size: 14px;"
                          ng-if="reqItem.item.item.itemMaster.itemType.hasLots">{{reqItem.fractionalQuantity}}</span>
                </td>
                <td>{{reqItem.item.namePath}}</td>

            </tr>
            </tbody>
        </table>
    </div>

    <div id="rejectReason-view" class="sections-model modal">
        <div class="modal-content">
            <h3 style="text-align: center;background: lavender;height: 20%;margin: 0px;padding-top: 10px;">Enter Reject
                Reason</h3>

            <div style="height: 60%;">
                <div class="form-group" style="margin-right: 0px;width: 100%;margin-top: 10px;">

                    <label class="col-sm-3 control-label" style="text-align: right;margin-top: 10px;">Reason <span
                            class="asterisk">*</span> : </label>

                    <div class="col-sm-9">
                        <textarea class="form-control" ng-model="requestDetailsVm.request.reason" rows="5"
                                  style="resize: none"
                                  placeholder="Enter Reason"></textarea>
                    </div>
                </div>
            </div>
            <div style="height: 20%;text-align: center;background: lightgrey;">
                <button class="btn btn-sm btn-primary" ng-click="requestDetailsVm.reject()"
                        style="margin-top: 5px;">Reject
                </button>
                <button class="btn btn-sm btn-default" ng-click="requestDetailsVm.closeRejectView()"
                        style="margin-top: 5px;">Close
                </button>
            </div>
        </div>
    </div>

    <div id="rejectItemReason-view" class="sections-model modal">
        <div class="modal-content">
            <h3 style="text-align: center;background: lavender;height: 20%;margin: 0px;padding-top: 10px;">Enter Reject
                Reason</h3>

            <div style="height: 60%;">
                <div class="form-group" style="margin-right: 0px;width: 100%;margin-top: 10px;">

                    <label class="col-sm-3 control-label" style="text-align: right;margin-top: 10px;">Reason <span
                            class="asterisk">*</span> : </label>

                    <div class="col-sm-9">
                        <textarea class="form-control" ng-model="requestDetailsVm.selectedItemToReject.reason" rows="5"
                                  style="resize: none"
                                  placeholder="Enter Reason"></textarea>
                    </div>
                </div>
            </div>
            <div style="height: 20%;text-align: center;background: lightgrey;">
                <button class="btn btn-sm btn-primary" ng-click="requestDetailsVm.rejectItem()"
                        style="margin-top: 5px;">Reject
                </button>
                <button class="btn btn-sm btn-default" ng-click="requestDetailsVm.closeRejectItemView()"
                        style="margin-top: 5px;">Close
                </button>
            </div>
        </div>
    </div>
</div>