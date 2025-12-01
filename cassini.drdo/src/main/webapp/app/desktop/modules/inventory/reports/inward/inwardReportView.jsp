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

        .item-container .container-item .inward-number {
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
        <button class="btn btn-success btn-sm backButton" ng-click="inwardReportVm.back()">Back</button>
        <button class="btn btn-sm" ng-click="inwardReportVm.generateReport()" title="Click to PDF Print">
            <i class="fa fa-print" style="font-size: 16px;color: black;"></i>
        </button>
    </div>

    <div class="view-content no-padding">
        <div ng-show="inwardReportVm.loading == true" style="padding: 20px;">
            <h4>Loading...</h4>
        </div>

        <div ng-show="inwardReportVm.loading == false && inwardReportVm.inwards.length == 0"
             style="padding: 20px;">
            <h4>No inwards found for this BOM</h4>
        </div>

        <div class="split-pane fixed-left"
             ng-show="inwardReportVm.loading == false && inwardReportVm.inwards.length > 0">
            <div class="split-pane-component split-left-pane" style="min-width: 350px;max-width: 350px;">
                <div class="item-container">

                    <div ng-repeat="inward in inwardReportVm.inwards" class="container-item"
                         ng-class="{'active-item': inward.number == inwardReportVm.inward.number}"
                         ng-click="inwardReportVm.loadInwardReport(inward)">
                        <div class="row margin-bottom">
                            <div class="col-sm-6">
                                <div style="font-weight: 600;">Inward Number</div>
                                <div>{{inward.number}}</div>
                            </div>
                            <div class="col-sm-6 text-right">
                                <div style="font-weight: 600;">Received Date</div>
                                <div>{{inward.createdDate}}</div>
                            </div>
                        </div>
                        <div class="row">
                            <div class="col-sm-6">
                                <div style="font-weight: 600;">System</div>
                                <div>
                                    {{inward.bom.item.itemMaster.itemName}}
                                </div>
                            </div>
                            <div class="col-sm-6 text-right">
                                <div style="font-weight: 600;">Gate Pass</div>
                                <div>
                                    {{inward.gatePass.gatePass.name}}
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
            <div class="split-pane-divider" style="left: 350px !important;"></div>
            <div id="inwardReportView" class="split-pane-component split-right-pane noselect"
                 style="left: 350px !important;">
                <div class="item-details">
                    <div>
                        <h4 style="color: black;font-size: 18px;font-weight: bolder;" class="section-title"
                            ng-click="inwardReportVm.toggleSection('basic')">
                            <span class="section-btn">
                                <i class="fa" ng-class="{'fa-plus-square': !inwardReportVm.sections.basic,
                                                            'fa-minus-square': inwardReportVm.sections.basic}"></i>
                            </span>Basic Information
                        </h4>
                    </div>

                    <div class="item-details" ng-show="inwardReportVm.sections.basic">
                        <div class="row">
                            <div class="label col-xs-4 col-sm-3 text-right">
                                <span>Number : </span>
                            </div>
                            <div class="value col-xs-8 col-sm-9" style="font-size: 15px;">
                                <span>{{inwardReportVm.inward.number}}</span>
                            </div>
                        </div>
                        <div class="row">
                            <div class="label col-xs-4 col-sm-3 text-right">
                                <span>System : </span>
                            </div>
                            <div class="value col-xs-8 col-sm-9" style="font-size: 15px;">
                                <span>{{inwardReportVm.inward.bom.item.itemMaster.itemName}}</span>
                            </div>
                        </div>
                        <div class="row">
                            <div class="label col-xs-4 col-sm-3 text-right">
                                <span>Status : </span>
                            </div>
                            <div class="value col-xs-8 col-sm-9" style="font-size: 15px;">
                                <inward-status inward="inwardReportVm.inward"></inward-status>
                            </div>
                        </div>
                        <div class="row">
                            <div class="label col-xs-4 col-sm-3 text-right">
                                <span>Gate Pass: </span>
                            </div>
                            <div class="value col-xs-8 col-sm-9" style="font-size: 15px;">
                                <span><a href=""
                                         ng-click="inwardReportVm.downloadGatePass(inwardReportVm.inward.gatePass)">
                                    {{inwardReportVm.inward.gatePass.gatePass.name}}</a>
                                </span>
                            </div>
                        </div>

                        <div class="row">
                            <div class="label col-xs-4 col-sm-3 text-right">
                                <span>Received By:</span>
                            </div>
                            <div class="value col-xs-8 col-sm-9" style="font-size: 15px;">
                                <span>{{inwardReportVm.inward.createdByObject.fullName}}</span>
                            </div>
                        </div>

                        <div class="row">
                            <div class="label col-xs-4 col-sm-3 text-right">
                                <span>Received Date:</span>
                            </div>
                            <div class="value col-xs-8 col-sm-9" style="font-size: 15px;">
                                <span>{{inwardReportVm.inward.createdDate}}</span>
                            </div>
                        </div>

                        <div class="row">
                            <div class="label col-xs-4 col-sm-3 text-right">
                                <span>Notes: </span>
                            </div>
                            <div class="value col-xs-8 col-sm-9" style="font-size: 15px;">
                                <span>{{inwardReportVm.inward.notes}}</span>
                            </div>
                        </div>
                    </div>

                    <br>

                    <div>
                        <h4 class="section-title" style="color: black;font-size: 18px;font-weight: bolder;"
                            ng-click="inwardReportVm.toggleSection('items')">
                            <span class="section-btn">
                            <i class="fa" ng-class="{'fa-plus-square': !inwardReportVm.sections.items,
                                                        'fa-minus-square': inwardReportVm.sections.items}"></i>
                            </span>Inward Items <span style="color: green;">
                            ( {{inwardReportVm.inwardItems.length}} )</span>
                        </h4>
                    </div>
                    <div ng-show="inwardReportVm.sections.items">
                        <table class="table table-striped highlight-row" id="inwardItemReport">
                            <thead>
                            <tr>
                                <th>Nomenclature</th>
                                <th>Type</th>
                                <th>Units</th>
                                <th style="text-align: center;">Inward Qty</th>
                                <th style="text-align: center;">Accepted Qty</th>
                                <th style="text-align: center;">OnHold Qty</th>
                                <th style="text-align: center;">Returned Qty</th>
                                <th style="text-align: center;">Failed Qty</th>
                            </tr>
                            </thead>

                            <tbody>
                            <tr ng-if="inwardReportVm.inwardItems.length == 0">
                                <td colspan="25">No Inward Items</td>
                            </tr>
                            <tr class="bom"
                                ng-repeat="item in inwardReportVm.inwardItems | orderBy:'inwardItem.bomItem.item.itemMaster.itemName'">
                                <td>{{item.inwardItem.bomItem.item.itemMaster.itemName}}
                                    {{item.inwardItem.bomItem.item.partSpec.specName}}
                                </td>
                                <td>{{item.inwardItem.bomItem.item.itemMaster.parentType.name}}</td>
                                <td>{{item.inwardItem.bomItem.item.itemMaster.itemType.units}}</td>
                                <td style="text-align: center;">
                                    <span class="badge badge-primary"
                                          ng-if="!item.inwardItem.bomItem.item.itemMaster.itemType.hasLots">
                                        {{item.inwardItem.quantity}}</span>
                                    <span class="badge badge-primary"
                                          ng-if="item.inwardItem.bomItem.item.itemMaster.itemType.hasLots">
                                        {{item.inwardItem.fractionalQuantity}}</span>
                                </td>
                                <td style="text-align: center;">
                                    <span class="badge badge-success"
                                          ng-if="!item.inwardItem.bomItem.item.itemMaster.itemType.hasLots">
                                        {{item.acceptedQty}}</span>
                                    <span class="badge badge-success"
                                          ng-if="item.inwardItem.bomItem.item.itemMaster.itemType.hasLots">
                                        {{item.fractionalAcceptedQty}}</span>
                                </td>
                                <td style="text-align: center;">
                                    <span class="badge badge-warning"
                                          ng-if="!item.inwardItem.bomItem.item.itemMaster.itemType.hasLots">
                                        {{item.onHold}}</span>
                                    <span class="badge badge-warning"
                                          ng-if="item.inwardItem.bomItem.item.itemMaster.itemType.hasLots">
                                        {{item.fractionalOnHold}}</span>
                                </td>
                                <td style="text-align: center;">
                                    <span class="badge badge-danger"
                                          ng-if="!item.inwardItem.bomItem.item.itemMaster.itemType.hasLots">
                                        {{item.returned}}</span>
                                    <span class="badge badge-danger"
                                          ng-if="item.inwardItem.bomItem.item.itemMaster.itemType.hasLots">
                                        {{item.fractionalReturned}}</span>
                                </td>
                                <td style="text-align: center;">
                                    <span class="badge badge-secondary"
                                          ng-if="!item.inwardItem.bomItem.item.itemMaster.itemType.hasLots">
                                        {{item.failed}}</span>
                                    <span class="badge badge-secondary"
                                          ng-if="item.inwardItem.bomItem.item.itemMaster.itemType.hasLots">
                                        {{item.fractionalFailed}}</span>
                                </td>
                            </tr>
                            </tbody>
                        </table>
                    </div>

                    <br>

                    <div>
                        <h4 class="section-title" style="color: black;font-size: 18px;font-weight: bolder;"
                            ng-click="inwardReportVm.toggleSection('accepted')">
                               <span class="section-btn">
                               <i class="fa" ng-class="{'fa-plus-square': !inwardReportVm.sections.accepted,
                                                           'fa-minus-square': inwardReportVm.sections.accepted}"></i>
                               </span>Accepted Parts<span style="color: orange;">( {{inwardReportVm.acceptedInstances.length}} )</span>
                        </h4>
                    </div>

                    <div ng-show="inwardReportVm.sections.accepted">
                        <table class="table table-striped highlight-row" id="acceptedItemReport">
                            <thead>
                            <tr>
                                <th>Code</th>
                                <th>Nomenclature</th>
                                <%--<th>Type</th>--%>
                                <th>Quantity</th>
                                <th>Status</th>
                                <th>UPN</th>
                                <th>Location</th>
                            </tr>
                            <tr ng-if="inwardReportVm.acceptedInstances.length == 0">
                                <td colspan="15">No Accepted Items</td>
                            </tr>
                            </thead>
                            <tbody>
                            <tr ng-if="inwardReportVm.acceptedInstances.length > 0"
                                ng-repeat="inwardInstance in inwardReportVm.acceptedInstances | orderBy:'item.item.itemMaster.itemName'">
                                <td>{{inwardInstance.bomItem.hierarchicalCode}}</td>
                                <td>{{inwardInstance.item.item.itemMaster.itemName}}
                                    {{inwardInstance.item.item.partSpec.specName}}
                                </td>
                                <%--<td>{{inwardInstance.item.item.itemMaster.itemType.name}}</td>--%>
                                <td>
                                    <span ng-if="!inwardInstance.item.item.itemMaster.itemType.hasLots">1</span>
                                    <span ng-if="inwardInstance.item.item.itemMaster.itemType.hasLots">
                                        {{inwardInstance.item.lotSize}}</span>
                                </td>
                                <td>
                                    <item-instance-status object="inwardInstance.item"></item-instance-status>
                                </td>
                                <td>
                                    <span class="badge badge-warning" style="font-size: 13px;">{{inwardInstance.item.upnNumber}}</span>
                                </td>
                                <td>{{inwardInstance.item.storage.name}}</td>
                            </tr>
                            </tbody>
                        </table>
                    </div>
                    <br>

                    <div>
                        <h4 class="section-title" style="color: black;font-size: 18px;font-weight: bolder;"
                            ng-click="inwardReportVm.toggleSection('onHold')">
                               <span class="section-btn">
                               <i class="fa" ng-class="{'fa-plus-square': !inwardReportVm.sections.onHold,
                                                           'fa-minus-square': inwardReportVm.sections.onHold}"></i>
                               </span>On Hold Parts<span style="color: orange;">( {{inwardReportVm.onHoldInstances.length}} )</span>
                        </h4>
                    </div>

                    <div ng-show="inwardReportVm.sections.onHold">
                        <table class="table table-striped highlight-row" id="onHoldItemReport">
                            <thead>
                            <tr>
                                <th>Code</th>
                                <th>Nomenclature</th>
                                <th>Type</th>
                                <th>Quantity</th>
                                <th>UPN</th>
                                <th>Location</th>
                            </tr>
                            <tr ng-if="inwardReportVm.onHoldInstances.length == 0">
                                <td colspan="15">No On Hold Items</td>
                            </tr>
                            </thead>
                            <tbody>
                            <tr ng-if="inwardReportVm.onHoldInstances.length > 0"
                                ng-repeat="inwardInstance in inwardReportVm.onHoldInstances | orderBy:'item.item.itemMaster.itemName'">
                                <td>{{inwardInstance.bomItem.hierarchicalCode}}</td>
                                <td>{{inwardInstance.item.item.itemMaster.itemName}}
                                    {{inwardInstance.item.item.partSpec.specName}}
                                </td>
                                <td>{{inwardInstance.item.item.itemMaster.parentType.name}}</td>
                                <td>
                                    <span ng-if="!inwardInstance.item.item.itemMaster.itemType.hasLots">1</span>
                                    <span ng-if="inwardInstance.item.item.itemMaster.itemType.hasLots">
                                        {{inwardInstance.item.lotSize}}</span>
                                </td>
                                <td>
                                    <span class="badge badge-warning" style="font-size: 13px;">{{inwardInstance.item.upnNumber}}</span>
                                </td>
                                <td>{{inwardInstance.item.storage.name}}</td>
                            </tr>
                            </tbody>
                        </table>
                    </div>
                    <br>

                    <div>
                        <h4 class="section-title" style="color: black;font-size: 18px;font-weight: bolder;"
                            ng-click="inwardReportVm.toggleSection('returns')">
                            <span class="section-btn">
                            <i class="fa" ng-class="{'fa-plus-square': !inwardReportVm.sections.returns,
                                                        'fa-minus-square': inwardReportVm.sections.returns}"></i>
                            </span>Returned Parts <span
                                style="color: red;">( {{inwardReportVm.returnInstances.length}} )</span>
                        </h4>
                    </div>
                    <div ng-show="inwardReportVm.sections.returns">
                        <table class="table table-striped highlight-row" id="returnItemReport">
                            <thead>
                            <tr>
                                <th>Code</th>
                                <th>Serial Number</th>
                                <th>UPN</th>
                                <th>Location</th>
                                <%--<th>Certificate Number</th>--%>
                                <th>Reason</th>
                                <th>ReturnBy</th>
                                <%--<th>Attachments</th>--%>
                            </tr>
                            <tr ng-if="inwardReportVm.returnInstances.length == 0">
                                <td colspan="15">No Rejects</td>
                            </tr>
                            </thead>
                            <tbody>
                            <tr ng-repeat="inwardInstance in inwardReportVm.returnInstances | orderBy:'item.item.itemMaster.itemName'">
                                <td>{{inwardInstance.bomItem.hierarchicalCode}}</td>
                                <td>{{inwardInstance.item.oemNumber}}</td>
                                <td>
                                    <span class="badge badge-danger" style="font-size: 13px;">{{inwardInstance.item.upnNumber}}</span>
                                </td>
                                <td>{{inwardInstance.item.storage.name}}</td>
                                <%--<td>{{inwardInstance.item.certificateNumber}}</td>--%>
                                <td>{{inwardInstance.item.reason}}</td>
                                <td>{{inwardInstance.item.returnBy.firstName}}</td>
                            </tr>
                            </tbody>
                        </table>
                    </div>

                    <br>

                    <div>
                        <h4 class="section-title" style="color: rgb(179, 16, 24);font-size: 18px;font-weight: bolder;"
                            ng-click="inwardReportVm.toggleSection('failures')">
                            <span class="section-btn">
                            <i class="fa" style="color:rgb(179, 16, 24)"
                               ng-class="{'fa-plus-square': !inwardReportVm.sections.failures,
                                                        'fa-minus-square': inwardReportVm.sections.failures}"></i>
                            </span>Failure Parts<span
                                style="color: rgb(179, 16, 24);">( {{inwardReportVm.failedInstances.length}} )</span>
                        </h4>
                    </div>
                    <div ng-show="inwardReportVm.sections.failures">
                        <table class="table table-striped highlight-row" id="failureItemReport">
                            <thead>
                            <tr>
                                <th>Code</th>
                                <th>Nomenclature</th>
                                <th>Type</th>
                                <th>Quantity</th>
                                <th>UPN</th>
                                <th>Serial Number</th>

                            </tr>
                            <tr ng-if="inwardReportVm.failedInstances.length == 0">
                                <td colspan="15">No Failed Parts</td>
                            </tr>
                            </thead>
                            <tbody>
                            <tr ng-if="inwardReportVm.failedInstances.length > 0"
                                ng-repeat="inwardInstance in inwardReportVm.failedInstances | orderBy:'item.item.itemMaster.itemName'">
                                <td>{{inwardInstance.bomItem.hierarchicalCode}}</td>
                                <td>{{inwardInstance.item.item.itemMaster.itemName}}
                                    {{inwardInstance.item.item.partSpec.specName}}
                                </td>
                                <td>{{inwardInstance.item.item.itemMaster.parentType.name}}</td>
                                <td>
                                    <span ng-if="!inwardInstance.item.item.itemMaster.itemType.hasLots">1</span>
                                    <span ng-if="inwardInstance.item.item.itemMaster.itemType.hasLots">
                                        {{inwardInstance.item.lotSize}}</span>
                                </td>
                                <td>
                                    <span class="badge badge-danger" style="font-size: 13px;">{{inwardInstance.item.upnNumber}}</span>
                                </td>
                                <td>
                                    <span ng-if="!inwardInstance.item.item.itemMaster.itemType.hasLots"><%--{{inwardItemInstance.item.manufacturer.name}} - --%></span>{{inwardItemInstance.item.oemNumber}}
                                </td>
                            </tr>
                            </tbody>
                        </table>
                    </div>


                    <div style="display: none">
                        <table class="table table-striped highlight-row" style="margin-bottom: 20px;"
                               id="inwardReport">
                            <thead>
                            <tr>
                                <th class="threeHundred-column">System</th>
                                <th class="hundred-column" style="text-align: center;">Inward Number</th>
                                <th class="oneFifty-column">Status</th>
                                <th class="oneFifty-column">GatePass Number</th>
                                <th class="hundred-column">Received By</th>
                                <th class="oneFifty-column">Received Date</th>
                            </tr>
                            </thead>
                            <tbody>
                            <tr>
                                <td>{{inwardReportVm.inward.bom.item.itemMaster.itemName}}</td>
                                <td>{{inwardReportVm.inward.number}}</td>
                                <td>{{inwardReportVm.inward.status}}</td>
                                <td>{{inwardReportVm.inward.gatePass.gatePassNumber}}</td>
                                <td>{{inwardReportVm.inward.createdByObject.fullName}}</td>
                                <td>{{inwardReportVm.inward.createdDate}}</td>
                            </tr>
                            </tbody>
                        </table>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
