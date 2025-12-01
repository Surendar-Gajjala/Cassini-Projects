<style scoped>
    .popover-content {
        padding: 5px !important;
    }

    .popover {
        min-width: 20% !important;
        max-width: 20% !important;
        z-index: 10000 !important;
    }

    .part-width {
        display: run-in;
        word-wrap: break-word;
        width: 18% !important;
        min-width: 18% !important;
        white-space: normal !important;
        text-align: left;
    }
</style>
<div style="max-height: 150px;overflow-y: auto !important;">
    <table class="table table-striped highlight-row">
        <tbody>
        <tr ng-repeat="storage in part.storages">
            <td class="part-width">{{storage.name}}
                <span ng-if="storage.onHold">( On Hold )</span>
                <span ng-if="storage.returned">( Return )</span>
            </td>
        </tr>
        </tbody>
    </table>
</div>
