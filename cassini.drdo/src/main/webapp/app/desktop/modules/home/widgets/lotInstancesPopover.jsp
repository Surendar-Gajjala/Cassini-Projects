<%--
  Created by IntelliJ IDEA.
  User: Nageshreddy
  Date: 18-02-2019
  Time: 18:41
  To change this template use File | Settings | File Templates.
--%>
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
            <th class="number-width">Present Status</th>
            <th class="location-width">Actions</th>
        </tr>
        </thead>
        <tbody>
        <tr ng-repeat="lotInstance in failureItem.lotInstanceList">
            <td class="upn-width">
                <a href="" ng-click="showUpnHistory(lotInstance,'left')" title="Click to show details">{{lotInstance.upnNumber}}
                    ({{lotInstance.lotQty}})</a>
            </td>
            <td class="upn-width">
                <span>{{lotInstance.presentStatus}}</span>
            </td>
            <%--<td class="number-width">
              <span>{{instance.itemInstance.certificateNumber}}</span>
            </td>--%>
            <td class="location-width">
                <button title="Show failure report for Lot"
                        class="btn btn-xs" style="background-color: #cf34c3"
                        ng-click="homeVm.showFailureListForLot(lotInstance, failureItem)">
                    <i class="fa fa-list"></i>
                </button>
            </td>
        </tr>
        </tbody>
    </table>
</div>
