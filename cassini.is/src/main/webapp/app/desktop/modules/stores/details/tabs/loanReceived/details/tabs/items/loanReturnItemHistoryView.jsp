<div style="max-height: 240px;overflow-y: auto !important;">
    <table class="table table-striped">
        <thead>
        <tr>
            <th style="width: 150px; text-align: left">Date</th>
            <th style="width: 150px; text-align: left">Quantity</th>
        </tr>
        </thead>
        <tbody>
        <tr data-ng-repeat="row in item.itemHistory">
            <td style="vertical-align: middle;">
                <span>{{row.date}}</span>
            </td>
            <td style="vertical-align: middle;">
                <span>{{row.quantity}}</span>
            </td>
        </tr>
        </tbody>
    </table>
</div>
