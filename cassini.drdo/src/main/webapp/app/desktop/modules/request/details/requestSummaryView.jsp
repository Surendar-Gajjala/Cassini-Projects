<div class="view-container" fitcontent>
    <div class="view-toolbar">
        <button class="btn btn-xs" ng-click="requestSummaryVm.back()">Back</button>
        <button class="btn btn-xs" ng-click="requestSummaryVm.printRequestSummary()">
            <i class="fa fa-print"></i>
        </button>
    </div>
    <div class="view-content no-padding" style="overflow-y: auto;">
        <div class="responsive-table" style="padding: 10px;">
            <table class="table table-striped highlight-row" id="requestSummary">
                <thead>
                <tr>
                    <th>Nomenclature</th>
                    <th>Type</th>
                    <th style="text-align: center">Requested Qty</th>
                    <th style="text-align: center">BDL</th>
                    <th style="text-align: center">CAS</th>
                    <th style="text-align: center">Store</th>
                    <th style="text-align: center">BDL QC</th>
                    <th style="text-align: center">BDL PPC</th>
                    <th style="text-align: center">Rejected</th>
                </tr>
                </thead>
                <tbody>
                <tr ng-if="requestSummaryVm.loading == true">
                    <td colspan="25">
                        <span style="font-size: 15px;">
                            <img src="app/assets/bower_components/cassini-platform/images/loaders/loader19.gif"
                                 class="mr5">
                            <span>Loading Request Summary...</span>
                        </span>
                    </td>
                </tr>

                <tr ng-if="requestSummaryVm.loading == false && requestSummaryVm.requestSummary.length == 0">
                    <td colspan="25">
                        <span>No Request Items</span>
                    </td>
                </tr>

                <tr ng-repeat="summary in requestSummaryVm.requestSummary">
                    <td>{{summary.itemName}}</td>
                    <td>{{summary.type}}</td>
                    <td style="text-align: center">
                        <span class="badge badge-info" style="font-size: 14px"
                              ng-show="summary.requestedQty > 0">{{summary.requestedQty}}</span>
                    </td>
                    <td style="text-align: center">
                        <span class="badge badge-primary" style="font-size: 14px"
                              ng-show="summary.acceptedQty > 0">{{summary.acceptedQty}}</span>
                    </td>
                    <td style="text-align: center">
                        <span class="badge badge-secondary" style="font-size: 14px"
                              ng-show="summary.approvedQty > 0">{{summary.approvedQty}}</span>
                    </td>
                    <td style="text-align: center">
                        <span class="badge badge-primary" style="font-size: 14px" ng-show="summary.issuedQty > 0">{{summary.issuedQty}}</span>
                    </td>
                    <td style="text-align: center">
                        <span class="badge badge-warning" style="font-size: 14px"
                              ng-show="summary.qcApprovedQty > 0">{{summary.qcApprovedQty}}</span>
                    </td>
                    <td style="text-align: center">
                        <span class="badge badge-success" style="font-size: 14px"
                              ng-show="summary.receivedQty > 0">{{summary.receivedQty}}</span>
                    </td>
                    <td style="text-align: center">
                        <span class="badge badge-danger" style="font-size: 14px"
                              ng-show="summary.rejectedQty > 0">{{summary.rejectedQty}}</span>
                    </td>
                </tr>
                </tbody>
            </table>
        </div>
    </div>
</div>