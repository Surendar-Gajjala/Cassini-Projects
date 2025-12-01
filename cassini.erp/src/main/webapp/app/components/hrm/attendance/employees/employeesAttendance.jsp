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

<table ng-hide="loading || attendance.length == 0" class="table table-bordered" style="overflow-x: scroll;width: 1245px;max-width: 1245px;display: table-cell;">
    <thead>
    <tr>
        <th colspan="2">Emp Num</th>
        <th colspan="2">
            <div class="row" style="padding: 0 0 15px 0;">
                <div class="col-sm-12">Emp Name</div>
            </div>
        </th>
        <th colspan="2" ng-repeat-start="days in attendance[0].attendance">
            <div class="row" style="text-align: center;">
                <div class="col-sm-12 attendance-border">{{days.date}}</div>
                <div class="col-sm-6">IN</div>
                <div class="col-sm-6">OUT</div>
            </div>
        </th>
        <th ng-repeat-end style="display: none;"></th>
    </tr>
    </thead>
    <tbody>
    <tr ng-repeat-start="days in attendance">
        <td colspan="2"><a href="" data-ng-click="employeeDetails(days);">{{days.empNumber}}</a></td>
        <td colspan="2"><div class="row" style="width: 150px;padding-left: 10px;"><div class="colm-sm-12">{{days.empName}}</div></div></td>
        <td colspan="2" ng-repeat-start="day in days.attendance" ng-class="{'attendance-absent': day.status == 'A'}">
            <div class="row" style="width: 200px;">
                <div class="col-sm-6">{{day.inTime | time}}</div>
                <div class="col-sm-6">{{day.outTime | time}}</div>
            </div>
        </td>
        <td ng-repeat-end style="display: none;"></td>
    </tr>
    <tr ng-repeat-end style="display: none;"></tr>
    </tbody>
</table>
