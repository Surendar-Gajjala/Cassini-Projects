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
            <h4 ng-click="lotUpnHistoryVm.showBasicInformation()"
                style="cursor: pointer;color: #313b8d;padding: 20px 0px;">
                <i class="fa fa-plus-square" ng-if="!lotUpnHistoryVm.showInfo"></i>
                <i class="fa fa-minus-square" ng-if="lotUpnHistoryVm.showInfo"></i>
                Basic Information : </h4>

            <div ng-if="lotUpnHistoryVm.showInfo" class="item-details" style="margin-left: 0px;">
                <div class="row">
                    <div class="col-sm-5 text-right">
                        <span>Nomenclature: </span>
                    </div>
                    <div class="col-sm-7">
                        {{lotUpnHistoryVm.upnDetails.bomItem.item.itemMaster.itemName}}
                    </div>
                </div>

                <div class="row">
                    <div class="col-sm-5 text-right">
                        <span>Item Code: </span>
                    </div>
                    <div class="col-sm-7">
                        {{lotUpnHistoryVm.upnDetails.bomItem.item.itemMaster.itemCode}}
                    </div>
                </div>

                <div class="row">
                    <div class="col-sm-5 text-right">
                        <span>Code: </span>
                    </div>
                    <div class="col-sm-7">
                        {{lotUpnHistoryVm.upnDetails.bomItem.hierarchicalCode}}
                    </div>
                </div>

                <div class="row">
                    <div class="col-sm-5 text-right">
                        <span>Inward Number: </span>
                    </div>
                    <div class="col-sm-7">
                        <a href="" ng-click="lotUpnHistoryVm.showInward(lotUpnHistoryVm.upnDetails.inwards[0])"><span
                                ng-if="lotUpnHistoryVm.upnDetails.inwards.length == 1">{{lotUpnHistoryVm.upnDetails.inwards[0].number}}
                            <span>( {{lotUpnHistoryVm.upnDetails.inwards[0].inwardItem.fractionalQuantity}} )</span>
                        </span>
                        </a>
                        <span ng-if="lotUpnHistoryVm.upnDetails.inwards.length > 1">
                            <li ng-repeat="inward in lotUpnHistoryVm.upnDetails.inwards">
                                <a href="" ng-click="lotUpnHistoryVm.showInward(inward)">
                                    {{inward.number}} <span>( {{inward.inwardItem.fractionalQuantity}} )</span></a>
                            </li>
                        </span>
                    </div>
                </div>

                <div class="row">
                    <div class="col-sm-5 text-right">
                        <span>Gate Pass: </span>
                    </div>
                    <div class="col-sm-7">
                        <%--<span>{{lotUpnHistoryVm.upnDetails.inwards[0].gatePass.gatePass.name}}</span>--%>
                        <a ng-if="lotUpnHistoryVm.upnDetails.inwards.length == 1" href=""
                           ng-click="lotUpnHistoryVm.downloadGatePass(lotUpnHistoryVm.upnDetails.inwards[0].gatePass)"
                           title="Click to download Gate Pass">{{lotUpnHistoryVm.upnDetails.inwards[0].gatePass.gatePass.name}}
                        </a>
                        <span ng-if="lotUpnHistoryVm.upnDetails.inwards.length > 1">
                            <li ng-repeat="inward in lotUpnHistoryVm.upnDetails.inwards">
                                <a href="" ng-click="lotUpnHistoryVm.downloadGatePass(inward.gatePass)"
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
                        <%--{{lotUpnHistoryVm.upnDetails.itemInstance.manufacturer.mfrCode}} ---%>
                            {{lotUpnHistoryVm.upnDetails.itemInstance.oemNumber}}
                    </div>
                </div>

                <div class="row" ng-if="lotUpnHistoryVm.mode == 'INWARD'">
                    <div class="col-sm-5 text-right">
                        <span>Location : </span>
                    </div>
                    <div class="col-sm-7">
                        {{lotUpnHistoryVm.upnDetails.storageLocation}}
                    </div>
                </div>

                <div class="row" ng-if="lotUpnHistoryVm.mode == 'INWARD'">
                    <div class="col-sm-5 text-right">
                        <span>Available Quantity : </span>
                    </div>
                    <div class="col-sm-7">
                        {{lotUpnHistoryVm.upnDetails.availableLotQuantity}}
                    </div>
                </div>
                <div class="row" ng-if="lotUpnHistoryVm.mode == 'ISSUE'">
                    <div class="col-sm-5 text-right">
                        <span>Issued Quantity : </span>
                    </div>
                    <div class="col-sm-7">
                        {{lotUpnHistoryVm.itemInstance.lotQty}}
                    </div>
                </div>

                <div class="row">
                    <div class="col-sm-5 text-right">
                        <span>Manufacturer : </span>
                    </div>
                    <div class="col-sm-7">
                        <span>{{lotUpnHistoryVm.upnDetails.itemInstance.manufacturer.name}}</span>
                    </div>
                </div>

                <div class="row">
                    <div class="col-sm-5 text-right">
                        <span>Manufacturer Date: </span>
                    </div>
                    <div class="col-sm-7">
                        <span>{{lotUpnHistoryVm.upnDetails.manufacturerDate}}</span>
                    </div>
                </div>
            </div>
        </div>

        <div>
            <h4 ng-click="lotUpnHistoryVm.showCodification()" style="cursor: pointer;color: #313b8d;padding: 20px 0px;">
                <i class="fa fa-plus-square" ng-if="!lotUpnHistoryVm.showCode"></i>
                <i class="fa fa-minus-square" ng-if="lotUpnHistoryVm.showCode"></i>
                Codification : </h4>

            <div ng-if="lotUpnHistoryVm.showCode" class="item-details" style="margin-left: 0px;">
                <div class="row">
                    <div class="col-sm-5 text-right">
                        <span>System : </span>
                    </div>
                    <div class="col-sm-7">
                        {{lotUpnHistoryVm.upnDetails.system.item.itemMaster.itemCode}} -
                        {{lotUpnHistoryVm.upnDetails.system.item.itemMaster.itemName}}
                    </div>
                </div>

                <div class="row" ng-if="lotUpnHistoryVm.mode == 'ISSUE'">
                    <div class="col-sm-5 text-right">
                        <span>Missile : </span>
                    </div>
                    <div class="col-sm-7">
                        {{lotUpnHistoryVm.upnDetails.missile.item.instanceName}}
                    </div>
                </div>

                <div class="row">
                    <div class="col-sm-5 text-right">
                        <span>Section : </span>
                    </div>
                    <div class="col-sm-7">
                        <span ng-if="lotUpnHistoryVm.upnDetails.itemInstance.section != null">
                            {{lotUpnHistoryVm.upnDetails.section.code}} - {{lotUpnHistoryVm.upnDetails.section.name}}
                        </span>
                        <span ng-if="lotUpnHistoryVm.upnDetails.itemInstance.section == null">
                           0
                        </span>
                    </div>
                </div>

                <div class="row">
                    <div class="col-sm-5 text-right">
                        <span>Sub System: </span>
                    </div>
                    <div class="col-sm-7">
                        <span ng-if="lotUpnHistoryVm.upnDetails.subSystem != null">
                            {{lotUpnHistoryVm.upnDetails.subSystem.code}} - {{lotUpnHistoryVm.upnDetails.subSystem.name}}
                        </span>
                        <span ng-if="lotUpnHistoryVm.upnDetails.subSystem == null">
                           0
                        </span>
                    </div>
                </div>

                <div class="row">
                    <div class="col-sm-5 text-right">
                        <span>Unit : </span>
                    </div>
                    <div class="col-sm-7">
                        <span ng-if="lotUpnHistoryVm.upnDetails.unit != null">
                            {{lotUpnHistoryVm.upnDetails.unit.code}} - {{lotUpnHistoryVm.upnDetails.unit.name}}
                        </span>
                        <span ng-if="lotUpnHistoryVm.upnDetails.unit == null">
                           00
                        </span>
                    </div>
                </div>

                <div class="row">
                    <div class="col-sm-5 text-right">
                        <span>Part : </span>
                    </div>
                    <div class="col-sm-7">
                        {{lotUpnHistoryVm.upnDetails.bomItem.item.itemMaster.itemCode}} -
                        {{lotUpnHistoryVm.upnDetails.bomItem.item.itemMaster.itemName}}
                    </div>
                </div>

                <div class="row">
                    <div class="col-sm-5 text-right">
                        <span>Serial Number : </span>
                    </div>
                    <div class="col-sm-7">
                        <%--{{lotUpnHistoryVm.upnDetails.itemInstance.manufacturer.mfrCode}} - --%>
                        {{lotUpnHistoryVm.upnDetails.itemInstance.oemNumber}}
                    </div>
                </div>
            </div>
        </div>

        <div ng-if="lotUpnHistoryVm.mode == 'INWARD'">
            <h4 ng-click="lotUpnHistoryVm.showLotIssues()"
                ng-if="lotUpnHistoryVm.upnDetails.lotInstances.length > 0"
                style="cursor: pointer;color: #313b8d;padding: 20px 0px;">
                <i class="fa fa-plus-square" ng-if="!lotUpnHistoryVm.showIssues"></i>
                <i class="fa fa-minus-square" ng-if="lotUpnHistoryVm.showIssues"></i>
                Issued Lots :
            </h4>

            <div ng-show="lotUpnHistoryVm.showIssues" style="background: #D9E2E4;">
                <div ng-repeat="lotInstance in lotUpnHistoryVm.upnDetails.lotInstances" style="padding: 10px 5px;">
                    <h4 style="color: green;">{{lotInstance.upnNumber}} :
                        {{lotUpnHistoryVm.upnDetails.itemInstance.oemNumber}} / {{lotInstance.sequence}} <span
                                style="font-style: italic;color: black;font-size: 16px !important;"> ( Quantity : {{lotInstance.lotQty}} )</span>
                    </h4>

                    <div>
                        <ul class="timeline">
                            <li ng-repeat="history in lotInstance.instanceHistories">
                                <div class="direction-r">
                                    <div class="flag-wrapper" style="text-align: left !important;">
                                    <span class="flag" ng-class="{'acceptColor':history.status == 'ISSUE',
                                                                  'returnColor': history.status == 'FAILURE'}">{{history.status}}
                                    </span>
                                    <span class="time-wrapper">
                                        <span class="time" style="font-size: 14px;">
                                            {{history.timestamp}} ( <span style="color: black;font-style: italic;">{{history.user.fullName}}</span> )
                                        </span>
                                    </span>
                                        <br>
                                        <a href="" title="Click to show Issue" ng-if="history.status == 'ISSUE'"
                                           ng-click="lotUpnHistoryVm.openIssue(lotInstance.issue)"
                                           style="padding: 0 7px;">
                                            Issue Number : {{lotInstance.issue.number}}</a>
                                    </div>
                                </div>
                            </li>
                        </ul>
                    </div>
                </div>
            </div>
        </div>

        <div>
            <h4>Inward Barcode : <span style="font-weight: normal">{{lotUpnHistoryVm.upnDetails.itemInstance.initialUpn}}</span>
            </h4>

            <%--<div id="inwardItemBarcode">
                <p style="text-align:center;margin-bottom: 0px;width: 195px">
                    {{lotUpnHistoryVm.upnDetails.itemInstance.initialUpn}}</p>
                <img ng-src="api/drdo/inwards/instance/barcode/{{lotUpnHistoryVm.upnDetails.itemInstance.initialUpn}}?bust={{lotUpnHistoryVm.barcodeBust}}"
                     alt="Barcode"
                     style="width: 200px;height: 50px;">
            </div>
            <button ng-click="lotUpnHistoryVm.printInwardUpn('inwardItemBarcode')" style="margin-left: 75px;"
                    title="Click to print">Print
            </button>--%>

            <h4 ng-if="lotUpnHistoryVm.mode == 'ISSUE'">
                Issue Barcode : <span style="font-weight: normal">{{lotUpnHistoryVm.itemInstance.upnNumber}}</span></h4>

            <%-- <div id="issueItemBarcode" ng-if="lotUpnHistoryVm.mode == 'ISSUE'">
                 <p style="text-align:center;margin-bottom: 0px;width: 195px">
                     {{lotUpnHistoryVm.itemInstance.upnNumber}}</p>
                 <img ng-src="api/drdo/inwards/instance/barcode/{{lotUpnHistoryVm.itemInstance.upnNumber}}?bust={{lotUpnHistoryVm.barcodeBust}}"
                      alt="Barcode"
                      style="width: 200px;height: 50px;">
             </div>
             <button ng-if="lotUpnHistoryVm.mode == 'ISSUE'"
                     ng-click="lotUpnHistoryVm.printIssuedUpn('issueItemBarcode')" style="margin-left: 75px;"
                     title="Click to print">Print
             </button>--%>
        </div>
        <div>

        </div>
        <hr>
        <h4>{{lotUpnHistoryVm.itemInstance.upnNumber}} History</h4>

        <div class="comments-panel">
            <ul class="timeline">
                <li ng-if="lotUpnHistoryVm.mode == 'ISSUE'"
                    ng-repeat="history in lotUpnHistoryVm.upnDetails.lotHistories">
                    <div class="direction-r">
                        <div class="flag-wrapper" style="text-align: left !important;">
                            <span class="flag" ng-class="{'acceptColor':history.status == 'ISSUE',
                                                          'returnColor': history.status == 'FAILURE'}">{{history.status}}
                            </span>
                        </div>
                        <br>
                        <span class="time" style="font-size: 14px;">
                            Date : {{history.timestamp}} ( <span style="color: black;font-style: italic;">{{history.user.fullName}}</span> )
                        </span>
                        <br>
                        <a href="" title="Click to show Issue" ng-if="history.status == 'ISSUE'"
                           ng-click="lotUpnHistoryVm.openIssue(lotUpnHistoryVm.upnDetails.issue)"
                           style="padding: 0 7px;">
                            Issue Number : {{lotUpnHistoryVm.upnDetails.issue.number}}</a>
                    </div>
                </li>
                <li ng-repeat="history in lotUpnHistoryVm.upnDetails.statusHistories">
                    <div class="direction-r">
                        <div class="flag-wrapper" style="text-align: left !important;">
                            <span class="flag" ng-class="{'acceptColor':history.status == 'ACCEPT',
                                     'pAccept-color':history.status == 'P_ACCEPT',
                                     'submitted-color':history.status == 'STORE_SUBMITTED' || history.status == 'REVIEWED',
                                     'inventoryColor':history.status == 'INVENTORY',
                                     'verifyColor':history.status == 'VERIFIED',
                                     'returnColor':history.status == 'REJECTED' || history.status == 'FAILURE',
                                     'issueColor':history.status == 'ISSUE',
                                     'noneColor':history.status == 'NEW',
                                     'dispatchedColor':history.status == 'DISPATCH',
                                     'reviewColor':history.status == 'REVIEW'}">{{history.status}}</span>
                        </div>
                        <br>
                        <span class="time" style="font-size: 14px;">
                                  Date :  {{history.timestamp}} ( <span style="color: black;font-style: italic;">{{history.user.fullName}}</span> )
                        </span>
                        <span class="time" ng-if="history.comment != null"
                              style="font-size: 14px;">
                             <span ng-if="history.status == 'P_ACCEPT' || history.status == 'REVIEW' || history.status == 'REJECTED'">Reason</span>
                             <span ng-if="history.status == 'REVIEWED'">Comment</span>
                              : <span style="color: black;">{{history.comment}}</span>
                            </span>
                    </div>
                </li>
            </ul>
        </div>
    </div>
</div>