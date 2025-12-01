<style scoped>
    .popover-content {
        padding: 5px !important;
    }

    .popover {
        min-width: 150px !important;
        max-width: 200px !important;
    }

    .upn-width {
        display: run-in;
        word-wrap: break-word;
        width: 50px !important;
        min-width: 50px !important;
        white-space: normal !important;
        text-align: left;
    }

    .location-width {
        display: run-in;
        word-wrap: break-word;
        width: 30px !important;
        min-width: 30px !important;
        white-space: normal !important;
        text-align: left;
    }
</style>
<div style="max-height: 240px;overflow-y: auto !important;">
    <table class="table table-striped highlight-row">
        <thead>
        <tr>
            <th class="upn-width">Instance</th>
            <th class="location-width">Qty</th>
        </tr>
        </thead>
        <tbody>
        <tr ng-repeat="missile in planningVm.selectedMissiles"
            ng-if="(item.listMap[''+ missile.id].allocateQty - item.listMap[''+ missile.id].issuedQty) > 0">
            <td class="upn-width">
                {{missile.instanceName}}
            </td>
            <td class="location-width">
            <span ng-show="item.item.bomItemType == 'PART'"
                  href=""
                  ng-class="{'disabled-row': item[missile.id].allocateQty == 0.0}"
                  title="{{item.listMap[missile.id].allocateQty >= 0.0 ? 'Allocated':'No allocations'}}"
                  class="sm badge badge-warning" style="font-size: 14px;"
                  ng-click="planningVm.showBatchNumbers(item,missile)">{{(item.listMap[""+ missile.id].allocateQty - item.listMap[""+ missile.id].issuedQty)}}
            </span></td>
        </tr>
        </tbody>
    </table>
</div>
