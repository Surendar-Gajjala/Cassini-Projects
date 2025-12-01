<style scoped>
    .heading {
        margin-bottom: 5px !important;
        margin-top: 5px !important;
    }

    .panel-heading {
        height: 40px !important;
    }

    a.disabled {
        cursor: not-allowed;
        color: lightgrey;
    }

    .col-md-4 {
        padding-left: 0px;
    }
</style>
<div class="panel panel-default panel-alt widget-messaging" style="padding: 20px 5px 10px;">
    <div class="panel-heading">
        <div class="col-md-12">
            <div class="col-md-4">
                <h4 class="heading">Latest Returns</h4>
            </div>
            <div class="col-md-8">
                <div class="pull-right text-center" style="margin-top: 5px !important;">
                    <div>
                        <span>
                            <medium>
                                <span style="margin-right: 10px;">
                                        Displaying {{returnWidgetVm.returnItems.numberOfElements}} of
                                            {{returnWidgetVm.returnItems.totalElements}}
                                    </span>
                            </medium>
                        </span>
                        <span class="mr10">Page {{returnWidgetVm.returnItems.totalElements != 0 ? returnWidgetVm.returnItems.number+1:0}} of {{returnWidgetVm.returnItems.totalPages}}</span>
                        <a href="" ng-click="returnWidgetVm.previousPage()"
                           ng-class="{'disabled': returnWidgetVm.returnItems.first}"><i
                                class="fa fa-arrow-circle-left mr10"></i></a>
                        <a href="" ng-click="returnWidgetVm.nextPage()"
                           ng-class="{'disabled': returnWidgetVm.returnItems.last}"><i
                                class="fa fa-arrow-circle-right"></i></a>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <div class="panel-body" style="overflow-x: hidden">
        <div class="widget-panel" style="max-height: 400px; min-height: 400px;overflow-x: auto">
            <table class="table table-striped highlight-row">
                <thead>
                <tr>
                    <th>Nomenclature</th>
                    <th>Type</th>
                    <th>Status</th>
                    <th>UPN</th>
                    <th>Serial Number</th>
                </tr>
                </thead>

                <tbody>
                <tr ng-if="returnWidgetVm.loading == true">
                    <td colspan="7">
                        <span style="font-size: 15px;">
                            <img src="app/assets/bower_components/cassini-platform/images/loaders/loader19.gif"
                                 class="mr5">Loading Return Items...
                        </span>
                    </td>
                </tr>

                <tr ng-if="returnWidgetVm.loading == false && returnWidgetVm.returnItems.content.length == 0">
                    <td colspan="7">No Return Items</td>
                </tr>
                <tr ng-repeat="returnItem in returnWidgetVm.returnItems.content">
                    <td>
                        {{returnItem.item.itemMaster.itemName}} {{returnItem.item.partSpec.specName}}
                    </td>
                    <td>{{returnItem.item.itemMaster.parentType.name}}</td>
                    <td>
                        <item-instance-status object="returnItem"></item-instance-status>
                    </td>
                    <td>
                        <span class="badge badge-danger" style="font-size: 13px;">
                            <a href="" style="color: white !important;" title="Click to history"
                               ng-click="showUpnHistory(returnItem)">{{returnItem.upnNumber}}</a>
                        </span>
                    </td>
                    <td>{{returnItem.oemNumber}}</td>
                </tr>
                </tbody>
            </table>
        </div>
    </div>
</div>