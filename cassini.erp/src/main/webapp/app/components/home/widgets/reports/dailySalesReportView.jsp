<div class="panel panel-default panel-alt widget-messaging" style="height: 500px">
    <div class="panel-heading" style="background-color: #E4E7EA; padding:0; height: 56px">
        <div class="row" style="padding-right: 10px;">
            <div class="panel-title col-sm-8" style="font-size:15px; padding: 20px 0 0 20px">Daily Sales Report</div>
            <div class="col-sm-4" style="text-align:right; padding-top: 8px;">
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
        <treasure-overlay-spinner active='spinner.active == true'>
            <div id="dailySalesReport"  style="min-height: 440px; max-height: 440px; overflow: hidden;">

            </div>
        </treasure-overlay-spinner>
    </div>
</div>