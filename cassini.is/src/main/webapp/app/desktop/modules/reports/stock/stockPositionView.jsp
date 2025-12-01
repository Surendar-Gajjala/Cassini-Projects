<link href="app/assets/css/app/desktop/searchBox.css" rel="stylesheet" type="text/css">
<div class="view-container" fitcontent>
    <div class="view-toolbar" style="margin: 0px;">
        <div class="row" style="margin: 0">
            <button ng-disabled="stockRepVm.maprows.length == 0" class="btn btn-sm btn-default min-width"
                    ng-click="stockRepVm.exportReport()">Export
            </button>
            <div class="search-date1 inner-addon right-addon">
                <input style="border:1px solid #ddd;background: #fdfdfd;" type="text"
                       class="form-control input-sm search-form"
                       date-picker
                       ng-model="stockRepVm.startDate"
                       name="startDate" placeholder="Start Date">
                <i class="fa fa-calendar"></i>
            </div>
            <div class="search-date2 inner-addon right-addon">
                <input style="border:1px solid #ddd;background: #fdfdfd;" type="text"
                       class="form-control input-sm search-form"
                       date-picker
                       ng-model="stockRepVm.endDate"
                       name="endDate" placeholder="Finish Date">
                <i class="fa fa-calendar"></i>
            </div>
            <button style="margin-right: 10px;" title="Search Records" `
                    class="btn btn-sm btn-default min-width pull-right"
                    ng-click="stockRepVm.searchReport()">Search<i style="margin-left: 10px" class="fa fa-search"></i>
            </button>
        </div>
    </div>

    <div class="view-content no-padding" style="overflow-y: auto;">
        <div class="responsive-table" style="padding: 10px;">
            <table class="table table-striped highlight-row">
                <thead>
                <tr>
                    <th>Item No.</th>
                    <th>Item</th>
                    <th>Unit</th>
                    <th ng-repeat="header in stockRepVm.headers track by $index">{{header}}</th>
                    <th>Total Qty</th>
                </tr>
                </thead>
                <tbody ng-repeat="(key, value) in stockRepVm.maprows track by $index">
                <tr>
                    <td style="font-weight: bold">{{$index+1}}</td>
                    <td colspan="9" style="font-weight: bold">{{key | uppercase}}</td>
                </tr>
                <tr ng-if="value.length == 0">
                    <td colspan="11" style="text-align: center">
                        <span style="font-size: 15px;">
                            No Items found.
                        </span>
                    </td>
                </tr>
                <tr ng-if="value.length > 0" ng-repeat="val in value track by $index">
                    <td style="text-align: left">{{val.itemNumber}}</td>
                    <td class="titleData">{{val.item}}</td>
                    <td>{{val.unit}}</td>
                    <td style="text-align: left" ng-repeat="qty in val.storesQty track by $index">{{qty+""}}</td>
                    <td style="text-align: left">{{val.totalQty}}</td>
                </tr>
                </tbody>
            </table>
        </div>
    </div>
</div>