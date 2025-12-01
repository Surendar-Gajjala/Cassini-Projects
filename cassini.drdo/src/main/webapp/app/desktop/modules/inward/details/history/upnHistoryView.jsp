<div style="padding: 10px;">
    <style scoped>
        .timeline {
            width: 250px;
        }

        .timeline::before {
            left: -80px;
        }

        .timeline .flag {
            cursor: pointer;
        }

        .timeline .time-wrapper {
            margin-top: -20px;
            margin-left: 100px;
            width: 330px;
        }

        .timeline .time {
            font-size: 12px;
        }

        .noneColor {
            color: black;
            background: #E5F3F3;
        }

        .noneColor::after {
            border: solid transparent;
            border-right-color: #E5F3F3 !important;
            border-width: 6px !important;
        }

        .submitted-color {
            color: white;
            background: blue;
        }

        .submitted-color::after {
            border: solid transparent;
            border-right-color: blue !important;
            border-width: 6px !important;
        }

        .acceptColor {
            color: white;
            background: green;
        }

        .acceptColor::after {
            border: solid transparent;
            border-right-color: green !important;
            border-width: 6px !important;
        }

        .pAccept-color {
            color: white;
            background: orange;
        }

        .pAccept-color::after {
            border: solid transparent;
            border-right-color: orange !important;
            border-width: 6px !important;
        }

        .inventoryColor {
            color: black;
            background: lightblue;

        }

        .inventoryColor::after {
            border: solid transparent;
            border-right-color: lightblue !important;
            border-width: 6px !important;
        }

        .issueColor {
            color: white;
            background: darkgreen;

        }

        .issueColor::after {
            border: solid transparent;
            border-right-color: darkgreen !important;
            border-width: 6px !important;
        }

        .verifyColor {
            background: blueviolet;
            color: white;
        }

        .verifyColor::after {
            border: solid transparent;
            border-right-color: blueviolet !important;
            border-width: 6px !important;
        }

        .returnColor {
            color: white;
            background: red;
        }

        .returnColor::after {
            border: solid transparent;
            border-right-color: red !important;
            border-width: 6px !important;
        }

        .reviewColor {
            color: black;
            background: lightcoral;
        }

        .reviewColor::after {
            border: solid transparent;
            border-right-color: lightcoral !important;
            border-width: 6px !important;
        }

        .dispatchedColor {
            color: white;
            background: darkblue;
        }

        .dispatchedColor::after {
            border: solid transparent;
            border-right-color: darkblue !important;
            border-width: 6px !important;
        }

    </style>


    <div>

        <div>
            <h4 ng-click="upnHistoryVm.showBasicInformation()" style="cursor: pointer;color: blue;padding: 20px 0px;">
                <i class="fa fa-plus-square" ng-if="!upnHistoryVm.showInfo"></i>
                <i class="fa fa-minus-square" ng-if="upnHistoryVm.showInfo"></i>
                Basic Information : </h4>

            <div ng-if="upnHistoryVm.showInfo" class="item-details" style="margin-left: 0px;">
                <div class="row">
                    <div class="col-sm-5 text-right">
                        <span>Nomenclature: </span>
                    </div>
                    <div class="col-sm-7">
                        {{upnHistoryVm.upnDetails.bomItem.item.itemMaster.itemName}}
                    </div>
                </div>

                <div class="row">
                    <div class="col-sm-5 text-right">
                        <span>Item Code: </span>
                    </div>
                    <div class="col-sm-7">
                        {{upnHistoryVm.upnDetails.bomItem.item.itemMaster.itemCode}}
                    </div>
                </div>

                <div class="row">
                    <div class="col-sm-5 text-right">
                        <span>Code: </span>
                    </div>
                    <div class="col-sm-7">
                        {{upnHistoryVm.upnDetails.bomItem.hierarchicalCode}}
                    </div>
                </div>

                <div class="row">
                    <div class="col-sm-5 text-right">
                        <span>Inward Number: </span>
                    </div>
                    <div class="col-sm-7">
                        <span ng-if="upnHistoryVm.upnDetails.inwards.length == 1">
                            <a href="" ng-click="upnHistoryVm.showInward(upnHistoryVm.upnDetails.inwards[0])">{{upnHistoryVm.upnDetails.inwards[0].number}}</a> (
                                <span ng-if="upnHistoryVm.upnDetails.inwards[0].inwardItem.bomItem.item.itemMaster.itemType.hasLots">{{upnHistoryVm.upnDetails.inwards[0].inwardItem.fractionalQuantity}}</span>
                                <span ng-if="!upnHistoryVm.upnDetails.inwards[0].inwardItem.bomItem.item.itemMaster.itemType.hasLots">{{upnHistoryVm.upnDetails.inwards[0].inwardItem.quantity}}</span>
                                )
                        </span>
                        <span ng-if="upnHistoryVm.upnDetails.inwards.length > 1">
                            <li ng-repeat="inward in upnHistoryVm.upnDetails.inwards">
                                <a href="" ng-click="upnHistoryVm.showInward(inward)">{{inward.number}}</a> (
                                <span ng-if="inward.inwardItem.bomItem.item.itemMaster.itemType.hasLots">{{inward.inwardItem.fractionalQuantity}}</span>
                                <span ng-if="!inward.inwardItem.bomItem.item.itemMaster.itemType.hasLots">{{inward.inwardItem.quantity}}</span>
                                )
                            </li>
                        </span>
                    </div>
                </div>

                <div class="row">
                    <div class="col-sm-5 text-right">
                        <span>Gate Pass: </span>
                    </div>
                    <div class="col-sm-7">
                        <%--<span>{{upnHistoryVm.upnDetails.inwards[0].gatePass.gatePass.name}}</span>--%>
                        <a ng-if="upnHistoryVm.upnDetails.inwards.length == 1" href=""
                           ng-click="upnHistoryVm.downloadGatePass(upnHistoryVm.upnDetails.inwards[0].gatePass)"
                           title="Click to download Gate Pass">{{upnHistoryVm.upnDetails.inwards[0].gatePass.gatePass.name}}
                        </a>
                        <span ng-if="upnHistoryVm.upnDetails.inwards.length > 1">
                            <li ng-repeat="inward in upnHistoryVm.upnDetails.inwards">
                                <a href="" ng-click="upnHistoryVm.downloadGatePass(inward.gatePass)"
                                   title="Click to download Gate Pass">{{inward.gatePass.gatePass.name}}
                                </a>
                            </li>
                        </span>
                    </div>
                </div>

                <div class="row">
                    <div class="col-sm-5 text-right">
                        <span>Serial Number: </span>
                    </div>
                    <div class="col-sm-7">
                        <%--{{upnHistoryVm.upnDetails.itemInstance.manufacturer.mfrCode}} - --%>
                        {{upnHistoryVm.upnDetails.itemInstance.oemNumber}}
                    </div>
                </div>

                <div class="row">
                    <div class="col-sm-5 text-right">
                        <span>Certificate Number: </span>
                    </div>
                    <div class="col-sm-7">
                        {{upnHistoryVm.upnDetails.certificateNumber}}
                    </div>
                </div>

                <div class="row">
                    <div class="col-sm-5 text-right">
                        <span>Location : </span>
                    </div>
                    <div class="col-sm-7">
                        <span>{{upnHistoryVm.upnDetails.storageLocation}}</span>
                        <span ng-if="upnHistoryVm.upnDetails.itemInstance.status == 'ISSUE'">Shop floor</span>
                    </div>
                </div>
                <div class="row">
                    <div class="col-sm-5 text-right">
                        <span>Manufacturer : </span>
                    </div>
                    <div class="col-sm-7">
                        <span>{{upnHistoryVm.upnDetails.itemInstance.manufacturer.name}}</span>
                    </div>
                </div>

                <div class="row">
                    <div class="col-sm-5 text-right">
                        <span>Manufacturer Date: </span>
                    </div>
                    <div class="col-sm-7">
                        <span>{{upnHistoryVm.upnDetails.manufacturerDate}}</span>
                    </div>
                </div>


                <div class="row"
                     ng-if="upnHistoryVm.upnDetails.itemInstance.rootCardNo != null && upnHistoryVm.upnDetails.itemInstance.rootCardNo != '' && upnHistoryVm.upnDetails.itemInstance.rootCardNo != undefined">
                    <div class="col-sm-5 text-right">
                        <span>Root Card Number : </span>
                    </div>
                    <div class="col-sm-7">
                        <span>{{upnHistoryVm.upnDetails.itemInstance.rootCardNo}}</span>
                    </div>
                </div>
            </div>
        </div>

        <div>
            <h4 ng-click="upnHistoryVm.showCodification()" style="cursor: pointer;color: blue;padding: 20px 0px;">
                <i class="fa fa-plus-square" ng-if="!upnHistoryVm.showCode"></i>
                <i class="fa fa-minus-square" ng-if="upnHistoryVm.showCode"></i>
                Codification : </h4>

            <div ng-if="upnHistoryVm.showCode" class="item-details" style="margin-left: 0px;">
                <div class="row">
                    <div class="col-sm-5 text-right">
                        <span>System : </span>
                    </div>
                    <div class="col-sm-7">
                        {{upnHistoryVm.upnDetails.system.item.itemMaster.itemCode}} -
                        {{upnHistoryVm.upnDetails.system.item.itemMaster.itemName}}
                    </div>
                </div>

                <div class="row">
                    <div class="col-sm-5 text-right">
                        <span>Section : </span>
                    </div>
                    <div class="col-sm-7">
                        <span ng-if="upnHistoryVm.upnDetails.itemInstance.section != null">
                            {{upnHistoryVm.upnDetails.section.code}} - {{upnHistoryVm.upnDetails.section.name}}
                        </span>
                        <span ng-if="upnHistoryVm.upnDetails.itemInstance.section == null">
                           0
                        </span>
                    </div>
                </div>

                <div class="row">
                    <div class="col-sm-5 text-right">
                        <span>Sub System: </span>
                    </div>
                    <div class="col-sm-7">
                        <span ng-if="upnHistoryVm.upnDetails.subSystem != null">
                            {{upnHistoryVm.upnDetails.subSystem.code}} - {{upnHistoryVm.upnDetails.subSystem.name}}
                        </span>
                        <span ng-if="upnHistoryVm.upnDetails.subSystem == null">0</span>
                    </div>
                </div>

                <div class=" row">
                    <div class="col-sm-5 text-right">
                        <span>Unit : </span>
                    </div>
                    <div class="col-sm-7">
                        <span ng-if="upnHistoryVm.upnDetails.unit != null">
                           {{upnHistoryVm.upnDetails.unit.code}} - {{upnHistoryVm.upnDetails.unit.name}}
                        </span>
                        <span ng-if="upnHistoryVm.upnDetails.unit == null">
                           00
                        </span>
                    </div>
                </div>

                <div class="row">
                    <div class="col-sm-5 text-right">
                        <span>Part : </span>
                    </div>
                    <div class="col-sm-7">
                        {{upnHistoryVm.upnDetails.bomItem.item.itemMaster.itemCode}} -
                        {{upnHistoryVm.upnDetails.bomItem.item.itemMaster.itemName}}
                    </div>
                </div>

                <div class="row">
                    <div class="col-sm-5 text-right">
                        <span>Serial Number : </span>
                    </div>
                    <div class="col-sm-7">
                        <%--{{upnHistoryVm.upnDetails.itemInstance.manufacturer.mfrCode}} - --%>
                        {{upnHistoryVm.upnDetails.itemInstance.oemNumber}}
                    </div>
                </div>
            </div>
        </div>

        <div>
            <h4>Inward Barcode : <span
                    style="font-weight: normal">{{upnHistoryVm.upnDetails.itemInstance.initialUpn}}</span></h4>

            <%--<div id="inwardItemBarcode">
                <p style="text-align:center;margin-bottom: 0px;width: 195px">
                    </p>
                <img ng-src="api/drdo/inwards/instance/barcode/{{upnHistoryVm.upnDetails.itemInstance.initialUpn}}?bust={{upnHistoryVm.barcodeBust}}"
                     alt="Barcode"
                     style="width: 200px;height: 50px;">
            </div>
            <button ng-click="upnHistoryVm.printInwardUpn('inwardItemBarcode')" style="margin-left: 75px;"
                    title="Click to print">Print
            </button>--%>

            <h4 ng-if="upnHistoryVm.upnDetails.itemInstance.status == 'ISSUE' || upnHistoryVm.upnDetails.itemInstance.status == 'FAILURE'">
                Issue Barcode : <span
                    style="font-weight: normal;">{{upnHistoryVm.upnDetails.itemInstance.upnNumber}}</span></h4>

            <%--<div ng-if="upnHistoryVm.upnDetails.itemInstance.status == 'ISSUE'" id="issueItemBarcode">
                <p style="text-align:center;margin-bottom: 0px;width: 195px">
                    {{upnHistoryVm.upnDetails.itemInstance.upnNumber}}</p>
                <img ng-src="api/drdo/inwards/instance/barcode/{{upnHistoryVm.upnDetails.itemInstance.upnNumber}}?bust={{upnHistoryVm.barcodeBust}}"
                     alt="Barcode"
                     style="width: 200px;height: 50px;">
            </div>
            <button ng-if="upnHistoryVm.upnDetails.itemInstance.status == 'ISSUE'"
                    ng-click="upnHistoryVm.printIssuedUpn('issueItemBarcode')" style="margin-left: 75px;"
                    title="Click to print">Print
            </button>--%>
        </div>
        <hr>
        <h4>History</h4>

        <div class="comments-panel">
            <ul class="timeline">
                <li ng-repeat="history in upnHistoryVm.upnDetails.statusHistories">
                    <div class="direction-r">
                        <div class="flag-wrapper" style="text-align: left !important;">
                            <span class="flag" ng-class="{'acceptColor':history.status == 'ACCEPT' || history.status == 'APPROVED',
                                     'pAccept-color':history.status == 'P_ACCEPT' || history.status == 'P_APPROVED',
                                     'submitted-color':history.status == 'STORE_SUBMITTED' || history.status == 'REVIEWED',
                                     'inventoryColor':history.status == 'INVENTORY',
                                     'verifyColor':history.status == 'VERIFIED',
                                     'returnColor':history.status == 'FAILURE' || history.status == 'REJECTED',
                                     'issueColor':history.status == 'ISSUE',
                                     'noneColor':history.status == 'NEW',
                                     'dispatchedColor':history.status == 'DISPATCH',
                                     'reviewColor':history.status == 'REVIEW'}">{{history.presentStatus}}
                            </span>
                            <a href="" title="Click to show Issue" ng-if="history.status == 'ISSUE'"
                               ng-click="upnHistoryVm.openIssue(upnHistoryVm.upnDetails.issue)" style="padding: 0 7px;">
                                Issue Number : {{upnHistoryVm.upnDetails.issue.number}}</a>
                        </div>
                        <br>
                       <span class="time" style="font-size: 14px;">
                                  Date :   {{history.timestamp}} ( <span style="color: black;font-style: italic;">{{history.user.fullName}}</span> )
                       </span>
                        <span class="time" ng-if="history.comment != null"
                              style="font-size: 14px;">
                             <span ng-if="history.status == 'P_ACCEPT' || history.status == 'REVIEW' || history.status == 'P_APPROVED' || history.status == 'REJECTED'"
                                   style="font-weight: 600">Reason</span>
                             <span ng-if="history.status == 'REVIEWED'" style="font-weight: 600">Comment</span>
                              : <span style="color: black;">{{history.comment}}</span>
                            </span>
                    </div>
                </li>
            </ul>
        </div>
    </div>
</div>