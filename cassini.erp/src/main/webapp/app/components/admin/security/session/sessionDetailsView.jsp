<div class="table-responsive">
    <table class="table table-striped table-bordered">
        <thead>
        <tr>
            <th style="width: 150px; text-align: center;">Action</th>
            <th style="width: 100px; text-align: center;">Session</th>
            <th style="">Details</th>
            <th style="width: 150px; text-align: center;">User</th>
            <th style="width: 200px; text-align: center;">Timestamp</th>
        </tr>
        </thead>
        <tbody>
        <tr ng-if="history.length == 0">
            <td colspan="5">No History</td>
        </tr>

        <tr ng-repeat="item in history">

            <td style="width: 150px; text-align: center; vertical-align: middle;">
                <span>{{ item.action }}</span>
            </td>

            <td style="width: 100px; text-align: center; vertical-align: middle;">
                <span><a href="">{{ item.session.id }}</a></span>
            </td>

            <td style="vertical-align: middle;">
                <span>{{ item.details }}</span>
            </td>

            <td style="width: 150px; text-align: center; vertical-align: middle;">
                <span>{{ item.login.loginName }}</span>
            </td>

            <td style="width: 200px; text-align: center; vertical-align: middle;">
                <span>{{ item.timestamp }}</span>
            </td>
        </tr>
        </tbody>
    </table>
</div>