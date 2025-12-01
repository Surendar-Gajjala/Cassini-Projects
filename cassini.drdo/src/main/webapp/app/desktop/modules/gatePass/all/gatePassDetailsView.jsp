<div style="overflow-y: auto;">
    <div class="responsive-table" style="padding: 10px;">
        <table class="table table-striped highlight-row">
            <thead>
            <tr>
                <th class="thirty-column">Nomenclature</th>
                <th class="tenPercent-column">Qty</th>
                <th class="forty-column">Serial Number</th>
                <th class="twenty-column">Status</th>
            </tr>
            </thead>
            <tbody>
            <tr ng-if="gatePassDetailsVm.loading == true">
                <td colspan="25">
                        <span style="font-size: 15px;">
                            <img src="app/assets/bower_components/cassini-platform/images/loaders/loader19.gif"
                                 class="mr5">
                            <span>Loading Details...</span>
                        </span>
                </td>
            </tr>

            <tr ng-if="gatePassDetailsVm.loading == false && gatePassDetailsVm.gatePassDetails.length == 0">
                <td colspan="25" ng-if="!gatePassDetailsVm.filterMode">No Details Found</td>
            </tr>

            <tr ng-repeat="detail in gatePassDetailsVm.gatePassDetails | orderBy:'bomItem.item.itemMaster.itemName'">
                <td class="thirty-column">{{detail.bomItem.item.itemMaster.itemName}}</td>
                <td class="tenPercent-column">
                    <span class="badge badge-primary" style="font-size: 16px"
                          ng-if="detail.bomItem.item.itemMaster.itemType.hasLots">{{detail.fractionalQuantity}}</span>
                    <span class="badge badge-primary" style="font-size: 16px"
                          ng-if="!detail.bomItem.item.itemMaster.itemType.hasLots">{{detail.quantity}}</span>
                </td>
                <td class="forty-column">
                    <div ng-repeat="instance in detail.inwardItemInstances" style="padding: 5px 0;">
                        {{instance.item.oemNumber}}
                    </div>
                </td>
                <td class="twenty-column">
                    <div ng-repeat="instance in detail.inwardItemInstances" style="padding: 5px 0;">
                        <item-instance-status object="instance.item"></item-instance-status>
                    </div>
                </td>
            </tr>
            </tbody>
        </table>
    </div>
</div>