<style scoped>
    .popover-content {
        padding: 5px !important;
    }

    .popover {
        min-width: 350px !important;
        max-width: 350px !important;
        z-index: 10000 !important;
    }

    .part-width {
        display: run-in;
        word-wrap: break-word;
        width: 260px !important;
        min-width: 260px !important;
        white-space: normal !important;
        text-align: left;
    }
</style>
<div style="max-height: 150px;overflow-y: auto !important;">
    <table class="table table-striped highlight-row">
        <tbody>
        <tr ng-repeat="storage in storageItem.returnStorages">
            <td class="part-width">{{storage.storagePath}}
            </td>
        </tr>
        </tbody>
    </table>
</div>
