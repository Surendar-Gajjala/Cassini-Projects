<%--
  Created by IntelliJ IDEA.
  User: Nageshreddy
  Date: 12-06-2018
  Time: 15:43
  To change this template use File | Settings | File Templates.
--%>
<div style="padding: 20px; height: auto;">
    <div class="col-md-12" style="padding:0px; height: auto;overflow: auto;">
        <table class="table table-striped">
            <tbody>
            <tr>
                <th style="vertical-align: middle;">
                    Type
                </th>
                <th style="vertical-align: middle;">
                    Nomenclature
                </th>
                <th style="vertical-align: middle;">
                    Quantity
                </th>
                <th style="vertical-align: middle;">
                    Part Number
                </th>
            </tr>

            <tr ng-if="showReScanVm.loading == true">
                <td colspan="7">
                        <span style="font-size: 15px;">
                            <img src="app/assets/bower_components/cassini-platform/images/loaders/loader19.gif"
                                 class="mr5">Loading items...
                        </span>
                </td>
            </tr>
            <tr ng-if="showReScanVm.items.length == 0">
                <td colspan="7">No items</td>
            </tr>
            <tr ng-if="showReScanVm.items.length > 0" ng-repeat="item in showReScanVm.items">
                <td style="vertical-align: middle;">
                    {{item.item.itemType.name}}
                </td>
                <td style="vertical-align: middle;">
                    {{item.item.itemName}}
                </td>
                <td style="vertical-align: middle;">
                    {{item.quantity}}
                </td>
                <td>
                    <span ng-if="item.item.itemType.parent == 'PART'"
                          ng-repeat="partNumber in item.batchItemsIssued | filter :{show : true}"
                          ng-style="partNumber.hasFailureList?{'color':'rgb(249, 243, 243)','background':'red'}:{'color':'black'}">
                        {{partNumber.upnNumber}}
                    </span>
                    <span ng-if="item.item.itemType.storeAsLot"
                          ng-repeat="partNumber in item.imLotItemsIssueds | filter :{show : true}"
                          ng-style="partNumber.hasFailureList?{'color':'rgb(249, 243, 243)','background':'red'}:{'color':'black'}">
                        {{partNumber.upnNumber}}
                    </span>
                    <span ng-if="item.item.itemType.parent == 'PART'">
                    <input ng-repeat="partNumber in item.batchItemsIssued | filter: { show: false }"
                           class="form-control" type="text"
                           ng-if="!partNumber.hasFailureList && partNumber.replaced == null"
                           ng-style="(partNumber.hasFailureList || partNumber.valid)?{'border':'3px solid red'}:{'border-color':'rgb(186, 206, 186)'}"
                           ng-model="partNumber.scannedUpn">

                    </span>
                    <span ng-if="item.item.itemType.storeAsLot">
                    <input ng-repeat="partNumber in item.imLotItemsIssueds | filter: { show: false }"
                           class="form-control" type="text"
                           ng-if="!partNumber.hasFailureList && partNumber.replaced == null"
                           ng-style="(partNumber.hasFailureList || partNumber.valid)?{'border':'3px solid red'}:{'border-color':'rgb(186, 206, 186)'}"
                           ng-model="partNumber.scannedUpn">
                    </span>
                </td>
            </tr>
            </tbody>
        </table>
    </div>
</div>
</div>
