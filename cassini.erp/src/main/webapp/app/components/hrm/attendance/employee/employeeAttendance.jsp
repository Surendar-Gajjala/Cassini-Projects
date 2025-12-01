<div class="row">
    <div class="col-md-12 mb20">
        <button class="btn btn-primary mr10" data-ng-click="navigate();"><i class="fa fa-chevron-left mr10"></i> Back</button>
    </div>
</div>
<div class="row" style="margin: 4px;">
    <div class="colm-md-12">
        <span ng-show="!loading && attendance.length == 0">There are no records</span>
        <span ng-show="loading">
            <img src="app/assets/images/loaders/loader19.gif" class="mr5">Loading ...
        </span>
    </div>
</div>

<div ng-hide="loading || attendance.length == 0" class="table-responsive">
    <table class="table table-striped mb20">
        <thead>
        <tr>
            <th>Date</th>
            <th>IN</th>
            <th>OUT</th>
            <th>Status</th>
        </tr>
        </thead>

        <tbody>
            <tr ng-repeat="day in attendance">
                <td>{{day.date}}</td>
                <td>{{day.inTime | time}}</td>
                <td>{{day.outTime | time}}</td>
                <td ng-style="day.status === 'A' && {'color': 'red'}">{{day.status}}</td>
            </tr>
        </tbody>
    </table>
</div>