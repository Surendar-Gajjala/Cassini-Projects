<style scoped>
    .popover-content {
        padding: 5px !important;
    }

    .popover {
        min-width: 15% !important;
        max-width: 15% !important;
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
        <tbody>
        <tr ng-repeat="instance in inventory.itemInstances">
            <td class="part-width">
                {{instance.upnNumber}}
            </td>
        </tr>
        </tbody>
    </table>
</div>
