<style scoped>
    .popover-content {
        padding: 5px !important;
    }

    .popover {
        min-width: 500px !important;
        max-width: 500px !important;
    }

    .upn-width {
        display: run-in;
        word-wrap: break-word;
        width: 150px !important;
        min-width: 150px !important;
        white-space: normal !important;
        text-align: left;
    }

    .number-width {
        display: run-in;
        word-wrap: break-word;
        width: 150px !important;
        min-width: 150px !important;
        white-space: normal !important;
        text-align: left;
    }

    .location-width {
        display: run-in;
        word-wrap: break-word;
        width: 150px !important;
        min-width: 150px !important;
        white-space: normal !important;
        text-align: left;
    }
</style>
<div style="max-height: 240px;overflow-y: auto !important;">
    <table class="table table-striped highlight-row">
        <thead>
        <tr>
            <th class="upn-width">UPN</th>
            <th class="location-width">Serial Number</th>
            <th class="number-width">Certificate Number</th>
        </tr>
        </thead>
        <tbody>
        <tbody>
        <tr ng-if="!inv.item.item.itemMaster.itemType.hasLots" ng-repeat="instance in inv.issuedInstances">
            <td class="upn-width">
                <a href="" ng-click="showUpnHistory(instance,'left')"
                   title="Click to show details">{{instance.upnNumber}}</a>
            </td>

            <td class="location-width">
                <%--{{instance.manufacturer.mfrCode}} - --%>{{instance.oemNumber}}
            </td>
            <td class="number-width">{{instance.certificateNumber}}</td>
        </tr>

        <tr ng-if="inv.item.item.itemMaster.itemType.hasLots" ng-repeat="instance in inv.issuedLotInstances">
            <td class="part-width">
                <a href="" ng-click="showLotUpnHistory(instance,'ISSUE')"
                   title="Click to show details">{{instance.upnNumber}} ({{instance.lotQty}})</a>
            </td>

            <td class="part-width">
                <%--{{instance.itemInstance.manufacturer.mfrCode}} - --%>{{instance.itemInstance.oemNumber}}
            </td>
            <td class="part-width">{{instance.certificateNumber}}</td>
        </tr>
        </tbody>
    </table>
</div>
