<%--
  Created by IntelliJ IDEA.
  User: Nageshreddy
  Date: 29-01-2019
  Time: 11:35
  To change this template use File | Settings | File Templates.
--%>
<style scoped>
    .popover-content {
        padding: 5px !important;
    }

    .popover {
        min-width: 450px !important;
        max-width: 450px !important;
    }

    .part-width {
        display: run-in;
        word-wrap: break-word;
        width: 11% !important;
        min-width: 11% !important;
        white-space: normal !important;
        text-align: left;
    }
</style>
<div style="max-height: 240px;overflow-y: auto !important;">
    <table class="table table-striped highlight-row">
        <thead>
        <tr>
            <th class="upn-width">UPN</th>
            <th class="location-width">Storage</th>
            <th class="number-width">Serial Number</th>
        </tr>
        </thead>
        <tbody>
        <tr ng-repeat="instance in inv.onHoldInstances">
            <td class="upn-width">
                <a href="" ng-if="!inv.item.item.itemMaster.itemType.hasLots"
                   ng-click="showUpnHistory(instance,'left')"
                   title="Click to show details">{{instance.upnNumber}}</a>
                <a href="" ng-if="inv.item.item.itemMaster.itemType.hasLots"
                   ng-click="showLotUpnHistory(instance,'INWARD')"
                   title="Click to show details">{{instance.upnNumber}}
                    ( {{instance.lotSize}} )
                </a>
            </td>
            <td class="location-width">{{instance.storage.name}}</td>
            <td class="number-width">{{instance.oemNumber}}</td>
        </tr>
        </tbody>
    </table>
</div>
