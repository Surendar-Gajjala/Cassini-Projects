<style>
    .description {
        display: run-in;
        word-wrap: break-word;
        width: 300px;
        white-space: normal !important;
        text-align: left;
    }
</style>
<div class="responsive-table" style="padding: 10px;">
    <table class="table table-striped highlight-row">
        <thead>
        <tr>
            <th>Nomenclature</th>
            <th ng-if="!selectedItemRevisionDetails.itemMaster.itemType.hasBom">Item Code</th>
            <th ng-if="!selectedItemRevisionDetails.itemMaster.itemType.hasBom">Type</th>
            <th ng-if="!selectedItemRevisionDetails.itemMaster.itemType.hasBom">UPN</th>
            <th>Status</th>
            <th>Serial Number</th>
            <th ng-if="!selectedItemRevisionDetails.itemMaster.itemType.hasBom">Quantity</th>
            <th class="oneFifty-column">Location</th>
        </tr>
        </thead>
        <tbody>
        <tr ng-if="itemInstanceVm.loading == true">
            <td colspan="25">
                        <span style="font-size: 15px;">
                            <img src="app/assets/bower_components/cassini-platform/images/loaders/loader19.gif"
                                 class="mr5">
                            <span>Loading Instances...</span>
                        </span>
            </td>
        </tr>

        <tr ng-if="itemInstanceVm.loading == false && itemInstanceVm.itemInstances.length == 0">
            <td colspan="25">No Instances</td>
        </tr>

        <tr ng-repeat="instance in itemInstanceVm.itemInstances">
            <td>
                <span ng-if="!selectedItemRevisionDetails.itemMaster.itemType.hasBom">{{instance.item.itemMaster.itemName}}</span>
                <span ng-if="selectedItemRevisionDetails.itemMaster.itemType.hasBom">{{instance.instanceName}}</span>
            </td>
            <td ng-if="!selectedItemRevisionDetails.itemMaster.itemType.hasBom">
                <span ng-if="!selectedItemRevisionDetails.itemMaster.itemType.hasBom">{{instance.item.itemMaster.itemCode}}</span>
            </td>
            <td ng-if="!selectedItemRevisionDetails.itemMaster.itemType.hasBom">
                <span ng-if="!selectedItemRevisionDetails.itemMaster.itemType.hasBom">{{instance.item.itemMaster.parentType.name}}</span>
            </td>
            <td ng-if="!selectedItemRevisionDetails.itemMaster.itemType.hasBom">
                <a href="" ng-if="!instance.item.itemMaster.hasLots" ng-click="showUpnHistory(instance,'right')"
                   title="Click to show details">
                    <span class="badge badge-secondary" style="font-size: 14px;">{{instance.upnNumber}}</span>
                </a>
                <a href="" ng-if="instance.item.itemMaster.hasLots"
                   ng-click="showLotUpnHistory(instance,'INWARD')"> title="Click to show details">
                    <span class="badge badge-secondary" style="font-size: 14px;">{{instance.upnNumber}}</span>
                </a>
            </td>
            <td style="font-size: 14px;">
                <item-instance-status object="instance"></item-instance-status>
            </td>
            <td><%--{{instance.manufacturer.mfrCode}} - --%>{{instance.oemNumber}}</td>
            <td ng-if="!selectedItemRevisionDetails.itemMaster.itemType.hasBom">
                <span class="badge badge-primary"
                      ng-if="!selectedItemRevisionDetails.itemMaster.itemType.hasBom && !instance.item.itemMaster.itemType.hasLots">1</span>
                <span class="badge badge-primary"
                      ng-if="!selectedItemRevisionDetails.itemMaster.itemType.hasBom && instance.item.itemMaster.itemType.hasLots">{{instance.lotSize}}</span>
            </td>
            <td class="oneFifty-column">{{instance.storagePath}}</td>
        </tr>
        </tbody>
    </table>
</div>