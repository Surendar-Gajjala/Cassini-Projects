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
        <button class="btn btn-success btn-sm backButton" ng-click="requestReportVm.back()">Back</button>
        <button class="btn btn-sm" ng-click="requestReportVm.generateReport()" title="Click to PDF Print">
            <i class="fa fa-print" style="font-size: 16px;color: black;"></i>
        </button>
    </div>

    <div class="view-content no-padding">
        <div ng-show="requestReportVm.loading == true" style="padding: 20px;">
            <h4>Loading...</h4>
        </div>

        <div ng-show="requestReportVm.loading == false && requestReportVm.requests.length == 0"
             style="padding: 20px;">
            <h4>No Requests found for this BOM Instance</h4>
        </div>

        <div class="split-pane fixed-left"
             ng-show="requestReportVm.loading == false && requestReportVm.requests.length > 0">
            <div class="split-pane-component split-left-pane" style="min-width: 350px;max-width: 350px;">
                <div class="item-container">

                    <div ng-repeat="request in requestReportVm.requests" class="container-item"
                         ng-class="{'active-item': request.reqNumber == requestReportVm.request.reqNumber}"
                         ng-click="requestReportVm.loadRequestReport(request)">
                        <div class="row margin-bottom">
                            <div class="col-sm-6">
                                <div style="font-weight: 600;">Request Number</div>
                                <div>{{request.reqNumber}}</div>
                            </div>
                            <div class="col-sm-6 text-right">
                                <div style="font-weight: 600;">Status</div>
                                <div>
                                    <request-status request="request"></request-status>
                                </div>
                            </div>
                        </div>
                        <div class="row">
                            <div class="col-sm-6">
                                <div style="font-weight: 600;">Instance</div>
                                <div>
                                    {{request.bomInstance.item.instanceName}}
                                </div>
                            </div>
                            <div class="col-sm-6 text-right">
                                <div style="font-weight: 600;">Requested By</div>
                                <div>
                                    {{request.requestedBy.fullName}}
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
                            ng-click="requestReportVm.toggleSection('basic')">
                            <span class="section-btn">
                                <i class="fa" ng-class="{'fa-plus-square': !requestReportVm.sections.basic,
                                                            'fa-minus-square': requestReportVm.sections.basic}"></i>
                            </span>Basic Information
                        </h4>
                    </div>

                    <div class="item-details" ng-show="requestReportVm.sections.basic">
                        <div class="row">
                            <div class="label col-xs-4 col-sm-3 text-right">
                                <span>Number : </span>
                            </div>
                            <div class="value col-xs-8 col-sm-9" style="font-size: 15px;">
                                <span>{{requestReportVm.request.reqNumber}}</span>
                            </div>
                        </div>
                        <div class="row">
                            <div class="label col-xs-4 col-sm-3 text-right">
                                <span>Instance : </span>
                            </div>
                            <div class="value col-xs-8 col-sm-9" style="font-size: 15px;">
                                <span>{{requestReportVm.request.bomInstance.item.instanceName}}</span>
                            </div>
                        </div>

                        <div class="row">
                            <div class="label col-xs-4 col-sm-3 text-right">
                                <span>Status :</span>
                            </div>
                            <div class="value col-xs-8 col-sm-9" style="font-size: 15px;">
                                <request-status request="requestReportVm.request"></request-status>
                            </div>
                        </div>

                        <div class="row">
                            <div class="label col-xs-4 col-sm-3 text-right">
                                <span>Requested By : </span>
                            </div>
                            <div class="value col-xs-8 col-sm-9" style="font-size: 15px;">
                                <span>{{requestReportVm.request.requestedBy.fullName}}
                                </span>
                            </div>
                        </div>

                        <div class="row">
                            <div class="label col-xs-4 col-sm-3 text-right">
                                <span>Requested Date :</span>
                            </div>
                            <div class="value col-xs-8 col-sm-9" style="font-size: 15px;">
                                <span>{{requestReportVm.request.requestedDate}}</span>
                            </div>
                        </div>

                        <div class="row">
                            <div class="label col-xs-4 col-sm-3 text-right">
                                <span>Notes: </span>
                            </div>
                            <div class="value col-xs-8 col-sm-9" style="font-size: 15px;">
                                <span>{{requestReportVm.request.notes}}</span>
                            </div>
                        </div>
                    </div>

                    <br>

                    <div>
                        <h4 class="section-title" style="color: black;font-size: 18px;font-weight: bolder;"
                            ng-click="requestReportVm.toggleSection('items')">
                            <span class="section-btn">
                            <i class="fa" ng-class="{'fa-plus-square': !requestReportVm.sections.items,
                                                        'fa-minus-square': requestReportVm.sections.items}"></i>
                            </span>Requested Items <span style="color: green;">
                            ( {{requestReportVm.requestItems.length}} )</span>
                        </h4>
                    </div>
                    <div ng-show="requestReportVm.sections.items">
                        <table class="table table-striped highlight-row">
                            <thead>
                            <tr>
                                <th>Type</th>
                                <th>Nomenclature</th>
                                <th>Units</th>
                                <th style="text-align: center;">Request Qty</th>
                                <th>Path</th>
                                <th>Status</th>
                            </tr>
                            </thead>

                            <tbody>
                            <tr ng-if="requestReportVm.requestItems.length == 0">
                                <td colspan="25">No Request Items</td>
                            </tr>
                            <tr class="bom"
                                ng-repeat="requestItem in requestReportVm.requestItems | orderBy:'item.item.itemMaster.itemName'">
                                <td>{{requestItem.item.item.itemMaster.parentType.name}}</td>
                                <td>{{requestItem.item.item.itemMaster.itemName}}</td>
                                <td>{{requestItem.item.item.itemMaster.itemType.units}}</td>
                                <td style="text-align: center;">
                                    <span class="badge badge-primary"
                                          ng-if="!requestItem.item.item.itemMaster.itemType.hasLots">{{requestItem.quantity}}</span>
                                    <span class="badge badge-primary"
                                          ng-if="requestItem.item.item.itemMaster.itemType.hasLots">{{requestItem.fractionalQuantity}}</span>
                                </td>
                                <td>{{requestItem.item.namePath}}</td>
                                <td>
                                    <span ng-if="requestItem.approved" class="badge badge-success">APPROVED</span>
                                    <span ng-if="requestItem.accepted && !requestItem.approved"
                                          class="badge badge-warning">ACCEPTED</span>
                                    <span ng-if="!requestItem.accepted && !requestItem.approved"
                                          class="badge badge-primary">PENDING</span>
                                </td>
                            </tr>
                            </tbody>
                        </table>
                    </div>

                    <br>

                    <div>
                        <h4 class="section-title" style="color: black;font-size: 18px;font-weight: bolder;"
                            ng-click="requestReportVm.toggleSection('relatedIssues')">
                            <span class="section-btn">
                            <i class="fa" ng-class="{'fa-plus-square': !requestReportVm.sections.relatedIssues,
                                                        'fa-minus-square': requestReportVm.sections.relatedIssues}"></i>
                            </span>Related Issues <span style="color: green;">
                            ( {{requestReportVm.issuedItems.length}} )</span>
                        </h4>
                    </div>
                    <div ng-show="requestReportVm.sections.relatedIssues">
                        <table class="table table-striped highlight-row" id="relatedIssuesReport">
                            <thead>
                            <tr>
                                <th>Type</th>
                                <th>Nomenclature</th>
                                <th>Units</th>
                                <th style="text-align: center;">Quantity</th>
                                <th>UPN</th>
                                <th>Serial Number</th>
                            </tr>
                            </thead>

                            <tbody>
                            <tr ng-if="requestReportVm.issuedItems.length == 0">
                                <td colspan="25">No Issues</td>
                            </tr>
                            <tr class="bom"
                                ng-repeat="issueItem in requestReportVm.issuedItems | orderBy:'requestItem.item.item.itemMaster.itemName'">
                                <td>{{issueItem.requestItem.item.item.itemMaster.parentType.name}}</td>
                                <td>{{issueItem.requestItem.item.item.itemMaster.itemName}}</td>
                                <td>{{issueItem.requestItem.item.item.itemMaster.itemType.units}}</td>
                                <td style="text-align: center;">
                                    <span class="badge badge-primary"
                                          ng-if="!issueItem.requestItem.item.item.itemMaster.itemType.hasLots">1</span>
                                    <span class="badge badge-primary"
                                          ng-if="issueItem.requestItem.item.item.itemMaster.itemType.hasLots">{{issueItem.issueItem.fractionalQuantity}}</span>
                                </td>
                                <td>
                                    <span class="badge badge-success" style="font-size: 14px;"
                                          title="Click to show details"
                                          ng-click="showUpnHistory(issueItem.itemInstance,'right')">{{issueItem.itemInstance.upnNumber}}</span>
                                </td>

                                <td>
                                    <%--{{issueItem.itemInstance.manufacturer.mfrCode}} - --%>
                                    {{issueItem.itemInstance.oemNumber}}
                                </td>
                            </tr>
                            </tbody>
                        </table>
                    </div>

                    <div style="display: none">
                        <table class="table table-striped highlight-row" style="margin-bottom: 20px;"
                               id="requestReport">
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
                                <td>{{requestReportVm.system}}</td>
                                <td>{{requestReportVm.request.bomInstance.item.instanceName}}</td>
                                <td>{{requestReportVm.request.reqNumber}}</td>
                                <td>{{requestReportVm.request.status}}</td>
                                <td>{{requestReportVm.request.requestedBy.fullName}}</td>
                                <td>{{requestReportVm.request.requestedDate}}</td>
                            </tr>
                            </tbody>
                        </table>

                        <table class="table table-striped highlight-row" style="margin-bottom: 20px;"
                               id="requestDetailsReport">
                            <thead>
                            <tr>
                                <th>Nomenclature</th>
                                <th>Type</th>
                                <th>Units</th>
                                <th>Requested Qty</th>
                                <th>Path</th>
                                <th>Status</th>

                            </tr>
                            </thead>

                            <tbody>
                            <tr ng-repeat="reqItem in requestReportVm.requestItems">
                                <td>{{reqItem.item.item.itemMaster.itemName}}</td>
                                <td>{{reqItem.item.item.itemMaster.parentType.name}}</td>
                                <td>{{reqItem.item.item.itemMaster.itemType.units}}</td>
                                <td>
                                    <span class="badge badge-primary" style="font-size: 14px;"
                                          ng-if="!reqItem.item.item.itemMaster.itemType.hasLots">{{reqItem.quantity}}</span>
                                    <span class="badge badge-primary" style="font-size: 14px;"
                                          ng-if="reqItem.item.item.itemMaster.itemType.hasLots">{{reqItem.fractionalQuantity}}</span>
                                </td>
                                <td>{{reqItem.item.namePath}}</td>
                                <td>
                                    <span ng-if="reqItem.approved" class="badge badge-success">APPROVED</span>
                                    <span ng-if="reqItem.accepted && !reqItem.approved"
                                          class="badge badge-warning">ACCEPTED</span>
                                    <span ng-if="!reqItem.accepted && !reqItem.approved"
                                          class="badge badge-primary">PENDING</span>
                                </td>

                            </tr>
                            </tbody>
                        </table>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
