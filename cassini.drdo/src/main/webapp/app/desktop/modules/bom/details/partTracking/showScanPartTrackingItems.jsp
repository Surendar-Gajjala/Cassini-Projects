<%--
  Created by IntelliJ IDEA.
  User: Nageshreddy
  Date: 21-05-2018
  Time: 15:43
  To change this template use File | Settings | File Templates.
--%>
<div style="padding: 20px; height: auto;">
    <div class="col-md-12" style="padding:0px; height: auto;overflow: auto;">
        <table class="table table-striped">
            <tbody>
            <tr>
                <th style="vertical-align: middle;">
                    Part Number
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

            <tr ng-if="showScanVm.loading == true">
                <td colspan="7">
                        <span style="font-size: 15px;">
                            <img src="app/assets/bower_components/cassini-platform/images/loaders/loader19.gif"
                                 class="mr5">Loading items...
                        </span>
                </td>
            </tr>
            <tr ng-if="showScanVm.items.length == 0">
                <td colspan="7">No items</td>
            </tr>
            <tr ng-if="showScanVm.items.length > 0" ng-repeat="item in showScanVm.items">
                <td style="vertical-align: middle;">
                    {{item.item.itemMaster.itemNumber}}
                </td>
                <td style="vertical-align: middle;">
                    {{item.item.itemMaster.itemName}}
                </td>
                <td style="vertical-align: middle;">
                    {{item.quantity}}
                </td>
                <td>
                    <span ng-repeat="partNumber in item.issuedInstances" ng-if="!item.item.itemMaster.itemType.hasLots"
                          class="form-control" type="text">{{partNumber.scannedUpn}}</span>
                    <span ng-if="item.item.itemMaster.itemType.hasLots"
                          ng-repeat="partNumber in item.issuedInstances | filter: { hasFailureList: false }"
                          class="form-control" type="text"
                    <%--ng-style="(partNumber.hasFailureList || partNumber.valid)?{'border':'3px solid red'}:{'border-color':'rgb(186, 206, 186)'}"--%>
                          ng-model="partNumber.scannedUpn">
                </td>
            </tr>
            </tbody>
        </table>
    </div>
</div>
</div>
