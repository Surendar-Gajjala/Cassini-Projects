<div class="panel panel-default panel-alt widget-messaging" style="height: 500px">
    <div class="panel-heading" style="background-color: #E4E7EA; padding:0; height: 56px">
        <div class="row" style="padding-right: 10px;">
            <div class="panel-title col-sm-4" style="font-size:15px; padding: 20px 0 0 20px">Book Sales</div>
            <div class="col-sm-4" style="padding-top: 8px;">
                <input placeholder="Select date range"
                       style="width: 100%; text-align: left"
                       options="dateRangeOptions"
                       date-range-picker
                       clearable="true"
                       class="form-control input-sm date-picker"
                       type="text" ng-model="filters.orderedDate"/>
            </div>
            <div class="col-sm-4" style="padding-top: 8px;">
                <input type="text" class="form-control input-sm" placeholder="Filter" ng-model="filterText">
            </div>
        </div>
    </div>
    <div class="panel-body">
        <treasure-overlay-spinner active='spinner.active == true'>
            <div class='responsive-table' style="min-height: 440px; max-height: 440px; overflow: auto;">
                <table class='table table-striped'>
                    <thead>
                    <tr>
                        <th style="cursor: pointer" ng-click="order('book')">
                            Book
                            <i ng-if="predicate == 'book'" class="fa" ng-class="{'fa-sort-desc': reverse, 'fa-sort-asc': !reverse}"></i>
                        </th>
                        <th style="cursor: pointer" ng-click="order('product')">
                            Product Sales
                            <i ng-if="predicate == 'product'" class="fa" ng-class="{'fa-sort-desc': reverse, 'fa-sort-asc': !reverse}"></i>
                        </th>
                        <th style="cursor: pointer" ng-click="order('sample')">
                            Sample Sales
                            <i ng-if="predicate == 'sample'" class="fa" ng-class="{'fa-sort-desc': reverse, 'fa-sort-asc': !reverse}"></i>
                        </th>
                        <th class="text-center" style="cursor: pointer" ng-click="order('percent')">
                            Percent
                            <i ng-if="predicate == 'percent'" class="fa" ng-class="{'fa-sort-desc': reverse, 'fa-sort-asc': !reverse}"></i>
                        </th>
                    </tr>
                    </thead>
                    <tbody>
                    <tr ng-repeat="row in reportData  | filter:filterText">
                        <td>{{row.book}}</td>
                        <td>{{row.product}}</td>
                        <td>{{row.sample}}</td>
                        <td class="text-center">
                            <span class="label" ng-class="{'label-danger': row.percent > 10, 'label-success':row.percent<=10}">{{row.percent}}%</span>
                        </td>
                    </tr>
                    <tr ng-if="spinner.active == false">
                        <td class="text-right"><strong>TOTAL:</strong></td>
                        <td><strong>{{aggregate.product}}</strong></td>
                        <td><strong>{{aggregate.sample}}</strong></td>
                        <td class="text-center">
                            <strong><span class="label" ng-class="{'label-danger': aggregate.percent > 10, 'label-success':aggregate.percent<=10}">{{aggregate.percent}}%</span></strong>
                        </td>
                    </tr>
                    </tbody>
                </table>
            </div>
        </treasure-overlay-spinner>
    </div>
</div>