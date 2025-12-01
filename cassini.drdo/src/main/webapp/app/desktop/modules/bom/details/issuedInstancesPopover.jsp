<style scoped>
    .popover-content {
        padding: 5px !important;
    }

    .popover {
        min-width: 600px !important;
        max-width: 600px !important;
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
            <th class="upn-width">Serial Number</th>
            <th class="number-width">Certificate Number</th>
            <th class="location-width">Actions</th>
        </tr>
        </thead>
        <tbody>
        <tr ng-if="!item.item.itemMaster.itemType.hasLots" ng-repeat="instance in item.issuedInstances">
            <td class="upn-width">
                <a href="" ng-click="showUpnHistory(instance,'left')" title="Click to show details">{{instance.upnNumber}}</a>
            </td>
            <td class="upn-width">
                <span><%--{{instance.manufacturer.mfrCode}} - --%>{{instance.oemNumber}}</span>
            </td>
            <td class="number-width">
                <span>{{instance.certificateNumber}}</span>
            </td>
            <td class="location-width">
                <button ng-if="instance.hasFailed == false && item.bomItemType == 'PART' && !item.item.itemMaster.itemType.hasLots"
                        title="Create failure report for Part"
                        class="btn btn-xs btn-danger" style="background-color: #ca181d"
                        ng-click="bomDetailsVm.createFailureList(item, instance)">
                    <i class="fa fa-list"></i>
                </button>
                <button ng-if="instance.hasFailed == false && item.bomItemType == 'PART' && item.item.itemMaster.itemType.hasLots"
                        title="Create failure report for Lot"
                        class="btn btn-xs btn-danger" style="background-color: #ca181d"
                        ng-click="bomDetailsVm.createFailureList(item, instance)">
                    <i class="fa fa-list"></i>
                </button>
                <button ng-if="instance.hasFailed == true && item.bomItemType == 'PART' && !item.item.itemMaster.itemType.hasLots"
                        title="Show failure report for Part"
                        class="btn btn-xs" style="background-color: #cf34c3"
                        ng-click="bomDetailsVm.showFailureList(item, instance)">
                    <i class="fa fa-list"></i>
                </button>
                <button ng-if="instance.hasFailed == true && item.bomItemType == 'PART' && item.item.itemMaster.itemType.hasLots"
                        title="Show failure report for Lot"
                        class="btn btn-xs" style="background-color: #cf34c3"
                        ng-click="bomDetailsVm.showFailureList(item, instance)">
                    <i class="fa fa-list"></i>
                </button>
            </td>
        </tr>
        <tr ng-if="item.item.itemMaster.itemType.hasLots" ng-repeat="instance in item.lotInstances">
            <td class="upn-width">
                <a href="" ng-click="showLotUpnHistory(instance,'ISSUE')" title="Click to show details">{{instance.upnNumber}}
                    ({{instance.lotQty}})</a>
            </td>
            <td class="upn-width">
                <span><%--{{instance.itemInstance.manufacturer.mfrCode}} - --%>{{instance.itemInstance.oemNumber}}</span>
            </td>
            <td class="number-width">
                <span>{{instance.itemInstance.certificateNumber}}</span>
            </td>
            <td class="location-width">
                <button ng-if="instance.hasFailed == false && item.bomItemType == 'PART' && !item.item.itemMaster.itemType.hasLots"
                        title="Create failure report for Part"
                        class="btn btn-xs btn-danger" style="background-color: #ca181d"
                        ng-click="bomDetailsVm.createFailureList(item, instance)">
                    <i class="fa fa-list"></i>
                </button>
                <button ng-if="instance.hasFailed == false && item.bomItemType == 'PART' && item.item.itemMaster.itemType.hasLots"
                        title="Create failure report for Lot"
                        class="btn btn-xs btn-danger" style="background-color: #ca181d"
                        ng-click="bomDetailsVm.createFailureList(item, instance)">
                    <i class="fa fa-list"></i>
                </button>
                <button ng-if="instance.hasFailed == true && item.bomItemType == 'PART' && !item.item.itemMaster.itemType.hasLots"
                        title="Show failure report for Part"
                        class="btn btn-xs" style="background-color: #cf34c3"
                        ng-click="bomDetailsVm.showFailureList(item, instance)">
                    <i class="fa fa-list"></i>
                </button>
                <button ng-if="instance.hasFailed == true && item.bomItemType == 'PART' && item.item.itemMaster.itemType.hasLots"
                        title="Show failure report for Lot"
                        class="btn btn-xs" style="background-color: #cf34c3"
                        ng-click="bomDetailsVm.showFailureList(item, instance)">
                    <i class="fa fa-list"></i>
                </button>
                <button ng-if="instance.hasFailed == false && item.bomItemType == 'PART' && !item.item.itemMaster.itemType.hasLots"
                        title="Accept Checklist for Part"
                        class="btn btn-xs btn-success" style="background-color: #ca181d"
                        ng-click="bomDetailsVm.acceptChecklist(item, instance)">
                    <i class="fa fa-check"></i>
                </button>
                <button ng-if="instance.hasFailed == false && item.bomItemType == 'PART' && item.item.itemMaster.itemType.hasLots"
                        title="Accept Checklist for Lot"
                        class="btn btn-xs btn-success" style="background-color: #ca181d"
                        ng-click="bomDetailsVm.acceptChecklist(item, instance)">
                    <i class="fa fa-check"></i>
                </button>
            </td>
        </tr>
        </tbody>
    </table>
</div>
