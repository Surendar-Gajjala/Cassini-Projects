<%--
  Created by IntelliJ IDEA.
  User: Nageshreddy
  Date:02-01-2019
  Time: 18:43
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
                    Code
                </th>
                <th style="vertical-align: middle;">
                    Nomenclature
                </th>
                <th style="vertical-align: middle;">
                    Part Number
                </th>
            </tr>

            <tr ng-if="scanVm.loading == true">
                <td colspan="7">
                        <span style="font-size: 15px;">
                            <img src="app/assets/bower_components/cassini-platform/images/loaders/loader19.gif"
                                 class="mr5">Loading items...
                        </span>
                </td>
            </tr>
            <tr ng-if="scanVm.items.length == 0">
                <td colspan="7">No items</td>
            </tr>
            <tr ng-if="scanVm.items.length > 0" ng-repeat="item in scanVm.items">
                <td style="vertical-align: middle;">
                    {{item.item.itemType.name}}
                </td>
                <td style="vertical-align: middle;">
                    {{item.item.itemNumber}}
                </td>
                <td style="vertical-align: middle;">
                    {{item.item.itemName}}
                </td>
                <td>
                    <input ng-repeat="partNumber in item.allocationList" class="form-control" type="text"
                           ng-style="partNumber.valid?{'border-color':'red'}:{'border-color':'rgb(186, 206, 186)'}"
                           ng-model="partNumber.scannedUpn">
                </td>
            </tr>
            </tbody>
        </table>
    </div>
</div>
</div>
