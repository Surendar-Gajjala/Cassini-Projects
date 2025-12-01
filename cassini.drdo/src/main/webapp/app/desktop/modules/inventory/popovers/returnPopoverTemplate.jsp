<style scoped>
    .popover-content {
        padding: 5px !important;
    }

    .popover {
        min-width: 450px !important;
        max-width: 450px !important;
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
        width: 100px !important;
        min-width: 100px !important;
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
        <tr ng-repeat="instance in inv.returnInstances">
            <td class="upn-width">
                <a href="" ng-click="showUpnHistory(instance,'left')" title="Click to show details">{{instance.upnNumber}}</a>
            </td>
            <td class="location-width">
                {{instance.storage.name}}
            </td>
            <td class="number-width">{{instance.oemNumber}}</td>
        </tr>
        </tbody>
    </table>
</div>
