<div style="max-height: 240px;overflow-y: auto !important;">
    <table class="table table-striped">
        <thead>
        <tr>
            <th style="width: 150px; text-align: left">Work Order Number</th>
            <th style="width: 150px; text-align: left">Created Date</th>
        </tr>
        </thead>
        <tbody>
        <tr data-ng-repeat="row in contractor.workOrders">
            <td style="vertical-align: middle;">
                <span>
                <a href="" ng-click="workOrderDetails(row)">{{row.number}}</a>
                    </span>
            </td>
            <td style="vertical-align: middle;">
                <span>{{row.createdDate}}</span>
            </td>
        </tr>
        </tbody>
    </table>
</div>
