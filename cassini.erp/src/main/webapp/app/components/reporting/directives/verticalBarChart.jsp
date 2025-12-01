<div class="panel panel-default panel-alt widget-messaging" style="height: 500px">
    <div class="panel-heading" style="background-color: #E4E7EA; padding:0; height: 56px">
        <div class="row" style="padding-right: 10px;">
            <div class="panel-title col-sm-8" style="font-size:15px; padding: 20px 0 0 20px">{{chart.title}}</div>
            <div class="col-sm-4" style="padding-top: 8px;">
                <input placeholder="Select date range"
                       style="width: 100%; text-align: left"
                       options="dateRangeOptions"
                       date-range-picker
                       clearable="true"
                       class="form-control input-sm date-picker"
                       type="text" ng-model="filters.orderedDate"/>
            </div>
        </div>
    </div>
    <div class="panel-body">
        <div ng-if="chart.data.length == 0 && loading == false"
             style="position: absolute; top: 50px; bottom: 10px; left: 10px;
         right: 10px;padding-top: 100px;text-align: center;width: 100%;">
            <div class="alert alert-warning" style="margin-left: auto;margin-right: auto;text-align: center;width: 300px;">
                <h4 style="margin-bottom: 0;"><i class="fa fa-warning mr10"></i>There is no data for this report</h4>
            </div>
        </div>
        <div ng-if="loading == true"
             style="position: absolute; top: 50px; bottom: 10px; left: 10px;
         right: 10px;padding-top: 100px;text-align: center;width: 100%;"><h3>Loading...</h3></div>
        <div class="report-view" ng-if="chart.data.length > 0 && loading == false"
             style="position: absolute; top: 50px; bottom: 10px; left: 10px; right: 10px"></div>
    </div>
</div>
